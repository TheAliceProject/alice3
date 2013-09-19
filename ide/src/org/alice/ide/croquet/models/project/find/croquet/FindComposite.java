/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.croquet.models.project.find.croquet;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

import org.alice.ide.IDE;
import org.alice.ide.croquet.models.project.find.core.FindContentManager;
import org.alice.ide.croquet.models.project.find.core.FindReferencesTreeState;
import org.alice.ide.croquet.models.project.find.core.SearchObject;
import org.alice.ide.croquet.models.project.find.core.SearchObjectNode;
import org.alice.ide.croquet.models.project.find.croquet.views.FindView;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.FrameComposite;
import org.lgna.croquet.Group;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringState;
import org.lgna.croquet.codecs.DefaultItemCodec;
import org.lgna.croquet.data.RefreshableListData;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserType;
import org.lgna.story.ImplementationAccessor;

import com.sun.tools.javac.util.Pair;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class FindComposite extends FrameComposite<FindView> {

	protected final FindContentManager manager = new FindContentManager();
	public static Group FIND_COMPOSITE_GROUP = Group.getInstance( java.util.UUID.fromString( "609c0bf5-73c3-4987-a2b5-8225c19f7886" ) );
	private final StringState searchState = createStringState( createKey( "searchState" ) );
	private final BooleanState shouldINavigate = createBooleanState( createKey( "shouldNav" ), true );
	private boolean isActive;
	private final FindReferencesTreeState referenceTree = new FindReferencesTreeState();
	private final ItemCodec<SearchObject> codec1 = new DefaultItemCodec<SearchObject>( SearchObject.class );
	private final Map<SearchObject<?>, Map<Integer, Boolean>> expandMap = Collections.newHashMap();

	public FindComposite() {
		this( java.util.UUID.fromString( "c454dba4-80ac-4873-b899-67ea3cd726e9" ) );
	}

	protected FindComposite( UUID fromString ) {
		super( fromString, IDE.INFORMATION_GROUP );
		searchState.addValueListener( searchStateListener );
		searchResults.addValueListener( new ValueListener<SearchObject>() {

			public void changing( State<SearchObject> state, SearchObject prevValue, SearchObject nextValue, boolean isAdjusting ) {
			}

			public void changed( State<SearchObject> state, SearchObject prevValue, SearchObject nextValue, boolean isAdjusting ) {
				referenceTree.refreshWith( searchResults.getValue() );
			}
		} );
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.addProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
		org.alice.ide.project.ProjectDocumentState.getInstance().addValueListener( this.projectDocumentChangeListener );
		referenceTree.addValueListener( new ValueListener<SearchObjectNode>() {

			public void changing( State<SearchObjectNode> state, SearchObjectNode prevValue, SearchObjectNode nextValue, boolean isAdjusting ) {
			}

			public void changed( State<SearchObjectNode> state, SearchObjectNode prevValue, SearchObjectNode nextValue, boolean isAdjusting ) {
				if( shouldINavigate.getValue() && ( nextValue != null ) ) {
					if( nextValue.getValue() instanceof Expression ) {
						IDE.getActiveInstance().selectDeclarationComposite( org.alice.ide.declarationseditor.DeclarationComposite.getInstance( ( (Expression)nextValue.getValue() ).getFirstAncestorAssignableTo( UserMethod.class ) ) );
						searchResults.getValue().stencilHighlightForReference( (Expression)nextValue.getValue() );
					} else {
						IDE.getActiveInstance().selectDeclarationComposite( org.alice.ide.declarationseditor.DeclarationComposite.getInstance( ( (Expression)nextValue.getChildren().get( 0 ).getValue() ).getFirstAncestorAssignableTo( UserMethod.class ) ) );
						IDE.getActiveInstance().getHighlightStencil().hideIfNecessary();
					}
				}
			}
		} );
	}

	private final ValueListener<String> searchStateListener = new ValueListener<String>() {

		public void changing( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
		}

		public void changed( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
			data.refresh();
			referenceTree.refreshWith( searchResults.getValue() );
			if( data.getItemCount() == 1 ) {
				searchResults.setSelectedIndex( 0 );
			}
		}
	};

	private final org.alice.ide.project.events.ProjectChangeOfInterestListener projectChangeOfInterestListener = new org.alice.ide.project.events.ProjectChangeOfInterestListener() {
		public void projectChanged() {
			refresh();
		}
	};

	private final State.ValueListener<org.alice.ide.ProjectDocument> projectDocumentChangeListener = new State.ValueListener<org.alice.ide.ProjectDocument>() {
		public void changing( org.lgna.croquet.State<org.alice.ide.ProjectDocument> state, org.alice.ide.ProjectDocument prevValue, org.alice.ide.ProjectDocument nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<org.alice.ide.ProjectDocument> state, org.alice.ide.ProjectDocument prevValue, org.alice.ide.ProjectDocument nextValue, boolean isAdjusting ) {
			refresh();
		}
	};

	private final RefreshableListData<SearchObject> data = new RefreshableListData<SearchObject>( codec1 ) {

		@Override
		protected List createValues() {
			return setSearchResults();
		}

	};
	private final ActionOperation howToAddOperation = createActionOperation( createKey( "howToAdd" ), new Action() {

		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			//needs work
			if( searchResults.getValue() != null ) {
				//				searchResults.getValue().getSearchObject().
				AbstractDeclaration searchObject = searchResults.getValue().getSearchObject();
				AbstractMethod abstractMethod = searchObject.getFirstAncestorAssignableTo( AbstractMethod.class );
				if( searchObject instanceof AbstractMethod ) {
					AbstractType<?, ?, ?> declaringType = ( (AbstractMethod)searchObject ).getDeclaringType();
					//					AnonymousConstructorPane.lookup( awtComponent )
					//					MemberTabComposite.
					IDE.getActiveInstance().getMethodInvocations( (AbstractMethod)searchObject );
				}
			}
			return null;
		}

	} );
	private final ListSelectionState<SearchObject> searchResults = createListSelectionState( createKey( "searchResultsList" ), data, -1 );

	private final TreeExpansionListener treeListener = new TreeExpansionListener() {

		public void treeExpanded( TreeExpansionEvent event ) {
			TreePath path = event.getPath();
			if( expandMap.get( searchResults.getValue() ) == null ) {
				expandMap.put( searchResults.getValue(), new HashMap<Integer, Boolean>() );
			}
			expandMap.get( searchResults.getValue() ).put( ( (SearchObjectNode)path.getLastPathComponent() ).getLocationAmongstSiblings(), true );
		}

		public void treeCollapsed( TreeExpansionEvent event ) {
			TreePath path = event.getPath();
			if( expandMap.get( searchResults.getValue() ) == null ) {
				expandMap.put( searchResults.getValue(), new HashMap<Integer, Boolean>() );
			}
			expandMap.get( searchResults.getValue() ).put( ( (SearchObjectNode)path.getLastPathComponent() ).getLocationAmongstSiblings(), false );
		}
	};
	private final KeyListener keyListener = new KeyListener() {

		@SuppressWarnings( "rawtypes" )
		State selected = searchResults;
		Map<SearchObject<?>, Pair<Integer, Integer>> pairMap = Collections.newHashMap();

		public void keyTyped( KeyEvent e ) {
		}

		public void keyReleased( KeyEvent e ) {
		}

		public void keyPressed( KeyEvent e ) {
			if( ImplementationAccessor.getKeyFromKeyCode( e.getKeyCode() ) == org.lgna.story.Key.UP ) {
				if( selected == searchResults ) {
					if( searchResults.getValue() != null ) {
						searchResults.setSelectedIndex( searchResults.getSelectedIndex() - 1 );
						Map<Integer, Boolean> innerMap = expandMap.get( searchResults.getValue() );
						if( innerMap != null ) {
							for( Integer i : innerMap.keySet() ) {
								if( innerMap.get( i ) ) {
									getView().getTree().expandNode( referenceTree.selectAtCoordinates( i, -1 ) );
								}
							}
						}
						if( searchResults.getValue() == null ) {
							getView().enableLeftAndRight();
						}
					}
				} else if( selected == referenceTree ) {
					referenceTree.moveSelectedUpOne();
				}
			} else if( ImplementationAccessor.getKeyFromKeyCode( e.getKeyCode() ) == org.lgna.story.Key.DOWN ) {
				if( selected == searchResults ) {
					if( searchResults.getItemCount() != ( searchResults.getSelectedIndex() + 1 ) ) {
						getView().disableLeftAndRight();
						searchResults.setSelectedIndex( searchResults.getSelectedIndex() + 1 );
						Map<Integer, Boolean> innerMap = expandMap.get( searchResults.getValue() );
						if( innerMap != null ) {
							for( Integer i : innerMap.keySet() ) {
								if( innerMap.get( i ) ) {
									getView().getTree().expandNode( referenceTree.selectAtCoordinates( i, -1 ) );
								}
							}
						}
					}
				} else if( selected == referenceTree ) {
					referenceTree.moveSelectedDownOne();
				}
			} else if( ImplementationAccessor.getKeyFromKeyCode( e.getKeyCode() ) == org.lgna.story.Key.LEFT ) {
				if( selected != searchResults ) {
					selected = searchResults;
					pairMap.put( searchResults.getValue(), referenceTree.getSelectedCoordinates() );
					referenceTree.setValueTransactionlessly( null );
				}
			} else if( ImplementationAccessor.getKeyFromKeyCode( e.getKeyCode() ) == org.lgna.story.Key.RIGHT ) {
				if( selected != referenceTree ) {
					if( referenceTree.isEmpty() ) {
						selected = referenceTree;
						Pair<Integer, Integer> pair = pairMap.get( searchResults.getValue() );
						if( pairMap.get( searchResults.getValue() ) != null ) {
							referenceTree.setValueTransactionlessly( referenceTree.selectAtCoordinates( pair.fst, pair.snd ) );
						} else {
							referenceTree.setValueTransactionlessly( referenceTree.getTopValue() );
						}
					}
				}
			}
		}
	};

	private void refresh() {
		if( this.isActive ) {
			manager.refresh();
			data.refresh();
		}
	}

	protected List<SearchObject<?>> setSearchResults() {
		return manager.getResultsForString( searchState.getValue() );
	}

	@Override
	protected FindView createView() {
		return new FindView( this );
	}

	@SuppressWarnings( "rawtypes" )
	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.isActive = true;
		if( !manager.isInitialized() ) {
			manager.initialize( (UserType)IDE.getActiveInstance().getProgramType().fields.get( 0 ).getValueType() );
			refresh();
		} else {
			refresh();
		}
	}

	@Override
	public void handlePostDeactivation() {
		this.isActive = false;
		super.handlePostDeactivation();
	}

	public StringState getSearchState() {
		return this.searchState;
	}

	@SuppressWarnings( "rawtypes" )
	public ListSelectionState<SearchObject> getSearchResults() {
		return this.searchResults;
	}

	public FindReferencesTreeState getReferenceResults() {
		return this.referenceTree;
	}

	public KeyListener getKeyListener() {
		return this.keyListener;
	}

	public TreeExpansionListener getTreeExpansionListener() {
		return treeListener;
	}

	public ActionOperation getHowToAddOperation() {
		return this.howToAddOperation;
	}
}
