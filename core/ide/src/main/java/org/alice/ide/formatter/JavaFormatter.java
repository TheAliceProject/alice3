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
package org.alice.ide.formatter;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.lgna.project.ast.ArithmeticInfixExpression;
import org.lgna.project.ast.BooleanExpressionBodyPair;
import org.lgna.project.ast.ConditionalInfixExpression;
import org.lgna.project.ast.ConditionalStatement;
import org.lgna.project.ast.CountLoop;
import org.lgna.project.ast.DoInOrder;
import org.lgna.project.ast.DoTogether;
import org.lgna.project.ast.EachInArrayTogether;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.ForEachInArrayLoop;
import org.lgna.project.ast.InfixExpression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.LambdaExpression;
import org.lgna.project.ast.LocalDeclarationStatement;
import org.lgna.project.ast.LogicalComplement;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NullLiteral;
import org.lgna.project.ast.RelationalInfixExpression;
import org.lgna.project.ast.TypeExpression;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.WhileLoop;

import java.util.Collections;
import java.util.Map;

/**
 * Formats code on Alice tiles in a Java style.
 *
 * @author Dennis Cosgrove
 */
public class JavaFormatter extends Formatter {
  private static final Map<Class<?>, String> templateMap;
  private static final Map<ArithmeticInfixExpression.Operator, String> arithmeticOperatorMap;
  private static final Map<ConditionalInfixExpression.Operator, String> conditionalOperatorMap;
  private static final Map<RelationalInfixExpression.Operator, String> relationalOperatorMap;

  static {
    Map<Class<?>, String> tempTemplateMap = Maps.newHashMap();
    tempTemplateMap.put(ExpressionStatement.class, "</expression/>;");
    tempTemplateMap.put(WhileLoop.class, "while( </conditional/> ) {\n\t</body/>\n}");
    tempTemplateMap.put(CountLoop.class, "for( </__variable__/> = 0; </_variable_/> < </count/>; </_variable_/>++ ) {\n\t</body/>\n}");
    tempTemplateMap.put(BooleanExpressionBodyPair.class, "if( </expression/> ) {\n\t</body/>");
    tempTemplateMap.put(ConditionalStatement.class, "</booleanExpressionBodyPairs/>\n} else {\n\t</elseBody/>\n}");
    tempTemplateMap.put(MethodInvocation.class, "</expression/></method/>(</requiredArguments/></variableArguments/></keyedArguments/>)");
    tempTemplateMap.put(FieldAccess.class, "</expression/>.</field/>");
    tempTemplateMap.put(LocalDeclarationStatement.class, "</__local__/> = </initializer/> ;");
    tempTemplateMap.put(DoInOrder.class, "/*do in order*/ {\n\t</body/>\n}");
    tempTemplateMap.put(DoTogether.class, "ThreadUtilities.doTogether( ()-> {\n\t</body/>\n} );");
    tempTemplateMap.put(ForEachInArrayLoop.class, "for( </__item__/> : </array/> ) {\n\t</body/>\n}");
    tempTemplateMap.put(TypeExpression.class, "</value/>");
    tempTemplateMap.put(InstanceCreation.class, "new </constructor/>( </requiredArguments/></variableArguments/></keyedArguments/> )");
    tempTemplateMap.put(LogicalComplement.class, "!</operand/>");
    tempTemplateMap.put(NullLiteral.class, "null");
    tempTemplateMap.put(LambdaExpression.class, "{# </value/> }");
    tempTemplateMap.put(EachInArrayTogether.class, "ThreadUtilities.eachInTogether( ( </__item__/> ) -> {\n\t</body/>\n}, </array/> );");
    templateMap = Collections.unmodifiableMap(tempTemplateMap);

    Map<ArithmeticInfixExpression.Operator, String> tempArithmeticOperatorMap = Maps.newHashMap();
    tempArithmeticOperatorMap.put(ArithmeticInfixExpression.Operator.INTEGER_DIVIDE, "</leftOperand/> / </rightOperand/>");
    tempArithmeticOperatorMap.put(ArithmeticInfixExpression.Operator.REAL_REMAINDER, "Math.IEEEremainder( </leftOperand/>, </rightOperand/> )");
    tempArithmeticOperatorMap.put(ArithmeticInfixExpression.Operator.INTEGER_REMAINDER, "</leftOperand/> % </rightOperand/>");
    arithmeticOperatorMap = Collections.unmodifiableMap(tempArithmeticOperatorMap);

    Map<ConditionalInfixExpression.Operator, String> tempConditionalOperatorMap = Maps.newHashMap();
    tempConditionalOperatorMap.put(ConditionalInfixExpression.Operator.AND, "</leftOperand/> && </rightOperand/>");
    tempConditionalOperatorMap.put(ConditionalInfixExpression.Operator.OR, "</leftOperand/> || </rightOperand/>");
    conditionalOperatorMap = Collections.unmodifiableMap(tempConditionalOperatorMap);

    Map<RelationalInfixExpression.Operator, String> tempRelationalOperatorMap = Maps.newHashMap();
    tempRelationalOperatorMap.put(RelationalInfixExpression.Operator.LESS_EQUALS, "</leftOperand/> <= </rightOperand/>");
    tempRelationalOperatorMap.put(RelationalInfixExpression.Operator.GREATER_EQUALS, "</leftOperand/> >= </rightOperand/>");
    tempRelationalOperatorMap.put(RelationalInfixExpression.Operator.EQUALS, "</leftOperand/> == </rightOperand/>");
    tempRelationalOperatorMap.put(RelationalInfixExpression.Operator.NOT_EQUALS, "</leftOperand/> != </rightOperand/>");
    relationalOperatorMap = Collections.unmodifiableMap(tempRelationalOperatorMap);
  }

