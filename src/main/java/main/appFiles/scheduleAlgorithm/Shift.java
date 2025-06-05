package main.appFiles.scheduleAlgorithm;

import main.appFiles.schedulingData.*;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.*;

/**
 * Represents a single shift assignment for an employee.
 */
public class Shift {
	private int employeeId;
	private DayOfWeek day;
	private LocalTime start;
	private LocalTime end;
	
	public Shift(int employeeId, DayOfWeek day, LocalTime start, LocalTime end) {
		this.employeeId = employeeId;
		this.day = day;
		this.start = start;
		this.end = end;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public DayOfWeek getDay() {
		return day;
	}

	public LocalTime getStart() {
		return start;
	}

	public LocalTime getEnd() {
		return end;
	}
	
	@Override
	public String toString() {
		return "Shift: " + start + " - " + end + ", Employee ID: " + employeeId;
	}
}
