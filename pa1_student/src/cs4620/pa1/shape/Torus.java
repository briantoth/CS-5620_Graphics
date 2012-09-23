package cs4620.pa1.shape;

import java.util.HashMap;
import java.util.Map;

public class Torus extends TriangleMesh
{
	private static float DEFAULT_MAJOR_RADIUS = 0.75f;
	private static float DEFAULT_MINOR_RADIUS = 0.25f;

	public Torus()
	{
		// NOP
	}

    private void torusVertex(
    		float R, float r,
    		int bigDiv, int smallDiv,
    		int bigIndex, int smallIndex,
    		float[] vertices, float[] normals, int pos)
    {
        float theta = (float)(bigIndex * 1.0f / bigDiv * 2 * Math.PI);
        float phi = (float)(smallIndex * 1.0f / smallDiv * 2 * Math.PI);

        float cosTheta = (float)Math.cos(theta);
        float sinTheta = (float)Math.sin(theta);
        float cosPhi = (float)Math.cos(phi);
        float sinPhi = (float)Math.sin(phi);

        vertices[3*pos]   = R*cosTheta + r*cosTheta*cosPhi;
        vertices[3*pos+1] = R*sinTheta + r*sinTheta*cosPhi;
        vertices[3*pos+2] = r*sinPhi;

        normals[3*pos]   = cosTheta*cosPhi;
        normals[3*pos+1] = sinTheta*cosPhi;
        normals[3*pos+2] = sinPhi;
    }

	@Override
	public void buildMesh(float tolerance) {
		int bigDiv = (int)Math.ceil(4*Math.PI*0.75 / tolerance);
		int smallDiv = (int) Math.ceil(4*Math.PI*0.25 / tolerance);

		int vertexCount = bigDiv * smallDiv;
		float[] vertices = new float[3*vertexCount];
		float[] normals = new float[3*vertexCount];

		for(int i0=0;i0<bigDiv;i0++)
			for(int j0=0;j0<smallDiv;j0++)
				torusVertex(DEFAULT_MAJOR_RADIUS, DEFAULT_MINOR_RADIUS,
						bigDiv, smallDiv, i0, j0, vertices, normals, i0*smallDiv+j0);

		int triangleCount = bigDiv * smallDiv * 2;
		int[] triangles = new int[3*triangleCount];
		for(int i0=0;i0<bigDiv;i0++)
		{
			int i1 = (i0+1) % bigDiv;
			for(int j0=0;j0<smallDiv;j0++)
			{
				int j1 = (j0+1) % smallDiv;

				triangles[6*(i0*smallDiv+j0)  ] = i0*smallDiv+j0;
				triangles[6*(i0*smallDiv+j0)+1] = i1*smallDiv+j0;
				triangles[6*(i0*smallDiv+j0)+2] = i1*smallDiv+j1;

				triangles[6*(i0*smallDiv+j0)+3] = i0*smallDiv+j0;
				triangles[6*(i0*smallDiv+j0)+4] = i1*smallDiv+j1;
				triangles[6*(i0*smallDiv+j0)+5] = i0*smallDiv+j1;
			}
		}

		setMeshData(vertices, normals, triangles);
	}

	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<Object,Object> result = new HashMap<Object, Object>();
		result.put("type", "Torus");
		return result;
	}
}
