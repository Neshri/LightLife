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
	
	public FloatPoint moveGetMTV(float x, float y) {
		a.moveGetMTV(x, y);
		b.moveGetMTV(x, y);
		return null;
	}

	public boolean move(float x, float y) {
		return false;
	}

	@Override
	public void draw(float[] vMatrix, float[] pMatrix, float[] mMatrix,
			List<PointLight> pointLights) {
		a.draw(vMatrix, pMatrix, mMatrix, pointLights);
		b.draw(vMatrix, pMatrix, mMatrix, pointLights);
	}
	
	@Override
	public String toString() {
		return a.toString() + ", " + b.toString();
	}

}
