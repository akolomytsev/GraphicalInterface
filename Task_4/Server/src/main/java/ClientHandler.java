import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private Server server;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running;
    private String nickName;
    private static int cnt = 0;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        running = true;
        cnt++;
        nickName = "user" + cnt;
    }

    public String getNickName(){
        return nickName;
    }

    @Override
    public void run() {
        try {
            out =new ObjectOutputStream(socket.getOutputStream()); // выходной поток данных клиента
            in = new ObjectInputStream(socket.getInputStream()); // входной поток данных клиента
            System.out.println("[DEBUG] client start processing: " + nickName);
            server.broadCastMessage(UserListMessage.of(server.getUserNickNames()));
            while (running) {
                AbstractMessage msg = (AbstractMessage) in.readObject();
                if (msg instanceof TextMessage){  // обробатываем полученное сообщение|
                    TextMessage message = (TextMessage) msg;
                    if (message.getTo() != null) {
                        server.sendMessageTo(message);
                    }
                    /*if (message.getTo() == null){
                        Thread.sleep (12000);
                        out.writeObject(msg);
                        out.flush();
                    }*/
                    else {
                        server.broadCastMessage(message);
                    }

                }
                if (msg instanceof  NickRequest){
                    out.writeObject(new NickResponse(nickName));
                    out.flush();
                }
                if (msg instanceof QuitRequest)
                    out.writeObject(msg);
                    out.flush();
                System.out.println("[DEBUG] message from client " + nickName +": "+ msg);
            }


        }catch (Exception e){
            System.err.println("Handled connection was broken" + " " +nickName); // выводим когда один из клиентов отключился
            server.removeClient(this); // обработка отключения клиента
            try {
               server.broadCastMessage(UserListMessage.of(server.getUserNickNames()));
            } catch (IOException ignored) {

            }
        }
    }

    public void sendMessage(AbstractMessage message) throws IOException {
            out.writeObject(message);
            out.flush();
    }

    public void sendPersonalMessage (String nickName, String message) throws IOException {
        out.writeUTF(message);
        out.writeUTF(nickName);
        out.flush();
    }


}


