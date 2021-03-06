package net.juniorbl.jtoyracing.entity.vehicle;

import net.juniorbl.jtoyracing.enums.ResourcesPath;
import net.juniorbl.jtoyracing.util.ModelUtil;

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
	private static final long serialVersionUID = 8702035354026675358L;
	private static final float TIRE_SCALE = 1.5f;
	private static final float WHEEL_SCALE = .4f;
	private static final float MASS = 4;
	private static final float TRACTION_ACCELERATION = 150;
	private static final float TRACTION_BREAK_REVERSE_ACCELERATION = 250;
	private static final float STEER_ACCELERATION = 30;
	private static final float MAX_STEER_ROTATION = 0.3f;
	private DynamicPhysicsNode wheel;
	private RotationalJointAxis tractionAxis;
	private RotationalJointAxis steerAxis;

	public Wheel(DynamicPhysicsNode wheelBase, Vector3f location) {
		createWheel(wheelBase, location);
		Joint tireBaseJoint = createBaseTireJoint(wheelBase.getSpace(), wheelBase);
		createSteerAxis(tireBaseJoint);
		createTractionAxis(tireBaseJoint);
		this.attachChild(wheel);
	}

	private void createWheel(DynamicPhysicsNode wheelBase, Vector3f location) {
		wheel = wheelBase.getSpace().createDynamicNode();
		wheel.setLocalTranslation(wheelBase.getLocalTranslation().add(location));
		PhysicsSphere tire = wheel.createSphere("tire");
		tire.setLocalScale(TIRE_SCALE);
		wheel.generatePhysicsGeometry();
		wheel.attachChild(ModelUtil.convertOBJToStatial(ResourcesPath.MODELS_PATH + "obj/whell.obj"));
		wheel.setMass(MASS);
		wheel.setMaterial(Material.RUBBER);
		wheel.setLocalScale(WHEEL_SCALE);
	}

	/**
	 * Creates a traction axis. It responds the traction action in the wheel.
	 */
	private void createTractionAxis(Joint tireBaseJoint) {
		tractionAxis = tireBaseJoint.createRotationalAxis();
		tractionAxis.setDirection(Vector3f.UNIT_X);
		tractionAxis.setRelativeToSecondObject(true);
		tractionAxis.setAvailableAcceleration(TRACTION_ACCELERATION);
	}

	/**
	 * Creates a steer axis. It responds the steer action in the wheel.
	 */
	private void createSteerAxis(Joint tireBaseJoint) {
		steerAxis = tireBaseJoint.createRotationalAxis();
		steerAxis.setDirection(Vector3f.UNIT_Y);
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

	public final void accelerate(float desiredVelocity) {
		boolean breakOrReverse = desiredVelocity < 0;
		if (breakOrReverse) {
			tractionAxis.setAvailableAcceleration(TRACTION_BREAK_REVERSE_ACCELERATION);
		} else {
			tractionAxis.setAvailableAcceleration(TRACTION_ACCELERATION);
		}
		tractionAxis.setDesiredVelocity(desiredVelocity);
	}

	public final void stop() {
		tractionAxis.setDesiredVelocity(0);
	}

	public final void steer(float desiredDirection) {
		steerAxis.setDesiredVelocity(desiredDirection);
		steerAxis.setPositionMaximum(MAX_STEER_ROTATION);
		steerAxis.setPositionMinimum(-MAX_STEER_ROTATION);
	}

	public final void unsteer() {
		steerAxis.setDesiredVelocity(0);
		steerAxis.setPositionMaximum(0);
		steerAxis.setPositionMinimum(0);
	}
}
