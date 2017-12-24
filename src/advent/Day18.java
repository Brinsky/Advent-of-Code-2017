package advent;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Day18 {

	public static void main(String[] args) throws IOException {
		Operation[] ops = parse(FileUtility.fileToString("input/18.txt"));
		
		// Part one
		FileUtility.printAndOutput(firstReceivedFreq(ops), "output/18A.txt");
		
		// Part two
		FileUtility.printAndOutput(countSends(ops), "output/18B.txt");
	}
	
	private static int charToValue(char c) {
		return (int) (c - 'a');
	}
	
	public static class Operation {
		public final String type;
		
		private final int[] values;
		private final boolean[] references;
		
		public Operation(String type, String[] fields) {
			this.type = type;
			
			values = new int[fields.length];
			references = new boolean[fields.length];
			
			for (int i = 0; i < fields.length; i++) {
				references[i] = isReference(fields[i]);
				
				if (references[i]) {
					values[i] = charToValue(fields[i].charAt(0));
				} else {
					values[i] = Integer.parseInt(fields[i]);
				}
			}
		}
		
		private static boolean isReference(String field) {
			return Character.isAlphabetic(field.charAt(0));
		}
		
		/** 
		 * Gets the value of this operation corresponding to {@code index}.
		 * Dereferences and performs a lookup in {@code registers} if needed.
		 */
		public long getValue(Registers regs, int index) {
			if (references[index]) {
				return regs.get(values[index]);
			} else {
				return values[index];
			}
		}
		
		public int getRef(int index) {
			if (references[index]) {
				return values[index];
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		public int numFields() {
			return values.length;
		}
	}
	
	public static Operation[] parse(String input) {
		String[] lines = input.split("\n");
		Operation[] ops = new Operation[lines.length];
		
		for (int i = 0; i < lines.length; i++) {
			String type = lines[i].substring(0, 3);
			String[] fields = lines[i].substring(4).split("\\s+");
			ops[i] = new Operation(type, fields);
		}
		
		return ops;
	}
	
	public static class Registers {
		private final Map<Integer, Long> registers = new HashMap<Integer, Long>(27);
		
		public void put(int register, long value) {
			registers.put(register, value);
		}
		
		public long get(int register) {
			if (!registers.containsKey(register)) {
				registers.put(register, 0L);
			}
			
			return registers.get(register);
		}
	}
	
	private static class StateInfo {
		public int index;
		public Deque<Long> sentValues;
		
		public StateInfo(int index, Deque<Long> sentValues) {
			this.index = index;
			this.sentValues = sentValues;
		}
	}
	
	private static StateInfo execute(Registers regs, Operation[] ops, 
			int index, Deque<Long> receivedValues, boolean soundMode) {
		Deque<Long> sentValues = new ArrayDeque<Long>();
		
		while (index >= 0 && index < ops.length) {
			Operation current = ops[index];
			long valueA = current.getValue(regs, 0);
			long valueB = (current.numFields() > 1)
					? current.getValue(regs, 1) : -1;
			long offset = 1;
			
			switch (current.type) {
			case "snd":
				sentValues.add(valueA);
				break;
			case "set":
				regs.put(current.getRef(0), valueB);
				break;
			case "add":
				regs.put(current.getRef(0), valueA + valueB);
				break;
			case "mul":
				regs.put(current.getRef(0), valueA * valueB);
				break;
			case "mod":
				regs.put(current.getRef(0), valueA % valueB);
				break;
			case "rcv":
				if (!soundMode) { // Multi-program send/receive mode
					if (receivedValues.isEmpty()) {
						return new StateInfo(index, sentValues);
					} else {
						regs.put(current.getRef(0), receivedValues.removeFirst());
					}
				} else if (valueA != 0) { // "Sound" mode
					return new StateInfo(index, sentValues);
				}
				break;
			case "jgz":
				if (valueA > 0) {
					offset = valueB;
				}
				break;
			default:
				throw new IllegalStateException();
			}
			
			index += offset;
		}
		
		return new StateInfo(index, sentValues);
	}
	
	public static long firstReceivedFreq(Operation[] ops) {
		StateInfo info = execute(new Registers(), ops, 0, new ArrayDeque<Long>(), true);
		return info.sentValues.removeLast();
	}
	
	public static long countSends(Operation[] ops) {
		Registers regsA = new Registers();
		Registers regsB = new Registers();
		
		regsA.put(charToValue('p'), 0);
		regsB.put(charToValue('p'), 1);
		
		StateInfo infoA = new StateInfo(0, new ArrayDeque<Long>());
		StateInfo infoB = new StateInfo(0, new ArrayDeque<Long>());
		
		long sentCount = 0;
		
		do {
			infoA = execute(regsA, ops, infoA.index, infoB.sentValues, false);
			infoB = execute(regsB, ops, infoB.index, infoA.sentValues, false);
			sentCount += infoB.sentValues.size();
		} while (!infoB.sentValues.isEmpty());
		
		return sentCount;
	}
}