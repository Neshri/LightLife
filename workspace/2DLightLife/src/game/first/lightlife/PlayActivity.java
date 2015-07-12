package game.first.lightlife;

import game.first.pawn.Player;
import gui.CustomView;
import gui.LevelEndedDialog;
import gui.LevelsMenu;
import gui.MainMenu;
import gui.OptionsMenu;
import gui.PauseMenuDialog;
import gui.SilentModePrompt;
import util.MusicPlayer;
import util.SoundPlayer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

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
	private CustomView currentView;
	private TextView textPrompt;

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
		musicPlayer = new MusicPlayer(this, levels.getLevelSong());
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
		if (withMusic) {
			musicPlayer.playSong(levels.getLevelSong(), 0);
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
		player = levels.loadLevel("FirstLevel");
		setPlayView();
	}

	/**
	 * Changes the current view into the options menu
	 */
	public void optionsMenu(boolean inGame) {
		currentView = new OptionsMenu(this, musicPlayer, soundPlayer);
		if (inGame) {
			backButtonAction = new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					setPlayView();
				}
			};
		} else {
			backButtonAction = new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mainMenu();
				}
			};
		}

	}

	/**
	 * Continues the last played level
	 */
	public void continueGame() {
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		String lastPlayed = getSharedPreferences(SAVE_DATA, 0).getString(
				"lastPlayed", "FirstLevel");
		player = levels.loadLevel(lastPlayed);
		setPlayView();
	}

	/**
	 * Changes the current view into the level menu where the player can decide
	 * which level to start
	 */
	public void levelsMenu() {
		currentView = new LevelsMenu(this);
		backButtonAction = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mainMenu();
			}
		};
	}

	/**
	 * Prompts the pause menu and pauses the game
	 */
	public void pauseMenu() {
		PauseMenuDialog pause = new PauseMenuDialog(this);
		pause.show(getFragmentManager(), "Pause");
	}

	public void objectiveSuccess() {
		LevelEndedDialog end = new LevelEndedDialog(this);
		end.show(getFragmentManager(), "End");
	}

	public void startLevel(String level) {
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		player = levels.loadLevel(level);
		if (player != null) {
			setPlayView();
		}
	}

	public void startNextLevel() {
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		player = levels.loadNextLevel();
		if (player == null) {
			mainMenu();
		} else {
			setPlayView();
		}
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
		setting.putFloat("musicVolume", musicPlayer.getVolume());
		setting.putFloat("soundVolume", soundPlayer.getVolume());
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
		SharedPreferences setting = getSharedPreferences(SETTING_DATA, 0);
		musicPlayer.setVolume(setting.getFloat("musicVolume", 0.5f));
		soundPlayer.setVolume(setting.getFloat("soundVolume", 0.5f));
		if (withMusic) {
			musicPlayer.resume();
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
			musicPlayer.playSong(levels.getLevelSong(), 0);
		}
		currentView.setAsView();
		setPromptText(player.getLevel().getStartText());
	}

	public void destroyPrompt() {
		((ViewGroup) textPrompt.getParent()).removeView(textPrompt);
	}

	public void setPromptText(String str) {
		textPrompt = new TextView(this);
		textPrompt.setBackgroundColor(0x9F000000);
		textPrompt.setTextColor(0xFFFFFFFF);
		float txtSize = (10 * size.x / size.y);
		textPrompt.setTextSize(txtSize);
		textPrompt.setText(str);
		this.addContentView((View) textPrompt, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		textPrompt.setPadding(50, 0, 50, 0);
		textPrompt.setGravity(android.view.Gravity.CENTER);
		textPrompt.setVisibility(View.VISIBLE);
	}

}
