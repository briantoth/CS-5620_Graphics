package cs4620.pa1.shape;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

// Wrap up the GLUT teapot object in our Mesh class
public class Teapot extends Mesh {

	private GLUT glut; // GLUT object, that will tesselate the teapot

	float scale = 1.0f; // Defines the scale of the teapot

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Teapot() {
		glut = new GLUT();
	}

	public void render(GL2 gl) {
		// GLU teapot edges are defined in reverse order
		gl.glFrontFace(GL2.GL_CW);

		glut.glutSolidTeapot(scale);

		// Restore the default ordering mode
		gl.glFrontFace(GL2.GL_CCW);
	}

	public void buildMesh(float tolerance) {
		// GLUT is in control of that
	}

	public Object getYamlObjectRepresentation() {
		Map<Object,Object> result = new HashMap<Object, Object>();
		result.put("type", "Teapot");
		return result;
	}

}
