package game.first.levels;

import game.first.lightlife.R;
import game.first.mechanics.Objective;
import game.first.pawn.Player;
import game.first.props.PythagorasTree;
import game.first.props.Rectangle;
import game.first.props.Shape;
import game.first.props.SymmetricPolygon;
import game.first.props.Triangle;
import game.first.world.World;

import java.util.List;

import util.InvalidFormatException;

public class TestLevel extends Level {

	private World world;
	
	
	public TestLevel() {
		musicId = R.raw.escape_route;
		startText = "Catch it!";
		levelName = "Test Level";
	}
	
	public void specificDestroy() {
		world = null;
	}
	
	@Override
	public Player load() {
		world = new World(1000, 1000);
		try {
			objective = null;
			world.createStatic(new Triangle(3, 3, black, 2, 0, 2, true, true));
			world.createStatic(new Rectangle(2, 2, black, -2.5f, -1, 2, true, true));
			world.createStatic(new Rectangle(2, 2, black, 3, 2, 2, true, true));
			world.createStatic(new SymmetricPolygon(8, 2, red, 2, -2, 2, true, true));
			world.createStatic(new Rectangle(2, 2, green, -1, -1, 3, false, true));
			world.createStatic(new PythagorasTree(-3, -3, 3, black, 2, 0.01f, 10, 45));
			world.createStatic(new PythagorasTree(-6, -3, 3, black, 2, 0.01f, 10, 20));
			world.createStatic(new PythagorasTree(-9, -3, 3, black, 2, 0.01f, 10, 30));
			world.createStatic(new PythagorasTree(-12, -3, 3, green, 2, 0.01f, 10, 60));
			world.createStatic(new PythagorasTree(-15, -3, 3, green, 2, 0.01f, 10, 10));
			world.createStatic(new Rectangle(15, 2, black, -15, -5, 2, true, false));
			float[] p1 = {-2f, 0f, 2.5f};
			float[] p2 = {2f, 0f, 2f};
			
			//world.addLight(new PointLight(-3,-1,2,5f));
			List<Shape> box = createStandardBoundingBox(35, 35);
			for (Shape i : box) {
				world.createStatic(i);
			}
			
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Player(0, 0, world, this);
	}
}
