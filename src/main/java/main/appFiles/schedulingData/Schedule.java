package main.appFiles.schedulingData;

import main.appFiles.databaseManagement.DbConnection;
import main.appFiles.scheduleAlgorithm.ScheduleBuilder;
import main.appFiles.scheduleAlgorithm.Shift;
import main.appFiles.databaseManagement.ScheduleDBCRUD;
import java.time.*;
import java.util.*;

public class Schedule {
    private LocalTime startTime;
    private LocalTime endTime;
	private String tableName;
	private ArrayList<Employee> employees;
    private List<Shift> schedule;

    public Schedule(String start, String end, ArrayList<Employee> employeeList) {
        this.startTime = LocalTime.parse(start);
        this.endTime = LocalTime.parse(end);
        this.employees = new ArrayList<>();
        this.tableName = "schedule_" + System.currentTimeMillis();

        for (Employee e : employeeList) {
            employees.add(e);
        }
        createTable();
        generateSchedule();
    }
    
    private void createTable() {
    	ScheduleDBCRUD.createScheduleTable(this);
    }
    
    private void generateSchedule() {
        ScheduleBuilder.scheduleBuild(this);
        ScheduleDBCRUD.addShifts(this);
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
		return schedule;
	}
	
	public void setShifts(List<Shift> shifts) {
		schedule = new ArrayList<Shift>();
		for (Shift s : shifts) {
			schedule.add(s);
		}
	}
}
