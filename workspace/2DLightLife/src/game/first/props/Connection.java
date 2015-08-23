package game.first.props;

import game.first.lighting.PointLight;
import game.first.math.FloatPoint;

import java.util.List;

public class Connection extends Shape {

	private Shape a, b;

	public Connection(float x, float y, float z, float[] color,
			boolean collision, boolean destructible) {
		super(x, y, z, color, destructible);

	}

	// Not done
	public FloatPoint moveGetMTV(float x, float y) {
		
		return null;
	}

	// Not done
	public boolean move(float x, float y) {
		return false;
	}

	// Not done
	public FloatPoint push(float x, float y, float pushMass) {
		return null;
	}

	@Override
	public void draw(float[] vMatrix, float[] pMatrix, float[] mMatrix,
			List<PointLight> pointLights) {
		mMatrix = modelMatrix;
		a.draw(vMatrix, pMatrix, mMatrix, pointLights);
		b.draw(vMatrix, pMatrix, mMatrix, pointLights);
	}

	@Override
	public float[] getRoughBounds() {
		if (a.collisionShape == null || b.collisionShape == null) {
			return null;
		}
		float[] send = new float[4];
		float[] aColli = a.collisionShape.roughBounds;
		float[] bColli = b.collisionShape.roughBounds;
		for (int i = 0; i < 2; i++) {
			if (aColli[i] < bColli[i]) {
				send[i] = aColli[i];
			} else {
				send[i] = bColli[i];
			}
		}
		for (int i = 2; i < 4; i++) {
			if (aColli[i] > bColli[i]) {
				send[i] = aColli[i];
			} else {
				send[i] = bColli[i];
			}
		}
		return send;
	}

	@Override
	public String toString() {
		return a.toString() + ", " + b.toString();
	}

}
