package helper;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VietnameseHelper extends HttpServlet{
	public static void setUTF8(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
		resp.setCharacterEncoding("UTF-8");
		req.setCharacterEncoding("UTF-8");
	}
}