  private static class SingletonHolder {
    private static JavaFormatter instance = new JavaFormatter();
  }

  public static JavaFormatter getInstance() {
    return SingletonHolder.instance;
  }

  private JavaFormatter() {
    super("Java");
  }

  @Override
  public String getHeaderTextForCode(UserCode code) {
    StringBuilder sb = new StringBuilder();
    if (code instanceof UserMethod) {
      sb.append("</getReturnType()/> </getName()/>");
    } else {
      sb.append("</getDeclaringType()/>");
    }
    sb.append(" ( </getParameters()/> ) {");
    return sb.toString();
  }

  @Override
  public String getTrailerTextForCode(UserCode code) {
    return "}";
  }

  @Override
  public String getTemplateText(Class<?> cls) {
    String rv = templateMap.get(cls);
    if (rv != null) {
      //pass
    } else {
      rv = super.getTemplateText(cls);
    }
    return rv;
  }

  @Override
  public String getInfixExpressionText(InfixExpression<?> infixExpression) {
    String rv;
    if (infixExpression instanceof ArithmeticInfixExpression) {
      ArithmeticInfixExpression arithmeticInfixExpression = (ArithmeticInfixExpression) infixExpression;
      rv = arithmeticOperatorMap.get(arithmeticInfixExpression.operator.getValue());
    } else if (infixExpression instanceof ConditionalInfixExpression) {
      ConditionalInfixExpression conditionalInfixExpression = (ConditionalInfixExpression) infixExpression;
      rv = conditionalOperatorMap.get(conditionalInfixExpression.operator.getValue());
    } else if (infixExpression instanceof RelationalInfixExpression) {
      RelationalInfixExpression relationalInfixExpression = (RelationalInfixExpression) infixExpression;
      rv = relationalOperatorMap.get(relationalInfixExpression.operator.getValue());
    } else {
      rv = null;
    }
    if (rv != null) {
      //pass
    } else {
      rv = super.getInfixExpressionText(infixExpression);
    }
    return rv;
  }

  // No localization for java
  @Override
  protected String localizeName(String key, String name) {
    return name;
  }

  @Override
  public String getTextForNull() {
    return "null";
  }

  @Override
  public String getTextForThis() {
    return "this";
  }

  @Override
  public boolean isTypeExpressionDesired() {
    return true;
  }

  @Override
  public String getFinalText() {
    return "final";
  }

  @Override
  protected String getClassesFormat() {
    return "%s classes";
  }

  @Override
  public String getNewFormat() {
    return "new %s( %s )";
  }
}
