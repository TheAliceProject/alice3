/*******************************************************************************
 * Copyright (c) 2019 Carnegie Mellon University. All rights reserved.
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

import org.lgna.project.code.CodeOrganizer;
import org.lgna.project.code.InstantiableTweedleNode;

/**
 * AstProcessor implementor work through some portion of the code stored in an AST.
 * Implementors may produce interim products and state as they require.
 * The ordering within an AST is to be defined by the dispatch through the nodes, and should be in the same order
 * as they would executed, meaning statements have a strict ordering,
 * but containing nodes,such as methods and type declarations do not.
 *
 */
public interface AstProcessor {
  CodeOrganizer getNewCodeOrganizerForTypeName(String typeName);

  // TODO move in use by NamedUserType and push down to JavaCodeGenerator
  default boolean isPublicStaticFinalFieldGetterDesired() {
    return true;
  }

  void processClass(CodeOrganizer codeOrganizer, NamedUserType userType);

  default void processResourceType(String jointedModelResource) { }

  default void processDynamicResource(String jointedModelResource, String resourceName, InstantiableTweedleNode[] addedJoints) { }

  void processConstructor(NamedUserConstructor constructor);

  void processMethod(UserMethod method);

  void processGetter(Getter getter);

  void processIndexedGetter(ArrayItemGetter getter);

  void processSetter(Setter setter);

  void processIndexedSetter(ArrayItemSetter setter);

  void processField(UserField field);

  void processLocalDeclaration(LocalDeclarationStatement stmt);

  void processExpressionStatement(ExpressionStatement stmt);

  void processReturnStatement(ReturnStatement stmt);

  void processBlock(BlockStatement blockStatement);

  void processConstructorBlock(ConstructorBlockStatement constructor);

  void processSuperConstructor(SuperConstructorInvocationStatement supCon);

  void processThisConstructor(ThisConstructorInvocationStatement thisCon);

  void processConditional(ConditionalStatement stmt);

  void processCountLoop(CountLoop loop);

  void processForEach(AbstractForEachLoop loop);

  void processWhileLoop(WhileLoop loop);

  void processDoInOrder(DoInOrder doInOrder);

  void processDoTogether(DoTogether doTogether);

  void processEachInTogether(AbstractEachInTogether eachInTogether);

  void processLambda(UserLambda lambda);

  void processExpression(Expression expression);

  void processMethodCall(MethodInvocation invocation);

  void processKeyedArgument(JavaKeyedArgument arg);

  void processAssignmentExpression(AssignmentExpression assignment);

  void processConcatenation(StringConcatenation concat);

  void processLogicalComplement(LogicalComplement complement);

  void processInfixExpression(InfixExpression infixExpression);

  void processInstantiation(InstanceCreation creation);

  void processArrayInstantiation(ArrayInstanceCreation creation);

  void processArrayAccess(ArrayAccess access);

  void processArrayLength(ArrayLength arrayLength);

  void processFieldAccess(FieldAccess access);

  void processNull();

  void processThisReference();

  void processSuperReference();

  void processBoolean(boolean b);

  void processInt(int n);

  void processFloat(float f);

  void processDouble(double d);

  void processEscapedStringLiteral(StringLiteral literal);

  void processTypeName(AbstractType<?, ?, ?> type);

  void processTypeLiteral(TypeLiteral typeLiteral);

  void processResourceExpression(ResourceExpression resourceExpression);

  void processMultiLineComment(String comment);

  void processVariableIdentifier(AbstractDeclaration variable);
}
