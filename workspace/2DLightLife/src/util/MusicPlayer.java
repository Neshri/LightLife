package util;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer {

	private MediaPlayer player;
	private Context cont;
	private int currentlyPlayingSong, songPos;
	private float volume;

	public MusicPlayer(Context context) {
		cont = context;
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

	// really pauses? I believe I get calls to the player to start after stop
	// and release, why?
	public void pause() {
		if (player != null) {
			songPos = player.getCurrentPosition();
			player.stop();
			player.release();
		}
	}

	public void playSong(int song) {
		if (currentlyPlayingSong != song) {
			if (player != null) {
				player.stop();
			}
			player = MediaPlayer.create(cont, song);
			player.setLooping(true);
			player.setVolume(volume, volume);
			player.start();
			currentlyPlayingSong = song;
		} else {
			// throws illegal state, solve!
			if (!player.isPlaying()) {
				player = MediaPlayer.create(cont, song);
				player.setLooping(true);
				player.setVolume(volume, volume);
				player.seekTo(songPos);
				player.start();
			}
		}

	}

}
