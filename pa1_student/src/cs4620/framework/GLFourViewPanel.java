package cs4620.framework;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLPbuffer;
import javax.media.opengl.GLProfile;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public class GLFourViewPanel extends JPanel implements GLEventListener, ActionListener, ViewsCoordinator {

	private static final long serialVersionUID = 1L;

	protected int initialFrameRate;

	protected GLView topView;
	protected GLView frontView;
	protected GLView perspectiveView;
	protected GLView rightView;

	protected PerspectiveCamera perspectiveCamera;
	protected OrthographicCamera frontCamera;
	protected OrthographicCamera rightCamera;
	protected OrthographicCamera topCamera;

	protected PickingController perspectiveController;
	protected PickingController frontController;
	protected PickingController rightController;
	protected PickingController topController;

	protected Timer timer;

	protected GLProfile glProfile;
	protected GLCapabilities glCapabilities;
	protected GLPbuffer sharedDrawable;

	protected boolean disposed = false;

	protected GLSceneDrawer drawer;

	protected boolean[] viewsUpdated = new boolean[4];

	public GLFourViewPanel(int frameRate, GLSceneDrawer drawer)
	{
		super();
		initLayout();
		initGLCapabilities();
		initTimer(frameRate);
		this.drawer = drawer;
		initCameras();
		initViews(drawer);
	}

	private void initViews(GLSceneDrawer drawer) {
		sharedDrawable = GLDrawableFactory.getFactory(glProfile).createGLPbuffer(null, glCapabilities, null, 1, 1, null);

		topController = new PickingController(new OrthographicCameraController(topCamera, drawer, this, 0));
		topView = createView(topController, sharedDrawable.getContext());

		perspectiveController = new PickingController(new PerspectiveCameraController(perspectiveCamera, drawer, this, 1));
		perspectiveView = createView(perspectiveController, sharedDrawable.getContext());

		frontController = new PickingController(new OrthographicCameraController(frontCamera, drawer, this, 2));
		frontView = createView(frontController, sharedDrawable.getContext());

		rightController = new PickingController(new OrthographicCameraController(rightCamera, drawer, this, 3));
		rightView = createView(rightController, sharedDrawable.getContext());
	}

	private void initCameras() {
		topCamera = new OrthographicCamera(
				new Point3f(0, 10, 0), new Point3f(0,0,0), new Vector3f(0,0,-1),
				0.1f, 100.0f, 45.0f);
		frontCamera = new OrthographicCamera(
				new Point3f(0, 0, 10), new Point3f(0,0,0), new Vector3f(0,1,0),
				0.1f, 100.0f, 45.0f);
		rightCamera = new OrthographicCamera(
				new Point3f(10, 0, 0), new Point3f(0,0,0), new Vector3f(0,1,0),
				0.1f, 100.0f, 45.0f);
		perspectiveCamera = new PerspectiveCamera(
				new Point3f(5,5,5), new Point3f(0,0,0), new Vector3f(0,1,0),
				0.1f, 100, 45);
	}

	private GLView createView(PickingController controller, GLContext sharedWith) {
		GLView view;
		if (sharedWith == null)
			view = new GLView(glCapabilities);
		else
			view = new GLView(glCapabilities, sharedWith);
		add(view);
		view.addGLController(controller);
		view.addGLEventListener(this);
		return view;
	}

	private void initTimer(int frameRate) {
		initialFrameRate = frameRate;
		timer = new Timer(1000 / frameRate, this);
	}

	private void initGLCapabilities() {
		glProfile = GLProfile.getDefault();
		glCapabilities = new GLCapabilities( glProfile );
	}

	private void initLayout() {
		GridLayout layout = new GridLayout(2,2,5,5);
		setLayout(layout);
	}

	public void dispose()
	{
		if (!disposed)
		{
			disposed = true;
		}
	}

	protected void finalize()
	{
		dispose();
	}

	@Override
	public void init(GLAutoDrawable arg0)
	{
		startAnimation();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// NOP
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		((Component)drawable).setMinimumSize(new Dimension(0,0));
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// NOP
	}

	public void startAnimation() {
		timer.start();
	}

	public void stopAnimation() {
		timer.stop();
	}

	public PerspectiveCamera getPerspectiveCamera() {
		return perspectiveCamera;
	}

	public OrthographicCamera getFrontCamera() {
		return frontCamera;
	}

	public OrthographicCamera getRightCamera() {
		return rightCamera;
	}

	public OrthographicCamera getTopCamera() {
		return topCamera;
	}

	protected void repaintViews()
	{
		topView.repaint();
		frontView.repaint();
		rightView.repaint();
		perspectiveView.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == timer)
		{
			repaintViews();
		}
	}

	public void immediatelyRepaint()
	{
		repaintViews();
	}

	public GLContext getSharedContext()
	{
		return sharedDrawable.getContext();
	}

	public void addPickingEventListener(PickingEventListener listener)
	{
		perspectiveController.addPickingEventListener(listener);
		frontController.addPickingEventListener(listener);
		topController.addPickingEventListener(listener);
		rightController.addPickingEventListener(listener);
	}

	public void removePickingEventListener(PickingEventListener listener)
	{
		perspectiveController.removePickingEventListener(listener);
		frontController.removePickingEventListener(listener);
		topController.removePickingEventListener(listener);
		rightController.removePickingEventListener(listener);
	}

	public void addPrioritizedObjectId(int id)
	{
		perspectiveController.addPrioritizedObjectId(id);
		frontController.addPrioritizedObjectId(id);
		topController.addPrioritizedObjectId(id);
		rightController.addPrioritizedObjectId(id);
	}

	public void removePrioritizedObjectId(int id)
	{
		perspectiveController.removePrioritizedObjectId(id);
		frontController.removePrioritizedObjectId(id);
		topController.removePrioritizedObjectId(id);
		rightController.removePrioritizedObjectId(id);
	}

	public void captureNextFrame()
	{
		perspectiveController.getCameraController().captureNextFrame();
	}

    public void resetUpdatedStatus()
    {
		for (int i = 0; i < 4; i++)
			viewsUpdated[i] = false;
    }

    public boolean checkAllViewsUpdated()
    {
	    return viewsUpdated[0] && viewsUpdated[1] && viewsUpdated[2] && viewsUpdated[3];
    }

	@Override
    public void setViewUpdated(int viewId)
    {
	    viewsUpdated[viewId] = true;
    }
}
