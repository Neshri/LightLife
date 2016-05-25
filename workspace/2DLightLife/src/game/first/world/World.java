package game.first.world;

import game.first.lighting.PointLight;
import game.first.pawn.Pawn;
import game.first.physics.CollisionShape;
import game.first.props.Shape;
import game.first.world.SortedIntegerMap.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import android.util.Log;

public class World extends Observable {
	
	public final static int POINT_LIGHT = 1;
	public final static int DYNAMIC_SHAPES = 2;
	public final static int STATIC_SHAPES = 3;
	public final static int MAX_POINT_LIGHTS = 10;

	public static float LIGHT_STRENGTH_MULTIPLIER = 1;

	private static final int INCREASE_MULTIPLIER = 2;
	private Shape[] staticObjects, dynamicObjects;
	private PointNearList staticNearList;
	private FreeList staticFree, dynamicFree;
	@SuppressWarnings("unused")
	private int nbrObjects, nbrStatic, nbrDynamic;
	private SortedIntegerMap orderOfDraw;
	private ArrayList<PointLight> pointLights;
	private LinkedList<Pawn> pawns;

	public World(int statNbrObj, int dynNbrObj) {
		pawns = new LinkedList<Pawn>();
		staticObjects = new Shape[statNbrObj];
		staticFree = new FreeList(statNbrObj, this, 0);
		dynamicObjects = new Shape[dynNbrObj];
		dynamicFree = new FreeList(dynNbrObj, this, 1);
		nbrObjects = 0;
		nbrDynamic = 0;
		nbrStatic = 0;
		staticNearList = new PointNearList();
		orderOfDraw = new SortedIntegerMap();
		pointLights = new ArrayList<PointLight>(10);

	}
	
	public void addPawn(Pawn pawn) {
		pawns.add(pawn);
	}
	
	public void removePawn(Pawn pawn) {
		pawns.remove(pawn);
	}

	public void addPointLight(PointLight light) {
		if (pointLights.size() >= 10) {
			return;
		}
		pointLights.add(light);
		notifyObs(POINT_LIGHT);
	}

	public void removePointLight(PointLight light) {
		pointLights.remove(light);
		notifyObs(POINT_LIGHT);
	}
	

	public List<PointLight> getPointLights() {
		ArrayList<PointLight> send = new ArrayList<PointLight>(pointLights.size());
		//Iterator<PointLight> iter = pointLights.iterator();
		for (int i = 0; i < pointLights.size(); i++) {
			if (pointLights.get(i) != null) {
				send.add(pointLights.get(i));
			}
		}
//		while (iter.hasNext()) {
//			send.add(iter.next());
//		}
		return send;
	}

	private void notifyObs(int type) {
		super.setChanged();
		super.notifyObservers(type);
	}

	public List<Shape> getNearShapes(float[] bounds) {
		LinkedList<Shape> send = new LinkedList<Shape>();
		send = (LinkedList<Shape>) staticNearList.retrieve(send, bounds);
		int count = nbrDynamic;
		for (int i = 1; i <= count; i++) {
			if (i == dynamicObjects.length) {
				break;
			}
			if (dynamicObjects[i] != null) {
				send.add(dynamicObjects[i]);
			} else {
				count++;
			}
		}
		return send;
	}
	

	public void step() {
		LinkedList<Pawn> temp = new LinkedList<Pawn>();
		for (Pawn i : pawns) {
			temp.add(i);
		}
		for (Pawn i : temp) {
			i.step(this);
		}
	}

	public void createStatic(Shape shape) {
		if (shape == null) {
			return;
		}
		shape.id = staticFree.getFreeId();
		shape.type = 'S';
		staticObjects[shape.id] = shape;
		if (shape.getRoughBounds() != null) {
			staticNearList.insert(shape);
		}
		orderOfDraw.add(shape.id, Math.round(shape.position[2]), 'S');
		nbrObjects++;
		nbrStatic++;
		notifyObs(STATIC_SHAPES);
	}

	public Shape getStatic(int id) {
		return staticObjects[id];
	}

	public void destroyStatic(int id) {
		if (id >= staticObjects.length || id < 1) {
			return;
		}
		if (staticObjects[id] == null) {
			return;
		}
		if (staticObjects[id].getRoughBounds() != null) {
			staticNearList.delete(staticObjects[id]);
		}
		orderOfDraw.delete(id, Math.round(staticObjects[id].position[2]), 'S');
		staticObjects[id] = null;
		staticFree.storeId(id);
		nbrObjects--;
		nbrStatic--;
		notifyObs(STATIC_SHAPES);
	}

	private void increaseStatic() {
		Shape[] newList = new Shape[staticObjects.length * INCREASE_MULTIPLIER];
		for (int i = 0; i < staticObjects.length; i++) {
			newList[i] = staticObjects[i];
		}
		staticObjects = newList;
	}

	public void createDynamic(Shape shape) {
		if (shape == null) {
			return;
		}
		shape.id = dynamicFree.getFreeId();
		shape.type = 'D';
		dynamicObjects[shape.id] = shape;
		orderOfDraw.add(shape.id, Math.round(shape.position[2]), 'D');
		nbrObjects++;
		nbrDynamic++;
		notifyObs(DYNAMIC_SHAPES);
	}

	public Shape getDynamic(int id) {
		return dynamicObjects[id];
	}

	public void destroyDynamic(int id) {
		if (id >= dynamicObjects.length || dynamicObjects[id] == null || id < 1) {
			return;
		}
		orderOfDraw.delete(id, Math.round(dynamicObjects[id].position[2]), 'D');
		dynamicObjects[id] = null;
		dynamicFree.storeId(id);
		nbrObjects--;
		nbrDynamic--;
		notifyObs(DYNAMIC_SHAPES);
	}

	private void increaseDynamic() {
		Shape[] newList = new Shape[dynamicObjects.length * INCREASE_MULTIPLIER];
		for (int i = 0; i < dynamicObjects.length; i++) {
			newList[i] = dynamicObjects[i];
		}
		dynamicObjects = newList;
	}
	
	public List<Shape> getStaticShapes() {
		LinkedList<Shape> shapes = new LinkedList<Shape>();
		List<Node> order = orderOfDraw.getSorted();
		for (Node i : order) {
			if (i.getType() == 'S') {
				shapes.add(staticObjects[i.getId()]);
			}
		}
		return shapes;
	}

	public List<Shape> getDynamicShapes() {
		LinkedList<Shape> shapes = new LinkedList<Shape>();
		List<Node> order = orderOfDraw.getSorted();
		for (Node i : order) {
			if (i.getType() == 'D') {
				if (dynamicObjects[i.getId()] != null) {
					shapes.add(dynamicObjects[i.getId()]);
				}
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
			if (nextCell == size - 1) {
				int[] newFree = new int[size * INCREASE_MULTIPLIER];
				for (int i = size; i < size * INCREASE_MULTIPLIER; i++) {
					newFree[i] = i;
				}
				freeList = newFree;
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
