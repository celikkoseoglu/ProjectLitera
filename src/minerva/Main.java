package minerva;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import litera.Data.LocalDataManager;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("minerva.fxml"));
        primaryStage.setTitle("Project Litera");
        primaryStage.setScene(new Scene(root, 920, 700));
        primaryStage.setMinHeight(576);
        primaryStage.setMinWidth(720);
        primaryStage.show();

        primaryStage.setOnCloseRequest(we -> {
            System.out.println("Stage is closing");
            Controller.onExit();
        });

    }

    public static void main(String[] args)
    {
        LocalDataManager.setOS_FILE_PATH();
        launch(args);
    }
}