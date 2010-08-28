package net.juniorbl.jtoyracing.action.vehicle;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;
import net.juniorbl.jtoyracing.entity.vehicle.Vehicle;

/**
 * Steering action of a vehicle.
 *
 * @version 1.0 Sep 7, 2007
 * @author Carlos Luz Junior
 */
public class Steer implements InputActionInterface {

	/**
	 * A vehicle.
	 */
	private Vehicle vehicle;

	/**
	 * The direction of the steer, right or left.
	 */
	private float direction;

	/**
	 * Constructs a steer action.
	 *
	 * @param vehicle the vehicle.
	 * @param direction the desired direction.
	 */
	public Steer(Vehicle vehicle, float direction) {
		this.vehicle = vehicle;
		this.direction = direction;
	}

	/**
	 * Steers the vehicle right or left.
	 *
	 * @param event the event.
	 */
	public final void performAction(InputActionEvent event) {
		if (event.getTriggerPressed()) {
			vehicle.steer(direction);
		} else {
			vehicle.unsteer();
		}
	}
}
