package gui;

import game.first.lightlife.PlayActivity;
import game.first.lightlife.R;
import android.view.View;
import android.widget.Button;

public class LevelsMenu implements CustomView {

	private PlayActivity act;

	public LevelsMenu(PlayActivity act) {
		this.act = act;
		setAsView();
	}

	@Override
	public void setAsView() {
		act.setContentView(R.layout.levels_menu);
		installButton(R.id.levelButton1, "FirstLevel");
		installButton(R.id.levelButton2, "SecondLevel");
		installButton(R.id.levelButton3, "ThirdLevel");
		installButton(R.id.levelButton4, "FourthLevel");
		
	}

	private void installButton(int id, String level) {
		final Button level1 = (Button) act.findViewById(id);
		final String lev = level;
		level1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				act.startLevel(lev);
			}
		});
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
