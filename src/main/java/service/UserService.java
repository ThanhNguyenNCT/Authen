package service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import entity.User;
import repository.UserRepository;

public class UserService {
	public List<User> getAllUsers() {
		List<User> listUsers = new UserRepository().getAllUsers();
		if (listUsers != null && !listUsers.isEmpty()) {
			System.out.println("Users found: " + listUsers.size());
			for (User user : listUsers) {
				user.setRoleName(new RoleService().getRoleNameById(user.getRoleId()));
			}
		} else {
			System.out.println("No users found.");
		}
		return listUsers;
	}
	
	public boolean insertUser(String email, String password, String fullName,String phone, int roleId) {
		User user = new User();
		String[] names = fullName.split(" ");
		if (names.length > 1) {
			user.setFirst_name(names[names.length - 1]);
			//Gán phần còn lại của tên vào last_name
			StringBuilder lastName = new StringBuilder();
			for (int i = 0; i < names.length - 1; i++) {
				lastName.append(names[i]);
				if (i < names.length - 2) {
					lastName.append(" ");
				}
			}
			user.setLast_name(lastName.toString());
		} else {
			user.setFirst_name(fullName);
			user.setLast_name("");
		}
		user.setEmail(email);
		user.setPassword(password);
		user.setPhone(phone);
		user.setRoleId(roleId);
		user.setUsername(user.getFirst_name()); 
		int result = new UserRepository().insertUser(user);
		if (result > 0) {
			System.out.println("User inserted successfully.");
			return true;
		} else {
			System.out.println("Failed to insert user.");
			return false;
		}
		
	}
	
	public boolean editUser(User user, String firstName, String lastName, String username, int roleId) {
		user.setFirst_name(firstName);
		user.setLast_name(lastName);
		user.setUsername(username);
		user.setRoleId(roleId);
		int result = new UserRepository().editUser(user);
		if (result > 0) {
			System.out.println("User updated successfully.");
			return true;
		} else {
			System.out.println("Failed to update user.");
			return false;
		}
	}
	public User getUserById(int userId) {
		User user = new UserRepository().getUserById(userId);
		if (user != null) {
			System.out.println("User found: " + user.getFullName());
		} else {
			System.out.println("No user found with ID: " + userId);
		}
		return user;
	}
	
	public boolean deleteUser(int userId) {
		int result = new UserRepository().deleteUser(userId);
		if (result > 0) {
			System.out.println("User deleted successfully.");
			return true;
		} else {
			System.out.println("Failed to delete user.");
			return false;
		}
	}
	
	public List<Map<String, Object>> getDetailsByUserId(int userId) {
		List<Map<String, Object>> details = new UserRepository().getDetailsByUserId(userId);
		if (details != null && !details.isEmpty()) {
			System.out.println("Details found for user ID: " + userId);
		} else {
			System.out.println("No details found for user ID: " + userId);
		}
		return details;
	}
	
	public User getUserByEmailPassword(Cookie[] cookies) {
		String email = "";
		String password = "";
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("email")) {
				email = cookie.getValue();
			} else if (cookie.getName().equals("password")) {
				password = cookie.getValue();
			}
		}
		User user = new UserRepository().getUserByEmailPassword(email, password);
		if (user != null) {
			System.out.println("User found: " + user.getFirst_name() + " " + user.getLast_name());
		} else {
			System.out.println("No user found with email: " + email);
		}
		return user;
	}
	
	public List<User> getListLeaders() {
		List<User> listLeads = new UserRepository().getListLeaders();
		if (listLeads != null && !listLeads.isEmpty()) {
			System.out.println("Leaders found: " + listLeads.size());
		} else {
			System.out.println("No leads found.");
		}
		return listLeads;
	}
	
	public List<User> getUsersByRoleName(String roleName) {
		List<User> users = new UserRepository().getUsersByRoleName(roleName);
		if (users != null && !users.isEmpty()) {
			System.out.println("Users found with role " + roleName + ": " + users.size());
		} else {
			System.out.println("No users found with role " + roleName);
		}
		return users;
	}
}
