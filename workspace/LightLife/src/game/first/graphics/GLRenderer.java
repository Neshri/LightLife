package game.first.graphics;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public abstract class GLRenderer implements Renderer {
	private boolean mFirstDraw;
	private boolean mSurfaceCreated;
	private int mWidth;
	private int mHeight;
	private long mLastTime;
	private int mFPS, FPS;

	/**
	 * Constructor that makes sure there's no accidental null pointers.
	 */
	public GLRenderer() {
		mFirstDraw = true;
		mSurfaceCreated = false;
		mWidth = -1;
		mHeight = -1;
		mLastTime = System.currentTimeMillis();
		mFPS = 0;
		FPS = 0;
	}

	/**
	 * Prepare for a graphical reload of the program upon regaining focus.
	 */
	@Override
	public void onSurfaceCreated(GL10 notUsed, EGLConfig config) {
		mSurfaceCreated = true;
		mWidth = -1;
		mHeight = -1;
		createShapes();
	}

	/**
	 * Registers the dimensions of the device's screen.
	 */
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

		// FPS mätare
		mFPS++;
		long currentTime = System.currentTimeMillis();
		if (currentTime - mLastTime >= 1000) {
			FPS = mFPS;
			mFPS = 0;
			mLastTime = currentTime;
		}

		if (mFirstDraw) {
			mFirstDraw = false;
		}
	}

	/**
	 * 
	 * @return current FPS
	 */
	public int getFPS() {
		return FPS;
	}

	/**
	 * Utility method for debugging OpenGL calls. Provide the name of the call
	 * just after making it:
	 * 
	 * <pre>
	 * mColorHandle = GLES20.glGetUniformLocation(mProgram, &quot;vColor&quot;);
	 * MyGLRenderer.checkGlError(&quot;glGetUniformLocation&quot;);
	 * </pre>
	 * 
	 * If the operation is not successful, the check throws an error.
	 * 
	 * @param glOperation
	 *            - Name of the OpenGL call to check.
	 */
	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e("Renderer", glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	public static int loadShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);
		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}

	public abstract void onCreate(int width, int height, boolean contextLost);

	public abstract void onDrawFrame(boolean firstDraw);

	public abstract void createShapes();
}
