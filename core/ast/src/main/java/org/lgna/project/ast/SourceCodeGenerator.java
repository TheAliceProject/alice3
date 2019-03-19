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
import org.lgna.project.code.CodeAppender;
import org.lgna.project.code.CodeOrganizer;
import org.lgna.project.code.PrecedentedAppender;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

public abstract class SourceCodeGenerator {

	public SourceCodeGenerator( Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitionMap,
															CodeOrganizer.CodeOrganizerDefinition defaultCodeDefinitionOrganizer ) {
		codeOrganizerDefinitions = Collections.unmodifiableMap( codeOrganizerDefinitionMap );
		defaultCodeOrganizerDefn = defaultCodeDefinitionOrganizer;
	}

	public String getText() {
		return String.valueOf( getCodeStringBuilder() );
	}

	protected StringBuilder getCodeStringBuilder() {
		return codeStringBuilder;
	}

	// ** Class structure **

	CodeOrganizer getNewCodeOrganizerForTypeName( String typeName ) {
		return new CodeOrganizer( codeOrganizerDefinitions.getOrDefault( typeName, defaultCodeOrganizerDefn ) );
	}

	void appendClass( CodeOrganizer codeOrganizer, NamedUserType userType ) {
		appendClassHeader( userType );
		for( Map.Entry<String, List<CodeAppender>> entry : codeOrganizer.getOrderedSections().entrySet() ) {
			if( !entry.getValue().isEmpty() ) {
				appendSection( codeOrganizer, userType, entry );
			}
		}
		appendClassFooter();
	}

	protected void appendSection( CodeOrganizer codeOrganizer, NamedUserType userType,
																Map.Entry<String, List<CodeAppender>> entry ) {
		for( CodeAppender item : entry.getValue() ) {
			item.appendCode( this );
		}
	}

	protected abstract void appendClassHeader( NamedUserType userType );

  protected abstract void appendClassFooter();

	// ** Methods and Fields **

	public abstract void appendConstructor( NamedUserConstructor constructor );

	public void appendMethod( UserMethod method ) {
		appendMethodHeader( method );
		appendStatement( method.body.getValue() );
	}

	public abstract void appendMethodHeader( AbstractMethod method );

	protected void appendParameters( Code code ) {
		openParen();
		String prefix = "";
		int i = 0;
		for( AbstractParameter parameter : code.getRequiredParameters() ) {
			appendString( prefix );
			appendTypeName( parameter.getValueType() );
			appendSpace();
			String parameterName = parameter.getValidName();
			appendString( parameterName != null ? parameterName : "p" + i );
			prefix = getListSeparator();
			i += 1;
		}
		closeParen();
	}

	protected String getListSeparator() {
		return ",";
	}

	public void appendGetter( Getter getter ) {
		UserField field = getter.getField();
		appendMethodHeader( getter );
		openBlock();
		appendSingleCodeLine( () -> {
			appendString( "return this." );
			appendString( field.name.getValue() );
		} );
		closeBlock();
	}

	void appendIndexedGetter( ArrayItemGetter getter ) {
		UserField field = getter.getField();
		appendMethodHeader( getter );
		openBlock();
		appendSingleCodeLine( () -> {
			appendString( "return this." );
			appendString( field.name.getValue() );
			appendString( "[index]" );
		});
		closeBlock();
	}

	public void appendSetter( Setter setter ) {
		UserField field = setter.getField();
		appendMethodHeader( setter );
		openBlock();
		appendSingleCodeLine( () -> {
			appendString( "this." );
			appendString( field.name.getValue() );
			appendAssignmentOperator();
			appendString( field.name.getValue() );
		} );
		closeBlock();
	}

	void appendIndexedSetter( ArrayItemSetter setter ) {
		UserField field = setter.getField();
		appendMethodHeader( setter );
		openBlock();
		appendSingleCodeLine( () -> {
			appendString( "this." );
			appendString( field.name.getValue() );
			appendString( "[index]" );
			appendAssignmentOperator();
			appendString( "value" );
		} );
		closeBlock();
	}

	public void appendField( UserField field ) {
		appendSingleCodeLine( () -> {
			appendTypeName( field.valueType.getValue() );
			appendSpace();
			appendString( field.name.getValue() );
			appendAssignmentOperator();
			appendExpression( field.initializer.getValue() );
		});
	}

	// ** Statements **

	protected final void appendStatement( Statement stmt ) {
		boolean isDisabled = !stmt.isEnabled.getValue();
		if( isDisabled ) {
			pushStatementDisabled();
		}
		try {
			stmt.appendCode( this );
		} finally {
			if( isDisabled ) {
				popStatementDisabled();
			}
		}
	}

	public abstract void appendLocalDeclaration( LocalDeclarationStatement stmt );

