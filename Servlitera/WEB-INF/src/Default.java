/**
 * @author Minerva
 * Server side program for handling HTTP requests
 * This service will be used in synchronization functionalities of ProjectLitera 
 * 
 * Descriptions:
 * FILE_ID is a unique number to distinguish a user's files.
 * FILENAME is the file's name with the path. example : "{LiteraUserPath}/note1/note1_files/file1.dat
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import com.enterprisedt.net.ftp.*;
import com.enterprisedt.util.debug.*; 

public class Default extends HttpServlet
{
	//PROPERTIES//
	
	//connection string
	private Connection con;
	private ResultSet rs;
	
	private String user;
	private String pass;
	
 	

    //  Database credentials
    static final String USER = "dyigit96_litera";
    static final String PASS = "Polatman58!";
    
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://mysql1003.mochahost.com/dyigit96_litera?user="+USER+"&password="+PASS; 
   
	private FileTransferClient ftpClient;
	
	//misc.
	PrintWriter out;
	boolean auth;
	
	
	public void doGet( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		//program start...
		auth = false;
		out = response.getWriter(); 
			
		startCon(); //initialize sql connection
		startFTP(); //initialize ftp connection
		
		//parameters
        user = request.getParameter("User");
        pass = request.getParameter("Pass");
        String type = request.getParameter("Type");
        String att1 = request.getParameter("Attributes1");
        String att2 = request.getParameter("Attributes2");
        String att3 = request.getParameter("Attributes3");
        
        //PROGRAM CODE
        auth = login( user, pass); //Set authentication bit.
        if( auth)
        	out.println( "OK!");
        if( auth && type.equals("IMPORT"))
        {
        	//SEND ALL
        }
    	else if( auth && type.equals("GET_FILE"))
    	{
    		//SEND SPECIFIED FILES TO CLIENT, FILE_IDs in attb (CSV)
    		sendFile( att1, response);
    	}
    	else if( auth && type.equals("CHECK_CHANGED"))
    	{
    		//MD5 HASHES: att1 (CSV)
    		//FILE_ID: att2 (CSV)
    		//SAME ORDER
    		//CHECKSUM
    		//PRINT CHANGED FILES' FILE_IDs
    	}
    	else if( auth && type.equals("NEWFILE"))
    	{
    		//CLIENT UPLOADS A NEW FILE
    		//FILE_ID IN attb
    		//FILENAME IN bttb
    		//
    	}
    	else if( auth && type.equals("UPDATE_FILE"))
    	{
    		//CLIENT UPLOADS FILE
    		//REPLACE FILE
    	}
    	else if( auth && type.equals("NEW_USER"))
    	{
    		//username att1
    		//password att2
    		//email att3
    		//create ftp folder
    		//create sql table
    	}
    
	}

	public void doPost( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		//REAAD FILE ID LIST & PATHS & USERNAME & PASSWORD
		
		//program start...
		auth = false;
		out = response.getWriter(); 
		startCon(); //initialize sql connection
		startFTP(); //initialize ftp connection 
		
		out.println("POST REQ RECEIVED");
		saveFile( request, response);
		
	}
	
	//METHODS//
	public void startCon() //Create SQL connection for this session..
	{
		
		try
		{	
			Class.forName(JDBC_DRIVER).newInstance();
	        con = DriverManager.getConnection(DB_URL);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			out.println("fail! @ startCon ex: " + e);
		}
		
	}

	public void startFTP()
	{
		try
		{	
			ftpClient = new FileTransferClient();
			ftpClient.setRemoteHost( "ftp.yigitpolat.com");
			ftpClient.setRemotePort( 21);
			ftpClient.setUserName( "litera@yigitpolat.com");
			ftpClient.setPassword( "Polatman58!");
			ftpClient.connect();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			out.println("fail! @ startFTP ex: " + e);
		}
		
	}

	public boolean login( String user_, String pw) //User credential check.. returns true when a successful login is eventuated.
	{
		
		try
		{
			PreparedStatement stmt;
			stmt = con.prepareStatement( "SELECT * FROM Users WHERE username = ? ");
			int column = 1;
			stmt.setString( column, user_);
			rs = stmt.executeQuery();
			
			if(rs.next() && rs.getString("password").equals(pw))
			{
				return true;	
			}
			else
			{
				out.println("fail! @ login ");
				return false;
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			out.println("fail! @ login ex: " + e);
			return false;
		}	
	}
	
	
	public void sendFile( String fileID, HttpServletResponse resp)
	{
		String path, name;
		
		path = "";
		name = "";
		
		try
		{
			try
			{
				PreparedStatement stmt;
				stmt = con.prepareStatement( "SELECT * FROM Files WHERE user = '" + user + "' AND FileID = ? ");
				stmt.setString( 1, fileID);
				rs = stmt.executeQuery();
				
				if( rs.next() && rs.getString( "fileID").equals( fileID))
				{
					path = rs.getString("filepath");
					name = rs.getString("filename");
				}
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				out.println("fail! @ findFile ex: " + e);
			}
		
			resp.reset();
			resp.setHeader( "content-disposition", "attachment;filename=" + name);
			byte[] bytes = ftpClient.downloadByteArray( "/user_files/" + path + name);
			resp.getOutputStream().write( bytes);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			out.println("fail! @ sendFile ex: " + e + path + name);
		}
		
	}

	public void saveFile( HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
		    // Gets file name for HTTP header
	        String fileName = request.getHeader("fileName");
	        
	        File thisFile = new File( "/user_files/" + fileName);
	        
	        // opens input stream of the request for reading data
	        InputStream inputStream = request.getInputStream();
	        
	        // opens an output stream for writing file
	        FileTransferOutputStream outputStream = ftpClient.uploadStream( "/user_files/" + fileName);
	      
	        byte[] buffer = new byte[4096];
	        int bytesRead = -1;
	        out.println("Receiving data...");
	        
	        while ((bytesRead = inputStream.read(buffer)) != -1) 
	        {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	      
	        out.println("Data received.");
	        outputStream.close();
	        inputStream.close();
	        
	        out.println("File written to: " + thisFile.getAbsolutePath());
	         
	        response.getWriter().print("UPLOAD DONE");
	        
		}
		catch( Exception e)
		{
			out.println(e.toString());
		}
	}

	public String[] getMD5hash( String fileID)
	{
		return null;
	}
	
}
