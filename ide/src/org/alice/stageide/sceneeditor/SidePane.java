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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.alice.stageide.sceneeditor.viewmanager.ManipulationHandleControlPanel;
import org.alice.stageide.sceneeditor.viewmanager.SceneViewManagerPanel;
import org.alice.stageide.sceneeditor.viewmanager.SnapControlPanel;
import org.alice.ide.IDE;
import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.SnapState;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.toolkit.scenegraph.SceneGraphViewerPanel;

/**
 * @author Dennis Cosgrove
 */
class SidePane extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private boolean isExpanded = false;
	private ManipulationHandleControlPanel handleControlPanel;
	private SceneViewManagerPanel viewManagerPanel = null;
	private SnapControlPanel snapControlPanel = null;
	private MoveAndTurnSceneEditor sceneEditor = null;
	private JPanel mainPanel = null;
	
	private SceneGraphViewerPanel sceneGraphViewer = null;
//	private JSplitPane sceneGraphViewSplitPane = null;
	private JDialog sceneGraphViewDialog = null;
	private JButton showSceneGraphButton = null;
	
	public SidePane(MoveAndTurnSceneEditor sceneEditor) {
		this.sceneEditor = sceneEditor;
		this.mainPanel = new JPanel();
		this.handleControlPanel = new ManipulationHandleControlPanel();
		this.viewManagerPanel = new SceneViewManagerPanel(sceneEditor);
		this.snapControlPanel = new SnapControlPanel(sceneEditor.getSnapState(), sceneEditor);

		this.showSceneGraphButton = new JButton("Show Scene Graph");
		this.showSceneGraphButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e) {
				showSceneGraphView();
			}
			
		}
		);
		
		JPanel startingCameraViewManager = new JPanel();
		startingCameraViewManager.setOpaque(false);
		JLabel startingViewLabel = new JLabel("Starting View: ");
		startingViewLabel.setFont(startingViewLabel.getFont().deriveFont(Font.BOLD, 14));
		startingCameraViewManager.setLayout(new GridBagLayout());
		startingCameraViewManager.add(startingViewLabel, new GridBagConstraints( 
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
		startingCameraViewManager.add(sceneEditor.getStartingViewMarkerFieldList().createComboBox().getAwtComponent(), new GridBagConstraints( 
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
		
		this.mainPanel.setLayout(new GridBagLayout());

		this.mainPanel.add(startingCameraViewManager, new GridBagConstraints(
				0, // gridX
				0, // gridY
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
		
		this.mainPanel.add(this.viewManagerPanel, new GridBagConstraints(
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

		this.mainPanel.add(this.handleControlPanel, new GridBagConstraints(
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
		this.mainPanel.add(this.snapControlPanel, new GridBagConstraints(
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
		
		if (SystemUtilities.isPropertyTrue(IDE.DEBUG_PROPERTY_KEY))
		{
			this.mainPanel.add(showSceneGraphButton, new GridBagConstraints(
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
		}
		
		this.mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
//		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( mainPanel ), Constraint.CENTER);
	}

	public boolean isExpanded() {
		return this.isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
		this.revalidateAndRepaint();
	}

	public void showSceneGraphView()
	{
		if (SystemUtilities.isPropertyTrue(IDE.DEBUG_PROPERTY_KEY))
		{
			if (this.sceneGraphViewDialog == null)
			{
				showSceneGraphView(this.sceneEditor.getScene().getSGComposite());
			}
			else
			{
				this.sceneGraphViewDialog.setVisible(true);
			}
		}
	}
	
	public void showSceneGraphView( Component root )
	{
		if (SystemUtilities.isPropertyTrue(IDE.DEBUG_PROPERTY_KEY))
		{
			if (this.sceneGraphViewer == null)
			{
				this.sceneGraphViewer = new SceneGraphViewerPanel();
			}
			this.sceneGraphViewer.setRoot(root);
			if (this.sceneGraphViewDialog == null)
			{
	//			this.sceneGraphViewDialog = new JDialog();
				this.sceneGraphViewDialog = new JDialog(edu.cmu.cs.dennisc.javax.swing.SwingUtilities.getRootJFrame(this.getAwtComponent()), "Scene Graph Viewer", false);
				this.sceneGraphViewDialog.getContentPane().add(this.sceneGraphViewer);
				this.sceneGraphViewDialog.setSize(new Dimension(1024, 768));
	//			this.sceneGraphViewDialog.setMinimumSize(new Dimension(800, 600));
				this.sceneGraphViewDialog.setLocation(100, 100);
			}
			this.sceneGraphViewDialog.setVisible(true);
		}
	}
	
	public void setDragAdapter(AbstractDragAdapter dragAdapter) {
		this.handleControlPanel.setDragAdapter(dragAdapter);
	}

	public SceneViewManagerPanel getViewManager() {
		return this.viewManagerPanel;
	}

	public void setSnapState(SnapState snapState)
	{
		this.snapControlPanel.setSnapState(snapState);
	}
	

}
