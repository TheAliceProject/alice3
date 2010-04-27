package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.alice.interact.SnapState;

public class SnapControlPanel extends JPanel implements ChangeListener, ActionListener
{
	private SnapState snapState;
	
	protected JCheckBox snapOnOffCheckbox;
	protected JSpinner gridSizeSpinner;
	protected SpinnerNumberModel gridSizeModel;
	
	public SnapControlPanel(SnapState snapState)
	{
		this.snapState = snapState;
		this.snapOnOffCheckbox = new JCheckBox("Use Snap: ", snapState.isSnapEnabled());
		this.snapOnOffCheckbox.addActionListener(this);
		
		
		this.gridSizeModel = new SpinnerNumberModel(this.snapState.getGridSpacing(), .01d, 10d, .05d);
		this.gridSizeSpinner = new JSpinner(this.gridSizeModel);
		this.gridSizeSpinner.addChangeListener(this);
		JLabel gridSpacingLabel = new JLabel("Grid Spacing: ");
		
		this.setLayout(new GridBagLayout());
		
		this.add(this.snapOnOffCheckbox , new GridBagConstraints( 
				0, //gridX
				0, //gridY
				2, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,0,0,0), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.add(gridSpacingLabel , new GridBagConstraints( 
				0, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,0,0,0), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.add(this.gridSizeSpinner , new GridBagConstraints( 
				1, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,0,0,0), //insets
				0, //ipadX
				0 ) //ipadY
		);
	}
	
	protected void updateUIFromSnapState()
	{
		this.snapOnOffCheckbox.setSelected(this.snapState.isSnapEnabled());
		this.gridSizeModel.setValue(new Double(this.snapState.getGridSpacing()));
	}
	
	protected void updateSnapStateFromUI()
	{
		this.snapState.setGridSpacing(this.gridSizeModel.getNumber().doubleValue());
		this.snapState.setSnapEnabled(this.snapOnOffCheckbox.isSelected());
	}
	
	public void setSnapState(SnapState snapState)
	{
		this.snapState = snapState;
		updateUIFromSnapState();
	}

	public void stateChanged(ChangeEvent e) {
		updateSnapStateFromUI();
//		if (e.getSource() == this.gridSizeSpinner)
//		{
//			this.snapState.setGridSpacing(this.gridSizeModel.getNumber().doubleValue());
//		}
		
	}

	public void actionPerformed(ActionEvent e) {
		updateSnapStateFromUI();
//		if (e.getSource() == this.snapOnOffCheckbox)
//		{
//			this.snapState.setSnapEnabled(this.snapOnOffCheckbox.isSelected());
//		}
	}
	
}
