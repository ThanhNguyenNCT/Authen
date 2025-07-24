package filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName =  "AuthenticationFilter", urlPatterns = {"/user-add", "/user-table"})
public class AuthenticationFilter extends HttpFilter{

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		Cookie cookies[] = req.getCookies();
		String roleName = "";
		
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("roleName")) {
					roleName = cookie.getValue();
					break;
				}
			}
		}
		
		if (roleName.equals("ROLE_ADMIN")) {
			chain.doFilter(req, res);
		} else {
			res.sendRedirect("index.jsp");
		}
	}
}
