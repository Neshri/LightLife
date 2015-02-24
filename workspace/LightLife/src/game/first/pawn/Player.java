package game.first.pawn;

import android.opengl.Matrix;
import android.util.Log;
import game.first.math.Vect3;
import game.first.world.*;

public class Player {

	private final float[] vDirection = new float[4];
	private final float[] vPosition = new float[4];
	private World world;
	private Controller control;
	private float speed;

	public final float[] mViewMatrix = new float[16];

	public Player(Vect3 pos, Vect3 rot, World world) {
		setPos(pos);
		setRot(rot);

		this.world = world;
		speed = 0.3f;
		float[] posi = pos.getArray();
		Matrix.setLookAtM(mViewMatrix, 0, posi[0], posi[1], posi[2] - 3f,
				posi[0], posi[1], posi[2], 0f, 1.0f, 0.0f);
		control = new Controller(this);
	}

	public World getWorld() {
		return world;
	}

	public Controller getController() {
		return control;
	}

	public void updatePos(float distance, float degrees) {
		float[] translate = new float[16];
		float[] rotMatrix = new float[16];
		Matrix.setIdentityM(translate, 0);
		Matrix.setIdentityM(rotMatrix, 0);
		if (distance != 0.0f) {
			distance = distance * speed;
			float moveX = vDirection[0] * distance;
			float moveY = vDirection[1] * distance;
			float moveZ = vDirection[2] * distance;

			Matrix.translateM(translate, 0, moveX, moveY, moveZ);
			Matrix.multiplyMV(vPosition, 0, translate, 0, vPosition, 0);

			Log.d("values", "" + vPosition[0] + " " + vPosition[1] + " "
					+ vPosition[2]);
		}

		Matrix.setRotateM(rotMatrix, 0, degrees, 0f, 1f, 0f);
		Matrix.multiplyMV(vDirection, 0, rotMatrix, 0, vDirection, 0);

		
		Matrix.multiplyMM(mViewMatrix, 0, rotMatrix, 0, mViewMatrix, 0);
		Matrix.multiplyMM(mViewMatrix, 0, translate, 0, mViewMatrix, 0);
	}

	public float[] getPos() {
		return vPosition;
	}

	public float[] getRot() {
		return vDirection;
	}

	public void setPos(Vect3 pos) {
		vPosition[0] = pos.getX();
		vPosition[1] = pos.getY();
		vPosition[2] = pos.getZ();
		vPosition[3] = 1f;
	}

	public void setRot(Vect3 rot) {
		vDirection[0] = rot.getX();
		vDirection[1] = rot.getY();
		vDirection[2] = rot.getZ();
		vDirection[3] = 1f;
		float length = Matrix.length(vDirection[0], vDirection[1],
				vDirection[2]);
		vDirection[0] = vDirection[0] / length;
		vDirection[1] = vDirection[1] / length;
		vDirection[2] = vDirection[2] / length;
	}

}
