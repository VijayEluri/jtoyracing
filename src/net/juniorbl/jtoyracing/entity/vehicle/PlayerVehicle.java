package net.juniorbl.jtoyracing.entity.vehicle;

import java.util.ArrayList;
import java.util.List;

import net.juniorbl.jtoyracing.core.audio.AudioConfig;
import net.juniorbl.jtoyracing.core.monitor.Health;
import net.juniorbl.jtoyracing.core.monitor.HealthObserver;
import net.juniorbl.jtoyracing.enums.ResourcesPath;
import net.juniorbl.jtoyracing.util.ModelUtil;
import net.juniorbl.jtoyracing.util.StateUtil;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jmex.audio.AudioTrack;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.material.Material;

/**
 * A vehicle of the game. May be a superclass of various kids of vehicles in the future.
 *
 * @version 1.0 Aug 11, 2007
 * @author Carlos Luz Junior
 */
public class PlayerVehicle extends Node implements Health {

	public static final float CHASSIS_MASS = 40;

	public static final int MAX_HEALTH_VALUE = 500;

	private static final float CHASSIS_COLLISION_BOX_SCALE = 0.5f;

	private static final int MIN_HEALTH_VALUE = 0;

	private static final long serialVersionUID = 2049305191562715341L;

	private static final int DECREASE_HEALTH_VALUE = 25;

	private static final float CHASSIS_SCALE = 1f;

	private static final Vector3f FRONT_SUSPENSION_LOCATION = new Vector3f(-.2f, -1.7f, 0);

	private static final Vector3f REAR_SUSPENSION_LOCATION = new Vector3f(3.2f, -1.6f, 0);

	private DynamicPhysicsNode chassis;

	private List <HealthObserver> healthObservers = new ArrayList<HealthObserver>();

	private int health;

	private Suspension rearSuspension;

	private Suspension frontSuspension;

	private AudioTrack engineSound;

	private Quaternion rotationQuaternion = new Quaternion();

	private PhysicsSpace physicsSpace;

	public PlayerVehicle(PhysicsSpace physicsSpace, ColorRGBA color) {
		this.physicsSpace = physicsSpace;
		health = MAX_HEALTH_VALUE;
		createChassis(color);
		createSuspension();
		loadEngineSound();
	}

	private void createChassis(ColorRGBA color) {
		chassis = physicsSpace.createDynamicNode();
		Box chassisCollisionBox = createCollisionBox();
		chassis.attachChild(chassisCollisionBox);
		chassis.generatePhysicsGeometry(true);
		applyColor(color);
		chassis.setMaterial(Material.IRON);
		chassis.setLocalScale(CHASSIS_SCALE);
		chassis.setMass(CHASSIS_MASS);
		this.attachChild(chassis);
	}

	/**
	 * FIXME don't use two model, see how to change a DynamicNode's color.
	 *
	 * FIXME move to StateUtil.
	 */
	private void applyColor(ColorRGBA color) {
		if (ColorRGBA.red.equals(color)) {
			chassis.attachChild(
					ModelUtil.convertOBJToStatial(ResourcesPath.MODELS_PATH + "obj/redTruck.obj"));
		} else if (ColorRGBA.blue.equals(color)) {
			chassis.attachChild(
					ModelUtil.convertOBJToStatial(ResourcesPath.MODELS_PATH + "obj/blueTruck.obj"));
		}
	}

	/**
	 * Creates the collision box that is used to simulate the chassis collision.
	 */
	private Box createCollisionBox() {
		final Vector3f collisionBoxLocation = new Vector3f(3, .8f, 1.1f);
		final float xSize = 5.9f;
		final int ySize = 1;
		final float zSize = 1.5f;
		Box chassisCollisionBox = new Box("chassisCollisionBox", collisionBoxLocation, xSize, ySize, zSize);
		StateUtil.makeTransparent(chassisCollisionBox);
		chassisCollisionBox.setLocalScale(CHASSIS_COLLISION_BOX_SCALE);
		return chassisCollisionBox;
	}

	private void createSuspension() {
		frontSuspension = new Suspension(physicsSpace, chassis, FRONT_SUSPENSION_LOCATION);
		this.attachChild(frontSuspension);
		rearSuspension = new Suspension(physicsSpace, chassis, REAR_SUSPENSION_LOCATION);
		this.attachChild(rearSuspension);
	}

	public final void rotateUponItself(Float radians) {
		// FIXME rotateUpTo should do this but it doesn't work.
		rotationQuaternion.fromAngleNormalAxis(radians, Vector3f.UNIT_Y);
	    this.getLocalRotation().multLocal(rotationQuaternion);
	}

	public final void accelerate(float desiredVelocity) {
		if (hasHealth()) {
			rearSuspension.accelerate(desiredVelocity);
			frontSuspension.accelerate(desiredVelocity);
		}
	}

	public final void stop() {
		if (hasHealth()) {
			rearSuspension.stop();
			frontSuspension.stop();
		}
	}

	private boolean hasHealth() {
		return health > MIN_HEALTH_VALUE;
	}

	/**
	 * FIXME turn up the volume is not correct
	 */
	public final void updateEngineSound() {
		engineSound.setVolume((getSpeed() / 40) + .5f);
	}

	private float getSpeed() {
		return chassis.getLinearVelocity(Vector3f.ZERO).length();
	}

	public final void steer(float direction) {
		frontSuspension.steer(direction);
	}

	public final void unsteer() {
		frontSuspension.unsteer();
	}

	public final DynamicPhysicsNode getChassis() {
		return this.chassis;
	}

//	/**
//	 * Changes the location of the vehicle.
//	 *
//	 * @param location the new location.
//	 */
//	public void changeLocation(Vector3f location) {
//		//TODO http://www.jmonkeyengine.com/jmeforum/index.php?topic=5837.msg47229#msg47229
//	}

	public final int getHealth() {
		return health;
	}

	public final int decreaseHealth() {
		health -= DECREASE_HEALTH_VALUE;
		if (!hasHealth()) {
			rearSuspension.stop();
			frontSuspension.stop();
			engineSound.stop();
			notifyObserversHealthEnded();
		}
		return health;
	}

	public final int rechargeHealth(int healthAmount) {
		health = healthAmount;
		if (!engineSound.isPlaying()) {
			engineSound.play();
		}
		return health;
	}

	private void loadEngineSound() {
		engineSound = AudioConfig.loadSoundEffect(ResourcesPath.AUDIO_PATH + "engine.ogg");
		engineSound.play();
	}

	public final void addObserver(final HealthObserver observadorEnergia) {
		this.healthObservers.add(observadorEnergia);
	}

	public final void removeObserver(HealthObserver observadorEnergia) {
		healthObservers.remove(observadorEnergia);
	}

	public final void notifyObserversHealthEnded() {
		for (HealthObserver healthObserver : this.healthObservers) {
			healthObserver.healthEnded();
		}
	}
}
