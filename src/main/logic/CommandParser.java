package main.logic;

public abstract class CommandParser {
	
	/***************************** Data Members ************************/
	private String description = null;
	private String venue = null;
	private String startDate = null;
	private String endDate = null;
	private String startTime = null;
	private String endTime = null;	
	private String reminder = null;
	private String recurrence = null;
	private String completed = null;
	
	/***************************** Constructors ************************/
	public CommandParser(String arguments) {
		
	}
	
	/***************************** Accessors ************************/
	public String getDescription() {
		return description;
	}

	public String getVenue() {
		return venue;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getStartTime() {
		return startTime;
	}	

	public String getEndTime() {
		return endTime;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public String getCompleted() {
		return completed;
	}
	
	/***************************** Mutators ************************/
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setVenue(String venue) {
		this.venue = venue;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}


	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}
	
	public abstract String parse();

}
