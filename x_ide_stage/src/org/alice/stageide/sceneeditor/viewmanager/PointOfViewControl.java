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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;

import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

/**
 * @author David Culyba
 */
public class PointOfViewControl extends JPanel implements ActionListener, AbsoluteTransformationListener{
	private PointOfViewButton pointOfViewButton;
	private PointOfViewManager pointOfViewManager;
	private JButton deleteButton;
	private JButton setViewButton;
	private JButton recordButton;
	
	private ImageIcon stopIcon;
	private ImageIcon recordIcon;
	
	
	public PointOfViewControl(PointOfView pointOfView, PointOfViewManager pointsOfViewManager)
	{
		this.pointOfViewManager = pointsOfViewManager;
		this.pointOfViewButton = new PointOfViewButton(pointOfView);
		ImageIcon deleteIcon = new ImageIcon( this.getClass().getResource( "images/close.png" ));
		this.deleteButton = new JButton(deleteIcon);
		this.deleteButton.setOpaque( false );
		Dimension deleteButtonSize = new Dimension(deleteIcon.getIconWidth(), deleteIcon.getIconHeight());
		this.deleteButton.setPreferredSize( deleteButtonSize );
		this.deleteButton.setMinimumSize( deleteButtonSize );
		this.deleteButton.setMaximumSize( deleteButtonSize );
		ImageIcon cameraIcon = new ImageIcon( this.getClass().getResource( "images/camera.png" ));
		this.setViewButton = new JButton(cameraIcon);
		this.setViewButton.setOpaque( false );
		Dimension cameraButtonSize = new Dimension(cameraIcon.getIconWidth(), cameraIcon.getIconHeight());
		this.setViewButton.setPreferredSize( cameraButtonSize );
		this.setViewButton.setMinimumSize( cameraButtonSize );
		this.setViewButton.setMaximumSize( cameraButtonSize );
		
		this.recordIcon = new ImageIcon( this.getClass().getResource( "images/record.png" ));
		this.stopIcon = new ImageIcon( this.getClass().getResource( "images/stop.png" ));
		this.recordButton = new JButton(this.recordIcon);
		this.recordButton.setOpaque( false );
		Dimension recordButtonSize = new Dimension(this.recordIcon.getIconWidth(), this.recordIcon.getIconHeight());
		this.recordButton.setPreferredSize( recordButtonSize );
		this.recordButton.setMinimumSize( recordButtonSize );
		this.recordButton.setMaximumSize( recordButtonSize );
		
		
		this.setLayout( new GridBagLayout() );
		this.add(this.pointOfViewButton, 
				new GridBagConstraints( 0, //gridX
										0, //gridY
										1, //gridWidth
										3, //gridHeight
										1.0, //weightX
										1.0, //weightY
										GridBagConstraints.WEST, //anchor 
										GridBagConstraints.NONE, //fill
										new Insets(2,2,2,2), //insets
										0, //ipadX
										0 ) //ipadY
		);
		this.add(this.deleteButton, 
				new GridBagConstraints( 1, //gridX
										0, //gridY
										1, //gridWidth
										1, //gridHeight
										1.0, //weightX
										1.0, //weightY
										GridBagConstraints.NORTHEAST, //anchor 
										GridBagConstraints.NONE, //fill
										new Insets(2,2,2,2), //insets
										0, //ipadX
										0 ) //ipadY
		);
		this.add(this.recordButton, 
				new GridBagConstraints( 1, //gridX
										1, //gridY
										1, //gridWidth
										1, //gridHeight
										1.0, //weightX
										1.0, //weightY
										GridBagConstraints.EAST, //anchor 
										GridBagConstraints.NONE, //fill
										new Insets(2,2,2,2), //insets
										0, //ipadX
										0 ) //ipadY
		);
		this.add(this.setViewButton, 
				new GridBagConstraints( 1, //gridX
										2, //gridY
										1, //gridWidth
										1, //gridHeight
										1.0, //weightX
										1.0, //weightY
										GridBagConstraints.SOUTHEAST, //anchor 
										GridBagConstraints.NONE, //fill
										new Insets(2,2,2,2), //insets
										0, //ipadX
										0 ) //ipadY
		);
		this.setOpaque( false );
		this.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ));
		this.setPreferredSize( new Dimension(90, 60) );
		this.setMinimumSize( new Dimension(90, 60) );
		
		this.deleteButton.addActionListener( this );
		this.pointOfViewButton.addActionListener( this );
		this.setViewButton.addActionListener( this );
		this.recordButton.addActionListener( this );
		
	}
	
	public PointOfView getPointOfView()
	{
		return this.pointOfViewButton.getPointOfView();
	}
	
	public PointOfViewButton getPointOfViewButton()
	{
		return this.pointOfViewButton;
	}
	
	private void toggleRecording()
	{
		if (this.recordButton.isSelected())
		{
			this.recordButton.setSelected( false );
			this.recordButton.setIcon( this.recordIcon );
			this.pointOfViewManager.removeTransformationListener( this );
		}
		else
		{
			this.recordButton.setSelected( true );
			this.recordButton.setIcon( this.stopIcon );
			this.pointOfViewManager.setPointOfView( this.pointOfViewButton.getPointOfView() );
			this.pointOfViewManager.addTransformationListener( this );
		}
	}

	public void actionPerformed( ActionEvent e ) {
		if (e.getSource() == this.pointOfViewButton)
		{
			this.pointOfViewManager.setPointOfView( this.pointOfViewButton.getPointOfView() );
		}
		else if (e.getSource() == this.deleteButton)
		{
			this.pointOfViewManager.removePointOfView( this.pointOfViewButton.getPointOfView() );
		}
		else if (e.getSource() == this.setViewButton)
		{
			PointOfView pov = this.pointOfViewManager.getCurrentPointOfView();
			this.pointOfViewButton.setPointOfView( pov );
		}
		else if (e.getSource() == this.recordButton)
		{
			this.toggleRecording();
		}
		
	}

	public void absoluteTransformationChanged( AbsoluteTransformationEvent absoluteTransformationEvent ) {
		PointOfView pov = this.pointOfViewManager.getCurrentPointOfView();
		this.pointOfViewButton.setPointOfView( pov );
	}

}
