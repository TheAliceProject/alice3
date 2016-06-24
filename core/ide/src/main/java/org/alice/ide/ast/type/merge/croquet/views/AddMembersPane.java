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
package org.alice.ide.ast.type.merge.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class AddMembersPane extends org.lgna.croquet.views.MigPanel {
	private void addToolPaletteViewIfAppropriate( org.alice.ide.ast.type.merge.croquet.MembersToolPalette<?, ?> composite, org.lgna.croquet.views.MigPanel panel ) {
		if( composite.getTotalCount() > 0 ) {
			org.lgna.croquet.views.ToolPaletteView toolPaletteView = composite.getOuterComposite().getView();
			toolPaletteView.getTitle().setInert( true );
			toolPaletteView.getTitle().setBackgroundColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( composite.getView().getBackgroundColor(), 1.0, 0.90, 0.85 ) );
			//toolPaletteView.getTitle().changeFont( edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
			toolPaletteView.getTitle().setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
			panel.addComponent( toolPaletteView, "grow, shrink, gap 16, wrap" );
		}
	}

	public AddMembersPane( org.alice.ide.ast.type.merge.croquet.AddMembersPage composite ) {
		super( composite, "fillx" );
		org.lgna.croquet.views.Label classLabel = new org.lgna.croquet.views.Label( "class", org.alice.ide.common.TypeIcon.getInstance( composite.getDstType() ) );
		classLabel.setHorizontalTextPosition( org.lgna.croquet.views.HorizontalTextPosition.LEADING );
		this.addComponent( classLabel, "wrap" );

		org.lgna.croquet.views.MigPanel panel = new org.lgna.croquet.views.MigPanel( null, "fill, insets 0" );
		this.addToolPaletteViewIfAppropriate( composite.getAddProceduresComposite(), panel );
		this.addToolPaletteViewIfAppropriate( composite.getAddFunctionsComposite(), panel );
		this.addToolPaletteViewIfAppropriate( composite.getAddFieldsComposite(), panel );

		org.lgna.croquet.views.ScrollPane scrollPane = new org.lgna.croquet.views.ScrollPane( panel );
		this.addComponent( scrollPane, "grow, shrink, wrap" );

		if( composite.isContainingDifferentImplementations() ) {
			this.addComponent( composite.getDifferentImplementationsHeader().createLabel(), "gaptop 32, wrap" );
			this.addComponent( composite.getDifferentImplementationsSubHeader().createLabel(), "wrap" );
			this.addComponent( composite.getAcceptAllDifferentImplementationsOperation().createButton(), "split 2" );
			this.addComponent( composite.getRejectAllDifferentImplementationsOperation().createButton() );
		}

		this.setBackgroundColor( org.alice.ide.ThemeUtilities.getActiveTheme().getTypeColor() );
		panel.setBackgroundColor( this.getBackgroundColor() );
		scrollPane.setBackgroundColor( this.getBackgroundColor() );
		this.setMinimumPreferredWidth( 800 );
	}
}
