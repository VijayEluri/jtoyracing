package net.juniorbl.jtoyracing.entity.environment;

import java.util.ArrayList;
import java.util.List;

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

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 8801407867521059306L;

	/**
	 * The actual Track.
	 */
	private StaticPhysicsNode track;

	/**
	 * Checkpoints of the race track.
	 */
	private List<StaticPhysicsNode> checkPoints;

	/**
	 * Constructs a race track. It uses various states to build its components.
	 */
	public RaceTrack(PhysicsSpace physicsSpace, Float floorHeight) {
		Vector3f trackLocation = new Vector3f(20, floorHeight, 55);
		createTrack(physicsSpace, trackLocation);
		createCheckPoints(physicsSpace, trackLocation);
	}

	private void createTrack(PhysicsSpace physicsSpace, Vector3f trackLocation) {
		track = physicsSpace.createStaticNode();
		track.setLocalTranslation(trackLocation);
		track.setLocalScale(1f);
		track.attachChild(ModelUtil.convertMultipleModelToJME(ResourcesPath.MODELS_PATH + "obj/raceTrack.obj"));
		track.generatePhysicsGeometry(true);
		track.setMaterial(Material.IRON);
		this.attachChild(track);
	}

	/**
	 * Creates the checkpoints of the track. Checkpoints are some locations around a race track
	 * that recharge the health of the cars. It's the same location as the track because
	 * they are in the same 3D model.
	 */
	private void createCheckPoints(PhysicsSpace physicsSpace, Vector3f checkPointsLocation) {
		checkPoints = new ArrayList<StaticPhysicsNode>();
		StaticPhysicsNode bendThreeCheckPoint = createBendCheckpoint(physicsSpace, checkPointsLocation);
		checkPoints.add(bendThreeCheckPoint);
		this.attachChild(bendThreeCheckPoint);
		StaticPhysicsNode startCheckPoint = createStartCheckPoint(physicsSpace, checkPointsLocation);
		checkPoints.add(startCheckPoint);
		this.attachChild(startCheckPoint);
	}

	/**
	 * Bend #3 checkpoint.
	 */
	private StaticPhysicsNode createBendCheckpoint(PhysicsSpace physicsSpace, Vector3f checkPointsLocation) {
		StaticPhysicsNode bendThreeCheckPoint = physicsSpace.createStaticNode();
		bendThreeCheckPoint.setLocalTranslation(checkPointsLocation);
		bendThreeCheckPoint.attachChild(
				ModelUtil.convertModelSimpleObjToJME(
						ResourcesPath.MODELS_PATH + "obj/bendThreeCheckpoint.obj"));
		configureCheckPoint(bendThreeCheckPoint);
		return bendThreeCheckPoint;
	}

	/**
	 * Start checkpoint.
	 */
	private StaticPhysicsNode createStartCheckPoint(PhysicsSpace physicsSpace, Vector3f checkPointsLocation) {
		StaticPhysicsNode startCheckPoint = physicsSpace.createStaticNode();
		startCheckPoint.setLocalTranslation(checkPointsLocation);
		startCheckPoint.attachChild(
				ModelUtil.convertModelSimpleObjToJME(
						ResourcesPath.MODELS_PATH + "obj/startCheckPoint.obj"));
		configureCheckPoint(startCheckPoint);
		return startCheckPoint;
	}

	/**
	 * Configures the checkpoint so that there's no collision with it.
	 */
	private void configureCheckPoint(StaticPhysicsNode checkPoint) {
		checkPoint.generatePhysicsGeometry();
		checkPoint.setLocalScale(1f);
		checkPoint.setMaterial(Material.GHOST);
		StateUtil.makeTransparent(checkPoint);
	}

	/**
	 * Checks whether a vehicle reached a checkpoint.
	 */
	public final boolean isVehicleReachedCheckpoint(BoundingVolume vehicleBoundingVolume) {
		for (StaticPhysicsNode checkPoint : checkPoints) {
			if (vehicleBoundingVolume.intersects(checkPoint.getWorldBound())) {
				return true;
			}
		}
		return false;
	}
}
