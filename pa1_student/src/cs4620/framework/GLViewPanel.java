package cs4620.framework;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLProfile;
import javax.swing.JPanel;
import javax.swing.Timer;

public abstract class GLViewPanel extends JPanel
	implements GLController, ActionListener
{
	private static final long serialVersionUID = 1L;

	protected int initialFrameRate;
	protected GLView glView;
	protected Timer timer;

	public GLViewPanel(int frameRate, GLContext sharedWith)
	{
		super(new BorderLayout());

		initialFrameRate = frameRate;

		GLProfile glProfile = GLProfile.getDefault();
		GLCapabilities glCapabilities = new GLCapabilities( glProfile );
		if (sharedWith != null)
			glView = new GLView( glCapabilities, sharedWith );
		else
			glView = new GLView( glCapabilities );
		glView.addGLController(this);

		timer = new Timer(1000 / initialFrameRate, this);

		add( glView, BorderLayout.CENTER );

	}

	public GLViewPanel(int frameRate)
	{
		this(frameRate, null);
	}

	public void init(GLAutoDrawable drawable) {
		timer.start();
	}

	public void display(GLAutoDrawable drawable) {
		// NOP
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		// NOP
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		((Component)drawable).setMinimumSize(new Dimension(0,0));
	}

	public void keyTyped(KeyEvent key) {
		// NOP
	}

	public void keyPressed(KeyEvent key)
	{
		// NOP
	}

	public void keyReleased(KeyEvent key) {
		// NOP
	}

	public void mouseClicked(MouseEvent mouse) {
		// NOP
	}

	public void mousePressed(MouseEvent mouse) {
		// NOP
	}

	public void mouseReleased(MouseEvent mouse) {
		// NOP
	}

	public void mouseEntered(MouseEvent mouse) {
		// NOP
	}

	public void mouseExited(MouseEvent mouse) {
		// NOP
	}

	public void mouseDragged(MouseEvent mouse) {
		// NOP
	}

	public void mouseMoved(MouseEvent e) {
		// NOP
	}

	public void dispose(GLAutoDrawable drawable) {
		// NOP
	}

	public void startAnimation() {
		timer.start();
	}

	public void stopAnimation() {
		timer.stop();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == timer)
		{
			glView.repaint();
		}
	}

	public void immediatelyRepaint()
	{
		glView.repaint();
	}

	public void addGLController(GLController controller) {
		glView.addGLController(controller);
	}
}