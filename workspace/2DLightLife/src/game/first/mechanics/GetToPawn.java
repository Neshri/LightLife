package game.first.mechanics;

import game.first.math.FloatPoint;
import game.first.pawn.Pawn;

public class GetToPawn implements Objective {

	private Pawn player, target;
	private float reqDist;

	public GetToPawn(Pawn player, Pawn target, float reqDist) {
		this.player = player;
		this.target = target;
		this.reqDist = reqDist;
	}

	@Override
	public boolean isCompleted() {
		FloatPoint pp = player.getPosition();
		FloatPoint tp = target.getPosition();
		if (pp.distance(tp) < reqDist) {
			return true;
		}
		return false;
	}


}
