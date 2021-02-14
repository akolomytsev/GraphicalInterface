import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Network network = Network.getInstance();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(request -> { // это все реакция на кнопку закрыть окно чата
        try {
            network.writeMessage(new QuitRequest()); // команда для выхода из чата
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    });
    }
}
