package game.first.pawn;

import java.util.Observable;
import java.util.Observer;

import android.opengl.Matrix;

public class Camera implements Observer {
	public final float[] mViewMatrix = new float[16];
	//private final float[] pos = new float[4];
	private float z;
	
	public Camera(float x, float y, float z) {
		this.z = z + 5;
//		pos[0] = x;
//		pos[1] = y;
//		pos[2] = z;
//		pos[3] = 1f;
		Matrix.setLookAtM(mViewMatrix, 0, x, y, z + 5, x, y, z, 0, 1f, 0);

	}

	@Override
	public void update(Observable observable, Object data) {
		Player player = (Player) observable;
		float[] playPos = player.getPosition();
//		pos[0] = playPos[0];
//		pos[1] = playPos[1];
		Matrix.setLookAtM(mViewMatrix, 0, playPos[0], playPos[1], z + 5, playPos[0],
				playPos[1], z, 0, 1f, 0);
		//Log.d("PlayerMove", pos[0] + " , " + pos[1]);
	}
}
