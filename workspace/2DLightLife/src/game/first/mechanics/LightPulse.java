package game.first.mechanics;


import game.first.lighting.PointLight;

public class LightPulse {
	
	private PointLight light;
	private float midStrength, amplitude, pulseAdd, frequency;
	private long lastTime;
	
	public LightPulse(PointLight light, float amplitude, float frequency) {
		this.light = light;
		midStrength = light.getStrength();
		this.amplitude = amplitude;
		pulseAdd = 0.001f;
		this.frequency = frequency;
		
	}

	public void update() {
		long timeDiff = System.currentTimeMillis() - lastTime;
		if (frequency > timeDiff) {
			return;
		}
		if (light.getStrength() > midStrength + amplitude) {
			pulseAdd = pulseAdd * -1;
		} else if (light.getStrength() < midStrength - amplitude) {
			pulseAdd = pulseAdd * -1;
		}
		light.setStrength(light.getStrength() + pulseAdd);
		lastTime = System.currentTimeMillis();
		
	}

}
