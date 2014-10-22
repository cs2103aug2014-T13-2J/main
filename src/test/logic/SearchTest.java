package test.logic;

import static org.junit.Assert.*;

import main.logic.AddHandler;
import main.logic.CommandHandler;
import main.logic.SearchHandler;
import main.storage.Storage;
import org.junit.Test;

public class SearchTest {
	private static Storage storage = Storage.getInstance();

	@Test
	public void test() {

		// Equivalence Partitioning: this test case is in the 'true' partition.
		String stringOne = "meeting with Prof at 11pm at CLB on 3/10/2014";
		CommandHandler addOne = new AddHandler(stringOne);
		addOne.execute();
		// search term is the description
		String searchStringOne = "meeting with Prof";
		SearchHandler searchOne = new SearchHandler(searchStringOne);
		String resultOne = searchOne.execute();
		String expectedOne = "";
		assertEquals(resultOne, expectedOne);

		// Equivalence Partitioning: this test case is in the 'true' partition.
		storage.clearAllTasks();
		String stringTwo = "lunch at biz canteen at 11am on 3/10/2014";
		CommandHandler addTwo = new AddHandler(stringTwo);
		addTwo.execute();
		// search term is the venue
		String searchStringTwo = "biz canteen";
		SearchHandler searchTwo = new SearchHandler(searchStringTwo);
		String resultTwo = searchTwo.execute();
		String expectedTwo = "";
		assertEquals(resultTwo, expectedTwo);

		// Equivalence Partitioning: this test case is in the 'true' partition.
		storage.clearAllTasks();
		String stringThree = "brunch at deck at 10am on 3/10/2014";
		CommandHandler addThree = new AddHandler(stringThree);
		addThree.execute();
		// search term is the time
		String searchStringThree = "10";
		SearchHandler searchThree = new SearchHandler(searchStringThree);
		String resultThree = searchThree.execute();
		String expectedThree = "";
		assertEquals(resultThree, expectedThree);

		// Equivalence Partitioning: this test case is in the 'true' partition.
		storage.clearAllTasks();
		String stringFour = "dinner at science at 7pm on 3/10/2014";
		CommandHandler addFour = new AddHandler(stringFour);
		addFour.execute();
		// search term is the date
		String searchStringFour = "2014";
		SearchHandler searchFour = new SearchHandler(searchStringFour);
		String resultFour = searchFour.execute();
		String expectedFour = "";
		assertEquals(resultFour, expectedFour);

		// Equivalence Partitioning: this test case is in the 'null' partition.
		// null task list
		storage.clearAllTasks();
		String searchStringFive = "2014";
		SearchHandler searchFive = new SearchHandler(searchStringFive);
		String resultFive = searchFive.execute();
		String expectedFive = "Task list is empty. There is nothing to search from.";
		assertEquals(resultFive, expectedFive);

		// Equivalence Partitioning: this test case is in the 'false' partition.
		storage.clearAllTasks();
		String stringSix = "meeting with Prof at 11pm at CLB on 3/10/2014";
		CommandHandler addSix = new AddHandler(stringSix);
		addSix.execute();
		String searchStringSix = "project";
		SearchHandler searchSix = new SearchHandler(searchStringSix);
		String resultSix = searchSix.execute();
		String expectedSix = "Search term cannot be found in task list.";
		assertEquals(resultSix, expectedSix);

		// Equivalence Partitioning: this test case is in the 'null' partition.
		// null search term
		storage.clearAllTasks();
		String stringSeven = "meeting with Prof at 11pm at CLB on 3/10/2014";
		CommandHandler addSeven = new AddHandler(stringSeven);
		addSeven.execute();
		String searchStringSeven = "";
		SearchHandler searchSeven = new SearchHandler(searchStringSeven);
		String resultSeven = searchSeven.execute();
		String expectedSeven = "No search term specified.";
		assertEquals(resultSeven, expectedSeven);

	}

}
