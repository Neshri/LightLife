package gui;

import android.view.View;
import android.widget.Button;
import game.first.lightlife.PlayActivity;
import game.first.lightlife.R;

public class SilentModePrompt implements CustomView {

	private PlayActivity act;

	public SilentModePrompt(PlayActivity act) {
		this.act = act;
		setAsView();
	}

	@Override
	public void setAsView() {
		act.setContentView(R.layout.ask_sound_off);
		final Button no = (Button) act.findViewById(R.id.sound_no);
		final Button yes = (Button) act.findViewById(R.id.sound_yes);

		no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				act.disableSound();
				act.disableMusic();
				act.mainMenu();
			}
		});

		yes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				act.enableSound();
				act.enableMusic();
				act.mainMenu();
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
