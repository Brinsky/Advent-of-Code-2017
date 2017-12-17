package advent;

import java.io.IOException;

public class Day12 {

	public static void main(String[] args) throws IOException {
		int[][] graph = parse(FileUtility.fileToString("input/12.txt"));
		
		// Part one
		FileUtility.printAndOutput(
				traverse(graph, 0, new boolean[graph.length], 0),
				"output/12A.txt");
		
		// Part two
		FileUtility.printAndOutput(countGroups(graph), "output/12B.txt");
	}
	
	private static int[][] parse(String input) {
		String[] lines = input.split("\n");
		int[][] graph = new int[lines.length][];
		
		for (int i = 0; i < lines.length; i++) {
			String[] fields = lines[i].split(",?\\s+");
			graph[i] = new int[fields.length - 2];
			
			for (int j = 0; j < graph[i].length; j++) {
				graph[i][j] = Integer.parseInt(fields[j + 2]);
			}
		}
		
		return graph;
	}

	private static int traverse(int[][] graph, int node, boolean[] visited, int visits) {
		if (!visited[node]) {
			visited[node] = true;
			visits++;
			
			for (int child : graph[node]) {
				visits = traverse(graph, child, visited, visits);
			}
		}
		
		return visits;
	}
	
	private static int countGroups(int[][] graph) {
		int groups = 0;
		boolean[] visited = new boolean[graph.length];
		
		for (int node = 0; node < graph.length; node++) {
			if (!visited[node]) {
				groups++;
				traverse(graph, node, visited, 0);
			}
		}
		
		return groups;
	}
}
