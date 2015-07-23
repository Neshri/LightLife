package game.first.pawn;

import game.first.math.FloatPoint;
import game.first.props.Shape;
import game.first.world.World;

public class MoveTo implements Pawn {

	private Shape shape;
	private FloatPoint target;
	private float speedDivider;

	public MoveTo(Shape shape, World world, FloatPoint target,
			float speedDivider) {
		this.shape = shape;
		shape.setPushable(false);
		this.target = target;
		world.createDynamic(shape);
		world.addPawn(this);
	}

	@Override
	public void step(World world) {
		FloatPoint moveDir = target.sub(new FloatPoint(shape.position[0],
				shape.position[1]));
		moveDir.normalize();
		float dist = target.distance(new FloatPoint(shape.position[0],
				shape.position[1]));
		float speed = dist / speedDivider;
		BasicMovement.move(shape, world, moveDir.mult(speed));
	}

	@Override
	public FloatPoint getPosition() {
		return new FloatPoint(shape.position[0], shape.position[1]);
	}

	@Override
	public float getSpeed() {
		return 0;
	}

}
