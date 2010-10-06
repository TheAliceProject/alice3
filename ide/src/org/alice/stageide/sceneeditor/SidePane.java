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
package org.alice.stageide.sceneeditor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JDialog;

import org.alice.stageide.sceneeditor.snap.SnapControlPanel;
import org.alice.stageide.sceneeditor.snap.SnapState;
import org.alice.stageide.sceneeditor.viewmanager.SceneViewManagerPanel;
import org.alice.ide.IDE;
import org.alice.ide.swing.BasicTreeNodeViewerPanel;
import org.alice.ide.swing.FieldListCellRenderer;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.croquet.ActionOperation;
import edu.cmu.cs.dennisc.croquet.ComboBox;
import edu.cmu.cs.dennisc.croquet.GridBagPanel;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.lookingglass.opengl.AdapterFactory;
import edu.cmu.cs.dennisc.lookingglass.opengl.SceneAdapter;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.croquet.BorderPanel;

/**
 * @author Dennis Cosgrove
 */
class SidePane extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private boolean isExpanded = false;
	private SceneViewManagerPanel viewManagerPanel = null;
	private SnapControlPanel snapControlPanel = null;
	private GridBagPanel mainPanel = null;
	
	private BasicTreeNodeViewerPanel sceneGraphViewer = null;
	private JDialog sceneGraphViewDialog = null;
	private ActionOperation showSceneGraphActionOperation; 
	private SceneObjectPropertyManager propertyManager;
	
	public void setSceneGraphRoot()
	{
		if (this.sceneGraphViewer != null)
		{
			Component root = ((MoveAndTurnSceneEditor)(IDE.getSingleton().getSceneEditor())).getScene().getSGComposite();
			SidePane.this.sceneGraphViewer.setRoot(root);
		}
	}
	
	private class ShowSceneGraphViewerActionOperation extends org.alice.ide.operations.InconsequentialActionOperation {
		public ShowSceneGraphViewerActionOperation() {
			super( java.util.UUID.fromString( "41f92d36-2b83-4268-8d0f-f8cd3502f12c" ) );
			this.setName( "Show SceneGraph" );
		}
		@Override
		protected void performInternal( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
			if (SystemUtilities.isPropertyTrue(IDE.DEBUG_PROPERTY_KEY))
			{
				if (SidePane.this.sceneGraphViewer == null)
				{
					SidePane.this.sceneGraphViewer = new BasicTreeNodeViewerPanel();
					SidePane.this.setSceneGraphRoot();
				}
				if (SidePane.this.sceneGraphViewDialog == null)
				{
					SidePane.this.sceneGraphViewDialog = new JDialog(edu.cmu.cs.dennisc.javax.swing.SwingUtilities.getRootJFrame(SidePane.this.getAwtComponent()), "Scene Graph Viewer", false);
					SidePane.this.sceneGraphViewDialog.getContentPane().add(SidePane.this.sceneGraphViewer);
					SidePane.this.sceneGraphViewDialog.setSize(new Dimension(1024, 768));
					SidePane.this.sceneGraphViewDialog.setLocation(100, 100);
				}
				SidePane.this.sceneGraphViewDialog.setVisible(true);
			}
		}
	}
	
	private BasicTreeNodeViewerPanel lookingglassViewer = null;
	private JDialog lookingglassViewDialog = null;
	private ActionOperation showLookingglassViewActionOperation; 
	
	public void setLookingglassRoot()
	{
		if (this.lookingglassViewer != null)
		{
			SceneAdapter root = (SceneAdapter)AdapterFactory.getAdapterFor( ((MoveAndTurnSceneEditor)(IDE.getSingleton().getSceneEditor())).getScene().getSGComposite() );
			SidePane.this.lookingglassViewer.setRoot(root);
		}
	}
	
	private class ShowLookingglassViewerActionOperation extends org.alice.ide.operations.InconsequentialActionOperation {
		public ShowLookingglassViewerActionOperation() {
			super( java.util.UUID.fromString( "7b3b1ae7-9e29-498b-8cfe-624b7d651460" ) );
			this.setName( "Show Lookingglass Tree" );
		}
		@Override
		protected void performInternal( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
			if (SystemUtilities.isPropertyTrue(IDE.DEBUG_PROPERTY_KEY))
			{
				if (SidePane.this.lookingglassViewer == null)
				{
					SidePane.this.lookingglassViewer = new BasicTreeNodeViewerPanel();
					SidePane.this.setLookingglassRoot();
				}
				if (SidePane.this.lookingglassViewDialog == null)
				{
					SidePane.this.lookingglassViewDialog = new JDialog(edu.cmu.cs.dennisc.javax.swing.SwingUtilities.getRootJFrame(SidePane.this.getAwtComponent()), "Lookingglass Tree Viewer", false);
					SidePane.this.lookingglassViewDialog.getContentPane().add(SidePane.this.lookingglassViewer);
					SidePane.this.lookingglassViewDialog.setSize(new Dimension(1024, 768));
					SidePane.this.lookingglassViewDialog.setLocation(100, 100);
				}
				SidePane.this.lookingglassViewDialog.setVisible(true);
			}
		}
	}
	
	public SidePane(MoveAndTurnSceneEditor sceneEditor) {
		this.mainPanel = new GridBagPanel();
		this.viewManagerPanel = new SceneViewManagerPanel(sceneEditor);
		this.snapControlPanel = new SnapControlPanel(sceneEditor.getSnapState(), sceneEditor);
		this.propertyManager = new SceneObjectPropertyManager();

		
		edu.cmu.cs.dennisc.croquet.BorderPanel handleControlPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
//		handleControlPanel.setOpaque( false );
		
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( HandleSet.class.getPackage().getName() + ".handle" );
		handleControlPanel.addComponent( new edu.cmu.cs.dennisc.croquet.Label( resourceBundle.getString("handleStyleTitle"), 1.5f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD), BorderPanel.Constraint.PAGE_START );
		handleControlPanel.addComponent(sceneEditor.getDragAdapter().getInteractionSelectionStateList().createDefaultRadioButtons(), BorderPanel.Constraint.CENTER);
		handleControlPanel.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );

		this.mainPanel.addComponent(this.propertyManager, new GridBagConstraints(
				0, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				1.0, // weightY
				GridBagConstraints.NORTHWEST, // anchor
				GridBagConstraints.NONE, // fill
				new Insets(2, 0, 2, 0), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		
		this.mainPanel.addComponent(this.viewManagerPanel, new GridBagConstraints(
				0, // gridX
				1, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				1.0, // weightY
				GridBagConstraints.NORTH, // anchor
				GridBagConstraints.BOTH, // fill
				new Insets(2, 0, 2, 0), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);

		this.mainPanel.addComponent(handleControlPanel, new GridBagConstraints(
				0, // gridX
				2, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.NORTH, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets(2, 0, 2, 0), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		this.mainPanel.addComponent(this.snapControlPanel, new GridBagConstraints(
				0, // gridX
				3, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.NORTH, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets(2, 0, 2, 0), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		
		this.showSceneGraphActionOperation = new ShowSceneGraphViewerActionOperation();
		this.showLookingglassViewActionOperation = new ShowLookingglassViewerActionOperation();
		
		if (SystemUtilities.isPropertyTrue(IDE.DEBUG_PROPERTY_KEY))
		{
			this.mainPanel.addComponent(this.showSceneGraphActionOperation.createButton(), new GridBagConstraints(
					0, // gridX
					4, // gridY
					1, // gridWidth
					1, // gridHeight
					1.0, // weightX
					0.0, // weightY
					GridBagConstraints.NORTH, // anchor
					GridBagConstraints.NONE, // fill
					new Insets(2, 0, 2, 0), // insets (top, left, bottom, right)
					0, // ipadX
					0) // ipadY
					);
			this.mainPanel.addComponent(this.showLookingglassViewActionOperation.createButton(), new GridBagConstraints(
					0, // gridX
					5, // gridY
					1, // gridWidth
					1, // gridHeight
					1.0, // weightX
					0.0, // weightY
					GridBagConstraints.NORTH, // anchor
					GridBagConstraints.NONE, // fill
					new Insets(2, 0, 2, 0), // insets (top, left, bottom, right)
					0, // ipadX
					0) // ipadY
					);
		}
		
		this.mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
//		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.addComponent( mainPanel, Constraint.CENTER);
		
		edu.cmu.cs.dennisc.croquet.LineAxisPanel undoRedoPanel = new edu.cmu.cs.dennisc.croquet.LineAxisPanel(
				org.alice.ide.croquet.models.history.UndoOperation.getInstance().createButton(), 
				org.alice.ide.croquet.models.history.RedoOperation.getInstance().createButton(),
				edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalGlue()
		);
		this.addComponent( undoRedoPanel, Constraint.PAGE_START );
	}
	
	public ActionOperation getShowSceneGraphViewerActionOperation()
	{
		return this.showSceneGraphActionOperation;
	}
	
	public boolean isExpanded() {
		return this.isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
		this.revalidateAndRepaint();
	}
	
	public SceneObjectPropertyManager getPropertyManager() {
		return this.propertyManager;
	}
	
	public SceneViewManagerPanel getViewManager() {
		return this.viewManagerPanel;
	}

	public void setSnapState(SnapState snapState)
	{
		this.snapControlPanel.setSnapState(snapState);
	}
	

}
