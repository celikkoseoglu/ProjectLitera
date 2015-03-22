package minerva;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import litera.Data.LocalDataManager;

public class Main extends Application
{
    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("minerva.fxml"));
        primaryStage.setTitle("Project Litera");
        primaryStage.setScene(new Scene(root, 920, 700));
        primaryStage.setMinHeight(576);
        primaryStage.setMinWidth(720);

        primaryStage.show();

        root.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });

        primaryStage.setOnCloseRequest(we -> {
            System.out.println("Stage is closing");
            Controller.onExit();
            System.out.println("Closed!");
        });
    }

    public static void main(String[] args)
    {
        LocalDataManager.setOS_FILE_PATH();
        launch(args);
    }
}