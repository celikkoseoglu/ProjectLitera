package litera.Options;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class OptionsController implements Initializable
{
    @FXML
    Button exportAllNotesButton, importNotesButton, encryptAllNotesButton, decryptAllNotesButton;

    public OptionsController()
    {

    }

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {
    }
}