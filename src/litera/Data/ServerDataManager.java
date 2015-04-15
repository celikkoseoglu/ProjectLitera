package litera.Data;

/**
 * @author Minerva
 * Server tasks manager
 * 
 */
public class ServerManager
{
    private String username;
    private String password;
    private String literaPath
    private ArrayList<String> files; //File ID List
    private ArrayList<String> hashes; //validation hashes for the files in fileID arraylist, same indexes.
    private boolean isSignedIn;
    private LiteraClient client;
    
    public ServerDataManager( String user, String pass, String path)
    {
        //Initiate user data
        username = user;
        password = pass;
        //create and initiate client
        client = new LiteraClient( username, password);
        
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
    public boolean downloadNoteHTMLOnly( String noteName)
    {
        return true;
    }

    /**
     * @param osFilePath the path for the current OS for Litera
     * @param noteName   the note which we want to download
     * @return true when upload operation is successful
     * @description only uploads the HTML file containing the note  DO THIS ASYNCHRONOUSLY!!
     */
    public boolean uploadNoteHTMLOnly( String noteName)
    {
        return true;
    }


    public boolean connect()
    {
        //try login
        //set isSignedIn bit to true if login succesful
        isSignedIn = client.login( username, password);
        //return true when successful.
        return isSignedIn;
    }
    
    /**
     * @return 0 if possible 1 if duplicate username, 2 if duplicate email, 3 if connection not possible
     * @description checks if the username and password is valid, then creates a new user and its directories on the server
     */
    public int createNewUser(String user, String pass, String email)
    {
        return 0;
    }

}
