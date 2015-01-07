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

import com.sun.source.tree.AssertTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.ExpressionStatementTree;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import java.util.List;
import org.netbeans.api.java.source.TreeMaker;

import org.lgna.project.ast.*;

public class StatementUtilities {

	public static ReturnTree generateGetterStatement(TreeMaker treeMaker, Getter getter) {
		return treeMaker.Return(
				treeMaker.MemberSelect(
				treeMaker.Identifier("this"),
				getter.getField().getName()));
	}

	public static ExpressionStatementTree generateSetterStatement(TreeMaker treeMaker, Setter setter) {
		return treeMaker.ExpressionStatement(
				treeMaker.Assignment(
				treeMaker.MemberSelect(
				treeMaker.Identifier("this"),
				setter.getField().getName()),
				treeMaker.Identifier(setter.getRequiredParameters().get(0).getName())));
	}

	public static AssertTree generateAssertStatement(TreeMaker treeMaker, AssertStatement assertStatement) {
		ExpressionTree expressionTree = ExpressionUtilities.generate(treeMaker, assertStatement.expression.getValue());
		ExpressionTree messageTree = ExpressionUtilities.generate(treeMaker, assertStatement.message.getValue());
		return treeMaker.Assert(expressionTree, messageTree);
	}

	public static BlockTree generateBlockStatement(TreeMaker treeMaker, BlockStatement blockStatement) {
		java.util.List<StatementTree> statements = new java.util.LinkedList<StatementTree>();
		if (blockStatement instanceof ConstructorBlockStatement) {
			ConstructorBlockStatement constructorBlockStatement = (ConstructorBlockStatement) blockStatement;
			StatementTree statementTree = StatementUtilities.generateConstructorInvocationStatement(treeMaker, constructorBlockStatement.constructorInvocationStatement.getValue());
			;
			statements.add(statementTree);
		}
		List<org.netbeans.api.java.source.Comment> pendingComments = Lists.newLinkedList();
		StatementTree previousStatementTree = null;
		for (Statement statement : blockStatement.statements) {
			if (statement instanceof Comment) {
				Comment comment = (Comment) statement;
				String text = comment.text.getValue();
				org.netbeans.api.java.source.Comment cmt = org.netbeans.api.java.source.Comment.create(org.netbeans.api.java.source.Comment.Style.LINE, text);

				if (previousStatementTree != null) {
					treeMaker.addComment(previousStatementTree, cmt, false);
				} else {
					pendingComments.add(cmt);
				}
			} else {
				if (statement.isEnabled.getValue()) {
					StatementTree[] statementTrees = StatementUtilities.generate(treeMaker, statement);
					if (statementTrees != null) {
						for (StatementTree statementTree : statementTrees) {
							if (statementTree != null) {
								for (org.netbeans.api.java.source.Comment cmt : pendingComments) {
									treeMaker.addComment(statementTree, cmt, true);
								}
								pendingComments.clear();
								statements.add(statementTree);
							}
							previousStatementTree = statementTree;
						}
					} else {
						edu.cmu.cs.dennisc.print.PrintUtilities.println("WARNING:", statement);
					}
				} else {
					//todo: generate comment
				}
			}
		}

		if (pendingComments.size() > 0) {
			Logger.todo(pendingComments);
		}
		//todo
		boolean isStatic = false;
		BlockTree rv = treeMaker.Block(statements, isStatic);

//		for (Statement statement : blockStatement.statements) {
//			if( statement instanceof Comment ) {
//				Comment comment = (Comment)statement;
//				String text = comment.text.getValue();
//				org.netbeans.api.java.source.Comment cmt = org.netbeans.api.java.source.Comment.create( org.netbeans.api.java.source.Comment.Style.LINE, -1, -1, 0, text );
//				boolean isPreceding = true;
//				treeMaker.addComment( rv, cmt, isPreceding);
//				JOptionPane.showMessageDialog(null, "comment text: " + text );
//			}
//		}
		return rv;
	}

