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

package org.alice.ide.x;

/**
 * @author Dennis Cosgrove
 */
public abstract class IdeAstI18nFactory extends AstI18nFactory {
	@Override
	protected org.lgna.croquet.components.JComponent<?> createIdeExpressionPane( org.alice.ide.ast.IdeExpression ideExpression ) {
		if( ideExpression instanceof org.alice.ide.ast.EmptyExpression ) {
			return new org.alice.ide.common.EmptyExpressionPane( (org.alice.ide.ast.EmptyExpression)ideExpression );
		} else if( ideExpression instanceof org.alice.ide.ast.PreviousValueExpression ) {
			return new org.alice.ide.common.PreviousValueExpressionPane( this, (org.alice.ide.ast.PreviousValueExpression)ideExpression );
		} else if( ideExpression instanceof org.alice.ide.ast.CurrentThisExpression ) {
			return new org.alice.ide.x.components.ThisExpressionLikeView( this, (org.alice.ide.ast.CurrentThisExpression)ideExpression );
		} else if( ideExpression instanceof org.alice.ide.ast.SelectedInstanceFactoryExpression ) {
			//rv = new org.alice.ide.common.SelectedFieldExpressionPane( (org.alice.ide.ast.SelectedInstanceFactoryExpression)expression );
			return new org.alice.ide.common.SelectedInstanceFactoryExpressionPanel( this );
		} else {
			throw new RuntimeException( ideExpression.toString() );
		}
	}

	@Override
	public org.lgna.croquet.components.JComponent<?> createExpressionPropertyPane( org.lgna.project.ast.ExpressionProperty expressionProperty, org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return this.createExpressionPane( expressionProperty.getValue() );
	}
}
