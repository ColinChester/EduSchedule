package main.appFiles.schedulingData;

import main.appFiles.scheduleAlgorithm.ScheduleBuilder;
import main.appFiles.scheduleAlgorithm.Shift;
import main.appFiles.databaseManagement.ScheduleDb;

import java.sql.SQLException;
import java.time.*;
import java.util.*;

/**
 * Container for schedule information for a single day.
 */
public class Schedule {
    private LocalTime startTime;
    private LocalTime endTime;
	private String tableName;
	private ArrayList<Employee> employees;
    private List<Shift> shifts;

    /**
     * Build a schedule from the provided employees and time range.
     *
     * @param start       day start time as HH:mm
     * @param end         day end time as HH:mm
     * @param employeeList employees available to schedule
     * @param day         day of week to generate a schedule for
     */
    public Schedule(String start, String end, ArrayList<Employee> employeeList, DayOfWeek day) {
        this.startTime = LocalTime.parse(start);
        this.endTime = LocalTime.parse(end);
        this.employees = new ArrayList<>();

        for (Employee e : employeeList) {
            employees.add(e);
        }
        ScheduleBuilder.scheduleBuildDay(this, day);
        try {
			ScheduleDb.persistSchedule(this);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    }
    
	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getTableName() {
		return tableName;
	}

	public ArrayList<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(ArrayList<Employee> employees) {
		this.employees = employees;
	}
	
	public List<Shift> getShifts(){
		return shifts;
	}
	
        /**
         * Replace current shifts with the provided list.
         */
        public void setShifts(List<Shift> shifts) {
                this.shifts = new ArrayList<>();
                for (Shift s : shifts) {
                        this.shifts.add(s);
                }
        }
}
