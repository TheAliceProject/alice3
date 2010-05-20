package edu.cmu.cs.dennisc.toolkit.scenegraph;

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
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import edu.cmu.cs.dennisc.scenegraph.Component;

public class SceneGraphViewerPanel extends JPanel implements ActionListener {

	protected JScrollPane scrollPane;
	protected JSplitPane splitPane;
	protected JButton captureButton;
	protected JCheckBox mirrorSelectionCheckBox;
	protected JPanel buttonPanel;
	protected Component sgRoot;
	
	private SceneGraphViewer leftTree = null;
	private SceneGraphViewer rightTree = null;
	
	public SceneGraphViewerPanel()
	{
		this.setLayout(new BorderLayout());
		this.buttonPanel = new JPanel();
		this.captureButton = new JButton("Capture SceneGraph");
		this.captureButton.addActionListener(this);
		this.mirrorSelectionCheckBox = new JCheckBox("Mirror Selection");
		this.mirrorSelectionCheckBox.setSelected(true);
		this.buttonPanel.add(this.captureButton);
		this.buttonPanel.add(this.mirrorSelectionCheckBox);
		this.add(this.buttonPanel, BorderLayout.SOUTH);
		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.add(this.splitPane, BorderLayout.CENTER);
	}
	
	public void setRoot( Component sceneGraphRoot )
	{
		this.sgRoot = sceneGraphRoot;
		captureTree();
	}
	
	private void getFlattenedTree( SceneGraphTreeNode node, List<SceneGraphTreeNode> flattenedTree)
	{
		flattenedTree.add(node);
		for (int i=0; i<node.getChildCount(); i++)
		{
			getFlattenedTree((SceneGraphTreeNode)node.getChildAt(i), flattenedTree);
		}
	}

	private void diffTrees( SceneGraphTreeNode treeA, SceneGraphTreeNode treeB)
	{
		LinkedList<SceneGraphTreeNode> flatTreeA = new LinkedList<SceneGraphTreeNode>();
		LinkedList<SceneGraphTreeNode> flatTreeB = new LinkedList<SceneGraphTreeNode>();
		getFlattenedTree(treeA, flatTreeA);
		getFlattenedTree(treeB, flatTreeB);
		for (SceneGraphTreeNode nodeA : flatTreeA)
		{ 
			
			nodeA.markDifferent(SceneGraphTreeNode.Difference.NONE);
		}
		for (SceneGraphTreeNode nodeB : flatTreeB)
		{ 
			nodeB.markDifferent(SceneGraphTreeNode.Difference.NONE);
		}
		for (SceneGraphTreeNode nodeA : flatTreeA)
		{
			int matchingIndex = flatTreeB.indexOf(nodeA);
			if (matchingIndex != -1)
			{
				SceneGraphTreeNode nodeB = flatTreeB.get(matchingIndex);
				boolean isDifferent = nodeA.isDifferent(nodeB);
				if (isDifferent)
				{
					nodeB.markDifferent(SceneGraphTreeNode.Difference.ATTRIBUTES);
				}
				flatTreeB.remove(matchingIndex);
			}
			else
			{
				nodeA.markDifferent(SceneGraphTreeNode.Difference.NEW_NODE);
			}
		}
		for (SceneGraphTreeNode nodeB : flatTreeB)
		{
			nodeB.markDifferent(SceneGraphTreeNode.Difference.NEW_NODE);
		}
	}
	
	private void captureTree()
	{
		if (this.sgRoot != null)
		{
			SceneGraphTreeNode newRoot = SceneGraphTreeNode.createSceneGraphTreeStructure(this.sgRoot);
			SceneGraphViewer oldTree = (SceneGraphViewer)this.splitPane.getBottomComponent();
			if (oldTree != null)
			{
				SceneGraphTreeNode oldRoot = oldTree.getRootNode();
				diffTrees(oldRoot, newRoot);
				oldTree.setRootNode(oldRoot);
				
				this.splitPane.remove(oldTree);
				this.splitPane.setTopComponent(oldTree);
				this.leftTree = oldTree;
			}
			SceneGraphViewer newTreeViewer = new SceneGraphViewer(newRoot, this);
			this.rightTree = newTreeViewer;
			this.splitPane.setBottomComponent(newTreeViewer);
			if (this.splitPane.getTopComponent() != null)
			{
				this.splitPane.setDividerLocation(.5);
			}
			this.invalidate();
		}
	}

	public boolean shouldMirrorSelection()
	{
		return this.mirrorSelectionCheckBox.isSelected();
	}
	
	public void setSelectionOnOtherTree(SceneGraphViewer sender, SceneGraphTreeNode selectedNode)
	{
		if (sender ==  this.leftTree)
		{
			if (this.rightTree != null)
			{
				this.rightTree.setSelectedNode(selectedNode, false);
			}
		}
		else if (sender ==  this.rightTree)
		{
			if (this.leftTree != null)
			{
				this.leftTree.setSelectedNode(selectedNode, false);
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() ==  this.captureButton)
		{
			this.captureTree();
		}
		
	}
	
	
}
