package game.first.props;

import game.first.lighting.PointLight;
import game.first.physics.CollisionTriangle;

import java.util.List;

import android.opengl.GLES20;

public class Triangle extends Shape {

	public Triangle(float base, float height, float color[], float x, float y,
			float z, boolean collision, boolean destructible) {
		super(x, y, z, color, destructible);
		float[] points = createVertices(base, height);
		if (collision) {
			float[] collisionPoints = new float[6];
			for (int i = 0; i < 3; i++) {
				collisionPoints[i * 2] = points[i * 3];
				collisionPoints[i * 2 + 1] = points[i * 3 + 1];
			}
			super.collisionShape = new CollisionTriangle(collisionPoints,
					(int) points[2]);
		}
		super.installVertices(points);

	}

	private float[] createVertices(float base, float height) {
		float[] vertices = new float[9];
		float[] pos = this.position;
		for (int i = 0; i < 3; i++) {
			vertices[i] = pos[i];
		}
		vertices[3] = pos[0] + base;
		for (int i = 4; i < 6; i++) {
			vertices[i] = pos[i - 3];
		}
		vertices[6] = pos[0] + base / 2;
		vertices[7] = pos[1] + height;
		vertices[8] = pos[2];

		return vertices;
	}

	@Override
	public void draw(float[] vMatrix, float[] pMatrix, float[] mMatrix,
			List<PointLight> pointLights) {
		super.draw(vMatrix, pMatrix, mMatrix, pointLights);
		// Draw the triangle
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(super.positionHandle);

	}

	@Override
	public String toString() {
		return "Triangle: " + position[0] + ", " + position[1];
	}

}
