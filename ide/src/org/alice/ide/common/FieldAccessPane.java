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
public class FieldAccessPane extends org.alice.ide.common.ExpressionLikeSubstance {
	private edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess;

	public FieldAccessPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
		this.fieldAccess = fieldAccess;
		boolean isExpressionDesired;
		if( this.fieldAccess.expression.getValue() instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
			isExpressionDesired = org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().isTypeExpressionDesired();
		} else {
			isExpressionDesired = true;
		}
		if( isExpressionDesired ) {
			this.addComponent( factory.createExpressionPropertyPane( this.fieldAccess.expression, null ) );
			if( getIDE().isJava() ) {
				//pass
			} else {
				this.addComponent( new org.lgna.croquet.components.Label( "." ) );
			}
		}
		Class< ? extends edu.cmu.cs.dennisc.alice.ast.Expression > cls = edu.cmu.cs.dennisc.alice.ast.FieldAccess.class;
		this.setEnabledBackgroundPaint( getIDE().getTheme().getColorFor( cls ) );
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = this.fieldAccess.field.getValue();
		org.alice.ide.common.DeclarationNameLabel nodeNameLabel = new org.alice.ide.common.DeclarationNameLabel( field );
		//nodeNameLabel.scaleFont( 1.2f );
		//nodeNameLabel.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
		org.alice.ide.IDE.AccessorAndMutatorDisplayStyle accessorAndMutatorDisplayStyle = org.alice.ide.IDE.getActiveInstance().getAccessorAndMutatorDisplayStyle( field );
		boolean isGetter = accessorAndMutatorDisplayStyle == org.alice.ide.IDE.AccessorAndMutatorDisplayStyle.GETTER_AND_SETTER;
		if( isExpressionDesired ) {
			if( isGetter ) {
				org.lgna.croquet.components.Label getLabel = new org.lgna.croquet.components.Label( "get" );
				//getLabel.scaleFont( 1.2f );
				//getLabel.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
				this.addComponent( getLabel );
			}
		}
		this.addComponent( nodeNameLabel );
		if( isExpressionDesired ) {
			if( isGetter ) {
				if( org.alice.ide.IDE.getActiveInstance().isJava() ) {
					this.addComponent( new org.lgna.croquet.components.Label( "()" ) );
				}
			}
		}
	}
	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		if( this.fieldAccess != null ) {
			if( this.fieldAccess.expression.getValue() instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
				return super.isExpressionTypeFeedbackDesired();
			} else {
				if( isExpressionTypeFeedbackSurpressedBasedOnParentClass( this.fieldAccess ) ) {
					return false;
				} else {
					return super.isExpressionTypeFeedbackDesired();
				}
			}
		} else {
			return true;
		}
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getExpressionType() {
		if( this.fieldAccess != null ) {
			return this.fieldAccess.field.getValue().getValueType();
		} else {
			return null;
		}
	}
}
