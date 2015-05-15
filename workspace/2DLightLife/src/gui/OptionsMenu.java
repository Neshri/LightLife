package gui;

import game.first.lightlife.PlayActivity;
import game.first.lightlife.R;
import util.MusicPlayer;
import util.SoundPlayer;
import android.widget.SeekBar;

public class OptionsMenu implements CustomView {

	private PlayActivity act;
	private MusicPlayer musicPlayer;
	private SoundPlayer soundPlayer;

	public OptionsMenu(PlayActivity act, MusicPlayer musicPlayer,
			SoundPlayer soundPlayer) {
		this.act = act;
		this.musicPlayer = musicPlayer;
		this.soundPlayer = soundPlayer;

		setAsView();
	}

	@Override
	public void setAsView() {
		act.setContentView(R.layout.options_menu);
		// final Button backButton = (Button) act.findViewById(R.id.backButton);
		// backButtonAction = new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// mainMenu();
		// }
		// };
		// backButton.setOnClickListener(backButtonAction);

		final SeekBar musicVolumeBar = (SeekBar) act
				.findViewById(R.id.music_volume_bar);
		musicVolumeBar.setMax(1000);
		if (act.withMusic()) {
			musicVolumeBar
					.setProgress((int) (musicVolumeBar.getMax() * musicPlayer
							.getVolume()));
		} else {
			musicVolumeBar.setProgress(0);
		}

		musicVolumeBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						float musicVolume = (float) progress
								/ (float) seekBar.getMax();
						act.enableMusic();
						musicPlayer.setVolume(musicVolume);
						musicPlayer.playSong(act.getPreferredSong());

						if (progress == 0) {
							musicPlayer.pause();
							act.disableMusic();
						}
					}
				});

		final SeekBar soundVolumeBar = (SeekBar) act
				.findViewById(R.id.sound_volume_bar);
		soundVolumeBar.setMax(1000);
		if (act.withSound()) {
			int volume = (int) (soundVolumeBar.getMax() * soundPlayer
					.getVolume());
			soundVolumeBar.setProgress(volume);
		} else {
			soundVolumeBar.setProgress(0);
		}
		soundVolumeBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						
						float volume = (float) progress
								/ (float) seekBar.getMax();
						soundPlayer.setVolume(volume);
						act.enableSound();
						if (progress == 0) {
							act.disableSound();
						}
					}
				});

	}

	/**
	 * Not supported
	 * (does nothing)
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		setAsView();

	}

}
