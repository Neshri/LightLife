package game.first.physics;

import game.first.lighting.PointLight;
import game.first.math.FloatPoint;
import game.first.pawn.GoAndDie;
import game.first.pawn.Pawn;
import game.first.props.Rectangle;
import game.first.props.Shape;
import game.first.props.ShapeSlave;
import game.first.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Bullet implements Pawn {

	private final static float SIZE = 0.1f;

	private FloatPoint position, directionSpeed;
	private PointLight light;
	private Rectangle rect;
	private CollisionBox collision;
	private long createdTime;
	private List<Shape> nearShapes;
	private float strength;
	private boolean dying;
	private Shape shooter;

	public Bullet(FloatPoint position, FloatPoint directionSpeed, World world,
			Shape shooter, float strength, PointLight light) {
		this.position = position;
		this.directionSpeed = directionSpeed;
		this.shooter = shooter;
		float[] points = new float[8];
		points[0] = position.getX() - SIZE / 2;
		points[6] = points[0];
		points[2] = position.getX() + SIZE / 2;
		points[4] = points[2];
		points[1] = position.getY() - SIZE / 2;
		points[3] = points[1];
		points[5] = position.getY() + SIZE / 2;
		points[7] = points[5];
		collision = new CollisionBox(points, 2);
		float[] white = { 1.0f, 1.0f, 1.0f, 1.0f };
		rect = new Rectangle(SIZE, SIZE, white, points[0], points[1], 2.0f,
				false, false);
		this.strength = strength;
		this.light = light;
		world.createDynamic(rect);
		world.addPointLight(light);
		world.addPawn(this);
		createdTime = System.currentTimeMillis();
	}

	@Override
	public void step(World world) {
		float rAlpha = rect.getAlpha();
		rect.setAlpha(rAlpha * 0.97f);
		if (dying) {
			if (System.currentTimeMillis() - createdTime > 500) {
				world.removePawn(this);
				world.removePointLight(light);

				return;
			}
			light.setStrength(light.getStrength() * 0.95f);
			return;
		}
		if (System.currentTimeMillis() - createdTime > 1000) {
			world.removePointLight(light);
			world.removePawn(this);
			world.destroyDynamic(rect.id);
			return;
		}
		nearShapes = world.getNearShapes(collision.roughBounds);
		Iterator<Shape> iter = nearShapes.iterator();
		while (iter.hasNext()) {
			Shape test = iter.next();
			if (test == shooter) {
				test = iter.next();
			}
			if (collision.overlaps(test.collisionShape)) {
				dying = true;
				createdTime = System.currentTimeMillis();
				light.setStrength(light.getStrength() + 0.1f);
				new Splitter(rect, world, SIZE, 1);
				world.destroyDynamic(rect.id);
				if (!test.isDestructible()) {
					return;
				} else {
					float alpha = test.getAlpha() - strength;
					if (alpha <= 0.1f) {
						if (test.type == 'S') {
							world.destroyStatic(test.id);
						} else {
							world.destroyDynamic(test.id);
						}
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
		rect.move(directionSpeed.getX(), directionSpeed.getY());
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

	/**
	 * Second iteration spawns in wrong place, why?
	 * @author Anton
	 *
	 */
	private class Splitter implements Observer {
		private final static float SPEED = 0.03f;
		private final static float LENGTH = 1.5f;
		private ShapeSlave[] splits = new ShapeSlave[4];
		private int iteration;
		private World world;
		private float sideSize;

		public Splitter(Shape shape, World world, float sideSize, int iterations) {
			iteration = iterations;
			this.sideSize = sideSize;
			this.world = world;
			for (int i = 0; i < 4; i++) {
				splits[i] = new ShapeSlave(shape);
				float mX = splits[i].position[0] + sideSize / 2;
				float mY = splits[i].position[1] + sideSize / 2;
				splits[i].translate(mX, mY);
				splits[i].scale(0.5f, 0.5f);
			}

			new GoAndDie(splits[0], world, new FloatPoint(-1, -1), SPEED,
					LENGTH);
			splits[1].translate(sideSize / 2, 0);
			new GoAndDie(splits[1], world, new FloatPoint(1, -1), SPEED, LENGTH);
			splits[2].translate(sideSize / 2, sideSize / 2);
			new GoAndDie(splits[2], world, new FloatPoint(1, 1), SPEED, LENGTH);
			splits[3].translate(0, sideSize / 2);
			new GoAndDie(splits[3], world, new FloatPoint(-1, 1), SPEED, LENGTH)
					.addObserver(this);

		}

		@Override
		public void update(Observable observable, Object data) {
			if (iteration <= 1) {
				return;
			} else {
				for (int i = 0; i < 4; i++) {
					new Splitter(splits[i], world, sideSize / 2, iteration - 1);
				}
			}
		}
	}
}
