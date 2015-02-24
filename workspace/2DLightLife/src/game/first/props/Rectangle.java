package game.first.props;

import game.first.physics.CollisionBox;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

import util.InvalidFormatException;

public class Rectangle extends Shape {


	private float[] color;
	private ShortBuffer drawListBuffer;

	private int positionHandle, colorHandle, mMVPMatrixHandle;

	private short drawOrder[] = { 1, 2, 0, 0, 2, 3 };

	public Rectangle(float width, float height, float[] color, float x,
			float y, float z, boolean collision) throws InvalidFormatException {
		super(x, y, z);

		if (color.length != 4) {
			throw new InvalidFormatException(
					"A color should consist of 4 float values(r,g,b,a)");
		} else {
			float[] points = createVertices(width, height);
			super.installVertices(points);
			this.color = color;
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
	}

	private float[] createVertices(float width, float height) {
		float[] vertices = new float[12];
		float[] pos = position;
		for (int i = 0; i < 3; i++) {
			vertices[i] = pos[i];
		}
		vertices[3] = pos[0] + width;
		vertices[4] = pos[1];
		vertices[5] = pos[2];
		vertices[6] = pos[0] + width;
		vertices[7] = pos[1] + height;
		vertices[8] = pos[2];
		vertices[9] = pos[0];
		vertices[10] = vertices[7];
		vertices[11] = pos[2];

		return vertices;
	}

	@Override
	public void draw(float[] mvpMatrix) {
		// Add program to OpenGL ES environment
		GLES20.glUseProgram(shaderProgram);

		// get handle to vertex shader's vPosition member
		positionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");

		// Enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(positionHandle);

		// GLES20.glVertexAttrib3fv(positionHandle, super.modelMatrix, 0);

		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, 12, super.vertexBuffer);

		// get handle to fragment shader's vColor member
		colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");

		// Set color for drawing the triangle
		GLES20.glUniform4fv(colorHandle, 1, color, 0);

		// get handle to shape's transformation matrix
		mMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram,
				"uMVPMatrix");

		Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, super.modelView, 0);
		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(positionHandle);
	}

	@Override
	public String[] getShaders() {
		String[] send = new String[2];
		send[0] = standardVertexShaderCode;
		send[1] = standardFragmentShaderCode;
		return send;
	}

}
