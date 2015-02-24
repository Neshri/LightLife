package game.first.pawn;

import game.first.props.Rectangle;
import game.first.props.Shape;
import game.first.props.SymmetricPolygon;
import game.first.world.World;

import java.util.Observable;

import util.InvalidFormatException;

public class Player extends Observable {
	private final static float SLOWDOWNSPEED = 1.1f;
	private final static float SPEEDCAP = 0.2f;

	private final float[] vDirection = new float[2];
	private final float[] vPosition = new float[2];
	private World world;
	private Controller control;
	private Camera camera;
	private Shape playerModel;

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
			// playerModel = new Rectangle(0.2f, 0.2f, color, x - 0.1f, y -
			// 0.1f,
			// 1, true);

			playerModel = new SymmetricPolygon(8, 0.3f, color, x - 0.1f,
					y - 0.1f, 1, true);
			world.createDynamic(playerModel);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		control = new Controller(this);
	}

	public void move(float x, float y) {
		vDirection[0] = vDirection[0] + x / 500f;
		vDirection[1] = vDirection[1] + y / 500f;
		if (vDirection[0] >= SPEEDCAP) {
			vDirection[0] = SPEEDCAP;
		}
		if (vDirection[1] >= SPEEDCAP) {
			vDirection[1] = SPEEDCAP;
		}

		if (!playerModel.move(vDirection[0], vDirection[1], world)) {
			// Log.d("Collision", "hej");
			vDirection[0] = 0;
			vDirection[1] = 0;
			return;
		}
		vPosition[0] += vDirection[0];
		vPosition[1] += vDirection[1];

		vDirection[0] = vDirection[0] / SLOWDOWNSPEED;
		vDirection[1] = vDirection[1] / SLOWDOWNSPEED;

		setChanged();
		notifyObservers();
	}

	public Camera getCamera() {
		return camera;
	}

	public float[] getPosition() {
		return vPosition;
	}

	// Not tested yet
	public void setPosition(float x, float y) {
		float goX = x - vPosition[0];
		float goY = y - vPosition[1];

		if (!playerModel.move(goX, goY, world)) {
			return;
		}
		vPosition[0] = x;
		vPosition[1] = y;
		setChanged();
		notifyObservers();
	}

	public Controller getController() {
		return control;
	}

	public World getWorld() {
		return world;
	}

}
