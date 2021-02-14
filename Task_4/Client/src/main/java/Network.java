import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Network {

    private static final int PORT = 8189; // порт висит на классе сеть

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

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
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.err.println("Problem with server on port: 8189");
        }
    }

    public void writeMessage(AbstractMessage message) throws IOException { // метод для отправки написанных сообщений
        out.writeObject(message); //
        out.flush(); //
    }

    public AbstractMessage readMessage() throws IOException, ClassNotFoundException { // метод для чтения полученых сообщений
        return (AbstractMessage) in.readObject();
    }

    public void close() throws IOException { // метод для закрытия клиента
        out.close(); // исходящий поток обрубить
        in.close(); // входящий поток обрубить
        socket.close(); // обрубить канал
    }

}