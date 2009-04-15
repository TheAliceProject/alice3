package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
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
		this.pointsOfViewPanel.setLayout(new BoxLayout(this.pointsOfViewPanel, BoxLayout.Y_AXIS));
		
		this.setLayout(new BorderLayout());
		this.add(this.captureViewButton, BorderLayout.WEST);
		this.add(this.pointsOfViewPanel,BorderLayout.CENTER);
		
		this.captureViewButton.addActionListener(this);
	}
	
	public void setPOVManager(PointOfViewManager pointOfViewManager)
	{
		this.pointOfViewManager = pointOfViewManager;
		for (int i=0; i<this.pointsOfViewPanel.getComponentCount(); i++)
		{
			if (this.pointsOfViewPanel.getComponent( i ) instanceof JButton)
			{
				JButton povButton = (JButton)this.pointsOfViewPanel.getComponent( i );
				povButton.removeActionListener( this );
			}
		}
		this.pointsOfViewPanel.removeAll();
		for (int i=0; i<this.pointOfViewManager.getPointOfViewCount(); i++)
		{
			PointOfView pov = this.pointOfViewManager.getPointOfViewAt( i );
			PointOfViewButton povButton = new PointOfViewButton(pov);
			this.pointsOfViewPanel.add( povButton );
			this.pointsOfViewPanel.validate();
			povButton.addActionListener(this);
		}
		this.pointsOfViewPanel.revalidate();
		this.pointsOfViewPanel.repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.captureViewButton)
		{
			PointOfView pov = this.pointOfViewManager.capturePointOfView();
			PointOfViewButton povButton = new PointOfViewButton(pov);
			this.pointsOfViewPanel.add( povButton );
			this.pointsOfViewPanel.revalidate();
			this.pointsOfViewPanel.repaint();
			povButton.addActionListener(this);
		}
		else if (e.getSource() instanceof PointOfViewButton)
		{
			PointOfViewButton povButton = (PointOfViewButton)e.getSource();
			this.pointOfViewManager.setPointOfView(povButton.getPointOfView());
		}
		
		
	}
	
}
