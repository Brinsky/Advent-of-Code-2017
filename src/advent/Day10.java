package advent;

import java.io.IOException;

public class Day10 {

	private static final int[] TRAILING_LENGTHS = new int[] {17, 31, 73, 47, 23};
	private static final int LIST_SIZE = 256;
	private static final int REPETITIONS = 64;
	private static final int BLOCK_SIZE = 16;
	
	public static void main(String[] args) throws IOException {
		String input = FileUtility.fileToString("input/10.txt");
		
		// Part one
		int product = applyReversals(getList(), parseLengths(input), new HashingState());
		FileUtility.printAndOutput(product, "output/10A.txt");
		
		// Part two
		FileUtility.printAndOutput(knotHash(input), "output/10B.txt");
	}

	private static int[] parseLengths(String input) {
		String[] numbers = input.split(",\\s*");
		int[] lengths = new int[numbers.length];
		
		for (int i = 0; i < numbers.length; i++) {
			lengths[i] = Integer.parseInt(numbers[i]);
		}
		
		return lengths;
	}
	
	private static int[] asciiLengths(String input) {
		int[] lengths = new int[input.length() + TRAILING_LENGTHS.length];
		for (int i = 0; i < input.length(); i++) {
			lengths[i] = (int) input.charAt(i);
		}
		for (int j = 0; j < TRAILING_LENGTHS.length; j++) {
			lengths[input.length() + j] = TRAILING_LENGTHS[j];
		}
		
		return lengths;
	}
	
	private static int[] getList() {
		int[] list = new int[LIST_SIZE];
		
		for (int i = 0; i < list.length; i++) {
			list[i] = i;
		}
		
		return list;
	}
	
	private static void reverseSublist(int[] list, int start, int length) {
		for (int i = 0; i < length / 2; i++) {
			int first = (start + i) % list.length;
			int last = (start + length - 1 - i) % list.length;
			
			int temp = list[first];
			list[first] = list[last];
			list[last] = temp;
		}
	}
	
	private static class HashingState {
		public int currentPosition = 0;
		public int skipSize = 0;
	}
	
	private static int applyReversals(int[] list, int[] lengths, HashingState state) {
		for (int length : lengths) {
			reverseSublist(list, state.currentPosition, length);
			state.currentPosition += length + state.skipSize;
			state.skipSize++;
		}
		
		return list[0] * list[1];
	}
	
	public static String knotHash(String input) {
		int[] lengths = asciiLengths(input);
		int[] list = getList();
		HashingState state = new HashingState();
		
		// "Sparse hash"
		for (int i = 0; i < REPETITIONS; i++) {
			applyReversals(list, lengths, state);
		}
		
		// "Dense hash"
		int[] denseHash = new int[LIST_SIZE / BLOCK_SIZE];
		for (int i = 0; i < list.length; i++) {
			denseHash[i / BLOCK_SIZE] ^= list[i];
		}
		
		StringBuilder builder = new StringBuilder(32);
		for (int value : denseHash) {
			builder.append(String.format("%02x", value));
		}
		
		return builder.toString();
	}
}
