package litera.Multimedia;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import litera.Data.LocalDataManager;
import litera.MainFrame.Controller;
import litera.MainFrame.Note;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.ResourceBundle;

public class AudioController implements Initializable
{
    @FXML
    Button rec;
    @FXML
    Button play;
    @FXML
    Button stop;
    @FXML
    Button choose;
    @FXML
    Button ok;
    @FXML
    Pane audioPane;
    @FXML
    private Label statusLabel;

    private File file;
    private Audio record;
    private String filePath, fileName;
    private Note currentNote;
    private String localTime;

    public AudioController(Note currentNote)
    {
        this.currentNote = currentNote;
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {
        rec.setDisable(false);
        play.setDisable(true);
        stop.setDisable(true);
        choose.setDisable(false);
        ok.setDisable(false);

        rec.setOnAction(event -> {
            //change path in every record not to override audio files
            localTime = java.time.LocalDateTime.now().toString();
            fileName = localTime + ".wav";
            filePath = LocalDataManager.getFilePathForNote(currentNote) + "/" + fileName;
            record = new Audio(filePath, fileName);
            record.captureAudio();
            rec.setDisable(true);
            play.setDisable(true);
            stop.setDisable(false);
            choose.setDisable(true);
            ok.setDisable(true);
            statusLabel.setText("Recording...");
        });

        play.setOnAction(event -> {
            rec.setDisable(true);
            play.setDisable(true);
            stop.setDisable(false);
            choose.setDisable(true);
            ok.setDisable(true);
            statusLabel.setText("Playing...");

            Task<Void> task = new Task<Void>()
            {
                @Override
                public Void call() throws InterruptedException
                {
                    Audio.playSound(record.getFilePath());
                    return null;
                }
            };
            Thread playAudio = new Thread(task);
            playAudio.setDaemon(true);
            playAudio.start();
        });

        stop.setOnAction(event -> {
            record.stopCapture();
            record.saveAudio();
            rec.setDisable(false);
            play.setDisable(false);
            stop.setDisable(true);
            choose.setDisable(false);
            ok.setDisable(false);
            statusLabel.setText("Stopped.");
        });

        choose.setOnAction(event -> {
            rec.setDisable(true);
            play.setDisable(true);
            stop.setDisable(true);
            choose.setDisable(true);
            ok.setDisable(true);

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac", "*.ogg"));
            file = fileChooser.showOpenDialog(choose.getScene().getWindow());

            rec.setDisable(false);
            play.setDisable(false);
            stop.setDisable(true);
            choose.setDisable(false);
            ok.setDisable(false);
            statusLabel.setText("Selected: " + file.getName());
        });

        ok.setOnAction(event -> {
            try
            {
                if (file != null)
                {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("New Media Content");
                    dialog.setContentText("Please enter content title:");
                    dialog.setHeaderText("Litera Audio");
                    dialog.getDialogPane().getStyleClass().add("border-pane");
                    dialog.getDialogPane().getStylesheets().add(LocalDataManager.getNoteCSS(currentNote).replace(" ", "%20"));
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(name -> Controller.addMedia(file.getName(), result.get()));

                    Files.copy(Paths.get(file.getPath()), new File(LocalDataManager.getLocalNotesFilePath() + currentNote.getNoteName() + "/" + file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);

                }
                else if (record != null)
                {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("New Media Content");
                    dialog.setContentText("Please enter content title:");
                    dialog.setHeaderText("Litera Audio");
                    dialog.getDialogPane().getStyleClass().add("border-pane");
                    dialog.getDialogPane().getStylesheets().add(LocalDataManager.getNoteCSS(currentNote).replace(" ", "%20"));
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(name -> Controller.addMedia(record.getFileName(), result.get()));
                }
                ((Stage) ok.getScene().getWindow()).close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            //Controller.addMedia(locTime + ".wav", "aaaa");

        });

        loadCSS(currentNote);
    }

    private void loadCSS(Note n)
    {
        audioPane.getStyleClass().add("border-pane"); //border-pane has the background-color property
        audioPane.getStylesheets().clear();
        audioPane.getStylesheets().add(LocalDataManager.getNoteCSS(n).replace(" ", "%20"));
    }

}