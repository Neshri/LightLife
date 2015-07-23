package game.first.levels;

import game.first.lightlife.R;
import game.first.math.FloatPoint;
import game.first.pawn.LinkedMove;
import game.first.pawn.Player;
import game.first.props.Rectangle;
import game.first.props.Shape;
import game.first.world.World;

import java.util.List;

public class ThirdLevel extends Level {

	public ThirdLevel() {
		super(R.raw.escape_route, "", "");
	}

	@Override
	public Player load() {
		world = new World(100, 100);
		List<Shape> box = createStandardBoundingBox(5, 5, grey);
		for (Shape i : box) {
			world.createStatic(i);
		}
		Player player = new Player(2, 0, white, world, this);
		// Follower follow = new Follower(-1.2f, -2, green, player, world);
		// objective = new GetToPawn(follow, player, 0.3f);
		Shape controller = new Rectangle(0.5f, 0.5f, darkGreen, -1, 0, 2, true, false);
		Shape subject = new Rectangle(0.5f, 0.5f, red, 0.5f, 0, 2, true, false);
		new LinkedMove(controller, subject, world, new FloatPoint(0, 1),
				new FloatPoint(-1, 0));

		return player;
	}

}