	void appendExpressionStatement( ExpressionStatement stmt ) {
		appendSingleStatement( stmt, () -> appendExpression( stmt.expression.getValue() ) );
	}

	void appendReturnStatement( ReturnStatement stmt ) {
		appendSingleStatement( stmt, () -> {
			appendString( "return " );
			appendExpression( stmt.expression.getValue() );
		});
	}

	void appendBlock( BlockStatement blockStatement ) {
		openBlock();
		appendBody( blockStatement );
		closeBlock();
	}

	void appendConstructorBlock( ConstructorBlockStatement constructor ) {
		openBlock();
		appendStatement( constructor.constructorInvocationStatement.getValue() );
		appendBody( constructor );
		closeBlock();
	}

	private void appendBody( BlockStatement block ) {
		for( Statement statement : block.statements ) {
			appendStatement( statement );
		}
	}

	void appendSuperConstructor( SuperConstructorInvocationStatement supCon ) {
		appendSingleStatement( supCon, () -> {
			appendSuperReference();
			appendArguments( supCon );
		} );
	}

	void appendThisConstructor( ThisConstructorInvocationStatement thisCon ) {
		appendSingleStatement( thisCon, () -> {
			appendThisReference();
			appendArguments( thisCon );
		} );
	}

	void appendArguments( ArgumentOwner argumentOwner ) {
		openParen();
		String prefix = "";
		for( SimpleArgument argument : argumentOwner.getRequiredArgumentsProperty() ) {
			appendString( prefix );
			appendArgument( argument );
			prefix = getListSeparator();
		}
		for( SimpleArgument argument : argumentOwner.getVariableArgumentsProperty() ) {
			appendString( prefix );
			appendArgument( argument );
			prefix = getListSeparator();
		}
		for( JavaKeyedArgument argument : argumentOwner.getKeyedArgumentsProperty() ) {
			appendString( prefix );
			appendArgument( argument );
			prefix = getListSeparator();
		}
		closeParen();
	}

	private void appendArgument( SimpleArgument arg )
	{
		pushAndAppendArgument( arg );
	}

	protected void appendArgument( JavaKeyedArgument arg )
	{
		pushAndAppendArgument( arg );
	}

	private void pushAndAppendArgument( AbstractArgument argument ) {
		AbstractParameter parameter = argument.parameter.getValue();
		AbstractType<?, ?, ?> type = argument.getExpressionTypeForParameterType( parameter.getValueType() );
		pushTypeForLambda( type );
		try {
			appendArgument( parameter, argument );
		} finally {
			assert popTypeForLambda() == type;
		}
	}

	protected abstract void appendArgument( AbstractParameter parameter, AbstractArgument argument );

	protected abstract void appendKeyedArgument( JavaKeyedArgument arg );

	protected void appendSingleStatement( Statement stmt, Runnable appender ) {
		appender.run();
		appendStatementCompletion( stmt );
	}

	protected void appendSingleCodeLine( Runnable appender ) {
		appender.run();
		appendStatementCompletion();
	}

	protected void appendStatementCompletion( Statement stmt ) {
		appendChar( ';' );
	}

