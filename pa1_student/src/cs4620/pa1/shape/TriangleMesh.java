package cs4620.pa1.shape;

import javax.media.opengl.GL2;

public abstract class TriangleMesh extends Mesh {
	protected float[] vertices = null;
	protected float[] normals = null;
	protected int[] triangles = null;

	public TriangleMesh()
	{
		super();
	}

	protected void setVertex(int i, float x, float y, float z)
	{
		vertices[3*i]   = x;
		vertices[3*i+1] = y;
		vertices[3*i+2] = z;
	}

	protected void setNormal(int i, float x, float y, float z)
	{
		normals[3*i]   = x;
		normals[3*i+1] = y;
		normals[3*i+2] = z;
	}

	protected void setTriangle(int i, int i0, int i1, int i2)
	{
		triangles[3*i]   = i0;
		triangles[3*i+1] = i1;
		triangles[3*i+2] = i2;
	}
	
	public final void render(GL2 gl)
	{
		// TODO: (Problem 1) Fill in the code to render the mesh.
	}

	public final void setMeshData(float[] vertices, float[] normals, int[] triangles)
	{
		if (vertices.length % 3 != 0)
			throw new Error("Vertex array's length is not a multiple of 3.");
		if (normals.length % 3 != 0)
			throw new Error("Normal array's length is not a multiple of 3");
		if (vertices.length != normals.length)
			throw new Error("Vertex and normal array are not equal in size.");
	    if (triangles.length % 3 != 0)
	        throw new Error("Triangle array's length is not a multiple of 3.");

	    this.vertices = vertices;
	    this.normals = normals;
	    this.triangles = triangles;
	}
}
