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

	/**
	 * Vehicle that will receive the traction.
	 */
	private Vehicle vehicle;

	/**
	 * Value of the velocity.
	 */
	private float velocity;

	/**
	 * Constructs a traction action.
	 *
	 * @param vehicle the vehicle.
	 * @param velocity the value of the velocity.
	 */
	public Traction(Vehicle vehicle, float velocity) {
		this.vehicle = vehicle;
		this.velocity = velocity;
	}

	/**
	 * Moves the vehicle forward and back.
	 *
	 * @param evt the event.
	 */
	public final void performAction(InputActionEvent evt) {
		if (evt.getTriggerPressed()) {
			vehicle.accelerate(velocity);
		} else {
			vehicle.stop();
		}
	}
}