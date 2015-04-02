/*
 * @author Minerva
 * Server side program for handling HTTP requests
 * 
 */
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class Default extends HttpServlet
{
	
	//connection string
	private String connectionUrl = ("jdbc:sqlserver://SQL5012.myASP.NET;databaseName=DB_9BC06A_sync;user=DB_9BC06A_sync_admin;password=Polatman58!;");

	//jdbc objects initialization
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	PrintWriter out;
	
	public void doGet( HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		out = response.getWriter();
		startCon();
		
		//parameters
        String user = request.getParameter("User");
        String pass = request.getParameter("Pass");
        //String type = request.getParameter("Type");
        //String pathCSV = request.getParameter("Paths");
        
		response.setContentType("text/html");
		
		if( login( user, pass))
			out.println("success!");
		else
			out.println("fail!");
	}

	public void startCon()
	{
		
		try
		{	
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        con = DriverManager.getConnection(connectionUrl);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			out.println("fail! @ startCon ex: " + e);
		}
		
	}

	public boolean login( String user, String pw)
	{
		
		try
		{
			PreparedStatement stmt;
			stmt = con.prepareStatement( "SELECT * FROM Users WHERE username = ? " );
			int column = 1;
			stmt.setString( column, user);
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
	
}
