package litera.Data;

import javafx.collections.ObservableList;
import litera.Defaults.Defaults;
import litera.MainFrame.Note;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

/**
 * the Local Data Manager class for Litera. Manages local data & encryption
 * all functions are made as efficient as possible. prove otherwise, and your code will replace mine :) (please do it)
 * @author Çelik Köseoğlu
 * @version 5
 */

/*
 * change log:
 * 25/04/2015
 * Trash was finally implemented.
 *
 * 23/04/2015
 * New comments were added
 * Shortened some of the code (Thanks to my RubberDuck)
 * I may have introduced some new bugs, we'll see
 *
 * 22/04/2015
 * moveToTrash and deleteNote functions were implemented
 * also, RubberDuck method really works!
 *
 * 20/04/2015
 * merged 'directoryExists() methods'
 * implemented load last note algorithm (possible bug: try closing the app after deleting all notes)
 *
 * 13/03/2015
 * fixed the "New Note" bug which prevented you from creating new notes if you clicked the New Note button repeatedly
 * fixed the bug which the app was unable to save any new note. this was caused by the major change in data structure, my mistake..
 *
 * 11/03/2015
 * changed the data structure a little bit. notes go to a directory called Notes now.
 * Meh: added the ability to delete notes (trash feature coming in the next version, just use the '-' button for now)
 */

public class LocalDataManager
{
    private static String OS_FILE_PATH;
    private static String OS_NOTES_FILE_PATH;
    private static String OS_TRASH_FILE_PATH;
    private static String OS_OPTIONS_FILE_PATH;

