package main.appFiles;

import java.util.ArrayList;
import java.util.List;
import main.appFiles.databaseManagement.*;
import main.appFiles.scheduleAlgorithm.Shift;
import main.appFiles.schedulingData.Employee;
import main.appFiles.schedulingData.Schedule;
import main.appFiles.tools.ClearTables;
import main.appFiles.tools.DbTableInit;
import main.appFiles.tools.TableReader;

public class Main { 
	public static void main(String[] args) {
        // 1. Connect & initialize tables
        DbConnection.connect();
        DbTableInit.TableInit();

        // 2. Create employees
        List<Employee> employees = new ArrayList<>();
        Employee emp1 = new Employee("Alice", "One",   "E001", "a.one@example.com",   "555-0001", "Staff");
        Employee emp2 = new Employee("Bob",   "Two",   "E002", "b.two@example.com",   "555-0002", "Staff");
        Employee emp3 = new Employee("Carol", "Three", "E003", "c.three@example.com", "555-0003", "Staff");
        Employee emp4 = new Employee("Dave",  "Four",  "E004", "d.four@example.com",  "555-0004", "Staff");

        // 3. Persist them
        for (Employee e : List.of(emp1, emp2, emp3, emp4)) {
            EmployeeDBCRUD.addEmployeeDb(e);
            employees.add(e);
        }

        // 4. Mark unavailability for MONDAY
        //    emp1: hours 0–1, 3–6, 7–12, 14–15
        emp1.addAvailability("MONDAY","00:00","01:00");
        emp1.addAvailability("MONDAY","03:00","06:00");
        emp1.addAvailability("MONDAY","07:00","12:00");
        emp1.addAvailability("MONDAY","14:00","15:00");
        AvailabilityDBCRUD.addAvailabilityDb(emp1.getEmployeeId(), emp1.getAvailabilities().get("MONDAY"));

        //    emp2: hours 4–8, 12–14
        emp2.addAvailability("MONDAY","04:00","08:00");
        emp2.addAvailability("MONDAY","12:00","14:00");
        AvailabilityDBCRUD.addAvailabilityDb(emp2.getEmployeeId(), emp2.getAvailabilities().get("MONDAY"));

        //    emp3: hours 0–4, 7–9
        emp3.addAvailability("MONDAY","00:00","04:00");
        emp3.addAvailability("MONDAY","07:00","09:00");
        AvailabilityDBCRUD.addAvailabilityDb(emp3.getEmployeeId(), emp3.getAvailabilities().get("MONDAY"));

        //    emp4: hours 1–7, 10–15
        emp4.addAvailability("MONDAY","01:00","07:00");
        emp4.addAvailability("MONDAY","10:00","15:00");
        AvailabilityDBCRUD.addAvailabilityDb(emp4.getEmployeeId(), emp4.getAvailabilities().get("MONDAY"));

        // 5. Build a schedule from 00:00 to 15:00 (15 one-hour slots)
        Schedule schedule = new Schedule("00:00", "15:00", new ArrayList<>(employees));

        // 6. Print out the resulting shifts
        System.out.println("Generated schedule for MONDAY (00:00–15:00):");
        for (Shift shift : schedule.getShifts()) {
            System.out.println(shift);
        }

        // 7. Clean up
        ClearTables.clearAllTables();
	}
}

