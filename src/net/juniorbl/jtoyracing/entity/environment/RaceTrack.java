package net.juniorbl.jtoyracing.entity.environment;

import java.util.ArrayList;
import java.util.List;

import net.juniorbl.jtoyracing.enums.GridPosition;
import net.juniorbl.jtoyracing.enums.ResourcesPath;
import net.juniorbl.jtoyracing.util.ModelUtil;
import net.juniorbl.jtoyracing.util.StateUtil;

import com.jme.bounding.BoundingVolume;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

/**
 * Place where the race happens.
 *
 * @version 1.0 Oct 2, 2007
 * @author Carlos Luz Junior
 */
public class RaceTrack extends Node {

	public static final float VEHICLE_X_LOCATION = 28.2f;

	private static final int X_LOCATION_FIRST_GRID_POSITION = -50;

	private static final int X_LOCATION_SECOND_GRID_POSITION = -40;

	private static final long serialVersionUID = 8801407867521059306L;

	private StaticPhysicsNode track;

	private List<StaticPhysicsNode> checkPoints;

	public RaceTrack(PhysicsSpace physicsSpace, Float floorHeight) {
		Vector3f trackLocation = new Vector3f(20, floorHeight, 55);
		createTrack(physicsSpace, trackLocation);
		createCheckPoints(physicsSpace, trackLocation);
	}

	private void createTrack(PhysicsSpace physicsSpace, Vector3f trackLocation) {
		track = physicsSpace.createStaticNode();
		track.setLocalTranslation(trackLocation);
		track.setLocalScale(1f);
		track.attachChild(ModelUtil.convertOBJToStatial(ResourcesPath.MODELS_PATH + "obj/raceTrack.obj"));
		track.generatePhysicsGeometry(true);
		track.setMaterial(Material.IRON);
		this.attachChild(track);
	}

	/**
	 * Creates the checkpoints of the track. Checkpoints are some locations around a race track
	 * that recharge the health of the vehicles.
	 */
	private void createCheckPoints(PhysicsSpace physicsSpace, Vector3f checkPointsLocation) {
		checkPoints = new ArrayList<StaticPhysicsNode>();
		StaticPhysicsNode bendThreeCheckPoint = createBendThreeCheckpoint(physicsSpace, checkPointsLocation);
		checkPoints.add(bendThreeCheckPoint);
		this.attachChild(bendThreeCheckPoint);
		StaticPhysicsNode startCheckPoint = createStartCheckPoint(physicsSpace, checkPointsLocation);
		checkPoints.add(startCheckPoint);
		this.attachChild(startCheckPoint);
	}

	private StaticPhysicsNode createBendThreeCheckpoint(PhysicsSpace physicsSpace, Vector3f checkPointsLocation) {
		StaticPhysicsNode bendThreeCheckPoint = physicsSpace.createStaticNode();
		bendThreeCheckPoint.setLocalTranslation(checkPointsLocation);
		bendThreeCheckPoint.attachChild(
				ModelUtil.convertOBJToStatial(ResourcesPath.MODELS_PATH + "obj/bendThreeCheckpoint.obj"));
		configureCheckPoint(bendThreeCheckPoint);
		return bendThreeCheckPoint;
	}

	private StaticPhysicsNode createStartCheckPoint(PhysicsSpace physicsSpace, Vector3f checkPointsLocation) {
		StaticPhysicsNode startCheckPoint = physicsSpace.createStaticNode();
		startCheckPoint.setLocalTranslation(checkPointsLocation);
		startCheckPoint.attachChild(
				ModelUtil.convertOBJToStatial(ResourcesPath.MODELS_PATH + "obj/startCheckPoint.obj"));
		configureCheckPoint(startCheckPoint);
		return startCheckPoint;
	}

	private void configureCheckPoint(StaticPhysicsNode checkPoint) {
		checkPoint.setLocalScale(1f);
		checkPoint.setMaterial(Material.GHOST);
		StateUtil.makeTransparent(checkPoint);
	}

	public final boolean isVehicleReachedCheckpoint(BoundingVolume vehicleBoundingVolume) {
		for (StaticPhysicsNode checkPoint : checkPoints) {
			if (vehicleBoundingVolume.intersects(checkPoint.getWorldBound())) {
				return true;
			}
		}
		return false;
	}

	public final Vector3f getGridPosition(GridPosition position, Float floorHeight) {
		float yLocation = floorHeight - VEHICLE_X_LOCATION;
		final Vector3f gridPosition = new Vector3f(0, yLocation, 120);
		if (GridPosition.FIRST.equals(position)) {
			gridPosition.setX(X_LOCATION_FIRST_GRID_POSITION);
		} else if (GridPosition.SECOND.equals(position)) {
			gridPosition.setX(X_LOCATION_SECOND_GRID_POSITION);
		}
		return gridPosition;
	}
}
