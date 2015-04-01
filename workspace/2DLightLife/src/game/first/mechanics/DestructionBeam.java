package game.first.mechanics;

import game.first.lighting.LineLight;
import game.first.math.FloatPoint;
import game.first.world.World;

public class DestructionBeam {

	private World world;
	private float strength;
	private LineLight light;
	private static final float MAX_STRENGTH = 2.0f;

	public DestructionBeam(World world) {
		this.world = world;
		float[] color = { 1f, 1f, 1f, 1f };
		float[] pos = { 0, 0, 2f };
		light = new LineLight(pos, pos, color);
	}

	public void shoot(FloatPoint direction, FloatPoint position) {
		if (direction.getX() == 0 && direction.getY() == 0) {
			strength = 0;
			light.setStrength(0);
			return;
		}
		strength += 0.01f;
		if (strength > MAX_STRENGTH) {
			strength = MAX_STRENGTH;
		}

	}
}
