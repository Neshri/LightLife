package game.first.levels;

import game.first.mechanics.Objective;
import game.first.pawn.Player;
import game.first.props.Rectangle;
import game.first.props.Shape;

import java.util.LinkedList;
import java.util.List;

public abstract class Level {

	protected static final float[] green = { 0.2f, 1.0f, 0.2f, 1.0f };
	protected static final float[] red = { 1.0f, 0.2f, 0.2f, 1.0f };
	protected static final float[] grey = { 0.5f, 0.5f, 0.5f, 1.0f };
	protected static final float[] black = { 0.0f, 0.0f, 0.0f, 1.0f };
	protected static final float[] white = { 1.0f, 1.0f, 1.0f, 1.0f };
	protected static final float[] blue = { 0.2f, 0.2f, 1.0f, 1.0f };
	protected Objective objective;
	protected int musicId;
	protected String startText;
	protected String levelName;

	public Objective getObjective() {
		return objective;
	}

	public String getStartText() {
		return startText;
	}

	public int getMusicId() {
		return musicId;
	}

	/**
	 * Loads the assets of the level.
	 * 
	 * @return the player pawn in the created level
	 */
	public abstract Player load();

	/**
	 * Sets the asset variables to null, clearing ram space.
	 */
	public void destroy() {
		objective = null;
		specificDestroy();
	}

	protected abstract void specificDestroy();

	protected List<Shape> createStandardBoundingBox(float width, float height) {
		LinkedList<Shape> send = new LinkedList<Shape>();
		send.add(new Rectangle(width + 2, height + 2, grey, (-width / 2) - 1,
				(-height / 2) - 1, 3, false, false));
		send.add(new Rectangle(width + 6, 3, black, -width / 2 - 3,
				-height / 2 - 3, 2, true, false));
		send.add(new Rectangle(width + 6, 3, black, -width / 2 - 3, height / 2,
				2, true, false));
		send.add(new Rectangle(3, height + 1, black, -width / 2 - 3,
				-height / 2 - 0.5f, 2, true, false));
		send.add(new Rectangle(3, height + 1, black, width / 2,
				-height / 2 - 0.5f, 2, true, false));
		return send;
	}

	protected List<Shape> createDestroyableBox(float width, float height,
			float x, float y, float wallWidth, float[] color) {
		if (color == null) {
			color = green;
		}
		LinkedList<Shape> send = new LinkedList<Shape>();
		send.add(new Rectangle(width + 2 * wallWidth, wallWidth, color, x
				- wallWidth, y - wallWidth, 2, true, true));
		send.add(new Rectangle(width + 2 * wallWidth, wallWidth, color, x
				- wallWidth, y + height, 2, true, true));
		send.add(new Rectangle(wallWidth, height + wallWidth, color, x
				- wallWidth, y - wallWidth / 2, 2, true, true));
		send.add(new Rectangle(wallWidth, height + wallWidth, color, x + width,
				y - wallWidth / 2, 2, true, true));
		return send;
	}

}
