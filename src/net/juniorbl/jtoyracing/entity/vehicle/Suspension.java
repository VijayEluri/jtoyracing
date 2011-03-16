package net.juniorbl.jtoyracing.entity.vehicle;

import com.jme.math.Vector3f;

import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.TranslationalJointAxis;
import com.jmex.physics.material.Material;

/**
 * Suspension of a car.
 *
 * @version 1.0 Sep 3, 2007
 * @author Carlos Luz Junior
 */
public class Suspension extends Node {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 160713063828231761L;

	/**
	 * Scale of the wheel base.
	 */
	private static final float WHEEL_BASE_SCALE = .1f;

	/**
	 * Location of the right wheel.
	 */
	private static final Vector3f RIGHT_WHEEL_LOCATION = new Vector3f(0, 0, -2);

	/**
	 * Location of the left wheel.
	 */
	private static final Vector3f LEFT_WHEEL_LOCATION = new Vector3f(0, 0, 2);

	/**
	 * Mass of the suspension.
	 */
	private static final float MASS = 7.5f;

	/**
	 * Max dislocation of springs.
	 */
	private static final float MAX_SPRING_DISLOCATION = 0.01f;

	/**
	 * Acceleration of the springs.
	 */
	private static final float SPRING_ACCELERATION = 1;

	/**
	 * Velocity of the springs.
	 */
	private static final float SPRING_VELOCITY = -15;

	/**
	 * Location of the left wheel base.
	 */
	private static final Vector3f LEFT_WHEEL_BASE_LOCATION = new Vector3f(0, .9f, -.3f);

	/**
	 * Location of the right wheel base.
	 */
	private static final Vector3f RIGHT_WHEEL_BASE_LOCATION = new Vector3f(0, -.9f, -1.3f);

	/**
	 * Axis of the springs (Y axis).
	 */
	private static final Vector3f SPRING_AXIS = new Vector3f(0, 1, 0);

	/**
	 * Left wheel of the suspension.
	 */
	private Wheel leftWheel;

	/**
	 * Right wheel of the suspension.
	 */
	private Wheel rightWheel;

	/**
	 * Constructs a suspension. In order to create a suspension, it is necessary a chassis and the location of the
	 * suspension.
	 */
	public Suspension(PhysicsSpace physicsSpace, DynamicPhysicsNode chassis, Vector3f location) {
		DynamicPhysicsNode leftWheelBase = createWheelBase(physicsSpace, chassis, location.add(LEFT_WHEEL_BASE_LOCATION));
		DynamicPhysicsNode rightWheelBase = createWheelBase(physicsSpace, chassis, location.subtract(RIGHT_WHEEL_BASE_LOCATION));
		leftWheel = new Wheel(leftWheelBase, LEFT_WHEEL_LOCATION);
		this.attachChild(leftWheel);
		rightWheel = new Wheel(rightWheelBase, RIGHT_WHEEL_LOCATION);
		this.attachChild(rightWheel);
	}

	/**
	 * Creates a base for a wheel. A base is connected to the chassis by a joint which is applied a spring.
	 */
	private DynamicPhysicsNode createWheelBase(PhysicsSpace physicsSpace, DynamicPhysicsNode chassis,
			Vector3f location) {
		DynamicPhysicsNode wheelBase = physicsSpace.createDynamicNode();
		wheelBase.setLocalTranslation(chassis.getLocalTranslation().add(location));
		wheelBase.setLocalScale(WHEEL_BASE_SCALE);
		wheelBase.setMaterial(Material.GHOST);
		wheelBase.setMass(MASS);
		this.attachChild(wheelBase);
		Joint chassisWheelJoint = createChassisWheelJoint(physicsSpace, chassis, wheelBase);
		createSpring(chassisWheelJoint);
		return wheelBase;
	}

	/**
	 * Creates a joint which connects chassis and wheel base.
	 */
	private Joint createChassisWheelJoint(PhysicsSpace physicsSpace, DynamicPhysicsNode chassis,
			DynamicPhysicsNode wheelBase) {
		Joint chassisSuspensionJoint = physicsSpace.createJoint();
		chassisSuspensionJoint.attach(wheelBase, chassis);
		return chassisSuspensionJoint;
	}

	/**
	 * Creates a spring. A spring allows the suspension to move in the Y axis.
	 */
	private void createSpring(Joint chassisSuspensionJoint) {
		TranslationalJointAxis spring = chassisSuspensionJoint.createTranslationalAxis();
		spring.setPositionMaximum(MAX_SPRING_DISLOCATION);
		spring.setPositionMinimum(-MAX_SPRING_DISLOCATION);
		spring.setAvailableAcceleration(SPRING_ACCELERATION);
		spring.setDesiredVelocity(SPRING_VELOCITY);
		spring.setDirection(SPRING_AXIS);
	}

	/**
	 * Accelerates the wheels.
	 */
	public final void accelerate(float desiredVelocity) {
		leftWheel.accelerate(desiredVelocity);
		rightWheel.accelerate(desiredVelocity);
	}

	/**
	 * Stops the wheels.
	 */
	public final void stop() {
		leftWheel.stop();
		rightWheel.stop();
	}

	/**
	 * Steers the wheels.
	 */
	public final void steer(float desiredVelocity) {
		leftWheel.steer(desiredVelocity);
		rightWheel.steer(desiredVelocity);
	}

	/**
	 * Unsteer the wheels.
	 */
	public final void unsteer() {
		leftWheel.unsteer();
		rightWheel.unsteer();
	}
}
