package game.first.physics;

import game.first.math.FloatPoint;

public class CollisionSymmetricPolygon extends CollisionShape {

	public CollisionSymmetricPolygon(float[] points, int z) {

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
		FloatPoint[] vertices = new FloatPoint[points.length / 2];
		for (int i = 0; i < points.length / 2; i++) {
			vertices[i] = new FloatPoint(points[i * 2], points[i * 2 + 1]);
		}
		super.vertices = vertices;
		super.axes = getAxes();
		super.z = z;
		float perimeter = vertices[0].distance(vertices[1]);
		perimeter = perimeter * vertices.length;
		float midX = 0;
		float midY = 0;
		for (int i = 0; i < vertices.length; i++) {
			midX += vertices[i].getX();
			midY += vertices[i].getY();
		}
		midX = midX / vertices.length;
		midY = midY / vertices.length;
		float lineX = (vertices[0].getX() + vertices[1].getX()) / 2;
		float lineY = (vertices[0].getY() + vertices[1].getY()) / 2;
		float apothem = new FloatPoint(midX, midY).distance(new FloatPoint(
				lineX, lineY));
		
		mass = apothem * perimeter / 2;
		
		midPoint = new float[3];
		midPoint[2] = z;
		for (int i = 0; i < vertices.length; i++) {
			midPoint[0] += vertices[i].getX();
			midPoint[1] += vertices[i].getY();
		}
		midPoint[0] = midPoint[0] / vertices.length;
		midPoint[1] = midPoint[1] / vertices.length;
	}

	private FloatPoint[] getAxes() {
		FloatPoint[] axes = new FloatPoint[vertices.length];

		FloatPoint edge;
		for (int i = 0; i < vertices.length - 1; i++) {
			edge = vertices[i].sub(vertices[i + 1]);
			axes[i] = new FloatPoint(-edge.getY(), edge.getX());
			axes[i].normalize();
		}
		edge = vertices[vertices.length - 1].sub(vertices[0]);
		axes[axes.length - 1] = new FloatPoint(-edge.getY(), edge.getX());
		axes[axes.length - 1].normalize();

		return axes;
	}

}
