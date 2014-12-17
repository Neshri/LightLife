package game.first.world;



import game.first.math.Vect3;

public class Cube extends Shape {

	private FastIntegerHashMap hashPoints;
	

	// If this cube is child to another shape id should be 0
	/**
	 * Creates a cube with the specified id, position and dimensions.
	 * @param vect 
	 * @param id
	 * @param dimX
	 * @param dimY
	 * @param dimZ
	 */
	public Cube(Vect3 vect, int id, int dimX, int dimY, int dimZ) {
		super(vect, id);
		Vect3 point;
		hashPoints = new FastIntegerHashMap((dimX * dimY + dimX * dimZ + dimY * dimZ) * 2);
		for (int x = 0; x < dimX; x++) {
			for (int y = 0; y < dimY; y++) {
				point = vect;
				point.addX(x);
				point.addY(y);
				hashPoints.add(point.hashCode());
			}
		}
		for (int x = 0; x < dimX; x++) {
			for (int y = 0; y < dimY; y++) {
				point = vect;
				point.add(new Vect3(x, y, dimZ));
				hashPoints.add(point.hashCode());
			}
		}
		for (int x = 0; x < dimX; x++) {
			for (int z = 0; z < dimZ; z++) {
				point = vect;
				point.addX(x);
				point.addZ(z);
				hashPoints.add(point.hashCode());
			}
		}
		for (int x = 0; x < dimX; x++) {
			for (int z = 0; z < dimZ; z++) {
				point = vect;
				point.add(new Vect3(x, dimY, z));
				hashPoints.add(point.hashCode());
			}
		}
		for (int y = 0; y < dimY; y++) {
			for (int z = 0; z < dimZ; z++) {
				point = vect;
				point.addY(y);
				point.addZ(z);
				hashPoints.add(point.hashCode());
			}
		}
		for (int y = 0; y < dimY; y++) {
			for (int z = 0; z < dimZ; z++) {
				point = vect;
				point.add(new Vect3(dimX, y, z));
				hashPoints.add(point.hashCode());
			}
		}

	}

	

	@Override
	public boolean isPartOf(Vect3 checkVect) {
		return hashPoints.contains(checkVect.hashCode());
	}

}