    /**
     * @return true if operating system is supported by Litera
     * sets the default OS file path for Litera's data.
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
            directoryExists(OS_NOTES_FILE_PATH + n.getNoteName() + "/");
            FileWriter fw = new FileWriter(OS_NOTES_FILE_PATH + n.getNoteName() + "/" + n.getNoteName() + ".html"/* ,true (to append)*/);
            fw.write(/*EncryptionManager.encryptString*/(n.getHtmlNote()));
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

    /**
     * @param n    the Note object to change the name for
     * @param name new name for the note
     * @return true if the rename operation is a success
     * @descriotion renames the Note object
     * @author Orhun Caglayan - written the rename method
     * @editor Celik Koseoglu - changed the method to return a boolean so we know if the rename operation is successful or not.
     */
    public static boolean renameNote(Note n, String name)
    {
        boolean fileRenameSuccessful = false, folderRenameSuccessful = false;

        File oldFile = new File(OS_NOTES_FILE_PATH + n.getNoteName() + "/" + n.getNoteName() + ".html");
        File newFile = new File(OS_NOTES_FILE_PATH + n.getNoteName() + "/" + name + ".html");
        File oldDir = new File(OS_NOTES_FILE_PATH + n.getNoteName());
        File newDir = new File(OS_NOTES_FILE_PATH + name);

        if ( oldFile.exists() )
            fileRenameSuccessful = oldFile.renameTo(newFile);

        if ( oldDir.exists() )
            folderRenameSuccessful = oldDir.renameTo(newDir);

        if ( fileRenameSuccessful && folderRenameSuccessful )
        {
            n.setNoteName(name);
            return true;
        }
        return false;

    }

    /**
     * DO NOT TOUCH THIS!!! STILL IMPLEMENTING...
     *
     * @return true if deletion is successful
     * @description permanently deletes notes. this function will be used for the Trash in the future..
     */
    public static boolean deleteNote(ObservableList selectedNotes)
    {
        for ( Object s : selectedNotes )
        {
            try
            {
                Files.delete(Paths.get(OS_TRASH_FILE_PATH + s));
            }
            catch ( Exception e )
            {
                System.out.println("Permanent deletion error!");
            }
        }
        return true;
    }

    /**
     * gets note names as a String[]. Checks if the application directory exists before trying to retrieve note names
     * @return the String[] which contains notes or if the note directory doesn't even exist, returns null
     */
    public static String[] getNoteNames(String directoryName)
    {
        String[] listOfFileNames;
        if ( directoryExists(directoryName) )
        {
            File folder = new File(directoryName);
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
     * creates and returns a note object from the specified noteName
     * @param noteName is the name of the note
     * @return Note object or null if note file is not found
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
            return new Note(noteName, /*EncryptionManager.decryptString*/(strBuffer.toString()));
        }

        catch ( Exception e )
        {
            throw new RuntimeException("Something is wrong with Litera's note files.");
        }
    }

    /**
     * returns the last note name that was being worked on while Litera was closing.
     *
     * @return the name of the last note
     */
    public static String getLastNote()
    {
        try
        {
            FileReader fr = new FileReader(OS_OPTIONS_FILE_PATH + "lastNote.lit");
            BufferedReader textReader = new BufferedReader(fr);
            String s = EncryptionManager.decryptString(textReader.readLine());
            System.out.println(s);
            textReader.close();
            fr.close();
            return s;
        }
        catch ( Exception e )
        {
            System.out.println("Last note not found!");
        }
        return null;
    }

    /**
     * saves the last note that was being worked on while Litera was closing.
     *
     * @param n the last Note object that the user opened
     * @return true if the save operation for the last note was a success.
     */
    public static boolean saveLastNote(Note n)
    {
        try
        {
            saveNote(n);
            directoryExists(OS_OPTIONS_FILE_PATH);
            FileWriter fw = new FileWriter(OS_OPTIONS_FILE_PATH + "lastNote.lit");
            fw.write(EncryptionManager.encryptString(n.getNoteName()));
            fw.close();
            return true;
        }
        catch ( Exception e )
        {
            throw new RuntimeException("Where is the last note? Did you delete all of them?");
        }
    }

    /**
     * generates a new note name when the user clicks the addNoteButton. Checks if the note with the Default.NewNoteName
     * exists and appends (occurenceCount) to the end of the new note name if a note with the same name exists.
     * @return new note name  -> Ex: New Note (4)
     */
    public static String createNewNote()
    {
        String newNoteName = Defaults.newNoteName;
        String[] listOfNoteNames = getNoteNames(OS_NOTES_FILE_PATH);
        if ( listOfNoteNames != null )
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

    public static File addAudio(File file, Note n)
    {
        try
        {
            //pass note object instead of making current note public!!
            directoryExists(OS_NOTES_FILE_PATH + n.getNoteName() + "/");
            // copy!
            return file;
        }
        catch ( NullPointerException nullPtrException )
        {
            System.err.println(nullPtrException.toString() + " :cannot save a null note. How did it even get here?");
            return null;
        }
        catch ( Exception ioe )
        {
            System.err.println("IOException: " + ioe.getMessage());
            return null;
        }
    }

    /**
     * DO NOT TOUCH THIS!!! STILL IMPLEMENTING...
     *
     * @param selectedNotes
     * @return
     * @description moves the selected notes to trash when the |-| button is clicked
     */
    public static boolean moveNotes(ObservableList selectedNotes, boolean isMovingToTrash)
    {
        directoryExists(isMovingToTrash ? OS_TRASH_FILE_PATH : OS_NOTES_FILE_PATH); //creates the trash directory if it does not exist
        for ( Object s : selectedNotes )
        {
            System.out.println("Trying to move: " + s);
            Path source = Paths.get((isMovingToTrash ? OS_NOTES_FILE_PATH : OS_TRASH_FILE_PATH) + s);
            Path destination = Paths.get((isMovingToTrash ? OS_TRASH_FILE_PATH : OS_NOTES_FILE_PATH) + s);
            try
            {
                Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File move success!");
            }
            catch ( Exception e )
            {
                System.out.println("File move operation failed");
            }
        }
        return false;

    }

    /**
     * checks if the specified directory exists. If yes, returns true, if no, creates the directory and returns false
     * @param directoryName the directory that is to be created if not found
     * @return true if exists
     */
    private static boolean directoryExists(String directoryName)
    {
        File f = new File(directoryName);
        if ( f.exists() && f.isDirectory() )
            return true;
        else
        {
            try
            {
                new File(directoryName).mkdirs();
            }
            catch ( SecurityException sException )
            {
                System.err.println(sException.toString() + " :Cannot create " + directoryName + " directory because of security permissions");
            }
            catch ( Exception ex )
            {
                System.err.println(ex.toString() + " :Unknown exception occured while creating \"" + directoryName + "\" directory.");
            }
            return false;
        }
    }

    public static String getLocalTrashFilePath()
    {
        return OS_TRASH_FILE_PATH;
    }

    public static String getLocalNotesFilePath()
    {
        return OS_NOTES_FILE_PATH;
    }
}