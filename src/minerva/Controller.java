package minerva;

import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.WebPage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ToggleButton boldToggleButton;
    @FXML
    private ListView noteListScrollPane;
    @FXML
    private WebView editor;
    @FXML
    private TextField noteNameTextField;
    @FXML
    private Button trashButton;
    @FXML
    private Button addNoteButton, deleteNoteButton;

    private static Note currentNote;
    private ObservableList<String> noteListScrollPaneItems;
    private WebPage webPage;

    //TODO
    //Just for performance optimization : store opened notes in an ArrayList so you don't need to create Note objects again when the same
    // note is clicked again. Scheduled for future releases...

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {
        assert boldToggleButton != null : "fx:id=\"myButton\" was not injected: check your FXML file 'minerva.fxml'.";
        assert noteListScrollPane != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert editor != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert noteNameTextField != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert trashButton != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert deleteNoteButton != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected

        /*** *** *** *** *** BEGINNING OF Button Listeners *** *** *** *** ***/
        boldToggleButton.setOnAction(event -> {
            System.out.println("CS102 - All Stars");
            Note note = new Note(noteListScrollPane.getSelectionModel().getSelectedItem().toString(), getWebViewContent());
            DataManager.saveNote(note);

            webPage = Accessor.getPageFor(editor.getEngine());
            webPage.executeCommand("bold" ,boldToggleButton.selectedProperty().getValue().toString());
        });

        trashButton.setOnAction(event -> {
            System.out.println(trashButton);
        });

        noteListScrollPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (currentNote != null)
                {
                    currentNote.setHtmlNote(getWebViewContent());
                    DataManager.saveNote(currentNote);
                }

                if ((currentNote = DataManager.getNote(newValue)) == null)
                    currentNote = new Note(Defaults.newNoteName, Defaults.newNotePage);

                editor.getEngine().loadContent(currentNote.getHtmlNote());
                //webPage.load(webPage.getMainFrame(), currentNote.getHtmlNote(), "text/html");
                noteNameTextField.setText(newValue);
            }
        });

        addNoteButton.setOnAction(event -> {
            noteListScrollPaneItems.add(Defaults.newNoteName);
            noteListScrollPane.getSelectionModel().select(noteListScrollPane.getItems().size() - 1);
        });

        deleteNoteButton.setOnAction(event -> {
            noteListScrollPaneItems.remove(Defaults.newNoteName);
            noteListScrollPane.getSelectionModel().select(noteListScrollPane.getItems().size() - 1);
        });

        /*** *** *** *** *** END OF Button Listeners *** *** *** *** ***/
        noteListScrollPaneItems = FXCollections.observableArrayList(DataManager.getNoteNames());
        noteListScrollPane.setItems(noteListScrollPaneItems);
        noteListScrollPane.getSelectionModel().select(0);
    }

    public static void onExit()
    {
        if(currentNote != null)
            DataManager.saveNote(currentNote);
    }

    private String getWebViewContent()
    {
        return (String)editor.getEngine().executeScript("document.documentElement.outerHTML");
    }
}
