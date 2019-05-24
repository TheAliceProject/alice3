package org.alice.serialization.tweedle;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import org.apache.commons.lang.StringUtils;
import org.lgna.project.ast.*;
import org.lgna.project.code.CodeAppender;
import org.lgna.project.code.CodeOrganizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Encoder extends SourceCodeGenerator {
  private static final String INDENTION = "  ";
  private static final String NODE_DISABLE = "*<";
  private static final String NODE_ENABLE = ">*";
  private int indent = 0;
  private static final Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitionMap = new HashMap<>();
  private static final Map<String,String[]> methodsMissingParameterNames = new HashMap<>();

  static {
    codeOrganizerDefinitionMap.put("Scene", CodeOrganizer.sceneClassCodeOrganizer);
    codeOrganizerDefinitionMap.put("Program", CodeOrganizer.programClassCodeOrganizer);
    methodsMissingParameterNames.put("say", new String[] {"text"});
    methodsMissingParameterNames.put("think", new String[] {"text"});
    methodsMissingParameterNames.put("setJointedModelResource", new String[] {"resource"});
    methodsMissingParameterNames.put("setVehicle", new String[] {"vehicle"});
    methodsMissingParameterNames.put("setFloorPaint", new String[] {"paint"});
    methodsMissingParameterNames.put("setWallPaint", new String[] {"paint"});
    methodsMissingParameterNames.put("setCeilingPaint", new String[] {"paint"});
    methodsMissingParameterNames.put("setOpacity", new String[] {"portion"});
    methodsMissingParameterNames.put("pow", new String[] {"base", "exponent"});
    methodsMissingParameterNames.put("nextIntegerFromAToBExclusive", new String[] {"a", "b"});
    methodsMissingParameterNames.put("nextIntegerFromAToBInclusive", new String[] {"a", "b"});
    methodsMissingParameterNames.put("nextIntegerFrom0ToNExclusive", new String[] {"n"});
    methodsMissingParameterNames.put("toFlooredInteger", new String[] {"decimalNumber"});
    methodsMissingParameterNames.put("toRoundedInteger", new String[] {"decimalNumber"});
    methodsMissingParameterNames.put("toCeilingedInteger", new String[] {"decimalNumber"});
    methodsMissingParameterNames.put("abs", new String[] {"number"});
    // Min and max cover both decimal and whole
    methodsMissingParameterNames.put("min", new String[] {"a", "b"});
    methodsMissingParameterNames.put("max", new String[] {"a", "b"});

    methodsMissingParameterNames.put("floor", new String[] {"decimalNumber"});
    methodsMissingParameterNames.put("rint", new String[] {"decimalNumber"});
    methodsMissingParameterNames.put("ceil", new String[] {"decimalNumber"});
    methodsMissingParameterNames.put("nextDoubleInRange", new String[] {"a", "b"});
    methodsMissingParameterNames.put("sqrt", new String[] {"decimalNumber"});
    methodsMissingParameterNames.put("sin", new String[] {"radians"});
    methodsMissingParameterNames.put("cos", new String[] {"radians"});
    methodsMissingParameterNames.put("tan", new String[] {"radians"});
    methodsMissingParameterNames.put("asin", new String[] {"sin"});
    methodsMissingParameterNames.put("acos", new String[] {"cos"});
    methodsMissingParameterNames.put("atan", new String[] {"tan"});
    methodsMissingParameterNames.put("atan2", new String[] {"y", "x"});
    methodsMissingParameterNames.put("exp", new String[] {"power"});
    methodsMissingParameterNames.put("log", new String[] {"x"});
  }

  private final Set<AbstractDeclaration> terminalNodes;

  Encoder(Set<AbstractDeclaration> terminals) {
    super(codeOrganizerDefinitionMap, CodeOrganizer.defaultCodeOrganizer);
    terminalNodes = terminals;
  }

  Encoder() {
    super(codeOrganizerDefinitionMap, CodeOrganizer.defaultCodeOrganizer);
    terminalNodes = new HashSet<>();
  }

  public String encode(CodeAppender node) {
    node.appendCode(this);
    return getCodeStringBuilder().toString();
  }

  /** Class structure **/

  @Override
  protected void appendSection(CodeOrganizer codeOrganizer, NamedUserType userType, Map.Entry<String, List<CodeAppender>> entry) {
    super.appendSection(codeOrganizer, userType, entry);
    if ("ConstructorSection".equals(entry.getKey())) {
      appendResources(userType);
    }
  }

  private void appendResources(NamedUserType userType) {
    for(String resource: userType.getResourceNames()) {
      appendNewLine();
      appendIndent();
      appendString("static TextString ");
      appendString(resource);
      appendAssignmentOperator();
      appendEscapedString(userType.getName() + "/" + resource);
      appendStatementCompletion();
    }
  }

  @Override
  protected void appendClassHeader(NamedUserType userType) {
    getCodeStringBuilder().append("class ").append(userType.getName()).append(" extends ").append(userType.getSuperType().getName());
    // TODO Only show for models and replace with resource identifier
    //    if (userType.isModel())
    getCodeStringBuilder().append(" models ").append(userType.getName());
    openBlock();
  }

  @Override
  protected void appendClassFooter() {
    closeBlock();
  }

  /** Methods and Fields **/

  @Override
  public void appendConstructor(NamedUserConstructor constructor) {
    appendIndent();
    appendTypeName(constructor.getDeclaringType());
    appendParameters(constructor);
    appendStatement(constructor.body.getValue());
    appendNewLine();
  }

  @Override
  public void appendMethod(UserMethod method) {
    appendNewLine();
    appendIndent();
    super.appendMethod(method);
    appendNewLine();
  }

  @Override
  public void appendMethodHeader(AbstractMethod method) {
    if (method.isStatic()) {
      appendString("static ");
    }
    appendTypeName(method.getReturnType());
    appendSpace();
    appendString(method.getName());
    appendParameters(method);
  }

  @Override
  public void appendGetter(Getter getter) {
    appendIndent();
    super.appendGetter(getter);
    appendNewLine();
  }

  @Override
  public void appendSetter(Setter setter) {
    appendIndent();
    super.appendSetter(setter);
    appendNewLine();
  }

  /** Statements **/

  @Override
  public void appendLocalDeclaration(LocalDeclarationStatement stmt) {
    appendSingleStatement(stmt, () -> {
      UserLocal localVar = stmt.local.getValue();
      appendTypeName(localVar.getValueType());
      appendSpace();
      appendString(localVar.getValidName());
      appendAssignmentOperator();
      appendExpression(stmt.initializer.getValue());
    });
  }

  @Override
  protected void appendStatementCompletion(Statement stmt) {
    super.appendStatementCompletion(stmt);
    appendStatementEnd(stmt);
  }

  @Override
  protected void appendStatementCompletion() {
    super.appendStatementCompletion();
    appendNewLine();
  }

  private void appendStatementEnd(Statement stmt) {
    if (!stmt.isEnabled.getValue()) {
      appendSpace();
      appendString(NODE_ENABLE);
    }
    appendNewLine();
  }

  @Override
  protected void pushStatementDisabled() {
    appendString(NODE_DISABLE);
    super.pushStatementDisabled();
  }

  @Override
  protected void appendArgument(JavaKeyedArgument arg) {
    appendKeyedArgument(arg);
  }

  @Override
  protected void appendKeyedArgument(JavaKeyedArgument arg) {
    Expression expressionValue = arg.expression.getValue();
    if (expressionValue instanceof MethodInvocation) {
      MethodInvocation methodInvocation = (MethodInvocation) expressionValue;
      AbstractMethod method = methodInvocation.method.getValue();
      AbstractType<?, ?, ?> factoryType = AstUtilities.getKeywordFactoryType(arg);
      if (factoryType != null) {
        appendString(method.getName());
        appendString(": ");
        appendOneRequiredArgument(methodInvocation);
        return;
      }
    }
    appendExpression(expressionValue);
  }

  private void appendOneRequiredArgument(ArgumentOwner argumentOwner) {
    if (!argumentOwner.getVariableArgumentsProperty().isEmpty() || !argumentOwner.getKeyedArgumentsProperty().isEmpty() || argumentOwner.getRequiredArgumentsProperty().size() != 1) {
      Logger.errln("Expected a single required argument.", argumentOwner);
    }
    if (argumentOwner.getRequiredArgumentsProperty().size() > 0) {
      argumentOwner.getRequiredArgumentsProperty().get(0).appendCode(this);
    }
  }

  @Override
  protected void appendArgument(AbstractParameter parameter, AbstractArgument argument) {
    appendString(getParameterLabel(parameter));
    appendString(": ");
    argument.appendCode(this);
  }

  private String getParameterLabel(AbstractParameter parameter) {
    String label = parameter.getName();
    if (null != label) {
      return label;
    }
    if (parameter instanceof JavaMethodParameter) {
      final String methodName = parameter.getCode().getName();
      if (methodsMissingParameterNames.containsKey(methodName)) {
        String[] paramNames = methodsMissingParameterNames.get(methodName);
        int i = parameterIndex((JavaMethodParameter) parameter);
        return paramNames[i];
      }
    }
    if (parameter instanceof JavaConstructorParameter) {
      String javaType = parameter.getCode().getDeclaringType().getName();
      if ("Double".equals(javaType)) {
        return "wholeNumber";
      }
    }
    final String paramType = parameter.getValueType().getName().toLowerCase();
    final String message = String.format("Unable to read label from parameter on method: %s\nUsing the type as label: %s\nGenerated code may contain errors.", parameter.getCode().toString(), paramType);
    Dialogs.showError("Unlabeled parameter", message);
    Logger.errln(message);
    return paramType;
  }

  private int parameterIndex(JavaMethodParameter parameter) {
      AbstractParameter[] parameters = parameter.getCode().getAllParameters();
      for (int i = 0; i < parameters.length; i++) {
        if (parameter == parameters[i]) {
          return i;
        }
      }
      return -1;
  }

  @Override
  protected void appendSingleStatement(Statement stmt, Runnable appender) {
    appendIndent(stmt);
    super.appendSingleStatement(stmt, appender);
  }

  @Override
  protected void appendSingleCodeLine(Runnable appender) {
    appendIndent();
    super.appendSingleCodeLine(appender);
  }

  /** Code Flow **/

  @Override
  protected void appendCodeFlowStatement(Statement stmt, Runnable appender) {
    appendIndent(stmt);
    appender.run();
    appendStatementEnd(stmt);
  }

  @Override
  public void appendCountLoop(CountLoop loop) {
    appendCodeFlowStatement(loop, () -> {
      appendString("countUpTo( ");
      appendString(loop.getVariableName());
      appendString(" < ");
      appendExpression(loop.count.getValue());
      appendString(" )");
      appendStatement(loop.body.getValue());
    });
  }

  @Override
  protected void appendForEachToken() {
    appendString("forEach");
  }

  @Override
  protected void appendInEachToken() {
    appendString(" in ");
  }

  @Override
  public void appendDoInOrder(DoInOrder doInOrder) {
    appendLabeledCodeFlow(doInOrder, "doInOrder");
  }

  @Override
  public void appendDoTogether(DoTogether doTogether) {
    appendLabeledCodeFlow(doTogether, "doTogether");
  }

  private void appendLabeledCodeFlow(AbstractStatementWithBody stmt, String label) {
    appendCodeFlowStatement(stmt, () -> {
      appendString(label);
      appendStatement(stmt.body.getValue());
    });
  }

  @Override
  public void appendEachInTogether(AbstractEachInTogether eachInTogether) {
    appendCodeFlowStatement(eachInTogether, () -> {
      appendString("eachTogether");
      UserLocal itemValue = eachInTogether.item.getValue();
      Expression items = eachInTogether.getArrayOrIterableProperty().getValue();
      appendEachItemsClause(itemValue, items);
      appendStatement(eachInTogether.body.getValue());
    });
  }

  /** Expressions **/

  @Override
  protected void appendResourceExpression(ResourceExpression resourceExpression) {
    appendEscapedString(resourceExpression.resource.getValue().getName());
  }

  /** Comments **/

  @Override
  protected void appendSingleLineComment(String line) {
    appendIndent();
    super.appendSingleLineComment(line);
  }

  @Override
  public String getLocalizedComment(AbstractType<?, ?, ?> type, String itemName, Locale locale) {
    return "//";
  }

  /** Primitives and syntax **/

  @Override
  protected void openBlock() {
    appendString(" {\n");
    pushIndent();
  }

  @Override
  protected void closeBlock() {
    popIndent();
    appendIndent();
    super.closeBlock();
  }

  @Override
  protected void appendAssignmentOperator() {
    appendString(" <- ");
  }

  @Override
  protected void appendParameterTypeName(AbstractType<?, ?, ?> type) {
    final String typeName = type.getName();
    if (typeName.endsWith("Resource")) {
      appendString("TextString");
    } else {
      super.appendParameterTypeName(type);
    }
  }

  @Override
  protected void appendTypeName(AbstractType<?, ?, ?> type) {
    final String typeName = type.getName();
    switch (typeName) {
    case "Double":
      appendString("DecimalNumber");
      break;
    case "Double[]":
      appendString("DecimalNumber[]");
      break;
    case "Integer":
      appendString("WholeNumber");
      break;
    case "Integer[]":
      appendString("WholeNumber[]");
      break;
    case "String":
      appendString("TextString");
      break;
    case "String[]":
      appendString("TextString[]");
      break;
    default:
      if (typeName.endsWith("Resource")) {
        appendString(typeName.substring(0, typeName.length() - 8));
      } else {
        appendString(typeName);
      }
    }
  }

  @Override
  protected String getListSeparator() {
    return ", ";
  }

  /** Formatting **/

  private void pushIndent() {
    indent++;
  }

  private void popIndent() {
    indent--;
  }

  private void appendIndent() {
    appendString(StringUtils.repeat(INDENTION, indent));
  }

  private void appendIndent(Statement stmt) {
    final int indent = stmt.isEnabled.getValue() ? this.indent : this.indent - 1;
    appendString(StringUtils.repeat(INDENTION, indent));
  }
}
