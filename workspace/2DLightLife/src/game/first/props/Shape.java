package game.first.props;

import game.first.graphics.GLRenderer;
import game.first.lighting.LightSource;
import game.first.lighting.PointLight;
import game.first.math.FloatPoint;
import game.first.physics.CollisionShape;
import game.first.world.World;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.List;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

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
			+ "uniform vec4 u_PointLight[10];"
			+ "varying vec3 v_Position;"
			+ "uniform vec4 vColor;"
			+ "void main() {"
			+ "int i;"
			+ "float strength;"
			+ "vec3 temp = u_PointLight[0].xyz;"
			+ "float distance = length(temp - v_Position);"
			+ "distance = distance / u_PointLight[0].w;"
			+ "vec4 pointLightColor = u_PointLight[1];"
			+ "float test_distance;"
			+ "for (i = 2; i < 10; i+=2) {"
			+ "temp = u_PointLight[i].xyz;"
			+ "test_distance = length(temp - v_Position);"
			+ "test_distance = test_distance / u_PointLight[i].w;"
			+ "if (test_distance < distance) {"
			+ "distance = test_distance;"
			+ "pointLightColor = u_PointLight[i+1];"
			+ "}"
			+ "}"
			+ "distance = 1.0 / (distance * distance);"
			+ "distance = clamp(distance, 0.0, 1.0);"
			+ "pointLightColor.x = pointLightColor.x * vColor.x;"
			+ "pointLightColor.y = pointLightColor.y * vColor.y;"
			+ "pointLightColor.z = pointLightColor.z * vColor.z;"
			+ "pointLightColor.w = pointLightColor.w * vColor.w;"
			+ "gl_FragColor = pointLightColor * distance;" + "}";

	public int id;
	public static final int COORDS_PER_VERTEX = 3;
	public float[] position = new float[3];
	protected FloatBuffer vertexBuffer;
	protected final float[] modelMatrix = new float[16];
	protected int shaderProgram, positionHandle;
	public CollisionShape collisionShape;
	private List<CollisionShape> nearShapes;
	private int mMVPMatrixHandle, mMMatrixHandle, colorHandle;
	private float[] color = new float[4];
	protected String vertexShaderCode, fragmentShaderCode;

	public Shape(float x, float y, float z, float[] color) {
		position[0] = x;
		position[1] = y;
		position[2] = z;
		for (int i = 0; i < 4; i++) {
			this.color[i] = color[i];
		}
		Matrix.setIdentityM(modelMatrix, 0);
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

	public void draw(float[] vMatrix, float[] pMatrix, List<PointLight> pointLights) {
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
		mMMatrixHandle = GLES20.glGetUniformLocation(shaderProgram,
				"u_MMatrix");
//		float[] mvMatrix = new float[16];
//		Matrix.multiplyMM(mvMatrix, 0, vMatrix, 0, modelMatrix, 0);
		GLES20.glUniformMatrix4fv(mMMatrixHandle, 1, false, modelMatrix, 0);
		// get handle to shape's transformation matrix
		mMVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram,
				"u_MVPMatrix");
		float[] mvpMatrix = new float[16];
		Matrix.multiplyMM(mvpMatrix, 0, pMatrix, 0, vMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, modelMatrix, 0);
		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		// Log.d("Graphics", "first" + lights.size());
		colorHandle = GLES20.glGetUniformLocation(shaderProgram, "vColor");
		GLES20.glUniform4fv(colorHandle, 1, color, 0);
		if (pointLights.isEmpty()) {
			return;
		}
		int lightHandle = GLES20.glGetUniformLocation(shaderProgram,
				"u_PointLight");
		float[] pointLightData = new float[pointLights.size() * 8];
		for (int i = 0; i < pointLights.size(); i++) {
			float[] temp = pointLights.get(i).getPos();
			pointLightData[i*8] = temp[0];
			pointLightData[i*8+1] = temp[1];
			pointLightData[i*8+2] = temp[2];
			pointLightData[i*8+3] = pointLights.get(i).getStrength();
			temp = pointLights.get(i).getColor();
			pointLightData[i*8+4] = temp[0];
			pointLightData[i*8+5] = temp[1];
			pointLightData[i*8+6] = temp[2];
			pointLightData[i*8+7] = temp[3];
		}

		GLES20.glUniform4fv(lightHandle, pointLights.size() * 2, pointLightData, 0);

		

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
		if (collisionShape == null) {
			moveWithoutCollision(x, y);
			return send;
		}
		Iterator<CollisionShape> iter = nearShapes.iterator();
		collisionShape.move(x, y);
		while (iter.hasNext()) {
			if (collisionShape.overlaps(iter.next())) {
				collisionShape.move(-x, -y);
				send = collisionShape.getLastMTV();
				return send;
			}
		}
		moveWithoutCollision(x, y);
		return send;
	}

	public boolean move(float x, float y) {
		if (collisionShape == null) {
			moveWithoutCollision(x, y);
			return true;
		}
		Iterator<CollisionShape> iter = nearShapes.iterator();
		collisionShape.move(x, y);
		while (iter.hasNext()) {
			if (collisionShape.overlaps(iter.next())) {
				collisionShape.move(-x, -y);
				return false;
			}
		}
		moveWithoutCollision(x, y);
		return true;
	}

	public void updateCollisionShapeList(World world) {
		nearShapes = world.getNearCollision(this.getRoughBounds());
		nearShapes.remove(collisionShape);
	}

	private void moveWithoutCollision(float x, float y) {
		Matrix.translateM(modelMatrix, 0, x, y, 0);
		position[0] += x;
		position[1] += y;
	}

}
