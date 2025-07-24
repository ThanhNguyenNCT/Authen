package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Project;
import entity.Task;
import entity.User;
import helper.VietnameseHelper;
import service.ProjectService;
import service.TaskService;
import service.UserService;

@WebServlet(name = "ProjectController", urlPatterns = {"/groupwork", "/groupwork-add", "/groupwork-edit", "/groupwork-delete", 
		"/groupwork-details"})
public class ProjectController extends HttpServlet{
	private ProjectService projectService = new ProjectService();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String servletPath = req.getServletPath();
		VietnameseHelper.setUTF8(req, resp);
		switch (servletPath) {
			case "/groupwork":
				List<Project> listProjects = projectService.getAllProjects();
				List<User> listLeaders = new UserService().getListLeaders();
				
				req.setAttribute("listLeaders", listLeaders);
				req.setAttribute("listProjects", listProjects);
				req.getRequestDispatcher("/groupwork.jsp").forward(req, resp);
				break;
			case "/groupwork-add":
				req.getRequestDispatcher("/groupwork-add.jsp").forward(req, resp);
				break;
			case "/groupwork-details":
				// Logic to handle project details
				int projectId = Integer.parseInt(req.getParameter("id"));
				Project project = projectService.getProjectById(projectId);
				List<Map<String, Object>> details = projectService.getProjectDetailsById(projectId);
				
				int totalTasks = details.stream()
						.mapToInt(detail -> (int) detail.get("total_task"))
						.sum();
				Map<String, Double> percentagesByStatus = details.stream()
						.collect(Collectors.toMap(
								detail -> (String) detail.get("status"),
								detail -> {
									int total = (int) detail.get("total_task");
									return total > 0 ? (double) total / totalTasks * 100 : 0.0;
								}
						));
				Map<String, List<Task>> tasksByStatus = details.stream()
					    .collect(Collectors.toMap(
					        detail -> (String) detail.get("status"),
					        detail -> {
					            @SuppressWarnings("unchecked")
								List<Integer> taskIds = (List<Integer>) detail.get("task_ids");
					            return taskIds.stream()
					                .map(taskId -> new TaskService().getTaskById(taskId))
					                .collect(Collectors.toList());
					        }
					    ));
				Map<User, Map<String, List<Task>>> tasksGroupByUserAndStatus = tasksByStatus.values().stream()
					    .flatMap(List::stream)
					    .filter(task -> task.getUser() != null)
					    .collect(Collectors.groupingBy(
					        Task::getUser,
					        Collectors.groupingBy(Task::getStatus)
					    ));
				req.setAttribute("tasksGrouped", tasksGroupByUserAndStatus);
				req.setAttribute("project", project);
				req.setAttribute("percentagesByStatus", percentagesByStatus);
				req.getRequestDispatcher("/groupwork-details.jsp").forward(req, resp);
				break;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String servletPath = req.getServletPath();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		VietnameseHelper.setUTF8(req, resp);
		switch (servletPath) {
			case "/groupwork-add":
				String name = req.getParameter("name");
				String startDayStr = req.getParameter("start_day");
				String endDayStr = req.getParameter("end_day");
				
				LocalDate startDay = LocalDate.parse(startDayStr, formatter);
				LocalDate endDay = LocalDate.parse(endDayStr, formatter);
				
				if (projectService.insertProject(name, startDay, endDay)) {
					resp.sendRedirect("groupwork?success=true");
				} else {
					resp.sendRedirect("groupwork?success=false");
				}
				break;
			case "/groupwork-edit":
				// Logic to handle project editing
				int projectEditId = Integer.parseInt(req.getParameter("id"));
				int leaderId = Integer.parseInt(req.getParameter("id_leader"));
				String nameEdit = req.getParameter("name");
				String startDayEditStr = req.getParameter("start_day");
				String endDayEditStr = req.getParameter("end_day");
				
				LocalDate startDayEdit = LocalDate.parse(startDayEditStr, formatter);
				LocalDate endDayEdit = LocalDate.parse(endDayEditStr, formatter);
				
				Project project = projectService.getProjectById(projectEditId);
				if(projectService.editProject(project, nameEdit, startDayEdit, endDayEdit, leaderId)) {
					resp.sendRedirect("groupwork?success=true");
				} else {
					resp.sendRedirect("groupwork?success=false");
				}
				break;
			case "/groupwork-delete":
				int projectDeleteId = Integer.parseInt(req.getParameter("id"));
				if (projectService.deleteProject(projectDeleteId)) {
					resp.sendRedirect("groupwork?success=true");
				} else {
					resp.sendRedirect("groupwork?success=false");
				}
				break;
		}
	}
}
