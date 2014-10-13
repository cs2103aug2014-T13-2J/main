package test.logic;

import static org.junit.Assert.*;
import main.logic.AddHandler;
import main.logic.CommandHandler;

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
	public void testAddHandlerAndExecute() {
		String s1 = "add meeting with Prof at 11pm at CLB on 3/10/2014";
		CommandHandler e1 = new AddHandler(s1);
		assertEquals("Success message: ", "Task added!", e1.execute());
		
		String s2 = "add meeting with Prof at CLB at 11pm on 3/10";
		CommandHandler e2 = new AddHandler(s2);
		assertEquals("Success message: ", "Task added!", e2.execute());
		
		String s3 = "add meeting with Prof at CLB on 4/12 at 11pm";
		CommandHandler e3 = new AddHandler(s3);
		assertEquals("Success message: ", "Task added!", e3.execute());
		
		String s4 = "add gathering on 3/10/2014";
		CommandHandler e4 = new AddHandler(s4);
		assertEquals("Success message: ", "Task added!", e4.execute());
		
		String s5 = "add gathering on 3/10";
		CommandHandler e5 = new AddHandler(s5);
		assertEquals("Success message: ", "Task added!", e5.execute());
	
		String s6 = "add gathering on 3 October";	
		CommandHandler e6 = new AddHandler(s6);
		assertEquals("Success message: ", "Task added!", e6.execute());
		
		String s7 = "add gathering on 3 October 2014";
		CommandHandler e7 = new AddHandler(s7);
		assertEquals("Success message: ", "Task added!", e7.execute());
		
		String s8 = "add meeting from 3/10/2014 11pm to 4/10/2014 1am at Utown";
		CommandHandler e8 = new AddHandler(s8);
		assertEquals("Success message: ", "Task added!", e8.execute());
		
		String s9 = "add meeting from 3/10 to 4/10 at Utown";
		CommandHandler e9 = new AddHandler(s9);
		assertEquals("Success message: ", "Task added!", e9.execute());
		
		String s10 = "add meeting from 3 October 11pm to 4 October 1am at Utown";
		CommandHandler e10 = new AddHandler(s10);
		assertEquals("Success message: ", "Task added!", e10.execute());
		
		String s11 = "add meeting from 3/10 to 18/10";
		CommandHandler e11 = new AddHandler(s11);
		assertEquals("Success message: ", "Task added!", e11.execute());
		
		String s12 = "add meeting with Prof on 4/12 at CLB at 11pm";
		CommandHandler e12 = new AddHandler(s12);
		assertEquals("Success message: ", "Task added!", e12.execute());
		
		String s13 = "add meeting with Prof on 4/12 at 11pm at CLB";
		CommandHandler e13 = new AddHandler(s13);
		assertEquals("Success message: ", "Task added!", e13.execute());
	}

	@Test
	public void testConvertParsedDetailsToTask() {
		fail("Not yet implemented");
	}

}
