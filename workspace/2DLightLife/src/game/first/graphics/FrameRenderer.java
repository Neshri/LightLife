package game.first.graphics;

import game.first.lighting.LightSource;
import game.first.lighting.PointLight;
import game.first.pawn.Camera;
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
	private final float[] mProjectionMatrix = new float[16];

	private List<PointLight> pointLights;
	private List<Shape> shapes;
	private Camera camera;
	private World world;

	
	public FrameRenderer(Player player) {
		super();
		camera = player.getCamera();
		world = player.getWorld();
		world.addObserver(this);
		shapes = world.getShapes();
		pointLights = world.getPointLights();
	}

	@Override
	public void onCreate(int width, int height, boolean contextLost) {
		GLES20.glClearColor(0f, 0f, 0f, 1f);
		GLES20.glViewport(0, 0, width, height);
		float aspectRatio = (float) width / (float) height;

		// this projection matrix is applied to object coordinates
		// in the onDrawFrame() method
		Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1, 1,
				-10f, 10f);

	}

	@Override
	public void createShapes() {
		Iterator<Shape> iter = shapes.iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();
			String[] shaders = shape.getShaders();
			shape.installGraphics(shaders[0], shaders[1]);
		}
	}

	@Override
	public void onDrawFrame(boolean firstDraw) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		for (Shape a : shapes) {
			a.draw(camera.mViewMatrix, mProjectionMatrix, pointLights);
		}
		//Log.d("FPS", super.getFPS() +"");

	}

	@Override
	public void update(Observable observable, Object data) {
		shapes = world.getShapes();
		pointLights = world.getPointLights();
		createShapes();
	}
}
