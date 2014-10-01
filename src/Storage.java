import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Storage {
	
	public static String writeToFile() {
		return null;
	}
	
	public static void writeToFile(String fileName, ArrayList<Task> tasks) {
		File file = new File(fileName);

		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			PrintWriter printWriter = new PrintWriter(file);
			String text;
			for (int i = 0; i < tasks.size(); i++) {
				text = i + 1 + ". " + tasks.get(i);
				printWriter.println(text);
			}
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
