package net.juniorbl.jtoyracing.core;

import static net.juniorbl.jtoyracing.entity.vehicle.Vehicle.MAX_VALUE_HEALTH;
import net.juniorbl.jtoyracing.action.camera.CameraPositionHandler;
import net.juniorbl.jtoyracing.action.vehicle.Steer;
import net.juniorbl.jtoyracing.action.vehicle.Traction;
import net.juniorbl.jtoyracing.audio.AudioConfig;
import net.juniorbl.jtoyracing.core.camera.VehicleChaseCamera;
import net.juniorbl.jtoyracing.core.hud.Info;
import net.juniorbl.jtoyracing.entity.environment.KidsRoom;
import net.juniorbl.jtoyracing.entity.vehicle.Vehicle;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.CullState;
import com.jmex.physics.util.SimplePhysicsGame;

/**
 * Start point of the game.
 *
 * @version 1.0 Aug 11, 2007
 * @author Carlos Luz Junior
 */
public final class JToyRacing extends SimplePhysicsGame implements ChronometerObserver, HealthObserver {

	/**
	 * Direction of left steer.
	 */
	private static final int LEFT_STEER_DIRECTION = -100;

	/**
	 * Direction of right steer.
	 */
	private static final int RIGHT_STEER_DIRECTION = 100;

	/**
	 * Velocity of the forward traction.
	 */
	private static final int FORWARD_TRACTION_VELOCITY = 50;

	/**
	 * Velocity of the backward traction.
	 */
	private static final int BACKWARD_TRACTION_VELOCITY = -50;

	/**
	 * Amount of seconds that a vehicle has to stop when its health ends.
	 */
	private static final int WAIT_SECONDS = 3;

	/**
	 * Location of the light.
	 */
	private static final Vector3f LIGHT_LOCATION = new Vector3f(0, 40, 0);

	/**
	 * Location of the vehicle in the race track.
	 */
	private static final float VEHICLE_RACE_TRACK_LOCATION = 28.2f;

	/**
	 * Location of the camera.
	 */
	private static final Vector3f CAMERA_LOCATION = new Vector3f(-40, 0, 163);

	/**
	 * Normal gravity.
	 */
	private static final Vector3f NORMAL_GRAVITY = new Vector3f(0, -45, 0);

	/**
	 * Amount of health that will be recharged when a vehicle stops.
	 */
	private static final int RECHARGE_HEALTH = 250;

	/**
	 * A camera that chase a vehicle.
	 */
	private VehicleChaseCamera vehicleChaseCamera;

	/**
	 * Vehicle of the game.
	 */
	private Vehicle vehicle;

	/**
	 * First level of the game.
	 */
	private KidsRoom kidsRoom;

	/**
	 * Information displayed on the screen.
	 */
	private Info info;

	/**
	 * Thread which monitors the health of the vehicles.
	 */
	private HealthMonitor healthMonitor;

	/**
	 * Chronometer that runs every time a vehicle stops because lack of health.
	 */
	private HealthChronometer healthChronometer;

	/**
	 * Audio configuration.
	 */
	private AudioConfig audio;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void simpleInitGame() {
		loadOptimization();
		loadEnvironment();
		loadVehicles();
		loadGravitation();
		loadCamera();
		loadKeyboardControllers();
		loadInfo();
		loadAudio();
		loadHealthMonitor();
	}

	/**
	 * Thread to monitor the health of the vehicles.
	 */
	private void loadHealthMonitor() {
		healthMonitor = new HealthMonitor(this);
		healthMonitor.start();
	}

