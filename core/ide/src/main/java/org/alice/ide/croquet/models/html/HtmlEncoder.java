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
package org.alice.ide.croquet.models.html;

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import org.alice.ide.common.BodyPane;
import org.alice.ide.common.TypeComponent;
import org.alice.ide.x.ProjectEditorAstI18nFactory;
import org.alice.ide.x.components.StatementListPropertyView;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGIDGenerator;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.*;
import org.lgna.project.code.CodeOrganizer;
import org.lgna.project.code.ProcessableNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class HtmlEncoder implements AstProcessor {

  private final Document document;
  private SVGGraphics2D activeSVG = null;
  private SVGIDGenerator firstIDGenerator = null;
  private final Stack<org.w3c.dom.Element> activeElements = new Stack<>();
  private static final Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitionMap = new HashMap<>();
  private final Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitions = codeOrganizerDefinitionMap;
  private static final Collection<String> sectionsToSkip = Collections.unmodifiableList(Arrays.asList("ConstructorSection", "GettersAndSettersSection", "StaticMethodsSection"));

  static {
    codeOrganizerDefinitionMap.put("Scene", CodeOrganizer.sceneClassCodeOrganizer);
    codeOrganizerDefinitionMap.put("Program", CodeOrganizer.programClassCodeOrganizer);
  }

  HtmlEncoder(Document doc) {
    document = doc;
  }

  public void encode(ProcessableNode node, org.w3c.dom.Element root) {
    activeElements.push(root);
    node.process(this);
  }

  /** Document creation **/

  private void pushDiv(String classes, Runnable appender) {
    final org.w3c.dom.Element element = addDiv(classes);
    activeElements.push(element);
    appender.run();
    assert element == activeElements.pop();
  }

  private void pushSpan(String classes, Runnable appender) {
    final org.w3c.dom.Element element = addSpan(classes, " ");
    activeElements.push(element);
    appender.run();
    assert element == activeElements.pop();
  }

  private org.w3c.dom.Node parentNode() {
    org.w3c.dom.Node parent = parentElement();
    return parent == null ? document : parent;
  }

  private org.w3c.dom.Element parentElement() {
    return activeElements.peek();
  }

  private Element addDiv(String classes) {
    return addElement("div", classes, " ");
  }

  private Element addSpan(String classes, String value) {
    return addElement("span", classes, value);
  }

  private Element addElement(String elementType, String classes, String content) {
    final org.w3c.dom.Element element = document.createElement(elementType);
    element.setAttribute("class", classes);
    element.setTextContent(content);
    parentNode().appendChild(element);
    return element;
  }

  private void pushSvg(Runnable content) {
    if (activeSVG != null) {
      throw new RuntimeException("Attempted to create an SVG inside another SVG.");
    }
    activeSVG = new SVGGraphics2D(document);
    activeSVG.setSVGCanvasSize(new Dimension(0, 0));
    useCommonIdGenerator();

    content.run();

    final Element svgRoot = activeSVG.getRoot();
    svgRoot.setAttribute("class", "alice-generated-svg");
    parentNode().appendChild(svgRoot);
    activeSVG = null;
  }

  private void useCommonIdGenerator() {
    if (firstIDGenerator == null) {
      // Use the one from the first SVG generated
      firstIDGenerator = activeSVG.getGeneratorContext().getIDGenerator();
    } else {
      // Replace the generator on other SVGs to continue number and prevent conflicts
      activeSVG.getGeneratorContext().setIDGenerator(firstIDGenerator);
    }
  }

  private void addToSvg(SwingComponentView<?> view) {
    if (activeSVG == null) {
      throw new RuntimeException("Attempted to add to SVG before creating it.");
    }
    JComponent awt = view.getAwtComponent();
    ComponentUtilities.doLayoutTree(awt);
    ComponentUtilities.setSizeToPreferredSizeTree(awt);
    Dimension addedSize = awt.getPreferredSize();
    Dimension svgSize = activeSVG.getSVGCanvasSize();
    activeSVG.setSVGCanvasSize(new Dimension(Math.max(addedSize.width, svgSize.width), Math.max(addedSize.height, svgSize.height)));
    awt.paint(activeSVG);
  }

  private void addTypeSvgInline(AbstractType<?, ?, ?> type) {
    pushSvg(() -> addToSvg(TypeComponent.createInstance(type)));
  }

  private void addExpressionToSvg(Expression expression) {
    addToSvg(ProjectEditorAstI18nFactory.getInstance().createExpressionPane(expression));
  }

  /** Localization **/

  private String localizedCodeEditorTerm(String key) {
    return ResourceBundleUtilities.getStringForKey(key, "org.alice.ide.codeeditor.CodeEditor");
  }

  /** Class structure **/

  @Override
  public CodeOrganizer getNewCodeOrganizerForTypeName(String typeName) {
    return new CodeOrganizer(codeOrganizerDefinitions.getOrDefault(typeName, CodeOrganizer.defaultCodeOrganizer));
  }

  @Override
  public void processClass(CodeOrganizer codeOrganizer, NamedUserType userType) {
    if (isClassEmpty(codeOrganizer)) {
      return;
    }
    pushDiv("alice-class", () -> {
      pushDiv("alice-class-header alice-code-header", () -> {
        addSpan("alice-class-name", userType.getName());
        addSpan("alice-code-header-detail", "extends");
        addSpan("alice-class-superType", userType.getSuperType().getName());
      });
      for (Map.Entry<String, List<ProcessableNode>> entry : codeOrganizer.getOrderedSections().entrySet()) {
          if (isSectionToInclude(entry.getKey()) && !entry.getValue().isEmpty()) {
            appendSection(entry.getValue());
        }
      }
    });
  }

  private boolean isClassEmpty(CodeOrganizer codeOrganizer) {
    for (Map.Entry<String, List<ProcessableNode>> entry : codeOrganizer.getOrderedSections().entrySet()) {
      if (isSectionToInclude(entry.getKey()) && !entry.getValue().isEmpty()) {
        for (ProcessableNode item : entry.getValue()) {
          if (!(item instanceof UserMethod) || !((UserMethod) item).getManagementLevel().isGenerated()) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private boolean isSectionToInclude(String key) {
    return !sectionsToSkip.contains(key);
  }

  private void appendSection(List<ProcessableNode> items) {
    pushDiv("alice-class-section", () -> {
      for (ProcessableNode item : items) {
        item.process(this);
      }
    });
  }

  /** Methods and Fields **/

  @Override
  public void processField(UserField field) {
    pushDiv("alice-field-definition alice-code-header", () -> {
      addTypeSvgInline(field.valueType.getValue());
      addSpan("alice-field-name", field.name.getValue());
      addSpan("alice-assignment", "<-");
      pushSvg(() -> addExpressionToSvg(field.initializer.getValue()));
    });
  }

  @Override
  public void processMethod(UserMethod method) {
    // Skip code that is generated or managed, but this does not include Program.main, so there is an extra check to skip it
    if (method.getManagementLevel().isGenerated() || (method.isStatic() && "main".equals(method.getName()))) {
      return;
    }
    pushDiv("alice-method", () -> {
      if ("initializeEventListeners".equals(method.getName())) {
        pushDiv("alice-method-header alice-code-header", () -> addSpan("alice-method-name alice-code-header", method.getName()));
        splitUpListeners(method);
      } else {
        appendMethodHeader(method);
        pushSvg(() -> statementsAsSvg(method.body.getValue().statements));
      }
    });
  }

  private void splitUpListeners(UserMethod initializeEventListeners) {
    ArrayList<Statement> listeners = initializeEventListeners.body.getValue().statements.getValue();
    for (Statement listener : listeners) {
      if (listener instanceof ExpressionStatement) {
        Expression exp = ((ExpressionStatement) listener).expression.getValue();
        if (exp instanceof MethodInvocation) {
          addListener((MethodInvocation) exp, !listener.isEnabled.getValue());
        }
      }
    }
  }

  private void addListener(MethodInvocation addListenerCall, boolean isDisabled) {
    pushDiv("alice-listener-addition", () -> {
      if (isDisabled) {
        addDiv("alice-disabled");
      }
      pushDiv("alice-method-header alice-code-header", () -> {
        pushSvg(() -> addExpressionToSvg(addListenerCall.expression.getValue()));  // generally a reference to "this"
        final AbstractMethod listenerMethod = addListenerCall.method.getValue();
        addSpan("alice-method-name alice-code-header", listenerMethod.getName());
        appendLambdaArguments(addListenerCall);
      });
      ArrayList<SimpleArgument> args = addListenerCall.requiredArguments.getValue();
      if (!args.isEmpty() && "listener".equals(args.get(0).parameter.getValue().getName()) && args.get(0).expression.getValue() instanceof LambdaExpression) {
        Lambda lambda = ((LambdaExpression) args.get(0).expression.getValue()).value.getValue();
        if (lambda instanceof UserLambda) {
          UserLambda userLambda = (UserLambda) lambda;
          List<? extends AbstractMethod> listenerTypeMethods = args.get(0).parameter.getValue().getValueType().getDeclaredMethods();
          AbstractMethod first = listenerTypeMethods.get(0);

          pushDiv("alice-listener-declaration", () -> {
            appendLambdaMethodHeader(first.getName());
            pushSvg(() -> statementsAsSvg(userLambda.body.getValue().statements));
          });
        }
      }
    });
  }

  private void statementsAsSvg(StatementListProperty statements) {
    ProjectEditorAstI18nFactory factory = ProjectEditorAstI18nFactory.getInstance();
    StatementListPropertyView statementListComponent = new StatementListPropertyView(factory, statements, 0);
    BodyPane bodyPane = new BodyPane(statementListComponent);
    addToSvg(bodyPane);
  }

  private void appendMethodHeader(AbstractUserMethod method) {
    pushDiv("alice-method-header alice-code-header", () -> {
      addSpan("alice-code-header-detail", localizedCodeEditorTerm("declare"));
      if (method.isStatic()) {
        addSpan("alice-code-header-detail", localizedCodeEditorTerm("static"));
      }
      if (method.isFunction()) {
        addTypeSvgInline(method.getReturnType());
        addSpan("alice-code-header-detail", localizedCodeEditorTerm("function"));
      } else {
        addSpan("alice-code-header-detail", localizedCodeEditorTerm("procedure"));
      }
      addSpan("alice-method-name alice-code-header", method.getName());
      appendParameters(method);
    });
  }

  private void appendLambdaMethodHeader(String name) {
    pushDiv("alice-method-header alice-code-header", () -> {
      addSpan("alice-code-header-detail", localizedCodeEditorTerm("declare"));
      addSpan("alice-code-header-detail", localizedCodeEditorTerm("procedure"));
      addSpan("alice-method-name alice-code-header", name);
    });
  }

  private void appendLambdaArguments(MethodInvocation code) {
    pushSpan("alice-parameters alice-code-header", () -> {
      for (SimpleArgument arg : code.requiredArguments.getValue()) {
        if (!"listener".equals(arg.parameter.getValue().getName())) {
          appendLambdaArgument(arg);
        }
      }
      for (SimpleArgument arg : code.variableArguments.getValue()) {
        appendLambdaArgument(arg);
      }
      for (JavaKeyedArgument arg : code.keyedArguments.getValue()) {
        appendLambdaArgument(arg);
      }
    });
  }

  private void appendLambdaArgument(AbstractArgument arg) {
    pushSvg(() -> addExpressionToSvg(arg.expression.getValue()));
  }

  private void appendParameters(Code code) {
    pushSpan("alice-parameters alice-code-header", () -> {
      final List<? extends AbstractParameter> requiredParameters = code.getRequiredParameters();
      if (requiredParameters.size() == 1) {
        addSpan("alice-code-header-detail", localizedCodeEditorTerm("withParameter"));
      }
      if (requiredParameters.size() > 1) {
        addSpan("alice-code-header-detail", localizedCodeEditorTerm("withParameters"));
      }
      for (AbstractParameter parameter : requiredParameters) {
        appendParameter(parameter);
      }
    });
  }

  private void appendParameter(AbstractParameter parameter) {
    pushSpan("alice-parameter alice-code-header", () -> {
      addTypeSvgInline(parameter.getValueType());
      String parameterName = parameter.getValidName();
      if (parameterName != null) {
        addSpan("alice-parameter-label", parameterName);
      }
    });
  }

  @Override
  public void processGetter(Getter getter) {
    // Does not include getters
  }

  @Override
  public void processIndexedGetter(ArrayItemGetter getter) {
    // Does not include getters
  }

  @Override
  public void processSetter(Setter setter) {
    // Does not include setters
  }

  @Override
  public void processIndexedSetter(ArrayItemSetter setter) {
    // Does not include setters
  }

  @Override
  public void processConstructor(NamedUserConstructor constructor) {
    // Does not include constructors
  }

  @Override
  public void processSuperConstructor(SuperConstructorInvocationStatement supCon) {
    // Does not include constructors
  }

  /** Statements will not be processed, as they are all inside generated SVGs **/

  @Override
  public void processExpressionStatement(ExpressionStatement stmt) {
  }

  @Override
  public void processReturnStatement(ReturnStatement stmt) {
  }

  @Override
  public void processBlock(BlockStatement blockStatement) {
  }

  @Override
  public void processConstructorBlock(ConstructorBlockStatement constructor) {
  }

  @Override
  public void processThisConstructor(ThisConstructorInvocationStatement thisCon) {
  }

  @Override
  public void processLocalDeclaration(LocalDeclarationStatement stmt) {
  }

  @Override
  public void processKeyedArgument(JavaKeyedArgument arg) {
  }

  @Override
  public void processArgument(AbstractParameter parameter, AbstractArgument argument) {
  }

  /** Code Flow **/

  @Override
  public void processConditional(ConditionalStatement stmt) {
  }

  @Override
  public void processForEach(AbstractForEachLoop loop) {
  }

  @Override
  public void processEachInTogether(AbstractEachInTogether eachInTogether) {
  }

  @Override
  public void processWhileLoop(WhileLoop loop) {
  }

  @Override
  public void processCountLoop(CountLoop loop) {
  }

  @Override
  public void processDoInOrder(DoInOrder doInOrder) {
  }

  @Override
  public void processDoTogether(DoTogether doTogether) {
  }

  @Override
  public void processLambda(UserLambda lambda) {
  }

  /** Expressions **/

  @Override
  public void processExpression(Expression expression) {
  }

  @Override
  public void processMethodCall(MethodInvocation invocation) {
  }

  @Override
  public void processFieldAccess(FieldAccess access) {
  }

  @Override
  public void processAssignmentExpression(AssignmentExpression assignment) {
  }

  @Override
  public void processConcatenation(StringConcatenation concat) {
  }

  @Override
  public void processLogicalComplement(LogicalComplement complement) {
  }

  @Override
  public void processInfixExpression(InfixExpression infixExpression) {
  }

  @Override
  public void processInstantiation(InstanceCreation creation) {
  }

  @Override
  public void processArrayInstantiation(ArrayInstanceCreation creation) {
  }

  @Override
  public void processArrayAccess(ArrayAccess access) {
  }

  @Override
  public void processArrayLength(ArrayLength arrayLength) {
  }

  @Override
  public void processResourceExpression(ResourceExpression resourceExpression) {
  }

  @Override
  public void processNewJointId(String joint, String parent, String model) {
    // Come from models. Not part of code print out.
  }

  /** Comments **/

  @Override
  public void processMultiLineComment(String comment) {
  }

  /** Primitives and syntax **/

  @Override
  public void processNull() {
  }

  @Override
  public void processThisReference() {
  }

  @Override
  public void processSuperReference() {
  }

  @Override
  public void processBoolean(boolean b) {
  }

  @Override
  public void processInt(int n) {
  }

  @Override
  public void processFloat(float f) {
  }

  @Override
  public void processDouble(double d) {
  }

  @Override
  public void processEscapedStringLiteral(StringLiteral literal) {
  }

  @Override
  public void processVariableAccess(String name) {
  }

  @Override
  public void processTypeLiteral(TypeLiteral typeLiteral) {
  }

  @Override
  public void processTypeName(AbstractType<?, ?, ?> type) {
  }
}
