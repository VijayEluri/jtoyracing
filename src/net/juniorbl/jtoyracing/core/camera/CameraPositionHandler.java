package net.juniorbl.jtoyracing.core.camera;


import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;

/**
 * Responsible for handle the position of the camera.
 *
 * @version 1.0 Nov 4, 2007
 * @author Carlos Luz Junior
 */
public class CameraPositionHandler implements InputActionInterface {

	/**
	 * The camera to positionate.
	 */
	private VehicleChaseCamera vehicleChaseCamera;

	/**
	 * Constructors a camera position handler.
	 *
	 * @param chaseCamera the chase camera.
	 */
	public CameraPositionHandler(VehicleChaseCamera chaseCamera) {
		this.vehicleChaseCamera = chaseCamera;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void performAction(InputActionEvent evt) {
		if (evt.getTriggerPressed()) {
			vehicleChaseCamera.changeVision();
		}
	}
}
