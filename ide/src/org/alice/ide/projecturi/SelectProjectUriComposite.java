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

package org.alice.ide.projecturi;

/**
 * @author Dennis Cosgrove
 */
public final class SelectProjectUriComposite extends org.lgna.croquet.ValueCreatorInputDialogCoreComposite<org.lgna.croquet.components.Panel, UriProjectPair> {

	private final ErrorStatus noSelectionError = this.createErrorStatus( this.createKey( "noSelectionError" ) );
	private final TemplatesTab templatesTab = new TemplatesTab();
	private final MyProjectsTab myProjectsTab = new MyProjectsTab();
	private final RecentProjectsTab recentProjectsTab = new RecentProjectsTab();
	private final FileSystemTab fileSystemTab = new FileSystemTab();
	private final org.lgna.croquet.TabSelectionState<SelectUriTab> tabState = this.createTabSelectionState( this.createKey( "tabState" ), SelectUriTab.class, -1, this.templatesTab, this.myProjectsTab, this.recentProjectsTab, this.fileSystemTab );

	private final class SelectedUriMetaState extends org.lgna.croquet.MetaState<java.net.URI> {
		@Override
		public java.net.URI getValue() {
			SelectUriTab tab = tabState.getValue();
			if( tab != null ) {
				return tab.getSelectedUri();
			} else {
				return null;
			}
		}
	}

	private final SelectedUriMetaState metaState = new SelectedUriMetaState();
	private final edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<java.net.URI, UriProjectPair> mapUriToUriProjectPair = edu.cmu.cs.dennisc.java.util.Collections.newInitializingIfAbsentHashMap();

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

	public org.lgna.croquet.TabSelectionState<SelectUriTab> getTabState() {
		return this.tabState;
	}

	public org.lgna.croquet.Composite<?> getSideSubComposite() {
		return this.sideSubComposite;
	}

	public org.lgna.croquet.MetaState<java.net.URI> getMetaState() {
		return this.metaState;
	}

	@Override
	protected UriProjectPair createValue() {
		java.net.URI uri = this.metaState.getValue();
		if( uri != null ) {
			return this.mapUriToUriProjectPair.getInitializingIfAbsent( uri, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<java.net.URI, UriProjectPair>() {
				public org.alice.ide.projecturi.UriProjectPair initialize( java.net.URI key ) {
					return new UriProjectPair( key );
				}
			} );
		} else {
			return null;
		}
	}

	@Override
	protected org.lgna.croquet.components.Panel createView() {
		return new org.alice.ide.projecturi.views.SelectProjectUriWithPreviewPanel( this );
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		java.net.URI uri = this.metaState.getValue();
		if( uri != null ) {
			return IS_GOOD_TO_GO_STATUS;
		} else {
			return this.noSelectionError;
		}
	}

	@Override
	protected Integer getWiderGoldenRatioSideFromWidth() {
		if( this.sideSubComposite != null ) {
			return 960;
		} else {
			return 620;
		}
	}

	public void selectAppropriateTab( boolean isNew ) {
		SelectUriTab tab;
		if( isNew ) {
			tab = this.templatesTab;
		} else {
			tab = this.myProjectsTab; // todo: recentTab?
		}
		this.tabState.setValueTransactionlessly( tab );
		org.lgna.croquet.components.ComponentManager.revalidateAndRepaintAllComponents( this.tabState );
	}

	private void refresh() {
		for( SelectUriTab contentTab : this.tabState ) {
			contentTab.refresh();
		}
	}

	private final org.lgna.croquet.MetaState.MetaStateValueListener<java.net.URI> metaUriListener = new org.lgna.croquet.MetaState.MetaStateValueListener<java.net.URI>() {
		public void metaStateValueChanged( java.net.URI prevValue, java.net.URI nextValue ) {
			handleMetaStateValueChanged( nextValue );
		}
	};

	private void handleMetaStateValueChanged( java.net.URI uri ) {
		if( this.sideSubComposite != null ) {
			this.sideSubComposite.handleMetaStateValueChanged( uri );
		}
	}

	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		this.refresh();
		this.mapUriToUriProjectPair.clear();
		if( this.sideSubComposite != null ) {
			this.metaState.activate( completionStep );
			this.metaState.addMetaStateValueListener( this.metaUriListener );
		}
		super.handlePreShowDialog( completionStep );
	}

	@Override
	protected void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		if( this.sideSubComposite != null ) {
			this.metaState.removeMetaStateValueListener( this.metaUriListener );
		}
		this.metaState.deactivate( completionStep );
		super.handlePostHideDialog( completionStep );
	}
}
