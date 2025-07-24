package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.MySQLConnection;
import entity.Role;

public class RoleRepository {
	public String getRoleNameById(int roleId) {
		String roleName = "";
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT name FROM roles WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, roleId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				roleName = rs.getString("name");
			}
		} catch (SQLException e) {
			System.out.println("Error executing query in function getRoleNameById: " + e.getMessage());
			e.printStackTrace();
		}
		return roleName;
	}
	
	public List<Role> getAllRoles() {
		List<Role> listRoles = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM roles";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getBoolean("isdeleted")) {
					continue; // Skip deleted roles
				}
				Role role = new Role();
				role.setId(rs.getInt("id"));
				role.setName(rs.getString("name"));
				role.setDescription(rs.getString("description"));
				listRoles.add(role);
			}
		} catch (SQLException e) {
			System.out.println("Error executing query in function getAllRoles: " + e.getMessage());
			e.printStackTrace();
		}
		return listRoles;
	}
	
	public int addRole(Role role) {
		int rs = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "INSERT INTO roles (name, description) VALUES (?, ?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, role.getName());
			ps.setString(2, role.getDescription());
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing query in function addRole: " + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	public int editRole(Role role) {
		int rs = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "UPDATE roles SET name = ?, description = ? WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, role.getName());
			ps.setString(2, role.getDescription());
			ps.setInt(3, role.getId());
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing query in function editRole: " + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	public Role getRoleById(int id) {
		Role role = null;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM roles WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				role = new Role();
				role.setId(rs.getInt("id"));
				role.setName(rs.getString("name"));
				role.setDescription(rs.getString("description"));
			}
		} catch (SQLException e) {
			System.out.println("Error executing query in function getRoleById: " + e.getMessage());
			e.printStackTrace();
		}
		return role;
	}
	public int deleteRole(int id) {
		int rs = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "UPDATE roles SET isdeleted = true WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing query in function deleteRole: " + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
}
