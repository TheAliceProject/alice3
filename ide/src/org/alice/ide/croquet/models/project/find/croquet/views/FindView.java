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
package org.alice.ide.croquet.models.project.find.croquet.views;

import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;

import org.alice.ide.croquet.models.project.find.core.SearchObject;
import org.alice.ide.croquet.models.project.find.core.SearchObjectNode;
import org.alice.ide.croquet.models.project.find.croquet.FindComposite;
import org.alice.ide.croquet.models.project.find.croquet.views.renderers.SearchReferencesTreeCellRenderer;
import org.alice.ide.croquet.models.project.find.croquet.views.renderers.SearchResultListCellRenderer;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.List;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.croquet.components.TextField;
import org.lgna.croquet.components.Tree;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;

import edu.cmu.cs.dennisc.math.GoldenRatio;

/**
 * @author Matt May
 */
public class FindView extends BorderPanel {

	private final TextField searchBox;
	private InputMap inputMap;
	private final Object left;
	private final Object right;
	private Tree<SearchObjectNode> referencesTreeList;
	private final List<SearchObject> searchResultsList;

	private final ValueListener<SearchObject> resultsListener = new ValueListener<SearchObject>() {
		@Override
		public void valueChanged( ValueEvent<SearchObject> e ) {
			searchResultsList.ensureIndexIsVisible( searchResultsList.getAwtComponent().getSelectedIndex() );
		}
	};

	public FindView( FindComposite composite ) {
		super( composite );
		searchBox = composite.getSearchState().createTextField();
		inputMap = searchBox.getAwtComponent().getInputMap();
		left = inputMap.get( KeyStroke.getKeyStroke( "LEFT" ) );
		right = inputMap.get( KeyStroke.getKeyStroke( "RIGHT" ) );
		this.addPageStartComponent( searchBox );
		GridPanel panel = GridPanel.createGridPane( 1, 2 );
		panel.setPreferredSize( GoldenRatio.createWiderSizeFromHeight( 250 ) );
		searchResultsList = composite.getSearchResults().createList();
		referencesTreeList = composite.getReferenceResults().createTree();
		referencesTreeList.setRootVisible( false );
		searchResultsList.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
		referencesTreeList.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
		BorderPanel bPanel = new BorderPanel();
		bPanel.addCenterComponent( new ScrollPane( searchResultsList ) );
		//		bPanel.addPageEndComponent( composite.getHowToAddOperation().createButton() );
		panel.addComponent( bPanel );
		searchResultsList.setCellRenderer( new SearchResultListCellRenderer() );
		referencesTreeList.setCellRenderer( new SearchReferencesTreeCellRenderer() );
		panel.addComponent( new ScrollPane( referencesTreeList ) );
		this.addCenterComponent( panel );
		searchBox.addKeyListener( composite.getKeyListener() );
		searchResultsList.getAwtComponent().setFocusable( false );
		referencesTreeList.getAwtComponent().setFocusable( false );
		referencesTreeList.getAwtComponent().addTreeExpansionListener( composite.getTreeExpansionListener() );
	}

	public void enableLeftAndRight() {
		inputMap.put( KeyStroke.getKeyStroke( "LEFT" ), left );
		inputMap.put( KeyStroke.getKeyStroke( "RIGHT" ), right );
	}

	public void disableLeftAndRight() {
		inputMap.put( KeyStroke.getKeyStroke( "LEFT" ), "NONE" );
		inputMap.put( KeyStroke.getKeyStroke( "RIGHT" ), "NONE" );
	}

	public Tree<SearchObjectNode> getTree() {
		return referencesTreeList;
	}

	@Override
	protected void handleDisplayable() {
		FindComposite findComposite = (FindComposite)this.getComposite();
		findComposite.getSearchResults().addNewSchoolValueListener( this.resultsListener );
		super.handleDisplayable();
	}

	@Override
	protected void handleUndisplayable() {
		super.handleUndisplayable();
		FindComposite findComposite = (FindComposite)this.getComposite();
		findComposite.getSearchResults().removeNewSchoolValueListener( this.resultsListener );
	}
}
