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

package org.alice.ide.croquet.models.cascade;

/**
 * @author Dennis Cosgrove
 */
public class RelationalExpressionLeftAndRightOperandsFillIn extends ExpressionFillInWithExpressionBlanks<org.lgna.project.ast.RelationalInfixExpression> {
	private static edu.cmu.cs.dennisc.map.MapToMap<org.lgna.project.ast.AbstractType<?, ?, ?>, org.lgna.project.ast.RelationalInfixExpression.Operator, RelationalExpressionLeftAndRightOperandsFillIn> mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static RelationalExpressionLeftAndRightOperandsFillIn getInstance( org.lgna.project.ast.AbstractType<?, ?, ?> operandType, org.lgna.project.ast.RelationalInfixExpression.Operator operator ) {
		synchronized( mapToMap ) {
			RelationalExpressionLeftAndRightOperandsFillIn rv = mapToMap.get( operandType, operator );
			if( rv != null ) {
				//pass
			} else {
				rv = new RelationalExpressionLeftAndRightOperandsFillIn( operandType, operator );
				mapToMap.put( operandType, operator, rv );
			}
			return rv;
		}
	}

	public static RelationalExpressionLeftAndRightOperandsFillIn getInstance( Class<?> operandCls, org.lgna.project.ast.RelationalInfixExpression.Operator operator ) {
		return getInstance( org.lgna.project.ast.JavaType.getInstance( operandCls ), operator );
	}

	private final org.lgna.project.ast.RelationalInfixExpression transientValue;

	private RelationalExpressionLeftAndRightOperandsFillIn( org.lgna.project.ast.AbstractType<?, ?, ?> operandType, org.lgna.project.ast.RelationalInfixExpression.Operator operator ) {
		super( java.util.UUID.fromString( "f0dd5d2e-947f-4d8d-86b0-99a4ec6e759a" ) );
		this.transientValue = org.alice.ide.ast.IncompleteAstUtilities.createIncompleteRelationalInfixExpression( operandType, operator, operandType );
		this.addBlank( CascadeManager.getBlankForType( operandType ) );
		this.addBlank( CascadeManager.getBlankForType( operandType ) );
	}

	@Override
	protected org.lgna.project.ast.RelationalInfixExpression createValue( org.lgna.project.ast.Expression[] expressions ) {
		assert expressions.length == 2;
		return new org.lgna.project.ast.RelationalInfixExpression( expressions[ 0 ], this.transientValue.operator.getValue(), expressions[ 1 ], this.transientValue.leftOperandType.getValue(), this.transientValue.rightOperandType.getValue() );
	}

	@Override
	public org.lgna.project.ast.RelationalInfixExpression getTransientValue( org.lgna.croquet.cascade.ItemNode<? super org.lgna.project.ast.RelationalInfixExpression, org.lgna.project.ast.Expression> step ) {
		return this.transientValue;
	}
}
