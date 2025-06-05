package main.appFiles.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.appFiles.schedulingData.Employee;

/**
 * Helper for reading employee information from CSV files.
 */
public class CSVConverter {
    
    /**
     * Parse a CSV file into {@link Employee} objects.
     *
     * @param filePath path to the CSV file
     * @return list of employees loaded from the file
     * @throws IOException if the file cannot be read
     */
    public List<Employee> readFile(String filePath) throws IOException {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                	continue;
                }
                String[] parts = line.split(",");
                String fName = parts[0].trim();
                String lName = parts[1].trim();
                String schoolId = parts[2].trim();
                String email = parts[3].trim();
                String phoneNum = parts[4].trim();
                String title = parts[5].trim();
                Employee emp = new Employee(fName, lName, schoolId, email, phoneNum, title);
                employees.add(emp);
            }
        }
        return employees;
    }
}
