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

/**
 * @author Dennis Cosgrove
 */
public class JavaFormatter extends Formatter {
	private static final java.util.Map<Class<?>, String> templateMap;
	private static final java.util.Map<org.lgna.project.ast.ArithmeticInfixExpression.Operator, String> arithmeticOperatorMap;
	private static final java.util.Map<org.lgna.project.ast.ConditionalInfixExpression.Operator, String> conditionalOperatorMap;
	private static final java.util.Map<org.lgna.project.ast.RelationalInfixExpression.Operator, String> relationalOperatorMap;
	static {
		java.util.Map<Class<?>, String> tempTemplateMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		tempTemplateMap.put( org.lgna.project.ast.ExpressionStatement.class, "</expression/>;" );
		tempTemplateMap.put( org.lgna.project.ast.WhileLoop.class, "while( </conditional/> ) {\n\t</body/>\n}" );
		tempTemplateMap.put( org.lgna.project.ast.CountLoop.class, "</__constant__/> = </count/>;\nfor( </__variable__/> = 0; </_variable_/> < </_constant_/>; </_variable_/>++ ) {\n\t</body/>\n}" );
		tempTemplateMap.put( org.lgna.project.ast.BooleanExpressionBodyPair.class, "if( </expression/> ) {\n\t</body/>" );
		tempTemplateMap.put( org.lgna.project.ast.ConditionalStatement.class, "</booleanExpressionBodyPairs/>\n} else {\n\t</elseBody/>\n}" );
		tempTemplateMap.put( org.lgna.project.ast.MethodInvocation.class, "</expression/></method/>(</requiredArguments/></variableArguments/></keyedArguments/>)" );
		tempTemplateMap.put( org.lgna.project.ast.FieldAccess.class, "</expression/>.</field/>" );
		tempTemplateMap.put( org.lgna.project.ast.LocalDeclarationStatement.class, "</__local__/> = </initializer/> ;" );
		tempTemplateMap.put( org.lgna.project.ast.DoInOrder.class, "/*do in order*/ {\n\t</body/>\n}" );
		tempTemplateMap.put( org.lgna.project.ast.DoTogether.class, "ThreadUtilities.doTogether( ()-> {\n\t</body/>\n} );" );
		tempTemplateMap.put( org.lgna.project.ast.ForEachInArrayLoop.class, "for( </__item__/> : </array/> ) {\n\t</body/>\n}" );
		tempTemplateMap.put( org.lgna.project.ast.TypeExpression.class, "</value/>" );
		tempTemplateMap.put( org.lgna.project.ast.InstanceCreation.class, "new </constructor/>( </requiredArguments/></variableArguments/></keyedArguments/> )" );
		tempTemplateMap.put( org.lgna.project.ast.LogicalComplement.class, "!</operand/>" );
		tempTemplateMap.put( org.lgna.project.ast.NullLiteral.class, "null" );
		tempTemplateMap.put( org.lgna.project.ast.LambdaExpression.class, "{# </value/> }" );
		tempTemplateMap.put( org.lgna.project.ast.EachInArrayTogether.class, "ThreadUtilities.eachInTogether( ( </__item__/> ) -> {\n\t</body/>\n}, </array/> );" );
		templateMap = java.util.Collections.unmodifiableMap( tempTemplateMap );

		java.util.Map<org.lgna.project.ast.ArithmeticInfixExpression.Operator, String> tempArithmeticOperatorMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		tempArithmeticOperatorMap.put( org.lgna.project.ast.ArithmeticInfixExpression.Operator.INTEGER_DIVIDE, "</leftOperand/> / </rightOperand/>" );
		tempArithmeticOperatorMap.put( org.lgna.project.ast.ArithmeticInfixExpression.Operator.REAL_REMAINDER, "Math.IEEEremainder( </leftOperand/>, </rightOperand/> )" );
		tempArithmeticOperatorMap.put( org.lgna.project.ast.ArithmeticInfixExpression.Operator.INTEGER_REMAINDER, "</leftOperand/> % </rightOperand/>" );
		arithmeticOperatorMap = java.util.Collections.unmodifiableMap( tempArithmeticOperatorMap );

		java.util.Map<org.lgna.project.ast.ConditionalInfixExpression.Operator, String> tempConditionalOperatorMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		tempConditionalOperatorMap.put( org.lgna.project.ast.ConditionalInfixExpression.Operator.AND, "</leftOperand/> && </rightOperand/>" );
		tempConditionalOperatorMap.put( org.lgna.project.ast.ConditionalInfixExpression.Operator.OR, "</leftOperand/> || </rightOperand/>" );
		conditionalOperatorMap = java.util.Collections.unmodifiableMap( tempConditionalOperatorMap );

		java.util.Map<org.lgna.project.ast.RelationalInfixExpression.Operator, String> tempRelationalOperatorMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		tempRelationalOperatorMap.put( org.lgna.project.ast.RelationalInfixExpression.Operator.LESS_EQUALS, "</leftOperand/> <= </rightOperand/>" );
		tempRelationalOperatorMap.put( org.lgna.project.ast.RelationalInfixExpression.Operator.GREATER_EQUALS, "</leftOperand/> >= </rightOperand/>" );
		tempRelationalOperatorMap.put( org.lgna.project.ast.RelationalInfixExpression.Operator.EQUALS, "</leftOperand/> == </rightOperand/>" );
		tempRelationalOperatorMap.put( org.lgna.project.ast.RelationalInfixExpression.Operator.NOT_EQUALS, "</leftOperand/> != </rightOperand/>" );
		relationalOperatorMap = java.util.Collections.unmodifiableMap( tempRelationalOperatorMap );
	}

