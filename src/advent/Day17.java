package advent;

import java.io.IOException;

public class Day17 {
	
	private static final int FINAL_VALUE_A = 2017;
	private static final int FINAL_VALUE_B = 50_000_000;

	public static void main(String[] args) throws IOException {
		int stepSize = Integer.parseInt(FileUtility.fileToString("input/17.txt"));
		
		// Part one
		Node headA = buildList(stepSize, FINAL_VALUE_A);
		FileUtility.printAndOutput(find(headA, FINAL_VALUE_A).next.value, "output/17A.txt");
		
		// Part two
		// Awfully slow. Maybe there's a way to make the "steps" take constant time?
		Node headB = buildList(stepSize, FINAL_VALUE_B);
		FileUtility.printAndOutput(headB.next.value, "output/17B.txt");
	}

	private static class Node {
		public final int value;
		public Node next;
		
		public Node(int value, Node next) {
			this.value = value;
			this.next = next;
		}
	}
	
	private static Node buildList(int stepSize, int finalValue) {
		Node head = new Node(0, null);
		head.next = head;
		
		Node current = head;
		for (int i = 1; i <= finalValue; i++) {
			// Step forward n times
			for (int j = 0; j < stepSize; j++) {
				current = current.next;
			}
			
			// Insert
			Node insert = new Node(i, current.next);
			current.next = insert;
			current = insert;
		}
		
		return head;
	}
	
	private static Node find(Node head, int value) {
		Node current = head;
		
		do {
			if (current.value == value) {
				return current;
			}
			current = current.next;
		} while (current != head);
		
		return null;
	}
}
