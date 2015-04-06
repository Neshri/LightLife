package game.first.props;

import game.first.lighting.LightSource;
import game.first.lighting.PointLight;
import game.first.math.FloatPoint;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import android.opengl.GLES20;

public class PythagorasTree extends Shape {

	private int iterations, drawOrderLength;

	private ShortBuffer drawListBuffer;
	private int rotationDegree;

	public PythagorasTree(float x, float y, float z, float[] color, float size,
			int iterations, int rotationDegree) {
		super(x, y, z, color, false);
		if (iterations > 10) {
			iterations = 10;
		}
		this.rotationDegree = rotationDegree;
		this.iterations = iterations;
		double downScale = Math.sqrt(2) / 2;
		ArrayList<Float> verticeList = new ArrayList<Float>();
		ArrayList<Short> drawOrderList = new ArrayList<Short>();

		float[] points = new float[4];
		float sizeAdd = size * 0.18f;
		verticeList.add(position[0] + size * 0.41f);
		verticeList.add(position[1]);
		verticeList.add(position[2]);
		drawOrderList.add((short) 0);
		verticeList.add(verticeList.get(0) + sizeAdd);
		verticeList.add(verticeList.get(1));
		verticeList.add(verticeList.get(2));
		drawOrderList.add((short) 1);
		verticeList.add(verticeList.get(0) + sizeAdd);
		verticeList.add(verticeList.get(1) + sizeAdd * 2);
		verticeList.add(verticeList.get(2));
		drawOrderList.add((short) 2);
		verticeList.add(verticeList.get(0));
		verticeList.add(verticeList.get(1) + sizeAdd * 2);
		verticeList.add(verticeList.get(2));
		drawOrderList.add((short) 0);
		drawOrderList.add((short) 2);
		drawOrderList.add((short) 3);

		points[0] = verticeList.get(6);
		points[1] = verticeList.get(7);
		points[2] = verticeList.get(9);
		points[3] = verticeList.get(10);
		createVertices(verticeList, drawOrderList, downScale, 0, points,
				new FloatPoint(0, 1), true);

		float[] vertices = new float[verticeList.size()];
		for (int i = 0; i < verticeList.size(); i++) {
			vertices[i] = verticeList.get(i);
		}
		short[] drawOrderTemp = new short[drawOrderList.size()];
		for (int i = 0; i < drawOrderList.size(); i++) {
			drawOrderTemp[i] = drawOrderList.get(i);
		}
		drawOrderLength = drawOrderTemp.length;
		super.installVertices(vertices);
		ByteBuffer dlb = ByteBuffer.allocateDirect(
		// (# of coordinate values * 2 bytes per short)
				drawOrderTemp.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrderTemp);
		drawListBuffer.position(0);
	}

	private void createVertices(ArrayList<Float> verticeList,
			ArrayList<Short> drawOrderList, double downScale, int iteration,
			float[] points, FloatPoint direction, boolean fromLeft) {
		if (iteration == iterations) {
			return;
		}
		float size = (float) (Math.sqrt((double) ((points[0] - points[2])
				* (points[0] - points[2]) + (points[1] - points[3])
				* (points[1] - points[3]))) * downScale);

		float[] points1 = new float[4];
		short left;
		short right;
		if (iteration != 0) {
			if (fromLeft) {
				right = (short) (verticeList.size() / 3 - 2);
				left = (short) (verticeList.size() / 3 - 3);
			} else {
				left = (short) (verticeList.size() / 3 - 2);
				right = (short) (verticeList.size() / 3 - 3);
			}

		} else {
			left = (short) (verticeList.size() / 3 - 1);
			right = (short) (verticeList.size() / 3 - 2);
		}

		FloatPoint direction1 = direction.rotate(rotationDegree);
		FloatPoint direction2 = direction.rotate(-rotationDegree);
		FloatPoint widthAdd = direction1.mult(size);
		FloatPoint heightAdd = direction2.mult(size);
		drawOrderList.add(right);
		verticeList.add(points[0] + heightAdd.getX());
		points1[0] = verticeList.get(verticeList.size() - 1);
		verticeList.add(points[1] + heightAdd.getY());
		points1[1] = verticeList.get(verticeList.size() - 1);
		verticeList.add(position[2]);
		drawOrderList.add((short) (verticeList.size() / 3 - 1));
		verticeList.add(verticeList.get(verticeList.size() - 3)
				+ widthAdd.getX());
		points1[2] = verticeList.get(verticeList.size() - 1);
		verticeList.add(verticeList.get(verticeList.size() - 3)
				+ widthAdd.getY());
		points1[3] = verticeList.get(verticeList.size() - 1);
		verticeList.add(position[2]);
		drawOrderList.add((short) (verticeList.size() / 3 - 1));
		drawOrderList.add(right);
		drawOrderList.add((short) (verticeList.size() / 3 - 1));
		verticeList.add(verticeList.get(verticeList.size() - 3)
				- heightAdd.getX());
		verticeList.add(verticeList.get(verticeList.size() - 3)
				- heightAdd.getY());
		verticeList.add(position[2]);
		drawOrderList.add((short) (verticeList.size() / 3 - 1));

		createVertices(verticeList, drawOrderList, downScale, iteration + 1,
				points1, direction2, false);

		drawOrderList.add(left);
		verticeList.add(points[2] + widthAdd.getX());
		points1[2] = verticeList.get(verticeList.size() - 1);
		verticeList.add(points[3] + widthAdd.getY());
		points1[3] = verticeList.get(verticeList.size() - 1);
		verticeList.add(position[2]);
		drawOrderList.add((short) (verticeList.size() / 3 - 1));
		verticeList.add(verticeList.get(verticeList.size() - 3)
				+ heightAdd.getX());
		points1[0] = verticeList.get(verticeList.size() - 1);
		verticeList.add(verticeList.get(verticeList.size() - 3)
				+ heightAdd.getY());
		points1[1] = verticeList.get(verticeList.size() - 1);
		verticeList.add(position[2]);
		drawOrderList.add((short) (verticeList.size() / 3 - 1));
		drawOrderList.add(left);
		drawOrderList.add((short) (verticeList.size() / 3 - 1));
		verticeList.add(verticeList.get(verticeList.size() - 3)
				- widthAdd.getX());
		// points1[2] = verticeList.get(verticeList.size() - 1);
		verticeList.add(verticeList.get(verticeList.size() - 3)
				- widthAdd.getY());
		// points1[3] = verticeList.get(verticeList.size() - 1);
		verticeList.add(position[2]);
		drawOrderList.add((short) (verticeList.size() / 3 - 1));

		createVertices(verticeList, drawOrderList, downScale, iteration + 1,
				points1, direction1, true);

	}

	@Override
	public void draw(float[] vMatrix, float[] pMatrix,
			List<PointLight> pointLights) {
		super.draw(vMatrix, pMatrix, pointLights);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrderLength,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(positionHandle);

	}

}
