package net.juniorbl.jtoyracing.test.util;

import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.StaticPhysicsNode;

/**
 * Implementation of StaticPhysicsNode for unit tests.
 * 
 * @version 1.0 Jun 21, 2008
 * @author Fco. Carlos L. Barros Junior
 */
public final class StaticPhysicsNodeImpl extends StaticPhysicsNode {
	
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = -1025602488634116692L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PhysicsSpace getSpace() {
		return null;
	}
}
