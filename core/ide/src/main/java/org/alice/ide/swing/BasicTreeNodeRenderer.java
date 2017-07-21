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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class BasicTreeNodeRenderer implements TreeCellRenderer
{
	private final static Color SELECTED_BORDER_COLOR = Color.GREEN;
	private final static Color NOT_SELECTED_BORDER_COLOR = Color.GRAY;

	private final static Color NEUTRAL_BACKGROUND_COLOR = new Color( 240, 240, 240 );
	private final static Color DIFFERENT_NODE_BACKGROUND_COLOR = Color.YELLOW;
	private final static Color DIFFERENT_ATTRIBUTE_BACKGROUND_COLOR = Color.CYAN;
	private final static Color DIFFERENT_CHILD_BACKGROUND_COLOR = Color.PINK;

	private JPanel holder;
	private JLabel nameLabel;
	private JPanel differenceIndicator;
	private DefaultTreeCellRenderer defaultRenderer = null;

	public BasicTreeNodeRenderer()
	{
		this.holder = new JPanel();
		//		this.holder.setLayout(new BoxLayout(this.holder, BoxLayout.X_AXIS));
		//		this.holder.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		this.holder.setLayout( new GridBagLayout() );
		this.holder.setOpaque( false );

		this.differenceIndicator = new JPanel();
		this.differenceIndicator.setPreferredSize( new Dimension( 4, 20 ) );
		this.differenceIndicator.setMaximumSize( new Dimension( 4, 100 ) );
		this.differenceIndicator.setBackground( Color.RED );

		this.nameLabel = new JLabel();
		this.nameLabel.setOpaque( true );
		this.nameLabel.setBorder( createBorder( false ) );
		this.nameLabel.setBackground( NEUTRAL_BACKGROUND_COLOR );
		this.holder.add( this.differenceIndicator, new GridBagConstraints(
				0, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.0, // weightX
				1.0, // weightY
				GridBagConstraints.CENTER, // anchor
				GridBagConstraints.VERTICAL, // fill
				new Insets( 2, 0, 2, 2 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		this.holder.add( this.nameLabel, new GridBagConstraints(
				1, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				1.0, // weightY
				GridBagConstraints.CENTER, // anchor
				GridBagConstraints.BOTH, // fill
				new Insets( 2, 0, 2, 2 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		this.defaultRenderer = new DefaultTreeCellRenderer();
	}

	private Border createBorder( boolean isSelected )
	{
		final int THICKNESS = 2;
		final int MARGIN = 2;
		if( isSelected )
		{
			return BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder( SELECTED_BORDER_COLOR, THICKNESS ),
					BorderFactory.createEmptyBorder( MARGIN, MARGIN, MARGIN, MARGIN )
					);
		}
		else
		{
			return BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder( NOT_SELECTED_BORDER_COLOR, THICKNESS ),
					BorderFactory.createEmptyBorder( MARGIN, MARGIN, MARGIN, MARGIN )
					);
		}
	}

	@Override
	public Component getTreeCellRendererComponent( JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus )
	{
		if( value instanceof BasicTreeNode )
		{
			BasicTreeNode node = (BasicTreeNode)value;
			this.nameLabel.setText( node.toString() );
			this.nameLabel.setBorder( createBorder( selected ) );
			if( node.difference == BasicTreeNode.Difference.NEW_NODE )
			{
				this.differenceIndicator.setOpaque( true );
				this.differenceIndicator.setBackground( DIFFERENT_NODE_BACKGROUND_COLOR );
			}
			else if( node.difference == BasicTreeNode.Difference.ATTRIBUTES )
			{
				this.differenceIndicator.setOpaque( true );
				this.differenceIndicator.setBackground( DIFFERENT_ATTRIBUTE_BACKGROUND_COLOR );
			}
			else if( node.hasDifferentChild() )
			{
				this.differenceIndicator.setOpaque( true );
				this.differenceIndicator.setBackground( DIFFERENT_CHILD_BACKGROUND_COLOR );
			}
			else
			{
				this.differenceIndicator.setOpaque( false );
				this.differenceIndicator.setBackground( NEUTRAL_BACKGROUND_COLOR );
			}
			if( node.hasExtras && ( node.color != null ) )
			{
				this.nameLabel.setBackground( node.getAWTColor() );
			}
			else
			{
				this.nameLabel.setBackground( NEUTRAL_BACKGROUND_COLOR );
			}
			this.holder.revalidate();
			this.holder.doLayout();
			return this.holder;
		}
		else
		{
			return this.defaultRenderer.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
		}
	}

}
