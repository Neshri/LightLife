package game.first.physics;

import game.first.math.FloatPoint;

public class CollisionBox extends CollisionShape {

	public CollisionBox(float[] points, int z) {
		float[] roughBounds = new float[4];
		roughBounds[0] = points[0];
		roughBounds[1] = points[1];
		roughBounds[2] = points[4];
		roughBounds[3] = points[5];
		super.roughBounds = roughBounds;
		super.z = z;
		FloatPoint[] vertices = new FloatPoint[points.length / 2];
		for (int i = 0; i < points.length / 2; i++) {
			vertices[i] = new FloatPoint(points[i*2], points[i*2 + 1]);
		}
		super.vertices = vertices;
		super.axes = getAxes();
	}

	private FloatPoint[] getAxes() {
		float[] axes = new float[4];
		FloatPoint edge = vertices[0].sub(vertices[1]);
		axes[0] = -edge.getY();
		axes[1] = edge.getX();
		edge = vertices[1].sub(vertices[2]);
		axes[2] = -edge.getY();
		axes[3] = edge.getX();
		FloatPoint[] send = new FloatPoint[2];
		send[0] = new FloatPoint(axes[0], axes[1]);
		send[1] = new FloatPoint(axes[2], axes[3]);
		send[0].normalize();
		send[1].normalize();
		return send;
	}
}
