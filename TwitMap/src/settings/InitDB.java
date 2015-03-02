package settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InitDB {
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
		    String createTable1 = "CREATE TABLE info ("
		    		+ "id varchar(50) not null primary key, "
		    		+ "time datetime not null, "
		    		+ "lati varchar(20), "
		    		+ "longi varchar(20)"
		    		+");";
		    
		    String createTable3 = "CREATE TABLE keyword ("
		    		+ "kid int not null primary key AUTO_INCREMENT, "
		    		+ "kword varchar(50) not null,"
		    		+ "UNIQUE (kword)"
		    		+ ");";
		    
		    String createTable2 = "CREATE TABLE twit_keyword ("
		    		+ "twit_id varchar(50) not null references info(id), "
		    		+ "key_id int not null references keyword(kid)"
		    		+ ");";
		    
		    setupStatement.addBatch(createTable1);
		    setupStatement.addBatch(createTable3);
		    setupStatement.addBatch(createTable2);
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
