package main.appFiles.schedulingData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import main.appFiles.databaseManagement.DbConnection;

public class Employee {
    private String fName;
    private String lName;
    private String schoolId;
    private String email;
    private String phoneNum;
    private String title;
    private int employeeId;
    
    private Map<String, Availability> availabilities = new HashMap<>();
    
    public Employee(String fName, String lname, String schoolId, String email, String phoneNum, String title) {
        this.fName = fName;
        this.lName = lname;
        this.schoolId = schoolId;
        this.email = email;
        this.phoneNum = phoneNum;
        this.title = title;
    }
    
    public Employee(int employeeId, String fName, String lname, String schoolId, String email, String phoneNum, String title) {
        this.employeeId = employeeId;
        this.fName = fName;
        this.lName = lname;
        this.schoolId = schoolId;
        this.email = email;
        this.phoneNum = phoneNum;
        this.title = title;
    }
    
    public String getFName() {
        return fName;
    }
    public void setFName(String fName) {
        this.fName = fName;
    }
    public String getLName() {
        return lName;
    }
    public void setLName(String lName) {
        this.lName = lName;
    }
    public String getSchoolId() {
        return schoolId;
    }
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public void addAvailability(String day, String start, String end) {
        if (availabilities.containsKey(day)) {
            availabilities.get(day).addTimeRange(start, end);
        } else {
            Availability avail = new Availability(day);
            avail.addTimeRange(start, end);
            availabilities.put(day, avail);
        }
    }
    
    public Map<String, Availability> getAvailabilities() {
        return availabilities;
    }
    
    public void printAvailabilities() {
        for (Availability avail : availabilities.values()) {
            System.out.println(avail.toString());
        }
    }
    
    public void employeeRefresh() {
        String tableQuery = "SELECT employee_id, first_name, last_name, school_id, email, phone_number, title FROM employees WHERE employee_id = ?";
        try (var conn = DbConnection.getConnection();
             var pstmt = conn.prepareStatement(tableQuery)) {
             pstmt.setInt(1, this.employeeId);
             try (var query = pstmt.executeQuery()) {
                 if (query.next()) {
                     this.employeeId = query.getInt("employee_id");
                     this.fName = query.getString("first_name");
                     this.lName = query.getString("last_name");
                     this.schoolId = query.getString("school_id");
                     this.email = query.getString("email");
                     this.phoneNum = query.getString("phone_number");
                     this.title = query.getString("title");
                 } else {
                     System.out.println("No record found for employee ID " + this.employeeId);
                 }
             }
        } catch (SQLException e) {
             System.err.println("Error refreshing employee: " + e.getMessage());
             e.printStackTrace();
        }
    }
    
    public static Employee employeeRefresh(int id) {
        Employee emp = null;
    	String tableQuery = "SELECT employee_id, first_name, last_name, school_id, email, phone_number, title FROM employees WHERE employee_id = ?";
        try (var conn = DbConnection.getConnection();
             var pstmt = conn.prepareStatement(tableQuery)) {
             pstmt.setInt(1, id);
             try (var query = pstmt.executeQuery()) {
                 if (query.next()) {
                	 emp = new Employee(
                     query.getInt("employee_id"),
                     query.getString("first_name"),
                     query.getString("last_name"),
                     query.getString("school_id"),
                     query.getString("email"),
                     query.getString("phone_number"),
                     query.getString("title"));
                 } else {
                     System.out.println("No record found for employee ID " + id);
                     return null;
                 }
             }
        } catch (SQLException e) {
             System.err.println("Error refreshing employee: " + e.getMessage());
             e.printStackTrace();
        }
        return emp;
    }
    
    @Override
    public String toString() {
    	return this.fName + " " + this.lName + " " + this.schoolId + " " + this.email + " " + this.phoneNum + " " + this.title + " " + this.employeeId + " " + this.availabilities;
    }
}
