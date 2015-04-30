package litera.MainFrame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import litera.Data.LocalDataManager;

/**
 * Starts the Main stage of Litera
 *
 * @author Çelik Köseoğlu
 * @version 2
 */

public class Main extends Application
{
    private static Stage primaryStage;
    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) throws Exception
    {
        LocalDataManager.setOS_FILE_PATH();
        launch(args);
    }

    public static Stage getPrimaryStage()
    {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("minerva.fxml"));
        primaryStage.setTitle("Project Litera");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinHeight(576);
        primaryStage.setMinWidth(720);
        primaryStage.show();

        //DragMove support
        root.setOnMousePressed(event -> {
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
        });
        //End of DragMove support

        //Stage Closing event handler
        primaryStage.setOnCloseRequest(we -> {
            System.out.println("Stage is closing");
            Controller.onExit();
            System.out.println("Closed!");
        });
    }
}