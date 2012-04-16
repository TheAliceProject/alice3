package org.alice.ide.croquet.models.project;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.lgna.croquet.Application;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringState;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

public class SearchDialogManager extends SearchTreeManager implements ValueListener<String> {

	private StringState searchState;
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

	public SearchDialogManager( Map<UserMethod,LinkedList<MethodInvocation>> methodParentMap ) {
		super( java.util.UUID.fromString( "bb4777b7-20df-4d8d-b214-92acd390fdde" ), methodParentMap );

		this.searchState = new StringState( Application.INFORMATION_GROUP, java.util.UUID.fromString( "7012f7cd-c25b-4e9f-bba2-f4d172a0590b" ), "" ) {
		};
		searchState.addValueListener( this );
		this.addValueListener( adapter );
	}
	public void changing( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
	}

	public void changed( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
		hideAll();
		String check = state.getValue();
		check = check.replaceAll( "\\*", ".*" );
		String errorMessage;
		try {
			Pattern pattern = Pattern.compile( check.toLowerCase() );
			ArrayList<SearchTreeNode> iterateList = Collections.newArrayList( hiddenList );
			for( SearchTreeNode hiddenNode : iterateList ) {
				Matcher matcher = pattern.matcher( hiddenNode.getContent().getName().toLowerCase() );
				if( matcher.find() ) {
					if( check.length() == 0 || hiddenNode.getDepth() <= SHOULD_BE_EXPANDED ) {
						show( hiddenNode );
					}
				}
			}
			errorMessage = null;
		} catch( PatternSyntaxException pse ) {
			errorMessage = "bad pattern";
		}
		if( errorMessage != null ) {
			Logger.todo( "update label", errorMessage );
		}
		this.refreshAll();
		setProperExpandedLevels( root );
		//		owner.expandAllRows();
	}
	public StringState getStringState() {
		return this.searchState;
	}
}
