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
package org.alice.ide.cascade.fillerinners;

/**
 * @author Dennis Cosgrove
 */
public class BooleanFillerInner extends ExpressionFillerInner {
	private final java.util.List<org.lgna.project.ast.AbstractType<?, ?, ?>> relationalTypes = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	public BooleanFillerInner() {
		super( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE );
	}

	public void addRelationalType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		this.relationalTypes.add( type );
	}

	@Override
	public void appendItems( java.util.List<org.lgna.croquet.CascadeBlankChild> items, org.lgna.project.annotations.ValueDetails<?> details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		if( isTop && ( prevExpression instanceof org.lgna.project.ast.ConditionalInfixExpression ) ) {
			// previous conditional
			org.lgna.project.ast.ConditionalInfixExpression conditionalInfixExpression = (org.lgna.project.ast.ConditionalInfixExpression)prevExpression;
			for( org.lgna.project.ast.ConditionalInfixExpression.Operator operator : org.lgna.project.ast.ConditionalInfixExpression.Operator.values() ) {
				if( operator == conditionalInfixExpression.operator.getValue() ) {
					//pass
				} else {
					items.add( org.alice.ide.croquet.models.cascade.conditional.ReplaceOperatorInPreviousConditionalExpressionFillIn.getInstance( operator ) );
				}
			}
			items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			items.add( org.alice.ide.croquet.models.cascade.conditional.ReduceToLeftOperandInPreviousConditionalExpressionFillIn.getInstance() );
			items.add( org.alice.ide.croquet.models.cascade.conditional.ReduceToRightOperandInPreviousConditionalExpressionFillIn.getInstance() );
			items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
		}

		if( isTop && ( prevExpression instanceof org.lgna.project.ast.LogicalComplement ) ) {
			// previous logical complement
			items.add( org.alice.ide.croquet.models.cascade.logicalcomplement.ReduceToInnerOperandInPreviousLogicalComplementFillIn.getInstance() );
			items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
		}

		items.add( org.alice.ide.croquet.models.cascade.literals.BooleanLiteralFillIn.getInstance( true ) );
		items.add( org.alice.ide.croquet.models.cascade.literals.BooleanLiteralFillIn.getInstance( false ) );
		items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );

		if( isTop && ( prevExpression != null ) ) {
			items.add( org.alice.ide.croquet.models.cascade.StaticMethodInvocationFillIn.getInstance( org.lgna.common.RandomUtilities.class, "nextBoolean" ) );
			items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
		}

		if( isTop && ( prevExpression != null ) ) {
			items.add( org.alice.ide.croquet.models.cascade.logicalcomplement.LogicalComplementOfPreviousExpressionFillIn.getInstance() );
			items.add( org.alice.ide.croquet.models.cascade.logicalcomplement.LogicalComplementOperandFillIn.getInstance() );
			items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
		}

		if( isTop && ( prevExpression != null ) ) {
			for( org.lgna.project.ast.ConditionalInfixExpression.Operator operator : org.lgna.project.ast.ConditionalInfixExpression.Operator.values() ) {
				items.add( org.alice.ide.croquet.models.cascade.conditional.ConditionalExpressionRightOperandOnlyFillIn.getInstance( operator ) );
			}
			for( org.lgna.project.ast.ConditionalInfixExpression.Operator operator : org.lgna.project.ast.ConditionalInfixExpression.Operator.values() ) {
				items.add( org.alice.ide.croquet.models.cascade.conditional.ConditionalExpressionLeftAndRightOperandsFillIn.getInstance( operator ) );
			}
			items.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
		}

		if( isTop && ( prevExpression != null ) ) {
			items.add( org.alice.ide.croquet.models.cascade.RelationalNumberCascadeMenu.getInstance() );
			items.add( org.alice.ide.croquet.models.cascade.RelationalIntegerCascadeMenu.getInstance() );
			for( org.lgna.project.ast.AbstractType<?, ?, ?> type : this.relationalTypes ) {
				items.add( new org.alice.ide.croquet.models.cascade.RelationalObjectCascadeMenu( type ) );
			}
			items.add( org.alice.ide.croquet.models.cascade.string.StringComparisonCascadeMenu.getInstance() );
		}
	}
}
