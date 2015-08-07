package game.first.levels;

import game.first.pawn.Player;
import game.first.props.Rectangle;
import game.first.props.Shape;
import game.first.props.ShapeSlave;
import game.first.world.World;

import java.util.List;

public class FifthLevel extends Level {
	
	public FifthLevel() {
		super(0, "", "");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Player load() {
		world = new World(100, 100);
		List<Shape> box = createStandardBoundingBox(5, 5, grey);
		for (Shape i : box) {
			world.createStatic(i);
		}
		Player player = new Player(2, 0, white, world, this);
		Shape rect = new Rectangle(1, 1, red, -1, 1, 2, true, false);
		//rect.rotate(90, 1, -1);
		
		rect.scale(0.5f, 0.5f, -1, 1);
		world.createStatic(rect);
		world.createDynamic(new Rectangle(0.05f, 0.05f, green, -1, 1, 2, true, false));
		world.createStatic(new ShapeSlave(new Rectangle(1, 1, red, 1, 1, 2, false, false)));
		world.createStatic(new Rectangle(0.1f, 0.1f, red, 0, 0, 2, true, false));
		return player;
	}

}
