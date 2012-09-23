package cs4620.pa1.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import cs4620.pa1.util.Util;

public class TransformationNode extends SceneNode
{
	private static final long serialVersionUID = 1L;

	/**
	 * Rotation component of this transformation.
	 * Stored in degrees.
	 */
	public final Vector3f rotation = new Vector3f();

	/**
	 * Scaling component of this transformation.
	 */
	public final Vector3f scaling = new Vector3f();

	/**
	 * Translation component of this transformation.
	 */
	public final Vector3f translation = new Vector3f();

	Matrix3f tempMatrix = new Matrix3f();

	/**
	 * Required for I/O.
	 */
	public TransformationNode()
	{
		super();
	}

	public TransformationNode(String name)
	{
		super(name);
		resetTransformation();

	}

	protected void resetTransformation()
	{
		scaling.set(1,1,1);
		rotation.set(0, 0, 0);
		translation.set(0, 0, 0);
	}

	public void setRotation(float xAngle, float yAngle, float zAngle)
	{
		this.rotation.set(xAngle, yAngle, zAngle);
	}

	public void setScaling(float x, float y, float z)
	{
		this.scaling.set(x, y, z);
	}

	public void setTranslation(float x, float y, float z)
	{
		this.translation.set(x, y, z);
	}

	private Object convertVector3ToIntList(Vector3f v)
	{
		List<Object> result = new ArrayList<Object>();
		result.add(v.x);
		result.add(v.y);
		result.add(v.z);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<String, Object> result = (Map<String, Object>)super.getYamlObjectRepresentation();
		result.put("type", "TransformationNode");
		result.put("translation", convertVector3ToIntList(translation));
		result.put("rotation", convertVector3ToIntList(rotation));
		result.put("scaling", convertVector3ToIntList(scaling));
		return result;
	}

	public void extractTransformationFromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;
		translation.set(Util.getVector3ffromYamlObject(yamlMap.get("translation")));
		rotation.set(Util.getVector3ffromYamlObject(yamlMap.get("rotation")));
		scaling.set(Util.getVector3ffromYamlObject(yamlMap.get("scaling")));
	}

	public void addChildrenFromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;
		List<?> childrenList = (List<?>)yamlMap.get("children");
		for(Object o : childrenList)
			insert(SceneNode.fromYamlObject(o),getChildCount());
	}

	public static SceneNode fromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;

		TransformationNode result = new TransformationNode();
		result.setName((String)yamlMap.get("name"));
		result.extractTransformationFromYamlObject(yamlObject);
		result.addChildrenFromYamlObject(yamlObject);

		return result;
	}
}
