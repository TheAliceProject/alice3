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
package org.alice.stageide.gallerybrowser.search.croquet;

import org.alice.stageide.gallerybrowser.GalleryTab;

/**
 * @author Dennis Cosgrove
 */
public class SearchTab extends GalleryTab {
	public SearchTab() {
		super( java.util.UUID.fromString( "4e3e7dc2-c8ed-4e8c-9028-9493a19ba50d" ) );
	}

	public org.lgna.croquet.StringState getFilterState() {
		return this.filterState;
	}

	public org.lgna.croquet.PlainStringValue getNoMatchesLabel() {
		return this.noMatchesLabel;
	}

	public org.lgna.croquet.PlainStringValue getNoEntryLabel() {
		return this.noEntryLabel;
	}

	@Override
	protected org.alice.stageide.gallerybrowser.search.croquet.views.SearchTabView createView() {
		return new org.alice.stageide.gallerybrowser.search.croquet.views.SearchTabView( this );
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.getFilterState().addAndInvokeNewSchoolValueListener( this.filterListener );
	}

	@Override
	public void handlePostDeactivation() {
		this.getFilterState().removeNewSchoolValueListener( this.filterListener );
		this.cancelWorkerIfNecessary();
		super.handlePostDeactivation();
	}

	private void cancelWorkerIfNecessary() {
		if( this.worker != null ) {
			if( this.worker.isDone() ) {
				//pass
			} else {
				this.worker.cancel( false );
			}
			this.worker = null;
		}
	}

	private void handleFilterChanged( String filter ) {
		this.cancelWorkerIfNecessary();
		org.alice.stageide.gallerybrowser.search.croquet.views.SearchTabView view = (org.alice.stageide.gallerybrowser.search.croquet.views.SearchTabView)this.getView();
		synchronized( view.getTreeLock() ) {
			view.removeAllGalleryDragComponents();
		}
		this.worker = new org.alice.stageide.gallerybrowser.search.core.SearchGalleryWorker( filter, view );
		this.worker.execute();
	}

	private org.alice.stageide.gallerybrowser.search.core.SearchGalleryWorker worker;
	private final org.lgna.croquet.StringState filterState = this.createStringState( "filterState" );
	private final org.lgna.croquet.PlainStringValue noMatchesLabel = this.createStringValue( "noMatchesLabel" );
	private final org.lgna.croquet.PlainStringValue noEntryLabel = this.createStringValue( "noEntryLabel" );
	private final org.lgna.croquet.event.ValueListener<String> filterListener = new org.lgna.croquet.event.ValueListener<String>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<String> e ) {
			SearchTab.this.handleFilterChanged( e.getNextValue() );
		}
	};
}
