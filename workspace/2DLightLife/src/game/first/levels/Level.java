package game.first.levels;

import java.util.LinkedList;
import java.util.List;

import game.first.pawn.Player;
import game.first.props.Rectangle;
import game.first.props.Shape;

public abstract class Level {
	protected static final float[] green = { 0.5f, 0.9f, 0.2f, 1.0f };
	protected static final float[] red = { 1.0f, 0.1f, 0.1f, 1.0f };
	protected static final float[] grey = { 0.5f, 0.5f, 0.5f, 1.0f };
	protected static final float[] black = { 0.0f, 0.0f, 0.0f, 1.0f };

	public abstract Player load();

	protected List<Shape> createStandardBoundingBox(float width, float height) {
		LinkedList<Shape> send = new LinkedList<Shape>();
		send.add(new Rectangle(width, height, grey, -width / 2, -height / 2, 3, false, false));
		send.add(new Rectangle(width, 2, black, -width / 2, -height / 2-1, 2, true, false));
		send.add(new Rectangle(width, 2, black, -width / 2, height / 2-1, 2, true, false));
		send.add(new Rectangle(2, height, black, -width / 2-1, -height / 2, 2, true, false));
		send.add(new Rectangle(2, height, black, width / 2-1, -height / 2, 2, true, false));
		return send;
	}

}
