package advent;

import java.io.IOException;

public class Day19 {

	public static void main(String[] args) throws IOException {
		char[][] grid = parse(FileUtility.fileToString("input/19.txt"));
		PathInfo info = traverse(grid);
		
		// Part one
		FileUtility.printAndOutput(info.path, "output/19A.txt");
		
		// Part two
		FileUtility.printAndOutput(info.totalSteps, "output/19B.txt");
	}

	private static char[][] parse(String input) {
		String[] lines = input.split("\n");
		char[][] grid = new char[lines.length][];
		
		for (int i = 0; i < lines.length; i++) {
			grid[i] = lines[i].toCharArray();
		}
		
		return grid;
	}
	
	private static int findStartColumn(char[][] grid) {
		for (int i = 0; i < grid[0].length; i++) {
			if (grid[0][i] == '|') {
				return i;
			}
		}
		
		return -1;
	}
	
	private static char safeGet(char[][] grid, int row, int col) {
		if (row >= 0 && row < grid.length && col >= 0 && col < grid[row].length) {
			return grid[row][col];
		} else {
			return ' ';
		}
	}
	
	private enum Heading {
		NORTH(-1, 0),
		EAST(0, 1),
		SOUTH(1, 0),
		WEST(0, -1);
		
		public final int rowHeading;
		public final int colHeading;
		
		private Heading(int rowHeading, int colHeading) {
			this.rowHeading = rowHeading;
			this.colHeading = colHeading;
		}
		
		public Heading getClockwiseHeading() {
			switch (this) {
			case NORTH: return EAST;
			case EAST: return SOUTH;
			case SOUTH: return WEST;
			case WEST: return NORTH;
			default: return null;
			}
		}
		
		public Heading getCounterClockwiseHeading() {
			switch (this) {
			case NORTH: return WEST;
			case EAST: return NORTH;
			case SOUTH: return EAST;
			case WEST: return SOUTH;
			default: return null;
			}
		}
	}
	
	private static class PathInfo {
		public final String path;
		public final int totalSteps;
		
		public PathInfo(String path, int totalSteps) {
			this.path = path;
			this.totalSteps = totalSteps;
		}
	}
	
	// Upper left-hand corner is (0,0)
	private static PathInfo traverse(char[][] grid) {
		int steps = 1;
		int row = 0;
		int col = findStartColumn(grid);
		Heading heading = Heading.SOUTH;
		StringBuilder builder = new StringBuilder();
		
		while (true) {
			if (Character.isAlphabetic(grid[row][col])) {
				builder.append(grid[row][col]);
			}
			
			Heading cw = heading.getClockwiseHeading();
			Heading ccw = heading.getCounterClockwiseHeading();
			
			if (safeGet(grid, row + heading.rowHeading, col + heading.colHeading) != ' ') {
				// Do nothing
			} else if (safeGet(grid, row + cw.rowHeading, col + cw.colHeading) != ' ') {
				heading = cw;
			} else if (safeGet(grid, row + ccw.rowHeading, col + ccw.colHeading) != ' ') {
				heading = ccw;
			} else {
				return new PathInfo(builder.toString(), steps);
			}
			
			row += heading.rowHeading;
			col += heading.colHeading;
			steps++;
		}
	}
	
	
}
