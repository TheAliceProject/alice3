package org.alice.ide.croquet.models.project;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;

import org.lgna.croquet.Application;
import org.lgna.croquet.CustomTreeSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringState;
import org.lgna.croquet.components.Tree;

import edu.cmu.cs.dennisc.java.util.Collections;

public class SearchDialogManager extends CustomTreeSelectionState<SearchTreeNode> implements ValueListener<String> {

	private List<SearchTreeNode> parentList = Collections.newLinkedList();
	private HashMap<SearchTreeNode,SearchTreeNode> parentMap = Collections.newHashMap();
	private HashMap<SearchTreeNode,List<SearchTreeNode>> childMap = Collections.newHashMap();
	private static SearchTreeNode root = new SearchTreeNode( null, "Project" );
	private StringState searchState;
	private Tree<SearchTreeNode> owner;
	private ValueListener<SearchTreeNode> adapter = new ValueListener<SearchTreeNode>() {

		public void changing( State<SearchTreeNode> state, SearchTreeNode prevValue, SearchTreeNode nextValue, boolean isAdjusting ) {
		}

		public void changed( State<SearchTreeNode> state, SearchTreeNode prevValue, SearchTreeNode nextValue, boolean isAdjusting ) {
			if( state.getValue() != prevValue ) {
				SearchTreeNode selection = state.getValue();
				if( selection != null ) {
					selection.invokeOperation();
				}
			}
		}

	};

	public SearchDialogManager() {
		super( Application.INFORMATION_GROUP, java.util.UUID.fromString( "bb4777b7-20df-4d8d-b214-92acd390fdde" ), SearchCodec.getSingleton(), null );

		this.searchState = new StringState( Application.INFORMATION_GROUP, java.util.UUID.fromString( "7012f7cd-c25b-4e9f-bba2-f4d172a0590b" ), "" ) {
		};
		searchState.addValueListener( this );
	}

	public void changing( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
	}

	public void changed( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
		for( SearchTreeNode parent : parentList ) {
			parent.removeSelf();
		}
		String check = state.getValue();
		check = check.replaceAll( "\\*", ".*" );
		for( SearchTreeNode parent : parentList ) {
			for( SearchTreeNode child : childMap.get( parent ) ) {
				Pattern pattern = Pattern.compile( check.toLowerCase() );
				Matcher matcher = pattern.matcher( child.getText().toLowerCase() );
				if( matcher.find() ) {
					show( child );
				}
			}
		}
		this.refreshAll();
		owner.expandAllRows();
	}
	private void show( SearchTreeNode node ) {
		SearchTreeNode parent = node.getParent();
		if( !root.getChildren().contains( node.getParent() ) ) {
			root.addChild( parent );
			parent.removeChildren();
			parent.addChild( node );
		} else {
			parent.addChild( node );
		}
	}
	public void addParentWithChildren( SearchTreeNode parent, List<SearchTreeNode> children ) {
		parentList.add( parent );
		childMap.put( parent, children );
		for( SearchTreeNode child : children ) {
			parentMap.put( child, parent );
		}
	}

	@Override
	protected int getChildCount( SearchTreeNode parent ) {
		return parent.getNumChildren();
	}

	@Override
	protected SearchTreeNode getChild( SearchTreeNode parent, int index ) {
		return parent.getChild( index );
	}

	@Override
	protected int getIndexOfChild( SearchTreeNode parent, SearchTreeNode child ) {
		return parent.getChildIndex( child );
	}

	@Override
	protected SearchTreeNode getRoot() {
		return root;
	}

	@Override
	protected SearchTreeNode getParent( SearchTreeNode node ) {
		return node.getParent();
	}

	@Override
	protected String getTextForNode( SearchTreeNode node ) {
		return node.getText();
	}

	@Override
	protected Icon getIconForNode( SearchTreeNode node ) {
		return node.getIcon();
	}

	public StringState getStringState() {
		return this.searchState;
	}

	public SearchTreeNode addNode( SearchTreeNode parent, Object content ) {
		SearchTreeNode rv;
		if( parent != null ) {
			parent.addChild( rv = new SearchTreeNode( parent, content ) );
		} else {
			getRoot().addChild( rv = new SearchTreeNode( getRoot(), content ) );
		}
		return rv;
	}

	private void removeNode( SearchTreeNode node ) {
		node.removeSelf();
	}

	@Override
	public boolean isLeaf( SearchTreeNode node ) {
		return node.getNumChildren() == 0;
	}

	public void setOwner( Tree<SearchTreeNode> tree ) {
		this.owner = tree;
		this.addValueListener( this.adapter );
	}
}
