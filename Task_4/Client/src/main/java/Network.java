import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {

    private static final int PORT = 8189; // порт висит на классе сеть

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private static Network instance; //

    public static Network getInstance() {
        if (instance == null) {
            instance = new Network();
        }
        return instance;
    }

    private Network() {
        try {
            socket = new Socket("localhost", PORT);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.err.println("Problem with server on port: 8189");
        }
    }

    public void writeMessage(String message) throws IOException { // метод для отправки написанных сообщений
        out.writeUTF(message); //
        out.flush(); //
    }

    public String readMessage() throws IOException { // метод для чтения полученых сообщений
        return in.readUTF();
    }

    public void close() throws IOException { // метод для закрытия клиента
        out.close(); // исходящий поток обрубить
        in.close(); // входящий поток обрубить
        socket.close(); // обрубить канал
    }
}