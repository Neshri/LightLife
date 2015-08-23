package game.first.levels;

import game.first.lightlife.R;
import game.first.math.FloatPoint;
import game.first.mechanics.GetToPawn;
import game.first.pawn.Follower;
import game.first.pawn.LinkedMove;
import game.first.pawn.Player;
import game.first.props.PythagorasTree;
import game.first.props.Rectangle;
import game.first.props.Shape;
import game.first.props.Triangle;
import game.first.world.World;

import java.util.List;

public class FourthLevel extends Level {

	public FourthLevel() {
		super(
				R.raw.escape_route,
				"Again, so familiar but still different. " +
				"Snow knew not to take her surroundings\' behavior for granted, " +
				"if it looked different it could also behave differently.",
				"SomethingInTheCogs");
	}

	@Override
	public Player load() {
		world = new World(100, 100);
		List<Shape> box = createStandardBoundingBox(5, 5, grey);
		for (Shape i : box) {
			world.createStatic(i);
		}
		Player player = new Player(-1.2f, -1f, white, world, this);
		Follower follow = new Follower(2, 2, green, player, world);
		objective = new GetToPawn(follow, player, 0.3f);
		box = createDestroyableBox(0.5f, 0.5f, 2, 2, 0.2f, blue);
		for (Shape i : box) {
			world.createStatic(i);
		}

		Shape controller = new Rectangle(0.5f, 0.5f, darkGreen, -0.5f, 0, 2,
				true, false);
		Shape subject = new Rectangle(0.5f, 1f, red, 0.555f, 0, 2, true, false);
		Shape lock = new Triangle(new FloatPoint(0.555f, -1f), new FloatPoint(
				1.305f, 0.45f), new FloatPoint(1.305f, -1f), 2, blue, true,
				true);

		new LinkedMove(controller, subject, world, 1.7f, new FloatPoint(0, 1),
				new FloatPoint(-0.5f, 0), lock, new FloatPoint(0, -0.01f));

		// locking mechanism

		box = createBoxWithHole(2.15f, 4.9f, 'L', 0.4f, 1.375f, 0, 0.3f,
				darkRed);
		for (Shape i : box) {
			world.createStatic(i);
		}

		// controller should not be able to move further than subject
		world.createStatic(new Rectangle(2.55f, 1.05f, black, -1.25f, 2.25f, 2,
				true, false));
		world.createStatic(new Rectangle(2.55f, 1.05f, black, -1.25f, -2.25f,
				2, true, false));
		world.createStatic(new Rectangle(0.9f, 5f, black, -2.06f, 0, 2, true,
				false));

		world.createStatic(new PythagorasTree(0.55f, -2.55f, 2, darkGreenRed,
				3, 0.7f, 10, 25));

		return player;
	}

}
