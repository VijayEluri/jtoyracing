package net.juniorbl.jtoyracing.util;

import com.jme.scene.Node;
import com.jme.scene.TriMesh;

/**
 * Implementation used with JMockit for unit tests.
 * 
 * @version 1.0 Jun 21, 2008
 * @author Fco. Carlos L. Barros Junior
 */
public final class ModelUtilForTest {
	
	/**
	 * Prevents this class from being instantiated.
	 */
	private ModelUtilForTest() { }

	/**
	 * @see net.juniorbl.jtoyracing.util.ModelUtil#convertMultipleModelToJME()
	 */
	public static Node convertMultipleModelToJME(String path) {
		return new Node();
	}
	
	/**
	 * @see net.juniorbl.jtoyracing.util.ModelUtil#convertModelSimpleObjToJME()
	 */
	public static TriMesh convertModelSimpleObjToJME(String path) {
		return new TriMesh();
	}
}
