package org.alice.interact;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PointsOfViewPanel extends JPanel implements ActionListener{

	private PointOfViewManager pointOfViewManager;
	private JButton captureViewButton;
	private JPanel pointsOfViewPanel;
	
	public PointsOfViewPanel(PointOfViewManager pointOfViewManager)
	{
		super();
		this.pointOfViewManager = pointOfViewManager;
		this.captureViewButton = new JButton("Capture View");
		this.pointsOfViewPanel = new JPanel();
		this.pointsOfViewPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		this.setLayout(new BorderLayout());
		this.add(this.captureViewButton, BorderLayout.WEST);
		this.add(this.pointsOfViewPanel,BorderLayout.CENTER);
		
		this.captureViewButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.captureViewButton)
		{
			PointOfView pov = this.pointOfViewManager.capturePointOfView();
			PointOfViewButton povButton = new PointOfViewButton(pov);
			this.pointsOfViewPanel.add( povButton );
			this.pointsOfViewPanel.validate();
			povButton.addActionListener(this);
		}
		else if (e.getSource() instanceof PointOfViewButton)
		{
			PointOfViewButton povButton = (PointOfViewButton)e.getSource();
			this.pointOfViewManager.setPointOfView(povButton.getPointOfView());
		}
		
		
	}
	
}
