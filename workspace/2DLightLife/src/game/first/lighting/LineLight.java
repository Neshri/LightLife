package game.first.lighting;


public class LineLight {

	private float strength;
	private float[] color, points;

	public LineLight(float[] point1, float[] point2, float[] color) {
		color = new float[4];
		for (int i = 0; i < 4; i++) {
			this.color[i] = color[i];
		}
		points = new float[6];
		for (int i = 0; i < point1.length; i++) {
			points[i] = point1[i];
		}
		for (int i = 0; i < point2.length; i++) {
			points[i + 3] = point2[i];
		}
		strength = 0;

	}
	
	public float[] getPositions() {
		float[] send = new float[6];
		for (int i = 0; i < 6; i++) {
			send[i] = points[i];
		}
		return send;
	}
	
	

	public void setPosition(float[] points) {
		for (int i = 0; i < points.length; i++) {
			this.points[i] = points[i];
		}
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}
	
	public float getStrength() {
		return strength;
	}

}
