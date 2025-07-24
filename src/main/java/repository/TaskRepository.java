package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.MySQLConnection;
import entity.Task;

public class TaskRepository {
	public List<Task> getAllTasks() {
		List<Task> listTasks = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM tasks t JOIN status s ON t.id_status = s.id";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getBoolean("isdeleted")) {
					continue; // Skip deleted tasks
				}
				Task task = new Task();
				task.setId(rs.getInt("id"));
				task.setName_task(rs.getString("name_task"));
				task.setStart_task(rs.getDate("start_task").toLocalDate());
				task.setEnd_task(rs.getDate("end_task").toLocalDate());
				
				int projectId = rs.getInt("id_project");
				int userId = rs.getInt("id_user");
				task.setProject(new ProjectRepository().getProjectById(projectId));
				task.setUser(new UserRepository().getUserById(userId));
				
				task.setStatus(rs.getString("status_name"));
				listTasks.add(task);
			}
		} catch (SQLException e) {
			System.out.println("Error executing query in function getAllTasks: " + e.getMessage());
			e.printStackTrace();
		}
		return listTasks;
	}
	
	public int insertTask(Task task) {
		int result = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "INSERT INTO tasks (name_task, start_task, end_task, id_project, id_user) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, task.getName_task());
			ps.setDate(2, java.sql.Date.valueOf(task.getStart_task()));
			ps.setDate(3, java.sql.Date.valueOf(task.getEnd_task()));
			ps.setInt(4, task.getProject().getId());
			ps.setInt(5, task.getUser().getId());
			
			result = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing query in function insertTask: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public int editTask(Task task) {
		int result = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "UPDATE tasks SET name_task = ?, start_task = ?, end_task = ?, id_project = ?, id_user = ? WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, task.getName_task());
			ps.setDate(2, java.sql.Date.valueOf(task.getStart_task()));
			ps.setDate(3, java.sql.Date.valueOf(task.getEnd_task()));
			ps.setInt(4, task.getProject().getId());
			ps.setInt(5, task.getUser().getId());
			ps.setInt(6, task.getId());
			
			result = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing query in function editTask: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public Task getTaskById(int id) {
		Task task = null;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM tasks t JOIN status s ON t.id_status = s.id WHERE t.id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				task = new Task();
				task.setId(rs.getInt("id"));
				task.setName_task(rs.getString("name_task"));
				task.setStart_task(rs.getDate("start_task").toLocalDate());
				task.setEnd_task(rs.getDate("end_task").toLocalDate());
				
				int projectId = rs.getInt("id_project");
				int userId = rs.getInt("id_user");
				task.setProject(new ProjectRepository().getProjectById(projectId));
				task.setUser(new UserRepository().getUserById(userId));
				
				task.setStatus(rs.getString("status_name"));
			}
		} catch (SQLException e) {
			System.out.println("Error executing query in function getTaskById: " + e.getMessage());
			e.printStackTrace();
		}
		return task;
	}
	
	public int deleteTask(int id) {
		int result = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "UPDATE tasks SET isdeleted = true WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing query in function deleteTask: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	public List<Task> getTasksByUserId(int userId) {
		List<Task> listTasks = new ArrayList<>();
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "SELECT * FROM tasks t JOIN status s ON t.id_status = s.id WHERE t.id_user = ? AND t.isdeleted = false";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Task task = new Task();
				task.setId(rs.getInt("id"));
				task.setName_task(rs.getString("name_task"));
				task.setStart_task(rs.getDate("start_task").toLocalDate());
				task.setEnd_task(rs.getDate("end_task").toLocalDate());
				
				int projectId = rs.getInt("id_project");
				task.setProject(new ProjectRepository().getProjectById(projectId));
				
				task.setStatus(rs.getString("status_name"));
				listTasks.add(task);
			}
		} catch (SQLException e) {
			System.out.println("Error executing query in function getTasksByUserId: " + e.getMessage());
			e.printStackTrace();
		}
		return listTasks;
	}
	
	public int setTaskStatus(int taskId, int statusId) {
		int result = 0;
		try(Connection conn = MySQLConnection.getConnection();) {
			String query = "UPDATE tasks SET id_status = ? WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, statusId);
			ps.setInt(2, taskId);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error executing query in function setTaskStatus: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
