package main.appFiles.schedulingData;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import main.appFiles.schedulingData.Employee;
import main.appFiles.databaseManagement.EmployeeDBCRUD;
import main.appFiles.tools.ClearTables;
import main.appFiles.databaseManagement.DbConnection;
import main.appFiles.tools.DbTableInit;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class EmployeeTests {

    @BeforeEach
    void setupDatabase() {
        DbConnection.connect();
        DbTableInit.TableInit();
        ClearTables.clearAllTables();
    }

    @Test
    @DisplayName("Employees are addded correctly and IDs are created")
    void addEmployees() {
        Employee john = new Employee("John", "Doe", "12345", "jod@gmail.com", "123-4567", "tester");
        Employee jane = new Employee("Jane", "Doe", "56789", "jad@gmail.com", "234-5678", "cleaner");
        Employee jude = new Employee("Jude", "Doe", "01234", "jud@gmail.com", "345-6789", "player");
        int johnId = EmployeeDBCRUD.addEmployeeDb(john);
        int janeId = EmployeeDBCRUD.addEmployeeDb(jane);
        int judeId = EmployeeDBCRUD.addEmployeeDb(jude);
        assertAll("IDs should be positive and sequential",
            () -> assertTrue(johnId > 0),
            () -> assertEquals(1, johnId),
            () -> assertEquals(2, janeId),
            () -> assertEquals(3, judeId)
        );
        Employee fetchedJane = EmployeeDBCRUD.getEmployee(janeId);
        assertNotNull(fetchedJane);
        assertEquals("Jane", fetchedJane.getFName());
        assertEquals("cleaner", fetchedJane.getTitle());
    }

    @Test
    @DisplayName("Refresh returns correct data as expected")
    void refreshEmployee() {
        Employee emp = new Employee("Alice", "Smith", "A100", "a.smith@example.com", "555-0100", "engineer");
        int id = EmployeeDBCRUD.addEmployeeDb(emp);
        assertTrue(id > 0);
        Employee refreshed = Employee.employeeRefresh(id);
        assertNotNull(refreshed);
        assertEquals("Alice", refreshed.getFName());
        assertEquals("A100", refreshed.getSchoolId());
    }

    @Test
    @DisplayName("Editing an employee")
    void editEmployee() {
        Employee emp = new Employee("Bob", "Brown", "B200", "b.brown@example.com", "555-0200", "analyst");
        int id = EmployeeDBCRUD.addEmployeeDb(emp);
        emp.setTitle("senior analyst");
        boolean changed = EmployeeDBCRUD.editEmployee(emp);
        assertTrue(changed, "editEmployee should return true on success");
        Employee updated = EmployeeDBCRUD.getEmployee(id);
        assertNotNull(updated);
        assertEquals("senior analyst", updated.getTitle());
    }

    @Test
    @DisplayName("Deleting an employee")
    void deleteEmployee() {
        Employee emp = new Employee("Carol", "White", "C300", "c.white@example.com", "555-0300", "manager");
        int id = EmployeeDBCRUD.addEmployeeDb(emp);
        assertNotNull(EmployeeDBCRUD.getEmployee(id));
        boolean deleted = EmployeeDBCRUD.delEmployee(id);
        assertTrue(deleted, "delEmployee should return true when exactly one row is removed");
        assertNull(EmployeeDBCRUD.getEmployee(id), "Employee should no longer be found after deletion");
    }
}
