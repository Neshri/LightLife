package game.first.lightlife;

import game.first.pawn.Player;
import gui.CustomView;
import gui.MainMenu;
import gui.OptionsMenu;
import gui.PauseMenuDialog;
import gui.SilentModePrompt;
import util.MusicPlayer;
import util.SoundPlayer;
import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class PlayActivity extends Activity {

	public static final String SAVE_DATA = "SaveData";
	public static final String SETTING_DATA = "SettingData";

	private Display display;
	private Point size;
	private LevelSelector levels;
	private Player player;
	private MusicPlayer musicPlayer;
	private SoundPlayer soundPlayer;
	private boolean withMusic, withSound;
	private OnClickListener backButtonAction;
	private float musicVolume, soundVolume;
	private CustomView currentView;

	/**
	 * Called when the activity is starting
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		levels = new LevelSelector();
		musicVolume = getSharedPreferences(SETTING_DATA, 0).getFloat(
				"musicVolume", 0.5f);
		soundVolume = getSharedPreferences(SETTING_DATA, 0).getFloat(
				"soundVolume", 0.5f);
		musicPlayer = new MusicPlayer(this);
		soundPlayer = new SoundPlayer(this);
		askSound();

	}

	/**
	 * Enables music to play
	 */
	public void enableMusic() {
		withMusic = true;
	}

	/**
	 * Disable the playing of music
	 */
	public void disableMusic() {
		withMusic = false;
	}

	/**
	 * 
	 * @return the current status whether music should be played or not
	 */
	public boolean withMusic() {
		return withMusic;
	}

	/**
	 * 
	 * @return the song preferred to be played in accordance to the current mood
	 */
	public int getPreferredSong() {
		return levels.getLevelSong();
	}

	/**
	 * Enables the playing of sound effects
	 */
	public void enableSound() {
		withSound = true;
	}

	/**
	 * Disables the playing of sound effects
	 */
	public void disableSound() {
		withSound = false;
	}

	/**
	 * 
	 * @return whether sound effects should be played
	 */
	public boolean withSound() {
		return withSound;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Asks the user if the app should be started in silent mode
	 */
	public void askSound() {
		currentView = new SilentModePrompt(this);
		backButtonAction = null;
	}

	/**
	 * Changes the current view into the main menu
	 */
	public void mainMenu() {
		currentView = new MainMenu(this, levels);
		musicPlayer.setVolume(musicVolume);
		if (withMusic) {
			musicPlayer.playSong(levels.getLevelSong());
		}
		backButtonAction = null;
	}

	/**
	 * Starts a new game at the first level
	 */
	public void startNew() {
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		player = levels.loadLevel("TestLevel");
		setPlayView();
	}

	/**
	 * Changes the current view into the options menu
	 */
	public void optionsMenu() {
		currentView = new OptionsMenu(this, musicPlayer, soundPlayer);
		backButtonAction = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainMenu();
			}
		};
	}

	/**
	 * Continues the last played level
	 */
	public void continueGame() {
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		String lastPlayed = getSharedPreferences(SAVE_DATA, 0).getString(
				"lastPlayed", "TestLevel");
		player = levels.loadLevel(lastPlayed);
		setPlayView();
	}

	/**
	 * Changes the current view into the level menu where the player can decide
	 * which level to start
	 */
	public void levelsMenu() {
		setContentView(R.layout.levels_menu);
		final Button backButton = (Button) findViewById(R.id.backButton);
		backButtonAction = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mainMenu();
			}
		};
		backButton.setOnClickListener(backButtonAction);

	}

	/**
	 * Prompts the pause menu and pauses the game
	 */
	public void pauseMenu() {
		PauseMenuDialog pause = new PauseMenuDialog(this);
		pause.show(getFragmentManager(), "Pause");
	}
	
	public void pauseCurrent() {
		currentView.pause();
	}
	
	public void resumeCurrent() {
		currentView.resume();
	}

	/**
	 * Decides what happens when the back button is pressed
	 */
	@Override
	public void onBackPressed() {
		if (backButtonAction == null) {
			onPause();
			super.finish();
			super.onBackPressed();
		} else {
			backButtonAction.onClick(null);
		}
	}

	/**
	 * Is called whenever the activity is shut down or minimized
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Editor save = getSharedPreferences(SAVE_DATA, 0).edit();
		if (levels.getLastPlayed() != null) {
			save.putString("lastPlayed", levels.getLastPlayed());
		}
		save.apply();
		Editor setting = getSharedPreferences(SETTING_DATA, 0).edit();
		setting.putFloat("musicVolume", musicVolume);
		setting.putFloat("soundVolume", soundVolume);
		setting.apply();
		if (currentView != null) {
			currentView.pause();
		}
		musicPlayer.pause();
	}

	/**
	 * Is called whenever the activity starts or is maximized
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (withMusic) {
			musicPlayer.playSong(levels.getLevelSong());
		}
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		currentView.setAsView();
	}

	private void setPlayView() {
		backButtonAction = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pauseMenu();
			}
		};
		currentView = new PlayView(this, player, size);
		if (withMusic) {
			musicPlayer.playSong(levels.getLevelSong());
		}
		currentView.setAsView();
	}

}
