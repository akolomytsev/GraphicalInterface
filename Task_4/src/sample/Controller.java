package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Controller {

    public ListView<String> listView;
    public TextField input;





    public void sendMessag(ActionEvent actionEvent) {
        String message = input.getText();
        listView.getItems().add(message);
        input.clear();
    }
}
