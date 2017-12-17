package advent;

import java.io.IOException;

public class Day13 {

	public static void main(String[] args) throws IOException {
		Scanner[] scanners = parse(FileUtility.fileToString("input/13.txt"));
		
		// Part one
		FileUtility.printAndOutput(severity(scanners, 0), "output/13A.txt");
		
		// Part two
		FileUtility.printAndOutput(bestDelay(scanners), "output/13B.txt");
	}
	
	private static class Scanner {
		public final int depth;
		public final int range;
		
		public Scanner(int depth, int range) {
			this.depth = depth;
			this.range = range;
		}
	}

	private static Scanner[] parse(String input) {
		String[] lines = input.split("\n");
		Scanner[] scanners = new Scanner[lines.length];
		
		for (int i = 0; i < lines.length; i++) {
			String[] fields = lines[i].split(":\\s*");
			scanners[i] = new Scanner(
						Integer.parseInt(fields[0]), 
						Integer.parseInt(fields[1]));
		}
		
		return scanners;
	}
	
	private static int position(int range, int time) {
		int cyclePos = time % (2 * range - 2);
		
		if (cyclePos < range) {
			return cyclePos;
		} else {
			return (2 * range - 2) - cyclePos;
		}
	}
	
	private static int severity(Scanner[] scanners, int delay) {
		// -1 indicates that we were never caught. Severity could still be 0 if
		// we get caught by scanner 0.
		int severity = -1;
		
		for (int i = 0; i < scanners.length; i++) {
			if (position(scanners[i].range, scanners[i].depth + delay) == 0) {
				if (severity < 0) {
					severity = 0;
				}
				severity += scanners[i].range * scanners[i].depth;
			}
		}
		
		return severity;
	}
	
	private static int bestDelay(Scanner[] scanners) {
		int delay = 0;

		// Brute force test of every possible delay
		while (severity(scanners, delay) >= 0) {
			delay++;
		}
		
		return delay;
	}
}
