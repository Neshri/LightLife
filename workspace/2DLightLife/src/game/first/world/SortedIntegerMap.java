package game.first.world;

import java.util.LinkedList;
import java.util.List;

public class SortedIntegerMap {

	private Node[] roots;
	private static final int NUMBER_OF_Z = 4;
	
	/**
	 * Max z = 4
	 */
	public SortedIntegerMap() {
		roots = new Node[NUMBER_OF_Z+1];
		
	}
	
	public void add(int id, int z, char type) {
		if (z > NUMBER_OF_Z) {
			return;
		}
		if (roots[z] == null) {
			roots[z] = new Node(id, type);
		} else {
			Node node = new Node(id, type);
			node.setNext(roots[z]);
			roots[z] = node;
		}
	}
	
	public List<Node> getSorted() {
		LinkedList<Node> send = new LinkedList<Node>();
		for (int i = NUMBER_OF_Z; i >= 0; i--) {
			Node node = roots[i];
			while (node != null) {
				send.add(node);
				node = node.getNext();
			}
		}
		
		return send;
	}
	
	public void delete(int id, int z, char type) {
		if (z > NUMBER_OF_Z) {
			return;
		}
		Node node1 = roots[z];
		if (node1 == null) {
			return;
		}
		if (node1.getId() == id) {
			roots[z] = node1.getNext();
		}
		Node node2 = node1.getNext();
		while (node2 != null) {
			if (node2.getId() == id && node2.getType() == type) {
				node1.setNext(node2.getNext());
				return;
			}
			Node switchHolder = node2;
			node2 = node2.getNext();
			node1 = switchHolder;
		}
	}
	
	
	
	
	public class Node {
		
		private int id;
		private char type;
		private Node next;
		
		public Node(int id, char type) {
			this.id = id;
			this.type = type;
		}
		
		public void setNext(Node next) {
			this.next = next;
		}
		
		public Node getNext() {
			return next;
		}
		
		public char getType() {
			return type;
		}
		
		public int getId() {
			return id;
		}
		
		
	}
}
