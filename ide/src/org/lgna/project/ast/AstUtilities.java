/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
public class AstUtilities {
	private AstUtilities() {
		throw new AssertionError();
	}
	
	public static <N extends org.lgna.project.ast.AbstractNode> N createCopy( N original, org.lgna.project.ast.NamedUserType root ) {
		java.util.Set< org.lgna.project.ast.AbstractDeclaration > abstractDeclarations = root.createDeclarationSet();
		original.removeDeclarationsThatNeedToBeCopied( abstractDeclarations );
		java.util.Map< Integer, org.lgna.project.ast.AbstractDeclaration > map = org.lgna.project.ast.AbstractNode.createMapOfDeclarationsThatShouldNotBeCopied( abstractDeclarations );
		org.w3c.dom.Document xmlDocument = original.encode( abstractDeclarations );
		try {
			org.lgna.project.ast.AbstractNode dst = org.lgna.project.ast.AbstractNode.decode( xmlDocument, org.lgna.project.Version.getCurrentVersionText(), map, false );
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "check copy", dst );
			return (N)dst;
		} catch( org.lgna.project.VersionNotSupportedException vnse ) {
			throw new AssertionError( vnse );
		}
	}
	
	public static boolean isKeywordExpression( Expression expression ) {
		if( expression != null ) {
//			if( expression instanceof MethodInvocation ) {
//			MethodInvocation methodInvocation = (MethodInvocation)expression;
//			if( methodInvocation.method.getValue().isStatic() ) {
				return expression.getParent() instanceof JavaKeyedArgument;
//			}
//		}
		} else {
			return false;
		}
	}

	private static boolean isValidMethod( java.lang.reflect.Method mthd, AbstractType< ?,?,? > valueType ) {
		int modifiers = mthd.getModifiers();
		if( java.lang.reflect.Modifier.isPublic( modifiers ) && java.lang.reflect.Modifier.isStatic( modifiers )  ) {
			return valueType.isAssignableFrom( mthd.getReturnType() ); 
		} else {
			return false;
		}
	}
	public static Iterable< JavaMethod > getKeyMethods( AbstractParameter parameter ) {
		java.util.List< JavaMethod > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		AbstractType< ?,?,? > valueType = parameter.getValueType().getComponentType();
		AbstractType< ?,?,? > keywordFactoryType = valueType.getKeywordFactoryType();
		if( keywordFactoryType != null ) {
			Class<?> cls = ((JavaType)keywordFactoryType).getClassReflectionProxy().getReification();
			for( java.lang.reflect.Method mthd : cls.getMethods() ) {
				if( isValidMethod( mthd, valueType ) ) {
					JavaMethod keyMethod = JavaMethod.getInstance( mthd );
					rv.add( keyMethod );
				}
			}
		}
		return rv;
	}
	public static Iterable< JavaMethod > getKeyMethods( ArgumentListProperty< JavaKeyedArgument > argumentListProperty ) {
		return getKeyMethods( argumentListProperty.getOwner().getParameterOwnerProperty().getValue().getKeyedParameter() );
	}
	public static boolean isKeyedArgumentListPropertyComplete( ArgumentListProperty< JavaKeyedArgument > argumentListProperty ) {
		for( JavaMethod method : getKeyMethods( argumentListProperty ) ) {
			boolean isFound = false;
			for( JavaKeyedArgument argument : argumentListProperty ) {
				if( argument.getKeyMethod() == method ) {
					isFound = true;
					break;
				}
			}
			if( isFound ) {
				//pass
			} else {
				return false;
			}
		}
		return true;
	}
	
	private static java.util.List< JavaMethod > updatePersistentPropertyGetters( java.util.List< JavaMethod > rv, JavaType javaType ) {
		for( JavaMethod method : javaType.getDeclaredMethods() ) { 
			java.lang.reflect.Method mthd = method.getMethodReflectionProxy().getReification();
			if( mthd != null ) {
				if( mthd.isAnnotationPresent( org.lgna.project.annotations.GetterTemplate.class ) ) {
					org.lgna.project.annotations.GetterTemplate gttrTemplate = mthd.getAnnotation( org.lgna.project.annotations.GetterTemplate.class );
					if( gttrTemplate.isPersistent() ) {
						rv.add( method );
					}
				}
			}
		}
		return rv;
	}
	public static Iterable< JavaMethod > getDeclaredPersistentPropertyGetters( JavaType javaType ) {
		java.util.List< JavaMethod > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		updatePersistentPropertyGetters( rv, javaType );
		return rv;
	}
	public static Iterable< JavaMethod > getPersistentPropertyGetters( AbstractType< ?,?,? > type ) {
		java.util.List< JavaMethod > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		JavaType javaType = type.getFirstEncounteredJavaType();
		while( true ) {
			updatePersistentPropertyGetters( rv, javaType );
			if( javaType.isFollowToSuperClassDesired() ) {
				//pass
			} else {
				break;
			}
			javaType = javaType.getSuperType();
		}
		return rv;
	}
	public static JavaMethod getSetterForGetter( JavaMethod getter, JavaType type ) {
		java.lang.reflect.Method gttr = getter.getMethodReflectionProxy().getReification();
		java.lang.reflect.Method sttr = edu.cmu.cs.dennisc.property.PropertyUtilities.getSetterForGetter( gttr, type.getClassReflectionProxy().getReification() );
		if( sttr != null ) {
			return JavaMethod.getInstance( sttr );
		} else {
			return null;
		}
	}
	public static JavaMethod getSetterForGetter( JavaMethod getter ) {
		return getSetterForGetter( getter, getter.getDeclaringType() );
	}	
	
	public static UserMethod createMethod( String name, AbstractType<?,?,?> returnType ) {
		return new UserMethod( name, returnType, new UserParameter[] {}, new BlockStatement() );
	}
	public static UserMethod createFunction( String name, AbstractType<?,?,?> returnType ) {
		return createMethod( name, returnType );
	}
	public static UserMethod createFunction( String name, Class<?> returnCls ) {
		return createMethod( name, JavaType.getInstance( returnCls ) );
	}
	public static UserMethod createProcedure( String name ) {
		return createMethod( name, JavaType.VOID_TYPE );
	}
	
	public static NamedUserType createType( String name, AbstractType<?,?,?> superType ) {
		NamedUserConstructor constructor = new NamedUserConstructor();
		constructor.body.setValue( new ConstructorBlockStatement() );
		NamedUserType rv = new NamedUserType();
		rv.name.setValue( name );
		rv.superType.setValue( superType );
		rv.constructors.add( constructor );
		return rv;
	}
	public static DoInOrder createDoInOrder() {
		return new DoInOrder( new BlockStatement() );
	}
	public static DoTogether createDoTogether() {
		return new DoTogether( new BlockStatement() );
	}
	public static Comment createComment() {
		return new Comment();
	}
	
	public static LocalDeclarationStatement createLocalDeclarationStatement( UserLocal local, Expression initializerExpression ) {
		return new LocalDeclarationStatement(
				local,
				initializerExpression 
		);
	}
	public static CountLoop createCountLoop( Expression count ) {
		return new CountLoop(
				new UserLocal( null, JavaType.INTEGER_OBJECT_TYPE, false ),
				new UserLocal( null, JavaType.INTEGER_OBJECT_TYPE, true ),
				count, 
				new BlockStatement() 
		);
	}
	public static WhileLoop createWhileLoop( Expression conditional ) {
		return new WhileLoop(
				conditional, 
				new BlockStatement() 
		);
	}
	public static ConditionalStatement createConditionalStatement( Expression conditional ) {
		return new ConditionalStatement(
				new BooleanExpressionBodyPair[] {
						new BooleanExpressionBodyPair( 
								conditional, 
								new BlockStatement()
						)
				}, 
				new BlockStatement() 
		);
	}
	public static ForEachInArrayLoop createForEachInArrayLoop( Expression arrayExpression ) {
		UserLocal item = new UserLocal( null, arrayExpression.getType().getComponentType(), true );
		return new ForEachInArrayLoop(
				item,
				arrayExpression, 
				new BlockStatement() 
		);
	}

	public static EachInArrayTogether createEachInArrayTogether( Expression arrayExpression ) {
		UserLocal item = new UserLocal( null, arrayExpression.getType().getComponentType(), true );
		return new EachInArrayTogether(
				item,
				arrayExpression, 
				new BlockStatement() 
		);
	}
	public static MethodInvocation createStaticMethodInvocation( AbstractMethod method, Expression... argumentExpressions ) {
		return AstUtilities.createMethodInvocation( new TypeExpression( method.getDeclaringType() ), method, argumentExpressions );
	}
	public static FieldAccess createFieldAccess( Expression expression, AbstractField field ) {
		FieldAccess rv = new FieldAccess();
		rv.expression.setValue( expression );
		rv.field.setValue( field );
		return rv;
	}
	public static FieldAccess createStaticFieldAccess( AbstractField field ) {
		assert field.isStatic();
		return createFieldAccess( new TypeExpression( field.getDeclaringType() ), field );
	}
	public static FieldAccess createStaticFieldAccess( Class<?> cls, String fieldName ) {
		java.lang.reflect.Field fld = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getDeclaredField( cls, fieldName );
		JavaField field = JavaField.getInstance( fld );
		return createStaticFieldAccess( field );
	}
	
	public static MethodInvocation createNextMethodInvocation( MethodInvocation prevMethodInvocation, Expression expression, AbstractMethod nextMethod ) {
		MethodInvocation rv = new MethodInvocation();
		rv.expression.setValue( prevMethodInvocation.expression.getValue() );
		rv.method.setValue( nextMethod );
		java.util.ArrayList< ? extends AbstractParameter > parameters = nextMethod.getRequiredParameters();
		final int N = parameters.size();
		for( int i=0; i<N-1; i++ ) {
			AbstractArgument argument = prevMethodInvocation.requiredArguments.get( i );
			if( argument instanceof SimpleArgument ) {
				rv.requiredArguments.add( new SimpleArgument( parameters.get( i ), ((SimpleArgument)argument).expression.getValue() ) );
			} else {
				throw new RuntimeException();
			}
		}
		rv.requiredArguments.add( new SimpleArgument( parameters.get( N-1 ), expression ) );
		return rv;
	}

	public static MethodInvocation completeMethodInvocation( MethodInvocation rv, Expression instanceExpression, Expression... argumentExpressions ) {
		rv.expression.setValue( instanceExpression );
		int i = 0;
		for( AbstractArgument argument : rv.requiredArguments ) {
			if( argument instanceof SimpleArgument ) {
				((SimpleArgument)argument).expression.setValue( argumentExpressions[ i ] );
			} else {
				throw new RuntimeException();
			}
			i ++;
		}
		return rv;
	}
	
	public static MethodInvocation createMethodInvocation( Expression instanceExpression, AbstractMethod method, Expression... argumentExpressions ) {
		MethodInvocation rv = new MethodInvocation();
		rv.expression.setValue( instanceExpression );
		rv.method.setValue( method );
		int i = 0;
		for( AbstractParameter parameter : method.getRequiredParameters() ) {
			SimpleArgument argument = new SimpleArgument( parameter, argumentExpressions[ i ] );
			rv.requiredArguments.add( argument );
			i++;
		}
		return rv;
	}
	public static ExpressionStatement createMethodInvocationStatement( Expression instanceExpression, AbstractMethod method, Expression... argumentExpressions ) {
		return new ExpressionStatement( createMethodInvocation( instanceExpression, method, argumentExpressions ) );
	}
	public static TypeExpression createTypeExpression( AbstractType<?,?,?> type ) {
		return new TypeExpression( type );
	}
	public static TypeExpression createTypeExpression( Class<?> cls ) {
		return createTypeExpression( JavaType.getInstance( cls ) );
	}
	
	public static InstanceCreation createInstanceCreation( AbstractConstructor constructor, Expression... argumentExpressions ) {
		InstanceCreation rv = new InstanceCreation( constructor );
		int i = 0;
		for( AbstractParameter parameter : constructor.getRequiredParameters() ) {
			SimpleArgument argument = new SimpleArgument( parameter, argumentExpressions[ i ] );
			rv.requiredArguments.add( argument );
			i++;
		}
		return rv;
	}
	public static InstanceCreation createInstanceCreation( AbstractType<?,?,?> type ) {
		return createInstanceCreation( type.getDeclaredConstructor() );
	}
	public static InstanceCreation createInstanceCreation( Class<?> cls, Class<?>[] parameterClses, Expression... argumentExpressions ) {
		return createInstanceCreation( JavaConstructor.getInstance( cls, parameterClses ), argumentExpressions );
	}
	public static InstanceCreation createInstanceCreation( Class<?> cls ) {
		return createInstanceCreation( JavaType.getInstance( cls ) );
	}
	
	
	public static ArrayInstanceCreation createArrayInstanceCreation( AbstractType<?,?,?> arrayType, Expression... expressions ) {
		Integer[] lengths = { expressions.length };
		return new ArrayInstanceCreation( arrayType, lengths, expressions );
	}
	public static ArrayInstanceCreation createArrayInstanceCreation( Class<?> arrayCls, Expression... expressions ) {
		return createArrayInstanceCreation( JavaType.getInstance( arrayCls ), expressions );
	}
	public static ArrayInstanceCreation createArrayInstanceCreation( AbstractType<?,?,?> arrayType, java.util.Collection< Expression > expressions ) {
		return createArrayInstanceCreation( arrayType, edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( expressions, Expression.class ) );
	}
	public static ArrayInstanceCreation createArrayInstanceCreation( Class<?> arrayCls, java.util.Collection< Expression > expressions ) {
		return createArrayInstanceCreation( JavaType.getInstance( arrayCls ), edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( expressions, Expression.class ) );
	}

	public static JavaMethod lookupMethod( Class<?> cls, String methodName, Class<?>... parameterTypes ) {
		return JavaMethod.getInstance( cls, methodName, parameterTypes );
	}

	
	public static ReturnStatement createReturnStatement( AbstractType<?,?,?> type, Expression expression ) {
		return new ReturnStatement( type, expression );
	}
	public static ReturnStatement createReturnStatement( Class<?> cls, Expression expression ) {
		return createReturnStatement( JavaType.getInstance( cls ), expression );
	}
	
	public static Expression createLocalAssignment( UserLocal local, Expression valueExpression ) {
		assert local.isFinal.getValue() == false;
		Expression localAccess = new LocalAccess( local ); 
		return new AssignmentExpression( local.valueType.getValue(), localAccess, AssignmentExpression.Operator.ASSIGN, valueExpression ); 
	}
	public static ExpressionStatement createLocalAssignmentStatement( UserLocal local, Expression valueExpression ) {
		return new ExpressionStatement( createLocalAssignment( local, valueExpression) );
	}

	public static ExpressionStatement createLocalArrayAssignmentStatement( UserLocal local, Expression indexExpression, Expression valueExpression ) {
		Expression localAccess = new LocalAccess( local ); 
		ArrayAccess arrayAccess = new ArrayAccess( local.valueType.getValue(), localAccess, indexExpression ); 
		Expression expression = new AssignmentExpression( local.valueType.getValue().getComponentType(), arrayAccess, AssignmentExpression.Operator.ASSIGN, valueExpression ); 
		return new ExpressionStatement( expression );
	}
	public static ExpressionStatement createParameterArrayAssignmentStatement( UserParameter parameter, Expression indexExpression, Expression valueExpression ) {
		Expression parameterAccess = new ParameterAccess( parameter ); 
		ArrayAccess arrayAccess = new ArrayAccess( parameter.valueType.getValue(), parameterAccess, indexExpression ); 
		Expression expression = new AssignmentExpression( parameter.valueType.getValue().getComponentType(), arrayAccess, AssignmentExpression.Operator.ASSIGN, valueExpression ); 
		return new ExpressionStatement( expression );
	}

	
	public static StringConcatenation createStringConcatenation( Expression left, Expression right ) {
		return new StringConcatenation( left, right );
	}

