package game.first.pawn;


public class Controller {
	public static final float ROT_RATIO = 1.5f;
	private Player player;
	


	public Controller(Player player) {
		this.player = player;
	}

	

	public void update(float leftX, float leftY, float rightX, float rightY) {
		player.move(leftX, leftY);
		
	}

}
