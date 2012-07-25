package org.alice.ide.croquet.models.project.TreeNodesAndManagers;

import java.util.List;

import javax.swing.Icon;

import org.alice.ide.IDE;
import org.lgna.croquet.ItemCodec;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Collections;

public class SearchTreeNode implements Comparable<SearchTreeNode> {

	private List<SearchTreeNode> children = Collections.newLinkedList();
	private SearchTreeNode parent;
	private AbstractMethod method;
	private MethodInvocation methodInvocation;
	private boolean isGenerated;

	public SearchTreeNode( SearchTreeNode parent, AbstractMethod content ) {
		this.parent = parent;
		this.method = content;
		this.isGenerated = (content instanceof UserMethod) && ((UserMethod)content).getManagementLevel().isGenerated();
	}
	
	public SearchTreeNode( SearchTreeNode parent, MethodInvocation invocation ) {
		this.parent = parent;
		this.methodInvocation = invocation;
		this.method = methodInvocation.method.getValue();
		this.isGenerated = (invocation.method.getValue() instanceof UserMethod) && ((UserMethod)invocation.method.getValue()).getManagementLevel().isGenerated();
	}

	public boolean getIsGenerated() {
		return isGenerated;
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
		if( method instanceof UserMethod ) {
			UserMethod userMethod = (UserMethod)method;
			String edit = "";
			if( parent != null ) {
				edit = parent.parent == null ? "edit" : "";
			}
			return "<html> " + edit + " <strong>" + userMethod.getName() + "</strong></html>";
		} else if( method instanceof JavaMethod ) {
			JavaMethod javaMethod = (JavaMethod)method;
			return javaMethod.getName();
		}
		return "ERROR: (mmay) unhandledtype in tree: " + method.getClass();
	}
	public Icon getIcon() {
		return null;
	}

	public void addChild( SearchTreeNode searchTreeNode ) {
		this.children.add( searchTreeNode );
		java.util.Collections.sort( children );
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
		if( parent != null && parent.getParent() == null ) {// && content instanceof UserMethod ) {//node is not root AND node's parent is root
			assert methodInvocation == null;
			org.alice.ide.IDE.getActiveInstance().selectDeclarationComposite( org.alice.ide.declarationseditor.DeclarationComposite.getInstance( (UserMethod)method ) );
		} else if( parent != null ) {
			assert methodInvocation != null;
			org.alice.ide.IDE.getActiveInstance().selectDeclarationComposite( org.alice.ide.declarationseditor.DeclarationComposite.getInstance( (UserMethod)parent.method ) );
			IDE.getActiveInstance().showHighlightStencil( methodInvocation.expression.getValue(), null );
		}
	}

	public AbstractMethod getContent() {
		return method;
	}

	public int getDepth() {
		SearchTreeNode blah = this;
		int depth = 0;
		while( blah.getParent() != null ) {
			blah = blah.getParent();
			++depth;
		}
		return depth;
	}

	public int compareTo( SearchTreeNode o ) {
		if( getDepth() != o.getDepth() ) {
			return new Integer( getDepth() ).compareTo( new Integer( o.getDepth() ) );
		}
		return method.getName().compareTo( o.getContent().getName() );
	}

	public SearchTreeNode find( UserMethod method ) {
		if( this.method.equals( method ) ) {
			return this;
		}
		for( SearchTreeNode child : children ) {
			SearchTreeNode find = child.find( method );
			if( find != null ) {
				return find;
			}
		}
		return null;
	}

	public static ItemCodec<SearchTreeNode> getNewItemCodec() {
		return new ItemCodec<SearchTreeNode>(){

			public Class<SearchTreeNode> getValueClass() {
				return null;
			}

			public SearchTreeNode decodeValue( BinaryDecoder binaryDecoder ) {
				return null;
			}

			public void encodeValue( BinaryEncoder binaryEncoder, SearchTreeNode value ) {
			}

			public StringBuilder appendRepresentation( StringBuilder rv, SearchTreeNode value ) {
				return null;
			}
			
		};
	}
}
