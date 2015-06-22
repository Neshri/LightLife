package game.first.lightlife;

import game.first.levels.FirstLevel;
import game.first.levels.Level;
import game.first.levels.TestLevel;
import game.first.mechanics.Objective;
import game.first.pawn.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class LevelSelector {

	private HashMap<String, Level> levels;
	private String levelRunning;
	private Objective currentObjective;
	private ArrayList<String> levelOrder;

	public LevelSelector() {
		levels = new HashMap<String, Level>();
		levelOrder = new ArrayList<String>();
		levels.put("TestLevel", new TestLevel());
		levelOrder.add("TestLevel");
		levels.put("FirstLevel", new FirstLevel());
		levelOrder.add("FirstLevel");
	}
	
	
	
	public void stopLevel() {
		if (levelRunning == null) {
			return;
		}
		Level temp = levels.get(levelRunning);
		temp.destroy();
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
	
	public Objective getCurrentObjective() {
		return currentObjective;
	}
	
	public Player loadNextLevel() {
		if (levelRunning == null) {
			return null;
		}
		int next = 0;
		for (int i = 0; i < levelOrder.size(); i++) {
			if (levelOrder.get(i).equals(levelRunning)) {
				next = i + 1;
				break;
			}
		}
		stopLevel();
		Level temp = levels.get(levelOrder.get(next));
		Player player = temp.load();
		currentObjective = temp.getObjective();
		levelRunning = levelOrder.get(next);
		return player;
	}
	
	

	/**
	 * Loads the requested level and returns the player for the level, if no
	 * level is found for the given name returns null
	 * 
	 * @param level
	 * @return Player
	 */
	public Player loadLevel(String level) {
		stopLevel();
		Level temp = levels.get(level);
		if (temp == null) {
			return null;
		}
		Player player = temp.load();
		currentObjective = temp.getObjective();
		levelRunning = level;
		return player;
	}

}
