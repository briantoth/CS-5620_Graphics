package cs4620.pa1.shape;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Point3f;
import javax.vecmath.Point3i;
import javax.vecmath.Vector3f;

public class Cube extends TriangleMesh {

	private static final float[] cubeVertices;
	private static final float[] cubeNormals;
	private static final int[]   cubeTriangles;

	static {
		/* The eight vertices of the cube. n = negative, p = positive */
		Point3f nnn = new Point3f(-1.0f, -1.0f, -1.0f);
		Point3f nnp = new Point3f(-1.0f, -1.0f, +1.0f);
		Point3f npn = new Point3f(-1.0f, +1.0f, -1.0f);
		Point3f npp = new Point3f(-1.0f, +1.0f, +1.0f);
		Point3f pnn = new Point3f(+1.0f, -1.0f, -1.0f);
		Point3f pnp = new Point3f(+1.0f, -1.0f, +1.0f);
		Point3f ppn = new Point3f(+1.0f, +1.0f, -1.0f);
		Point3f ppp = new Point3f(+1.0f, +1.0f, +1.0f);

		/* Normals for the different faces of the cube */
		Vector3f lNormal = new Vector3f(-1, 0, 0);
		Vector3f rNormal = new Vector3f(+1, 0, 0);
		Vector3f dNormal = new Vector3f(0, -1, 0);
		Vector3f uNormal = new Vector3f(0, +1, 0);
		Vector3f bNormal = new Vector3f(0, 0, -1);
		Vector3f fNormal = new Vector3f(0, 0, +1);

		// Vertex matrix
		Point3f[] vertices = new Point3f[] {
				ppp, npp, nnp, pnp, // Front face
				ppn, pnn, nnn, npn, // Back face
				ppn, ppp, pnp, pnn, // Right face
				npp, npn, nnn, nnp, // Left face
				ppp, ppn, npn, npp, // Up face
				pnp, nnp, nnn, pnn }; // Down face

		// Normal matrix
		Vector3f[] normals = new Vector3f[] {
				fNormal, fNormal, fNormal, fNormal,
				bNormal, bNormal, bNormal, bNormal,
				rNormal, rNormal, rNormal, rNormal,
				lNormal, lNormal, lNormal, lNormal,
				uNormal, uNormal, uNormal, uNormal,
				dNormal, dNormal, dNormal, dNormal };

		// Vertex indices
		Point3i[] triangles = new Point3i[] {
				new Point3i(0, 1, 2),
				new Point3i(2, 3, 0),
				new Point3i(4, 5, 6),
				new Point3i(6, 7, 4),
				new Point3i(8, 9, 10),
				new Point3i(10, 11, 8),
				new Point3i(12, 13, 14),
				new Point3i(14, 15, 12),
				new Point3i(16, 17, 18),
				new Point3i(18, 19, 16),
				new Point3i(20, 21, 22),
				new Point3i(22, 23, 20) };

		cubeVertices = new float[3*vertices.length];
		for(int i=0;i<vertices.length;i++)
		{
			cubeVertices[3*i  ] = vertices[i].x;
			cubeVertices[3*i+1] = vertices[i].y;
			cubeVertices[3*i+2] = vertices[i].z;
		}

		cubeNormals = new float[3*normals.length];
		for(int i=0;i<normals.length;i++)
		{
			cubeNormals[3*i  ] = normals[i].x;
			cubeNormals[3*i+1] = normals[i].y;
			cubeNormals[3*i+2] = normals[i].z;
		}

		cubeTriangles = new int[3*triangles.length];
		for(int i=0;i<triangles.length;i++)
		{
			cubeTriangles[3*i  ] = triangles[i].x;
			cubeTriangles[3*i+1] = triangles[i].y;
			cubeTriangles[3*i+2] = triangles[i].z;
		}
	}

	@Override
	public void buildMesh(float tolerance) {
		setMeshData(cubeVertices, cubeNormals, cubeTriangles);
	}

	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<Object,Object> result = new HashMap<Object, Object>();
		result.put("type", "Cube");
		return result;
	}
}
