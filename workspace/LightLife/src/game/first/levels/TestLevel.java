package game.first.levels;
import game.first.lightlife.Player;
import game.first.math.Vect3;
import game.first.world.*;
public class TestLevel implements Level {
	
	World world;
	FreeList stat, dyn;
	Player player;
	
	public TestLevel() {
		
	}

	@Override
	public void load() {
		world = new World(100, 100);
		stat = world.getFreeStatic();
		dyn = world.getFreeDynamic();
		world.createStatic(new Cube(new Vect3(0, 0, 100), stat.getFreeId(), 300, 300, 300));
		player = new Player(new Vect3(0,0,0), new Vect3(0,0,1), world, 300);
	}

	@Override
	public Player getPlayer() {
		return player;
	}


}
