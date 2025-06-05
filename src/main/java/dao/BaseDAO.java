package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import main.appFiles.databaseManagement.DbConnection;

/**
 * Generic DAO providing basic CRUD operations for a table.
 *
 * @param <T> domain object handled by the DAO
 */
public abstract class BaseDAO<T> {

        /**
         * Opens a connection to the database using {@link DbConnection}.
         *
         * @return an active JDBC {@link Connection}
         * @throws SQLException if the connection cannot be established
         */
        protected Connection conn() throws SQLException {
                return DbConnection.getConnection();
        }
	
	protected abstract String tableName();
	protected abstract String idName();
	protected abstract String insertStatment();
	protected abstract void fillInsert(PreparedStatement ps, T obj) throws SQLException;
	protected abstract String editStatement();
	protected abstract void fillEdit(PreparedStatement ps, T obj) throws SQLException;
	protected abstract T toObject(ResultSet rs) throws SQLException;
	
        /**
         * Insert a new record represented by the given object.
         *
         * @param obj the object to persist
         * @return the generated id or {@code -1} if none was returned
         */
        public int insert(T obj) {
		try (var conn = conn();
				var ps = conn.prepareStatement(insertStatment(), Statement.RETURN_GENERATED_KEYS)){
			fillInsert(ps, obj);
			ps.executeUpdate();
			var id = ps.getGeneratedKeys();
			return id.next() ? id.getInt(1) : -1;
		}catch (SQLException e) {
			throw new RuntimeException("Insert failed", e);
		}
	}
	
        /**
         * Read all rows from the table and convert them into objects.
         *
         * @return list of all objects stored in the table
         */
        public List<T> readTable(){
		String sql = "SELECT * FROM " + tableName();
		try (var conn = conn();
				var stmt = conn.createStatement();
				var rs = stmt.executeQuery(sql)){
			var list = new ArrayList<T>();
			while (rs.next()) {
				list.add(toObject(rs));
			}
			return list;
		}catch (SQLException e) {
			throw new RuntimeException("Table read failed", e);
		}
	}
	
        /**
         * Retrieve a single record by its primary key.
         *
         * @param id identifier of the record
         * @return the matching object or {@code null} if not found
         */
        public T idFetch(int id) {
		String sql = "SELECT * FROM " + tableName() + " WHERE " + idName() + " = ?";
		try (var conn = conn();
				var ps = conn.prepareStatement(sql)){
			ps.setInt(1, id);
			var rs = ps.executeQuery();
			return rs.next() ? toObject(rs) : null;
		}catch (SQLException e) {
			throw new RuntimeException("Query by id failed", e);
		}
	}
	
        /**
         * Update an existing record with the values from the given object.
         *
         * @param obj the object containing updated values
         * @return number of affected rows
         */
        public int edit(T obj) {
		try (var conn = conn();
				var ps = conn.prepareStatement(editStatement())){
			fillEdit(ps, obj);
			return ps.executeUpdate();
		}catch (SQLException e) {
			throw new RuntimeException("Edit failed");
		}
	}
	
        /**
         * Delete the record matching the provided id.
         *
         * @param id identifier of the record to delete
         * @return number of rows removed
         */
        public int delete(int id) {
		String sql = "DELETE FROM " + tableName() + " WHERE " + idName() + " = ?";
		try (var conn = conn();
				var ps = conn.prepareStatement(sql)){
			ps.setInt(1,  id);
			return ps.executeUpdate();
		}catch (SQLException e) {
			throw new RuntimeException("Delete failed", e);
		}
	}
	
        /**
         * Count how many rows exist in the table.
         *
         * @return the total number of records
         */
        public long count() {
		String sql = "SELECT COUNT(*) FROM " + tableName();
		try (var conn = conn();
				var stmt = conn.createStatement();
				var rs = stmt.executeQuery(sql)){
			rs.next();
			return rs.getLong(1);
		}catch (SQLException e) {
			throw new RuntimeException("Count failed", e);
		}
	}
	
        /**
         * Check whether a record with the given id exists.
         *
         * @param id the identifier to search for
         * @return {@code true} if a matching row is found
         */
        public boolean exists(int id) {
		String sql = "SELECT 1 FROM " + tableName() + " WHERE " + idName() + " = ?";
		try (var conn = conn();
				var ps = conn.prepareStatement(sql)){
			ps.setInt(1, id);
			try (var rs = ps.executeQuery()){
				return rs.next();
			}
		}catch (SQLException e) {
			throw new RuntimeException("Existence check failed", e);
		}
	}
}