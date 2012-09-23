package cs4620.pa1.scene;

import java.util.Map;

import javax.media.opengl.GL2;

import cs4620.pa1.material.GLPhongMaterial;
import cs4620.pa1.material.Material;
import cs4620.pa1.shape.Mesh;
import cs4620.pa1.shape.Sphere;
import cs4620.pa1.util.Util;

public class MeshNode extends TransformationNode
{
	private static final long	serialVersionUID	= 1L;

	private Mesh mesh;
	private Material material;

	/**
	 * Required for I/O
	 */
	public MeshNode()
	{
		this("", null, null);
	}

	public MeshNode(String name)
	{
		this(name, new Sphere(), new GLPhongMaterial());
	}

	public MeshNode(String name, Mesh mesh)
	{
		this(name, mesh, new GLPhongMaterial());
	}

	public MeshNode(String name, Mesh mesh, Material material)
	{
		super(name);
		this.mesh = mesh;
		this.material = material;
	}

	public Mesh getMesh()
	{
		return mesh;
	}

	public void setMesh(Mesh mesh)
	{
		this.mesh = mesh;
	}

	public Material getMaterial()
	{
		return material;
	}
	
	public void draw(GL2 gl)
	{
		getMaterial().use(gl);
		getMesh().render(gl);
		getMaterial().unuse(gl);
	}
	
	public void drawForPicking(GL2 gl)
	{
		gl.glLoadName(getMesh().getId());
		getMesh().render(gl);
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<String, Object> result = (Map<String, Object>)super.getYamlObjectRepresentation();
		result.put("type", "MeshNode");
		result.put("mesh", mesh.getYamlObjectRepresentation());
		result.put("material", material.getYamlObjectRepresentation());
		return result;
	}

	public void extractMeshFromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;

		mesh = Mesh.fromYamlObject(yamlMap.get("mesh"));
	}

	public void extractMaterialFromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;

		if (!(yamlMap.get("material") instanceof Map))
			throw new RuntimeException("material field not a Map");
		Map<?, ?> materialMap = (Map<?, ?>)yamlMap.get("material");

		if (!materialMap.get("type").equals("GLPhongMaterial"))
			throw new RuntimeException("material other than GLPhongMaterial is not supported");

		GLPhongMaterial glMaterial = new GLPhongMaterial();
		Util.assign4ElementArrayFromYamlObject(glMaterial.ambient, materialMap.get("ambient"));
		Util.assign4ElementArrayFromYamlObject(glMaterial.diffuse, materialMap.get("diffuse"));
		Util.assign4ElementArrayFromYamlObject(glMaterial.specular, materialMap.get("specular"));
		glMaterial.shininess = Float.valueOf(materialMap.get("shininess").toString());

		material = glMaterial;
	}

	public static SceneNode fromYamlObject(Object yamlObject)
	{
		if (!(yamlObject instanceof Map))
			throw new RuntimeException("yamlObject not a Map");
		Map<?, ?> yamlMap = (Map<?, ?>)yamlObject;

		MeshNode result = new MeshNode();
		result.setName((String)yamlMap.get("name"));
		result.extractTransformationFromYamlObject(yamlObject);
		result.addChildrenFromYamlObject(yamlObject);
		result.extractMeshFromYamlObject(yamlObject);
		result.extractMaterialFromYamlObject(yamlObject);

		return result;
	}
}
