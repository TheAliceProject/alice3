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
package org.alice.stageide.gallerybrowser.views;

/**
 * @author Dennis Cosgrove
 */
public class ImportTabView extends GalleryTabView {
	private class DragComponentsView extends org.lgna.croquet.components.LineAxisPanel {
		@Override
		protected void internalRefresh() {
			super.internalRefresh();
			this.removeAllComponents();
			java.io.File directory = getDirectory();
			if( directory != null ) {
				java.io.File[] files = edu.cmu.cs.dennisc.java.io.FileUtilities.listFiles( directory, org.lgna.project.io.IoUtilities.TYPE_EXTENSION );
				if( ( files != null ) && ( files.length > 0 ) ) {
					for( java.io.File file : files ) {
						this.addComponent( new org.lgna.croquet.components.Label( file.getName() ) );
					}
				} else {
					this.addComponent( noFilesLabel );
				}
			} else {
				this.addComponent( notDirectoryLabel );
			}
		}
	}

	private final org.lgna.croquet.State.ValueListener<String> directoryListener = new org.lgna.croquet.State.ValueListener<String>() {
		public void changing( org.lgna.croquet.State<java.lang.String> state, java.lang.String prevValue, java.lang.String nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
			dragComponentsView.refreshLater();
		}
	};
	private final org.lgna.croquet.components.AbstractLabel notDirectoryLabel;
	private final org.lgna.croquet.components.AbstractLabel noFilesLabel;
	private final DragComponentsView dragComponentsView = new DragComponentsView();

	public ImportTabView( org.alice.stageide.gallerybrowser.ImportTab composite ) {
		super( composite );
		this.notDirectoryLabel = composite.getNotDirectoryText().createLabel();
		this.noFilesLabel = composite.getNoFilesText().createLabel();

		org.lgna.croquet.components.MigPanel panel = new org.lgna.croquet.components.MigPanel( null, "insets 0, fillx", "[shrink]4[grow]4[shrink]16[shrink]" );
		panel.addComponent( composite.getDirectoryState().getSidekickLabel().createLabel() );
		panel.addComponent( composite.getDirectoryState().createTextField(), "growx 100" );
		panel.addComponent( composite.getBrowseOperation().createButton() );
		panel.addComponent( composite.getRestoreToDefaultOperation().createButton(), "wrap" );
		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( this.dragComponentsView );
		panel.addComponent( scrollPane, "span 4, wrap" );
		this.addPageStartComponent( panel );
		this.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
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
		importTab.getDirectoryState().addAndInvokeValueListener( this.directoryListener );
	}

	@Override
	public void handleCompositePostDeactivation() {
		org.alice.stageide.gallerybrowser.ImportTab importTab = (org.alice.stageide.gallerybrowser.ImportTab)this.getComposite();
		importTab.getDirectoryState().removeValueListener( this.directoryListener );
		super.handleCompositePostDeactivation();
	}

}
