package util;

import android.content.Context;
import android.media.MediaPlayer;

public abstract class CustomMediaPlayer {
	
	private float volume;
	protected MediaPlayer player;
	protected Context cont;
	
	public CustomMediaPlayer(Context cont) {
		this.cont = cont;
	}
	
	public void setVolume(float value) {
		if (value < 0) {
			value = 0;
		} else if (value > 1) {
			value = 1;
		}
		volume = value;
		if (player != null) {

			player.setVolume(value, value);
		}
	}
	
	public float getVolume() {
		return volume;
	}

}