	/**
	 * Loads the audio configuration.
	 */
	private void loadAudio() {
		this.audio = new AudioConfig(cam);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void simpleUpdate() {
		super.simpleUpdate();
		vehicleChaseCamera.update(tpf);
		audio.update();
		updateVehicleEngineSound();
	}

	private void updateVehicleEngineSound() {
		vehicle.updateEngineSound();
	}

	public void updateVehiclesHealth() {
		// When a vehicle reach a checkpoint, its health is recharged
		if (kidsRoom.isVehicleReachedCheckpoint(vehicle.getWorldBound())) {
			info.setHealthBarValue(vehicle.rechargeHealth(MAX_VALUE_HEALTH));
		}
		info.setHealthBarValue(vehicle.decreaseHealth());
	}

	private void loadVehicles() {
		float florHeight = kidsRoom.getFloorHeight() - VEHICLE_RACE_TRACK_LOCATION;
		Vector3f vehicleLocation = new Vector3f(-40, florHeight, 120);
		vehicle = new Vehicle(getPhysicsSpace(), vehicleLocation);
		vehicle.addObserver(this);
		rootNode.attachChild(vehicle);
	}

	private void loadGravitation() {
		getPhysicsSpace().setDirectionalGravity(NORMAL_GRAVITY);
	}

	public static void main(String[] args) {
		JToyRacing game = new JToyRacing();
		game.setDialogBehaviour(NEVER_SHOW_PROPS_DIALOG);
		game.start();
	}

	private void loadOptimization() {
		// TODO change the key to display debug mode, now is 'V'.
		// Don't display the back of the triangles.
		CullState cullState = display.getRenderer().createCullState();
		cullState.setCullMode(CullState.CS_BACK);
		rootNode.setRenderState(cullState);
	}

	private void loadKeyboardControllers() {
		input.addAction(new Traction(vehicle, BACKWARD_TRACTION_VELOCITY), InputHandler.DEVICE_KEYBOARD,
				KeyInput.KEY_UP, InputHandler.AXIS_NONE, false);
		input.addAction(new Traction(vehicle, FORWARD_TRACTION_VELOCITY), InputHandler.DEVICE_KEYBOARD,
				KeyInput.KEY_DOWN, InputHandler.AXIS_NONE, false);
		input.addAction(new Steer(vehicle, LEFT_STEER_DIRECTION),
				InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_LEFT,
				InputHandler.AXIS_NONE, false);
		input.addAction(new Steer(vehicle, RIGHT_STEER_DIRECTION),
				InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_RIGHT,
				InputHandler.AXIS_NONE, false);

		CameraPositionHandler cameraPositionHandler = new CameraPositionHandler(vehicleChaseCamera);
		input.addAction(cameraPositionHandler, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_V, InputHandler.AXIS_NONE, false);
	}

	/**
	 * Loads screen information system.
	 */
	private void loadInfo() {
		info = new Info(display.getRenderer(), vehicle.getHealth());
		// Use fpsNone node to display the characters.
		fpsNode.attachChild(info);
	}

	/**
	 * Loads the camera which will follow the vehicle.
	 */
	private void loadCamera() {
		vehicleChaseCamera = (VehicleChaseCamera) VehicleChaseCamera.getInstance(cam, vehicle.getChassis());
		cam.setLocation(CAMERA_LOCATION);
	}

	/**
	 * Loads the environment where the race takes place.
	 */
	private void loadEnvironment() {
		loadRoom();
		loadLight();
	}

	private void loadLight() {
		PointLight light = new PointLight();
		light.setLocation(LIGHT_LOCATION);
		//Shadow
		light.setAmbient(ColorRGBA.gray);
		light.setDiffuse(ColorRGBA.white);
		//Reflex color
		light.setSpecular(ColorRGBA.lightGray);
		light.setEnabled(true);
		lightState.attach(light);
		rootNode.setRenderState(lightState);
	}

	/**
	 * Loads the room, first level of the game.
	 */
	private void loadRoom() {
		kidsRoom = new KidsRoom(getPhysicsSpace(), display.getRenderer());
		kidsRoom.attachChild(vehicle);
		rootNode.attachChild(kidsRoom);
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateTime(int seconds) {
		// TODO use internalization
		info.printMessage("Time left: " + String.valueOf(seconds) + " seconds");
	}

	/**
	 * {@inheritDoc}
	 */
	public void timeUP() {
		info.printMessage(String.valueOf(""));
		info.setHealthBarValue(vehicle.rechargeHealth(RECHARGE_HEALTH));
		healthChronometer.stopChronometer();
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateHealth() {
		updateVehiclesHealth();
	}

	/**
	 * {@inheritDoc}
	 */
	public void healthEnded() {
		//TODO player that wins the challenge at the beginning may win one more second
		healthChronometer = new HealthChronometer(WAIT_SECONDS);
		healthChronometer.addObserver(this);
		healthChronometer.start();
	}
}
