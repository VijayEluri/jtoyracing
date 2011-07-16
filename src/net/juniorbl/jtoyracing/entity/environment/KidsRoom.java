package net.juniorbl.jtoyracing.entity.environment;

import javax.swing.ImageIcon;

import net.juniorbl.jtoyracing.enums.GridPosition;
import net.juniorbl.jtoyracing.enums.ResourcesPath;
import net.juniorbl.jtoyracing.util.ModelUtil;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingVolume;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;
import com.jmex.terrain.TerrainBlock;
import com.jmex.terrain.util.MidPointHeightMap;

/**
 * A kid's room. First level of the game.
 *
 * @version 1.0 Aug 11, 2007
 * @author Carlos Luz Junior
 */
public class KidsRoom extends Node {

	private static final int ROOM_WIDTH = 162;

	private static final int ROOM_HEIGHT = 100;

	private static final int ROOM_LENGTH = 279;

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -4018480325870153949L;

	/**
	 * Scale of the room's texture.
	 */
	private static final Vector3f ROOM_TEXTURE_SCALE = new Vector3f(2f, 2f, 2f);

	/**
	 * Size of the floor.
	 */
	private static final Vector3f FLOOR_SIZE = new Vector3f(9, 0, 5.2f);

	/**
	 * Location of the room.
	 */
	private static final Vector3f LOCATION = new Vector3f(-66, -30, 28);

	/**
	 * Block of terrain where the room is.
	 */
	private TerrainBlock floorBlock;

	/**
	 * Race track of the room.
	 */
	private RaceTrack raceTrack;

	/**
	 * Constructs a Room. It uses physicsSpace states and renderer to build its components.
	 */
	public KidsRoom(PhysicsSpace physicsSpace, TextureState textureState) {
		createFloor(physicsSpace, textureState);
		createRaceTrack(physicsSpace);
		createWall();
		createRoomObjects(physicsSpace);
		this.setLocalTranslation(LOCATION);
	}

	private void createFloor(PhysicsSpace physicsSpace, TextureState textureState) {
		MidPointHeightMap mapHeight = new MidPointHeightMap(32, 5f);
		floorBlock = new TerrainBlock("terrain", mapHeight.getSize(),
				FLOOR_SIZE, mapHeight.getHeightMap(),
				new Vector3f(0, 0, 0), false);
		floorBlock.setModelBound(new BoundingBox());
		floorBlock.updateModelBound();

		StaticPhysicsNode floorNode = physicsSpace.createStaticNode();
		floorNode.attachChild(floorBlock);
		floorNode.generatePhysicsGeometry(true);
		floorNode.setMaterial(Material.WOOD);
		this.attachChild(floorNode);
		loadFloorTexture(textureState);
	}

	private void loadFloorTexture(TextureState textureState) {
		ImageIcon imagemTextura = new ImageIcon(
				KidsRoom.class.getClassLoader().getResource(ResourcesPath.TEXTURE_PATH + "roomFloor.jpg"));
		Texture texture = TextureManager.loadTexture(
				imagemTextura.getImage(), Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR, true);
		texture.setWrap(Texture.WM_WRAP_S_WRAP_T);
		texture.setScale(ROOM_TEXTURE_SCALE);
		textureState.setTexture(texture);
		floorBlock.setRenderState(textureState);
	}

	private void createRaceTrack(PhysicsSpace physicsSpace) {
		raceTrack = new RaceTrack(physicsSpace, getFloorHeight());
		this.attachChild(raceTrack);
	}

	private void createWall() {
		createLeftWall();
		createRightWall();
		createBackWall();
		createFrontWall();
	}

	private void createFrontWall() {
		Quad frontWall = new Quad("frontWall", ROOM_WIDTH, ROOM_HEIGHT);
		frontWall.setLocalTranslation(new Vector3f(278, getFloorHeight() + 50, 80));
		frontWall.setLocalRotation(ModelUtil.calculateRotation(270));
		this.attachChild(frontWall);
	}

	private void createBackWall() {
		Quad backWall = new Quad("backWall", ROOM_WIDTH, ROOM_HEIGHT);
		backWall.setLocalTranslation(new Vector3f(0, getFloorHeight() + 50, 81));
		backWall.setLocalRotation(ModelUtil.calculateRotation(90));
		this.attachChild(backWall);
	}

	private void createRightWall() {
		Quad rightWall = new Quad("rightWall", ROOM_LENGTH, ROOM_HEIGHT);
		rightWall.setLocalTranslation(new Vector3f(139, getFloorHeight() + 50, 161));
		rightWall.setLocalRotation(ModelUtil.calculateRotation(180));
		this.attachChild(rightWall);
	}

	private void createLeftWall() {
		Quad leftWall = new Quad("leftWall", ROOM_LENGTH, ROOM_HEIGHT);
		leftWall.setLocalTranslation(new Vector3f(139, getFloorHeight() + 50, 0));
		this.attachChild(leftWall);
	}

	/**
	 * Checks whether a vehicle reached a checkpoint of the race track.
	 */
	public final boolean isVehicleReachedCheckpoint(BoundingVolume vehicleBoundingVolume) {
		return raceTrack.isVehicleReachedCheckpoint(vehicleBoundingVolume);
	}

	public final float getFloorHeight() {
		return floorBlock.getHeight(new Vector3f());
	}

	/**
	 * Creates the lego doll in bend three.
	 */
	private void createLegoDoll(PhysicsSpace physicsSpace) {
		StaticPhysicsNode legoDoll = physicsSpace.createStaticNode();
		legoDoll.setLocalTranslation(new Vector3f(200, getFloorHeight() + 3.9f, 30));
		legoDoll.attachChild(ModelUtil.convertMultipleModelToJME(
				ResourcesPath.MODELS_PATH + "obj/legoDoll.obj"));
		final float legoDollScale = 2.5f;
		legoDoll.setLocalScale(legoDollScale);
		this.attachChild(legoDoll);
	}

	private void createRoomObjects(PhysicsSpace physicsSpace) {
		createBed(physicsSpace);
		createLegoDoll(physicsSpace);
	}

	private void createBed(PhysicsSpace physicsSpace) {
		StaticPhysicsNode bed = physicsSpace.createStaticNode();
		bed.setLocalTranslation(new Vector3f(235, getFloorHeight(), 130));
		bed.attachChild(ModelUtil.convertMultipleModelToJME(ResourcesPath.MODELS_PATH + "obj/bed.obj"));
		bed.setLocalScale(0.3f);
		this.attachChild(bed);
	}

	public final Vector3f getGridPosition(GridPosition position) {
		return raceTrack.getGridPosition(position, getFloorHeight());
	}
}
