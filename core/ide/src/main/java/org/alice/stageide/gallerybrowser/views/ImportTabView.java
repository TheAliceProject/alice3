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
package org.alice.stageide.gallerybrowser.views;

/**
 * @author Dennis Cosgrove
 */
public class ImportTabView extends GalleryTabView {
	private final java.util.Map<java.net.URI, org.alice.ide.croquet.components.gallerybrowser.GalleryDragComponent> mapUriToDragComponent = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public synchronized org.alice.ide.croquet.components.gallerybrowser.GalleryDragComponent getGalleryDragComponent( java.net.URI uri ) {
		org.alice.ide.croquet.components.gallerybrowser.GalleryDragComponent rv = this.mapUriToDragComponent.get( uri );
		if( rv != null ) {
			//pass
		} else {
			org.alice.stageide.gallerybrowser.uri.UriGalleryDragModel dragModel = org.alice.stageide.gallerybrowser.uri.UriGalleryDragModel.getInstance( uri );
			rv = new org.alice.ide.croquet.components.gallerybrowser.GalleryDragComponent( dragModel );
			this.mapUriToDragComponent.put( uri, rv );
		}
		return rv;
	}

	private class DragComponentsView extends org.lgna.croquet.views.LineAxisPanel {
		@Override
		protected void internalRefresh() {
			super.internalRefresh();
			this.removeAllComponents();
			java.io.File directory = getDirectory();
			if( directory != null ) {
				java.io.File[] files = edu.cmu.cs.dennisc.java.io.FileUtilities.listFiles( directory, org.lgna.project.io.IoUtilities.TYPE_EXTENSION );
				if( ( files != null ) && ( files.length > 0 ) ) {
					for( java.io.File file : files ) {
						this.addComponent( getGalleryDragComponent( file.toURI() ) );
					}
				} else {
					this.addComponent( noFilesLabel );
				}
			} else {
				this.addComponent( notDirectoryLabel );
			}
		}
	}

	private final org.lgna.croquet.event.ValueListener<String> directoryListener = new org.lgna.croquet.event.ValueListener<String>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<String> e ) {
			handleDirectoryChanged();
		}
	};
	private final org.lgna.croquet.views.AbstractLabel notDirectoryLabel = new org.lgna.croquet.views.Label( "", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
	private final org.lgna.croquet.views.AbstractLabel noFilesLabel = new org.lgna.croquet.views.Label( "", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
	private final DragComponentsView dragComponentsView = new DragComponentsView();

	public ImportTabView( org.alice.stageide.gallerybrowser.ImportTab composite ) {
		super( composite );
		//this.notDirectoryLabel = composite.getNotDirectoryText().createLabel( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
		//this.noFilesLabel = composite.getNoFilesText().createLabel( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );

		org.lgna.croquet.views.MigPanel panel = new org.lgna.croquet.views.MigPanel( null, "insets 0, fill", "[shrink]4[grow]4[shrink]16[shrink]" );
		panel.addComponent( composite.getDirectoryState().getSidekickLabel().createLabel() );
		panel.addComponent( composite.getDirectoryState().createTextField(), "growx 100" );
		panel.addComponent( composite.getBrowseOperation().createButton() );
		panel.addComponent( composite.getRestoreToDefaultOperation().createButton(), "wrap" );
		org.lgna.croquet.views.ScrollPane scrollPane = createGalleryScrollPane( this.dragComponentsView );
		panel.addComponent( scrollPane, "span 4, wrap" );
		this.addCenterComponent( panel );
		this.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
		this.dragComponentsView.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
		this.notDirectoryLabel.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
		this.noFilesLabel.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
	}

	private void handleDirectoryChanged() {
		this.dragComponentsView.refreshLater();
		org.alice.stageide.gallerybrowser.ImportTab importTab = (org.alice.stageide.gallerybrowser.ImportTab)this.getComposite();
		importTab.getRestoreToDefaultOperation().setEnabled( importTab.isDirectoryStateSetToDefault() == false );

		String path = importTab.getDirectoryState().getValue();
		this.notDirectoryLabel.setText( importTab.getNotDirectoryText().getText().replace( "</directory/>", path ) );
		this.noFilesLabel.setText( importTab.getNoFilesText().getText().replace( "</directory/>", path ) );
	}

	private java.io.File getDirectory() {
		org.alice.stageide.gallerybrowser.ImportTab importTab = (org.alice.stageide.gallerybrowser.ImportTab)this.getComposite();
		String path = importTab.getDirectoryState().getValue();
		java.io.File file = new java.io.File( path );
		if( file.isDirectory() ) {
			return file;
		} else {
			return null;
		}
	}

	@Override
	public void handleCompositePreActivation() {
		super.handleCompositePreActivation();
		org.alice.stageide.gallerybrowser.ImportTab importTab = (org.alice.stageide.gallerybrowser.ImportTab)this.getComposite();
		importTab.getDirectoryState().addAndInvokeNewSchoolValueListener( this.directoryListener );
	}

	@Override
	public void handleCompositePostDeactivation() {
		org.alice.stageide.gallerybrowser.ImportTab importTab = (org.alice.stageide.gallerybrowser.ImportTab)this.getComposite();
		importTab.getDirectoryState().removeNewSchoolValueListener( this.directoryListener );
		super.handleCompositePostDeactivation();
	}

}
