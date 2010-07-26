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
import java.awt.GridBagConstraints;
import java.awt.Insets;


import org.alice.apis.moveandturn.CameraMarker;
import org.alice.ide.swing.FieldListCellRenderer;
import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.croquet.GridBagPanel;
import edu.cmu.cs.dennisc.croquet.ScrollPane;

/**
 * @author David Culyba
 */
public class SceneViewManagerPanel extends GridBagPanel{
	
	private edu.cmu.cs.dennisc.croquet.List<FieldDeclaredInAlice> markerFieldList;
	private CreateCameraMarkerActionOperation createCameraMarkerAction = null;
	private org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor sceneEditor;
	
	public SceneViewManagerPanel(org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor sceneEditor)
	{
		super();
//		this.setOpaque( false );
		this.sceneEditor = sceneEditor;
		this.createCameraMarkerAction = new CreateCameraMarkerActionOperation(this.sceneEditor);	
		this.markerFieldList = this.sceneEditor.getSceneMarkerFieldList().createList();
		this.markerFieldList.setCellRenderer(new FieldListCellRenderer(){
			@Override
			protected javax.swing.JLabel getListCellRendererComponent(javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.alice.ast.AbstractField value, int index, boolean isSelected, boolean cellHasFocus) {
				if( value == null ) {
					rv.setText( org.alice.ide.IDE.getSingleton().getTextForNull() );
					
				} else {
					rv.setText( value.getName() );
					if (SceneViewManagerPanel.this.isFieldActive(value))
					{
						rv.setForeground(Color.GREEN);
					}
					else
					{
						rv.setForeground(Color.BLACK);
					}
					if (SceneViewManagerPanel.this.isFieldStartingField(value))
					{
						rv.setBackground(Color.YELLOW);
					}
					
				}
				return rv;
			}
		});
		
		ScrollPane markerScrollPane = new ScrollPane(this.markerFieldList, ScrollPane.VerticalScrollbarPolicy.AS_NEEDED, ScrollPane.HorizontalScrollbarPolicy.AS_NEEDED);
		markerScrollPane.setBorder(null);
		this.addComponent( new edu.cmu.cs.dennisc.croquet.Label( "Camera Markers:", 1.5f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD), new GridBagConstraints( 
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
		this.addComponent( createCameraMarkerAction.createButton(), new GridBagConstraints( 
				1, //gridX
				0, //gridY
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
		this.addComponent(markerScrollPane, new GridBagConstraints( 
				0, //gridX
				1, //gridY
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
	
	public void updateMarkerList()
	{
		this.markerFieldList.revalidateAndRepaint();
	}
	
	protected boolean isFieldStartingField(edu.cmu.cs.dennisc.alice.ast.AbstractField field)
	{
		return (this.sceneEditor.getStartingViewMarkerFieldList().getSelectedItem() == field); 
	}

	protected boolean isFieldActive(edu.cmu.cs.dennisc.alice.ast.AbstractField field)
	{
		CameraMarker activeMarker = this.sceneEditor.getCameraMarkerForField( (FieldDeclaredInAlice)field );
		return (activeMarker == this.sceneEditor.getMainCameraMarkerList().getSelectedItem() );
	}

}
