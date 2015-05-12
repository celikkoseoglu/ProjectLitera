package litera.MainFrame;

/**
 * Note class for Litera. Contains Note properties
 *
 * @author Çelik Köseoğlu
 * @version 2 - Encryption changes
 */

public class Note
{
    private String noteName, htmlNote;
    private boolean isEncrypted;

    public Note(String noteName, String htmlNote, boolean isEncrypted)
    {
        this.noteName = noteName;
        this.htmlNote = htmlNote;
        this.isEncrypted = isEncrypted;
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

    public boolean getEncrypted()
    {
        return isEncrypted;
    }

    public void setEncrypted(boolean isEncrypted)
    {
        this.isEncrypted = isEncrypted;
    }
}
