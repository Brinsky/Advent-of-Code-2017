package advent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day11 {

	public static void main(String[] args) throws IOException {
		PathInfo info = walkPath(parse(FileUtility.fileToString("input/11.txt")));
		
		// Part one
		FileUtility.printAndOutput(info.finalDistance, "output/11A.txt");
		
		// Part two
		FileUtility.printAndOutput(info.maxDistance, "output/11B.txt");
	}
	
	// Using "cube coordinates" due to https://www.redblobgames.com/grids/hexagons/
	private static class Position {
		public int x, y, z;
		
		public Position(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public void plusEquals(Position p) {
			x += p.x;
			y += p.y;
			z += p.z;
		}
		
		public int distance() {
			return (Math.abs(x) + Math.abs(y) + Math.abs(z)) / 2;
		}
	}
	
	private enum Direction {
		NORTH("n", new Position(0, 1, -1)),
		NORTH_EAST("ne", new Position(1, 0, -1)),
		SOUTH_EAST("se", new Position(1, -1, 0)),
		SOUTH("s", new Position(0, -1, 1)),
		SOUTH_WEST("sw", new Position(-1, 0, 1)),
		NORTH_WEST("nw", new Position(-1, 1, 0));
		
		public final String name;
		public final Position delta;
		
		private Direction(String name, Position delta) {
			this.name = name;
			this.delta = delta;
		}
		
		public static Direction getDirection(String name) {
			for (Direction d : Direction.values()) {
				if (d.name.equals(name)) {
					return d;
				}
			}
			
			return null;
		}
	}

	private static List<Direction> parse(String input) throws IOException {
		List<Direction> path = new ArrayList<Direction>();
		Scanner in = new Scanner(input);
		in.useDelimiter(",");
		
		while (in.hasNext()) {
			path.add(Direction.getDirection(in.next()));
		}
		
		in.close();
		return path;
	}
	
	private static class PathInfo {
		public int maxDistance;
		public int finalDistance;
	}
	
	private static PathInfo walkPath(List<Direction> path) {
		Position pos = new Position(0, 0, 0);
		PathInfo info = new PathInfo();
		info.maxDistance = Integer.MIN_VALUE;
		
		for (Direction step : path) {
			pos.plusEquals(step.delta);
			
			if (pos.distance() > info.maxDistance) {
				info.maxDistance = pos.distance();
			}
		}
		
		info.finalDistance = pos.distance();
		return info;
	}
}
