package main.appFiles.schedulingData;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import main.appFiles.schedulingData.Availability;
import main.appFiles.schedulingData.TimeRange;
import main.appFiles.schedulingData.Employee;
import main.appFiles.databaseManagement.EmployeeDBCRUD;
import main.appFiles.databaseManagement.AvailabilityDBCRUD;
import main.appFiles.tools.ClearTables;
import main.appFiles.databaseManagement.DbConnection;
import main.appFiles.tools.DbTableInit;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AvailabilityTests {

    private int employeeId;

    @BeforeEach
    void setupDatabase() {
        DbConnection.connect();
        DbTableInit.TableInit();
        ClearTables.clearAllTables();
        Employee emp = new Employee("Test", "User", "T100", "test.user@example.com", "555-1000", "tester");
        employeeId = EmployeeDBCRUD.addEmployeeDb(emp);
        assertTrue(employeeId > 0, "Employee should be inserted successfully");
    }

    @Test
    @DisplayName("Add and get an availability")
    void addGetAvailability() {
        Availability avail = new Availability("MONDAY");
        avail.addTimeRange("09:00", "12:00");
        int availId = AvailabilityDBCRUD.addAvailabilityDb(employeeId, avail);
        assertTrue(availId > 0, "Should return a positive availability_id");
        Availability reloaded = new Availability("MONDAY");
        reloaded.refreshAvailability(employeeId);
        List<TimeRange> ranges = reloaded.getTimeRanges();
        assertEquals(1, ranges.size(), "Should have exactly one time range");
        assertEquals("09:00-12:00", ranges.get(0).toString());
    }

    @Test
    @DisplayName("Delete an availability")
    void deleteAvailability() {
        Availability avail = new Availability("TUESDAY");
        avail.addTimeRange("10:00", "11:00");
        int availId = AvailabilityDBCRUD.addAvailabilityDb(employeeId, avail);
        Availability before = new Availability("TUESDAY");
        before.refreshAvailability(employeeId);
        assertFalse(before.getTimeRanges().isEmpty(), "Should have availability before delete");
        AvailabilityDBCRUD.delAvailability(availId);
        Availability after = new Availability("TUESDAY");
        after.refreshAvailability(employeeId);
        assertTrue(after.getTimeRanges().isEmpty(), "All time ranges should be gone after delete");
    }
}
