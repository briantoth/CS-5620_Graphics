package cs4620.pa1.util;

import java.util.List;

import javax.vecmath.Vector3f;

public class Util
{
	public static Vector3f getVector3ffromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof List))
			throw new RuntimeException("yamlObject not a List");
		List<?> yamlList = (List<?>)yamlObject;
		return new Vector3f(
				Float.valueOf(yamlList.get(0).toString()),
				Float.valueOf(yamlList.get(1).toString()),
				Float.valueOf(yamlList.get(2).toString()));
	}

	public static void assign4ElementArrayFromYamlObject(float[] output, Object yamlObject)
	{
		if (!(yamlObject instanceof List))
			throw new RuntimeException("yamlObject not a List");
		List<?> yamlList = (List<?>)yamlObject;

		output[0] = Float.valueOf(yamlList.get(0).toString());
		output[1] = Float.valueOf(yamlList.get(1).toString());
		output[2] = Float.valueOf(yamlList.get(2).toString());
		output[3] = Float.valueOf(yamlList.get(3).toString());
	}
}
