package gui;

import game.first.lightlife.R;
import android.app.Activity;

public class LevelsMenu implements CustomView {

	private Activity act;

	public LevelsMenu(Activity act) {
		this.act = act;
		setAsView();
	}

	@Override
	public void setAsView() {
		act.setContentView(R.layout.levels_menu);
		
	}

	
	/**
	 * Not Supported
	 */
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		setAsView();
	}

}
