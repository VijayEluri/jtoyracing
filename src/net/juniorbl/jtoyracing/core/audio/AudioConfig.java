package net.juniorbl.jtoyracing.core.audio;

import com.jme.renderer.Camera;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;

/**
 * Audio configuration of the game.
 *
 * @version 1.0 Jan 02, 2008
 * @author Carlos Luz Junior
 */
public final class AudioConfig {

	/**
	 * Volume of the sound effect.
	 */
	private static final float SOUND_EFFECT_VOLUME = 0.1f;

    /**
     * The audio system of the game.
     */
	private AudioSystem audioSystem;

	/**
	 * Constructs a audio configuration.
	 *
	 * @param camera a camera to simulate audio 3D.
	 */
	public AudioConfig(Camera camera) {
		loadAudioSystem(camera);
	}

	/**
	 * Loads a sound effect. Sound effects are around in the scene.
	 *
	 * @param effectPath path of the sound effect.
	 * @return sound effect loaded.
	 */
	public static AudioTrack loadSoundEffect(String effectPath) {
		AudioTrack soundEffect = AudioSystem.getSystem().
			createAudioTrack(AudioConfig.class.getClassLoader().getResource(effectPath), false);
		soundEffect.setType(AudioTrack.TrackType.ENVIRONMENT);
		soundEffect.setRelative(true);
		soundEffect.setLooping(true);
		soundEffect.setVolume(SOUND_EFFECT_VOLUME);
		return soundEffect;
	}

	/**
	 * Loads the audio system of the game. A camera's position is used as a reference.
	 *
	 * @param camera the camera.
	 */
	private void loadAudioSystem(Camera camera) {
		audioSystem = AudioSystem.getSystem();
		audioSystem.getEar().trackOrientation(camera);
		audioSystem.getEar().trackPosition(camera);
	}

	/**
	 * Updates the audio system.
	 */
	public void update() {
		audioSystem.update();
	}
}
