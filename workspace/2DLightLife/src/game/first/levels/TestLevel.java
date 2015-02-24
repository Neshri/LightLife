package game.first.levels;

import game.first.pawn.Player;
import game.first.props.Rectangle;
import game.first.props.Triangle;
import game.first.world.World;
import util.InvalidFormatException;

public class TestLevel implements Level {

	private World world;


	@Override
	public Player load() {
		world = new World(100, 100);
		float[] green = {0.5f, 0.9f, 0.2f, 1.0f};
		float[] red = {1.0f, 0.1f, 0.1f, 1.0f};
		try {
			world.createStatic(new Triangle(3, 3, red, 2, 0, 2, true));
			world.createStatic(new Rectangle(2,2,red,-2.5f,-1,2, true));
			world.createStatic(new Rectangle(2,2,red,3,2,2, true));
			world.createStatic(new Rectangle(2,2,green,-1,0,3, false));
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Player(0, 0, world);
	}

}
