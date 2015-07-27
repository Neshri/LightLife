package game.first.pawn;

import game.first.math.FloatPoint;
import game.first.props.Shape;
import game.first.world.World;

public class LinkedMove implements Pawn {

	private Shape controller, subject;
	private FloatPoint subjectDirection, controllerPoint;
	private float length;

	public LinkedMove(Shape controller, Shape subject, World world,
			FloatPoint subjectDirection, FloatPoint controllerPoint) {
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
		BasicMovement.move(subject, world, subjectDirection.mult(dist - length));
		if (dist > 0.001f) {
			FloatPoint moveDir = controllerPoint.sub(new FloatPoint(
					controller.position[0], controller.position[1]));
			moveDir.normalize();
			float speed = dist / 40f;
			BasicMovement.move(controller, world, moveDir.mult(speed));
		}

		
		length = dist;
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