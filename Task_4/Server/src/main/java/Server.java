import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class Server {

    private static final int DEFAULT_PORT = 8189; // порт на котором открывается наш сервер

    private ConcurrentLinkedDeque<ClientHandler> clients; // можно писать со скольки угодно клиентов, уже синхронизированна

    public Server(int port) { // здесь вся логика
        clients = new ConcurrentLinkedDeque<>();
        try (ServerSocket server = new ServerSocket(port)){ // обрабатываем на том порту который передали из конструктора
            System.out.println("[DEBUG] server started on port: " + port);
            while (true) {
                Socket socket = server.accept(); // get connection точка подключения со строны сервера (информация - кто подключился с какого IP, port)
                socket.setSoTimeout(120000);
                System.out.println("[DEBUG] client accepted"); // клиент подключен
                ClientHandler handler = new ClientHandler(socket, this); // список клиентов
                addClient(handler); // добавляем нового клиента
                new Thread(handler).start(); // запускаем нового клиента
               }
        } catch (Exception e){ // обрабатываем исключения
            System.err.println("Server Was broken"); // все ошибки выводят через err

        }
    }
    public List<String> getUserNickNames(){
        return clients.stream()
                .map(ClientHandler::getNickName)
                .collect(Collectors.toList());
    }

    public void  addClient(ClientHandler clientHandler){
        clients.add(clientHandler); // клиент добавлен
        System.out.println("[DEBUG] client added to broadcast queue");
    }

    public void removeClient(ClientHandler clientHandler){
        clients.remove(clientHandler); // клиент убран
        System.out.println("[DEBUG] client removed from broadcast queue");
    }

    public void broadCastMessage(AbstractMessage msg) throws IOException { //
        for (ClientHandler client : clients) {
            client.sendMessage(msg); // рассылает сообщения по всем клиентам полученное от клиента
        }
    }

    public void sendMessageTo(TextMessage message) throws IOException {
        for (ClientHandler client : clients) {
            if (client.getNickName().equals(message.getTo())||
                    client.getNickName().equals(message.getFrom())){
                client.sendMessage(message);
            }
        }
    }

    public static void main(String[] args) {
        int port = -1;
        if (args != null && args.length == 1){
            port = Integer.parseInt(args[0]); // достаем порт
        }
        if (port == -1){
            port = DEFAULT_PORT; // или все норм
        }
        new Server(port);
    }


}
