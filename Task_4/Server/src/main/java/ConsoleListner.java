import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class ConsoleListener {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8189);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());
        Scanner sc = new Scanner(System.in);
        Thread console = new Thread(() -> {
            while (sc.hasNext()) {
                String next = sc.next(); // block
                try {
                    out.writeUTF(next);
                    out.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        console.setDaemon(true);
        console.start();

        try {
            while (true) {
                System.out.println(in.readUTF());
            }
        } catch (Exception e) {
            System.out.println("Broken OK");
        }
    }
}