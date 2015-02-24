package game.first.lightlife;

import game.first.graphics.FrameRenderer;
import game.first.pawn.Player;
import util.ErrorHandler;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

public class PlayView extends GLSurfaceView implements ErrorHandler {
	
	private GameLoop looper;
	private Point size;
	public final float[] controlVals = new float[4];

	public PlayView(Context context, Player player, Point size) {
		super(context);
		setEGLContextClientVersion(2);
		setRenderer(new FrameRenderer(player), 1f);
		this.size = size;
		looper = new GameLoop(this, player);
	}

	public PlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void handleError(final ErrorType errorType, final String cause) {
		// Queue on UI thread.
		post(new Runnable() {
			@Override
			public void run() {
				final String text;

				switch (errorType) {
				case BUFFER_CREATION_ERROR:
					text = String.format(
							getContext().getResources().getString(0), cause);
					break;
				default:
					text = String.format(
							getContext().getResources().getString(0), cause);
				}

				Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();

			}
		});
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!looper.isAlive()) {
			looper.start();
		}
		if (event != null) {
			
			int eventaction = event.getAction();

		    switch (eventaction) {
		        case MotionEvent.ACTION_DOWN: 
		        	calculatePointers(event);
		            break;

		        case MotionEvent.ACTION_MOVE:
		            calculatePointers(event);
		            break;

		        case MotionEvent.ACTION_UP:   
		            controlVals[0] = 0f;
		            controlVals[1] = 0f;
		            controlVals[2] = 0f;
		            controlVals[3] = 0f;
		            break;
		    }
			return true;
		} else {
			return super.onTouchEvent(event);
		}
	}

	private void calculatePointers(MotionEvent event) {
		if (event.getPointerCount() > 1) {
			float test = event.getX(0) / size.x;
			if (test < 0.5) {
				controlVals[0] = test * 4f - 1;
				controlVals[1] = 1 - (event.getY(0) / size.y) * 2;
				test = event.getX(1) / size.x;
				if (test > 0.5) {
					controlVals[2] = test * 4 - 3f;
					controlVals[3] = 1 - (event.getY(1) / size.y) * 2;
				} else {
					controlVals[2] = 0f;
					controlVals[3] = 0f;
				}
			} else {
				controlVals[2] = test * 4 - 3f;
				controlVals[3] = 1 - (event.getY(0) / size.y) * 2;
				test = event.getX(1) / size.x;
				if (test < 0.5) {
					controlVals[0] = test * 4f - 1;
					controlVals[1] = 1 - (event.getY(1) / size.y) * 2;
				} else {
					controlVals[0] = 0f;
					controlVals[1] = 0f;
				}
			}

		} else {
			float test = event.getX(0) / size.x;
			if (test < 0.5) {
				controlVals[0] = test * 4f - 1;
				controlVals[1] = 1 - (event.getY(0) / size.y) * 2;
				controlVals[2] = 0f;
				controlVals[3] = 0f;
			} else {
				controlVals[2] = test * 4 - 3f;
				controlVals[3] = 1 - (event.getY(0) / size.y) * 2;
				controlVals[0] = 0f;
				controlVals[1] = 0f;
			}

		}
	}

	@Override
	public void onPause() {
		looper.onPause();
		super.onPause();
	}

	// Hides superclass method.
	public void setRenderer(FrameRenderer renderer, float density) {
		super.setRenderer(renderer);
	}

}