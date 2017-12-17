package advent;

import java.io.IOException;

public class Day09 {

	public static void main(String[] args) throws IOException {
		String stream = FileUtility.fileToString("input/09.txt");
		StreamInfo info = wade(stream);
		
		// Part one
		FileUtility.printAndOutput(info.score, "output/09A.txt");

		// Part two
		FileUtility.printAndOutput(info.garbageCount, "output/09B.txt");
		
	}
	
	private static class StreamInfo {
		public int score = 0;
		public int garbageCount = 0;
	}

	private static StreamInfo wade(String input) {
		StreamInfo info = new StreamInfo();
		
		int depth = 0;
		boolean inGarbage = false;
		
		char[] chars = input.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			
			if (inGarbage) {
				if (c == '!') {
					i++;
				} else if (c == '>') {
					inGarbage = false;
				} else {
					info.garbageCount++;
				}
			} else if (c == '{') {
				depth++;
			} else if (c == '}') {
				info.score += depth;
				depth--;
			} else if (c == '<') {
				inGarbage = true;
			}
		}
		
		return info;
	}
}
