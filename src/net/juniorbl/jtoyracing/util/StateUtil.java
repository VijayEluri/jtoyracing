package net.juniorbl.jtoyracing.util;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.SceneElement;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.MaterialState;

/**
 * Utility operations with states.
 *
 * @version 1.0 Mar 21, 2011
 * @author Carlos Luz Junior
 */
public final class StateUtil {

	private static Renderer renderer;

	private StateUtil() { }

	/**
	 * Makes an arbitrary number of scene elements transparent.
	 */
	public static void makeTransparent(SceneElement... sceneElements) {
		for (SceneElement sceneElement : sceneElements) {
			MaterialState materialState = loadMaterialState();
			AlphaState alphaState = loadAlphaState();
			sceneElement.setRenderState(materialState);
			sceneElement.setRenderState(alphaState);
			sceneElement.updateRenderState();
			sceneElement.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		}
	}

	private static AlphaState loadAlphaState() {
		AlphaState alphaState = renderer.createAlphaState();
		alphaState.setBlendEnabled(true);
		alphaState.setSrcFunction(AlphaState.SB_SRC_ALPHA);
		alphaState.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
		alphaState.setTestEnabled(false);
		alphaState.setEnabled(true);
		return alphaState;
	}

	private static MaterialState loadMaterialState() {
		MaterialState materialState = renderer.createMaterialState();
		materialState.setAmbient(new ColorRGBA(0.0f, 0.0f, 0.0f, -0.5f));
		materialState.setDiffuse(new ColorRGBA(0.1f, 0.5f, 0.8f, -0.5f));
		materialState.setSpecular(new ColorRGBA(1.0f, 1.0f, 1.0f, -0.5f));
		materialState.setShininess(128.0f);
		materialState.setEmissive(new ColorRGBA(0.0f, 0.0f, 0.0f, -0.5f));
		materialState.setEnabled(true);
		materialState.setMaterialFace(MaterialState.MF_FRONT_AND_BACK);
		return materialState;
	}

	public static void setRenderer(Renderer renderer) {
		StateUtil.renderer = renderer;
	}
}
