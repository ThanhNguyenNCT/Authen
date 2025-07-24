package entity;

import java.sql.Date;
import java.time.LocalDate;

public class Project {
	private int id;
	private String name;
	private LocalDate startDay;
	private LocalDate endDay;
	private User manager; // User who manages the project
	
	public Project() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStartDay() {
		return startDay;
	}

	public void setStartDay(LocalDate startDay) {
		this.startDay = startDay;
	}

	public LocalDate getEndDay() {
		return endDay;
	}

	public void setEndDay(LocalDate endDay) {
		this.endDay = endDay;
	}

	//get Date for SQL
	public Date getStartDateSQL() {
	    return Date.valueOf(startDay);
	}
	public Date getEndDateSQL() {
	    return Date.valueOf(endDay);
	}

	public User getManager() {
		return manager;
	}
	public void setManager(User manager) {
		this.manager = manager;
	}

}
