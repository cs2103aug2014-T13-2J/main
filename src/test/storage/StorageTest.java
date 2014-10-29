package test.storage;

import static org.junit.Assert.*;

import main.storage.Storage;
import main.storage.Task;
import main.storage.TaskBuilder;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StorageTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConvertToDateTime() {
		DateTime result = Storage.convertToDateTime("2014-10-20T09:00:00.000+08:00");
		assertEquals("converted DateTime: ", new DateTime(2014, 10, 20, 9, 0, 0, 0), result);
	}

	@Test
	public void testConvertToBoolean() {
		boolean condition = Storage.convertToBoolean("false");
		assertEquals("convertToBoolean: ", false, condition);
		condition = Storage.convertToBoolean("true");
		assertEquals("convertToBoolean: ", true, condition);
	}

	@Test
	public void testWriteToFileAndReadFromFile() {
		String fileName = "data.csv";
		Storage s1 = Storage.getInstance();
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDate(new LocalDate(2014, 10, 20));
		builder.setStartTime(new LocalTime(20, 0));
		builder.setEndDate(new LocalDate(2014, 11, 21));
		builder.setEndTime(new LocalTime(23, 59));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		Task task1 = builder.buildTask();
		
		s1.addTask(task1);
		
		builder.setDescription("overseas");
		builder.setVenue("Germany");
		builder.setStartDate(new LocalDate(2015, 12, 20));
		builder.setStartTime(new LocalTime(20, 0));
		builder.setEndDate(new LocalDate(2015, 12, 31));
		builder.setEndTime(new LocalTime(23, 59));
		builder.setReminder(null);
		builder.setRecurrence(null);
		Task task2 = builder.buildTask();
		
		s1.addTask(task2);
		
		assertEquals("writeToFile: ", "Tasks added.", s1.writeToFile(fileName));
		
		s1.clearAllTasks();
		
		assertEquals("readFromFile: ", "Data read from storage.", s1.readFromFile(fileName));
		
	}

}
