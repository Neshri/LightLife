package game.first.graphics;

import game.first.lighting.PointLight;
import game.first.pawn.Camera;
import game.first.pawn.Player;
import game.first.props.Shape;
import game.first.world.World;

import java.util.HashSet;
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
	
	private List<Shape> staticShapes, dynamicShapes;
	private Camera camera;
	private World world;
	private HashSet<Integer> needUpdate;
	private int[] updateArray;

	public FrameRenderer(Player player) {
		super();
		needUpdate = new HashSet<Integer>();
		camera = player.getCamera();
		world = player.getWorld();
		world.addObserver(this);
		staticShapes = world.getStaticShapes();
		dynamicShapes = world.getDynamicShapes();
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
		GLES20.glEnable (GLES20.GL_BLEND);
		GLES20.glBlendFunc (GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

	}

	@Override
	public void createShapes() {
		Iterator<Shape> iter = staticShapes.iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();
			String[] shaders = shape.getShaders();
			shape.installGraphics(shaders[0], shaders[1]);
		}
		iter = dynamicShapes.iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();
			String[] shaders = shape.getShaders();
			shape.installGraphics(shaders[0], shaders[1]);
		}
	}

	@Override
	public void onDrawFrame(boolean firstDraw) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		for (Shape a : staticShapes) {
			a.draw(camera.mViewMatrix, mProjectionMatrix, pointLights);
		}
		for (Shape a : dynamicShapes) {
			a.draw(camera.mViewMatrix, mProjectionMatrix, pointLights);
		}

		synchronized (needUpdate) {
			Iterator<Integer> iter = needUpdate.iterator();
			updateArray = new int[needUpdate.size()];
			int i = 0;
			while (iter.hasNext()) {
				updateArray[i] = iter.next();
				i++;
			}
			needUpdate.clear();
		}

		for (int a : updateArray) {
			updateList(a);
		}
		// Log.d("FPS", super.getFPS() +"");

	}

	private void updateList(int type) {
		switch (type) {
		case World.DYNAMIC_SHAPES:
			dynamicShapes = world.getDynamicShapes();
			createDynamicShapes();
			break;
		case World.POINT_LIGHT:
			//Log.d("Graphics", "keke");
			pointLights = world.getPointLights();
			break;
		case World.STATIC_SHAPES:
			staticShapes = world.getStaticShapes();
			createStaticShapes();
			break;

		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data != null) {
			int type = (int) data;
			synchronized (needUpdate) {
				needUpdate.add(type);
			}
		}

	}

	private void createStaticShapes() {
		Iterator<Shape> iter = staticShapes.iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();
			String[] shaders = shape.getShaders();
			shape.installGraphics(shaders[0], shaders[1]);
		}
	}

	private void createDynamicShapes() {
		Iterator<Shape> iter = dynamicShapes.iterator();
		while (iter.hasNext()) {
			Shape shape = iter.next();
			String[] shaders = shape.getShaders();
			shape.installGraphics(shaders[0], shaders[1]);
		}
	}
}
