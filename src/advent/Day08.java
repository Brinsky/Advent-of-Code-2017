package advent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day08 {

	public static void main(String[] args) throws IOException {
		Instruction[] instructions = parse(FileUtility.textFileToString("input/08.txt"));
		Map<String, Integer> registers = new HashMap<String, Integer>();
		
		int maxSeen = process(registers, instructions);
		
		// Part one
		FileUtility.printAndOutput(maxValue(registers), "output/08A.txt");
		
		// Part two
		FileUtility.printAndOutput(maxSeen, "output/08B.txt");
	}

	private static class Instruction {
		public String register;
		public int delta;
		
		public String testRegister;
		public String operator;
		public int testValue;
	}
	
	private static Instruction parseInstruction(String text) {
		String[] fields = text.split("\\s+");
		Instruction ins = new Instruction();
		
		ins.register = fields[0];
		ins.delta = Integer.parseInt(fields[2]);
		
		if (fields[1].equals("dec")) {
			ins.delta = -ins.delta;
		}
		
		ins.testRegister = fields[4];
		ins.operator = fields[5];
		ins.testValue = Integer.parseInt(fields[6]);
		
		return ins;
	}
	
	private static Instruction[] parse(String input) {
		String[] lines = input.split("\n");
		Instruction[] instructions = new Instruction[lines.length];
		
		for (int i = 0; i < lines.length; i++) {
			instructions[i] = parseInstruction(lines[i]);
		}
		
		return instructions;
	}
	
	private static void ensureInitialized(Map<String, Integer> registers, String register) {
		if (!registers.containsKey(register)) {
			registers.put(register, 0);
		}
	}
	
	private static boolean test(int a, int b, String operator) {
		switch (operator) {
		case ">": return a > b;
		case "<": return a < b;
		case ">=": return a >= b;
		case "<=": return a <= b;
		case "==": return a == b;
		case "!=": return a != b;
		default: throw new IllegalStateException();
		}
	}
	
	private static int process(Map<String, Integer> registers, Instruction[] instructions) {
		int maxSeen = 0; // All registers start at zero
		for (Instruction ins : instructions) {
			ensureInitialized(registers, ins.register);
			ensureInitialized(registers, ins.testRegister);
			
			int oldValue = registers.get(ins.register);
			if (test(registers.get(ins.testRegister), ins.testValue, ins.operator)) {
				registers.put(ins.register, oldValue + ins.delta);
				
				if (registers.get(ins.register) > maxSeen) {
					maxSeen = registers.get(ins.register);
				}
			}

		}
		
		return maxSeen;
	}
	
	private static int maxValue(Map<String, Integer> registers) {
		int max = Integer.MIN_VALUE;
		
		for (int value : registers.values()) {
			if (value > max) {
				max = value;
			}
		}
		
		return max;
	}
}
