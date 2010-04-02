/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass;

public class PointsOfViewPanel extends JPanel implements ActionListener{

	private PointOfViewManager pointOfViewManager;
	private JButton captureViewButton;
	private JLabel titleLabel; 
	private PointsOfViewListUI pointsOfViewList;
	
	public PointsOfViewPanel(PointOfViewManager pointOfViewManager)
	{
		super();
		this.pointOfViewManager = pointOfViewManager;
		this.captureViewButton = new JButton("Capture View");
		this.titleLabel = new JLabel("Other Views");
		this.titleLabel.setFont(this.titleLabel.getFont().deriveFont( 12f ));
		this.pointsOfViewList = new PointsOfViewListUI(this.pointOfViewManager);		

		this.setLayout(new GridBagLayout());
		this.add(this.titleLabel, new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.add(this.captureViewButton, new GridBagConstraints( 
				1, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.add(this.pointsOfViewList, new GridBagConstraints( 
				0, //gridX
				1, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.captureViewButton.addActionListener(this);
		
	}
	
	public void updateViewThumbnails(final OffscreenLookingGlass offscreenLookingGlass)
	{
		PointsOfViewPanel.this.pointsOfViewList.updatePointOfViewImages(offscreenLookingGlass);
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
