package game.first.lightlife;

import android.content.Context;
import android.graphics.Canvas;
import android.view.*;

public class PlayView extends SurfaceView implements SurfaceHolder.Callback{

	private FrameCreator frameCre;
	
	public PlayView(Context context, int width, int height) {
		super(context);
		getHolder().addCallback(this);
		frameCre = new FrameCreator(width, height, getHolder());	
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(frameCre).start();
		
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		
	}


	
	

}
