package advent;

import java.io.IOException;

public class Day14 {

	private static final int ROWS = 128;
	private static final int COLS = 128;
	
	public static void main(String[] args) throws IOException {
		HashBits[] rows = parse(FileUtility.fileToString("input/14.txt"));
		
		// Part one
		FileUtility.printAndOutput(bitCount(rows), "output/14A.txt");
		
		// Part two
		FileUtility.printAndOutput(numGroups(rows), "output/14B.txt");
	}

	private static class HashBits {
		private final long bits1;
		private final long bits2;
		
		public HashBits(String hash) {
			bits1 = Long.parseUnsignedLong(hash.substring(16, 32), 16);
			bits2 = Long.parseUnsignedLong(hash.substring(0, 16), 16);
		}
		
		public int getBit(int i) {
			long bits;
			
			if (i >= 0 && i < 64) {
				bits = bits1;
			} else if (i >= 64 && i < 128) {
				i -= 64;
				bits = bits2;
			} else {
				throw new ArrayIndexOutOfBoundsException();
			}
			
			return (int) (((1L << i) & bits) >>> i);
		}
		
		public int bitCount() {
			return Long.bitCount(bits1) + Long.bitCount(bits2);
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder(128);
			
			for (int i = 0; i < COLS; i++) {
				if (getBit(i) == 0) {
					builder.append('0');
				} else {
					builder.append('1');
				}
			}
			
			return builder.toString();
		}
	}
	
	private static HashBits[] parse(String key) {
		HashBits[] rows = new HashBits[ROWS];
		
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new HashBits(Day10.knotHash(key + "-" + i));
		}
		
		return rows;
	}

	private static int bitCount(HashBits[] rows) {
		int bits = 0;
		
		for (HashBits row : rows) {
			bits += row.bitCount();
		}
		
		return bits;
	}
	
	private static int numGroups(HashBits[] rows) {
		int[][] groups = new int[ROWS][COLS];
		
		int currentGroup = 1;
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				// Traverse and mark any undiscovered groups of 1s
				if (rows[row].getBit(col) == 1 && groups[row][col] == 0) {
					markGroup(rows, row, col, currentGroup, groups);
					currentGroup++;
				}
			}
		}
		
		return currentGroup - 1;
	}
	
	private static void markGroup(HashBits[] rows, int row, int col, int group, int[][] groups) {
		if (row >= 0 && row < ROWS
				&& col >= 0 && col < COLS
				&& rows[row].getBit(col) == 1
				&& groups[row][col] == 0) {
			groups[row][col] = group;
			markGroup(rows, row + 1, col, group, groups);
			markGroup(rows, row - 1, col, group, groups);
			markGroup(rows, row, col + 1, group, groups);
			markGroup(rows, row, col - 1, group, groups);
		}
	}
}
