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
package org.alice.ide.x.components;

/**
 * @author Dennis Cosgrove
 */
public class AbstractExpressionView<E extends org.lgna.project.ast.Expression> extends org.alice.ide.common.ExpressionLikeSubstance {
	private final org.alice.ide.x.AstI18nFactory factory;
	private final E expression;

	public AbstractExpressionView( org.alice.ide.x.AstI18nFactory factory, E expression ) {
		super( null, expression != null ? expression.getType() == org.lgna.project.ast.JavaType.VOID_TYPE : false );
		this.factory = factory;
		this.expression = expression;
		this.setBackgroundColor( org.alice.ide.ThemeUtilities.getActiveTheme().getColorFor( expression ) );
	}

	public E getExpression() {
		return this.expression;
	}

	@Override
	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
		java.awt.Paint validPaint = super.getBackgroundPaint( x, y, width, height );
		if( ( this.expression != null ) && this.expression.isValid() ) {
			return validPaint;
		} else {
			return this.factory.getInvalidExpressionPaint( validPaint, x, y, width, height );
		}
	}

	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		if( this.expression != null ) {
			if( org.lgna.project.ast.AstUtilities.isKeywordExpression( expression ) ) {
				return false;
			} else {
				if( isExpressionTypeFeedbackSurpressedBasedOnParentClass( this.expression ) ) {
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
	public org.lgna.project.ast.AbstractType<?, ?, ?> getExpressionType() {
		if( this.expression != null ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> rv = this.expression.getType();
			return rv;
		} else {
			return org.lgna.project.ast.JavaType.OBJECT_TYPE;
		}
	}

	@Override
	protected int getInsetTop() {
		if( ( this.expression instanceof org.lgna.project.ast.InfixExpression ) || ( this.expression instanceof org.lgna.project.ast.LogicalComplement ) ) {
			return 0;
		} else {
			return super.getInsetTop();
		}
	}

	@Override
	protected int getInsetBottom() {
		if( ( this.expression instanceof org.lgna.project.ast.InfixExpression ) || ( this.expression instanceof org.lgna.project.ast.LogicalComplement ) ) {
			return 0;
		} else {
			return super.getInsetTop();
		}
	}
}
