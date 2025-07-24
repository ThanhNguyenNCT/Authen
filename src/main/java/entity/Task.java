package entity;

import java.sql.Date;
import java.time.LocalDate;

public class Task {
	private int id;
	private String name_task;
	private LocalDate start_task;
	private LocalDate end_task;
	private String status;
	private Project project;
	private User user;
	
	public Task() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName_task() {
		return name_task;
	}

	public void setName_task(String name_task) {
		this.name_task = name_task;
	}

	public LocalDate getStart_task() {
		return start_task;
	}

	public void setStart_task(LocalDate start_task) {
		this.start_task = start_task;
	}

	public LocalDate getEnd_task() {
		return end_task;
	}

	public void setEnd_task(LocalDate end_task) {
		this.end_task = end_task;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	// Getters for SQL Date
	public Date getStartDateSQL() {
		return Date.valueOf(start_task);
	}
	public Date getEndDateSQL() {
		return Date.valueOf(end_task);
	}
}
