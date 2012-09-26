package cs4620.pa1.shape;

import java.util.HashMap;
import java.util.Map;

public class Sphere extends TriangleMesh 
{
	private static double DEFAULT_RADIUS =1.0f;
	@Override
	public void buildMesh(float tolerance)
	{
		// TODO: (Problem 2) Fill in the code to create a sphere mesh.
		// poles at y=1 and y=-1, project this onto the xy plane and you get a circle
		// need to divide this circle up into cross sections by n lines of y =constant
		// need to pick n so that n = floor(pi*r/tolerance) (this is number of latitude lines)
		// now for every latitude line find r2 and then we need ceil(2*pi*r2 / tolerance) vertices at that lattitude
		// to do that we need to know at what angle (relative to the xz plane) the latitude lines lie at
		// well, we know we cut pi radians into n + 1 slices and n lattitude point/lines, so the angles are
		// pi/(n+1), 2*pi/(n+1), 3*pi/(n+1), ..., n*pi/(n+1)
		// now based on that angle, need to figure out the size of the circular cross section...
		// well, we can project into xy again, we know an angle relative to x, and we want to find
		// a line segment length where two of the sides are of size r, and we know the angle between them
		// (which is going to be pi - angle from the x plane)
		// which means we get the diameter of our lattitude circle by law of cosines
		// r = sqrt(2*r^2(1 - cos(pi - angle))) / 2
		
		
		// figure out how many latitudes we need
		double rxy = DEFAULT_RADIUS;
		int numxy = (int) Math.floor(Math.PI * rxy / tolerance);
		// for each lattitude
		int numvertices = 2;
		int numVL, point;
		double theta, rxz, phi;
		for (int i=1; i < numxy + 1; i++) {
			theta =  ( (i)*Math.PI / ( (numxy + 1))); // theta from xz plane to y
			rxz =  Math.sqrt(2*Math.pow(rxy, 2)*(1 - Math.cos(Math.PI - theta)));
			numvertices += (int) Math.ceil(2 * Math.PI * rxz / tolerance);
		}
		// now we know how many vertices total there will be, so we can allocate our arrays
		float[] vertices = new float[3*numvertices];
		float[] normals = new float[3*numvertices];
		// we know first and last vertex (top and bottom pole)
		vertices[0] = 0; vertices[1] = 1; vertices[2] = 0;
		vertices[vertices.length-3] = 0; vertices[vertices.length-2] = -1;
		vertices[vertices.length-1] = 0;
		point = 1;
		for (int i=1; i < numxy + 1; i++) {
			theta =  ( ( i)*Math.PI / ( (numxy + 1))); // theta from xz plane to y
			rxz =  Math.sqrt(2*Math.pow(rxy, 2)*(1 - Math.cos(Math.PI - theta)));
			numVL = (int) Math.ceil(2 * Math.PI * rxz / tolerance);
			phi =  (2 * Math.PI / numVL);
			for (int j = 0; j < numVL; j++) {
				vertices[point] = (float) (rxy * Math.cos(theta));
				vertices[point+1] = (float) (rxy * Math.sin(theta));
				vertices[point+2] = (float) (rxz * Math.sin(phi));
				point += 1;
				assert(point != numvertices - 1);
			}
		}
		
		for (int i = 0; i < vertices.length; i++) {
			// and because the normal will be defined relative to the origin
			// as are these points, the normal is going to simply be x, y, z
			normals[i] = vertices[i];
		}
			// theta from xz plane is i*pi/(n+1)
			// r = sqrt(2*r^2(1-cos(pi-theta))) / 2
			// so circumferance is 2*pi*r
			// so we need to break this up into 2*pi*r/tolerance sections
			// so we need ceil(2*pi*r/tolerance) vertices
			// angles between them will be 2*pi/#vertices = phi
			// y = r * sin(theta)
			// x = r * cos(theta)
			// z = r * sin(phi)
			
		
		// now need to figure out how to connect all of these...
		// first we need to know how many triangles we're going to have
		// with the exception of the poles, each vertex will be in two triangles
		// with the latitude below it (it'll also be in two above, but we'd be double counting)
		int numtriangles = 0;
		for (int i = 1; i < numxy; i++) {
			theta =  ( (i)*Math.PI / ( (numxy + 1))); // theta from xz plane to y
			rxz =  Math.sqrt(2*Math.pow(rxy, 2)*(1 - Math.cos(Math.PI - theta)));
			numVL = (int) Math.ceil(2 * Math.PI * rxz / tolerance);
			numtriangles += 2 * numVL;
		}
		// top pole
		theta =  (Math.PI / ( (numxy + 1))); // theta from xz plane to y
		rxz =  Math.sqrt(2*Math.pow(rxy, 2)*(1 - Math.cos(Math.PI - theta)));
		numVL = (int) Math.ceil(2 * Math.PI * rxz / tolerance);
		numtriangles += numVL;
		// bottom pole
		theta =  (numxy * Math.PI / ( (numxy + 1))); // theta from xz plane to y
		rxz =  Math.sqrt(2*Math.pow(rxy, 2)*(1 - Math.cos(Math.PI - theta)));
		numVL = (int) Math.ceil(2 * Math.PI * rxz / tolerance);
		numtriangles += numVL;
		
		// ok we know how many triangles we can now allocate and fill
		int[] triangles = new int[3*numtriangles];
		// two special cases: poles
		// for top pole connect it to each pair of vertices in the "first" latitude
		theta = Math.PI / (numxy + 1);
		rxz =  Math.sqrt(2*Math.pow(rxy, 2)*(1 - Math.cos(Math.PI - theta)));
		numVL = (int) Math.ceil(2 * Math.PI * rxz / tolerance);
		point = 1;
		int triangle = 0;
		for (int i = 0; i < numVL - 1; i++) {
			triangles[3 * triangle + 0] = point*3 + i*3;
			triangles[3 * triangle + 1] = point*3 + i*3 + 3;
			triangles[3 * triangle + 2] = 0;
			triangle += 1;
		}
		triangles[3 * triangle + 0] = point*3 + (numVL - 1)*3;
		triangles[3 * triangle + 1] = point*3 + 0;
		triangles[3 * triangle + 2] = 0;
		triangle += 1;
		point += numVL;
		// for latitudes in between... each vertex will be in part of two triangles below 
		// (or above) it so for a latitude a (size m), with latitude b (size n which should
		// be m+1 or m-1) below it, b1 in b1 a1 a0 and b1 b2 a1, b2 in  b2 a2 a1 and b2 b3 a2
		// so for b0 we get b0 a0 am and b0 b1 a0
		// for bn we get bn an a(n-1) and bn b0 am
		double thetab, rxzb;
		int numVLb;
		for (int i = 1; i < numxy - 1; i++) {
			theta =  ( (i)*Math.PI / ( (numxy + 1))); // theta from xz plane to y
			rxz =  Math.sqrt(2*Math.pow(rxy, 2)*(1 - Math.cos(Math.PI - theta)));
			numVL = (int) Math.ceil(2 * Math.PI * rxz / tolerance);
			thetab =  ( (i+1)*Math.PI / ( (numxy + 1))); // theta from xz plane to y
			rxzb =  Math.sqrt(2*Math.pow(rxy, 2)*(1 - Math.cos(Math.PI - theta)));
			numVLb = (int) Math.ceil(2 * Math.PI * rxz / tolerance);
			// first special case: b0
			triangles[3 * triangle + 0] = 3 * (point + numVL + 0);
			triangles[3 * triangle + 1] = 3 * (point + numVL + 0 + 1);
			triangles[3 * triangle + 2] = 3 * (point + 0);
			triangle += 1;
			triangles[3 * triangle + 0] = 3 * (point + numVL + 0);
			triangles[3 * triangle + 1] = 3 * (point + 0);
			triangles[3 * triangle + 2] = 3 * (point + numVL - 1);
			triangle += 1;
			// normal cases
			for (int j = 1; j < numVLb - 1; j++) {
				triangles[3 * triangle + 0] = 3 * (point + numVL + j);
				triangles[3 * triangle + 1] = 3 * (point + numVL + j + 1);
				triangles[3 * triangle + 2] = 3 * (point + j);
				triangle += 1;
				triangles[3 * triangle + 0] = 3 * (point + numVL + j);
				triangles[3 * triangle + 1] = 3 * (point + j);
				triangles[3 * triangle + 2] = 3 * (point + j - 1);
				triangle += 1;
			}
			// special case bn
			triangles[3 * triangle + 0] = 3 * (point + numVL + numVLb);
			triangles[3 * triangle + 1] = 3 * (point + numVL + 0);
			triangles[3 * triangle + 2] = 3 * (point + numVLb);
			triangle += 1;
			triangles[3 * triangle + 0] = 3 * (point + numVL + numVLb);
			triangles[3 * triangle + 1] = 3 * (point + numVLb);
			triangles[3 * triangle + 2] = 3 * (point + numVLb - 1);
			triangle += 1;
			point += numVL;
		}
		// for bottom pole connect it to each pair of vertices in the "last" latitude
		theta =  (numxy * Math.PI / ( (numxy + 1))); // theta from xz plane to y
		rxz =  Math.sqrt(2*Math.pow(rxy, 2)*(1 - Math.cos(Math.PI - theta)));
		numVL = (int) Math.ceil(2 * Math.PI * rxz / tolerance);
		for (int i = 0; i < numVL - 1; i++) {
			triangles[3 * triangle + 0] = vertices.length - 3;
			triangles[3 * triangle + 1] = point*3 + i*3 + 3;
			triangles[3 * triangle + 2] = point*3 + i*3;
			triangle += 1;
		}
		triangles[3 * triangle + 0] = vertices.length - 3;
		triangles[3 * triangle + 1] = point*3 + 0;
		triangles[3 * triangle + 2] = point*3 + (numVL - 1)*3;
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
