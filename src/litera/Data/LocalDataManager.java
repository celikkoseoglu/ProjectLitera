package litera.Data;

import litera.Defaults.Defaults;
import minerva.Note;

import java.io.*;
import java.util.Arrays;

/**
 * @description the Local Data Manager class for Litera. Manages local data & encryption
 * @info all functions are made as efficient as possible. prove otherwise, and your code will replace mine :) (please do it)
 * @devloper Çelik Köseoğlu
 * @revision 5
 */

/*
 * change log:
 * 13/04/2015
 * fixed the "New Note" bug which prevented you from creating new notes if you clicked the New Note button repeatedly
 * fixed the bug which the app was unable to save any new note. this was caused by the major change in data structure, my mistake..
 *
 * 11/04/2015
 * changed the data structure a little bit. notes go to a directory called Notes now.
 * Meh: added the ability to delete notes (trash feature coming in the next version, just use the '-' button for now)
 */

public class LocalDataManager
{
    private static String OS_FILE_PATH;
    private static String OS_NOTES_FILE_PATH;
    private static String OS_TRASH_FILE_PATH;
    private static String OS_OPTIONS_FILE_PATH;
    private static boolean firstRun = false;

    /**
     * @return true if operating system is supported by Litera
     * @description sets the default OS file path for Litera's data.
     * Windows : C:/Users/user.name/Documents/Litera
     * Mac : user.home/Litera/
     * Linux? Will try soon...
     */
    public static boolean setOS_FILE_PATH()
    {
        String osName = System.getProperty("os.name");
        System.out.println("Operating System: " + osName + "\n");

        if ( osName.indexOf("Mac") != -1 )
            OS_FILE_PATH = System.getProperty("user.home") + "/Litera/";

        else if ( osName.indexOf("Windows") != -1 ) //what if we changed user.name to user.home? will try soon...
            OS_FILE_PATH = "C:/Users/" + System.getProperty("user.name") + "/Documents/Litera/";

        OS_NOTES_FILE_PATH = OS_FILE_PATH + "Notes/";
        OS_TRASH_FILE_PATH = OS_FILE_PATH + "Trash/";
        OS_OPTIONS_FILE_PATH = OS_FILE_PATH + "Options/";

        System.out.println("Default OS FilePath: " + OS_FILE_PATH);
        System.out.println("Default note directory FilePath:" + OS_NOTES_FILE_PATH);
        System.out.println("Default trash directory FilePath: " + OS_TRASH_FILE_PATH);
        System.out.println("Default options directory FilePath: " + OS_OPTIONS_FILE_PATH);

        System.out.println("\nOS FILE PATH SET SUCCESS\n");

        return true;
    }

    /**
     * @param n is the note to be saved
     * @return true if save is successful
     * @description saves the Note object which is passed by reference
     */
    public static boolean saveNote(Note n)
    {
        try
        {
            noteDirectoryExists(n.getNoteName());
            FileWriter fw = new FileWriter(OS_NOTES_FILE_PATH + n.getNoteName() + "/" + n.getNoteName() + ".html"/* ,true (to append)*/);
            fw.write(EncryptionManager.encryptString(n.getHtmlNote()));
            fw.close();
            return true;
        }
        catch ( NullPointerException nullPtrException )
        {
            System.err.println(nullPtrException.toString() + " :cannot save a null note. How did it even get here?");
            return false;
        }
        catch ( Exception ioe )
        {
            System.err.println("IOException: " + ioe.getMessage());
            return false;
        }
    }

    /** DO NOT TOUCH THIS!!! STILL IMPLEMENTING...
     * @return true if deletion is successful
     * @description permanently deletes notes. this function will be used for the Trash in the future..
     */
    public static boolean deleteNote(String[] selectedNotes)
    {
        File f = new File(OS_NOTES_FILE_PATH + "/");
        return f.delete() ? true : false;
    }

