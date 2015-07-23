package game.first.props;

import game.first.lighting.PointLight;
import game.first.physics.CollisionSymmetricPolygon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.List;

import android.opengl.GLES20;

public class SymmetricPolygon extends Shape {

	private ShortBuffer drawListBuffer;
	private short[] drawOrder;

	/**
	 * 
	 * 
	 * @param corners
	 *            defines the the jaggedness of the shape
	 * @param color
	 *            Is it blue and black or gold and white?
	 * @param x
	 * @param y
	 * @param z
	 * @param collision
	 *            if true the shape will calculate possible collisions
	 */
	public SymmetricPolygon(int corners, float maxRadius, float[] color,
			float x, float y, float z, boolean collision, boolean destructible) {
		super(x, y, z, color, destructible);
		if (corners < 3) {
			corners = 3;
		}
		float[] points = createVertices(corners, maxRadius);
		super.installVertices(points);
		if (collision) {
			float[] collisionPoints = new float[corners * 2];
			for (int i = 0; i < corners; i++) {
				collisionPoints[i * 2] = points[i * 3];
				collisionPoints[i * 2 + 1] = points[i * 3 + 1];
			}
			addCollision(new CollisionSymmetricPolygon(collisionPoints, (int) z));
		}
		ByteBuffer dlb = ByteBuffer.allocateDirect(
		// (# of coordinate values * 2 bytes per short)
				drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);

		// TODO Auto-generated constructor stub
	}

	private float[] createVertices(int corners, float maxRadius) {
		double degreeChunk = 2 * Math.PI / corners;
		float[] send = new float[corners * 3];
		drawOrder = new short[(corners - 2) * 3];

		for (int i = 0; i < corners; i++) {
			send[i * 3] = (float) (super.position[0] + Math
					.cos(degreeChunk * i) * maxRadius);

			send[i * 3 + 1] = (float) (super.position[1] + Math.sin(degreeChunk
					* i)
					* maxRadius);

			send[i * 3 + 2] = super.position[2];
			if (i < corners - 2) {
				drawOrder[i * 3] = 0;
				drawOrder[i * 3 + 1] = (short) (i + 1);
				drawOrder[i * 3 + 2] = (short) (i + 2);
			}

		}

		return send;
	}

	@Override
	public void draw(float[] vMatrix, float[] pMatrix, float[] mMatrix,
			List<PointLight> pointLights) {
		// if (vertexShaderCode != null || fragmentShaderCode != null) {
		// drawSimple(vMatrix, pMatrix);
		// return;
		// }
		super.draw(vMatrix, pMatrix, mMatrix, pointLights);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(super.positionHandle);

	}

	@Override
	public String toString() {
		return "Symmetric Polygon: " + position[0] + ", " + position[1];
	}

}
