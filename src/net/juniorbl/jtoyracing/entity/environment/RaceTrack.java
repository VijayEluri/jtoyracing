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
	private Vector3f trackLocation;
	private PhysicsSpace physicsSpace;

	public RaceTrack(PhysicsSpace physicsSpace, Float floorHeight) {
		trackLocation = new Vector3f(20, floorHeight, 55);
		this.physicsSpace = physicsSpace;
		createTrack(trackLocation);
		createCheckPoints(trackLocation);
	}

	private void createTrack(Vector3f trackLocation) {
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
	private void createCheckPoints(Vector3f checkPointsLocation) {
		checkPoints = new ArrayList<StaticPhysicsNode>();
		StaticPhysicsNode bendThreeCheckPoint = loadCheckpointModel("obj/bendThreeCheckpoint.obj");
		checkPoints.add(bendThreeCheckPoint);
		this.attachChild(bendThreeCheckPoint);
		StaticPhysicsNode startCheckPoint = loadCheckpointModel("obj/startCheckPoint.obj");
		checkPoints.add(startCheckPoint);
		this.attachChild(startCheckPoint);
	}

	private StaticPhysicsNode loadCheckpointModel(String modelPath) {
		StaticPhysicsNode checkPoint = physicsSpace.createStaticNode();
		checkPoint.setLocalTranslation(trackLocation);
		checkPoint.attachChild(ModelUtil.convertOBJToStatial(ResourcesPath.MODELS_PATH + modelPath));
		checkPoint.setLocalScale(1f);
		checkPoint.setMaterial(Material.GHOST);
		StateUtil.makeTransparent(checkPoint);
		return checkPoint;
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
