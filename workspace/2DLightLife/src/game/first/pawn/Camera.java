package game.first.pawn;

import game.first.math.FloatPoint;

import java.util.Observable;
import java.util.Observer;

import android.opengl.Matrix;

public class Camera implements Observer {
	public final float[] mViewMatrix = new float[16];
	private float z;

	public Camera(float x, float y, float z) {
		this.z = z;
		Matrix.setLookAtM(mViewMatrix, 0, x, y, z + 5, x, y, z, 0, 1f, 0);
	}

	@Override
	public void update(Observable observable, Object data) {
		Player player = (Player) observable;
		FloatPoint playPos = player.getPosition();
		Matrix.setLookAtM(mViewMatrix, 0, playPos.getX(), playPos.getY(),
				z + 5, playPos.getX(), playPos.getY(), z, 0, 1f, 0);
	}
}
