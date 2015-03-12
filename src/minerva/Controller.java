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
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import litera.Data.LocalDataManager;
import litera.Defaults.Defaults;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    // FXML StyleToolbar Declaration
    @FXML
    private ToolBar styleToolbar;
    @FXML
    private ToggleButton boldToggleButton;
    @FXML
    private ToggleButton italicToggleButton;
    @FXML
    private ToggleButton underlineToggleButton;
    @FXML
    private ToggleButton strikethroughToggleButton;
    @FXML
    private ToggleButton insertOrderedListToggleButton;
    @FXML
    private ToggleButton insertUnorderedListToggleButton;

    // FXML Rest of the Program Declaration
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

    // Other variables' Declaration
    private static Note currentNote;
    private ObservableList<String> noteListScrollPaneItems;
    private WebPage webPage;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {
        // Style Buttons
        assert styleToolbar != null : "fx:id\"styleToolBar\" was not injected: check you FXML file 'minerva.fxml'.";
        assert boldToggleButton != null : "fx:id=\"bold\" was not injected: check your FXML file 'minerva.fxml'.";
        assert italicToggleButton != null : "fx:id=\"italic\" was not injected: check your FXML file 'minerva.fxml'.";
        assert underlineToggleButton != null : "fx:id=\"underline\" was not injected: check your FXML file 'minerva.fxml'.";
        assert strikethroughToggleButton != null : "fx:id=\"strikethrough\" was not injected: check your FXML file 'minerva.fxml'.";
        assert insertOrderedListToggleButton != null : "fx:id=\"insertOrderedList\" was not injected: check your FXML file 'minerva.fxml'.";
        assert insertUnorderedListToggleButton != null : "fx:id=\"insertUnorderedList\" was not injected: check your FXML file 'minerva.fxml'.";

        // Editor and Pane
        assert noteListScrollPane != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert editor != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";

        // Add delete pane
        assert noteNameTextField != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert trashButton != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";
        assert deleteNoteButton != null : "fx:id=\"noteListScrollPane\" was not injected: check your FXML file 'minerva.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected

        // initializing webPage
        webPage = Accessor.getPageFor(editor.getEngine());

        /*** *** *** *** *** START OF Button Listeners *** *** *** *** ***/
        // Button Listeners for Style
        boldToggleButton.setOnAction(event -> addStyle(Defaults.BOLD_COMMAND));
        italicToggleButton.setOnAction(event -> addStyle(Defaults.ITALIC_COMMAND));
        underlineToggleButton.setOnAction(event -> addStyle(Defaults.UNDERLINE_COMMAND));
        strikethroughToggleButton.setOnAction(event -> addStyle(Defaults.STRIKETHROUGH_COMMAND));
        insertOrderedListToggleButton.setOnAction(event -> addStyle(Defaults.NUMBERS_COMMAND));
        insertUnorderedListToggleButton.setOnAction(event -> addStyle(Defaults.BULLETS_COMMAND));

        // Listener for  Style buttons
        editor.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> buttonFeedback());
        editor.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> buttonFeedback());
        editor.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, event -> buttonFeedback());
        editor.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> buttonFeedback());

        // Button Listeners for Bottom Toolbar
        trashButton.setOnAction(event -> {
            System.out.println("trashButton clicked");
        });

        addNoteButton.setOnAction(event -> {
            noteListScrollPaneItems.add(LocalDataManager.getNewNoteName());
            noteListScrollPane.getSelectionModel().select(noteListScrollPane.getItems().size() - 1);
        });

        deleteNoteButton.setOnAction(event -> {
            noteListScrollPaneItems.remove(Defaults.newNoteName);
            noteListScrollPane.getSelectionModel().select(noteListScrollPane.getItems().size() - 1);
        });

        // Listener for Note list selection
        noteListScrollPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if ( currentNote != null )
                {
                    currentNote.setHtmlNote(getWebViewContent());
                    LocalDataManager.saveNote(currentNote);
                }

                if ( ( currentNote = LocalDataManager.getNote(newValue) ) == null )
                {
                    currentNote = new Note(Defaults.newNoteName, Defaults.newNotePage);
                }
                editor.getEngine().loadContent(currentNote.getHtmlNote());
                noteNameTextField.setText(newValue);
            }

        });
        /*** *** *** *** *** END OF Button Listeners *** *** *** *** ***/

        // Start up of program
        noteListScrollPaneItems = FXCollections.observableArrayList(LocalDataManager.getNoteNames());
        noteListScrollPane.setItems(noteListScrollPaneItems);
        noteListScrollPane.getSelectionModel().select(0);
    }

    // Saving on exit
    public static void onExit()
    {
        if ( currentNote != null )
            LocalDataManager.saveNote(currentNote);
    }

    // Returns the string version of the page
    private String getWebViewContent()
    {
        return (String) editor.getEngine().executeScript("document.documentElement.outerHTML");
    }

    // Style method for all things need styling
    private void addStyle(String command)
    {
        webPage.executeCommand(command, null);
        editor.requestFocus();
        buttonFeedback();
    }

    // Button Feedback is aimed to show buttons natural :d
    private void buttonFeedback()
    {
        boldToggleButton.setSelected(webPage.queryCommandState(Defaults.BOLD_COMMAND));
        italicToggleButton.setSelected(webPage.queryCommandState(Defaults.ITALIC_COMMAND));
        underlineToggleButton.setSelected(webPage.queryCommandState(Defaults.UNDERLINE_COMMAND));
        strikethroughToggleButton.setSelected(webPage.queryCommandState(Defaults.STRIKETHROUGH_COMMAND));
        insertOrderedListToggleButton.setSelected(webPage.queryCommandState(Defaults.NUMBERS_COMMAND));
        insertUnorderedListToggleButton.setSelected(webPage.queryCommandState(Defaults.BULLETS_COMMAND));
    }
}
