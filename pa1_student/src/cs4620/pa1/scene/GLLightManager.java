package cs4620.pa1.scene;

import javax.media.opengl.GL2;

public class GLLightManager
{
	/**
	 * The IDs of OpenGL lights.
	 */
	protected final static int[] lightIds = { GL2.GL_LIGHT0, GL2.GL_LIGHT1, GL2.GL_LIGHT2, GL2.GL_LIGHT3, GL2.GL_LIGHT4, GL2.GL_LIGHT5, GL2.GL_LIGHT6, GL2.GL_LIGHT7 };

	/**
	 * The number of lights used so far.
	 */
	private static int usedLightCount = 0;

	public static int getNextLightId()
	{
		if (usedLightCount < lightIds.length)
		{
			int result = lightIds[usedLightCount];
			usedLightCount++;
			return result;
		}
		else
		{
			return -1;
		}
	}

	public static boolean hasNextLight()
	{
		return usedLightCount < lightIds.length;
	}

	public static void startSettingUpLighting(GL2 gl)
	{
		usedLightCount = 0;
		gl.glEnable(GL2.GL_LIGHTING);
	}

	public static void teardownLighting(GL2 gl)
	{
		gl.glDisable(GL2.GL_LIGHTING);
		for(int i=0;i<lightIds.length;i++)
			gl.glDisable(lightIds[i]);
	}
}
