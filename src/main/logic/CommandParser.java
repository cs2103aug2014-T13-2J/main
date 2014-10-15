package main.logic;

import java.util.LinkedList;

import org.joda.time.DateTime;

public abstract class CommandParser {

	/***************************** Data Members ************************/
	protected final static String MESSAGE_INVALID_DESCRIPTION = "Sorry, we did not capture your description. Please try again.";
	protected final static String MESSAGE_INVALID_TIME_FORMAT = "Sorry we did not manage to capture the time. Please ensure you entered it in the correct format.";
	protected final static String MESSAGE_INVALID_VENUE = "Sorry we did not manage to capture the venue. Please try again.";
	protected final static String MESSAGE_INVALID_DATE_FORMAT = "Sorry we did not manage to capture the date. Please ensure you entered it in the correct format.";
	protected final static String MESSAGE_PARSE_SUCCESS = "Parse successful.";
	protected final static String INVALID_RECURRENCE_FORMAT = "Sorry we did not manage to capture the recurrence. Please try again.";
	protected final static String INVALID_COMPLETED_FORMAT = "Sorry we did not manage to capture the completion of the task. Please try again.";
	protected final static Integer INDEX_HOUR = 0;
	protected final static Integer INDEX_MINUTE = 1;
	protected final static Integer INDEX_DAY = 0;
	protected final static Integer INDEX_MONTH = 1;
	protected final static Integer INDEX_YEAR = 2;

