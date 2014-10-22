package game.first.lightlife;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

public class FrameCreator implements Runnable {

	private int width, height;
	private SurfaceHolder surfaceHolder;
	private int[] pixels;
	private Canvas canvas;
	private int red, green, blue;

	public FrameCreator(int width, int height, SurfaceHolder surfaceHolder) {
		this.width = width;
		this.height = height;
		this.surfaceHolder = surfaceHolder;
		pixels = new int[width * height];
		
		red = 0;
		green = 100;
		blue = 200;

	}

	private void paintFrame() {
		canvas = surfaceHolder.lockCanvas();
		for (int i = 0; i < pixels.length; i++) {
			if (red == 256) {
				red = 0;
			}
			if (blue > 255) {
				blue = 0;
			}
			if (green > 255) {
				green = 0;
			}
			pixels[i] = Color.argb(red, red, red, red);
			red++;
			blue += 2;
			green += 3;
		}
		
		sendFrame();

	}
	
	

	private void sendFrame() {
		canvas.drawBitmap(pixels, 0, width, 0, 0, width, height, true, null);
		surfaceHolder.unlockCanvasAndPost(canvas);

	}

	@Override
	public void run() {
		for (;;) {
			paintFrame();
		}
	}

}
