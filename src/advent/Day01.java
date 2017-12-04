package advent;

import java.io.IOException;

public class Day01 {

	public static void main(String[] args) throws IOException {
		int[] digits = getDigits(FileUtility.textFileToString("input/01.txt"));
		
		// Part one
		FileUtility.printAndOutput(sumRepeated(digits, 1), "output/01A.txt");
		
		// Part two
		FileUtility.printAndOutput(sumRepeated(digits, digits.length / 2), "output/01B.txt");
	}
	
	private static int[] getDigits(String input) {
		int[] digits = new int[input.length()];
		
		for (int i = 0; i < digits.length; i++) {
			digits[i] = input.charAt(i) - '0';
		}
		
		return digits;
	}
	
	public static int sumRepeated(int[] digits, int offset) {
		int sum = 0;
		for (int i = 0; i < digits.length; i++) {
			if (digits[i] == digits[(i + offset) % digits.length]) {
				sum += digits[i];
			}
		}
		
		return sum;
	}
}
