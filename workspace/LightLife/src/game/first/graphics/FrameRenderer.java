package game.first.graphics;

import android.opengl.GLES20;

public class FrameRenderer extends GLRenderer {

	public volatile float deltaX, deltaY;
	
	public FrameRenderer() {
		super();
	}
	
	@Override
	public void onCreate(int width, int height, boolean contextLost) {
		GLES20.glClearColor(0f, 0f, 0f, 1f);

	}

	@Override
	public void onDrawFrame(boolean firstDraw) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		

	}

}
