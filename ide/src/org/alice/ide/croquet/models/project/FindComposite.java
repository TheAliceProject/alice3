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
package org.alice.ide.croquet.models.project;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.alice.ide.IDE;
import org.alice.ide.croquet.models.project.TreeNodesAndManagers.FindContentManager;
import org.alice.ide.croquet.models.project.TreeNodesAndManagers.SearchObject;
import org.alice.ide.croquet.models.project.views.FindView;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.FrameComposite;
import org.lgna.croquet.Group;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.StringState;
import org.lgna.croquet.data.RefreshableListData;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserType;
import org.lgna.story.ImplementationAccessor;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class FindComposite extends FrameComposite<FindView> {

	private FindContentManager manager = new FindContentManager();
	private StringState searchState = createStringState( createKey( "searchState" ) );
	private BooleanState shouldINavigate = createBooleanState( createKey( "shouldNav" ), true );
	@SuppressWarnings( "rawtypes" )
	ItemCodec<SearchObject> codec1 = new ItemCodec<SearchObject>() {

		public Class<SearchObject> getValueClass() {
			return SearchObject.class;
		}

		public SearchObject<?> decodeValue( BinaryDecoder binaryDecoder ) {
			return null;
		}

		public void encodeValue( BinaryEncoder binaryEncoder, SearchObject value ) {
		}

		public void appendRepresentation( StringBuilder sb, SearchObject value ) {
			sb.append( value );
		}
	};
	ItemCodec<Expression> codec2 = new ItemCodec<Expression>() {

		public Class<Expression> getValueClass() {
			return Expression.class;
		}

		public Expression decodeValue( BinaryDecoder binaryDecoder ) {
			return null;
		}

		public void encodeValue( BinaryEncoder binaryEncoder, Expression value ) {
		}

		public void appendRepresentation( StringBuilder sb, Expression value ) {
			sb.append( value );
		}
	};
	@SuppressWarnings( "rawtypes" )
	private RefreshableListData<SearchObject> data = new RefreshableListData<SearchObject>( codec1 ) {

		@SuppressWarnings( "unchecked" )
		@Override
		protected List createValues() {
			return manager.getResultsForString( searchState.getValue() );
		}
	};
	private RefreshableListData<Expression> referencesData = new RefreshableListData<Expression>( codec2 ) {

		@Override
		protected List<Expression> createValues() {
			return searchResults.getValue() != null ? searchResults.getValue().getReferences() : Collections.newArrayList();
		}
	};

	ValueListener<String> searchStateListener = new ValueListener<String>() {

		public void changing( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
		}

		public void changed( State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
			System.out.println( "CHANGE" );
			data.refresh();
			referencesData.refresh();
			if( data.getItemCount() == 1 ) {
				searchResults.setSelectedIndex( 0 );
			}
		}
	};
	@SuppressWarnings( "rawtypes" )
	private ListSelectionState<SearchObject> searchResults = createListSelectionState( createKey( "searchResultsList" ),
			data, -1 );
	private ListSelectionState<Expression> referenceResults = createListSelectionState( createKey( "searchResultsList" ),
			referencesData, -1 );

	@SuppressWarnings( "rawtypes" )
	public FindComposite() {
		this( java.util.UUID.fromString( "c454dba4-80ac-4873-b899-67ea3cd726e9" ), null );
	}

	protected FindComposite( UUID fromString, Group group ) {
		super( fromString, group );
		searchState.addValueListener( searchStateListener );
		searchResults.addValueListener( new ValueListener<SearchObject>() {

			public void changing( State<SearchObject> state, SearchObject prevValue, SearchObject nextValue, boolean isAdjusting ) {
			}

			public void changed( State<SearchObject> state, SearchObject prevValue, SearchObject nextValue, boolean isAdjusting ) {
				referencesData.refresh();
			}
		} );
		referenceResults.addValueListener( new ValueListener<Expression>() {

			public void changing( State<Expression> state, Expression prevValue, Expression nextValue, boolean isAdjusting ) {
			}

			public void changed( State<Expression> state, Expression prevValue, Expression nextValue, boolean isAdjusting ) {
				if( shouldINavigate.getValue() && ( nextValue != null ) ) {
					IDE.getActiveInstance().selectDeclarationComposite( org.alice.ide.declarationseditor.DeclarationComposite.getInstance( nextValue.getFirstAncestorAssignableTo( UserMethod.class ) ) );
					searchResults.getValue().stencilHighlightForReference( nextValue );
				}
			}
		} );
	}

	@Override
	protected FindView createView() {
		return new FindView( this );
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		manager.initialize( (UserType)IDE.getActiveInstance().getProgramType().fields.get( 0 ).getValueType() );
	}

	public StringState getSearchState() {
		return this.searchState;
	}

	@SuppressWarnings( "rawtypes" )
	public ListSelectionState<SearchObject> getSearchResults() {
		return this.searchResults;
	}

	public ListSelectionState<Expression> getReferenceResults() {
		return this.referenceResults;
	}

	private KeyListener keyListener = new KeyListener() {

		ListSelectionState selected = searchResults;
		Map<SearchObject<?>, Integer> map = Collections.newHashMap();

		public void keyTyped( KeyEvent e ) {
		}

		public void keyReleased( KeyEvent e ) {
		}

		public void keyPressed( KeyEvent e ) {
			if( ImplementationAccessor.getKeyFromKeyCode( e.getKeyCode() ) == org.lgna.story.Key.UP ) {
				if( selected == searchResults ) {
					if( searchResults.getValue() != null ) {
						searchResults.setSelectedIndex( searchResults.getSelectedIndex() - 1 );
						if( searchResults.getValue() == null ) {
							getView().enableLeftAndRight();
						}
					}
				} else if( selected == referenceResults ) {
					if( referenceResults.getSelectedIndex() > 0 ) {
						referenceResults.setSelectedIndex( referenceResults.getSelectedIndex() - 1 );
					}
				}
			} else if( ImplementationAccessor.getKeyFromKeyCode( e.getKeyCode() ) == org.lgna.story.Key.DOWN ) {
				if( selected == searchResults ) {
					if( searchResults.getItemCount() != ( searchResults.getSelectedIndex() + 1 ) ) {
						getView().disableLeftAndRight();
						searchResults.setSelectedIndex( searchResults.getSelectedIndex() + 1 );
					}
				} else if( selected == referenceResults ) {
					if( referenceResults.getItemCount() != ( referenceResults.getSelectedIndex() + 1 ) ) {
						referenceResults.setSelectedIndex( referenceResults.getSelectedIndex() + 1 );
					}
				}
			} else if( ImplementationAccessor.getKeyFromKeyCode( e.getKeyCode() ) == org.lgna.story.Key.LEFT ) {
				selected = searchResults;
				map.put( searchResults.getValue(), referenceResults.getSelectedIndex() == -1 ? 0 : referenceResults.getSelectedIndex() );
				referenceResults.setSelectedIndex( -1 );
			} else if( ImplementationAccessor.getKeyFromKeyCode( e.getKeyCode() ) == org.lgna.story.Key.RIGHT ) {
				if( referenceResults.getItemCount() > 0 ) {
					selected = referenceResults;
					if( ( referenceResults.getValue() == null ) && ( referenceResults.getItemCount() > 0 ) ) {
						if( map.get( searchResults.getValue() ) != null ) {
							referenceResults.setSelectedIndex( map.get( searchResults.getValue() ) );
						} else {
							referenceResults.setSelectedIndex( 0 );
						}
					}
				}
			}
		}
	};

	public KeyListener getKeyListener() {
		return this.keyListener;
	}
}
