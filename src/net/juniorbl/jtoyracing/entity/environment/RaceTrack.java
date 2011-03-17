package net.juniorbl.jtoyracing.entity.environment;

import java.util.ArrayList;
import java.util.List;

import net.juniorbl.jtoyracing.enums.ResourcesPath;
import net.juniorbl.jtoyracing.util.ModelUtil;

import com.jme.bounding.BoundingVolume;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
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
	public RaceTrack(PhysicsSpace physicsSpace, Vector3f trackLocation, Renderer renderer) {
		createTrack(physicsSpace, trackLocation);
		createCheckPoints(physicsSpace, trackLocation, renderer);
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
	private void createCheckPoints(PhysicsSpace physicsSpace, Vector3f checkPointsLocation, Renderer renderer) {
		checkPoints = new ArrayList<StaticPhysicsNode>();
		//bend #3 checkpoint
		StaticPhysicsNode bendThreeCheckPoint = physicsSpace.createStaticNode();
		bendThreeCheckPoint.setLocalTranslation(checkPointsLocation);
		bendThreeCheckPoint.attachChild(
				ModelUtil.convertModelSimpleObjToJME(
						ResourcesPath.MODELS_PATH + "obj/bendThreeCheckpoint.obj"));
		configureCheckPoint(bendThreeCheckPoint);
		checkPoints.add(bendThreeCheckPoint);
		//start checkpoint
		StaticPhysicsNode startCheckPoint = physicsSpace.createStaticNode();
		startCheckPoint.setLocalTranslation(checkPointsLocation);
		startCheckPoint.attachChild(
				ModelUtil.convertModelSimpleObjToJME(
						ResourcesPath.MODELS_PATH + "obj/startCheckPoint.obj"));
		configureCheckPoint(startCheckPoint);
		checkPoints.add(startCheckPoint);

		configureCheckPointsStates(renderer);
		this.attachChild(bendThreeCheckPoint);
		this.attachChild(startCheckPoint);
	}

	/**
	 * Configures the checkpoint so that there's no collision with it.
	 */
	private void configureCheckPoint(StaticPhysicsNode checkPoint) {
		checkPoint.generatePhysicsGeometry();
		checkPoint.setLocalScale(1f);
		checkPoint.setMaterial(Material.GHOST);
	}

	/**
	 * Configures the states of the checkpoints. TextureState is used along with alphastate to set them transparent.
	 */
	private void configureCheckPointsStates(Renderer renderer) {
		AlphaState alphaState = renderer.createAlphaState();
		TextureState textureState = renderer.createTextureState();
		for (StaticPhysicsNode checkPoint : checkPoints) {
			Texture texture = TextureManager.loadTexture(getClass().getClassLoader()
					.getResource(ResourcesPath.TEXTURE_PATH + "transparency.png"),
					Texture.MM_LINEAR, Texture.FM_LINEAR, 1.0f, true);
			texture.setWrap(Texture.WM_WRAP_S_WRAP_T);
			texture.setScale(new Vector3f(1f, 1f, 1f));
			textureState.setTexture(texture);
			checkPoint.setRenderState(textureState);

			alphaState.setBlendEnabled(true);
			alphaState.setSrcFunction(AlphaState.SB_SRC_ALPHA);
			alphaState.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
			alphaState.setTestEnabled(false);
			alphaState.setEnabled(true);
			checkPoint.setRenderState(alphaState);
		}
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
