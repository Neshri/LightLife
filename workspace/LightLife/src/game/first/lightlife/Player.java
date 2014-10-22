package game.first.lightlife;

import game.first.math.Vect3;
import game.first.world.*;


public class Player {
	
	private Shape[] nearObjects;
	private int awareRadius;
	private Vect3 pos, rot;
	private World world;
	
	public Player(Vect3 pos, Vect3 rot, World world, int radius) {
		this.pos = pos;
		this.rot = rot;
		this.rot.norm();
		this.world = world;
		awareRadius = radius;
	}
	
	public void moveForw(float dist) {
		Vect3 temp = rot;
		temp.mul(dist);
		pos.add(temp);
	}
	
	public void turnLeft(float degrees) {
		rot.rotateZ(degrees);
	}
	
	public Vect3 getPos() {
		return pos;
	}
	
	public Vect3 getRot() {
		return rot;
	}
	
	public void setPos(Vect3 pos) {
		this.pos = pos;
	}
	
	public void setRot(Vect3 rot) {
		this.rot = rot;
		this.rot.norm();
	}
	
	public void updateObjectList() {
		nearObjects = world.getObjectsNear(pos, awareRadius);
	}
	
	
}
