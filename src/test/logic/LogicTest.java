package test.logic;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class LogicTest {

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
	public void testAddTask() {
		String details1 = "add meeting with Prof at CLB next Thursday at 11pm";
		String details2 = "add meeting on next Thursday from 11pm to 1am at Utown";
		String details3 = "add gathering on 3/10/2014";
		String details4 = "add mid terms on 3/10 at 9am";
		String details5 = "add make up lecture on 3 October";
		String details6 = "overseas from next Monday to next Friday";
		String details7 = "overseas from 3/10 to 18/10";
		
		String result1 = "New task added. Description: meeting with Prof, Venue: CLB, Date: 091014, Time: 2300";
		String result2 = "New task added. Description: meeting, Venue: Utown, Start date: 091014, Start time: 2300, End date: 101014, End time: 0100";
		String result3 = "New task added. Description: gathering, Date: 03102014";
		String result4 = "New task added. Description: mid terms, Date: 03102014, Time: 0900";
		String result5 = "New task added. Description: make up lecture, Date: 03102014";
		String result6 = "New task added. Description: overseas, Start date: 05102014, End date: 10102014";
		String result7 = "New task added. Description: overseas, Start date: 05102014, End date: 10102014";
		
//		assertEquals()
//		addTask(details2);
//		addTask(details3);
//		addTask(details4);
//		addTask(details5);
	}

}
