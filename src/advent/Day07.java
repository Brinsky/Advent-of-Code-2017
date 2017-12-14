package advent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day07 {

	public static void main(String[] args) throws IOException {
		Program root = parse(FileUtility.textFileToString("input/07.txt"));
		
		// Part one
		FileUtility.printAndOutput(root.name, "output/07A.txt");
		
		// Part two
		FileUtility.printAndOutput(getImbalance(root), "output/07B.txt");
	}

	private static class Program {
		public final String name;
		public final int weight;
		
		public Program[] children;
		public Program parent;
		
		public String[] childNames;
		
		private int subtreeSum = -1;
		
		public Program(String name, int weight, String[] childNames) {
			this.name = name;
			this.weight = weight;
			this.childNames = childNames;
		}
		
		public int subtreeSum() {
			return subtreeSum(this);
		}
		
		public int numChildren() {
			return children.length;
		}
		
		/** Total weight of this node and all nodes below it */
		private static int subtreeSum(Program root) {
			if (root.subtreeSum < 0) {
				int sum = 0;
				for (Program child : root.children) {
					sum += subtreeSum(child);
				}
				
				root.subtreeSum = sum + root.weight;
			}
		
			return root.subtreeSum;
		}
	}

	/** Returns the root of the Program tree */
	private static Program parse(String input) {
		Map<String, Program> programs = new HashMap<String, Program>();
		
		String[] lines = input.split("\n");
		for (int i = 0; i < lines.length; i++) {
			int weightStart = lines[i].indexOf('(');
			int weightEnd = lines[i].indexOf(')');
			int arrowEnd = lines[i].indexOf('>');
			
			String name = lines[i].substring(0, weightStart).trim();
			int weight = Integer.parseInt(lines[i].substring(weightStart + 1, weightEnd));
			
			String[] childNames;
			if (arrowEnd > 0) {
				childNames = lines[i].substring(arrowEnd + 1).trim().split(",\\s+");
			} else {
				childNames = new String[0];
			}
			
			programs.put(name, new Program(name, weight, childNames));
		}
		
		// Update parents and children with actual reference links
		for (Program program : programs.values()) {
			program.children = new Program[program.childNames.length];
			
			for (int i = 0; i < program.childNames.length; i++) {
				program.children[i] = programs.get(program.childNames[i]);
				program.children[i].parent = program;
			}
		}
		
		return findRoot(programs);
	}

	private static Program findRoot(Map<String, Program> programs) {
		for (Program program : programs.values()) {
			if (program.parent == null) {
				return program;
			}
		}
		
		return null;
	}
	
	/** A post-order traversal looking for imbalance on the way back up */
	private static int getImbalance(Program root) {
		if (root.numChildren() == 0) {
			return -1;
		}
		
		// Check if any of our subtrees are unbalanced
		for (Program child : root.children) {
			int imbalance = getImbalance(child);
			if (imbalance >= 0) {
				return imbalance;
			}
		}
		
		// Test if this node itself is unbalanced.
		// The lowest imbalance MUST occur on a node with 3 or more children -
		// otherwise, there would be multiple ways to correct the weight
		int expectedSum = expectedSubtreeSum(root.children);
		for (Program child : root.children) {
			if (child.subtreeSum() != expectedSum) {
				return expectedSum - (child.subtreeSum() - child.weight);
			}
		}
		
		return -1;
	}
	
	/** We assume at most one child has a different subtree sum from the others */
	private static int expectedSubtreeSum(Program[] children) {
		// If 0 and 1 are equal, they must have the correct sum
		if (children[0].subtreeSum() == children[1].subtreeSum()) {
			return children[0].subtreeSum();
		// We don't know which is correct if there are fewer than 3 children
		} else if (children.length < 3) {
			throw new IllegalStateException();
		// If 0 and 2 agree, they must have the correct sum
		} else if (children[0].subtreeSum() == children[2].subtreeSum()) {
			return children[0].subtreeSum();
		// If 0 disagrees with both 1 and 2, it must have the incorrect value
		} else {
			return children[1].subtreeSum();
		}
	}
}
