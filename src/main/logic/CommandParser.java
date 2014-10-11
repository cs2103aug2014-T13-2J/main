package main.logic;

public abstract class CommandParser {
	
	/***************************** Data Members ************************/
	private String description = null;
	private String venue = null;
	private String startDateYear = null; 	
	private String startDateMonth = null;
	private String startDateDay = null;
	private String endDateYear = null;
	private String endDateMonth = null;		
	private String endDateDay = null;
	private String startTimeHour = null;
	private String startTimeMinute = null;
	private String endTimeHour = null;	
	private String endTimeMinute = null;
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
	
	public String getStartDateYear() {
		return startDateYear;
	}

	public String getStartDateMonth() {
		return startDateMonth;
	}

	public String getStartDateDay() {
		return startDateDay;
	}

	public String getEndDateYear() {
		return endDateYear;
	}

	public String getEndDateMonth() {
		return endDateMonth;
	}
	
	public String getEndDateDay() {
		return endDateDay;
	}

	public String getStartTimeHour() {
		return startTimeHour;
	}

	public String getStartTimeMinute() {
		return startTimeMinute;
	}

	public String getEndTimeHour() {
		return endTimeHour;
	}
	
	public String getEndTimeMinute() {
		return endTimeMinute;
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
	
	public void setStartDateYear(String startDateYear) {
		this.startDateYear = startDateYear;
	}


	public void setStartDateMonth(String startDateMonth) {
		this.startDateMonth = startDateMonth;
	}


	public void setEndDateYear(String endDateYear) {
		this.endDateYear = endDateYear;
	}


	public void setEndDateMonth(String endDateMonth) {
		this.endDateMonth = endDateMonth;
	}


	public void setEndDateDay(String endDateDay) {
		this.endDateDay = endDateDay;
	}

	public void setStartTimeHour(String startTimeHour) {
		this.startTimeHour = startTimeHour;
	}

	public void setStartTimeMinute(String startTimeMinute) {
		this.startTimeMinute = startTimeMinute;
	}

	public void setEndTimeHour(String endTimeHour) {
		this.endTimeHour = endTimeHour;
	}

	public void setEndTimeMinute(String endTimeMinute) {
		this.endTimeMinute = endTimeMinute;
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
