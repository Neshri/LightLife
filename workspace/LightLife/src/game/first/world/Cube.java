package game.first.world;

import game.first.math.Vect3;

public class Cube extends Shape {

	Vect3[] points;

	// If this cube is child to another shape id should be 0
	public Cube(Vect3 vect, int id, int dimX, int dimY, int dimZ) {
		super(vect, id);
		points = new Vect3[8];
		points[0] = vect;
		Vect3 temp = vect;
		temp.add(new Vect3(0, 0, dimZ));
		points[1] = temp;
		temp = vect;
		temp.add(new Vect3(0, dimY, 0));
		points[2] = temp;
		temp = vect;
		temp.add(new Vect3(0, dimY, dimZ));
		points[3] = temp;
		temp = vect;
		temp.add(new Vect3(dimX, 0, 0));
		points[4] = temp;
		temp = vect;
		temp.add(new Vect3(dimX, 0, dimZ));
		points[5] = temp;
		temp = vect;
		temp.add(new Vect3(dimX, dimY, 0));
		points[6] = temp;
		temp = vect;
		temp.add(new Vect3(dimX, dimY, dimZ));
		points[7] = temp;

	}

	@Override
	public void go() {
		return;

	}

	@Override
	public boolean isPartOf(Vect3 checkVect) {
		
		// TODO Auto-generated method stub
		return false;
	}

}
