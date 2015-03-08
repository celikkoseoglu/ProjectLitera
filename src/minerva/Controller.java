package minerva;

import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.WebPage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // FXML Panes
    @FXML
    private GridPane footer;
    @FXML
    private GridPane rightPane;

    // FXML StyleToolbar Declaration
    @FXML
    private ToolBar styleToolBar;
    @FXML
    private ToggleButton bold;
    @FXML
    private ToggleButton italic;
    @FXML
    private ToggleButton underline;
    @FXML
    private ToggleButton strikethrough;
    @FXML
    private ToggleButton insertOrderedList;
    @FXML
    private ToggleButton insertUnorderedList;

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
        // Panes
        assert footer != null : "fx:id\"footer\" was not injected: check you FXML file 'minerva.fxml'.";
        assert rightPane != null : "fx:id\"rightPane\" was not injected: check you FXML file 'minerva.fxml'.";

        // Style Buttons
        assert styleToolBar != null : "fx:id\"styleToolBar\" was not injected: check you FXML file 'minerva.fxml'.";
        assert bold != null : "fx:id=\"bold\" was not injected: check your FXML file 'minerva.fxml'.";
        assert italic != null : "fx:id=\"italic\" was not injected: check your FXML file 'minerva.fxml'.";
        assert underline != null : "fx:id=\"underline\" was not injected: check your FXML file 'minerva.fxml'.";
        assert strikethrough != null : "fx:id=\"strikethrough\" was not injected: check your FXML file 'minerva.fxml'.";
        assert insertOrderedList != null : "fx:id=\"insertOrderedList\" was not injected: check your FXML file 'minerva.fxml'.";
        assert insertUnorderedList != null : "fx:id=\"insertUnorderedList\" was not injected: check your FXML file 'minerva.fxml'.";

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
        bold.setOnAction(event -> addStyle( bold.getId()));
        italic.setOnAction(event -> addStyle( italic.getId()));
        underline.setOnAction(event -> addStyle( underline.getId()));
        strikethrough.setOnAction(event -> addStyle( strikethrough.getId()));
        insertOrderedList.setOnAction(event -> addStyle( insertOrderedList.getId()));
        insertUnorderedList.setOnAction(event -> addStyle( insertUnorderedList.getId()));

        // Listener for  Style buttons
        editor.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> buttonFeedback());
        editor.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> buttonFeedback());
        editor.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, event -> buttonFeedback());
        editor.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> buttonFeedback());

        // Button Listeners for Bottom Toolbar
        trashButton.setOnAction(event -> {
            System.out.println(trashButton);
        });

        addNoteButton.setOnAction(event -> {
            noteListScrollPaneItems.add(Defaults.newNoteName);
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
                if (currentNote != null)
                {
                    currentNote.setHtmlNote(getWebViewContent());
                    DataManager.saveNote(currentNote);
                }

                if ((currentNote = DataManager.getNote(newValue)) == null) {
                    currentNote = new Note(Defaults.newNoteName, Defaults.newNotePage);
                }
                editor.getEngine().loadContent(currentNote.getHtmlNote());
                noteNameTextField.setText(newValue);
            }

        });
        /*** *** *** *** *** END OF Button Listeners *** *** *** *** ***/

        // Start up of program
        noteListScrollPaneItems = FXCollections.observableArrayList(DataManager.getNoteNames());
        noteListScrollPane.setItems(noteListScrollPaneItems);
        noteListScrollPane.getSelectionModel().select(0);

        // Make transparent everything beside the BorderPane
        editor.setBlendMode(BlendMode.MULTIPLY);
        footer.setBlendMode(BlendMode.MULTIPLY);
        noteNameTextField.setBlendMode(BlendMode.MULTIPLY);
        rightPane.setBlendMode(BlendMode.MULTIPLY);
        noteListScrollPane.setBlendMode(BlendMode.MULTIPLY);

    }

    // Saving on exit
    public static void onExit()
    {
        if(currentNote != null)
            DataManager.saveNote(currentNote);
    }

    // Returns the string version of the page
    private String getWebViewContent()
    {
        return (String)editor.getEngine().executeScript("document.documentElement.outerHTML");
    }

    // Style method for all things need styling
    private void addStyle( String command){
        webPage.executeCommand( command, "false");
        editor.requestFocus();
        buttonFeedback();
    }

    // Button Feedback is aimed to show buttons natural :d
    private void buttonFeedback(){
        // For Style Toolbar
        ToggleButton button;
        for( Node node : styleToolBar.getItems()){
            button = (ToggleButton)node;
            button.setDisable(!webPage.queryCommandEnabled( button.getId()));
            button.setSelected(webPage.queryCommandState( button.getId()));
        }
    }

}
