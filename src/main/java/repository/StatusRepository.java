package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.MySQLConnection;
import entity.Status;

public class StatusRepository {
	public List<Status> getAllStatuses() {
		List<Status> listStatuses = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM status";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Status status = new Status();
				status.setId(rs.getInt("id"));
				status.setName(rs.getString("status_name"));
				listStatuses.add(status);
			}
		} catch (SQLException e) {
			System.out.println("Error executing query in function getAllStatuses: " + e.getMessage());
			e.printStackTrace();
		}
		return listStatuses;
	}
	
	public Status getStatusById(int id) {
		Status status = null;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM status WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				status = new Status();
				status.setId(rs.getInt("id"));
				status.setName(rs.getString("status_name"));
			}
		} catch (SQLException e) {
			System.out.println("Error executing query in function getStatusById: " + e.getMessage());
			e.printStackTrace();
		}
		return status;
	}
}
