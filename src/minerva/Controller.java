package minerva;

import ModifiedHTMLEditor.ExtendedHTMLEditor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button myButton;

    @FXML
    private ListView noteListScrollPane;

    @FXML
    private ToggleButton boldButton;

    @FXML
    private ExtendedHTMLEditor notesArea;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert myButton != null : "fx:id=\"myButton\" was not injected: check your FXML file 'minerva.fxml'.";
        assert noteListScrollPane != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert boldButton != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert notesArea != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        myButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("CS102 - All Stars");
            }
        });

        boldButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Bold button event fired!");
                notesArea.boldFire();
            }
        });



        ObservableList<String> items = FXCollections.observableArrayList("Welcome!", "Don't Forget!", "Club Events", "Bug List");
        noteListScrollPane.setItems(items);
    }

    public Controller()
    {

    }
}
