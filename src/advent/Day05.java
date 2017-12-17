package advent;

import java.io.IOException;
import java.util.Arrays;

public class Day05 {

	public static void main(String[] args) throws IOException {
		int[] jumps = parse(FileUtility.fileToString("input/05.txt"));
		
		// Part one
		FileUtility.printAndOutput(
				stepsToEscape(Arrays.copyOf(jumps, jumps.length),
						i -> 1), "output/05A.txt");
		
		// Part two
		FileUtility.printAndOutput(stepsToEscape(jumps,
				i -> (i >= 3) ? -1 : 1), "output/05B.txt");
	}

	private static int[] parse(String input) {
		String[] lines = input.split("\n");
		int[] numbers = new int[lines.length];
		
		for (int i = 0; i < lines.length; i++) {
			numbers[i] = Integer.parseInt(lines[i]);
		}
		
		return numbers;
	}
	
	@FunctionalInterface
	private interface OffsetRule {
		public int offset(int original);
	}
	
	private static int stepsToEscape(int[] jumps, OffsetRule rule) {
		int steps = 0;
		int position = 0;
		
		while (position >= 0 && position < jumps.length) {
			int oldPosition = position;
			position += jumps[position];
			jumps[oldPosition] += rule.offset(jumps[oldPosition]);
			steps++;
		}
		
		return steps;
	}
}
