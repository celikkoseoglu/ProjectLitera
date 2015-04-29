package litera.MainFrame;

import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.WebPage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import litera.Data.LocalDataManager;
import litera.Defaults.Defaults;
import litera.Multimedia.PlayerController;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

/**
 * UI Controller for the Main Frame
 *
 * @author Çelik Köseoğlu - all except below
 * @author Caner Çalışkaner - buttonFeedback method and styling toolbar listeners
 * @author Orhun Çağlayan - rename algorithm
 */

public class Controller implements Initializable
{
    private static Note currentNote;
    private static WebPage webPage;
    private ObservableList<String> noteListScrollPaneItems;
    private boolean isNoteChanged;

    @FXML
    private ToggleButton boldToggleButton, italicToggleButton, underlineToggleButton, strikethroughToggleButton, insertOrderedListToggleButton;
    @FXML
    private Button addAudioButton, addVideoButton, addImageButton;
    @FXML
    private Button addNoteButton, deleteNoteButton, trashButton, optionsButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ListView noteListView, trashNoteListView;
    @FXML
    private TextField noteNameTextField;
    @FXML
    private ColorPicker foregroundColorPicker, notePadColorPicker;
    @FXML
    private WebView editor;
    @FXML
    private ContextMenu trashContextMenu;
    @FXML
    private MenuItem recoverMenuItem, deleteMenuItem;
    @FXML
    private ToolBar optionsToolbar;

    //Stage closing event calls this function to save the last note so Litera can start with the latest edited note next time.
    public static void onExit()
    {
        currentNote.setHtmlNote(webPage.getHtml(webPage.getMainFrame()));
        LocalDataManager.saveLastNote(currentNote);
    }

    public static void addAudio(/*File file*/)
    {
        webPage.executeScript(webPage.getMainFrame(), "document.write('<button contentEditable=\"false\" id=\"audio\" onclick=\"alert(this.id)\">SesVer</button>')");
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {
        webPage = Accessor.getPageFor(editor.getEngine()); //webPage is the controller for the webView for executing scripts etc.
        isNoteChanged = false;

        // Button Listeners for Style
        boldToggleButton.setOnAction(event -> addStyle(Defaults.BOLD_COMMAND, null));
        italicToggleButton.setOnAction(event -> addStyle(Defaults.ITALIC_COMMAND, null));
        underlineToggleButton.setOnAction(event -> addStyle(Defaults.UNDERLINE_COMMAND, null));
        strikethroughToggleButton.setOnAction(event -> addStyle(Defaults.STRIKETHROUGH_COMMAND, null));
        insertOrderedListToggleButton.setOnAction(event -> addStyle(Defaults.NUMBERS_COMMAND, null));

        // Listeners for Style buttons
        editor.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> buttonFeedback());
        editor.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> buttonFeedback());
        editor.addEventHandler(KeyEvent.KEY_RELEASED, event -> buttonFeedback());
        editor.addEventHandler(KeyEvent.KEY_PRESSED, event -> buttonFeedback());

        trashButton.setOnAction(event -> {
            trashNoteListView.setItems(FXCollections.observableArrayList(LocalDataManager.getNoteNames(LocalDataManager.getLocalTrashFilePath())));
            trashContextMenu.show(trashButton, Side.RIGHT, -20, -20); //show the trash menu on left click at the correct location
        });

        recoverMenuItem.setOnAction(event -> {
            LocalDataManager.moveNotes(trashNoteListView.getSelectionModel().getSelectedItems(), false);
            populateNoteListbox(); //load recovered notes back while keeping the alphabetical order
        });

        deleteMenuItem.setOnAction(event -> LocalDataManager.deleteNote(trashNoteListView.getSelectionModel().getSelectedItems()));

        optionsButton.setOnAction(event -> loadWindow("/litera/Options/options.fxml", "Litera Options"));

        addAudioButton.setOnAction(event -> {
            loadWindow("/litera/Multimedia/audio.fxml", "Litera Recorder");
        });

