package service;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.User;
import repository.LoginRepository;

public class LoginService {
	private LoginRepository loginRepo = new LoginRepository();
	public boolean validUser(String email, String password) {
		List<User> listUsers = null;
		try {
			listUsers = loginRepo.getValidUser(email, password);
		} catch (ConnectException e) {
			System.out.println("Error connecting to the database: " + e.getMessage());
			e.printStackTrace();
		}
		if (listUsers != null && !listUsers.isEmpty()) {
			System.out.println("User found: " + listUsers.get(0).getLast_name() + " " + listUsers.get(0).getFirst_name());	
			System.out.println("Login successful !!!!11");
			return true;
		}
		System.out.println("Login failed !!!");
		return false;
		
	}
	
	public Map<Integer, String> getIdAndRoleNameByEmailandPassword(String email, String password) {
		Map<Integer, String> result = new HashMap<>();
		try {
			result = loginRepo.getIdAndRoleNameByEmailandPassword(email, password);
			if (result.isEmpty()) {
				System.out.println("No user found with the provided email and password.");
			} else {
				System.out.println("User ID: " + result.keySet().iterator().next() + ", Role Name: " + result.values().iterator().next());
			}
		} catch (Exception e) {
			System.out.println("Error retrieving user information: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