	public static IfTree generateConditionalStatement(TreeMaker treeMaker, ConditionalStatement conditionalStatement) {
//		for( BooleanExpressionBodyPair booleanExpressionBodyPair : conditionalStatement.booleanExpressionBodyPairs ) {
//			ExpressionTree expressionTree = generate(booleanExpressionBodyPair.expression.getValue());
//		}
		assert conditionalStatement.booleanExpressionBodyPairs.size() == 1;
		ExpressionTree expressionTree = ExpressionUtilities.generate(treeMaker, conditionalStatement.booleanExpressionBodyPairs.get(0).expression.getValue());
		StatementTree trueStatementTree = StatementUtilities.generateBlockStatement(treeMaker, conditionalStatement.booleanExpressionBodyPairs.get(0).body.getValue());
		StatementTree falseStatementTree = StatementUtilities.generateBlockStatement(treeMaker, conditionalStatement.elseBody.getValue());
		return treeMaker.If(expressionTree, trueStatementTree, falseStatementTree);
	}

	public static ClassTree generateAnonymousClass(TreeMaker treeMaker) {
		CharSequence name = "";

		ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(treeMaker);
		java.util.List<TypeParameterTree> typeParameters = new java.util.LinkedList<TypeParameterTree>();
		Tree extendsClause = null;
		java.util.List<Tree> implementsClauses = new java.util.LinkedList<Tree>();
		java.util.List<Tree> memberDecls = new java.util.LinkedList<Tree>();

		//memberDecls.add(generate(runMethod));

		return treeMaker.Class(modifiersTree, name, typeParameters, extendsClause, implementsClauses, memberDecls);
	}
	private static final JavaType THREAD_UTILITIES_TYPE = JavaType.getInstance(org.lgna.common.ThreadUtilities.class);
	private static final boolean IS_IMPORT_STATIC_FOR_THREAD_UTILITIES_METHODS_DESIRED = true;

	private static ExpressionTree createThreadUtilitiesTypeTree(TreeMaker treeMaker) {
		if (IS_IMPORT_STATIC_FOR_THREAD_UTILITIES_METHODS_DESIRED) {
			return null;
		} else {
			return ExpressionUtilities.generate(treeMaker, new TypeExpression(THREAD_UTILITIES_TYPE));
		}
	}

	public static ExpressionStatementTree generateDoTogether(TreeMaker treeMaker, DoTogether doTogether) {
		ExpressionTree threadUtilitiesTypeTree = createThreadUtilitiesTypeTree(treeMaker);
		MethodInvocationTree invokeAndWaitMethodInvocationTree = ExpressionUtilities.generateMethodInvocation(treeMaker, threadUtilitiesTypeTree, "doTogether", new java.util.LinkedList<ExpressionTree>());
		NetBeansASTGenerator generator = NetBeansASTGenerator.get(treeMaker);
		for (Statement statement : doTogether.body.getValue().statements) {
			if (statement instanceof Comment) {
				//todo
			} else {
				ExpressionTree enclosingExpression = null;
				java.util.List<ExpressionTree> typeArguments = new java.util.LinkedList<ExpressionTree>();
				ExpressionTree identifier = treeMaker.Identifier("Runnable");
				ClassTree classBody = StatementUtilities.generateAnonymousClass(treeMaker);
				BlockStatement blockStatement;
				if (statement instanceof BlockStatement) {
					blockStatement = (BlockStatement) statement;
				} else {
					blockStatement = new BlockStatement(statement);
				}
				generator.pushIsInnerClass(true);
				try {
					MethodTree runMethodTree = generator.generateMethod(new UserMethod("run", JavaType.VOID_TYPE, new UserParameter[]{}, blockStatement));
					classBody = treeMaker.addClassMember(classBody, runMethodTree);
					java.util.List<ExpressionTree> arguments = new java.util.LinkedList<ExpressionTree>();
					NewClassTree newClassTree = treeMaker.NewClass(enclosingExpression, typeArguments, identifier, arguments, classBody);
					invokeAndWaitMethodInvocationTree = treeMaker.addMethodInvocationArgument(invokeAndWaitMethodInvocationTree, newClassTree);
				} finally {
					generator.popIsInnerClass();
				}
			}
		}
		return treeMaker.ExpressionStatement(invokeAndWaitMethodInvocationTree);
	}

	public static ExpressionStatementTree generateExpressionStatement(TreeMaker treeMaker, ExpressionStatement expressionStatement) {
		return treeMaker.ExpressionStatement(ExpressionUtilities.generate(treeMaker, expressionStatement.expression.getValue()));
	}

	public static ReturnTree generateReturnStatement(TreeMaker treeMaker, ReturnStatement returnStatement) {
		return treeMaker.Return(ExpressionUtilities.generate(treeMaker, returnStatement.expression.getValue()));
	}

