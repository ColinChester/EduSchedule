package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import main.appFiles.databaseManagement.DbConnection;

public abstract class BaseDAO<T> {
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
	
	public int edit(T obj) {
		try (var conn = conn();
				var ps = conn.prepareStatement(editStatement())){
			fillEdit(ps, obj);
			return ps.executeUpdate();
		}catch (SQLException e) {
			throw new RuntimeException("Edit failed");
		}
	}
	
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