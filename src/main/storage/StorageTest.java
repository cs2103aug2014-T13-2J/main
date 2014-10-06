package main.storage;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.joda.time.DateTime;
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
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDateTime(new DateTime(2014, 10, 20, 9, 0, 0, 0));
		builder.setEndDateTime(new DateTime(2014, 11, 21, 10, 0, 0, 0));
		builder.setReminder(new DateTime(2014, 9, 20, 21, 30, 0, 0));
		builder.setRecurrence("weekly");
		Task task1 = builder.buildTask();
		
		tasks.add(task1);
		
		builder.setDescription("overseas");
		builder.setVenue("Germany");
		builder.setStartDateTime(new DateTime(2014, 11, 20, 20, 0, 0, 0));
		builder.setEndDateTime(new DateTime(2014, 11, 30, 23, 0, 0, 0));
		builder.setReminder(null);
		builder.setRecurrence(null);
		Task task2 = builder.buildTask();
		
		tasks.add(task2);
		
		assertEquals("writeToFile: ", "Tasks added.", Storage.writeToFile(fileName, tasks));
		
		tasks.clear();
		
		assertEquals("readFromFile: ", "Data read from storage.", Storage.readFromFile(fileName, tasks));
		
		for(Task task: tasks) {
			System.out.println(task);
		}
	}

}
