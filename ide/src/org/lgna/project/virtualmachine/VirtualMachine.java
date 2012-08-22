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
package org.lgna.project.virtualmachine;

/**
 * @author Dennis Cosgrove
 */
public abstract class VirtualMachine {
	public abstract LgnaStackTraceElement[] getStackTrace( Thread thread );

	protected abstract UserInstance getThis();

	protected abstract void pushBogusFrame( UserInstance instance );

	protected abstract void pushConstructorFrame( org.lgna.project.ast.NamedUserType type, java.util.Map<org.lgna.project.ast.AbstractParameter, Object> map );

	protected abstract void setConstructorFrameUserInstance( UserInstance instance );

	protected abstract void pushMethodFrame( UserInstance instance, org.lgna.project.ast.UserMethod method, java.util.Map<org.lgna.project.ast.AbstractParameter, Object> map );

	protected abstract void pushLambdaFrame( UserInstance instance, org.lgna.project.ast.UserLambda lambda, org.lgna.project.ast.AbstractMethod singleAbstractMethod, java.util.Map<org.lgna.project.ast.AbstractParameter, Object> map );

	protected abstract void popFrame();

	protected abstract Object lookup( org.lgna.project.ast.AbstractParameter parameter );

	protected abstract void pushLocal( org.lgna.project.ast.UserLocal local, Object value );

	protected abstract Object getLocal( org.lgna.project.ast.UserLocal local );

	protected abstract void setLocal( org.lgna.project.ast.UserLocal local, Object value );

	protected abstract void popLocal( org.lgna.project.ast.UserLocal local );

	//	protected abstract Frame createCopyOfCurrentFrame();
	protected abstract Frame getFrameForThread( Thread thread );

	protected abstract void pushCurrentThread( Frame frame );

	protected abstract void popCurrentThread();

	public Object[] ENTRY_POINT_evaluate( UserInstance instance, org.lgna.project.ast.Expression[] expressions ) {
		this.pushBogusFrame( instance );
		try {
			Object[] rv = new Object[ expressions.length ];
			for( int i = 0; i < expressions.length; i++ ) {
				rv[ i ] = this.evaluate( expressions[ i ] );
			}
			return rv;
		} finally {
			this.popFrame();
		}
	}

	public void ENTRY_POINT_invoke( UserInstance instance, org.lgna.project.ast.AbstractMethod method, Object... arguments ) {
		this.invoke( instance, method, arguments );
	}

	public UserInstance ENTRY_POINT_createInstance( org.lgna.project.ast.UserType<? extends org.lgna.project.ast.NamedUserConstructor> entryPointType, Object... arguments ) {
		return this.createInstanceFromUserConstructor( entryPointType.getDeclaredConstructor(), arguments );
	}

	public UserInstance ACCEPTABLE_HACK_FOR_SCENE_EDITOR_createInstanceWithInverseMap( org.lgna.project.ast.NamedUserType entryPointType, Object... arguments ) {
		pushCurrentThread( null );
		try {
			return UserInstance.createInstanceWithInverseMap( this, entryPointType.getDeclaredConstructor(), arguments );
		} finally {
			popCurrentThread();
		}
	}

	public Object ACCEPTABLE_HACK_FOR_SCENE_EDITOR_initializeField( UserInstance instance, org.lgna.project.ast.UserField field ) {
		//		pushCurrentThread( null );
		try {
			this.pushBogusFrame( instance );
			try {
				return instance.createAndSetFieldInstance( this, field );
			} finally {
				this.popFrame();
			}
		} finally {
			//			popCurrentThread();
		}
	}

	public void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_removeField( UserInstance instance, org.lgna.project.ast.UserField field, UserInstance value ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( instance, field, value );
	}

	public void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_executeStatement( UserInstance instance, org.lgna.project.ast.Statement statement ) {
		assert ( statement instanceof org.lgna.project.ast.ReturnStatement ) == false;
		//		pushCurrentThread( null );
		try {
			this.pushBogusFrame( instance );
			try {
				try {
					this.execute( statement );
				} catch( ReturnException re ) {
					throw new AssertionError();
				}
			} finally {
				this.popFrame();
			}
		} finally {
			//			popCurrentThread();
		}
	}

	private java.util.Map<Class<?>, Class<?>> mapAnonymousClsToAdapterCls = new java.util.HashMap<Class<?>, Class<?>>();

	public void registerAnonymousAdapter( Class<?> anonymousCls, Class<?> adapterCls ) {
		this.mapAnonymousClsToAdapterCls.put( anonymousCls, adapterCls );
	}

	protected UserInstance createInstanceFromUserConstructor( org.lgna.project.ast.NamedUserConstructor constructor, Object[] arguments ) {
		return UserInstance.createInstance( this, constructor, arguments );
	}

	protected Object createInstanceFromConstructorDeclaredInJava( org.lgna.project.ast.JavaConstructor constructor, Object[] arguments ) {
		UserInstance.updateArrayWithInstancesInJavaIfNecessary( arguments );
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( constructor.getConstructorReflectionProxy().getReification(), arguments );
	}

