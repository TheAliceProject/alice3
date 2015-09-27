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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class BasicTreeViewer extends JPanel implements TreeSelectionListener
{
	protected JPanel mainPanel;
	protected JPanel infoPanel;
	protected DefaultTreeModel treeModel;
	protected JTree tree;

	protected JLabel nameLabel;
	protected JLabel classLabel;

	protected JPanel extrasPanel;

	protected JLabel colorLabel;

	protected Date timeStamp;
	protected JLabel timeStampLabel;

	protected boolean listenToSelection = true;
	protected BasicTreeNodeViewerPanel parentPanel = null;

	protected JSplitPane splitPane;

	protected JLabel transformLabel;
	protected JTextArea stackTracePanel;
	protected JScrollPane stackTraceScrollPane;

	protected JPanel virtualParentPanel;
	protected JLabel virtualParentNameLabel;
	protected JButton goToVirtualParentButton;
	protected int virtualParentHashCode = -1;

	protected JPanel parentInfoPanel;
	protected JButton goToParentButton;
	protected JLabel parentNameLabel;
	protected int parentHashCode = -1;

	protected JLabel opacityLabel;
	protected JLabel isShowingLabel;
	protected JLabel scaleLabel;

	public BasicTreeViewer( BasicTreeNode treeRoot )
	{
		this( treeRoot, null );
	}

	public BasicTreeViewer( BasicTreeNode treeRoot, BasicTreeNodeViewerPanel parentPanel )
	{
		super();
		this.parentPanel = parentPanel;
		this.timeStamp = new Date( System.currentTimeMillis() );
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout( new BorderLayout() );
		this.treeModel = new DefaultTreeModel( treeRoot );
		this.tree = new JTree( treeModel );
		this.tree.setCellRenderer( new BasicTreeNodeRenderer() );
		this.infoPanel = new JPanel();
		this.infoPanel.setLayout( new GridBagLayout() );

		this.nameLabel = new JLabel( "" );
		this.nameLabel.setBorder( createTitledBorder( "Name" ) );
		this.classLabel = new JLabel( "" );
		this.classLabel.setBorder( createTitledBorder( "Class" ) );
		this.transformLabel = new JLabel( "" );
		this.transformLabel.setBorder( createTitledBorder( "Absolute Transform" ) );

		this.parentNameLabel = new JLabel( "" );
		this.parentInfoPanel = new JPanel();
		this.parentInfoPanel.setLayout( new BoxLayout( this.parentInfoPanel, BoxLayout.X_AXIS ) );
		this.parentInfoPanel.setBorder( createTitledBorder( "Parent" ) );
		this.goToParentButton = new JButton( "Go to parent" );
		this.goToParentButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				if( parentHashCode != -1 )
				{
					setSelectedNode( parentHashCode, true );
				}

			}
		} );

		this.parentInfoPanel.add( this.parentNameLabel );
		this.parentInfoPanel.add( this.goToParentButton );

		this.infoPanel.add( this.nameLabel, new GridBagConstraints(
				0, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.NORTHEAST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		this.infoPanel.add( this.classLabel, new GridBagConstraints(
				0, // gridX
				1, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.NORTHEAST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		this.infoPanel.add( this.transformLabel, new GridBagConstraints(
				0, // gridX
				2, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.NORTHEAST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);

		this.stackTracePanel = new JTextArea();
		this.stackTracePanel.setEditable( false );
		this.stackTracePanel.setLayout( new BoxLayout( this.stackTracePanel, BoxLayout.Y_AXIS ) );
		this.stackTraceScrollPane = new JScrollPane( this.stackTracePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		this.stackTraceScrollPane.setMinimumSize( new Dimension( 50, 80 ) );
		this.stackTraceScrollPane.setPreferredSize( new Dimension( 200, 80 ) );

		this.opacityLabel = new JLabel( "" );
		this.opacityLabel.setBorder( createTitledBorder( "Opacity" ) );
		this.isShowingLabel = new JLabel( "" );
		this.isShowingLabel.setBorder( createTitledBorder( "Showing" ) );
		this.scaleLabel = new JLabel( "" );
		this.scaleLabel.setBorder( createTitledBorder( "Scale" ) );
		this.colorLabel = new JLabel( "" );
		this.colorLabel.setBorder( createTitledBorder( "Color" ) );

		this.extrasPanel = new JPanel();
		this.extrasPanel.setLayout( new GridBagLayout() );

		this.virtualParentPanel = new JPanel();
		this.virtualParentPanel.setLayout( new BoxLayout( this.virtualParentPanel, BoxLayout.X_AXIS ) );
		this.virtualParentPanel.setBorder( createTitledBorder( "Virtual Parent" ) );
		this.virtualParentNameLabel = new JLabel( "NO VIRTUAL PARENT" );
		this.goToVirtualParentButton = new JButton( "Go to parent" );
		this.goToVirtualParentButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				if( virtualParentHashCode != -1 )
				{
					setSelectedNode( virtualParentHashCode, true );
				}

			}
		} );

		this.virtualParentPanel.add( this.virtualParentNameLabel );
		this.virtualParentPanel.add( this.goToVirtualParentButton );

		this.splitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
		this.splitPane.setTopComponent( this.mainPanel );
		this.splitPane.setBottomComponent( this.stackTraceScrollPane );
		this.splitPane.setDividerLocation( .1 );

		this.add( splitPane, BorderLayout.CENTER );

		this.timeStampLabel = new JLabel( this.timeStamp.toString() );

		this.mainPanel.add( this.timeStampLabel, BorderLayout.NORTH );
		this.mainPanel.add( new JScrollPane( this.tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ), BorderLayout.CENTER );
		this.mainPanel.add( this.infoPanel, BorderLayout.SOUTH );
		this.mainPanel.setPreferredSize( new Dimension( 100, 600 ) );

		this.setLayout( new BorderLayout() );
		this.add( this.mainPanel, BorderLayout.CENTER );

		this.setData( treeRoot );

		this.tree.addTreeSelectionListener( this );

	}

	protected void setData( BasicTreeNode node )
	{
		if( node.name == null )
		{
			this.nameLabel.setText( "<NO NAME>" );
		}
		else
		{
			this.nameLabel.setText( "'" + node.name + "'" );
		}
		this.classLabel.setText( node.className );
		this.extrasPanel.removeAll();
		this.removeAll();
		if( node.hasExtras )
		{
			this.infoPanel.add( this.extrasPanel, new GridBagConstraints(
					1, // gridX
					0, // gridY
					1, // gridWidth
					this.infoPanel.getComponentCount(), // gridHeight
					1.0, // weightX
					1.0, // weightY
					GridBagConstraints.NORTHEAST, // anchor
					GridBagConstraints.BOTH, // fill
					new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
					0, // ipadX
					0 ) // ipadY
			);
			String colorString = "NO COLOR";
			if( node.color != null )
			{
				colorString = String.format( "%.2f, %.2f, %.2f, %.2f", node.color.red, node.color.green, node.color.blue, node.color.alpha );
				Color backgroundColor = new Color( (int)( node.color.red * 255 ), (int)( node.color.green * 255 ), (int)( node.color.blue * 255 ) );
				this.colorLabel.setBackground( backgroundColor );
				this.colorLabel.setOpaque( true );
			}
			else
			{
				this.colorLabel.setOpaque( false );
			}
			this.colorLabel.setText( colorString );
			this.colorLabel.revalidate();
		}
		else if( this.extrasPanel.getParent() != null )
		{
			this.infoPanel.remove( this.extrasPanel );
		}

		this.extrasPanel.add( this.colorLabel, new GridBagConstraints(
				0, // gridX
				1, // gridY
				2, // gridWidth
				1, // gridHeight
				1.0, // weightX
				1.0, // weightY
				GridBagConstraints.NORTHEAST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		this.virtualParentHashCode = -1;
		if( node instanceof SceneGraphTreeNode )
		{
			SceneGraphTreeNode sgNode = (SceneGraphTreeNode)node;
			String positionString = "NO POSITION";
			if( sgNode.absoluteTransform != null )
			{
				positionString = String.format( "[%.3f, %.3f, %.3f]", sgNode.absoluteTransform.translation.x, sgNode.absoluteTransform.translation.y, sgNode.absoluteTransform.translation.z ).toString();
			}
			this.transformLabel.setText( positionString );
			if( sgNode.stackTrace != null )
			{
				StringBuilder sb = new StringBuilder();
				for( StackTraceElement stack : sgNode.stackTrace )
				{
					sb.append( stack.toString() + "\n" );
				}
				this.stackTracePanel.setText( sb.toString() );
				JScrollBar verticalScrollBar = this.stackTraceScrollPane.getVerticalScrollBar();
				if( verticalScrollBar != null )
				{
					verticalScrollBar.setValue( verticalScrollBar.getMinimum() );
				}
			}
			this.parentNameLabel.setText( sgNode.parentName );
			this.parentHashCode = sgNode.parentHash;
			this.goToParentButton.setEnabled( ( this.parentHashCode != -1 ) );
			this.virtualParentHashCode = sgNode.virtualParentHashCode;
			if( sgNode.virtualParentHashCode != -1 )
			{
				this.virtualParentNameLabel.setText( sgNode.virtualParentName );
				this.infoPanel.add( this.virtualParentPanel, new GridBagConstraints(
						0, // gridX
						3, // gridY
						1, // gridWidth
						1, // gridHeight
						1.0, // weightX
						0.0, // weightY
						GridBagConstraints.NORTHEAST, // anchor
						GridBagConstraints.HORIZONTAL, // fill
						new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
						0, // ipadX
						0 ) // ipadY
				);
			}
			else if( this.virtualParentPanel.getParent() != null )
			{
				this.infoPanel.remove( this.virtualParentPanel );
			}

			if( sgNode.hasExtras )
			{
				if( sgNode.isShowing )
				{
					this.isShowingLabel.setText( "SHOWING" );
				}
				else
				{
					this.isShowingLabel.setText( "NOT SHOWING" );
				}
				String opacityString = String.format( "%.2f", sgNode.opacity );
				this.opacityLabel.setText( opacityString );
			}

			this.splitPane.setTopComponent( this.mainPanel );
			this.splitPane.setBottomComponent( this.stackTraceScrollPane );
			this.add( splitPane, BorderLayout.CENTER );
			this.extrasPanel.add( this.isShowingLabel, new GridBagConstraints(
					0, // gridX
					0, // gridY
					1, // gridWidth
					1, // gridHeight
					1.0, // weightX
					1.0, // weightY
					GridBagConstraints.NORTHEAST, // anchor
					GridBagConstraints.HORIZONTAL, // fill
					new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
					0, // ipadX
					0 ) // ipadY
			);
			this.extrasPanel.add( this.opacityLabel, new GridBagConstraints(
					1, // gridX
					0, // gridY
					1, // gridWidth
					1, // gridHeight
					1.0, // weightX
					1.0, // weightY
					GridBagConstraints.NORTHEAST, // anchor
					GridBagConstraints.HORIZONTAL, // fill
					new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
					0, // ipadX
					0 ) // ipadY
			);
			this.extrasPanel.add( this.colorLabel, new GridBagConstraints(
					0, // gridX
					1, // gridY
					2, // gridWidth
					1, // gridHeight
					1.0, // weightX
					1.0, // weightY
					GridBagConstraints.NORTHEAST, // anchor
					GridBagConstraints.HORIZONTAL, // fill
					new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
					0, // ipadX
					0 ) // ipadY
			);
			this.extrasPanel.add( this.scaleLabel, new GridBagConstraints(
					0, // gridX
					2, // gridY
					1, // gridWidth
					1, // gridHeight
					1.0, // weightX
					1.0, // weightY
					GridBagConstraints.NORTHEAST, // anchor
					GridBagConstraints.HORIZONTAL, // fill
					new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
					0, // ipadX
					0 ) // ipadY
			);
			this.extrasPanel.add( this.parentInfoPanel, new GridBagConstraints(
					1, // gridX
					2, // gridY
					1, // gridWidth
					1, // gridHeight
					1.0, // weightX
					1.0, // weightY
					GridBagConstraints.NORTHEAST, // anchor
					GridBagConstraints.HORIZONTAL, // fill
					new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
					0, // ipadX
					0 ) // ipadY
			);
			this.infoPanel.add( this.transformLabel, new GridBagConstraints(
					0, // gridX
					2, // gridY
					1, // gridWidth
					1, // gridHeight
					1.0, // weightX
					0.0, // weightY
					GridBagConstraints.NORTHEAST, // anchor
					GridBagConstraints.HORIZONTAL, // fill
					new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
					0, // ipadX
					0 ) // ipadY
			);
		}
		else
		{
			this.add( this.mainPanel, BorderLayout.CENTER );
			if( this.transformLabel.getParent() != null )
			{
				this.transformLabel.getParent().remove( this.transformLabel );
			}
		}

		if( this.virtualParentHashCode != -1 )
		{
			this.infoPanel.add( this.virtualParentPanel, new GridBagConstraints(
					0, // gridX
					3, // gridY
					1, // gridWidth
					1, // gridHeight
					1.0, // weightX
					0.0, // weightY
					GridBagConstraints.NORTHEAST, // anchor
					GridBagConstraints.HORIZONTAL, // fill
					new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
					0, // ipadX
					0 ) // ipadY
			);
		}
		else if( this.virtualParentPanel.getParent() != null )
		{
			this.infoPanel.remove( this.virtualParentPanel );
		}

		this.revalidate();
	}

	public void setSelectedNode( int hashCode, boolean listenToSelection )
	{
		BasicTreeNode rootNode = (BasicTreeNode)this.treeModel.getRoot();
		BasicTreeNode foundNode = rootNode.getMatchingNode( hashCode );
		if( foundNode != null )
		{
			this.listenToSelection = listenToSelection;
			this.tree.setSelectionPath( new TreePath( foundNode.getPath() ) );
			this.listenToSelection = true;
		}
	}

	public void setSelectedNode( BasicTreeNode node, boolean listenToSelection )
	{
		BasicTreeNode rootNode = (BasicTreeNode)this.treeModel.getRoot();
		BasicTreeNode foundNode = rootNode.getMatchingNode( node );
		if( foundNode != null )
		{
			this.listenToSelection = listenToSelection;
			this.tree.setSelectionPath( new TreePath( foundNode.getPath() ) );
			this.listenToSelection = true;
		}
	}

	public void setRootNode( BasicTreeNode root )
	{
		this.treeModel.setRoot( root );
	}

	public BasicTreeNode getRootNode()
	{
		return (BasicTreeNode)this.treeModel.getRoot();
	}

	protected TitledBorder createTitledBorder( String title )
	{
		TitledBorder border = new TitledBorder( title );
		border.setTitleColor( Color.DARK_GRAY );
		border.setTitleFont( border.getTitleFont().deriveFont( Font.ITALIC, 10 ) );
		return border;
	}

	@Override
	public void valueChanged( TreeSelectionEvent e )
	{
		if( e.getNewLeadSelectionPath() != null )
		{
			Object selectedObject = e.getNewLeadSelectionPath().getLastPathComponent();
			if( selectedObject instanceof BasicTreeNode )
			{
				BasicTreeNode sgNode = (BasicTreeNode)selectedObject;
				setData( sgNode );
				this.tree.scrollPathToVisible( e.getNewLeadSelectionPath() );
				if( this.listenToSelection && ( this.parentPanel != null ) && this.parentPanel.shouldMirrorSelection() )
				{
					this.parentPanel.setSelectionOnOtherTree( this, sgNode );
				}
			}
		}
	}

}
