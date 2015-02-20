package minerva;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button myButton;

    @FXML
    private ListView noteListScrollPane;

    @FXML
    private ToolBar toolbar;

    @FXML
    private WebView editor;

    @FXML
    private TextField noteNameTextField;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {
        assert myButton != null : "fx:id=\"myButton\" was not injected: check your FXML file 'minerva.fxml'.";
        assert noteListScrollPane != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert toolbar != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert editor != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert noteNameTextField != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected

        myButton.setOnAction(event -> {
            System.out.println("CS102 - All Stars");
            Note note = new Note(noteListScrollPane.getSelectionModel().getSelectedItem().toString(), (String) editor.getEngine().executeScript("document.documentElement.outerHTML"));
            DataManager.saveNote(note);
        });

        ObservableList<String> items = FXCollections.observableArrayList(DataManager.getNoteNames());
        noteListScrollPane.setItems(items);

        noteListScrollPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                editor.getEngine().loadContent(DataManager.getNote(newValue).getHtmlNote());
                noteNameTextField.setText(newValue);
            }
        });
    }

    public Controller()
    {
    }
}
