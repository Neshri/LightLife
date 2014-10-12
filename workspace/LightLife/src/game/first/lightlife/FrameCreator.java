package game.first.lightlife;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

public class FrameCreator implements Runnable {

	private int width, height;
	private SurfaceHolder surfaceHolder;
	private static int[] pixels;
	private Thread calc1, calc2;
	private Canvas canvas;
	private boolean sendReady;

	public FrameCreator(int width, int height, SurfaceHolder surfaceHolder) {
		this.width = width;
		this.height = height;
		this.surfaceHolder = surfaceHolder;
		pixels = new int[width * height];
		sendReady = false;
		calc1 = new Thread(new CellCalculator(true));
		calc2 = new Thread(new CellCalculator(false));
	}

	private void paintFrame() {
		canvas = surfaceHolder.lockCanvas();
		

	}

	private void sendFrame() {
		if (!sendReady) {
			sendReady = true;
		} else {
			canvas.drawBitmap(pixels, 0, width, 0, 0, width, height, true, null);
			surfaceHolder.unlockCanvasAndPost(canvas);
			sendReady = false;
		}
	}

	@Override
	public void run() {
		calc1.start();
		calc2.start();
		for (;;) {
			paintFrame();
		}
	}

	private class CellCalculator implements Runnable {

		private boolean leftSide;
		private int halfArray;

		public CellCalculator(boolean leftSide) {
			this.leftSide = leftSide;
			halfArray = pixels.length / 2;

		}

		public void calculate() {
			if (leftSide) {
				for (int i = 0; i < halfArray; i++) {
					pixels[i] = Color.WHITE;

				}
				// Do Stuff
				sendFrame();
			} else {
				for (int i = halfArray; i < pixels.length; i++) {
					pixels[i] = Color.BLACK;

				}
				// Do Stuff
				sendFrame();
			}
		}

		@Override
		public void run() {
			calculate();
		}
	}
}
