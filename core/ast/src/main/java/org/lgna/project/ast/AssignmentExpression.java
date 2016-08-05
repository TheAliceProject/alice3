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
package org.lgna.project.ast;

import org.lgna.project.code.CodeAppender;

/**
 * @author Dennis Cosgrove
 */
public final class AssignmentExpression extends Expression {
	public static enum Operator implements CodeAppender {
		ASSIGN( "=" ),
		PLUS_ASSIGN( "+=" ),
		MINUS_ASSIGN( "-=" ),
		TIMES_ASSIGN( "*=" ),
		DIVIDE_ASSIGN( "/=" ),
		BIT_AND_ASSIGN( "&=" ),
		BIT_OR_ASSIGN( "|=" ),
		BIT_XOR_ASSIGN( "^=" ),
		REMAINDER_ASSIGN( "%=" ),
		LEFT_SHIFT_ASSIGN( "<<=" ),
		RIGHT_SHIFT_SIGNED_ASSIGN( ">>=" ),
		RIGHT_SHIFT_UNSIGNED_ASSIGN( ">>>=" );
		private final String text;

		private Operator( String text ) {
			this.text = text;
		}

		@Override
		public void appendJava( JavaCodeGenerator generator ) {
			generator.appendString( this.text );
		}
	}

	public AssignmentExpression() {
	}

	public AssignmentExpression( AbstractType<?, ?, ?> expressionType, Expression leftHandSide, Operator operator, Expression rightHandSide ) {
		this.expressionType.setValue( expressionType );
		this.leftHandSide.setValue( leftHandSide );
		this.operator.setValue( operator );
		this.rightHandSide.setValue( rightHandSide );
	}

	@Override
	public AbstractType<?, ?, ?> getType() {
		return JavaType.VOID_TYPE;
	}

	@Override
	public boolean contentEquals( Node o, ContentEqualsStrictness strictness, edu.cmu.cs.dennisc.property.PropertyFilter filter ) {
		if( super.contentEquals( o, strictness, filter ) ) {
			AssignmentExpression other = (AssignmentExpression)o;
			if( this.expressionType.valueContentEquals( other.expressionType, strictness, filter ) ) {
				if( this.leftHandSide.valueContentEquals( other.leftHandSide, strictness, filter ) ) {
					if( this.operator.valueEquals( other.operator, filter ) ) {
						return this.rightHandSide.valueContentEquals( other.rightHandSide, strictness, filter );
					}
				}
			}
		}
		return false;
	}

	@Override
	public void appendJava( JavaCodeGenerator generator ) {
		generator.appendExpression( this.leftHandSide.getValue() );
		this.operator.getValue().appendJava( generator );
		generator.appendExpression( this.rightHandSide.getValue() );
	}

	public final DeclarationProperty<AbstractType<?, ?, ?>> expressionType = DeclarationProperty.createReferenceInstance( this );
	public final ExpressionProperty leftHandSide = new ExpressionProperty( this ) {
		@Override
		public AbstractType<?, ?, ?> getExpressionType() {
			return AssignmentExpression.this.expressionType.getValue();
		}
	};

	public final edu.cmu.cs.dennisc.property.InstanceProperty<Operator> operator = new edu.cmu.cs.dennisc.property.InstanceProperty<Operator>( this, null );
	//todo: new name
	public final ExpressionProperty rightHandSide = new ExpressionProperty( this ) {
		@Override
		public AbstractType<?, ?, ?> getExpressionType() {
			return AssignmentExpression.this.expressionType.getValue();
		}
	};
}
