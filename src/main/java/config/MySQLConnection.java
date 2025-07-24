package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnection {
	private static final String url = "jdbc:mysql://localhost:3307/crmapp";
	private static final String user = "root";
	private static final String password = "admin123";
	
	public static Connection getConnection(){
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection =  DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			System.out.println("Error connecting to the database: " + e.getMessage());
		}
		
		return connection;
	}
}
