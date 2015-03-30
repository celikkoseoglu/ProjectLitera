package minerva;

import javafx.scene.paint.Color;

public class Note
{
    private String noteName, htmlNote;
    private boolean isEncrypted;
    private Color noteColor, notePadColor;

    public Note(String noteName, String htmlNote)
    {
        this.noteName = noteName;
        this.htmlNote = htmlNote;
        this.notePadColor = Color.WHITE;
    }

    public void setHtmlNote(String htmlNote)
    {
        this.htmlNote = htmlNote;
    }
    public void setNoteName(String newName)
    {
        noteName = newName;
    }

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
