package game.first.lightlife;

import game.first.math.Vect3;
import game.first.world.*;

public class Player {

	private Shape[] nearObjects;
	private int awareRadius;
	private Vect3 pos, rot;
	private World world;
	private static final int height = 180;

	public Player(Vect3 pos, Vect3 rot, World world, int radius) {
		this.pos = pos;
		this.rot = rot;
		this.rot.norm();
		this.world = world;
		awareRadius = radius;
		updateObjectList();
	}

	public int distToColli(float yRot, float xRot) {
		int distance = 0;
		Vect3 direction = rot;
		direction.rotateY(yRot);
		direction.rotateX(xRot);
		Vect3 checkPos = pos;
		checkPos.addX(height);
		while (distance < awareRadius) {
			for (Shape check : nearObjects) {
				if (check != null) {
					if (check.isPartOf(direction)) {
						return distance;
					}
				}
				
			}
			checkPos.add(direction);
			distance++;
		}

		return distance;
	}

	public void moveForw(float dist) {
		Vect3 temp = rot;
		temp.mul(dist);
		pos.add(temp);
	}

	public void turnLeft(float degrees) {
		rot.rotateY(Math.toRadians(degrees));
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
