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
	private static final float SOUND_EFFECT_VOLUME = 0.1f;
	private AudioSystem audioSystem;

	public AudioConfig(Camera camera) {
		loadAudioSystem(camera);
	}

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
	 */
	private void loadAudioSystem(Camera camera) {
		audioSystem = AudioSystem.getSystem();
		audioSystem.getEar().trackOrientation(camera);
		audioSystem.getEar().trackPosition(camera);
	}

	public void update() {
		audioSystem.update();
	}
}
