package org.alice.interact;

import javax.swing.JButton;

public class PointOfViewButton extends JButton {

	private PointOfView pointOfView;
	
	public PointOfViewButton( PointOfView pointOfView )
	{
		super();
		this.pointOfView = pointOfView;
		this.setText("Go to me!");
	}

	public PointOfView getPointOfView() {
		return pointOfView;
	}

	public void setPointOfView(PointOfView pointOfView) {
		this.pointOfView = pointOfView;
	}
}
