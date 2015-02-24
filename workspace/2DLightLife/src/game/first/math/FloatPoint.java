package game.first.math;

import android.util.Log;

public class FloatPoint {

	// for projection x = min and y = max
	private float x, y;

	public FloatPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void normalize() {
		float abs = (float) Math.sqrt((double) x * x + (double) y * y);
		x = x / abs;
		y = y / abs;
		
	}

	public boolean overlap(FloatPoint other) {
		float oX = other.getX();
		float oY = other.getY();
		// min1<min2&&max1>min2||min1>min2&&
		// min1>min2&&min1<max2||max1>min2&&max1<max2
		return (x > oX && x < oY) || (y > oX && y < oY);
	}

	public FloatPoint sub(FloatPoint other) {
		return new FloatPoint(x - other.getX(), y - other.getY());
	}

	public FloatPoint add(FloatPoint other) {
		return new FloatPoint(x + other.getX(), y + other.getY());
	}

	public float dot(FloatPoint other) {
		if (other == null) {
			return 0;
		}
		float send = x * other.getX() + y * other.getY();
		return send;
	}

}
