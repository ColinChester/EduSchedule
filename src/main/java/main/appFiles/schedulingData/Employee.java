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
    
    public void addAvailability(String day, String start, String end, int employeeId) {
        if (availabilities.containsKey(day)) {
            availabilities.get(day).addTimeRange(start, end);
        } else {
            Availability avail = new Availability(day, employeeId);
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
    
    @Override
    public String toString() {
    	return this.fName + " " + this.lName + " " + this.schoolId + " " + this.email + " " + this.phoneNum + " " + this.title + " " + this.employeeId + " " + this.availabilities;
    }
}
