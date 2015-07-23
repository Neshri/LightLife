package game.first.pawn;

import game.first.math.FloatPoint;
import game.first.props.Shape;
import game.first.world.World;

public class BasicMovement {

	public static FloatPoint movePushSlide(Shape shape, World world,
			FloatPoint vector) {
		shape.updateCollisionShapeList(world);
		FloatPoint normalVector = shape
				.moveGetMTV(vector.getX(), vector.getY());
		if (normalVector != null) {
			Shape toPush = shape.lastCollision;
			toPush.updateCollisionShapeList(world);
			// PUSH IT!!!
			FloatPoint pushVector = new FloatPoint(-normalVector.getX(),
					-normalVector.getY());
			pushVector = pushVector.mult(pushVector.dot(vector));
			pushVector = toPush.push(pushVector.getX(), pushVector.getY(),
					shape.collisionShape.mass);
			
			FloatPoint slide = new FloatPoint(normalVector.getY(),
					-normalVector.getX());
			float multiplier = slide.dot(new FloatPoint(vector.getX(), vector
					.getY()));
			slide = slide.mult(multiplier);
			
			slide = slide.add(pushVector);
			
			if (!shape.move(slide.getX(), slide.getY())) {
				return new FloatPoint(0, 0);
			} else {
				return slide;
			}
		}
		return vector;
	}
	
	public static void movePush(Shape shape, World world, FloatPoint vector) {
		
	}

	public static FloatPoint moveSlide(Shape shape, World world,
			FloatPoint vector) {
		shape.updateCollisionShapeList(world);
		FloatPoint normalVector = shape
				.moveGetMTV(vector.getX(), vector.getY());
		if (normalVector != null) {
			FloatPoint slide = new FloatPoint(normalVector.getY(),
					-normalVector.getX());
			float multiplier = slide.dot(new FloatPoint(vector.getX(), vector
					.getY()));
			slide = slide.mult(multiplier);
			if (!shape.move(slide.getX(), slide.getY())) {
				return new FloatPoint(0, 0);
			} else {
				return slide;
			}
		}
		return vector;
	}

	public static boolean move(Shape shape, World world, FloatPoint vector) {
		shape.updateCollisionShapeList(world);
		return shape.move(vector.getX(), vector.getY());
	}

}
