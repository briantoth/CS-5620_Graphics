package cs4620.pa1.p3;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import layout.TableLayout;
import cs4620.framework.CameraController;
import cs4620.framework.GLSceneDrawer;
import cs4620.framework.PickingEventListener;
import cs4620.pa1.material.GLPhongMaterial;
import cs4620.pa1.scene.GLLightManager;
import cs4620.pa1.scene.LightNode;
import cs4620.pa1.scene.MeshNode;
import cs4620.pa1.scene.Scene;
import cs4620.pa1.scene.SceneNode;
import cs4620.pa1.scene.TransformationNode;
import cs4620.pa1.shape.Cube;
import cs4620.pa1.shape.Cylinder;
import cs4620.pa1.shape.Mesh;
import cs4620.pa1.shape.Sphere;
import cs4620.pa1.shape.Teapot;
import cs4620.pa1.shape.Torus;
import cs4620.pa1.ui.BasicAction;
import cs4620.pa1.ui.GLPhongMaterialSettingPanel;
import cs4620.pa1.ui.LightSettingPanel;
import cs4620.pa1.ui.OneFourViewPanel;
import cs4620.pa1.ui.PopupListener;
import cs4620.pa1.ui.ToleranceSliderPanel;
import cs4620.pa1.ui.TransformSettingPanel;
import cs4620.pa1.ui.TreeRenderer;

/**
 * Main window of Problem 3.
 *
 * This application can:
 * 1) Load, display, and save scene graph.
 * 2) Let the user manipulate scene graph by elements by entering text.
 *
 * @author pramook, arbree
 */
public class Problem3 extends JFrame implements GLSceneDrawer,
	ChangeListener, ActionListener, PickingEventListener, TreeSelectionListener

