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
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.alice.apis.moveandturn.CameraMarker;
import org.alice.ide.common.FieldDeclarationPane;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;
import edu.cmu.cs.dennisc.alice.ast.AbstractField;
import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.ast.ThisExpression;
import edu.cmu.cs.dennisc.croquet.ListSelectionState;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.zoot.ZManager;

/**
 * @author David Culyba
 */
public class SceneViewManagerPanel extends JPanel{
	
	private boolean isActive = false;
	private edu.cmu.cs.dennisc.croquet.List<FieldDeclaredInAlice> markerFieldList;
//	private JPanel cameraMarkerPanel;
	private JButton addCameraMarkerButton;
	private CreateCameraMarkerActionOperation createCameraMarkerAction = null;
//	private Map<AbstractField,  JComponent> fieldComponentMap = new HashMap<AbstractField,  JComponent>();
	private org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor sceneEditor;
	private JScrollPane markerScrollPane = null;
	
	
	
	public SceneViewManagerPanel(org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor sceneEditor)
	{
		super();
		this.setOpaque( false );
		this.setLayout( new GridBagLayout() );	
		this.sceneEditor = sceneEditor;
		this.createCameraMarkerAction = new CreateCameraMarkerActionOperation(this.sceneEditor);	
		this.markerFieldList = this.sceneEditor.getSceneMarkerFieldList().createList();
		JLabel cameraMarkerTitle = new JLabel( "Camera Markers:");
		cameraMarkerTitle.setFont( cameraMarkerTitle.getFont().deriveFont( Font.BOLD, 18f ) );
		
		addCameraMarkerButton = createCameraMarkerAction.createButton().getAwtComponent();
		
//		cameraMarkerPanel = new JPanel();
//		cameraMarkerPanel.setLayout( new GridBagLayout()  );
		
		markerScrollPane = new JScrollPane(this.markerFieldList.getAwtComponent(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		markerScrollPane.setBorder(null);
		this.add( cameraMarkerTitle, new GridBagConstraints( 
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
		this.add( addCameraMarkerButton, new GridBagConstraints( 
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
		this.add(markerScrollPane, new GridBagConstraints( 
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
	
//	private void syncComponentsWithFields(List<AbstractField> cameraFields)
//	{
//		//Make new UI components as needed
//		for (AbstractField field : cameraFields)
//		{
//			if (!this.fieldComponentMap.containsKey(field))
//			{
//				JComponent viewComponent = makeCameraMarkerComponent(field);
//				this.fieldComponentMap.put(field, viewComponent);
//			}
//		}
//		
//		//Remove components no longer needed
//		List<AbstractField> toRemove = new LinkedList<AbstractField>();
//		for (Entry<AbstractField, JComponent> entry : this.fieldComponentMap.entrySet())
//		{
//			if (!cameraFields.contains(entry.getKey()))
//			{
//				toRemove.add(entry.getKey());
//			}
//		}
//		for (AbstractField field : toRemove)
//		{
//			this.fieldComponentMap.remove(field);
//		}
//	}
	
//	private JComponent makeCameraMarkerComponent(final edu.cmu.cs.dennisc.alice.ast.AbstractField field)
//	{
//		org.alice.ide.memberseditor.templates.GetterTemplate fieldComponent = new CameraViewFieldComponent(field);
//		fieldComponent.setActive(true);
//		return fieldComponent.getAwtComponent();
//	}
//	
//	public void scrollToField(AbstractField field)
//	{
//		if (this.fieldComponentMap.containsKey(field))
//		{
//			JComponent viewComponent = this.fieldComponentMap.get(field);
//			Rectangle viewRect = viewComponent.getBounds();
//			viewComponent.scrollRectToVisible( javax.swing.SwingUtilities.getLocalBounds( viewComponent ) );
////			this.markerScrollPane.scrollRectToVisible(viewRect);
//		}
//	}
//	
//	public void refreshFields()
//	{
//		if (this.sceneEditor != null)
//		{
//			this.cameraMarkerPanel.removeAll();
//			List<AbstractField> cameraFields = new LinkedList<AbstractField>();
//			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : this.sceneEditor.getDeclaredFields()) 
//			{
//				if (field.getValueType().isAssignableTo( org.alice.apis.moveandturn.CameraMarker.class ))
//				{
//					cameraFields.add(field);
//				}
//			}
//			syncComponentsWithFields(cameraFields);
//			int count = 0;
//			for (AbstractField cameraField : cameraFields)
//			{
//				assert this.fieldComponentMap.containsKey(cameraField);
//				Component c = this.fieldComponentMap.get(cameraField);
//				assert c != null;
//				cameraMarkerPanel.add(c, new GridBagConstraints( 
//						0, //gridX
//						count, //gridY
//						1, //gridWidth
//						1, //gridHeight
//						1.0, //weightX
//						0.0, //weightY
//						GridBagConstraints.WEST, //anchor 
//						GridBagConstraints.NONE, //fill
//						new Insets(2,10,2,2), //insets (top, left, bottom, right)
//						0, //ipadX
//						0 ) //ipadY
//				);
//				count++;
//			}
//			
//			cameraMarkerPanel.add(Box.createVerticalGlue(), new GridBagConstraints( 
//					0, //gridX
//					count, //gridY
//					1, //gridWidth
//					1, //gridHeight
//					1.0, //weightX
//					1.0, //weightY
//					GridBagConstraints.CENTER, //anchor 
//					GridBagConstraints.BOTH, //fill
//					new Insets(0,0,0,0), //insets (top, left, bottom, right)
//					0, //ipadX
//					0 ) //ipadY
//			);
//			this.cameraMarkerPanel.revalidate();
//		}
//	}
	
//	public synchronized void setActive(boolean isActive)
//	{
//		if (isActive != this.isActive)
//		{
//			this.isActive = isActive;
//		}
//	}

//	public void fieldAdded(final FieldDeclaredInAlice addedField) {
//		PrintUtilities.println("todo: implement a better fieldsAdded in "+this.getClass().getSimpleName());
//		refreshFields();
//		if (addedField.getDesiredValueType().isAssignableFrom(CameraMarker.class))
//		{
//			SwingUtilities.invokeLater(new Runnable()
//			{
//				public void run()
//				{
//					scrollToField(addedField);
//				}
//			});
//		}
//		
//	}
//
//	public void fieldRemoved(FieldDeclaredInAlice removedField) {
//		PrintUtilities.println("todo: implement a better fieldsRemoved in "+this.getClass().getSimpleName());
//		refreshFields();
//	}
//
//	public void fieldsSet(Collection<? extends FieldDeclaredInAlice> newFields) {
//		refreshFields();
//	}

}
