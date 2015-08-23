package game.first.props;

import game.first.graphics.GLRenderer;
import game.first.lighting.PointLight;
import game.first.math.FloatPoint;
import game.first.physics.CollisionShape;
import game.first.world.World;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.List;

import android.opengl.GLES20;
import android.opengl.Matrix;

public abstract class Shape {

	protected static final String standardVertexShaderCode =
	// This matrix member variable provides a hook to manipulate
	// the coordinates of the objects that use this vertex shader
	"uniform mat4 u_MVPMatrix;"
			+ "uniform mat4 u_MMatrix;"
			+ "attribute vec4 a_Position;"
			+ "varying vec3 v_Position;"
			+ "void main() {"
			// the matrix must be included as a modifier of gl_Position
			// Note that the uMVPMatrix factor *must be first* in order
			// for the matrix multiplication product to be correct.
			+ "v_Position = vec3(u_MMatrix * a_Position);"
			+ "gl_Position = u_MVPMatrix * a_Position;" + "}";

	protected static final String standardFragmentShaderCode = "precision lowp float;"
			// Max 10 pointLights
			+ "uniform vec4 u_PointLight[20];"
			+ "varying vec3 v_Position;"
			+ "uniform vec4 vColor;"
			+ "void main() {"
			+ "int i;"
			+ "vec3 temp = u_PointLight[0].xyz;"
			+ "float distance = length(temp - v_Position);"
			+ "distance = distance / u_PointLight[0].w;"
			+ "distance = 1.0 / (distance * distance);"
			+ "distance = clamp(distance, 0.0, 1.0);"
			+ "vec4 lightColor = u_PointLight[1] * distance;"
			+ "vec4 tempLight;"
			+ "for (i = 2; i < 10; i+=2) {"
			+ "temp = u_PointLight[i].xyz;"
			+ "distance = length(temp - v_Position);"
			+ "distance = distance / u_PointLight[i].w;"
			+ "distance = 1.0 / (distance * distance);"
			+ "distance = clamp(distance, 0.0, 1.0);"
			+ "tempLight = u_PointLight[i+1] * distance;"
			+ "lightColor.x = lightColor.x + tempLight.x;"
			+ "lightColor.y = lightColor.y + tempLight.y;"
			+ "lightColor.z = lightColor.z + tempLight.z;"
			+ "}"
			+ "lightColor.x = lightColor.x * vColor.x;"
			+ "lightColor.y = lightColor.y * vColor.y;"
			+ "lightColor.z = lightColor.z * vColor.z;"
			+ "lightColor.w = vColor.w;" + "gl_FragColor = lightColor;" + "}";

	// memory
	public int id;
	public char type;

	// draw variables
	public static final int COORDS_PER_VERTEX = 3;
	protected FloatBuffer vertexBuffer;
	protected final float[] modelMatrix = new float[16];
	public int shaderProgram;
	protected int positionHandle;
	private int mMVPMatrixHandle, mMMatrixHandle, colorHandle;
	private float[] color = new float[4];
	protected String vertexShaderCode, fragmentShaderCode;

	// collision variables
	private FloatPoint directionVector;
	private boolean pushable = true;

	public float[] position = new float[4];
	public CollisionShape collisionShape;
	private List<Shape> nearShapes;
	public Shape lastCollision;
	private boolean destructible;

	public Shape(float x, float y, float z, float[] color, boolean destructible) {
		position[0] = x;
		position[1] = y;
		position[2] = z;
		position[3] = 1;
		this.destructible = destructible;
		for (int i = 0; i < 4; i++) {
			this.color[i] = color[i];
		}
		Matrix.setIdentityM(modelMatrix, 0);
		directionVector = new FloatPoint(0, 0);
	}

