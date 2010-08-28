//package net.juniorbl.jtoyracing.test.entity.vehicle;
//
//import static org.easymock.EasyMock.expect;
//import static org.easymock.EasyMock.expectLastCall;
//import static org.easymock.classextension.EasyMock.createMock;
//import static org.easymock.classextension.EasyMock.replay;
//import junit.framework.Assert;
//import mockit.Mockit;
//import net.juniorbl.jtoyracing.entity.environment.RaceTrack;
//import net.juniorbl.jtoyracing.util.ModelUtil;
//import net.juniorbl.jtoyracing.util.ModelUtilForTest;
//import net.juniorbl.jtoyracing.util.StaticPhysicsNodeImpl;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.jme.math.Vector3f;
//import com.jme.scene.state.AlphaState;
//import com.jme.scene.state.TextureState;
//import com.jmex.physics.PhysicsSpace;
//
///**
// * Tests for RaceTrack.
// *
// * TODO not finished yet.
// *
// * @version 1.0 Jun 21, 2008
// * @author Carlos Luz Junior
// */
//public class RaceTrackTest {
//
//	/**
//	 * The location for the vehicle and track.
//	 */
//	private static final Vector3f TESTED_LOCATION = new Vector3f(10, 10, 10);
//
//	/**
//	 * The RaceTrack.
//	 */
//	private RaceTrack raceTrack;
//
//	/**
//	 * PhysicsSpace necessary to construct a RaceTrack.
//	 */
//	private PhysicsSpace physicsSpace;
//
//	/**
//	 * AlphaState necessary to construct a RaceTrack.
//	 */
//	private AlphaState alphaState;
//
//	/**
//	 * TextureState necessary to construct a RaceTrack.
//	 */
//	private TextureState textureState;
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Before
//	public final void setUp() throws Exception {
//		Assert.fail("not finished yet");
//		physicsSpace = createMock(PhysicsSpace.class);
//		physicsSpace = createMock(PhysicsSpace.class);
//		alphaState = createMock(AlphaState.class);
//		textureState = createMock(TextureState.class);
//		StaticPhysicsNodeImpl physicsNodeImpl = new StaticPhysicsNodeImpl();
//		expect(physicsSpace.createStaticNode()).andReturn(physicsNodeImpl);
//		expectLastCall().times(3);
//		replay(physicsSpace);
//
//		Mockit.redefineMethods(ModelUtil.class, ModelUtilForTest.class);
//
//		raceTrack = new RaceTrack(physicsSpace, TESTED_LOCATION, alphaState, textureState);
//	}
//
//	/**
//	 * Test the correct result when a vehicle reaches a checkpoint.
//	 */
//	@Test
//	public final void testIsVehicleReachedCheckpoint() {
//		Assert.fail("not finished yet");
//		StaticPhysicsNodeImpl vehicle = new StaticPhysicsNodeImpl();
//		vehicle.setLocalTranslation(TESTED_LOCATION);
//		Assert.assertTrue(raceTrack.isVehicleReachedCheckpoint(vehicle.getWorldBound()));
//	}
//}
