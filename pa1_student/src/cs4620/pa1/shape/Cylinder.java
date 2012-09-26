package cs4620.pa1.shape;

import java.util.HashMap;
import java.util.Map;

public class Cylinder extends TriangleMesh
{
	private static final float DEFAULT_HEIGHT= 2;
	private static final float DEFAULT_RADIUS= 1;
	
	@Override
	public void buildMesh(float tolerance)
	{
		// TODO: (Problem 2) Fill in the code to create a cylinder mesh.
		int numCircumferencePoints= (int) (2*Math.PI*DEFAULT_RADIUS/tolerance);
		
		int vertexCount= numCircumferencePoints * 1 + 1; //TODO: should be * 4 + 2
		
		float[] vertices = new float[3*vertexCount];
		float[] normals = new float[3*vertexCount];
		
		int nextStart= addVertices(numCircumferencePoints, 0, vertices, normals, 1, false);
		
		//add center of top circle
		vertices[nextStart * 3]= 0;
		vertices[nextStart * 3 + 1]= 1;
		vertices[nextStart * 3 + 2]= 0;
		normals[nextStart * 3]= 0;
		normals[nextStart * 3 + 1]= 1;
		normals[nextStart * 3 + 2]= 0;
		
		int triangleCount= numCircumferencePoints * 1; //TODO: 1 should be 4
		
		int[] triangles= new int[3*triangleCount];
		
		for(int i= 0; i < numCircumferencePoints; i++)
		{
			triangles[i*3]= i;
			triangles[i*3 + 1]= numCircumferencePoints; //will always be the center of the circle
			triangles[i*3 + 2]= (i+1) % numCircumferencePoints;
		}
		
		setMeshData(vertices, normals, triangles);
	}
	
	private int addVertices(int vertexCount, int start, float[] vertices, float[] normals, float yCoord, boolean side)
	{
		float deltaTheta= (float) (2*Math.PI/vertexCount);
		for(int i= start; i < start + vertexCount; i++)
		{	
			float sinTheta= (float) Math.sin(deltaTheta*i);
			float cosTheta= (float) Math.cos(deltaTheta*i);
			//assuming x, y, z
			vertices[3*i]= cosTheta;
			vertices[3*i + 1]= yCoord;
			vertices[3*i + 2]= sinTheta;
			
			if(side)
			{
				
			}
			else
			{
				normals[3*i]= 0;
				normals[3*i + 2]= 0;
				if(yCoord > 0)
				{
					normals[3*i + 1]= 1;
				}
				else
				{
					normals[3*i + 1]= -1;
				}
			}
		}
		
		return start + vertexCount;
	}

	@Override
	public Object getYamlObjectRepresentation()
	{
		Map<Object,Object> result = new HashMap<Object, Object>();
		result.put("type", "Cylinder");
		return result;
	}
}
