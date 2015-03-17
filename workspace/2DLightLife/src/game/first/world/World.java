package game.first.world;

import game.first.lighting.LightSource;
import game.first.lighting.PointLight;
import game.first.pawn.Pawn;
import game.first.physics.CollisionShape;
import game.first.props.Shape;
import game.first.world.SortedIntegerMap.Node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import android.util.Log;

public class World extends Observable {

	private static final int INCREASE_MULTIPLIER = 2;
	private Shape[] staticObjects, dynamicObjects;
	private PointNearList staticNearList;
	private FreeList staticFree, dynamicFree;
	@SuppressWarnings("unused")
	private int nbrObjects;
	private SortedIntegerMap orderOfDraw;
	private LinkedList<PointLight> pointLights;
	private LinkedList<Pawn> pawns;

	public World(int statNbrObj, int dynNbrObj) {
		pawns = new LinkedList<Pawn>();
		staticObjects = new Shape[statNbrObj];
		staticFree = new FreeList(statNbrObj, this, 0);
		dynamicObjects = new Shape[dynNbrObj];
		dynamicFree = new FreeList(dynNbrObj, this, 1);
		nbrObjects = 0;
		staticNearList = new PointNearList();
		orderOfDraw = new SortedIntegerMap();
		pointLights = new LinkedList<PointLight>();

	}
	
	public void addPawn(Pawn pawn) {
		pawns.add(pawn);
	}
	
	public void removePawn(Pawn pawn) {
		pawns.remove(pawn);
	}

	public void addPointLight(PointLight light) {
		pointLights.add(light);
		notifyObs();
	}

	public void removePointLight(PointLight light) {
		pointLights.remove(light);
		notifyObs();
	}

	public List<PointLight> getPointLights() {
		LinkedList<PointLight> send = new LinkedList<PointLight>();
		Iterator<PointLight> iter = pointLights.iterator();
		while (iter.hasNext()) {
			send.add(iter.next());
		}
		return send;
	}

	private void notifyObs() {
		super.setChanged();
		super.notifyObservers();
	}

	public List<CollisionShape> getNearCollision(float[] bounds) {
		LinkedList<CollisionShape> send = new LinkedList<CollisionShape>();
		Iterator<Shape> iter = staticNearList.retrieve(new LinkedList<Shape>(),
				bounds).iterator();
		while (iter.hasNext()) {
			send.add(iter.next().collisionShape);
		}
		for (int i = 0; i < dynamicObjects.length; i++) {
			if (dynamicObjects[i] != null) {
				send.add(dynamicObjects[i].collisionShape);
			}
		}
		// Log.d("Collision", "" + send.size());
		return send;
	}

	public void step() {
		for (Pawn i : pawns) {
			i.step();
		}
	}

	public void createStatic(Shape shape) {
		shape.id = staticFree.getFreeId();
		staticObjects[shape.id] = shape;
		if (shape.getRoughBounds() != null) {
			staticNearList.insert(shape);
		}
		orderOfDraw.add(shape.id, (int) shape.position[2], 'S');
		nbrObjects++;
		notifyObs();
	}

	public Shape getStatic(int id) {
		return staticObjects[id];
	}

	public void destroyStatic(int id) {
		if (id > staticObjects.length || id < 1) {
			return;
		}
		if (staticObjects[id] == null) {
			return;
		}
		if (staticObjects[id].getRoughBounds() != null) {
			staticNearList.delete(staticObjects[id]);
		}
		orderOfDraw.delete(id, (int) staticObjects[id].position[2], 'S');
		staticObjects[id] = null;
		staticFree.storeId(id);
		nbrObjects--;
		notifyObs();
	}

	private void increaseStatic() {
		Shape[] newList = new Shape[staticObjects.length * INCREASE_MULTIPLIER];
		for (int i = 0; i < staticObjects.length; i++) {
			newList[i] = staticObjects[i];
		}
		staticObjects = newList;
	}

	public void createDynamic(Shape shape) {
		shape.id = dynamicFree.getFreeId();
		dynamicObjects[shape.id] = shape;
		orderOfDraw.add(shape.id, (int) shape.position[2], 'D');
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
		orderOfDraw.delete(id, (int) dynamicObjects[id].position[2], 'D');
		dynamicObjects[id] = null;
		dynamicFree.storeId(id);
		nbrObjects--;
		notifyObs();
	}

	private void increaseDynamic() {
		Shape[] newList = new Shape[dynamicObjects.length * INCREASE_MULTIPLIER];
		for (int i = 0; i < dynamicObjects.length; i++) {
			newList[i] = dynamicObjects[i];
		}
		dynamicObjects = newList;
	}

	public List<Shape> getShapes() {
		LinkedList<Shape> shapes = new LinkedList<Shape>();
		List<Node> order = orderOfDraw.getSorted();
		for (Node i : order) {
			if (i.getType() == 'S') {
				shapes.add(staticObjects[i.getId()]);
			} else {
				shapes.add(dynamicObjects[i.getId()]);
			}
		}
		return shapes;
	}

	private class FreeList {

		@SuppressWarnings("unused")
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
				int[] newFree = new int[size * INCREASE_MULTIPLIER];
				for (int i = size; i < size * INCREASE_MULTIPLIER; i++) {
					newFree[i] = i;
				}
				if (type == STATIC_TYPE) {
					world.increaseStatic();
				} else {
					world.increaseDynamic();
				}
				size = size * INCREASE_MULTIPLIER;
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

	}

}
