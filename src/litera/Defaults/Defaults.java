package litera.Defaults;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import litera.Data.LocalDataManager;
import litera.MainFrame.Note;

import java.util.Locale;

/**
 * contains default parameters for the HTML page and some other essential functions for Litera
 *
 * @author Çelik Köseoğlu
 * @version 2
 */

public class Defaults
{
    public final static String welcomePage = "<!DOCTYPE html>" +
            "<html lang=\"en\"><head contenteditable=true><meta charset=\"utf-8\">" +
            "<title>Celik Koseoglu</title></head><body contenteditable=\"true\"></body></html>";

    public final static String[] welcomeList = {"Welcome!"};

    public final static String newNoteName = "New Note";

    public final static String newNotePage = "<!DOCTYPE html>" +
            "<html lang=\"en\"><head contenteditable=true><meta charset=\"utf-8\">" +
            "<title>Celik Koseoglu</title></head><body contenteditable=\"true\"></body></html>";

    public static final String CUT_COMMAND = "cut";
    public static final String COPY_COMMAND = "copy";
    public static final String PASTE_COMMAND = "paste";

    public static final String UNDO_COMMAND = "undo";
    public static final String REDO_COMMAND = "redo";

    public static final String NUMBERS_COMMAND = "insertOrderedList";

    public static final String BOLD_COMMAND = "bold";
    public static final String ITALIC_COMMAND = "italic";
    public static final String UNDERLINE_COMMAND = "underline";
    public static final String STRIKETHROUGH_COMMAND = "strikethrough";

    public static final String FOREGROUND_COLOR_COMMAND = "forecolor";
    public static final String BACKGROUND_COLOR_COMMAND = "backcolor";

    public static final String COLOR_SCHEME_CSS_1 = ".list-cell:filled:selected:focused,.list-cell:filled:selected {-fx-background-color:";
    public static final String COLOR_SCHEME_CSS_2 = ";-fx-text-fill: white;}.list-cell:filled:hover {-fx-background-color: #d2d2d2;-fx-text-fill: white;}.list-view{-fx-border-color:";
    public static final String COLOR_SCHEME_CSS_3 = ";}.border-pane{-fx-background-color:";
    public static final String COLOR_SCHEME_CSS_4 = ";}";

    /**
     * @param c Color to be converted
     * @return String containing Hex representation of the color
     * @description converts the Color object c to a Hex representation of th color
     */
    public static String colorValueToHex(Color c)
    {
        return String.format((Locale) null, "#%02x%02x%02x",
                Math.round(c.getRed() * 255),
                Math.round(c.getGreen() * 255),
                Math.round(c.getBlue() * 255));
    }

    public static void loadCSS(Note n, Parent component)
    {
        component.getStylesheets().clear();
        component.getStylesheets().add(LocalDataManager.getNoteCSS(n).replace(" ", "%20"));
    }

}
