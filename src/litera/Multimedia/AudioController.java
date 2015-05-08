package litera.Multimedia;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import litera.Data.LocalDataManager;
import litera.MainFrame.Note;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

/**
 * Created by Alchemistake on 29/03/15.
 */
public class AudioController implements Initializable {
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
    Slider timeSlider;

    File file;
    Audio record;
    String path;
    Note current;
    private Duration duration;

    /**
     * @param currentNote takes currentNote as a parameter and uses it to save audio to right file path
     */
    public AudioController(Note currentNote)
    {
        super();
        current = currentNote;
        //record = new Audio(path);
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
            path = LocalDataManager.getLocalNotesFilePath() + current.getNoteName()/*.replace( " ", "%20")*/ + "/" + java.time.LocalDateTime.now() + ".wav";
            record = new Audio(path);
            record.captureAudio();
            rec.setDisable(true);
            play.setDisable(true);
            stop.setDisable(false);
            choose.setDisable(true);
            ok.setDisable(true);
            timeSlider.setVisible(false);

        });

        play.setOnAction(event ->{
            rec.setDisable(true);
            play.setDisable(true);
            stop.setDisable(false);
            choose.setDisable(true);
            ok.setDisable(true);
            Audio.playSound(record.getFileName());
            timeSlider.setVisible( true);
        });

        stop.setOnAction(event ->{
            record.stopCapture();
            record.saveAudio();
            rec.setDisable(false);
            play.setDisable(false);
            stop.setDisable(true);
            choose.setDisable(false);
            ok.setDisable(false);
        });

        choose.setOnAction(event ->{
            rec.setDisable(true);
            play.setDisable(true);
            stop.setDisable(true);
            choose.setDisable(true);
            ok.setDisable(true);
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac", "*.ogg")
            );
            file = fileChooser.showOpenDialog(choose.getScene().getWindow());
            rec.setDisable(false);
            play.setDisable(false);
            stop.setDisable(true);
            choose.setDisable(false);
            ok.setDisable(false);
        });

        ok.setOnAction(event ->{
            if( file != null){
                //Controller.addAudio( LocalDataManager.addAudio( file, null));
            }
            ((Stage) ok.getScene().getWindow()).close();
        });


            // Add time slider
       /* timeSlider.valueProperty().addListener(new InvalidationListener()
        {
            public void invalidated(Observable ov)
            {
                if ( timeSlider.isValueChanging() )
                {
                    // multiply duration by percentage calculated by slider position
                    mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
                }
            }
        });
    }*/
   /* protected void updateValues(){
        if ( timeSlider != null)
        {
            Platform.runLater(new Runnable() {
                public void run() {
                    javafx.util.Duration currentTime = mp.getCurrentTime();
                    timeSlider.setDisable(duration.isUnknown());
                    if (!timeSlider.isDisabled()
                            && duration.greaterThan(javafx.util.Duration.ZERO)
                            && !timeSlider.isValueChanging()) {
                        timeSlider.setValue(currentTime.divide(duration).toMillis() * 100.0);
                    }

                }
            });
        }*/


        }

    }