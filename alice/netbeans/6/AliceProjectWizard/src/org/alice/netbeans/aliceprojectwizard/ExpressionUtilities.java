/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.netbeans.aliceprojectwizard;

import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Tree;

import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.netbeans.api.java.source.TreeMaker;

import org.lgna.project.ast.*;
import org.lgna.project.reflect.ClassInfoManager;
import org.lgna.project.reflect.ClassInfo;
import org.lgna.project.reflect.MethodInfo;

//todo: remove
//@SuppressWarnings("deprecation")
public class ExpressionUtilities {

	public static ArrayAccessTree generateArrayAccess(TreeMaker treeMaker, ArrayAccess arrayAccess) {
		Expression array = arrayAccess.array.getValue();
		Expression index = arrayAccess.index.getValue();
		return treeMaker.ArrayAccess(ExpressionUtilities.generate(treeMaker, array), ExpressionUtilities.generate(treeMaker, index));
	}

	public static MemberSelectTree generateArrayLength(TreeMaker treeMaker, ArrayLength arrayLength) {
		Expression array = arrayLength.array.getValue();
		return treeMaker.MemberSelect(ExpressionUtilities.generate(treeMaker, array), "length");
	}

	public static NewArrayTree generateArrayInstanceCreation(TreeMaker treeMaker, ArrayInstanceCreation arrayInstanceCreation) {
		Tree componentTypeTree = NetBeansASTGenerator.get(treeMaker).generateTypeTree(arrayInstanceCreation.arrayType.getValue().getComponentType());
		java.util.List<? extends ExpressionTree> dimensions = new java.util.LinkedList<ExpressionTree>();
//		for( Integer i : arrayInstanceCreation.lengths ) {
//			//dimensions.add( treeMaker.Literal(i) );
//			dimensions.add( null );
//		}
		java.util.List<? extends ExpressionTree> initializers = ExpressionUtilities.generate(treeMaker, arrayInstanceCreation.expressions.getValue());
		return treeMaker.NewArray(componentTypeTree, dimensions, initializers);
	}

	public static ExpressionTree generateAssignmentExpression(TreeMaker treeMaker, AssignmentExpression assignmentExpression) {
		Expression left = assignmentExpression.leftHandSide.getValue();
		Expression right = assignmentExpression.rightHandSide.getValue();
		return treeMaker.Assignment(ExpressionUtilities.generate(treeMaker, left), ExpressionUtilities.generate(treeMaker, right));
	}

	public static LiteralTree generateBooleanLiteral(TreeMaker treeMaker, BooleanLiteral booleanLiteral) {
		return treeMaker.Literal(booleanLiteral.value.getValue());
	}

	public static NewClassTree generateInstanceCreation(TreeMaker treeMaker, InstanceCreation instanceCreation) {
		ExpressionTree enclosingExpression = null;
		java.util.List<ExpressionTree> typeArguments = new java.util.LinkedList<ExpressionTree>();


		AbstractType declaringType = instanceCreation.constructor.getValue().getDeclaringType();
		ExpressionTree identifier = NetBeansASTGenerator.get(treeMaker).generateTypeTree(declaringType);


		ClassTree classBody;

		if (declaringType instanceof AnonymousUserType) {
			AnonymousUserType anonymousInnerTypeDeclaredInAlice = (AnonymousUserType) declaringType;
			CharSequence name = "";

			ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(treeMaker);
			java.util.List<TypeParameterTree> typeParameters = new java.util.LinkedList<TypeParameterTree>();
			Tree extendsClause = null;
			java.util.List<Tree> implementsClauses = new java.util.LinkedList<Tree>();
			java.util.List<Tree> memberDecls = new java.util.LinkedList<Tree>();

			NetBeansASTGenerator generator = NetBeansASTGenerator.get(treeMaker);
			generator.pushIsInnerClass(true);
			try {
				generator.generateMethods(memberDecls, anonymousInnerTypeDeclaredInAlice);
				;
				generator.generateGettersAndSetters(memberDecls, anonymousInnerTypeDeclaredInAlice);
				generator.generateFields(memberDecls, anonymousInnerTypeDeclaredInAlice);
			} finally {
				generator.popIsInnerClass();
			}
			classBody = treeMaker.Class(modifiersTree, name, typeParameters, extendsClause, implementsClauses, memberDecls);
		} else {
			classBody = null;
		}

		java.util.List<ExpressionTree> arguments = new java.util.LinkedList<ExpressionTree>();
		ArgumentUtilities.generateSimpleArguments(arguments, treeMaker, instanceCreation.requiredArguments);
		ArgumentUtilities.generateKeyedArguments(arguments, treeMaker, instanceCreation.keyedArguments);
		return treeMaker.NewClass(enclosingExpression, typeArguments, identifier, arguments, classBody);
	}

