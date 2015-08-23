package game.first.pawn;

import game.first.math.FloatPoint;
import game.first.props.Shape;
import game.first.world.World;

public class LinkedMove implements Pawn {

	private Shape controller, subject, lock;
	private FloatPoint subjectDirection, controllerPoint, sUnlockedDirection;
	private float length, multiplier;
	private boolean lockEnabled;

	public LinkedMove(Shape controller, Shape subject, World world,
			float multiplier, FloatPoint subjectDirection,
			FloatPoint controllerPoint, Shape lock,
			FloatPoint sUnlockedDirection) {
		if (lock != null) {
			lockEnabled = true;
			this.lock = lock;
			world.createStatic(lock);
			this.sUnlockedDirection = sUnlockedDirection;
		}
		this.multiplier = multiplier;
		this.controller = controller;
		this.subject = subject;
		subject.setPushable(false);
		this.subjectDirection = subjectDirection;
		this.subjectDirection.normalize();
		this.controllerPoint = controllerPoint;
		world.createDynamic(controller);
		world.createDynamic(subject);
		world.addPawn(this);
	}

	@Override
	public void step(World world) {
		float dist = controllerPoint.distance(new FloatPoint(
				controller.position[0], controller.position[1]));
		BasicMovement.move(subject, world,
				subjectDirection.mult((dist - length) * multiplier));
		if (dist > 0.001f) {
			FloatPoint moveDir = controllerPoint.sub(new FloatPoint(
					controller.position[0], controller.position[1]));
			moveDir.normalize();
			float speed = dist / 40f;
			BasicMovement.move(controller, world, moveDir.mult(speed));
		}

		length = dist;

		if (lockEnabled) {
			if (lock.getAlpha() == 0) {
				BasicMovement.move(subject, world, sUnlockedDirection);
			}
		}
	}

	@Override
	public FloatPoint getPosition() {
		return null;
	}

	@Override
	public float getSpeed() {
		return 0;
	}

}
