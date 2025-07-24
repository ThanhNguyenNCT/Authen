package repository;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.MySQLConnection;
import entity.User;
import service.RoleService;

public class LoginRepository {
	public List<User> getValidUser(String email, String password) throws ConnectException {
		List<User> listUsers = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();){
			String query = "SELECT * FROM users WHERE email = ? AND password = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
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
				listUsers.add(user);
			}
		} catch (SQLException e) {
			System.out.println("Error executing query: " + e.getMessage());
			e.printStackTrace();
		} 
		return listUsers;
	}
	
	public Map<Integer, String> getIdAndRoleNameByEmailandPassword(String email, String password) {
	    Map<Integer, String> result = new HashMap<>();
	    try (Connection conn = MySQLConnection.getConnection()) {
	        String query = "SELECT users.id AS id, roles.name AS role_name " +
	                       "FROM users JOIN roles ON users.id_role = roles.id " +
	                       "WHERE email = ? AND password = ?";
	        PreparedStatement ps = conn.prepareStatement(query);
	        ps.setString(1, email);
	        ps.setString(2, password);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            int id = rs.getInt("id");
	            String roleName = rs.getString("role_name");
	            result.put(id, roleName);
	        }
	    } catch (SQLException e) {
	        System.err.println("Error executing query: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return result;
	}

}
