package game.first.graphics;

import game.first.pawn.Player;
import game.first.props.Shape;
import game.first.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class FrameRenderer extends GLRenderer implements Observer {

	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjectionMatrix = new float[16];

	private long lastTime;
	private List<Shape> shapes;
	private Player player;
	private World world;

	public FrameRenderer(Player player) {
		super();
		this.player = player;
		world = player.getWorld();
		world.addObserver(this);
		shapes = world.getShapes();
	}

	@Override
	public void onCreate(int width, int height, boolean contextLost) {
		GLES20.glClearColor(0f, 0f, 0f, 1f);
		GLES20.glViewport(0, 0, width, height);
		lastTime = System.currentTimeMillis();

		float ratio = (float) width / height;
		// this projection matrix is applied to object coordinates
		// in the onDrawFrame() method
		Matrix.perspectiveM(mProjectionMatrix, 0, 90, ratio, 0, 100f);

	}

	@Override
	public void createShapes() {
		Iterator<Shape> iter = shapes.iterator();
		while (iter.hasNext()) {
			iter.next().installGraphics();
		}
	}

	@Override
	public void onDrawFrame(boolean firstDraw) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0,
				player.mViewMatrix, 0);

		Iterator<Shape> iter = shapes.iterator();
		while (iter.hasNext()) {
			Shape draw = iter.next();
			draw.draw(mMVPMatrix);
			Log.d("triangle", draw.getPos().toString());
		}
		// skriver fps varannan sekund
		if (lastTime <= System.currentTimeMillis() - 2000l) {
			Log.d("FPS", "" + super.getFPS());
			lastTime = System.currentTimeMillis();
		}

	}

	@Override
	public void update(Observable observable, Object data) {
		shapes = world.getShapes();
	}
}
