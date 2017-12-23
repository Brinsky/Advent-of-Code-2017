package advent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day21 {
	
	private static final Pattern seed = new Pattern(".#./..#/###");

	private static final int ITERATIONS_A = 5;
	private static final int ITERATIONS_B = 18;
	
	public static void main(String[] args) throws IOException {
		Map<Pattern, Pattern> rules = parse(FileUtility.fileToString("input/21.txt"));
		
		// Part one
		FileUtility.printAndOutput(
				performIterations(rules, ITERATIONS_A).countTrue(), 
				"output/21A.txt");

		// Part two
		FileUtility.printAndOutput(
				performIterations(rules, ITERATIONS_B).countTrue(), 
				"output/21B.txt");
	}
	
	private static class Pattern {
		public final boolean[][] grid;
		public final int size;
		
		public Pattern(String pattern) {
			String[] lines = pattern.split("/");
			
			// Assume square pattern
			size = lines.length;
			grid = new boolean[size][size];
			
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					grid[row][col] = lines[row].charAt(col) == '#';
				}
			}
		}
		
		// Create a larger pattern from a grid of subpatterns
		public Pattern(Pattern[][] subpatterns) {
			int subSize = subpatterns[0][0].size;
			
			size = subSize * subpatterns.length;
			grid = new boolean[size][size];
			
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					int metaRow = row / subSize;
					int metaCol = col / subSize;
					int subRow = row % subSize;
					int subCol = col % subSize;
					
					grid[row][col] = subpatterns[metaRow][metaCol].grid[subRow][subCol];
				}
			}
		}
		
		// Extract a subpattern of a larger pattern
		public Pattern(Pattern meta, int subSize, int metaRow, int metaCol) {
			size = subSize;
			grid = new boolean[size][size];
			
			int rowStart = metaRow * subSize;
			int colStart = metaCol * subSize;
			
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					grid[row][col] = meta.grid[rowStart + row][colStart + col];
				}
			}
		}
		
		@FunctionalInterface
		private interface Transform {
			public int get(int row, int col);
		}
		
		private Transform flipCol(Transform transform) {
			return (r, c) -> (size - transform.get(r, c) - 1);
		}
		
		private Transform clockwiseRow(int degree) {
			switch (degree % 4) {
			case 0: return (row, col) -> row;
			case 1: return (row, col) -> col;
			case 2: return (row, col) -> size - row - 1;
			case 3: return (row, col) -> size - col - 1;
			default: throw new IllegalStateException();
			}
		}
		
		private Transform clockwiseCol(int degree) {
			return clockwiseRow(degree + 1);
		}
		
		public boolean matches(Pattern p) {
			if (p.size != size) {
				return false;
			}
			
			// Test all possible flips and rotations
			for (int degree = 0; degree < 4; degree++) {
				if (equals(p, clockwiseRow(degree), clockwiseCol(degree))
						|| equals(p, clockwiseRow(degree), flipCol(clockwiseCol(degree)))) {
					return true;
				}
			}
			
			return false;
		}
		
		private boolean equals(Pattern p, Transform rowTransform, Transform colTransform) {
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					int rowT = rowTransform.get(row, col);
					int colT = colTransform.get(row, col);
					
					if (p.grid[row][col] != grid[rowT][colT]) {
						return false;
					}
				}
			}
			
			return true;
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Pattern)) {
				return false;
			}
			
			return equals((Pattern) o, (r, c) -> r, (r, c) -> c);
		}
		
		public int countTrue() {
			int numTrue = 0;
			
			for (int row = 0; row < size; row++) {
				for (int col = 0; col < size; col++) {
					if (grid[row][col]) {
						numTrue++;
					}
				}
			}
			
			return numTrue;
		}
	}
	
	private static Map<Pattern, Pattern> parse(String input) {
		String[] lines = input.split("\n");
		Map<Pattern, Pattern> rules = new HashMap<Pattern, Pattern>(lines.length);
		
		for (String line : lines) {
			String[] patterns = line.split("\\s+=>\\s+");
			rules.put(new Pattern(patterns[0]), new Pattern(patterns[1]));
		}
		
		return rules;
	}
	
	private static Pattern getReplacement(Map<Pattern, Pattern> rules, Pattern p) {
		for (Pattern q : rules.keySet()) {
			if (q.matches(p)) {
				return rules.get(q);
			}
		}
		
		return null;
	}
	
	private static Pattern iterate(Map<Pattern, Pattern> rules, Pattern p) {
		int subSize;
		if (p.size % 2 == 0) {
			subSize = 2;
		} else if (p.size % 3 == 0) {
			subSize = 3;
		} else {
			throw new IllegalStateException();
		}
		
		int metaSize = p.size / subSize;
		Pattern[][] subpatterns = new Pattern[metaSize][metaSize];
		
		for (int metaRow = 0; metaRow < metaSize; metaRow++) {
			for (int metaCol = 0; metaCol < metaSize; metaCol++) {
				Pattern subpattern = new Pattern(p, subSize, metaRow, metaCol);
				subpatterns[metaRow][metaCol] = getReplacement(rules, subpattern);
			}
		}
		
		return new Pattern(subpatterns);
	}
	
	private static Pattern performIterations(Map<Pattern, Pattern> rules, int iterations) {
		Pattern p = seed;
		
		for (int i = 0; i < iterations; i++) {
			p = iterate(rules, p);
		}
		
		return p;
	}
}
