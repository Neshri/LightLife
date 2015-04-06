package game.first.pawn;

import game.first.lighting.LightSource;
import game.first.lighting.PointLight;
import game.first.math.FloatPoint;
import game.first.mechanics.DestructionLight;
import game.first.mechanics.LightPulse;
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
	private final static float SPEEDCAP = 0.5f;

	private final float[] vDirection = new float[2];
	private final float[] vPosition = new float[2];
	private World world;
	private Controller control;
	private Camera camera;
	private Shape playerModel;
	private PointLight lightAura;
	private LightPulse lightBreath;
	private DestructionLight gun;


	public Player(float x, float y, World world) {
		this.world = world;
		camera = new Camera(x, y, 0f);
		addObserver(camera);
		vPosition[0] = x;
		vPosition[1] = y;

		vDirection[0] = 0f;
		vDirection[1] = 0f;
		float[] color = { 1.0f, 1.0f, 1.0f, 1.0f };
		try {
			// Creates the Player model
			playerModel = new SymmetricPolygon(8, 0.1f, color, x, y, 2, true, false);
			//playerModel.setShaders(vertexShaderCode, fragmentShaderCode);
			world.createDynamic(playerModel);
			
			lightAura = new PointLight(x, y, 2f, color, 0.6f);
			world.addPointLight(lightAura);
			lightBreath = new LightPulse(lightAura, 0.1f, 1);
		} catch (InvalidFormatException e) {

			e.printStackTrace();
		}
		new Follower(x, y-1, this, world);
		gun = new DestructionLight(world);
		control = new Controller(this);
	}

	public void move(float x, float y) {
		FloatPoint maxCalc = new FloatPoint(x, y);
		if (maxCalc.getLength() > 1.0f) {
			maxCalc.normalize();
			x = maxCalc.getX();
			y = maxCalc.getY();
		}
		
		
		vDirection[0] = vDirection[0] + x / 500f;
		vDirection[1] = vDirection[1] + y / 500f;
		maxCalc = new FloatPoint(vDirection[0], vDirection[1]);
		if (maxCalc.getLength() > SPEEDCAP) {
			maxCalc.normalize();
			maxCalc = maxCalc.mult(SPEEDCAP);
			vDirection[0] = maxCalc.getX();
			vDirection[1] = maxCalc.getY();
		}
		lightBreath.update();
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
		setChanged();
		notifyObservers();
	}

	
	public void shoot(float x, float y) {
		gun.shoot(new FloatPoint(x, y), new FloatPoint(vPosition[0], vPosition[1]));
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
	public void step(World world) {
		// does nothing
		
	}
	
	@Override
	public float getSpeed() {
		float send = (float) Math.sqrt(vDirection[0] * vDirection[0] + vDirection[1] * vDirection[1]);
		return send;
	}

}
