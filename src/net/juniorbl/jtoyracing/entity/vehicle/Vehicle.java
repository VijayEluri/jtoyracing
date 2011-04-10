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
public class Vehicle extends Node implements Health {

	/**
	 * Mass of the chassis.
	 */
	public static final float CHASSIS_MASS = 40;

	/**
	 * Maximum health value.
	 */
	public static final int MAX_VALUE_HEALTH = 500;

	/**
	 * Scale of the collision box of the chassis.
	 */
	private static final float CHASSIS_COLLISION_BOX_SCALE = 0.5f;

	/**
	 * Minimum health value.
	 */
	private static final int MIN_VALUE_HEALTH = 0;

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 2049305191562715341L;

	/**
	 * Amount of health that is decreased.
	 */
	private static final int DECREASE_HEALTH_VALUE = 25;

	/**
	 * Scale of the chassis.
	 */
	private static final float CHASSIS_SCALE = 1f;

	/**
	 * Location of the front suspension.
	 */
	private static final Vector3f FRONT_SUSPENSION_LOCATION = new Vector3f(-.2f, -1.7f, 0);

	/**
	 * Location of the rear suspension.
	 */
	private static final Vector3f REAR_SUSPENSION_LOCATION = new Vector3f(3.2f, -1.6f, 0);

	/**
	 * Chassis of the vehicle.
	 */
	private DynamicPhysicsNode chassis;

	/**
	 * Health observers.
	 */
	private List <HealthObserver> healthObservers = new ArrayList<HealthObserver>();

	/**
	 * Health of the vehicle. The health decreases with time and is recharged when a checkpoint is reached.
	 */
	private int health;

	/**
	 * Rear suspension.
	 */
	private Suspension rearSuspension;

	/**
	 * Front suspension.
	 */
	private Suspension frontSuspension;

	/**
	 * Sound of the engine.
	 */
	private AudioTrack engineSound;

	/**
	 * Quaternion to apply rotation on the vehicle.
	 */
	private Quaternion rotationQuaternion = new Quaternion();

	public Vehicle(PhysicsSpace physicsSpace, ColorRGBA color) {
		health = MAX_VALUE_HEALTH;
		createChassis(physicsSpace, color);
		createSuspension(physicsSpace);
		loadEngineSound();
	}

	private void createChassis(PhysicsSpace physicsSpace, ColorRGBA color) {
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
					ModelUtil.convertMultipleModelToJME(ResourcesPath.MODELS_PATH + "obj/redTruck.obj"));
		} else if (ColorRGBA.blue.equals(color)) {
			chassis.attachChild(
					ModelUtil.convertMultipleModelToJME(ResourcesPath.MODELS_PATH + "obj/blueTruck.obj"));
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

	private void createSuspension(PhysicsSpace physicsSpace) {
		frontSuspension = new Suspension(physicsSpace, chassis, FRONT_SUSPENSION_LOCATION);
		this.attachChild(frontSuspension);
		rearSuspension = new Suspension(physicsSpace, chassis, REAR_SUSPENSION_LOCATION);
		this.attachChild(rearSuspension);
	}

	public final void rotateUponItself(Float value) {
		// FIXME rotateUpTo should do this but it doesn't work.
		rotationQuaternion.fromAngleNormalAxis(value, Vector3f.UNIT_Y);
	    this.getLocalRotation().multLocal(rotationQuaternion);
	}

	public final void accelerate(float desiredVelocity) {
		if (hasHealth()) {
			rearSuspension.accelerate(desiredVelocity);
			frontSuspension.accelerate(desiredVelocity);
			if (!engineSound.isPlaying()) {
				engineSound.play();
			}
		} else {
			engineSound.stop();
		}
	}

	public final void stop() {
		if (hasHealth()) {
			rearSuspension.stop();
			frontSuspension.stop();
			if (!engineSound.isPlaying()) {
				engineSound.play();
			}
		} else {
			engineSound.stop();
		}
	}

	/**
	 * Verifies whether there is health available or not.
	 */
	private boolean hasHealth() {
		return health > MIN_VALUE_HEALTH;
	}

	/**
	 * Update the engine sound depending on the speed.
	 *
	 * FIXME turn up the volume is not correct
	 */
	public final void updateEngineSound() {
		engineSound.setVolume((getSpeed() / 40) + .5f);
	}

	/**
	 * Returns the current speed.
	 */
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

	/**
	 * Decreases the health by a value that depends on the vehicle.
	 */
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

	/**
	 * {@inheritDoc}
	 */
	public final void addObserver(final HealthObserver observadorEnergia) {
		this.healthObservers.add(observadorEnergia);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void removeObserver(HealthObserver observadorEnergia) {
		healthObservers.remove(observadorEnergia);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void notifyObserversHealthEnded() {
		for (HealthObserver healthObserver : this.healthObservers) {
			healthObserver.healthEnded();
		}
	}
}
