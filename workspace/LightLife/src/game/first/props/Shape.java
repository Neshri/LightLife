package game.first.props;

import game.first.math.Vect3;
import game.first.world.World;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.Matrix;

public abstract class Shape {
	protected final float[] modelViewMatrix = new float[16];
	
	protected final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // the matrix must be included as a modifier of gl_Position
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    protected final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";
	
	private Vect3 position;
	protected FloatBuffer vertexBuffer;
	public int id;

	public Shape(Vect3 pos) {
		position = pos;
		Matrix.setIdentityM(modelViewMatrix, 0);
	}
	
	public Vect3 getPos() {
		return position;
	}

	/**
	 * The world is the environment we want our shape installed in. The propID
	 * is used to identify this object in case we need to find it in the world.
	 * 
	 * @param world
	 * @return propID
	 */
	public int installShape(World world) {
		// TODO
		return 0;
	}
	
	public abstract void installGraphics();
	


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

	/**
	 * Changes the prop's position in the world to the specified parameters.
	 * 
	 * @param pos
	 */
	public void changePosition(float xPos, float yPos, float zPos) {
		position = new Vect3(xPos, yPos, zPos);
	}
	
	public abstract void draw(float[] mvpMatrix);
}
