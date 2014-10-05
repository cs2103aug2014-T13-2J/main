package storage;
import org.joda.time.DateTime;

public class Task {
	
	/***************************** Data Members ************************/
	private String description;
	private String venue;
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
			boolean hasReminder,
			DateTime reminder,
			boolean hasRecurrence,
			String recurrence,
			boolean completed
			) {
		this.description = description;
		this.venue = venue;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.hasReminder = hasReminder;
		this.reminder = reminder;
		this.hasRecurrence = hasRecurrence;
		this.recurrence = recurrence;
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
	
	public void setHasReminder(boolean hasReminder) {
		this.hasReminder = hasReminder;
	}
	
	public void setReminder(DateTime reminder) {
		this.reminder = reminder;
	}
	
	public void setHasRecurrence(boolean hasRecurrence) {
		this.hasRecurrence = hasRecurrence;
	}
	
	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public String toString() {
		String result = "";
		result = result + description + "#!";
		result = result + venue + "#!";
		result = result + startDateTime + "#!";
		result = result + endDateTime + "#!";
		result = result + hasReminder + "#!";
		result = result + reminder + "#!";
		result = result + hasRecurrence + "#!";
		result = result + recurrence + "#!";
		result = result + completed;
		
		return result;
	}
}