package main.appFiles;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

import main.appFiles.databaseManagement.AvailabilityDAO;
import main.appFiles.databaseManagement.EmployeeDAO;
import main.appFiles.schedulingData.Availability;
import main.appFiles.schedulingData.Employee;
import main.appFiles.schedulingData.Schedule;
import main.appFiles.schedulingData.TimeRange;
import main.appFiles.tools.ClearTables;
import main.appFiles.tools.DbTableInit;

public class Main {
	private final static EmployeeDAO employeeDAO = new EmployeeDAO();
	private final static AvailabilityDAO availabilityDAO = new AvailabilityDAO();
	
	public static void main(String[] args) {
		DbTableInit.TableInit();
		ClearTables.clearAllTables();
		
		Employee alice = new Employee("Alice", "Smith", "S001", "alice@company.com", "555-0001", "Manager");
        Employee bob   = new Employee("Bob",   "Jones", "S002", "bob@company.com",   "555-0002", "Developer");
        Employee carol = new Employee("Carol", "Lee",   "S003", "carol@company.com", "555-0003", "Tester");
        Employee david = new Employee("David", "Kim",   "S004", "david@company.com", "555-0004", "Designer");
        
        employeeDAO.insert(alice);
        employeeDAO.insert(bob);
        employeeDAO.insert(carol);
        employeeDAO.insert(david);
        
        defineAvailability(alice, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0), availabilityDAO);
        defineAvailability(bob,   DayOfWeek.MONDAY, LocalTime.of(12, 0), LocalTime.of(17, 0), availabilityDAO);
        defineAvailability(carol, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), availabilityDAO);
        defineAvailability(david, DayOfWeek.MONDAY, LocalTime.of(13, 0), LocalTime.of(17, 0), availabilityDAO);
        
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(alice);
        employees.add(bob);
        employees.add(carol);
        employees.add(david);
        
        Schedule schedule = new Schedule("09:00", "17:00", employees);
        
        System.out.println("Generated Schedule:");
        schedule.getShifts().forEach(System.out::println);

	}
	private static void defineAvailability(Employee e, DayOfWeek day, LocalTime start, LocalTime end, AvailabilityDAO availabilityDAO) {
		e.addAvailability(day.toString(), start.toString(), end.toString(), e.getEmployeeId());
		Availability a = new Availability(day, e.getEmployeeId());
		a.addTimeRange(new TimeRange(start, end));
		availabilityDAO.insert(a);
		}
}

