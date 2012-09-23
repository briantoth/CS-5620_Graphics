package cs4620.pa1.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import layout.TableLayout;
import cs4620.pa1.scene.LightNode;

public class LightSettingPanel extends JPanel implements ChangeListener
{
	private static final long	serialVersionUID	= 1L;

	LightNode lightNode = null;
	boolean changeLightNode = false;

	private JSpinner aR, aG, aB;
	private JSpinner dR, dG, dB;
	private JSpinner sR, sG, sB;
	private JSpinner pX, pY, pZ;

	public LightSettingPanel()
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
					5, TableLayout.MINIMUM,
					5
				}
		};

		TableLayout tableLayout = new TableLayout(tableLayoutSize);
		setLayout(tableLayout);

		setBorder(BorderFactory.createTitledBorder("Light"));

		initLabels();
		initTextFields();
	}

	private void initLabels()
	{
		JLabel RLabel = new JLabel("R/X");
		add(RLabel, "3, 1, c, c");
		JLabel GLabel = new JLabel("G/Y");
		add(GLabel, "5, 1, c, c");
		JLabel BLabel = new JLabel("B/Z");
		add(BLabel, "7, 1, c, c");

		JLabel ambientLabel = new JLabel("ambient = ");
		add(ambientLabel, "1, 3, r, c");
		JLabel diffuseLabel = new JLabel("diffuse = ");
		add(diffuseLabel, "1, 5, r, c");
		JLabel specularLabel = new JLabel("specular = ");
		add(specularLabel, "1, 7, r, c");
		JLabel positionLabel = new JLabel("position = ");
		add(positionLabel, "1, 9, r, c");
	}

	private void initTextFields()
	{
	    aR = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));
	    aG = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));
	    aB = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));
	    dR = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));
	    dG = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));
	    dB = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));
	    sR = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));
	    sG = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));
	    sB = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0, 0.1));
	    pX = new JSpinner(new SpinnerNumberModel(0.0, -10.0, 10.0, 0.1));
	    pY = new JSpinner(new SpinnerNumberModel(0.0, -10.0, 10.0, 0.1));
	    pZ = new JSpinner(new SpinnerNumberModel(0.0, -10.0, 10.0, 0.1));

	    add(aR, "3, 3, 3, 3");
	    add(aG, "5, 3, 5, 3");
	    add(aB, "7, 3, 7, 3");

	    add(dR, "3, 5, 3, 5");
	    add(dG, "5, 5, 5, 5");
	    add(dB, "7, 5, 7, 5");

	    add(sR, "3, 7, 3, 7");
	    add(sG, "5, 7, 5, 7");
	    add(sB, "7, 7, 7, 7");

	    add(pX, "3, 9, 3, 9");
	    add(pY, "5, 9, 5, 9");
	    add(pZ, "7, 9, 7, 9");

	    aR.addChangeListener(this);
	    aG.addChangeListener(this);
	    aB.addChangeListener(this);
	    dR.addChangeListener(this);
	    dG.addChangeListener(this);
	    dB.addChangeListener(this);
	    sR.addChangeListener(this);
	    sG.addChangeListener(this);
	    sB.addChangeListener(this);
	    pX.addChangeListener(this);
	    pY.addChangeListener(this);
	    pZ.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent arg0)
	{
		if (lightNode != null && changeLightNode)
		{
			lightNode.ambient[0] = ((Double)aR.getValue()).floatValue();
			lightNode.ambient[1] = ((Double)aG.getValue()).floatValue();
			lightNode.ambient[2] = ((Double)aB.getValue()).floatValue();

			lightNode.diffuse[0] = ((Double)dR.getValue()).floatValue();
			lightNode.diffuse[1] = ((Double)dG.getValue()).floatValue();
			lightNode.diffuse[2] = ((Double)dB.getValue()).floatValue();

			lightNode.specular[0] = ((Double)sR.getValue()).floatValue();
			lightNode.specular[1] = ((Double)sG.getValue()).floatValue();
			lightNode.specular[2] = ((Double)sB.getValue()).floatValue();

			lightNode.position[0] = ((Double)pX.getValue()).floatValue();
			lightNode.position[1] = ((Double)pY.getValue()).floatValue();
			lightNode.position[2] = ((Double)pZ.getValue()).floatValue();
		}
	}

	public void setLightNode(LightNode lightNode)
	{
		changeLightNode = false;

		this.lightNode = lightNode;

		aR.setValue(new Double(this.lightNode.ambient[0]));
		aG.setValue(new Double(this.lightNode.ambient[1]));
		aB.setValue(new Double(this.lightNode.ambient[2]));

		dR.setValue(new Double(this.lightNode.diffuse[0]));
		dG.setValue(new Double(this.lightNode.diffuse[1]));
		dB.setValue(new Double(this.lightNode.diffuse[2]));

		sR.setValue(new Double(this.lightNode.specular[0]));
		sG.setValue(new Double(this.lightNode.specular[1]));
		sB.setValue(new Double(this.lightNode.specular[2]));

		pX.setValue(new Double(this.lightNode.position[0]));
		pY.setValue(new Double(this.lightNode.position[1]));
		pZ.setValue(new Double(this.lightNode.position[2]));

		changeLightNode = true;
	}
}
