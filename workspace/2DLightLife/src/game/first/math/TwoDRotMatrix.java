package game.first.math;

public class TwoDRotMatrix {

	private float[] rotM = new float[4];

	public TwoDRotMatrix(float degree) {
		double radian = Math.toRadians(degree);
		float cos = (float) Math.cos(radian);
		float sin = (float) Math.sin(radian);
		rotM[0] = cos;
		rotM[1] = -sin;
		rotM[2] = sin;
		rotM[3] = cos;
	}

	public FloatPoint rotate(FloatPoint point) {
		float x = point.getX() * rotM[0] + point.getY() * rotM[1];
		float y = point.getX() * rotM[2] + point.getY() * rotM[3];
		return new FloatPoint(x, y);
	}

}
