package net.juniorbl.jtoyracing.entity.environment;

import net.juniorbl.jtoyracing.util.ModelUtil;
import net.juniorbl.jtoyracing.util.ResourcesPath;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

//FIXME don't load a 3D model, use 4 Quad

//FIXME erase this class, load the wall inside the room

/**
 * Wall of the room.
 *
 * @version 1.0 Sep 16, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public class Wall extends Node {
	
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 8276219154604080263L;

	/**
	 * Constructs a wall.
	 * 
	 * @param physicsSpace the physicsSpace;
	 */
	public Wall(PhysicsSpace physicsSpace) {
		super("wall");
		StaticPhysicsNode wall = physicsSpace.createStaticNode();
		wall.setLocalTranslation(new Vector3f(0, -30, 30));
		wall.attachChild(ModelUtil.convertMultipleModelToJME(ResourcesPath.MODELS_PATH + "obj/wall.obj"));
		wall.setMaterial(Material.GHOST);
		wall.setLocalScale(11f);
		wall.generatePhysicsGeometry();
		this.attachChild(wall);
	}
}
