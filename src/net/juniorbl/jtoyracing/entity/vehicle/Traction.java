package net.juniorbl.jtoyracing.entity.vehicle;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;

/**
 * Traction of a car. It's responsible for the forward and backward movement.
 *
 * @version 1.0 Sep 7, 2007
 * @author Carlos Luz Junior
 */
public class Traction implements InputActionInterface {
	private Vehicle vehicle;
	private float velocity;

	public Traction(Vehicle vehicle, float velocity) {
		this.vehicle = vehicle;
		this.velocity = velocity;
	}

	/**
	 * Moves the vehicle forward and back.
	 */
	public final void performAction(InputActionEvent evt) {
		if (evt.getTriggerPressed()) {
			vehicle.accelerate(velocity);
		} else {
			vehicle.stop();
		}
	}
}
