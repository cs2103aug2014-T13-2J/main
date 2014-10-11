package test.logic;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;

import main.logic.AddHandler;
import main.logic.AddParser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AddParserTest {

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
	public void testParse() {
		String s1 = "add meeting with Prof at 11pm at CLB on 3/10/2014";
		String s2 = "add meeting with Prof at CLB at 11pm on 3/10";
		String s3 = "add meeting with Prof at CLB on 4/12 at 11pm";
		String s4 = "add gathering on 3/10/2014";
		String s5 = "add gathering on 3/10";
		String s6 = "add gathering on 3 October";
		String s7 = "add gathering on 3 October 2014";
		String s8 = "add meeting from 3/10/2014 11pm to 4/10/2014 1am Utown";
		String s9 = "add meeting from 3/10 to 4/10 at Utown";
		String s10 = "add meeting from 3 October 11pm to 4 October 1am at Utown";
		String s11 = "add meeting from 3/10 to 18/10";
		String s12 = "add meeting with Prof on 4/12 at CLB at 11pm";
		String s13 = "add meeting with Prof on 4/12 at 11pm at CLB";
		
		AddParser p1 = new AddParser(s1);
		AddParser p2 = new AddParser(s2);
		AddParser p3 = new AddParser(s3);
		AddParser p4 = new AddParser(s4);
		AddParser p5 = new AddParser(s5);
		AddParser p6 = new AddParser(s6);
		AddParser p7 = new AddParser(s7); 
		AddParser p8 = new AddParser(s8); 
		AddParser p9 = new AddParser(s9); 
		AddParser p10 = new AddParser(s10); 
		AddParser p11 = new AddParser(s11);  
		AddParser p12 = new AddParser(s12); 
		AddParser p13 = new AddParser(s13);
		
		assertEquals("Parser: ", "Parse successful.", p1.parse());
		assertEquals("Parser: ", "Parse successful.", p2.parse());
		assertEquals("Parser: ", "Parse successful.", p3.parse());
		assertEquals("Parser: ", "Parse successful.", p4.parse());
		assertEquals("Parser: ", "Parse successful.", p5.parse());
		assertEquals("Parser: ", "Parse successful.", p6.parse());
		assertEquals("Parser: ", "Parse successful.", p7.parse()); 
		assertEquals("Parser: ", "Parse successful.", p8.parse());  
		assertEquals("Parser: ", "Parse successful.", p9.parse()); 
		assertEquals("Parser: ", "Parse successful.", p10.parse()); 
		assertEquals("Parser: ", "Parse successful.", p11.parse()); 
		assertEquals("Parser: ", "Parse successful.", p12.parse()); 
		assertEquals("Parser: ", "Parse successful.", p13.parse()); 
		
	}
	
	@Test
	public void testGetDescriptionAndTrimUserInput() {
		String s1 = "meeting at 10pm";
		LinkedList<String> l1 = new LinkedList<String>(Arrays.asList(s1.split(" ")));
		String s2 = "NUS Overseas College Fireside Chat at CLB";
		LinkedList<String> l2 = new LinkedList<String>(Arrays.asList(s2.split(" ")));
		String s3 = "Study session at engineering at 2pm";
		LinkedList<String> l3 = new LinkedList<String>(Arrays.asList(s3.split(" ")));
		String s4 = "Overseas in Germany from Thursday to Friday";		
		LinkedList<String> l4 = new LinkedList<String>(Arrays.asList(s4.split(" ")));
		String s5 = "EE2021 Midterms next Monday at 9am";
		LinkedList<String> l5 = new LinkedList<String>(Arrays.asList(s5.split(" ")));
		String s6 = "Beach party on 12/10";
		LinkedList<String> l6 = new LinkedList<String>(Arrays.asList(s6.split(" ")));
		String s7 = "";
		LinkedList<String> l7 = new LinkedList<String>(Arrays.asList(s7.split(" ")));		
		String s8 = "meeting with Prof";
		LinkedList<String> l8 = new LinkedList<String>(Arrays.asList(s8.split(" ")));

		
		assertEquals("Description", "meeting", AddParser.getDescriptionAndTrimUserInput(l1));
		assertEquals("Description", "NUS Overseas College Fireside Chat", AddParser.getDescriptionAndTrimUserInput(l2));
		assertEquals("Description", "Study session", AddParser.getDescriptionAndTrimUserInput(l3));
		assertEquals("Description", "Overseas in Germany", AddParser.getDescriptionAndTrimUserInput(l4));
		assertEquals("Description", "EE2021 Midterms", AddParser.getDescriptionAndTrimUserInput(l5));
		assertEquals("Description", "Beach party", AddParser.getDescriptionAndTrimUserInput(l6));
		//assertEquals("Description", new IllegalArgumentException(), AddParser.getDescriptionAndTrimUserInput(l7));		assertEquals("Description", new IllegalArgumentException(), AddParser.getDescriptionAndTrimUserInput(l7));
		assertEquals("Description", "meeting with Prof", AddParser.getDescriptionAndTrimUserInput(l8));



		
	}

	@Test
	public void testRepresentsTime() {
		String s1 = "11am";
		String s2 = "12pm";
		String s3 = "9am";
		String s4 = "1pm";
		String s5 = "0pm";
		String s6 = "20am";
		String s7 = "20 pm";
		String s8 = "0800";
		String s9 = "09am";
		String s10 = "02pm";
		String s11 = "1a2m";
		String s12 = "1a2am";
		String s13 = "12depm";
		String s14 = "12.30am";
		String s15 = "12.30pm";
		String s16 = "12323.12321am";
		String s17 = "0900am";
		String s18 = "0900.12321pm";
		String s19 = "900.02321pm";


		
		assertEquals("Represents time: ", true, AddParser.representsTime(s1));
		assertEquals("Represents time: ", true, AddParser.representsTime(s2));
		assertEquals("Represents time: ", true, AddParser.representsTime(s3));
		assertEquals("Represents time: ", true, AddParser.representsTime(s4));
		//assertEquals("Represents time: ", false, AddParser.representsTime(s5));
		assertEquals("Represents time: ", true, AddParser.representsTime(s6));
		assertEquals("Represents time: ", false, AddParser.representsTime(s7));
		//assertEquals("Represents time: ", false, AddParser.representsTime(s8));
		//assertEquals("Represents time: ", true, AddParser.representsTime(s9));
		//assertEquals("Represents time: ", true, AddParser.representsTime(s10));
		assertEquals("Represents time: ", false, AddParser.representsTime(s11));
		assertEquals("Represents time: ", false, AddParser.representsTime(s12));
		assertEquals("Represents time: ", false, AddParser.representsTime(s13));
		assertEquals("Represents time: ", true, AddParser.representsTime(s14));
		assertEquals("Represents time: ", true, AddParser.representsTime(s15));
		assertEquals("Represents time: ", true, AddParser.representsTime(s16));
		//assertEquals("Represents time: ", false, AddParser.representsTime(s17));
		//assertEquals("Represents time: ", false, AddParser.representsTime(s18));
		//assertEquals("Represents time: ", false, AddParser.representsTime(s19));

	} 

	@Test
	public void testGetTime() {
		String s1 = "9am at CLB";
		LinkedList<String> l1 = new LinkedList<String>(Arrays.asList(s1.split(" ")));
		String s2 = "12pm at CLB";
		LinkedList<String> l2 = new LinkedList<String>(Arrays.asList(s2.split(" ")));
		String s3 = "12.30am at CLB";
		LinkedList<String> l3 = new LinkedList<String>(Arrays.asList(s3.split(" ")));
		String s4 = "9.30pm at CLB";
		LinkedList<String> l4 = new LinkedList<String>(Arrays.asList(s4.split(" ")));
		
		assertEquals("Time: ", "9.0", AddParser.getTime(l1));
		assertEquals("Time: ", "12.0", AddParser.getTime(l2));
		assertEquals("Time: ", "0.30", AddParser.getTime(l3));
		assertEquals("Time: ", "21.30", AddParser.getTime(l4));
	}

	@Test
	public void testGetVenueAndTrimUserInput() {
		String s1 = "Zouk at 11pm";
		LinkedList<String> l1 = new LinkedList<String>(Arrays.asList(s1.split(" ")));
		String s2 = "MPSH1 from 5pm to 6pm";
		LinkedList<String> l2 = new LinkedList<String>(Arrays.asList(s2.split(" ")));
		String s3 = "CLB next Thursday";
		LinkedList<String> l3 = new LinkedList<String>(Arrays.asList(s3.split(" ")));
		String s4 = "SRC on 11/10";
		LinkedList<String> l4 = new LinkedList<String>(Arrays.asList(s4.split(" ")));
		String s5 = "Jalan Kayu Street 31 on 5 October";
		LinkedList<String> l5 = new LinkedList<String>(Arrays.asList(s5.split(" ")));
		String s6 = " on 25 December";
		LinkedList<String> l6 = new LinkedList<String>(Arrays.asList(s6.split(" ")));


		
		assertEquals("Venue: ", "Zouk", AddParser.getVenueAndTrimUserInput(l1));
		assertEquals("Venue: ", "MPSH1", AddParser.getVenueAndTrimUserInput(l2));
		assertEquals("Venue: ", "CLB", AddParser.getVenueAndTrimUserInput(l3));
		assertEquals("Venue: ", "SRC", AddParser.getVenueAndTrimUserInput(l4));
		assertEquals("Venue: ", "Jalan Kayu Street 31", AddParser.getVenueAndTrimUserInput(l5));
		//assertEquals("Venue: ", "", AddParser.getVenueAndTrimUserInput(l6));
		
	}

	@Test
	public void testGetDateAndTrimUserInput() {
		String s1 = "2/10/2015 at CLB";
		LinkedList<String> l1 = new LinkedList<String>(Arrays.asList(s1.split(" ")));
		String s2 = "2/10/2014";
		LinkedList<String> l2 = new LinkedList<String>(Arrays.asList(s2.split(" ")));
		String s3 = "3/9 at CLB";
		LinkedList<String> l3 = new LinkedList<String>(Arrays.asList(s3.split(" ")));
		String s4 = "3/9";
		LinkedList<String> l4 = new LinkedList<String>(Arrays.asList(s4.split(" ")));
		String s5 = "99/12 at CLB";
		LinkedList<String> l5 = new LinkedList<String>(Arrays.asList(s5.split(" ")));
		String s6 = "99/12";
		LinkedList<String> l6 = new LinkedList<String>(Arrays.asList(s6.split(" ")));
		String s7 = "35/1 at CLB";
		LinkedList<String> l7 = new LinkedList<String>(Arrays.asList(s7.split(" ")));
		String s8 = "35/1";
		LinkedList<String> l8 = new LinkedList<String>(Arrays.asList(s8.split(" ")));
		String s9 = "42 October 2050";
		LinkedList<String> l9 = new LinkedList<String>(Arrays.asList(s9.split(" ")));
		String s10 = "42 October";
		LinkedList<String> l10 = new LinkedList<String>(Arrays.asList(s10.split(" ")));
		String s11 = "27 February 1002 at CLB";
		LinkedList<String> l11 = new LinkedList<String>(Arrays.asList(s11.split(" ")));
		String s12 = "27 February 1002";
		LinkedList<String> l12 = new LinkedList<String>(Arrays.asList(s12.split(" ")));
		
		assertEquals("Date: ", "2/10/2015", AddParser.getDateAndTrimUserInput(l1));
		assertEquals("Date: ", "2/10/2014", AddParser.getDateAndTrimUserInput(l2));
		assertEquals("Date: ", "3/9/2014", AddParser.getDateAndTrimUserInput(l3));
		assertEquals("Date: ", "3/9/2014", AddParser.getDateAndTrimUserInput(l4));
		assertEquals("Date: ", "99/12/2014", AddParser.getDateAndTrimUserInput(l5));
		assertEquals("Date: ", "99/12/2014", AddParser.getDateAndTrimUserInput(l6));
		assertEquals("Date: ", "35/1/2014", AddParser.getDateAndTrimUserInput(l7));
		assertEquals("Date: ", "35/1/2014", AddParser.getDateAndTrimUserInput(l8));
		assertEquals("Date: ", "42/10/2050", AddParser.getDateAndTrimUserInput(l9)); 
		assertEquals("Date: ", "42/10/2014", AddParser.getDateAndTrimUserInput(l10));
		assertEquals("Date: ", "27/2/1002", AddParser.getDateAndTrimUserInput(l11)); 
		assertEquals("Date: ", "27/2/1002", AddParser.getDateAndTrimUserInput(l12));
		
	}
	
/**	@Test
	public void testConvertMonthToNumberStringFormat() {
		String s1 = "January";
		String s2 = "feBruary";
		String s3 = "mARch";
		String s4 = "apriL";
		String s5 = "may";
		String s6 = "JUNE";
		String s7 = "July";
		String s8 = "August";
		String s9 = "September";
		String s10 = "October";
		String s11 = "NoveMBER";
		String s12 = "deceMBER";
		
		assertEquals("Month: ", "1", AddParser.convertMonthToNumberStringFormat(s1));
		assertEquals("Month: ", "2", AddParser.convertMonthToNumberStringFormat(s2));
		assertEquals("Month: ", "3", AddParser.convertMonthToNumberStringFormat(s3));
		assertEquals("Month: ", "4", AddParser.convertMonthToNumberStringFormat(s4));
		assertEquals("Month: ", "5", AddParser.convertMonthToNumberStringFormat(s5));
		assertEquals("Month: ", "6", AddParser.convertMonthToNumberStringFormat(s6));
		assertEquals("Month: ", "7", AddParser.convertMonthToNumberStringFormat(s7));
		assertEquals("Month: ", "8", AddParser.convertMonthToNumberStringFormat(s8));
		assertEquals("Month: ", "9", AddParser.convertMonthToNumberStringFormat(s9));
		assertEquals("Month: ", "10", AddParser.convertMonthToNumberStringFormat(s10));
		assertEquals("Month: ", "11", AddParser.convertMonthToNumberStringFormat(s11));
		assertEquals("Month: ", "12", AddParser.convertMonthToNumberStringFormat(s12));
		
		
	} **/

}
