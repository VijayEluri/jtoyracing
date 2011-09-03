package net.juniorbl.jtoyracing.entity.vehicle;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.TranslationalJointAxis;
import com.jmex.physics.material.Material;

/**
 * Suspension of a vehicle.
 *
 * @version 1.0 Sep 3, 2007
 * @author Carlos Luz Junior
 */
public class Suspension extends Node {
	private static final long serialVersionUID = 160713063828231761L;
	private static final float WHEEL_BASE_SCALE = .1f;
	private static final Vector3f RIGHT_WHEEL_LOCATION = new Vector3f(0, 0, -2);
	private static final Vector3f LEFT_WHEEL_LOCATION = new Vector3f(0, 0, 2);
	private static final float MASS = 7.5f;
	private static final float MAX_SPRING_DISLOCATION = 0.01f;
	private static final float SPRING_ACCELERATION = 1;
	private static final float SPRING_VELOCITY = -15;
	private static final Vector3f LEFT_WHEEL_BASE_LOCATION = new Vector3f(0, .9f, -.3f);
	private static final Vector3f RIGHT_WHEEL_BASE_LOCATION = new Vector3f(0, -.9f, -1.3f);
	private Wheel leftWheel;
	private Wheel rightWheel;
	private PhysicsSpace physicsSpace;

	public Suspension(PhysicsSpace physicsSpace, DynamicPhysicsNode chassis, Vector3f location) {
		this.physicsSpace = physicsSpace;
		DynamicPhysicsNode leftWheelBase = createWheelBase(chassis, location.add(LEFT_WHEEL_BASE_LOCATION));
		DynamicPhysicsNode rightWheelBase = createWheelBase(chassis, location.subtract(RIGHT_WHEEL_BASE_LOCATION));
		leftWheel = new Wheel(leftWheelBase, LEFT_WHEEL_LOCATION);
		this.attachChild(leftWheel);
		rightWheel = new Wheel(rightWheelBase, RIGHT_WHEEL_LOCATION);
		this.attachChild(rightWheel);
	}

	/**
	 * Creates a base for a wheel. A base is connected to the chassis by a joint which is applied a spring.
	 */
	private DynamicPhysicsNode createWheelBase(DynamicPhysicsNode chassis, Vector3f location) {
		DynamicPhysicsNode wheelBase = physicsSpace.createDynamicNode();
		wheelBase.setLocalTranslation(chassis.getLocalTranslation().add(location));
		wheelBase.setLocalScale(WHEEL_BASE_SCALE);
		wheelBase.setMaterial(Material.GHOST);
		wheelBase.setMass(MASS);
		this.attachChild(wheelBase);
		Joint chassisWheelJoint = createChassisWheelJoint(chassis, wheelBase);
		createSpring(chassisWheelJoint);
		return wheelBase;
	}

	/**
	 * Creates a joint which connects chassis and wheel base.
	 */
	private Joint createChassisWheelJoint(DynamicPhysicsNode chassis, DynamicPhysicsNode wheelBase) {
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
		spring.setDirection(Vector3f.UNIT_Y);
	}

	public final void accelerate(float desiredVelocity) {
		leftWheel.accelerate(desiredVelocity);
		rightWheel.accelerate(desiredVelocity);
	}

	public final void stop() {
		leftWheel.stop();
		rightWheel.stop();
	}

	public final void steer(float desiredVelocity) {
		leftWheel.steer(desiredVelocity);
		rightWheel.steer(desiredVelocity);
	}

	public final void unsteer() {
		leftWheel.unsteer();
		rightWheel.unsteer();
	}
}
