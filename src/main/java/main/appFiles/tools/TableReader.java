package main.appFiles.tools;

import main.appFiles.databaseManagement.DbConnection;
import java.sql.SQLException;
import java.sql.ResultSet;

public class TableReader {// Test resource, https://www.sqlitetutorial.net/sqlite-java/select/
	public static void readEmployeeTable() {
		String tableQuery = "SELECT employee_id, first_name, last_name, school_id, email, phone_number, title FROM employees";
		try (var conn = DbConnection.getConnection()){
			var stmt = conn.createStatement();
			var query = stmt.executeQuery(tableQuery);
			
			while (query.next()) {
				System.out.printf("%-5s%-10s%-10s%-10s%-35s%-15s%-10s%n",
						query.getInt("employee_id"),
						query.getString("first_name"),
						query.getString("last_name"),
						query.getString("school_id"),
						query.getString("email"),
						query.getString("phone_number"),
						query.getString("title")
					);
			}
		} catch (SQLException e) {
			System.err.println("Connection error 4: " + e.getMessage());
			e.getStackTrace();
		}
	}
	
	public static void readAvailabilityTable() {
		String tableQuery = "SELECT availability_id, employee_id, day_of_week, start_time, end_time FROM availability";
		try (var conn = DbConnection.getConnection()){
			var stmt = conn.createStatement();
			var query = stmt.executeQuery(tableQuery);
			
			while (query.next()) {
				System.out.printf("%-5s%-5s%-10s%-7s%-7s%n",
						query.getInt("availability_id"),
						query.getInt("employee_id"),
						query.getString("day_of_week"),
						query.getString("start_time"),
						query.getString("end_time"));
			}
		} catch (SQLException e) {
			System.err.println("Connection error 4a: " + e.getMessage());
			e.getStackTrace();
		}
	}
	
	public static void readScheduleTables() {
		String tableQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name LIKE 'schedule_%';";
		try (var conn = DbConnection.getConnection()){
			var stmt = conn.createStatement();
			var query = stmt.executeQuery(tableQuery);
			while (query.next()) {
				String tableName = query.getString("name");
				System.out.println("Reading table " + tableName);
				String tableInfo = "SELECT shift_id, employee_id, day_of_week, shift_start, shift_end FROM " + tableName;
				try (var newStmt = conn.createStatement()){
					var newQuery = newStmt.executeQuery(tableInfo);
					while (newQuery.next()) {
						System.out.printf("%-5s%-5s%-10s%-10s%-s%n",
								query.getInt("shift_id"),
								query.getInt("employee_id"),
								query.getString("day_of_week"),
								query.getString("shift_start"),
								query.getString("shift_end"));
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Connection error 4b: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