	protected String userInput;
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
		this.userInput = arguments;
	}

	/***************************** Accessors ************************/
	public String getUserInput() {
		return userInput;
	}

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
		this.startDateYear = startDateYear;
		if (startDateYear == null) {
			this.setHasStartDate(false);
		} else {
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
		this.endDateYear = endDateYear;
		if (endDateYear == null) {
			this.setHasEndDate(false);
		} else {
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
		if (startTimeHour == null) {
			this.setHasStartTime(false);
		} else {
			this.setHasStartTime(true);
		}

	}

	public void setStartTimeMinute(String startTimeMinute) {
		this.startTimeMinute = startTimeMinute;
	}

	public void setEndTimeHour(String endTimeHour) {
		this.endTimeHour = endTimeHour;
		if (endTimeHour == null) {
			this.setHasEndTime(false);
		} else {
			this.setHasEndTime(true);
		}
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
		recurrence = recurrence.toLowerCase();
		if(recurrence.equals("weekly") || recurrence.equals("monthly") || recurrence.equals("yearly")) {
			this.recurrence = recurrence;
		} else {
			throw new IllegalArgumentException(INVALID_RECURRENCE_FORMAT);
		}		
	}

	public void setCompleted(String completed) {
		completed = completed.toLowerCase();
		if(completed.equals("true") || completed.equals("false")) {
			this.completed = completed;
		} else {
			throw new IllegalArgumentException(INVALID_COMPLETED_FORMAT);
		}
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

	// This function assumes that any of the reserved words will not be part of
	// the description
	public static String getDescriptionAndTrimUserInput(
			LinkedList<String> wordsList) throws IllegalArgumentException {
		String currentWord, description = "";
		while (!wordsList.isEmpty()) {
			currentWord = wordsList.peek();
			if (isReservedWord(currentWord)) {
				break;
			} else {
				wordsList.poll();
				description += currentWord + " ";
			}
		}
		description = description.trim();
		// this condition is put here because "".split(" ") returns a string
		// array
		// of non zero length, so before trim(), description will be equal to
		// something like " "
		if (description.equals("")) {
			throw new IllegalArgumentException(MESSAGE_INVALID_DESCRIPTION);
		}
		return description;
	}

	protected static boolean isReservedWord(String word) {
		word = word.toLowerCase();
		if (word.equals("at") || word.equals("on") || word.equals("from")
				|| word.equals("next")) {
			return true;
		} else {
			return false;
		}
	}

	// This function ensures that word is of the format H.M or H, appended by am
	// or pm. H and M can be any integer.
	// H or M cannot start with the number 0
	public static boolean representsTime(String word)
			throws IllegalArgumentException {
		if (word.length() < 2) {
			return false;
		}
		String numPortion = word.substring(0, word.length() - 2);
		String amPortion = word.substring(word.length() - 2, word.length());
		// checks that H does not start with 0
		if (numPortion.startsWith("0")) {
			throw new IllegalArgumentException(MESSAGE_INVALID_TIME_FORMAT);
		}
		if (numPortion.contains(".")) {
			// Need the double backslash if we want to use . as the delimiter
			String[] numPortionParts = numPortion.split("\\.");
			if (numPortionParts.length != 2) {
				throw new IllegalArgumentException(MESSAGE_INVALID_TIME_FORMAT);
			}
			String numHour = numPortionParts[INDEX_HOUR];
			String numMinute = numPortionParts[INDEX_MINUTE];
			// checks that M does not start with 0
			if (numMinute.startsWith("0")) {
				throw new IllegalArgumentException(MESSAGE_INVALID_TIME_FORMAT);
			}
			if (isInteger(numHour) && isInteger(numMinute)
					&& (amPortion.equals("am") || amPortion.equals("pm"))) {
				return true;
			} else {
				return false;
			}

		} else if (isInteger(numPortion)
				&& (amPortion.equals("am") || amPortion.equals("pm"))) {
			return true;
		} else {
			return false;
		}
	}

	public static String getTime(LinkedList<String> wordList) {
		String result = "";
		String word = wordList.poll();

		String pmPortion = word.substring(word.length() - 2, word.length());
		String numPortion = word.substring(0, word.length() - 2);

		boolean isPm;
		if (pmPortion.equals("pm")) {
			isPm = true;
		} else {
			isPm = false;
		}

		// to cater to cases that format is not one of the following: 9am,
		// 9.30pm, 12.30am
		if (numPortion.contains(".")) {
			// Need the double backslash if we want to use . as the delimiter
			String[] numPortionParts = numPortion.split("\\.");
			Integer numHour = Integer.parseInt(numPortionParts[INDEX_HOUR]);
			Integer numMinute = Integer.parseInt(numPortionParts[INDEX_MINUTE]);

			if (isPm == true && numHour != 12) {
				numHour += 12;
			} else if (isPm == false && numHour == 12) { // to cater to the 12am
															// case: we want the
															// output to be 0.0
				numHour -= 12;
			}

			result = numHour + "." + numMinute;
			return result;
		} else {
			Integer numHour = Integer.parseInt(numPortion);
			// insert assert statement that numPortionParts.length == 1
			if (isPm == true && numHour != 12) {
				numHour += 12;
			} else if (isPm == false && numHour == 12) { // to cater to the 12am
															// case: we want the
															// output to be 0.0
				numHour -= 12;
			}
			result = numHour + "." + "0";
			return result;
		}

	}

	public static String getVenueAndTrimUserInput(LinkedList<String> wordsList)
			throws IllegalArgumentException {
		String currentWord, venue = "";
		while (!wordsList.isEmpty()) {
			currentWord = wordsList.peek();
			if (isReservedWord(currentWord)) {
				break;
			} else {
				wordsList.poll();
				venue += currentWord + " ";
			}
		}
		venue = venue.trim();
		// this condition is put here because "".split(" ") returns a string
		// array
		// of non zero length, so before trim(), venue will be equal to
		// something like " "
		if (venue.equals("")) {
			throw new IllegalArgumentException(MESSAGE_INVALID_VENUE);
		}

		return venue;
	}

	public static String getDateAndTrimUserInput(LinkedList<String> wordsList)
			throws IllegalArgumentException {
		String date = "";
		String currentWord = wordsList.poll();
		String[] arr = currentWord.split("/");
		// if the format is D/M/Y, where D, M, Y can be any integer, but cannot
		// start with 0
		if (arr.length == 3) {
			if (!isInteger(arr[INDEX_DAY]) || !isInteger(arr[INDEX_MONTH])
					|| !isInteger(arr[INDEX_YEAR])) {
				throw new IllegalArgumentException(MESSAGE_INVALID_DATE_FORMAT);
			}
			return currentWord;
		} else if (arr.length == 2) { // if the format is D/M, where D, M can be
										// any integer, but cannot start with 0
			// get current year and append to D/M
			DateTime current = new DateTime();
			Integer year = current.getYear();
			date = currentWord + "/" + year.toString();
			return date;
		} else { // if the format is D M or D M Y
			// get day
			if (isInteger(currentWord)) {
				date += currentWord;
			} else {
				throw new IllegalArgumentException(MESSAGE_INVALID_DATE_FORMAT);
			}
			// get month
			if (!wordsList.isEmpty()) {
				currentWord = wordsList.poll();
				if (isMonth(currentWord)) {
					date += "/" + convertMonthToNumberStringFormat(currentWord);
				} else {
					throw new IllegalArgumentException(
							MESSAGE_INVALID_DATE_FORMAT);
				}
			} else {
				throw new IllegalArgumentException(MESSAGE_INVALID_DATE_FORMAT);
			}

			// get year
			if (!wordsList.isEmpty()) {
				currentWord = wordsList.peek();
				if (isYear(currentWord)) {
					date += "/" + currentWord;
					wordsList.poll();
				} else {
					// append the current year
					DateTime current = new DateTime();
					Integer year = current.getYear();
					date = date + "/" + year.toString();
					return date;
				}
			} else {
				// append the current year
				DateTime current = new DateTime();
				Integer year = current.getYear();
				date = date + "/" + year.toString();
				return date;
			}
			return date;
		}
		// if the format is along the lines of Thursday or next Thursday (to be
		// implemented)
	}

	// This function assumes startTime is of the format H.M, where H and M can
	// be any integer not starting with 0
	public static String getHour(String timeFormat) {
		if (timeFormat == null) {
			return null;
		}
		String[] temp = timeFormat.split("\\.");
		return temp[INDEX_HOUR];
	}

	// This function assumes startTime is of the format H.M, where H and M can
	// be any integer not starting with 0
	public static String getMinute(String timeFormat) {
		if (timeFormat == null) {
			return null;
		}
		String[] temp = timeFormat.split("\\.");
		return temp[INDEX_MINUTE];
	}

	// this function assumes dateFormat is of the format D/M/Y, where H and M
	// can be any integer not starting with 0
	public static String getYear(String dateFormat) {
		if (dateFormat == null) {
			return null;
		}
		String[] temp = dateFormat.split("/");
		return temp[INDEX_YEAR];
	}

	// this function assumes dateFormat is of the format D/M/Y, where H and M
	// can be any integer not starting with 0
	public static String getMonth(String dateFormat) {
		if (dateFormat == null) {
			return null;
		}
		String[] temp = dateFormat.split("/");
		return temp[INDEX_MONTH];
	}

	// this function assumes dateFormat is of the format D/M/Y, where H and M
	// can be any integer not starting with 0
	public static String getDay(String dateFormat) {
		if (dateFormat == null) {
			return null;
		}
		String[] temp = dateFormat.split("/");
		return temp[INDEX_DAY];
	}

	protected static boolean isMonth(String word) {
		word = word.toLowerCase();
		if (word.equals("january") || word.equals("february")
				|| word.equals("march") || word.equals("may")
				|| word.equals("june") || word.equals("july")
				|| word.equals("august") || word.equals("september")
				|| word.equals("october") || word.equals("november")
				|| word.equals("december")) {
			return true;
		} else {
			return false;
		}
	}

	protected static String convertMonthToNumberStringFormat(String word) {
		word = word.toLowerCase();
		if (word.equals("january")) {
			return "1";
		} else if (word.equals("february")) {
			return "2";
		} else if (word.equals("march")) {
			return "3";
		} else if (word.equals("april")) {
			return "4";
		} else if (word.equals("may")) {
			return "5";
		} else if (word.equals("june")) {
			return "6";
		} else if (word.equals("july")) {
			return "7";
		} else if (word.equals("august")) {
			return "8";
		} else if (word.equals("september")) {
			return "9";
		} else if (word.equals("october")) {
			return "10";
		} else if (word.equals("november")) {
			return "11";
		} else {
			return "12";
		}
	}

	protected static boolean isYear(String word) {
		if (word.length() == 4 && isInteger(word)) {
			return true;
		} else {
			return false;
		}
	}

	protected static boolean isInteger(String number) {
		try {
			Integer.parseInt(number);
			if (number.startsWith("0")) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
