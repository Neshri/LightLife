package game.first.mechanics;

import game.first.lighting.PointLight;
import game.first.math.FloatPoint;
import game.first.physics.Bullet;
import game.first.world.World;

public class DestructionLight {

	private World world;
	private float strength;
	private final float[] color = { 1f, 1.0f, 1.0f, 1f };
	private static final float MAX_STRENGTH = 2.0f;
	private long lastShot;

	public DestructionLight(World world) {
		this.world = world;
		strength = 0.25f;
		lastShot = System.currentTimeMillis();
	}

	public void shoot(FloatPoint direction, FloatPoint position) {
		if (System.currentTimeMillis() - lastShot < 500) {
			return;
		}
		if (direction.getX() == 0 && direction.getY() == 0) {
			return;
		}
		FloatPoint speed = direction.div(25);
		new Bullet(position, speed, world, strength,
				new PointLight(position.getX(), position.getY(), 2.5f, color, 0.7f));
		lastShot = System.currentTimeMillis();
	}

}
