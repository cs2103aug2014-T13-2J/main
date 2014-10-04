package storage;
import org.joda.time.DateTime;

class TaskBuilder {
	
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
	
	public TaskBuilder setendDateTime(DateTime endDateTime) {
		this.endDateTime = endDateTime;
		return this;
	}
	
	public TaskBuilder setHasReminder(boolean hasReminder) {
		this.hasReminder = hasReminder;
		return this;
	}
	
	public TaskBuilder setReminder(DateTime reminder) {
		this.reminder = reminder;
		return this;
	}
	
	public TaskBuilder setHasRecurrence(boolean hasRecurrence) {
		this.hasRecurrence = hasRecurrence;
		return this;
	}
	
	public TaskBuilder setRecurrence(String recurrence) {
		this.recurrence = recurrence;
		return this;
	}
	
	public TaskBuilder setCompleted(boolean completed) {
		this.completed = completed;
		return this;
	}
	
	public Task buildTask() {
		return new Task(description, venue, startDateTime, endDateTime, hasReminder, 
				reminder, hasRecurrence, recurrence, completed);
	}
}