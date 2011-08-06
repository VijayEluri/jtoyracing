package net.juniorbl.jtoyracing.core.hud;

import java.nio.FloatBuffer;

import net.juniorbl.jtoyracing.enums.ResourcesPath;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.geom.BufferUtils;

/**
 * Responsible for put the information on the screen.
 *
 * Note: part of the creation of this bar was extracted from JMonkeyEngine documentation.
 *
 * @version 1.0 Oct 16, 2007
 * @author Carlos Luz Junior
 */
public class Info extends Node {

	private static final long serialVersionUID = -7326797374588827635L;

	private static final float HEALTH_SYMBOL_HEIGHT = 40f;

	private static final float HEALTH_SYMBOL_WIDTH = 40f;

	private static final Vector3f TEXT_INFO_LOCATION = new Vector3f(0, 85, 0);

	private static final Vector3f HEALTH_BAR_LOCATION = new Vector3f(80, 65, 0);

	private static final int BAR_BORDER_WIDTH = 96;

	private static final int BAR_BORDER_HEIGHT = 13;

	private static final int HEALTH_BAR_WIDTH = 94;

	private static final int HEALTH_BAR_HEIGHT = 11;

	private static final Vector3f HEALTH_SYMBOL_LOCATION = new Vector3f(25, 55, 0);

	private static final int BAR_FIRST_PIXEL = 53;

	private static final int BAR_LAST_PIXEL = 63;

	private Quad healthSymbol;

	private Quad barBorders;

	private Quad healthBar;

	private Text textInfo;

	private Node healthBarNode;

	private int maxHealthBarValue;

	private int healthBarTextureHeight;

	/**
	 * Info uses a renderer to create texture and alpha states. A vehicle's health is used in the health bar.
	 */
	public Info(Renderer renderer, Integer vehicleHealth) {
		this.maxHealthBarValue = vehicleHealth;
		TextureState healthSymbolTextureState = loadTextureState(renderer.createTextureState(),
				ResourcesPath.IMAGES_PATH + "healthSymbol.png");
		createHealthSymbol(healthSymbolTextureState);
		TextureState healthTextureState = loadTextureState(renderer.createTextureState(),
				ResourcesPath.IMAGES_PATH + "healthBar.png");
		createHealthBar(healthTextureState);
		createTransparency(renderer.createAlphaState());
		//FIXME the use of "10" below is because the bar doesn't fill entirely.
		setHealthBarValue(vehicleHealth + 10);
		startTextInformation();
	}

	private void startTextInformation() {
		textInfo = new Text("textNode", "");
		textInfo.setTextColor(ColorRGBA.red);
		textInfo.setLocalTranslation(TEXT_INFO_LOCATION);
		this.attachChild(textInfo);
	}

	public final void printMessage(String message) {
		textInfo.print(message);
	}

	/**
	 * Creates the health bar. This bar shows the amount of health each car has available for the race
	 * at some moment.
	 */
	private void createHealthBar(TextureState textureState) {
		healthBarNode = new Node();
		healthBarNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		healthBarNode.setLocalTranslation(HEALTH_BAR_LOCATION);
		healthBarNode.setLightCombineMode(LightState.OFF);
		healthBarNode.updateRenderState();
		healthBarNode.setRenderState(textureState);

		int healthBarTextureWidth = textureState.getTexture().getImage().getWidth();
		healthBarTextureHeight = textureState.getTexture().getImage().getHeight();

		barBorders = new Quad("barBorder", new Float(BAR_BORDER_WIDTH), new Float(BAR_BORDER_HEIGHT));
		healthBar = new Quad("bar", new Float(HEALTH_BAR_WIDTH), new Float(HEALTH_BAR_HEIGHT));

		healthBarNode.attachChild(barBorders);
		healthBarNode.attachChild(healthBar);
		barBorders.setTextureBuffer(0, createTextureCoordinates(BAR_BORDER_WIDTH, BAR_BORDER_HEIGHT,
				healthBarTextureWidth, healthBarTextureHeight));
	}

	private TextureState loadTextureState(TextureState textureState, String imagePath) {
		textureState.setTexture(TextureManager.loadTexture(getClass()
				.getClassLoader().getResource(imagePath),
				Texture.MM_LINEAR, Texture.FM_LINEAR, 1.0f, true));
		textureState.setEnabled(true);
		return textureState;
	}

	private FloatBuffer createTextureCoordinates(int imageWidth, int imageHeight, int textureWidth, int textureHeight) {
		FloatBuffer textureCoordinates = BufferUtils.createVector2Buffer(4);
		textureCoordinates.put((float) 0 / textureWidth).put(1f - (float) 0 / textureHeight);
		textureCoordinates.put((float) 0 / textureWidth).put(1f - (float) imageHeight / textureHeight);
		textureCoordinates.put((float) imageWidth / textureWidth).put(1f - (float) imageHeight / textureHeight);
		textureCoordinates.put((float) imageWidth / textureWidth).put(1f - (float) 0 / textureHeight);
		return textureCoordinates;
	}

	/**
	 * FIXME move to stateutil
	 */
	private void createTransparency(AlphaState alphaState) {
		alphaState.setBlendEnabled(true);
		alphaState.setSrcFunction(AlphaState.SB_SRC_ALPHA);
		alphaState.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
		alphaState.setTestEnabled(false);
		alphaState.setEnabled(true);
		this.setRenderState(alphaState);

		healthBarNode.setRenderState(alphaState);
		healthBarNode.updateRenderState();
		this.attachChild(healthBarNode);
	}

	private void createHealthSymbol(TextureState textureState) {
		healthSymbol = new Quad("hud", HEALTH_SYMBOL_WIDTH, HEALTH_SYMBOL_HEIGHT);
		healthSymbol.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		healthSymbol.setLocalTranslation(HEALTH_SYMBOL_LOCATION);
		healthSymbol.setLightCombineMode(LightState.OFF);
		healthSymbol.updateRenderState();
		healthSymbol.setRenderState(textureState);
		int healthSymbolTextureWidth = textureState.getTexture().getImage().getWidth();
		int healthSymbolTextureHeight = textureState.getTexture().getImage().getHeight();
		healthSymbol.setTextureBuffer(0,
				createTextureCoordinates((int) HEALTH_SYMBOL_WIDTH, (int) HEALTH_SYMBOL_HEIGHT,
						healthSymbolTextureWidth, healthSymbolTextureHeight));
		this.attachChild(healthSymbol);
	}

	public final void setHealthBarValue(int healthValue) {
		int desiredHealth = healthValue;
		desiredHealth %= (int) maxHealthBarValue;
		FloatBuffer coordinates = BufferUtils.createVector2Buffer(4);
		calculateContentLocation(desiredHealth, coordinates);
	}

	private void calculateContentLocation(int desiredHealth, FloatBuffer coordinates) {
		float relCoord = 0.5f - ((float) desiredHealth / maxHealthBarValue) * 0.5f;
		coordinates.put(relCoord).put(1f - (float) BAR_FIRST_PIXEL / healthBarTextureHeight);
		coordinates.put(relCoord).put(1f - (float) BAR_LAST_PIXEL / healthBarTextureHeight);
		coordinates.put(relCoord + 0.5f).put(1f - (float) BAR_LAST_PIXEL / healthBarTextureHeight);
		coordinates.put(relCoord + 0.5f).put(1f - (float) BAR_FIRST_PIXEL / healthBarTextureHeight);
		healthBar.setTextureBuffer(0, coordinates);
	}
}
