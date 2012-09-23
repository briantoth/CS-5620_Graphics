package cs4620.pa1.material;

import java.util.ArrayList;

import javax.media.opengl.GL2;

public abstract class Material
{
	protected static ArrayList<Material> instances = new ArrayList<Material>();

	public abstract void use(GL2 gl);
	public abstract void unuse(GL2 gl);
	public abstract void dispose(GL2 gl);

	public Material()
	{
		synchronized(instances) {
			instances.add(this);
		}
	}

	public abstract Object getYamlObjectRepresentation();
}
