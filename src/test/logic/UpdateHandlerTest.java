package test.logic;

import static org.junit.Assert.*;
import main.logic.AddHandler;
import main.logic.CommandHandler;
import main.logic.UpdateHandler;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UpdateHandlerTest {

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
		String s1 = "meeting with Prof at 11pm at CLB on 3/10/2014";
		CommandHandler e1 = new AddHandler(s1);
		e1.execute();
		String ss1 = "1 description date with girlfriend";
		UpdateHandler u1 = new UpdateHandler(ss1);
		u1.execute();
		assertEquals("New description:", "date with girlfriend", UpdateHandler.getTask(1).getDescription());

		String s2 = "meeting with Prof at Central Forum at 11pm on 3/10";
		CommandHandler e2 = new AddHandler(s2);
		e2.execute();
		String ss2 = "2 venue Reeds cafe";
		UpdateHandler u2 = new UpdateHandler(ss2);
		u2.execute();
		assertEquals("New description:", "Reeds cafe", UpdateHandler.getTask(2).getVenue());
		
		String s3 = "meeting with Prof at CLB on 4/12 at 11pm";
		CommandHandler e3 = new AddHandler(s3);
		e3.execute();
		String ss3 = "3 start 9 October";
		UpdateHandler u3 = new UpdateHandler(ss3);
		u3.execute();
		assertEquals("New description:", "2014-10-09", UpdateHandler.getTask(3).getStartDate().toString());
		
		String s4 = "gathering on 3/10/2014";
		CommandHandler e4 = new AddHandler(s4);
		e4.execute();
		String ss4 = "4 end 9/10";
		UpdateHandler u4 = new UpdateHandler(ss4);
		u4.execute();
		assertEquals("New description:", "2014-10-09", UpdateHandler.getTask(4).getEndDate().toString());

		String s5 = "gathering on 3/10";
		CommandHandler e5 = new AddHandler(s5);
		e5.execute();
		String ss5 = "5 start 11am";
		UpdateHandler u5 = new UpdateHandler(ss5);
		u5.execute();
		assertEquals("New description:", "11:00:00.000", UpdateHandler.getTask(5).getStartTime().toString());

		String s6 = "meeting from 3/10/2014 11pm to 4/10/2014 1am at Utown";	
		CommandHandler e6 = new AddHandler(s6);
		e6.execute();
		String ss6 = "6 end 2am";
		UpdateHandler u6 = new UpdateHandler(ss6);
		u6.execute();
		assertEquals("New description:", "02:00:00.000", UpdateHandler.getTask(6).getEndTime().toString());
		
		String s7 = "gathering on 3 October 2014";
		CommandHandler e7 = new AddHandler(s7);
		e7.execute();
		String ss7 = "7 recurrence weekly";
		UpdateHandler u7 = new UpdateHandler(ss7);
		u7.execute();
		assertEquals("New description:", "weekly", UpdateHandler.getTask(7).getRecurrence());
		
		String s8 = "meeting from 3/10/2014 11pm to 4/10/2014 1am at Utown";
		CommandHandler e8 = new AddHandler(s8);
		e8.execute();
		String ss8 = "8 complete";
		UpdateHandler u8 = new UpdateHandler(ss8);
		u8.execute();
		assertEquals("New description:", true, UpdateHandler.getTask(8).getCompleted());
		
		String ss9 = "8 incomplete";
		UpdateHandler u9 = new UpdateHandler(ss9);
		u9.execute();		
		assertEquals("New description:", false, UpdateHandler.getTask(8).getCompleted());
	}

}
