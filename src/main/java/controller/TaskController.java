package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Project;
import entity.Status;
import entity.Task;
import entity.User;
import helper.VietnameseHelper;
import service.ProjectService;
import service.StatusService;
import service.TaskService;
import service.UserService;

@WebServlet(name = "TaskController", urlPatterns = {"/task", "/task-add", "/task-edit", "/task-details", "/task-delete"})
public class TaskController extends HttpServlet{
	private TaskService taskService = new TaskService();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		VietnameseHelper.setUTF8(req, resp);
		String servletPath = req.getServletPath();
		List<User> listUsers = new UserService().getAllUsers();
		List<Project> listProjects = new ProjectService().getAllProjects();
		switch (servletPath) {
			case "/task":
				List<Task> listTasks = taskService.getAllTasks();
				List<Status> listStatuses = new StatusService().getAllStatuses();

				req.setAttribute("listUsers", listUsers);
				req.setAttribute("listProjects", listProjects);
				req.setAttribute("listStatuses", listStatuses);
				req.setAttribute("listTasks", listTasks);
				req.getRequestDispatcher("/task.jsp").forward(req, resp);
				break;
			case "/task-add":
				Cookie[] cookies = req.getCookies();
				String roleName = "";
				String userId = "";
				if (cookies != null) {
					for (Cookie cookie : cookies) {
						if (cookie.getName().equals("roleName")) {
							roleName = cookie.getValue();
						}
						if (cookie.getName().equals("userId")) {
							userId = cookie.getValue();
						}
					}
				}
				if (roleName.equals("ROLE_ADMIN") ) {
					req.setAttribute("listProjects", listProjects);
					req.setAttribute("listUsers", listUsers);
				} else if (roleName.equals("ROLE_LEAD")) {
					List<Project> projects = new ProjectService().getProjectsByUserId(Integer.parseInt(userId));
					List<User> users = new UserService().getUsersByRoleName("ROLE_USER");
					req.setAttribute("listProjects", projects);
					req.setAttribute("listUsers", users);
				}
				
				req.getRequestDispatcher("/task-add.jsp").forward(req, resp);
				break;
			case "/task-details":
				// Logic to handle task details
				int taskId = Integer.parseInt(req.getParameter("id"));
				Task task = taskService.getTaskById(taskId);

				req.setAttribute("task", task);
				req.getRequestDispatcher("/task-details.jsp").forward(req, resp);
				break;
			

		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String servletPath = req.getServletPath();
		VietnameseHelper.setUTF8(req, resp);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		switch (servletPath) {
			case "/task-add":
				int projectId = Integer.parseInt(req.getParameter("id_project"));
				String name_task = req.getParameter("name_task");
				int userId = Integer.parseInt(req.getParameter("id_user"));
				
				String startDayStr = req.getParameter("start_task");
				String endDayStr = req.getParameter("end_task");
				LocalDate startDay = LocalDate.parse(startDayStr, formatter);
				LocalDate endDay = LocalDate.parse(endDayStr, formatter);
				
				if (taskService.insertTask(name_task, startDay, endDay, projectId, userId)) {
					resp.sendRedirect("task?success=true");
				} else {
					resp.sendRedirect("task?success=false");
				}
				break;
			case "/task-edit":
				int taskEditId = Integer.parseInt(req.getParameter("id"));
				
				String name_taskEdit = req.getParameter("name_task");
				int projectEditId = Integer.parseInt(req.getParameter("id_project"));
				int userEditId = Integer.parseInt(req.getParameter("id_user"));
				
				String startDayEditStr = req.getParameter("start_task");
				String endDayEditStr = req.getParameter("end_task");
				LocalDate startDayEdit = LocalDate.parse(startDayEditStr, formatter);
				LocalDate endDayEdit = LocalDate.parse(endDayEditStr, formatter);
				
				Task task = taskService.getTaskById(taskEditId);
				
				if (taskService.editTask(task, name_taskEdit, startDayEdit, endDayEdit, projectEditId, userEditId)) {
					resp.sendRedirect("task?success=true");
				} else {
					resp.sendRedirect("task?success=false");
				}
				
				break;
			case "/task-delete":
				int taskDeleteId = Integer.parseInt(req.getParameter("id"));
				if (taskService.deleteTask(taskDeleteId)) {
					resp.sendRedirect("task?success=true");
				} else {
					resp.sendRedirect("task?success=false");
				}	
				break;
		}
	}
}
