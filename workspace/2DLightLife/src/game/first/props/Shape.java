package game.first.props;

import game.first.graphics.GLRenderer;
import game.first.physics.CollisionShape;
import game.first.world.World;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Iterator;

import android.opengl.GLES20;
import android.opengl.Matrix;

public abstract class Shape {

	protected static final String standardVertexShaderCode =
	// This matrix member variable provides a hook to manipulate
	// the coordinates of the objects that use this vertex shader
	"uniform mat4 uMVPMatrix;" + "attribute vec4 vPosition;" + "void main() {" +
	// the matrix must be included as a modifier of gl_Position
	// Note that the uMVPMatrix factor *must be first* in order
	// for the matrix multiplication product to be correct.
			"  gl_Position = uMVPMatrix * vPosition;" + "}";

	protected static final String standardFragmentShaderCode = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main() {"
			+ "  gl_FragColor = vColor;" + "}";

	public int id;
	public static final int COORDS_PER_VERTEX = 3;
	public float[] position = new float[3];
	protected FloatBuffer vertexBuffer;
	protected final float[] modelView = new float[16];
	protected int shaderProgram;
	public CollisionShape collisionShape;

	public Shape(float x, float y, float z) {
		position[0] = x;
		position[1] = y;
		position[2] = z;
		Matrix.setIdentityM(modelView, 0);
	}

	public void addCollision(CollisionShape collisionShape) {
		this.collisionShape = collisionShape;
	}

	public abstract String[] getShaders();

	public abstract void draw(float[] mvpMatrix);

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

	public boolean move(float x, float y, World world) {
		if (collisionShape == null) {
			Matrix.translateM(modelView, 0, x, y, 0);
			position[0] += x;
			position[1] += y;
			return true;
		}
		Iterator<CollisionShape> iter = world.getNearCollision(
				this.getRoughBounds()).iterator();
		collisionShape.move(x, y);
		// int a = 0;
		while (iter.hasNext()) {
			if (collisionShape.overlaps(iter.next())) {
				collisionShape.move(-x, -y);
				return false;
			}
			// a++;

		}
		// Log.d("Collision", a+"");
		// Log.d("Collision", x + " "+ y);
		Matrix.translateM(modelView, 0, x, y, 0);
		position[0] += x;
		position[1] += y;
		return true;
	}

}
