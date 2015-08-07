package game.first.levels;

import java.util.List;

import game.first.lightlife.R;
import game.first.mechanics.GetToPawn;
import game.first.pawn.Follower;
import game.first.pawn.Player;
import game.first.props.PythagorasTree;
import game.first.props.Shape;
import game.first.world.World;

public class SecondLevel extends Level {

	public SecondLevel() {
		super(
				R.raw.escape_route,
				"This seemed familiar but something was different,"
						+ " a new force on her right side was present and it made "
						+ "the emitting of more light possible. Was this newfound \"power\" "
						+ "somehow affecting the environment?",
				"ShootSomething");
	}

	@Override
	public Player load() {
		world = new World(100, 100);
		List<Shape> box = createStandardBoundingBox(5, 5, grey);
		for (Shape i : box) {
			world.createStatic(i);
		}
		Player player = new Player(2, 0, white, world, this);
		Follower follow = new Follower(-1, 0, green, player, world);
		objective = new GetToPawn(follow, player, 0.3f);
		box = createDestroyableBox(1, 1, -1, 0, 0.4f, darkBlue);
		for (Shape i : box) {
			world.createStatic(i);
		}
		world.createStatic(new PythagorasTree(-0.2f, -2.5f, 2, darkGreen, 3,
				1f, 10, 30));
		return player;
	}

}