	protected void appendStatementCompletion() {
		appendChar( ';' );
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

	protected void appendCodeFlowStatement( Statement stmt, Runnable appender ) {
		appender.run();
	}

	void appendConditional( ConditionalStatement stmt ) {
		appendCodeFlowStatement(stmt, () -> {
			String text = "if";
			for (BooleanExpressionBodyPair booleanExpressionBodyPair : stmt.booleanExpressionBodyPairs) {
				appendString( text );
				appendString( "(" );
				appendExpression( booleanExpressionBodyPair.expression.getValue() );
				closeParen();
				appendStatement( booleanExpressionBodyPair.body.getValue() );
				text = " else if";
			}
			appendString( " else" );
			appendStatement( stmt.elseBody.getValue() );
		});
	}

	abstract public void appendCountLoop( CountLoop loop );

	void appendForEach( AbstractForEachLoop loop ) {
		appendCodeFlowStatement(loop, () -> {
			UserLocal itemValue = loop.item.getValue();
			final Expression items = loop.getArrayOrIterableProperty().getValue();
			appendForEachToken();
			appendEachItemsClause( itemValue, items );
			appendStatement( loop.body.getValue() );
		});
	}

	protected abstract void appendForEachToken();

	protected void appendEachItemsClause( UserLocal itemValue, Expression items ) {
		openParen();
		appendTypeName( itemValue.getValueType() );
		appendSpace();
		appendString( itemValue.getValidName() );
		appendInEachToken();
		appendExpression( items );
		closeParen();
	}

	protected abstract void appendInEachToken();

	void appendWhileLoop( WhileLoop loop ) {
		appendCodeFlowStatement(loop, () -> {
			appendString( "while (" );
			appendExpression( loop.conditional.getValue() );
			closeParen();
			appendStatement( loop.body.getValue() );
		});
	}

	public abstract void appendDoInOrder( DoInOrder doInOrder );

	public abstract void appendDoTogether( DoTogether doTogether );

	public abstract void appendEachInTogether( AbstractEachInTogether eachInTogether );

	private AbstractType<?, ?, ?> peekTypeForLambda() {
		return typeForLambdaStack.peek();
	}

	private void pushTypeForLambda( AbstractType<?, ?, ?> type ) {
		typeForLambdaStack.push( type );
	}

	private AbstractType<?, ?, ?> popTypeForLambda() {
		return typeForLambdaStack.pop();
	}

	public void appendLambda( UserLambda lambda ) {
		AbstractType<?, ?, ?> type = peekTypeForLambda();
		AbstractMethod singleAbstractMethod = AstUtilities.getSingleAbstractMethod( type );
		if( isLambdaSupported() ) {
			appendParameters( lambda );
			appendString( "->" );
		} else {
			appendString( "new " );
			appendTypeName( type );
			appendString( "()" );
			openBlock();
			appendMethodHeader( singleAbstractMethod );
		}
		appendStatement( lambda.body.getValue() );
		if (!isLambdaSupported()) {
			closeBlock();
		}
	}

	boolean isLambdaSupported() {
		return true;
	}

	// ** Expressions **

	protected void appendExpression( Expression expression ) {
		expression.appendCode( this );
	}

	void appendMethodCall( MethodInvocation invocation ) {
		appendTargetAndMethodName( invocation.expression.getValue(), invocation.method.getValue() );
		appendArguments( invocation);
	}

	protected void appendTargetAndMethodName( Expression target, AbstractMethod method ) {
		appendExpression( target );
		appendChar( '.' );
		appendString( method.getName() );
	}

	protected abstract void appendResourceExpression( ResourceExpression resourceExpression );

	void appendAssignmentExpression( AssignmentExpression assignment ) {
		appendPrecedented(assignment, () -> {
			appendExpression( assignment.leftHandSide.getValue() );
			final AssignmentExpression.Operator assignmentOp = assignment.operator.getValue();
			if (AssignmentExpression.Operator.ASSIGN.equals( assignmentOp )) {
				appendAssignmentOperator();
			} else {
				// Only basic assignment is used in existing Alice code.
				Logger.errln("Use of unexpected assignent operator " + assignmentOp + " in " + assignment );
				assignmentOp.appendCode( this );
			}
			appendExpression( assignment.rightHandSide.getValue() );
		});
	}

	void appendConcatenation( StringConcatenation concat ) {
		appendPrecedented(concat, () -> {
			appendExpression( concat.leftOperand.getValue() );
			appendChar( '+' );
			appendExpression( concat.rightOperand.getValue() );
		});
	}

	void appendLogicalComplement( LogicalComplement complement ) {
		appendPrecedented(complement, () -> {
			appendChar( '!' );
			appendExpression( complement.operand.getValue() );
		});
	}

	void appendInfixExpression( InfixExpression infixExpression ) {
		appendPrecedented(infixExpression, () -> {
			appendExpression( infixExpression.leftOperand.getValue() );
			infixExpression.getOperatorValue().appendCode( this );
			appendExpression( infixExpression.rightOperand.getValue() );
		});
	}

	void appendInstantiation( InstanceCreation creation ) {
		appendPrecedented(creation, () -> {
			appendString( "new " );
			AbstractType<?, ?, ?> type = creation.getType();
			if (null == type) {
				type = ((UserField) creation.getParent()).valueType.getValue();
			}
			appendTypeName( type );
			appendArguments( creation );
		});
	}

	void appendArrayInstantiation( ArrayInstanceCreation creation ) {
		appendPrecedented(creation, () -> {
			appendString( "new " );
			appendTypeName( creation.arrayType.getValue().getComponentType() );

			//todo: lengths
			appendChar( '[' );
			appendChar( ']' );

			appendChar( '{' );
			String prefix = "";
			for( Expression expression : creation.expressions ) {
				appendString( prefix );
				appendExpression( expression );
				prefix = ", ";
			}
			appendChar( '}' );
		});
	}

	void appendArrayAccess( ArrayAccess access ) {
		// Array access has the highest level of precedence, 16, so it will never need parentheses
		pushPrecedented(access, () -> {
			appendExpression( access.array.getValue() );
			appendChar( '[' );
			appendExpression( access.index.getValue() );
			appendChar( ']' );
		});
	}

	void appendArrayLength( ArrayLength arrayLength ) {
		appendExpression( arrayLength.array.getValue() );
		appendString( ".length" );
	}

	void appendFieldAccess( FieldAccess access ) {
		// Field access has the highest level of precedence, 16, so it will never need parentheses
		pushPrecedented(access, () -> {
			appendExpression( access.expression.getValue() );
			appendChar( '.' );
			appendString( access.field.getValue().getName() );
		});
	}

	private void appendPrecedented( PrecedentedAppender expr, Runnable appender ) {
		boolean shouldParenthesize = areParenthesesNeeded( expr );
		if (shouldParenthesize) {
			openParen();
		}

		pushPrecedented( expr, appender );

		if (shouldParenthesize) {
			closeParen();
		}
	}

	private void pushPrecedented( PrecedentedAppender expr, Runnable appender ) {
		operatorStack.push( expr );

		appender.run();

		PrecedentedAppender popped = operatorStack.pop();
		if (popped != expr) {
			Logger.errln( "Unexpected expression on stack. These two should have been the same:", expr, popped );
		}
	}

	private boolean areParenthesesNeeded( PrecedentedAppender expr ) {
		return !operatorStack.empty() &&
						expr.getLevelOfPrecedence() < operatorStack.peek().getLevelOfPrecedence();
	}

	// ** Comments **

	public void formatMultiLineComment( String comment ) {
		for( String line : splitIntoLines(comment) ) {
			appendSingleLineComment( line );
		}
	}

	protected void appendSingleLineComment( String line ) {
		appendString( "// " );
		appendString( line );
		appendNewLine();
	}

	static String[] splitIntoLines( String src ) {
		return src.split( "\n" );
	}

	public abstract String getLocalizedComment( AbstractType<?, ?, ?> type, String itemName, Locale locale );

	// ** Primitives and syntax **

	void appendNull() {
		appendString( "null" );
	}

	void appendThisReference() {
		appendString( "this" );
	}

	void appendSuperReference() {
		appendString( "super" );
	}

	void appendBoolean( boolean b ) {
		codeStringBuilder.append( b );
	}

	void appendInt( int n ) {
		if( n == Integer.MAX_VALUE ) {
			appendString( "Integer.MAX_VALUE" );
		} else if( n == Integer.MIN_VALUE ) {
			appendString( "Integer.MIN_VALUE" );
		} else {
			getCodeStringBuilder().append( n );
		}
	}

	void appendFloat( float f ) {
		if( Float.isNaN( f ) ) {
			appendString( "Float.NaN" );
		} else if( f == Float.POSITIVE_INFINITY ) {
			appendString( "Float.POSITIVE_INFINITY" );
		} else if( f == Float.NEGATIVE_INFINITY ) {
			appendString( "Float.NEGATIVE_INFINITY" );
		} else {
			getCodeStringBuilder().append( f );
			appendChar( 'f' );
		}
	}

	void appendDouble( double d ) {
		if( Double.isNaN( d ) ) {
			appendString( "Double.NaN" );
		} else if( d == Double.POSITIVE_INFINITY ) {
			appendString( "Double.POSITIVE_INFINITY" );
		} else if( d == Double.NEGATIVE_INFINITY ) {
			appendString( "Double.NEGATIVE_INFINITY" );
		} else {
			getCodeStringBuilder().append( d );
		}
	}

	void appendEscapedStringLiteral( StringLiteral literal ) {
		appendEscapedString( literal.value.getValue() );
	}

	protected void appendEscapedString( String value ) {
		appendChar( '"' );
		String escaped = StringEscapeUtils.escapeJava( value );
		appendString( escaped );
		appendChar( '"' );
	}

	void appendChar( char c ) {
		codeStringBuilder.append( c );
	}

	protected void appendSpace() {
		appendChar( ' ' );
	}

	protected void appendString( String s ) {
		codeStringBuilder.append( s );
	}

	protected void appendNewLine() {
		appendChar( '\n' );
	}

	private void openParen() {
		appendChar( '(' );
	}

	private void closeParen() {
		appendChar( ')' );
	}

	protected void openBlock() {
		appendChar( '{' );
	}

	protected void closeBlock() {
		appendChar( '}' );
	}

	protected abstract void appendAssignmentOperator();

	protected abstract void appendTypeName( AbstractType<?, ?, ?> type );

	void appendTypeLiteral( TypeLiteral typeLiteral ) {
		appendTypeName( typeLiteral.value.getValue() );
		appendString( ".class" );
	}

	// TODO move in use by NamedUserType and push down to JavaCodeGenerator
	boolean isPublicStaticFinalFieldGetterDesired() {
		return true;
	}

	private final Stack<PrecedentedAppender> operatorStack = new Stack<>();
	private final StringBuilder codeStringBuilder = new StringBuilder();
	private final Stack<AbstractType<?, ?, ?>> typeForLambdaStack = new Stack<>();
	private int statementDisabledCount = 0;
	private final Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitions;
	private final CodeOrganizer.CodeOrganizerDefinition defaultCodeOrganizerDefn;
}
