package cs4620.pa1.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import layout.TableLayout;
import cs4620.framework.GLFourViewPanel;
import cs4620.framework.GLSceneDrawer;
import cs4620.framework.GLViewPanelWithCameraControl;
import cs4620.framework.PerspectiveCameraController;
import cs4620.framework.PickingEventListener;

public class OneFourViewPanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	JPanel optionPanel;
	GLFourViewPanel fourViewPanel;
	GLViewPanelWithCameraControl oneViewPanel;

	ButtonGroup windowModeButtonGroup;
	JRadioButton fourViewRadioButton;
	JRadioButton oneViewRadioButton;

	JCheckBox wireframeCheckBox;
	JCheckBox lightingCheckBox;

	GLSceneDrawer drawer;

	TableLayout tableLayout;

	boolean showFourView = true;
	boolean showOneView = false;

	public OneFourViewPanel(GLSceneDrawer drawer)
	{
		tableLayout = new TableLayout(new double[][] {
			{ TableLayout.FILL },
			{ TableLayout.MINIMUM, TableLayout.FILL, 0 }
		});

		setLayout(tableLayout);

		fourViewPanel = new GLFourViewPanel(60, drawer);
		PerspectiveCameraController controller = new PerspectiveCameraController(
				fourViewPanel.getPerspectiveCamera(), drawer);
		add(fourViewPanel, "0, 1, 0, 1");

		oneViewPanel = new GLViewPanelWithCameraControl(60, controller, fourViewPanel.getSharedContext());
		add(oneViewPanel, "0, 2, 0, 2");
		oneViewPanel.setVisible(false);

		initOptionPanel();
		add(optionPanel, "0, 0, 0, 0");
	}

	private void initOptionPanel() {
		optionPanel = new JPanel();
		optionPanel.setLayout(new BorderLayout());

		JPanel radioButtonPanel = new JPanel();
		radioButtonPanel.setLayout(new FlowLayout());

		JPanel displayModePanel = new JPanel();

		windowModeButtonGroup = new ButtonGroup();

		oneViewRadioButton = new JRadioButton("1 View", false);
	    radioButtonPanel.add(oneViewRadioButton);
	    oneViewRadioButton.addActionListener(this);
	    windowModeButtonGroup.add(oneViewRadioButton);

	    fourViewRadioButton = new JRadioButton("4 Views", true);
	    radioButtonPanel.add(fourViewRadioButton);
	    fourViewRadioButton.addActionListener(this);
	    windowModeButtonGroup.add(fourViewRadioButton);

	    displayModePanel.add(radioButtonPanel, BorderLayout.LINE_START);

		displayModePanel.setLayout(new FlowLayout());
		optionPanel.add(displayModePanel, BorderLayout.LINE_END);

		wireframeCheckBox = new JCheckBox("Wireframe");
		wireframeCheckBox.setSelected(false);
		displayModePanel.add(wireframeCheckBox);

		lightingCheckBox = new JCheckBox("Lighting");
		lightingCheckBox.setSelected(true);
		displayModePanel.add(lightingCheckBox);
	}

	public void actionPerformed(ActionEvent e) {
		// Note: The lines marked (*) look redundant, but under Ubuntu they seem
		//       to be necessary to keep the panels visible after repeated switching.
		
		if (e.getSource() == oneViewRadioButton)
		{
			if (oneViewRadioButton.isSelected() && !showOneView)
			{
				oneViewPanel.setVisible(true); // (*)
				
				fourViewPanel.setVisible(false);
				fourViewPanel.stopAnimation();
				fourViewPanel.setSize(0,0);
				tableLayout.setRow(1, 0);
				showFourView = false;

				oneViewPanel.setVisible(true);
				oneViewPanel.startAnimation();
				oneViewPanel.setSize(oneViewPanel.getPreferredSize());
				tableLayout.setRow(2, TableLayout.FILL);
				showOneView = true;

				setSize(getPreferredSize());
			}
		}
		else if (e.getSource() == fourViewRadioButton)
		{
			if (fourViewRadioButton.isSelected() && !showFourView)
			{
				fourViewPanel.setVisible(true); // (*)
				
				oneViewPanel.setVisible(false);
				oneViewPanel.stopAnimation();
				oneViewPanel.setSize(0,0);
				tableLayout.setRow(2, 0);
				showOneView = false;
				
				fourViewPanel.setVisible(true);
				fourViewPanel.startAnimation();
				fourViewPanel.setSize(fourViewPanel.getPreferredSize());
				tableLayout.setRow(1, TableLayout.FILL);
				showFourView = true;

				setSize(getPreferredSize());
			}
		}
	}

	public boolean isWireframeMode()
	{
		return wireframeCheckBox.isSelected();
	}

	public boolean isLightingMode()
	{
		return lightingCheckBox.isSelected();
	}

	public void startAnimation()
	{
		if (showFourView)
			fourViewPanel.startAnimation();
		else
			oneViewPanel.startAnimation();
	}

	public void stopAnimation()
	{
		fourViewPanel.stopAnimation();
		oneViewPanel.stopAnimation();
	}

	public void addPickingEventListener(PickingEventListener listener)
	{
		fourViewPanel.addPickingEventListener(listener);
		oneViewPanel.addPickingEventListener(listener);
	}

	public void removePickingEventListener(PickingEventListener listener)
	{
		fourViewPanel.removePickingEventListener(listener);
		oneViewPanel.removePickingEventListener(listener);
	}

	public void addPrioritizedObjectId(int id)
	{
		fourViewPanel.addPrioritizedObjectId(id);
		oneViewPanel.addPrioritizedObjectId(id);
	}

	public void removePrioritizedObjectId(int id)
	{
		fourViewPanel.removePrioritizedObjectId(id);
		oneViewPanel.removePrioritizedObjectId(id);
	}
}
