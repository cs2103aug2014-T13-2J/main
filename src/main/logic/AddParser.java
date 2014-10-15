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
				endTime = startTime;
				
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

		return MESSAGE_PARSE_SUCCESS;
	}

}
