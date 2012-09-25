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
		//gl.glBegin(GL2.GL_TRIANGLES);
		{
			// for each triangle
			for (int i = 0; i < triangles.length; i += 3) {
				gl.glBegin(GL2.GL_TRIANGLES);
				for (int j = 0; j < 3; j++){
					//System.out.println("i is " + i);
					//System.out.println("length of vertices is: " + vertices.length);
					//System.out.println("length of normals is: " + normals.length);
					int n = triangles[i+j]; // gets location of the jth vertex of the ith triangle
					n = n*3;
					//System.out.println("normal is : " + normals[n] + ", " + normals[n+1] + ", " + normals[n+2]);
					//System.out.println("n is " + n);
					//System.out.println("i is " + i);
					//System.out.println("j is " + j);
					//System.out.println("i+j is " + (i+j));
					gl.glNormal3f(normals[n], normals[n+1],normals[n+2]);
					gl.glVertex3f(vertices[n], vertices[n+1], vertices[n+2]);
					//gl.glVertex3f(vertices[n+2], vertices[n+1], vertices[n]);
					
				}
				gl.glEnd();
			}
			// for each triangle
				// set the normal
				// for each vertex in the triangle
					// gl.glVertex3f that shit
			
			
			
			
			/*for (int i=0; i<vertices.length; i+=3){
				gl.glNormal3f(normals[i], normals[i+1], normals[i+2]);
				for (int j=0; j<9; j+=3){
					gl.glVertex3f(vertices[j+i], vertices[j+i+1], vertices[j+i+2]);
				}
			}*/
		}
		//gl.glEnd();
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
