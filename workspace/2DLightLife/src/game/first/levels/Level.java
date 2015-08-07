package game.first.levels;

import game.first.math.FloatPoint;
import game.first.mechanics.Objective;
import game.first.pawn.Player;
import game.first.props.Rectangle;
import game.first.props.Shape;
import game.first.world.World;

import java.util.LinkedList;
import java.util.List;

public abstract class Level {

	public static final float[] green = { 0.2f, 1.0f, 0.2f, 1.0f };
	public static final float[] darkGreen = { 0.01f, 0.3f, 0.01f, 1.0f };
	public static final float[] darkGreenRed = { 0.3f, 0.3f, 0.01f, 1.0f };
	public static final float[] red = { 1.0f, 0.2f, 0.2f, 1.0f };
	public static final float[] darkRed = { 0.3f, 0.01f, 0.01f, 1.0f };
	public static final float[] grey = { 0.5f, 0.5f, 0.5f, 1.0f };
	public static final float[] brown = { 0.54f, 0.27f, 0.07f, 1.0f };
	public static final float[] black = { 0.0f, 0.0f, 0.0f, 1.0f };
	public static final float[] white = { 1.0f, 1.0f, 1.0f, 1.0f };
	public static final float[] blue = { 0.2f, 0.2f, 1.0f, 1.0f };
	public static final float[] darkBlue = { 0.1f, 0.1f, 0.6f, 1.0f };

	protected Objective objective;
	protected int musicId;
	protected String startText;
	protected String levelName;
	protected World world;

	public Level(int musicId, String startText, String levelName) {
		this.musicId = musicId;
		this.startText = startText;
		this.levelName = levelName;
	}

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
		world = null;
	}

	protected List<Shape> createStandardBoundingBox(float width, float height,
			float[] backgroundColor) {
		LinkedList<Shape> send = new LinkedList<Shape>();
		send.add(new Rectangle(width + 2, height + 2, backgroundColor, 0, 0, 3,
				false, false));
		send.add(new Rectangle(width + 6, 3, black, 0, -height / 2 - 1.5f, 2,
				true, false));
		send.add(new Rectangle(width + 6, 3, black, 0, height / 2 + 1.5f, 2,
				true, false));
		send.add(new Rectangle(3, height + 1, black, -width / 2 - 1.5f, 0, 2,
				true, false));
		send.add(new Rectangle(3, height + 1, black, width / 2 + 1.5f, 0, 2,
				true, false));
		return send;
	}

	protected List<Shape> createDestroyableBox(float width, float height,
			float x, float y, float wallWidth, float[] color) {
		if (color == null) {
			color = blue;
		}
		LinkedList<Shape> send = new LinkedList<Shape>();
		send.add(new Rectangle(width + 2 * wallWidth, wallWidth, color, x, y
				- (height + wallWidth) / 2, 2, true, true));
		send.add(new Rectangle(width + 2 * wallWidth, wallWidth, color, x, y
				+ (height + wallWidth) / 2, 2, true, true));
		send.add(new Rectangle(wallWidth, height + wallWidth, color, x
				- (width + wallWidth) / 2, y, 2, true, true));
		send.add(new Rectangle(wallWidth, height + wallWidth, color, x
				+ (width + wallWidth) / 2, y, 2, true, true));
		return send;
	}

	/**
	 * 
	 * @param width
	 * @param height
	 * @param holeDir
	 *            (L)eft, (R)ight, (U)p, (D)own
	 * @param x
	 * @param y
	 * @param wallWidth
	 * @param color
	 * @return
	 */
	protected List<Shape> createBoxWithHole(float width, float height,
			char holeDir, float holeWidth, float x, float y, float wallWidth,
			float[] color) {
		if (color == null) {
			color = green;
		}
		LinkedList<Shape> send = new LinkedList<Shape>();
		send.add(new Rectangle(wallWidth, (height - holeWidth) / 2 + 0.01f,
				color, x + (width + wallWidth) / 2, y - (height) / 4
						- holeWidth / 4 - 0.005f, 2, true, false));
		send.add(new Rectangle(wallWidth, (height - holeWidth) / 2 + 0.01f,
				color, x + (width + wallWidth) / 2, y + (height) / 4
						+ holeWidth / 4 + 0.005f, 2, true, false));
		send.add(new Rectangle(wallWidth, height + 0.01f, color, x
				- (width + wallWidth) / 2, y, 2, true, false));
		send.add(new Rectangle(width + wallWidth * 2, wallWidth, color, x, y
				+ (height + wallWidth) / 2, 2, true, false));
		send.add(new Rectangle(width + wallWidth * 2, wallWidth, color, x, y
				- (height + wallWidth) / 2, 2, true, false));

		if (holeDir != 'R') {
			float degree = 0;
			switch (holeDir) {
			case 'D':
				degree = -90;
				break;
			case 'U':
				degree = 90;
				break;
			case 'L':
				degree = 180;
				break;
			}
			for (Shape a : send) {
				a.rotate(degree, x, y);
			}

		}
		// if (holeDir == 'D') {
		// send.add(new Rectangle((width - holeWidth) / 2 + wallWidth,
		// wallWidth, color, x - wallWidth, y - wallWidth, 2, true,
		// false));
		// send.add(new Rectangle((width - holeWidth) / 2 + wallWidth,
		// wallWidth, color, x + (width + holeWidth) / 2, y
		// - wallWidth, 2, true, false));
		//
		// } else {
		// send.add(new Rectangle(width + 2 * wallWidth, wallWidth, color, x
		// - wallWidth, y - wallWidth, 2, true, false));
		// }
		// if (holeDir == 'R') {
		// send.add(new Rectangle(wallWidth, (height - holeWidth) / 2, color,
		// x + width, y, 2, true, false));
		// send.add(new Rectangle(wallWidth, (height - holeWidth) / 2, color,
		// x + width, y + (holeWidth + height) / 2, 2, true, false));
		// } else {
		// send.add(new Rectangle(wallWidth, height + wallWidth, color, x
		// + width, y - wallWidth / 2, 2, true, false));
		// }
		// if (holeDir == 'U') {
		// send.add(new Rectangle((width - holeWidth) / 2 + wallWidth,
		// wallWidth, color, x - wallWidth, y + height, 2, true, false));
		// send.add(new Rectangle((width - holeWidth) / 2 + wallWidth,
		// wallWidth, color, x + (width + holeWidth) / 2, y + height,
		// 2, true, false));
		// } else {
		// send.add(new Rectangle(width + 2 * wallWidth, wallWidth, color, x
		// - wallWidth, y + height, 2, true, false));
		// }
		// if (holeDir == 'L') {
		// send.add(new Rectangle(wallWidth, (height - holeWidth) / 2, color,
		// x - wallWidth, y, 2, true, false));
		// send.add(new Rectangle(wallWidth, (height - holeWidth) / 2, color,
		// x - wallWidth, y + (holeWidth + height) / 2, 2, true, false));
		// } else {
		// send.add(new Rectangle(wallWidth, height + wallWidth, color, x
		// - wallWidth, y - wallWidth / 2, 2, true, false));
		// }

		return send;
	}

}
