package game.first.lighting;

public class PointLight extends LightSource {
	
	private float strength;
	
	public PointLight(float x, float y, float z, float[] color, float strength) {
		super(x,y,z, color);
		this.strength = strength;
	}
	
	public void setStrength(float strength) {
		if (strength < 0) {
			strength = 0;
		}
		this.strength = strength;
	}
	
	public float getStrength() {
		return strength;
	}
}
