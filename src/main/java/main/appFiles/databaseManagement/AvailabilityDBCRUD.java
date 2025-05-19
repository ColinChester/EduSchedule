package main.appFiles.databaseManagement;

import main.appFiles.schedulingData.Availability;
import main.appFiles.schedulingData.TimeRange;
import java.sql.SQLException;
import java.sql.Statement;

public class AvailabilityDBCRUD {
	public static int addAvailabilityDb(int employeeId, Availability a) {
	    String availabilityAdd = "INSERT INTO availability (employee_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)";
	    int newId = 0;
	    try (var conn = DbConnection.getConnection();
	         var pstmt = conn.prepareStatement(availabilityAdd, Statement.RETURN_GENERATED_KEYS)) {
	        for (TimeRange tr : a.getTimeRanges()) {
	            pstmt.setInt(1, employeeId);
	            pstmt.setString(2, a.getDay().toString());
	            pstmt.setString(3, tr.getStart().toString());
	            pstmt.setString(4, tr.getEnd().toString());
	            int rows = pstmt.executeUpdate();
	            if (rows > 0) {
	                try (var keys = pstmt.getGeneratedKeys()) {
	                    if (keys.next()) {
	                        newId = keys.getInt(1);
	                    }
	                }
	            }
	        }
	        a.refreshAvailability(employeeId);
	    } catch (SQLException e) {
	        System.err.println("Connection error in addAvailabilityDb: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return newId;
	}


    
    public static void delAvailability(int availabilityId) {
        String availabilityDel = "DELETE FROM availability WHERE availability_id = ?";
        try (var conn = DbConnection.getConnection()){
            var pstmt = conn.prepareStatement(availabilityDel);
            pstmt.setInt(1, availabilityId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Connection error in delAvailability: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void editAvailability(int employeeId, int availabilityId, Availability a) {
        String availabilityEdit = "UPDATE availability SET"
                + " employee_id = COALESCE(?, employee_id),"
                + " day_of_week = COALESCE(?, day_of_week),"
                + " start_time = COALESCE(?, start_time),"
                + " end_time = COALESCE(?, end_time)"
                + " WHERE availability_id = ?";
        try (var conn = DbConnection.getConnection()){
            if (a.getTimeRanges().isEmpty()) {
                System.out.println("No time ranges available to update.");
                return;
            }
            
            var tr = a.getTimeRanges().get(0);
            var pstmt = conn.prepareStatement(availabilityEdit);
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, a.getDay().toString());
            pstmt.setString(3, tr.getStart().toString());
            pstmt.setString(4, tr.getEnd().toString());
            pstmt.setInt(5, availabilityId);
            
            int changedRows = pstmt.executeUpdate();
            if (changedRows == 0) {
                System.out.println("Availability not found.");
            }
        } catch (SQLException e) {
            System.err.println("Connection Error in editAvailability: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void getAvailability(int availabilityId) {
        String tableQuery = "SELECT availability_id, employee_id, day_of_week, start_time, end_time FROM availability WHERE availability_id = ?";
        try (var conn = DbConnection.getConnection()){
            var pstmt = conn.prepareStatement(tableQuery);
            pstmt.setInt(1, availabilityId);
            var query = pstmt.executeQuery();
            if (query.next()) {
                System.out.printf("%-5s%-5s%-10s%-7s%-7s%n",
                        query.getInt("availability_id"),
                        query.getInt("employee_id"),
                        query.getString("day_of_week"),
                        query.getString("start_time"),
                        query.getString("end_time"));
            }
        } catch (SQLException e) {
            System.err.println("Connection error in getAvailability: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void getEmployeeAvailability(int employeeId) {
        String tableQuery = "SELECT availability_id, day_of_week, start_time, end_time FROM availability WHERE employee_id = ?";
        try (var conn = DbConnection.getConnection()){
            var pstmt = conn.prepareStatement(tableQuery);
            pstmt.setInt(1, employeeId);
            try (var query = pstmt.executeQuery()){
                while (query.next()) {
                    System.out.printf("%-5s%-10s%-7s%-7s%n",
                            query.getInt("availability_id"),
                            query.getString("day_of_week"),
                            query.getString("start_time"),
                            query.getString("end_time"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Connection error in getEmployeeAvailability: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