        addVideoButton.setOnAction(event -> {
            try
            {
                try
                {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Choose Video");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP4 Files", "*.mp4", "*.m4v"));
                    File selectedFile = fileChooser.showOpenDialog(addVideoButton.getScene().getWindow());
                    System.out.println(Paths.get(selectedFile.toURI()));
                    System.out.println(Paths.get(LocalDataManager.getLocalNotesFilePath() + currentNote.getNoteName() + "/"));
                    Files.copy(Paths.get(selectedFile.toURI()), new File(LocalDataManager.getLocalNotesFilePath() + currentNote.getNoteName() + "\\" + selectedFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);

                    PlayerController a = new PlayerController(selectedFile);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Multimedia/player.fxml"));
                    fxmlLoader.setController(a);
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Litera Player");
                    stage.setScene(new Scene(root));
                    stage.show();
                    stage.setOnCloseRequest(new EventHandler<WindowEvent>()
                    {
                        public void handle(WindowEvent we)
                        {
                            System.out.println("Stage is closing");
                            a.disposeThis();

                        }
                    });


                }
                catch ( Exception ex )
                {
                    System.out.println(ex.toString());
                }
            }
            catch ( Exception ex )
            {
                System.out.println("File copy operation fail!");
            }
        });

        addImageButton.setOnAction(event -> {
            loadWindow("/litera/Multimedia/video.fxml", "Litera Player");
        });

        foregroundColorPicker.setOnAction(event -> {
            Color newValue = foregroundColorPicker.getValue();
            if ( newValue != null )
                addStyle(Defaults.FOREGROUND_COLOR_COMMAND, Defaults.colorValueToHex(newValue));
        });

        notePadColorPicker.setOnAction(event -> {
            Color newValue = notePadColorPicker.getValue();
            if ( newValue != null )
            {
                LocalDataManager.saveNoteCSS(currentNote, Defaults.colorValueToHex(newValue));
                loadCSS(currentNote);
            }
        });

        addNoteButton.setOnAction(event -> {
            noteListScrollPaneItems.add(LocalDataManager.createNewNote());
            noteListView.getSelectionModel().select(noteListView.getItems().size() - 1);
        });

        deleteNoteButton.setOnAction(event -> {
            LocalDataManager.moveNotes(noteListView.getSelectionModel().getSelectedItems(), true);
            noteListScrollPaneItems.remove(noteListView.getSelectionModel().getSelectedItem());
            noteListView.getSelectionModel().select(noteListView.getItems().size() - 1);
        });

        noteListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if ( isNoteChanged ) //there is no need for a save operation if you didn't change anything
                {
                    currentNote.setHtmlNote(webPage.getHtml(webPage.getMainFrame()));
                    LocalDataManager.saveNote(currentNote);
                    isNoteChanged = false;
                }
                currentNote = LocalDataManager.getNote(newValue);
                editor.getEngine().loadContent(currentNote.getHtmlNote());
                noteNameTextField.setText(newValue);
                loadCSS(currentNote);
            }
        });

        //Event handler for button clicks on the note
        editor.getEngine().setOnAlert((WebEvent<String> wEvent) -> {
            System.out.println("Alert Event  -  Message:  " + wEvent.getData());
        });

        noteNameTextField.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            //do not try to change the name if textField is empty or the note with the same name exists
            if ( !noteNameTextField.getText().isEmpty() && !noteListScrollPaneItems.contains(noteNameTextField.getText()) )
            {
                LocalDataManager.renameNote(currentNote, noteNameTextField.getText());
                populateNoteListbox(); //need this to retain the alphabetical order
            }
        });
        /*** *** *** *** *** END OF Button Listeners *** *** *** *** ***/

        // Start up of the program
        borderPane.getStyleClass().add("border-pane"); //border-pane has the background-color property
        noteNameTextField.getStyleClass().add("list-view"); //list-view has the border-color property
        optionsToolbar.getStyleClass().add("list-view");
        populateNoteListbox();
        loadLastNote();
    }

    private void loadWindow(String windowPath, String windowTitle)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(windowPath));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(windowTitle);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch ( Exception ex )
        {
            ex.toString();
        }
    }

    /**
     * @param command some commands are located in the Defaults class
     * @param commandComplement Color commands may go here
     * @description adds style to the selected text. It is a low-level function. Not much to say here.
     */
    private void addStyle(String command, String commandComplement)
    {
        webPage.executeCommand(command, commandComplement);
        editor.requestFocus();
        buttonFeedback();
    }

    /**
     * @return true if the operation is a success
     * @description loads saved notes into the list note list view
     */
    private boolean populateNoteListbox()
    {
        String[] noteList = LocalDataManager.getNoteNames(LocalDataManager.getLocalNotesFilePath());
        if ( noteList == null )
        {
            currentNote = new Note(Defaults.welcomeList[0], Defaults.welcomePage);
            LocalDataManager.saveNote(currentNote);
            LocalDataManager.generateAndSaveID(currentNote);
            noteList = LocalDataManager.getNoteNames(LocalDataManager.getLocalNotesFilePath());
        }
        noteListScrollPaneItems = FXCollections.observableArrayList(noteList);
        noteListView.setItems(noteListScrollPaneItems);
        return true;
    }

    /**
     * returns the last note name of
     *
     * @return
     */
    private boolean loadLastNote()
    {
        String lastNoteName = LocalDataManager.getLastNote();
        if ( noteListView.getItems().contains(lastNoteName) )
            noteListView.getSelectionModel().select(lastNoteName);
        else
            noteListView.getSelectionModel().selectFirst();
        return true;
    }

    //changes button states according to the currently edited text. For instance if the text you are working on is Italic, toggles the Italic button.
    private void buttonFeedback()
    {
        isNoteChanged = true;
        boldToggleButton.setSelected(webPage.queryCommandState(Defaults.BOLD_COMMAND));
        italicToggleButton.setSelected(webPage.queryCommandState(Defaults.ITALIC_COMMAND));
        underlineToggleButton.setSelected(webPage.queryCommandState(Defaults.UNDERLINE_COMMAND));
        strikethroughToggleButton.setSelected(webPage.queryCommandState(Defaults.STRIKETHROUGH_COMMAND));
        insertOrderedListToggleButton.setSelected(webPage.queryCommandState(Defaults.NUMBERS_COMMAND));
    }

    private void loadCSS(Note n)
    {
        borderPane.getStylesheets().clear();
        borderPane.getStylesheets().add(LocalDataManager.getNoteCSS(n).replace(" ", "%20"));
    }
}