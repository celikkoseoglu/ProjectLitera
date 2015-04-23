package litera.MainFrame;

import javafx.scene.paint.Color;

import java.util.Locale;

public class Note
{
    private String noteName, htmlNote;
    private boolean isPasswordEncrypted;
    private Color notePadColor;

    public Note(String noteName, String htmlNote)
    {
        this.noteName = noteName;
        this.htmlNote = htmlNote;
        this.notePadColor = Color.WHITE;
    }

    public String getHtmlNote()
    {
        return htmlNote;
    }

    public void setHtmlNote(String htmlNote)
    {
        this.htmlNote = htmlNote;
    }

    public String getNoteName()
    {
        return noteName;
    }

    public void setNoteName(String newName)
    {
        noteName = newName;
    }

    public boolean isPasswordEncrypted()
    {
        return isPasswordEncrypted;
    }

    public Color getNotePadColor()
    {
        return notePadColor;
    }

    /**
     * @param c Color to be converted
     * @return String containing Hex representation of the color
     * @description converts the Color object c to a Hex representation of th color
     */
    private String colorValueToHex(Color c)
    {
        return String.format((Locale) null, "#%02x%02x%02x",
                Math.round(c.getRed() * 255),
                Math.round(c.getGreen() * 255),
                Math.round(c.getBlue() * 255));
    }
}
