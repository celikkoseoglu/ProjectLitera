package minerva;

import javafx.scene.paint.Color;

public class Note
{
    private String noteName, htmlNote;
    private boolean isEncrypted;
    private Color noteColor, notePadColor;

    public String getHtmlNote()
    {
        return htmlNote;
    }

    public String getNoteName()
    {
        return noteName;
    }

    public boolean isEncrypted()
    {
        return isEncrypted;
    }

    public Color getNoteColor()
    {
        return noteColor;
    }

    public Color getNotePadColor()
    {
        return notePadColor;
    }
}
