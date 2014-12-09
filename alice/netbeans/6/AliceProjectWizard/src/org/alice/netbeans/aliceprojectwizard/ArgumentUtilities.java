/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alice.netbeans.aliceprojectwizard;

import com.sun.source.tree.ExpressionTree;
import org.netbeans.api.java.source.TreeMaker;
import org.lgna.project.ast.*;

/**
 *
 * @author dennisc
 */
public class ArgumentUtilities {

	public static java.util.List<ExpressionTree> generateSimpleArguments(java.util.List<ExpressionTree> rv, TreeMaker treeMaker, SimpleArgumentListProperty argumentListProperty) {
		for (SimpleArgument argument : argumentListProperty) {
			rv.add(ExpressionUtilities.generate(treeMaker, argument.expression.getValue(), argument.parameter.getValue().getValueType()));
		}
		return rv;
	}

	public static java.util.List<ExpressionTree> generateKeyedArguments(java.util.List<ExpressionTree> rv, TreeMaker treeMaker, KeyedArgumentListProperty argumentListProperty) {
		for (JavaKeyedArgument argument : argumentListProperty) {
			Expression expression = argument.expression.getValue();
			if (expression instanceof MethodInvocation) {
				MethodInvocation methodInvocation = (MethodInvocation) expression;

				JavaMethod keyMethod = argument.getKeyMethod();
				String typeName;
				AbstractType<?, ?, ?> factoryType = AstUtilities.getKeywordFactoryType(argument);
				if (factoryType != null) {
					typeName = factoryType.getName();
				} else {
					String methodName = argument.parameter.getValue().getCode().getName();
					typeName = Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
				}
				ExpressionTree typeExpression = treeMaker.Identifier(typeName);
				java.util.List<ExpressionTree> arguments = new java.util.LinkedList<ExpressionTree>();
				generateSimpleArguments(arguments, treeMaker, methodInvocation.requiredArguments);

				java.util.List<ExpressionTree> typeArguments = java.util.Collections.emptyList();
				ExpressionTree method = treeMaker.MemberSelect(typeExpression, keyMethod.getName());
				rv.add(treeMaker.MethodInvocation(typeArguments, method, arguments));
			} else {
				rv.add(ExpressionUtilities.generate(treeMaker, argument.expression.getValue(), argument.parameter.getValue().getValueType()));
			}
		}
		return rv;
	}
}
