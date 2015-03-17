package game.first.lighting;

public abstract class LightSource {
	
	private float[] position;
	private float[] color;
	
	public LightSource(float x, float y, float z, float[] color) {
		position = new float[3];
		this.color = color;
		position[0] = x;
		position[1] = y;
		position[2] = z;
	}
	
	public float[] getColor() {
		float[] send = new float[4];
		for (int i = 0; i < 4; i++) {
			send[i] = color[i];
		}
		return send;
	}
	
	public float[] getPos() {
		float[] send = new float[3];
		send[0] = position[0];
		send[1] = position[1];
		send[2] = position[2];
		return send;
	}
	
	public void setPos(float x, float y, float z) {
		position[0] = x;
		position[1] = y;
		position[2] = z;
	}

}
