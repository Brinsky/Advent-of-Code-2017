package advent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day16 {
	
	private static final int DANCERS = 16;
	
	// The key to this approach is that the name swaps and the other two
	// operations can be performed independently of one another (order-wise).
	// It is thus possible to represent the dance as a composition of two
	// functions, f(g(x)), where f performs the name-swapping operations and
	// g the other operations. Having computed f and g, it is easy to
	// exponentially compose them with themselves to get, for example
	// f(x) -> f(f(x)) -> f(f(f(f(x)))) -> f(f(f(f(f(f(f(f(x)))))))).
	// Finally, the billion-times-composed functions F and G (using groups of
	// 10 compositions at a time) can themselves be composed to produce the
	// correct answer.
	//
	// Maybe there's a better way :)
	public static void main(String[] args) throws IOException {
		Dance dance = parse(FileUtility.fileToString("input/16.txt"), DANCERS);
		
		// Part one
		FileUtility.printAndOutput(toString(performTenfold(dance, 0)), "output/16A.txt");

		// Part two
		FileUtility.printAndOutput(toString(performTenfold(dance, 9)), "output/16B.txt");
	}

	private static int charToValue(char c) {
		return (int) (c - 'a');
	}
	
	private static char valueToChar(int value) {
		return (char) (value + 'a');
	}

	public static String toString(int[] array) {
		StringBuilder builder = new StringBuilder(array.length);
		
		for (int i = 0; i < array.length; i++) {
			builder.append(valueToChar(array[i]));
		}
		
		return builder.toString();
	}
	
	private static class Dance {
		
		private static abstract class IndexMove { };
		
		private static class Spin extends IndexMove {
			
			public final int amount;
			
			public Spin(String text) {
				amount = Integer.parseInt(text.substring(1));
			}
		}
		
		private static class Exchange extends IndexMove {
			
			public final int indexA;
			public final int indexB;
			
			public Exchange(String text) {
				String[] fields = text.substring(1).split("/");
				indexA = Integer.parseInt(fields[0]);
				indexB = Integer.parseInt(fields[1]);
			}
		}
		
		private static class Partner {
			
			private final int nameA;
			private final int nameB;
			
			public Partner(String text) {
				String[] fields = text.substring(1).split("/");
				nameA = charToValue(fields[0].charAt(0));
				nameB = charToValue(fields[1].charAt(0));
			}
		}
		
		private final int numDancers;
		
		private final List<IndexMove> indexMoves = new ArrayList<IndexMove>();
		private final List<Partner> partnerMoves = new ArrayList<Partner>();
		
		public Dance(String[] moves, int numDancers) {
			this.numDancers = numDancers;
			
			for (String move : moves) {
				switch (move.charAt(0)) {
				case 's':
					indexMoves.add(new Spin(move));
					break;
				case 'x':
					indexMoves.add(new Exchange(move));
					break;
				case 'p':
					partnerMoves.add(new Partner(move));
					break;
				default:
					throw new IllegalArgumentException();
				}
			}
		}
		
		// Slow spin in lieu of using circular arrays
		public static void spin(int[] dancers, int amount) {
			int[] buffer = Arrays.copyOf(dancers, dancers.length);
			
			// "Shift" the array leftwards
			for (int i = 0; i < dancers.length; i++) {
				dancers[(i + amount) % dancers.length] = buffer[i];
			}
		}
		
		public int[] getPartnerMoves() {
			int[] dancers = identity(numDancers);
			
			for (Partner move : partnerMoves) {
				swap(dancers, move.nameA, move.nameB);
			}
			
			return dancers;
		}
		
		public int[] getIndexMoves() {
			int[] dancers = identity(numDancers);
			
			for (IndexMove move : indexMoves) {
				if (move instanceof Spin) {
					spin(dancers, ((Spin) move).amount);
				} else {
					Exchange exchange = (Exchange) move;
					swap(dancers, exchange.indexA, exchange.indexB);
				}
			}
			
			return dancers;
		}
	}

	private static Dance parse(String input, int numDancers) {
		return new Dance(input.split(","), numDancers);
	}
	
	private static int find(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				return i;
			}
		}
		
		return -1;
	}
	
	private static void swap(int[] array, int indexA, int indexB) {
		int temp = array[indexA];
		array[indexA] = array[indexB];
		array[indexB] = temp;
	}
	
	private static int[] identity(int numDancers) {
		int[] array = new int[numDancers];
		
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		
		return array;
	}
	
	private static int[] composeWith(int[] danceA, int[] danceB) {
		int[] output = new int[danceA.length];
		
		for (int i = 0; i < danceA.length; i++) {
			output[find(danceA, i)] = danceB[i];
		}
		
		return output;
	}
	
	private static int[] performPartnerMoves(int[] partnerMoves, int[] dancers) {
		int[] output = new int[dancers.length];
		
		for (int i = 0; i < partnerMoves.length; i++) {
			output[find(dancers, partnerMoves[i])] = dancers[find(dancers, i)];
		}
		
		return output;
	}
	
	/** Compose a dance with itself 10^power times */
	private static int[] composeTenfold(int[] moves, int power) {
		for (int i = 0; i < power; i++) {
			int[] temp = Arrays.copyOf(moves, moves.length);
			
			for (int j = 0; j < 9; j++) {
				moves = composeWith(moves, temp);
			}
		}
		
		return moves;
	}
	
	/** Perform a dance 10^power times in a row */
	private static int[] performTenfold(Dance dance, int power) {
		int[] indexMoves = composeTenfold(dance.getIndexMoves(), power);
		int[] partnerMoves = composeTenfold(dance.getPartnerMoves(), power);
		
		return performPartnerMoves(partnerMoves, indexMoves);
	}
}
