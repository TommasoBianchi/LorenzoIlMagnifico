package it.polimi.ingsw.LM45.view.cli;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import it.polimi.ingsw.LM45.util.Pair;
import it.polimi.ingsw.LM45.view.cli.ConsoleWriter.ConsoleColor;

/**
 * This is a synchronized, Thread-safe Singleton to read strings and integers from System.in
 * 
 * @author Tommy
 *
 */
public class ConsoleReader implements Runnable {

	private static final Scanner SCANNER = new Scanner(System.in);

	private static SynchronousUpdatingQueue<String> buffer = new ConsoleReader().new SynchronousUpdatingQueue<>();
	private static Thread currentWaitingThread;
	private static Object currentWaitingThreadLock = new Object();

	private static boolean isRunning = false;

	private ConsoleReader() {
	}

	/**
	 * Policy: if a thread is waiting for something and another thread comes in, then it means that the first one is outdated
	 * so it does no longer need anything from us; interrupt him (caller will need to manage InterruptedException).
	 * 
	 * @return the first full line read by System.in after calling this method
	 * @throws InterruptedException if the input is no longer meaningful for this thread because a new one has arrived
	 */
	public static String readLine() throws InterruptedException {
		if (!isRunning) {
			initialize();
		}

		synchronized (currentWaitingThreadLock) {
			if (currentWaitingThread != null && currentWaitingThread != Thread.currentThread()) {
				currentWaitingThread.interrupt();
			}

			currentWaitingThread = Thread.currentThread();
		}

		String string = buffer.take();
		synchronized (currentWaitingThreadLock) {
			currentWaitingThread = null;
			return string;
		}
	}

	/**
	 * Note: any string read and not parsable to integer will be discarded
	 * 
	 * @return the first integer read by System.in after calling this method
	 * @throws InterruptedException if the input is no longer meaningful for this thread because a new one has arrived
	 * @see #readLine
	 */
	public static int readInt() throws InterruptedException {
		while (true) {
			try {
				return Integer.parseInt(readLine());
			}
			catch (NumberFormatException e) {
				ConsoleWriter.printError("Input not valid. Please insert a number !");
			}
		}
	}

	/**
	 * @param optionsWithDescriptions a list of pairs containing the options and the descriptions to print to the user
	 * @param isCommand boolean to differentiate commands (printed with WHITE color) and choices (printed with CYAN color)
	 * @return the chosen option
	 * @throws InterruptedException if the input is no longer meaningful for this thread because a new one has arrived
	 */
	public static <T> T readOption(List<Pair<T, String>> optionsWithDescriptions, boolean isCommand) throws InterruptedException {
		if (isCommand)
			for (int i = 0; i < optionsWithDescriptions.size(); i++) {
				ConsoleWriter.printCommand((i + 1) + " - " + optionsWithDescriptions.get(i)._2());
			}
		else
			for (int i = 0; i < optionsWithDescriptions.size(); i++) {
				ConsoleWriter.printChoice((i + 1) + " - " + optionsWithDescriptions.get(i)._2());
			}

		int selection = readInt();
		while (selection <= 0 || selection > optionsWithDescriptions.size()) {
			ConsoleWriter.printError("Input not valid. Please insert a number between 1 and " + optionsWithDescriptions.size() + " !");
			selection = readInt();
		}

		return optionsWithDescriptions.get(selection - 1)._1();
	}
	
	/**
	 * @param options an array containing the options we want to choose from
	 * @param descriptionsSupplier a function providing a string description given an option
	 * @param isCommand boolean to differentiate commands (printed with WHITE color) and choices (printed with CYAN color)
	 * @return the chosen option
	 * @throws InterruptedException if the input is no longer meaningful for this thread because a new one has arrived
	 */
	public static <T> T readOption(T[] options, Function<T, String> descriptionsSupplier, boolean isCommand) throws InterruptedException {
		return readOption(Arrays.stream(options).map(option -> new Pair<>(option, descriptionsSupplier.apply(option))).collect(Collectors.toList()), isCommand);
	}

	@Override
	public void run() {
		while (isRunning) {
			String string = SCANNER.nextLine();
			buffer.put(string);
		}
	}

	private static synchronized void initialize() {
		if (!isRunning) {
			new Thread(new ConsoleReader()).start();
			isRunning = true;
		}
	}
	
	private class SynchronousUpdatingQueue<T> {

		private T element;
		private Object elementLock = new Object();

		public T take() throws InterruptedException {
			synchronized (elementLock) {
				while (element == null)
					elementLock.wait();
				T result = element;
				element = null;
				return result;
			}
		}

		public void put(T element) {
			synchronized (elementLock) {
				this.element = element;
				elementLock.notifyAll();
			}
		}

	}

}
