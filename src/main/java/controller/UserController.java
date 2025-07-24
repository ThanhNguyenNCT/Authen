package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Role;
import entity.Status;
import entity.Task;
import entity.User;
import helper.MD5Helper;
import helper.VietnameseHelper;
import service.RoleService;
import service.StatusService;
import service.TaskService;
import service.UserService;

@WebServlet(name = "UserController", urlPatterns = {"/user-table", "/user-add", "/user-edit", "/user-details", "/user-delete"
		,"/profile", "/profile-edit"})
public class UserController extends HttpServlet{
	private UserService userService = new UserService();
	private RoleService roleService = new RoleService();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		VietnameseHelper.setUTF8(req, resp);
		String servletPath = req.getServletPath();
		List<Role> listRoles = roleService.getAllRoles();
		switch (servletPath) {
			case "/user-table":
				List<User> listUsers = userService.getAllUsers();
				
				req.setAttribute("listRoles", listRoles);
				req.setAttribute("listUsers", listUsers);
				req.getRequestDispatcher("/user-table.jsp").forward(req, resp);
				break;
			case "/user-add":
	
				req.setAttribute("listRoles", listRoles);
				req.getRequestDispatcher("/user-add.jsp").forward(req, resp);
				break;
			case "/user-details":
				int userId = Integer.parseInt(req.getParameter("id"));
				User user = userService.getUserById(userId);
				List<Map<String, Object>> details = new UserService().getDetailsByUserId(userId);
				
				int totalTasks = details.stream()
						.mapToInt(detail -> (int) detail.get("total_task"))
						.sum();
				Map<String, Double> percentagesByStatus = details.stream()
						.collect(Collectors.toMap(
								detail -> (String) detail.get("status"),
								detail -> {
									int total = (int) detail.get("total_task");
									return totalTasks > 0 ? (total * 100.0) / totalTasks : 0.0;
								}
						));
				Map<String, Object> tasksByStatus = details.stream()
						.collect(Collectors.toMap(
								detail -> (String) detail.get("status"),
								detail -> ((List<Integer>) detail.get("task_ids")).stream()
										.map(taskId -> new TaskService().getTaskById(taskId))
										.collect(Collectors.toList())
						));
				req.setAttribute("percentagesByStatus", percentagesByStatus);
				req.setAttribute("tasksByStatus", tasksByStatus);
				req.setAttribute("user", user);
				req.getRequestDispatcher("/user-details.jsp").forward(req, resp);
				break;
			case "/profile":
				Cookie[] cookies = req.getCookies();
				User userProfile = userService.getUserByEmailPassword(cookies);
				
				List<Map<String, Object>> detailsProfile = new UserService().getDetailsByUserId(userProfile.getId());
				
				int countTotalTasks = detailsProfile.stream()
						.mapToInt(profile -> (int) profile.get("total_task"))
						.sum();
				Map<String, Double> percentageCompleted = detailsProfile.stream()
						.collect(Collectors.toMap(
								profile -> (String) profile.get("status"),
								profile -> {
									int total = (int) profile.get("total_task");
									return countTotalTasks > 0 ? (total * 100.0) / countTotalTasks : 0.0;
								}
						));
				List<Task> tasksProfile = new TaskService().getTasksByUserId(userProfile.getId());
				
				req.setAttribute("tasksProfile", tasksProfile);
				req.setAttribute("percentageCompleted", percentageCompleted);
				req.setAttribute("userProfile", userProfile);
				req.getRequestDispatcher("/profile.jsp").forward(req, resp);
				break;
			case "/profile-edit":
				int taskIdEdit = Integer.parseInt(req.getParameter("id_task"));
				Task task = new TaskService().getTaskById(taskIdEdit);
				List<Status> listStatuses = new StatusService().getAllStatuses();
				
				req.setAttribute("task", task);
				req.setAttribute("listStatuses", listStatuses);
				req.getRequestDispatcher("/profile-edit.jsp").forward(req, resp);
				break;
					
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String servletPath = req.getServletPath();
		VietnameseHelper.setUTF8(req, resp);
		switch (servletPath) {
			case "/user-add":
				String fullName = req.getParameter("fullname");
				String email = req.getParameter("email");
				String password = MD5Helper.getMd5(req.getParameter("password"));
				String phone = req.getParameter("phone");
				int roleId = Integer.parseInt(req.getParameter("id_role"));
				
				if (userService.insertUser(email, password, fullName, phone, roleId)) {
					resp.sendRedirect("user-table?success=true");
				} else {
					req.setAttribute("errorMessage", "Failed to add user");
					resp.sendRedirect("user-add?success=false");
				}
				
				break;
			case "/user-edit":
				String firstName = req.getParameter("first_name");
				String lastName = req.getParameter("last_name");
				String userName = req.getParameter("username");
				int roleIdEdit = Integer.parseInt(req.getParameter("id_role"));
				int id = Integer.parseInt(req.getParameter("id"));
				
				User user = userService.getUserById(id);
				if(userService.editUser(user, firstName, lastName, userName, roleIdEdit)) {
					resp.sendRedirect("user-table?success=true");
				} else {
					req.setAttribute("errorMessage", "Failed to edit user");
					resp.sendRedirect("user-table?success=false");
				}
				break;
			case "/user-delete":
				int userId = Integer.parseInt(req.getParameter("id"));
				if (userService.deleteUser(userId)) {
					resp.sendRedirect("user-table?success=true");
				} else {
					req.setAttribute("errorMessage", "Failed to delete user");
					resp.sendRedirect("user-table?success=false");
				}
				break;
			case "/profile-edit":
				int id_status = Integer.parseInt(req.getParameter("id_status"));
				int id_task = Integer.parseInt(req.getParameter("id_task"));
				if(new TaskService().setTaskStatus(id_task, id_status)) {
					resp.sendRedirect("profile?success=true");
				} else {
					req.setAttribute("errorMessage", "Failed to edit task status");
					resp.sendRedirect("profile?success=false");
				}
		}
	}
}
