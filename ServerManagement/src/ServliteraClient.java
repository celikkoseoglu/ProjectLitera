import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class ServliteraClient
{
	private final String SERVER_URL = "http://localhost:8080/Servlitera/Default";
	private String username;
	private String password;
	private String literaPath;
	
	private getThread getRequest;
	private postThread postRequest;
	
	
	public ServliteraClient(String user, String pass, String path) 
	{
		username = user;
		password = pass;
		literaPath = path;
		
		getRequest = new getThread();
		postRequest = new postThread();
		// TODO Auto-generated constructor stub
	}
	
	public boolean initializeConnection()
	{
		
		return true;
	}
	
	public boolean login()
	{
		return true;
	}

	public void postNotes( String[] fileIDs)
	{
		((postThread) postRequest).start();
	}
	
	public void getNotes( String[] fileIDs)
	{
		
	}
	
	class getThread implements Runnable
	{
		Thread t;
		int rqType;
		
		public void setType( int t)
		{
			
		}
		
		public void run() 
		{
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class postThread implements Runnable
	{
		Thread t;
		int rqType;
		
		public void setType( int t)
		{
			
		}
		
		public void run() 
		{
			postFile();
			
		}
		
		public void start()
		   {
		      if (t == null)
		      {
		         t = new Thread (this, "post");
		         t.start ();
		      }
		   }
		
		public void postFile()
		{
			try 
			{
				
				String filePath = "C:\\Users\\Yigit\\Documents\\ENG101\\How Music Is an Effective Tool in Politics_F.docx";
				File uploadFile = new File(filePath);
 
				System.out.println("File to upload: " + filePath);
 
				// creates a HTTP connection
				URL url = new URL(SERVER_URL);
				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				httpConn.setUseCaches(false);
				httpConn.setDoOutput(true);
				httpConn.setRequestMethod("POST");
				// sets file name as a HTTP header
				httpConn.setRequestProperty("fileName", uploadFile.getName());
 
				// opens output stream of the HTTP connection for writing data
				OutputStream outputStream = httpConn.getOutputStream();
 
				// Opens input stream of the file for reading data
				FileInputStream inputStream = new FileInputStream(uploadFile);
 
				byte[] buffer = new byte[512];
				int bytesRead = -1;
 
				System.out.println("Start writing data...");
				int i = 1;
				while ((bytesRead = inputStream.read(buffer)) != -1) 
				{
				    outputStream.write(buffer, 0, bytesRead);
				    i++;
				}
 
				System.out.println("Data was written. File Size = " + i);
				outputStream.close();
				inputStream.close();
 
				// always check HTTP response code from server
				int responseCode = httpConn.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) 
				{
				    // reads server's response
				    BufferedReader reader = new BufferedReader( new InputStreamReader( httpConn.getInputStream()));
				    String temp = reader.readLine();
				    String response = "";
				    while( temp != null)
				    {
				    	response += "\n" + temp;
				    	temp = reader.readLine();
				    }
				    System.out.println("Server's response: " + response);
				} 
				else 
				{
				    System.out.println("Server returned non-OK code: " + responseCode);
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	
}
