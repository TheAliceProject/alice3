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

import org.alice.ide.IDE;
import org.alice.ide.swing.BasicTreeNodeViewerPanel;
import org.alice.interact.handle.HandleSet;
import org.alice.stageide.croquet.models.sceneditor.MarkerPanelTab;
import org.alice.stageide.croquet.models.sceneditor.ObjectPropertiesTab;
import org.alice.stageide.croquet.models.sceneditor.PropertyAndMarkerPanelSelectionState;
import org.alice.stageide.sceneeditor.snap.SnapControlPanel;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.PredeterminedTab;
import org.lgna.croquet.TabSelectionState.TabCreator;
import org.lgna.croquet.components.BooleanStateButton;
import org.lgna.croquet.components.DefaultRadioButtons;
import org.lgna.croquet.components.GridBagPanel;
import org.lgna.croquet.components.JComponent;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.PushButton;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.croquet.components.ToolPaletteTabbedPane;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.lookingglass.opengl.AdapterFactory;
import edu.cmu.cs.dennisc.lookingglass.opengl.SceneAdapter;
import edu.cmu.cs.dennisc.scenegraph.Component;

/**
 * @author Dennis Cosgrove
 */
class SidePane extends org.lgna.croquet.components.GridBagPanel {
	private boolean isExpanded = false;
	private SnapControlPanel snapControlPanel = null;
	private GridBagPanel mainPanel = null;
	
	private BasicTreeNodeViewerPanel sceneGraphViewer = null;
	private JDialog sceneGraphViewDialog = null;
	private ActionOperation showSceneGraphActionOperation;
	
	public void setSceneGraphRoot()
	{
		if (this.sceneGraphViewer != null)
		{
			Component root = IDE.getActiveInstance().getSceneEditor().getActiveSceneImplementation().getSgComposite();
			SidePane.this.sceneGraphViewer.setRoot(root);
		}
	}
	
	private class ShowSceneGraphViewerActionOperation extends org.alice.ide.operations.InconsequentialActionOperation {
		public ShowSceneGraphViewerActionOperation() {
			super( java.util.UUID.fromString( "41f92d36-2b83-4268-8d0f-f8cd3502f12c" ) );
			this.setName( "Show SceneGraph" );
		}
		@Override
		protected void performInternal( org.lgna.croquet.history.ActionOperationStep step ) {
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
			SceneAdapter root = (SceneAdapter)AdapterFactory.getAdapterFor( IDE.getActiveInstance().getSceneEditor().getActiveSceneImplementation().getSgComposite() );
			SidePane.this.lookingglassViewer.setRoot(root);
		}
	}
	
