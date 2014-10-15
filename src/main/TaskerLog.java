package main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

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
		} catch (SecurityException e) {
			logger.severe("TaskerLog securityException " +e.getMessage());
		} catch (IOException e) {
			logger.severe("TaskerLog IOException "+e.getMessage());
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void logException(String Exception) {

		MyFormatter formatter = new MyFormatter("Exception: ");
		fh.setFormatter(formatter);
		logger.severe(Exception);
	}
	
	public void logInfo(String input) {

		MyFormatter formatter = new MyFormatter("Info: ");
		fh.setFormatter(formatter);
		logger.info(input);
	}
	
	public static void logSystemInfo(String logInfo) {
		TaskerLog infoLogger = TaskerLog.getInstance();
		infoLogger.logInfo(logInfo);
	}

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