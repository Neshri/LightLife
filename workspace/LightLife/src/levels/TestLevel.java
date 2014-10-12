package levels;
import game.first.lightlife.Player;
import game.first.math.Vect3;
import game.first.world.*;
public class TestLevel {
	
	World world;
	FreeList stat, dyn;
	Player player;
	
	public TestLevel() {
		world = new World(100, 100);
		stat = world.getFreeStatic();
		dyn = world.getFreeDynamic();
		player = new Player(new Vect3(0,0,1), new Vect3(1,0,0), world, 300);
		
		
	}


}