	public static ExpressionTree generateFieldAccess(TreeMaker treeMaker, FieldAccess fieldAccess) {
		ExpressionTree expressionTree = ExpressionUtilities.generate(treeMaker, fieldAccess.expression.getValue());
		String name = fieldAccess.field.getValue().getName();
		assert name != null;
		return treeMaker.MemberSelect(expressionTree, name);
	}

	public static BinaryTree generateArithmeticInfixExpression(TreeMaker treeMaker, ArithmeticInfixExpression arithmeticInfixExpression) {
		return treeMaker.Binary(OperatorUtilities.lookupArithmetic(arithmeticInfixExpression.operator.getValue()), ExpressionUtilities.generate(treeMaker, arithmeticInfixExpression.leftOperand.getValue()), ExpressionUtilities.generate(treeMaker, arithmeticInfixExpression.rightOperand.getValue()));
	}

	public static BinaryTree generateStringConcatenation(TreeMaker treeMaker, StringConcatenation stringConcatenation) {
		return treeMaker.Binary(Tree.Kind.PLUS, ExpressionUtilities.generate(treeMaker, stringConcatenation.leftOperand.getValue()), ExpressionUtilities.generate(treeMaker, stringConcatenation.rightOperand.getValue()));
	}

	public static BinaryTree generateConditionalInfixExpression(TreeMaker treeMaker, ConditionalInfixExpression conditionalInfixExpression) {
		return treeMaker.Binary(OperatorUtilities.lookupConditional(conditionalInfixExpression.operator.getValue()), ExpressionUtilities.generate(treeMaker, conditionalInfixExpression.leftOperand.getValue()), ExpressionUtilities.generate(treeMaker, conditionalInfixExpression.rightOperand.getValue()));
	}

	public static BinaryTree generateBitwiseInfixExpression(TreeMaker treeMaker, BitwiseInfixExpression bitwiseInfixExpression) {
		return treeMaker.Binary(OperatorUtilities.lookupBitwise(bitwiseInfixExpression.operator.getValue()), ExpressionUtilities.generate(treeMaker, bitwiseInfixExpression.leftOperand.getValue()), ExpressionUtilities.generate(treeMaker, bitwiseInfixExpression.rightOperand.getValue()));
	}

	public static BinaryTree generateShiftInfixExpression(TreeMaker treeMaker, ShiftInfixExpression shiftInfixExpression) {
		return treeMaker.Binary(OperatorUtilities.lookupShift(shiftInfixExpression.operator.getValue()), ExpressionUtilities.generate(treeMaker, shiftInfixExpression.leftOperand.getValue()), ExpressionUtilities.generate(treeMaker, shiftInfixExpression.rightOperand.getValue()));
	}

	public static BinaryTree generateRelationalInfixExpression(TreeMaker treeMaker, RelationalInfixExpression relationalInfixExpression) {
		return treeMaker.Binary(OperatorUtilities.lookupRelational(relationalInfixExpression.operator.getValue()), ExpressionUtilities.generate(treeMaker, relationalInfixExpression.leftOperand.getValue()), ExpressionUtilities.generate(treeMaker, relationalInfixExpression.rightOperand.getValue()));
	}

