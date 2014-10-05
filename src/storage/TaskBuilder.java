package storage;
import org.joda.time.DateTime;

class TaskBuilder {
	
	/***************************** Data Members ************************/
	private String description;
	private String venue;
	private DateTime startDateTime=null;
	private DateTime endDateTime=null;
	private DateTime reminder=null;
	private String recurrence=null;
	private boolean completed=false;

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
	
	public TaskBuilder setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
		return this;
	}
	
	public TaskBuilder setEndDateTime(DateTime endDateTime) {
		this.endDateTime = endDateTime;
		return this;
	}
	
	
	public void setReminder(DateTime reminder) {;
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
		return new Task(description, venue, startDateTime, endDateTime, 
				reminder, recurrence, completed);
	}
}