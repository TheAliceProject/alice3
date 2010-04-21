package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;

import edu.cmu.cs.dennisc.alice.ast.AbstractField;

public class StartingCameraViewManager extends JPanel {
	
	private CameraViewSelector viewSelector;
	
	public StartingCameraViewManager(MoveAndTurnSceneEditor sceneEditor)
	{
		super();
		this.setOpaque(false);
		this.viewSelector = new CameraViewSelector(sceneEditor);
		
		JLabel startingViewLabel = new JLabel("Starting View: ");
		startingViewLabel.setFont(startingViewLabel.getFont().deriveFont(Font.BOLD, 14));
		
		this.setLayout(new GridBagLayout());
		
		
		this.add(startingViewLabel, new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,0,0,0), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.add(this.viewSelector, new GridBagConstraints( 
				1, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,0,0,0), //insets
				0, //ipadX
				0 ) //ipadY
		);
		
	}
	
	public CameraFieldAndMarker getSelectedView()
	{
		return this.viewSelector.getSelectedMarker();
	}
	
	public void refreshFields()
	{
		this.viewSelector.refreshFields();
	}

}
