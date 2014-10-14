package main.logic;

import java.util.ArrayList;
import java.util.Collections;

public class DeleteParser extends CommandParser {
	
	private String arguments;
	private ArrayList<Integer> listOfIndexes;

	public DeleteParser(String arguments) {
		super(arguments);
		this.arguments = arguments;
		listOfIndexes = new ArrayList<>();
	}

	@Override
	public String parse() {
		int index = 0;
		String[] indexes = arguments.split(" ");
		for (String indexString : indexes) {
			index = Integer.parseInt(indexString) - 1;
			listOfIndexes.add(index);
		}
		Collections.sort(listOfIndexes);
		Collections.reverse(listOfIndexes);
		return null;
	}
	
	public ArrayList<Integer> getListOfIndexes() {
		return listOfIndexes;
	}
}
