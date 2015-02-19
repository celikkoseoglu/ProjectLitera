package minerva;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;

public class DataManager
{

    private static String OS_FILE_PATH;

    public static boolean saveNote(Note n)
    {
        try
        {
            applicationDirectoryExists(); //make sure the application folder is there before trying to write the file
            noteDirectoryExists(n.getNoteName());
            FileWriter fw = new FileWriter(OS_FILE_PATH + n.getNoteName() + ".html"/* ,true (to append)*/);
            fw.write(n.getHtmlNote());
            fw.close();
            return true;
        }
        catch ( Exception ioe )
        {
            System.err.println("IOException: " + ioe.getMessage());
            return false;
        }
    }

    /**
     * @return
     * @description Makes sure the application directory exists for file operations. Creates directory, then returns false if first run.
     */
    private static boolean applicationDirectoryExists()
    {
        File f = new File(OS_FILE_PATH);
        if ( f.exists() && f.isDirectory() )
            return true;
        else
        {
            new File(OS_FILE_PATH).mkdir();
            return false;
        }
    }

    /**
     * @param noteName
     * @return
     * @description Makes sure the note directory exists for file operations. Creates directory, then returns false if first run.
     */
    private static boolean noteDirectoryExists(String noteName)
    {
        File f = new File(OS_FILE_PATH + "\\" + noteName);
        if ( f.exists() && f.isDirectory() )
            return true;
        else
        {
            new File(OS_FILE_PATH).mkdir();
            return false;
        }
    }
}