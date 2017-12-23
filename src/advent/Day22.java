package advent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import advent.Day19.Heading;

public class Day22 {
	
	private static final int BURSTS_A = 10_000;
	private static final int BURSTS_B = 10_000_000;

	public static void main(String[] args) throws IOException {
		Grid grid = parse(FileUtility.fileToString("input/22.txt"));
		
		// Part one
		FileUtility.printAndOutput(
				infectionsCaused(grid, BURSTS_A, SIMPLE_RULE),
				"output/22A.txt");

		// Part two
		FileUtility.printAndOutput(
				infectionsCaused(grid, BURSTS_B, ADVANCED_RULE),
				"output/22B.txt");
	}

	private enum NodeState { CLEAN, INFECTED, WEAKENED, FLAGGED };
	
	private static class Grid {
		
		private static class Point {
			private final int x;
			private final int y;
			
			public Point(int x, int y) {
				this.x = x;
				this.y = y;
			}
			
			@Override
			public boolean equals(Object o) {
				if (!(o instanceof Point)) {
					return false;
				}
				
				Point p = (Point) o;
				return p.x == x && p.y == y;
			}
			
			@Override
			public int hashCode() {
				return 7 * x + 197 * y;
			}
		}
		
		private final Map<Point, NodeState> grid = new HashMap<Point, NodeState>();
		
		public void set(int row, int col, NodeState state) {
			grid.put(new Point(row, col), state);
		}
		
		public NodeState get(int row, int col) {
			if (!grid.containsKey(new Point(row, col))) {
				grid.put(new Point(row, col), NodeState.CLEAN);
			}
			
			return grid.get(new Point(row, col));
		}
		
		@Override
		public Grid clone() {
			Grid clone = new Grid();
			
			for (Entry<Point, NodeState> entry : grid.entrySet()) {
				clone.set(
						entry.getKey().x, 
						entry.getKey().y,
						entry.getValue());
			}
			
			return clone;
		}
	}
	
	private static Grid parse(String input) {
		String[] lines = input.split("\n");
		Grid grid = new Grid();
		
		int size = lines.length;
		int offset = size / 2; // Used to make the center (0, 0)
		
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				NodeState state = (lines[row].charAt(col) == '#')
						? NodeState.INFECTED
						: NodeState.CLEAN;
				grid.set(row - offset, col - offset, state);
			}
		}
		
		return grid;
	}
	
	@FunctionalInterface
	private interface ReplacementRule {
		public NodeState replace(NodeState state);
	}
	
	private static final ReplacementRule SIMPLE_RULE = state -> {
		switch (state) {
		case CLEAN: return NodeState.INFECTED;
		case INFECTED: return NodeState.CLEAN;
		default: throw new IllegalStateException();
		}
	};
	
	private static final ReplacementRule ADVANCED_RULE = state -> {
		switch (state) {
		case CLEAN: return NodeState.WEAKENED;
		case WEAKENED: return NodeState.INFECTED;
		case INFECTED: return NodeState.FLAGGED;
		case FLAGGED: return NodeState.CLEAN;
		default: throw new IllegalStateException();
		}
	};
	
	private static Heading newHeading(Heading current, NodeState state) {
		switch (state) {
		case CLEAN: return current.getCounterClockwiseHeading();
		case WEAKENED: return current;
		case INFECTED: return current.getClockwiseHeading();
		case FLAGGED: return current.getOppositeHeading();
		default: throw new IllegalStateException();
		}
	}
	
	private static int infectionsCaused(Grid grid, int bursts, ReplacementRule rule) {
		grid = grid.clone(); // Don't modify the original grid
		int row = 0;
		int col = 0;
		int infectionsCaused = 0;
		Heading current = Heading.NORTH;
		
		for (int i = 0; i < bursts; i++) {
			current = newHeading(current, grid.get(row, col));
			grid.set(row, col, rule.replace(grid.get(row, col)));
			
			if (grid.get(row, col) == NodeState.INFECTED) {
				infectionsCaused++;
			}
			
			row += current.rowHeading;
			col += current.colHeading;
		}
		
		return infectionsCaused;
	}
}