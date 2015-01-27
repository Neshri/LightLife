package game.first.pawn;


public class Controller {
	public static final float ROT_RATIO = 1.5f;
	private Player player;
	


	public Controller(Player player) {
		this.player = player;
	}

	

	public void update(float alphaX, float alphaY, float betaX, float betaY) {
		
		float rot = (alphaY-betaY) * ROT_RATIO;
		
		if (alphaY == 0 || betaY == 0) {
			player.updatePos(0.0f, rot);
		} else {
			player.updatePos(alphaY+betaY, rot);
		}
		

	}

}
