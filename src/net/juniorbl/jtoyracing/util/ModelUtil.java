package net.juniorbl.jtoyracing.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.XMLparser.Converters.ObjToJme;

/**
 * Utility class for models. Part of this code came from JMonkeyEngine documentation.
 *
 * @version 1.0 Sep 9, 2007
 * @author Carlos Luz Junior
 */
public final class ModelUtil {

	/**
	 * Prevents this class from being instantiated.
	 */
	private ModelUtil() { }

	private static ByteArrayOutputStream convertOBJModelToJME(String path) {
		ObjToJme converter = new ObjToJme();
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		try {
			URL objFile = ModelUtil.class.getClassLoader()
					.getResource(path);
			converter.setProperty("mtllib", objFile);
			converter.setProperty("texdir", objFile);
			converter.convert(objFile.openStream(), byteArrayOS);
		} catch (IOException e) {
			//TODO throw a business checked exception
			throw new Error(e);
		}
		return byteArrayOS;
	}

	public static Spatial convertOBJToStatial(String path) {
		Spatial model = null;
		try {
			ByteArrayOutputStream byteArrayOS = convertOBJModelToJME(path);
			model = (Spatial) BinaryImporter.getInstance().load(
					new ByteArrayInputStream(byteArrayOS.toByteArray()));
			model.setModelBound(new BoundingBox());
			model.updateModelBound();
		} catch (IOException e) {
			//TODO throw a business checked exception
			throw new Error(e);
		}
		return model;
	}

	public static Quaternion calculateRotation(int degrees) {
		Quaternion quaternion = new Quaternion();
		float radians = degrees * FastMath.DEG_TO_RAD;
		quaternion.fromAngleAxis(radians, Vector3f.UNIT_Y);
		return quaternion;
	}
}
