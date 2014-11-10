package main.logic;

import java.util.Arrays;
import java.util.LinkedList;

public class UpdateParser extends CommandParser {

	private Integer taskNumber;
	private String field;

	protected final static String FIELD_DESCRIPTION = "d";
	protected final static String FIELD_DESCRIPTION_FULL = "description";
	protected final static String FIELD_VENUE = "v";
	protected final static String FIELD_VENUE_FULL = "venue";
	protected final static String FIELD_START = "s";
	protected final static String FIELD_START_DATE = "start date";
	protected final static String FIELD_START_TIME = "start time";
	protected final static String FIELD_END = "e";
	protected final static String FIELD_END_DATE = "end date";
	protected final static String FIELD_END_TIME = "end time";
	private final static String MESSAGE_INVALID_FIELD = "Sorry we did not understand which field you were trying to update.";
	private final static String MESSAGE_INVALID_TASK_NUMBER = "Task number is not a valid number.";
	private final static String STRING_SPACE = " ";

	/***************************** Constructors ************************/
	public UpdateParser(String arguments) {
		super(arguments);
	}

	/***************************** Accessors ************************/

	public Integer getTaskNumber() {
		return taskNumber;
	}

	public String getField() {
		return field;
	}

	/***************************** Mutators ************************/

	public void setTaskNumber(String number) throws IllegalArgumentException {
		Integer result;
		result = convertStringToInteger(number);
		this.taskNumber = result;
	}

	public void setField(String field) {
		if (field.startsWith(FIELD_DESCRIPTION)
				|| field.startsWith(FIELD_VENUE)
				|| field.equals(FIELD_START_DATE)
				|| field.equals(FIELD_START_TIME)
				|| field.equals(FIELD_END_DATE) || field.equals(FIELD_END_TIME)) {
			this.field = field;
		} else {
			throw new IllegalArgumentException(MESSAGE_INVALID_FIELD);
		}
	}

	/****************************************************************/
	@Override
	public String parse() {
		String[] userInput = this.getUserInput().split(" ");
		LinkedList<String> wordsList = new LinkedList<String>(
				Arrays.asList(userInput));

		String taskNumber = wordsList.poll();
		setTaskNumber(taskNumber);
		String field;
		if (!wordsList.isEmpty()) {
			field = wordsList.poll().toLowerCase();
		} else {
			throw new IllegalArgumentException(MESSAGE_INVALID_FIELD);
		}

		if (isValidField(field)) {
			String details = getDetails(wordsList);
			setFieldAndDetails(field, details, wordsList);
		}
		return MESSAGE_PARSE_SUCCESS;
	}

	private boolean isValidField(String field) {
		if (field.startsWith(FIELD_DESCRIPTION) || field.startsWith(FIELD_VENUE)
				|| field.startsWith(FIELD_START) || field.startsWith(FIELD_END)) {
			return true;
		} else {
			throw new IllegalArgumentException(MESSAGE_INVALID_FIELD);
		}
	}

	private void setFieldAndDetails(String field, String details,
			LinkedList<String> wordsList) {
		if (field.startsWith(FIELD_DESCRIPTION)) {
			this.setDescription(details);
			this.setField(FIELD_DESCRIPTION_FULL);
		} else if (field.startsWith(FIELD_VENUE)) {
			this.setVenue(details);
			this.setField(FIELD_VENUE_FULL);
		} else if (field.startsWith(FIELD_START)) {
			if (representsTime(wordsList)) {
				String startTime = getTimeAndTrimUserInput(wordsList);
				setStartTime(startTime);
				this.setField(FIELD_START_TIME);
			} else {
				String startDate = getDateAndTrimUserInput(wordsList);
				setStartDate(startDate);
				this.setField(FIELD_START_DATE);
			}
		} else if (field.startsWith(FIELD_END)) {
			if (representsTime(wordsList)) {
				String endTime = getTimeAndTrimUserInput(wordsList);
				setEndTime(endTime);
				this.setField(FIELD_END_TIME);
			} else {
				String endDate = getDateAndTrimUserInput(wordsList);
				setEndDate(endDate);
				this.setField(FIELD_END_DATE);
			}
		}
	}

	private static String getDetails(LinkedList<String> wordsList) {
		String details = "";
		for (String word : wordsList) {
			details = details + word + STRING_SPACE;
		}
		details = details.trim();
		return details;
	}

	private static Integer convertStringToInteger(String number) {
		try {
			Integer result = Integer.parseInt(number);
			return result;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(MESSAGE_INVALID_TASK_NUMBER);
		}
	}

	private void setStartDate(String startDate) {
		String startDateYear = getYear(startDate);
		String startDateMonth = getMonth(startDate);
		String startDateDay = getDay(startDate);

		this.setStartDateYear(startDateYear);
		this.setStartDateMonth(startDateMonth);
		this.setStartDateDay(startDateDay);
	}

	private void setEndDate(String endDate) {
		String endDateYear = getYear(endDate);
		String endDateMonth = getMonth(endDate);
		String endDateDay = getDay(endDate);

		this.setEndDateYear(endDateYear);
		this.setEndDateMonth(endDateMonth);
		this.setEndDateDay(endDateDay);
	}

	private void setStartTime(String startTime) {
		String startTimeHour = getHour(startTime);
		String startTimeMinute = getMinute(startTime);
		this.setStartTimeHour(startTimeHour);
		this.setStartTimeMinute(startTimeMinute);
	}

	private void setEndTime(String endTime) {
		String endTimeHour = getHour(endTime);
		String endTimeMinute = getMinute(endTime);
		this.setEndTimeHour(endTimeHour);
		this.setEndTimeMinute(endTimeMinute);
	}
}
