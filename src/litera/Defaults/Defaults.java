package litera.Defaults;

import javafx.scene.paint.Color;
import minerva.Note;

public class Defaults
{
    public final static String welcomePage = "<html lang=\"en\" contenteditable=\"true\">\n" +
            "<head>\n" +
            "\t<style>\n" +
            "\tbody{\n" +
            "\t\tfont-family: \"HelveticaNeue-Light\", \"Helvetica Neue Light\", \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif;\n" +
            "\t\tmargin: 0px;\n" +
            "\t\tbackground-color: rgb(153 , 180, 209);\n" +
            "\t}\n" +
            "\th1{\n" +
            "\t\ttext-align left;\n" +
            "\t\tcolor: white;\n" +
            "\t\tpadding: 0px 15px 0px;\n" +
            "\t\tmargin: 0px;\n" +
            "\t\tfont-size: 24;\n" +
            "\t}\n" +
            "\th2{\n" +
            "\t\ttext-align: right;\n" +
            "\t\tcolor: white;\n" +
            "\t\tpadding: 0px 15px 0px;\n" +
            "\t\tfont-weight: normal;\n" +
            "\t}\n" +
            "\tli,ul{\n" +
            "\t\ttext-align: left;\n" +
            "\t\tcolor: white;\n" +
            "\t}\n" +
            "\t</style>\n" +
            "\t<link rel=\"stylesheet\" type=\"text/css\" href=\"index.css\">\n" +
            "\t<meta charset=\"utf-8\">\n" +
            "\t<title>Litera</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\t<h1>Project Litera</h1>\n" +
            "\t<h2>Team Minerva</h2>\n" +
            "\t<h1>Seems like you are running this application fro the first time.</h1>\n" +
            "\t<ul>\n" +
            "\t\t<li>This place is for you to take notes</li>\n" +
            "\t\t<li>You can customize this window from the options menu</li>\n" +
            "\t\t<li>Highlight some text and check out the buttons on the right to add style to your notes</li>\n" +
            "\t\t<li>You can also encrypt specific notes for privacy by clicking the encrypt/decrypt button on the bottom right corner.</li>\n" +
            "\t\t<li>You can also encrypt all your notes from the options menu by one click.</li>\n" +
            "\t\t<li>You can also use sync functions from options by signing up.</li>\n" +
            "\t\t<li>You can also add various multimedia\n" +
            "\t\t\t<ul>\n" +
            "\t\t\t\t<li>Add images</li>\n" +
            "\t\t\t\t<li>Add voice records</li>\n" +
            "\t\t\t\t<li>Add videos</li>\n" +
            "\t\t\t</ul>\n" +
            "\t\t</li>\n" +
            "\t</ul>\n" +
            "</body>\n" +
            "</html>";
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
