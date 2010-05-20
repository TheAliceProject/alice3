package edu.cmu.cs.dennisc.toolkit.scenegraph;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import edu.cmu.cs.dennisc.javax.swing.components.JBorderPane;

public class SceneGraphCellRenderer implements TreeCellRenderer 
{
	private final static Color SELECTED_BORDER_COLOR = Color.GREEN;
	private final static Color NOT_SELECTED_BORDER_COLOR = Color.GRAY;
	
	private final static Color NEUTRAL_BACKGROUND_COLOR = new Color(240, 240, 240);
	private final static Color DIFFERENT_NODE_BACKGROUND_COLOR = Color.YELLOW;
	private final static Color DIFFERENT_ATTRIBUTE_BACKGROUND_COLOR = Color.CYAN;
	private final static Color DIFFERENT_CHILD_BACKGROUND_COLOR = Color.PINK;
	
	private JPanel holder;
	private JLabel nameLabel;
	private JPanel differenceIndicator;
	private DefaultTreeCellRenderer defaultRenderer = null;
	
	public SceneGraphCellRenderer()
	{
		this.holder = new JPanel();
//		this.holder.setLayout(new BoxLayout(this.holder, BoxLayout.X_AXIS));
//		this.holder.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		this.holder.setLayout(new GridBagLayout());
		this.holder.setOpaque(false);
		
		this.differenceIndicator = new JPanel();
		this.differenceIndicator.setPreferredSize(new Dimension(4, 20));
		this.differenceIndicator.setMaximumSize(new Dimension(4, 100));
		this.differenceIndicator.setBackground(Color.RED);
		
		this.nameLabel = new JLabel();
		this.nameLabel.setOpaque(true);
		this.nameLabel.setBorder(createBorder(false));
		this.nameLabel.setBackground(NEUTRAL_BACKGROUND_COLOR);
		this.holder.add(this.differenceIndicator, new GridBagConstraints(
				0, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.0, // weightX
				1.0, // weightY
				GridBagConstraints.CENTER, // anchor
				GridBagConstraints.VERTICAL, // fill
				new Insets(2, 0, 2, 2), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		this.holder.add(this.nameLabel, new GridBagConstraints(
				1, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				1.0, // weightY
				GridBagConstraints.CENTER, // anchor
				GridBagConstraints.BOTH, // fill
				new Insets(2, 0, 2, 2), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		this.defaultRenderer = new DefaultTreeCellRenderer();
	}
	
	private Border createBorder(boolean isSelected)
	{
		final int THICKNESS = 2;
		final int MARGIN = 2;
		if (isSelected)
		{
			return BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(SELECTED_BORDER_COLOR, THICKNESS), 
					BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN)
				);
		}
		else
		{
			return BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(NOT_SELECTED_BORDER_COLOR, THICKNESS), 
					BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN)
				);
		}
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) 
	{
		if (value instanceof SceneGraphTreeNode)
		{
			SceneGraphTreeNode sgNode = (SceneGraphTreeNode)value;
			this.nameLabel.setText(sgNode.toString());
			this.nameLabel.setBorder(createBorder(selected));
			if (sgNode.difference == SceneGraphTreeNode.Difference.NEW_NODE)
			{
				this.differenceIndicator.setOpaque(true);
				this.differenceIndicator.setBackground(DIFFERENT_NODE_BACKGROUND_COLOR);
			}
			else if (sgNode.difference == SceneGraphTreeNode.Difference.ATTRIBUTES)
			{
				this.differenceIndicator.setOpaque(true);
				this.differenceIndicator.setBackground(DIFFERENT_ATTRIBUTE_BACKGROUND_COLOR);
			}
			else if (sgNode.hasDifferentChild())
			{
				this.differenceIndicator.setOpaque(true);
				this.differenceIndicator.setBackground(DIFFERENT_CHILD_BACKGROUND_COLOR);
			}
			else
			{
				this.differenceIndicator.setOpaque(false);
				this.differenceIndicator.setBackground(NEUTRAL_BACKGROUND_COLOR);
			}
			if (sgNode.hasExtras && sgNode.color != null)
			{
				this.nameLabel.setBackground(sgNode.getAWTColor());
			}
			else
			{
				this.nameLabel.setBackground(NEUTRAL_BACKGROUND_COLOR);
			}
			this.holder.revalidate();
			this.holder.doLayout();
			return this.holder;	
		}
		else
		{
			return this.defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
	}

}