	private static VariableTree generateLocalDeclarationStatement(TreeMaker treeMaker, UserLocal local, ExpressionTree initializerTree) {
		return NetBeansASTGenerator.get(treeMaker).generateLocalDeclarationStatement(local, initializerTree);
	}

	private static VariableTree generateLocalDeclarationStatement(TreeMaker treeMaker, UserLocal local, Expression initializer) {
		return generateLocalDeclarationStatement(treeMaker, local, ExpressionUtilities.generate(treeMaker, initializer));
	}

	public static VariableTree generateLocalDeclarationStatement(TreeMaker treeMaker, LocalDeclarationStatement constantDeclarationStatement) {
		return generateLocalDeclarationStatement(treeMaker, constantDeclarationStatement.local.getValue(), constantDeclarationStatement.initializer.getValue());
	}

	public static StatementTree[] generateCountLoop(TreeMaker treeMaker, CountLoop countLoop) {
		UserLocal constant = countLoop.constant.getValue();
		UserLocal variable = countLoop.variable.getValue();
//		CharSequence nName = constant.getValidName(countLoop);
//		CharSequence iName = variable.getValidName(countLoop);
		CharSequence nName = NetBeansASTGenerator.get(treeMaker).getValidName(constant, countLoop);
		CharSequence iName = NetBeansASTGenerator.get(treeMaker).getValidName(variable, countLoop);

		ExpressionTree nTree = null;
		Expression countExpression = countLoop.count.getValue();
		if (countExpression instanceof IntegerLiteral) {
			IntegerLiteral integerLiteral = (IntegerLiteral) countExpression;
			nTree = treeMaker.Literal(integerLiteral.value.getValue());
		} else if (countExpression instanceof LocalAccess) {
			LocalAccess localAccess = (LocalAccess) countExpression;
			UserLocal local = localAccess.local.getValue();
			if (local.isFinal.getValue()) {
				nTree = treeMaker.Identifier(local.name.getValue());
			}
		}

		VariableTree constantDeclarationStatementTree;
		if (nTree != null) {
			constantDeclarationStatementTree = null;
		} else {
			constantDeclarationStatementTree = generateLocalDeclarationStatement(treeMaker, constant, countLoop.count.getValue());
			nTree = treeMaker.Identifier(nName);
		}


		VariableTree variableTree = generateLocalDeclarationStatement(treeMaker, variable, treeMaker.Literal(0));

		java.util.List<StatementTree> initializers = new java.util.LinkedList<StatementTree>();
		initializers.add(variableTree);

		//ExpressionTree condition = treeMaker.Binary(Tree.Kind.LESS_THAN, treeMaker.MemberSelect(treeMaker.Identifier(iName), "value"), treeMaker.Identifier(nName));
		ExpressionTree condition = treeMaker.Binary(Tree.Kind.LESS_THAN, treeMaker.Identifier(iName), nTree);
		java.util.List<ExpressionStatementTree> updaters = new java.util.LinkedList<ExpressionStatementTree>();
		//updaters.add(treeMaker.ExpressionStatement(treeMaker.Unary(Tree.Kind.POSTFIX_INCREMENT, treeMaker.MemberSelect(treeMaker.Identifier(iName), "value"))));
		updaters.add(treeMaker.ExpressionStatement(treeMaker.Unary(Tree.Kind.POSTFIX_INCREMENT, treeMaker.Identifier(iName))));

		StatementTree statement = StatementUtilities.generateBlockStatement(treeMaker, countLoop.body.getValue());
		ForLoopTree forLoopTree = treeMaker.ForLoop(initializers, condition, updaters, statement);

		if (constantDeclarationStatementTree != null) {
			return new StatementTree[]{constantDeclarationStatementTree, forLoopTree};
//			java.util.List<StatementTree> statements = new java.util.LinkedList<StatementTree>();
//			statements.add(constantDeclarationStatementTree);
//			statements.add(forLoopTree);
//			boolean isStatic = false;
//			return new StatementTree[] { treeMaker.Block(statements, isStatic) };
		} else {
			return new StatementTree[]{forLoopTree};
		}
	}

