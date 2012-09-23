package cs4620.pa1.shape;

import java.util.HashMap;
import java.util.Map;

public class Sphere extends TriangleMesh 
{
	@Override
	public void buildMesh(float tolerance)
	{
		// TODO: (Problem 2) Fill in the code to create a sphere mesh.
	}

	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<Object,Object> result = new HashMap<Object, Object>();
		result.put("type", "Sphere");
		return result;
	}
}