//	public static AbstractParameter getNextParameter( MethodInvocation methodInvocation ) {
//		AbstractMethod method = methodInvocation.method.getValue();
//		final AbstractMethod nextLongerMethod = (AbstractMethod)method.getNextLongerInChain();
//		
//		java.util.ArrayList< ? extends AbstractParameter > parameters = nextLongerMethod.getParameters();
//		return parameters.get( parameters.size()-1 );
//	}
	
	public static java.util.Map< SimpleArgumentListProperty, SimpleArgument > removeParameter( java.util.Map< SimpleArgumentListProperty, SimpleArgument > rv,  NodeListProperty< UserParameter > parametersProperty, UserParameter userParameter, int index, java.util.List< SimpleArgumentListProperty > argumentListProperties ) {
		assert rv != null;
		assert parametersProperty.get( index ) == userParameter;
		rv.clear();
		parametersProperty.remove( index );
		for( SimpleArgumentListProperty argumentListProperty : argumentListProperties ) {
			SimpleArgument argument = argumentListProperty.remove( index );
			if( argument != null ) {
				rv.put( argumentListProperty, argument );
			}
		}
		return rv;
	}
	public static void addParameter( java.util.Map< SimpleArgumentListProperty, SimpleArgument > map, NodeListProperty< UserParameter > parametersProperty, UserParameter userParameter, int index, java.util.List< SimpleArgumentListProperty > argumentListProperties ) {
		parametersProperty.add( index, userParameter );
		for( SimpleArgumentListProperty argumentListProperty : argumentListProperties ) {
			SimpleArgument argument = map.get( argumentListProperty );
			if( argument != null ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "argument == null" );
				argument = new SimpleArgument( userParameter, new NullLiteral() );
			}
			argumentListProperty.add( index, argument );
		}
	}
	
	public static AbstractType<?,?,?>[] getParameterValueTypes( AbstractMethod method ) {
		java.util.ArrayList< ? extends AbstractParameter > parameters = method.getRequiredParameters();
		AbstractType<?,?,?>[] rv = new AbstractType[ parameters.size() ];
		int i = 0;
		for( AbstractParameter parameter : parameters ) {
			rv[ i ] = parameter.getValueType();
			i++;
		}
		return rv;
	}
	
	public static UserLambda createUserLambda( AbstractType< ?,?,? > type ) {
		java.util.ArrayList< ? extends AbstractMethod > methods = type.getDeclaredMethods();
		assert methods.size() == 1;
		AbstractMethod singleAbstractMethod = methods.get( 0 );
		assert singleAbstractMethod.isAbstract() : singleAbstractMethod;
		java.util.ArrayList< ? extends AbstractParameter > srcRequiredParameters = singleAbstractMethod.getRequiredParameters();
		UserParameter[] dstRequiredParameters = new UserParameter[ srcRequiredParameters.size() ];
		for( int i=0; i<dstRequiredParameters.length; i++ ) {
			AbstractParameter srcRequiredParameter = srcRequiredParameters.get( i );
			String name = srcRequiredParameter.getName();
			if( name != null && name.length() > 0 ) {
				//pass
			} else {
				name = "p" + i;
			}
			dstRequiredParameters[ i ] = new UserParameter( name, srcRequiredParameter.getValueType() );
		}
		UserLambda rv = new UserLambda( 
				singleAbstractMethod.getReturnType(), 
				dstRequiredParameters,
				new BlockStatement()
		);
		rv.isSignatureLocked.setValue( true );
		return rv;
	}

	public static UserLambda createUserLambda( Class<?> cls ) {
		return createUserLambda( JavaType.getInstance( cls ) );
	}
	public static LambdaExpression createLambdaExpression( AbstractType< ?,?,? > type ) {
		return new LambdaExpression(
				createUserLambda( type )
		);
	}
	public static LambdaExpression createLambdaExpression( Class<?> cls ) {
		return createLambdaExpression( JavaType.getInstance( cls ) );
	}
}
