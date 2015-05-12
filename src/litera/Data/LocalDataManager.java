package litera.Data;

import javafx.collections.ObservableList;
import litera.Defaults.Defaults;
import litera.MainFrame.Note;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * the Local Data Manager class for Litera. Manages local data & encryption
 * all functions are made as efficient as possible. prove otherwise, and your code will replace mine :) (please do it)
 *
 * @author Çelik Köseoğlu
 * @version 17
 */

/*
 * change log:
 * 01/05/2015
 * added getFilePathForNote() function
 * added more comments
 *
 * 26/05/2015
 * saves and loads note colors
 * every file has a unique id now
 * many functions were saving text so I created the saveText() method to eliminate code duplicates
 *
 * 25/04/2015
 * Trash was finally implemented.
 *
 * 23/04/2015
 * New comments have been added
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

        else if ( osName.indexOf("Linux") != -1 )
            OS_FILE_PATH = System.getProperty("user.home") + "/Litera/";

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

    private static boolean saveText(String filePath, String fileName, String text, boolean isEncrypted)
    {
        try
        {
            directoryExists(filePath);
            FileWriter fw = new FileWriter(filePath + fileName);
            fw.write(isEncrypted ? EncryptionManager.encryptString(text) : text);
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
     * @param n is the note to be saved
     * @return true if save is successful
     * @description saves the Note object which is passed by reference
     */
    public static boolean saveNote(Note n)
    {
        if ( n != null )
        {
            String filePath = OS_NOTES_FILE_PATH + n.getNoteName() + "/";
            String fileName = n.getNoteName() + (n.getEncrypted() ? ".litc" : ".html");
            saveText(filePath, fileName, n.getHtmlNote(), true);
            return true;
        }
        return false;
    }

    public static boolean saveNoteCSS(Note n, String hexColor)
    {
        if ( n != null && hexColor != null )
        {
            String filePath = OS_NOTES_FILE_PATH + n.getNoteName() + "/";
            String fileName = "style.css";
            saveText(filePath, fileName, Defaults.COLOR_SCHEME_CSS_1 + hexColor + Defaults.COLOR_SCHEME_CSS_2 + hexColor + Defaults.COLOR_SCHEME_CSS_3 + hexColor + Defaults.COLOR_SCHEME_CSS_4, false);
            return true;
        }
        return false;
    }

    public static String getNoteCSS(Note n)
    {
        File f = new File(OS_NOTES_FILE_PATH + n.getNoteName() + "/style.css");
        return f.exists() ? (System.getProperty("os.name").indexOf("Windows") != -1) ? "" + "file:///" + f.getAbsolutePath().replace("\\", "/") : "file:" + f.toString() : Defaults.class.getResource("/litera/Defaults/Default.css").toString();
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
     * deleted the selected notes permanently using the delete() method
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
                delete(new File(OS_TRASH_FILE_PATH + s));
            }
            catch ( Exception e )
            {
                System.out.println(e.toString());
            }
        }
        return true;
    }

    /**
     * recursively deletes directory and its subdirectories
     * credit - http://www.mkyong.com/java/how-to-delete-directory-in-java/
     *
     * @param file
     * @throws IOException
     */
    private static void delete(File file) throws IOException
    {

        if ( file.isDirectory() )
        {

            //directory is empty, then delete it
            if ( file.list().length == 0 )
            {
                file.delete();
                System.out.println("Directory is deleted : " + file.getAbsolutePath());
            }
            else
            {
                //list all the directory contents
                String files[] = file.list();

                for ( String temp : files )
                {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }
                //check the directory again, if empty then delete it
                if ( file.list().length == 0 )
                {
                    file.delete();
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }
        }
        else
        {
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    /**
     * gets note names as a String[]. Checks if the application directory exists before trying to retrieve note names
     *
     * @return the String[] which contains notes or if the note directory doesn't even exist, returns null
     */
    public static String[] getNoteNames(String directoryName)
    {
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
            String[] listOfFileNames = new String[listOfFiles.length];

            for ( int i = 0; i < listOfFiles.length; i++ )
                listOfFileNames[i] = listOfFiles[i].getName();

            System.out.println(Arrays.toString(listOfFileNames));

            return listOfFileNames;
        }
        return null;
    }

    /**
     * creates and returns a note object from the specified noteName
     *
     * @param noteName is the name of the note
     * @return Note object or null if note file is not found
     */
    public static Note getNote(String noteName)
    {
        if ( noteName != null )
            try
            {
                File f = new File(OS_NOTES_FILE_PATH + noteName + "/" + noteName + ".litc");
                FileReader fr = new FileReader(f.exists() ? f.toString() : (OS_NOTES_FILE_PATH + noteName + "/" + noteName + ".html"));
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
                return new Note(noteName, EncryptionManager.decryptString(strBuffer.toString()), f.exists());
            }

            catch ( Exception e )
            {
                System.out.println(e.toString() + ": Something is wrong with Litera's note files");
                e.printStackTrace();
                return null;
            }
        else
            return null;
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
            System.out.println("Last note is: " + s);
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
     *
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

        Note n = new Note(newNoteName, Defaults.newNotePage, false);
        saveNote(n);
        generateAndSaveID(n);
        return newNoteName;
    }

    public static boolean generateAndSaveID(Note n)
    {
        if ( n != null )
        {
            saveText(OS_NOTES_FILE_PATH + n.getNoteName() + "/", "id.lit", LocalDateTime.now().toString(), false);
            return true;
        }
        return false;
    }

    public static String[] getFileIDs()
    {
        try
        {
            String[] notes = getNoteNames(OS_NOTES_FILE_PATH);
            for ( int i = 0; i < notes.length; i++ )
            {
                FileReader fr = new FileReader(OS_NOTES_FILE_PATH + notes[i] + "/id.lit");
                BufferedReader textReader = new BufferedReader(fr);
                notes[i] = textReader.readLine();
                textReader.close();
                fr.close();
            }
            return notes;
        }
        catch ( Exception e )
        {
            System.out.println("FileID reading error");
        }
        return null;
    }

    /**
     * moves notes back and forth between trash list and the actual note list
     *
     * @param selectedNotes
     * @param isMovingToTrash true if the move operation is from the listbox to the trash, false otherwise
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
     *
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

    public static String getFilePathForNote(Note n)
    {
        return OS_NOTES_FILE_PATH + "/" + n.getNoteName() + "/";
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