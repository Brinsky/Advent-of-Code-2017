package advent;

import java.io.IOException;

import advent.Day18.Operation;
import advent.Day18.Registers;

public class Day23 {
	
	/* Note: These values are (probably) specific to my input */
	
	// Value in register 'b' before outermost loop starts
	private static final int START_VAL = 107_900; 

	// Difference between 'c' and 'b' before outermost loop starts
	private static final int OFFSET = 17_000;

	// Amount by which 'b' is incremented each iteration of the outermost loop
	private static final int STEP_SIZE = 17;

	public static void main(String[] args) throws IOException {
		Operation[] ops = Day18.parse(FileUtility.fileToString("input/23.txt"));
		
		// Part one
		FileUtility.printAndOutput(execute(new Registers(), ops).mulCount, "output/23A.txt");
		
		// Part two
		// By observation, I determined that the "program" was counting the
		// composite numbers between the initial values of registers 'b' and
		// 'c' and in steps of 17
		System.out.println(countComposites(START_VAL, OFFSET, STEP_SIZE));
	}

	private static class StateInfo {
		public final int mulCount;
		
		public StateInfo(int mulCount) {
			this.mulCount = mulCount;
		}
	}
	
	private static StateInfo execute(Registers regs, Operation[] ops) {
		int mulCount = 0;
		int index = 0;
		
		while (index >= 0 && index < ops.length) {
			Operation current = ops[index];
			long valueA = current.getValue(regs, 0);
			long valueB = (current.numFields() > 1)
					? current.getValue(regs, 1) : -1;
			long offset = 1;
			
			switch (current.type) {
			case "set":
				regs.put(current.getRef(0), valueB);
				break;
			case "sub":
				regs.put(current.getRef(0), valueA - valueB);
				break;
			case "mul":
				regs.put(current.getRef(0), valueA * valueB);
				mulCount++;
				break;
			case "jnz":
				if (valueA != 0) {
					offset = valueB;
				}
				break;
			default:
				throw new IllegalStateException();
			}
			
			index += offset;
		}
		
		return new StateInfo(mulCount);
	}
	
	private static boolean isPrime(int number) {
		if (number == 2) {
			return true;
		} else if (number % 2 == 0) {
			return false;
		}
		
		for (int i = 3; i * i < number; i += 2) {
			if (number % i == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	private static int countComposites(int start, int offset, int step) {
		int composites = 0;
		
		for (int i = start; i <= start + offset; i += step) {
			if (!isPrime(i)) {
				composites++;
			}
		}
		
		return composites;
	}
}
