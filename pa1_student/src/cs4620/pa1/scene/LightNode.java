package cs4620.pa1.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs4620.pa1.util.Util;

public class LightNode extends SceneNode
{
	private static final long serialVersionUID = 1L;

	public static final float AMBIENT_CONSTANT = 0.05f;
	public static final float DIFFUSE_CONSTANT = 0.4f;

	/**
	 * Ambient intensity.
	 */
	public final float[] ambient = new float[] { AMBIENT_CONSTANT, AMBIENT_CONSTANT, AMBIENT_CONSTANT, 1 };

	/**
	 * Diffuse intensity.
	 */
	public final float[] diffuse = new float[] { DIFFUSE_CONSTANT, DIFFUSE_CONSTANT, DIFFUSE_CONSTANT, 1 };

	/**
	 * Specular intensity.
	 */
	public final float[] specular = new float[] { 2, 2, 2, 1 };

	/**
	 * Light position.
	 */
	public final float[] position = new float[] { 6, 8, 10, 1 };

	/**
	 * For I/O.
	 */
	public LightNode()
	{
		// NOP
	}

	public LightNode(String name)
	{
		super(name);
	}

	public void setDiffuse(float r, float g, float b)
	{
		diffuse[0] = r;
		diffuse[1] = g;
		diffuse[2] = b;
	}

	public void setSpecular(float r, float g, float b)
	{
		specular[0] = r;
		specular[1] = g;
		specular[2] = b;
	}

	public void setAmbient(float r, float g, float b)
	{
		ambient[0] = r;
		ambient[1] = g;
		ambient[2] = b;
	}

	public void setPosition(float x, float y, float z)
	{
		position[0] = x;
		position[1] = y;
		position[2] = z;
	}

	private List<Object> convertFloatArrayToList(float[] a)
	{
		List<Object> result = new ArrayList<Object>();
		for(int i=0;i<a.length;i++)
			result.add(a[i]);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<String, Object> result = (Map<String, Object>)super.getYamlObjectRepresentation();
		result.put("type", "LightNode");
		result.put("ambient", convertFloatArrayToList(ambient));
		result.put("diffuse", convertFloatArrayToList(diffuse));
		result.put("specular", convertFloatArrayToList(specular));
		result.put("position", convertFloatArrayToList(position));
		return result;
	}

	public void extractLightFromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;

		Util.assign4ElementArrayFromYamlObject(ambient, yamlMap.get("ambient"));
		Util.assign4ElementArrayFromYamlObject(diffuse, yamlMap.get("diffuse"));
		Util.assign4ElementArrayFromYamlObject(specular, yamlMap.get("specular"));
		Util.assign4ElementArrayFromYamlObject(position, yamlMap.get("position"));
	}

	public static SceneNode fromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;

		LightNode result = new LightNode();
		result.setName((String)yamlMap.get("name"));		
		result.addChildrenFromYamlObject(yamlObject);
		result.extractLightFromYamlObject(yamlObject);

		return result;
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
}
