package game.first.levels;
import util.InvalidFormatException;
import game.first.math.Vect3;
import game.first.pawn.Player;
import game.first.props.Triangle;
import game.first.world.*;
public class TestLevel implements Level {
	
	private World world;
	
	public TestLevel() {
		
	}

	@Override
	public Player load() {
		world = new World(100, 100);
		float[] color = {0.5f, 0.7f, 0.2f, 1f};
		try {
			world.createStatic(new Triangle(1f,1.118f,color,new Vect3(-1f,0f,1f)));
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return new Player(new Vect3(0,0,0), new Vect3(0,0,1), world);
	}



}
