package main.appFiles.schedulingData;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import main.appFiles.databaseManagement.DbConnection;
import java.sql.SQLException;

public class Availability {
    private DayOfWeek day;
    private List<TimeRange> timeRanges = new ArrayList<>();
    private int availabilityId;
    
    public Availability(String day) {
        this.day = DayOfWeek.valueOf(day);
    }
    
    public Availability(DayOfWeek day) {
        this.day = day;
    }
    
    public Availability(String day, String rangesStr) {
        this.day = DayOfWeek.valueOf(day);
        String[] ranges = rangesStr.split("/");
        for (String r : ranges) {
            String[] parts = r.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid time range format: " + r);
            }
            addTimeRange(parts[0].trim(), parts[1].trim());
        }
    }
    
    public void addTimeRange(String start, String end) {
        TimeRange tr = new TimeRange(start, end);
        timeRanges.add(tr);
    }
    
    
    public DayOfWeek getDay() {
        return day;
    }
    
    public List<TimeRange> getTimeRanges() {
        return timeRanges;
    }
    
    public void setEmployeeId(int newId) {
    	this.availabilityId = newId;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(day.toString()).append(": ");
        for (int i = 0; i < timeRanges.size(); i++) {
            sb.append(timeRanges.get(i).toString());
            if (i < timeRanges.size() - 1) {
                sb.append(" / ");
            }
        }
        return sb.toString();
    }
    
    public void refreshAvailability(int employeeId) {
        String tableQuery = "SELECT start_time, end_time FROM availability WHERE employee_id = ? AND day_of_week = ?";
        try (var conn = DbConnection.getConnection();
             var pstmt = conn.prepareStatement(tableQuery)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, day.toString());
            try (var query = pstmt.executeQuery()) {
                timeRanges.clear();
                while (query.next()) {
                    String start = query.getString("start_time");
                    String end = query.getString("end_time");
                    addTimeRange(start, end);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error refreshing availability: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
