package game.first.levels;

import game.first.lightlife.R;
import game.first.mechanics.GetToPawn;
import game.first.pawn.Follower;
import game.first.pawn.Player;
import game.first.props.PythagorasTree;
import game.first.props.Shape;
import game.first.world.World;

import java.util.List;

public class FirstLevel extends Level {

	private World world;

	public FirstLevel() {
		super(
				R.raw.escape_route,
				"Her name was Snow, that was all she remembered. A force from her left seemed to be able to guide her, a second thought was that of a word, green, it was soothing she thought.",
				"BabySteps");
	}

	@Override
	public Player load() {
		world = new World(100, 100);
		List<Shape> box = createStandardBoundingBox(5, 5, grey);
		for (Shape i : box) {
			world.createStatic(i);
		}
		Player player = new Player(2, 0, white, world, this);
		Follower follow = new Follower(-1f, 0, green, player, world);
		objective = new GetToPawn(follow, player, 0.3f);
		box = createDestroyableBox(1, 1, -1f, 0, 0.4f, red);
		int count = 0;
		for (Shape i : box) {
			count++;
			if (count != 3) {
				world.createStatic(i);
			}
		}
		world.createStatic(new PythagorasTree(-0.2f, -2.5f, 2, darkGreen, 3,
				1f, 10, 30));
		player.setCannon(false);
		return player;
	}


}
