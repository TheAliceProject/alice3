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
package org.alice.ide.croquet.models.project.find.croquet;

import java.util.List;
import java.util.UUID;

import org.alice.ide.IDE;
import org.alice.ide.croquet.models.project.find.core.FindContentManager;
import org.alice.ide.croquet.models.project.find.core.SearchResult;
import org.alice.ide.croquet.models.project.find.core.criteria.AcceptIfNotGenerated;
import org.alice.ide.croquet.models.project.find.croquet.tree.FindReferencesTreeState;
import org.alice.ide.croquet.models.project.find.croquet.tree.nodes.SearchTreeNode;
import org.alice.ide.croquet.models.project.find.croquet.views.FindView;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.StringState;
import org.lgna.croquet.codecs.DefaultItemCodec;
import org.lgna.croquet.data.RefreshableListData;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserType;

import edu.cmu.cs.dennisc.pattern.Criterion;

/**
 * @author Matt May
 */
public abstract class AbstractFindComposite extends org.lgna.croquet.FrameCompositeWithInternalIsShowingState<FindView> {
	public static final org.lgna.croquet.Group FIND_COMPOSITE_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "609c0bf5-73c3-4987-a2b5-8225c19f7886" ) );

	private final FindContentManager manager = new FindContentManager();
	private final StringState searchState = createStringState( "searchState" );
	private final BooleanState isNavigationEnabledState = createBooleanState( "isNavigationEnabledState", true );
	private final FindReferencesTreeState referenceTreeState = new FindReferencesTreeState();
	private boolean isActive;
	private boolean showGenerated = false;

	protected AbstractFindComposite( UUID migrationID ) {
		super( migrationID, FIND_COMPOSITE_GROUP );
	}

	private final ValueListener<String> searchStateListener = new ValueListener<String>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<String> e ) {
			data.refresh();
			referenceTreeState.refreshWith( searchResultsState.getValue() );
			if( data.getItemCount() == 1 ) {
				searchResultsState.setSelectedIndex( 0 );
			}
			getView().revalidateAndRepaint();
		}
	};

	private final org.alice.ide.project.events.ProjectChangeOfInterestListener projectChangeOfInterestListener = new org.alice.ide.project.events.ProjectChangeOfInterestListener() {
		@Override
		public void projectChanged() {
			refresh();
		}
	};

	private final ValueListener<org.alice.ide.ProjectDocument> projectDocumentChangeListener = new ValueListener<org.alice.ide.ProjectDocument>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.ProjectDocument> e ) {
			refresh();
		}
	};

	private final ValueListener<SearchResult> searchResultsListener = new ValueListener<SearchResult>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.croquet.models.project.find.core.SearchResult> e ) {
			referenceTreeState.refreshWith( e.getNextValue() );
		}
	};

	private final ValueListener<SearchTreeNode> referenceTreeListener = new ValueListener<SearchTreeNode>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<SearchTreeNode> e ) {
			SearchTreeNode nextValue = e.getNextValue();
			if( isNavigationEnabledState.getValue() && ( nextValue != null ) ) {
				if( nextValue.getValue() instanceof Expression ) {
					IDE.getActiveInstance().getDocumentFrame().selectDeclarationComposite( org.alice.ide.declarationseditor.DeclarationComposite.getInstance( ( (Expression)nextValue.getValue() ).getFirstAncestorAssignableTo( UserMethod.class ) ) );
					searchResultsState.getValue().stencilHighlightForReference( (Expression)nextValue.getValue() );
				} else {
					IDE.getActiveInstance().getDocumentFrame().selectDeclarationComposite( org.alice.ide.declarationseditor.DeclarationComposite.getInstance( ( (Expression)nextValue.getChildren().get( 0 ).getValue() ).getFirstAncestorAssignableTo( UserMethod.class ) ) );
					IDE.getActiveInstance().getDocumentFrame().getHighlightStencil().hideIfNecessary();
				}
			}
		}
	};

	private static final org.lgna.croquet.ItemCodec<SearchResult> SEARCH_RESULT_CODEC = DefaultItemCodec.createInstance( SearchResult.class );
	private final RefreshableListData<SearchResult> data = new RefreshableListData<SearchResult>( SEARCH_RESULT_CODEC ) {

		@Override
		protected List createValues() {
			return setSearchResults();
		}

	};
	private final ActionOperation howToAddOperation = createActionOperation( "howToAdd", new Action() {

		@Override
		public Edit perform( CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws CancelException {
			//needs work
			if( searchResultsState.getValue() != null ) {
				AbstractDeclaration searchObject = searchResultsState.getValue().getDeclaration();
				AbstractMethod abstractMethod = searchObject.getFirstAncestorAssignableTo( AbstractMethod.class );
				if( searchObject instanceof AbstractMethod ) {
					AbstractType<?, ?, ?> declaringType = ( (AbstractMethod)searchObject ).getDeclaringType();
					IDE.getActiveInstance().getMethodInvocations( (AbstractMethod)searchObject );
				}
			}
			return null;
		}

	} );
	private final org.lgna.croquet.RefreshableDataSingleSelectListState<SearchResult> searchResultsState = createRefreshableListState( "searchResultsState", data, -1 );

	private void refresh() {
		if( this.isActive ) {
			manager.refresh( (UserType)IDE.getActiveInstance().getProgramType().fields.get( 0 ).getValueType(), getCriteria() );
			data.refresh();
		}
	}

	protected List<SearchResult> setSearchResults() {
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
		searchState.addNewSchoolValueListener( searchStateListener );
		searchResultsState.addNewSchoolValueListener( searchResultsListener );
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.addProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
		org.alice.ide.project.ProjectDocumentState.getInstance().addNewSchoolValueListener( this.projectDocumentChangeListener );
		referenceTreeState.addNewSchoolValueListener( referenceTreeListener );
		this.isActive = true;
		//		manager.initialize( (UserType)IDE.getActiveInstance().getProgramType().fields.get( 0 ).getValueType(), getCriteria() );
		refresh();
	}

	@Override
	public void handlePostDeactivation() {
		this.isActive = false;
		searchState.removeNewSchoolValueListener( searchStateListener );
		searchResultsState.removeNewSchoolValueListener( searchResultsListener );
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.removeProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
		org.alice.ide.project.ProjectDocumentState.getInstance().removeNewSchoolValueListener( this.projectDocumentChangeListener );
		referenceTreeState.removeNewSchoolValueListener( referenceTreeListener );
		super.handlePostDeactivation();
	}

	public StringState getSearchState() {
		return this.searchState;
	}

	public org.lgna.croquet.RefreshableDataSingleSelectListState<SearchResult> getSearchResults() {
		return this.searchResultsState;
	}

	public FindReferencesTreeState getReferenceResults() {
		return this.referenceTreeState;
	}

	public ActionOperation getHowToAddOperation() {
		return this.howToAddOperation;
	}

	public FindContentManager getManager() {
		return this.manager;
	}

	public List<Criterion> getCriteria() {
		List<Criterion> rv = edu.cmu.cs.dennisc.java.util.Lists.newArrayList();
		if( !showGenerated ) {
			rv.add( AcceptIfNotGenerated.getInstance() );
		}
		return rv;
	}
}
