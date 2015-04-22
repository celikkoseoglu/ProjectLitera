package litera.Multimedia;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Mert on 27.3.2015.
 */
public class PlayerMain extends Application
{
    private static final String MEDIA_URL = "http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv"; // internetten çekerken

    String workingDir = System.getProperty("user.dir"); // filepath
    final File f = new File(workingDir, "/03.mp3");//"bird.wav");//"/91.m4v");//"/08.mp3");

    @Override
    public void start(Stage primaryStage)
    {
        final Media m = new Media(MEDIA_URL); //MEDIA_URL);//f.toURI().toString());
        final MediaPlayer mp = new MediaPlayer(m);
        final MediaView mv = new MediaView(mp);

        LiteraPlayer video = new LiteraPlayer(mp);

        //final DoubleProperty width = mv.fitWidthProperty();
        //final DoubleProperty height = mv.fitHeightProperty();

        //width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        //height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

        //mv.setPreserveRatio(true);

        StackPane root = new StackPane();
        root.getChildren().add(mv);

        final Scene scene = new Scene(root);//, mv.getFitWidth(), mv.getFitHeight());
        //video.setPrefSize(mv.getFitWidth(), mv.getFitHeight());
        scene.setRoot(video);
        scene.setFill(Color.BLACK);


        primaryStage.setScene(scene);
        primaryStage.setTitle("Litera Player");
        //primaryStage.setMaxWidth(mv.getFitWidth());
        //primaryStage.setMaxHeight(mv.getFitHeight());
        primaryStage.sizeToScene();
        //primaryStage.setFullScreen(true);
        primaryStage.show();

        mp.play();
    }
}