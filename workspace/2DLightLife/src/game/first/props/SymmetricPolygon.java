package game.first.props;

import android.opengl.GLES20;
import android.opengl.Matrix;
import util.InvalidFormatException;

public class SymmetricPolygon extends Shape {

	private static final String vertexShaderCode =
	// This matrix member variable provides a hook to manipulate
	// the coordinates of the objects that use this vertex shader
	"uniform mat4 uMVPMatrix;" + "attribute vec4 vPosition;" + "void main() {" +
	// the matrix must be included as a modifier of gl_Position
	// Note that the uMVPMatrix factor *must be first* in order
	// for the matrix multiplication product to be correct.
			"  gl_Position = uMVPMatrix * vPosition;" + "}";

	private static final String fragmentShaderCode = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main() {"
			+ "  gl_FragColor = vColor;" + "}";

	private float[] color;
	private int positionHandle, colorHandle, mMVPMatrixHandle, corners;

	/**
	 * NOT DONE!!!
	 * 
	 * @param corners
	 * @param color
	 * @param x
	 * @param y
	 * @param z
	 * @param collision
	 */
	public SymmetricPolygon(int corners, float maxRadius, float[] color,
			float x, float y, float z, boolean collision)
			throws InvalidFormatException {
		super(x, y, z);
		this.corners = corners;
		if (color.length != 4) {
			throw new InvalidFormatException(
					"A color should consist of 4 float values(r,g,b,a)");
		} else {
			float[] points = createVertices(corners, maxRadius);
			super.installVertices(points);
			this.color = color;
			installVertices(points);
			if (collision) {
				// fixa
			}
		}

		// TODO Auto-generated constructor stub
	}

	private float[] createVertices(int corners, float maxRadius) {
		double degreeChunk = Math.PI / corners;
		float[] send = new float[corners * 3];
		for (int i = 0; i < corners; i++) {
			send[i * 3] = (float) (super.position[0] + Math
					.cos(degreeChunk * i) * maxRadius);
			send[i * 3 + 1] = (float) (super.position[1] + Math.sin(degreeChunk
					* i)
					* maxRadius);
			send[i * 3 + 2] = super.position[2];
		}

		return send;
	}

	@Override
	public String[] getShaders() {
		String[] send = new String[2];
		send[0] = vertexShaderCode;
		send[1] = fragmentShaderCode;
		return send;
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
				GLES20.GL_FLOAT, false, 4 * corners, super.vertexBuffer);

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
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, corners);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(positionHandle);

	}

}