	/* package-private */Object createInstance( org.lgna.project.ast.UserType<?> type, final UserInstance userInstance, java.lang.reflect.Constructor<?> cnstrctr, Object... arguments ) {
		Class<?> cls = cnstrctr.getDeclaringClass();
		Class<?> adapterCls = this.mapAnonymousClsToAdapterCls.get( cls );
		if( adapterCls != null ) {
			MethodContext context = new MethodContext() {
				public void invokeEntryPoint( org.lgna.project.ast.AbstractMethod method, final Object... arguments ) {
					VirtualMachine.this.ENTRY_POINT_invoke( userInstance, method, arguments );
				}
			};
			Class<?>[] parameterTypes = { MethodContext.class, org.lgna.project.ast.UserType.class, Object[].class };
			Object[] args = { context, type, arguments };
			return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( adapterCls, parameterTypes, args );
		} else {
			return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr, arguments );
		}
	}

	protected Object createInstanceFromAnonymousConstructor( org.lgna.project.ast.AnonymousUserConstructor constructor, Object[] arguments ) {
		throw new RuntimeException( "todo" );
		//		org.lgna.project.ast.AbstractType<?,?,?> type = constructor.getDeclaringType();
		//		if( type instanceof org.lgna.project.ast.AnonymousInnerTypeDeclaredInAlice ) {
		//			org.lgna.project.ast.AnonymousInnerTypeDeclaredInAlice anonymousType = (org.lgna.project.ast.AnonymousInnerTypeDeclaredInAlice)type;
		//			org.lgna.project.ast.AbstractType<?,?,?> superType = anonymousType.getSuperType();
		//			if( superType instanceof org.lgna.project.ast.TypeDeclaredInJava ) {
		//				Class< ? > anonymousCls = ((org.lgna.project.ast.TypeDeclaredInJava)superType).getClassReflectionProxy().getReification();
		//				Class< ? > adapterCls = this.mapAnonymousClsToAdapterCls.get( anonymousCls );
		//				if( adapterCls != null ) {
		//					final InstanceInAlice instance = this.getThis();
		//					Context context = new Context() {
		//						public void invokeEntryPoint( final org.lgna.project.ast.AbstractMethod method, final Object... arguments ) {
		////							new Thread() {
		////								@Override
		////								public void run() {
		//									VirtualMachine.this.ENTRY_POINT_invoke( instance, method, arguments );
		////								}
		////							}.start();
		//						}
		//					};
		//					Class< ? >[] parameterTypes = { Context.class, org.lgna.project.ast.AbstractTypeDeclaredInAlice.class, Object[].class };
		//					Object[] args = { context, anonymousType, arguments };
		//					return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( adapterCls, parameterTypes, args );
		//				} else {
		//					throw new RuntimeException();
		//				}
		//			} else {
		//				throw new RuntimeException();
		//			}
		//		} else {
		//			throw new RuntimeException();
		//		}
	}

	protected Object createInstance( org.lgna.project.ast.AbstractConstructor constructor, Object... arguments ) {
		assert constructor != null;
		if( constructor instanceof org.lgna.project.ast.NamedUserConstructor ) {
			return this.createInstanceFromUserConstructor( (org.lgna.project.ast.NamedUserConstructor)constructor, arguments );
		} else if( constructor instanceof org.lgna.project.ast.JavaConstructor ) {
			return this.createInstanceFromConstructorDeclaredInJava( (org.lgna.project.ast.JavaConstructor)constructor, arguments );
		} else if( constructor instanceof org.lgna.project.ast.AnonymousUserConstructor ) {
			return this.createInstanceFromAnonymousConstructor( (org.lgna.project.ast.AnonymousUserConstructor)constructor, arguments );
		} else {
			throw new RuntimeException();
		}
	}

	private UserArrayInstance createUserArrayInstance( org.lgna.project.ast.UserArrayType type, int[] lengths, Object[] values ) {
		return new UserArrayInstance( type, lengths, values );
	}

	private Object createJavaArrayInstance( org.lgna.project.ast.JavaType type, int[] lengths, Object[] values ) {
		Class<?> cls = type.getClassReflectionProxy().getReification();
		assert cls != null;
		Class<?> componentCls = cls.getComponentType();
		assert componentCls != null;
		Object rv = java.lang.reflect.Array.newInstance( componentCls, lengths );
		for( int i = 0; i < values.length; i++ ) {
			if( values[ i ] instanceof UserInstance ) {
				UserInstance userValue = (UserInstance)values[ i ];
				values[ i ] = userValue.getJavaInstance();
			}
			java.lang.reflect.Array.set( rv, i, values[ i ] );
		}
		return rv;
	}

	protected Object createArrayInstance( org.lgna.project.ast.AbstractType<?, ?, ?> type, int[] lengths, Object... values ) {
		assert type != null;
		if( type instanceof org.lgna.project.ast.UserArrayType ) {
			return this.createUserArrayInstance( (org.lgna.project.ast.UserArrayType)type, lengths, values );
		} else if( type instanceof org.lgna.project.ast.JavaType ) {
			return this.createJavaArrayInstance( (org.lgna.project.ast.JavaType)type, lengths, values );
		} else {
			throw new RuntimeException();
		}
	}

	private Object evaluateArgument( org.lgna.project.ast.AbstractArgument argument ) {
		assert argument != null;
		org.lgna.project.ast.Expression expression = argument.expression.getValue();
		assert expression != null;
		if( expression instanceof org.lgna.project.ast.LambdaExpression ) {
			return this.EPIC_HACK_evaluateLambdaExpression( (org.lgna.project.ast.LambdaExpression)expression, argument );
		} else {
			return this.evaluate( expression );
		}
	}

	protected Object[] evaluateArguments( org.lgna.project.ast.AbstractCode code, org.lgna.project.ast.NodeListProperty<org.lgna.project.ast.SimpleArgument> arguments,
			org.lgna.project.ast.NodeListProperty<org.lgna.project.ast.SimpleArgument> variableArguments, org.lgna.project.ast.NodeListProperty<org.lgna.project.ast.JavaKeyedArgument> keyedArguments ) {
		//todo: when variable length and keyed parameters are offered in the IDE (User) this code will need to be updated 
		java.util.ArrayList<? extends org.lgna.project.ast.AbstractParameter> requiredParameters = code.getRequiredParameters();
		org.lgna.project.ast.AbstractParameter variableParameter = code.getVariableLengthParameter();
		org.lgna.project.ast.AbstractParameter keyedParameter = code.getKeyedParameter();

		final int REQUIRED_N = arguments.size();
		assert requiredParameters.size() == REQUIRED_N : code.getName() + " " + requiredParameters.size() + " " + arguments.size();

		int length = REQUIRED_N;
		if( variableParameter != null ) {
			length += 1;
		}
		if( keyedParameter != null ) {
			length += 1;
		}
		Object[] rv = new Object[ length ];
		int rvIndex;
		for( rvIndex = 0; rvIndex < REQUIRED_N; rvIndex++ ) {
			rv[ rvIndex ] = this.evaluateArgument( arguments.get( rvIndex ) );
		}
		if( variableParameter != null ) {
			final int VARIABLE_N = variableArguments.size();
			org.lgna.project.ast.JavaType variableArrayType = variableParameter.getValueType().getFirstEncounteredJavaType();
			assert variableArrayType.isArray();
			Class<?> componentCls = variableArrayType.getComponentType().getClassReflectionProxy().getReification();
			Object array = java.lang.reflect.Array.newInstance( componentCls, VARIABLE_N );
			for( int i = 0; i < VARIABLE_N; i++ ) {
				//todo: support primitive types
				java.lang.reflect.Array.set( array, i, this.evaluateArgument( variableArguments.get( rvIndex ) ) );
			}
			rv[ rvIndex ] = array;
		}
		if( keyedParameter != null ) {
			final int KEYED_N = keyedArguments.size();
			org.lgna.project.ast.JavaType keyedArrayType = keyedParameter.getValueType().getFirstEncounteredJavaType();
			assert keyedArrayType.isArray();
			Class<?> componentCls = keyedArrayType.getComponentType().getClassReflectionProxy().getReification();
			Object array = java.lang.reflect.Array.newInstance( componentCls, KEYED_N );
			for( int i = 0; i < KEYED_N; i++ ) {
				//todo: support primitive types
				java.lang.reflect.Array.set( array, i, this.evaluateArgument( keyedArguments.get( i ) ) );
			}
			rv[ rvIndex ] = array;
			//
			//			
			//			org.lgna.project.ast.AbstractParameter paramLast = requiredParameters.get( N-1 );
			//			if( paramLast.isVariableLength() ) {
			//				Class<?> arrayCls =  paramLast.getValueType().getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getReification();
			//				assert arrayCls != null;
			//				Class<?> componentCls = arrayCls.getComponentType();
			//				assert componentCls != null;
			//				rv[ N-1 ] = java.lang.reflect.Array.newInstance( componentCls, M );
			//				for( int j=0; j<( M - (N-1) ); j++ ) {
			//					org.lgna.project.ast.AbstractArgument argumentJ = arguments.get( (N-1) + j );
			//					assert argumentJ != null;
			//					Object valueJ = this.evaluate( argumentJ );
			//					assert valueJ != null;
			//					java.lang.reflect.Array.set( rv[ N-1 ], j, valueJ );
			//				}
			//			} else {
			//				rv[ N-1 ] = this.evaluate( arguments.get( N-1 ) );
			//			}
		}
		return rv;
	}

	protected Integer getArrayLength( Object array ) {
		if( array != null ) {
			if( array instanceof UserArrayInstance ) {
				UserArrayInstance userArrayInstance = (UserArrayInstance)array;
				return userArrayInstance.getLength();
			} else {
				return java.lang.reflect.Array.getLength( array );
			}
		} else {
			throw new NullPointerException();
		}
	}

	protected Object getUserField( org.lgna.project.ast.UserField field, Object instance ) {
		assert instance != null : field.getName();
		assert instance instanceof UserInstance;
		UserInstance userInstance = (UserInstance)instance;
		return userInstance.getFieldValue( field );
	}

	protected void setUserField( org.lgna.project.ast.UserField field, Object instance, Object value ) {
		assert instance instanceof UserInstance;
		UserInstance userInstance = (UserInstance)instance;
		userInstance.setFieldValue( field, value );
	}

	protected Object getFieldDeclaredInJavaWithField( org.lgna.project.ast.JavaField field, Object instance ) {
		instance = UserInstance.getJavaInstanceIfNecessary( instance );
		java.lang.reflect.Field fld = field.getFieldReflectionProxy().getReification();
		assert fld != null : field.getFieldReflectionProxy();
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( fld, instance );
	}

	protected void setFieldDeclaredInJavaWithField( org.lgna.project.ast.JavaField field, Object instance, Object value ) {
		instance = UserInstance.getJavaInstanceIfNecessary( instance );
		java.lang.reflect.Field fld = field.getFieldReflectionProxy().getReification();
		assert fld != null : field;
		edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.set( fld, instance, value );
	}

	protected Object get( org.lgna.project.ast.AbstractField field, Object instance ) {
		assert field != null;
		assert ( instance != null ) || field.isStatic() : field;
		if( field instanceof org.lgna.project.ast.UserField ) {
			return this.getUserField( (org.lgna.project.ast.UserField)field, instance );
		} else if( field instanceof org.lgna.project.ast.JavaField ) {
			return this.getFieldDeclaredInJavaWithField( (org.lgna.project.ast.JavaField)field, instance );
		} else {
			throw new RuntimeException();
		}
	}

	protected void set( org.lgna.project.ast.AbstractField field, Object instance, Object value ) {
		assert field != null;
		if( field instanceof org.lgna.project.ast.UserField ) {
			this.setUserField( (org.lgna.project.ast.UserField)field, instance, value );
		} else if( field instanceof org.lgna.project.ast.JavaField ) {
			this.setFieldDeclaredInJavaWithField( (org.lgna.project.ast.JavaField)field, instance, value );
		} else {
			throw new RuntimeException();
		}
	}

	private void checkIndex( int index, int length ) {
		if( ( 0 <= index ) && ( index < length ) ) {
			//pass
		} else {
			throw new LgnaArrayIndexOutOfBoundsException( this, index, length );
		}
	}

	protected Object getItemAtIndex( org.lgna.project.ast.AbstractType<?, ?, ?> arrayType, Object array, Integer index ) {
		assert arrayType != null;
		assert arrayType.isArray();
		if( array instanceof UserArrayInstance ) {
			UserArrayInstance userArrayInstance = (UserArrayInstance)array;
			this.checkIndex( index, userArrayInstance.getLength() );
			return userArrayInstance.get( index );
		} else {
			this.checkIndex( index, java.lang.reflect.Array.getLength( array ) );
			return java.lang.reflect.Array.get( array, index );
		}
	}

	protected void setItemAtIndex( org.lgna.project.ast.AbstractType<?, ?, ?> arrayType, Object array, Integer index, Object value ) {
		value = UserInstance.getJavaInstanceIfNecessary( value );
		assert arrayType != null;
		assert arrayType.isArray() : arrayType;
		if( array instanceof UserArrayInstance ) {
			UserArrayInstance userArrayInstance = (UserArrayInstance)array;
			this.checkIndex( index, userArrayInstance.getLength() );
			userArrayInstance.set( index, value );
		} else {
			this.checkIndex( index, java.lang.reflect.Array.getLength( array ) );
			java.lang.reflect.Array.set( array, index, value );
		}
	}

	protected Object invokeUserMethod( Object instance, org.lgna.project.ast.UserMethod method, Object... arguments ) {
		if( method.isStatic() ) {
			assert instance == null;
		} else {
			assert instance != null : method;
			assert instance instanceof UserInstance : instance;
		}
		UserInstance userInstance = (UserInstance)instance;
		java.util.Map<org.lgna.project.ast.AbstractParameter, Object> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		for( int i = 0; i < arguments.length; i++ ) {
			map.put( method.requiredParameters.get( i ), arguments[ i ] );
		}
		this.pushMethodFrame( userInstance, method, map );
		try {
			this.execute( method.body.getValue() );
			if( method.isProcedure() ) {
				return null;
			} else {
				throw new LgnaNoReturnException( this );
			}
		} catch( ReturnException re ) {
			return re.getValue();
		} finally {
			this.popFrame();
		}
	}

	protected Object invokeMethodDeclaredInJava( Object instance, org.lgna.project.ast.JavaMethod method, Object... arguments ) {
		instance = UserInstance.getJavaInstanceIfNecessary( instance );
		UserInstance.updateArrayWithInstancesInJavaIfNecessary( arguments );
		java.lang.reflect.Method mthd = method.getMethodReflectionProxy().getReification();

		Class<?>[] parameterTypes = mthd.getParameterTypes();
		int lastParameterIndex = parameterTypes.length - 1;
		if( lastParameterIndex == arguments.length ) {
			if( mthd.isVarArgs() ) {
				Object[] fixedArguments = new Object[ parameterTypes.length ];
				System.arraycopy( arguments, 0, fixedArguments, 0, arguments.length );
				assert parameterTypes[ lastParameterIndex ].isArray() : parameterTypes[ lastParameterIndex ];
				fixedArguments[ lastParameterIndex ] = java.lang.reflect.Array.newInstance( parameterTypes[ lastParameterIndex ].getComponentType(), 0 );
				arguments = fixedArguments;
			}
		}

		if( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.isProtected( mthd ) ) {
			Class<?> adapterCls = mapAnonymousClsToAdapterCls.get( mthd.getDeclaringClass() );
			assert adapterCls != null;
			mthd = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getMethod( adapterCls, mthd.getName(), mthd.getParameterTypes() );
		}
		assert edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.isPublic( mthd );
		//		try {
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( instance, mthd, arguments );
		//		} catch( RuntimeException re ) {
		//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "warning: could not invoke ", method );
		//			for( Object argument : arguments ) {
		//				edu.cmu.cs.dennisc.print.PrintUtilities.println( argument );
		//			}
		//			return null;
		//		}
	}

	protected Object invoke( Object instance, org.lgna.project.ast.AbstractMethod method, Object... arguments ) {
		assert method != null;
		if( method instanceof org.lgna.project.ast.UserMethod ) {
			return this.invokeUserMethod( instance, (org.lgna.project.ast.UserMethod)method, arguments );
		} else if( method instanceof org.lgna.project.ast.JavaMethod ) {
			return this.invokeMethodDeclaredInJava( instance, (org.lgna.project.ast.JavaMethod)method, arguments );
		} else {
			throw new RuntimeException();
		}
	}

	protected Object evaluateAssignmentExpression( org.lgna.project.ast.AssignmentExpression assignmentExpression ) {
		org.lgna.project.ast.Expression leftHandExpression = assignmentExpression.leftHandSide.getValue();
		org.lgna.project.ast.Expression rightHandExpression = assignmentExpression.rightHandSide.getValue();
		Object rightHandValue = this.evaluate( rightHandExpression );
		if( assignmentExpression.operator.getValue() == org.lgna.project.ast.AssignmentExpression.Operator.ASSIGN ) {
			if( leftHandExpression instanceof org.lgna.project.ast.FieldAccess ) {
				org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)leftHandExpression;
				this.set( fieldAccess.field.getValue(), this.evaluate( fieldAccess.expression.getValue() ), rightHandValue );
			} else if( leftHandExpression instanceof org.lgna.project.ast.LocalAccess ) {
				org.lgna.project.ast.LocalAccess localAccess = (org.lgna.project.ast.LocalAccess)leftHandExpression;
				this.setLocal( localAccess.local.getValue(), rightHandValue );
			} else if( leftHandExpression instanceof org.lgna.project.ast.ArrayAccess ) {
				org.lgna.project.ast.ArrayAccess arrayAccess = (org.lgna.project.ast.ArrayAccess)leftHandExpression;
				this.setItemAtIndex( arrayAccess.arrayType.getValue(), this.evaluate( arrayAccess.array.getValue() ), this.evaluate( arrayAccess.index.getValue(), Integer.class ), rightHandValue );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: evaluateActual", assignmentExpression.leftHandSide.getValue(), rightHandValue );
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: evaluateActual", assignmentExpression );
		}
		return null;
	}

	protected Object evaluateBooleanLiteral( org.lgna.project.ast.BooleanLiteral booleanLiteral ) {
		return booleanLiteral.value.getValue();
	}

	protected Object evaluateInstanceCreation( org.lgna.project.ast.InstanceCreation classInstanceCreation ) {
		//		AbstractType classType =classInstanceCreation.constructor.getValue().getDeclaringType();
		Object[] arguments = this.evaluateArguments( classInstanceCreation.constructor.getValue(), classInstanceCreation.requiredArguments, classInstanceCreation.variableArguments, classInstanceCreation.keyedArguments );
		return this.createInstance( classInstanceCreation.constructor.getValue(), arguments );
	}

	protected Object evaluateArrayInstanceCreation( org.lgna.project.ast.ArrayInstanceCreation arrayInstanceCreation ) {
		Object[] values = new Object[ arrayInstanceCreation.expressions.size() ];
		for( int i = 0; i < values.length; i++ ) {
			values[ i ] = this.evaluate( arrayInstanceCreation.expressions.get( i ) );
		}
		int[] lengths = new int[ arrayInstanceCreation.lengths.size() ];
		for( int i = 0; i < lengths.length; i++ ) {
			lengths[ i ] = arrayInstanceCreation.lengths.get( i );
		}
		return this.createArrayInstance( arrayInstanceCreation.arrayType.getValue(), lengths, values );
	}

	protected Object evaluateArrayAccess( org.lgna.project.ast.ArrayAccess arrayAccess ) {
		return this.getItemAtIndex( arrayAccess.arrayType.getValue(), this.evaluate( arrayAccess.array.getValue() ), this.evaluate( arrayAccess.index.getValue(), Integer.class ) );
	}

	protected Integer evaluateArrayLength( org.lgna.project.ast.ArrayLength arrayLength ) {
		return this.getArrayLength( this.evaluate( arrayLength.array.getValue() ) );
	}

	protected Object evaluateFieldAccess( org.lgna.project.ast.FieldAccess fieldAccess ) {
		return this.get( fieldAccess.field.getValue(), this.evaluate( fieldAccess.expression.getValue() ) );
	}

	protected Object evaluateLocalAccess( org.lgna.project.ast.LocalAccess localAccess ) {
		return this.getLocal( localAccess.local.getValue() );
	}

	protected Object evaluateArithmeticInfixExpression( org.lgna.project.ast.ArithmeticInfixExpression arithmeticInfixExpression ) {
		Number leftOperand = (Number)this.evaluate( arithmeticInfixExpression.leftOperand.getValue() );
		Number rightOperand = (Number)this.evaluate( arithmeticInfixExpression.rightOperand.getValue() );
		return arithmeticInfixExpression.operator.getValue().operate( leftOperand, rightOperand );
	}

	protected Object evaluateBitwiseInfixExpression( org.lgna.project.ast.BitwiseInfixExpression bitwiseInfixExpression ) {
		Object leftOperand = this.evaluate( bitwiseInfixExpression.leftOperand.getValue() );
		Object rightOperand = this.evaluate( bitwiseInfixExpression.rightOperand.getValue() );
		return bitwiseInfixExpression.operator.getValue().operate( leftOperand, rightOperand );
	}

	protected Boolean evaluateConditionalInfixExpression( org.lgna.project.ast.ConditionalInfixExpression conditionalInfixExpression ) {
		org.lgna.project.ast.ConditionalInfixExpression.Operator operator = conditionalInfixExpression.operator.getValue();
		Boolean leftOperand = (Boolean)this.evaluate( conditionalInfixExpression.leftOperand.getValue() );
		if( operator == org.lgna.project.ast.ConditionalInfixExpression.Operator.AND ) {
			if( leftOperand ) {
				return (Boolean)this.evaluate( conditionalInfixExpression.rightOperand.getValue() );
			} else {
				return false;
			}
		} else if( operator == org.lgna.project.ast.ConditionalInfixExpression.Operator.OR ) {
			if( leftOperand ) {
				return true;
			} else {
				return (Boolean)this.evaluate( conditionalInfixExpression.rightOperand.getValue() );
			}
		} else {

			return false;
		}
	}

	protected Boolean evaluateRelationalInfixExpression( org.lgna.project.ast.RelationalInfixExpression relationalInfixExpression ) {
		Object leftOperand = UserInstance.getJavaInstanceIfNecessary( this.evaluate( relationalInfixExpression.leftOperand.getValue() ) );
		Object rightOperand = UserInstance.getJavaInstanceIfNecessary( this.evaluate( relationalInfixExpression.rightOperand.getValue() ) );
		return relationalInfixExpression.operator.getValue().operate( leftOperand, rightOperand );
	}

	protected Object evaluateShiftInfixExpression( org.lgna.project.ast.ShiftInfixExpression shiftInfixExpression ) {
		Object leftOperand = this.evaluate( shiftInfixExpression.leftOperand.getValue() );
		Object rightOperand = this.evaluate( shiftInfixExpression.rightOperand.getValue() );
		return shiftInfixExpression.operator.getValue().operate( leftOperand, rightOperand );
	}

	protected Object evaluateLogicalComplement( org.lgna.project.ast.LogicalComplement logicalComplement ) {
		Boolean operand = this.evaluate( logicalComplement.operand.getValue(), Boolean.class );
		return !operand;
	}

	protected String evaluateStringConcatenation( org.lgna.project.ast.StringConcatenation stringConcatenation ) {
		StringBuffer sb = new StringBuffer();
		Object leftOperand = this.evaluate( stringConcatenation.leftOperand.getValue() );
		Object rightOperand = this.evaluate( stringConcatenation.rightOperand.getValue() );
		sb.append( leftOperand );
		sb.append( rightOperand );
		return sb.toString();
	}

	protected Object evaluateMethodInvocation( org.lgna.project.ast.MethodInvocation methodInvocation ) {
		if( methodInvocation.isValid() ) {
			Object[] allArguments = this.evaluateArguments( methodInvocation.method.getValue(), methodInvocation.requiredArguments, methodInvocation.variableArguments, methodInvocation.keyedArguments );
			int parameterCount = methodInvocation.method.getValue().getRequiredParameters().size();
			if( methodInvocation.method.getValue().getVariableLengthParameter() != null ) {
				parameterCount += 1;
			}
			if( methodInvocation.method.getValue().getKeyedParameter() != null ) {
				parameterCount += 1;
			}
			assert parameterCount == allArguments.length : methodInvocation.method.getValue().getName();
			return this.invoke( this.evaluate( methodInvocation.expression.getValue() ), methodInvocation.method.getValue(), allArguments );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( methodInvocation.method.getValue() );
			return null;
		}
	}

	protected Object evaluateNullLiteral( org.lgna.project.ast.NullLiteral nullLiteral ) {
		return null;
	}

	protected Object evaluateNumberLiteral( org.lgna.project.ast.NumberLiteral numberLiteral ) {
		return numberLiteral.value.getValue();
	}

	protected Object evaluateDoubleLiteral( org.lgna.project.ast.DoubleLiteral doubleLiteral ) {
		return doubleLiteral.value.getValue();
	}

	protected Object evaluateFloatLiteral( org.lgna.project.ast.FloatLiteral floatLiteral ) {
		return floatLiteral.value.getValue();
	}

	protected Object evaluateIntegerLiteral( org.lgna.project.ast.IntegerLiteral integerLiteral ) {
		return integerLiteral.value.getValue();
	}

	protected Object evaluateParameterAccess( org.lgna.project.ast.ParameterAccess parameterAccess ) {
		return this.lookup( parameterAccess.parameter.getValue() );
	}

	protected Object evaluateStringLiteral( org.lgna.project.ast.StringLiteral stringLiteral ) {
		return stringLiteral.value.getValue();
	}

	protected Object evaluateThisExpression( org.lgna.project.ast.ThisExpression thisExpression ) {
		Object rv = this.getThis();
		assert rv != null;
		return rv;
	}

	protected Object evaluateTypeExpression( org.lgna.project.ast.TypeExpression typeExpression ) {
		return typeExpression.value.getValue();
	}

	protected Object evaluateTypeLiteral( org.lgna.project.ast.TypeLiteral typeLiteral ) {
		return typeLiteral.value.getValue();
	}

	protected Object evaluateResourceExpression( org.lgna.project.ast.ResourceExpression resourceExpression ) {
		return resourceExpression.resource.getValue();
	}

	protected Object EPIC_HACK_evaluateLambdaExpression( org.lgna.project.ast.LambdaExpression lambdaExpression, org.lgna.project.ast.AbstractArgument argument ) {
		org.lgna.project.ast.Lambda lambda = lambdaExpression.value.getValue();

		org.lgna.project.ast.AbstractType<?, ?, ?> type = argument.parameter.getValue().getValueType();
		if( type instanceof org.lgna.project.ast.JavaType ) {

			UserInstance thisInstance = this.getThis();
			assert thisInstance != null;

			org.lgna.project.ast.JavaType javaType = (org.lgna.project.ast.JavaType)type;
			Class<?> interfaceCls = javaType.getClassReflectionProxy().getReification();
			Class<?> adapterCls = this.mapAnonymousClsToAdapterCls.get( interfaceCls );
			assert adapterCls != null : interfaceCls;
			Class<?>[] parameterTypes = { org.lgna.project.virtualmachine.LambdaContext.class, org.lgna.project.ast.Lambda.class, org.lgna.project.virtualmachine.UserInstance.class };
			Object[] arguments = {
					new LambdaContext() {
						public void invokeEntryPoint( org.lgna.project.ast.Lambda lambda, org.lgna.project.ast.AbstractMethod singleAbstractMethod, org.lgna.project.virtualmachine.UserInstance thisInstance, java.lang.Object... arguments ) {
							assert thisInstance != null;
							if( lambda instanceof org.lgna.project.ast.UserLambda ) {
								org.lgna.project.ast.UserLambda userLambda = (org.lgna.project.ast.UserLambda)lambda;
								java.util.Map<org.lgna.project.ast.AbstractParameter, Object> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
								for( int i = 0; i < arguments.length; i++ ) {
									map.put( userLambda.requiredParameters.get( i ), arguments[ i ] );
								}
								pushLambdaFrame( thisInstance, userLambda, singleAbstractMethod, map );
								try {
									executeBlockStatement( userLambda.body.getValue() );
								} catch( ReturnException re ) {
									edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "handle return" );
									assert false : re;
								} finally {
									popFrame();
								}
							}
						}
					},
					lambda,
					thisInstance
			};
			try {
				java.lang.reflect.Constructor<?> cnstrctr = adapterCls.getDeclaredConstructor( parameterTypes );
				return cnstrctr.newInstance( arguments );
			} catch( Exception e ) {
				throw new RuntimeException( e );
			}
		} else {
			throw new RuntimeException( "todo" );
		}
	}

	protected Object evaluateLambdaExpression( org.lgna.project.ast.LambdaExpression lambdaExpression ) {
		throw new RuntimeException( "todo" );
	}

	protected Object evaluate( org.lgna.project.ast.Expression expression ) {
		if( expression != null ) {
			if( expression instanceof org.lgna.project.ast.AssignmentExpression ) {
				return this.evaluateAssignmentExpression( (org.lgna.project.ast.AssignmentExpression)expression );
			} else if( expression instanceof org.lgna.project.ast.BooleanLiteral ) {
				return this.evaluateBooleanLiteral( (org.lgna.project.ast.BooleanLiteral)expression );
			} else if( expression instanceof org.lgna.project.ast.InstanceCreation ) {
				return this.evaluateInstanceCreation( (org.lgna.project.ast.InstanceCreation)expression );
			} else if( expression instanceof org.lgna.project.ast.ArrayInstanceCreation ) {
				return this.evaluateArrayInstanceCreation( (org.lgna.project.ast.ArrayInstanceCreation)expression );
			} else if( expression instanceof org.lgna.project.ast.ArrayLength ) {
				return this.evaluateArrayLength( (org.lgna.project.ast.ArrayLength)expression );
			} else if( expression instanceof org.lgna.project.ast.ArrayAccess ) {
				return this.evaluateArrayAccess( (org.lgna.project.ast.ArrayAccess)expression );
			} else if( expression instanceof org.lgna.project.ast.FieldAccess ) {
				return this.evaluateFieldAccess( (org.lgna.project.ast.FieldAccess)expression );
			} else if( expression instanceof org.lgna.project.ast.LocalAccess ) {
				return this.evaluateLocalAccess( (org.lgna.project.ast.LocalAccess)expression );
			} else if( expression instanceof org.lgna.project.ast.ArithmeticInfixExpression ) {
				return this.evaluateArithmeticInfixExpression( (org.lgna.project.ast.ArithmeticInfixExpression)expression );
			} else if( expression instanceof org.lgna.project.ast.BitwiseInfixExpression ) {
				return this.evaluateBitwiseInfixExpression( (org.lgna.project.ast.BitwiseInfixExpression)expression );
			} else if( expression instanceof org.lgna.project.ast.ConditionalInfixExpression ) {
				return this.evaluateConditionalInfixExpression( (org.lgna.project.ast.ConditionalInfixExpression)expression );
			} else if( expression instanceof org.lgna.project.ast.RelationalInfixExpression ) {
				return this.evaluateRelationalInfixExpression( (org.lgna.project.ast.RelationalInfixExpression)expression );
			} else if( expression instanceof org.lgna.project.ast.ShiftInfixExpression ) {
				return this.evaluateShiftInfixExpression( (org.lgna.project.ast.ShiftInfixExpression)expression );
			} else if( expression instanceof org.lgna.project.ast.LogicalComplement ) {
				return this.evaluateLogicalComplement( (org.lgna.project.ast.LogicalComplement)expression );
			} else if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
				return this.evaluateMethodInvocation( (org.lgna.project.ast.MethodInvocation)expression );
			} else if( expression instanceof org.lgna.project.ast.NullLiteral ) {
				return this.evaluateNullLiteral( (org.lgna.project.ast.NullLiteral)expression );
			} else if( expression instanceof org.lgna.project.ast.StringConcatenation ) {
				return this.evaluateStringConcatenation( (org.lgna.project.ast.StringConcatenation)expression );
			} else if( expression instanceof org.lgna.project.ast.NumberLiteral ) {
				return this.evaluateNumberLiteral( (org.lgna.project.ast.NumberLiteral)expression );
			} else if( expression instanceof org.lgna.project.ast.DoubleLiteral ) {
				return this.evaluateDoubleLiteral( (org.lgna.project.ast.DoubleLiteral)expression );
			} else if( expression instanceof org.lgna.project.ast.FloatLiteral ) {
				return this.evaluateFloatLiteral( (org.lgna.project.ast.FloatLiteral)expression );
			} else if( expression instanceof org.lgna.project.ast.IntegerLiteral ) {
				return this.evaluateIntegerLiteral( (org.lgna.project.ast.IntegerLiteral)expression );
			} else if( expression instanceof org.lgna.project.ast.ParameterAccess ) {
				return this.evaluateParameterAccess( (org.lgna.project.ast.ParameterAccess)expression );
			} else if( expression instanceof org.lgna.project.ast.StringLiteral ) {
				return this.evaluateStringLiteral( (org.lgna.project.ast.StringLiteral)expression );
			} else if( expression instanceof org.lgna.project.ast.ThisExpression ) {
				return this.evaluateThisExpression( (org.lgna.project.ast.ThisExpression)expression );
			} else if( expression instanceof org.lgna.project.ast.TypeExpression ) {
				return this.evaluateTypeExpression( (org.lgna.project.ast.TypeExpression)expression );
			} else if( expression instanceof org.lgna.project.ast.TypeLiteral ) {
				return this.evaluateTypeLiteral( (org.lgna.project.ast.TypeLiteral)expression );
			} else if( expression instanceof org.lgna.project.ast.ResourceExpression ) {
				return this.evaluateResourceExpression( (org.lgna.project.ast.ResourceExpression)expression );
			} else if( expression instanceof org.lgna.project.ast.LambdaExpression ) {
				return this.evaluateLambdaExpression( (org.lgna.project.ast.LambdaExpression)expression );
			} else {
				throw new RuntimeException( expression.getClass().getName() );
			}
		} else {
			throw new NullPointerException();
		}
	}

	protected final <E extends Object> E evaluate( org.lgna.project.ast.Expression expression, Class<E> cls ) {
		//in order to support python...
		//if( result instanceof Integer ) {
		//	condition = ((Integer)result) != 0;
		//} else {
		//	condition = (Boolean)result;
		//}
		Object value = this.evaluate( expression );
		if( cls.isArray() ) {
			if( value instanceof UserArrayInstance ) {
				UserArrayInstance userArrayInstance = (UserArrayInstance)value;
				//todo
				value = userArrayInstance.getValues();
			}
		}
		return cls.cast( value );
	}

	protected void executeAssertStatement( org.lgna.project.ast.AssertStatement assertStatement ) {
		assert this.evaluate( assertStatement.expression.getValue(), Boolean.class ) : this.evaluate( assertStatement.message.getValue() );
	}

	protected void executeBlockStatement( org.lgna.project.ast.BlockStatement blockStatement ) throws ReturnException {
		//todo?
		org.lgna.project.ast.Statement[] array = new org.lgna.project.ast.Statement[ blockStatement.statements.size() ];
		blockStatement.statements.toArray( array );
		for( org.lgna.project.ast.Statement statement : array ) {
			this.execute( statement );
		}
	}

	protected void executeConditionalStatement( org.lgna.project.ast.ConditionalStatement conditionalStatement ) throws ReturnException {
		for( org.lgna.project.ast.BooleanExpressionBodyPair booleanExpressionBodyPair : conditionalStatement.booleanExpressionBodyPairs ) {
			if( evaluate( booleanExpressionBodyPair.expression.getValue(), Boolean.class ) ) {
				this.execute( booleanExpressionBodyPair.body.getValue() );
				return;
			}
		}
		this.execute( conditionalStatement.elseBody.getValue() );
	}

	protected void executeComment( org.lgna.project.ast.Comment comment ) {
	}

	protected void executeCountLoop( org.lgna.project.ast.CountLoop countLoop ) throws ReturnException {
		org.lgna.project.ast.UserLocal variable = countLoop.variable.getValue();
		org.lgna.project.ast.UserLocal constant = countLoop.constant.getValue();
		this.pushLocal( variable, -1 );
		try {
			final int n = this.evaluate( countLoop.count.getValue(), Integer.class );
			this.pushLocal( constant, n );
			try {
				for( int i = 0; i < n; i++ ) {
					this.setLocal( variable, i );
					this.execute( countLoop.body.getValue() );
				}
			} finally {
				this.popLocal( constant );
			}
		} finally {
			this.popLocal( variable );
		}
	}

	protected void executeDoInOrder( org.lgna.project.ast.DoInOrder doInOrder ) throws ReturnException {
		executeBlockStatement( doInOrder.body.getValue() );
	}

	private static int threadCount = 0;

	protected void executeDoTogether( org.lgna.project.ast.DoTogether doTogether ) throws ReturnException {
		org.lgna.project.ast.BlockStatement blockStatement = doTogether.body.getValue();
		//todo?
		switch( blockStatement.statements.size() ) {
		case 0:
			break;
		case 1:
			execute( blockStatement.statements.get( 0 ) );
			break;
		default:
			final Frame owner = this.getFrameForThread( Thread.currentThread() );
			Runnable[] runnables = new Runnable[ blockStatement.statements.size() ];
			for( int i = 0; i < runnables.length; i++ ) {
				final org.lgna.project.ast.Statement statementI = blockStatement.statements.get( i );
				runnables[ i ] = new Runnable() {
					;
					public void run() {
						//edu.cmu.cs.dennisc.print.PrintUtilities.println( statementI );
						pushCurrentThread( owner );
						try {
							execute( statementI );
						} catch( ReturnException re ) {
							//todo
						} finally {
							popCurrentThread();
						}
					}
				};
			}
			org.lgna.common.ThreadUtilities.doTogether( runnables );
		}
	}

	protected void executeExpressionStatement( org.lgna.project.ast.ExpressionStatement expressionStatement ) {
		@SuppressWarnings( "unused" )
		Object unused = this.evaluate( expressionStatement.expression.getValue() );
	}

	private void excecuteForEachLoop( org.lgna.project.ast.AbstractForEachLoop forEachInLoop, Object[] array ) throws ReturnException {
		org.lgna.project.ast.UserLocal item = forEachInLoop.item.getValue();
		org.lgna.project.ast.BlockStatement blockStatement = forEachInLoop.body.getValue();
		this.pushLocal( item, -1 );
		try {
			for( Object o : array ) {
				this.setLocal( item, o );
				this.execute( blockStatement );
			}
		} finally {
			this.popLocal( item );
		}

	}

	protected void executeForEachInArrayLoop( org.lgna.project.ast.ForEachInArrayLoop forEachInArrayLoop ) throws ReturnException {
		Object[] array = this.evaluate( forEachInArrayLoop.array.getValue(), Object[].class );
		excecuteForEachLoop( forEachInArrayLoop, array );
	}

	protected void executeForEachInIterableLoop( org.lgna.project.ast.ForEachInIterableLoop forEachInIterableLoop ) throws ReturnException {
		Iterable<?> iterable = this.evaluate( forEachInIterableLoop.iterable.getValue(), Iterable.class );
		excecuteForEachLoop( forEachInIterableLoop, edu.cmu.cs.dennisc.java.lang.IterableUtilities.toArray( iterable ) );
	}

	private void excecuteForEachTogether( org.lgna.project.ast.AbstractEachInTogether forEachInTogether, final Object[] array ) throws ReturnException {
		final org.lgna.project.ast.UserLocal item = forEachInTogether.item.getValue();
		final org.lgna.project.ast.BlockStatement blockStatement = forEachInTogether.body.getValue();

		switch( array.length ) {
		case 0:
			break;
		case 1:
			VirtualMachine.this.pushLocal( item, array[ 0 ] );
			try {
				VirtualMachine.this.execute( blockStatement );
			} finally {
				VirtualMachine.this.popLocal( item );
			}
			break;
		default:
			final Frame owner = this.getFrameForThread( Thread.currentThread() );
			org.lgna.common.ThreadUtilities.eachInTogether( new org.lgna.common.EachInTogetherRunnable<Object>() {
				public void run( Object value ) {
					pushCurrentThread( owner );
					try {
						VirtualMachine.this.pushLocal( item, value );
						try {
							VirtualMachine.this.execute( blockStatement );
						} catch( ReturnException re ) {
							//todo
						} finally {
							VirtualMachine.this.popLocal( item );
						}
					} finally {
						popCurrentThread();
					}
				}
			}, array );
		}
	}

	protected void executeEachInArrayTogether( org.lgna.project.ast.EachInArrayTogether eachInArrayTogether ) throws ReturnException {
		Object[] array = this.evaluate( eachInArrayTogether.array.getValue(), Object[].class );
		excecuteForEachTogether( eachInArrayTogether, array );
	}

	protected void executeEachInIterableTogether( org.lgna.project.ast.EachInIterableTogether eachInIterableTogether ) throws ReturnException {
		Iterable<?> iterable = this.evaluate( eachInIterableTogether.iterable.getValue(), Iterable.class );
		excecuteForEachTogether( eachInIterableTogether, edu.cmu.cs.dennisc.java.lang.IterableUtilities.toArray( iterable ) );
	}

	protected void executeReturnStatement( org.lgna.project.ast.ReturnStatement returnStatement ) throws ReturnException {
		Object returnValue = this.evaluate( returnStatement.expression.getValue() );
		//setReturnValue( returnValue );
		throw new ReturnException( returnValue );
	}

	protected void executeWhileLoop( org.lgna.project.ast.WhileLoop whileLoop ) throws ReturnException {
		while( this.evaluate( whileLoop.conditional.getValue(), Boolean.class ) ) {
			this.execute( whileLoop.body.getValue() );
		}
	}

	protected void executeLocalDeclarationStatement( org.lgna.project.ast.LocalDeclarationStatement localDeclarationStatement ) {
		this.pushLocal( localDeclarationStatement.local.getValue(), this.evaluate( localDeclarationStatement.initializer.getValue() ) );
		//handle pop on exit of owning block statement
	}

	protected void execute( org.lgna.project.ast.Statement statement ) throws ReturnException {
		assert statement != null;
		if( statement.isEnabled.getValue() ) {
			if( statement instanceof org.lgna.project.ast.AssertStatement ) {
				this.executeAssertStatement( (org.lgna.project.ast.AssertStatement)statement );
			} else if( statement instanceof org.lgna.project.ast.BlockStatement ) {
				this.executeBlockStatement( (org.lgna.project.ast.BlockStatement)statement );
			} else if( statement instanceof org.lgna.project.ast.ConditionalStatement ) {
				this.executeConditionalStatement( (org.lgna.project.ast.ConditionalStatement)statement );
			} else if( statement instanceof org.lgna.project.ast.Comment ) {
				this.executeComment( (org.lgna.project.ast.Comment)statement );
			} else if( statement instanceof org.lgna.project.ast.CountLoop ) {
				this.executeCountLoop( (org.lgna.project.ast.CountLoop)statement );
			} else if( statement instanceof org.lgna.project.ast.DoTogether ) {
				this.executeDoTogether( (org.lgna.project.ast.DoTogether)statement );
			} else if( statement instanceof org.lgna.project.ast.DoInOrder ) {
				this.executeDoInOrder( (org.lgna.project.ast.DoInOrder)statement );
			} else if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
				this.executeExpressionStatement( (org.lgna.project.ast.ExpressionStatement)statement );
			} else if( statement instanceof org.lgna.project.ast.ForEachInArrayLoop ) {
				this.executeForEachInArrayLoop( (org.lgna.project.ast.ForEachInArrayLoop)statement );
			} else if( statement instanceof org.lgna.project.ast.ForEachInIterableLoop ) {
				this.executeForEachInIterableLoop( (org.lgna.project.ast.ForEachInIterableLoop)statement );
			} else if( statement instanceof org.lgna.project.ast.EachInArrayTogether ) {
				this.executeEachInArrayTogether( (org.lgna.project.ast.EachInArrayTogether)statement );
			} else if( statement instanceof org.lgna.project.ast.EachInIterableTogether ) {
				this.executeEachInIterableTogether( (org.lgna.project.ast.EachInIterableTogether)statement );
			} else if( statement instanceof org.lgna.project.ast.ReturnStatement ) {
				this.executeReturnStatement( (org.lgna.project.ast.ReturnStatement)statement );
			} else if( statement instanceof org.lgna.project.ast.WhileLoop ) {
				this.executeWhileLoop( (org.lgna.project.ast.WhileLoop)statement );
			} else if( statement instanceof org.lgna.project.ast.LocalDeclarationStatement ) {
				this.executeLocalDeclarationStatement( (org.lgna.project.ast.LocalDeclarationStatement)statement );
			} else {
				throw new RuntimeException();
			}
		}
	}
}
