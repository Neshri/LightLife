package game.first.lightlife;

import java.util.HashMap;

import game.first.levels.*;
import game.first.pawn.Player;

public class LevelSelector {

	private HashMap<String, Level> levels;
	private String levelRunning;

	public LevelSelector() {
		levels = new HashMap<String, Level>();
		levels.put("TestLevel", new TestLevel());

	}
	
	public void stopLevel() {
		levelRunning = null;
	}
	
	public Level getLevel(String level) {
		return levels.get(level);
	}
	
	public int getLevelSong() {
		if (levelRunning == null) {
			return R.raw.and_the_faded_notes_play;
		} else {
			return levels.get(levelRunning).getMusicId();
		}
	}

	/**
	 * Returns the level currently being played or the last level played since
	 * activity start
	 * 
	 * @return
	 */
	public String getLastPlayed() {
		return levelRunning;
	}

	/**
	 * Loads the requested level and returns the player for the level, if no
	 * level is found for the given name returns null
	 * 
	 * @param level
	 * @return Player
	 */
	public Player loadLevel(String level) {
		Level temp = levels.get(level);
		if (temp == null) {
			return null;
		}
		levelRunning = level;
		return temp.load();
	}

}
