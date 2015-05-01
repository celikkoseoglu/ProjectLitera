package litera.Multimedia;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import litera.Data.LocalDataManager;
import litera.MainFrame.Note;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerController implements Initializable
{
    private final boolean repeat = false;
    private final Image pauseIcon = new Image(getClass().getResourceAsStream("/litera/Icons/pauseButton.png"));
    private final Image playIcon = new Image(getClass().getResourceAsStream("/litera/Icons/playButton.png"));
    private final ImageView pauseIconView = new ImageView(pauseIcon);
    private final ImageView playIconView = new ImageView(playIcon);

    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Duration duration;
    private Note n;

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

    public PlayerController(File f, Note note)
    {
        //System.out.println(f.toURI().toString());
        media = new Media(f.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        n = note;
    }

    private static String formatTime(Duration elapsed, Duration duration)
    {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);

        if (elapsedHours > 0)
            intElapsed -= elapsedHours * 60 * 60;

        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO))
        {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);

            if (durationHours > 0)
                intDuration -= durationHours * 60 * 60;

            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;

            if (durationHours > 0)
                return String.format("%d:%02d:%02d/%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds, durationHours, durationMinutes, durationSeconds);
            else
                return String.format("%02d:%02d/%02d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes, durationSeconds);
        }
        else
        {
            return (elapsedHours > 0) ? String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds) : String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
        }
    }

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {

        pauseIconView.setFitWidth(28);
        pauseIconView.setFitHeight(28);
        playIconView.setFitWidth(28);
        playIconView.setFitHeight(28);

        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setOnPlaying(() -> {

            if (stopRequested)
            {
                mediaPlayer.pause();
                stopRequested = false;
            }
            else playButton.setGraphic(pauseIconView);

        });

        mediaPlayer.setOnPaused(() -> playButton.setGraphic(playIconView));

        mediaPlayer.setOnReady(() -> {
            duration = mediaPlayer.getMedia().getDuration();
            updateValues();
        });

        mediaPlayer.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);

        mediaPlayer.setOnEndOfMedia(() -> {

            if (!repeat)
            {
                playButton.setGraphic(playIconView);
                stopRequested = true;
                atEndOfMedia = true;
            }

        });

        mediaPlayer.currentTimeProperty().addListener(ov -> updateValues());

        playButton.setOnAction(e -> {
            MediaPlayer.Status status = mediaPlayer.getStatus();

            if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY || status == MediaPlayer.Status.STOPPED)
            {
                if (atEndOfMedia)
                {
                    mediaPlayer.seek(mediaPlayer.getStartTime());
                    atEndOfMedia = false;
                }
                mediaPlayer.play();
            }
            else
                mediaPlayer.pause();
        });

        HBox.setHgrow(timeSlider, Priority.ALWAYS);
        timeSlider.setMinWidth(50);
        timeSlider.setMaxWidth(Double.MAX_VALUE);
        timeSlider.valueProperty().addListener(ov -> {
            if (timeSlider.isValueChanging())
                mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
        });

        volumeSlider.setPrefWidth(70);
        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSlider.setMinWidth(30);
        volumeSlider.valueProperty().addListener(ov -> {
            if (volumeSlider.isValueChanging())
                mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
        });

        mediaPlayer.play();
        mediaView.fitHeightProperty().bind(playerBorderPane.heightProperty().subtract(30));
        mediaView.fitWidthProperty().bind(playerBorderPane.widthProperty());
        loadCSS(n);
    }

    private void loadCSS(Note n)
    {
        playerBorderPane.getStyleClass().add("border-pane"); //border-pane has the background-color property
        playerBorderPane.getStylesheets().clear();
        playerBorderPane.getStylesheets().add(LocalDataManager.getNoteCSS(n).replace(" ", "%20"));
    }

    protected void updateValues()
    {
        if (playTime != null && timeSlider != null && volumeSlider != null)
        {
            Platform.runLater(() ->
            {
                Duration currentTime = mediaPlayer.getCurrentTime();
                playTime.setText(formatTime(currentTime, duration));
                timeSlider.setDisable(duration.isUnknown());
                if (!timeSlider.isDisabled() && duration.greaterThan(Duration.ZERO) && !timeSlider.isValueChanging())
                    timeSlider.setValue(currentTime.divide(duration).toMillis() * 100.0);

                if (!volumeSlider.isValueChanging())
                    volumeSlider.setValue((int) Math.round(mediaPlayer.getVolume() * 100));
            });
        }
    }

    public void disposeThis()
    {
        this.mediaPlayer.dispose();
    }
}