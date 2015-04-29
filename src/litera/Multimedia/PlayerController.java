package litera.Multimedia;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

//mert aytöre

public class PlayerController implements Initializable
{
    private final String MEDIA_URL = "http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv";
    private final boolean repeat = false; //new added here
    String workingDir = System.getProperty("user.dir"); // filepath
    final File f = new File(workingDir, "/91.m4v");//"bird.wav");//"/91.m4v");//"/08.mp3");
    private Media m;//= new Media(MEDIA_URL); //MEDIA_URL);//f.toURI().toString());
    private MediaPlayer mp; //= new MediaPlayer(m);
    @FXML
    private BorderPane playerBorderPane;
    @FXML
    private Button playButton;
    @FXML
    private Slider timeSlider, volumeSlider;
    @FXML
    private Label playTime;
    @FXML
    private MediaView mediaView;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Duration duration;

    public PlayerController(File f)// , Note note, String fileName) needed
    {
        File file;
        file = f;//file.toURI().toString());
        m = new Media(file.toURI().toString());
        mp = new MediaPlayer(m);
    }

    private static String formatTime(Duration elapsed, Duration duration)
    {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if ( elapsedHours > 0 )
        {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

        if ( duration.greaterThan(Duration.ZERO) )
        {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if ( durationHours > 0 )
            {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
            if ( durationHours > 0 )
            {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds, durationHours, durationMinutes, durationSeconds);
            }
            else
            {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes, durationSeconds);
            }
        }
        else
        {
            if ( elapsedHours > 0 )
            {
                return String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
            }
            else
            {
                return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
            }
        }
    }

    public void disposeThis()
    {
        this.mp.dispose();
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {
        mediaView.setMediaPlayer(mp);

        playButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                MediaPlayer.Status status = mp.getStatus();

                if ( status == MediaPlayer.Status.UNKNOWN || status == MediaPlayer.Status.HALTED )
                {
                    // don't do anything in these states
                    return;
                }

                if ( status == MediaPlayer.Status.PAUSED
                        || status == MediaPlayer.Status.READY
                        || status == MediaPlayer.Status.STOPPED )
                {
                    // rewind the movie if we're sitting at the end
                    if ( atEndOfMedia )
                    {
                        mp.seek(mp.getStartTime());
                        atEndOfMedia = false;
                    }
                    mp.play();
                }
                else
                {
                    mp.pause();
                }
            }
        });

        mp.currentTimeProperty().addListener(new InvalidationListener()
        {
            public void invalidated(Observable ov)
            {
                updateValues();
            }
        });

        mp.setOnPlaying(new Runnable()
        {
            public void run()
            {
                if ( stopRequested )
                {
                    mp.pause();
                    stopRequested = false;
                }
                else
                {
                    playButton.setText("||");
                }
            }
        });

        mp.setOnPaused(new Runnable()
        {
            public void run()
            {
                System.out.println("onPaused");
                playButton.setText(">");
            }
        });

        mp.setOnReady(new Runnable()
        {
            public void run()
            {
                duration = mp.getMedia().getDuration();
                updateValues();
            }
        });

        mp.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
        mp.setOnEndOfMedia(new Runnable()
        {
            public void run()
            {
                if ( !repeat )
                {
                    playButton.setText(">");
                    stopRequested = true;
                    atEndOfMedia = true;
                }
            }
        });

        // Add time slider
        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(50);
        timeSlider.setMaxWidth(Double.MAX_VALUE);
        timeSlider.valueProperty().addListener(new InvalidationListener()
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

        // Adjusting the volume
        volumeSlider.setPrefWidth(70);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);
        volumeSlider.valueProperty().addListener(new InvalidationListener()
        {
            public void invalidated(Observable ov)
            {
                if ( volumeSlider.isValueChanging() )
                {
                    mp.setVolume(volumeSlider.getValue() / 100.0);
                }
            }
        });

        mp.play();
        mediaView.fitWidthProperty().bind(playerBorderPane.widthProperty());
        mediaView.fitHeightProperty().bind(playerBorderPane.heightProperty());
        playerBorderPane.setMinSize(500, 500);
        System.out.println(mediaView.getFitWidth());
        System.out.println(mediaView.getFitHeight());
        //if() dosya uzant�s�na g�re
        //playerBorderPane.setMinSize(500, 600);
    }

    protected void updateValues()
    {
        if ( playTime != null && timeSlider != null && volumeSlider != null )
        {
            Platform.runLater(new Runnable()
            {
                public void run()
                {
                    Duration currentTime = mp.getCurrentTime();
                    playTime.setText(formatTime(currentTime, duration));
                    timeSlider.setDisable(duration.isUnknown());
                    if ( !timeSlider.isDisabled()
                            && duration.greaterThan(Duration.ZERO)
                            && !timeSlider.isValueChanging() )
                    {
                        timeSlider.setValue(currentTime.divide(duration).toMillis() * 100.0);
                    }
                    if ( !volumeSlider.isValueChanging() )
                    {
                        volumeSlider.setValue((int) Math.round(mp.getVolume() * 100));
                    }
                }
            });
        }
    }
}