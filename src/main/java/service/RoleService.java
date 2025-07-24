package service;

import java.util.List;

import entity.Role;
import repository.RoleRepository;

public class RoleService {
	public String getRoleNameById(int roleId) {
		String roleName = new RoleRepository().getRoleNameById(roleId);
		if (roleName != null && !roleName.isEmpty()) {
			System.out.println("Role found: " + roleName);
		} else {
			System.out.println("No role found for ID: " + roleId);
		}
		return roleName;
	}
	
	public List<Role> getAllRoles() {
		List<Role> listRoles = new RoleRepository().getAllRoles();
		if (listRoles != null && !listRoles.isEmpty()) {
			System.out.println("Roles found: " + listRoles.size());
		} else {
			System.out.println("No roles found.");
		}
		return listRoles;
	}
	
	public boolean addRole(String name, String description) {
		Role role = new Role();
		role.setName(name);
		role.setDescription(description);
		
		int result = new RoleRepository().addRole(role);
		if (result > 0) {
			System.out.println("Role added successfully: " + name);
			return true;
		} else {
			System.out.println("Failed to add role: " + name);
			return false;
		}
	}
	public Role getRoleById(int id) {
		Role role = new RoleRepository().getRoleById(id);
		if (role != null) {
			System.out.println("Role found: " + role.getName());
		} else {
			System.out.println("No role found for ID: " + id);
		}
		return role;
	}
	
	public boolean editRole(Role role, String name, String description) {
		role.setName(name);
		role.setDescription(description);
		
		int result = new RoleRepository().editRole(role);
		if (result > 0) {
			System.out.println("Role edited successfully: " + name);
			return true;
		} else {
			System.out.println("Failed to edit role: " + name);
			return false;
		}
	}
	
	public boolean deleteRole(int id) {
		int result = new RoleRepository().deleteRole(id);
		if (result > 0) {
			System.out.println("Role deleted successfully: " + id);
			return true;
		} else {
			System.out.println("Failed to delete role: " + id);
			return false;
		}
	}
}
