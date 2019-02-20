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

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Sets;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import edu.cmu.cs.dennisc.property.PropertyUtilities;
import org.alice.serialization.xml.XmlEncoderDecoder;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.annotations.AddEventListenerTemplate;
import org.lgna.project.annotations.GetterTemplate;
import org.lgna.project.code.CodeAppender;
import org.w3c.dom.Document;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class AstUtilities {
	private AstUtilities() {
		throw new AssertionError();
	}

	public static <N extends AbstractNode & CodeAppender> N createCopy( N original, NamedUserType root ) {
		Set<AbstractDeclaration> abstractDeclarations;
		if( root != null ) {
			abstractDeclarations = root.createDeclarationSet();
			original.removeDeclarationsThatNeedToBeCopied( abstractDeclarations );
		} else {
			abstractDeclarations = Collections.emptySet();
		}
		XmlEncoderDecoder coder = new XmlEncoderDecoder();
		Document xmlDocument = coder.encode( original , abstractDeclarations );
		try {
			AbstractNode dst = coder.copy( xmlDocument, abstractDeclarations );
			Logger.todo( "check copy", dst );
			return (N)dst;
		} catch( VersionNotSupportedException vnse ) {
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

	public static Expression getJavaKeyedArgumentSubArgument0Expression( JavaKeyedArgument argument ) {
		Expression expresssion = argument.expression.getValue();
		if( expresssion instanceof MethodInvocation ) {
			MethodInvocation methodInvocation = (MethodInvocation)expresssion;
			if( methodInvocation.requiredArguments.size() > 0 ) {
				return methodInvocation.requiredArguments.get( 0 ).expression.getValue();
			} else {
				throw new RuntimeException();
			}
		} else {
			throw new RuntimeException();
		}
	}

	private static boolean isValidMethod( java.lang.reflect.Method mthd, AbstractType<?, ?, ?> valueType ) {
		int modifiers = mthd.getModifiers();
		if( Modifier.isPublic( modifiers ) && Modifier.isStatic( modifiers ) ) {
			return valueType.isAssignableFrom( mthd.getReturnType() );
		} else {
			return false;
		}
	}

	public static Iterable<JavaMethod> getKeyMethods( AbstractParameter parameter ) {
		List<JavaMethod> rv = Lists.newLinkedList();
		AbstractType<?, ?, ?> valueType = parameter.getValueType().getComponentType();
		AbstractType<?, ?, ?> keywordFactoryType = valueType.getKeywordFactoryType();
		if( keywordFactoryType != null ) {
			Class<?> cls = ( (JavaType)keywordFactoryType ).getClassReflectionProxy().getReification();
			for( java.lang.reflect.Method mthd : cls.getMethods() ) {
				if( isValidMethod( mthd, valueType ) ) {
					JavaMethod keyMethod = JavaMethod.getInstance( mthd );
					rv.add( keyMethod );
				}
			}
		}
		return rv;
	}

	public static Iterable<JavaMethod> getKeyMethods( ArgumentListProperty<JavaKeyedArgument> argumentListProperty ) {
		return getKeyMethods( argumentListProperty.getOwner().getParameterOwnerProperty().getValue().getKeyedParameter() );
	}

	public static boolean isKeyedArgumentListPropertyComplete( ArgumentListProperty<JavaKeyedArgument> argumentListProperty ) {
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

	private static List<JavaMethod> updatePersistentPropertyGetters( List<JavaMethod> rv, JavaType javaType ) {
		for( JavaMethod method : javaType.getDeclaredMethods() ) {
			java.lang.reflect.Method mthd = method.getMethodReflectionProxy().getReification();
			if( mthd != null ) {
				if( mthd.isAnnotationPresent( GetterTemplate.class ) ) {
					GetterTemplate gttrTemplate = mthd.getAnnotation( GetterTemplate.class );
					if( gttrTemplate.isPersistent() ) {
						rv.add( method );
					}
				}
			}
		}
		return rv;
	}

	public static Iterable<JavaMethod> getDeclaredPersistentPropertyGetters( JavaType javaType ) {
		List<JavaMethod> rv = Lists.newLinkedList();
		updatePersistentPropertyGetters( rv, javaType );
		return rv;
	}

	public static Iterable<JavaMethod> getPersistentPropertyGetters( AbstractType<?, ?, ?> type ) {
		List<JavaMethod> rv = Lists.newLinkedList();
		JavaType javaType = type.getFirstEncounteredJavaType();
		while( true ) {
			if( javaType != null ) {
				updatePersistentPropertyGetters( rv, javaType );
				if( javaType.isFollowToSuperClassDesired() ) {
					//pass
				} else {
					break;
				}
				javaType = javaType.getSuperType();
			} else {
				Logger.severe( type );
				break;
			}
		}
		return rv;
	}

	public static JavaMethod getSetterForGetter( JavaMethod getter, JavaType type ) {
		java.lang.reflect.Method gttr = getter.getMethodReflectionProxy().getReification();
		java.lang.reflect.Method sttr = PropertyUtilities.getSetterForGetter( gttr, type.getClassReflectionProxy().getReification() );
		if( sttr != null ) {
			return JavaMethod.getInstance( sttr );
		} else {
			return null;
		}
	}

	public static JavaMethod getSetterForGetter( JavaMethod getter ) {
		return getSetterForGetter( getter, getter.getDeclaringType() );
	}

	public static UserMethod createMethod( String name, AbstractType<?, ?, ?> returnType ) {
		return new UserMethod( name, returnType, new UserParameter[] {}, new BlockStatement() );
	}

	public static UserMethod createFunction( String name, AbstractType<?, ?, ?> returnType ) {
		return createMethod( name, returnType );
	}

	public static UserMethod createFunction( String name, Class<?> returnCls ) {
		return createMethod( name, JavaType.getInstance( returnCls ) );
	}

	public static UserMethod createProcedure( String name ) {
		return createMethod( name, JavaType.VOID_TYPE );
	}

	public static NamedUserType createType( String name, AbstractType<?, ?, ?> superType ) {
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
		return new LocalDeclarationStatement( local, initializerExpression );
	}

	public static CountLoop createCountLoop( Expression count ) {
		return new CountLoop( new UserLocal( null, JavaType.INTEGER_OBJECT_TYPE, false ), new UserLocal( null, JavaType.INTEGER_OBJECT_TYPE, true ), count, new BlockStatement() );
	}

	public static WhileLoop createWhileLoop( Expression conditional ) {
		return new WhileLoop( conditional, new BlockStatement() );
	}

	public static ConditionalStatement createConditionalStatement( Expression conditional ) {
		return new ConditionalStatement( new BooleanExpressionBodyPair[] { new BooleanExpressionBodyPair( conditional, new BlockStatement() )
		}, new BlockStatement() );
	}

	public static ForEachInArrayLoop createForEachInArrayLoop( Expression arrayExpression ) {
		UserLocal item = new UserLocal( null, arrayExpression.getType().getComponentType(), true );
		return new ForEachInArrayLoop( item, arrayExpression, new BlockStatement() );
	}

	public static EachInArrayTogether createEachInArrayTogether( Expression arrayExpression ) {
		UserLocal item = new UserLocal( null, arrayExpression.getType().getComponentType(), true );
		return new EachInArrayTogether( item, arrayExpression, new BlockStatement() );
	}

	public static MethodInvocation createStaticMethodInvocation( AbstractMethod method, Expression... argumentExpressions ) {
		return AstUtilities.createMethodInvocation( new TypeExpression( method.getDeclaringType() ), method, argumentExpressions );
	}

	public static FieldAccess createStaticFieldAccess( AbstractField field ) {
		assert field.isStatic();
		return new FieldAccess(new TypeExpression(field.getDeclaringType() ), field);
	}

	public static FieldAccess createStaticFieldAccess( Field fld ) {
		return createStaticFieldAccess( JavaField.getInstance( fld ) );
	}

	public static FieldAccess createStaticFieldAccess( Class<?> cls, String fieldName ) {
		return createStaticFieldAccess( ReflectionUtilities.getDeclaredField( cls, fieldName ) );
	}

	public static MethodInvocation createNextMethodInvocation( MethodInvocation prevMethodInvocation, Expression expression, AbstractMethod nextMethod ) {
		MethodInvocation rv = new MethodInvocation();
		rv.expression.setValue( prevMethodInvocation.expression.getValue() );
		rv.method.setValue( nextMethod );
		List<? extends AbstractParameter> parameters = nextMethod.getRequiredParameters();
		final int N = parameters.size();
		for( int i = 0; i < ( N - 1 ); i++ ) {
			AbstractArgument argument = prevMethodInvocation.requiredArguments.get( i );
			if( argument instanceof SimpleArgument ) {
				rv.requiredArguments.add( new SimpleArgument( parameters.get( i ), ( (SimpleArgument)argument ).expression.getValue() ) );
			} else {
				throw new RuntimeException();
			}
		}
		rv.requiredArguments.add( new SimpleArgument( parameters.get( N - 1 ), expression ) );
		return rv;
	}

	public static MethodInvocation completeMethodInvocation( MethodInvocation rv, Expression instanceExpression, Expression... argumentExpressions ) {
		rv.expression.setValue( instanceExpression );
		int i = 0;
		for( AbstractArgument argument : rv.requiredArguments ) {
			if( argument instanceof SimpleArgument ) {
				( (SimpleArgument)argument ).expression.setValue( argumentExpressions[ i ] );
			} else {
				throw new RuntimeException();
			}
			i++;
		}
		return rv;
	}

	public static MethodInvocation createMethodInvocation( Expression instanceExpression, AbstractMethod method, Expression... argumentExpressions ) {
		List<? extends AbstractParameter> requiredParameters = method.getRequiredParameters();
		assert requiredParameters.size() == argumentExpressions.length : method;

		MethodInvocation rv = new MethodInvocation();
		rv.expression.setValue( instanceExpression );
		rv.method.setValue( method );
		int i = 0;
		for( AbstractParameter parameter : requiredParameters ) {
			SimpleArgument argument = new SimpleArgument( parameter, argumentExpressions[ i ] );
			rv.requiredArguments.add( argument );
			i++;
		}
		return rv;
	}

	public static ExpressionStatement createMethodInvocationStatement( Expression instanceExpression, AbstractMethod method, Expression... argumentExpressions ) {
		return new ExpressionStatement( createMethodInvocation( instanceExpression, method, argumentExpressions ) );
	}

	public static TypeExpression createTypeExpression( AbstractType<?, ?, ?> type ) {
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

	public static InstanceCreation createInstanceCreation( AbstractType<?, ?, ?> type ) {
		return createInstanceCreation( type.getDeclaredConstructor() );
	}

	public static InstanceCreation createInstanceCreation( Class<?> cls, Class<?>[] parameterClses, Expression... argumentExpressions ) {
		return createInstanceCreation( JavaConstructor.getInstance( cls, parameterClses ), argumentExpressions );
	}

	public static InstanceCreation createInstanceCreation( Class<?> cls ) {
		return createInstanceCreation( JavaType.getInstance( cls ) );
	}

	public static ArrayInstanceCreation createArrayInstanceCreation( AbstractType<?, ?, ?> arrayType, Expression... expressions ) {
		Integer[] lengths = { expressions.length };
		return new ArrayInstanceCreation( arrayType, lengths, expressions );
	}

	public static ArrayInstanceCreation createArrayInstanceCreation( Class<?> arrayCls, Expression... expressions ) {
		return createArrayInstanceCreation( JavaType.getInstance( arrayCls ), expressions );
	}

	public static ArrayInstanceCreation createArrayInstanceCreation( AbstractType<?, ?, ?> arrayType, Collection<Expression> expressions ) {
		return createArrayInstanceCreation( arrayType, ArrayUtilities.createArray( expressions, Expression.class ) );
	}

	public static ArrayInstanceCreation createArrayInstanceCreation( Class<?> arrayCls, Collection<Expression> expressions ) {
		return createArrayInstanceCreation( JavaType.getInstance( arrayCls ), ArrayUtilities.createArray( expressions, Expression.class ) );
	}

	public static JavaMethod lookupMethod( Class<?> cls, String methodName, Class<?>... parameterTypes ) {
		return JavaMethod.getInstance( cls, methodName, parameterTypes );
	}

	public static ReturnStatement createReturnStatement( AbstractType<?, ?, ?> type, Expression expression ) {
		return new ReturnStatement( type, expression );
	}

	public static ReturnStatement createReturnStatement( Class<?> cls, Expression expression ) {
		return createReturnStatement( JavaType.getInstance( cls ), expression );
	}

	public static AssignmentExpression createFieldAssignment( Expression expression, UserField field, Expression valueExpression ) {
		assert field.isFinal() == false : field;
		Expression fieldAccess = new FieldAccess( expression, field );
		return new AssignmentExpression( field.valueType.getValue(), fieldAccess, AssignmentExpression.Operator.ASSIGN, valueExpression );
	}

	public static ExpressionStatement createFieldAssignmentStatement( Expression expression, UserField field, Expression valueExpression ) {
		return new ExpressionStatement( createFieldAssignment( expression, field, valueExpression ) );
	}

	public static AssignmentExpression createFieldAssignment( UserField field, Expression valueExpression ) {
		return createFieldAssignment( new ThisExpression(), field, valueExpression );
	}

	public static ExpressionStatement createFieldAssignmentStatement( UserField field, Expression valueExpression ) {
		return new ExpressionStatement( createFieldAssignment( field, valueExpression ) );
	}

	public static AssignmentExpression createFieldArrayAssignment( Expression expression, UserField field, Expression indexExpression, Expression valueExpression ) {
		Expression fieldAccess = new FieldAccess( expression, field );
		ArrayAccess arrayAccess = new ArrayAccess( field.valueType.getValue(), fieldAccess, indexExpression );
		return new AssignmentExpression( field.valueType.getValue().getComponentType(), arrayAccess, AssignmentExpression.Operator.ASSIGN, valueExpression );
	}

	public static ExpressionStatement createFieldArrayAssignmentStatement( Expression expression, UserField field, Expression indexExpression, Expression valueExpression ) {
		return new ExpressionStatement( createFieldArrayAssignment( expression, field, indexExpression, valueExpression ) );
	}

	public static AssignmentExpression createFieldArrayAssignment( UserField field, Expression indexExpression, Expression valueExpression ) {
		return createFieldArrayAssignment( new ThisExpression(), field, indexExpression, valueExpression );
	}

	public static ExpressionStatement createFieldArrayAssignmentStatement( UserField field, Expression indexExpression, Expression valueExpression ) {
		return new ExpressionStatement( createFieldArrayAssignment( field, indexExpression, valueExpression ) );
	}

	public static AssignmentExpression createLocalAssignment( UserLocal local, Expression valueExpression ) {
		assert local.isFinal.getValue() == false : local;
		Expression localAccess = new LocalAccess( local );
		return new AssignmentExpression( local.valueType.getValue(), localAccess, AssignmentExpression.Operator.ASSIGN, valueExpression );
	}

	public static ExpressionStatement createLocalAssignmentStatement( UserLocal local, Expression valueExpression ) {
		return new ExpressionStatement( createLocalAssignment( local, valueExpression ) );
	}

	public static AssignmentExpression createLocalArrayAssignment( UserLocal local, Expression indexExpression, Expression valueExpression ) {
		Expression localAccess = new LocalAccess( local );
		ArrayAccess arrayAccess = new ArrayAccess( local.valueType.getValue(), localAccess, indexExpression );
		return new AssignmentExpression( local.valueType.getValue().getComponentType(), arrayAccess, AssignmentExpression.Operator.ASSIGN, valueExpression );
	}

	public static ExpressionStatement createLocalArrayAssignmentStatement( UserLocal local, Expression indexExpression, Expression valueExpression ) {
		return new ExpressionStatement( createLocalArrayAssignment( local, indexExpression, valueExpression ) );
	}

	public static AssignmentExpression createParameterArrayAssignment( UserParameter parameter, Expression indexExpression, Expression valueExpression ) {
		Expression parameterAccess = new ParameterAccess( parameter );
		ArrayAccess arrayAccess = new ArrayAccess( parameter.valueType.getValue(), parameterAccess, indexExpression );
		return new AssignmentExpression( parameter.valueType.getValue().getComponentType(), arrayAccess, AssignmentExpression.Operator.ASSIGN, valueExpression );
	}

	public static ExpressionStatement createParameterArrayAssignmentStatement( UserParameter parameter, Expression indexExpression, Expression valueExpression ) {
		return new ExpressionStatement( createParameterArrayAssignment( parameter, indexExpression, valueExpression ) );
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

	public static Map<SimpleArgumentListProperty, SimpleArgument> removeParameter( Map<SimpleArgumentListProperty, SimpleArgument> rv, NodeListProperty<UserParameter> parametersProperty, UserParameter userParameter, int index, List<SimpleArgumentListProperty> argumentListProperties ) {
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

	public static void addParameter( Map<SimpleArgumentListProperty, SimpleArgument> map, NodeListProperty<UserParameter> parametersProperty, UserParameter userParameter, int index, List<SimpleArgumentListProperty> argumentListProperties ) {
		parametersProperty.add( index, userParameter );
		for( SimpleArgumentListProperty argumentListProperty : argumentListProperties ) {
			SimpleArgument argument = map.get( argumentListProperty );
			if( argument != null ) {
				//pass
			} else {
				Logger.todo( "argument == null" );
				argument = new SimpleArgument( userParameter, new NullLiteral() );
			}
			argumentListProperty.add( index, argument );
		}
	}

	public static AbstractType<?, ?, ?>[] getParameterValueTypes( AbstractMethod method ) {
		List<? extends AbstractParameter> parameters = method.getRequiredParameters();
		AbstractType<?, ?, ?>[] rv = new AbstractType[ parameters.size() ];
		int i = 0;
		for( AbstractParameter parameter : parameters ) {
			rv[ i ] = parameter.getValueType();
			i++;
		}
		return rv;
	}

	public static <M extends AbstractMethod> M getSingleAbstractMethod( AbstractType<?, M, ?> type ) {
		List<M> methods = type.getDeclaredMethods();
		assert methods.size() == 1 : type;
		M singleAbstractMethod = methods.get( 0 );
		assert singleAbstractMethod.isAbstract() : singleAbstractMethod;
		return singleAbstractMethod;
	}

	public static UserLambda createUserLambda( AbstractType<?, ?, ?> type ) {
		AbstractMethod singleAbstractMethod = getSingleAbstractMethod( type );
		List<? extends AbstractParameter> srcRequiredParameters = singleAbstractMethod.getRequiredParameters();
		UserParameter[] dstRequiredParameters = new UserParameter[ srcRequiredParameters.size() ];
		for( int i = 0; i < dstRequiredParameters.length; i++ ) {
			AbstractParameter srcRequiredParameter = srcRequiredParameters.get( i );
			String name = srcRequiredParameter.getName();
			if( ( name != null ) && ( name.length() > 0 ) ) {
				//pass
			} else {
				name = "p" + i;
			}
			dstRequiredParameters[ i ] = new UserParameter( name, srcRequiredParameter.getValueType() );
		}
		UserLambda rv = new UserLambda( singleAbstractMethod.getReturnType(), dstRequiredParameters, new BlockStatement() );
		rv.isSignatureLocked.setValue( true );
		return rv;
	}

	public static UserLambda createUserLambda( Class<?> cls ) {
		return createUserLambda( JavaType.getInstance( cls ) );
	}

	public static LambdaExpression createLambdaExpression( AbstractType<?, ?, ?> type ) {
		return new LambdaExpression( createUserLambda( type ) );
	}

	public static LambdaExpression createLambdaExpression( Class<?> cls ) {
		return createLambdaExpression( JavaType.getInstance( cls ) );
	}

	public static boolean isAddEventListenerMethodInvocationStatement( Statement statement ) {
		if( statement instanceof ExpressionStatement ) {
			ExpressionStatement expressionStatement = (ExpressionStatement)statement;
			Expression expression = expressionStatement.expression.getValue();
			if( expression instanceof MethodInvocation ) {
				MethodInvocation methodInvocation = (MethodInvocation)expression;
				AbstractMethod method = methodInvocation.method.getValue();
				if( method instanceof JavaMethod ) {
					JavaMethod javaMethod = (JavaMethod)method;
					return javaMethod.isAnnotationPresent( AddEventListenerTemplate.class );
				}
			}
		}
		return false;
	}

	public static AbstractType<?, ?, ?> getKeywordFactoryType( JavaKeyedArgument argument ) {
		AbstractParameter parameter = argument.parameter.getValue();
		if( parameter.isKeyworded() ) {
			AbstractType<?, ?, ?> parameterType = parameter.getValueType();
			if( ( parameterType != null ) && parameterType.isArray() ) {
				AbstractType<?, ?, ?> componentType = parameterType.getComponentType();
				if( componentType != null ) {
					return componentType.getKeywordFactoryType();
				}
			}
		}
		return null;
	}

	private static AbstractType<?, ?, ?>[] getParameterTypes( AbstractMethod method ) {
		AbstractParameter[] parameters = method.getAllParameters();
		AbstractType<?, ?, ?>[] rv = new AbstractType<?, ?, ?>[ parameters.length ];
		for( int i = 0; i < parameters.length; i++ ) {
			rv[ i ] = parameters[ i ].getValueType();
		}
		return rv;
	}

	private static AbstractMethod getOverridenMethod( AbstractType<?, ?, ?> type, String methodName, AbstractType<?, ?, ?>[] parameterTypes ) {
		if( type != null ) {
			AbstractMethod rv = type.getDeclaredMethod( methodName, parameterTypes );
			if( rv != null ) {
				return rv;
			} else {
				//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( type, methodName, java.util.Arrays.toString( parameterTypes ) );
				return getOverridenMethod( type.getSuperType(), methodName, parameterTypes );
			}
		} else {
			return null;
		}
	}

	public static AbstractMethod getOverridenMethod( AbstractMethod method ) {
		AbstractType<?, ?, ?> type = method.getDeclaringType();
		return getOverridenMethod( type.getSuperType(), method.getName(), getParameterTypes( method ) );
	}

	private static void addInvokedMethods( Set<UserMethod> set, UserMethod from ) {
		IsInstanceCrawler<MethodInvocation> crawler = new IsInstanceCrawler<MethodInvocation>( MethodInvocation.class ) {
			@Override
			protected boolean isAcceptable( MethodInvocation methodInvocation ) {
				return true;
			}
		};
		from.body.getValue().crawl( crawler, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY );
		for( MethodInvocation methodInvocation : crawler.getList() ) {
			AbstractMethod m = methodInvocation.method.getValue();
			if( m instanceof UserMethod ) {
				UserMethod userMethod = (UserMethod)m;
				if( set.contains( userMethod ) ) {
					//pass
				} else {
					set.add( userMethod );
					addInvokedMethods( set, userMethod );
				}
			}
		}
	}

	public static Set<UserMethod> getAllInvokedMethods( UserMethod seed ) {
		Set<UserMethod> set = Sets.newHashSet();
		addInvokedMethods( set, seed );
		return set;
	}

	public static void fixRequiredArgumentsIfNecessary( MethodInvocation methodInvocation ) {
		AbstractMethod method = methodInvocation.method.getValue();
		List<? extends AbstractParameter> requiredParameters = method.getRequiredParameters();

		assert requiredParameters.size() == methodInvocation.requiredArguments.size() : method;

		final int N = requiredParameters.size();
		for( int i = 0; i < N; i++ ) {
			SimpleArgument argumentI = methodInvocation.requiredArguments.get( i );
			AbstractParameter parameterI = requiredParameters.get( i );
			if( argumentI.parameter.getValue() == parameterI ) {
				//pass
			} else {
				methodInvocation.requiredArguments.set( i, new SimpleArgument( parameterI, argumentI.expression.getValue() ) );
			}
		}
	}

	public static Collection<NamedUserType> getNamedUserTypes( Node node ) {
		Criterion<Declaration> declarationFilter = null;
		IsInstanceCrawler<NamedUserType> crawler = IsInstanceCrawler.createInstance( NamedUserType.class );
		node.crawl( crawler, CrawlPolicy.COMPLETE, declarationFilter );
		return crawler.getList();
	}

	public static AbstractType<?, ?, ?> getDeclaringTypeIfMemberOrTypeItselfIfType( AbstractDeclaration declaration ) {
		if( declaration != null ) {
			if( declaration instanceof AbstractType ) {
				return (AbstractType<?, ?, ?>)declaration;
			} else if( declaration instanceof AbstractMember ) {
				AbstractMember member = (AbstractMember)declaration;
				return member.getDeclaringType();
			} else {
				throw new UnsupportedOperationException();
			}
		} else {
			return null;
		}
	}

	private static void updateAllMethods( List<AbstractMethod> allMethods, AbstractType<?, ?, ?> type ) {
		allMethods.addAll( type.getDeclaredMethods() );
		AbstractType<?, ?, ?> superType = type.getSuperType();
		if( superType != null ) {
			updateAllMethods( allMethods, superType );
		}
	}

	public static List<AbstractMethod> getAllMethods( AbstractType<?, ?, ?> type ) {
		List<AbstractMethod> rv = Lists.newLinkedList();
		updateAllMethods( rv, type );
		return rv;
	}
}
