package game.first.props;

import game.first.lighting.LightSource;
import game.first.lighting.PointLight;
import game.first.physics.CollisionSymmetricPolygon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.List;

import android.opengl.GLES20;
import android.opengl.Matrix;
import util.InvalidFormatException;

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
			float x, float y, float z, boolean collision)
			throws InvalidFormatException {
		super(x, y, z, color);
		if (corners < 3) {
			throw new InvalidFormatException(
					"Need at least 3 corners in a polygon");
		} else {
			float[] points = createVertices(corners, maxRadius);
			super.installVertices(points);
			if (collision) {
				float[] collisionPoints = new float[corners * 2];
				for (int i = 0; i < corners; i++) {
					collisionPoints[i * 2] = points[i * 3];
					collisionPoints[i * 2 + 1] = points[i * 3 + 1];
				}
				addCollision(new CollisionSymmetricPolygon(collisionPoints,
						(int) z));
			}
			ByteBuffer dlb = ByteBuffer.allocateDirect(
			// (# of coordinate values * 2 bytes per short)
					drawOrder.length * 2);
			dlb.order(ByteOrder.nativeOrder());
			drawListBuffer = dlb.asShortBuffer();
			drawListBuffer.put(drawOrder);
			drawListBuffer.position(0);
		}

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
	public void draw(float[] vMatrix, float[] pMatrix, List<PointLight> pointLights) {
//		if (vertexShaderCode != null || fragmentShaderCode != null) {
//			drawSimple(vMatrix, pMatrix);
//			return;
//		}
		super.draw(vMatrix, pMatrix, pointLights);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(super.positionHandle);

	}

//	public void drawSimple(float[] vMatrix, float[] pMatrix) {
//		// Add program to OpenGL ES environment
//		GLES20.glUseProgram(shaderProgram);
//
//		// get handle to vertex shader's vPosition member
//		positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
//
//		// Enable a handle to the triangle vertices
//		GLES20.glEnableVertexAttribArray(positionHandle);
//
//		// Prepare the triangle coordinate data
//		GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
//				GLES20.GL_FLOAT, false, 12, super.vertexBuffer);
//
//		// get handle to fragment shader's vColor member
//		colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
//
//		// Set color for drawing the triangle
//		GLES20.glUniform4fv(colorHandle, 1, color, 0);
//
//		// get handle to shape's transformation matrix
//		mMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram,
//				"uMVPMatrix");
//
//		float[] inputMatrix = new float[16];
//		Matrix.multiplyMM(inputMatrix, 0, pMatrix, 0, vMatrix, 0);
//		Matrix.multiplyMM(inputMatrix, 0, inputMatrix, 0, modelMatrix, 0);
//
//		// Apply the projection and view transformation
//		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, inputMatrix, 0);
//		// Draw the shape
//		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
//				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
//
//		// Disable vertex array
//		GLES20.glDisableVertexAttribArray(positionHandle);
//	}

}
