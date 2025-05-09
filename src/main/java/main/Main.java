package main;

import java.util.ArrayList;

import main.appFiles.databaseManagement.EmployeeDBCRUD;
import main.appFiles.databaseManagement.*;
import main.appFiles.schedulingData.Employee;
import main.appFiles.schedulingData.Availability;
import main.appFiles.schedulingData.Schedule;
import main.appFiles.tools.ClearTables;
import main.appFiles.tools.DbTableInit;
import main.appFiles.tools.TableReader;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class Main { 
	public static void main(String[] args) {
		
		DbConnection.connect();
		DbTableInit.TableInit();
	    ArrayList<Employee> employees = new ArrayList<>();
	
	    Employee emp1 = new Employee("John", "Doe", "S123", "john.doe@example.com", "555-1234", "Manager");
	    EmployeeDBCRUD.addEmployee(emp1);
	
	    Employee emp2 = new Employee("Jane", "Smith", "S124", "jane.smith@example.com", "555-5678", "Staff");
	    EmployeeDBCRUD.addEmployee(emp2);
	
	    employees.add(emp1);
	    employees.add(emp2);
	
	    emp1.addAvailability("MONDAY", "09:00", "12:00");
	    Availability availability1 = emp1.getAvailabilities().get("MONDAY");
	    AvailabilityDBCRUD.addAvailability(emp1.getEmployeeId(), availability1);
	
	    emp2.addAvailability("MONDAY", "13:00", "17:00");
	    Availability availability2 = emp2.getAvailabilities().get("MONDAY");
	    AvailabilityDBCRUD.addAvailability(emp2.getEmployeeId(), availability2);
	
	    System.out.println("\nEmployees Table:");
	    TableReader.readEmployeeTable();
	
	    System.out.println("\nAvailability Table:");
	    TableReader.readAvailabilityTable();
	
	    Schedule schedule = new Schedule("09:00", "17:00", employees);
	
	    System.out.println("\nSchedule Tables:");
	    TableReader.readScheduleTables();
	
	    ClearTables.clearAllTables();
	}
}

