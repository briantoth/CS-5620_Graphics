package cs4620.pa1.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import layout.TableLayout;
import cs4620.pa1.scene.TransformationNode;

public class TransformSettingPanel extends JPanel implements ChangeListener
{
	private static final long	serialVersionUID	= 1L;

	private TransformationNode transformationNode = null;

	boolean changeTransformationNode = true;

	JSpinner tX, tY, tZ;
	JSpinner rX, rY, rZ;
	JSpinner sX, sY, sZ;

	public TransformSettingPanel()
	{
		double[][] tableLayoutSize = {
				{
					5, TableLayout.MINIMUM,
					5, TableLayout.FILL,
					5, TableLayout.FILL,
					5, TableLayout.FILL,
					5
				},
				{
					5, TableLayout.MINIMUM,
					5, TableLayout.MINIMUM,
					5, TableLayout.MINIMUM,
					5, TableLayout.MINIMUM,
					5
				}
		};

		TableLayout tableLayout = new TableLayout(tableLayoutSize);
		setLayout(tableLayout);

		setBorder(BorderFactory.createTitledBorder("Transformation"));

		initLabels();
		initTextFields();
	}

	private void initLabels()
	{
		JLabel XLabel = new JLabel("X");
		add(XLabel, "3, 1, c, c");
		JLabel GLabel = new JLabel("Y");
		add(GLabel, "5, 1, c, c");
		JLabel BLabel = new JLabel("Z");
		add(BLabel, "7, 1, c, c");

		JLabel ambientLabel = new JLabel("translation:");
		add(ambientLabel, "1, 3, r, c");
		JLabel diffuseLabel = new JLabel("rotation:");
		add(diffuseLabel, "1, 5, r, c");
		JLabel specularLabel = new JLabel("scaling:");
		add(specularLabel, "1, 7, r, c");
	}

	private void initTextFields()
	{
	    tX = new JSpinner(new SpinnerNumberModel(0.0, -10.0, 10.0, 0.1));
	    tY = new JSpinner(new SpinnerNumberModel(0.0, -10.0, 10.0, 0.1));
	    tZ = new JSpinner(new SpinnerNumberModel(0.0, -10.0, 10.0, 0.1));
	    rX = new JSpinner(new SpinnerNumberModel(0.0, -360.0, 360.0, 0.1));
	    rY = new JSpinner(new SpinnerNumberModel(0.0, -360.0, 360.0, 0.1));
	    rZ = new JSpinner(new SpinnerNumberModel(0.0, -360.0, 360.0, 0.1));
	    sX = new JSpinner(new SpinnerNumberModel(1.0, -10.0, 10.0, 0.1));
	    sY = new JSpinner(new SpinnerNumberModel(1.0, -10.0, 10.0, 0.1));
	    sZ = new JSpinner(new SpinnerNumberModel(1.0, -10.0, 10.0, 0.1));

	    add(tX, "3, 3, 3, 3");
	    add(tY, "5, 3, 5, 3");
	    add(tZ, "7, 3, 7, 3");

	    add(rX, "3, 5, 3, 5");
	    add(rY, "5, 5, 5, 5");
	    add(rZ, "7, 5, 7, 5");

	    add(sX, "3, 7, 3, 7");
	    add(sY, "5, 7, 5, 7");
	    add(sZ, "7, 7, 7, 7");

	    tX.addChangeListener(this);
	    tY.addChangeListener(this);
	    tZ.addChangeListener(this);
	    rX.addChangeListener(this);
	    rY.addChangeListener(this);
	    rZ.addChangeListener(this);
	    sX.addChangeListener(this);
	    sY.addChangeListener(this);
	    sZ.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent arg0)
	{
		if (changeTransformationNode && transformationNode != null)
		{
			transformationNode.translation.x = ((Double)tX.getValue()).floatValue();
			transformationNode.translation.y = ((Double)tY.getValue()).floatValue();
			transformationNode.translation.z = ((Double)tZ.getValue()).floatValue();

			transformationNode.rotation.x = ((Double)rX.getValue()).floatValue();
			transformationNode.rotation.y = ((Double)rY.getValue()).floatValue();
			transformationNode.rotation.z = ((Double)rZ.getValue()).floatValue();

			transformationNode.scaling.x = ((Double)sX.getValue()).floatValue();
			transformationNode.scaling.y = ((Double)sY.getValue()).floatValue();
			transformationNode.scaling.z = ((Double)sZ.getValue()).floatValue();
		}
	}

	public void setTransformationNode(TransformationNode transformationNode)
	{
		changeTransformationNode = false;

		this.transformationNode = transformationNode;

		tX.setValue(new Double(this.transformationNode.translation.x));
		tY.setValue(new Double(this.transformationNode.translation.y));
		tZ.setValue(new Double(this.transformationNode.translation.z));

		rX.setValue(new Double(this.transformationNode.rotation.x));
		rY.setValue(new Double(this.transformationNode.rotation.y));
		rZ.setValue(new Double(this.transformationNode.rotation.z));

		sX.setValue(new Double(this.transformationNode.scaling.x));
		sY.setValue(new Double(this.transformationNode.scaling.y));
		sZ.setValue(new Double(this.transformationNode.scaling.z));

		changeTransformationNode = true;
	}
}
