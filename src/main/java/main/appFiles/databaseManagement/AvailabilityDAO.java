package main.appFiles.databaseManagement;

import main.appFiles.schedulingData.Availability;
import main.appFiles.schedulingData.TimeRange;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import dao.BaseDAO;

public class AvailabilityDAO extends BaseDAO<Availability>{

	@Override
	protected String tableName() {
		return "availability";
	}

	@Override
	protected String idName() {
		return "availability_id";
	}

	@Override
	protected String insertStatment() {
		return "INSERT INTO availability (employee_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)";
	}

	@Override
	protected void fillInsert(PreparedStatement ps, Availability a) throws SQLException {
		TimeRange tr = a.getTimeRanges().get(0);
		ps.setInt(1, a.getEmployeeId());
		ps.setString(2, a.getDay().toString());
        ps.setString(3, tr.getStart().toString());
        ps.setString(4, tr.getEnd().toString());
	}

	@Override
	protected String editStatement() {
		return "UPDATE availability SET"
	            + "  employee_id = COALESCE(?, employee_id),"
	            + "  day_of_week  = COALESCE(?, day_of_week),"
	            + "  start_time   = COALESCE(?, start_time),"
	            + "  end_time     = COALESCE(?, end_time)"
	            + " WHERE availability_id = ?";
	}

	@Override
	protected void fillEdit(PreparedStatement ps, Availability a) throws SQLException {
		TimeRange tr = a.getTimeRanges().get(0);
        ps.setInt(1, a.getEmployeeId());
        ps.setString(2, a.getDay().toString());
        ps.setString(3, tr.getStart().toString());
        ps.setString(4, tr.getEnd().toString());
        ps.setInt(5, a.getAvailabilityId());
	}

	@Override
	protected Availability toObject(ResultSet rs) throws SQLException {
		int availabilityId = rs.getInt("availability_id");
	    int employeeId = rs.getInt("employee_id");
	    DayOfWeek day = DayOfWeek.valueOf(rs.getString("day_of_week"));
	    LocalTime start = LocalTime.parse(rs.getString("start_time"));
	    LocalTime end = LocalTime.parse(rs.getString("end_time"));
	    Availability a = new Availability(day, employeeId);
	    a.setAvailabilityId(availabilityId);
	    a.addTimeRange(new TimeRange(start,end));
	    return a;
	}
	
	public List<Availability> employeeAvailabilities(int employeeId) {
        String sql = "SELECT * FROM " + tableName() + " WHERE employee_id = ?";
        try (Connection conn = conn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            List<Availability> list = new ArrayList<>();
            while (rs.next()) {
                list.add(toObject(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Query by employee_id failed", e);
        }
    }
}
