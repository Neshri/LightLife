package game.first.world;

import game.first.props.Shape;
import java.util.ArrayList;
import java.util.List;

public class PointNearList {

	private Node root;

	public PointNearList() {
		// Defines size of the map
		float[] firstBound = new float[4];
		firstBound[0] = -500f;
		firstBound[1] = -500f;
		firstBound[2] = 500f;
		firstBound[3] = 500f;

		root = new Node(0, firstBound);
	}

	public void insert(Shape shape) {
		root.insert(shape);
	}

	public void clear() {
		root.clear();
	}

	public void delete(Shape shape) {
		root.delete(shape);
	}

	public List<Shape> retrieve(List<Shape> returnObjects, float[] bounds) {
		return root.retrieve(returnObjects, bounds);
	}

	private class Node {

		private static final int MAX_OBJECTS = 10;
		private static final int MAX_LEVELS = 40;

		private Node[] nodes;
		private int level;
		private List<Shape> objects;
		private float[] bounds;
		private double verticalMidPoint, horizontalMidPoint;

		public Node(int level, float[] bounds) {
			this.level = level;
			nodes = new Node[4];
			objects = new ArrayList<Shape>();
			this.bounds = bounds;
			verticalMidPoint = this.bounds[1]
					+ (this.bounds[3] - this.bounds[1]) / 2;
			horizontalMidPoint = this.bounds[0]
					+ (this.bounds[2] - this.bounds[0]) / 2;
		}

		public void clear() {
			objects.clear();

			for (int i = 0; i < nodes.length; i++) {
				if (nodes[i] != null) {
					nodes[i].clear();
					nodes[i] = null;
				}
			}
		}

		public void delete(Shape shape) {
			if (nodes[0] != null) {
				int index = getIndex(shape.getRoughBounds());

				if (index != -1) {
					nodes[index].delete(shape);
					return;
				}
			}

			objects.remove(shape);
		}

		private void split() {
			int subWidth = (int) (bounds[2] - bounds[0]) / 2;
			int subHeight = (int) (bounds[3] - bounds[1]) / 2;
			int x = (int) bounds[0];
			int y = (int) bounds[1];

			float[] bounds1 = new float[4];
			bounds1[0] = x + subWidth;
			bounds1[1] = y + subHeight;
			bounds1[2] = x + subWidth * 2;
			bounds1[3] = y + subHeight * 2;
			float[] bounds2 = new float[4];
			bounds2[0] = x;
			bounds2[1] = y + subHeight;
			bounds2[2] = x + subWidth;
			bounds2[3] = y + subHeight * 2;
			float[] bounds3 = new float[4];
			bounds3[0] = x;
			bounds3[1] = y;
			bounds3[2] = x + subWidth;
			bounds3[3] = y + subHeight;
			float[] bounds4 = new float[4];
			bounds4[0] = x + subWidth;
			bounds4[1] = y;
			bounds4[2] = x + subWidth * 2;
			bounds4[3] = y + subHeight;

			nodes[0] = new Node(level + 1, bounds1);
			nodes[1] = new Node(level + 1, bounds2);
			nodes[2] = new Node(level + 1, bounds3);
			nodes[3] = new Node(level + 1, bounds4);
		}

		private int getIndex(float[] bounds) {
			int index = -1;

			boolean bottomQuadrant = bounds[3] < horizontalMidPoint;
			boolean topQuadrant = bounds[1] > horizontalMidPoint;

			if (bounds[2] < verticalMidPoint) {
				if (topQuadrant) {
					index = 1;
				} else if (bottomQuadrant) {
					index = 2;
				}
			} else if (bounds[0] > verticalMidPoint) {
				if (topQuadrant) {
					index = 0;
				} else if (bottomQuadrant) {
					index = 3;
				}
			}

			return index;
		}

		public void insert(Shape shape) {
			if (nodes[0] != null) {
				int index = getIndex(shape.getRoughBounds());

				if (index != -1) {
					nodes[index].insert(shape);
					return;
				}
			}

			objects.add(shape);

			if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
				if (nodes[0] == null) {
					split();
				}

				int i = 0;
				while (i < objects.size()) {
					int index = getIndex(objects.get(i).getRoughBounds());
					if (index != -1) {
						nodes[index].insert(objects.remove(i));
					}
					i++;
				}
			}
		}


		/*
		 * Return all objects that could collide with the given object
		 */
		public List<Shape> retrieve(List<Shape> returnObjects, float[] bounds) {
			int index = getIndex(bounds);
			if (nodes[0] != null) {
				if (index != -1) {
					nodes[index].retrieve(returnObjects, bounds);
				}
			}

			returnObjects.addAll(objects);

			return returnObjects;
		}
	}
}
