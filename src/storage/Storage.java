package storage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;


public class Storage {
	
	public static String readFromFile(String fileName, ArrayList<Task> tasks) {
		return null;
	}
	
	public static String writeToFile() {
		return null;
	}
	
	public static String writeToFile(String fileName, ArrayList<Task> tasks) {
		//this function assumes that the ArrayList containing tasks is fully updated
		File file = new File(fileName);

		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			CSVWriter writer = new CSVWriter(new FileWriter(fileName));
			String[] currentEntry;
			for(Task task: tasks) {
				currentEntry = task.convertToCSVFormat().split("#!"); 
				writer.writeNext(currentEntry);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String add(String details) {
		return null;
	}
	
	public static String delete(String number) {
		return null;
	}
	
	public static String update(String details) {
		return null;
	}
	
	public static String search(String searchString) {
		return null;
	}
}
