package minerva;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.StackPane;
import javafx.scene.web.HTMLEditor;
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
    private StackPane notesStackPane;

    @FXML
    private ToggleButton editToggleButton;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert myButton != null : "fx:id=\"myButton\" was not injected: check your FXML file 'minerva.fxml'.";
        assert noteListScrollPane != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert toolbar != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert notesStackPane != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert editToggleButton != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        myButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    System.out.println("CS102 - All Stars");
            }
        });

        ObservableList<String> items = FXCollections.observableArrayList("Welcome!", "Don't Forget!", "Club Events", "Bug List","Welcome!", "Don't Forget!", "Club Events", "Bug List","Welcome!", "Don't Forget!", "Club Events", "Bug List","Welcome!", "Don't Forget!", "Club Events", "Bug List","Welcome!", "Don't Forget!", "Club Events", "Bug List","Welcome!", "Don't Forget!", "Club Events", "Bug List","Welcome!", "Don't Forget!", "Club Events", "Bug List","Welcome!", "Don't Forget!", "Club Events", "Bug List");
        noteListScrollPane.setItems(items);

        final HTMLEditor editor = new HTMLEditor();
        editor.setHtmlText("<h1></h1><i>Four score and twenty years ago . . .</i><br/><img src='http://cdn.marketplaceimages.windowsphone.com/v8/images/b43708b9-1993-4f36-90a5-5dd3b2f2b60b?imageType=ws_icon_large'/>");
        final WebView view = new WebView();

        // create a stack which swaps back an forth between the html editor and view
        // depending on the state of the editToggleButton.
        editToggleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
                notesStackPane.getChildren().clear();

                if (editToggleButton.isSelected())
                {
                    notesStackPane.getChildren().addAll(view, editor);
                }
                else
                {
                    view.getEngine().loadContent(editor.getHtmlText());
                    notesStackPane.getChildren().addAll(editor, view);
                }
            }
        });

        // set the initial state of the scene to webView.
        editToggleButton.fire();
        editToggleButton.fire();
    }

    public Controller()
    {
    }
}