	private static EnhancedForLoopTree generateForEachLoop(TreeMaker treeMaker, AbstractForEachLoop forEachLoop, Expression expression) {
		UserLocal item = forEachLoop.item.getValue();
		VariableTree variableTree = NetBeansASTGenerator.get(treeMaker).generateEachVariableDeclaration(item);
		ExpressionTree expressionTree = ExpressionUtilities.generate(treeMaker, expression);
		StatementTree statement = StatementUtilities.generateBlockStatement(treeMaker, forEachLoop.body.getValue());
		return treeMaker.EnhancedForLoop(variableTree, expressionTree, statement);
	}

	public static EnhancedForLoopTree generateForEachInArrayLoop(TreeMaker treeMaker, ForEachInArrayLoop forEachInArrayLoop) {
		return generateForEachLoop(treeMaker, forEachInArrayLoop, forEachInArrayLoop.array.getValue());
	}

	public static EnhancedForLoopTree generateForEachInIterableLoop(TreeMaker treeMaker, ForEachInIterableLoop forEachInIterableLoop) {
		return generateForEachLoop(treeMaker, forEachInIterableLoop, forEachInIterableLoop.iterable.getValue());
	}
	private static final JavaType EACH_IN_TOGETHER_RUNNABLE_TYPE = JavaType.getInstance(org.lgna.common.EachInTogetherRunnable.class);

	private static StatementTree generateEachInTogether(TreeMaker treeMaker, AbstractEachInTogether forEachTogether, Expression expression) {
		NetBeansASTGenerator generator = NetBeansASTGenerator.get(treeMaker);
		ExpressionTree threadUtilitiesTypeTree = createThreadUtilitiesTypeTree(treeMaker);

		java.util.List<ExpressionTree> arguments = new java.util.LinkedList<ExpressionTree>();

		UserLocal variable = forEachTogether.item.getValue();

		generator.pushIsInnerClass(true);
		try {
			ExpressionTree enclosingExpression = null;
			java.util.List<ExpressionTree> typeArguments = new java.util.LinkedList<ExpressionTree>();

			ExpressionTree baseTypeTree = ExpressionUtilities.generate(treeMaker, new TypeExpression(EACH_IN_TOGETHER_RUNNABLE_TYPE));
			java.util.List<ExpressionTree> parameterizedTypeArguments = new java.util.LinkedList<ExpressionTree>();
			parameterizedTypeArguments.add(ExpressionUtilities.generate(treeMaker, new TypeExpression(variable.valueType.getValue())));
			Tree parameterizedTypeTree = treeMaker.ParameterizedType(baseTypeTree, parameterizedTypeArguments);
			ExpressionTree forEachRunnableTypeTree = treeMaker.Identifier(parameterizedTypeTree.toString());

			ClassTree classBody = StatementUtilities.generateAnonymousClass(treeMaker);

			MethodTree runMethodTree = generator.generateMethod(new UserMethod("run", JavaType.VOID_TYPE, new UserParameter[]{new UserParameter(variable.getValidName(forEachTogether), variable.valueType.getValue())}, forEachTogether.body.getValue()));
			classBody = treeMaker.addClassMember(classBody, runMethodTree);

			NewClassTree newClassTree = treeMaker.NewClass(enclosingExpression, typeArguments, forEachRunnableTypeTree, new java.util.LinkedList<ExpressionTree>(), classBody);
			arguments.add(newClassTree);
		} finally {
			generator.popIsInnerClass();
		}
		arguments.add(ExpressionUtilities.generate(treeMaker, expression));

		MethodInvocationTree invokeAndWaitMethodInvocationTree = ExpressionUtilities.generateMethodInvocation(treeMaker, threadUtilitiesTypeTree, "eachInTogether", arguments);

		return treeMaker.ExpressionStatement(invokeAndWaitMethodInvocationTree);
	}

	public static StatementTree generateEachInArrayTogether(TreeMaker treeMaker, EachInArrayTogether eachInArrayTogether) {
		return generateEachInTogether(treeMaker, eachInArrayTogether, eachInArrayTogether.array.getValue());
	}

	public static StatementTree generateEachInIterableTogether(TreeMaker treeMaker, EachInIterableTogether eachInIterableTogether) {
		return generateEachInTogether(treeMaker, eachInIterableTogether, eachInIterableTogether.iterable.getValue());
	}

