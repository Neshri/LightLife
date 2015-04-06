package game.first.physics;

import game.first.lighting.PointLight;
import game.first.math.FloatPoint;
import game.first.pawn.Pawn;
import game.first.props.Shape;
import game.first.world.World;

import java.util.Iterator;
import java.util.List;

import android.util.Log;

public class Bullet implements Pawn {

	private FloatPoint position, directionSpeed;
	private PointLight light;
	private CollisionBox collision;
	private long createdTime;
	private List<Shape> nearShapes;
	private float strength;

	public Bullet(FloatPoint position, FloatPoint directionSpeed, World world,
			float strength, PointLight light) {
		this.position = position;
		this.directionSpeed = directionSpeed;
		float[] points = new float[8];
		points[0] = position.getX() - 0.05f;
		points[6] = points[0];
		points[2] = position.getX() + 0.05f;
		points[4] = points[2];
		points[1] = position.getY() - 0.05f;
		points[3] = points[1];
		points[5] = position.getY() + 0.05f;
		points[7] = points[5];
		collision = new CollisionBox(points, 2);
		this.strength = strength;
		this.light = light;
		world.addPointLight(light);
		world.addPawn(this);
		createdTime = System.currentTimeMillis();
	}

	@Override
	public void step(World world) {
		if (System.currentTimeMillis() - createdTime > 1000) {
			world.removePointLight(light);
			world.removePawn(this);
			//Log.d("Graphics", "yo");
			return;
		}
		nearShapes = world.getNearShapes(collision.roughBounds);
		Iterator<Shape> iter = nearShapes.iterator();
		while (iter.hasNext()) {
			Shape test = iter.next();
			if (collision.overlaps(test.collisionShape)) {
				world.removePointLight(light);
				world.removePawn(this);
				if (!test.isDestructible()) {
					return;
				} else {
					float alpha = test.getAlpha() - strength;
					if (alpha <= 0.1f) {
						world.destroyStatic(test.id);
						return;
					} else {
						test.setAlpha(alpha);
						return;
					}
				}
			}
		}
		strength = strength * 0.97f;
		float shine = light.getStrength();
		light.setStrength(shine * 0.99f);
		collision.move(directionSpeed.getX(), directionSpeed.getY());
		position = position.add(directionSpeed);
		light.setPos(position.getX(), position.getY(), 2f);
		
	}

	@Override
	public FloatPoint getPosition() {
		return position;
	}

	@Override
	public float getSpeed() {
		return directionSpeed.getLength();
	}
}
