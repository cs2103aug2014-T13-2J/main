package logic;

import java.util.ArrayList;

public class Logic {
	
	public static String addTask(String details) {
		String description, venue, startDate, endDate, startTime;
		
		ArrayList<Integer> atList = new ArrayList<Integer>();
		ArrayList<Integer> onList = new ArrayList<Integer>();
		ArrayList<Integer> nextList = new ArrayList<Integer>();
		
		String[] words = details.split(" ");
		
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

	public static String deleteTask(String number) {
		return null;
	}
	
	public static String updateTask(String details) {
		return null;
	}
	
	public static <ArrayList>String searchTask(String searchString) {
		ArrayList<String> linesWithTheKey = new ArrayList<String>();
		ArrayList<String> copyOfEntries = new ArrayList<String>();
		String lowerCaseKey = "";
		
		if(list.isEmpty()){
			return null;
		}
		lowerCaseKey = string.toLowerCase();
		
		for(int i = 0; i < list.size(); i++){
			copyOfEntries.add(list.get(i));
		}
		
		for(int i = 0; i < list.size(); i++){
			if(copyOfEntries.get(i).toLowerCase().contains(lowerCaseKey)){
				linesWithTheKey.add(list.get(i));
			}
		}
		
		for (int i=0; i < linesWithTheKey.size(); i++) {
			System.out.println((i+1) + "." + linesWithTheKey.get(i));
		}
		return linesWithTheKey;
	}
	
	public static String undo() {
		return null;
	}
	
	public static String repeatTask(String number) {
		return null;
	}
	
	public static String markAsComplete(String number) {
		return null;
	}
}
