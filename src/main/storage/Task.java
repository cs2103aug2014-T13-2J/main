package main.storage;
import org.joda.time.DateTime;


public class Task {
	
	static final String DELIMITER = "#!";
	
	/***************************** Data Members ************************/
	private String description;
	private String venue=null;
	private DateTime startDateTime=null;
	private DateTime endDateTime=null;
	private boolean hasReminder=false;
	private DateTime reminder=null;
	private boolean hasRecurrence=false;
	private String recurrence=null;
	private boolean completed=false;

	/***************************** Constructors ************************/
	public Task (
			String description,
			String venue,
			DateTime startDateTime,
			DateTime endDateTime,
			DateTime reminder,
			String recurrence,
			boolean completed
			) {
		this.setDescription(description);
		this.setVenue(venue);
		this.setStartDateTime(startDateTime);
		this.setEndDateTime(endDateTime);
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
	
	public DateTime getStartDateTime() {
		return startDateTime;
	}
	
	public DateTime getEndDateTime() {
		return endDateTime;
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
	
	public void setStartDateTime(DateTime startTime) {
		this.startDateTime = startTime;
	}
	
	public void setEndDateTime(DateTime endTime) {
		this.endDateTime = endTime;
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
		result = result + this.getStartDateTime() + DELIMITER;
		result = result + this.getEndDateTime() + DELIMITER;
		result = result + this.getReminder() + DELIMITER;
		result = result + this.getRecurrence() + DELIMITER;
		result = result + this.getCompleted();
		
		return result;
	}
	
	public String toString() {
		String result = "";
		result = result + "Description: " + this.getDescription();
		result = result + ", " + "Venue: " + this.getVenue();
		if(this.getStartDateTime() != null) {
			result = result + ", " + "Start date and time: " + this.getStartDateTime();
		}
		if(this.getEndDateTime() != null) {
			result = result + ", " + "End date and time: " + this.getEndDateTime();
		}
		if(this.getHasReminder()) {
			result = result + ", " + "Reminder on: " + this.getReminder();
		}
		if(this.getHasRecurrence()) {
			result = result + ", " + "Recurrence: " + this.getRecurrence();
		}
		return result;
	}
}