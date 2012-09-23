package cs4620.pa1.shape;

import java.util.ArrayList;
import java.util.Map;

import javax.media.opengl.GL2;

public abstract class Mesh {
	private static ArrayList<Mesh> meshes = new ArrayList<Mesh>();
	private int id;

	public Mesh()
	{
		id = meshes.size() + 1024;
		meshes.add(this);
	}

	public int getId() {
		return id;
	}

	public abstract void render(GL2 gl);

	public abstract void buildMesh(float tolerance);

	public abstract Object getYamlObjectRepresentation();

	public static Mesh fromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> meshMap = (Map<?, ?>)yamlObject;

		if (meshMap.get("type").equals("Sphere"))
			return new Sphere();
		else if (meshMap.get("type").equals("Cube"))
			return new Cube();
		else if (meshMap.get("type").equals("Cylinder"))
			return new Cylinder();
		else if (meshMap.get("type").equals("Torus"))
			return new Torus();
		else if (meshMap.get("type").equals("Teapot"))
			return new Teapot();
		else
			throw new RuntimeException("invalid mesh type");
	}

}