	public static UnaryTree generateLogicalComplement(TreeMaker treeMaker, LogicalComplement logicalComplement) {
		return treeMaker.Unary(Tree.Kind.LOGICAL_COMPLEMENT, ExpressionUtilities.generate(treeMaker, logicalComplement.operand.getValue()));
	}

	public static MethodInvocationTree generateMethodInvocation(TreeMaker treeMaker, ExpressionTree expressionTree, CharSequence name, java.util.List<ExpressionTree> arguments) {
		assert name != null;
		ExpressionTree method;
		if (expressionTree != null) {
			method = treeMaker.MemberSelect(expressionTree, name);
		} else {
			method = treeMaker.Identifier(name);
		}
		java.util.List<ExpressionTree> typeArguments = new java.util.LinkedList<ExpressionTree>();
		return treeMaker.MethodInvocation(typeArguments, method, arguments);
	}

	public static MethodInvocationTree generateMethodInvocation(TreeMaker treeMaker, MethodInvocation methodInvocation) {
		ExpressionTree expressionTree = ExpressionUtilities.generate(treeMaker, methodInvocation.expression.getValue());
		CharSequence name = methodInvocation.method.getValue().getName();
		java.util.List<ExpressionTree> arguments = new java.util.LinkedList<ExpressionTree>();
		ArgumentUtilities.generateSimpleArguments(arguments, treeMaker, methodInvocation.requiredArguments);
		ArgumentUtilities.generateKeyedArguments(arguments, treeMaker, methodInvocation.keyedArguments);
		return generateMethodInvocation(treeMaker, expressionTree, name, arguments);
	}

	public static LiteralTree generateNullLiteral(TreeMaker treeMaker, NullLiteral nullLiteral) {
		return treeMaker.Literal(null);
	}

	public static LiteralTree generateNumberLiteral(TreeMaker treeMaker, NumberLiteral numberLiteral) {
		return treeMaker.Literal(numberLiteral.value.getValue());
	}

	public static LiteralTree generateIntegerLiteral(TreeMaker treeMaker, IntegerLiteral integerLiteral) {
		return treeMaker.Literal(integerLiteral.value.getValue());
	}

	public static LiteralTree generateDoubleLiteral(TreeMaker treeMaker, DoubleLiteral doubleLiteral) {
		return treeMaker.Literal(doubleLiteral.value.getValue());
	}

	public static LiteralTree generateFloatLiteral(TreeMaker treeMaker, FloatLiteral floatLiteral) {
		return treeMaker.Literal(floatLiteral.value.getValue());
	}

	public static IdentifierTree generateParameterAccess(TreeMaker treeMaker, ParameterAccess parameterAccess) {
		return treeMaker.Identifier(parameterAccess.parameter.getValue().getName());
	}

	public static LiteralTree generateStringLiteral(TreeMaker treeMaker, StringLiteral stringLiteral) {
		return treeMaker.Literal(stringLiteral.value.getValue());
	}

	public static IdentifierTree generateThisExpression(TreeMaker treeMaker, ThisExpression thisExpression) {
		NetBeansASTGenerator generator = NetBeansASTGenerator.get(treeMaker);
		return treeMaker.Identifier(generator.generateThisIdentifier());
	}

	public static ExpressionTree generateTypeExpression(TreeMaker treeMaker, TypeExpression typeExpression) {
		NetBeansASTGenerator generator = NetBeansASTGenerator.get(treeMaker);
		return generator.generateTypeTree(typeExpression.value.getValue());
	}

	public static MemberSelectTree generateTypeLiteral(TreeMaker treeMaker, TypeLiteral typeLiteral) {
		NetBeansASTGenerator generator = NetBeansASTGenerator.get(treeMaker);
		return treeMaker.MemberSelect(generator.generateTypeTree(typeLiteral.value.getValue()), "class");
	}

	public static IdentifierTree generateLocalAccess(TreeMaker treeMaker, LocalAccess localAccess) {
		String name = localAccess.local.getValue().getValidName(localAccess);
		return treeMaker.Identifier(name);
	}

