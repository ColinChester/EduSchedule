package main.appFiles.databaseManagement;

import main.appFiles.schedulingData.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class EmployeeDBCRUD {
	public static int addEmployeeDb(Employee employee) {
	    String employeeAdd = 
	      "INSERT INTO employees (first_name, last_name, school_id, email, phone_number, title) VALUES (?, ?, ?, ?, ?, ?)";
	    int newId = 0;
	    try (var conn = DbConnection.getConnection();
	         var pstmt = conn.prepareStatement(employeeAdd, Statement.RETURN_GENERATED_KEYS)) {
	        pstmt.setString(1, employee.getFName());
	        pstmt.setString(2, employee.getLName());
	        pstmt.setString(3, employee.getSchoolId());
	        pstmt.setString(4, employee.getEmail());
	        pstmt.setString(5, employee.getPhoneNum());
	        pstmt.setString(6, employee.getTitle());

	        int rows = pstmt.executeUpdate();
	        if (rows > 0) {
	            try (ResultSet keys = pstmt.getGeneratedKeys()) {
	                if (keys.next()) {
	                    newId = keys.getInt(1);
	                    employee.setEmployeeId(newId);
	                }
	            }
	        }
	        employee.employeeRefresh();
	    } catch (SQLException e) {
	        System.err.println("Connection error 1: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return newId;
	}

	
	public static boolean delEmployee(int id) { //https://www.sqlitetutorial.net/sqlite-java/delete/
		String employeeDel = "DELETE FROM employees WHERE employee_id = ?";
		String findAvailability = "SELECT EXISTS(SELECT 1 FROM availability WHERE employee_id = ?";
		String availabilityDel = "DELETE FROM availability WHERE employee_id = ?";
		int empRow = 0;
		int avaRow = 0;
		
		try (Connection conn = DbConnection.getConnection()){
			var pstmt = conn.prepareStatement(employeeDel);
			pstmt.setInt(1, id);
			empRow = pstmt.executeUpdate();
			if (empRow > 0) {
				System.out.println("User successfully deleted from employee table");
			}
			
			pstmt = conn.prepareStatement(findAvailability);
			pstmt.setInt(1,  id);
			var result = pstmt.executeQuery();
			result.next();
			if (result.getInt(1) == 1) {
				pstmt = conn.prepareStatement(availabilityDel);
				pstmt.setInt(1,  id);
				avaRow = pstmt.executeUpdate();
				if (avaRow > 0) {
					System.out.println("User successfully deleted from availability table");
				}
			}	
		} catch (SQLException e) {
			System.out.println("Connection error 2: " + e.getMessage());
			e.getStackTrace();
		}
		return empRow > 0;
	}
	
	public static boolean editEmployee(Employee updatedEmployee) {
		String employeeUpd = "UPDATE employees SET"
				+ " first_name = COALESCE(?, first_name),"
				+ " last_name = COALESCE(?, last_name),"
				+ " school_id = COALESCE(?, school_id),"
				+ " email = COALESCE(?, email),"
				+ " phone_number = COALESCE(?, phone_number),"
				+ " title = COALESCE(?, title)"
				+ " WHERE employee_id = ?";
		try (Connection conn = DbConnection.getConnection()){
			var pstmt = conn.prepareStatement(employeeUpd);
			pstmt.setString(1, updatedEmployee.getFName());
			pstmt.setString(2, updatedEmployee.getLName());
			pstmt.setString(3, updatedEmployee.getSchoolId());
			pstmt.setString(4, updatedEmployee.getEmail());
			pstmt.setString(5, updatedEmployee.getPhoneNum());
			pstmt.setString(6, updatedEmployee.getTitle());
			pstmt.setInt(7, updatedEmployee.getEmployeeId());
			
			int changedRows = pstmt.executeUpdate();
			if (changedRows > 0) {
				System.out.println("Employee info updated");
				return true;
			}else {
				System.out.println("Employee not found");
			}
		}catch (SQLException e) {
			System.out.println("Error updating user: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static void printEmployee(int employeeId) {
		String tableQuery = "SELECT employee_id, first_name, last_name, school_id, email, phone_number, title FROM employees WHERE employee_id = ?";
		try (var conn = DbConnection.getConnection()){
			var pstmt = conn.prepareStatement(tableQuery);
			pstmt.setInt(1, employeeId);
			var query = pstmt.executeQuery();
			if (query.next()) {
				System.out.printf("%-5s%-10s%-10s%-10s%-35s%-15s%-10s%n",
						query.getInt("employee_id"),
						query.getString("first_name"),
						query.getString("last_name"),
						query.getString("school_id"),
						query.getString("email"),
						query.getString("phone_number"),
						query.getString("title")
					);
			}else {
				System.out.println("Employee not found at employee ID " + employeeId);
			}
		} catch (SQLException e){
			System.out.println("Connection Error 3: " + e.getMessage());
			e.getStackTrace();
		}
	}
	
	public static Employee getEmployee(int employeeId) {
		Employee emp = null;
		String tableQuery = "SELECT employee_id, first_name, last_name, school_id, email, phone_number, title FROM employees WHERE employee_id = ?";
		try (var conn = DbConnection.getConnection()){
			var pstmt = conn.prepareStatement(tableQuery);
			pstmt.setInt(1, employeeId);
			var query = pstmt.executeQuery();
			if (query.next()) {
				emp = new Employee(
						query.getInt("employee_id"),
						query.getString("first_name"),
						query.getString("last_name"),
						query.getString("school_id"),
						query.getString("email"),
						query.getString("phone_number"),
						query.getString("title")
					);
			}else {
				System.out.println("Employee not found at employee ID " + employeeId);
				return null;
			}
		} catch (SQLException e){
			System.out.println("Connection Error 3: " + e.getMessage());
			e.getStackTrace();
		}
		return emp;
	}
}
