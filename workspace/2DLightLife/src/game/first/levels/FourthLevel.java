package game.first.levels;

import game.first.lightlife.R;
import game.first.math.FloatPoint;
import game.first.pawn.Follower;
import game.first.pawn.Player;
import game.first.pawn.SimplePatroller;
import game.first.props.Rectangle;
import game.first.props.Shape;
import game.first.world.World;

import java.util.List;

public class FourthLevel extends Level {
	
	public FourthLevel() {
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
		Follower follow = new Follower(-1.2f, -2, green, player, world);
		Shape sh = new Rectangle(0.8f, 0.8f, blue, -1.5f, 0, 2, true, true);
		world.createDynamic(new Rectangle(0.5f, 0.5f, blue, -2.1f, -2.1f, 2, true, true));
		
		new SimplePatroller(world, sh, new FloatPoint(-1.5f, 0), new FloatPoint(0.7f, 0), 0.005f);
		return player;
	}

}
