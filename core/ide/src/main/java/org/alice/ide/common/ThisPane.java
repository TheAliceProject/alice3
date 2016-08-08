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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class ThisPane extends AccessiblePane {
	private static final org.lgna.project.ast.JavaType TYPE_FOR_NULL = org.lgna.project.ast.JavaType.getInstance( Void.class );
	private org.lgna.project.ast.AbstractType<?, ?, ?> type = TYPE_FOR_NULL;
	private org.lgna.croquet.event.ValueListener<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>> declarationListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>> e ) {
			org.alice.ide.declarationseditor.DeclarationComposite<?, ?> nextValue = e.getNextValue();
			ThisPane.this.updateBasedOnFocusedDeclaration( nextValue != null ? nextValue.getDeclaration() : null );
		}
	};

	public ThisPane() {
		super( org.alice.ide.ast.draganddrop.expression.ThisExpressionDragModel.getInstance() );
		this.addComponent( new org.lgna.croquet.views.Label( org.alice.ide.croquet.models.ui.formatter.FormatterState.getInstance().getValue().getTextForThis() ) );
		this.setBackgroundColor( org.alice.ide.ThemeUtilities.getActiveTheme().getColorFor( org.lgna.project.ast.ThisExpression.class ) );
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.updateBasedOnFocusedDeclaration( org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getMetaDeclarationFauxState().getValue() );
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().addAndInvokeNewSchoolValueListener( this.declarationListener );
	}

	@Override
	protected void handleUndisplayable() {
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().removeNewSchoolValueListener( this.declarationListener );
		super.handleUndisplayable();
	}

	private void updateBasedOnFocusedDeclaration( org.lgna.project.ast.AbstractDeclaration declaration ) {
		if( declaration != null ) {
			if( declaration instanceof org.lgna.project.ast.AbstractMember ) {
				this.type = ( (org.lgna.project.ast.AbstractMember)declaration ).getDeclaringType();
			} else if( declaration instanceof org.lgna.project.ast.AbstractType<?, ?, ?> ) {
				this.type = (org.lgna.project.ast.AbstractType<?, ?, ?>)declaration;
			} else {
				this.type = null;
			}
		} else {
			this.type = null;
		}
		if( this.type != null ) {
			this.setToolTipText( "the current instance of " + this.type.getName() + " is referred to as " + org.alice.ide.croquet.models.ui.formatter.FormatterState.getInstance().getValue().getTextForThis() );
		} else {
			this.type = TYPE_FOR_NULL;
			this.setToolTipText( null );
		}
	}

	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		super.paintEpilogue( g2, x, y, width, height );
		if( this.type == TYPE_FOR_NULL ) {
			g2.setPaint( org.lgna.croquet.views.PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}

}
