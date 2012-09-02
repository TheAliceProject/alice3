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
public class ResourceTabView extends GalleryTabView {
	private static final int PAD = 4;

	public ResourceTabView( org.alice.stageide.gallerybrowser.ResourceTab composite ) {
		super( composite );//, PAD, 0 );

		org.alice.stageide.modelresource.ResourceNodeTreeSelectionState state = org.alice.stageide.modelresource.ResourceNodeTreeSelectionState.getInstance();

		org.alice.stageide.modelresource.views.ModelResourceDirectoryView view = new org.alice.stageide.modelresource.views.ModelResourceDirectoryView( state );

		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( view ) {
			@Override
			protected javax.swing.JScrollPane createAwtComponent() {
				return new edu.cmu.cs.dennisc.javax.swing.components.HorizontalScrollBarPaintOmittingWhenAppropriateJScrollPane();
			}
		};
		scrollPane.setHorizontalScrollbarPolicy( org.lgna.croquet.components.ScrollPane.HorizontalScrollbarPolicy.ALWAYS );
		scrollPane.setBorder( null );
		scrollPane.setBothScrollBarIncrements( 16, 160 );

		org.lgna.croquet.components.GridPanel gridPanel = org.lgna.croquet.components.GridPanel.createGridPane( 0, 2 );

		gridPanel.addComponent( org.alice.stageide.ast.declaration.AddDiscManagedFieldComposite.getInstance().getOperation().createButton() );
		gridPanel.addComponent( org.alice.stageide.ast.declaration.AddTextModelManagedFieldOperationComposite.getInstance().getOperation().createButton() );

		gridPanel.addComponent( org.alice.stageide.ast.declaration.AddConeManagedFieldComposite.getInstance().getOperation().createButton() );
		gridPanel.addComponent( org.alice.stageide.ast.declaration.AddBillboardManagedFieldComposite.getInstance().getOperation().createButton() );

		gridPanel.addComponent( org.alice.stageide.ast.declaration.AddCylinderManagedFieldComposite.getInstance().getOperation().createButton() );
		gridPanel.addComponent( org.alice.stageide.ast.declaration.AddAxesManagedFieldComposite.getInstance().getOperation().createButton() );

		gridPanel.addComponent( org.alice.stageide.ast.declaration.AddSphereManagedFieldComposite.getInstance().getOperation().createButton() );
		gridPanel.addComponent( org.alice.stageide.ast.declaration.AddTorusManagedFieldComposite.getInstance().getOperation().createButton() );

		gridPanel.addComponent( org.alice.stageide.ast.declaration.AddBoxManagedFieldComposite.getInstance().getOperation().createButton() );
		//gridPanel.addComponent( org.lgna.croquet.components.BoxUtilities.createGlue() );

		for( java.awt.Component awtComponent : gridPanel.getAwtComponent().getComponents() ) {
			if( awtComponent instanceof javax.swing.JButton ) {
				javax.swing.JButton jButton = (javax.swing.JButton)awtComponent;
				jButton.setHorizontalAlignment( javax.swing.SwingConstants.LEADING );
			}
		}

		org.lgna.croquet.components.BorderPanel lineEndPanel = new org.lgna.croquet.components.BorderPanel.Builder()
				.pageStart( gridPanel )
				.build();

		if( org.alice.ide.croquet.models.ast.ExportTypeOperation.IS_READY_FOR_PRIME_TIME ) {
			lineEndPanel.addPageEndComponent( org.alice.stageide.croquet.models.gallerybrowser.DeclareFieldFromImportedTypeOperation.getInstance().createButton() );
		}

		org.lgna.croquet.components.BorderPanel panel = new org.lgna.croquet.components.BorderPanel.Builder()
				.vgap( PAD )
				.pageStart( new org.lgna.croquet.components.TreePathViewController( state ) )
				.center( scrollPane )
				.build();

		this.addCenterComponent( panel );
		this.addLineEndComponent( lineEndPanel );

		//todo
		view.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
		panel.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
		scrollPane.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
		this.setBackgroundColor( GalleryView.BACKGROUND_COLOR );
	}
}
