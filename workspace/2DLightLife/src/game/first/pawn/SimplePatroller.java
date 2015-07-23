package game.first.pawn;

import android.util.Log;
import game.first.math.FloatPoint;
import game.first.props.Shape;
import game.first.world.World;

public class SimplePatroller implements Pawn {

	private Shape shape;
	private float speed;
	private FloatPoint position;
	private FloatPoint a, b, target;
	private long lastTargetChange;

	public SimplePatroller(World world, Shape shape, FloatPoint a,
			FloatPoint b, float speed) {
		world.createDynamic(shape);
		world.addPawn(this);
		this.shape = shape;
		lastTargetChange = System.currentTimeMillis();
		this.speed = speed;
		position = new FloatPoint(shape.position[0], shape.position[1]);
		target = b;
		this.a = a;
		this.b = b;
	}

	@Override
	public void step(World world) {
		if (shape.getAlpha() <= 0) {
			world.removePawn(this);
			world.destroyDynamic(shape.id);
		}
		if (System.currentTimeMillis() - lastTargetChange > 1000) {
			if (position.distance(target) < 0.5f) {
				lastTargetChange = System.currentTimeMillis();
				if (target.equals(a)) {
					target = b;
				} else {
					target = a;
				}
			}
		}
		
		FloatPoint dir = target.sub(position);
		dir.normalize();
		dir = dir.mult(speed);
		BasicMovement.movePushSlide(shape, world, dir);
		position = new FloatPoint(shape.position[0], shape.position[1]);

	}

	@Override
	public FloatPoint getPosition() {
		return new FloatPoint(position.getX(), position.getY());
	}

	@Override
	public float getSpeed() {		
		return speed;
	}

}