	public static WhileLoopTree generateWhileLoop(TreeMaker treeMaker, WhileLoop whileLoop) {
		ExpressionTree expressionTree = ExpressionUtilities.generate(treeMaker, whileLoop.conditional.getValue());
		StatementTree[] statementTrees = StatementUtilities.generate(treeMaker, whileLoop.body.getValue());
		assert statementTrees != null;
		assert statementTrees.length == 1;
		assert statementTrees[ 0] != null;
		return treeMaker.WhileLoop(expressionTree, statementTrees[ 0]);
	}

	public static StatementTree generateDoInOrder(TreeMaker treeMaker, DoInOrder doInOrder) {
		StatementTree[] statementTrees = StatementUtilities.generate(treeMaker, doInOrder.body.getValue());
		assert statementTrees != null;
		assert statementTrees.length == 1;
		assert statementTrees[ 0] != null;
		return statementTrees[ 0];
	}

	public static StatementTree generateConstructorInvocationStatement(TreeMaker treeMaker, ConstructorInvocationStatement constructorInvocationStatement) {
		java.util.List<ExpressionTree> typeArguments = new java.util.LinkedList<ExpressionTree>();
		ExpressionTree literal;
		if (constructorInvocationStatement instanceof SuperConstructorInvocationStatement) {
			literal = treeMaker.Identifier("super");
		} else {
			literal = treeMaker.Identifier("this");
		}
		java.util.List<ExpressionTree> arguments = new java.util.LinkedList<ExpressionTree>();
		ArgumentUtilities.generateSimpleArguments(arguments, treeMaker, constructorInvocationStatement.requiredArguments);
		ArgumentUtilities.generateKeyedArguments(arguments, treeMaker, constructorInvocationStatement.keyedArguments);
		return treeMaker.ExpressionStatement(
				treeMaker.MethodInvocation(typeArguments, literal, arguments));
	}

	public static StatementTree[] generate(TreeMaker treeMaker, Statement statement) {
		StatementTree[] rv = null;
		StatementTree rvStatement;
		if (statement instanceof AssertStatement) {
			rvStatement = generateAssertStatement(treeMaker, (AssertStatement) statement);
		} else if (statement instanceof BlockStatement) {
			rvStatement = generateBlockStatement(treeMaker, (BlockStatement) statement);
		} else if (statement instanceof ConditionalStatement) {
			rvStatement = generateConditionalStatement(treeMaker, (ConditionalStatement) statement);
		} else if (statement instanceof CountLoop) {
			rvStatement = null;
			rv = generateCountLoop(treeMaker, (CountLoop) statement);
		} else if (statement instanceof DoTogether) {
			rvStatement = generateDoTogether(treeMaker, (DoTogether) statement);
		} else if (statement instanceof ExpressionStatement) {
			rvStatement = generateExpressionStatement(treeMaker, (ExpressionStatement) statement);
		} else if (statement instanceof ForEachInArrayLoop) {
			rvStatement = generateForEachInArrayLoop(treeMaker, (ForEachInArrayLoop) statement);
		} else if (statement instanceof EachInArrayTogether) {
			rvStatement = generateEachInArrayTogether(treeMaker, (EachInArrayTogether) statement);
		} else if (statement instanceof ForEachInArrayLoop) {
			rvStatement = generateForEachInIterableLoop(treeMaker, (ForEachInIterableLoop) statement);
		} else if (statement instanceof EachInIterableTogether) {
			rvStatement = generateEachInIterableTogether(treeMaker, (EachInIterableTogether) statement);
		} else if (statement instanceof ReturnStatement) {
			rvStatement = generateReturnStatement(treeMaker, (ReturnStatement) statement);
		} else if (statement instanceof LocalDeclarationStatement) {
			rvStatement = generateLocalDeclarationStatement(treeMaker, (LocalDeclarationStatement) statement);
		} else if (statement instanceof WhileLoop) {
			rvStatement = generateWhileLoop(treeMaker, (WhileLoop) statement);
		} else if (statement instanceof DoInOrder) {
			rvStatement = generateDoInOrder(treeMaker, (DoInOrder) statement);
		} else if (statement instanceof Comment) {
			//todo
			rvStatement = null;
		} else {
			assert false : statement;
			rvStatement = null;
		}
		if (rv != null) {
			//pass
		} else {
			if (statement != null) {
				rv = new StatementTree[]{rvStatement};
			} else {
				rv = null;
			}
		}
		return rv;
	}
}
