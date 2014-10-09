package main.logic;

import java.util.ArrayList;
import java.util.Arrays;

public class AddParser extends CommandParser {
	
	private String userInput;
	
	public AddParser(String arguments) {
		super(arguments);
		userInput = arguments;
	}

	public String parse() {
		String description=null, venue=null, startDate=null, endDate=null, startTime=null, endTime=null;
		
		String[] words = userInput.split(" ");
		
		ArrayList<String> wordsList = new ArrayList<String>(Arrays.asList(words));
		
		description = getDescription(wordsList);
		venue = getVenue(wordsList);
		
		/**if(wordsList.contains("from") && wordsList.contains("to")) {
			startDate = getStartDate(wordsList);
			endDate = getEndDate(wordsList);
			startTime = getStartTime(wordsList);
			endTime = getEndTime(wordsList);
		} else {
			startDate = getStartDate(wordsList);
			startTime = getStartTime(wordsList);
			endDate = startDate;
			endTime = startTime;
		}**/
		
		return null;
	}
	
	public String getDescription(ArrayList<String> wordsList) {
		String result = "";
		for(String word: wordsList)
			if(isNotReservedWord(word)) {
				result += word;
			}
		return result;
	}
	
	public boolean isNotReservedWord(String word) {
		if(word.equals("on") || word.equals("at") || word.equals("next")) {
			return false;
		} else {
			return true;
		}
	}
	
	public String getVenue(ArrayList<String> wordsList) {
		return null;
	}
}
