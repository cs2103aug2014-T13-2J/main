import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Event.Reminders;

class TaskBuilder {
	
	/***************************** Data Members ************************/
	private String description;
	private String venue;
	private EventDateTime startTime=null;
	private EventDateTime endTime=null;
	private EventDateTime startDate=null;
	private EventDateTime endDate=null;
	private boolean hasReminder=false;
	private Reminders reminder=null;
	private String recurringEventId=null;
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
	
	public TaskBuilder setStartTime(EventDateTime startTime) {
		this.startTime = startTime;
		return this;
	}
	
	public TaskBuilder setEndTime(EventDateTime endTime) {
		this.endTime = endTime;
		return this;
	}
	
	public TaskBuilder setStartDate(EventDateTime startDate) {
		this.startDate = startDate;
		return this;
	}
	
	public TaskBuilder setEndDate(EventDateTime endDate) {
		this.endDate = endDate;
		return this;
	}
	
	public TaskBuilder setHasReminder(boolean hasReminder) {
		this.hasReminder = hasReminder;
		return this;
	}
	
	public TaskBuilder setReminder(Reminders reminder) {
		this.reminder = reminder;
		return this;
	}
	
	public TaskBuilder setRecurringEventId(String recurringEventId) {
		this.recurringEventId = recurringEventId;
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
		return new Task(description, venue, startTime, endTime, startDate, endDate, hasReminder, 
				reminder, recurringEventId, hasRecurrence, recurrence, completed);
	}
}