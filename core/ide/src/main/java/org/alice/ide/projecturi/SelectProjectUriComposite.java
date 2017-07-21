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

package org.alice.ide.projecturi;

/**
 * @author Dennis Cosgrove
 */
public final class SelectProjectUriComposite extends org.lgna.croquet.SingleValueCreatorInputDialogCoreComposite<org.lgna.croquet.views.Panel, org.alice.ide.uricontent.UriProjectLoader> {
	private final ErrorStatus noSelectionError = this.createErrorStatus( "noSelectionError" );
	private final StartersTab startersTab = new StartersTab();
	private final TemplatesTab templatesTab = new TemplatesTab();
	private final MyProjectsTab myProjectsTab = new MyProjectsTab();
	private final RecentProjectsTab recentProjectsTab = new RecentProjectsTab();
	private final FileSystemTab fileSystemTab = new FileSystemTab();
	private final org.lgna.croquet.ImmutableDataTabState<SelectUriTab> tabState = this.createImmutableTabState(
			"tabState",
			-1,
			SelectUriTab.class,
			this.templatesTab,
			this.startersTab,
			this.myProjectsTab,
			this.recentProjectsTab,
			this.fileSystemTab );

	private final class SelectedUriMetaState extends org.lgna.croquet.meta.TransactionHistoryTrackingMetaState<org.alice.ide.uricontent.UriProjectLoader> {
		@Override
		public org.alice.ide.uricontent.UriProjectLoader getValue() {
			SelectUriTab tab = tabState.getValue();
			if( tab != null ) {
				return tab.getSelectedUri();
			} else {
				return null;
			}
		}
	}

	private final SelectedUriMetaState metaState = new SelectedUriMetaState();

	private final ProjectSideComposite sideSubComposite;

	private static class SingletonHolder {
		private static SelectProjectUriComposite instance = new SelectProjectUriComposite();
	}

	public static SelectProjectUriComposite getInstance() {
		return SingletonHolder.instance;
	}

	private SelectProjectUriComposite() {
		super( java.util.UUID.fromString( "3b9ec3fb-3fe5-485c-ac2a-688a5468b0b9" ) );
		final boolean IS_SIDE_SUB_COMPOSITE_READY_FOR_PRIME_TIME = false;
		if( IS_SIDE_SUB_COMPOSITE_READY_FOR_PRIME_TIME ) {
			this.sideSubComposite = new ProjectSideComposite();
			this.registerSubComposite( this.sideSubComposite );
		} else {
			this.sideSubComposite = null;
		}
	}

	public org.lgna.croquet.ImmutableDataTabState<SelectUriTab> getTabState() {
		return this.tabState;
	}

	public org.lgna.croquet.Composite<?> getSideSubComposite() {
		return this.sideSubComposite;
	}

	public org.lgna.croquet.meta.MetaState<org.alice.ide.uricontent.UriProjectLoader> getMetaState() {
		return this.metaState;
	}

	@Override
	protected org.alice.ide.uricontent.UriProjectLoader createValue() {
		return this.metaState.getValue();
	}

	@Override
	protected org.lgna.croquet.views.Panel createView() {
		return new org.alice.ide.projecturi.views.SelectProjectUriView( this );
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		org.alice.ide.uricontent.UriProjectLoader uri = this.metaState.getValue();
		if( uri != null ) {
			return IS_GOOD_TO_GO_STATUS;
		} else {
			return this.noSelectionError;
		}
	}

	@Override
	protected Integer getWiderGoldenRatioSizeFromWidth() {
		if( this.sideSubComposite != null ) {
			return 960;
		} else {
			return 800;
		}
	}

	public void selectAppropriateTab( boolean isNew ) {
		SelectUriTab tab;
		if( isNew ) {
			//tab = this.startersTab != null ? this.startersTab : this.templatesTab;
			tab = this.templatesTab;
		} else {
			tab = this.myProjectsTab; // todo: recentTab?
		}
		this.tabState.setValueTransactionlessly( tab );
		org.lgna.croquet.views.ComponentManager.revalidateAndRepaintAllComponents( this.tabState );
	}

	private void refresh() {
		for( SelectUriTab contentTab : this.tabState ) {
			contentTab.refresh();
		}
	}

	private final org.lgna.croquet.event.ValueListener<org.alice.ide.uricontent.UriProjectLoader> metaUriListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.uricontent.UriProjectLoader>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.uricontent.UriProjectLoader> e ) {
			handleMetaStateValueChanged( e.getNextValue() );
		}
	};

	private void handleMetaStateValueChanged( org.alice.ide.uricontent.UriProjectLoader uri ) {
		if( this.sideSubComposite != null ) {
			this.sideSubComposite.handleMetaStateValueChanged( uri );
		}
	}

	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		this.refresh();
		if( this.sideSubComposite != null ) {
			this.metaState.pushActivation( completionStep );
			this.metaState.addValueListener( this.metaUriListener );
		}
		super.handlePreShowDialog( completionStep );
	}

	@Override
	protected void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		if( this.sideSubComposite != null ) {
			this.metaState.removeValueListener( this.metaUriListener );
			this.metaState.popActivation();
		}
		super.handlePostHideDialog( completionStep );
	}
}
