package main.appFiles.schedulingData;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import main.appFiles.databaseManagement.DbConnection;
import java.sql.SQLException;

/**
 * Availability information for a single employee on a specific day.
 */
public class Availability {
    private DayOfWeek day;
    private List<TimeRange> timeRanges = new ArrayList<>();
    private int availabilityId;
    private int employeeId;
    
    /**
     * Create an availability entry using string values for the day.
     *
     * @param day textual day of week (e.g. "MONDAY")
     * @param employeeId owner of the availability
     */
    public Availability(String day, int employeeId) {
        this.day = DayOfWeek.valueOf(day);
        this.employeeId = employeeId;
    }
    
    /**
     * Create an availability entry.
     *
     * @param day day of the week
     * @param employeeId owner of the availability
     */
    public Availability(DayOfWeek day, int employeeId) {
        this.day = day;
        this.employeeId = employeeId;
    }
    
    /**
     * Construct from a day string and a list of ranges in the format
     * {@code "HH:mm-HH:mm/..."}.
     *
     * @param day textual day of week
     * @param rangesStr one or more time ranges separated by '/'
     */
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
    
    /**
     * Add a new time range specified by strings.
     */
    public void addTimeRange(String start, String end) {
        TimeRange tr = new TimeRange(start, end);
        timeRanges.add(tr);
    }
    
    /**
     * Add a preconstructed time range.
     */
    public void addTimeRange(TimeRange tr) {
        timeRanges.add(tr);
    }
    
    
    public DayOfWeek getDay() {
        return day;
    }
    
    public List<TimeRange> getTimeRanges() {
        return timeRanges;
    }
    
	public int getAvailabilityId() {
		return availabilityId;
	}

	public void setAvailabilityId(int availabilityId) {
		this.availabilityId = availabilityId;
	}
	
	public int getEmployeeId() {
		return employeeId;
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
}
