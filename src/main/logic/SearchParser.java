package main.logic;
import java.util.ArrayList;

public class SearchParser extends CommandParser {
	
	public SearchParser(String arguments) {
		super(arguments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String parse() {
		// search should be done here
		return null;
	}

	public String parser(String userInput) {
		 ArrayList<String> linesWithTheKey = new ArrayList<String>();
		 ArrayList<String> copyOfEntries = new ArrayList<String>(); 
		 ArrayList<String> list = new ArrayList<String>();

		 String lowerCaseKey = "";
		 
			if(list.isEmpty()){ 
				return null;
			}
			
			lowerCaseKey = userInput.toLowerCase();
			
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
			
			return null;
			
		}

	}