    /**
     * @return the String[] which contains notes or if the note directory doesn't even exist, returns null
     * @description gets note names as a String[]. Checks if the application directory exists before trying to retrieve note names
     */
    public static String[] getNoteNames()
    {
        String[] listOfFileNames;
        if ( noteDirectoryExists() )
        {
            File folder = new File(OS_NOTES_FILE_PATH);
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
        return null;
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
            FileReader fr = new FileReader(OS_NOTES_FILE_PATH + noteName + "/" + noteName + ".html");
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
            return new Note(noteName, EncryptionManager.decryptString(strBuffer.toString()));
        }

        catch ( Exception e )
        {
            throw new RuntimeException("Something is wrong with Litera's note files.");
        }
    }

    /**
     * @return new note name  -> Ex: New Note (4)
     * @description generates a new note name when the user clicks the addNoteButton. Checks if the note with the Default.NewNoteName
     * exists and appends (occurenceCount) to the end of the new note name if a note with the same name exists.
     */
    public static String createNewNote()
    {
        String newNoteName = Defaults.newNoteName;
        String[] listOfNoteNames = getNoteNames();
        if (listOfNoteNames != null)
        {
            int occurenceCount = 2;
            while ( Arrays.asList(listOfNoteNames).contains(newNoteName) )
            {
                newNoteName = Defaults.newNoteName + " (" + occurenceCount + ")";
                occurenceCount++;
            }
        }

        saveNote(new Note(newNoteName, Defaults.newNotePage));

        return newNoteName;
    }

    /** DO NOT TOUCH THIS!!! STILL IMPLEMENTING...
     * @description moves the selected notes to trash when the |-| button is clicked
     * @param selectedNotes
     * @return
     */
    public static boolean moveToTrash(String[] selectedNotes)
    {
        File sourceDir, targetDir;
        for (String s : selectedNotes)
        {
            //to be implemented soon
        }
        return true;
    }

    /**
     * @return true if the user.home/Litera/ exists
     * @description Makes sure the application directory exists for file operations. Creates directory, then returns false if first run.
     */
    private static boolean applicationDirectoryExists()
    {
        File f = new File(OS_FILE_PATH);
        if ( f.exists() && f.isDirectory() )
            return true;
        else
        {
            try
            {
                new File(OS_FILE_PATH).mkdirs();
            }
            catch ( SecurityException sException )
            {
                System.err.println(sException.toString() + " :Cannot create Litera/ directory because of security permissions");
            }
            catch ( Exception ex )
            {
                System.err.println(ex.toString() + " :Unknown exception occured while creating /Litera directory.");
            }
            return false;
        }
    }

    /**
     * @param noteName
     * @return true if the user.home/Litera/Notes/ exists
     * @description Makes sure the note directory exists for file operations. Creates directory, then returns false if first run.
     */
    private static boolean noteDirectoryExists(String noteName)
    {
        File f = new File(OS_NOTES_FILE_PATH + noteName + "/");
        if ( f.exists() && f.isDirectory() )
            return true;
        else
        {
            try
            {
                new File(OS_NOTES_FILE_PATH + noteName + "/").mkdirs();
            }
            catch ( SecurityException sException )
            {
                System.err.println(sException.toString() + " :Cannot create Litera/Notes/ directory because of security permissions");
            }
            catch ( Exception ex )
            {
                System.err.println(ex.toString() + " :Unknown exception occured while creating /Litera/Notes/ directory.");
            }
            return false;
        }
    }

    /**
     * @return true if the user.home/Litera/Notes/ exists
     * @description Makes sure the note directory exists for file operations. Creates directory, then returns false if first run.
     */
    private static boolean noteDirectoryExists()
    {
        File f = new File(OS_NOTES_FILE_PATH);
        if ( f.exists() && f.isDirectory() )
            return true;
        else
        {
            try
            {
                new File(OS_NOTES_FILE_PATH).mkdir();
            }
            catch ( SecurityException sException )
            {
                System.err.println(sException.toString() + " :Cannot create Litera/Notes/ directory because of security permissions");
            }
            catch ( Exception ex )
            {
                System.err.println(ex.toString() + " :Unknown exception occured while creating /Litera/Notes/ directory.");
            }
            return false;
        }
    }
}
