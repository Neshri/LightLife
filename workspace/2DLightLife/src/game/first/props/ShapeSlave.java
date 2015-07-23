package game.first.props;

import game.first.lighting.PointLight;

import java.util.List;

import android.opengl.Matrix;

public class ShapeSlave extends Shape {

	private Shape shape;

	public ShapeSlave(Shape shape) {
		super(shape.position[0], shape.position[1], shape.position[2], shape
				.getColor(), false);
		this.shape = shape;
		shape.getModelMatrix(modelMatrix);
	}
	
	public void scale(float xScale, float yScale) {
		Matrix.scaleM(modelMatrix, 0, xScale, yScale, 1);
		position[0] = position[0] * xScale;
		position[1] = position[1] * yScale;
	}
	
	public void translate(float x, float y) {
		Matrix.translateM(modelMatrix, 0, x, y, 0);
		position[0] += x;
		position[1] += y;
	}
	
	@Override
	public void draw(float[] vMatrix, float[] pMatrix, float[] mMatrix,
			List<PointLight> pointLights) {
		if (mMatrix == null) {
			mMatrix = modelMatrix;
		}
		shape.draw(vMatrix, pMatrix, mMatrix, pointLights);
	}

	@Override
	public String toString() {
		return "Slave for: " + shape.toString();
	}

}
