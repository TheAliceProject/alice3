package org.alice.ide.croquet.models.project;

import java.util.List;

import javax.swing.Icon;

import org.alice.ide.declarationseditor.DeclarationTabState;
import org.lgna.croquet.ActionOperation;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.java.util.Collections;

public class SearchTreeNode {

	private List<SearchTreeNode> children = Collections.newLinkedList();
	private SearchTreeNode parent;
	private Object content;
	private ActionOperation operation;

	public SearchTreeNode( SearchTreeNode parent, Object content ) {
		this.parent = parent;
		this.content = content;
		if( parent != null && parent.getParent() == null && content instanceof UserMethod ) {//node is not root AND node's parent is root
			this.operation = DeclarationTabState.getInstance().getItemSelectionOperation( (UserMethod)content );
		} else if( parent != null ) {
			this.operation = parent.operation;
		}
	}

	public int getNumChildren() {
		return children.size();
	}

	public SearchTreeNode getChild( int index ) {
		return children.get( index );
	}

	public int getChildIndex( SearchTreeNode child ) {
		return child.getParent().getChildren().indexOf( child );
	}

	public SearchTreeNode getParent() {
		return this.parent;
	}

	public String getText() {
		if( content instanceof UserMethod ) {
			UserMethod method = (UserMethod)content;
			String edit = parent.parent == null ? "edit" : "";
			return "<html> " + edit + " <strong>" + method.getName() + "</strong></html>";
		} else if( content instanceof JavaMethod ) {
			JavaMethod javaMethod = (JavaMethod)content;
			return javaMethod.getName();
		} else if( content instanceof String ) {
			return (String)content;
		}
		return "ERROR: (mmay) unhandledtype in tree: " + content.getClass();
	}
	public Icon getIcon() {
		return null;
	}

	public void addChild( SearchTreeNode searchTreeNode ) {
		this.children.add( searchTreeNode );
	}

	@Override
	public String toString() {
		return getText();
	}

	public void removeSelf() {
		parent.removeChild( this );
	}

	private void removeChild( SearchTreeNode child ) {
		children.remove( child );
	}

	public List<SearchTreeNode> getChildren() {
		return children;
	}

	public void removeChildren() {
		this.children.clear();
	}

	public void invokeOperation() {
		if( this.operation != null ) {
			this.operation.fire();
		}
	}
}
