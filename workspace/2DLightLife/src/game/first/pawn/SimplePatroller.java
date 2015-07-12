package game.first.pawn;

import android.util.Log;
import game.first.math.FloatPoint;
import game.first.props.Shape;
import game.first.world.World;

public class SimplePatroller implements Pawn {

	private Shape shape;
	private float speed;
	private float[] position = new float[2];
	private FloatPoint direction;
	private float currDist;
	private float length;

	public SimplePatroller(World world, Shape shape, FloatPoint a,
			FloatPoint b, float speed) {
		world.createDynamic(shape);
		world.addPawn(this);
		this.shape = shape;

		this.speed = speed;
		position[0] = shape.position[0];
		position[1] = shape.position[1];
		length = a.distance(b);
		direction = b.sub(a);
		direction.normalize();
		direction = direction.mult(speed);	
	}

	@Override
	public void step(World world) {
		if (shape.getAlpha() <= 0) {
			world.removePawn(this);
			world.destroyDynamic(shape.id);
		}
		
		shape.updateCollisionShapeList(world);
		if (shape.move(direction.getX(), direction.getY())) {
			position[0] = shape.position[0];
			position[1] = shape.position[1];
		} else {
			direction = direction.mult(-1);
		}

	}

	@Override
	public FloatPoint getPosition() {
		return new FloatPoint(position[0], position[1]);
	}

	@Override
	public float getSpeed() {		
		return speed;
	}

}
