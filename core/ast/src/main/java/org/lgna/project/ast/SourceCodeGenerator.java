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

import org.lgna.project.code.CodeAppender;
import org.lgna.project.code.CodeOrganizer;

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

	protected StringBuilder getCodeStringBuilder() {
		return codeStringBuilder;
	}

	protected void appendClass( CodeOrganizer codeOrganizer, NamedUserType userType ) {
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

	public abstract void appendConstructor( NamedUserConstructor constructor );

	int pushStatementDisabled() {
		return statementDisabledCount++;
	}

	int popStatementDisabled() {
		return statementDisabledCount--;
	}

	boolean isLambdaSupported() {
		return true;
	}

	boolean isPublicStaticFinalFieldGetterDesired() {
		return true;
	}

	void appendBoolean( boolean b ) {
		codeStringBuilder.append( b );
	}

	protected void appendChar( char c ) {
		codeStringBuilder.append( c );
	}

	protected void appendSpace() {
		appendChar( ' ' );
	}

	protected void appendStatementCompletion() {
		appendChar( ';' );
	}

	protected void appendString( String s ) {
		codeStringBuilder.append( s );
	}

	protected void appendExpression( Expression expression ) {
		expression.appendCode( this );
	}

	void appendAccessLevel( AccessLevel accessLevel ) {
		accessLevel.appendCode( this );
	}

	protected void appendInt( int n ) {
		if( n == Integer.MAX_VALUE ) {
			appendString( "Integer.MAX_VALUE" );
		} else if( n == Integer.MIN_VALUE ) {
			appendString( "Integer.MIN_VALUE" );
		} else {
			getCodeStringBuilder().append( n );
		}
	}

	protected void appendFloat( float f ) {
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

	protected void appendDouble( double d ) {
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

  protected abstract void appendResourceExpression( ResourceExpression resourceExpression );

  protected abstract void appendTypeName( AbstractType<?, ?, ?> type );

  protected abstract void appendTargetExpression( Expression target, AbstractMethod method );

	private AbstractType<?, ?, ?> peekTypeForLambda() {
		return typeForLambdaStack.peek();
	}

	private void pushTypeForLambda( AbstractType<?, ?, ?> type ) {
		typeForLambdaStack.push( type );
	}

	private AbstractType<?, ?, ?> popTypeForLambda() {
		return typeForLambdaStack.pop();
	}

	protected void appendParameters( Code code ) {
		appendChar( '(' );
		String prefix = "";
		int i = 0;
		for( AbstractParameter parameter : code.getRequiredParameters() ) {
			appendString( prefix );
			appendTypeName( parameter.getValueType() );
			appendSpace();
			String parameterName = parameter.getValidName();
			appendString( parameterName != null ? parameterName : "p" + i );
			prefix = ",";
			i += 1;
		}
		appendChar( ')' );
	}

	public abstract void appendMethodHeader( AbstractMethod method );

	public void appendArguments( ArgumentOwner argumentOwner ) {
		String prefix = "";
		for( SimpleArgument argument : argumentOwner.getRequiredArgumentsProperty() ) {
			appendString( prefix );
			appendArgument( argument );
			prefix = ",";
		}
		for( SimpleArgument argument : argumentOwner.getVariableArgumentsProperty() ) {
			appendString( prefix );
			appendArgument( argument );
			prefix = ",";
		}
		for( JavaKeyedArgument argument : argumentOwner.getKeyedArgumentsProperty() ) {
			appendString( prefix );
			appendArgument( argument );
			prefix = ",";
		}
	}

	private void appendArgument( AbstractArgument argument ) {
		AbstractParameter parameter = argument.parameter.getValue();
		AbstractType<?, ?, ?> type = argument.getExpressionTypeForParameterType( parameter.getValueType() );
		pushTypeForLambda( type );
		try {
			argument.appendCode( this );
		} finally {
			assert popTypeForLambda() == type;
		}
	}

	@Deprecated
  protected abstract void todo( Object o );

	public String getText() {
		return String.valueOf( getCodeStringBuilder() );
	}

  protected abstract String getTextWithImports();

	CodeOrganizer getNewCodeOrganizerForTypeName( String typeName ) {
		return new CodeOrganizer( codeOrganizerDefinitions.getOrDefault( typeName, defaultCodeOrganizerDefn ) );
	}

	public void appendBlock( BlockStatement blockStatement ) {
		openBlock();
		appendBody( blockStatement );
		closeBlock();
	}

	public void appendConstructorBlock( ConstructorBlockStatement constructor ) {
		openBlock();
		constructor.constructorInvocationStatement.getValue().appendCode( this );
		appendBody( constructor );
		closeBlock();
	}

	protected void openBlock() {
		appendChar( '{' );
	}

	protected void closeBlock() {
		appendChar( '}' );
	}

	private void appendBody( BlockStatement block ) {
		for( Statement statement : block.statements ) {
			statement.appendCode( this );
		}
	}

	public void appendExpressionStatement( ExpressionStatement expressionStatement ) {
		appendSingleStatement( () -> appendExpression( expressionStatement.expression.getValue() ) );
	}

	public void appendSuperConstructor( SuperConstructorInvocationStatement supCon ) {
		appendSingleStatement( () -> {
			appendString( "super(" );
			appendArguments( supCon );
			appendString( ")" );
		} );
	}

	protected void appendSingleStatement( Runnable appender ) {
		appender.run();
		appendStatementCompletion();
	}

	public void appendMethod( UserMethod method ) {
		appendMethodHeader( method );
		method.body.getValue().appendCode( this);
	}

	public void appendLocalDeclaration( LocalDeclarationStatement stmt ) {
		appendSingleStatement( () -> {
			UserLocal localVar = stmt.local.getValue();
			if (localVar.isFinal.getValue()) {
				appendString( "final " );
			}
			appendTypeName( localVar.getValueType() );
			appendSpace();
			appendString( localVar.getValidName() );
			appendAssignment();
			appendExpression( stmt.initializer.getValue() );
		} );
	}

	abstract protected void appendAssignment();

	public void appendConditional( ConditionalStatement stmt ) {
		String text = "if";
		for( BooleanExpressionBodyPair booleanExpressionBodyPair : stmt.booleanExpressionBodyPairs ) {
			appendString( text );
			appendString( "(" );
			appendExpression( booleanExpressionBodyPair.expression.getValue() );
			appendString( ")" );
			booleanExpressionBodyPair.body.getValue().appendCode( this );
			text = " else if";
		}
		appendString( " else" );
		stmt.elseBody.getValue().appendCode( this );
	}

	public void appendCountLoop( CountLoop loop ) {
		String variableName = loop.getVariableName();
		appendString( "for(Integer " );
		appendString( variableName );
		appendString( "=0;" );
		appendString( variableName );
		appendString( "<" );
		appendExpression( loop.count.getValue() );
		appendString( ";" );
		appendString( variableName );
		appendString( "++)" );
		loop.body.getValue().appendCode( this );
	}

	public void appendForEach( AbstractForEachLoop loop ) {
		UserLocal itemValue = loop.item.getValue();
		final Expression items = loop.getArrayOrIterableProperty().getValue();
		appendString( "for");
		appendEachItemsClause( itemValue, items );
		loop.body.getValue().appendCode( this );
	}

	protected void appendEachItemsClause( UserLocal itemValue, Expression items ) {
		appendChar( '(');
		appendTypeName( itemValue.getValueType() );
		appendSpace();
		appendString( itemValue.getValidName() );
		appendString( " : " );
		appendExpression( items );
		appendChar( ')' );
	}

	public void appendWhileLoop( WhileLoop loop ) {
		appendString( "while (" );
		appendExpression( loop.conditional.getValue() );
		appendString( ")" );
		loop.body.getValue().appendCode( this );
	}

	public void appendField( UserField field ) {
		appendSingleStatement( () -> {
		  appendTypeName( field.valueType.getValue() );
		  appendSpace();
		  appendString( field.name.getValue() );
		  appendAssignment();
		  appendExpression( field.initializer.getValue() );
	  });
	}

	public void appendReturnStatement( ReturnStatement stmt ) {
		appendSingleStatement( () -> {
			appendString( "return " );
			appendExpression( stmt.expression.getValue() );
		});
	}

	public void appendGetter( Getter getter ) {
		UserField field = getter.getField();
		appendMethodHeader( getter );
		openBlock();
		appendSingleStatement( () -> {
			appendString( "return this." );
			appendString( field.name.getValue() );
		} );
		closeBlock();
	}

	public void appendSetter( Setter setter ) {
		UserField field = setter.getField();
		appendMethodHeader( setter );
		openBlock();
		appendSingleStatement( () -> {
			appendString( "this." );
			appendString( field.name.getValue() );
			appendChar( '=' );
			appendString( field.name.getValue() );
		} );
		closeBlock();
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
		lambda.body.getValue().appendCode( this );
		if (!isLambdaSupported()) {
			closeBlock();
		}
	}

	public abstract void formatMultiLineComment( String value );

	abstract public String getLocalizedComment( AbstractType<?, ?, ?> type, String itemName, Locale locale );

	private final StringBuilder codeStringBuilder = new StringBuilder();

	private final Stack<AbstractType<?, ?, ?>> typeForLambdaStack = new Stack<>();

	private int statementDisabledCount;

	private final Map<String, CodeOrganizer.CodeOrganizerDefinition> codeOrganizerDefinitions;
	private final CodeOrganizer.CodeOrganizerDefinition defaultCodeOrganizerDefn;

	public void appendDoInOrder( DoInOrder doInOrder ) {
		doInOrder.body.getValue().appendCode( this );
	}

	public abstract void appendDoTogether( DoTogether doTogether );

	public abstract void appendEachInTogether( AbstractEachInTogether eachInTogether );
}
