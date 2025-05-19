package main.appFiles.databaseManagement;

import java.sql.DriverManager;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Connection;

public class DbConnection {
	
	    public static Connection connect() { //Implementation reworked from https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/
	    	Connection conn = null;
	    	try {
		        var url = DbConnection.class.getResource("/ScheduleData.sqlite");
		        if (url == null) {
		        	System.err.println("Could not find file.");
		        	return conn;
		        }
		        var dbFile = new File(url.toURI());
		        var path = "jdbc:sqlite:" + dbFile.getAbsolutePath();
		        conn = DriverManager.getConnection(path);
		        try (var stmt = conn.createStatement()){
		        	stmt.execute("PRAGMA foreign_keys = ON;");
		        }
		    } catch (URISyntaxException | SQLException e) { // As suggested by Eclipse
		    	System.err.print("URI Error: " + e.getMessage());
				e.getStackTrace();
		    }
			return conn;
	    }
	    
	    public static Connection getConnection() {
	    	return connect();
	    }

}