package game.first.pawn;

import game.first.math.FloatPoint;
import game.first.world.World;

public interface Pawn {
	
	public void step(World world);
	
	public FloatPoint getPosition();
	
	public float getSpeed();
	
	

}