	public static IdentifierTree generateResourceExpression(TreeMaker treeMaker, ResourceExpression resourceExpression) {
		NetBeansASTGenerator netBeansASTGenerator = NetBeansASTGenerator.get(treeMaker);
		return netBeansASTGenerator.getIdentifier(treeMaker, resourceExpression);
	}

	public static ExpressionTree generateLambdaExpression(TreeMaker treeMaker, LambdaExpression lambdaExpression, AbstractType<?, ?, ?> type) {
		Lambda lambda = lambdaExpression.value.getValue();
		if (lambda instanceof UserLambda) {
			UserLambda userLambda = (UserLambda) lambda;

			CharSequence methodName = null;
			if (type instanceof JavaType) {
				JavaType javaType = (JavaType) type;
				ClassReflectionProxy classReflectionProxy = javaType.getClassReflectionProxy();
				String clsName = classReflectionProxy.getName();
				List<MethodInfo> methodInfos = ClassInfoManager.getMethodInfos(clsName);
				if (methodInfos != null) {
					if (methodInfos.size() == 1) {
						methodName = methodInfos.get(0).getName();
					}
				}
			}
			if (methodName != null) {
				//pass
			} else {
				methodName = "sceneActivated";
			}
//			ArrayList<? extends AbstractMethod> methods = type.getDeclaredMethods();
//			if( methods.size() == 1 ) {
//				methodName = methods.get( 0 ).getName();
//			} else {
//				methodName = "sceneActivated";
//				Logger.severe( methods, methodName );
//			}

			ExpressionTree enclosingExpression = null;
			java.util.List<ExpressionTree> typeArguments = new java.util.LinkedList<ExpressionTree>();


			ExpressionTree identifier = NetBeansASTGenerator.get(treeMaker).generateTypeTree(type);

			CharSequence clsName = "";

			ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(treeMaker);
			java.util.List<TypeParameterTree> typeParameters = new java.util.LinkedList<TypeParameterTree>();
			Tree extendsClause = null;
			java.util.List<Tree> implementsClauses = new java.util.LinkedList<Tree>();
			java.util.List<Tree> memberDecls = new java.util.LinkedList<Tree>();

			NetBeansASTGenerator generator = NetBeansASTGenerator.get(treeMaker);
			generator.pushIsInnerClass(true);
			try {
				memberDecls.add(generator.generateMethodForLambda(userLambda, methodName));
			} finally {
				generator.popIsInnerClass();
			}
			ClassTree classBody = treeMaker.Class(modifiersTree, clsName, typeParameters, extendsClause, implementsClauses, memberDecls);

			java.util.List<ExpressionTree> arguments = Collections.emptyList();
			return treeMaker.NewClass(enclosingExpression, typeArguments, identifier, arguments, classBody);
		} else {
			return null;
		}
	}

	@Deprecated
	public static ExpressionTree generate(TreeMaker treeMaker, Expression expression) {
		return generate(treeMaker, expression, null);
	}

