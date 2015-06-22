package game.first.levels;

import game.first.lightlife.R;
import game.first.mechanics.GetToPawn;
import game.first.pawn.Follower;
import game.first.pawn.Player;
import game.first.props.PythagorasTree;
import game.first.props.Shape;
import game.first.world.World;

import java.util.List;

public class FirstLevel extends Level{
	
	private World world;

	public FirstLevel() {
		musicId = R.raw.escape_route;
		startText = "Catch it!";
		levelName = "BabySteps";
	}
	
	@Override
	public Player load() {
		world = new World(1000, 1000);
		List<Shape> box = createStandardBoundingBox(5, 5);
		for (Shape i : box) {
			world.createStatic(i);
		}
		Player player = new Player(2, 0, world, this);
		Follower follow = new Follower(-1.5f, 0, player, world);
		objective = new GetToPawn(follow, player, 0.3f);
		box = createDestroyableBox(1, 1, -2.1f, -0.3f, 0.4f, blue);
		for (Shape i : box) {
			world.createStatic(i);
		}
		world.createStatic(new PythagorasTree(-1, -2.5f, 2, green, 3, 1f, 10, 30));
		return player;
	}

	@Override
	protected void specificDestroy() {
		world = null;
		
	}

}
