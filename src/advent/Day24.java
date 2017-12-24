package advent;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day24 {

	public static void main(String[] args) throws IOException {
		ComponentSet components = parse(FileUtility.fileToString("input/24.txt"));
		List<BridgeInfo> bridges = buildBridges(components);
		
		// Part one
		FileUtility.printAndOutput(strongestBridge(bridges), "output/24A.txt");
		
		// Part two
		FileUtility.printAndOutput(strongestLongestBridge(bridges), "output/24B");
	}
	
	private static class ComponentSet {
		
		Map<Integer, Set<Component>> components = new HashMap<>();
		
		public void add(Component component) {
			for (int port : component.ports) {
				if (!components.containsKey(port)) {
					components.put(port, new HashSet<Component>());
				}
				
				components.get(port).add(component);
			}
		}
		
		public Set<Component> getSet(int port) {
			return components.get(port);
		}
		
		public int numUnused(int port) {
			int unused = 0;
			
			for (Component component : components.get(port)) {
				if (!component.used) {
					unused++;
				}
			}
			
			return unused;
		}
	}

	private static class Component {
		public boolean used = false;
		public final int[] ports = new int[2];
		
		public Component(int portA, int portB) {
			ports[0] = portA;
			ports[1] = portB;
		}
		
		public int strength() {
			return ports[0] + ports[1];
		}
		
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Component)) {
				return false;
			}
			
			Component c = (Component) o;
			return Arrays.equals(ports, c.ports);
		}
		
		@Override
		public int hashCode() {
			return 7 * ports[0] + 197 * ports[1];
		}
	}
	
	private static ComponentSet parse(String input) {
		String[] lines = input.split("\n");
		ComponentSet components = new ComponentSet();
		
		for (String line : lines) {
			String[] fields = line.split("/");
			Component component = new Component(
					Integer.parseInt(fields[0]), Integer.parseInt(fields[1]));
			components.add(component);
		}
		
		return components;
	}
	
	private static int strength(Collection<Component> bridge) {
		int strength = 0;
		
		for (Component component : bridge) {
			strength += component.strength();
		}
		
		return strength;
	}
	
	private static class BridgeInfo {
		public final int strength;
		public final int length;
		
		public BridgeInfo(int strength, int length) {
			this.strength = strength;
			this.length = length;
		}
	}
	
	private static int strongestLongestBridge(List<BridgeInfo> bridges) {
		int bestLength = Integer.MIN_VALUE;
		int bestStrength = Integer.MIN_VALUE;
		
		for (BridgeInfo bridge : bridges) {
			if (bridge.length > bestLength) {
				bestLength = bridge.length;
				bestStrength = bridge.strength;
			} else if (bridge.length == bestLength && bridge.strength > bestStrength) {
				bestStrength = bridge.strength;
			}
		}
		
		return bestStrength;
	}
	
	private static int strongestBridge(List<BridgeInfo> bridges) {
		int bestStrength = Integer.MIN_VALUE;
		
		for (BridgeInfo bridge : bridges) {
			if (bridge.strength > bestStrength) {
				bestStrength = bridge.strength;
			}
		}
		
		return bestStrength;
	}
	
	private static List<BridgeInfo> buildBridges(ComponentSet components) {
		List<BridgeInfo> bridges = new ArrayList<BridgeInfo>();
		buildBridges(bridges, components, new ArrayDeque<Component>(), 0);
		
		return bridges;
	}
	
	private static void buildBridges(List<BridgeInfo> bridges,
			ComponentSet components, Deque<Component> currentBridge, int currentPort) {
		if (components.numUnused(currentPort) == 0) {
			bridges.add(new BridgeInfo(strength(currentBridge), currentBridge.size()));
			return;
		}
		
		for (Component comp : components.getSet(currentPort)) {
			if (!comp.used) {
				comp.used = true;
				currentBridge.addLast(comp);
				
				int nextPort = (comp.ports[0] == currentPort) ? comp.ports[1] : comp.ports[0];
				buildBridges(bridges, components, currentBridge, nextPort);
				
				comp.used = false;
				currentBridge.removeLast();
			}
		}
	}
}
