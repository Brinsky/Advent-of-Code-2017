package advent;

import java.io.IOException;

public class Day02 {

	public static void main(String[] args) throws IOException {
		int[][] cells = parseSpreadsheet(FileUtility.fileToString("input/02.txt"));
		
		// Part one
		FileUtility.printAndOutput(maxMinChecksum(cells), "output/02A.txt");

		// Part two
		FileUtility.printAndOutput(dividesChecksum(cells), "output/02B.txt");
	}
	
	private static int[][] parseSpreadsheet(String input) {
		String[] lines = input.split("\n");
		int [][] cells = new int[lines.length][];
		
		for (int i = 0; i < lines.length; i++) {
			String[] values = lines[i].split("\\s+");
			int[] row = new int[values.length];
			
			for (int j = 0; j < row.length; j++) {
				row[j] = Integer.parseInt(values[j]);
			}
			
			cells[i] = row;
		}
		
		return cells;
	}
	
	private static int maxMinChecksum(int[][] cells) {
		int checksum = 0;
		
		for (int row = 0; row < cells.length; row++) {
			int max = Integer.MIN_VALUE;
			int min = Integer.MAX_VALUE;
			
			for (int col = 0; col < cells[row].length; col++) {
				if (cells[row][col] > max) {
					max = cells[row][col];
				}
				if (cells[row][col] < min) {
					min = cells[row][col];
				}
			}
			
			checksum += max - min;
		}
		
		return checksum;
	}
	
	private static int dividesChecksum(int[][] cells) {
		int checksum = 0;
		
		for (int row = 0; row < cells.length; row++) {
			checksum += rowDivision(cells[row]);
		}
		
		return checksum;
	}
	
	private static int rowDivision(int[] row) {
		for (int a = 0; a < row.length; a++) {
			for (int b = 0; b < row.length; b++) {
				if (a != b && divides(row[a], row[b])) {
					return row[b] / row[a];
				}
			}
		}
		
		return -1;
	}

	private static boolean divides(int a, int b) {
		return b % a == 0;
	}
}
