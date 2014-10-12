package main.storage;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


public class Task {
	
	static final String DELIMITER = "#!";
	
	/***************************** Data Members ************************/
	private String description;
	private String venue = null;
	private LocalDate startDate = null;
	private LocalTime startTime = null;
	private LocalDate endDate = null;
	private LocalTime endTime = null;
	private boolean hasReminder = false;
	private DateTime reminder = null;
	private boolean hasRecurrence = false;
	private String recurrence = null;
	private boolean completed = false;

	/***************************** Constructors ************************/
	public Task (
			String description,
			String venue,
			LocalDate startDate,
			LocalTime startTime,
			LocalDate endDate,
			LocalTime endTime,
			DateTime reminder,
			String recurrence,
			boolean completed
			) {
		this.setDescription(description);
		this.setVenue(venue);
		this.setStartDate(startDate);
		this.setStartTime(startTime);
		this.setEndDate(endDate);
		this.setEndTime(endTime);
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
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public LocalDate getEndDate() {
		return endDate;
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
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	
	private void setHasReminder(boolean hasReminder) {
		this.hasReminder = hasReminder;
	}
	
	public void setReminder(DateTime reminder) {
		if(reminder == null) {
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
		if(recurrence == null) {
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
		//insert print statements for date and time
		if(this.getHasReminder()) {
			result = result + ", " + "Reminder on: " + this.getReminder();
		}
		if(this.getHasRecurrence()) {
			result = result + ", " + "Recurrence: " + this.getRecurrence();
		}
		return result;
	}
}