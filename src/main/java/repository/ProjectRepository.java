package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import config.MySQLConnection;
import entity.Project;
import service.UserService;

public class ProjectRepository {
	public List<Project> getAllProjects() {
		List<Project> listProjects = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();){
			String query = "SELECT * FROM projects";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getBoolean("isdeleted")) {
					continue; // Skip deleted projects
				}
				Project project = new Project();
				project.setId(rs.getInt("id"));
				project.setName(rs.getString("name"));
				project.setStartDay(rs.getDate("start_day").toLocalDate());
				project.setEndDay(rs.getDate("end_day").toLocalDate());
				project.setManager(new UserService().getUserById(rs.getInt("id_manager")));
				listProjects.add(project);
			}
			
		} catch (SQLException e) {
			System.out.println("Error executing query in function getAllProjects: " + e.getMessage());
			e.printStackTrace();
		}
		return listProjects;
	}
	
	public int insertProject(Project project) {
		int rs = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "INSERT INTO projects (name, start_day, end_day) VALUES (?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, project.getName());
			ps.setDate(2, java.sql.Date.valueOf(project.getStartDay()));
			ps.setDate(3, java.sql.Date.valueOf(project.getEndDay()));
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing insert query in function insertProject: " + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	public int editProject(Project project) {
		int rs = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "UPDATE projects SET name = ?, start_day = ?, end_day = ?, id_user = ? WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, project.getName());
			ps.setDate(2, java.sql.Date.valueOf(project.getStartDay()));
			ps.setDate(3, java.sql.Date.valueOf(project.getEndDay()));
			ps.setInt(4, project.getManager().getId()); 
			ps.setInt(5, project.getId());
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing update query in function editProject: " + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	public Project getProjectById(int id) {
		Project project = null;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM projects WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				project = new Project();
				project.setId(rs.getInt("id"));
				project.setName(rs.getString("name"));
				project.setStartDay(rs.getDate("start_day").toLocalDate());
				project.setEndDay(rs.getDate("end_day").toLocalDate());
				project.setManager(new UserService().getUserById(rs.getInt("id_manager")));
			}
		} catch (SQLException e) {
			System.out.println("Error executing query in function getProjectById: " + e.getMessage());
			e.printStackTrace();
		}
		return project;
	}
	
	public int deleteProject(int id) {
		int rs = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "UPDATE projects SET isdeleted = true WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing delete query in function deleteProject: " + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	public List<Map<String, Object>> getDetailsByProjectId(int id) {
		List<Map<String, Object>> detailsList = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT s.status_name AS status,\r\n"
					+ "       COUNT(*) AS total_task,\r\n"
					+ "       GROUP_CONCAT(t.id SEPARATOR ', ') AS task_names\r\n"
					+ "FROM tasks t\r\n"
					+ "JOIN status s ON t.id_status = s.id\r\n"
					+ "WHERE t.id_project = ?\r\n"
					+ "GROUP BY s.status_name;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				List<Integer> taskIds = Arrays.stream(rs.getString("task_names").split(","))
						.map(String::trim)
						.map(Integer::parseInt)
						.toList();
				Map<String, Object> detail = Map.of(
						"status", rs.getString("status"),
						"total_task", rs.getInt("total_task"),
						"task_ids", taskIds
				);
				detailsList.add(detail);
			}
		} catch (SQLException e) {
			System.out.println("Error executing query in function getDetailsByProjectId: " + e.getMessage());
			e.printStackTrace();
		}
		return detailsList;
	}
	
	public List<Project> getProjectsByUserId(int userId) {
		List<Project> listProjects = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT p.* FROM projects p JOIN users u ON p.id_manager = u.id WHERE u.id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getBoolean("isdeleted")) {
					continue; // Skip deleted projects
				}
				Project project = new Project();
				project.setId(rs.getInt("id"));
				project.setName(rs.getString("name"));
				project.setStartDay(rs.getDate("start_day").toLocalDate());
				project.setEndDay(rs.getDate("end_day").toLocalDate());
				project.setManager(new UserService().getUserById(rs.getInt("id_manager")));
				listProjects.add(project);
			}
		} catch (SQLException e) {
			System.out.println("Error executing query in function getProjectsByUserId: " + e.getMessage());
			e.printStackTrace();
		}
		return listProjects;
	}
}
