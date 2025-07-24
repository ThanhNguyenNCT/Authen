package service;

import java.time.LocalDate;
import java.util.List;

import entity.Task;
import entity.User;
import repository.TaskRepository;

public class TaskService {
	public List<Task> getAllTasks() {
		List<Task> listTasks = new TaskRepository().getAllTasks();
		if (listTasks != null && !listTasks.isEmpty()) {
			System.out.println("Tasks found: " + listTasks.size());
		} else {
			System.out.println("No tasks found.");
		}
		return listTasks;
	}
	
	public boolean insertTask(String name_task, LocalDate start_task, LocalDate end_task, int id_project, int id_user) {
		Task task = new Task();
		task.setName_task(name_task);
		task.setStart_task(start_task);
		task.setEnd_task(end_task);
		
		task.setUser(new UserService().getUserById(id_user));
		task.setProject(new ProjectService().getProjectById(id_project));
		
		int result = new TaskRepository().insertTask(task);
		if (result > 0) {
			System.out.println("Task inserted successfully.");
			return true;
		} else {
			System.out.println("Failed to insert task.");
			return false;
		}
	}
	
	public Task getTaskById(int id) {
		Task task = new TaskRepository().getTaskById(id);
		if (task != null) {
			System.out.println("Task found: " + task.getName_task());
		} else {
			System.out.println("No task found with ID: " + id);
		}
		return task;
	}
	
	public boolean editTask(Task task, String name_task, LocalDate start_task, LocalDate end_task, int id_project, int id_user) {
		task.setName_task(name_task);
		task.setStart_task(start_task);
		task.setEnd_task(end_task);
		
		task.setUser(new UserService().getUserById(id_user));
		task.setProject(new ProjectService().getProjectById(id_project));
		
		int result = new TaskRepository().editTask(task);
		if (result > 0) {
			System.out.println("Task edited successfully.");
			return true;
		} else {
			System.out.println("Failed to edit task.");
			return false;
		}

	}
	public boolean deleteTask(int id) {
		int result = new TaskRepository().deleteTask(id);
		if (result > 0) {
			System.out.println("Task deleted successfully.");
			return true;
		} else {
			System.out.println("Failed to delete task.");
			return false;
		}
	}
	
	public List<Task> getTasksByUserId(int userId) {
		List<Task> listTasks = new TaskRepository().getTasksByUserId(userId);
		if (listTasks != null && !listTasks.isEmpty()) {
			System.out.println("Tasks found for user ID: " + userId);
		} else {
			System.out.println("No tasks found for user ID: " + userId);
		}
		return listTasks;
	}
	
	public boolean setTaskStatus(int taskId, int statusId) {
		int result = new TaskRepository().setTaskStatus(taskId, statusId);
		if (result > 0) {
			System.out.println("Task status updated successfully.");
			return true;
		} else {
			System.out.println("Failed to update task status.");
			return false;
		}
	}
}
