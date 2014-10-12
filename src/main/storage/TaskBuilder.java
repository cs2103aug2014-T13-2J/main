package main.storage;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class TaskBuilder {

	/***************************** Data Members ************************/
	private String description;
	private String venue = null;
	private LocalDate startDate = null;
	private LocalTime startTime = null;
	private LocalDate endDate = null;
	private LocalTime endTime = null;
	private DateTime reminder = null;
	private String recurrence = null;
	private boolean completed = false;

	/***************************** Constructors ************************/
	public TaskBuilder() {

	}

	public TaskBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public TaskBuilder setVenue(String venue) {
		this.venue = venue;
		return this;
	}

	public TaskBuilder setStartDate(LocalDate startDate) {
		this.startDate = startDate;
		return this;
	}

	public TaskBuilder setStartTime(LocalTime startTime) {
		this.startTime = startTime;
		return this;
	}

	public TaskBuilder setEndDate(LocalDate endDate) {
		this.endDate = endDate;
		return this;
	}

	public TaskBuilder setEndTime(LocalTime endTime) {
		this.endTime = endTime;
		return this;
	}

	public void setReminder(DateTime reminder) {
		this.reminder = reminder;
	}

	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;

	}

	public TaskBuilder setCompleted(boolean completed) {
		this.completed = completed;
		return this;
	}

	public Task buildTask() {
		return new Task(description, venue, startDate, startTime, endDate, endTime,
				reminder, recurrence, completed);
	}
}