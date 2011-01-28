package net.juniorbl.jtoyracing.entity.vehicle;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.juniorbl.jtoyracing.audio.AudioConfig;
import net.juniorbl.jtoyracing.util.ModelUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.jme.math.Vector3f;
import com.jmex.audio.AudioTrack;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.RotationalJointAxis;
import com.jmex.physics.TranslationalJointAxis;
import com.jmex.physics.geometry.PhysicsSphere;

/**
 * @author Carlos Luz Junior
 */
@RunWith(org.powermock.modules.junit4.legacy.PowerMockRunner.class)
@PrepareForTest({ ModelUtil.class, AudioConfig.class })
public class VehicleTest {

	private PhysicsSpace physicsSpace;

	private DynamicPhysicsNode mockedDynamicNode;

	private PhysicsSphere mockedPhysicsSphere;

	private Joint mockedJoint;

	private TranslationalJointAxis mockedTranslationalAxis;

	private RotationalJointAxis mockedRotationalAxis;

	private AudioTrack mockedAudio;

	@Before
	public void setUp() throws Exception {
		createMocks();
		setExpectations();
	}

	@Test
	public void testDecreaseHealth() {
		Vehicle vehicle = new Vehicle(physicsSpace, new Vector3f());
		vehicle.decreaseHealth();
		assertEquals(vehicle.getHealth(), 475);
	}

	@Test
	public void testRechargeHealth() {
		Vehicle vehicle = new Vehicle(physicsSpace, new Vector3f());
		vehicle.decreaseHealth();
		vehicle.rechargeHealth(250);
		assertEquals(vehicle.getHealth(), 250);
	}

	private void createMocks() {
		physicsSpace = mock(PhysicsSpace.class);
		mockedDynamicNode = mock(DynamicPhysicsNode.class);
		mockedPhysicsSphere = mock(PhysicsSphere.class);
		mockedJoint = mock(Joint.class);
		mockedTranslationalAxis = mock(TranslationalJointAxis.class);
		mockedRotationalAxis = mock(RotationalJointAxis.class);
		mockedAudio = mock(AudioTrack.class);
		PowerMockito.mockStatic(ModelUtil.class);
		PowerMockito.mockStatic(AudioConfig.class);
	}

	private void setExpectations() {
		when(mockedJoint.createTranslationalAxis()).thenReturn(mockedTranslationalAxis);
		when(mockedJoint.createRotationalAxis()).thenReturn(mockedRotationalAxis);
		when(physicsSpace.createDynamicNode()).thenReturn(mockedDynamicNode);
		when(physicsSpace.createJoint()).thenReturn(mockedJoint);
		when(mockedDynamicNode.getLocalTranslation()).thenReturn(new Vector3f());
		when(mockedDynamicNode.getSpace()).thenReturn(physicsSpace);
		when(mockedDynamicNode.createSphere("tire")).thenReturn(mockedPhysicsSphere);
		when(AudioConfig.loadSoundEffect(Mockito.anyString())).thenReturn(mockedAudio);
	}
}
