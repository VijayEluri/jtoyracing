package net.juniorbl.jtoyracing.entity.vehicle;

import net.juniorbl.jtoyracing.util.ModelUtil;
import net.juniorbl.jtoyracing.util.ResourcesPath;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.RotationalJointAxis;
import com.jmex.physics.geometry.PhysicsSphere;
import com.jmex.physics.material.Material;

/**
 * Wheel of a vehicle. It reacts to some speed by calculating the interaction between its own body
 * and the floor (done by JMonkeyEngine).
 *
 * @version 1.0 Aug 23, 2007
 * @author Carlos Luz Junior
 */
public class Wheel extends Node {

	/**
	 * Constant that represents the left side of a wheel.
	 */
	public static final Integer LEFT_WHEEL_SIDE = 0;

	/**
	 * Constant that represents the right side of a wheel.
	 */
	public static final Integer RIGHT_WHEEL_SIDE = 1;

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 8702035354026675358L;

	/**
	 * Scale of a tire.
	 */
	private static final int TIRE_SCALE = 2;

	/**
	 * Scale of a wheel.
	 */
	private static final float WHEEL_SCALE = .5f;

	/**
	 * Mass of the wheel.
	 */
	private static final float MASS = 5;

	/**
	 * Direction of traction axis.
	 */
	private static final Vector3f TRACTION_AXIS_DIRECTION = new Vector3f(0, 0, 1);

	/**
	 * Direction of steer axis.
	 */
	private static final Vector3f STEER_AXIS_DIRECTION = new Vector3f(0, 1, 0);

	/**
	 * Acceleration of the traction.
	 */
	private static final float TRACTION_ACCELERATIO = 150;

	/**
	 * Acceleration of the steer.
	 */
	private static final float STEER_ACCELERATION = 30;

	/**
	 * Maximum rotation of steer.
	 */
	private static final float MAX_STEER_ROTATION = 0.3f;

	/**
	 * Wheel.
	 */
	private DynamicPhysicsNode wheel;

	/**
	 * A traction axis.
	 */
	private RotationalJointAxis tractionAxis;

	/**
	 * A steer axis.
	 */
	private RotationalJointAxis steerAxis;

	/**
	 * Constructs a wheel.
	 */
	public Wheel(DynamicPhysicsNode wheelBase, Vector3f location, Integer wheelSide) {
		createWheel(wheelBase, location, wheelSide);
		Joint tireBaseJoint = createBaseTireJoint(wheelBase.getSpace(), wheelBase);
		createSteerAxis(tireBaseJoint);
		createTractionAxis(tireBaseJoint);
		this.attachChild(wheel);
	}

	/**
	 * Creates a wheel based on base, location and side.
	 */
	private void createWheel(DynamicPhysicsNode wheelBase, Vector3f location, Integer wheelSide) {
		wheel = wheelBase.getSpace().createDynamicNode();
		wheel.setLocalTranslation(wheelBase.getLocalTranslation().add(location));
		PhysicsSphere tire = wheel.createSphere("tire");
		tire.setLocalScale(TIRE_SCALE);
		wheel.generatePhysicsGeometry();
		selectWheelModel(wheelSide);
		wheel.setMass(MASS);
		wheel.setMaterial(Material.RUBBER);
		wheel.setLocalScale(WHEEL_SCALE);
	}

	/**
	 * Verifies the correct wheel model to load.
	 *
	 * FIXME don't use two models.
	 */
	private void selectWheelModel(Integer wheelSide) {
		if (wheelSide.equals(LEFT_WHEEL_SIDE)) {
			wheel.attachChild(ModelUtil.convertMultipleModelToJME(
					ResourcesPath.MODELS_PATH + "obj/whellLeftSide.obj"));
		} else if (wheelSide.equals(RIGHT_WHEEL_SIDE)) {
			wheel.attachChild(ModelUtil.convertMultipleModelToJME(
					ResourcesPath.MODELS_PATH + "obj/whellRightSide.obj"));
		}
	}

	/**
	 * Creates a traction axis. It responds the traction action in the wheel.
	 */
	private void createTractionAxis(Joint tireBaseJoint) {
		tractionAxis = tireBaseJoint.createRotationalAxis();
		tractionAxis.setDirection(TRACTION_AXIS_DIRECTION);
		tractionAxis.setRelativeToSecondObject(true);
		tractionAxis.setAvailableAcceleration(TRACTION_ACCELERATIO);
	}

	/**
	 * Creates a steer axis. It responds the steer action in the wheel.
	 */
	private void createSteerAxis(Joint tireBaseJoint) {
		steerAxis = tireBaseJoint.createRotationalAxis();
		steerAxis.setDirection(STEER_AXIS_DIRECTION);
		steerAxis.setAvailableAcceleration(STEER_ACCELERATION);
		unsteer();
	}

	/**
	 * Creates a joint which connects the base and the tire.
	 */
	private Joint createBaseTireJoint(PhysicsSpace physicsSpace, DynamicPhysicsNode wheelBase) {
		Joint tireBaseJoint = physicsSpace.createJoint();
		tireBaseJoint.attach(wheelBase, wheel);
		tireBaseJoint.setAnchor(wheel.getLocalTranslation().subtract(wheelBase.getLocalTranslation()));
		return tireBaseJoint;
	}

	/**
	 * Accelerates a wheel according to a given velocity.
	 */
	public final void accelerate(float desiredVelocity) {
		tractionAxis.setDesiredVelocity(desiredVelocity);
	}

	/**
	 * Stops a wheel.
	 */
	public final void stop() {
		tractionAxis.setDesiredVelocity(0);
	}

	/**
	 * Steers a wheel given a direction.
	 */
	public final void steer(float desiredDirection) {
		steerAxis.setDesiredVelocity(desiredDirection);
		steerAxis.setPositionMaximum(MAX_STEER_ROTATION);
		steerAxis.setPositionMinimum(-MAX_STEER_ROTATION);
	}

	/**
	 * Unsteer a wheel.
	 */
	public final void unsteer() {
		steerAxis.setDesiredVelocity(0);
		steerAxis.setPositionMaximum(0);
		steerAxis.setPositionMinimum(0);
	}
}
