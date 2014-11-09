package main.storage;

import main.TaskerLog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class Task {

	private static final String DELIMITER = "#!";
	private static final String MESSAGE_UPDATE_END_DATE_WITHOUT_START_DATE = "Can't update end date if there is no start date";
	private static final String MESSAGE_END_EARLIER_THAN_START = "Sorry, the end time can't be earlier than the start time.";
	private static final String MESSAGE_HAS_TIME_WITHOUT_DATE = "Sorry, can't have time without date.";

	/***************************** Data Members ************************/
	private String id = null;
	private boolean hasId = false;
	private String description;
	private boolean hasVenue = false;
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
	private boolean hasBeenUpdated = false;

	/***************************** Constructors ************************/
	public Task(String id, String description, String venue,
			LocalDate startDate, LocalTime startTime, LocalDate endDate,
			LocalTime endTime, DateTime reminder, String recurrence,
			boolean completed, boolean hasBeenUpdated) throws IllegalArgumentException {
		this.setId(id);
		this.setDescription(description);
		this.setVenue(venue);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		// we put setStartTime after setEndDate because of endIsEarlierThanStart
		// function
		// requires that if startTime is not null, endTime must not be null
		this.setStartTime(startTime);
		this.setEndTime(endTime);

		if (startDate == null && endDate == null
				&& (startTime != null || endTime != null)) {
			throw new IllegalArgumentException(MESSAGE_HAS_TIME_WITHOUT_DATE);
		}

		if (endIsEarlierThanStart(startDate, startTime, endDate, endTime)) {
			throw new IllegalArgumentException(MESSAGE_END_EARLIER_THAN_START);
		}

		this.setReminder(reminder);
		this.setRecurrence(recurrence);
		this.completed = completed;
		this.hasBeenUpdated = hasBeenUpdated;
	}

	/***************************** Accessors ************************/
	public boolean hasId() {
		return hasId;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public boolean hasVenue() {
		return hasVenue;
	}

	public String getVenue() {
		return venue;
	}

	public boolean hasStartDate() {
		return hasStartDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public boolean hasStartTime() {
		return hasStartTime;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public boolean hasEndDate() {
		return hasEndDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public boolean hasEndTime() {
		return hasEndTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public boolean hasReminder() {
		return hasReminder;
	}

	public DateTime getReminder() {
		return reminder;
	}

	public boolean hasRecurrence() {
		return hasRecurrence;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public boolean hasCompleted() {
		return completed;
	}
	
	public boolean hasBeenUpdated() {
		return hasBeenUpdated;
	}

	/***************************** Mutators ************************/
	private void setHasId(boolean hasId) {
		this.hasId = hasId;
	}

	public void setId(String id) {
		this.id = id;

		if (id == null) {
			this.setHasId(false);
		} else {
			this.setHasId(true);
		}
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setHasVenue(boolean hasVenue) {
		this.hasVenue = hasVenue;
	}

	public void setVenue(String venue) {
		this.venue = venue;

		if (venue == null) {
			this.setHasVenue(false);
		} else {
			this.setHasVenue(true);
		}
	}

	public void setHasStartDate(boolean hasStartDate) {
		this.hasStartDate = hasStartDate;
	}

	public void setStartDate(LocalDate startDate) {
		if (startDate == null) {
			this.startDate = startDate;
			this.setHasStartDate(false);
		} else {
			if (endDate == null || startDateEqualsEndDate()) {
				this.startDate = startDate;
				this.setHasStartDate(true);
				this.setEndDate(startDate);
				this.setHasEndDate(true);
			} else if (endDate != null
					&& endIsEarlierThanStart(startDate, this.getStartTime(),
							this.getEndDate(), this.getEndTime())) {
				throw new IllegalArgumentException(
						MESSAGE_END_EARLIER_THAN_START);
			} else {
				this.startDate = startDate;
				this.setHasStartDate(true);
			}
		}
	}

	public void setHasStartTime(boolean hasStartTime) {
		this.hasStartTime = hasStartTime;
	}

	public void setStartTime(LocalTime startTime) {
		if (startTime == null) {
			this.startTime = startTime;
			this.setHasStartTime(false);
		} else {
			if (endTime == null || startTimeEqualsEndTime()) {
				this.startTime = startTime;
				this.setHasStartTime(true);
				this.setEndTime(startTime);
				this.setHasEndTime(true);
			} else if (endTime != null
					&& endIsEarlierThanStart(this.getStartDate(), startTime,
							this.getEndDate(), this.getEndTime())) {
				throw new IllegalArgumentException(
						MESSAGE_END_EARLIER_THAN_START);
			} else {
				this.startTime = startTime;
				this.setHasStartTime(true);
			}
		}
	}

	public void setHasEndDate(boolean hasEndDate) {
		this.hasEndDate = hasEndDate;
	}

	public void setEndDate(LocalDate endDate) {
		if (endDate == null) {
			this.endDate = endDate;
			this.setHasEndDate(false);
		} else {
			if (endIsEarlierThanStart(this.getStartDate(), this.getStartTime(),
					endDate, this.getEndTime())) {
				throw new IllegalArgumentException(
						MESSAGE_END_EARLIER_THAN_START);
			} else {
				this.endDate = endDate;
				this.setHasEndDate(true);
			}
		}
	}

	public void setHasEndTime(boolean hasEndTime) {
		this.hasEndTime = hasEndTime;
	}

	public void setEndTime(LocalTime endTime) {
		if (endTime == null) {
			this.endTime = endTime;
			this.setHasEndTime(false);
		} else {
			if (endIsEarlierThanStart(this.getStartDate(), this.getStartTime(),
					this.getEndDate(), endTime)) {
				throw new IllegalArgumentException(
						MESSAGE_END_EARLIER_THAN_START);
			} else {
				this.endTime = endTime;
				this.setHasEndTime(true);
			}
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
	
	public void setHasBeenUpdated(boolean hasBeenUpdated) {
		this.hasBeenUpdated = hasBeenUpdated;
	}

	public String convertToCSVFormat() {
		String result = "";
		result = result + this.getId() + DELIMITER;
		result = result + this.getDescription() + DELIMITER;
		result = result + this.getVenue() + DELIMITER;
		result = result + this.getStartDate() + DELIMITER;
		result = result + this.getStartTime() + DELIMITER;
		result = result + this.getEndDate() + DELIMITER;
		result = result + this.getEndTime() + DELIMITER;
		result = result + this.getReminder() + DELIMITER;
		result = result + this.getRecurrence() + DELIMITER;
		result = result + this.hasCompleted() + DELIMITER;
		result = result + this.hasBeenUpdated();

		return result;
	}

	public String toString() {
		String result = "";
		result = result + "Description: " + this.getDescription();
		result = result + ", " + "Venue: " + this.getVenue();

		if (this.hasStartDate()) {
			result = result + ", " + "Start date: " + this.getStartDate();
		}

		if (this.hasStartTime()) {
			result = result + ", " + "Start time: " + this.getStartTime();
		}

		if (this.hasEndDate()) {
			result = result + ", " + "End date: " + this.getEndDate();
		}

		if (this.hasEndTime()) {
			result = result + ", " + "End time: " + this.getEndTime();
		}

		if (this.hasReminder()) {
			result = result + ", " + "Reminder on: " + this.getReminder();
		}
		if (this.hasRecurrence()) {
			result = result + ", " + "Recurrence: " + this.getRecurrence();
		}
		return result;
	}

	public static boolean endIsEarlierThanStart(LocalDate startDate,
			LocalTime startTime, LocalDate endDate, LocalTime endTime)
			throws IllegalArgumentException {
		if (startDate == null && endDate == null) {
			return false;
		} else if (startDate == null && endDate != null) {
			throw new IllegalArgumentException(
					MESSAGE_UPDATE_END_DATE_WITHOUT_START_DATE);
		}
		DateTime d1 = startDate.toDateTimeAtStartOfDay();
		DateTime d2 = endDate.toDateTimeAtStartOfDay();
		int compareResult = DateTimeComparator.getDateOnlyInstance().compare(
				d1, d2);
		switch (compareResult) {
		case -1: // startDate is earlier than endDate
			return false;
		case 0: { // if dates are equal we compare time unless they are null
			if (startTime == null && endTime == null) {
				return false;
			} else {
				DateTime t1 = startTime.toDateTimeToday();
				DateTime t2 = endTime.toDateTimeToday();
				compareResult = DateTimeComparator.getTimeOnlyInstance()
						.compare(t1, t2);
			}
			break;
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

	// this function assumes that startDate and endDate will never be null
	public boolean startDateEqualsEndDate() throws IllegalArgumentException {
		LocalDate startDate = this.getStartDate();
		LocalDate endDate = this.getEndDate();
		DateTime d1 = startDate.toDateTimeAtStartOfDay();
		DateTime d2 = endDate.toDateTimeAtStartOfDay();
		int compareResult = DateTimeComparator.getDateOnlyInstance().compare(
				d1, d2);
		switch (compareResult) {
		case -1: // startDate is earlier than endDate
			return false;
		case 0: // startDate equals endDate
			return true;
		default: // we should never reach this case
			TaskerLog
					.logSystemInfo("Entered unexpected case in startDateEqualsEndDate function in Task class");
			throw new IllegalArgumentException(MESSAGE_END_EARLIER_THAN_START);
		
		}
	}

	// this function assumes that startTime and endTime will never be null
	public boolean startTimeEqualsEndTime() throws IllegalArgumentException {
		LocalTime startTime = this.getStartTime();
		LocalTime endTime = this.getEndTime();
		DateTime t1 = startTime.toDateTimeToday();
		DateTime t2 = endTime.toDateTimeToday();
		int compareResult = DateTimeComparator.getTimeOnlyInstance().compare(
				t1, t2);
		switch (compareResult) {
		case -1: // startTime is earlier than endTime
			return false;
		case 0: // startTime equals endTime
			return true;
		default: // we should never reach this case
			TaskerLog
					.logSystemInfo("Entered unexpected case in startTimeEqualsEndTime function in Task class");
			throw new IllegalArgumentException(MESSAGE_END_EARLIER_THAN_START);
		}
	}

}