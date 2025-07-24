package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Role;
import helper.VietnameseHelper;
import service.RoleService;
@WebServlet(name = "RoleController", urlPatterns = {"/role-table", "/role-add", "/role-edit", "/role-delete"})
public class RoleController extends HttpServlet{
	private RoleService roleService = new RoleService();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		VietnameseHelper.setUTF8(req, resp);
		String servletPath = req.getServletPath();
		switch (servletPath) {
			case "/role-table":
				List<Role> listRoles = roleService.getAllRoles();
				
				req.setAttribute("listRoles", listRoles);
				req.getRequestDispatcher("/role-table.jsp").forward(req, resp);
				break;
			case "/role-add":
				req.getRequestDispatcher("/role-add.jsp").forward(req, resp);
				break;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		VietnameseHelper.setUTF8(req, resp);
		String servletPath = req.getServletPath();
		switch (servletPath) {
			case "/role-add":
				String name = req.getParameter("name");
				String description = req.getParameter("description");
				if (roleService.addRole(name, description)) {
					resp.sendRedirect("role-table?success=true");
				} else {
					resp.sendRedirect("role-table?success=false");
				}
				break;
			case "/role-edit":
				String nameEdit = req.getParameter("name");
				String descriptionEdit = req.getParameter("description");
				int idEdit = Integer.parseInt(req.getParameter("id"));
				
				Role role = roleService.getRoleById(idEdit);
				if (roleService.editRole(role, nameEdit, descriptionEdit)) {
					resp.sendRedirect("role-table?success=true");
				} else {
					resp.sendRedirect("role-edit?success=false");
				}
				break;
			case "/role-delete":
				int idDelete = Integer.parseInt(req.getParameter("id"));
				if (roleService.deleteRole(idDelete)) {
					resp.sendRedirect("role-table?success=true");
				} else {
					resp.sendRedirect("role-table?success=false");
				}
				break;
		}
	}

}
