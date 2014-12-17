package game.first.world;

import game.first.math.*;

//All shapes are described in the form of one or more cubes,
//each cube is described by 8 Vect3 positions
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

	public abstract boolean isPartOf(Vect3 checkVect);


}
