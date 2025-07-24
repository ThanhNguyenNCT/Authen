package filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Task;
import service.ProjectService;
import service.TaskService;

@WebFilter(filterName =  "AuthenticationFilter", urlPatterns = {
		"/groupwork", "/groupwork-add", "/groupwork-edit", "/groupwork-delete", "/groupwork-details",
		"/role-table", "/role-add", "/role-edit", "/role-delete",
		"/task", "/task-add", "/task-edit", "/task-details", "/task-delete",
		"/user-table", "/user-add", "/user-edit", "/user-details", "/user-delete","/profile", "/profile-edit"})
public class AuthenticationFilter extends HttpFilter{

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		Cookie cookies[] = req.getCookies();
		String roleName = "";
		String userId = "";
		
		if (cookies.length > 1) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("roleName")) {
					roleName = cookie.getValue();
				}
				if (cookie.getName().equals("userId")) {
					userId = cookie.getValue();
				}
			}
		} else {
			res.sendRedirect("index?success=false&reason=not_logged_in");
			return;
		}
		boolean isAllowed = false;
		String servletPath = req.getServletPath();
		switch (roleName) {
			case "ROLE_ADMIN":
				isAllowed = true;
				break;
			case "ROLE_USER":
				if(servletPath.equals("/profile") || servletPath.equals("/profile-edit") || servletPath.equals("/task")
						|| servletPath.equals("/user-table")) {
					isAllowed = true;
				} else if(servletPath.equals("/task-details")) {
					int taskId = Integer.parseInt(req.getParameter("id"));
					Task task = new TaskService().getTaskById(taskId);
					int taskUserId = task.getUser().getId();
					if (taskUserId == Integer.parseInt(userId)) {
						isAllowed = true;
					}
				}
				
				break;
			case "ROLE_LEAD":
				if(servletPath.equals("/profile") || servletPath.equals("/profile-edit")
						|| servletPath.equals("/groupwork") 
						|| servletPath.equals("/task") || servletPath.equals("/task-add")
						|| servletPath.equals("/task-details")
						|| servletPath.equals("/user-table") || servletPath.equals("/user-details")) {
					isAllowed = true;						
				} else if(servletPath.equals("/task-edit") || servletPath.equals("/task-delete")) {
					int taskId = Integer.parseInt(req.getParameter("id"));
					Task task = new TaskService().getTaskById(taskId);
					int taskManagerId = task.getProject().getManager().getId();
					if (taskManagerId == Integer.parseInt(userId)) {
						isAllowed = true;
					}			
				} else if(servletPath.equals("/groupwork-details")) {
					int groupworkId = Integer.parseInt(req.getParameter("id"));
					int groupworkManagerId = new ProjectService().getProjectById(groupworkId).getManager().getId();
					if (groupworkManagerId == Integer.parseInt(userId)) {
						isAllowed = true;
					}
				}
				break;
		}

		
		if (isAllowed) {
			chain.doFilter(req, res);
		} else {
			res.sendRedirect("index?success=false&reason=no_permission");
		}
	}
}
