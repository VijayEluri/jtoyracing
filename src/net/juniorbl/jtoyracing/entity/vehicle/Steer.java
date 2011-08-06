package net.juniorbl.jtoyracing.entity.vehicle;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;

/**
 * Steering action of a vehicle.
 *
 * @version 1.0 Sep 7, 2007
 * @author Carlos Luz Junior
 */
public class Steer implements InputActionInterface {

	private PlayerVehicle vehicle;

	/**
	 * The direction of the steer, right or left.
	 */
	private float direction;

	public Steer(PlayerVehicle vehicle, float direction) {
		this.vehicle = vehicle;
		this.direction = direction;
	}

	/**
	 * Steers the vehicle.
	 */
	public final void performAction(InputActionEvent event) {
		if (event.getTriggerPressed()) {
			vehicle.steer(direction);
		} else {
			vehicle.unsteer();
		}
	}
}
