package litera.MainFrame;

public class Note
{
    private String noteName, htmlNote;
    private boolean isPasswordEncrypted;

    public Note(String noteName, String htmlNote)
    {
        this.noteName = noteName;
        this.htmlNote = htmlNote;
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
}
