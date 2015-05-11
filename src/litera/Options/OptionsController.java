package litera.Options;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import litera.Data.LocalDataManager;
import litera.Data.ServerManager;
import litera.Defaults.Defaults;
import litera.MainFrame.Note;

import java.net.URL;
import java.util.ResourceBundle;

public class OptionsController implements Initializable
{
    @FXML
    private Button exportAllNotesButton, importNotesButton, encryptAllNotesButton, decryptAllNotesButton, signUpButton, uploadButton, downloadButton, signInButton, refreshButton;
    @FXML
    private TextField usernameTextField, emailTextField;
    @FXML
    private PasswordField passwordBox, verifyPasswordBox;

    private ServerManager serverManager;
    private ListView noteListView;
    private Note currentNote;
    private ObservableList noteListScrollPaneItems;

    public OptionsController(ListView noteListView, Note currentNote, ObservableList noteListScrollPaneItems)
    {
        this.noteListView = noteListView;
        this.currentNote = currentNote;
        this.noteListScrollPaneItems = noteListScrollPaneItems;
    }

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {
        signUpButton.setOnAction(event -> {
            serverManager = new ServerManager(null, null);
            int createUserStatus = serverManager.createNewUser(usernameTextField.getText(), passwordBox.getText(), emailTextField.getText());

            if (createUserStatus == 0)
            {
                System.out.println("ok");
                serverManager.setUser(usernameTextField.getText(), passwordBox.getText());
            }
            else if (createUserStatus == 1)
                System.out.println("exists");
            else
                System.out.println("Connection err");
        });

        signInButton.setOnAction(event -> {
            serverManager = new ServerManager(usernameTextField.getText(), passwordBox.getText());
        });

        uploadButton.setOnAction(event -> {
            serverManager.uploadAll();
        });

        downloadButton.setOnAction(event -> {

            Task<Void> task = new Task<Void>()
            {
                @Override
                public Void call() throws InterruptedException
                {
                    serverManager.downloadDifferent();
                    return null;
                }
            };
            Thread downloadNotes = new Thread(task);
            downloadNotes.setDaemon(true);
            downloadNotes.start();
        });

        refreshButton.setOnAction(event -> {
            populateNoteListbox();
        });
    }

    /**
     * @return true if the operation is a success
     * @description loads saved notes into the list note list view
     */
    private boolean populateNoteListbox()
    {
        String[] noteList = LocalDataManager.getNoteNames(LocalDataManager.getLocalNotesFilePath());
        if (noteList == null)
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
}