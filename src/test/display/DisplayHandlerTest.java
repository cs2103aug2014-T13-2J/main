package test.display;

import static org.junit.Assert.*;

import java.util.ArrayList;

import main.logic.AddHandler;
import main.logic.CommandHandler;
import main.logic.DisplayHandler;
import main.storage.Storage;
import main.storage.Task;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DisplayHandlerTest {

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

/**	@Test
	public void testExecute() {
		fail("Not yet implemented");
	}

	@Test
	public void testDisplayHandler() {
		fail("Not yet implemented");
	}
**/
	@Test
	public void testDisplayTask() {
		String s1 = "meeting with Prof at 11pm at CLB on 3/10/2014";
		CommandHandler e1 = new AddHandler(s1);
		assertEquals("meeting with Prof at CLB on 2014-10-03 at 23:00 added!", e1.execute());
		
		String s2 = "meeting with Prof at CLB at 11pm on 3/10";
		CommandHandler e2 = new AddHandler(s2);
		assertEquals("meeting with Prof at CLB on 2014-10-03 at 23:00 added!", e2.execute());
		
		String s3 = "meeting with Prof at CLB on 4/12 at 11pm";
		CommandHandler e3 = new AddHandler(s3);
		assertEquals("meeting with Prof at CLB on 2014-12-04 at 23:00 added!", e3.execute());
		
		String s4 = "gathering on 3/10/2014";
		CommandHandler e4 = new AddHandler(s4);
		assertEquals("1", e4.execute());
		
		String s5 = "gathering on 3/10";
		CommandHandler e5 = new AddHandler(s5);
		assertEquals("1", e5.execute());
		
		String s6 = "gathering on 3 October";	
		CommandHandler e6 = new AddHandler(s6);
		assertEquals("1", e6.execute());
		
		String s7 = "gathering on 3 October 2014";
		CommandHandler e7 = new AddHandler(s7);
		assertEquals("1", e7.execute());
		
		String s8 = "meeting from 3/10/2014 11pm to 4/10/2014 1am at Utown";
		CommandHandler e8 = new AddHandler(s8);
		assertEquals("1", e8.execute());
		
		String s9 = "meeting from 3/10 to 4/10 at Utown";
		CommandHandler e9 = new AddHandler(s9);
		assertEquals("1", e9.execute());
		
		String s10 = "meeting from 3 October 11pm to 4 October 1am at Utown";
		CommandHandler e10 = new AddHandler(s10);
		assertEquals("1", e10.execute());
		
		String s11 = "meeting from 3/10 to 18/10";
		CommandHandler e11 = new AddHandler(s11);
		assertEquals("1", e11.execute());
		
		String s12 = "meeting with Prof on 4/12 at CLB at 11pm";
		CommandHandler e12 = new AddHandler(s12);
		assertEquals("1", e12.execute());
		
		String s13 = "meeting with Prof on 4/12 at 11pm at CLB";
		CommandHandler e13 = new AddHandler(s13);
		assertEquals("1", e13.execute());
		
	}

}
