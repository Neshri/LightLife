package game.first.props;

import game.first.physics.CollisionTriangle;
import util.InvalidFormatException;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class Triangle extends Shape {

	private int positionHandle, colorHandle, mMVPMatrixHandle;

	private float color[];

	public Triangle(float base, float height, float color[], float x, float y,
			float z, boolean collision) throws InvalidFormatException {
		super(x, y, z);

		if (color.length != 4) {
			throw new InvalidFormatException(
					"A color should consist of 4 float values(r,g,b,a)");
		} else {
			float[] points = createVertices(base, height);
			float[] collisionPoints = new float[6];
			for (int i = 0; i < 3; i++) {
				collisionPoints[i * 2] = points[i * 3];
				collisionPoints[i * 2 + 1] = points[i * 3 + 1];
			}
			super.collisionShape = new CollisionTriangle(collisionPoints,
					(int) points[2]);
			super.installVertices(points);
			this.color = color;

		}
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

		// Draw the triangle
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

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