	private static class SingletonHolder {
		private static JavaFormatter instance = new JavaFormatter();
	}

	public static JavaFormatter getInstance() {
		return SingletonHolder.instance;
	}

	private JavaFormatter() {
		super( "Java" );
	}

	@Override
	public String getHeaderTextForCode( org.lgna.project.ast.UserCode code ) {
		StringBuilder sb = new StringBuilder();
		if( code instanceof org.lgna.project.ast.UserMethod ) {
			sb.append( "</getReturnType()/> </getName()/>" );
		} else {
			sb.append( "</getDeclaringType()/>" );
		}
		sb.append( " ( </getParameters()/> ) {" );
		return sb.toString();
	}

	@Override
	public String getTrailerTextForCode( org.lgna.project.ast.UserCode code ) {
		return "}";
	}

	@Override
	public String getTemplateText( Class<?> cls ) {
		String rv = templateMap.get( cls );
		if( rv != null ) {
			//pass
		} else {
			rv = super.getTemplateText( cls );
		}
		return rv;
	}

	@Override
	public String getInfixExpressionText( org.lgna.project.ast.InfixExpression<?> infixExpression ) {
		String rv;
		if( infixExpression instanceof org.lgna.project.ast.ArithmeticInfixExpression ) {
			org.lgna.project.ast.ArithmeticInfixExpression arithmeticInfixExpression = (org.lgna.project.ast.ArithmeticInfixExpression)infixExpression;
			rv = arithmeticOperatorMap.get( arithmeticInfixExpression.operator.getValue() );
		} else if( infixExpression instanceof org.lgna.project.ast.ConditionalInfixExpression ) {
			org.lgna.project.ast.ConditionalInfixExpression conditionalInfixExpression = (org.lgna.project.ast.ConditionalInfixExpression)infixExpression;
			rv = conditionalOperatorMap.get( conditionalInfixExpression.operator.getValue() );
		} else if( infixExpression instanceof org.lgna.project.ast.RelationalInfixExpression ) {
			org.lgna.project.ast.RelationalInfixExpression relationalInfixExpression = (org.lgna.project.ast.RelationalInfixExpression)infixExpression;
			rv = relationalOperatorMap.get( relationalInfixExpression.operator.getValue() );
		} else {
			rv = null;
		}
		if( rv != null ) {
			//pass
		} else {
			rv = super.getInfixExpressionText( infixExpression );
		}
		return rv;
	}

	@Override
	protected String getTextForCls( Class<?> cls ) {
		return cls.getSimpleName();
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
	protected String getTextForJavaParameter( org.lgna.project.ast.JavaParameter javaParameter ) {
		return javaParameter.getName();
	}

	@Override
	protected String getTextForMethodReflectionProxy( org.lgna.project.ast.MethodReflectionProxy methodReflectionProxy ) {
		return methodReflectionProxy.getName();
	}

	@Override
	public boolean isTypeExpressionDesired() {
		return true;
	}

	@Override
	protected String getNameForField( java.lang.reflect.Field fld ) {
		return fld.getName();
	}

	@Override
	public String getFinalText() {
		return "final";
	}
}
