package minerva;

import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.WebPage;
import com.sun.webkit.dom.KeyboardEventImpl;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ToggleButton boldToggleButton;
    @FXML
    private ToggleButton italicToggleButton;
    @FXML
    private ToggleButton underlineToggleButton;
    @FXML
    private ToggleButton strikeToggleButton;
    @FXML
    private ToggleButton listToggleButton;
    @FXML
    private ToggleButton bulletToggleButton;

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
        // Style Buttons
        assert boldToggleButton != null : "fx:id=\"boldToggleButton\" was not injected: check your FXML file 'minerva.fxml'.";
        assert italicToggleButton != null : "fx:id=\"italicToggleButton\" was not injected: check your FXML file 'minerva.fxml'.";
        assert underlineToggleButton != null : "fx:id=\"underlineToggleButton\" was not injected: check your FXML file 'minerva.fxml'.";
        assert strikeToggleButton != null : "fx:id=\"strikeToggleButton\" was not injected: check your FXML file 'minerva.fxml'.";
        assert listToggleButton != null : "fx:id=\"listToggleButton\" was not injected: check your FXML file 'minerva.fxml'.";
        assert bulletToggleButton != null : "fx:id=\"bulletToggleButton\" was not injected: check your FXML file 'minerva.fxml'.";

        // Editor and Pane
        assert noteListScrollPane != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert editor != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";

        // Add delete pane
        assert noteNameTextField != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert trashButton != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert deleteNoteButton != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected

        webPage = Accessor.getPageFor(editor.getEngine());
        /*** *** *** *** *** BEGINNING OF Button Listeners *** *** *** *** ***/

        boldToggleButton.setOnAction(event -> addStyle(Defaults.BOLD_COMMAND));
        italicToggleButton.setOnAction(event -> addStyle(Defaults.ITALIC_COMMAND));
        underlineToggleButton.setOnAction(event -> addStyle(Defaults.UNDERLINE_COMMAND));
        strikeToggleButton.setOnAction(event -> addStyle(Defaults.STRIKETHROUGH_COMMAND));
        listToggleButton.setOnAction(event -> addStyle(Defaults.NUMBERS_COMMAND));
        bulletToggleButton.setOnAction(event -> addStyle(Defaults.BULLETS_COMMAND));

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

                if ((currentNote = DataManager.getNote(newValue)) == null) {
                    currentNote = new Note(Defaults.newNoteName, Defaults.newNotePage);
                }
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

        // button state checker part
        editor.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> buttonFeedback());
        editor.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> buttonFeedback());
        editor.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, event -> buttonFeedback());
        editor.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> buttonFeedback());


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

    private void addStyle( String command){
        webPage.executeCommand( command, "false");
        editor.requestFocus();
        buttonFeedback();
    }

    private void buttonFeedback(){
        boldToggleButton.setDisable(!webPage.queryCommandEnabled(Defaults.BOLD_COMMAND));
        boldToggleButton.setSelected(webPage.queryCommandState(Defaults.BOLD_COMMAND));

        italicToggleButton.setDisable(!webPage.queryCommandEnabled(Defaults.ITALIC_COMMAND));
        italicToggleButton.setSelected(webPage.queryCommandState(Defaults.ITALIC_COMMAND));

        underlineToggleButton.setDisable(!webPage.queryCommandEnabled(Defaults.UNDERLINE_COMMAND));
        underlineToggleButton.setSelected(webPage.queryCommandState(Defaults.UNDERLINE_COMMAND));

        strikeToggleButton.setDisable(!webPage.queryCommandEnabled(Defaults.STRIKETHROUGH_COMMAND));
        strikeToggleButton.setSelected(webPage.queryCommandState(Defaults.STRIKETHROUGH_COMMAND));

        listToggleButton.setDisable(!webPage.queryCommandEnabled(Defaults.NUMBERS_COMMAND));
        listToggleButton.setSelected(webPage.queryCommandState(Defaults.NUMBERS_COMMAND));

        bulletToggleButton.setDisable(!webPage.queryCommandEnabled(Defaults.BULLETS_COMMAND));
        bulletToggleButton.setSelected(webPage.queryCommandState(Defaults.BULLETS_COMMAND));
    }
}
