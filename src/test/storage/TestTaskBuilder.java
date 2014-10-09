package test.storage;

import static org.junit.Assert.*;
import main.storage.TaskBuilder;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.api.services.calendar.model.Event.Reminders;

public class TestTaskBuilder {

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
	public void testTaskBuilder() {
		TaskBuilder builder = new TaskBuilder();
		builder.setDescription("meeting");
		builder.setVenue("CLB");
		builder.setStartDateTime(new DateTime());
		builder.setEndDateTime(new DateTime());
//		builder.setHasReminder(true);
	}

}
