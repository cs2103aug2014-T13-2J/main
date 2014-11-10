package main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
//@author A0100239W
/*
 * This class contains the logging function used for defensive coding. 
 */
public class TaskerLog {
	private static TaskerLog TaskerLogger = null;
	private FileHandler fh;
	private final static String fileName = "TaskerLogger.txt";
	private final static Logger logger = Logger.getLogger("TaskerLogger");

	public static TaskerLog getInstance() {
		if (TaskerLogger == null) {
			TaskerLogger = new TaskerLog();
		}
		return TaskerLogger;
	}

	private TaskerLog() {
		try {
			fh = new FileHandler(fileName, false);
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
		} catch (SecurityException e) {
			logger.severe("TaskerLog securityException " + e.getMessage());
		} catch (IOException e) {
			logger.severe("TaskerLog IOException " + e.getMessage());
		}
	}

	public String getFileName() {
		return fileName;
	}

	private void logException(String Exception) {

		MyFormatter formatter = new MyFormatter("Exception: ");
		fh.setFormatter(formatter);
		logger.severe(Exception);
	}

	private void logInfo(String input) {

		MyFormatter formatter = new MyFormatter("Info: ");
		fh.setFormatter(formatter);
		logger.info(input);
	}
	
	/*
	 * This method is the public method used to log information.
	 */
	public static void logSystemInfo(String logInfo) {
		TaskerLog infoLogger = TaskerLog.getInstance();
		infoLogger.logInfo(logInfo);
	}
	
	/*
	 * This method is the public method used to log exceptions.
	 */
	public static void logSystemExceptionError(String logMessage) {
		TaskerLog exceptionLogger = TaskerLog.getInstance();
		exceptionLogger.logException(logMessage);
	}
}

class MyFormatter extends Formatter {
	private String callerInfo;

	public MyFormatter(String callerInfo) {
		if (callerInfo == null) {
			this.callerInfo = "";
		}
		this.callerInfo = callerInfo;
	}

	@Override
	public String format(LogRecord record) {
		return callerInfo + record.getMessage() + "\n";
	}
}