package test.logic;

import static org.junit.Assert.*;
import main.logic.AddHandler;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AddHandlerTest {

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
	public void testExecute() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddHandler() {
		AddHandler h = new AddHandler("meeting at 10pm");
		h.execute();
	}

	@Test
	public void testConvertParsedDetailsToTask() {
		fail("Not yet implemented");
	}

}
