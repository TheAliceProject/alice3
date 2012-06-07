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
package org.alice.ide.custom.components;

/**
 * @author Dennis Cosgrove
 */
public class ArrayCustomExpressionCreatorView extends RowBasedCustomExpressionCreatorView {
	private static class ExpressionList extends org.lgna.croquet.components.MutableList< org.lgna.project.ast.Expression, org.lgna.croquet.components.Label, org.lgna.croquet.components.Label, org.lgna.croquet.components.Label > {
		public ExpressionList( org.lgna.croquet.ListSelectionState< org.lgna.project.ast.Expression > state, org.lgna.croquet.PopupPrepModel popupPrepModel ) {
			super( state, popupPrepModel );
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );
		}
		@Override
		protected org.lgna.croquet.components.Label createLeadingComponent() {
			org.lgna.croquet.components.Label rv = new org.lgna.croquet.components.Label( "leading", 1.4f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
			rv.setVerticalAlignment( org.lgna.croquet.components.VerticalAlignment.CENTER );
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0,0,0,8 ) );
			return rv;
		}
		@Override
		protected org.lgna.croquet.components.Label createMainComponent() {
			org.lgna.croquet.components.Label rv = new org.lgna.croquet.components.Label( "main", 1.4f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
			rv.setVerticalAlignment( org.lgna.croquet.components.VerticalAlignment.CENTER );
			return rv;
		}
		@Override
		protected org.lgna.croquet.components.Label createTrailingComponent() {
			return null;
		}
		@Override
		protected void update( org.lgna.croquet.components.Label leadingComponent, org.lgna.croquet.components.Label mainComponent, org.lgna.croquet.components.Label trailingComponent, int index, org.lgna.project.ast.Expression item ) {
			leadingComponent.setText( "[" + index + "]" );
			org.lgna.project.ast.Expression expression = this.getModel().getItemAt( index );
			String text = expression != null ? expression.getRepr( javax.swing.JComponent.getDefaultLocale() ) : "null";
			mainComponent.setText( text );
		}
		@Override
		protected void updateSelection( org.lgna.croquet.components.Label leadingComponent, org.lgna.croquet.components.Label mainComponent, org.lgna.croquet.components.Label trailingComponent, boolean isSelected ) {
			java.awt.Color color = isSelected ? java.awt.Color.WHITE : java.awt.Color.BLACK;
			leadingComponent.setForegroundColor( color );
			mainComponent.setForegroundColor( color );
		}
	}

	
	public ArrayCustomExpressionCreatorView( org.alice.ide.custom.ArrayCustomExpressionCreatorComposite composite ) {
		super( composite );
	}
	@Override
	protected void appendRows( java.util.List< Row > rows ) {
		org.alice.ide.custom.ArrayCustomExpressionCreatorComposite composite = (org.alice.ide.custom.ArrayCustomExpressionCreatorComposite)this.getComposite();
		rows.add( new Row( composite.getArrayTypeLabel(), new org.lgna.croquet.components.Label( org.alice.ide.common.TypeIcon.getInstance( composite.getArrayType() ) ) ) );
		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( new ExpressionList( composite.getValueState(), composite.getAddItemCascade().getRoot().getPopupPrepModel() ) );
		scrollPane.setBorder( null );
		rows.add( new Row( composite.getValueLabel(), scrollPane, org.lgna.croquet.components.VerticalAlignment.TOP, true ) );
	}
}
