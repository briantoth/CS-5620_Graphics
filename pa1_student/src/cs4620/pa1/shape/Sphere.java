package cs4620.pa1.shape;

import java.util.HashMap;
import java.util.Map;

public class Sphere extends TriangleMesh 
{
	private static double DEFAULT_RADIUS =1.0f;
	@Override
	public void buildMesh(float tolerance)
	{
		// figure out how many latitudes we need
		double r = DEFAULT_RADIUS;
		int numlats = (int) Math.floor(Math.PI * r / tolerance);
		// figure out how many vertices we need on the largest possible latitude:
		int numVL = (int) Math.ceil(2 * Math.PI * r / tolerance);
		// total vertices we need (plus the two poles)
		int numvertices = numlats * numVL + 2;
		// now we know how many vertices total there will be, so we can allocate our arrays
		float[] vertices = new float[3*numvertices];
		float[] normals = new float[3*numvertices];
		
		// we know first and last vertex (top and bottom pole)
		vertices[0] = 0; vertices[1] = 0; vertices[2] = 1;
		vertices[vertices.length-3] = 0; vertices[vertices.length-2] = 0;
		vertices[vertices.length-1] = -1;
		
		// now fill in the rest
		int point = 1;
		double theta, deltaphi;
		deltaphi = 2 * Math.PI / numVL;
		for (int i=1; i < numlats + 1; i++) {
			theta =  ( i * Math.PI / (numlats + 1) ); // theta from +z to xy plane
			for (int j = 0; j < numVL; j++) {
				vertices[3 * point + 0] = (float) (r * Math.sin(theta) * Math.cos(j * deltaphi)); // x
				vertices[3 * point + 1] = (float) (r * Math.sin(theta) * Math.sin(j * deltaphi)); // y
				vertices[3 * point + 2] = (float) (r * Math.cos(theta)); // z
				point += 1;
			}
		}
		
		// now do normals
		for (int i = 0; i < vertices.length; i++) {
			// because the normal will be defined relative to the origin
			// as are these points, the normal is going to simply be x, y, z
			normals[i] = vertices[i];
		}
		
		
		// now need to figure out how to connect all of these...
		// first we need to know how many triangles we're going to have
		// with the exception of the poles, each vertex will be in two triangles
		// with the latitude below it (it'll also be in two above, but we'd be double counting)
		// the poles will will make numxy triangles
		// numlats - 1 latitudes will make 2 * numxy triangles
		// so we can simplify numxy + numxy + 2 * numxy * (numlats - 1);
		int numtriangles = 2 * numVL * numlats;
		
		// ok we know how many triangles we can now allocate and fill
		int[] triangles = new int[3*numtriangles];
		
		
		// two special cases: poles
		// for top pole connect it to each pair of vertices in the "first" latitude
		point = 1;
		int triangle = 0;
		for (int i = 0; i < numVL - 1; i++) {
			triangles[3 * triangle + 0] =  (point + i);
			triangles[3 * triangle + 1] =  (point + i + 1);
			triangles[3 * triangle + 2] = 0;
			triangle += 1;
		}
		triangles[3 * triangle + 0] =  (point + numVL - 1);
		triangles[3 * triangle + 1] =  (point + 0);
		triangles[3 * triangle + 2] = 0;
		triangle += 1;
		
		// for latitudes in between... each vertex will be in part of two triangles below it
		// actually not true, each vertex will be part of four triangles below it,
		// but easiest to count two triangles for each vertex (else we'd double count)
		for (int i = 1; i < numlats; i++) {
			theta =  ( i * Math.PI / (numlats + 1) ); // theta from +z to xy plane
			// normal cases
			for (int j = 0; j < numVL - 1; j++) {
				// 5 6 2
				// 6 3 2
				// 6 4 3
				// 4 1 3
				triangles[3 * triangle + 0] =  (point + numVL + j + 0);
				triangles[3 * triangle + 1] =  (point + numVL + j + 1);
				triangles[3 * triangle + 2] =  (point + j);
				triangle += 1;
				triangles[3 * triangle + 0] =  (point + numVL + j + 1);
				triangles[3 * triangle + 1] =  (point + j + 1);
				triangles[3 * triangle + 2] =  (point + j + 0);
				triangle += 1;
			}
			// special case the last point
			triangles[3 * triangle + 0] =  (point + numVL + numVL - 1);
			triangles[3 * triangle + 1] =  (point + numVL + 0);
			triangles[3 * triangle + 2] =  (point + numVL - 1);
			triangle += 1;
			triangles[3 * triangle + 0] =  (point + numVL + 0);
			triangles[3 * triangle + 1] =  (point + 0);
			triangles[3 * triangle + 2] =  (point + numVL - 1);
			triangle += 1;
			point += numVL;
		}
		
		// for bottom pole connect it to each pair of vertices in the "last" latitude
		for (int i = 0; i < numVL - 1; i++) {
			triangles[3 * triangle + 0] = numvertices - 1;
			triangles[3 * triangle + 1] =  (point + i + 1);
			triangles[3 * triangle + 2] =  (point + i + 0);
			triangle += 1;
		}
		triangles[3 * triangle + 0] = numvertices - 1;
		triangles[3 * triangle + 1] =  (point + 0);
		triangles[3 * triangle + 2] = (point + numVL - 1);
		triangle += 1;
		point += numVL;
		
		setMeshData(vertices, normals, triangles);
	}

	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<Object,Object> result = new HashMap<Object, Object>();
		result.put("type", "Sphere");
		return result;
	}
}
