package game.first.lightlife;

import java.util.HashMap;

import game.first.levels.*;

public class LevelSelector {

	private HashMap<String, Level> levels;
	private String levelRunning;

	public LevelSelector() {
		levels = new HashMap<String, Level>();
		levels.put("TestLevel", new TestLevel());

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
		temp.load();
		levelRunning = level;
		return temp.getPlayer();
	}

}
