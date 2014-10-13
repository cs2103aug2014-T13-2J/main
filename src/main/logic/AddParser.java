package main.logic;

import java.util.Arrays;
import java.util.LinkedList;

import org.joda.time.DateTime;

public class AddParser extends CommandParser {

	final static String INVALID_DESCRIPTION_MESSAGE = "Sorry, we did not capture your description. Please try again.";
	final static String INVALID_TIME_FORMAT_MESSAGE = "Sorry we did not manage to capture the time. Please ensure you entered it in the correct format.";
	final static String INVALID_VENUE_MESSAGE = "Sorry we did not manage to capture the venue. Please try again.";
	final static String INVALID_DATE_FORMAT_MESSAGE = "Sorry we did not manage to capture the date. Please ensure you entered it in the correct format.";
	final static String DEFAULT_END_TIME = "23.59";
	final static String PARSE_SUCCESS_MESSAGE = "Parse successful.";
	final static Integer HOUR_INDEX = 0;
	final static Integer MINUTE_INDEX = 1;
	final static Integer DAY_INDEX = 0;
	final static Integer MONTH_INDEX = 1;
	final static Integer YEAR_INDEX = 2;

	private String userInput;

	public AddParser(String arguments) {
		super(arguments);
		userInput = arguments;
	}

	public String getUserInput() {
		return this.userInput;
	}

