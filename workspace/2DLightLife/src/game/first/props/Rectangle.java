package game.first.props;

import game.first.lighting.PointLight;
import game.first.physics.CollisionBox;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.List;

import android.opengl.GLES20;

public class Rectangle extends Shape {

	private ShortBuffer drawListBuffer;

	private short drawOrder[] = { 1, 2, 0, 0, 2, 3 };

	public Rectangle(float width, float height, float[] color, float x,
			float y, float z, boolean collision, boolean destructible) {
		super(x, y, z, color, destructible);
		float[] points = createVertices(width, height);
		super.installVertices(points);
		if (collision) {
			float[] collisionPoints = new float[8];
			for (int i = 0; i < 4; i++) {
				collisionPoints[i * 2] = points[i * 3];
				collisionPoints[i * 2 + 1] = points[i * 3 + 1];
			}
			addCollision(new CollisionBox(collisionPoints, (int) z));
		}
		ByteBuffer dlb = ByteBuffer.allocateDirect(
		// (# of coordinate values * 2 bytes per short)
				drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);

	}

	private float[] createVertices(float width, float height) {
		float[] vertices = new float[12];
		float[] pos = position;
		vertices[0] = pos[0] - width / 2;
		vertices[1] = pos[1] - height / 2;
		vertices[2] = pos[2];

		vertices[3] = pos[0] + width / 2;
		vertices[4] = pos[1] - height / 2;
		vertices[5] = pos[2];

		vertices[6] = pos[0] + width / 2;
		vertices[7] = pos[1] + height / 2;
		vertices[8] = pos[2];

		vertices[9] = pos[0] - width / 2;
		vertices[10] = pos[1] + height / 2;
		vertices[11] = pos[2];

		return vertices;
	}

	@Override
	public void draw(float[] vMatrix, float[] pMatrix, float[] mMatrix,
			List<PointLight> pointLights) {
		super.draw(vMatrix, pMatrix, mMatrix, pointLights);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(positionHandle);
	}

	@Override
	public String toString() {
		return "Rectangle: " + position[0] + ", " + position[1];
	}

}
