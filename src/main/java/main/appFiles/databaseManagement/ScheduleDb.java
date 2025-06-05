package main.appFiles.databaseManagement;

import java.sql.*;
import java.util.List;

import main.appFiles.scheduleAlgorithm.Shift;
import main.appFiles.schedulingData.Schedule;

/**
 * Utility responsible for creating schedule tables and persisting generated
 * {@link Shift} assignments.
 */
public class ScheduleDb {
	
	private static final String SCHEDULE_TEMPLATE =
	        "CREATE TABLE %s ("
	      + "shift_id    INTEGER PRIMARY KEY AUTOINCREMENT, "
	      + "employee_id TEXT    NOT NULL, "
	      + "day_of_week TEXT    NOT NULL, "
	      + "start_time  TIME    NOT NULL, "
	      + "end_time    TIME    NOT NULL"
	      + ")";
	    private static final String INSERT_TEMPLATE =
	        "INSERT INTO %s (employee_id, day_of_week, start_time, end_time) "
	      + "VALUES (?, ?, ?, ?)";

            /**
             * Persist the given schedule in a newly created table.
             *
             * @param schedule schedule to store
             * @return the name of the created table
             * @throws SQLException if database errors occur during persistence
             */
            public static String persistSchedule(Schedule schedule) throws SQLException {
	        String tableName = "schedule_" + System.currentTimeMillis();
	        String scheduleTable = String.format(SCHEDULE_TEMPLATE, tableName);
	        try (Connection conn = DbConnection.getConnection();
	             Statement stmt = conn.createStatement()) {
	            stmt.execute(scheduleTable);
	        }
	        String insertSql = String.format(INSERT_TEMPLATE, tableName);
	        try (Connection conn = DbConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(insertSql)) {
	            conn.setAutoCommit(false);
	            try {
	                for (Shift s : schedule.getShifts()) {
	                    ps.setInt(1, s.getEmployeeId());
	                    ps.setString(2, s.getDay().toString());
	                    ps.setTime(3, java.sql.Time.valueOf(s.getStart()));
	                    ps.setTime(4, java.sql.Time.valueOf(s.getEnd()));
	                    ps.addBatch();
	                }
	                ps.executeBatch();
	                conn.commit();
	            } catch (SQLException ex) {
	                conn.rollback();
	                throw ex;
	            } finally {
	                conn.setAutoCommit(true);
	            }
	        }

	        return tableName;
	    }
}
