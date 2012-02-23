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

package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class ResourceTab extends GalleryTab {
	public static final javax.swing.Icon CREATE_PERSON_LARGE_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon(GalleryBrowser.class.getResource("images/create_person.png") );
	public static final javax.swing.Icon CREATE_PERSON_SMALL_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon(GalleryBrowser.class.getResource("images/create_person_24.png") );
	
	private static class SingletonHolder {
		private static ResourceTab instance = new ResourceTab();
	}
	public static ResourceTab getInstance() {
		return SingletonHolder.instance;
	}
	private ResourceTab() {
		super( java.util.UUID.fromString( "811380db-5339-4a2e-84e3-695b502188af" ) );
	}
	@Override
	public void customizeTitleComponent( org.lgna.croquet.BooleanState booleanState, org.lgna.croquet.components.BooleanStateButton< ? > button ) {
		super.customizeTitleComponent( booleanState, button );
		booleanState.setIconForBothTrueAndFalse( org.alice.ide.icons.Icons.EMPTY_HEIGHT_ICON_SMALL );
	}
	@Override
	protected org.lgna.croquet.components.View< ?, ? > createView() {
		final int PAD = 4;
		class ResourceView extends org.lgna.croquet.components.BorderPanel {
			public ResourceView() {
				super( PAD, 0 );
				this.setBorder( javax.swing.BorderFactory.createEmptyBorder( PAD,PAD,PAD,PAD ) );
				org.lgna.croquet.components.BorderPanel topPanel = new org.lgna.croquet.components.BorderPanel();
				topPanel.addComponent( new org.lgna.croquet.components.TreePathViewController( org.alice.ide.croquet.models.gallerybrowser.GalleryResourceTreeSelectionState.getInstance() ), Constraint.LINE_START );
				org.lgna.croquet.components.TextField filterTextField = FilterState.getInstance().createTextField();
				filterTextField.setMinimumPreferredWidth( 320 );
				filterTextField.setMaximumSizeClampedToPreferredSize( true );
				filterTextField.scaleFont( 1.5f );

				topPanel.addComponent( filterTextField, Constraint.LINE_END );

				org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( new GalleryDirectoryViewController() ) {
					@Override
					protected javax.swing.JScrollPane createAwtComponent() {
						return new edu.cmu.cs.dennisc.javax.swing.components.HorizontalScrollBarPaintOmittingWhenAppropriateJScrollPane();
					}
				};
				scrollPane.setHorizontalScrollbarPolicy( org.lgna.croquet.components.ScrollPane.HorizontalScrollbarPolicy.ALWAYS );
				scrollPane.setBorder( null );
		        scrollPane.setBothScrollBarIncrements( 16, 160 );
				
				org.lgna.croquet.components.GridPanel gridPanel = org.lgna.croquet.components.GridPanel.createGridPane( 0,  2 );
				
				gridPanel.addComponent( org.alice.stageide.croquet.models.declaration.DiscFieldDeclarationOperation.getInstance().createButton() );
				gridPanel.addComponent( org.alice.stageide.croquet.models.declaration.TextModelFieldDeclarationOperation.getInstance().createButton() );
				
				gridPanel.addComponent( org.alice.stageide.croquet.models.declaration.ConeFieldDeclarationOperation.getInstance().createButton() );
				gridPanel.addComponent( org.alice.stageide.croquet.models.declaration.BillboardFieldDeclarationOperation.getInstance().createButton() );
				
				gridPanel.addComponent( org.alice.stageide.croquet.models.declaration.CylinderFieldDeclarationOperation.getInstance().createButton() );
				gridPanel.addComponent( org.alice.stageide.croquet.models.declaration.AxesFieldDeclarationOperation.getInstance().createButton() );
				
				gridPanel.addComponent( org.alice.stageide.croquet.models.declaration.SphereFieldDeclarationOperation.getInstance().createButton() );
				gridPanel.addComponent( org.lgna.croquet.components.BoxUtilities.createGlue() );
				
		        org.lgna.croquet.components.BorderPanel lineEndPanel = new org.lgna.croquet.components.BorderPanel();
		        lineEndPanel.addComponent( gridPanel, Constraint.PAGE_START );

		        org.lgna.croquet.components.BorderPanel panel = new org.lgna.croquet.components.BorderPanel( 0, PAD );

				panel.addComponent( topPanel, Constraint.PAGE_START );
				panel.addComponent( scrollPane, Constraint.CENTER );

				this.addComponent( panel, Constraint.CENTER );
				this.addComponent( lineEndPanel, Constraint.LINE_END );

				//todo
				panel.setBackgroundColor( GalleryBrowser.BACKGROUND_COLOR );
				this.setBackgroundColor( GalleryBrowser.BACKGROUND_COLOR );
				scrollPane.setBackgroundColor( GalleryBrowser.BACKGROUND_COLOR );
			}
		}
		return new ResourceView();
	}
	@Override
	public boolean contains( org.lgna.croquet.Model model ) {
		//todo
		return false;
	}
}
