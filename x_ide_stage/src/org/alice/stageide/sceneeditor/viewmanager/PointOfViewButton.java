package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.Dimension;

import javax.swing.JButton;

public class PointOfViewButton extends JButton {

	private PointOfView pointOfView;
	
	public PointOfViewButton( PointOfView pointOfView )
	{
		super();
		this.pointOfView = pointOfView;
		this.setText("POV");
		this.setPreferredSize( new Dimension(60, 45) );
		this.setMinimumSize( new Dimension(60, 45) );
		this.setMaximumSize( new Dimension(60, 45) );
	}

	public PointOfView getPointOfView() {
		return pointOfView;
	}

	public void setPointOfView(PointOfView pointOfView) {
		this.pointOfView = pointOfView;
	}
}
