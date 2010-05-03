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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.alice.ide.common.FieldDeclarationPane;

import edu.cmu.cs.dennisc.zoot.ZManager;

/**
 * @author David Culyba
 */
public class SceneViewManagerPanel extends JPanel{
	
	private boolean isActive = false;
	
	private JPanel cameraMarkerPanel;
	private JButton addCameraMarkerButton;
	private JPanel startingViewPanel;

	private CreateCameraMarkerActionOperation createCameraMarkerAction = null;
	
	private org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor sceneEditor;
	
	
	
	public SceneViewManagerPanel(org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor sceneEditor)
	{
		super();
		this.setOpaque( false );
		this.setLayout( new GridBagLayout() );	
		
		this.sceneEditor = sceneEditor;
		this.createCameraMarkerAction = new CreateCameraMarkerActionOperation(this.sceneEditor);	
		
		JLabel title = new JLabel( "Starting View = ");
		title.setFont( title.getFont().deriveFont( Font.BOLD, 18f ) );
		
		startingViewPanel = new JPanel();
		startingViewPanel.add( new JLabel("Not implemented yet."));
		
		JLabel cameraMarkerTitle = new JLabel( "Camera Markers:");
		cameraMarkerTitle.setFont( cameraMarkerTitle.getFont().deriveFont( Font.BOLD, 18f ) );
		
		addCameraMarkerButton = edu.cmu.cs.dennisc.croquet.Application.getSingleton().createButton(createCameraMarkerAction).getJComponent();
		
		cameraMarkerPanel = new JPanel();
		cameraMarkerPanel.setLayout( new GridBagLayout()  );
		
		this.add(title, new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.add(startingViewPanel, new GridBagConstraints( 
				1, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.add( cameraMarkerTitle, new GridBagConstraints( 
				0, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.add( addCameraMarkerButton, new GridBagConstraints( 
				1, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.add(cameraMarkerPanel, new GridBagConstraints( 
				0, //gridX
				2, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets(0,0,0,0), //insets
				0, //ipadX
				0 ) //ipadY
		);
	}
	
	private Component makeCameraMarkerComponent(final edu.cmu.cs.dennisc.alice.ast.AbstractField field)
	{
		JPanel componentPanel = new JPanel();
		componentPanel.add( new JLabel(field.getName()) );
		JButton deleteViewButton = edu.cmu.cs.dennisc.croquet.Application.getSingleton().createButton( new org.alice.ide.operations.ast.DeleteFieldOperation( (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice) field ) ).getJComponent();
		componentPanel.add( deleteViewButton );
		
		return componentPanel;
	}
	
	public void refreshFields()
	{
		if (this.sceneEditor != null)
		{
			this.cameraMarkerPanel.removeAll();
			int count = 0;
			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : this.sceneEditor.getDeclaredFields()) 
			{
				if (field.getValueType().isAssignableTo( org.alice.apis.moveandturn.CameraMarker.class ))
				{
					cameraMarkerPanel.add(makeCameraMarkerComponent(field), new GridBagConstraints( 
							0, //gridX
							count, //gridY
							1, //gridWidth
							1, //gridHeight
							1.0, //weightX
							0.0, //weightY
							GridBagConstraints.WEST, //anchor 
							GridBagConstraints.NONE, //fill
							new Insets(2,10,2,2), //insets (top, left, bottom, right)
							0, //ipadX
							0 ) //ipadY
					);
					count++;
				}
			}
			cameraMarkerPanel.add(Box.createVerticalGlue(), new GridBagConstraints( 
					0, //gridX
					count, //gridY
					1, //gridWidth
					1, //gridHeight
					1.0, //weightX
					1.0, //weightY
					GridBagConstraints.CENTER, //anchor 
					GridBagConstraints.BOTH, //fill
					new Insets(0,0,0,0), //insets (top, left, bottom, right)
					0, //ipadX
					0 ) //ipadY
			);
			this.cameraMarkerPanel.revalidate();
		}
	}
	
	public synchronized void setActive(boolean isActive)
	{
		if (isActive != this.isActive)
		{
			this.isActive = isActive;
		}
	}

}
