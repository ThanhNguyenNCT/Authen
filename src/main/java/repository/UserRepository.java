package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import config.MySQLConnection;
import entity.User;

public class UserRepository {
	public List<User> getAllUsers() {
		List<User> listUsers = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM users";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getBoolean("isdeleted")) {
					continue;
				}
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setFirst_name(rs.getString("first_name"));
				user.setLast_name(rs.getString("last_name"));
				user.setPhone(rs.getString("phone"));
				user.setRoleId(rs.getInt("id_role"));
				user.setUsername(rs.getString("username"));
				listUsers.add(user);
			}
		} catch (SQLException e) {
			System.out.println("Error executing query: " + e.getMessage());
			e.printStackTrace();
		}
		return listUsers;
	}
	
	public int insertUser(User user) {
		int rs = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "INSERT INTO users (email, password, first_name, last_name, phone, id_role, username) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getFirst_name());
			ps.setString(4, user.getLast_name());
			ps.setString(5, user.getPhone());
			ps.setInt(6, user.getRoleId());
			ps.setString(7, user.getUsername());
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing insert query: " + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	public int editUser(User user) {
		int rs = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "UPDATE users SET first_name = ?, last_name = ?,id_role = ?, username = ? WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getFirst_name());
			ps.setString(2, user.getLast_name());
			ps.setInt(3, user.getRoleId());
			ps.setString(4, user.getUsername());
			ps.setInt(5, user.getId());
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing update query: " + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	public User getUserById(int id) {
		User user = null;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM users WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setFirst_name(rs.getString("first_name"));
				user.setLast_name(rs.getString("last_name"));
				user.setPhone(rs.getString("phone"));
				user.setRoleId(rs.getInt("id_role"));
				user.setUsername(rs.getString("username"));
			}
		} catch (SQLException e) {
			System.out.println("Error executing query: " + e.getMessage());
			e.printStackTrace();
		}
		return user;
	}
	public int deleteUser(int id) {
		int rs = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "UPDATE users SET isdeleted = true WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing delete query: " + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	public List<Map<String, Object>> getDetailsByUserId(int userId) {
		List<Map<String, Object>> detailsList = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT s.status_name AS status,\r\n"
					+ "       COUNT(*) AS total_task,\r\n"
					+ "       GROUP_CONCAT(t.id SEPARATOR ', ') AS task_id\r\n"
					+ "FROM tasks t\r\n"
					+ "JOIN status s ON t.id_status = s.id\r\n"
					+ "WHERE t.id_user = ?\r\n"
					+ "GROUP BY s.status_name;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String status = rs.getString("status");
				int totalTask = rs.getInt("total_task");
				String taskIdsStr = rs.getString("task_id");
				
				List<Integer> taskIds = Arrays.stream(taskIdsStr.split(","))
						.map(String::trim)
						.map(Integer::parseInt)
						.collect(Collectors.toList());
				
				Map<String, Object> details = new HashMap<>();
				details.put("status", status);
				details.put("total_task", totalTask);
				details.put("task_ids", taskIds);
				
				detailsList.add(details);
			}
		} catch (SQLException e) {
			System.out.println("Error executing query: " + e.getMessage());
			e.printStackTrace();
		}
		return detailsList;
	}
	
	public User getUserByEmailPassword(String email, String password) {
		User user = null;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM users WHERE email = ? AND password = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setFirst_name(rs.getString("first_name"));
				user.setLast_name(rs.getString("last_name"));
				user.setPhone(rs.getString("phone"));
				user.setRoleId(rs.getInt("id_role"));
				user.setUsername(rs.getString("username"));
			}
		} catch (SQLException e) {
			System.out.println("Error executing query: " + e.getMessage());
			e.printStackTrace();
		}
		return user;
	}
	
	public List<User> getListLeaders() {
		List<User> listLeaders = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM users WHERE id_role = 2 AND isdeleted = false";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setFirst_name(rs.getString("first_name"));
				user.setLast_name(rs.getString("last_name"));
				user.setPhone(rs.getString("phone"));
				user.setRoleId(rs.getInt("id_role"));
				user.setUsername(rs.getString("username"));
				listLeaders.add(user);
			}
		} catch (SQLException e) {
			System.out.println("Error executing query: " + e.getMessage());
			e.printStackTrace();
		}
		return listLeaders;
	}
	
	public List<User> getUsersByRoleName(String roleName) {
		List<User> listUsers = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT u.* FROM users u JOIN roles r ON u.id_role = r.id WHERE r.name = ? AND u.isdeleted = false";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, roleName);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setFirst_name(rs.getString("first_name"));
				user.setLast_name(rs.getString("last_name"));
				user.setPhone(rs.getString("phone"));
				user.setRoleId(rs.getInt("id_role"));
				user.setUsername(rs.getString("username"));
				listUsers.add(user);
			}
		} catch (SQLException e) {
			System.out.println("Error executing query: " + e.getMessage());
			e.printStackTrace();
		}
		return listUsers;
	}
	
}
