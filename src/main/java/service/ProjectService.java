package service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import entity.Project;
import repository.ProjectRepository;

public class ProjectService {
	public List<Project> getAllProjects() {
		List<Project> listProjects = new ProjectRepository().getAllProjects();
		if (listProjects != null && !listProjects.isEmpty()) {
			System.out.println("Projects found: " + listProjects.size());
		} else {
			System.out.println("No projects found.");
		}
		return listProjects;
	}
	
	public boolean insertProject(String name, LocalDate startDay, LocalDate endDay) {
		Project project = new Project();
		project.setName(name);
		project.setStartDay(startDay);
		project.setEndDay(endDay);
		
		int result = new ProjectRepository().insertProject(project);
		if (result > 0) {
			System.out.println("Project inserted successfully.");
			return true;
		} else {
			System.out.println("Failed to insert project.");
			return false;
		}
		
	}
	
	public Project getProjectById(int id) {
		Project project = new ProjectRepository().getProjectById(id);
		if (project != null) {
			System.out.println("Project found: " + project.getName());
			return project;
		} else {
			System.out.println("No project found with ID: " + id);
		}
		return null;
	}
	
	public boolean editProject(Project project, String name, LocalDate startDay, LocalDate endDay, int leaderId) {
		project.setManager(new UserService().getUserById(leaderId));
		project.setName(name);
		project.setStartDay(startDay);
		project.setEndDay(endDay);
		
		int result = new ProjectRepository().editProject(project);
		if (result > 0) {
			System.out.println("Project updated successfully.");
			return true;
		} else {
			System.out.println("Failed to update project.");
			return false;
		}
	}
	
	public boolean deleteProject(int id) {
		int result = new ProjectRepository().deleteProject(id);
		if (result > 0) {
			System.out.println("Project deleted successfully.");
			return true;
		} else {
			System.out.println("Failed to delete project.");
			return false;
		}
	}
	
	public List<Map<String, Object>> getProjectDetailsById(int id) {
		List<Map<String, Object>> details = new ProjectRepository().getDetailsByProjectId(id);
		if (details != null && !details.isEmpty()) {
			System.out.println("Details found for project ID: " + id);
			return details;
		} else {
			System.out.println("No details found for project ID: " + id);
			return null;
		}
	}
	
	public List<Project> getProjectsByUserId(int userId) {
		List<Project> projects = new ProjectRepository().getProjectsByUserId(userId);
		if (projects != null && !projects.isEmpty()) {
			System.out.println("Projects found for user ID: " + userId);
			return projects;
		} else {
			System.out.println("No projects found for user ID: " + userId);
			return null;
		}
	}
}
