package game.first.lightlife;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import game.first.math.FloatPoint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.view.SurfaceHolder;

public class FrameCreator implements Renderer {

	private int width, height;
	private SurfaceHolder surfaceHolder;
	private int[] pixels;
	private Canvas canvas;
	private Player player;
	private volatile boolean shouldSleep;
	
	
	private FloatPoint[] fov;


	/**
	 * Creates a class which calculates the frames for the game with the help of
	 * the player class
	 * 
	 * @param width
	 * @param height
	 * @param surfaceHolder
	 * @param player
	 */
	public FrameCreator(int width, int height, SurfaceHolder surfaceHolder,
			Player player) {
		this.width = width;
		this.height = height;
		this.surfaceHolder = surfaceHolder;
		this.player = player;
		pixels = new int[width * height];
		shouldSleep = false;
		
		
		fov = new FloatPoint[width * height];
		float xPlus = (float) (Math.PI / height);
		float yPlus = (float) (Math.PI / width);
		float x = (float) Math.PI / 2;
		float y = (float) Math.PI;
		x += xPlus;
		for (int i = 0; i < fov.length; i++) {
			if (i % width == 0) {
				x -= xPlus;
				y = (float) Math.PI;
			}
			fov[i] = new FloatPoint(x, y);
			y -= yPlus;	
		}

		

		

	}

	/**
	 * Tells the thread to not begin any new calculations until it gets notified
	 * 
	 * @throws InterruptedException
	 */
	public void pauseThread() throws InterruptedException {
		shouldSleep = true;
	}

	
	private void paintFrame() {
		if (shouldSleep) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		canvas = surfaceHolder.lockCanvas();
		player.updateObjectList();
		int value = 0;
		for (int i = 0; i < pixels.length; i++) {
			value = 255 - player.distToColli(fov[i].getY(), fov[i].getX());
			if (value < 0) {
				value = 0;
			}
			pixels[i] = Color.argb(255, value, value, value);
		}

		sendFrame();

	}

	private void sendFrame() {
		canvas.drawBitmap(pixels, 0, width, 0, 0, width, height, true, null);
		surfaceHolder.unlockCanvasAndPost(canvas);

	}

	

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
	}

}
