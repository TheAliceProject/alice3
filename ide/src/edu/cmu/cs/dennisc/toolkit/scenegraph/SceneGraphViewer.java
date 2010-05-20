package edu.cmu.cs.dennisc.toolkit.scenegraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

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

import edu.cmu.cs.dennisc.javax.swing.BorderFactory;

public class SceneGraphViewer extends JPanel implements TreeSelectionListener
{
	private JPanel mainPanel;
	private JSplitPane splitPane;
	private JPanel infoPanel;
	private DefaultTreeModel treeModel;
	private JTree tree;
	
	private JLabel nameLabel;
	private JLabel classLabel;
	private JLabel transformLabel;
	private JTextArea stackTracePanel;
	private JScrollPane stackTraceScrollPane;
	
	private JPanel virtualParentPanel;
	private JLabel virtualParentNameLabel;
	private JButton goToVirtualParentButton;
	private int virtualParentHashCode = -1;
	
	private JPanel extrasPanel;
	
	private JLabel colorLabel;
	private JLabel opacityLabel;
	private JLabel isShowingLabel;
	private JLabel scaleLabel;
	
	private Date timeStamp;
	private JLabel timeStampLabel;
	
	private boolean listenToSelection = true;
	private SceneGraphViewerPanel parentPanel = null;
	
	public SceneGraphViewer(SceneGraphTreeNode treeRoot)
	{
		this(treeRoot, null);
	}
	
	public SceneGraphViewer(SceneGraphTreeNode treeRoot, SceneGraphViewerPanel parentPanel)
	{
		super();
		this.parentPanel = parentPanel;
		this.timeStamp = new Date(System.currentTimeMillis());
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BorderLayout());
		this.treeModel = new DefaultTreeModel(treeRoot);
		this.tree = new JTree(treeModel);
		this.tree.setCellRenderer(new SceneGraphCellRenderer());
		this.infoPanel = new JPanel();
		this.infoPanel.setLayout(new GridBagLayout());
		
		this.nameLabel = new JLabel("");
		this.nameLabel.setBorder(createTitledBorder("Name"));
		this.classLabel = new JLabel("");
		this.classLabel.setBorder(createTitledBorder("Class"));
		this.transformLabel = new JLabel("");
		this.transformLabel.setBorder(createTitledBorder("Absolute Transform"));
		
