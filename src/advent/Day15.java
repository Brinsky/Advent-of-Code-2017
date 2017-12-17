package advent;

import java.io.IOException;

public class Day15 {
	
	// Start values sourced from input/15.txt
	private static final long START_A = 634;
	private static final long START_B = 301;
	
	private static final long FACTOR_A = 16807;
	private static final long FACTOR_B = 48271;
	
	private static final int MOD_A = 4;
	private static final int MOD_B = 8;
	
	private static final long DIVISOR = 2147483647;
	
	private static final long MASK_16 = 0xFFFF;
	
	private static final int TRIALS_1 = 40_000_000;
	private static final int TRIALS_2 = 5_000_000;

	public static void main(String[] args) throws IOException {
		// Part one
		FileUtility.printAndOutput(countMatches(TRIALS_1,
				i -> nextNumber(i, FACTOR_A),
				i -> nextNumber(i, FACTOR_B)),
				"output/15A.txt");
		
		// Part two
		FileUtility.printAndOutput(countMatches(TRIALS_2,
				i -> nextCriteriaMatch(i, FACTOR_A, MOD_A),
				i -> nextCriteriaMatch(i, FACTOR_B, MOD_B)),
				"output/15B.txt");
	}
	
	@FunctionalInterface
	private interface Generator {
		public long nextNumber(long previous);
	}
	
	private static long nextNumber(long previous, long factor) {
		return (previous * factor) % DIVISOR;
	}
	
	private static long nextCriteriaMatch(long previous, long factor, int mod) {
		long current = nextNumber(previous, factor);
		
		while (current % mod != 0) {
			current = nextNumber(current, factor);
		}
		
		return current;
	}
	
	private static int countMatches(int numTrials, Generator a, Generator b) {
		long currentA = START_A;
		long currentB = START_B;
		int matches = 0;
		
		for (int i = 0; i < numTrials; i++) {
			currentA = a.nextNumber(currentA);
			currentB = b.nextNumber(currentB);
			
			if ((currentA & MASK_16) == (currentB & MASK_16)) {
				matches++;
			}
		}
		
		return matches;
	}
}
