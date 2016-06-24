/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.croquet.models.project.find.croquet.views;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

import org.alice.ide.croquet.models.project.find.core.SearchResult;
import org.alice.ide.croquet.models.project.find.croquet.AbstractFindComposite;
import org.alice.ide.croquet.models.project.find.croquet.tree.FindReferencesTreeState;
import org.alice.ide.croquet.models.project.find.croquet.tree.FindReferencesTreeState.TwoDimensionalTreeCoordinate;
import org.alice.ide.croquet.models.project.find.croquet.tree.nodes.SearchTreeNode;
import org.alice.ide.croquet.models.project.find.croquet.views.renderers.SearchReferencesTreeCellRenderer;
import org.alice.ide.croquet.models.project.find.croquet.views.renderers.SearchResultListCellRenderer;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.GridPanel;
import org.lgna.croquet.views.List;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.TextField;
import org.lgna.croquet.views.Tree;

import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public class FindView extends BorderPanel {

	private final TextField searchBox;
	private InputMap inputMap;
	private final Object left;
	private final Object right;
	private final Tree<SearchTreeNode> referencesTreeList;
	private final List<SearchResult> searchResultsList;
	private final Map<SearchResult, Map<Integer, Boolean>> searchResultToExpandParentsMap = Maps.newHashMap();
	private final Map<SearchResult, TwoDimensionalTreeCoordinate> searchResultToLastTreeCoordinatesMap = Maps.newHashMap();
	private final FindReferencesTreeState referenceResults;
	private final org.lgna.croquet.RefreshableDataSingleSelectListState<SearchResult> searchResults;
	boolean listIsSelected = true;

	public FindView( AbstractFindComposite composite ) {
		super( composite );
		referenceResults = getComposite().getReferenceResults();
		searchResults = getComposite().getSearchResults();
		searchBox = composite.getSearchState().createTextField();
		inputMap = searchBox.getAwtComponent().getInputMap();
		left = inputMap.get( KeyStroke.getKeyStroke( "LEFT" ) );
		right = inputMap.get( KeyStroke.getKeyStroke( "RIGHT" ) );
		this.addPageStartComponent( searchBox );
		GridPanel panel = GridPanel.createGridPane( 1, 2 );
		panel.setPreferredSize( edu.cmu.cs.dennisc.java.awt.DimensionUtilities.createWiderGoldenRatioSizeFromHeight( 250 ) );
		searchResultsList = composite.getSearchResults().createList();
		referencesTreeList = referenceResults.createTree();
		referencesTreeList.setRootVisible( false );
		searchResultsList.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
		referencesTreeList.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
		BorderPanel bPanel = new BorderPanel();
		bPanel.addCenterComponent( new ScrollPane( searchResultsList ) );
		//		bPanel.addPageEndComponent( composite.getHowToAddOperation().createButton() );
		panel.addComponent( bPanel );
		searchResultsList.setCellRenderer( new SearchResultListCellRenderer( composite ) );
		composite.getSearchResults().addNewSchoolValueListener( searchResultsValueListener );
		referencesTreeList.setCellRenderer( new SearchReferencesTreeCellRenderer() );
		panel.addComponent( new ScrollPane( referencesTreeList ) );
		this.addCenterComponent( panel );
		searchResultsList.getAwtComponent().setFocusable( false );
		referencesTreeList.getAwtComponent().setFocusable( false );
	}

	private final ValueListener<SearchResult> searchResultsValueListener = new ValueListener<SearchResult>() {

		@Override
		public void valueChanged( ValueEvent<SearchResult> e ) {
			listIsSelected = true;
			searchResultsList.ensureIndexIsVisible( searchResultsList.getAwtComponent().getSelectedIndex() );
			Map<Integer, Boolean> innerMap = searchResultToExpandParentsMap.get( searchResults.getValue() );
			if( innerMap != null ) {
				for( Integer i : innerMap.keySet() ) {
					if( innerMap.get( i ) ) {
						getTree().expandNode( referenceResults.selectAtCoordinates( i, -1 ) );
					}
				}
			}
		}
	};

	private ValueListener<SearchTreeNode> referenceTreeListener = new ValueListener<SearchTreeNode>() {

		@Override
		public void valueChanged( ValueEvent<SearchTreeNode> e ) {
			if( e.getNextValue() != null ) {
				searchResultToLastTreeCoordinatesMap.put( searchResults.getValue(), referenceResults.getSelectedCoordinates() );
				referencesTreeList.scrollPathToVisible( referencesTreeList.getAwtComponent().getSelectionPath() );
			}
		}
	};

	private final TreeExpansionListener treeListener = new TreeExpansionListener() {

		@Override
		public void treeExpanded( TreeExpansionEvent event ) {
			org.lgna.croquet.RefreshableDataSingleSelectListState<SearchResult> searchResults = getComposite().getSearchResults();
			TreePath path = event.getPath();
			if( searchResultToExpandParentsMap.get( searchResults.getValue() ) == null ) {
				searchResultToExpandParentsMap.put( searchResults.getValue(), new HashMap<Integer, Boolean>() );
			}
			searchResultToExpandParentsMap.get( searchResults.getValue() ).put( ( (SearchTreeNode)path.getLastPathComponent() ).getLocationAmongstSiblings(), true );
		}

		@Override
		public void treeCollapsed( TreeExpansionEvent event ) {
			org.lgna.croquet.RefreshableDataSingleSelectListState<SearchResult> searchResults = getComposite().getSearchResults();
			TreePath path = event.getPath();
			if( searchResultToExpandParentsMap.get( searchResults.getValue() ) == null ) {
				searchResultToExpandParentsMap.put( searchResults.getValue(), new HashMap<Integer, Boolean>() );
			}
			searchResultToExpandParentsMap.get( searchResults.getValue() ).put( ( (SearchTreeNode)path.getLastPathComponent() ).getLocationAmongstSiblings(), false );
		}
	};

	private final KeyListener keyListener = new KeyListener() {

		private boolean isListSelected() {
			return listIsSelected;
		}

		boolean isTreeSelected() {
			return !listIsSelected;
		}

		private void setListSelected() {
			listIsSelected = true;
		}

		private void setTreeSelected() {
			listIsSelected = false;
		}

		@Override
		public void keyTyped( KeyEvent e ) {
		}

		@Override
		public void keyReleased( KeyEvent e ) {
		}

		@Override
		public void keyPressed( KeyEvent e ) {
			int keyCode = e.getKeyCode();

			java.awt.ComponentOrientation componentOrientation = e.getComponent().getComponentOrientation();

			final int LEADING_KEY_CODE;
			final int TRAILING_KEY_CODE;
			if( componentOrientation.isLeftToRight() ) {
				LEADING_KEY_CODE = KeyEvent.VK_LEFT;
				TRAILING_KEY_CODE = KeyEvent.VK_RIGHT;
			} else {
				LEADING_KEY_CODE = KeyEvent.VK_RIGHT;
				TRAILING_KEY_CODE = KeyEvent.VK_LEFT;
			}

			if( keyCode == KeyEvent.VK_UP ) {
				if( isListSelected() ) {
					if( searchResults.getValue() != null ) {
						searchResults.setSelectedIndex( searchResults.getSelectedIndex() - 1 );
						if( searchResults.getValue() == null ) {
							enableLeftAndRight();
						}
					}
				} else if( isTreeSelected() ) {
					referenceResults.moveSelectedUpOne();
				}
			} else if( keyCode == KeyEvent.VK_DOWN ) {
				if( isListSelected() ) {
					if( searchResults.getItemCount() != ( searchResults.getSelectedIndex() + 1 ) ) {
						disableLeftAndRight();
						searchResults.setSelectedIndex( searchResults.getSelectedIndex() + 1 );
						//						Map<Integer, Boolean> innerMap = searchResultToExpandParentsMap.get( searchResults.getValue() );
						//						if( innerMap != null ) {
						//							for( Integer i : innerMap.keySet() ) {
						//								if( innerMap.get( i ) ) {
						//									getTree().expandNode( referenceResults.selectAtCoordinates( i, -1 ) );
						//								}
						//							}
						//						}
					}
				} else if( isTreeSelected() ) {
					referenceResults.moveSelectedDownOne();
					//					searchResultToLastTreeCoordinatesMap.put( searchResults.getValue(), referenceResults.getSelectedCoordinates() );
				}
			} else if( keyCode == LEADING_KEY_CODE ) {
				if( isTreeSelected() ) {
					setListSelected();
					//					searchResultToLastTreeCoordinatesMap.put( searchResults.getValue(), referenceTreeState.getSelectedCoordinates() );
					referenceResults.setValueTransactionlessly( null );
				}
			} else if( keyCode == TRAILING_KEY_CODE ) {
				if( isListSelected() ) {
					if( referenceResults.isEmpty() ) {
						setTreeSelected();
						TwoDimensionalTreeCoordinate pair = searchResultToLastTreeCoordinatesMap.get( searchResults.getValue() );
						if( pair != null ) {
							referenceResults.setValueTransactionlessly( referenceResults.selectAtCoordinates( pair.getA(), pair.getB() ) );
						} else {
							referenceResults.setValueTransactionlessly( referenceResults.getTopValue() );
						}
					}
				}
			}
		}
	};

	@Override
	public AbstractFindComposite getComposite() {
		return (AbstractFindComposite)super.getComposite();
	}

	public void enableLeftAndRight() {
		inputMap.put( KeyStroke.getKeyStroke( "LEFT" ), left );
		inputMap.put( KeyStroke.getKeyStroke( "RIGHT" ), right );
	}

	public void disableLeftAndRight() {
		inputMap.put( KeyStroke.getKeyStroke( "LEFT" ), "NONE" );
		inputMap.put( KeyStroke.getKeyStroke( "RIGHT" ), "NONE" );
	}

	public Tree<SearchTreeNode> getTree() {
		return referencesTreeList;
	}

	@Override
	protected void handleDisplayable() {
		referenceResults.addNewSchoolValueListener( referenceTreeListener );
		searchBox.addKeyListener( keyListener );
		referencesTreeList.getAwtComponent().addTreeExpansionListener( treeListener );
		super.handleDisplayable();
	}

	@Override
	protected void handleUndisplayable() {
		super.handleUndisplayable();
		referenceResults.addNewSchoolValueListener( referenceTreeListener );
		searchBox.addKeyListener( keyListener );
		referencesTreeList.getAwtComponent().addTreeExpansionListener( treeListener );
	}
}
