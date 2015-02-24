package game.first.physics;

import game.first.math.FloatPoint;

public class CollisionTriangle extends CollisionShape {

	public CollisionTriangle(float[] points, int z) {
		float[] roughBounds = new float[4];
		roughBounds[0] = points[0];
		roughBounds[1] = points[1];
		roughBounds[2] = points[0];
		roughBounds[3] = points[1];
		for (int i = 1; i < points.length / 2; i++) {
			if (points[i * 2] < roughBounds[0]) {
				roughBounds[0] = points[i * 2];
			} else if (points[i * 2] > roughBounds[2]) {
				roughBounds[2] = points[i * 2];
			}

			if (points[i * 2 + 1] < roughBounds[1]) {
				roughBounds[1] = points[i * 2 + 1];
			} else if (points[i * 2 + 1] > roughBounds[3]) {
				roughBounds[3] = points[i * 2 + 1];
			}
		}
		super.roughBounds = roughBounds;
		FloatPoint[] vertices = new FloatPoint[3];
		vertices[0] = new FloatPoint(points[0], points[1]);
		vertices[1] = new FloatPoint(points[2], points[3]);
		vertices[2] = new FloatPoint(points[4], points[5]);
		super.vertices = vertices;
		super.axes = getAxes();
		super.z = z;

	}

	
	private FloatPoint[] getAxes() {
		float[] axes = new float[6];
		FloatPoint edge = vertices[0].sub(vertices[1]);
		axes[0] = -edge.getY();
		axes[1] = edge.getX();
		edge = vertices[1].sub(vertices[2]);
		axes[2] = -edge.getY();
		axes[3] = edge.getX();
		edge = vertices[2].sub(vertices[0]);
		axes[4] = -edge.getY();
		axes[5] = edge.getX();
		FloatPoint[] send = new FloatPoint[3];
		send[0] = new FloatPoint(axes[0], axes[1]);
		send[1] = new FloatPoint(axes[2], axes[3]);
		send[2] = new FloatPoint(axes[4], axes[5]);
		send[0].normalize();
		send[1].normalize();
		send[2].normalize();
		return send;
	}

}
