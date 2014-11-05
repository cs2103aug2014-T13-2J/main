package main.logic;

import java.util.Arrays;
import java.util.LinkedList;

public class AddParser extends CommandParser {

	public AddParser(String arguments) {
		super(arguments);
		userInput = arguments;
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
			String currentReservedWord = wordsList.peek();
			// insert an assert that currentReservedWord is indeed a reserved
			// word
			if (currentReservedWord.equals("at")) {
				//remove reserved word
				removeCurrentWord(wordsList);
				String nextWord = wordsList.peek();
				// determine if next word is time or venue
				if (representsTime(nextWord)) {
					startTime = getTimeAndTrimUserInput(wordsList);
					// set endTime to startTime by default, in the case that
					// user only
					// specifies startTime without endTime
					endTime = startTime;
				} else {
					// if it's not time, then the next word must represent venue
					venue = getVenueAndTrimUserInput(wordsList);
				}
			} else if (currentReservedWord.equals("on")) { // get date
				//remove reserved word
				removeCurrentWord(wordsList);
				startDate = getDateAndTrimUserInput(wordsList);
				endDate = startDate;
			} else if (currentReservedWord.equals("from")) {
				//remove reserved word
				removeCurrentWord(wordsList);
				String nextWord = viewNextWord(wordsList);
				if (representsTime(nextWord)) {
					startTime = getTimeAndTrimUserInput(wordsList);
					// remove the word "to"
					removeCurrentWord(wordsList);
					endTime = getTimeAndTrimUserInput(wordsList);
				} else {
					startDate = getDateAndTrimUserInput(wordsList);
					nextWord = viewNextWord(wordsList);
					if (representsTime(nextWord)) {
						startTime = getTimeAndTrimUserInput(wordsList);
						// remove the word "to"
						removeCurrentWord(wordsList);
						endDate = getDateAndTrimUserInput(wordsList);
						endTime = getTimeAndTrimUserInput(wordsList);
					} else {
						// remove the word "to"
						removeCurrentWord(wordsList);
						endDate = getDateAndTrimUserInput(wordsList);
					}
				}

			} else if (currentReservedWord.equals("next")) {
				startDate = getDateAndTrimUserInput(wordsList);				
				endDate = startDate;
			}
		}
		
		//if no time and date, reject
		if(startDate == null && startTime == null) {
			throw new IllegalArgumentException(MESSAGE_REQUIRE_START_TIME_OR_DATE);
		} else if (startDate == null && startTime != null) {
			//if time but no date, set today
			startDateYear = generateYear();
			startDateMonth = generateMonth();
			startDateDay = generateDay();
			endDateYear = startDateYear;
			endDateMonth = startDateMonth;
			endDateDay = startDateDay;
		} else {
			startDateYear = getYear(startDate);
			startDateMonth = getMonth(startDate);
			startDateDay = getDay(startDate);
			endDateYear = getYear(endDate);
			endDateMonth = getMonth(endDate);
			endDateDay = getDay(endDate);
		}
		
		startTimeHour = getHour(startTime);
		startTimeMinute = getMinute(startTime);
		endTimeHour = getHour(endTime);
		endTimeMinute = getMinute(endTime);
		
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

		return MESSAGE_PARSE_SUCCESS;
	}
	
	private static void removeCurrentWord(LinkedList<String> wordsList) {
		wordsList.poll();
	}
	
	private static String viewNextWord(LinkedList<String> wordsList) {
		return wordsList.peek();
	}

}
