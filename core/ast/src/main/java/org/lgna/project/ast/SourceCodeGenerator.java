/*******************************************************************************
 * Copyright (c) 2018 Carnegie Mellon University. All rights reserved.
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

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.apache.commons.text.StringEscapeUtils;
import org.lgna.project.code.ProcessableNode;
import org.lgna.project.code.CodeOrganizer;
import org.lgna.project.code.PrecedentedOperation;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

public abstract class SourceCodeGenerator implements AstProcessor {

  public SourceCodeGenerator(Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitionMap, CodeOrganizer.CodeOrganizerDefinition defaultCodeDefinitionOrganizer) {
    codeOrganizerDefinitions = Collections.unmodifiableMap(codeOrganizerDefinitionMap);
    defaultCodeOrganizerDefn = defaultCodeDefinitionOrganizer;
  }

  public String getText() {
    return String.valueOf(getCodeStringBuilder());
  }

  protected StringBuilder getCodeStringBuilder() {
    return codeStringBuilder;
  }

  // ** Class structure **

  @Override
  public CodeOrganizer getNewCodeOrganizerForTypeName(String typeName) {
    return new CodeOrganizer(codeOrganizerDefinitions.getOrDefault(typeName, defaultCodeOrganizerDefn));
  }

  @Override
  public void processClass(CodeOrganizer codeOrganizer, NamedUserType userType) {
    appendClassHeader(userType);
    for (Map.Entry<String, List<ProcessableNode>> entry : codeOrganizer.getOrderedSections().entrySet()) {
      if (!entry.getValue().isEmpty()) {
        appendSection(codeOrganizer, userType, entry);
      }
    }
    appendClassFooter(userType.getName());
  }

  protected void appendSection(CodeOrganizer codeOrganizer, NamedUserType userType, Map.Entry<String, List<ProcessableNode>> entry) {
    for (ProcessableNode item : entry.getValue()) {
      item.process(this);
    }
  }

  protected abstract void appendClassHeader(NamedUserType userType);

  protected void appendClassFooter(String userTypeName) {
    closeBlock();
  }

  // ** Methods and Fields **

  @Override
  public void processMethod(UserMethod method) {
    appendMethodHeader(method);
    appendStatement(method.body.getValue());
  }

  public abstract void appendMethodHeader(AbstractMethod method);

  protected void appendParameters(Code code) {
    parenthesize(() -> appendParameters(code.getRequiredParameters()));
  }

  private void appendParameters(List<? extends AbstractParameter> requiredParameters) {
    String prefix = "";
    int i = 0;
    for (AbstractParameter parameter : requiredParameters) {
      appendString(prefix);
      appendParameterTypeName(parameter.getValueType());
      appendSpace();
      String parameterName = identifierName(parameter);
      appendString(parameterName != null ? parameterName : "p" + i);
      prefix = getListSeparator();
      i += 1;
    }
  }

  protected String getListSeparator() {
    return ",";
  }

  @Override
  public void processGetter(Getter getter) {
    UserField field = getter.getField();
    appendMethodHeader(getter);
    openBlock();
    appendSingleCodeLine(() -> {
      appendString("return this.");
      processVariableIdentifier(field);
    });
    closeBlock();
  }

  @Override
  public void processIndexedGetter(ArrayItemGetter getter) {
    UserField field = getter.getField();
    appendMethodHeader(getter);
    openBlock();
    appendSingleCodeLine(() -> {
      appendString("return this.");
      processVariableIdentifier(field);
      appendString("[index]");
    });
    closeBlock();
  }

  @Override
  public void processSetter(Setter setter) {
    UserField field = setter.getField();
    appendMethodHeader(setter);
    openBlock();
    appendSingleCodeLine(() -> {
      appendString("this.");
      processVariableIdentifier(field);
      appendAssignmentOperator();
      processVariableIdentifier(field);
    });
    closeBlock();
  }

  @Override
  public void processIndexedSetter(ArrayItemSetter setter) {
    UserField field = setter.getField();
    appendMethodHeader(setter);
    openBlock();
    appendSingleCodeLine(() -> {
      appendString("this.");
      processVariableIdentifier(field);
      appendString("[index]");
      appendAssignmentOperator();
      appendString("value");
    });
    closeBlock();
  }

  @Override
  public void processField(UserField field) {
    appendSingleCodeLine(() -> {
      processTypeName(field.valueType.getValue());
      appendSpace();
      processVariableIdentifier(field);
      appendAssignmentOperator();
      processExpression(field.initializer.getValue());
    });
  }

  // ** Statements **

  protected final void appendStatement(Statement stmt) {
    boolean isDisabled = !stmt.isEnabled.getValue();
    if (isDisabled) {
      pushStatementDisabled();
    }
    try {
      stmt.process(this);
    } finally {
      if (isDisabled) {
        popStatementDisabled();
      }
    }
  }

  @Override
  public void processExpressionStatement(ExpressionStatement stmt) {
    processSingleStatement(stmt, () -> processExpression(stmt.expression.getValue()));
  }

  @Override
  public void processReturnStatement(ReturnStatement stmt) {
    processSingleStatement(stmt, () -> {
      appendString("return ");
      processExpression(stmt.expression.getValue());
    });
  }

  @Override
  public void processBlock(BlockStatement blockStatement) {
    openBlock();
    appendBody(blockStatement);
    closeBlockInline();
  }

  @Override
  public void processConstructorBlock(ConstructorBlockStatement constructor) {
    openBlock();
    appendStatement(constructor.constructorInvocationStatement.getValue());
    appendBody(constructor);
    closeBlock();
  }

  private void appendBody(BlockStatement block) {
    for (Statement statement : block.statements) {
      appendStatement(statement);
    }
  }

  @Override
  public void processThisConstructor(ThisConstructorInvocationStatement thisCon) {
    processSingleStatement(thisCon, () -> {
      processThisReference();
      appendArguments(thisCon);
    });
  }

  void appendArguments(ArgumentOwner argumentOwner) {
    parenthesize(() -> appendEachArgument(argumentOwner));
  }

  protected void appendEachArgument(ArgumentOwner argumentOwner) {
    String prefix = "";
    for (SimpleArgument argument : argumentOwner.getRequiredArgumentsProperty()) {
      appendString(prefix);
      appendArgument(argument);
      prefix = getListSeparator();
    }
    for (SimpleArgument argument : argumentOwner.getVariableArgumentsProperty()) {
      appendString(prefix);
      appendArgument(argument);
      prefix = getListSeparator();
    }
    for (JavaKeyedArgument argument : argumentOwner.getKeyedArgumentsProperty()) {
      appendString(prefix);
      appendArgument(argument);
      prefix = getListSeparator();
    }
  }

  private void appendArgument(SimpleArgument arg) {
    pushAndAppendArgument(arg);
  }

  protected void appendArgument(JavaKeyedArgument arg) {
    pushAndAppendArgument(arg);
  }

  private void pushAndAppendArgument(AbstractArgument argument) {
    AbstractParameter parameter = argument.parameter.getValue();
    AbstractType<?, ?, ?> type = argument.getExpressionTypeForParameterType(parameter.getValueType());
    pushTypeForLambda(type);
    try {
      processArgument(parameter, argument);
    } finally {
      assert popTypeForLambda() == type;
    }
  }

  public abstract void processArgument(AbstractParameter parameter, AbstractArgument argument);

  public void processSingleStatement(Statement stmt, Runnable appender) {
    appender.run();
    appendStatementCompletion(stmt);
  }

  protected void appendSingleCodeLine(Runnable appender) {
    appender.run();
    appendStatementCompletion();
  }

  protected void appendStatementCompletion(Statement stmt) {
    appendChar(';');
  }

  protected void appendStatementCompletion() {
    appendChar(';');
  }

  protected void pushStatementDisabled() {
    ++statementDisabledCount;
  }

  protected void popStatementDisabled() {
    --statementDisabledCount;
  }

  boolean isCodeNowDisabled() {
    return statementDisabledCount == 1;
  }

  boolean isCodeNowEnabled() {
    return statementDisabledCount == 0;
  }

  // ** Code Flow **

  protected void appendCodeFlowStatement(Statement stmt, Runnable appender) {
    appender.run();
  }

  @Override
  public void processConditional(ConditionalStatement stmt) {
    appendCodeFlowStatement(stmt, () -> {
      String text = "if";
      for (BooleanExpressionBodyPair booleanExpressionBodyPair : stmt.booleanExpressionBodyPairs) {
        appendString(text);
        parenthesize(() -> processExpression(booleanExpressionBodyPair.expression.getValue()));
        appendStatement(booleanExpressionBodyPair.body.getValue());
        text = " else if";
      }
      appendString(" else");
      appendStatement(stmt.elseBody.getValue());
    });
  }

  @Override
  public void processForEach(AbstractForEachLoop loop) {
    appendCodeFlowStatement(loop, () -> {
      UserLocal itemValue = loop.item.getValue();
      final Expression items = loop.getArrayOrIterableProperty().getValue();
      appendForEachToken();
      appendEachItemsClause(itemValue, items);
      appendStatement(loop.body.getValue());
    });
  }

  protected abstract void appendForEachToken();

  protected void appendEachItemsClause(UserLocal itemValue, Expression items) {
    parenthesize(() -> {
      processTypeName(itemValue.getValueType());
      appendSpace();
      appendString(identifierName(itemValue));
      appendInEachToken();
      processExpression(items);
    });
  }

  protected abstract void appendInEachToken();

  @Override
  public void processWhileLoop(WhileLoop loop) {
    appendCodeFlowStatement(loop, () -> {
      appendString("while ");
      parenthesize(() -> processExpression(loop.conditional.getValue()));
      appendStatement(loop.body.getValue());
    });
  }

  private AbstractType<?, ?, ?> peekTypeForLambda() {
    return typeForLambdaStack.peek();
  }

  private void pushTypeForLambda(AbstractType<?, ?, ?> type) {
    typeForLambdaStack.push(type);
  }

  private AbstractType<?, ?, ?> popTypeForLambda() {
    return typeForLambdaStack.pop();
  }

  @Override
  public void processLambda(UserLambda lambda) {
    AbstractType<?, ?, ?> type = peekTypeForLambda();
    AbstractMethod singleAbstractMethod = AstUtilities.getSingleAbstractMethod(type);
    if (isLambdaSupported()) {
      appendParameters(lambda);
      appendString(" ->");
    } else {
      appendString("new ");
      processTypeName(type);
      appendString("()");
      openBlock();
      appendMethodHeader(singleAbstractMethod);
    }
    appendStatement(lambda.body.getValue());
    if (!isLambdaSupported()) {
      closeBlock();
    }
  }

  boolean isLambdaSupported() {
    return true;
  }

  // ** Expressions **

  @Override
  public void processExpression(Expression expression) {
    expression.process(this);
  }

  @Override
  public void processMethodCall(MethodInvocation invocation) {
    appendTargetAndMethodName(invocation.expression.getValue(), invocation.method.getValue());
    appendArguments(invocation);
  }

  protected void appendTargetAndMethodName(Expression target, AbstractMethod method) {
    appendTargetAndMember(target, method.getName(), method.getReturnType());
  }

  protected void appendTargetAndMember(Expression target, String member, AbstractType<?, ?, ?> returnType) {
    processExpression(target);
    appendAccessSeparator();
    appendString(member);
  }

  @Override
  public void processAssignmentExpression(AssignmentExpression assignment) {
    appendPrecedented(assignment, () -> {
      processExpression(assignment.leftHandSide.getValue());
      final AssignmentExpression.Operator assignmentOp = assignment.operator.getValue();
      if (AssignmentExpression.Operator.ASSIGN.equals(assignmentOp)) {
        appendAssignmentOperator();
      } else {
        // Only basic assignment is used in existing Alice code.
        Logger.errln("Use of unexpected assignment operator " + assignmentOp + " in " + assignment);
        appendString(assignmentOp.toString());
      }
      processExpression(assignment.rightHandSide.getValue());
    });
  }

  @Override
  public void processConcatenation(StringConcatenation concat) {
    appendPrecedented(concat, () -> {
      processExpression(concat.leftOperand.getValue());
      appendConcatenationOperator();
      processExpression(concat.rightOperand.getValue());
    });
  }

  protected abstract void appendConcatenationOperator();

  @Override
  public void processLogicalComplement(LogicalComplement complement) {
    appendPrecedented(complement, () -> {
      appendChar('!');
      processExpression(complement.operand.getValue());
    });
  }

  @Override
  public void processInfixExpression(InfixExpression infixExpression) {
    appendPrecedented(infixExpression, () -> {
      processExpression(infixExpression.leftOperand.getValue());
      appendString(infixExpression.getOperatorValue().getSymbol());
      processExpression(infixExpression.rightOperand.getValue());
    });
  }

  @Override
  public void processInstantiation(InstanceCreation creation) {
    appendPrecedented(creation, () -> {
      appendString("new ");
      AbstractType<?, ?, ?> type = creation.getType();
      if (null == type) {
        type = ((UserField) creation.getParent()).valueType.getValue();
      }
      processTypeName(type);
      appendArguments(creation);
    });
  }

  @Override
  public void processArrayInstantiation(ArrayInstanceCreation creation) {
    appendPrecedented(creation, () -> {
      appendString("new ");
      processTypeName(creation.arrayType.getValue().getComponentType());

      //todo: lengths
      appendChar('[');
      appendChar(']');

      appendChar('{');
      String prefix = "";
      for (Expression expression : creation.expressions) {
        appendString(prefix);
        processExpression(expression);
        prefix = ", ";
      }
      appendChar('}');
    });
  }

  @Override
  public void processArrayAccess(ArrayAccess access) {
    // Array access has the highest level of precedence, 16, so it will never need parentheses
    pushPrecedented(access, () -> {
      processExpression(access.array.getValue());
      appendChar('[');
      processExpression(access.index.getValue());
      appendChar(']');
    });
  }

  @Override
  public void processArrayLength(ArrayLength arrayLength) {
    processExpression(arrayLength.array.getValue());
    appendString(".length");
  }

  @Override
  public void processFieldAccess(FieldAccess access) {
    // Field access has the highest level of precedence, 16, so it will never need parentheses
    pushPrecedented(access, () -> {
      appendTargetAndMember(access.expression.getValue(), identifierName(access.field.getValue()), null);
    });
  }

  private void appendPrecedented(PrecedentedOperation expr, Runnable appender) {
    if (areParenthesesNeeded(expr)) {
      parenthesize(() -> pushPrecedented(expr, appender));
    } else {
      pushPrecedented(expr, appender);
    }
  }

  private void pushPrecedented(PrecedentedOperation expr, Runnable appender) {
    operatorStack.push(expr);

    appender.run();

    PrecedentedOperation popped = operatorStack.pop();
    if (popped != expr) {
      Logger.errln("Unexpected expression on stack. These two should have been the same:", expr, popped);
    }
  }

  private boolean areParenthesesNeeded(PrecedentedOperation expr) {
    return !operatorStack.empty() && expr.getLevelOfPrecedence() <= operatorStack.peek().getLevelOfPrecedence();
  }

  // ** Comments **

  @Override
  public void processMultiLineComment(String comment) {
    for (String line : splitIntoLines(comment)) {
      appendSingleLineComment(line);
    }
  }

  protected void appendSingleLineComment(String line) {
    appendString("// ");
    appendString(line);
    appendNewLine();
  }

  static String[] splitIntoLines(String src) {
    return src.split("\n");
  }

  public abstract String getLocalizedComment(AbstractType<?, ?, ?> type, String itemName, Locale locale);

  // ** Primitives and syntax **

  @Override
  public void processNull() {
    appendString("null");
  }

  @Override
  public void processThisReference() {
    appendString("this");
  }

  @Override
  public void processSuperReference() {
    appendString("super");
  }

  @Override
  public void processBoolean(boolean b) {
    codeStringBuilder.append(b);
  }

  @Override
  public void processInt(int n) {
    if (n == Integer.MAX_VALUE) {
      appendString("Integer.MAX_VALUE");
    } else if (n == Integer.MIN_VALUE) {
      appendString("Integer.MIN_VALUE");
    } else {
      getCodeStringBuilder().append(n);
    }
  }

  @Override
  public void processFloat(float f) {
    if (Float.isNaN(f)) {
      appendString("Float.NaN");
    } else if (f == Float.POSITIVE_INFINITY) {
      appendString("Float.POSITIVE_INFINITY");
    } else if (f == Float.NEGATIVE_INFINITY) {
      appendString("Float.NEGATIVE_INFINITY");
    } else {
      getCodeStringBuilder().append(f);
      appendChar('f');
    }
  }

  @Override
  public void processDouble(double d) {
    if (Double.isNaN(d)) {
      appendString("Double.NaN");
    } else if (d == Double.POSITIVE_INFINITY) {
      appendString("Double.POSITIVE_INFINITY");
    } else if (d == Double.NEGATIVE_INFINITY) {
      appendString("Double.NEGATIVE_INFINITY");
    } else {
      getCodeStringBuilder().append(d);
    }
  }

  @Override
  public void processEscapedStringLiteral(StringLiteral literal) {
    appendEscapedString(literal.value.getValue());
  }

  protected void appendEscapedString(String value) {
    appendChar('"');
    String escaped = StringEscapeUtils.escapeJava(value);
    appendString(escaped);
    appendChar('"');
  }

  protected void appendChar(char c) {
    codeStringBuilder.append(c);
  }

  protected void appendSpace() {
    appendChar(' ');
  }

  protected void appendAccessSeparator() {
    appendChar('.');
  }

  protected void appendString(String s) {
    codeStringBuilder.append(s);
  }

  protected void appendNewLine() {
    appendChar('\n');
  }

  private void openParen() {
    appendChar('(');
  }

  private void closeParen() {
    appendChar(')');
  }

  protected void parenthesize(Runnable appender) {
    openParen();
    appender.run();
    closeParen();
  }

  protected void openBlock() {
    appendChar('{');
  }

  protected void closeBlock() {
    closeBlockInline();
  }

  protected void closeBlockInline() {
    appendChar('}');
  }

  protected void bracketize(Runnable appender) {
    openBlock();
    appender.run();
    closeBlock();
  }

  protected abstract void appendAssignmentOperator();

  @Override
  public void processVariableIdentifier(AbstractDeclaration variable) {
    appendString(identifierName(variable));
  }

  protected String identifierName(AbstractDeclaration variable) {
    return variable.getValidName();
  }

  private void appendParameterTypeName(AbstractType<?, ?, ?> type) {
    processTypeName(type);
  }

  @Override
  public void processTypeLiteral(TypeLiteral typeLiteral) {
    processTypeName(typeLiteral.value.getValue());
    appendString(".class");
  }

  private final Stack<PrecedentedOperation> operatorStack = new Stack<>();
  private final StringBuilder codeStringBuilder = new StringBuilder();
  private final Stack<AbstractType<?, ?, ?>> typeForLambdaStack = new Stack<>();
  private int statementDisabledCount = 0;
  private final Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitions;
  private final CodeOrganizer.CodeOrganizerDefinition defaultCodeOrganizerDefn;

}
