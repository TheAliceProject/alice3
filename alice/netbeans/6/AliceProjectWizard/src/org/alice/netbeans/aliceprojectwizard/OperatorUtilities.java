/*******************************************************************************
 * Copyright (c) 2006, 2016, Carnegie Mellon University. All rights reserved.
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

package org.alice.netbeans.aliceprojectwizard;

import com.sun.source.tree.Tree;
import org.lgna.project.ast.*;

public class OperatorUtilities {

	private static java.util.Map<ArithmeticInfixExpression.Operator, Tree.Kind> mapArithmeticOperatorToTreeKind;
	private static java.util.Map<ConditionalInfixExpression.Operator, Tree.Kind> mapConditionalOperatorToTreeKind;
	private static java.util.Map<BitwiseInfixExpression.Operator, Tree.Kind> mapBitwiseOperatorToTreeKind;
	private static java.util.Map<ShiftInfixExpression.Operator, Tree.Kind> mapShiftOperatorToTreeKind;
	private static java.util.Map<RelationalInfixExpression.Operator, Tree.Kind> mapRelationalOperatorToTreeKind;

	static {
		OperatorUtilities.mapArithmeticOperatorToTreeKind = new java.util.HashMap<ArithmeticInfixExpression.Operator, Tree.Kind>();
		OperatorUtilities.mapArithmeticOperatorToTreeKind.put(ArithmeticInfixExpression.Operator.PLUS, Tree.Kind.PLUS);
		OperatorUtilities.mapArithmeticOperatorToTreeKind.put(ArithmeticInfixExpression.Operator.MINUS, Tree.Kind.MINUS);
		OperatorUtilities.mapArithmeticOperatorToTreeKind.put(ArithmeticInfixExpression.Operator.TIMES, Tree.Kind.MULTIPLY);
		//todo:
		OperatorUtilities.mapArithmeticOperatorToTreeKind.put(ArithmeticInfixExpression.Operator.REAL_DIVIDE, Tree.Kind.DIVIDE);
		//todo:
		OperatorUtilities.mapArithmeticOperatorToTreeKind.put(ArithmeticInfixExpression.Operator.INTEGER_DIVIDE, Tree.Kind.DIVIDE);
		//todo:
		OperatorUtilities.mapArithmeticOperatorToTreeKind.put(ArithmeticInfixExpression.Operator.REAL_REMAINDER, Tree.Kind.REMAINDER);
		//todo:
		OperatorUtilities.mapArithmeticOperatorToTreeKind.put(ArithmeticInfixExpression.Operator.INTEGER_REMAINDER, Tree.Kind.REMAINDER);

		OperatorUtilities.mapConditionalOperatorToTreeKind = new java.util.HashMap<ConditionalInfixExpression.Operator, Tree.Kind>();
		OperatorUtilities.mapConditionalOperatorToTreeKind.put(ConditionalInfixExpression.Operator.AND, Tree.Kind.CONDITIONAL_AND);
		OperatorUtilities.mapConditionalOperatorToTreeKind.put(ConditionalInfixExpression.Operator.OR, Tree.Kind.CONDITIONAL_OR);

		//it seems crazy that they wouldn't disambiguate between logical and bitwise
		OperatorUtilities.mapBitwiseOperatorToTreeKind = new java.util.HashMap<BitwiseInfixExpression.Operator, Tree.Kind>();
		OperatorUtilities.mapBitwiseOperatorToTreeKind.put(BitwiseInfixExpression.Operator.AND, Tree.Kind.AND);
		OperatorUtilities.mapBitwiseOperatorToTreeKind.put(BitwiseInfixExpression.Operator.OR, Tree.Kind.OR);
		OperatorUtilities.mapBitwiseOperatorToTreeKind.put(BitwiseInfixExpression.Operator.XOR, Tree.Kind.XOR);

		OperatorUtilities.mapShiftOperatorToTreeKind = new java.util.HashMap<ShiftInfixExpression.Operator, Tree.Kind>();
		OperatorUtilities.mapShiftOperatorToTreeKind.put(ShiftInfixExpression.Operator.LEFT_SHIFT, Tree.Kind.LEFT_SHIFT);
		OperatorUtilities.mapShiftOperatorToTreeKind.put(ShiftInfixExpression.Operator.RIGHT_SHIFT_SIGNED, Tree.Kind.RIGHT_SHIFT);
		OperatorUtilities.mapShiftOperatorToTreeKind.put(ShiftInfixExpression.Operator.RIGHT_SHIFT_UNSIGNED, Tree.Kind.UNSIGNED_RIGHT_SHIFT);

		OperatorUtilities.mapRelationalOperatorToTreeKind = new java.util.HashMap<RelationalInfixExpression.Operator, Tree.Kind>();
		OperatorUtilities.mapRelationalOperatorToTreeKind.put(RelationalInfixExpression.Operator.LESS, Tree.Kind.LESS_THAN);
		OperatorUtilities.mapRelationalOperatorToTreeKind.put(RelationalInfixExpression.Operator.LESS_EQUALS, Tree.Kind.LESS_THAN_EQUAL);
		OperatorUtilities.mapRelationalOperatorToTreeKind.put(RelationalInfixExpression.Operator.GREATER, Tree.Kind.GREATER_THAN);
		OperatorUtilities.mapRelationalOperatorToTreeKind.put(RelationalInfixExpression.Operator.GREATER_EQUALS, Tree.Kind.GREATER_THAN_EQUAL);
		OperatorUtilities.mapRelationalOperatorToTreeKind.put(RelationalInfixExpression.Operator.EQUALS, Tree.Kind.EQUAL_TO);
		OperatorUtilities.mapRelationalOperatorToTreeKind.put(RelationalInfixExpression.Operator.NOT_EQUALS, Tree.Kind.NOT_EQUAL_TO);
	}

	public static Tree.Kind lookupArithmetic(ArithmeticInfixExpression.Operator operator) {
		return OperatorUtilities.mapArithmeticOperatorToTreeKind.get(operator);
	}

	public static Tree.Kind lookupConditional(ConditionalInfixExpression.Operator operator) {
		return OperatorUtilities.mapConditionalOperatorToTreeKind.get(operator);
	}

	public static Tree.Kind lookupBitwise(BitwiseInfixExpression.Operator operator) {
		return OperatorUtilities.mapBitwiseOperatorToTreeKind.get(operator);
	}

	public static Tree.Kind lookupShift(ShiftInfixExpression.Operator operator) {
		return OperatorUtilities.mapShiftOperatorToTreeKind.get(operator);
	}

	public static Tree.Kind lookupRelational(RelationalInfixExpression.Operator operator) {
		return OperatorUtilities.mapRelationalOperatorToTreeKind.get(operator);
	}
}
