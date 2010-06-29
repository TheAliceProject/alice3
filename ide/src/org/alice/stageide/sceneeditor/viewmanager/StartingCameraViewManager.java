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
	private AbstractField desiredSelectedField;
	
	public StartingCameraViewManager(MoveAndTurnSceneEditor sceneEditor)
	{
		super();
		this.setOpaque(false);
		this.desiredSelectedField = null;
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
	
	public CameraViewSelector getCameraViewSelector()
	{
		return this.viewSelector;
	}
	
	public void setDesiredSelectedView(AbstractField desiredView)
	{
		this.desiredSelectedField = desiredView;
	}
	
	public void setSelectedView(AbstractField view)
	{
		this.viewSelector.setSelectedView(view);
	}
	
	public CameraFieldAndMarker getSelectedView()
	{
		return this.viewSelector.getSelectedMarker();
	}
	
	public void refreshFields()
	{
		this.viewSelector.refreshFields();
		if (this.desiredSelectedField != null)
		{
			int desiredIndex = this.viewSelector.getIndexForField(this.desiredSelectedField);
			if (desiredIndex != -1)
			{
				this.viewSelector.setSelectedIndex(desiredIndex);
				this.desiredSelectedField = null;
			}
		}
	}

}
