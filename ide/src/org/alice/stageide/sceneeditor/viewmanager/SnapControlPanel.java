package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.alice.interact.SnapState;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;

import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInDegrees;

public class SnapControlPanel extends JPanel implements ChangeListener, ActionListener
{
	private SnapState snapState;
	private MoveAndTurnSceneEditor sceneEditor; 
	
	private JCheckBox snapOnOffCheckBox;
	private JCheckBox snapToGroundCheckBox;
	private JCheckBox showSnapGridCheckBox;
	
	private JCheckBox snapToGridCheckBox;
	private JSpinner gridSizeSpinner;
	private JLabel gridSpacingLabel;
	private SpinnerNumberModel gridSizeModel;
	
	private JCheckBox rotationSnapCheckBox;
	private JSpinner snapAngleSpinner;
	private JLabel snapAngleLabel;
	private SpinnerListModel snapAngleModel;
	
	public SnapControlPanel(SnapState snapState, MoveAndTurnSceneEditor sceneEditor)
	{
		this.sceneEditor = sceneEditor;
		this.snapState = snapState;
		this.snapOnOffCheckBox = new JCheckBox("Use Snap: ", snapState.isSnapEnabled());
		this.snapOnOffCheckBox.addActionListener(this);
	
		this.snapToGroundCheckBox = new JCheckBox("Snap to ground", snapState.isSnapToGroundEnabled());
		this.snapToGroundCheckBox.addActionListener(this);
		
		this.snapToGridCheckBox = new JCheckBox("Snap to grid", snapState.isSnapToGridEnabled());
		this.snapToGridCheckBox.addActionListener(this);
		
		this.showSnapGridCheckBox =  new JCheckBox("Show Snap Grid", true);
		this.showSnapGridCheckBox.addActionListener(this);
		
		Dimension spinnerSize = new Dimension(50, 26);
		
		this.gridSizeModel = new SpinnerNumberModel(this.snapState.getGridSpacing(), .01d, 10d, .05d);
		this.gridSizeSpinner = new JSpinner(this.gridSizeModel);
		this.gridSizeSpinner.setPreferredSize(spinnerSize);
		this.gridSizeSpinner.setMinimumSize(spinnerSize);
		this.gridSizeSpinner.setMaximumSize(spinnerSize);
		this.gridSizeSpinner.addChangeListener(this);
		this.gridSizeModel.addChangeListener(this);
		this.gridSpacingLabel = new JLabel("Grid Spacing: ");
		this.gridSpacingLabel.setFont(this.gridSpacingLabel.getFont().deriveFont(Font.PLAIN, 12));
		
		this.rotationSnapCheckBox = new JCheckBox("Snap rotation", snapState.isRotationSnapEnabled());
		this.rotationSnapCheckBox.addActionListener(this);
		
		this.snapAngleModel = new SpinnerListModel(SnapState.ANGLE_SNAP_OPTIONS);
		this.snapAngleModel.setValue(SnapState.getAngleOptionForAngle((int)this.snapState.getRotationSnapAngle().getAsDegrees()));
		this.snapAngleSpinner = new JSpinner(this.snapAngleModel);
		this.snapAngleSpinner.addChangeListener(this);
		this.snapAngleSpinner.setPreferredSize(spinnerSize);
		this.snapAngleSpinner.setMinimumSize(spinnerSize);
		this.snapAngleSpinner.setMaximumSize(spinnerSize);
		this.snapAngleLabel = new JLabel("Angle Snap: ");
		this.snapAngleLabel.setFont(this.snapAngleLabel.getFont().deriveFont(Font.PLAIN, 12));
		
		JPanel snapToGridPanel = new JPanel();
		snapToGridPanel.setLayout(new BoxLayout(snapToGridPanel, BoxLayout.X_AXIS));
		snapToGridPanel.setBorder(null);
		snapToGridPanel.add(this.snapToGridCheckBox);
		snapToGridPanel.add(Box.createHorizontalStrut(10));
		snapToGridPanel.add(this.gridSpacingLabel);
		snapToGridPanel.add(this.gridSizeSpinner);
		
		JPanel rotationSnapPanel = new JPanel();
		rotationSnapPanel.setLayout(new BoxLayout(rotationSnapPanel, BoxLayout.X_AXIS));
		rotationSnapPanel.setBorder(null);
		rotationSnapPanel.add(this.rotationSnapCheckBox);
		rotationSnapPanel.add(Box.createHorizontalStrut(10));
		rotationSnapPanel.add(this.snapAngleLabel);
		rotationSnapPanel.add(this.snapAngleSpinner);
		
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
		this.add(this.showSnapGridCheckBox , new GridBagConstraints( 
				1, //gridX
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
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,10,0,0), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);
		this.add(rotationSnapPanel , new GridBagConstraints( 
				0, //gridX
				2, //gridY
				2, //gridWidth
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
				3, //gridY
				2, //gridWidth
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
		this.snapToGroundCheckBox.setSelected(this.snapState.isSnapToGroundEnabled());
		this.snapToGroundCheckBox.setEnabled(this.snapState.isSnapEnabled());
		
		this.gridSizeModel.setValue(new Double(this.snapState.getGridSpacing()));
		this.snapToGridCheckBox.setSelected(this.snapState.isSnapToGridEnabled());
		this.snapToGridCheckBox.setEnabled(this.snapState.isSnapEnabled());
		this.gridSizeSpinner.setEnabled(this.snapState.isSnapEnabled());
		this.gridSpacingLabel.setEnabled(this.snapState.isSnapEnabled());
		
		this.snapAngleModel.setValue(SnapState.getAngleOptionForAngle((int)this.snapState.getRotationSnapAngle().getAsDegrees()));
		this.rotationSnapCheckBox.setSelected(this.snapState.isRotationSnapEnabled());
		this.rotationSnapCheckBox.setEnabled(this.snapState.isSnapEnabled());
		this.snapAngleSpinner.setEnabled(this.snapState.isSnapEnabled());
		this.snapAngleLabel.setEnabled(this.snapState.isSnapEnabled());
	}
	
	protected void updateSnapStateFromUI()
	{
		this.snapState.setGridSpacing(this.gridSizeModel.getNumber().doubleValue());
		this.snapState.setSnapEnabled(this.snapOnOffCheckBox.isSelected());
		this.snapState.setShouldSnapToGridEnabled(this.snapToGridCheckBox.isSelected());
		this.snapState.setShouldSnapToGroundEnabled(this.snapToGroundCheckBox.isSelected());
		this.snapState.setRotationSnapEnabled(this.rotationSnapCheckBox.isSelected());
		this.snapState.setRotationSnapAngle(new AngleInDegrees(((Integer)this.snapAngleModel.getValue()).doubleValue()));
	}
	
	public void setSnapState(SnapState snapState)
	{
		this.snapState = snapState;
		updateUIFromSnapState();
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == this.gridSizeModel)
		{
			if (this.sceneEditor != null)
			{
				this.sceneEditor.setSnapGridSpacing(this.gridSizeModel.getNumber().doubleValue());
			}
		}
		updateSnapStateFromUI();
		updateUIFromSnapState();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.showSnapGridCheckBox)
		{
			if (this.sceneEditor != null)
			{
				this.sceneEditor.setShowSnapGrid(this.showSnapGridCheckBox.isSelected());
			}
		}
		updateSnapStateFromUI();
		updateUIFromSnapState();
	}
	
}
