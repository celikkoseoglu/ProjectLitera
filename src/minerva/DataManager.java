package minerva;

import java.io.*;

public class DataManager
{
    private static String OS_FILE_PATH;
    private static boolean firstRun = false;

    /**
     * @return true if operating system is supported by Litera
     * @description sets the default OS file path for Litera's data.
     * Windows : C:/Users/user.name/Documents/Litera
     * Mac : user.home/Litera/
     */
    public static boolean setOS_FILE_PATH()
    {
        String osName = System.getProperty("os.name");
        System.out.println(osName);

        if ( osName.indexOf("Mac") != -1 )
            OS_FILE_PATH = System.getProperty("user.home") + "/Litera/";

        else if ( osName.indexOf("Windows") != -1 )
            OS_FILE_PATH = "C:/Users/" + System.getProperty("user.name") + "/Documents/Litera/";

        System.out.println(OS_FILE_PATH);

        return true;
    }

    /**
     * @param n is the note to be saved
     * @return true if save is successful
     * @description saves Note object which is passed by reference
     */
    public static boolean saveNote(Note n)
    {
        try
        {
            if ( n != null )
            {
                applicationDirectoryExists(); //make sure the application folder is there before trying to write the file
                noteDirectoryExists(n.getNoteName());
                FileWriter fw = new FileWriter(OS_FILE_PATH + n.getNoteName() + "/" + n.getNoteName() + ".html"/* ,true (to append)*/);
                fw.write(n.getHtmlNote());
                fw.close();
                return true;
            }
            else
                throw new NullPointerException("You tried to save the null note!");
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
        File f = new File(OS_FILE_PATH + noteName);
        if ( f.exists() && f.isDirectory() )
            return true;
        else
        {
            new File(OS_FILE_PATH + noteName).mkdir();
            return false;
        }
    }

    /**
     * @return the String[] which contains notes or the Welcome! note
     * @description gets note names as a String[]. Checks if the application directory exists before trying to retrieve note names
     */
    public static String[] getNoteNames()
    {
        String[] listOfFileNames;
        if ( applicationDirectoryExists() )
        {
            File folder = new File(OS_FILE_PATH);
            FileFilter textFilter = new FileFilter()
            {
                public boolean accept(File folder)
                {
                    return folder.isDirectory();
                }
            };
            File[] listOfFiles = folder.listFiles(textFilter);
            listOfFileNames = new String[listOfFiles.length];

            for ( int i = 0; i < listOfFiles.length; i++ )
                listOfFileNames[i] = listOfFiles[i].getName();

            return listOfFileNames;
        }
        else
        {
            firstRun = true;
            listOfFileNames = new String[1];
            listOfFileNames[0] = "Welcome!";
            return listOfFileNames;
        }
    }

    /**
     * @param noteName is the name of the note
     * @return Note object or null if note file is not found
     * @descrition creates and returns a note object from the specified noteName
     */
    public static Note getNote(String noteName)
    {
        try
        {
            if(!firstRun)
            {
                FileReader fr = new FileReader(OS_FILE_PATH + noteName + "/" + noteName + ".html");
                BufferedReader textReader = new BufferedReader(fr);
                StringBuffer strBuffer = new StringBuffer();
                String tempString = textReader.readLine();

                while ( tempString != null )
                {
                    strBuffer.append(tempString);
                    tempString = textReader.readLine();
                }

                textReader.close();
                fr.close();
                return new Note(noteName, strBuffer.toString());
            }

            else
            {
                firstRun = false;
                return new Note(noteName, Defaults.welcomePage);
            }
        }

        catch ( Exception e )
        {
            System.out.println("Cannot find the note file. Make sure the file is in the correct place. Returning null");
            return null;
        }
    }
}