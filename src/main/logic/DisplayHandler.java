package main.logic;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

import java.util.ArrayList;

import main.storage.Storage;
import main.storage.Task;

import org.fusesource.jansi.AnsiConsole;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class DisplayHandler extends CommandHandler {

	public static final String DISPLAY_NUM_OF_TASKS = "Total number of tasks: %d\n";
	public static final String DISPLAY_TABLE_ROW_STRING_FORMAT = "%-8s %-1s %-33s %-1s %-26s %-1s %-18s %-1s %-18s\n";
	public static final String MESSAGE_EMPTY = "There is nothing to display";
	public static final String MESSAGE_DISPLAY_SUCCESS = "Display successful.";
	public static final Integer TIME_STRING_START_INDEX = 0;
	public static final Integer TIME_STRING_END_INDEX = 5;
	public static final String STRING_SPACE = " ";
	public static final String MESSAGE_ADDED = " added!";
	private static String result;
	private static Storage storage = Storage.getInstance();

	public DisplayHandler(String details) {
		super(details);
		AnsiConsole.systemInstall();
	}

	@Override
	public String execute() {
		ArrayList<Task> tasks = storage.getTasks();
		result = "";
		if (tasks.isEmpty()) {
			System.out.println(MESSAGE_EMPTY);
		} else {
			System.out.println();
			int numberOfTasks = tasks.size();
			result = String.format(DISPLAY_NUM_OF_TASKS, numberOfTasks);
			result += displayLineSeparator();
			result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
					ansi().fg(RED).a("ID").reset(), "  |", ansi().fg(MAGENTA)
							.a(" DESCRIPTION").reset(), "|",
					ansi().fg(CYAN).a(" VENUE").reset(), "|", ansi().fg(YELLOW)
							.a(" TIME").reset(), "|",
					ansi().fg(GREEN).a(" DATE").reset());
			result += displayLineSeparator();
			for (int j = 0; j < tasks.size(); j++) {
				result += displayTaskInTable(j, tasks.get(j));
			}
			// result += displayLineSeparator();
			result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
					ansi().fg(RED).a("ID").reset(), "  |", ansi().fg(MAGENTA)
							.a(" DESCRIPTION").reset(), "|",
					ansi().fg(CYAN).a(" VENUE").reset(), "|", ansi().fg(YELLOW)
							.a(" TIME").reset(), "|",
					ansi().fg(GREEN).a(" DATE").reset());
			result += displayLineSeparator();

		}
		return result;
	}

	public static String displayLineSeparator() {
		String lineString = "";
		int terminalWidth = 79;
		for (int i = 0; i < terminalWidth; i++) {
			lineString += "-";
		}
		lineString += "\n";
		return lineString;
	}

	public static String displayTaskInTable(int number, Task task) {
		result = "";
		boolean completed = task.hasCompleted();
		int status = determinePastPresentFuture(task);
		String taskNumber = getTaskNumber(number);
		String taskDescription = task.getDescription();
		String taskDescriptionExtra = "";
		String taskDescriptionExtraOne = "";
		String taskDescriptionExtraTwo = "";

		if (taskDescription.length() >= 25 && taskDescription.length() < 50) {
			taskDescriptionExtraOne = taskDescription.substring(25);
			taskDescription = taskDescription.substring(0, 25);
		}

		else if (taskDescription.length() >= 50
				&& taskDescription.length() < 75) {
			taskDescriptionExtraTwo = taskDescription.substring(50);
			taskDescriptionExtraOne = taskDescription.substring(25, 50);
			taskDescription = taskDescription.substring(0, 25);
		}

		else if (taskDescription.length() >= 75) {
			taskDescriptionExtra = taskDescription.substring(75);
			taskDescriptionExtraTwo = taskDescription.substring(50, 75);
			taskDescriptionExtraOne = taskDescription.substring(25, 50);
			taskDescription = taskDescription.substring(0, 25);
		}

		String taskVenue = "-";
		String taskVenueExtra = "";
		String taskVenueExtraOne = "";
		String taskVenueExtraTwo = "";
		if (task.hasVenue()) {
			taskVenue = task.getVenue();

			if (taskVenue.length() >= 12 && taskVenue.length() < 24) {
				taskVenueExtraOne = taskVenue.substring(12);
				taskVenue = taskVenue.substring(0, 12);
			} else if (taskVenue.length() >= 24 && taskVenue.length() < 36) {
				taskVenueExtraTwo = taskVenue.substring(24);
				taskVenueExtraOne = taskVenue.substring(12, 24);
				taskVenue = taskVenue.substring(0, 12);
			} else if (taskVenue.length() >= 36) {
				taskVenueExtra = taskVenue.substring(36);
				taskVenueExtraTwo = taskVenue.substring(24, 36);
				taskVenueExtraOne = taskVenue.substring(12, 24);
				taskVenue = taskVenue.substring(0, 12);
			}
		}

		String startTaskTime = "-";
		String endTaskTime = "-";
		if (task.hasStartTime()) {
			startTaskTime = addStartTime(task);
			if (!task.getEndTime().equals(task.getStartTime())) {
				// taskTime += "to " + addEndTime(task);
				endTaskTime = addEndTime(task);
			}
		}

		String startTaskDate = "-";
		String endTaskDate = "-";
		if (task.hasStartDate()) {
			startTaskDate = addStartDate(task);
			if (!task.getEndDate().equals(task.getStartDate())) {
				// taskDate += "to " + addEndDate(task);
				endTaskDate = addEndDate(task);
			}
		}

		String nullSpace = "";
		int nullSpaceLength = taskNumber.length();
		for (int i = 0; i < nullSpaceLength; i++) {
			nullSpace += " ";
		}

		if (taskVenue.contains("null")) {
			taskVenue = "-";
		}

		String to = "to";
		if (number < 9) {
			if (completed) {
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(YELLOW).a(taskNumber).reset(), " |",
						ansi().fg(YELLOW).a(taskDescription).reset(), "|",
						ansi().fg(YELLOW).a(taskVenue).reset(), "|",
						ansi().fg(YELLOW).a(startTaskTime).reset(), "|", ansi()
								.fg(YELLOW).a(startTaskDate).reset());
				result += "\n";
				
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(YELLOW).a(nullSpace).reset(), " |",
						ansi().fg(YELLOW).a(taskDescriptionExtraOne).reset(),
						"|", ansi().fg(YELLOW).a(taskVenueExtraOne).reset(),
						"|", ansi().fg(YELLOW).a(to).reset(), "|",
						ansi().fg(YELLOW).a(to).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(YELLOW).a(nullSpace).reset(), " |",
						ansi().fg(YELLOW).a(taskDescriptionExtraTwo).reset(),
						"|", ansi().fg(YELLOW).a(taskVenueExtraTwo).reset(),
						"|", ansi().fg(YELLOW).a(endTaskTime).reset(), "|",
						ansi().fg(YELLOW).a(endTaskDate).reset());
				result += "\n";

			} else if (status == 0) {
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(RED).a(taskNumber).reset(), " |",
						ansi().fg(RED).a(taskDescription).reset(), "|", ansi()
								.fg(RED).a(taskVenue).reset(), "|",
						ansi().fg(RED).a(startTaskTime).reset(), "|", ansi()
								.fg(RED).a(startTaskDate).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(RED).a(nullSpace).reset(), " |",
						ansi().fg(RED).a(taskDescriptionExtraOne).reset(), "|",
						ansi().fg(RED).a(taskVenueExtraOne).reset(), "|",
						ansi().fg(RED).a(to).reset(), "|", ansi().fg(RED).a(to)
								.reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(RED).a(nullSpace).reset(), " |",
						ansi().fg(RED).a(taskDescriptionExtraTwo).reset(), "|",
						ansi().fg(RED).a(taskVenueExtraTwo).reset(), "|",
						ansi().fg(RED).a(endTaskTime).reset(), "|",
						ansi().fg(RED).a(endTaskDate).reset());
				result += "\n";

			} else if (status == -1) {
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(CYAN).a(taskNumber).reset(), " |", ansi().fg(CYAN)
						.a(taskDescription).reset(), "|",
						ansi().fg(CYAN).a(taskVenue).reset(), "|",
						ansi().fg(CYAN).a(startTaskTime).reset(), "|", ansi()
								.fg(CYAN).a(startTaskDate).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(CYAN).a(nullSpace).reset(), " |", ansi().fg(CYAN)
						.a(taskDescriptionExtraOne).reset(), "|",
						ansi().fg(CYAN).a(taskVenueExtraOne).reset(), "|",
						ansi().fg(CYAN).a(to).reset(), "|",
						ansi().fg(CYAN).a(to).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(CYAN).a(nullSpace).reset(), " |", ansi().fg(CYAN)
						.a(taskDescriptionExtraTwo).reset(), "|",
						ansi().fg(CYAN).a(taskVenueExtraTwo).reset(), "|",
						ansi().fg(CYAN).a(endTaskTime).reset(), "|",
						ansi().fg(CYAN).a(endTaskDate).reset());
				result += "\n";

			}	

			else if (status == 1) {
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(GREEN).a(taskNumber).reset(), " |", ansi()
						.fg(GREEN).a(taskDescription).reset(), "|",
						ansi().fg(GREEN).a(taskVenue).reset(), "|",
						ansi().fg(GREEN).a(startTaskTime).reset(), "|", ansi()
								.fg(GREEN).a(startTaskDate).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(GREEN).a(nullSpace).reset(), " |", ansi().fg(GREEN)
						.a(taskDescriptionExtraOne).reset(), "|",
						ansi().fg(GREEN).a(taskVenueExtraOne).reset(), "|",
						ansi().fg(GREEN).a(to).reset(), "|", ansi().fg(GREEN)
								.a(to).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(GREEN).a(nullSpace).reset(), " |", ansi().fg(GREEN)
						.a(taskDescriptionExtraTwo).reset(), "|",
						ansi().fg(GREEN).a(taskVenueExtraTwo).reset(), "|",
						ansi().fg(GREEN).a(endTaskTime).reset(), "|", ansi()
								.fg(GREEN).a(endTaskDate).reset());
				result += "\n";

			}

			else {
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(MAGENTA).a(taskNumber).reset(), " |",
						ansi().fg(MAGENTA).a(taskDescription).reset(), "|",
						ansi().fg(MAGENTA).a(taskVenue).reset(), "|", ansi()
								.fg(MAGENTA).a(startTaskTime).reset(), "|",
						ansi().fg(MAGENTA).a(startTaskDate).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(MAGENTA).a(nullSpace).reset(), " |",
						ansi().fg(MAGENTA).a(taskDescriptionExtraOne).reset(),
						"|", ansi().fg(MAGENTA).a(taskVenueExtraOne).reset(),
						"|", ansi().fg(MAGENTA).a(to).reset(), "|",
						ansi().fg(MAGENTA).a(to).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(MAGENTA).a(nullSpace).reset(), " |",
						ansi().fg(MAGENTA).a(taskDescriptionExtraTwo).reset(),
						"|", ansi().fg(MAGENTA).a(taskVenueExtraTwo).reset(),
						"|", ansi().fg(MAGENTA).a(endTaskTime).reset(), "|",
						ansi().fg(MAGENTA).a(endTaskDate).reset());
				result += "\n";

			}
		} else {
			if (completed) {
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(YELLOW).a(taskNumber).reset(), "|",
						ansi().fg(YELLOW).a(taskDescription).reset(), "|",
						ansi().fg(YELLOW).a(taskVenue).reset(), "|",
						ansi().fg(YELLOW).a(startTaskTime).reset(), "|", ansi()
								.fg(YELLOW).a(startTaskDate).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(YELLOW).a(nullSpace).reset(), "|", ansi()
						.fg(YELLOW).a(taskDescriptionExtraOne).reset(), "|",
						ansi().fg(YELLOW).a(taskVenueExtraOne).reset(), "|",
						ansi().fg(YELLOW).a(to).reset(), "|", ansi().fg(YELLOW)
								.a(to).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(YELLOW).a(nullSpace).reset(), "|", ansi()
						.fg(YELLOW).a(taskDescriptionExtraTwo).reset(), "|",
						ansi().fg(YELLOW).a(taskVenueExtraTwo).reset(), "|",
						ansi().fg(YELLOW).a(endTaskTime).reset(), "|", ansi()
								.fg(YELLOW).a(endTaskDate).reset());
				result += "\n";

			} else if (status == 0) {
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(RED).a(taskNumber).reset(), "|",
						ansi().fg(RED).a(taskDescription).reset(), "|", ansi()
								.fg(RED).a(taskVenue).reset(), "|",
						ansi().fg(RED).a(startTaskTime).reset(), "|", ansi()
								.fg(RED).a(startTaskDate).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(RED).a(nullSpace).reset(), "|",
						ansi().fg(RED).a(taskDescriptionExtraOne).reset(), "|",
						ansi().fg(RED).a(taskVenueExtraOne).reset(), "|",
						ansi().fg(RED).a(to).reset(), "|", ansi().fg(RED).a(to)
								.reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(RED).a(nullSpace).reset(), "|",
						ansi().fg(RED).a(taskDescriptionExtraTwo).reset(), "|",
						ansi().fg(RED).a(taskVenueExtraTwo).reset(), "|",
						ansi().fg(RED).a(endTaskTime).reset(), "|",
						ansi().fg(RED).a(endTaskDate).reset());
				result += "\n";

			} else if (status == -1) {
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(CYAN).a(taskNumber).reset(), "|", ansi().fg(CYAN)
						.a(taskDescription).reset(), "|",
						ansi().fg(CYAN).a(taskVenue).reset(), "|",
						ansi().fg(CYAN).a(startTaskTime).reset(), "|", ansi()
								.fg(CYAN).a(startTaskDate).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(CYAN).a(nullSpace).reset(), "|",
						ansi().fg(CYAN).a(taskDescriptionExtraOne).reset(),
						"|", ansi().fg(CYAN).a(taskVenueExtraOne).reset(), "|",
						ansi().fg(CYAN).a(to).reset(), "|",
						ansi().fg(CYAN).a(to).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(CYAN).a(nullSpace).reset(), "|",
						ansi().fg(CYAN).a(taskDescriptionExtraTwo).reset(),
						"|", ansi().fg(CYAN).a(taskVenueExtraTwo).reset(), "|",
						ansi().fg(CYAN).a(endTaskTime).reset(), "|",
						ansi().fg(CYAN).a(endTaskDate).reset());
				result += "\n";

			}

			else if (status == 1) {
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(GREEN).a(taskNumber).reset(), "|", ansi().fg(GREEN)
						.a(taskDescription).reset(), "|",
						ansi().fg(GREEN).a(taskVenue).reset(), "|",
						ansi().fg(GREEN).a(startTaskTime).reset(), "|", ansi()
								.fg(GREEN).a(startTaskDate).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(GREEN).a(nullSpace).reset(), "|", ansi().fg(GREEN)
						.a(taskDescriptionExtraOne).reset(), "|",
						ansi().fg(GREEN).a(taskVenueExtraOne).reset(), "|",
						ansi().fg(GREEN).a(to).reset(), "|", ansi().fg(GREEN)
								.a(to).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(GREEN).a(nullSpace).reset(), "|", ansi().fg(GREEN)
						.a(taskDescriptionExtraTwo).reset(), "|",
						ansi().fg(GREEN).a(taskVenueExtraTwo).reset(), "|",
						ansi().fg(GREEN).a(endTaskTime).reset(), "|", ansi()
								.fg(GREEN).a(endTaskDate).reset());
				result += "\n";

			}

			else {
				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(MAGENTA).a(taskNumber).reset(), "|",
						ansi().fg(MAGENTA).a(taskDescription).reset(), "|",
						ansi().fg(MAGENTA).a(taskVenue).reset(), "|", ansi()
								.fg(MAGENTA).a(startTaskTime).reset(), "|",
						ansi().fg(MAGENTA).a(startTaskDate).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(MAGENTA).a(nullSpace).reset(), "|",
						ansi().fg(MAGENTA).a(taskDescriptionExtraOne).reset(),
						"|", ansi().fg(MAGENTA).a(taskVenueExtraOne).reset(),
						"|", ansi().fg(MAGENTA).a(to).reset(), "|",
						ansi().fg(MAGENTA).a(to).reset());
				result += "\n";

				result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
						.fg(MAGENTA).a(nullSpace).reset(), "|",
						ansi().fg(MAGENTA).a(taskDescriptionExtraTwo).reset(),
						"|", ansi().fg(MAGENTA).a(taskVenueExtraTwo).reset(),
						"|", ansi().fg(MAGENTA).a(endTaskTime).reset(), "|",
						ansi().fg(MAGENTA).a(endTaskDate).reset());
				result += "\n";

			}
		}

		if (taskDescriptionExtra.isEmpty() && taskVenueExtra.isEmpty()) {
			result += displayLineSeparator();

		}

		if (!taskDescriptionExtra.isEmpty() || !taskVenueExtra.isEmpty()) {
			double descriptionLines = Math
					.ceil(taskDescriptionExtra.length() / 25.0);
			double venueLines = Math.ceil(taskVenueExtra.length() / 12.0);

			if (venueLines == descriptionLines) {
				for (int i = 0; i < venueLines - 1; i++) {
					String displayVenue = taskVenueExtra.substring(0, 12);
					String displayDescription = taskDescriptionExtra.substring(
							0, 25);
					taskVenueExtra = taskVenueExtra.substring(12);
					taskDescriptionExtra = taskDescriptionExtra.substring(25);

					if (number < 9) {
						if (completed) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(YELLOW).a(nullSpace).reset(),
									" |", ansi().fg(YELLOW)
											.a(displayDescription).reset(),
									"|", ansi().fg(YELLOW).a(displayVenue)
											.reset(), "|", "           |", "",
									"");
						} else if (status == 0) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(RED).a(nullSpace).reset(), " |",
									ansi().fg(RED).a(displayDescription)
											.reset(), "|",
									ansi().fg(RED).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else if (status == -1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(CYAN).a(nullSpace).reset(), " |",
									ansi().fg(CYAN).a(displayDescription)
											.reset(), "|",
									ansi().fg(CYAN).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else if (status == 1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(), " |",
									ansi().fg(GREEN).a(displayDescription)
											.reset(), "|",
									ansi().fg(GREEN).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(),
									" |",
									ansi().fg(MAGENTA).a(displayDescription)
											.reset(), "|", ansi().fg(MAGENTA)
											.a(displayVenue).reset(), "|",
									"           |", "", "");
						}
					} else {
						if (completed) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(YELLOW).a(nullSpace).reset(),
									"|", ansi().fg(YELLOW)
											.a(displayDescription).reset(),
									"|", ansi().fg(YELLOW).a(displayVenue)
											.reset(), "|", "           |", "",
									"");
						} else if (status == 0) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(RED).a(nullSpace).reset(), "|",
									ansi().fg(RED).a(displayDescription)
											.reset(), "|",
									ansi().fg(RED).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else if (status == -1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(CYAN).a(nullSpace).reset(), "|",
									ansi().fg(CYAN).a(displayDescription)
											.reset(), "|",
									ansi().fg(CYAN).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else if (status == 1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(), "|",
									ansi().fg(GREEN).a(displayDescription)
											.reset(), "|",
									ansi().fg(GREEN).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(),
									"|",
									ansi().fg(MAGENTA).a(displayDescription)
											.reset(), "|", ansi().fg(MAGENTA)
											.a(displayVenue).reset(), "|",
									"           |", "", "");
						}
					}
				}
				if(number<9){
					if (completed) {
						result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(YELLOW).a(nullSpace).reset(), " |",
								ansi().fg(YELLOW).a(taskDescriptionExtra).reset(),
								"|", ansi().fg(YELLOW).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
						result += displayLineSeparator();

					} else if (status == 0) {
						result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(RED).a(nullSpace).reset(), " |", ansi()
										.fg(RED).a(taskDescriptionExtra).reset(),
								"|", ansi().fg(RED).a(taskVenueExtra).reset(), "|",
								"           |", "", "");
						result += displayLineSeparator();
					} else if (status == -1) {
						result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(CYAN).a(nullSpace).reset(), " |", ansi()
										.fg(CYAN).a(taskDescriptionExtra).reset(),
								"|", ansi().fg(CYAN).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
						result += displayLineSeparator();
					} else if (status == 1) {
						result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(GREEN).a(nullSpace).reset(), " |", ansi()
										.fg(GREEN).a(taskDescriptionExtra).reset(),
								"|", ansi().fg(GREEN).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
						result += displayLineSeparator();
					} else {
						result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(MAGENTA).a(nullSpace).reset(), " |",
								ansi().fg(MAGENTA).a(taskDescriptionExtra).reset(),
								"|", ansi().fg(MAGENTA).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
						result += displayLineSeparator();

					}
				}
				else{
				if (completed) {
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
							ansi().fg(YELLOW).a(nullSpace).reset(), "|",
							ansi().fg(YELLOW).a(taskDescriptionExtra).reset(),
							"|", ansi().fg(YELLOW).a(taskVenueExtra).reset(),
							"|", "           |", "", "");
					result += displayLineSeparator();

				} else if (status == 0) {
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
							ansi().fg(RED).a(nullSpace).reset(), "|", ansi()
									.fg(RED).a(taskDescriptionExtra).reset(),
							"|", ansi().fg(RED).a(taskVenueExtra).reset(), "|",
							"           |", "", "");
					result += displayLineSeparator();
				} else if (status == -1) {
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
							ansi().fg(CYAN).a(nullSpace).reset(), "|", ansi()
									.fg(CYAN).a(taskDescriptionExtra).reset(),
							"|", ansi().fg(CYAN).a(taskVenueExtra).reset(),
							"|", "           |", "", "");
					result += displayLineSeparator();
				} else if (status == 1) {
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
							ansi().fg(GREEN).a(nullSpace).reset(), "|", ansi()
									.fg(GREEN).a(taskDescriptionExtra).reset(),
							"|", ansi().fg(GREEN).a(taskVenueExtra).reset(),
							"|", "           |", "", "");
					result += displayLineSeparator();
				} else {
					result += String.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
							ansi().fg(MAGENTA).a(nullSpace).reset(), "|",
							ansi().fg(MAGENTA).a(taskDescriptionExtra).reset(),
							"|", ansi().fg(MAGENTA).a(taskVenueExtra).reset(),
							"|", "           |", "", "");
					result += displayLineSeparator();

				}
				}
			}

			else if (venueLines > descriptionLines) {

				for (int i = 0; i < descriptionLines - 1; i++) {
					String displayVenue = taskVenueExtra.substring(0, 12);
					String displayDescription = taskDescriptionExtra.substring(
							0, 25);
					
					
					if(number<9){
						if (completed) {
							result += String
									.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
											.fg(YELLOW).a(nullSpace).reset(), " |",
											ansi().fg(YELLOW).a(displayDescription)
													.reset(), "|", ansi()
													.fg(YELLOW).a(displayVenue)
													.reset(), "|", "           |",
											"", "");
						}

						else if (status == 0) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
											.a(nullSpace).reset(), " |",
									ansi().fg(RED).a(displayDescription).reset(),
									"|", ansi().fg(RED).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else if (status == -1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
											.fg(CYAN).a(nullSpace).reset(), " |",
									ansi().fg(CYAN).a(displayDescription).reset(),
									"|", ansi().fg(CYAN).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else if (status == 1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(), " |",
									ansi().fg(GREEN).a(displayDescription).reset(),
									"|", ansi().fg(GREEN).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(), " |",
									ansi().fg(MAGENTA).a(displayDescription)
											.reset(), "|",
									ansi().fg(MAGENTA).a(displayVenue).reset(),
									"|", "           |", "", "");
						}
					}
					else{
					if (completed) {
						result += String
								.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
										.fg(YELLOW).a(nullSpace).reset(), "|",
										ansi().fg(YELLOW).a(displayDescription)
												.reset(), "|", ansi()
												.fg(YELLOW).a(displayVenue)
												.reset(), "|", "           |",
										"", "");
					}

					else if (status == 0) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
										.a(nullSpace).reset(), "|",
								ansi().fg(RED).a(displayDescription).reset(),
								"|", ansi().fg(RED).a(displayVenue).reset(),
								"|", "           |", "", "");
					} else if (status == -1) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
										.fg(CYAN).a(nullSpace).reset(), "|",
								ansi().fg(CYAN).a(displayDescription).reset(),
								"|", ansi().fg(CYAN).a(displayVenue).reset(),
								"|", "           |", "", "");
					} else if (status == 1) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(GREEN).a(nullSpace).reset(), "|",
								ansi().fg(GREEN).a(displayDescription).reset(),
								"|", ansi().fg(GREEN).a(displayVenue).reset(),
								"|", "           |", "", "");
					} else {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(MAGENTA).a(nullSpace).reset(), "|",
								ansi().fg(MAGENTA).a(displayDescription)
										.reset(), "|",
								ansi().fg(MAGENTA).a(displayVenue).reset(),
								"|", "           |", "", "");
					}
					}
					taskVenueExtra = taskVenueExtra.substring(12);
					taskDescriptionExtra = taskDescriptionExtra.substring(25);
				}
				if (venueLines - descriptionLines - 1 == 0) {
					if(number<9){
						if (completed) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(YELLOW).a(nullSpace).reset(), " |",
									ansi().fg(YELLOW).a(taskDescriptionExtra)
											.reset(), "|",
									ansi().fg(YELLOW).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						} else if (status == 0) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
											.a(nullSpace).reset(), " |",
									ansi().fg(RED).a(taskDescriptionExtra).reset(),
									"|", ansi().fg(RED).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						} else if (status == -1) {
							result += String
									.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
											.fg(CYAN).a(nullSpace).reset(), " |",
											ansi().fg(CYAN).a(taskDescriptionExtra)
													.reset(), "|", ansi().fg(CYAN)
													.a(taskVenueExtra).reset(),
											"|", "           |", "", "");
						} else if (status == 1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(), " |",
									ansi().fg(GREEN).a(taskDescriptionExtra)
											.reset(), "|",
									ansi().fg(GREEN).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						} else {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(), " |",
									ansi().fg(MAGENTA).a(taskDescriptionExtra)
											.reset(), "|",
									ansi().fg(MAGENTA).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						}
					}
					else{
					if (completed) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(YELLOW).a(nullSpace).reset(), "|",
								ansi().fg(YELLOW).a(taskDescriptionExtra)
										.reset(), "|",
								ansi().fg(YELLOW).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
					} else if (status == 0) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
										.a(nullSpace).reset(), "|",
								ansi().fg(RED).a(taskDescriptionExtra).reset(),
								"|", ansi().fg(RED).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
					} else if (status == -1) {
						result += String
								.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
										.fg(CYAN).a(nullSpace).reset(), "|",
										ansi().fg(CYAN).a(taskDescriptionExtra)
												.reset(), "|", ansi().fg(CYAN)
												.a(taskVenueExtra).reset(),
										"|", "           |", "", "");
					} else if (status == 1) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(GREEN).a(nullSpace).reset(), "|",
								ansi().fg(GREEN).a(taskDescriptionExtra)
										.reset(), "|",
								ansi().fg(GREEN).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
					} else {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(MAGENTA).a(nullSpace).reset(), "|",
								ansi().fg(MAGENTA).a(taskDescriptionExtra)
										.reset(), "|",
								ansi().fg(MAGENTA).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
					}
					}
					result += displayLineSeparator();

				} else {
					if(number<9){
						if (completed) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(YELLOW).a(nullSpace).reset(),
									" |",
									ansi().fg(YELLOW).a(taskDescriptionExtra)
											.reset(),
									"|",
									ansi().fg(YELLOW)
											.a(taskVenueExtra.substring(0, 12))
											.reset(), "|", "           |", "", "");
						} else if (status == 0) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(RED).a(nullSpace).reset(),
									" |",
									ansi().fg(RED).a(taskDescriptionExtra).reset(),
									"|",
									ansi().fg(RED)
											.a(taskVenueExtra.substring(0, 12))
											.reset(), "|", "           |", "", "");
						} else if (status == -1) {
							result += String
									.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
											ansi().fg(CYAN).a(nullSpace).reset(),
											" |",
											ansi().fg(CYAN).a(taskDescriptionExtra)
													.reset(),
											"|",
											ansi().fg(CYAN)
													.a(taskVenueExtra.substring(0,
															12)).reset(), "|",
											"           |", "", "");
						} else if (status == 1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(),
									" |",
									ansi().fg(GREEN).a(taskDescriptionExtra)
											.reset(),
									"|",
									ansi().fg(GREEN)
											.a(taskVenueExtra.substring(0, 12))
											.reset(), "|", "           |", "", "");
						} else {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(),
									" |",
									ansi().fg(MAGENTA).a(taskDescriptionExtra)
											.reset(),
									"|",
									ansi().fg(MAGENTA)
											.a(taskVenueExtra.substring(0, 12))
											.reset(), "|", "           |", "", "");
						}
					}
					else{
					if (completed) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(YELLOW).a(nullSpace).reset(),
								"|",
								ansi().fg(YELLOW).a(taskDescriptionExtra)
										.reset(),
								"|",
								ansi().fg(YELLOW)
										.a(taskVenueExtra.substring(0, 12))
										.reset(), "|", "           |", "", "");
					} else if (status == 0) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(RED).a(nullSpace).reset(),
								"|",
								ansi().fg(RED).a(taskDescriptionExtra).reset(),
								"|",
								ansi().fg(RED)
										.a(taskVenueExtra.substring(0, 12))
										.reset(), "|", "           |", "", "");
					} else if (status == -1) {
						result += String
								.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(CYAN).a(nullSpace).reset(),
										"|",
										ansi().fg(CYAN).a(taskDescriptionExtra)
												.reset(),
										"|",
										ansi().fg(CYAN)
												.a(taskVenueExtra.substring(0,
														12)).reset(), "|",
										"           |", "", "");
					} else if (status == 1) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(GREEN).a(nullSpace).reset(),
								"|",
								ansi().fg(GREEN).a(taskDescriptionExtra)
										.reset(),
								"|",
								ansi().fg(GREEN)
										.a(taskVenueExtra.substring(0, 12))
										.reset(), "|", "           |", "", "");
					} else {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(MAGENTA).a(nullSpace).reset(),
								"|",
								ansi().fg(MAGENTA).a(taskDescriptionExtra)
										.reset(),
								"|",
								ansi().fg(MAGENTA)
										.a(taskVenueExtra.substring(0, 12))
										.reset(), "|", "           |", "", "");
					}
					}
					
					
					taskVenueExtra = taskVenueExtra.substring(12);

					for (int i = 0; i < venueLines - descriptionLines - 1; i++) {
						if (taskVenueExtra.length() > 12) {
							
							if(number<9){
								if (completed) {
									result += String.format(
											DISPLAY_TABLE_ROW_STRING_FORMAT,
											ansi().fg(YELLOW).a(nullSpace).reset(),
											" |",
											ansi().fg(YELLOW).a("").reset(),
											"|",
											ansi().fg(YELLOW)
													.a(taskVenueExtra.substring(0,
															12)).reset(), "|",
											"           |", "", "");
								} else if (status == 0) {
									result += String.format(
											DISPLAY_TABLE_ROW_STRING_FORMAT,
											ansi().fg(RED).a(nullSpace).reset(),
											" |",
											ansi().fg(RED).a("").reset(),
											"|",
											ansi().fg(RED)
													.a(taskVenueExtra.substring(0,
															12)).reset(), "|",
											"           |", "", "");
								} else if (status == -1) {
									result += String.format(
											DISPLAY_TABLE_ROW_STRING_FORMAT,
											ansi().fg(CYAN).a(nullSpace).reset(),
											" |",
											ansi().fg(CYAN).a("").reset(),
											"|",
											ansi().fg(CYAN)
													.a(taskVenueExtra.substring(0,
															12)).reset(), "|",
											"           |", "", "");
								} else if (status == 1) {
									result += String.format(
											DISPLAY_TABLE_ROW_STRING_FORMAT,
											ansi().fg(GREEN).a(nullSpace).reset(),
											" |",
											ansi().fg(GREEN).a("").reset(),
											"|",
											ansi().fg(GREEN)
													.a(taskVenueExtra.substring(0,
															12)).reset(), "|",
											"           |", "", "");
								} else {
									result += String
											.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
													ansi().fg(MAGENTA).a(nullSpace)
															.reset(),
													" |",
													ansi().fg(MAGENTA).a("")
															.reset(),
													"|",
													ansi().fg(MAGENTA)
															.a(taskVenueExtra
																	.substring(0,
																			12))
															.reset(), "|",
													"           |", "", "");
								}
							}
							else{
							if (completed) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(YELLOW).a(nullSpace).reset(),
										"|",
										ansi().fg(YELLOW).a("").reset(),
										"|",
										ansi().fg(YELLOW)
												.a(taskVenueExtra.substring(0,
														12)).reset(), "|",
										"           |", "", "");
							} else if (status == 0) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(RED).a(nullSpace).reset(),
										"|",
										ansi().fg(RED).a("").reset(),
										"|",
										ansi().fg(RED)
												.a(taskVenueExtra.substring(0,
														12)).reset(), "|",
										"           |", "", "");
							} else if (status == -1) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(CYAN).a(nullSpace).reset(),
										"|",
										ansi().fg(CYAN).a("").reset(),
										"|",
										ansi().fg(CYAN)
												.a(taskVenueExtra.substring(0,
														12)).reset(), "|",
										"           |", "", "");
							} else if (status == 1) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(GREEN).a(nullSpace).reset(),
										"|",
										ansi().fg(GREEN).a("").reset(),
										"|",
										ansi().fg(GREEN)
												.a(taskVenueExtra.substring(0,
														12)).reset(), "|",
										"           |", "", "");
							} else {
								result += String
										.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
												ansi().fg(MAGENTA).a(nullSpace)
														.reset(),
												"|",
												ansi().fg(MAGENTA).a("")
														.reset(),
												"|",
												ansi().fg(MAGENTA)
														.a(taskVenueExtra
																.substring(0,
																		12))
														.reset(), "|",
												"           |", "", "");
							}
							}
							taskVenueExtra = taskVenueExtra.substring(12);
						}

					}
					
					if(number<9){
						if (completed) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(YELLOW).a(nullSpace).reset(), " |",
									ansi().fg(YELLOW).a("").reset(), "|", ansi()
											.fg(YELLOW).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						}

						else if (status == 0) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
											.a(nullSpace).reset(), " |",
									ansi().fg(RED).a("").reset(), "|",
									ansi().fg(RED).a(taskVenueExtra).reset(), "|",
									"           |", "", "");
						} else if (status == -1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
											.fg(CYAN).a(nullSpace).reset(), " |",
									ansi().fg(CYAN).a("").reset(), "|",
									ansi().fg(CYAN).a(taskVenueExtra).reset(), "|",
									"           |", "", "");
						} else if (status == 1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(), " |",
									ansi().fg(GREEN).a("").reset(), "|",
									ansi().fg(GREEN).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						} else {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(), " |",
									ansi().fg(MAGENTA).a("").reset(), "|", ansi()
											.fg(MAGENTA).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						}
					}
					else{
					if (completed) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(YELLOW).a(nullSpace).reset(), "|",
								ansi().fg(YELLOW).a("").reset(), "|", ansi()
										.fg(YELLOW).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
					}

					else if (status == 0) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
										.a(nullSpace).reset(), "|",
								ansi().fg(RED).a("").reset(), "|",
								ansi().fg(RED).a(taskVenueExtra).reset(), "|",
								"           |", "", "");
					} else if (status == -1) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
										.fg(CYAN).a(nullSpace).reset(), "|",
								ansi().fg(CYAN).a("").reset(), "|",
								ansi().fg(CYAN).a(taskVenueExtra).reset(), "|",
								"           |", "", "");
					} else if (status == 1) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(GREEN).a(nullSpace).reset(), "|",
								ansi().fg(GREEN).a("").reset(), "|",
								ansi().fg(GREEN).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
					} else {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(MAGENTA).a(nullSpace).reset(), "|",
								ansi().fg(MAGENTA).a("").reset(), "|", ansi()
										.fg(MAGENTA).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
					}
					}

					result += displayLineSeparator();
				}

			}

			else if (descriptionLines > venueLines) {
				for (int i = 0; i < venueLines - 1; i++) {
					String displayVenue = taskVenueExtra.substring(0, 12);
					String displayDescription = taskDescriptionExtra.substring(
							0, 25);
					
					if(number<9){
						if (completed) {
							result += String
									.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
											.fg(YELLOW).a(nullSpace).reset(), " |",
											ansi().fg(YELLOW).a(displayDescription)
													.reset(), "|", ansi()
													.fg(YELLOW).a(displayVenue)
													.reset(), "|", "           |",
											"", "");
						} else if (status == 0) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
											.a(nullSpace).reset(), " |",
									ansi().fg(RED).a(displayDescription).reset(),
									"|", ansi().fg(RED).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else if (status == -1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
											.fg(CYAN).a(nullSpace).reset(), " |",
									ansi().fg(CYAN).a(displayDescription).reset(),
									"|", ansi().fg(CYAN).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else if (status == 1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(), " |",
									ansi().fg(GREEN).a(displayDescription).reset(),
									"|", ansi().fg(GREEN).a(displayVenue).reset(),
									"|", "           |", "", "");
						} else {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(), " |",
									ansi().fg(MAGENTA).a(displayDescription)
											.reset(), "|",
									ansi().fg(MAGENTA).a(displayVenue).reset(),
									"|", "           |", "", "");
						}

					}
					else{
					if (completed) {
						result += String
								.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
										.fg(YELLOW).a(nullSpace).reset(), "|",
										ansi().fg(YELLOW).a(displayDescription)
												.reset(), "|", ansi()
												.fg(YELLOW).a(displayVenue)
												.reset(), "|", "           |",
										"", "");
					} else if (status == 0) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
										.a(nullSpace).reset(), "|",
								ansi().fg(RED).a(displayDescription).reset(),
								"|", ansi().fg(RED).a(displayVenue).reset(),
								"|", "           |", "", "");
					} else if (status == -1) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
										.fg(CYAN).a(nullSpace).reset(), "|",
								ansi().fg(CYAN).a(displayDescription).reset(),
								"|", ansi().fg(CYAN).a(displayVenue).reset(),
								"|", "           |", "", "");
					} else if (status == 1) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(GREEN).a(nullSpace).reset(), "|",
								ansi().fg(GREEN).a(displayDescription).reset(),
								"|", ansi().fg(GREEN).a(displayVenue).reset(),
								"|", "           |", "", "");
					} else {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(MAGENTA).a(nullSpace).reset(), "|",
								ansi().fg(MAGENTA).a(displayDescription)
										.reset(), "|",
								ansi().fg(MAGENTA).a(displayVenue).reset(),
								"|", "           |", "", "");
					}
					}
					taskVenueExtra = taskVenueExtra.substring(12);
					taskDescriptionExtra = taskDescriptionExtra.substring(25);
				}

				if (descriptionLines - venueLines - 1 == 0) {
					if (taskDescriptionExtra.length() > 25) {
						if(number<9){
							if (completed) {
								result += String
										.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
												ansi().fg(YELLOW).a(nullSpace)
														.reset(),
												" |",
												ansi().fg(YELLOW)
														.a(taskDescriptionExtra
																.substring(0, 25))
														.reset(), "|",
												ansi().fg(YELLOW).a(taskVenueExtra)
														.reset(), "|",
												"           |", "", "");

								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(YELLOW).a(nullSpace).reset(),
										" |",
										ansi().fg(YELLOW)
												.a(taskDescriptionExtra
														.substring(25)).reset(),
										"|", ansi().fg(YELLOW).a("").reset(), "|",
										"           |", "", "");
							} else if (status == 0) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(RED).a(nullSpace).reset(),
										" |",
										ansi().fg(RED)
												.a(taskDescriptionExtra.substring(
														0, 25)).reset(), "|",
										ansi().fg(RED).a(taskVenueExtra).reset(),
										"|", "           |", "", "");

								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(RED).a(nullSpace).reset(),
										" |",
										ansi().fg(RED)
												.a(taskDescriptionExtra
														.substring(25)).reset(),
										"|", ansi().fg(RED).a("").reset(), "|",
										"           |", "", "");
							} else if (status == -1) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(CYAN).a(nullSpace).reset(),
										" |",
										ansi().fg(CYAN)
												.a(taskDescriptionExtra.substring(
														0, 25)).reset(), "|",
										ansi().fg(CYAN).a(taskVenueExtra).reset(),
										"|", "           |", "", "");

								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(CYAN).a(nullSpace).reset(),
										" |",
										ansi().fg(CYAN)
												.a(taskDescriptionExtra
														.substring(25)).reset(),
										"|", ansi().fg(CYAN).a("").reset(), "|",
										"           |", "", "");
							} else if (status == 1) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(GREEN).a(nullSpace).reset(),
										" |",
										ansi().fg(GREEN)
												.a(taskDescriptionExtra.substring(
														0, 25)).reset(), "|",
										ansi().fg(GREEN).a(taskVenueExtra).reset(),
										"|", "           |", "", "");

								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(GREEN).a(nullSpace).reset(),
										" |",
										ansi().fg(GREEN)
												.a(taskDescriptionExtra
														.substring(25)).reset(),
										"|", ansi().fg(GREEN).a("").reset(), "|",
										"           |", "", "");
							}

							else {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(MAGENTA).a(nullSpace).reset(),
										" |",
										ansi().fg(MAGENTA)
												.a(taskDescriptionExtra.substring(
														0, 25)).reset(), "|",
										ansi().fg(MAGENTA).a(taskVenueExtra)
												.reset(), "|", "           |", "",
										"");

								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(MAGENTA).a(nullSpace).reset(),
										" |",
										ansi().fg(MAGENTA)
												.a(taskDescriptionExtra
														.substring(25)).reset(),
										"|", ansi().fg(MAGENTA).a("").reset(), "|",
										"           |", "", "");
							}

						}
						else{
						if (completed) {
							result += String
									.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
											ansi().fg(YELLOW).a(nullSpace)
													.reset(),
											"|",
											ansi().fg(YELLOW)
													.a(taskDescriptionExtra
															.substring(0, 25))
													.reset(), "|",
											ansi().fg(YELLOW).a(taskVenueExtra)
													.reset(), "|",
											"           |", "", "");

							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(YELLOW).a(nullSpace).reset(),
									"|",
									ansi().fg(YELLOW)
											.a(taskDescriptionExtra
													.substring(25)).reset(),
									"|", ansi().fg(YELLOW).a("").reset(), "|",
									"           |", "", "");
						} else if (status == 0) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(RED).a(nullSpace).reset(),
									"|",
									ansi().fg(RED)
											.a(taskDescriptionExtra.substring(
													0, 25)).reset(), "|",
									ansi().fg(RED).a(taskVenueExtra).reset(),
									"|", "           |", "", "");

							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(RED).a(nullSpace).reset(),
									"|",
									ansi().fg(RED)
											.a(taskDescriptionExtra
													.substring(25)).reset(),
									"|", ansi().fg(RED).a("").reset(), "|",
									"           |", "", "");
						} else if (status == -1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(CYAN).a(nullSpace).reset(),
									"|",
									ansi().fg(CYAN)
											.a(taskDescriptionExtra.substring(
													0, 25)).reset(), "|",
									ansi().fg(CYAN).a(taskVenueExtra).reset(),
									"|", "           |", "", "");

							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(CYAN).a(nullSpace).reset(),
									"|",
									ansi().fg(CYAN)
											.a(taskDescriptionExtra
													.substring(25)).reset(),
									"|", ansi().fg(CYAN).a("").reset(), "|",
									"           |", "", "");
						} else if (status == 1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(),
									"|",
									ansi().fg(GREEN)
											.a(taskDescriptionExtra.substring(
													0, 25)).reset(), "|",
									ansi().fg(GREEN).a(taskVenueExtra).reset(),
									"|", "           |", "", "");

							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(),
									"|",
									ansi().fg(GREEN)
											.a(taskDescriptionExtra
													.substring(25)).reset(),
									"|", ansi().fg(GREEN).a("").reset(), "|",
									"           |", "", "");
						}

						else {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(),
									"|",
									ansi().fg(MAGENTA)
											.a(taskDescriptionExtra.substring(
													0, 25)).reset(), "|",
									ansi().fg(MAGENTA).a(taskVenueExtra)
											.reset(), "|", "           |", "",
									"");

							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(),
									"|",
									ansi().fg(MAGENTA)
											.a(taskDescriptionExtra
													.substring(25)).reset(),
									"|", ansi().fg(MAGENTA).a("").reset(), "|",
									"           |", "", "");
						}
						}
						result += displayLineSeparator();
					} else {
						
						if(number<9){
							if (completed) {
								result += String
										.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
												ansi().fg(YELLOW).a(nullSpace)
														.reset(),
												" |",
												ansi().fg(YELLOW)
														.a(taskDescriptionExtra)
														.reset(), "|",
												ansi().fg(YELLOW).a(taskVenueExtra)
														.reset(), "|",
												"           |", "", "");
							}

							else if (status == 0) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(RED).a(nullSpace).reset(), " |",
										ansi().fg(RED).a(taskDescriptionExtra)
												.reset(), "|",
										ansi().fg(RED).a(taskVenueExtra).reset(),
										"|", "           |", "", "");
							} else if (status == -1) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(CYAN).a(nullSpace).reset(), " |",
										ansi().fg(CYAN).a(taskDescriptionExtra)
												.reset(), "|",
										ansi().fg(CYAN).a(taskVenueExtra).reset(),
										"|", "           |", "", "");
							} else if (status == 1) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(GREEN).a(nullSpace).reset(), " |",
										ansi().fg(GREEN).a(taskDescriptionExtra)
												.reset(), "|",
										ansi().fg(GREEN).a(taskVenueExtra).reset(),
										"|", "           |", "", "");
							} else {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(MAGENTA).a(nullSpace).reset(),
										" |",
										ansi().fg(MAGENTA).a(taskDescriptionExtra)
												.reset(), "|", ansi().fg(MAGENTA)
												.a(taskVenueExtra).reset(), "|",
										"           |", "", "");
							}
						}
						else{
						if (completed) {
							result += String
									.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
											ansi().fg(YELLOW).a(nullSpace)
													.reset(),
											"|",
											ansi().fg(YELLOW)
													.a(taskDescriptionExtra)
													.reset(), "|",
											ansi().fg(YELLOW).a(taskVenueExtra)
													.reset(), "|",
											"           |", "", "");
						}

						else if (status == 0) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(RED).a(nullSpace).reset(), "|",
									ansi().fg(RED).a(taskDescriptionExtra)
											.reset(), "|",
									ansi().fg(RED).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						} else if (status == -1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(CYAN).a(nullSpace).reset(), "|",
									ansi().fg(CYAN).a(taskDescriptionExtra)
											.reset(), "|",
									ansi().fg(CYAN).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						} else if (status == 1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(), "|",
									ansi().fg(GREEN).a(taskDescriptionExtra)
											.reset(), "|",
									ansi().fg(GREEN).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						} else {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(),
									"|",
									ansi().fg(MAGENTA).a(taskDescriptionExtra)
											.reset(), "|", ansi().fg(MAGENTA)
											.a(taskVenueExtra).reset(), "|",
									"           |", "", "");
						}
						}
						result += displayLineSeparator();
					}

				}

				else {
					
					if(number<9){
						if (completed) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(YELLOW).a(nullSpace).reset(),
									" |",
									ansi().fg(YELLOW)
											.a(taskDescriptionExtra
													.substring(0, 25)).reset(),
									"|", ansi().fg(YELLOW).a(taskVenueExtra)
											.reset(), "|", "           |", "", "");
						}

						else if (status == 0) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(RED).a(nullSpace).reset(),
									" |",
									ansi().fg(RED)
											.a(taskDescriptionExtra
													.substring(0, 25)).reset(),
									"|", ansi().fg(RED).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						} else if (status == -1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(CYAN).a(nullSpace).reset(),
									" |",
									ansi().fg(CYAN)
											.a(taskDescriptionExtra
													.substring(0, 25)).reset(),
									"|", ansi().fg(CYAN).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						} else if (status == 1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(),
									" |",
									ansi().fg(GREEN)
											.a(taskDescriptionExtra
													.substring(0, 25)).reset(),
									"|",
									ansi().fg(GREEN).a(taskVenueExtra).reset(),
									"|", "           |", "", "");
						} else {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(),
									" |",
									ansi().fg(MAGENTA)
											.a(taskDescriptionExtra
													.substring(0, 25)).reset(),
									"|", ansi().fg(MAGENTA).a(taskVenueExtra)
											.reset(), "|", "           |", "", "");
						}
						
					}
					else{
					if (completed) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(YELLOW).a(nullSpace).reset(),
								"|",
								ansi().fg(YELLOW)
										.a(taskDescriptionExtra
												.substring(0, 25)).reset(),
								"|", ansi().fg(YELLOW).a(taskVenueExtra)
										.reset(), "|", "           |", "", "");
					}

					else if (status == 0) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(RED).a(nullSpace).reset(),
								"|",
								ansi().fg(RED)
										.a(taskDescriptionExtra
												.substring(0, 25)).reset(),
								"|", ansi().fg(RED).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
					} else if (status == -1) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(CYAN).a(nullSpace).reset(),
								"|",
								ansi().fg(CYAN)
										.a(taskDescriptionExtra
												.substring(0, 25)).reset(),
								"|", ansi().fg(CYAN).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
					} else if (status == 1) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(GREEN).a(nullSpace).reset(),
								"|",
								ansi().fg(GREEN)
										.a(taskDescriptionExtra
												.substring(0, 25)).reset(),
								"|",
								ansi().fg(GREEN).a(taskVenueExtra).reset(),
								"|", "           |", "", "");
					} else {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(MAGENTA).a(nullSpace).reset(),
								"|",
								ansi().fg(MAGENTA)
										.a(taskDescriptionExtra
												.substring(0, 25)).reset(),
								"|", ansi().fg(MAGENTA).a(taskVenueExtra)
										.reset(), "|", "           |", "", "");
					}
					
					}
					taskDescriptionExtra = taskDescriptionExtra.substring(25);

					for (int i = 0; i < descriptionLines - venueLines - 1; i++) {
						if (taskDescriptionExtra.length() > 25) {
							if(number<9){
								if (completed) {
									result += String.format(
											DISPLAY_TABLE_ROW_STRING_FORMAT,
											ansi().fg(YELLOW).a(nullSpace).reset(),
											" |",
											ansi().fg(YELLOW)
													.a(taskDescriptionExtra
															.substring(0, 25))
													.reset(), "|", ansi()
													.fg(YELLOW).a("").reset(), "|",
											"           |", "", "");
								} else if (status == 0) {
									result += String.format(
											DISPLAY_TABLE_ROW_STRING_FORMAT,
											ansi().fg(RED).a(nullSpace).reset(),
											" |",
											ansi().fg(RED)
													.a(taskDescriptionExtra
															.substring(0, 25))
													.reset(), "|", ansi().fg(RED)
													.a("").reset(), "|",
											"           |", "", "");
								} else if (status == -1) {
									result += String.format(
											DISPLAY_TABLE_ROW_STRING_FORMAT,
											ansi().fg(CYAN).a(nullSpace).reset(),
											" |",
											ansi().fg(CYAN)
													.a(taskDescriptionExtra
															.substring(0, 25))
													.reset(), "|", ansi().fg(CYAN)
													.a("").reset(), "|",
											"           |", "", "");
								} else if (status == 1) {
									result += String.format(
											DISPLAY_TABLE_ROW_STRING_FORMAT,
											ansi().fg(GREEN).a(nullSpace).reset(),
											" |",
											ansi().fg(GREEN)
													.a(taskDescriptionExtra
															.substring(0, 25))
													.reset(), "|", ansi().fg(GREEN)
													.a("").reset(), "|",
											"           |", "", "");
								} else {
									result += String
											.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
													ansi().fg(MAGENTA).a(nullSpace)
															.reset(),
													" |",
													ansi().fg(MAGENTA)
															.a(taskDescriptionExtra
																	.substring(0,
																			25))
															.reset(), "|", ansi()
															.fg(MAGENTA).a("")
															.reset(), "|",
													"           |", "", "");
								}
							}
							else{
							if (completed) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(YELLOW).a(nullSpace).reset(),
										"|",
										ansi().fg(YELLOW)
												.a(taskDescriptionExtra
														.substring(0, 25))
												.reset(), "|", ansi()
												.fg(YELLOW).a("").reset(), "|",
										"           |", "", "");
							} else if (status == 0) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(RED).a(nullSpace).reset(),
										"|",
										ansi().fg(RED)
												.a(taskDescriptionExtra
														.substring(0, 25))
												.reset(), "|", ansi().fg(RED)
												.a("").reset(), "|",
										"           |", "", "");
							} else if (status == -1) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(CYAN).a(nullSpace).reset(),
										"|",
										ansi().fg(CYAN)
												.a(taskDescriptionExtra
														.substring(0, 25))
												.reset(), "|", ansi().fg(CYAN)
												.a("").reset(), "|",
										"           |", "", "");
							} else if (status == 1) {
								result += String.format(
										DISPLAY_TABLE_ROW_STRING_FORMAT,
										ansi().fg(GREEN).a(nullSpace).reset(),
										"|",
										ansi().fg(GREEN)
												.a(taskDescriptionExtra
														.substring(0, 25))
												.reset(), "|", ansi().fg(GREEN)
												.a("").reset(), "|",
										"           |", "", "");
							} else {
								result += String
										.format(DISPLAY_TABLE_ROW_STRING_FORMAT,
												ansi().fg(MAGENTA).a(nullSpace)
														.reset(),
												"|",
												ansi().fg(MAGENTA)
														.a(taskDescriptionExtra
																.substring(0,
																		25))
														.reset(), "|", ansi()
														.fg(MAGENTA).a("")
														.reset(), "|",
												"           |", "", "");
							}
							}
							taskDescriptionExtra = taskDescriptionExtra
									.substring(25);
						}
					}
					if(number<9){
						if (completed) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(YELLOW).a(nullSpace).reset(), " |",
									ansi().fg(YELLOW).a(taskDescriptionExtra)
											.reset(), "|", ansi().fg(YELLOW).a("")
											.reset(), "|", "           |", "", "");
						} else if (status == 0) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
											.a(nullSpace).reset(), " |",
									ansi().fg(RED).a(taskDescriptionExtra).reset(),
									"|", ansi().fg(RED).a("").reset(), "|",
									"           |", "", "");
						} else if (status == -1) {
							result += String
									.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
											.fg(CYAN).a(nullSpace).reset(), " |",
											ansi().fg(CYAN).a(taskDescriptionExtra)
													.reset(), "|", ansi().fg(CYAN)
													.a("").reset(), "|",
											"           |", "", "");
						} else if (status == 1) {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(GREEN).a(nullSpace).reset(), " |",
									ansi().fg(GREEN).a(taskDescriptionExtra)
											.reset(), "|", ansi().fg(GREEN).a("")
											.reset(), "|", "           |", "", "");
						} else {
							result += String.format(
									DISPLAY_TABLE_ROW_STRING_FORMAT,
									ansi().fg(MAGENTA).a(nullSpace).reset(), " |",
									ansi().fg(MAGENTA).a(taskDescriptionExtra)
											.reset(), "|", ansi().fg(MAGENTA).a("")
											.reset(), "|", "           |", "", "");
						}
					}
					else{
					if (completed) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(YELLOW).a(nullSpace).reset(), "|",
								ansi().fg(YELLOW).a(taskDescriptionExtra)
										.reset(), "|", ansi().fg(YELLOW).a("")
										.reset(), "|", "           |", "", "");
					} else if (status == 0) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT, ansi().fg(RED)
										.a(nullSpace).reset(), "|",
								ansi().fg(RED).a(taskDescriptionExtra).reset(),
								"|", ansi().fg(RED).a("").reset(), "|",
								"           |", "", "");
					} else if (status == -1) {
						result += String
								.format(DISPLAY_TABLE_ROW_STRING_FORMAT, ansi()
										.fg(CYAN).a(nullSpace).reset(), "|",
										ansi().fg(CYAN).a(taskDescriptionExtra)
												.reset(), "|", ansi().fg(CYAN)
												.a("").reset(), "|",
										"           |", "", "");
					} else if (status == 1) {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(GREEN).a(nullSpace).reset(), "|",
								ansi().fg(GREEN).a(taskDescriptionExtra)
										.reset(), "|", ansi().fg(GREEN).a("")
										.reset(), "|", "           |", "", "");
					} else {
						result += String.format(
								DISPLAY_TABLE_ROW_STRING_FORMAT,
								ansi().fg(MAGENTA).a(nullSpace).reset(), "|",
								ansi().fg(MAGENTA).a(taskDescriptionExtra)
										.reset(), "|", ansi().fg(MAGENTA).a("")
										.reset(), "|", "           |", "", "");
					}
					}
					result += displayLineSeparator();

				}

			}

		}

		return result;
	}

	public static String displayTaskForAdd(Task task) {
		result = "";
		result = result + displayTask(task) + MESSAGE_ADDED;
		return result;
	}

	public static String displayTask(Task task) {
		result = "";
		result += getDescription(task);
		result += getVenue(task);
		// if a task has a start date, it will definitely have an end date, it
		// just depends whether they are equal
		if (task.hasStartDate() && !task.startDateEqualsEndDate()) { // append
																		// "from.. ..to"
			result += addFrom();
			result += addStartDate(task);
			if (task.hasStartTime()) {
				result += addStartTime(task);
			}
			result += addTo();
			result += addEndDate(task);
			if (task.hasEndTime()) {
				result += addEndTime(task);
			}
		} else if (task.hasStartDate()) {
			result += addOn();
			result += addStartDate(task);

			if (task.hasStartTime() && task.hasEndTime()
					&& !task.startTimeEqualsEndTime()) {
				result += addFrom();
				result += addStartTime(task);
				result += addTo();
				result += addEndTime(task);
			} else if (task.hasStartTime()) {
				result += addAt();
				result += addStartTime(task);
			}

		} else if (task.hasStartTime()) {
			result += addAt();
			result += addStartTime(task);
		}

		if (task.hasRecurrence()) {
			result += addRecurrence(task);
		}

		if (task.hasCompleted()) {
			result += addCompleted(task);
		}
		result = result.trim();
		return result;
	}

	private static String getTaskNumber(int number) {
		return number + 1 + "." + STRING_SPACE;
	}

	private static String getDescription(Task task) {
		return (task.getDescription() + STRING_SPACE);
	}

	private static String getVenue(Task task) {
		String venue = task.getVenue();
		if (venue != null) {
			return "at " + task.getVenue() + STRING_SPACE;
		} else {
			return "";
		}
	}

	private static String addFrom() {
		return "from" + STRING_SPACE;
	}

	private static String addTo() {
		return "to" + STRING_SPACE;
	}

	private static String addStartDate(Task task) {
		return task.getStartDate() + STRING_SPACE;
	}

	private static String addStartTime(Task task) {
		return task.getStartTime().toString()
				.substring(TIME_STRING_START_INDEX, TIME_STRING_END_INDEX)
				+ STRING_SPACE;
	}

	private static String addEndDate(Task task) {
		return task.getEndDate() + STRING_SPACE;
	}

	private static String addEndTime(Task task) {
		return task.getEndTime().toString()
				.substring(TIME_STRING_START_INDEX, TIME_STRING_END_INDEX)
				+ STRING_SPACE;
	}

	private static String addOn() {
		return "on" + STRING_SPACE;
	}

	private static String addAt() {
		return "at" + STRING_SPACE;
	}

	private static String addRecurrence(Task task) {
		return "(" + task.getRecurrence() + ")" + STRING_SPACE;
	}

	private static String addCompleted(Task task) {
		return "(" + task.hasCompleted() + ")" + STRING_SPACE;
	}
	
	public static void displayTop(){
		String resultTop = "";
		resultTop += DisplayHandler.displayLineSeparator();
		resultTop += String.format(
				DisplayHandler.DISPLAY_TABLE_ROW_STRING_FORMAT,
				ansi().fg(RED).a("ID").reset(), "  |",
				ansi().fg(MAGENTA).a(" DESCRIPTION").reset(), "|", ansi()
						.fg(CYAN).a(" VENUE").reset(), "|",
				ansi().fg(YELLOW).a(" TIME").reset(), "|", ansi().fg(GREEN)
						.a(" DATE").reset());
		resultTop += DisplayHandler.displayLineSeparator();
		System.out.print(resultTop);
	}
	
	public static void displayBottom(){
		String resultBottom = "";
		resultBottom += String.format(
				DisplayHandler.DISPLAY_TABLE_ROW_STRING_FORMAT,
				ansi().fg(RED).a("ID").reset(), "  |",
				ansi().fg(MAGENTA).a(" DESCRIPTION").reset(), "|", ansi()
						.fg(CYAN).a(" VENUE").reset(), "|",
				ansi().fg(YELLOW).a(" TIME").reset(), "|", ansi().fg(GREEN)
						.a(" DATE").reset());
		resultBottom += DisplayHandler.displayLineSeparator();
		System.out.print(resultBottom);
	}
	
	public static void displayContents(int number, Task task){
		String print = "";
		print += displayTaskInTable(number,task);
		System.out.println(print);
	}

	// this function returns -1 if the start date time of the task is before
	// the current date time, 0 if the the start date time of the task is after
	// the current date time but on the same day, 1 if the start date time of
	// the task
	// is after the current date time and not on the same day
	public static Integer determinePastPresentFuture(Task task) {
		DateTime currentDateTime = new DateTime();
		LocalDate taskDate = task.getStartDate();
		LocalTime taskTime = task.getStartTime();
		if (taskDate == null && taskTime == null) {
			return -2;
		} else {
			DateTime taskDateTime = taskDate.toDateTime(taskTime);

			int compareDate = DateTimeComparator.getInstance().compare(
					taskDateTime, currentDateTime);

			switch (compareDate) {
			case -1:
				return -1;
			case 0:
			case 1: {
				int taskDay = taskDateTime.getDayOfYear();
				int currentDay = currentDateTime.getDayOfYear();
				if (taskDay == currentDay) {
					return 0;
				} else {
					return 1;
				}
			}
			default:
				// we should never reach this case
				throw new IllegalArgumentException();
			}
		}
	}
}