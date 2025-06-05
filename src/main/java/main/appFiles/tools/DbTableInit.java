package main.appFiles.tools;

import main.appFiles.databaseManagement.DbConnection;
import java.sql.Connection;
import java.sql.SQLException;

/** Initialize required database tables if they do not exist. */
public class DbTableInit { // https://www.sqlitetutorial.net/sqlite-java/create-table/

        /**
         * Create the employee and availability tables when absent.
         */
        public static void TableInit() {
		try(Connection conn = DbConnection.getConnection()){
			if (conn == null) {
				System.err.print("connection error 5 (init)");
				return;
			}
			String employeeTableInit = "CREATE TABLE IF NOT EXISTS employees ("
					+ " employee_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " first_name TEXT NOT NULL,"
					+ " last_name TEXT NOT NULL,"
					+ " school_id TEXT NOT NULL UNIQUE,"
					+ " email TEXT NOT NULL,"
					+ " phone_number TEXT NOT NULL,"
					+ " title TEXT"
					+ " );";
			
			String availabilityTableInit = "CREATE TABLE IF NOT EXISTS availability ("
					+ " availability_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ " employee_id INTEGER NOT NULL,"
					+ " day_of_week TEXT NOT NULL,"
					+ " start_time TEXT NOT NULL,"
					+ " end_time TEXT NOT NULL,"
					+ " FOREIGN KEY (employee_id) REFERENCES employees (employee_id)"
					+ " );";
			try (var stmt = conn.createStatement()){
				stmt.execute(employeeTableInit);
				System.out.println("Employee table startup successful");
			} catch (SQLException e) {
				System.err.println("Error creating Employee Table: " + e.getMessage());
				e.getStackTrace();
			}
			try (var stmt = conn.createStatement()){
				stmt.execute(availabilityTableInit);
				System.out.println("Availability table startup successful");
			} catch (SQLException e) {
				System.err.println("Error creating Availability table: " + e.getMessage());
				e.getStackTrace();
			}
		} catch (SQLException e) {
			System.err.println("Connection Error 6: " + e.getMessage());
			e.getStackTrace();
		}
	}
}