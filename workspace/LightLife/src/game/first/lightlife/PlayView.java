package game.first.lightlife;


import util.ErrorHandler;
import game.first.graphics.FrameRenderer;
import game.first.pawn.Controller;
import game.first.pawn.Player;
import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

public class PlayView extends GLSurfaceView implements ErrorHandler {
	private FrameRenderer renderer;
	private Controller control;

	private Point size;

	public PlayView(Context context, Player player,
			Point size) {
		super(context);
		control = player.getController();
		setEGLContextClientVersion(2);
		setRenderer(new FrameRenderer(player), 1f);
		this.size = size;
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event != null) {
			float alphaX, alphaY, betaX, betaY;
			if (event.getPointerCount() > 1) {
				float test = event.getX(0) / size.x;
				if (test < 0.5) {
					alphaX = test * 4f - 1;
					alphaY = 1 - (event.getY(0) / size.y) * 2;
					test = event.getX(1) / size.x;
					if (test > 0.5) {
						betaX = test * 4 - 3f;
						betaY = 1 - (event.getY(1) / size.y) * 2;
					} else {
						betaX = 0f;
						betaY = 0f;
					}
				} else {
					betaX = test * 4 - 3f;
					betaY = 1 - (event.getY(0) / size.y) * 2;
					test = event.getX(1) / size.x;
					if (test < 0.5) {
						alphaX = test * 4f - 1;
						alphaY = 1 - (event.getY(1) / size.y) * 2;
					} else {
						alphaX = 0f;
						alphaY = 0f;
					}
				}

			} else {
				float test = event.getX(0) / size.x;
				if (test < 0.5) {
					alphaX = test * 4f - 1;
					alphaY = 1 - (event.getY(0) / size.y) * 2;
					betaX = 0f;
					betaY = 0f;
				} else {
					betaX = test * 4 - 3f;
					betaY = 1 - (event.getY(0) / size.y) * 2;
					alphaX = 0f;
					alphaY = 0f;
				}

			}

			control.update(alphaX, alphaY, betaX, betaY);
			// renderer.translate(0, 0, -0.1f);
			return true;
		} else {
			return super.onTouchEvent(event);
		}
	}

	// Hides superclass method.
	public void setRenderer(FrameRenderer renderer, float density) {
		this.renderer = renderer;
		super.setRenderer(renderer);
	}

}