{
	private static final long serialVersionUID = 1L;

	//Menu commands
	private static final String SAVE_AS_MENU_TEXT = "Save As";
	private static final String OPEN_MENU_TEXT = "Open";
	private static final String EXIT_MENU_TEXT = "Exit";
	private static final String CLEAR_SELECTED_TEXT = "Clear selection";
	private static final String GROUP_MENU_TEXT = "Group selected";
	private static final String REPARENT_MENU_TEXT = "Reparent selected";
	private static final String DELETE_MENU_TEXT = "Delete selected";	
	private static final String ADD_LIGHT_MENU_TEXT = "Add Light";
	private static final String ADD_SPHERE_MENU_TEXT = "Add Sphere";
	private static final String ADD_CUBE_MENU_TEXT = "Add Cube";
	private static final String ADD_CYLINDER_MENU_TEXT = "Add Cylinder";
	private static final String ADD_TORUS_MENU_TEXT = "Add Torus";
	private static final String ADD_TEAPOT_MENU_TEXT = "Add Teapot";

	JSplitPane mainSplitPane;
	JSplitPane leftSplitPane;
	OneFourViewPanel oneFourViewPanel;
	ToleranceSliderPanel sliderPanel;
	JFileChooser fileChooser;
	JTree treeView;
	GLPhongMaterialSettingPanel phongMaterialPanel;
	TransformSettingPanel transformSettingPanel;
	LightSettingPanel lightSettingPanel;
	JPanel nodeSettingPanel;

	Scene scene;

	boolean drawForPicking = false;
	SceneNode[] nodesToReparent = null;
	boolean isReparenting = false;

	public Problem3() {
		super("CS 4620/5620 Programming Assignment 1 / Problem 3");
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent windowevent ) {
				terminate();
			}
		});

		initMainSplitPane();
		getContentPane().add(mainSplitPane, BorderLayout.CENTER);
		sliderPanel = new ToleranceSliderPanel(this);
		getContentPane().add(sliderPanel, BorderLayout.EAST);

		scene = new Scene();
		treeView.setModel(scene.getTreeModel());

		initActionsAndMenus();

		fileChooser = new JFileChooser(new File("data"));
	}

	public static void main(String[] args)
	{
		new Problem3().run();
	}

	public void run()
	{
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		oneFourViewPanel.startAnimation();
	}

	/**
	 * Maps all GUI actions to listeners in this object and builds the menu
	 */
	protected void initActionsAndMenus()
	{
		//Create all the actions
		BasicAction group = new BasicAction(GROUP_MENU_TEXT, this);
		BasicAction reparent = new BasicAction(REPARENT_MENU_TEXT, this);
		BasicAction delete = new BasicAction(DELETE_MENU_TEXT, this);
		BasicAction clear = new BasicAction(CLEAR_SELECTED_TEXT, this);

		BasicAction addLight = new BasicAction(ADD_LIGHT_MENU_TEXT, this);
		BasicAction addSphere = new BasicAction(ADD_SPHERE_MENU_TEXT, this);
		BasicAction addCube = new BasicAction(ADD_CUBE_MENU_TEXT, this);
		BasicAction addCylinder = new BasicAction(ADD_CYLINDER_MENU_TEXT, this);
		BasicAction addTorus = new BasicAction(ADD_TORUS_MENU_TEXT, this);
		BasicAction addTeapot = new BasicAction(ADD_TEAPOT_MENU_TEXT, this);

		BasicAction saveAs = new BasicAction(SAVE_AS_MENU_TEXT, this);
		BasicAction open = new BasicAction(OPEN_MENU_TEXT, this);
		BasicAction exit = new BasicAction(EXIT_MENU_TEXT, this);

		//Set shortcut keys
		group.setAcceleratorKey(KeyEvent.VK_G, KeyEvent.CTRL_MASK);
		reparent.setAcceleratorKey(KeyEvent.VK_R, KeyEvent.CTRL_MASK);
		delete.setAcceleratorKey(KeyEvent.VK_DELETE, 0);		

		saveAs.setAcceleratorKey(KeyEvent.VK_A, KeyEvent.CTRL_MASK);
		open.setAcceleratorKey(KeyEvent.VK_O, KeyEvent.CTRL_MASK);
		exit.setAcceleratorKey(KeyEvent.VK_Q, KeyEvent.CTRL_MASK);

		//Create the menu
		JMenuBar bar = new JMenuBar();
		JMenu menu;

		menu = new JMenu("File");
		menu.setMnemonic('F');
		menu.add(new JMenuItem(open));
		menu.add(new JMenuItem(saveAs));
		menu.addSeparator();
		menu.add(new JMenuItem(exit));
		bar.add(menu);

		menu = new JMenu("Edit");
		menu.setMnemonic('E');
		menu.add(new JMenuItem(group));
		menu.add(new JMenuItem(reparent));
		menu.add(new JMenuItem(delete));
		bar.add(menu);

		menu = new JMenu("Scene");
		menu.setMnemonic('S');
		menu.add(new JMenuItem(addLight));
		menu.add(new JMenuItem(addSphere));
		menu.add(new JMenuItem(addCube));
		menu.add(new JMenuItem(addCylinder));
		menu.add(new JMenuItem(addTorus));
		menu.add(new JMenuItem(addTeapot));
		bar.add(menu);

		setJMenuBar(bar);

		JPopupMenu p = new JPopupMenu();
		p.add(new JMenuItem(group));
		p.add(new JMenuItem(reparent));
		p.add(new JMenuItem(delete));
		p.add(new JMenuItem(clear));
		p.addSeparator();
		p.add(new JMenuItem(addLight));
		p.add(new JMenuItem(addSphere));
		p.add(new JMenuItem(addCube));
		p.add(new JMenuItem(addCylinder));
		p.add(new JMenuItem(addTorus));
		p.add(new JMenuItem(addTeapot));

		treeView.addMouseListener(new PopupListener(p));
		treeView.addTreeSelectionListener(this);
	}

	private void initMainSplitPane()
	{
		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
		initLeftSplitPane();
		mainSplitPane.setLeftComponent(leftSplitPane);

		oneFourViewPanel = new OneFourViewPanel(this);
		oneFourViewPanel.addPickingEventListener(this);
		mainSplitPane.setRightComponent(oneFourViewPanel);

		mainSplitPane.resetToPreferredSizes();
		mainSplitPane.setOneTouchExpandable(true);
		mainSplitPane.setResizeWeight(0.2);
	}

	private void initLeftSplitPane()
	{
		leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
		initTreeView();
		leftSplitPane.setTopComponent(treeView);

		nodeSettingPanel = new JPanel();
		nodeSettingPanel.setLayout(new TableLayout(new double[][] {
			{
				TableLayout.FILL
			},
			{
				TableLayout.MINIMUM,
				TableLayout.MINIMUM,
				TableLayout.MINIMUM
			}
		}));
		leftSplitPane.setBottomComponent(nodeSettingPanel);

		transformSettingPanel = new TransformSettingPanel();
		nodeSettingPanel.add(transformSettingPanel, "0,0,0,0");
		transformSettingPanel.setVisible(false);

		phongMaterialPanel = new GLPhongMaterialSettingPanel();
		nodeSettingPanel.add(phongMaterialPanel, "0,1,0,1");
		phongMaterialPanel.setVisible(false);

		lightSettingPanel = new LightSettingPanel();
		nodeSettingPanel.add(lightSettingPanel, "0,2,0,2");
		lightSettingPanel.setVisible(false);

		leftSplitPane.resetToPreferredSizes();
		leftSplitPane.setOneTouchExpandable(true);
		leftSplitPane.setResizeWeight(0.95);
	}

	private void initTreeView()
	{
		// Create the tree views
		treeView = new JTree();
		DefaultTreeCellRenderer renderer = new TreeRenderer();
		treeView.setCellRenderer(renderer);
		treeView.setEditable(true);
		treeView.setCellEditor(new DefaultTreeCellEditor(treeView, renderer));
		treeView.setShowsRootHandles(true);
		treeView.setRootVisible(true);

		KeyListener[] kls = treeView.getKeyListeners();
		for (int i=0; i<kls.length; i++)
			treeView.removeKeyListener(kls[i]);
	}

	private float getTolerance()
	{
		return sliderPanel.getTolerance();
	}

	public void init(GLAutoDrawable drawable, CameraController cameraController) {
		final GL2 gl = drawable.getGL().getGL2();

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Set depth buffer.
		gl.glClearDepth(1.0f);
		gl.glDepthFunc(GL2.GL_LESS);
		gl.glEnable(GL2.GL_DEPTH_TEST);

		// Set blending mode.
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDisable(GL2.GL_BLEND);

		// Forces OpenGL to normalize transformed normals to be of
		// unit length before using the normals in OpenGL's lighting equations.
		gl.glEnable(GL2.GL_NORMALIZE);

		// Cull back faces.
		gl.glDisable(GL2.GL_CULL_FACE);

		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

		rebuildMeshes();
		oneFourViewPanel.startAnimation();
	}


	public void draw(GLAutoDrawable drawable, CameraController cameraController)
	{
		final GL2 gl = drawable.getGL().getGL2();

		if (drawForPicking)
		{
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			GLLightManager.teardownLighting(gl);
			scene.renderForPicking(gl);
		}
		else
		{
			gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE);
			gl.glEnable(GL2.GL_COLOR_MATERIAL);

			if (oneFourViewPanel.isWireframeMode())
				gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
			else
				gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

			if (oneFourViewPanel.isLightingMode())
				scene.setupLighting(gl);
			else
				gl.glDisable(GL2.GL_LIGHTING);

			scene.render(gl);

			if (oneFourViewPanel.isLightingMode())
				GLLightManager.teardownLighting(gl);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == sliderPanel.getSlider())
		{
			rebuildMeshes();
		}
	}

	protected void rebuildMeshes()
	{
		scene.rebuildMeshes(getTolerance());
	}

	public void terminate()
	{
		oneFourViewPanel.stopAnimation();
		dispose();
		System.exit(0);
	}

	protected void refresh()
	{
		oneFourViewPanel.repaint();
	}

	/**
	 * Save the current tree to a file.
	 */
	protected void saveTreeAs()
	{
		//Pick a file
		int choice = fileChooser.showSaveDialog(this);
		if (choice != JFileChooser.APPROVE_OPTION)
		{
			refresh();
			return;
		}
		String filename = fileChooser.getSelectedFile().getAbsolutePath();

		//Write the tree out
		try
		{
			scene.save(filename);
		}
		catch (IOException ioe)
		{
			showExceptionDialog(ioe);
		}

		refresh();
	}

	/**
	 * Displays an exception in a window
	 * @param e
	 */
	protected void showExceptionDialog(Exception e)
	{
		String str = "The following exception was thrown: " + e.toString() + ".\n\n" + "Would you like to see the stack trace?";
		int choice = JOptionPane.showConfirmDialog(this, str, "Exception Thrown", JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads a tree stored in a file
	 */
	protected void openTree()
	{
		//Select a file
		int choice = fileChooser.showOpenDialog(this);
		if (choice != JFileChooser.APPROVE_OPTION)
		{
			refresh();
			return;
		}
		String filename = fileChooser.getSelectedFile().getAbsolutePath();

		//Load the tree
		try
		{
			scene.load(filename);
			rebuildMeshes();
		}
		catch (Exception e) {
			showExceptionDialog(e);
		}

		//Update the window
		refresh();
	}

	/**
	 * Add a new shape to the tree
	 */
	protected void addNewShape(Class<? extends Mesh> c)
	{
		TreePath path = treeView.getSelectionPath();

		//Add the node
		try
		{
			scene.addNewShape(path, c);
			rebuildMeshes();
			refresh();
		}
		catch (Exception e) {
			showExceptionDialog(e);
		}
	}

	protected SceneNode[] getSelection()
	{
		TreePath[] paths = treeView.getSelectionPaths();
		if (paths == null) {
			return new SceneNode[] {};
		}
		SceneNode[] ts = new SceneNode[paths.length];
		for (int i = 0; i < paths.length; i++) {
			ts[i] = (SceneNode) paths[i].getLastPathComponent();
		}
		return ts;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd == null) {
			return;
		}
		else if (cmd.equals(GROUP_MENU_TEXT)) {
			SceneNode groupNode = scene.groupNodes(getSelection(), "Group");
			treeView.expandPath(new TreePath(groupNode.getPath()));
			refresh();
		}
		else if (cmd.equals(CLEAR_SELECTED_TEXT)) {
			treeView.clearSelection();
		}
		else if (cmd.equals(REPARENT_MENU_TEXT)) {
			nodesToReparent = getSelection();
			isReparenting = true;
		}
		else if (cmd.equals(DELETE_MENU_TEXT)) {
			scene.deleteNodes(getSelection());
			refresh();
		}
		else if (cmd.equals(ADD_LIGHT_MENU_TEXT)) {
			scene.addNewLight(treeView.getSelectionPath());
		}
		else if (cmd.equals(ADD_SPHERE_MENU_TEXT)) {
			addNewShape(Sphere.class);
		}
		else if (cmd.equals(ADD_CUBE_MENU_TEXT)) {
			addNewShape(Cube.class);
		}
		else if (cmd.equals(ADD_CYLINDER_MENU_TEXT)) {
			addNewShape(Cylinder.class);
		}
		else if (cmd.equals(ADD_TORUS_MENU_TEXT)) {
			addNewShape(Torus.class);
		}
		else if (cmd.equals(ADD_TEAPOT_MENU_TEXT)) {
			addNewShape(Teapot.class);
		}
		else if (cmd.equals(OPEN_MENU_TEXT)) {
			openTree();
		}
		else if (cmd.equals(SAVE_AS_MENU_TEXT)) {
			saveTreeAs();
		}
		else if (cmd.equals(EXIT_MENU_TEXT)) {
			terminate();
		}
	}

	@Override
	public void objectPicked(Object source, int objectId,
			Vector3f pickLocation, Vector2f mousePosition)
	{				
		SceneNode node = scene.searchForMeshId(objectId);
		if (node != null)
			treeView.setSelectionPath(new TreePath(node.getPath()));		
	}

	@Override
	public void startPickingMode(Object source)
	{
		drawForPicking = true;
	}

	@Override
	public void stopPickingMode(Object source)
	{
		drawForPicking = false;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		SceneNode[] selection = getSelection();

		// Handle reparenting.
		if (isReparenting)
		{
			// Invalid number of new parents selected?
			if (selection.length != 1) return;
			SceneNode parent = selection[0];
			scene.reparent(nodesToReparent, parent);
			isReparenting = false;
		}

		showHideSettingPanels(selection);
		
		refresh();
	}

	private void showHideSettingPanels(SceneNode[] selection)
	{
		if (selection.length == 1)
		{
			SceneNode node = selection[0];

			int visibleCount = 0;

			if (node instanceof TransformationNode)
			{
				TransformationNode transformationNode = (TransformationNode)node;
				transformSettingPanel.setTransformationNode(transformationNode);
				transformSettingPanel.setVisible(true);

				nodeSettingPanel.add(transformSettingPanel, "0,"+Integer.toString(visibleCount)+",0,"+Integer.toString(visibleCount));
				visibleCount += 1;
			}
			else
				transformSettingPanel.setVisible(false);

			if (node instanceof MeshNode)
			{
				MeshNode meshNode = (MeshNode)node;
				GLPhongMaterial material = (GLPhongMaterial)meshNode.getMaterial();
				phongMaterialPanel.setMaterial(material);
				phongMaterialPanel.setVisible(true);

				nodeSettingPanel.add(phongMaterialPanel, "0,"+Integer.toString(visibleCount)+",0,"+Integer.toString(visibleCount));
				visibleCount += 1;
			}
			else
				phongMaterialPanel.setVisible(false);

			if (node instanceof LightNode)
			{
				LightNode lightNode = (LightNode)node;
				lightSettingPanel.setLightNode(lightNode);
				lightSettingPanel.setVisible(true);

				nodeSettingPanel.add(lightSettingPanel, "0,"+Integer.toString(visibleCount)+",0,"+Integer.toString(visibleCount));
				visibleCount += 1;
			}
			else
				lightSettingPanel.setVisible(false);
		}
		else
		{
			phongMaterialPanel.setVisible(false);
			transformSettingPanel.setVisible(false);
			lightSettingPanel.setVisible(false);
		}
	}

	@Override
	public void mousePressed(MouseEvent e, CameraController controller) {
		// NOP
	}

	@Override
	public void mouseReleased(MouseEvent e, CameraController controller) {
		// NOP
	}

	@Override
	public void mouseDragged(MouseEvent e, CameraController controller) {
		// NOP
	}
}
