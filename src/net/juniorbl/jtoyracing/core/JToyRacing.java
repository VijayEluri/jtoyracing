package net.juniorbl.jtoyracing.core;

import static net.juniorbl.jtoyracing.entity.vehicle.PlayerVehicle.MAX_VALUE_HEALTH;
import net.juniorbl.jtoyracing.core.audio.AudioConfig;
import net.juniorbl.jtoyracing.core.camera.CameraPositionHandler;
import net.juniorbl.jtoyracing.core.camera.VehicleChaseCamera;
import net.juniorbl.jtoyracing.core.hud.Info;
import net.juniorbl.jtoyracing.core.monitor.ChronometerObserver;
import net.juniorbl.jtoyracing.core.monitor.HealthChronometer;
import net.juniorbl.jtoyracing.core.monitor.HealthMonitor;
import net.juniorbl.jtoyracing.core.monitor.HealthObserver;
import net.juniorbl.jtoyracing.entity.environment.KidsRoom;
import net.juniorbl.jtoyracing.entity.vehicle.ComputerVehicle;
import net.juniorbl.jtoyracing.entity.vehicle.Steer;
import net.juniorbl.jtoyracing.entity.vehicle.Traction;
import net.juniorbl.jtoyracing.entity.vehicle.PlayerVehicle;
import net.juniorbl.jtoyracing.enums.GridPosition;
import net.juniorbl.jtoyracing.util.StateUtil;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.light.PointLight;
import com.jme.math.Vector2f;
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

	private static final int LEFT_STEER_DIRECTION = -100;

	private static final int RIGHT_STEER_DIRECTION = 100;

	private static final int FORWARD_TRACTION_VELOCITY = 50;

	private static final int BACKWARD_TRACTION_VELOCITY = -50;

	private static final int WAIT_SECONDS = 3;

	private static final Vector3f LIGHT_LOCATION = new Vector3f(0, 40, 0);

	private static final Vector3f CAMERA_LOCATION = new Vector3f(-40, 0, 163);

	private static final Vector3f NORMAL_GRAVITY = new Vector3f(0, -45, 0);

	private static final int RECHARGE_HEALTH = 250;

	private VehicleChaseCamera vehicleChaseCamera;

	private PlayerVehicle playerVehicle;

	private ComputerVehicle computerVehicle;

	private KidsRoom kidsRoom;

	private Info info;

	private HealthMonitor healthMonitor;

	private HealthChronometer healthChronometer;

	private AudioConfig audio;

	@Override
	protected void simpleInitGame() {
		loadUtil();
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

	@Override
	protected void simpleUpdate() {
		super.simpleUpdate();
		vehicleChaseCamera.update(tpf);
		audio.update();
		updateEngineSounds();
		updateComputerVehiclePosition();
	}

	private void loadUtil() {
		StateUtil.setRenderer(display.getRenderer());
	}

	private void loadHealthMonitor() {
		healthMonitor = new HealthMonitor(this);
		healthMonitor.start();
	}

	private void loadAudio() {
		this.audio = new AudioConfig(cam);
	}

	private void updateComputerVehiclePosition() {
		// test target: -40f, -28.2f, 70f (just a few meters in front of the starting line)
		Vector2f testTarget = new Vector2f(-40, 70);
		computerVehicle.goTo(testTarget);
		Vector3f newPosition = new Vector3f(computerVehicle.getCurrentPosition().x,
				kidsRoom.getGridPosition(GridPosition.SECOND).getY(),
				computerVehicle.getCurrentPosition().y);
		computerVehicle.setLocalTranslation(newPosition);
	}

	private void updateEngineSounds() {
		playerVehicle.updateEngineSound();
		computerVehicle.updateEngineSound();
	}

	public void updateVehiclesHealth() {
		// When a vehicle reach a checkpoint, its health is recharged
		if (kidsRoom.isVehicleReachedCheckpoint(playerVehicle.getWorldBound())) {
			info.setHealthBarValue(playerVehicle.rechargeHealth(MAX_VALUE_HEALTH));
		}
		info.setHealthBarValue(playerVehicle.decreaseHealth());
	}

	private void loadVehicles() {
		loadPlayerVehicle();
		loadComputerVehicle();
	}

	private void loadComputerVehicle() {
		// The initial position uses the Z axis instead of the Y because in a 3D space the X and Z axes
		// are in the plane of the ground, the Y axis is the "up" and it's used for gravity
		// (from "Artificial Intelligence for Games" by Ian Millington).
		Vector2f initialPosition = new Vector2f(
				kidsRoom.getGridPosition(GridPosition.SECOND).getX(),
				kidsRoom.getGridPosition(GridPosition.SECOND).getZ());
		computerVehicle = new ComputerVehicle(getPhysicsSpace(), initialPosition, ColorRGBA.blue);
		computerVehicle.setLocalTranslation(kidsRoom.getGridPosition(GridPosition.SECOND));
	    computerVehicle.rotateUponItself(-1.6f);
		computerVehicle.addObserver(this);
		rootNode.attachChild(computerVehicle);
	}

	private void loadPlayerVehicle() {
		playerVehicle = new PlayerVehicle(getPhysicsSpace(), ColorRGBA.red);
		playerVehicle.setLocalTranslation(kidsRoom.getGridPosition(GridPosition.FIRST));
	    playerVehicle.rotateUponItself(-1.6f);
		playerVehicle.addObserver(this);
		rootNode.attachChild(playerVehicle);
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
		input.addAction(new Traction(playerVehicle, FORWARD_TRACTION_VELOCITY), InputHandler.DEVICE_KEYBOARD,
				KeyInput.KEY_UP, InputHandler.AXIS_NONE, false);
		input.addAction(new Traction(playerVehicle, BACKWARD_TRACTION_VELOCITY), InputHandler.DEVICE_KEYBOARD,
				KeyInput.KEY_DOWN, InputHandler.AXIS_NONE, false);
		input.addAction(new Steer(playerVehicle, LEFT_STEER_DIRECTION), InputHandler.DEVICE_KEYBOARD,
				KeyInput.KEY_LEFT, InputHandler.AXIS_NONE, false);
		input.addAction(new Steer(playerVehicle, RIGHT_STEER_DIRECTION), InputHandler.DEVICE_KEYBOARD,
				KeyInput.KEY_RIGHT, InputHandler.AXIS_NONE, false);

		CameraPositionHandler cameraPositionHandler = new CameraPositionHandler(vehicleChaseCamera);
		input.addAction(cameraPositionHandler, InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_V, InputHandler.AXIS_NONE, false);
	}

	private void loadInfo() {
		info = new Info(display.getRenderer(), playerVehicle.getHealth());
		// Use fpsNone node to display the characters.
		fpsNode.attachChild(info);
	}

	private void loadCamera() {
		vehicleChaseCamera = (VehicleChaseCamera) VehicleChaseCamera.getInstance(cam, playerVehicle.getChassis());
		cam.setLocation(CAMERA_LOCATION);
	}

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

	private void loadRoom() {
		kidsRoom = new KidsRoom(getPhysicsSpace(), display.getRenderer());
		kidsRoom.attachChild(playerVehicle);
		rootNode.attachChild(kidsRoom);
	}

	public void updateTime(int seconds) {
		// TODO use internalization
		info.printMessage("Time left: " + String.valueOf(seconds) + " seconds");
	}

	public void timeUP() {
		info.printMessage(String.valueOf(""));
		info.setHealthBarValue(playerVehicle.rechargeHealth(RECHARGE_HEALTH));
		healthChronometer.stopChronometer();
	}

	public void updateHealth() {
		updateVehiclesHealth();
	}

	public void healthEnded() {
		//TODO player that wins the challenge at the beginning may win one more second
		healthChronometer = new HealthChronometer(WAIT_SECONDS);
		healthChronometer.addObserver(this);
		healthChronometer.start();
	}
}