	public static ExpressionTree generate(TreeMaker treeMaker, Expression expression, AbstractType<?, ?, ?> type) {
		assert expression != null;
		ExpressionTree rv;
		if (expression instanceof ArrayAccess) {
			rv = generateArrayAccess(treeMaker, (ArrayAccess) expression);
		} else if (expression instanceof ArrayLength) {
			rv = generateArrayLength(treeMaker, (ArrayLength) expression);
		} else if (expression instanceof ArrayInstanceCreation) {
			rv = generateArrayInstanceCreation(treeMaker, (ArrayInstanceCreation) expression);
		} else if (expression instanceof AssignmentExpression) {
			rv = generateAssignmentExpression(treeMaker, (AssignmentExpression) expression);
		} else if (expression instanceof BooleanLiteral) {
			rv = generateBooleanLiteral(treeMaker, (BooleanLiteral) expression);
		} else if (expression instanceof InstanceCreation) {
			rv = generateInstanceCreation(treeMaker, (InstanceCreation) expression);
		} else if (expression instanceof FieldAccess) {
			rv = generateFieldAccess(treeMaker, (FieldAccess) expression);
		} else if (expression instanceof ShiftInfixExpression) {
			rv = generateShiftInfixExpression(treeMaker, (ShiftInfixExpression) expression);
		} else if (expression instanceof BitwiseInfixExpression) {
			rv = generateBitwiseInfixExpression(treeMaker, (BitwiseInfixExpression) expression);
		} else if (expression instanceof MethodInvocation) {
			rv = generateMethodInvocation(treeMaker, (MethodInvocation) expression);
		} else if (expression instanceof NullLiteral) {
			rv = generateNullLiteral(treeMaker, (NullLiteral) expression);
		} else if (expression instanceof NumberLiteral) {
			rv = generateNumberLiteral(treeMaker, (NumberLiteral) expression);
		} else if (expression instanceof DoubleLiteral) {
			rv = generateDoubleLiteral(treeMaker, (DoubleLiteral) expression);
		} else if (expression instanceof FloatLiteral) {
			rv = generateFloatLiteral(treeMaker, (FloatLiteral) expression);
		} else if (expression instanceof IntegerLiteral) {
			rv = generateIntegerLiteral(treeMaker, (IntegerLiteral) expression);
		} else if (expression instanceof ParameterAccess) {
			rv = generateParameterAccess(treeMaker, (ParameterAccess) expression);
		} else if (expression instanceof StringLiteral) {
			rv = generateStringLiteral(treeMaker, (StringLiteral) expression);
		} else if (expression instanceof ThisExpression) {
			rv = generateThisExpression(treeMaker, (ThisExpression) expression);
		} else if (expression instanceof TypeExpression) {
			rv = generateTypeExpression(treeMaker, (TypeExpression) expression);
		} else if (expression instanceof TypeLiteral) {
			rv = generateTypeLiteral(treeMaker, (TypeLiteral) expression);
		} else if (expression instanceof LocalAccess) {
			rv = generateLocalAccess(treeMaker, (LocalAccess) expression);
		} else if (expression instanceof StringConcatenation) {
			rv = generateStringConcatenation(treeMaker, (StringConcatenation) expression);
		} else if (expression instanceof ResourceExpression) {
			rv = generateResourceExpression(treeMaker, (ResourceExpression) expression);
		} else if (expression instanceof LambdaExpression) {
			rv = generateLambdaExpression(treeMaker, (LambdaExpression) expression, type);
		} else {
			rv = null;
			if (expression instanceof InfixExpression || expression instanceof LogicalComplement) {
				if (expression instanceof ArithmeticInfixExpression) {
					rv = generateArithmeticInfixExpression(treeMaker, (ArithmeticInfixExpression) expression);
				} else if (expression instanceof ConditionalInfixExpression) {
					rv = generateConditionalInfixExpression(treeMaker, (ConditionalInfixExpression) expression);
				} else if (expression instanceof RelationalInfixExpression) {
					rv = generateRelationalInfixExpression(treeMaker, (RelationalInfixExpression) expression);
				} else if (expression instanceof LogicalComplement) {
					rv = generateLogicalComplement(treeMaker, (LogicalComplement) expression);
				}
				if (rv != null) {
					Node parantNode = expression.getParent();
					//todo?
					if (parantNode instanceof StringConcatenation || parantNode instanceof InfixExpression || parantNode instanceof LogicalComplement) {
						rv = treeMaker.Parenthesized(rv);
					}
				}
			}
			assert rv != null;
		}
		return rv;
	}

	public static java.util.List<? extends ExpressionTree> generate(TreeMaker treeMaker, java.util.List<? extends Expression> expressions) {
		java.util.List<ExpressionTree> rv = new java.util.ArrayList<ExpressionTree>();
		for (Expression expression : expressions) {
			rv.add(ExpressionUtilities.generate(treeMaker, expression));
		}
		return rv;
	}
}
