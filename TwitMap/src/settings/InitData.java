package settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InitData {
	public static void main(String[] args) 
	{
		  // Read RDS Connection Information from the Environment
		  String dbName = Global.dbName;
		  String userName = Global.userName;
		  String password = Global.password;
		  String hostname = Global.hostname;
		  String port = Global.port;
		  String jdbcUrl = "jdbc:mysql://" + hostname + ":" +
		    port + "/" + dbName + "?user=" + userName + "&password=" + password;
		  
		  // Load the JDBC Driver
		  try {
		    System.out.println("Loading driver...");
		    Class.forName("com.mysql.jdbc.Driver");
		    System.out.println("Driver loaded!");
		  } catch (ClassNotFoundException e) {
		    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		  }
		  
		  Connection conn = null;
		  Statement setupStatement = null;


		  try {
		    // Create connection to RDS instance
		    conn = DriverManager.getConnection(jdbcUrl);
		    
		    // Create a table and write two rows
		    setupStatement = conn.createStatement();
		    String ins = "Insert into keyword(kword) values ('music');";
		    String ins2 = "Insert into keyword(kword) values ('friend');";
		    String ins3 = "Insert into keyword(kword) values ('apple');";

		    
		    setupStatement.addBatch(ins);
		    setupStatement.addBatch(ins2);
		    setupStatement.addBatch(ins3);
		    setupStatement.executeBatch();
		    setupStatement.close();
		    
		  } catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		  } finally {
		    System.out.println("Closing the connection.");
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		  }
	}
}
