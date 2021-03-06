package game.first.world;

import game.first.math.Vect3;



public class World {
	
	private Shape[] staticObjects, dynamicObjects;
	private FreeList staticFree, dynamicFree;
	
	
	
	
	
	public World(int statNbrObj, int dynNbrObj) {
		staticObjects = new Shape[statNbrObj];
		staticFree = new FreeList(statNbrObj, this, 0);
		dynamicObjects = new Shape[dynNbrObj];
		dynamicFree = new FreeList(dynNbrObj, this, 1);
		
		
	}
	
	public void createStatic(Shape shape) {
		staticObjects[shape.id] = shape;
	}
	
	public Shape getStatic(int id) {
		return staticObjects[id];
	}
	
	public FreeList getFreeStatic() {
		return staticFree;
	}
	
	public void destroyStatic(int id) {
		if (id > staticObjects.length || staticObjects[id] == null) {
			return;
		}
		staticObjects[id] = null;
		staticFree.storeId(id);
	}
	/**
	 * DO NOT USE!!
	 * FreeList uses automatically
	 */
	public void increaseStatic() {
		Shape[] newList = new Shape[staticObjects.length * 2];
		for (int i = 0; i < staticObjects.length; i++) {
			newList[i] = staticObjects[i];
		}
		staticObjects = newList;
	}
	
	
	
	
	public void createDynamic(Shape shape) {
		dynamicObjects[shape.id] = shape;
	}
	
	public Shape getDynamic(int id) {
		return dynamicObjects[id];
	}
	
	public FreeList getFreeDynamic() {
		return dynamicFree;
	}
	
	public void destroyDynamic(int id) {
		if (id > dynamicObjects.length || dynamicObjects[id] == null) {
			return;
		}
		dynamicObjects[id] = null;
		dynamicFree.storeId(id);
	}
	/**
	 * DO NOT USE!!!
	 * FreeList uses automatically
	 */
	public void increaseDynamic() {
		Shape[] newList = new Shape[dynamicObjects.length * 2];
		for (int i = 0; i < dynamicObjects.length; i++) {
			newList[i] = dynamicObjects[i];
		}
		dynamicObjects = newList;
	}
	// FIXA!!!!!!!!!!!!!!!
	public Shape[] getObjectsNear(Vect3 pos, int range) {
		
		return null;
	}
	
	

}
