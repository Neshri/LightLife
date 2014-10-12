package game.first.world;

import game.first.math.*;

//All shapes are described in the form of a
//3D matrix of int values where 0 is solid matter and 1 is nothing
public abstract class Shape {

	public Vect3 pos;
	public int id;

	public Shape(Vect3 pos, int id) {
		this.pos = pos;
		this.id = id;
	}

	public void moveTo(Vect3 newVect) {
		pos = newVect;
	}
	
	public abstract void go();

}
