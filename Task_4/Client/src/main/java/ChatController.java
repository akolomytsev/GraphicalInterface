import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.sun.xml.internal.ws.server.ServerRtException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController implements Initializable {


    public TextField input;
    public ListView<String> listView; // сообщения строкой приходят
    public ListView<String> userList;
    private Network network;
    private String nick;

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        String to = userList.getSelectionModel().getSelectedItem();
        if (to != null) {
            network.writeMessage(TextMessage.of(nick, to, input.getText()));
        } else {
            network.writeMessage(TextMessage.of(nick, input.getText()));
        }
        userList.getSelectionModel().clearSelection();
        input.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        network = Network.getInstance();


        new Thread(() -> {
            try {
                network.writeMessage(new NickRequest());
                    while (true) {
                    AbstractMessage message = network.readMessage();
                    if (message instanceof NickResponse){
                        nick = ((NickResponse) message).getNick();
                    }
                    if (message instanceof UserListMessage){
                        Platform.runLater(() -> {
                            userList.getItems().clear();
                            userList.getItems().addAll(((UserListMessage) message).getNames());
                        });
                    }
                    if (message instanceof QuitRequest) { // ели получаю /quit то выхожу из потока
                        network.close(); // порвется сеть
                        break;
                    }
                    if (message instanceof TextMessage){
                        TextMessage msg = (TextMessage) message;
                        String out = msg.getSendAt() + " " + msg.getFrom() + ": " + msg.getMessage();
                        Platform.runLater(() -> listView.getItems().add(out));
                    }
                }
            } catch (IOException ioException) {
                System.err.println("Server was broken");
                Platform.runLater(() -> listView.getItems().add("Server was broken")); //  при остановке сервера выводит это сообщение в окне клиента
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void quit(ActionEvent actionEvent) throws IOException {
        network.writeMessage(new QuitRequest());
    }
}