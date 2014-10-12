package main.logic;

public abstract class CommandParser {

	/***************************** Data Members ************************/
	private String description = null;
	private boolean hasVenue = false;
	private String venue = null;
	private boolean hasStartDate = false;
	private String startDateYear = null;
	private String startDateMonth = null;
	private String startDateDay = null;
	private boolean hasEndDate = false;
	private String endDateYear = null;
	private String endDateMonth = null;
	private String endDateDay = null;
	private boolean hasStartTime = false;
	private String startTimeHour = null;
	private String startTimeMinute = null;
	private boolean hasEndTime = false;
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

	public boolean hasVenue() {
		return hasVenue;
	}

	public boolean hasStartDate() {
		return hasStartDate;
	}

	public boolean hasEndDate() {
		return hasEndDate;
	}

	public boolean hasStartTime() {
		return hasStartTime;
	}

	public boolean hasEndTime() {
		return hasEndTime;
	}

	/***************************** Mutators ************************/
	public void setDescription(String description) {
		this.description = description;
	}

	public void setVenue(String venue) {
		this.venue = venue;
		this.setHasVenue(true);
	}

	public void setStartDateYear(String startDateYear) {
		if (startDateYear != null) {
			this.startDateYear = startDateYear;
			this.setHasStartDate(true);
		}
	}

	public void setStartDateMonth(String startDateMonth) {
		this.startDateMonth = startDateMonth;
	}

	public void setStartDateDay(String startDateDay) {
		this.startDateDay = startDateDay;
	}

	public void setEndDateYear(String endDateYear) {
		if (endDateYear != null) {
			this.endDateYear = endDateYear;
			this.setHasEndDate(true);
		}
	}

	public void setEndDateMonth(String endDateMonth) {
		this.endDateMonth = endDateMonth;
	}

	public void setEndDateDay(String endDateDay) {
		this.endDateDay = endDateDay;
	}

	public void setStartTimeHour(String startTimeHour) {
		this.startTimeHour = startTimeHour;
		this.setHasStartTime(true);
	}

	public void setStartTimeMinute(String startTimeMinute) {
		this.startTimeMinute = startTimeMinute;
	}

	public void setEndTimeHour(String endTimeHour) {
		this.endTimeHour = endTimeHour;
		this.setHasEndTime(true);
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

	private void setHasVenue(boolean hasVenue) {
		this.hasVenue = hasVenue;
	}

	private void setHasStartDate(boolean hasStartDate) {
		this.hasStartDate = hasStartDate;
	}

	private void setHasEndDate(boolean hasEndDate) {
		this.hasEndDate = hasEndDate;
	}

	private void setHasStartTime(boolean hasStartTime) {
		this.hasStartTime = hasStartTime;
	}

	private void setHasEndTime(boolean hasEndTime) {
		this.hasEndTime = hasEndTime;
	}

	public abstract String parse();

}
