package advent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day03 {

	public static void main(String[] args) throws IOException {
		int address = Integer.parseInt(FileUtility.textFileToString("input/03.txt"));
		
		// Part one
		FileUtility.printAndOutput(locate(address).magnitude(), "output/03A.txt");
		
		// Part two
		FileUtility.printAndOutput(firstExceeding(address), "output/03B.txt");
	}
	
	private static int firstExceeding(int cutoff) {
		List<Integer> sequence = new ArrayList<Integer>();
		
		sequence.add(1);
		int currentAddress = 1;
		
		while (sequence.get(currentAddress - 1) < cutoff) {
			currentAddress++;
			Position pos = locate(currentAddress);
			int sum = 0;
			
			// This iterates over the current cell itself, but the value
			// hasn't been set yet so it doesn't contribute to the sum.
			for (int deltaX = -1; deltaX <= 1; deltaX++) {
				for (int deltaY = -1; deltaY <= 1; deltaY++) {
					sum += safeGet(sequence, addressOf(pos.x + deltaX, pos.y + deltaY) - 1);
				}
			}
			
			sequence.add(sum);
		}
		
		return sequence.get(currentAddress - 1);
	}
	
	private static int safeGet(List<Integer> list, int index) {
		return (index < list.size()) ? list.get(index) : 0;
	}
	
	private static Position locate(int address) {
		int layer = layer(address);
		int side = (address - layerStart(layer)) / layerWidth(layer);
		int delta = (address - layerStart(layer)) % layerWidth(layer);
		
		switch (side) {
		case 0:
			return new Position(layer, -layer + 1 + delta);
		case 1:
			return new Position(layer - 1 - delta, layer);
		case 2:
			return new Position(-layer, layer - 1 - delta);
		case 3:
			return new Position(-layer + 1 + delta, - layer);
		}
		
		return null;
	}
	
	private static int addressOf(int x, int y) {
		int layer = Math.max(Math.abs(x), Math.abs(y));
		int side, delta;
		
		if (x == layer && y > -layer) {
			side = 0;
			delta = y + layer - 1;
		} else if (y == layer && x < layer) {
			side = 1;
			delta = layer - 1 - x;
		} else if (x == -layer && y < layer) {
			side = 2;
			delta = layer - 1 - y;
		} else /* if (y == -layer && x > -layer) */ {
			side = 3;
			delta = x + layer - 1;
		}
		
		return layerStart(layer) + side * layerWidth(layer) + delta;
	}
	
	private static class Position {
		public final int x;
		public final int y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int magnitude() {
			return Math.abs(x) + Math.abs(y);
		}
	}

	private static int layer(int address) {
		return ((int) Math.ceil(Math.sqrt(address))) / 2;
	}
	
	private static int layerWidth(int n) {
		return 2 * n;
	}
	
	private static int layerStart(int n) {
		int start = layerWidth(n - 1) + 1;
		return start * start + 1;
	}
}