	public void getModelMatrix(float[] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			matrix[i] = modelMatrix[i];
		}
	}

	public void setAlpha(float alpha) {
		if (alpha < 0) {
			alpha = 0;
		} else if (alpha > 1.0f) {
			alpha = 1.0f;
		}
		color[3] = alpha;
	}

	public float getAlpha() {
		return color[3];
	}

	public float[] getColor() {
		float[] send = new float[4];
		for (int i = 0; i < 4; i++) {
			send[i] = color[i];
		}
		return send;
	}

	public boolean isDestructible() {
		return destructible;
	}

	public void addCollision(CollisionShape collisionShape) {
		this.collisionShape = collisionShape;
	}

	public void setShaders(String vertexShader, String fragmentShader) {
		vertexShaderCode = vertexShader;
		fragmentShaderCode = fragmentShader;
	}

	public String[] getShaders() {
		String[] send = new String[2];
		if (vertexShaderCode == null || fragmentShaderCode == null) {
			send[0] = standardVertexShaderCode;
			send[1] = standardFragmentShaderCode;
			return send;
		} else {
			send[0] = vertexShaderCode;
			send[1] = fragmentShaderCode;
			return send;
		}

	}

	/**
	 * 
	 * @param vMatrix
	 * @param pMatrix
	 * @param mMatrix
	 *            leave as null unless it's a slave
	 * @param pointLights
	 */
	public void draw(float[] vMatrix, float[] pMatrix, float[] mMatrix,
			List<PointLight> pointLights) {

		if (mMatrix == null) {
			mMatrix = modelMatrix;
		}
		// Add program to OpenGL ES environment
		GLES20.glUseProgram(shaderProgram);

		// get handle to vertex shader's vPosition member
		positionHandle = GLES20
				.glGetAttribLocation(shaderProgram, "a_Position");

		// Enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(positionHandle);

		// Prepare the shape coordinate data
		GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, 12, vertexBuffer);
		// Log.d("Graphics", "" + vertexBuffer.capacity());
		mMMatrixHandle = GLES20
				.glGetUniformLocation(shaderProgram, "u_MMatrix");
		GLES20.glUniformMatrix4fv(mMMatrixHandle, 1, false, mMatrix, 0);
		// get handle to shape's transformation matrix
		mMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram,
				"u_MVPMatrix");
		float[] mvpMatrix = new float[16];
		float[] pvMatrix = new float[16];
		Matrix.multiplyMM(pvMatrix, 0, pMatrix, 0, vMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, pvMatrix, 0, mMatrix, 0);
		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		// Log.d("Graphics", "first" + lights.size());

		colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
		GLES20.glUniform4fv(colorHandle, 1, color, 0);

		int pointLightHandle = GLES20.glGetUniformLocation(shaderProgram,
				"u_PointLight");
		float[] pointLightData = new float[World.MAX_POINT_LIGHTS * 8];
		int i = 0;
		Iterator<PointLight> iter = pointLights.iterator();
		while (iter.hasNext()) {
			PointLight temp = iter.next();
			float[] data = temp.getPos();
			pointLightData[i * 8] = data[0];
			pointLightData[i * 8 + 1] = data[1];
			pointLightData[i * 8 + 2] = data[2];
			pointLightData[i * 8 + 3] = temp.getStrength();
			data = temp.getColor();
			pointLightData[i * 8 + 4] = data[0];
			pointLightData[i * 8 + 5] = data[1];
			pointLightData[i * 8 + 6] = data[2];
			pointLightData[i * 8 + 7] = data[3];
			i++;

		}

		GLES20.glUniform4fv(pointLightHandle, World.MAX_POINT_LIGHTS * 2,
				pointLightData, 0);

	}

	public void giveShaderProgram(int program) {
		shaderProgram = program;
	}

	public void installGraphics(String vertexShaderCode,
			String fragmentShaderCode) {

		int vertexShader = GLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);

		int fragmentShader = GLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		shaderProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(shaderProgram, fragmentShader);
		GLES20.glAttachShader(shaderProgram, vertexShader);
		GLES20.glLinkProgram(shaderProgram);
	}

	/**
	 * Sets up a floatbuffer based on the given coordinates which later can be
	 * used to render our shape.
	 * 
	 * @param vertexCoords
	 */
	protected void installVertices(float vertexCoords[]) {
		ByteBuffer bb = ByteBuffer.allocateDirect(vertexCoords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(vertexCoords);
		vertexBuffer.position(0);
	}

	public float[] getRoughBounds() {
		if (collisionShape == null) {
			return null;
		}
		return collisionShape.roughBounds;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param world
	 * @return If null, move was successful
	 */
	public FloatPoint moveGetMTV(float x, float y) {
		FloatPoint send = null;
		if (collisionShape == null || nearShapes == null
				|| nearShapes.isEmpty()) {
			moveWithoutCollision(x, y);
			return send;
		}
		if (x == 0 && y == 0) {
			directionVector = new FloatPoint(0, 0);
			return null;
		}
		Iterator<Shape> iter = nearShapes.iterator();
		collisionShape.move(x, y);
		while (iter.hasNext()) {
			Shape test = iter.next();
			if (collisionShape.overlaps(test.collisionShape)) {
				collisionShape.move(-x, -y);
				send = collisionShape.getLastMTV();
				lastCollision = test;
				return send;
			}
		}
		moveWithoutCollision(x, y);
		return send;
	}

	public boolean move(float x, float y) {
		if (collisionShape == null || nearShapes == null
				|| nearShapes.isEmpty()) {
			moveWithoutCollision(x, y);
			return true;
		}
		if (x == 0 && y == 0) {
			directionVector = new FloatPoint(0, 0);
			return true;
		}
		Iterator<Shape> iter = nearShapes.iterator();
		collisionShape.move(x, y);
		while (iter.hasNext()) {
			Shape test = iter.next();
			if (collisionShape.overlaps(test.collisionShape)) {
				collisionShape.move(-x, -y);
				lastCollision = test;
				return false;
			}
		}
		moveWithoutCollision(x, y);
		return true;
	}

	public void updateCollisionShapeList(World world) {
		nearShapes = world.getNearShapes(this.getRoughBounds());
	}

	private void moveWithoutCollision(float x, float y) {
		Matrix.translateM(modelMatrix, 0, x, y, 0);
		position[0] += x;
		position[1] += y;
		directionVector = new FloatPoint(x, y);
	}

	public void setPushable(boolean pushable) {
		this.pushable = pushable;
	}

	public FloatPoint push(float x, float y, float pushMass) {
		if (type == 'S' || !pushable) {
			return new FloatPoint(0, 0);
		}

		float strength = pushMass / (collisionShape.mass + pushMass);

		FloatPoint result = new FloatPoint(x, y);
		result = result.mult(strength);
		if (move(result.getX(), result.getY())) {
			return new FloatPoint(result.getX(), result.getY());
		}
		return new FloatPoint(0, 0);
	}

	public FloatPoint getForce() {
		FloatPoint send;
		if (directionVector.getX() == 0 && directionVector.getY() == 0) {
			return null;
		} else {
			send = new FloatPoint(directionVector.getX(),
					directionVector.getY());
			send = send.mult(collisionShape.mass);
			return send;
		}
	}

	/**
	 * 
	 * @param degree
	 *            to rotate
	 * @param x
	 *            coordinate to rotate around
	 * @param y
	 *            coordinate to rotate around
	 */
	public void rotate(float degree, float x, float y) {
		float[] rot = new float[16];
		Matrix.setIdentityM(rot, 0);
		Matrix.translateM(rot, 0, x, y, position[2]);
		Matrix.rotateM(rot, 0, degree, 0, 0, 1);
		Matrix.translateM(rot, 0, -x, -y, -position[2]);
		float[] temp = new float[16];
		Matrix.multiplyMM(temp, 0, modelMatrix, 0, rot, 0);
		for (int i = 0; i < 16; i++) {
			modelMatrix[i] = temp[i];
		}

		Matrix.multiplyMV(temp, 0, rot, 0, position, 0);
		if (collisionShape != null) {
			collisionShape.move(temp[0] - position[0], temp[1] - position[1]);
			collisionShape.rotate(degree);
		}
		for (int i = 0; i < 3; i++) {
			position[i] = temp[i];
		}
	}

	/**
	 * Not compatible with collision at the moment
	 * 
	 * @param xScale
	 * @param yScale
	 * @param zScale
	 */
	public void scale(float xScale, float yScale, float x, float y) {
		if (position[0] == 0 && position[1] == 0) {
			Matrix.scaleM(modelMatrix, 0, xScale, yScale, 1);
			position[0] = position[0] * xScale;
			position[1] = position[1] * yScale;

		} else {
			Matrix.translateM(modelMatrix, 0, x, y, 0);
			Matrix.scaleM(modelMatrix, 0, xScale, yScale, 1);
			Matrix.translateM(modelMatrix, 0, -x, -y, 0);
			position[0] = position[0] - (position[0] - x) * xScale;
			position[1] = position[1] - (position[1] - y) * yScale;
		}

	}

	public void translateZ(float z) {
		Matrix.translateM(modelMatrix, 0, 0, 0, z);
		position[2] += z;
	}

	public abstract String toString();
}
