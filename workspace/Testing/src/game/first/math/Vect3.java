package game.first.math;

public class Vect3 {

	private int[] vect;

	/**
	 * Creates a vector of 3 dimensions and stores it as an array of ints.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vect3(int x, int y, int z) {
		vect = new int[3];
		vect[0] = x;
		vect[1] = y;
		vect[2] = z;
	}

	/**
	 * Adds the specified vector to this vector.
	 * 
	 * @param vect
	 * @return
	 */
	public boolean add(Vect3 vect) {
		int[] temp = vect.getArray();
		for (int i = 0; i < 3; i++) {
			this.vect[i] += temp[i];
		}
		return true;
	}

	/**
	 * Returns this vector in the form of an array of ints.
	 * 
	 * @return
	 */
	public int[] getArray() {
		return vect;
	}

	
	@Override
	public String toString() {
		String send = vect[0] + ", " + vect[1] + ", " + vect[2];
		return send;
	}
}
