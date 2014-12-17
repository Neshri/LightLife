package game.first.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public abstract class GLRenderer implements Renderer {
	private boolean mFirstDraw;
	private boolean mSurfaceCreated;
	private int mWidth;
	private int mHeight;
	private long mLastTime;
	private int mFPS;

	public GLRenderer() {
		mFirstDraw = true;
		mSurfaceCreated = false;
		mWidth = -1;
		mHeight = -1;
		mLastTime = System.currentTimeMillis();
		mFPS = 0;
	}

	@Override
	public void onSurfaceCreated(GL10 notUsed, EGLConfig config) {
		mSurfaceCreated = true;
		mWidth = -1;
		mHeight = -1;
	}

	@Override
	public void onSurfaceChanged(GL10 notUsed, int width, int height) {
		if (!mSurfaceCreated && width == mWidth && height == mHeight) {
			// debug message
			return;
		}
		mWidth = width;
		mHeight = height;
		onCreate(mWidth, mHeight, mSurfaceCreated);
		mSurfaceCreated = false;
	}

	@Override
	public void onDrawFrame(GL10 notUsed) {
		onDrawFrame(mFirstDraw);

		mFPS++;
		long currentTime = System.currentTimeMillis();
		if (currentTime - mLastTime >= 1000) {
			mFPS = 0;
			mLastTime = currentTime;
		}

		if (mFirstDraw) {
			mFirstDraw = false;
		}
	}

	public int getFPS() {
		return mFPS;
	}

	public abstract void onCreate(int width, int height, boolean contextLost);

	public abstract void onDrawFrame(boolean firstDraw);
}
