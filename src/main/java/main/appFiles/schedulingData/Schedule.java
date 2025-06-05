package main.appFiles.schedulingData;

import main.appFiles.scheduleAlgorithm.ScheduleBuilder;
import main.appFiles.scheduleAlgorithm.Shift;
import main.appFiles.databaseManagement.ScheduleDb;

import java.sql.SQLException;
import java.time.*;
import java.util.*;

public class Schedule {
    private LocalTime startTime;
    private LocalTime endTime;
	private String tableName;
	private ArrayList<Employee> employees;
    private List<Shift> shifts;

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
	
	public void setShifts(List<Shift> shifts) {
		shifts = new ArrayList<Shift>();
		for (Shift s : shifts) {
			shifts.add(s);
		}
	}
}
