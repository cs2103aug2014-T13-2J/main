package main.logic;

public class SearchParser extends CommandParser {
	
	@Override
	public String parse(String userInput) {
		// search should be done here
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
}
