package main.logic;

import java.util.Arrays;
import java.util.LinkedList;

public class AddParser extends CommandParser {
	
	final static String INVALID_DESCRIPTION_MESSAGE = "Sorry, we did not capture your description. Please try again.";
	final static String INVALID_TIME_FORMAT_MESSAGE = "Sorry we did not manage to capture the time. Please ensure you entered it in the correct format.";
	final static String INVALID_VENUE_MESSAGE = "Sorry we did not manage to capture the venue. Please try again.";
	
	private String userInput;
	
	public AddParser(String arguments) {
		super(arguments);
		userInput = arguments;
		this.parse();
	}
	
	public String getUserInput() {
		return this.userInput;
	}

	public String parse() {
		String description = null, venue = null, startDate = null, startTime = null, endDate = null, endTime = null;
		
		String[] userInput = this.getUserInput().split(" ");
		LinkedList<String> wordsList = new LinkedList<String>(Arrays.asList(userInput));

		description = getDescriptionAndTrimUserInput(wordsList);
		
		while(!wordsList.isEmpty()) {
			String currentReservedWord = wordsList.poll();
			//insert an assert that currentReservedWord is indeed a reserved word
			if(currentReservedWord.equals("at")) {
				String nextWord = wordsList.poll();
				//determine if next word is time or venue
				if(representsTime(nextWord)) {
					//if we have already parsed through start date or start time
					if(startTime == null) {
						startTime = getTime(nextWord);
					} else {
						//find endTime
						endTime = getTime(nextWord);
					}
				} else {
					//if it's not time, then the next word must represent venue
					venue = getVenueAndTrimUserInput(wordsList);
				}
			} else if(currentReservedWord.equals("on")) {
				//get date
				startDate = getDateAndTrimUserInput(wordsList);
				endDate = startDate;
			} else if(currentReservedWord.equals("from")) {
				//get start date
				//get start time
				//get end date
				//get end time
			} else if(currentReservedWord.equals("next")) {
				//get start date
				//get start time
				//set end date equal to start date
				//set end time equal to 2359
			}
		}

		return null;
	}
	
	//This function assumes that any of the reserved words will not be part of the description
	public static String getDescriptionAndTrimUserInput(LinkedList<String> wordsList) throws IllegalArgumentException {
		String currentWord, description = "";
		while(!wordsList.isEmpty()) {
			currentWord = wordsList.poll();
			if(!isReservedWord(currentWord)) {
				description += currentWord;
			}
		}
		if(description.equals("")) {
			throw new IllegalArgumentException(INVALID_DESCRIPTION_MESSAGE);
		}
		description = description.trim();
		return description;
	}
	
	public static boolean isReservedWord(String word) {
		if(word.equals("at") || word.equals("on") || word.equals("from") || word.equals("next")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean representsTime(String word) {
		if( word.matches("\\d") && 
				(word.substring(word.length()-2, word.length()).equals("am") 
						|| word.substring(word.length()-2, word.length()).equals("pm") ) ) {
			return true;
		} else {
			return false;
		}
	}
	
	//This function assumes that word will be of the format HH.MM(am/pm) or HH(am/pm)
	public static String getTime(String word) {
		boolean isPm;
		if(word.substring(word.length()-2, word.length()).equals("pm")) {
			isPm = true;
		} else {
			isPm = false;
		}
		word = word.substring(0, word.length()-2);
		Integer numTime = Integer.parseInt(word);
		if(isPm == true) {
			numTime += 12;
		}
		
		String stringTime = numTime.toString();
		return stringTime;
		
	}
	
	public static String getVenueAndTrimUserInput(LinkedList<String> wordsList) {
		String currentWord, venue = "";
		while(!wordsList.isEmpty()) {
			currentWord = wordsList.poll();
			if(!isReservedWord(currentWord)) {
				venue += currentWord;
			}
		}
		if(venue.equals("")) {
			throw new IllegalArgumentException(INVALID_VENUE_MESSAGE);
		}
		venue = venue.trim();
		return venue;
	}
	
	public static String getDateAndTrimUserInput(LinkedList<String> wordsList) {
		return null;
	}

}
