/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.java.util.Sets;
import org.lgna.common.EachInTogetherRunnable;
import org.lgna.common.Resource;
import org.lgna.common.ThreadUtilities;
import org.lgna.project.code.ProcessableNode;
import org.lgna.project.code.CodeOrganizer;
import org.lgna.project.resource.ResourcesTypeWrapper;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class JavaCodeGenerator extends SourceCodeGenerator {

  public static class Builder {
    public Builder() {
    }

    public Builder isLambdaSupported(boolean isLambdaSupported) {
      this.isLambdaSupported = isLambdaSupported;
      return this;
    }

    public Builder isPublicStaticFinalFieldGetterDesired(boolean isPublicStaticFinalFieldGetterDesired) {
      this.isPublicStaticFinalFieldGetterDesired = isPublicStaticFinalFieldGetterDesired;
      return this;
    }

    public Builder setResourcesTypeWrapper(ResourcesTypeWrapper resourcesTypeWrapper) {
      this.resourcesTypeWrapper = resourcesTypeWrapper;
      return this;
    }

    public Builder addCommentsLocalizationBundleName(String bundleName) {
      this.commentsLocalizationBundleName = bundleName;
      return this;
    }

    public Builder addImportOnDemandPackage(Package pckg) {
      this.importOnDemandPackages.add(JavaPackage.getInstance(pckg));
      return this;
    }

    public Builder addCodeOrganizerDefinition(String key, CodeOrganizer.CodeOrganizerDefinition organizerDef) {
      this.codeOrganizerDefinitionMap.put(key, organizerDef);
      return this;
    }

    public Builder addDefaultCodeOrganizerDefinition(CodeOrganizer.CodeOrganizerDefinition organizerDef) {
      this.defaultCodeDefinitionOrganizer = organizerDef;
      return this;
    }

    public Builder addImportStaticMethod(java.lang.reflect.Method mthd) {
      assert mthd != null;
      assert Modifier.isStatic(mthd.getModifiers()) : mthd;
      this.importStaticMethods.add(JavaMethod.getInstance(mthd));
      return this;
    }

    public JavaCodeGenerator build() {
      return new JavaCodeGenerator(this);
    }

    private boolean isLambdaSupported;
    private boolean isPublicStaticFinalFieldGetterDesired;
    private ResourcesTypeWrapper resourcesTypeWrapper;
    private final List<JavaPackage> importOnDemandPackages = Lists.newLinkedList();
    private final List<JavaMethod> importStaticMethods = Lists.newLinkedList();
    private final Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitionMap = Maps.newHashMap();
    private CodeOrganizer.CodeOrganizerDefinition defaultCodeDefinitionOrganizer;
    private String commentsLocalizationBundleName;
  }

  public JavaCodeGenerator(Builder builder) {
    super(builder.codeOrganizerDefinitionMap, builder.defaultCodeDefinitionOrganizer);
    this.isLambdaSupported = builder.isLambdaSupported;
    this.isPublicStaticFinalFieldGetterDesired = builder.isPublicStaticFinalFieldGetterDesired;
    this.resourcesTypeWrapper = builder.resourcesTypeWrapper;
    this.packagesMarkedForOnDemandImport = Collections.unmodifiableList(builder.importOnDemandPackages);
    this.staticMethodsMarkedForImport = Collections.unmodifiableList(builder.importStaticMethods);
    this.commentsLocalizationBundleName = builder.commentsLocalizationBundleName;
  }

  @Override
  protected void pushStatementDisabled() {
    super.pushStatementDisabled();
    if (isCodeNowDisabled()) {
      appendString("\n/* disabled\n");
    }
  }

  @Override
  protected void popStatementDisabled() {
    super.popStatementDisabled();
    if (isCodeNowEnabled()) {
      appendString("\n*/\n");
    }
  }

  @Override
  boolean isLambdaSupported() {
    return isLambdaSupported;
  }

  @Override
  public boolean isPublicStaticFinalFieldGetterDesired() {
    return isPublicStaticFinalFieldGetterDesired;
  }

  @Override
  public void processNewJointId(String joint, String parent, String model) {
    throw new RuntimeException("This should not be called.");
  }

  @Override
  protected void appendConcatenationOperator() {
    appendString(" + ");
  }

  @Override
  protected void appendAssignmentOperator() {
    appendChar('=');
  }

  private void appendResource(Resource resource) {
    getCodeStringBuilder().append(ResourcesTypeWrapper.getTypeName()).append(".").append(ResourcesTypeWrapper.getFixedName(resource));
  }

  @Override
  public void processResourceExpression(ResourceExpression resourceExpression) {
    Resource resource = resourceExpression.resource.getValue();
    if (resource != null) {
      if (resourcesTypeWrapper != null) {
        UserField field = resourcesTypeWrapper.getFieldForResource(resource);
        if (field != null) {
          getCodeStringBuilder().append(field.getDeclaringType().getName());
          getCodeStringBuilder().append(".");
          getCodeStringBuilder().append(field.getName());
        } else {
          appendResource(resource);
        }
      } else {
        appendResource(resource);
      }
    } else {
      getCodeStringBuilder().append("null"); //todo?
    }
  }

  @Override
  public void processClass(CodeOrganizer codeOrganizer, NamedUserType userType) {
    super.processClass(codeOrganizer, userType);
    // Prepend Imports
    getCodeStringBuilder().insert(0, getImports());
  }

  @Override
  protected void appendClassHeader(NamedUserType userType) {
    appendString("class ");
    processTypeName(userType);
    appendString(" extends ");
    processTypeName(userType.superType.getValue());
    appendString("{");
  }

  @Override
  protected void appendClassFooter() {
    appendString("}");
  }

  @Override
  protected void appendSection(CodeOrganizer codeOrganizer, NamedUserType userType, Map.Entry<String, List<ProcessableNode>> entry) {
    boolean shouldCollapseSection = codeOrganizer.shouldCollapseSection(entry.getKey());
    appendSectionPrefix(userType, entry.getKey(), shouldCollapseSection);
    super.appendSection(codeOrganizer, userType, entry);
    appendSectionPostfix(userType, entry.getKey(), shouldCollapseSection);
  }

  protected void appendSectionPrefix(AbstractType<?, ?, ?> declaringType, String sectionName, boolean shouldCollapse) {
    String sectionComment = getLocalizedMultiLineComment(declaringType, sectionName);
    if (sectionComment != null) {
      getCodeStringBuilder().append("\n\n").append(sectionComment).append("\n");
    }
  }

  protected void appendSectionPostfix(AbstractType<?, ?, ?> declaringType, String sectionName, boolean shouldCollapse) {
    String sectionComment = getLocalizedMultiLineComment(declaringType, sectionName + ".end");
    if (sectionComment != null) {
      getCodeStringBuilder().append("\n").append(sectionComment).append("\n");
    }
  }

  @Override
  public void processConstructor(NamedUserConstructor constructor) {
    appendMemberPrefix(constructor);
    appendString(constructor.getAccessLevel().getJavaText());
    processTypeName(constructor.getDeclaringType());
    appendParameters(constructor);
    appendStatement(constructor.body.getValue());
    appendMemberPostfix(constructor);
  }


  @Override
  public void processSuperConstructor(SuperConstructorInvocationStatement supCon) {
    processSingleStatement(supCon, () -> {
      processSuperReference();
      appendArguments(supCon);
    });
  }

  @Override
  public void processTypeName(AbstractType<?, ?, ?> type) {
    if (type instanceof JavaType) {
      JavaType javaType = (JavaType) type;
      if (!javaType.isPrimitive()) {
        JavaPackage javaPackage = javaType.getPackage();
        if (javaPackage != null) {
          JavaType enclosingType = javaType.getEnclosingType();
          //todo: choose EnclosingTypeName.ClassName instead?
          if (enclosingType != null || !packagesMarkedForOnDemandImport.contains(javaPackage)) {
            typesToImport.add(javaType);
          } else {
            packagesToImportOnDemand.add(javaPackage);
          }
        }
        // else - should be covered already by the primitive check
      }
    }
    //todo: handle imports
    appendString(type.getName());
  }

  @Override
  protected void appendTargetAndMethodName(Expression target, AbstractMethod method) {
    if (method instanceof JavaMethod && method.isStatic() && staticMethodsMarkedForImport.contains(method)) {
      methodsToImportStatic.add((JavaMethod) method);
      appendString(method.getName());
    } else {
      super.appendTargetAndMethodName(target, method);
    }
  }

  @Override
  public void processMethod(UserMethod method) {
    appendMemberPrefix(method);
    super.processMethod(method);
    appendMemberPostfix(method);
  }

  @Override
  public void appendMethodHeader(AbstractMethod method) {
    AbstractMethod overridenMethod = AstUtilities.getOverridenMethod(method);
    if (overridenMethod != null) {
      appendString("@Override ");
    }
    appendString(method.getAccessLevel().getJavaText());
    if (method.isStatic()) {
      appendString("static ");
    }
    processTypeName(method.getReturnType());
    appendSpace();
    appendString(method.getName());
    appendParameters(method);
  }

  @Override
  public void processLocalDeclaration(LocalDeclarationStatement stmt) {
    processSingleStatement(stmt, () -> {
      UserLocal localVar = stmt.local.getValue();
      if (localVar.isFinal.getValue()) {
        appendString("final ");
      }
      processTypeName(localVar.getValueType());
      appendSpace();
      appendString(localVar.getValidName());
      appendAssignmentOperator();
      processExpression(stmt.initializer.getValue());
    });
  }

  @Override
  public void processArgument(AbstractParameter parameter, AbstractArgument argument) {
    argument.process(this);
  }

  @Override
  public void processKeyedArgument(JavaKeyedArgument arg) {
    Expression expressionValue = arg.expression.getValue();
    if (expressionValue instanceof MethodInvocation) {
      MethodInvocation methodInvocation = (MethodInvocation) expressionValue;
      AbstractMethod method = methodInvocation.method.getValue();
      AbstractType<?, ?, ?> factoryType = AstUtilities.getKeywordFactoryType(arg);
      if (factoryType != null) {
        processTypeName(factoryType);
        appendChar('.');
        appendString(method.getName());
        appendArguments(methodInvocation);
      } else {
        processExpression(expressionValue);
      }
    } else {
      processExpression(expressionValue);
    }
  }

  protected String getImportsPrefix() {
    return "";
  }

  protected String getImportsPostfix() {
    return "";
  }

  private StringBuilder getImports() {
    StringBuilder sb = new StringBuilder();
    sb.append(getImportsPrefix());
    for (JavaPackage packageToImportOnDemand : packagesToImportOnDemand) {
      sb.append("import ");
      sb.append(packageToImportOnDemand.getName());
      sb.append(".*;");
    }
    for (JavaType typeToImport : typesToImport) {
      JavaPackage pack = typeToImport.getPackage();
      if (!"java.lang".contentEquals(pack.getName())) {
        sb.append("import ");
        sb.append(typeToImport.getPackage().getName());
        sb.append('.');
        JavaType enclosingType = typeToImport.getEnclosingType();
        if (enclosingType != null) {
          sb.append(enclosingType.getName());
          sb.append('.');
        }
        sb.append(typeToImport.getName());
        sb.append(';');
      }
    }
    for (JavaMethod methodToImportStatic : methodsToImportStatic) {
      sb.append("import static ");
      sb.append(methodToImportStatic.getDeclaringType().getPackage().getName());
      sb.append('.');
      sb.append(methodToImportStatic.getDeclaringType().getName());
      sb.append('.');
      sb.append(methodToImportStatic.getName());
      sb.append(';');
    }
    sb.append(getImportsPostfix());
    return sb;
  }

  @Override
  public void processCountLoop(CountLoop loop) {
    appendCodeFlowStatement(loop, () -> {
      String variableName = loop.getVariableName();
      appendString("for(Integer ");
      appendString(variableName);
      appendString("=0;");
      appendString(variableName);
      appendString("<");
      processExpression(loop.count.getValue());
      appendString(";");
      appendString(variableName);
      appendString("++)");
      appendStatement(loop.body.getValue());
    });
  }

  @Override
  protected void appendForEachToken() {
    appendString("for");
  }

  @Override
  protected void appendInEachToken() {
    appendString(" : ");
  }

  @Override
  public void processDoInOrder(DoInOrder doInOrder) {
    openBlock();
    try {
      final String doInOrderName = ResourceBundleUtilities.getStringFromSimpleNames(doInOrder.getClass(), "org.alice.ide.controlflow.Templates");
      // TODO adjust CodeFormatter to not insert linefeed before this comment
      appendSingleLineComment(doInOrderName);
    } catch (MissingResourceException mre) {
      System.out.println("No resource bundle setup to localize do in order.");
    }
    BlockStatement blockStatement = doInOrder.body.getValue();
    for (Statement subStatement : blockStatement.statements) {
      appendStatement(subStatement);
    }
    closeBlock();
  }

  @Override
  public void processDoTogether(DoTogether doTogether) {
    JavaType threadUtilitiesType = JavaType.getInstance(ThreadUtilities.class);
    JavaMethod doTogetherMethod = threadUtilitiesType.getDeclaredMethod("doTogether", Runnable[].class);
    TypeExpression target = new TypeExpression(threadUtilitiesType);
    appendTargetAndMethodName(target, doTogetherMethod);
    appendString("(");
    String prefix = "";
    for (Statement statement : doTogether.body.getValue().statements) {
      appendString(prefix);
      if (isLambdaSupported()) {
        appendString("()->{");
      } else {
        appendString("new Runnable(){public void run(){");
      }
      if (statement instanceof DoInOrder) {
        DoInOrder doInOrder = (DoInOrder) statement;
        BlockStatement blockStatement = doInOrder.body.getValue();
        for (Statement subStatement : blockStatement.statements) {
          appendStatement(subStatement);
        }
      } else {
        appendStatement(statement);
      }
      if (isLambdaSupported()) {
        appendString("}");
      } else {
        appendString("}}");
      }
      prefix = ",";
    }
    appendString(");");
  }

  @Override
  public void processEachInTogether(AbstractEachInTogether eachInTogether) {
    JavaType threadUtilitiesType = JavaType.getInstance(ThreadUtilities.class);
    JavaMethod eachInTogetherMethod = threadUtilitiesType.getDeclaredMethod("eachInTogether", EachInTogetherRunnable.class, Object[].class);
    TypeExpression target = new TypeExpression(threadUtilitiesType);
    appendTargetAndMethodName(target, eachInTogetherMethod);
    appendString("(");

    UserLocal itemValue = eachInTogether.item.getValue();
    AbstractType<?, ?, ?> itemType = itemValue.getValueType();
    if (isLambdaSupported()) {
      appendString("(");
      processTypeName(itemType);
      appendSpace();
      appendString(itemValue.getName());
      appendString(")->");
    } else {
      appendString("new ");
      processTypeName(JavaType.getInstance(EachInTogetherRunnable.class));
      appendString("<");
      processTypeName(itemType);
      appendString(">() { public void run(");
      processTypeName(itemType);
      appendSpace();
      appendString(itemValue.getName());
      appendString(")");
    }
    appendStatement(eachInTogether.body.getValue());
    if (!isLambdaSupported()) {
      appendString("}");
    }
    Expression arrayOrIterableExpression = eachInTogether.getArrayOrIterableProperty().getValue();
    if (arrayOrIterableExpression instanceof ArrayInstanceCreation) {
      ArrayInstanceCreation arrayInstanceCreation = (ArrayInstanceCreation) arrayOrIterableExpression;
      for (Expression variableLengthExpression : arrayInstanceCreation.expressions) {
        appendString(",");
        processExpression(variableLengthExpression);
      }
    } else {
      appendString(",");
      processExpression(arrayOrIterableExpression);
    }
    appendString(");");
  }

  private void appendMemberPrefix(AbstractMember member) {
    String memberComment = getLocalizedMultiLineComment(member.getDeclaringType(), member.getName());
    if (memberComment != null) {
      getCodeStringBuilder().append("\n").append(memberComment).append("\n");
    }
  }

  private void appendMemberPostfix(AbstractMember member) {
    String memberComment = getLocalizedMultiLineComment(member.getDeclaringType(), member.getName() + ".end");
    if (memberComment != null) {
      getCodeStringBuilder().append("\n").append(memberComment).append("\n");
    }
  }

  @Override
  public void processMultiLineComment(String comment) {
    appendNewLine();
    super.processMultiLineComment(comment);
  }

  private String formatBlockComment(String commentText) {
    String[] commentLines = splitIntoLines(commentText);
    StringBuilder sb = new StringBuilder();

    sb.append("/* ");
    for (int i = 0; i < commentLines.length; i++) {
      sb.append(commentLines[i]);
      if (i < (commentLines.length - 1)) {
        sb.append("\n * "); // End each line with a new line and start each line with " *"
      }
    }
    sb.append(" */");
    return sb.toString();
  }

  protected String getLocalizedMultiLineComment(AbstractType<?, ?, ?> type, String sectionName) {
    String comment = getLocalizedComment(type, sectionName, Locale.getDefault());
    if (comment != null) {
      comment = formatBlockComment(comment);
    }
    return comment;
  }

  @Override
  public String getLocalizedComment(AbstractType<?, ?, ?> type, String itemName, Locale locale) {
    if (commentsLocalizationBundleName != null) {
      ResourceBundle resourceBundle = ResourceBundleUtilities.getUtf8Bundle(commentsLocalizationBundleName, locale);
      String key;
      AbstractType<?, ?, ?> t = type;
      boolean done = false;
      String returnVal = null;
      do {
        if (t != null) {
          key = t.getName() + "." + itemName;
          t = t.getSuperType();
        } else {
          key = itemName;
          done = true;
        }
        try {
          returnVal = resourceBundle.getString(key);
          break;
        } catch (RuntimeException re) {
          //pass;
        }
      } while (!done);
      if (returnVal != null) {
        returnVal = returnVal.replaceAll("<classname>", type.getName());
        returnVal = returnVal.replaceAll("<objectname>", itemName);
      }
      return returnVal;
    }
    return null;
  }

  @Override
  public void processField(UserField field) {
    appendMemberPrefix(field);
    appendString(field.getAccessLevel().getJavaText());
    if (field.isStatic()) {
      appendString("static ");
    }
    if (field.isFinal()) {
      appendString("final ");
    }
    super.processField(field);
    appendMemberPostfix(field);
  }

  @Override
  public void processGetter(Getter getter) {
    appendMemberPrefix(getter);
    super.processGetter(getter);
    appendMemberPostfix(getter);
  }

  @Override
  public void processSetter(Setter setter) {
    appendMemberPrefix(setter);
    super.processSetter(setter);
    appendMemberPostfix(setter);
  }

  @Override
  public void processLambda(UserLambda lambda) {
    appendMemberPrefix(lambda);
    super.processLambda(lambda);
    appendMemberPostfix(lambda);
  }

  private final boolean isLambdaSupported;
  private final boolean isPublicStaticFinalFieldGetterDesired;

  private final Set<JavaPackage> packagesToImportOnDemand = Sets.newHashSet();
  private final Set<JavaType> typesToImport = Sets.newHashSet();
  private final Set<JavaMethod> methodsToImportStatic = Sets.newHashSet();

  private final List<JavaPackage> packagesMarkedForOnDemandImport;
  private final List<JavaMethod> staticMethodsMarkedForImport;

  private final ResourcesTypeWrapper resourcesTypeWrapper;

  private final String commentsLocalizationBundleName;
}
