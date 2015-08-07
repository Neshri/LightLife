package game.first.physics;

import game.first.math.FloatPoint;
import game.first.math.TwoDRotMatrix;

public abstract class CollisionShape {
	public float mass;
	public float[] roughBounds;
	public FloatPoint[] axes;
	public FloatPoint[] vertices;
	public float[] midPoint;
	public int z;
	private FloatPoint lastTestedShortAxis;

	public FloatPoint getLastMTV() {
		return lastTestedShortAxis;
	}

	public boolean overlaps(CollisionShape shape) {
		if (shape == null || axes == null || shape.axes == null
				|| shape == this) {
			return false;
		}
		if (shape.z != z) {
			return false;
		}
		float overlap = Float.MAX_VALUE;
		FloatPoint[] axes1 = axes;
		FloatPoint[] axes2 = shape.axes;

		for (int i = 0; i < axes1.length; i++) {
			FloatPoint axis = axes1[i];
			// project both shapes onto the axis

			FloatPoint p1 = project(axis, vertices);
			FloatPoint p2 = shape.project(axis, shape.vertices);

			// do the projections overlap?
			if (!p1.overlap(p2)) {
				// then we can guarantee that the shapes do not overlap
				return false;
			} else {
				float temp = p1.getOverlap(p2);
				if (temp < overlap) {
					lastTestedShortAxis = axis;
					overlap = temp;
				}
			}
		}

		// loop over the axes2
		for (int i = 0; i < axes2.length; i++) {
			FloatPoint axis = axes2[i];
			// project both shapes onto the axis
			FloatPoint p1 = project(axis, vertices);
			FloatPoint p2 = shape.project(axis, shape.vertices);
			// do the projections overlap?
			if (!p1.overlap(p2)) {
				// then we can guarantee that the shapes do not overlap
				return false;
			} else {
				float temp = p1.getOverlap(p2);
				if (temp < overlap) {
					lastTestedShortAxis = axis;
					overlap = temp;
				}
			}
		}
		return true;
	}

	public FloatPoint project(FloatPoint axis, FloatPoint[] vertices) {
		float min = axis.dot(vertices[0]);
		float max = min;
		for (int i = 1; i < vertices.length; i++) {
			// NOTE: the axis must be normalized to get accurate projections
			float p = axis.dot(vertices[i]);
			if (p < min) {
				min = p;
			} else if (p > max) {
				max = p;
			}
		}
		return new FloatPoint(min, max);
	}

	public void move(float x, float y) {
		for (int i = 0; i < 2; i++) {
			roughBounds[i * 2] += x;
			roughBounds[i * 2 + 1] += y;
			vertices[i] = vertices[i].add(new FloatPoint(x, y));
		}
		for (int i = 2; i < vertices.length; i++) {
			vertices[i] = vertices[i].add(new FloatPoint(x, y));
		}
		midPoint[0] += x;
		midPoint[1] += y;
	}

	public void rotate(float degree) {
		FloatPoint temp;
		TwoDRotMatrix rot = new TwoDRotMatrix(degree);
		for (int i = 0; i < vertices.length; i++) {
			temp = vertices[i];
			temp = temp.translate(-midPoint[0], -midPoint[1]);
			temp = rot.rotate(temp);
			temp = temp.translate(midPoint[0], midPoint[1]);
			vertices[i] = temp;
			if (vertices[i].getX() < roughBounds[0]) {
				roughBounds[0] = vertices[i].getX();
			} else if (vertices[i].getX() > roughBounds[2]) {
				roughBounds[2] = vertices[i].getX();
			}
			if (vertices[i].getY() < roughBounds[1]) {
				roughBounds[1] = vertices[i].getY();
			} else if (vertices[i].getY() > roughBounds[3]) {
				roughBounds[3] = vertices[i].getY();
			}
		}
		for (int i = 0; i < axes.length; i++) {
			axes[i] = rot.rotate(axes[i]);
		}

	}
}
