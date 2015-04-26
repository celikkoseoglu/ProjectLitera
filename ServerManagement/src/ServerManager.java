import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ServerManager 
{	
	private String username;
	private String password;
	private String literaPath;
	private ArrayList<String> files;
	private ArrayList<String> hashes;
	private boolean isSignedIn;
	
	private ServliteraClient client;
	
	public ServerManager( String user, String pass, String path)
	{
		username = user;
		password = pass;
		literaPath = path;
		client = new ServliteraClient( username, password, literaPath);
	}
	
	public int connect()
	{
		if( client.initializeConnection())
			if( client.login())
				return 0;
			else
				return 1;
		else
			return 2;
	}
	
	public int setUser( String user, String pass)
	{
		username = user;
		password = pass;
		return connect();
	}
	
	public int createNewUser( String user, String pass, String email)
	{
		return 0;
	}
	
	
	public String[] getNoteList()
	{
		return null;
	}
	
	public boolean uploadNoteDirectory( String noteName)
	{
		return true;
	}
	
	public boolean downloadNoteDirectory( String noteName)
	{
		return true;
	}
	
	public boolean uploadHTMLOnly( String noteName)
	{
		return true;
	}
	
	public boolean downloadHTMLOnly( String noteName)
	{
		return true;
	}
	
	
	
}
