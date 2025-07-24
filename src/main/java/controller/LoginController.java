package controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helper.MD5Helper;
import helper.VietnameseHelper;
import service.LoginService;
@WebServlet("/login")
public class LoginController extends HttpServlet{
	private LoginService loginService = new LoginService();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		VietnameseHelper.setUTF8(req, resp);
		
		req.getRequestDispatcher("/login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		VietnameseHelper.setUTF8(req, resp);
		String email = req.getParameter("email");
		String password = MD5Helper.getMd5(req.getParameter("password"));
		Map<Integer, String> userInfo = loginService.getIdAndRoleNameByEmailandPassword(email, password);
		int userId = userInfo.keySet().iterator().next();
	    String roleName = userInfo.get(userId);
		
		
		if(loginService.validUser(email, password)) {
			Cookie EmailUserCookie = new Cookie("email", email);
			EmailUserCookie.setMaxAge(60 * 60 * 24); 
			resp.addCookie(EmailUserCookie);
			
			Cookie PasswordUserCookie = new Cookie("password", password);
			PasswordUserCookie.setMaxAge(60 * 60 * 24); 
			resp.addCookie(PasswordUserCookie);
			
			
			Cookie RoleNameCookie = new Cookie("roleName", roleName);
			RoleNameCookie.setMaxAge(60 * 60 * 24);
			resp.addCookie(RoleNameCookie);
			
			Cookie UserIdCookie = new Cookie("userId", String.valueOf(userId));
			UserIdCookie.setMaxAge(60 * 60 * 24);
			resp.addCookie(UserIdCookie);
			resp.sendRedirect("/crm09/");
		} else {
			req.setAttribute("errorMessage", "Invalid email or password");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}
	}
}
