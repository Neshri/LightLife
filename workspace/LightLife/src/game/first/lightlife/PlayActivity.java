package game.first.lightlife;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class PlayActivity extends Activity {

	Display display;
	PlayView view;
	Point size;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		view = new PlayView(this, size.x, size.y);
		setContentView(view);
	}
}
