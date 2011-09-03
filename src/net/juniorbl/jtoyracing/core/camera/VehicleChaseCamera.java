package net.juniorbl.jtoyracing.core.camera;

import java.util.HashMap;

import com.jme.input.ChaseCamera;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Spatial;

/**
 * Camera that follows a vehicle.
 *
 * @version 1.0 Jun 22, 2008
 * @author Carlos Luz Junior
 */
public final class VehicleChaseCamera extends ChaseCamera {
	private static final int FAR_DISTANCE = 50;
	private static final int CLOSE_DISTANCE = 30;
	private static ChaseCamera vehicleChaseCamera;

	private VehicleChaseCamera(Camera camera, Spatial vehicleToFollow, HashMap properties) {
		super(camera, vehicleToFollow, properties);
	}

	public static ChaseCamera getInstance(Camera camera, Spatial vehicleToFollow) {
		HashMap properties = loadProperties();
		vehicleChaseCamera = new VehicleChaseCamera(camera, vehicleToFollow, properties);
		vehicleChaseCamera.setMaxDistance(FAR_DISTANCE);
		vehicleChaseCamera.setMinDistance(FAR_DISTANCE);
		vehicleChaseCamera.setStayBehindTarget(true);
		return vehicleChaseCamera;
	}

	private static HashMap<String, Object> loadProperties() {
		Vector3f targetOffset = new Vector3f();
		targetOffset.y = 4;
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
		properties.put(ChaseCamera.PROP_INITIALSPHERECOORDS, new Vector3f(15, 0, 30 * FastMath.DEG_TO_RAD));
		properties.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset);
		return properties;
	}

	public void changeVision() {
		if (vehicleChaseCamera != null) {
			if (vehicleChaseCamera.getMaxDistance() == FAR_DISTANCE) {
				vehicleChaseCamera.setMaxDistance(CLOSE_DISTANCE);
				vehicleChaseCamera.setMinDistance(CLOSE_DISTANCE);
			} else {
				vehicleChaseCamera.setMaxDistance(FAR_DISTANCE);
				vehicleChaseCamera.setMinDistance(FAR_DISTANCE);
			}
		}
	}
}
