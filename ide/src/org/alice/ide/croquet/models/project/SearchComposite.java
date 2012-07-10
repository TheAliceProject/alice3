/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.ide.croquet.models.project;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.alice.ide.croquet.models.project.views.SearchView;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringState;
import org.lgna.croquet.TabComposite;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

/**
 * @author Matt May
 */
public class SearchComposite extends TabComposite<SearchView> {

	private StringState searchState = createStringState( createKey( "searchState" ) );
	private BooleanState showGenerated = createBooleanState( createKey( "showGenerated" ), false );
	private BooleanState showProcedures = createBooleanState( createKey( "showProcedure" ), true );
	private BooleanState showFunctions = createBooleanState( createKey( "showFunctions" ), true );
	private final SearchDialogManager manager;
	private boolean isJumpDesired = true;

	public SearchComposite() {
		super( java.util.UUID.fromString( "8f75a1e2-805d-4d9f-bef6-099204fe8d60" ) );
		manager = new SearchDialogManager();
	}

	public BooleanState getShowGenerated() {
		return this.showGenerated;
	}
	public BooleanState getShowProcedure() {
		return this.showProcedures;
	}
	public BooleanState getShowFunctions() {
		return this.showFunctions;
	}

	@Override
	public boolean isCloseable() {
		return false;
	}

	@Override
	protected SearchView createView() {
		return new SearchView( this );
	}

	public void addSelectedListener( ValueListener<SearchTreeNode> listener ) {
		manager.addValueListener( listener );
	}

	public StringState getStringState() {
		return this.searchState;
	}

	public SearchDialogManager getManager() {
		return manager;
	}

	public class SearchDialogManager extends SearchTreeManager implements ValueListener<String> {

		private ValueListener<Boolean> booleanListener = new ValueListener<Boolean>() {

			public void changing( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}

			public void changed( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				manager.changed( getStringState(), searchState.getValue(), searchState.getValue(), true );
			}
		};

		@Override
		protected boolean shouldShow( SearchTreeNode node ) {
			boolean generated = showGenerated.getValue() || !node.getIsGenerated();
			boolean function = showFunctions.getValue() || !node.getContent().isFunction();
			boolean procedure = showProcedures.getValue() || !node.getContent().isProcedure();
			if(node.getText().equals("getJoint")){
			System.out.println( node.getText() + ": ( " + generated + ", " + function + ", " + procedure + " )" );
			System.out.println( showGenerated.getValue() + "   " + !node.getIsGenerated() );
			}
			boolean shouldShowThis = generated && function && procedure;
			if( !shouldShowThis ) {
				for( SearchTreeNode child : node.getChildren() ) {
					if( shouldShow( child ) ) {
						return true;
					}
				}
			}
			return generated && function && procedure;
		}

		public ValueListener<Boolean> getBooleanListener() {
			return this.booleanListener;
		}

		private ValueListener<SearchTreeNode> adapter = new ValueListener<SearchTreeNode>() {

			public void changing( State<SearchTreeNode> state, SearchTreeNode prevValue, SearchTreeNode nextValue, boolean isAdjusting ) {
			}

			public void changed( State<SearchTreeNode> state, SearchTreeNode prevValue, SearchTreeNode nextValue, boolean isAdjusting ) {
				if( state.getValue() != prevValue ) {
					SearchTreeNode selection = state.getValue();
					if( selection != null && isJumpDesired ) {
						selection.invokeOperation();
					}
				}
			}

		};

		public SearchDialogManager() {
			super( java.util.UUID.fromString( "bb4777b7-20df-4d8d-b214-92acd390fdde" ) );

			searchState.addValueListener( this );
			this.addValueListener( adapter );
			SearchComposite.this.showFunctions.addValueListener( booleanListener );
			SearchComposite.this.showProcedures.addValueListener( booleanListener );
			SearchComposite.this.showGenerated.addValueListener( booleanListener );
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
		}
	}

	public SearchTreeNode setSelected( UserMethod method ) {
		SearchTreeNode find = manager.root.find( method );
		manager.setValue( find );
		return find;
	}

	public void setJumpDesired( boolean b ) {
		this.isJumpDesired = b;
	}
}
