package advent;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Day04 {

	public static void main(String[] args) throws IOException {
		String[][] words = parseWords(FileUtility.fileToString("input/04.txt"));

		// Part one
		FileUtility.printAndOutput(
				linesWithoutRepeats(words, word -> word), "output/04A.txt");
		
		// Part two
		FileUtility.printAndOutput(linesWithoutRepeats(words, sortChars), "output/04B.txt");	
	}
	
	@FunctionalInterface
	private interface WordTransform {
		public String transformWord(String word);
	}
	
	private static WordTransform sortChars = word -> {
		char[] chars = word.toCharArray();
		Arrays.sort(chars);
		return new String(chars);
	};

	private static String[][] parseWords(String input) {
		String[] lines = input.split("\n");
		String[][] words = new String[lines.length][];
		
		for (int i = 0; i < lines.length; i++) {
			words[i] = lines[i].split("\\s+");
		}
		
		return words;
	}
	
	private static int linesWithoutRepeats(String[][] words, WordTransform transform) {
		int valid = words.length; // # of lines
		
		for (int line = 0; line < words.length; line++) {
			Set<String> set = new HashSet<String>();
			
			for (int i = 0; i < words[line].length; i++) {
				String word = transform.transformWord(words[line][i]);
				
				if (set.contains(word)) {
					valid--;
					break;
				} else {
					set.add(word);
				}
			}
		}
		
		return valid;
	}
}
