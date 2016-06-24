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
public class TreeOwningGalleryTabView extends GalleryTabView {
	private class ModelResourceDirectoryView extends org.lgna.croquet.views.TreeDirectoryViewController<org.alice.stageide.modelresource.ResourceNode> {
		public ModelResourceDirectoryView( org.lgna.croquet.SingleSelectTreeState<org.alice.stageide.modelresource.ResourceNode> model ) {
			super( model );
		}

		@Override
		protected org.lgna.croquet.views.SwingComponentView<?> getComponentFor( org.alice.stageide.modelresource.ResourceNode value ) {
			return TreeOwningGalleryTabView.this.getGalleryDragComponent( value );
		}
	}

	private final org.lgna.croquet.event.ValueListener<org.alice.stageide.modelresource.ResourceNode> treeListener = new org.lgna.croquet.event.ValueListener<org.alice.stageide.modelresource.ResourceNode>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.stageide.modelresource.ResourceNode> e ) {
			handleChanged( e.getPreviousValue(), e.getNextValue() );
		}
	};

	private final java.util.Map<org.alice.stageide.modelresource.ResourceNode, Integer> mapNodeToHorizontalScrollPosition = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final org.lgna.croquet.views.ScrollPane scrollPane;

	public TreeOwningGalleryTabView( org.alice.stageide.gallerybrowser.TreeOwningGalleryTab composite ) {
		super( composite );

		org.alice.stageide.modelresource.ResourceNodeTreeState state = composite.getResourceNodeTreeSelectionState();
		ModelResourceDirectoryView view = new ModelResourceDirectoryView( state );

		this.scrollPane = createGalleryScrollPane( view );

		final boolean IS_BREAD_CRUMB_COLOR_DESIRED_UNDER_ANY_CIRCUMSTANCES = false;
		java.awt.Color breadCrumbColor;
		if( IS_BREAD_CRUMB_COLOR_DESIRED_UNDER_ANY_CIRCUMSTANCES && ( composite instanceof org.alice.stageide.gallerybrowser.ResourceBasedTab ) ) {
			breadCrumbColor = org.alice.ide.DefaultTheme.DEFAULT_CONSTRUCTOR_COLOR;
		} else {
			breadCrumbColor = null;
		}
		org.lgna.croquet.views.BorderPanel panel = new org.lgna.croquet.views.BorderPanel.Builder()
				.vgap( PAD )
				.pageStart( new org.lgna.croquet.views.TreePathViewController( state, breadCrumbColor ) )
				.center( scrollPane )
				.build();

		this.addCenterComponent( panel );

		//todo
		view.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
		panel.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
		this.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
	}

	@Override
	protected void handleDisplayable() {
		org.alice.stageide.gallerybrowser.TreeOwningGalleryTab composite = (org.alice.stageide.gallerybrowser.TreeOwningGalleryTab)this.getComposite();
		org.alice.stageide.modelresource.ResourceNodeTreeState state = composite.getResourceNodeTreeSelectionState();
		state.addNewSchoolValueListener( this.treeListener );
		super.handleDisplayable();
	}

	@Override
	protected void handleUndisplayable() {
		super.handleUndisplayable();
		org.alice.stageide.gallerybrowser.TreeOwningGalleryTab composite = (org.alice.stageide.gallerybrowser.TreeOwningGalleryTab)this.getComposite();
		org.alice.stageide.modelresource.ResourceNodeTreeState state = composite.getResourceNodeTreeSelectionState();
		state.removeNewSchoolValueListener( this.treeListener );
	}

	private void handleChanged( org.alice.stageide.modelresource.ResourceNode prevValue, org.alice.stageide.modelresource.ResourceNode nextValue ) {
		final javax.swing.JScrollBar jHorizontalScrollBar = this.scrollPane.getAwtComponent().getHorizontalScrollBar();
		this.mapNodeToHorizontalScrollPosition.put( prevValue, jHorizontalScrollBar.getValue() );
		Integer i = this.mapNodeToHorizontalScrollPosition.get( nextValue );
		final int nextScrollPosition;
		if( i != null ) {
			nextScrollPosition = i;
		} else {
			nextScrollPosition = 0;
		}
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				jHorizontalScrollBar.setValue( nextScrollPosition );
			}
		} );
	}
}
