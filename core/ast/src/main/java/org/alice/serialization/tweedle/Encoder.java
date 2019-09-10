package org.alice.serialization.tweedle;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import org.apache.commons.lang.StringUtils;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.ast.*;
import org.lgna.project.code.IdentifiableTweedleNode;
import org.lgna.project.code.ProcessableNode;
import org.lgna.project.code.CodeOrganizer;
import org.lgna.project.code.InstantiableTweedleNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Encoder extends SourceCodeGenerator {
  private static final String INDENTION = "  ";
  private static final String NODE_DISABLE = "*<";
  private static final String NODE_ENABLE = ">*";
  private int indent = 0;
  private static final Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitionMap = new HashMap<>();
  private static final List<String> angleMembers = new ArrayList<>();
  private static final Map<String, String> typesToRename = new HashMap<>();
  private static final Map<String, String> membersToRename = new HashMap<>();
  private static final Map<String, String[]> methodsMissingParameterNames = new HashMap<>();
  private static final Map<String, Map<String, String>> methodsWithWrappedArgs = new HashMap<>();
  private static final Map<String, String> optionalParamsToWrap = new HashMap<>();
  private static final Map<String, Map<String, String>> constructorsWithRelabeledParams = new HashMap<>();
  private static final Map<String, String> classesToAddPassThroughConstructorsTo = new HashMap<>();

  static {
    codeOrganizerDefinitionMap.put("Scene", CodeOrganizer.sceneClassCodeOrganizer);
    codeOrganizerDefinitionMap.put("Program", CodeOrganizer.programClassCodeOrganizer);

    membersToRename.put("rint", "round");
    membersToRename.put("ceil", "ceiling");
    typesToRename.put("IntegerUtilities", "$WholeNumber");
    membersToRename.put("toFlooredInteger", "floor");
    membersToRename.put("toRoundedInteger", "round");
    membersToRename.put("toCeilingedInteger", "ceiling");
    typesToRename.put("RandomUtilities", "$Random");
    membersToRename.put("nextIntegerFrom0ToNExclusive", "wholeNumberFrom0ToNExclusive");
    membersToRename.put("nextIntegerFromAToBExclusive", "wholeNumberFromAToBExclusive");
    membersToRename.put("nextIntegerFromAToBInclusive", "wholeNumberFromAToBInclusive");
    membersToRename.put("nextDoubleInRange", "decimalNumberInRange");
    membersToRename.put("nextBoolean", "boolean");

    typesToRename.put("Double", "DecimalNumber");
    typesToRename.put("Double[]", "DecimalNumber[]");
    typesToRename.put("Integer", "WholeNumber");
    typesToRename.put("Integer[]", "WholeNumber[]");
    typesToRename.put("String", "TextString");
    typesToRename.put("String[]", "TextString[]");
    typesToRename.put("SandDunes", "Terrain");
    typesToRename.put("Visual", "SThing");
    typesToRename.put("Visual[]", "SThing[]");
    typesToRename.put("StartOcclusionEvent", "ModelInteractionEvent");
    typesToRename.put("EndOcclusionEvent", "ModelInteractionEvent");
    typesToRename.put("StartCollisionEvent", "ThingInteractionEvent");
    typesToRename.put("EndCollisionEvent", "ThingInteractionEvent");
    typesToRename.put("EnterProximityEvent", "ThingInteractionEvent");
    typesToRename.put("ExitProximityEvent", "ThingInteractionEvent");
    typesToRename.put("EnterViewEvent", "ViewEvent");
    typesToRename.put("ExitViewEvent", "ViewEvent");

    methodsMissingParameterNames.put("say", new String[] {"text"});
    methodsMissingParameterNames.put("think", new String[] {"text"});
    methodsMissingParameterNames.put("setJointedModelResource", new String[] {"resource"});
    methodsMissingParameterNames.put("setVehicle", new String[] {"vehicle"});
    methodsMissingParameterNames.put("setFloorPaint", new String[] {"paint"});
    methodsMissingParameterNames.put("setWallPaint", new String[] {"paint"});
    methodsMissingParameterNames.put("setCeilingPaint", new String[] {"paint"});
    methodsMissingParameterNames.put("setOpacity", new String[] {"opacity"});
    methodsMissingParameterNames.put("strikePose", new String[] {"pose"});
    methodsMissingParameterNames.put("pow", new String[] {"b", "power"});
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
    angleMembers.add("sin");
    angleMembers.add("cos");
    angleMembers.add("tan");
    angleMembers.add("asin");
    angleMembers.add("acos");
    angleMembers.add("atan");
    angleMembers.add("atan2");
    angleMembers.add("PI");
    methodsMissingParameterNames.put("sin", new String[] {"radians"});
    methodsMissingParameterNames.put("cos", new String[] {"radians"});
    methodsMissingParameterNames.put("tan", new String[] {"radians"});
    methodsMissingParameterNames.put("asin", new String[] {"sin"});
    methodsMissingParameterNames.put("acos", new String[] {"cos"});
    methodsMissingParameterNames.put("atan", new String[] {"tan"});
    methodsMissingParameterNames.put("atan2", new String[] {"y", "x"});
    methodsMissingParameterNames.put("exp", new String[] {"power"});
    methodsMissingParameterNames.put("log", new String[] {"x"});

    Map<String, String> duration = new HashMap<>();
    duration.put("duration", "new Duration(seconds: ");
    methodsWithWrappedArgs.put("delay", duration);
    Map<String, String> frequency = new HashMap<>();
    frequency.put("frequency", "new Duration(seconds: ");
    methodsWithWrappedArgs.put("addTimeListener", frequency);
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
    String rootConstructor = "(TextString resource, JointId root) {\n"
            + "  super(resource: resource, root: root);\n  }\n";
    classesToAddPassThroughConstructorsTo.put("Prop", rootConstructor);
    classesToAddPassThroughConstructorsTo.put("Aircraft", rootConstructor);
    classesToAddPassThroughConstructorsTo.put("Automobile", rootConstructor);
    classesToAddPassThroughConstructorsTo.put("Train", rootConstructor);
    classesToAddPassThroughConstructorsTo.put("Transport", rootConstructor);
    classesToAddPassThroughConstructorsTo.put("Watercraft", rootConstructor);
    String flyerConstructor = "(TextString resource,\n"
        + "       JointedModelPose spreadWingsPose,\n"
        + "       JointedModelPose foldWingsPose,\n"
        + "       JointId[] tailArray,\n"
        + "       JointId[] neckArray)  {\n"
        + "    super(resource: resource,\n"
        + "          spreadWingsPose: spreadWingsPose,\n"
        + "          foldWingsPose: foldWingsPose,\n"
        + "          tailArray: tailArray,\n"
        + "          neckArray: neckArray);\n  }";
    classesToAddPassThroughConstructorsTo.put("Flyer", flyerConstructor);
    String slithererConstructor = "(TextString resource, JointId[] tailArray) {\n"
        + "    super(resource: resource, tailArray: tailArray);\n  }";
    classesToAddPassThroughConstructorsTo.put("Slitherer", slithererConstructor);
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

  public String encode(ProcessableNode node) {
    node.process(this);
    return getCodeStringBuilder().toString();
  }

  /** Class structure **/

  @Override
  protected void appendSection(CodeOrganizer codeOrganizer, NamedUserType userType, Map.Entry<String, List<ProcessableNode>> entry) {
    super.appendSection(codeOrganizer, userType, entry);
    if ("ConstructorSection".equals(entry.getKey())) {
      appendResources(userType);
    }
  }

  private void appendResources(NamedUserType userType) {
    final String typeName = userType.getName();
    if (classesToAddPassThroughConstructorsTo.containsKey(typeName)) {
      appendNewLine();
      appendString(typeName);
      appendString(classesToAddPassThroughConstructorsTo.get(typeName));
      return;
    }
    Class resourceClass = getResourceClass(userType);
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
        if (value instanceof InstantiableTweedleNode) {
          appendStaticField(field, () -> ((InstantiableTweedleNode) value).encodeDefinition(this));
        } else {
          if (value.getClass().isArray() && IdentifiableTweedleNode.class.isAssignableFrom(field.getType().getComponentType())) {
            Object[] values = (Object[]) value;
            appendStaticField(field, () -> {
              // The new X[] syntax is not required in Java to create an array, but it is in tweedle, for now
              appendString("new ");
              appendString(field.getType().getSimpleName());
              appendList(values, (v) -> appendString(((IdentifiableTweedleNode) v).getCodeIdentifier(this)));
            });
          } else {
            Logger.info("Export will skip non-generator field " + resourceClass.getSimpleName() + "." + field.getName());
          }
        }
      } catch (IllegalAccessException e) {
        Logger.info("Export will skip inaccessible field " + resourceClass.getSimpleName() + "." + field.getName());
      }
    }
  }

  private void appendStaticField(Field field, Runnable value) {
    appendSingleCodeLine(() -> {
      appendVisibilityTag(field.getAnnotation(FieldTemplate.class));
      appendString("static ");
      appendString(field.getType().getSimpleName());
      appendSpace();
      appendString(field.getName());
      appendAssignmentOperator();
      value.run();
    });
  }

  public void appendNewJointId(String joint, String parentReference) {
    appendInstantiation("JointId", () -> {
      appendArg("name", () -> quoteString(joint));
      appendAnotherArg("parent", parentReference);
    });
  }

  public String getFieldReference(String type, String field) {
    return tweedleTypeName(type) + "." + field;
  }

  public void appendNewPose(InstantiableTweedleNode[] jointTransformations) {
    appendInstantiation("JointedModelPose", () -> {
      appendString("pairs: new JointIdTransformationPair[]");
      appendList(jointTransformations, (v) -> {
        appendNewLine();
        v.encodeDefinition(this);
      });
    });
  }

  public void appendNewJointTransformation(String jointId, AffineMatrix4x4 transformation) {
    appendString("        ");
    appendInstantiation("JointIdTransformationPair", () -> {
      appendArg("joint", jointId);
      appendAnotherArg("orientation", () -> {
        final UnitQuaternion orientationUnitQuaternion = transformation.orientation.createUnitQuaternion();
        appendInstantiation("Orientation", () -> {
          appendArg("x", Double.toString(orientationUnitQuaternion.x));
          appendAnotherArg("y", Double.toString(orientationUnitQuaternion.y));
          appendAnotherArg("z", Double.toString(orientationUnitQuaternion.z));
          appendAnotherArg("w", Double.toString(orientationUnitQuaternion.w));
        });
      });
      appendAnotherArg("position", () -> {
        final Point3 position = transformation.translation;
        appendInstantiation("Position", () -> {
          appendArg("x", Double.toString(position.x));
          appendAnotherArg("y", Double.toString(position.y));
          appendAnotherArg("z", Double.toString(position.z));
        });
      });
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
  public void processConstructor(NamedUserConstructor constructor) {
    appendIndent();
    processTypeName(constructor.getDeclaringType());
    appendParameters(constructor);
    appendStatement(constructor.body.getValue());
    appendNewLine();
  }

  @Override
  public void processSuperConstructor(SuperConstructorInvocationStatement supCon) {
    processSingleStatement(supCon, () -> {
      processSuperReference();
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
        Class resourceClass = getResourceClass(nut);
        if (resourceClass != null) {
          if ("Flyer".equals(declaringType.getSuperType().getName())) {
            appendAnotherArg("spreadWingsPose", nut.getName() + ".SPREAD_WINGS_POSE");
            appendAnotherArg("foldWingsPose", nut.getName() + ".FOLD_WINGS_POSE");
            appendAnotherArg("tailArray", nut.getName() + ".TAIL_ARRAY");
            appendAnotherArg("neckArray", nut.getName() + ".NECK_ARRAY");
            return;
          }
          if ("Slitherer".equals(declaringType.getSuperType().getName())) {
            appendAnotherArg("tailArray", nut.getName() + ".TAIL_ARRAY");
            return;
          }
          if (Arrays.stream(resourceClass.getDeclaredFields()).anyMatch(field -> "ROOT".equals(field.getName()))) {
            appendAnotherArg("root", nut.getName() + ".ROOT");
            return;
          }
          if ("Biplane".equals(nut.getName())) {
            appendString(", root: Biplane.BIPLANE_ROOT");
          }
        }
      }
    }
  }

  @Override
  public void processMethod(UserMethod method) {
    appendNewLine();
    appendIndent();
    super.processMethod(method);
    appendNewLine();
  }

  @Override
  public void appendMethodHeader(AbstractMethod method) {
    if (method.isStatic()) {
      appendString("static ");
    }
    processTypeName(method.getReturnType());
    appendSpace();
    appendString(method.getName());
    appendParameters(method);
  }

  @Override
  public void processGetter(Getter getter) {
    appendIndent();
    super.processGetter(getter);
    appendNewLine();
  }

  @Override
  public void processSetter(Setter setter) {
    appendIndent();
    super.processSetter(setter);
    appendNewLine();
  }

  /** Statements **/

  @Override
  public void processLocalDeclaration(LocalDeclarationStatement stmt) {
    processSingleStatement(stmt, () -> {
      UserLocal localVar = stmt.local.getValue();
      processTypeName(localVar.getValueType());
      appendSpace();
      appendString(localVar.getValidName());
      appendAssignmentOperator();
      processExpression(stmt.initializer.getValue());
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
    processKeyedArgument(arg);
  }

  @Override
  public void processKeyedArgument(JavaKeyedArgument arg) {
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
    processExpression(expressionValue);
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
  public void processArgument(AbstractParameter parameter, AbstractArgument argument) {
    final String parameterLabel = getParameterLabel(parameter);
    appendString(parameterLabel);
    appendString(": ");

    Map<String, String> wrappedParams = methodsWithWrappedArgs.get(parameter.getCode().getName());
    appendWrappedArg(argument, parameterLabel, wrappedParams);
  }

  private void appendWrappedArg(ProcessableNode argument, String parameterLabel, Map<String, String> wrappedParams) {
    String argStart = wrappedParams == null ? null : wrappedParams.get(parameterLabel);
    if (argStart != null) {
      appendString(argStart);
    }
    argument.process(this);
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
  public void processSingleStatement(Statement stmt, Runnable appender) {
    appendIndent(stmt);
    super.processSingleStatement(stmt, appender);
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
  public void processCountLoop(CountLoop loop) {
    appendCodeFlowStatement(loop, () -> {
      appendString("countUpTo( ");
      appendString(loop.getVariableName());
      appendString(" < ");
      processExpression(loop.count.getValue());
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
  public void processDoInOrder(DoInOrder doInOrder) {
    appendLabeledCodeFlow(doInOrder, "doInOrder");
  }

  @Override
  public void processDoTogether(DoTogether doTogether) {
    appendLabeledCodeFlow(doTogether, "doTogether");
  }

  private void appendLabeledCodeFlow(AbstractStatementWithBody stmt, String label) {
    appendCodeFlowStatement(stmt, () -> {
      appendString(label);
      appendStatement(stmt.body.getValue());
    });
  }

  @Override
  public void processEachInTogether(AbstractEachInTogether eachInTogether) {
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
  protected void appendTargetAndMember(Expression target, String member, AbstractType<?, ?, ?> returnType) {
    if (targetIsMath(target)) {
      appendString(tweedleModuleForMath(member, returnType));
    } else {
      processExpression(target);
    }
    appendAccessSeparator();

    String tweedleName = membersToRename.get(member);
    appendString(tweedleName == null ? member : tweedleName);
  }

  private boolean targetIsMath(Expression target) {
    if (target instanceof TypeExpression) {
      AbstractType<?, ?, ?> innerType = ((TypeExpression) target).value.getValue();
      return innerType instanceof JavaType && "Math".equals(innerType.getName());
    }
    return false;
  }

  private String tweedleModuleForMath(String member, AbstractType<?, ?, ?> returnType) {
    if (returnType != null && "int".equals(returnType.getName())) {
      return "$WholeNumber";
    }
    if (angleMembers.contains(member)) {
      return "$Angle";
    }
    return "$DecimalNumber";
  }

  @Override
  public void processResourceExpression(ResourceExpression resourceExpression) {
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
  protected void appendConcatenationOperator() {
    appendString(" .. ");
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
  public void processTypeName(AbstractType<?, ?, ?> type) {
    appendString(tweedleTypeName(type.getName()));
  }

  private String tweedleTypeName(String typeName) {
    String newName = typesToRename.get(typeName);
    if (newName != null) {
      return newName;
    }
    if (typeName.endsWith("Resource")) {
      return typeName.substring(0, typeName.length() - 8);
    }
    return typeName;
  }

  @Override
  protected String getListSeparator() {
    return ", ";
  }

  /** Helper methods **/

  // Enums are replaced with strings and this is used in the conversion.
  private Class getResourceClass(NamedUserType nut) {
    try {
      return Class.forName(getResourceName(nut));
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  private String getResourceName(NamedUserType nut) {
    String owningClass = nut.getName();
    if ("SandDunes".equals(owningClass)) {
      owningClass = "Terrain";
    }
    return "org.lgna.story.resources." + nut.getSuperType().getName().toLowerCase() + "." + owningClass + "Resource";
  }

  private void appendVisibilityTag(FieldTemplate fieldAnnotation) {
    if (fieldAnnotation == null) {
      return;
    }
    switch (fieldAnnotation.visibility()) {
    case COMPLETELY_HIDDEN:
      appendString("@CompletelyHidden ");
      break;
    case PRIME_TIME:
      appendString("@PrimeTime ");
      break;
    case TUCKED_AWAY:
      appendString("@TuckedAway ");
      break;
    default:
    }
  }

  private void appendInstantiation(String className, Runnable args) {
    appendString("new ");
    appendString(className);
    parenthesize(args);
  }

  private void appendArg(String label, String value) {
    appendString(label);
    appendString(": ");
    appendString(value);
  }

  private void appendArg(String label, Runnable value) {
    appendString(label);
    appendString(": ");
    value.run();
  }

  private void appendAnotherArg(String label, String value) {
    appendString(getListSeparator());
    appendArg(label, value);
  }

  private void appendAnotherArg(String label, Runnable value) {
    appendString(getListSeparator());
    appendArg(label, value);
  }

  private <T> void appendList(T[] values, Consumer<T> appendValue) {
    bracketize(() -> {
      int i = 0;
      while (i < values.length) {
        appendValue.accept(values[i]);
        i++;
        if (i < values.length) {
          appendString(getListSeparator());
        }
      }
    });
  }

  private void quoteString(String aString) {
    appendString("\"");
    appendString(aString);
    appendString("\"");
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
