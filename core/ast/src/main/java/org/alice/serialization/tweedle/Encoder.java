package org.alice.serialization.tweedle;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import org.apache.commons.lang.StringUtils;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.ast.*;
import org.lgna.project.code.CodeAppender;
import org.lgna.project.code.CodeGenerator;
import org.lgna.project.code.CodeOrganizer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Encoder extends SourceCodeGenerator {
  private static final String INDENTION = "  ";
  private static final String NODE_DISABLE = "*<";
  private static final String NODE_ENABLE = ">*";
  private int indent = 0;
  private static final Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitionMap = new HashMap<>();
  private static final Map<String, String[]> methodsMissingParameterNames = new HashMap<>();
  private static final Map<String, Map<String, String>> methodsWithWrappedArgs = new HashMap<>();
  private static final Map<String, String> optionalParamsToWrap = new HashMap<>();
  private static final Map<String, Map<String, String>> constructorsWithRelabeledParams = new HashMap<>();
  private static final List<String> classesToAddConstructorsTo = new ArrayList<>();

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
    methodsMissingParameterNames.put("setOpacity", new String[] {"opacity"});
    methodsMissingParameterNames.put("strikePose", new String[] {"pose"});
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

    Map<String, String> amount = new HashMap<>();
    amount.put("amount", "new Angle(revolutions: ");
    methodsWithWrappedArgs.put("roll", amount);
    methodsWithWrappedArgs.put("turn", amount);
    Map<String, String> density = new HashMap<>();
    density.put("density", "new Portion(portion: ");
    methodsWithWrappedArgs.put("setFogDensity", density);
    Map<String, String> opacity = new HashMap<>();
    opacity.put("opacity", "new Portion(portion: ");
    methodsWithWrappedArgs.put("setOpacity", opacity);

    optionalParamsToWrap.put("duration", "new Duration(seconds: ");

    Map<String, String> sizeParams = new HashMap<>();
    sizeParams.put("leftToRight", "width");
    sizeParams.put("bottomToTop", "height");
    sizeParams.put("frontToBack", "depth");
    constructorsWithRelabeledParams.put("Size", sizeParams);
    Map<String, String> positionParams = new HashMap<>();
    positionParams.put("right", "x");
    positionParams.put("up", "y");
    positionParams.put("backward", "z");
    constructorsWithRelabeledParams.put("Position", positionParams);
    Map<String, String> imageParams = new HashMap<>();
    imageParams.put("imageResource", "resource");
    constructorsWithRelabeledParams.put("ImageSource", imageParams);
    classesToAddConstructorsTo.add("Prop");
    classesToAddConstructorsTo.add("Aircraft");
    classesToAddConstructorsTo.add("Automobile");
    classesToAddConstructorsTo.add("Train");
    classesToAddConstructorsTo.add("Transport");
    classesToAddConstructorsTo.add("Watercraft");
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
    final String typeName = userType.getName();
    if (classesToAddConstructorsTo.contains(typeName)) {
      appendNewLine();
      appendString(typeName);
      appendString("(TextString resource, JointId root) {\n" + "  super(resource: resource, root: root);\n" + "}\n");
      return;
    }
    Class resourceClass = userType.getResourceClass();
    if (resourceClass == null) {
      return;
    }
    appendResourceNames(userType, resourceClass);
    appendResourceFields(resourceClass);
  }

  private void appendResourceNames(NamedUserType userType, Class resourceClass) {
    final List<String> resourceNames = Arrays.stream(resourceClass.getEnumConstants()).map(Object::toString).collect(Collectors.toList());
    for (String resource: resourceNames) {
      appendNewLine();
      appendIndent();
      appendString("static TextString ");
      appendString(resource);
      appendAssignmentOperator();
      appendEscapedString(tweedleTypeName(userType.getName()) + "/" + resource);
      appendStatementCompletion();
    }
  }

  private void appendResourceFields(Class resourceClass) {
    Field[] fields = resourceClass.getDeclaredFields();
    for (Field field : fields) {
      try {
        Object value = field.get(resourceClass);
        if (value instanceof CodeGenerator) {
          appendStaticField(field, (CodeGenerator) value);
        } else {
          Logger.info("Export will skip non-generator field " + resourceClass.getSimpleName() + "." + field.getName());
        }
      } catch (IllegalAccessException e) {
        Logger.info("Export will skip inaccessible field " + resourceClass.getSimpleName() + "." + field.getName());
      }
    }
  }

  private void appendStaticField(Field field, CodeGenerator value) {
    appendSingleCodeLine(() -> {
      FieldTemplate fieldAnnotation = field.getAnnotation(FieldTemplate.class);
      appendString(visibilityTag(fieldAnnotation));
      appendString(" static ");
      appendString(field.getType().getSimpleName());
      appendSpace();
      appendString(field.getName());
      appendAssignmentOperator();
      value.appendCode(this);
    });
  }

  private String visibilityTag(FieldTemplate fieldAnnotation) {
    switch (fieldAnnotation.visibility()) {
    case COMPLETELY_HIDDEN:
      return "@CompletelyHidden";
    case PRIME_TIME:
      return "@PrimeTime";
    case TUCKED_AWAY:
      return "@TuckedAway";
    default:
      return "";
    }
  }

  @Override
  public void appendNewJointId(String joint, String parent, String model) {
    appendString("new JointId");
    parenthesize(() -> {
      appendString("name: \"");
      appendString(joint);
      appendString("\", parent: ");
      if (parent == null) {
        appendNull();
      } else {
        appendString(tweedleTypeName(model));
        appendAccessSeparator();
        appendString(parent);
      }
    });
  }

  @Override
  protected void appendClassHeader(NamedUserType userType) {
    getCodeStringBuilder().append("class ").append(tweedleTypeName(userType.getName())).append(" extends ").append(userType.getSuperType().getName());
    // TODO Only show for models and replace with resource identifier
    //    if (userType.isModel())
    getCodeStringBuilder().append(" models ").append(tweedleTypeName(userType.getName()));
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
  protected void appendSuperConstructor(SuperConstructorInvocationStatement supCon) {
    appendSingleStatement(supCon, () -> {
      appendSuperReference();
      parenthesize(() -> {
        appendEachArgument(supCon);
        appendResourceArgs(supCon);
      });
    });
  }

  private void appendResourceArgs(SuperConstructorInvocationStatement supCon) {
    final Node parent = supCon.getParent().getParent();
    if (parent instanceof NamedUserConstructor) {
      final AbstractType<?, ?, ?> declaringType = ((NamedUserConstructor) parent).getDeclaringType();
      if (declaringType instanceof NamedUserType) {
        final NamedUserType nut = (NamedUserType) declaringType;
        Class resourceClass = nut.getResourceClass();
        if (resourceClass != null) {
          if (Arrays.stream(resourceClass.getDeclaredFields()).anyMatch(field -> "ROOT".equals(field.getName()))) {
            appendString(", root:");
            appendString(nut.getName());
            appendString(".ROOT");
          } else {
            if ("Biplane".equals(nut.getName())) {
              appendString(", root: Biplane.BIPLANE_ROOT");
            }
          }
        }
      }
    }
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
        appendOneArgument(methodInvocation);
        return;
      }
    }
    appendExpression(expressionValue);
  }

  private void appendOneArgument(MethodInvocation argumentOwner) {
    if (!argumentOwner.getVariableArgumentsProperty().isEmpty() || !argumentOwner.getKeyedArgumentsProperty().isEmpty() || argumentOwner.getRequiredArgumentsProperty().size() != 1) {
      Logger.errln("Expected a single argument.", argumentOwner);
    }
    if (argumentOwner.getRequiredArgumentsProperty().size() > 0) {
      final String methodName = argumentOwner.method.getValue().getName();
      Map<String, String> wrappedParams = optionalParamsToWrap.containsKey(methodName) ? optionalParamsToWrap : null;
      appendWrappedArg(argumentOwner.getRequiredArgumentsProperty().get(0), methodName, wrappedParams);
    }
  }

  @Override
  protected void appendArgument(AbstractParameter parameter, AbstractArgument argument) {
    final String parameterLabel = getParameterLabel(parameter);
    appendString(parameterLabel);
    appendString(": ");

    Map<String, String> wrappedParams = methodsWithWrappedArgs.get(parameter.getCode().getName());
    appendWrappedArg(argument, parameterLabel, wrappedParams);
  }

  private void appendWrappedArg(CodeAppender argument, String parameterLabel, Map<String, String> wrappedParams) {
    String argStart = wrappedParams == null ? null : wrappedParams.get(parameterLabel);
    if (argStart != null) {
      appendString(argStart);
    }
    argument.appendCode(this);
    if (argStart != null) {
      appendString(")");
    }
  }

  private String getParameterLabel(AbstractParameter parameter) {
    if (parameter instanceof JavaConstructorParameter) {
      String className = ((JavaConstructorParameter) parameter).getCode().getDeclaringType().getName();
      Map<String, String> paramLabelMap = constructorsWithRelabeledParams.get(className);
      if (paramLabelMap != null) {
        final String newLabel = paramLabelMap.get(parameter.getName());
        if (newLabel != null) {
          return newLabel;
        }
      }
    }
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
    appendString(tweedleTypeName(type.getName()));
  }

  private String tweedleTypeName(String typeName) {
    switch (typeName) {
    case "Double":
      return "DecimalNumber";
    case "Double[]":
      return "DecimalNumber[]";
    case "Integer":
      return "WholeNumber";
    case "Integer[]":
      return "WholeNumber[]";
    case "String":
      return "TextString";
    case "String[]":
      return "TextString[]";
    case "SandDunes":
      return "Terrain";
    default:
      if (typeName.endsWith("Resource")) {
        return typeName.substring(0, typeName.length() - 8);
      } else {
        return typeName;
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
