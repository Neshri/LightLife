package util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

public class MusicPlayer extends CustomMediaPlayer {

	private int currentlyPlayingSong, songPos;

	public MusicPlayer(Context context, int defaultSong) {
		super(context);
		currentlyPlayingSong = defaultSong;
	}

	public void pause() {
		if (player != null) {
			try {
				songPos = player.getCurrentPosition();
			} catch (IllegalStateException e) {
				songPos = 0;
			}
			stop(player);
			player = null;
		}
	}

	public void resume() {
		playSong(currentlyPlayingSong, songPos);
	}

	public void playSong(int song, int songPos) {
		if (currentlyPlayingSong != song) {
			if (player != null) {
				stop(player);
			}
			player = MediaPlayer.create(cont, song);
			player.setOnErrorListener(new MediaError());
			if (player == null) {
				return;
			}
			this.songPos = songPos;
			start(player);
			currentlyPlayingSong = song;
		} else {
			if (player == null) {
				player = MediaPlayer.create(cont, song);
				player.setOnErrorListener(new MediaError());
				start(player);
				return;
			}
			try {
				if (player.isPlaying()) {
					return;
				}
			} catch (IllegalStateException e) {
				player = MediaPlayer.create(cont, song);
				player.setOnErrorListener(new MediaError());
				player.setVolume(volume, volume);
				player.setLooping(true);
				player.start();
			}
		}
	}

	private void stop(MediaPlayer mp) {
		mp.setLooping(false);
		mp.stop();
		mp.release();
	}

	private void start(MediaPlayer mp) {
		mp.setLooping(true);
		mp.setVolume(volume, volume);
		mp.seekTo(songPos);
		mp.start();
	}
}
