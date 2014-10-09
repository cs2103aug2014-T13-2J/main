package main.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import org.joda.time.DateTime;

import main.storage.Task;
import main.storage.TaskBuilder;

public class AddHandler extends CommandHandler {
	
	private AddParser parser;

	public AddHandler(String details) {
		super(details);
		parser = new AddParser(details);
	}

	@Override
	public String execute() {
		String details = parser.getUserInput();
		
		String[] words = details.split(" ");
		
		ArrayList<String> wordsList = new ArrayList<String>(Arrays.asList(words));
		
		String description = getDescription(wordsList);
		Integer[] startTime = getTime(wordsList);
		
		ArrayList<Task> taskList = getCurrentTaskList();
		
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription(description);
		builder.setStartDateTime(new DateTime(2014, startTime[0], startTime[1], 9, 0, 0, 0));
		Task task = builder.buildTask();
		
		taskList.add(task);
		
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
	
	public Integer[] getTime(ArrayList<String> wordsList) {
		ListIterator<String> iterator = wordsList.listIterator();
		if(iterator.hasNext() && iterator.next().equals("at")) {
			String currentWord = iterator.next();
			if(isOfTimeFormat(currentWord)) {
				return convertTimeToIntegerArray(currentWord);
			}
		}
		return null;
	}
	
	public boolean isOfTimeFormat(String currentWord) {
		if( currentWord.matches("\\d") && ( currentWord.contains("am") || currentWord.contains("pm") )) {
			return true;
		} else {
			return false;
		}
	}
	
	public Integer[] convertTimeToIntegerArray(String word) {
		boolean addTwelveHours;
		if(word.subSequence(word.length() - 2, word.length()).equals("am")) {
			addTwelveHours = false;
		} else if (word.subSequence(word.length()-3, word.length()-1).equals("am")) {
			addTwelveHours = true;
		} else {
			System.out.println("Unexpected input in convertTimeToIntegerArray");
		}
		
		word = word.substring(0, word.length() - 2);
		String[] temp = word.split(".");
		Integer[] result = {0, 0};
		int tempNumber;
		for(int i = 0; i < temp.length; i++) {
			tempNumber = Integer.parseInt(temp[i]);
			result[i] = tempNumber;
		}
		
		return result;
	}

}
