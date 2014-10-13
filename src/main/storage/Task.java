package main.storage;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class Task {

	static final String DELIMITER = "#!";

	/***************************** Data Members ************************/
	private String description;
	private String venue = null;
	private boolean hasStartDate = false;
	private LocalDate startDate = null;
	private boolean hasStartTime = false;
	private LocalTime startTime = null;
	private boolean hasEndDate = false;
	private LocalDate endDate = null;
	private boolean hasEndTime = false;
	private LocalTime endTime = null;
	private boolean hasReminder = false;
	private DateTime reminder = null;
	private boolean hasRecurrence = false;
	private String recurrence = null;
	private boolean completed = false;

	/***************************** Constructors ************************/
	public Task(String description, String venue, LocalDate startDate,
			LocalTime startTime, LocalDate endDate, LocalTime endTime,
			DateTime reminder, String recurrence, boolean completed)
			throws IllegalArgumentException {
		this.setDescription(description);
		this.setVenue(venue);
		this.setStartDate(startDate);
		this.setStartTime(startTime);
		this.setEndDate(endDate);
		this.setEndTime(endTime);

		if (endIsEarlierThanStart(startDate, startTime, endDate, endTime)) {
			throw new IllegalArgumentException();
		}

		this.setReminder(reminder);
		this.setRecurrence(recurrence);
		this.completed = completed;
	}

	/***************************** Accessors ************************/
	public String getDescription() {
		return description;
	}

	public String getVenue() {
		return venue;
	}

	public boolean getHasStartDate() {
		return hasStartDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public boolean getHasStartTime() {
		return hasStartTime;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public boolean getHasEndDate() {
		return hasEndDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public boolean getHasEndTime() {
		return hasEndTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public boolean getHasReminder() {
		return hasReminder;
	}

	public DateTime getReminder() {
		return reminder;
	}

	public boolean getHasRecurrence() {
		return hasRecurrence;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public boolean getCompleted() {
		return completed;
	}

	/***************************** Mutators ************************/
	public void setDescription(String description) {
		this.description = description;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public void setHasStartDate(boolean hasStartDate) {
		this.hasStartDate = hasStartDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;

		if (startDate != null) {
			this.setHasStartDate(true);
		} else {
			this.setHasStartDate(false);
		}
	}

	public void setHasStartTime(boolean hasStartTime) {
		this.hasStartTime = hasStartTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;

		if (startTime != null) {
			this.setHasStartTime(true);
		} else {
			this.setHasStartTime(false);
		}
	}

	public void setHasEndDate(boolean hasEndDate) {
		this.hasEndDate = hasEndDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;

		if (endDate != null) {
			this.setHasEndDate(true);
		} else {
			this.setHasEndDate(false);
		}

	}

	public void setHasEndTime(boolean hasEndTime) {
		this.hasEndTime = hasEndTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;

		if (endTime != null) {
			this.setHasEndTime(true);
		} else {
			this.setHasEndTime(false);
		}
	}

	private void setHasReminder(boolean hasReminder) {
		this.hasReminder = hasReminder;
	}

	public void setReminder(DateTime reminder) {
		if (reminder == null) {
			this.setHasReminder(false);
			this.reminder = reminder;
		} else {
			this.setHasReminder(true);
			this.reminder = reminder;
		}
	}

	private void setHasRecurrence(boolean hasRecurrence) {
		this.hasRecurrence = hasRecurrence;
	}

	public void setRecurrence(String recurrence) {
		if (recurrence == null) {
			this.setHasRecurrence(false);
			this.recurrence = recurrence;
		} else {
			this.setHasRecurrence(true);
			this.recurrence = recurrence;
		}
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String convertToCSVFormat() {
		String result = "";
		result = result + this.getDescription() + DELIMITER;
		result = result + this.getVenue() + DELIMITER;
		result = result + this.getStartDate() + DELIMITER;
		result = result + this.getStartTime() + DELIMITER;
		result = result + this.getEndDate() + DELIMITER;
		result = result + this.getEndTime() + DELIMITER;
		result = result + this.getReminder() + DELIMITER;
		result = result + this.getRecurrence() + DELIMITER;
		result = result + this.getCompleted();

		return result;
	}

	public String toString() {
		String result = "";
		result = result + "Description: " + this.getDescription();
		result = result + ", " + "Venue: " + this.getVenue();

		if (this.getHasStartDate()) {
			result = result + ", " + "Start date: " + this.getStartDate();
		}

		if (this.getHasStartTime()) {
			result = result + ", " + "Start time: " + this.getStartTime();
		}

		if (this.getHasEndDate()) {
			result = result + ", " + "End date: " + this.getEndDate();
		}

		if (this.getHasEndTime()) {
			result = result + ", " + "End time: " + this.getEndTime();
		}

		if (this.getHasReminder()) {
			result = result + ", " + "Reminder on: " + this.getReminder();
		}
		if (this.getHasRecurrence()) {
			result = result + ", " + "Recurrence: " + this.getRecurrence();
		}
		return result;
	}

	public static boolean endIsEarlierThanStart(LocalDate startDate,
			LocalTime startTime, LocalDate endDate, LocalTime endTime)throws IllegalArgumentException {
		if (startDate == null && endDate == null) {
			return false;
		}
		DateTime d1 = startDate.toDateTimeAtStartOfDay();
		DateTime d2 = endDate.toDateTimeAtStartOfDay();
		int compareResult = DateTimeComparator.getDateOnlyInstance().compare(d1, d2);
		switch (compareResult) {
		case -1: // startDate is earlier than endDate
			return false;
		case 0: {	// if dates are equal we compare time unless they are null
			if (startTime == null && endTime == null) {
				return false;
			} else {
				DateTime t1 = startTime.toDateTimeToday();
				DateTime t2 = endTime.toDateTimeToday();
				compareResult = DateTimeComparator.getTimeOnlyInstance().compare(t1, t2);
			}
		}	
		case 1: // startDate is later than endDate
			return true;
		}

		switch (compareResult) {
		case -1: // startTime is earlier than endTime
			return false;
		case 0: // startTime is equal to endTime
			return false;
		case 1: // startTime is later than endTime
			return true;
		default:
			// we should never reach this case
			throw new IllegalArgumentException();
		}

	}
}