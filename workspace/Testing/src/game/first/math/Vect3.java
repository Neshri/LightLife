package game.first.math;

public class Vect3 {

	private float[] vect;

	private static final double[] yRotM = { 0, 0, 0, 0, 1, 0, 0, 0, 0 };

	public Vect3(float x, float y, float z) {
		vect = new float[3];
		vect[0] = x;
		vect[1] = y;
		vect[2] = z;
	}

	public float[] getArray() {
		return vect;
	}

	public void add(Vect3 other) {
		float[] oth = other.getArray();
		for (int i = 0; i < 3; i++) {
			vect[i] += oth[i];
		}
	}

	public void mul(float mult) {
		for (int i = 0; i < 3; i++) {
			vect[i] = vect[i] * mult;
		}
	}

	public void addX(float x) {
		vect[0] += x;
	}

	public void addY(float y) {
		vect[1] += y;
	}

	public void addZ(float z) {
		vect[2] += z;
	}

	public float getX() {
		return vect[0];
	}

	public float getY() {
		return vect[1];
	}

	public float getZ() {
		return vect[2];
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

	public void rotateY(float degrees) {
		double radian = Math.toRadians(degrees);
		double sin = Math.sin(radian);
		double cos = Math.cos(radian);
		double[] temp = new double[9];
		for (int i = 0; i < 9; i++) {
			temp[i] = yRotM[i];
		}
		temp[0] = cos;
		temp[2] = sin;
		temp[6] = -sin;
		temp[8] = cos;

		float[] oldVect = new float[3];
		for (int i = 0; i < 3; i++) {
			oldVect[i] = vect[i];
		}
		vect[0] = (float) (temp[0] * oldVect[0] + temp[2] * oldVect[2]);
		vect[2] = (float) (temp[6] * oldVect[0] + temp[8] * oldVect[2]);

	}

	@Override
	public String toString() {
		String send = "" + vect[0] + "," + vect[1] + "," + vect[2];
		return send;
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass() != this.getClass()) {
			return false;
		}

		return hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}