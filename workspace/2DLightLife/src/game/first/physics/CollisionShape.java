package game.first.physics;

import game.first.math.FloatPoint;

public abstract class CollisionShape {
	public float[] roughBounds;
	public FloatPoint[] axes;
	public FloatPoint[] vertices;
	public int z;

	public boolean overlaps(CollisionShape shape) {
		if (axes == null || shape.axes == null) {
			return false;
		}
//		if (shape.z != z) {
//			return false;
//		}
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
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = vertices[i].add(new FloatPoint(x, y));
		}
	}

}