	private class ShowLookingglassViewerActionOperation extends org.alice.ide.operations.InconsequentialActionOperation {
		public ShowLookingglassViewerActionOperation() {
			super( java.util.UUID.fromString( "7b3b1ae7-9e29-498b-8cfe-624b7d651460" ) );
			this.setName( "Show Lookingglass Tree" );
		}
		@Override
		protected void performInternal( org.lgna.croquet.history.ActionOperationStep step ) {
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
	
	public SidePane() {
	    
	    ToolPaletteTabbedPane<PredeterminedTab> tptp = PropertyAndMarkerPanelSelectionState.getInstance().createDefaultToolPaletteTabbedPane();
	    
		this.mainPanel = new GridBagPanel();
		this.snapControlPanel = new SnapControlPanel();
		
		//Set up the handle components
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( HandleSet.class.getPackage().getName() + ".handle" );
		Label handleLabel = new org.lgna.croquet.components.Label( resourceBundle.getString("handleStyleTitle"), 1.2f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD);
		DefaultRadioButtons<org.alice.stageide.sceneeditor.HandleStyle> handleRadioButtons = new DefaultRadioButtons<HandleStyle>(org.alice.stageide.croquet.models.sceneditor.HandleStyleListSelectionState.getInstance(), false){
            @Override 
            protected BooleanStateButton<?> createBooleanStateButton(
                    HandleStyle item, BooleanState booleanState)
            {
                if (item.getIcon() != null)
                {
                    booleanState.setIconForBothTrueAndFalse( item.getIcon() );
                }
                if (item.getToolTipText() != null)
                {
                    booleanState.setToolTipText(item.getToolTipText());
                }
                PushButton b = booleanState.createPushButton();
                b.setSelectedColor(org.alice.ide.IDE.getActiveInstance().getTheme().getSelectedColor());
                b.setBackgroundColor(org.alice.ide.IDE.getActiveInstance().getTheme().getPrimaryBackgroundColor());
                return b;
            }
        };
        //Layout the handle panel
		org.lgna.croquet.components.GridBagPanel handleControlPanel = new org.lgna.croquet.components.GridBagPanel();
		handleControlPanel.addComponent(handleLabel, new GridBagConstraints(
                0, // gridX
                0, // gridY
                1, // gridWidth
                1, // gridHeight
                0.25, // weightX
                0.0, // weightY
                GridBagConstraints.WEST, // anchor
                GridBagConstraints.NONE, // fill
                new Insets(0, 4, 0, 0), // insets (top, left, bottom, right)
                0, // ipadX
                0) // ipadY
                );
		handleControlPanel.addComponent( handleRadioButtons, new GridBagConstraints(
                1, // gridX
                0, // gridY
                1, // gridWidth
                1, // gridHeight
                .75, // weightX
                0.0, // weightY
                GridBagConstraints.WEST, // anchor
                GridBagConstraints.NONE, // fill
                new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
                0, // ipadX
                0) // ipadY
                );
		//This is to create a line separator between the undo/redo panel and the handle controls
		//edu.cmu.cs.dennisc.croquet.HorizontalSeparator() doesn't provide good color control
		handleControlPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, org.alice.ide.IDE.getActiveInstance().getTheme().getSecondaryBackgroundColor()), 
		        BorderFactory.createEmptyBorder(4, 0, 0, 0)));
		//Construct the undo/redo panel
		org.lgna.croquet.components.LineAxisPanel undoRedoPanel = new org.lgna.croquet.components.LineAxisPanel(
                org.alice.ide.croquet.models.history.UndoOperation.getInstance().createButton(), 
                org.alice.ide.croquet.models.history.RedoOperation.getInstance().createButton(),
                org.lgna.croquet.components.BoxUtilities.createHorizontalGlue()
        );
		//Layout out the header panel with the undo/redo buttons, the handle controls, and the snap controls
        GridBagPanel headerPanel = new GridBagPanel();
        int headerIndex = 0;
        headerPanel.addComponent(undoRedoPanel, new GridBagConstraints(
                0, // gridX
                headerIndex++, // gridY
                1, // gridWidth
                1, // gridHeight
                1.0, // weightX
                0.0, // weightY
                GridBagConstraints.CENTER, // anchor
                GridBagConstraints.NONE, // fill
                new Insets(2, 0, 2, 0), // insets (top, left, bottom, right)
                0, // ipadX
                0) // ipadY
                );
        headerPanel.addComponent(handleControlPanel, new GridBagConstraints(
                0, // gridX
                headerIndex++, // gridY
                1, // gridWidth
                1, // gridHeight
                1.0, // weightX
                0.0, // weightY
                GridBagConstraints.NORTHWEST, // anchor
                GridBagConstraints.HORIZONTAL, // fill
                new Insets(2, 0, 2, 0), // insets (top, left, bottom, right)
                0, // ipadX
                0) // ipadY
                );
        headerPanel.addComponent(this.snapControlPanel, new GridBagConstraints(
                0, // gridX
                headerIndex++, // gridY
                1, // gridWidth
                1, // gridHeight
                1.0, // weightX
                0.0, // weightY
                GridBagConstraints.NORTHWEST, // anchor
                GridBagConstraints.HORIZONTAL, // fill
                new Insets(2, 0, 2, 0), // insets (top, left, bottom, right)
                0, // ipadX
                0) // ipadY
                );
		//Layout the main body panel		
		int mainPanelIndex = 0;
		this.showSceneGraphActionOperation = new ShowSceneGraphViewerActionOperation();
		this.showLookingglassViewActionOperation = new ShowLookingglassViewerActionOperation();
		//Add debug components
		if (SystemUtilities.isPropertyTrue(IDE.DEBUG_PROPERTY_KEY))
		{
			this.mainPanel.addComponent(this.showSceneGraphActionOperation.createButton(), new GridBagConstraints(
					0, // gridX
					mainPanelIndex++, // gridY
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
					mainPanelIndex++, // gridY
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
		
		//Put the header and the main body in this component
		this.addComponent( headerPanel, new GridBagConstraints(
                0, // gridX
                0, // gridY
                1, // gridWidth
                1, // gridHeight
                1.0, // weightX
                0.0, // weightY
                GridBagConstraints.NORTHWEST, // anchor
                GridBagConstraints.BOTH, // fill
                new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
                0, // ipadX
                0) // ipadY
                );
		
		TabCreator< PredeterminedTab > tabCreator = new TabCreator< PredeterminedTab >() {
	        public final java.util.UUID getId(PredeterminedTab item) {
	            java.util.UUID rv = item.getId();
	            assert rv != null;
	            return rv;
	        }
	        public final JComponent<?> createMainComponent(PredeterminedTab item) {
	            return item.getMainComponent();
	        }
	        public void customizeTitleComponent( org.lgna.croquet.BooleanState booleanState, org.lgna.croquet.components.AbstractButton< ?, org.lgna.croquet.BooleanState > button, org.lgna.croquet.PredeterminedTab item ) {
	            item.customizeTitleComponent( booleanState, button );
	            button.scaleFont(1.2f);
	            button.setFont(edu.cmu.cs.dennisc.java.awt.FontUtilities.deriveFont( button.getAwtComponent(), edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD ) );
//	            button.getAwtComponent().revalidate();
	        }
	        public void releaseTitleComponent( org.lgna.croquet.BooleanState booleanState, org.lgna.croquet.components.AbstractButton< ?, org.lgna.croquet.BooleanState > button, org.lgna.croquet.PredeterminedTab item ) {
	        	item.releaseTitleComponent( booleanState, button );
	        }
	        public final ScrollPane createScrollPane( PredeterminedTab item ) {
	            return item.createScrollPane();
	        }
	        public final boolean isCloseable(org.lgna.croquet.PredeterminedTab item) {
	            return false;
	        }
	    };
		ToolPaletteTabbedPane<PredeterminedTab> propertyMarkerToolPalette =  new ToolPaletteTabbedPane< PredeterminedTab >( PropertyAndMarkerPanelSelectionState.getInstance(), tabCreator );
		
//		ScrollPane mainScrollPane = new ScrollPane(this.mainPanel, ScrollPane.VerticalScrollbarPolicy.ALWAYS, ScrollPane.HorizontalScrollbarPolicy.NEVER);
//        mainScrollPane.setBorder(null);
		this.addComponent( propertyMarkerToolPalette, new GridBagConstraints(
		        0, // gridX
                1, // gridY
                1, // gridWidth
                1, // gridHeight
                1.0, // weightX
                1.0, // weightY
                GridBagConstraints.NORTHWEST, // anchor
                GridBagConstraints.BOTH, // fill
                new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
                0, // ipadX
                0) // ipadY
                );
		
		//Set up colors
		this.setBackgroundColor(org.alice.ide.IDE.getActiveInstance().getTheme().getPrimaryBackgroundColor());
		this.mainPanel.setBackgroundColor(org.alice.ide.IDE.getActiveInstance().getTheme().getSecondaryBackgroundColor());
        this.snapControlPanel.setBackgroundColor(org.alice.ide.IDE.getActiveInstance().getTheme().getPrimaryBackgroundColor());
        
        ObjectPropertiesTab.getInstance().getMainComponent().setBackgroundColor(org.alice.ide.IDE.getActiveInstance().getTheme().getSecondaryBackgroundColor());
        MarkerPanelTab.getInstance().getMainComponent().setBackgroundColor(org.alice.ide.IDE.getActiveInstance().getTheme().getSecondaryBackgroundColor());
        MarkerPanelTab.getInstance().getMainComponent().setSelectedItemColor(org.alice.ide.IDE.getActiveInstance().getTheme().getSelectedColor());
        
        headerPanel.setBackgroundColor(org.alice.ide.IDE.getActiveInstance().getTheme().getPrimaryBackgroundColor());
	        
		Dimension minSize = new Dimension(this.getAwtComponent().getMinimumSize());
		minSize.width = 300;
		this.getAwtComponent().setMinimumSize(minSize);
		
//		this.propertyManager.setBackgroundColor(Color.GREEN);
//        this.mainPanel.setBackgroundColor(Color.BLUE);
//        this.setBackgroundColor(Color.YELLOW);
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
	

}
