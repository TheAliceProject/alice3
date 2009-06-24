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
	private PointsOfViewListUI pointsOfViewList;
	
	public PointsOfViewPanel(PointOfViewManager pointOfViewManager)
	{
		super();
		this.pointOfViewManager = pointOfViewManager;
		this.captureViewButton = new JButton("Capture View");
		this.pointsOfViewList = new PointsOfViewListUI(this.pointOfViewManager);

		this.setLayout(new BorderLayout());
		this.add(this.captureViewButton, BorderLayout.WEST);
		this.add(this.pointsOfViewList,BorderLayout.CENTER);
		
		this.captureViewButton.addActionListener(this);
	}
	
	public void setPOVManager(PointOfViewManager pointOfViewManager)
	{
		this.pointOfViewManager = pointOfViewManager;
		this.pointsOfViewList.setListManager(this.pointOfViewManager);
		this.pointsOfViewList.revalidate();
		this.pointsOfViewList.repaint();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.captureViewButton)
		{
			this.pointOfViewManager.capturePointOfView();
		}
		else if (e.getSource() instanceof PointOfViewButton)
		{
			PointOfViewButton povButton = (PointOfViewButton)e.getSource();
			this.pointOfViewManager.setPointOfView(povButton.getPointOfView());
		}
		
		
	}
	
}
