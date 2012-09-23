package cs4620.pa1.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class SceneNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;

	protected String name;

	public SceneNode()
	{
		// NOP
	}
	
	public SceneNode getSceneNodeChild(int i)
	{
		return (SceneNode)getChildAt(i);
	}

	public SceneNode(String name)
	{
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}

	public Object getYamlObjectRepresentation()
	{
		Map<Object, Object> result = new HashMap<Object, Object>();
		result.put("name", getName());
		List<Object> children = new ArrayList<Object>();
		for (int ctr = 0; ctr < getChildCount(); ctr++)
			  children.add(((SceneNode)getChildAt(ctr)).getYamlObjectRepresentation());
		result.put("children", children);
		return result;
	}

	public static SceneNode fromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;

		if (yamlMap.get("type").equals("TransformationNode"))
			return TransformationNode.fromYamlObject(yamlObject);
		else if (yamlMap.get("type").equals("MeshNode"))
			return MeshNode.fromYamlObject(yamlObject);
		else if (yamlMap.get("type").equals("LightNode"))
			return LightNode.fromYamlObject(yamlObject);
		else
			throw new RuntimeException("invalid SceneNode type: " + yamlMap.get("type").toString());
	}
}
