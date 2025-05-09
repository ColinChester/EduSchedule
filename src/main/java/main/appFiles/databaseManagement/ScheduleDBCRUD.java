package main.appFiles.databaseManagement;

import java.sql.SQLException;

import main.appFiles.scheduleAlgorithm.Shift;
import main.appFiles.schedulingData.Schedule;

public class ScheduleDBCRUD {
	public static void createScheduleTable(Schedule s) {
        String scheduleAdd = "CREATE TABLE IF NOT EXISTS " + s.getTableName() + " ("
                + "shift_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "employee_id INTEGER NOT NULL,"
                + "day_of_week TEXT NOT NULL,"
                + "shift_start TEXT NOT NULL,"
                + "shift_end TEXT NOT NULL,"
                + "FOREIGN KEY(employee_id) REFERENCES employees(employee_id)"
                + ");";

        try (var conn = DbConnection.getConnection(); var stmt = conn.createStatement()) {
            stmt.execute(scheduleAdd);
            System.out.println("Created schedule table: " + s.getTableName());
        } catch (SQLException e) {
            System.err.println("Error creating schedule table: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
	public static void addShifts(Schedule s) {
	    String shiftAdd = "INSERT INTO " + s.getTableName() +
	                 " (employee_id, day_of_week, shift_start, shift_end) VALUES (?, ?, ?, ?)";
	    try (var conn = DbConnection.getConnection()) {
	        for (Shift shift : s.getShifts()) {
	            try (var pstmt = conn.prepareStatement(shiftAdd)) {
	            	pstmt.setInt(1, shift.getEmployeeid());
	                pstmt.setString(2, shift.getDay().toString());
	                pstmt.setString(3, shift.getStart().toString());
	                pstmt.setString(4, shift.getEnd().toString());
	                pstmt.executeUpdate();
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error inserting shifts: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
}
