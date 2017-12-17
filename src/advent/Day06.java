package advent;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day06 {
	
	private static final int NUM_BANKS = 16;

	public static void main(String[] args) throws IOException {
		int[] banks = parse(FileUtility.fileToString("input/06.txt"));
		
		CycleData cycles = performCycles(banks);
		
		// Part one
		FileUtility.printAndOutput(cycles.total, "output/06A.txt");
		
		// Part two
		FileUtility.printAndOutput(cycles.betweenRepeats, "output/06B.txt");
	}
	
	private static int[] parse(String input) {
		Scanner in = new Scanner(input);
		
		int[] banks = new int[NUM_BANKS];
		for (int i = 0; i < banks.length; i++) {
			banks[i] = in.nextInt();
		}
		
		in.close();
		return banks;
	}
	
	private static class State {
		private final int[] banks;
		
		public State(int[] banks) {
			this.banks = Arrays.copyOf(banks, banks.length);
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof State)) {
				return false;
			}
			
			State other = (State) o;
			return Arrays.equals(banks, other.banks);
		}
		
		@Override
		public int hashCode() {
			return banks[0] + 10 * banks[1] + 100 * banks[2] + 1000 * banks[3];
		}
	}

	private static class CycleData {
		private final int total;
		private final int betweenRepeats;
		
		public CycleData(int total, int betweenRepeats) {
			this.total = total;
			this.betweenRepeats = betweenRepeats;
		}
	}
	
	private static CycleData performCycles(int[] banks) {
		Map<State, Integer> states = new HashMap<State, Integer>();
		State current = new State(banks);
		int cycle = 0;
		
		do {
			states.put(current, cycle);
			redistribute(banks);
			current = new State(banks);
			cycle++;
		} while (!states.containsKey(current));
		
		return new CycleData(cycle, cycle - states.get(current));
	}
	
	private static int indexOfMax(int[] array) {
		int max = Integer.MIN_VALUE;
		int index = -1;
		
		for (int i = 0; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				index = i;
			}
		}
		
		return index;
	}
	
	private static void redistribute(int[] banks) {
		int index = indexOfMax(banks);
		int start = (index + 1) % banks.length;
		int amount = banks[index];
		banks[index] = 0;
		
		// # of times we will add 1 to every bank
		int multiples = amount / banks.length;
		
		// Remaining amount to distribute
		int remaining = amount % banks.length;
		
		for (int i = 0; i < banks.length; i++) {
			banks[i] += multiples;
		}
		
		while (remaining > 0) {
			banks[start] += 1;
			start = (start + 1) % banks.length;
			remaining--;
		}
	}
}
