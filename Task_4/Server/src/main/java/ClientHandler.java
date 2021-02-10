import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;
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



    @Override
    public void run() {
        try {
            out =new DataOutputStream(socket.getOutputStream()); // выходной поток данных клиента
            in = new DataInputStream(socket.getInputStream()); // входной поток данных клиента
            System.out.println("[DEBUG] client start processing: " + nickName);
            while (running) {
                String msg = in.readUTF(); // обробатываем полученное сообщение
                if (msg.equals("/quit")) { // если /quit то обробатываем выход
                    out.writeUTF(msg); // не совсем понимаю как от сюда уходим в Exception e, ведь это просто выходной поток данных в байтах
                }
                //String[] words = msg.split(" ");
                if (msg.startsWith("/w")) {
                    String[] words = msg.split(" ");
                    String nickoftheReciver = words[1];
                    String message = msg.substring(3);
                    server.sendMessageTo(nickName + " for " + message,  nickoftheReciver); //
                } else {
                    server.broadCastMessage(nickName + ": " + msg); // в остальных случаях обрабатываем  от кого получили сообщение и само сообщение (рассылем по всем)
                }
                System.out.println("[DEBUG] message from client " + nickName +": "+ msg);
            }
        }catch (Exception e){
            System.err.println("Handled connection was broken" + " " +nickName); // выводим когда один из клиентов отключился
            server.removeClient(this); // обработка отключения клиента
        }
    }

    public void sendMessage(String message) throws IOException {
            out.writeUTF(message);
            out.flush();
    }

    public void sendPersonalMessage (String nickName, String message) throws IOException {
        out.writeUTF(message);
        out.writeUTF(nickName);
        out.flush();
    }
    public String getNickName(){
        return nickName;
    }

}


