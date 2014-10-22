package game.first.world;

import game.first.math.Vect3;

public class Cube extends Shape{
	
	int[][][] cube;
	
	public Cube(Vect3 vect, int id, int dimX, int dimY, int dimZ) {
		super(vect, id);
		cube = new int[dimX][dimY][dimZ];
	}

	@Override
	public void go() {
		return;
		
	}

}
