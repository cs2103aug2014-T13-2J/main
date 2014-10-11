package main.logic;

import java.util.Arrays;
import java.util.LinkedList;

public class AddParser extends CommandParser {
	
	final static String INVALID_DESCRIPTION_MESSAGE = "Sorry, we did not recognize your description. Please try again.";
	
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
		
		String currentReservedWord;
		while(!wordsList.isEmpty()) {
			currentReservedWord = wordsList.poll();
			//assert that currentReservedWord is indeed a reserved word
			if(currentReservedWord.equals("at")) {
				//determine if next word is time or venue
					//if time, get time
					//else if venue, get venue
					//else throw exception
			} else if(currentReservedWord.equals("on")) {
				//get date
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
		//if "at", determine venue or time
		venue = getVenueAndTrimUserInput(wordsList);
			//if venue, get venue
			//else if time, get time
			//else throw exception
		//else if "on", determine date
		//else if "next", determine date 
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
	
	public static String getVenueAndTrimUserInput(LinkedList<String> wordsList) {
		
	}

}
