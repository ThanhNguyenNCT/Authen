package service;

import java.util.List;

import entity.Status;
import repository.StatusRepository;

public class StatusService {
	public List<Status> getAllStatuses() {
		List<Status> listStatuses = new StatusRepository().getAllStatuses();
		if (listStatuses != null && !listStatuses.isEmpty()) {
			System.out.println("Statuses found: " + listStatuses.size());
		} else {
			System.out.println("No statuses found.");
		}
		return listStatuses;
	}
	
	public Status getStatusById(int id) {
		Status status = new StatusRepository().getStatusById(id);
		if (status != null) {
			System.out.println("Status found: " + status.getName());
			return status;
		} else {
			System.out.println("No status found with ID: " + id);
		}
		return null;
	}
}
