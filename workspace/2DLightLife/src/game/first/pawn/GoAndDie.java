package game.first.pawn;

import java.util.Observable;

import game.first.math.FloatPoint;
import game.first.props.Shape;
import game.first.world.World;

public class GoAndDie extends Observable implements Pawn {

	private Shape shape;
	private FloatPoint moveTerm;
	private float speed, distance, goalDist;
	
	/**
	 * Without collision!
	 * @param shape
	 * @param world
	 * @param direction
	 * @param speed
	 * @param length
	 */
	public GoAndDie(Shape shape, World world, FloatPoint direction, float speed, float length) {
		this.shape = shape;
		world.createDynamic(shape);
		world.addPawn(this);
		direction.normalize();
		moveTerm = direction.mult(speed);
		this.speed = speed;
		goalDist = length;
		
	}

	@Override
	public void step(World world) {
		if (distance > goalDist) {
			world.removePawn(this);
			world.destroyDynamic(shape.id);
			setChanged();
			notifyObservers();
		} else {
			shape.move(moveTerm.getX(), moveTerm.getY());
			distance += speed;
		}
		
	}

	@Override
	public FloatPoint getPosition() {
		return new FloatPoint(shape.position[0], shape.position[1]);
	}

	@Override
	public float getSpeed() {
		return speed;
	}
}