	public String parse() {
		String description = null, venue = null;
		String startDate = null, startDateYear = null, startDateMonth = null, startDateDay = null;
		String startTime = null, startTimeHour = null, startTimeMinute = null;
		String endDate = null, endDateYear = null, endDateMonth = null, endDateDay = null;
		String endTime = null, endTimeHour = null, endTimeMinute = null;

		String[] userInput = this.getUserInput().split(" ");
		LinkedList<String> wordsList = new LinkedList<String>(
				Arrays.asList(userInput));

		description = getDescriptionAndTrimUserInput(wordsList);
		while (!wordsList.isEmpty()) {
			String currentReservedWord = wordsList.poll();
			// insert an assert that currentReservedWord is indeed a reserved
			// word
			if (currentReservedWord.equals("at")) {
				String nextWord = wordsList.peek();
				// determine if next word is time or venue
				if (representsTime(nextWord)) {
					// if we have already parsed through or start time
					if (startTime == null) {
						startTime = getTime(wordsList);
						startTimeHour = getHour(startTime);
						startTimeMinute = getMinute(startTime);
						
						//set endTime to startTime by default, in the case that user only
						//specifies startTime without endTime
						endTime = startTime;
						endTimeHour = getHour(endTime);
						endTimeMinute = getMinute(endTime);
					} else {
						// find endTime
						endTime = getTime(wordsList);
						endTimeHour = getHour(endTime);
						endTimeMinute = getMinute(endTime);
					}
				} else {
					// if it's not time, then the next word must represent venue
					venue = getVenueAndTrimUserInput(wordsList);
				}
			} else if (currentReservedWord.equals("on")) { // get date
				startDate = getDateAndTrimUserInput(wordsList);
				endDate = startDate;
				
				startDateYear = getYear(startDate);
				startDateMonth = getMonth(startDate);
				startDateDay = getDay(startDate);
				endDateYear = getYear(endDate);
				endDateMonth = getMonth(endDate);
				endDateDay = getDay(endDate);

			} else if (currentReservedWord.equals("from")) {
				startDate = getDateAndTrimUserInput(wordsList);
				String nextWord = wordsList.peek();
				if(representsTime(nextWord)) {
					startTime = getTime(wordsList);
				}
			
				// remove the word "to"
				wordsList.poll();
				endDate = getDateAndTrimUserInput(wordsList);
				if(!wordsList.isEmpty()) {
					nextWord = wordsList.peek();
					if(representsTime(nextWord)) {
						endTime = getTime(wordsList);
					}
				}

				startDateYear = getYear(startDate);
				startDateMonth = getMonth(startDate);
				startDateDay = getDay(startDate);
				endDateYear = getYear(endDate);
				endDateMonth = getMonth(endDate);
				endDateDay = getDay(endDate);
				startTimeHour = getHour(startTime);
				startTimeMinute = getMinute(startTime);
				endTimeHour = getHour(endTime);
				endTimeMinute = getMinute(endTime);
			} else if (currentReservedWord.equals("next")) {
				startDate = getDateAndTrimUserInput(wordsList);
				startTime = getTime(wordsList);
				endDate = startDate;
				endTime = DEFAULT_END_TIME;
				
				startDateYear = getYear(startDate);
				startDateMonth = getMonth(startDate);
				startDateDay = getDay(startDate);
				endDateYear = getYear(endDate);
				endDateMonth = getMonth(endDate);
				endDateDay = getDay(endDate);
				startTimeHour = getHour(startTime);
				startTimeMinute = getMinute(startTime);
				endTimeHour = getHour(endTime);
				endTimeMinute = getMinute(endTime);
			}
		}

		this.setDescription(description);
		this.setVenue(venue);
		this.setStartDateYear(startDateYear);
		this.setStartDateMonth(startDateMonth);
		this.setStartDateDay(startDateDay);
		this.setStartTimeHour(startTimeHour);
		this.setStartTimeMinute(startTimeMinute);
		this.setEndDateYear(endDateYear);
		this.setEndDateMonth(endDateMonth);
		this.setEndDateDay(endDateDay);
		this.setStartTimeHour(startTimeHour);
		this.setStartTimeMinute(startTimeMinute);
		this.setEndTimeHour(endTimeHour);
		this.setEndTimeMinute(endTimeMinute);

		return PARSE_SUCCESS_MESSAGE;
	}

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
			throw new IllegalArgumentException(INVALID_DESCRIPTION_MESSAGE);
		}
		return description;
	}

	public static boolean isReservedWord(String word) {
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
	public static boolean representsTime(String word) {
		if(word.length() < 2) {
			return false;
		}
		String numPortion = word.substring(0, word.length() - 2);
		String amPortion = word.substring(word.length() - 2, word.length());
		// checks that H does not start with 0
		if (numPortion.startsWith("0")) {
			throw new IllegalArgumentException(INVALID_TIME_FORMAT_MESSAGE);
		}
		if (numPortion.contains(".")) {
			// Need the double backslash if we want to use . as the delimiter
			String[] numPortionParts = numPortion.split("\\.");
			if (numPortionParts.length != 2) {
				throw new IllegalArgumentException(INVALID_TIME_FORMAT_MESSAGE);
			}
			String numHour = numPortionParts[HOUR_INDEX];
			String numMinute = numPortionParts[MINUTE_INDEX];
			// checks that M does not start with 0
			if (numMinute.startsWith("0")) {
				throw new IllegalArgumentException(INVALID_TIME_FORMAT_MESSAGE);
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
			Integer numHour = Integer.parseInt(numPortionParts[HOUR_INDEX]);
			Integer numMinute = Integer.parseInt(numPortionParts[MINUTE_INDEX]);

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

	public static String getVenueAndTrimUserInput(LinkedList<String> wordsList) {
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
			throw new IllegalArgumentException(INVALID_VENUE_MESSAGE);
		}

		return venue;
	}

	public static String getDateAndTrimUserInput(LinkedList<String> wordsList) {
		String date = "";
		String currentWord = wordsList.poll();
		String[] arr = currentWord.split("/");
		// if the format is D/M/Y, where D, M, Y can be any integer, but cannot
		// start with 0
		if (arr.length == 3) {
			if (!isInteger(arr[DAY_INDEX]) || !isInteger(arr[MONTH_INDEX])
					|| !isInteger(arr[YEAR_INDEX])) {
				throw new IllegalArgumentException(INVALID_DATE_FORMAT_MESSAGE);
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
				throw new IllegalArgumentException(INVALID_DATE_FORMAT_MESSAGE);
			}
			// get month
			if (!wordsList.isEmpty()) {
				currentWord = wordsList.poll();
				if (isMonth(currentWord)) {
					date += "/" + convertMonthToNumberStringFormat(currentWord);
				} else {
					throw new IllegalArgumentException(
							INVALID_DATE_FORMAT_MESSAGE);
				}
			} else {
				throw new IllegalArgumentException(INVALID_DATE_FORMAT_MESSAGE);
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
	private static String getHour(String timeFormat) {
		if(timeFormat == null) {
			return null;
		}
		String[] temp = timeFormat.split("\\.");
		return temp[HOUR_INDEX];
	}

	// This function assumes startTime is of the format H.M, where H and M can
	// be any integer not starting with 0
	private static String getMinute(String timeFormat) {
		if(timeFormat == null) {
			return null;
		}
		String[] temp = timeFormat.split("\\.");
		return temp[MINUTE_INDEX];
	}

	// this function assumes dateFormat is of the format D/M/Y, where H and M
	// can be any integer not starting with 0
	private static String getYear(String dateFormat) {
		if(dateFormat == null) {
			return null;
		}
		String[] temp = dateFormat.split("/");
		return temp[YEAR_INDEX];
	}

	// this function assumes dateFormat is of the format D/M/Y, where H and M
	// can be any integer not starting with 0
	private static String getMonth(String dateFormat) {
		if(dateFormat == null) {
			return null;
		}
		String[] temp = dateFormat.split("/");
		return temp[MONTH_INDEX];
	}

	// this function assumes dateFormat is of the format D/M/Y, where H and M
	// can be any integer not starting with 0
	private static String getDay(String dateFormat) {
		if(dateFormat == null) {
			return null;
		}
		String[] temp = dateFormat.split("/");
		return temp[DAY_INDEX];
	}

	private static boolean isMonth(String word) {
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

	private static String convertMonthToNumberStringFormat(String word) {
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

	private static boolean isYear(String word) {
		if (word.length() == 4 && isInteger(word)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isInteger(String number) {
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
