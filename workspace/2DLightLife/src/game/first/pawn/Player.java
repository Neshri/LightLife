package game.first.pawn;

import game.first.lighting.LightSource;
import game.first.lighting.PointLight;
import game.first.math.FloatPoint;
import game.first.props.Rectangle;
import game.first.props.Shape;
import game.first.props.SymmetricPolygon;
import game.first.world.World;

import java.util.Observable;

import android.util.Log;

import util.InvalidFormatException;

public class Player extends Observable implements Pawn {

	// This matrix member variable provides a hook to manipulate
	// the coordinates of the objects that use this vertex shader
	private String vertexShaderCode = "uniform mat4 uMVPMatrix;"
			+ "attribute vec4 vPosition;" + "void main() {" +
			// the matrix must be included as a modifier of gl_Position
			// Note that the uMVPMatrix factor *must be first* in order
			// for the matrix multiplication product to be correct.
			"  gl_Position = uMVPMatrix * vPosition;" + "}";

	private String fragmentShaderCode = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main() {"
			+ "  gl_FragColor = vColor;" + "}";

	private final static float SLOWDOWNSPEED = 1.1f;
	private final static float SPEEDCAP = 0.4f;

	private final float[] vDirection = new float[2];
	private final float[] vPosition = new float[2];
	private World world;
	private Controller control;
	private Camera camera;
	private Shape playerModel;
	private PointLight lightAura;
	private float lightGlow, glowAdd;

	public Player(float x, float y, World world) {
		this.world = world;
		lightGlow = 0.5f;
		glowAdd = 0.001f;
		camera = new Camera(x, y, 0f);
		addObserver(camera);
		vPosition[0] = x;
		vPosition[1] = y;

		vDirection[0] = 0f;
		vDirection[1] = 0f;
		float[] color = { 1.0f, 1.0f, 1.0f, 1.0f };
		try {
			// Creates the Player model
			playerModel = new SymmetricPolygon(8, 0.1f, color, x, y, 2, true);
			//playerModel.setShaders(vertexShaderCode, fragmentShaderCode);
			world.createDynamic(playerModel);
			
			lightAura = new PointLight(x, y, 2f, color, lightGlow);
			world.addPointLight(lightAura);
		} catch (InvalidFormatException e) {

			e.printStackTrace();
		}
		new Follower(x, y-1, this, world);
		control = new Controller(this);
	}

	public void move(float x, float y) {
		vDirection[0] = vDirection[0] + x / 500f;
		vDirection[1] = vDirection[1] + y / 500f;
		if (vDirection[0] > SPEEDCAP) {
			vDirection[0] = SPEEDCAP;
		}
		if (vDirection[1] > SPEEDCAP) {
			vDirection[1] = SPEEDCAP;
		}
		playerModel.updateCollisionShapeList(world);
		FloatPoint normalVector = playerModel.moveGetMTV(vDirection[0],
				vDirection[1]);
		if (normalVector != null) {
			FloatPoint slide = new FloatPoint(normalVector.getY(),
					-normalVector.getX());
			// Log.d("Collision", normalVector.toString());
			float multiplier = slide.dot(new FloatPoint(vDirection[0],
					vDirection[1]));
			slide = slide.mult(multiplier);

			vDirection[0] = slide.getX();
			vDirection[1] = slide.getY();
			if (!playerModel.move(vDirection[0], vDirection[1])) {
				vDirection[0] = 0;
				vDirection[1] = 0;
				return;
			}

		}

		vPosition[0] += vDirection[0];
		vPosition[1] += vDirection[1];

		vDirection[0] = vDirection[0] / SLOWDOWNSPEED;
		vDirection[1] = vDirection[1] / SLOWDOWNSPEED;
		lightAura.setPos(vPosition[0], vPosition[1], 2f);
		if (lightGlow > 0.7f) {
			glowAdd = -0.001f;
		} else if (lightGlow < 0.5f) {
			glowAdd = 0.001f;
		}
		lightGlow += glowAdd;
		lightAura.setStrength(lightGlow);
		setChanged();
		notifyObservers();
	}

	public Camera getCamera() {
		return camera;
	}

	@Override
	public FloatPoint getPosition() {
		FloatPoint send = new FloatPoint(vPosition[0], vPosition[1]);
		return send;
	}

	// Not tested yet
	public void setPosition(float x, float y) {
		float goX = x - vPosition[0];
		float goY = y - vPosition[1];
		playerModel.updateCollisionShapeList(world);
		if (!playerModel.move(goX, goY)) {
			return;
		}
		vPosition[0] = x;
		vPosition[1] = y;
		lightAura.setPos(vPosition[0], vPosition[1], 2f);
		setChanged();
		notifyObservers();
	}

	public Controller getController() {
		return control;
	}

	public World getWorld() {
		return world;
	}

	@Override
	public void step() {
		// does nothing
		
	}
	
	@Override
	public float getSpeed() {
		float send = (float) Math.sqrt(vDirection[0] * vDirection[0] + vDirection[1] * vDirection[1]);
		return send;
	}

}
