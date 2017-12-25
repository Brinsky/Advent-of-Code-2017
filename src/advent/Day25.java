package advent;

public class Day25 {
	
	/* Note: the values below were source from input/25.txt */
	
	private static final int STEPS = 12586542;
	
	private static final Operation[] ifZero = new Operation[] {
			new Operation(true, 1, 1),	// A = 0
			new Operation(false, 1, 2),	// B = 1
			new Operation(true, 1, 3),	// C = 2
			new Operation(true, -1, 4), // D = 3
			new Operation(true, -1, 0), // E = 4
			new Operation(true, 1, 0),	// F = 5
	};
	
	private static final Operation[] ifOne = new Operation[] {
			new Operation(false, -1, 1),	// A = 0
			new Operation(true, -1, 1),		// B = 1
			new Operation(false, -1, 0),	// C = 2
			new Operation(true, -1, 5), 	// D = 3
			new Operation(false, -1, 3), 	// E = 4
			new Operation(true, -1, 4),		// F = 5
	};

	public static void main(String[] args) {
		Tape tape = new Tape(STEPS);
		int state = 0;
		int position = 0;
		
		for (int i = 0; i < STEPS; i++) {
			Operation op = (tape.read(position)) ? ifOne[state] : ifZero[state];
			
			tape.write(position, op.writeValue);
			position += op.offset;
			state = op.nextState;
		}
		
		System.out.println(tape.getChecksum());
	}
	
	private static class Operation {
		public final boolean writeValue;
		public final int offset;
		public final int nextState;
		
		public Operation(boolean writeValue, int offset, int nextState) {
			this.writeValue = writeValue;
			this.offset = offset;
			this.nextState = nextState;
		}
	}
	
	private static class Tape {
		private final boolean[] tape;
		
		// Location of the "middle" of the tape
		private final int offset;
		
		private int checksum = 0;
		
		public Tape(int radius) {
			tape = new boolean[radius * 2 + 1];
			offset = tape.length / 2;
		}
		
		public void write(int position, boolean value) {
			int index = position + offset;
			
			if (tape[index] && !value) {
				checksum--;
			} else if (!tape[index] && value) {
				checksum++;
			}
			
			tape[index] = value;
		}
		
		public boolean read(int position) {
			return tape[position + offset];
		}
		
		public int getChecksum() {
			return checksum;
		}
	}
}
