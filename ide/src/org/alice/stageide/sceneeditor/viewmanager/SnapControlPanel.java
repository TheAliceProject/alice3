package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
	
	protected JCheckBox snapOnOffCheckBox;
	protected JCheckBox snapToGroundCheckBox;
	protected JCheckBox snapToGridCheckBox;
	protected JSpinner gridSizeSpinner;
	protected JLabel gridSpacingLabel;
	protected SpinnerNumberModel gridSizeModel;
	
	public SnapControlPanel(SnapState snapState)
	{
		this.snapState = snapState;
		this.snapOnOffCheckBox = new JCheckBox("Use Snap: ", snapState.isSnapEnabled());
		this.snapOnOffCheckBox.addActionListener(this);
	
		this.snapToGroundCheckBox = new JCheckBox("Snap to ground", snapState.isSnapToGroundEnabled());
		this.snapToGroundCheckBox.addActionListener(this);
		
		this.snapToGridCheckBox = new JCheckBox("Snap to grid", snapState.isSnapToGridEnabled());
		this.snapToGridCheckBox.addActionListener(this);
		
		this.gridSizeModel = new SpinnerNumberModel(this.snapState.getGridSpacing(), .01d, 10d, .05d);
		this.gridSizeSpinner = new JSpinner(this.gridSizeModel);
		this.gridSizeSpinner.addChangeListener(this);
		this.gridSpacingLabel = new JLabel("Grid Spacing: ");
		this.gridSpacingLabel.setFont(this.gridSpacingLabel.getFont().deriveFont(Font.PLAIN, 12));
		
		JPanel snapToGridPanel = new JPanel();
		snapToGridPanel.setLayout(new BoxLayout(snapToGridPanel, BoxLayout.X_AXIS));
		snapToGridPanel.setBorder(null);
		snapToGridPanel.add(this.snapToGridCheckBox);
		snapToGridPanel.add(Box.createHorizontalStrut(10));
		snapToGridPanel.add(this.gridSpacingLabel);
		snapToGridPanel.add(this.gridSizeSpinner);
		
		this.setLayout(new GridBagLayout());
		
		this.add(this.snapOnOffCheckBox , new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,0,0,0), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);
		this.add(snapToGridPanel , new GridBagConstraints( 
				0, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,10,0,0), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);
		this.add(snapToGroundCheckBox , new GridBagConstraints( 
				0, //gridX
				2, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,10,0,0), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);
		
		updateUIFromSnapState();
	}
	
	protected void updateUIFromSnapState()
	{
		this.snapOnOffCheckBox.setSelected(this.snapState.isSnapEnabled());
		this.gridSizeModel.setValue(new Double(this.snapState.getGridSpacing()));
		this.snapToGridCheckBox.setSelected(this.snapState.isSnapToGridEnabled());
		this.snapToGridCheckBox.setEnabled(this.snapState.isSnapEnabled());
		this.snapToGroundCheckBox.setSelected(this.snapState.isSnapToGroundEnabled());
		this.snapToGroundCheckBox.setEnabled(this.snapState.isSnapEnabled());
		this.gridSizeSpinner.setEnabled(this.snapState.isSnapEnabled());
		this.gridSpacingLabel.setEnabled(this.snapState.isSnapEnabled());
	}
	
	protected void updateSnapStateFromUI()
	{
		this.snapState.setGridSpacing(this.gridSizeModel.getNumber().doubleValue());
		this.snapState.setSnapEnabled(this.snapOnOffCheckBox.isSelected());
		this.snapState.setShouldSnapToGridEnabled(this.snapToGridCheckBox.isSelected());
		this.snapState.setShouldSnapToGroundEnabled(this.snapToGroundCheckBox.isSelected());
	}
	
	public void setSnapState(SnapState snapState)
	{
		this.snapState = snapState;
		updateUIFromSnapState();
	}

	public void stateChanged(ChangeEvent e) {
		updateSnapStateFromUI();
		updateUIFromSnapState();
	}

	public void actionPerformed(ActionEvent e) {
		updateSnapStateFromUI();
		updateUIFromSnapState();
	}
	
}
