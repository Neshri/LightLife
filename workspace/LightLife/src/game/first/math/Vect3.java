package game.first.math;

public class Vect3 {

	private float[] vect;
	private static float[][] zRot;

	/**
	 * Creates a vector of 3 dimensions and stores it as an array of floats.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vect3(float x, float y, float z) {
		vect = new float[3];
		vect[0] = x;
		vect[1] = y;
		vect[2] = z;
		if (zRot == null) {
			zRot = new float[3][3];
			zRot[2][2] = 1f;
		}
	}

	/**
	 * Adds the specified vector to this vector.
	 * 
	 * @param vect
	 * @return
	 */
	public void add(Vect3 vect) {
		float[] temp = vect.getArray();
		for (int i = 0; i < 3; i++) {
			this.vect[i] += temp[i];
		}
	}

	public void mul(float mult) {
		vect[0] = vect[0] * mult;
		vect[1] = vect[1] * mult;
		vect[2] = vect[2] * mult;
	}

	/**
	 * Returns this vector in the form of an array of floats.
	 * 
	 * @return
	 */
	public float[] getArray() {
		return vect;
	}

	/**
	 * Norm this vector
	 */
	public void norm() {
		float abs = (float) Math.sqrt(vect[0] * vect[0] + vect[1] * vect[1]
				+ vect[2] * vect[2]);
		vect[0] = vect[0] / abs;
		vect[1] = vect[1] / abs;
		vect[2] = vect[2] / abs;

	}

	/**
	 * Rotate this vector around the Z-axis
	 * 
	 * @param degree
	 */
	public void rotateZ(double degree) {
		float sin = (float) Math.sin(degree);
		float cos = (float) Math.cos(degree);
		float[][] matrix = zRot;
		matrix[0][0] = cos;
		matrix[1][0] = -sin;
		matrix[0][1] = sin;
		matrix[1][1] = cos;
		vect[0] = matrix[0][0] * vect[0] + matrix[1][0] * vect[1]
				+ matrix[2][0] * vect[2];
		vect[1] = matrix[0][1] * vect[0] + matrix[1][1] * vect[1]
				+ matrix[2][1] * vect[2];
		vect[2] = matrix[0][2] * vect[0] + matrix[1][2] * vect[1]
				+ matrix[2][2] * vect[2];

	}

	@Override
	public String toString() {
		String send = vect[0] + ", " + vect[1] + ", " + vect[2];
		return send;
	}
}
