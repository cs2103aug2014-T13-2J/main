package logic;

import java.util.ArrayList;

public class AddParser extends CommandParser {
	
	public String parse(String userInput) {
		String description, venue, startDate, endDate, startTime;
		
		ArrayList<Integer> atList = new ArrayList<Integer>();
		ArrayList<Integer> onList = new ArrayList<Integer>();
		ArrayList<Integer> nextList = new ArrayList<Integer>();
		
		String[] words = userInput.split(" ");
		
		String currentWord;
		//run through every word and look for "on" or "at" or "next" and store their positions
		for(int i = 0; i < words.length; i++) {
			currentWord = words[i].toLowerCase();
			if(currentWord.equals("on")) {
				onList.add(i);
			}
			else if(currentWord.equals("at")) {
				atList.add(i);
			}
			else if(currentWord.equals("next")) {
				nextList.add(i);
			}
		}
		
		
		return null;
	}
}
