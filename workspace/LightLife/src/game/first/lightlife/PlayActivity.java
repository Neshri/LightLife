package game.first.lightlife;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class PlayActivity extends Activity {

	Display display;
	PlayView view;
	Point size;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		view = new PlayView(this, size.x, size.y);
		setContentView(view);
	}
}
