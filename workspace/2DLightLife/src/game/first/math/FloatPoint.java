package game.first.math;

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

	public FloatPoint translate(float x, float y) {
		return new FloatPoint(this.x + x, this.y + y);
	}

	public float distance(FloatPoint other) {
		if (other == null) {
			return -1;
		}
		float oX = other.getX();
		float oY = other.getY();
		float ans = (float) Math
				.sqrt((oX - x) * (oX - x) + (oY - y) * (oY - y));
		return ans;
	}

	public void normalize() {
		float abs = (float) Math.sqrt((double) x * x + (double) y * y);
		x = x / abs;
		y = y / abs;

	}

	public float getLength() {
		float send = (float) Math.sqrt(x * x + y * y);
		return send;
	}

	/**
	 * 
	 * @param other
	 * @return the shortest length between the two projections
	 */
	public float getOverlap(FloatPoint other) {
		float first = other.getY() - x;
		float second = y - other.getX();
		if (first < 0) {
			first = first * -1;
		}
		if (second < 0) {
			second = second * -1;
		}
		if (first < second) {
			return first;
		} else {
			return second;
		}
	}

	public boolean overlap(FloatPoint other) {
		float oX = other.getX();
		float oY = other.getY();
		return (x > oX && x < oY) || (y > oX && y < oY) || (oX > x && oX < y)
				|| (oY > x && oY < y);
	}

	public FloatPoint mult(float multiplier) {
		return new FloatPoint(x * multiplier, y * multiplier);
	}

	public FloatPoint div(float divisor) {
		return new FloatPoint(x / divisor, y / divisor);
	}

	public FloatPoint sub(FloatPoint other) {
		return new FloatPoint(x - other.getX(), y - other.getY());
	}

	public FloatPoint add(FloatPoint other) {
		return new FloatPoint(x + other.getX(), y + other.getY());
	}

	public FloatPoint rotate(double degrees) {
		double radian = Math.toRadians(degrees);
		float cos = (float) Math.cos(radian);
		float sin = (float) Math.sin(radian);
		return new FloatPoint(x * cos - y * sin, x * sin + y * cos);
	}

	public float dot(FloatPoint other) {
		if (other == null) {
			return 0;
		}
		float send = x * other.getX() + y * other.getY();
		return send;
	}

	@Override
	public String toString() {
		String send = x + ", " + y;
		return send;
	}

}
