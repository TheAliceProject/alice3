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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class ThisPane extends AccessiblePane {
	private static final org.lgna.project.ast.JavaType TYPE_FOR_NULL = org.lgna.project.ast.JavaType.getInstance( Void.class );
	private org.lgna.project.ast.AbstractType<?,?,?> type = TYPE_FOR_NULL;
	private org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.editorstabbedpane.CodeComposite > codeSelectionObserver = new org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.editorstabbedpane.CodeComposite >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.editorstabbedpane.CodeComposite > state, org.alice.ide.editorstabbedpane.CodeComposite prevValue, org.alice.ide.editorstabbedpane.CodeComposite nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.editorstabbedpane.CodeComposite > state, org.alice.ide.editorstabbedpane.CodeComposite prevValue, org.alice.ide.editorstabbedpane.CodeComposite nextValue, boolean isAdjusting ) {
			ThisPane.this.updateBasedOnFocusedCode( nextValue != null ? nextValue.getCode() : null );
		}
	};

	public ThisPane() {
		this.addComponent( new org.lgna.croquet.components.Label( org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().getTextForThis() ) );
		this.setEnabledBackgroundPaint( getIDE().getTheme().getColorFor( org.lgna.project.ast.ThisExpression.class ) );
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.updateBasedOnFocusedCode( org.alice.ide.IDE.getActiveInstance().getFocusedCode() );
		org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().addAndInvokeValueObserver( this.codeSelectionObserver );
	}
	@Override
	protected void handleUndisplayable() {
		org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().removeValueObserver( this.codeSelectionObserver );
		super.handleUndisplayable();
	}
	private void updateBasedOnFocusedCode( org.lgna.project.ast.AbstractCode code ) {
		if( code != null ) {
			this.type = code.getDeclaringType();
		} else {
			this.type = null;
		}
		if( this.type != null ) {
			this.setToolTipText( "the current instance of " + this.type.getName() + " is referred to as " + org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().getTextForThis() );
		} else {
			this.type = TYPE_FOR_NULL;
			this.setToolTipText( null );
		}
	}
	@Override
	public org.lgna.project.ast.AbstractType<?,?,?> getExpressionType() {
		return this.type;
	}
	@Override
	public org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.lgna.project.ast.ExpressionProperty expressionProperty ) {
		return org.alice.ide.croquet.models.ast.cascade.expression.ThisOperation.getInstance( expressionProperty );
	}

	@Override
	protected void paintEpilogue(java.awt.Graphics2D g2, int x, int y, int width, int height) {
		super.paintEpilogue(g2, x, y, width, height);
		if( this.type == TYPE_FOR_NULL ) {
			g2.setPaint( org.lgna.croquet.components.PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
	
}
