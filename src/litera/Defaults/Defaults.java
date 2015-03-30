package litera.Defaults;

import javafx.scene.paint.Color;
import minerva.Note;

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

    // HTML Style Commands
    public static final String CUT_COMMAND = "cut";
    public static final String COPY_COMMAND = "copy";
    public static final String PASTE_COMMAND = "paste";

    public static final String UNDO_COMMAND = "undo";
    public static final String REDO_COMMAND = "redo";

    public static final String INSERT_HORIZONTAL_RULE_COMMAND = "inserthorizontalrule";

    public static final String ALIGN_LEFT_COMMAND = "justifyleft";
    public static final String ALIGN_CENTER_COMMAND = "justifycenter";
    public static final String ALIGN_RIGHT_COMMAND = "justifyright";
    public static final String ALIGN_JUSTIFY_COMMAND = "justifyfull";

    public static final String BULLETS_COMMAND = "insertUnorderedList";
    public static final String NUMBERS_COMMAND = "insertOrderedList";

    public static final String INDENT_COMMAND = "indent";
    public static final String OUTDENT_COMMAND = "outdent";

    public static final String FORMAT_COMMAND = "formatblock";
    public static final String FONT_FAMILY_COMMAND = "fontname";
    public static final String FONT_SIZE_COMMAND = "fontsize";

    public static final String BOLD_COMMAND = "bold";
    public static final String ITALIC_COMMAND = "italic";
    public static final String UNDERLINE_COMMAND = "underline";
    public static final String STRIKETHROUGH_COMMAND = "strikethrough";

    public static final String FOREGROUND_COLOR_COMMAND = "forecolor";
    public static final String BACKGROUND_COLOR_COMMAND = "backcolor";

    public static final Color DEFAULT_BG_COLOR = Color.WHITE;
    public static final Color DEFAULT_FG_COLOR = Color.BLACK;

    public static final String FORMAT_PARAGRAPH = "<p>";
    public static final String FORMAT_HEADING_1 = "<h1>";
    public static final String FORMAT_HEADING_2 = "<h2>";
    public static final String FORMAT_HEADING_3 = "<h3>";
    public static final String FORMAT_HEADING_4 = "<h4>";
    public static final String FORMAT_HEADING_5 = "<h5>";
    public static final String FORMAT_HEADING_6 = "<h6>";

    public static final String SIZE_XX_SMALL = "1";
    public static final String SIZE_X_SMALL = "2";
    public static final String SIZE_SMALL = "3";
    public static final String SIZE_MEDIUM = "4";
    public static final String SIZE_LARGE = "5";
    public static final String SIZE_X_LARGE = "6";
    public static final String SIZE_XX_LARGE = "7";

    public static final String INSERT_NEW_LINE_COMMAND = "insertnewline";
    public static final String INSERT_TAB_COMMAND = "inserttab";
    // end of HTML style commands
}
