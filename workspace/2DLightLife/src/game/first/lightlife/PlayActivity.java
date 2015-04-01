package game.first.lightlife;

import game.first.pawn.Player;
import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

public class PlayActivity extends Activity {

	public static final String SAVE_DATA = "SaveData";
	public static final String SETTING_DATA = "SettingData";

	private Display display;
	private PlayView view;
	private Point size;
	private LevelSelector levels;
	private Player player;
	private MediaPlayer musicPlayer;
	private Boolean withMusic, withSound, inMainMenu;
	private OnClickListener backButtonAction;
	private float musicVolume, soundVolume;

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
		askSound();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		display = getWindowManager().getDefaultDisplay();
		size = new Point();
		display.getSize(size);
		if (player != null) {
			setPlayView();
		}
		super.onConfigurationChanged(newConfig);
	}

	public void askSound() {
		setContentView(R.layout.ask_sound_off);
		inMainMenu = true;
		final Button no = (Button) findViewById(R.id.sound_no);
		final Button yes = (Button) findViewById(R.id.sound_yes);

		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				withMusic = false;
				withSound = false;
				mainMenu();
			}
		});

		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				withMusic = true;
				withSound = true;
				mainMenu();
			}
		});
	}

	/**
	 * Changes the current view into the main menu
	 */
	public void mainMenu() {
		setContentView(R.layout.activity_play);
		if (withMusic) {
			createAndPlayMainMusic();
		}
		inMainMenu = true;

		final Button newGButton = (Button) findViewById(R.id.newGButton);
		newGButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				inMainMenu = false;
				startNew();
			}
		});

		final Button contButton = (Button) findViewById(R.id.contButton);
		contButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				inMainMenu = false;
				continueGame();

			}
		});

		final Button optionsButton = (Button) findViewById(R.id.optionsButton);
		optionsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				inMainMenu = false;
				optionsMenu();

			}
		});

		final Button levelsButton = (Button) findViewById(R.id.levelsButton);
		levelsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				inMainMenu = false;
				levelsMenu();

			}
		});
	}

	private void createAndPlayMainMusic() {
		if (musicPlayer == null) {
			musicPlayer = MediaPlayer.create(this, R.raw.and_the_faded_notes_play);
		}
		musicPlayer.setVolume(musicVolume, musicVolume);
		musicPlayer.setLooping(true);
		musicPlayer.start();
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
		setContentView(R.layout.options_menu);
		final Button backButton = (Button) findViewById(R.id.backButton);
		backButtonAction = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mainMenu();
			}
		};
		backButton.setOnClickListener(backButtonAction);

		final SeekBar musicVolumeBar = (SeekBar) findViewById(R.id.music_volume_bar);
		musicVolumeBar.setMax(1000);
		if (withMusic) {
			musicVolumeBar.setProgress((int) (musicVolumeBar.getMax() * musicVolume));
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
						if (musicPlayer != null) {
							musicVolume = (float) progress
									/ (float) seekBar.getMax();
							withMusic = true;
							musicPlayer.setVolume(musicVolume, musicVolume);
							musicPlayer.start();
						} else {
							musicVolume = (float) progress
									/ (float) seekBar.getMax();
							withMusic = true;
							createAndPlayMainMusic();
						}
						if (progress == 0) {
							musicPlayer.pause();
							withMusic = false;
						}
					}
				});
		
		final SeekBar soundVolumeBar = (SeekBar) findViewById(R.id.sound_volume_bar);
		soundVolumeBar.setMax(1000);
		if (withSound) {
			soundVolumeBar.setProgress((int) (soundVolumeBar.getMax() * soundVolume));
		} else {
			soundVolumeBar.setProgress(0);
		}
		
		soundVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		

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
		PauseMenuDialog pause = new PauseMenuDialog(view, this);
		pause.show(getFragmentManager(), "Pause");
	}

	/**
	 * Decides what happens when the back button is pressed
	 */
	@Override
	public void onBackPressed() {
		if (inMainMenu) {
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
		if (view != null) {
			view.onPause();
		}
		if (musicPlayer != null) {
			musicPlayer.pause();
		}

	}

	/**
	 * Is called whenever the activity starts or is maximized
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (musicPlayer != null && withMusic) {
			musicPlayer.start();
		}
		if (levels.getLastPlayed() != null) {
			display = getWindowManager().getDefaultDisplay();
			size = new Point();
			display.getSize(size);
			setPlayView();
		}

	}

	private void setPlayView() {
		backButtonAction = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pauseMenu();
			}
		};
		view = new PlayView(this, player, size);
		setContentView(view);
	}

}
