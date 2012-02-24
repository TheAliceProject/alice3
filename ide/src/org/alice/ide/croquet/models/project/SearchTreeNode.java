package org.alice.ide.croquet.models.project;

import java.util.List;

import javax.swing.Icon;

import edu.cmu.cs.dennisc.java.util.Collections;

public class SearchTreeNode {

	private List<SearchTreeNode> children = Collections.newLinkedList();
	private SearchTreeNode parent;
	private String content;

	public SearchTreeNode( SearchTreeNode parent, String content ) {
		this.parent = parent;
		this.content = content;
	}

	public int getNumChildren() {
		return children.size();
	}

	public SearchTreeNode getChild( int index ) {
		return children.get( index );
	}

	public int getChildIndex( SearchTreeNode child ) {
		return child.getParent().getChildIndex( child );
	}

	public SearchTreeNode getParent() {
		return this.parent;
	}

	public String getText() {
		return content;
	}

	public Icon getIcon() {
		return null;
	}

	public void addChild( SearchTreeNode searchTreeNode ) {
		this.children.add( searchTreeNode );
	}

	@Override
	public String toString() {
		super.toString();
		return content;
	}

	public void removeSelf() {
		parent.removeChild( this );
	}

	private void removeChild( SearchTreeNode child ) {
		System.out.println( child.getText() );
		children.remove( child );
	}

}
