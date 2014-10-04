package storage;
import org.joda.time.DateTime;
import com.google.api.services.calendar.model.Event.Reminders;

class Task {
	
	/***************************** Data Members ************************/
	private String description;
	private String venue;
	private DateTime startDateTime=null;
	private DateTime endDateTime=null;
	private boolean hasReminder;
	private Reminders reminder;
	private String recurringEventId;
	private boolean hasRecurrence;
	private String recurrence;
	private boolean completed;

	/***************************** Constructors ************************/
	public Task (
			String description,
			String venue,
			DateTime startDateTime,
			DateTime endDateTime,
			boolean hasReminder,
			Reminders reminder,
			String recurringEventId,
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
		this.recurringEventId = recurringEventId;
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
	
	public Reminders getReminder() {
		return reminder;
	}
	
	public String getRecurringEventId() {
		return recurringEventId;
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
	
	public void setReminder(Reminders reminder) {
		this.reminder = reminder;
	}
	
	public void setRecurringEventId(String recurringEventId) {
		this.recurringEventId = recurringEventId;
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
		//uncompleted - put into a CSV friendly format
		String result = "";
		return result;
	}
}