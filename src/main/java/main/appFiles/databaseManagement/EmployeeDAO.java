package main.appFiles.databaseManagement;

import main.appFiles.schedulingData.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import dao.BaseDAO;

import java.sql.ResultSet;

public class EmployeeDAO extends BaseDAO<Employee>{

	@Override
	protected String tableName() {
		return "employees";
	}

	@Override
	protected String idName() {
		return "employee_id";
	}

	@Override
	protected String insertStatment() {
		return "INSERT INTO employees (first_name, last_name, school_id, email, phone_number, title) VALUES (?, ?, ?, ?, ?, ?)";
	}

	@Override
	protected void fillInsert(PreparedStatement ps, Employee e) throws SQLException {
		ps.setString(1, e.getFName());
		ps.setString(2, e.getLName());
		ps.setString(3, e.getSchoolId());
		ps.setString(4, e.getEmail());
		ps.setString(5, e.getPhoneNum());
		ps.setString(6, e.getTitle());
	}

	@Override
	protected String editStatement() {
		return "UPDATE employees SET"
				+ " first_name = COALESCE(?, first_name),"
				+ " last_name = COALESCE(?, last_name),"
				+ " school_id = COALESCE(?, school_id),"
				+ " email = COALESCE(?, email),"
				+ " phone_number = COALESCE(?, phone_number),"
				+ " title = COALESCE(?, title)"
				+ " WHERE employee_id = ?";
	}

	@Override
	protected void fillEdit(PreparedStatement ps, Employee e) throws SQLException {
		ps.setString(1, e.getFName());
		ps.setString(2, e.getLName());
		ps.setString(3, e.getSchoolId());
		ps.setString(4, e.getEmail());
		ps.setString(5, e.getPhoneNum());
		ps.setString(6, e.getTitle());
		ps.setInt(7, e.getEmployeeId());
	}

	@Override
	protected Employee toObject(ResultSet rs) throws SQLException {
		return new Employee(
                rs.getInt("employee_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("school_id"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("title"));
	}
}
