package litera.Data;

/**
 * Created by celikkoseoglu on 09/03/15.
 */
public class ServerDataManager
{
    private String username;
    private String password;
    private ArrayList<String> files;
    private ArrayList<String> hashes;
    private boolean isSignedIn;
    
    
    public ServerDataManager( String user, String pass)
    {
        //Initiate user data
        //try login
        //set isSignedIn bit to true if login succesful
    }
    
    /**
     * @param username lowercase string
     * @param password
     * @return true when provided user credentials are true
     * @description sets the current user. returns true when provided credentials are true.
     */
    public boolean setUser(String username, String password)
    {
        return true;
    }

    /**
     * @return
     * @description return the note names for the provided user credentials. return array of 0 length if the user doesn't have any notes
     */
    public String[] getNoteList()
    {
        return null;
    }

    /**
     * @param osFilePath the path for the current OS for Litera
     * @param noteName   the note which we want to download
     * @return true when upload is successful
     * @description uploads the folder containing noteName. Including all files inside <5MB. DO THIS ASYNCHRONOUSLY!!
     */
    public boolean uploadNoteDirectory(String osFilePath, String noteName)
    {
        return true;
    }

    /**
     * @param osFilePath the path for the current OS for Litera
     * @param noteName   the note which we want to download
     * @return true when download & save operation is successful
     * @description downloads the directory containing the noteName. DO THIS ASYNCHRONOUSLY!!
     */
    public boolean downloadNoteDirectory(String osFilePath, String noteName)
    {
        return true;
    }

    /**
     * @param osFilePath the path for the current OS for Litera
     * @param noteName   the note which we want to download
     * @return true when download & save operation is successful
     * @description only downloads the HTML file containing the note  DO THIS ASYNCHRONOUSLY!!
     */
    public boolean downloadNoteHTMLOnly(String osFilePath, String noteName)
    {
        return true;
    }

    /**
     * @param osFilePath the path for the current OS for Litera
     * @param noteName   the note which we want to download
     * @return true when upload operation is successful
     * @description only uploads the HTML file containing the note  DO THIS ASYNCHRONOUSLY!!
     */
    public boolean uploadNoteHTMLOnly(String osFilePath, String noteName)
    {
        return true;
    }


    public boolean login()
    {
        //login using username & password properties...
        //return true when successful.
        return true;
    }
    
    /**
     * @return
     * @description checks if the username and password is valid, then creates a new user and its directories on the server
     */
    public boolean createNewUser(String username, String password, String email)
    {
        return true;
    }

    /**
     * @return true if the username is available for usage
     * @description checks for the username on the database when creating a new user
     * @description Makes sure the application directory exists for file operations. Creates directory, then returns false if first run.
     */
    private boolean duplicateUserData() //This should better be a createUser Exception.. ---- Consider later.
    {
        return true;
    }
}
