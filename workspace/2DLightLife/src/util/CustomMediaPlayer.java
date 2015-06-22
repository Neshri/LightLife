package util;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public abstract class CustomMediaPlayer {

	protected float volume;
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
			try {
				player.setVolume(value, value);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	public float getVolume() {
		return volume;
	}
	
	protected static class MediaError implements MediaPlayer.OnErrorListener {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			return false;
		}
		
	}

}
