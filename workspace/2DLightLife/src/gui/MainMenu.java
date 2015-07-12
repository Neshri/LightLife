package gui;

import game.first.lightlife.LevelSelector;
import game.first.lightlife.PlayActivity;
import game.first.lightlife.R;
import android.view.View;
import android.widget.Button;

public class MainMenu implements CustomView {

	private LevelSelector levels;
	private PlayActivity act;

	public MainMenu(PlayActivity act, LevelSelector levels) {
		this.act = act;
		this.levels = levels;
		setAsView();
	}

	@Override
	public void setAsView() {
		act.setContentView(R.layout.activity_play);
		levels.stopLevel();

		final Button newGButton = (Button) act.findViewById(R.id.newGButton);
		newGButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				act.startNew();
			}
		});

		final Button contButton = (Button) act.findViewById(R.id.contButton);
		contButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				act.continueGame();

			}
		});

		final Button optionsButton = (Button) act
				.findViewById(R.id.optionsButton);
		optionsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				act.optionsMenu(false);

			}
		});

		final Button levelsButton = (Button) act
				.findViewById(R.id.levelsButton);
		levelsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				act.levelsMenu();

			}
		});

	}

	/**
	 * Not supported
	 * 
	 */
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		setAsView();

	}

}
