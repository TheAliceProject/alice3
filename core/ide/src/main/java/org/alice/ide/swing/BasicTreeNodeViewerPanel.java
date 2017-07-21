/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.ide.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import edu.cmu.cs.dennisc.scenegraph.Component;

public class BasicTreeNodeViewerPanel extends JPanel implements ActionListener {

	protected JScrollPane scrollPane;
	protected JSplitPane splitPane;
	protected JButton captureButton;
	protected JCheckBox mirrorSelectionCheckBox;
	protected JPanel buttonPanel;
	protected Object root;

	private BasicTreeViewer leftTree = null;
	private BasicTreeViewer rightTree = null;

	public BasicTreeNodeViewerPanel()
	{
		this.setLayout( new BorderLayout() );
		this.buttonPanel = new JPanel();
		this.captureButton = new JButton( "Capture SceneGraph" );
		this.captureButton.addActionListener( this );
		this.mirrorSelectionCheckBox = new JCheckBox( "Mirror Selection" );
		this.mirrorSelectionCheckBox.setSelected( true );
		this.buttonPanel.add( this.captureButton );
		this.buttonPanel.add( this.mirrorSelectionCheckBox );
		this.add( this.buttonPanel, BorderLayout.SOUTH );
		this.splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		this.add( this.splitPane, BorderLayout.CENTER );
	}

	public void setRoot( Object sceneGraphRoot )
	{
		this.root = sceneGraphRoot;
		captureTree();
	}

	private void getFlattenedTree( BasicTreeNode node, List<BasicTreeNode> flattenedTree )
	{
		flattenedTree.add( node );
		for( int i = 0; i < node.getChildCount(); i++ )
		{
			getFlattenedTree( (BasicTreeNode)node.getChildAt( i ), flattenedTree );
		}
	}

	private void diffTrees( BasicTreeNode treeA, BasicTreeNode treeB )
	{
		LinkedList<BasicTreeNode> flatTreeA = new LinkedList<BasicTreeNode>();
		LinkedList<BasicTreeNode> flatTreeB = new LinkedList<BasicTreeNode>();
		getFlattenedTree( treeA, flatTreeA );
		getFlattenedTree( treeB, flatTreeB );
		for( BasicTreeNode nodeA : flatTreeA )
		{

			nodeA.markDifferent( BasicTreeNode.Difference.NONE );
		}
		for( BasicTreeNode nodeB : flatTreeB )
		{
			nodeB.markDifferent( BasicTreeNode.Difference.NONE );
		}
		for( BasicTreeNode nodeA : flatTreeA )
		{
			int matchingIndex = flatTreeB.indexOf( nodeA );
			if( matchingIndex != -1 )
			{
				BasicTreeNode nodeB = flatTreeB.get( matchingIndex );
				boolean isDifferent = nodeA.isDifferent( nodeB );
				if( isDifferent )
				{
					nodeB.markDifferent( BasicTreeNode.Difference.ATTRIBUTES );
				}
				flatTreeB.remove( matchingIndex );
			}
			else
			{
				nodeA.markDifferent( BasicTreeNode.Difference.NEW_NODE );
			}
		}
		for( BasicTreeNode nodeB : flatTreeB )
		{
			nodeB.markDifferent( BasicTreeNode.Difference.NEW_NODE );
		}
	}

	private void captureTree()
	{
		if( this.root != null )
		{
			BasicTreeNode newRoot = null;
			if( this.root instanceof Component )
			{
				newRoot = SceneGraphTreeNode.createSceneGraphTreeStructure( (Component)this.root );
			}
			//			else if (this.root instanceof CompositeAdapter<?>)
			//			{
			//				newRoot = LookingglassTreeNode.createLookingglassTreeStructure((CompositeAdapter<?>)this.root);
			//			}
			BasicTreeViewer oldTree = (BasicTreeViewer)this.splitPane.getBottomComponent();
			if( oldTree != null )
			{
				BasicTreeNode oldRoot = oldTree.getRootNode();
				diffTrees( oldRoot, newRoot );
				oldTree.setRootNode( oldRoot );

				this.splitPane.remove( oldTree );
				this.splitPane.setTopComponent( oldTree );
				this.leftTree = oldTree;
			}
			BasicTreeViewer newTreeViewer = new BasicTreeViewer( newRoot, this );
			this.rightTree = newTreeViewer;
			this.splitPane.setBottomComponent( newTreeViewer );
			if( this.splitPane.getTopComponent() != null )
			{
				this.splitPane.setDividerLocation( .5 );
			}
			this.invalidate();
		}
	}

	public boolean shouldMirrorSelection()
	{
		return this.mirrorSelectionCheckBox.isSelected();
	}

	public void setSelectionOnOtherTree( BasicTreeViewer sender, BasicTreeNode selectedNode )
	{
		if( sender == this.leftTree )
		{
			if( this.rightTree != null )
			{
				this.rightTree.setSelectedNode( selectedNode, false );
			}
		}
		else if( sender == this.rightTree )
		{
			if( this.leftTree != null )
			{
				this.leftTree.setSelectedNode( selectedNode, false );
			}
		}
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		if( e.getSource() == this.captureButton )
		{
			this.captureTree();
		}

	}

}
