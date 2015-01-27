package game.first.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import game.first.math.Vect3;
import game.first.props.Shape;

public class World extends Observable{

	private final int increaseMult = 2;
	private Shape[] staticObjects, dynamicObjects;
	private FreeList staticFree, dynamicFree;
	private int nbrObjects;

	public World(int statNbrObj, int dynNbrObj) {
		staticObjects = new Shape[statNbrObj];
		staticFree = new FreeList(statNbrObj, this, 0);
		dynamicObjects = new Shape[dynNbrObj];
		dynamicFree = new FreeList(dynNbrObj, this, 1);
		nbrObjects = 0;

	}
	
	private void notifyObs() {
		super.setChanged();
		super.notifyObservers();
	}

	public void createStatic(Shape shape) {
		shape.id = staticFree.getFreeId();
		staticObjects[shape.id] = shape;
		nbrObjects++;
		notifyObs();
	}

	public Shape getStatic(int id) {
		return staticObjects[id];
	}

	public void destroyStatic(int id) {
		if (id > staticObjects.length || staticObjects[id] == null || id < 1) {
			return;
		}
		staticObjects[id] = null;
		staticFree.storeId(id);
		nbrObjects--;
		notifyObs();
	}

	private void increaseStatic() {
		Shape[] newList = new Shape[staticObjects.length * increaseMult];
		for (int i = 0; i < staticObjects.length; i++) {
			newList[i] = staticObjects[i];
		}
		staticObjects = newList;
	}

	public void createDynamic(Shape shape) {
		shape.id = dynamicFree.getFreeId();
		dynamicObjects[shape.id] = shape;
		nbrObjects++;
		notifyObs();
	}

	public Shape getDynamic(int id) {
		return dynamicObjects[id];
	}

	public void destroyDynamic(int id) {
		if (id > dynamicObjects.length || dynamicObjects[id] == null || id < 1) {
			return;
		}
		dynamicObjects[id] = null;
		dynamicFree.storeId(id);
		nbrObjects--;
		notifyObs();
	}

	private void increaseDynamic() {
		Shape[] newList = new Shape[dynamicObjects.length * increaseMult];
		for (int i = 0; i < dynamicObjects.length; i++) {
			newList[i] = dynamicObjects[i];
		}
		dynamicObjects = newList;
	}

	public List<Shape> getShapes() {
		ArrayList<Shape> shapes = new ArrayList<Shape>();
		for (Shape i : staticObjects) {
			if (i != null) {
				shapes.add(i);
			}
		}
		for (Shape i : dynamicObjects) {
			if (i != null) {
				shapes.add(i);
			}

		}

		return shapes;
	}

	private class FreeList {

		public static final int DYNAMIC_TYPE = 1;
		public static final int STATIC_TYPE = 0;

		private int nextCell, size, type;
		private int[] freeList;
		private World world;

		/**
		 * Creates a list of ID values that are free to use, type = 0||1 where
		 * 0=static and 1=dynamic
		 * 
		 * @param size
		 */
		public FreeList(int size, World world, int type) {
			this.type = type;
			this.world = world;
			freeList = new int[size];
			this.size = size;
			nextCell = 0;
			for (int i = 0; i < size; i++) {
				freeList[i] = i + 1;
			}
		}

		/**
		 * Returns a non-used ID
		 * 
		 * @return FreeID
		 */
		public int getFreeId() {
			if (nextCell == size) {
				int[] newFree = new int[size * increaseMult];
				for (int i = size; i < size * increaseMult; i++) {
					newFree[i] = i;
				}
				if (type == STATIC_TYPE) {
					world.increaseStatic();
				} else {
					world.increaseDynamic();
				}
				size = size * increaseMult;
			}
			int send = freeList[nextCell];
			freeList[nextCell] = 0;
			nextCell++;
			return send;
		}

		/**
		 * Stores an ID that's no longer in use for future usage
		 * 
		 * @param id
		 */
		public void storeId(int id) {
			if (nextCell == 0) {
				return;
			}
			nextCell--;
			freeList[nextCell] = id;
		}

		/**
		 * 
		 * @return true if there are no non-used IDs left else false
		 */
		public boolean isEmpty() {
			if (nextCell == size) {
				return true;
			}
			return false;
		}
	}

}
