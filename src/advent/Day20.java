package advent;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day20 {
	
	private static final Pattern vectorPattern =
			Pattern.compile("<(-?\\d+),(-?\\d+),(-?\\d+)>");
	
	// Note - the solutions here are naive and the time values were determined
	// by experimentation and finger-crossing. They may not work for every
	// puzzle input.
	public static void main(String[] args) throws IOException {
		Particle[] particles = parse(FileUtility.fileToString("input/20.txt"));
		
		// Part one
		FileUtility.printAndOutput(findNearest(particles, 1000), "output/20A.txt");
		
		// Part two
		FileUtility.printAndOutput(collisionSurvivals(particles, 100), "output/20B.txt");
		
		
	}

	private static class Particle {

		public final int id;
		public final int length;
		
		public final long[] position;
		public final long[] velocity;
		public final long[] acceleration;
		
		public Particle(int id, int length) {
			this.id = id;
			this.length = length;
			
			position = new long[length];
			velocity = new long[length];
			acceleration = new long[length];
		}
		
		public long positionAt(int component, long time) {
			return position[component] 
					+ time * velocity[component]
					+ (time * (time + 1) * acceleration[component]) / 2;
		}
		
		@Override
		public int hashCode() {
			return (int) (position[0] + 10 * position[1]
					+ 100 * position[2] + 1000 * velocity[0]);
		}
	}
	
	private static void findComponents(long[] array, Matcher matcher) {
		matcher.find();
		
		for (int i = 0; i < array.length; i++) {
			array[i] = Long.parseLong(matcher.group(i + 1));
		}
	}
	
	private static Particle[] parse(String input) {
		String[] lines = input.split("\n");
		Particle[] particles = new Particle[lines.length];
		
		for (int i = 0; i < lines.length; i++) {
			Particle current = new Particle(i, 3);
			Matcher matcher = vectorPattern.matcher(lines[i]);

			findComponents(current.position, matcher);
			findComponents(current.velocity, matcher);
			findComponents(current.acceleration, matcher);
			
			particles[i] = current;
		}
		
		return particles;
	}
	
	private static long norm(long[] vector) {
		long sum = 0;
		
		for (int i = 0; i < vector.length; i++) {
			sum += Math.abs(vector[i]);
		}
		
		return sum;
	}
	
	private static int findNearest(Particle[] particles, long time) {
		int id = -1;
		long smallestNorm = Long.MAX_VALUE;
		long[] position = new long[3];
		
		for (Particle particle : particles) {
			for (int i = 0; i < 3; i++) {
				position[i] = particle.positionAt(i, time);
			}
			
			if (norm(position) < smallestNorm) {
				id = particle.id;
				smallestNorm = norm(position);
			}
		}
		
		return id;
	}
	
	private static boolean isCollision(Particle a, Particle b, long time) {
		for (int i = 0; i < a.length; i++) {
			if (a.positionAt(i, time) != b.positionAt(i, time)) {
				return false;
			}
		}
		
		return true;
	}
	
	private static void processCollisionsAt(Particle[] particles, 
			boolean[] removed, long time) {
		for (int i = 0; i < particles.length; i++) {
			if (!removed[i]) {
				for (int j = i + 1; j < particles.length; j++) {
					if (!removed[j]
							&& isCollision(particles[i], particles[j], time)) {
						removed[i] = true;
						removed[j] = true;
					}
				}
			}
		}
	}
	
	private static int collisionSurvivals(Particle[] particles, long time) {
		boolean[] removed = new boolean[particles.length];
		
		for (long i = 0; i < time; i++) {
			processCollisionsAt(particles, removed, i);
		}
		
		int survivals = 0;
		for (int i = 0; i < removed.length; i++) {
			if (!removed[i]) {
				survivals++;
			}
		}
		
		return survivals;
	}
}
