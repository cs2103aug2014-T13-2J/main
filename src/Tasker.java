import java.util.Scanner;

public class Tasker {
	
	public static final String MESSAGE_WELCOME = "Welcome to Tasker!";
	
	public static void main(String[] args) {
		initializeEnvironment();
		readAndExecuteCommands();
	}
	
	public static void initializeEnvironment() {
		System.out.println(MESSAGE_WELCOME);
	}
	
	public static void readAndExecuteCommands() {
		
	}
	
	public static String readCmd(Scanner sc) {
		return null;
	}

}
