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
package org.alice.ide.x.components;

/**
 * @author Dennis Cosgrove
 */
public class FieldAccessView extends AbstractExpressionView<org.lgna.project.ast.FieldAccess> {
	private final org.lgna.croquet.components.JComponent<?> replacement;

	public FieldAccessView( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.FieldAccess fieldAccess ) {
		super( factory, fieldAccess );
		org.lgna.croquet.components.Component<?> prefixPane = org.alice.ide.IDE.getActiveInstance().getPrefixPaneForFieldAccessIfAppropriate( fieldAccess );
		if( prefixPane != null ) {
			this.addComponent( prefixPane );
		}

		this.replacement = org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().createReplacementForFieldAccessIfAppropriate( fieldAccess );
		if( this.replacement != null ) {
			this.addComponent( this.replacement );
		} else {
			boolean isExpressionDesired;
			if( fieldAccess.expression.getValue() instanceof org.lgna.project.ast.TypeExpression ) {
				isExpressionDesired = org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().isTypeExpressionDesired();
			} else {
				isExpressionDesired = true;
			}
			if( isExpressionDesired ) {
				this.addComponent( factory.createExpressionPropertyPane( fieldAccess.expression ) );
				if( org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.isJava() ) {
					//pass
				} else {
					this.addComponent( new org.lgna.croquet.components.Label( "." ) );
				}
			}
			org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
			org.alice.ide.ast.components.DeclarationNameLabel nodeNameLabel = new org.alice.ide.ast.components.DeclarationNameLabel( field );
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
					if( org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.isJava() ) {
						this.addComponent( new org.lgna.croquet.components.Label( "()" ) );
					}
				}
			}
		}
	}

	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		if( this.replacement != null ) {
			return true;
		} else {
			org.lgna.project.ast.FieldAccess fieldAccess = this.getExpression();
			if( fieldAccess != null ) {
				if( fieldAccess.expression.getValue() instanceof org.lgna.project.ast.TypeExpression ) {
					return super.isExpressionTypeFeedbackDesired();
				} else {
					if( isExpressionTypeFeedbackSurpressedBasedOnParentClass( fieldAccess ) ) {
						return false;
					} else {
						return super.isExpressionTypeFeedbackDesired();
					}
				}
			} else {
				return true;
			}
		}
	}

	@Override
	public org.lgna.project.ast.AbstractType<?, ?, ?> getExpressionType() {
		org.lgna.project.ast.FieldAccess fieldAccess = this.getExpression();
		if( fieldAccess != null ) {
			return fieldAccess.field.getValue().getValueType();
		} else {
			return null;
		}
	}
}