		this.infoPanel.add(this.nameLabel, new GridBagConstraints(
				0, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.NORTHEAST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		this.infoPanel.add(this.classLabel, new GridBagConstraints(
				0, // gridX
				1, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.NORTHEAST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		this.infoPanel.add(this.transformLabel, new GridBagConstraints(
				0, // gridX
				2, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.NORTHEAST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		
		this.stackTracePanel = new JTextArea();
		this.stackTracePanel.setEditable(false);
		this.stackTracePanel.setLayout(new BoxLayout(this.stackTracePanel, BoxLayout.Y_AXIS));
		this.stackTraceScrollPane = new JScrollPane(this.stackTracePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.stackTraceScrollPane.setMinimumSize(new Dimension(50, 80));
		this.stackTraceScrollPane.setPreferredSize(new Dimension(200, 80));
		
		this.colorLabel = new JLabel("");
		this.colorLabel.setBorder(createTitledBorder("Color"));
		this.opacityLabel = new JLabel("");
		this.opacityLabel.setBorder(createTitledBorder("Opacity"));
		this.isShowingLabel = new JLabel("");
		this.isShowingLabel.setBorder(createTitledBorder("Showing"));
		this.scaleLabel = new JLabel("");
		this.scaleLabel.setBorder(createTitledBorder("Scale"));
		
		this.extrasPanel = new JPanel();
		this.extrasPanel.setLayout(new GridBagLayout());
		
		this.extrasPanel.add(this.isShowingLabel, new GridBagConstraints(
				0, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				1.0, // weightY
				GridBagConstraints.NORTHEAST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		this.extrasPanel.add(this.opacityLabel, new GridBagConstraints(
				1, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				1.0, // weightY
				GridBagConstraints.NORTHEAST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		this.extrasPanel.add(this.colorLabel, new GridBagConstraints(
				0, // gridX
				1, // gridY
				2, // gridWidth
				1, // gridHeight
				1.0, // weightX
				1.0, // weightY
				GridBagConstraints.NORTHEAST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		this.extrasPanel.add(this.scaleLabel, new GridBagConstraints(
				0, // gridX
				2, // gridY
				2, // gridWidth
				1, // gridHeight
				1.0, // weightX
				1.0, // weightY
				GridBagConstraints.NORTHEAST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
				0, // ipadX
				0) // ipadY
				);
		
		this.virtualParentPanel = new JPanel();
		this.virtualParentPanel.setLayout( new BoxLayout(this.virtualParentPanel, BoxLayout.X_AXIS));
		this.virtualParentPanel.setBorder(createTitledBorder("Virtual Parent"));
		this.virtualParentNameLabel = new JLabel("NO VIRTUAL PARENT");
		this.goToVirtualParentButton = new JButton("Go to parent");
		this.goToVirtualParentButton.addActionListener( new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (virtualParentHashCode != -1)
				{
					setSelectedNode(virtualParentHashCode, true);
				}
				
			}
		});
		
		this.virtualParentPanel.add(this.virtualParentNameLabel);
		this.virtualParentPanel.add(this.goToVirtualParentButton);
		
		this.timeStampLabel = new JLabel(this.timeStamp.toString());
		
		this.mainPanel.add(this.timeStampLabel, BorderLayout.NORTH);
		this.mainPanel.add(new JScrollPane(this.tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		this.mainPanel.add(this.infoPanel, BorderLayout.SOUTH);
		this.mainPanel.setPreferredSize(new Dimension(100, 600));
		
		this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.splitPane.setTopComponent(this.mainPanel);
		this.splitPane.setBottomComponent(this.stackTraceScrollPane);
		this.splitPane.setDividerLocation(.1);
		
		this.setLayout(new BorderLayout());
		this.add(splitPane, BorderLayout.CENTER);
		
		this.tree.addTreeSelectionListener(this);
		
	}
	
	public void setSelectedNode(int hashCode, boolean listenToSelection)
	{
		SceneGraphTreeNode rootNode =  (SceneGraphTreeNode)this.treeModel.getRoot();
		SceneGraphTreeNode foundNode = rootNode.getMatchingNode(hashCode);
		if (foundNode != null)
		{
			this.listenToSelection = listenToSelection;
			this.tree.setSelectionPath(new TreePath(foundNode.getPath()));
			this.listenToSelection = true;
		}
	}
	
	public void setSelectedNode( SceneGraphTreeNode node, boolean listenToSelection )
	{
		SceneGraphTreeNode rootNode =  (SceneGraphTreeNode)this.treeModel.getRoot();
		SceneGraphTreeNode foundNode = rootNode.getMatchingNode(node);
		if (foundNode != null)
		{
			this.listenToSelection = listenToSelection;
			this.tree.setSelectionPath(new TreePath(foundNode.getPath()));
			this.listenToSelection = true;
		}
	}
	
	public void setRootNode( SceneGraphTreeNode root )
	{
		this.treeModel.setRoot(root);
	}
	
	public SceneGraphTreeNode getRootNode()
	{
		return (SceneGraphTreeNode)this.treeModel.getRoot();
	}
	
	private TitledBorder createTitledBorder(String title)
	{
		TitledBorder border = new TitledBorder(title);
		border.setTitleColor(Color.DARK_GRAY);
		border.setTitleFont(border.getTitleFont().deriveFont(Font.ITALIC, 10));
		return border;
	}
	
	private void setData(SceneGraphTreeNode sgNode)
	{
		if (sgNode.name == null)
		{
			this.nameLabel.setText("<NO NAME>");
		}
		else
		{
			this.nameLabel.setText("'"+sgNode.name+"'");
		}
		this.classLabel.setText(sgNode.className);
		String positionString = "NO POSITION";
		if (sgNode.absoluteTransform != null)
		{
			positionString = String.format("[%.3f, %.3f, %.3f]", sgNode.absoluteTransform.translation.x, sgNode.absoluteTransform.translation.y, sgNode.absoluteTransform.translation.z).toString();
		}
		this.transformLabel.setText(positionString);
		
		if (sgNode.stackTrace != null)
		{
			StringBuilder sb = new StringBuilder();
			for (StackTraceElement stack : sgNode.stackTrace)
			{
				sb.append(stack.toString() + "\n");
			}
			this.stackTracePanel.setText(sb.toString());
			JScrollBar verticalScrollBar = this.stackTraceScrollPane.getVerticalScrollBar();
			if (verticalScrollBar != null)
			{
				verticalScrollBar.setValue(verticalScrollBar.getMinimum());
			}
		}
		this.virtualParentHashCode = -1;
		if (sgNode.virtualParentHashCode != -1)
		{
			this.virtualParentHashCode = sgNode.virtualParentHashCode;
			this.virtualParentNameLabel.setText(sgNode.virtualParentName);
			this.infoPanel.add(this.virtualParentPanel, new GridBagConstraints(
					0, // gridX
					3, // gridY
					1, // gridWidth
					1, // gridHeight
					1.0, // weightX
					0.0, // weightY
					GridBagConstraints.NORTHEAST, // anchor
					GridBagConstraints.HORIZONTAL, // fill
					new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
					0, // ipadX
					0) // ipadY
					);
		}
		else if ( this.virtualParentPanel.getParent() != null)
		{
			this.infoPanel.remove(this.virtualParentPanel);
		}
		
		if (sgNode.hasExtras)
		{
			this.infoPanel.add(this.extrasPanel, new GridBagConstraints(
					1, // gridX
					0, // gridY
					1, // gridWidth
					this.infoPanel.getComponentCount(), // gridHeight
					1.0, // weightX
					1.0, // weightY
					GridBagConstraints.NORTHEAST, // anchor
					GridBagConstraints.BOTH, // fill
					new Insets(0, 0, 0, 0), // insets (top, left, bottom, right)
					0, // ipadX
					0) // ipadY
					);
			if (sgNode.isShowing)
			{
				this.isShowingLabel.setText("SHOWING");
			}
			else
			{
				this.isShowingLabel.setText("NOT SHOWING");
			}
			String colorString = String.format("%.2f, %.2f, %.2f, %.2f", sgNode.color.red, sgNode.color.green, sgNode.color.blue, sgNode.color.alpha);
			this.colorLabel.setText(colorString);
			Color backgroundColor = new Color((int)(sgNode.color.red*255), (int)(sgNode.color.green*255), (int)(sgNode.color.blue*255));
			this.colorLabel.setBackground(backgroundColor);
			this.colorLabel.setOpaque(true);
			this.colorLabel.revalidate();
			
			String opacityString = String.format("%.2f", sgNode.opacity);
			this.opacityLabel.setText(opacityString);
			
		}
		else if (this.extrasPanel.getParent() != null)
		{
			this.infoPanel.remove(this.extrasPanel);
		}
		
		
		this.infoPanel.invalidate();
	}

	public void valueChanged(TreeSelectionEvent e) 
	{
		if (e.getNewLeadSelectionPath() != null)
		{
			Object selectedObject = e.getNewLeadSelectionPath().getLastPathComponent();
			if (selectedObject instanceof SceneGraphTreeNode)
			{
				SceneGraphTreeNode sgNode = (SceneGraphTreeNode)selectedObject;
				setData(sgNode);
				this.tree.scrollPathToVisible(e.getNewLeadSelectionPath());
				if (this.listenToSelection && this.parentPanel != null && this.parentPanel.shouldMirrorSelection())
				{
					this.parentPanel.setSelectionOnOtherTree(this, sgNode);
				}
			}
		}
	}
	
}
