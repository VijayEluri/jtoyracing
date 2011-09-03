package net.juniorbl.jtoyracing.entity.vehicle;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jmex.physics.PhysicsSpace;

/**
 * Simulated vehicle controlled by the computer. It is based on the Steering
 * Behaviors algorithms (http://www.red3d.com/cwr/steer/gdc99/).
 *
 * @author Carlos Luz Junior
 */
public class ComputerVehicle extends Vehicle {
	private Vector2f velocity;
	private Vector2f steerForce;
	private Vector2f position;

	public ComputerVehicle(PhysicsSpace physicsSpace, Vector2f initialPosition,
			ColorRGBA color) {
		super(physicsSpace, color);
		velocity = new Vector2f();
		steerForce = new Vector2f();
		position = initialPosition;
	}

	private void seek(Vector2f targetPosition) {
		Vector2f desiredVelocity = new Vector2f();
		desiredVelocity = targetPosition.subtract(position);
		desiredVelocity = desiredVelocity.normalize();
		steerForce = desiredVelocity.subtract(velocity);
	}

	public final void goTo(Vector2f targetPosition) {
		seek(targetPosition);
		Vector2f acceleration = new Vector2f();
		// Acceleration = Force / Mass
		acceleration = steerForce.divide(CHASSIS_MASS);
		velocity = velocity.add(acceleration);
		position = velocity.add(position);
		// accelerate(position.subtract(targetPosition).length());
	}

	public final Vector2f getCurrentPosition() {
		return position;
	}
}
