package game.first.lightlife;

import game.first.pawn.Player;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class PlayActivity extends Activity {

	public static final String SAVE_DATA = "SaveData";
	public static final String SETTING_DATA = "SettingData";

	private Display display;
	private PlayView view;
	private Point size;
	private LevelSelector levels;
	private Player player;

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
		mainMenu();
		levels = new LevelSelector();

	}

	/**
	 * Changes the current view into the main menu
	 */
	public void mainMenu() {
		setContentView(R.layout.activity_play);

		final Button newGButton = (Button) findViewById(R.id.newGButton);
		newGButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startNew();
			}
		});

		final Button contButton = (Button) findViewById(R.id.contButton);
		contButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				continueGame();

			}
		});

		final Button optionsButton = (Button) findViewById(R.id.optionsButton);
		optionsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				optionsMenu();

			}
		});

		final Button levelsButton = (Button) findViewById(R.id.levelsButton);
		levelsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				levelsMenu();

			}
		});
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
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mainMenu();

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
				"lastPlayed", "");
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
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mainMenu();

			}
		});

	}

	/**
	 * Prompts the pause menu and pauses the game
	 */
	public void pauseMenu() {
		new PauseMenuDialog();
	}

	/**
	 * Decides what happens when the back button is pressed
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//pauseMenu();
	}

	/**
	 * Is called whenever the activity is shut down or minimized
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (view != null) {
			view.onPause();
		}
		Editor save = getSharedPreferences(SAVE_DATA, 0).edit();
		save.putString("lastPlayed", levels.getLastPlayed());
		save.apply();
	}

	/**
	 * Is called whenever the activity starts or is maximized
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (levels.getLastPlayed() != null) {
			display = getWindowManager().getDefaultDisplay();
			size = new Point();
			display.getSize(size);
			setPlayView();
		}

	}

	private void setPlayView() {
		view = new PlayView(this, player, size);
		setContentView(view);
	}


}
