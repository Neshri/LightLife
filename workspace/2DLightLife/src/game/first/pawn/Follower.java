package game.first.pawn;

import game.first.lighting.LightSource;
import game.first.lighting.PointLight;
import game.first.math.FloatPoint;
import game.first.mechanics.LightPulse;
import game.first.props.Rectangle;
import game.first.props.Shape;
import game.first.props.SymmetricPolygon;
import game.first.world.World;
import util.InvalidFormatException;

public class Follower implements Pawn {

	private FloatPoint vPosition;
	private FloatPoint vDirection;
	private Shape model;
	private PointLight lightAura;
	private LightPulse lightBreath;
	private Pawn target;
	private World world;
	private float speed;

	public Follower(float x, float y, Pawn target, World world) {
		vPosition = new FloatPoint(x, y);
		speed = 0;
		this.target = target;
		this.world = world;
		world.addPawn(this);
		vDirection = new FloatPoint(0, 0);
		float[] color = { 1.0f, 1.0f, 1.0f, 1.0f };
		// Creates the Player model
		try {
			model = new SymmetricPolygon(5, 0.03f, color, x, y, 2, true, false);
			world.createDynamic(model);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//model = new Rectangle(0.06f, 0.06f, color, x, y, 2, true);
		// playerModel.setShaders(vertexShaderCode, fragmentShaderCode);
		
		
		lightAura = new PointLight(x, y, 2f, color, 0.3f);
		world.addPointLight(lightAura);
		lightBreath = new LightPulse(lightAura, 0.05f, 1);
		
	}

	@Override
	public void step() {
		lightBreath.update();
		FloatPoint targetPos = target.getPosition();
		targetPos = targetPos.sub(vPosition);
		float length = targetPos.getLength();

		if (length < 0.2f) {
			speed = 0;
		} else {
			speed = length / 100f;
		}
		targetPos.normalize();
		vDirection = targetPos.mult(speed);
		model.updateCollisionShapeList(world);
		targetPos = model.moveGetMTV(vDirection.getX(), vDirection.getY());
		if (targetPos != null) {
			targetPos = new FloatPoint(targetPos.getY(), -targetPos.getX());
			float mult = targetPos.dot(vDirection);
			targetPos = targetPos.mult(mult);
			if (model.move(targetPos.getX(), targetPos.getY())) {
				vDirection = targetPos;
			} else {
				return;
			}
		}

		vPosition = vPosition.add(vDirection);
		lightAura.setPos(vPosition.getX(), vPosition.getY(), 2);
	}

	@Override
	public FloatPoint getPosition() {
		FloatPoint send = new FloatPoint(vPosition.getX(), vPosition.getY());
		return send;
	}

	@Override
	public float getSpeed() {
		float send = vDirection.getLength();
		return send;
	}
}
