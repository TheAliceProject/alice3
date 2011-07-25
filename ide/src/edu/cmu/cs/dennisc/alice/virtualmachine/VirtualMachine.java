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
package edu.cmu.cs.dennisc.alice.virtualmachine;

/**
 * @author Dennis Cosgrove
 */
public abstract class VirtualMachine {
	protected abstract Object getThis();

	protected abstract void pushConstructorFrame( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type, java.util.Map<edu.cmu.cs.dennisc.alice.ast.AbstractParameter,Object> map );
	protected abstract void setConstructorFrameInstanceInAlice( InstanceInAlice instance );
	protected abstract void pushMethodFrame( InstanceInAlice instance, java.util.Map<edu.cmu.cs.dennisc.alice.ast.AbstractParameter,Object> map );
	protected abstract void popFrame();

	protected abstract Object lookup( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter );

	protected abstract void pushLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local, Object value );
	protected abstract Object getLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local );
	protected abstract void setLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local, Object value );
	protected abstract void popLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local );

	protected abstract Frame createCopyOfCurrentFrame();
	protected abstract Frame getFrameForThread( Thread thread );
	
	protected abstract void pushCurrentThread( Frame frame );
	protected abstract void popCurrentThread();

	public Object[] evaluateEntryPoint( InstanceInAlice instance, edu.cmu.cs.dennisc.alice.ast.Expression[] expressions ) {
		pushCurrentThread( null );
		try {
			this.pushMethodFrame( instance, (java.util.Map)java.util.Collections.emptyMap() );
			try {
				Object[] rv = new Object[ expressions.length ];
				for( int i=0; i<expressions.length; i++ ) {
					rv[ i ] = this.evaluate( expressions[ i ] );
				}
				return rv;
			} finally {
				this.popFrame();
			}
		} finally {
			popCurrentThread();
		}
	} 
	public void invokeEntryPoint( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, Object instance, Object... arguments ) {
		pushCurrentThread( null );
		try {
			invoke( method, instance, arguments );
		} finally {
			popCurrentThread();
		}
	}
	public Object createInstanceEntryPoint( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> entryPointType, Object... arguments ) {
		pushCurrentThread( null );
		try {
			return this.createInstance( entryPointType.getDeclaredConstructor(), arguments );
		} finally {
			popCurrentThread();
		}
	}
	public InstanceInAlice ACCEPTABLE_HACK_FOR_SCENE_EDITOR_createInstanceWithInverseMapWithoutExcutingConstructorBody( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice entryPointType, Object... arguments ) {
		this.isConstructorBodyExecutionDesired = false;
		try {
			pushCurrentThread( null );
			try {
				return InstanceInAlice.createInstanceWithInverseMap( this, entryPointType.getDeclaredConstructor(), arguments );
			} finally {
				popCurrentThread();
			}
		} finally {
			this.isConstructorBodyExecutionDesired = true;
		}
	}
	
	private void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_pushFrame( InstanceInAlice instance ) {
		this.pushMethodFrame( instance, new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractParameter, Object >() );
	}
	public Object ACCEPTABLE_HACK_FOR_SCENE_EDITOR_initializeField( InstanceInAlice instance, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		pushCurrentThread( null );
		try {
			this.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_pushFrame( instance );
			try {
				return instance.createAndSetFieldInstance( this, field );
			} finally {
				this.popFrame();
			}
		} finally {
			popCurrentThread();
		}
	}
	public void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_removeField( InstanceInAlice instance, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, InstanceInAlice value ) {
	}
	public void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_executeStatement( InstanceInAlice instance, edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		assert statement instanceof edu.cmu.cs.dennisc.alice.ast.ReturnStatement == false;
		pushCurrentThread( null );
		try {
			this.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_pushFrame( instance );
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
			popCurrentThread();
		}
	}
	
		
	private java.util.Map< Class<?>, Class<?> > mapAnonymousClsToAdapterCls = new java.util.HashMap< Class<?>, Class<?> >();
	public void registerAnonymousAdapter( Class<?> anonymousCls, Class<?> adapterCls ) {
		this.mapAnonymousClsToAdapterCls.put( anonymousCls, adapterCls );
	}

	private boolean isConstructorBodyExecutionDesired = true;
	/*package-private*/ boolean isConstructorBodyExecutionDesired() {
		return this.isConstructorBodyExecutionDesired;
	}
	private Object createInstanceFromConstructorDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice constructor, Object[] arguments ) {
		return InstanceInAlice.createInstance( this, constructor, arguments );
	}
	private Object createInstanceFromConstructorDeclaredInJava( edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor, Object[] arguments ) {
		InstanceInAlice.updateArrayWithInstancesInJavaIfNecessary( arguments );
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( constructor.getConstructorReflectionProxy().getReification(), arguments );
	}
	
	
	/*package-private*/Object createInstance( edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice< ? > type, final InstanceInAlice instanceInAlice, java.lang.reflect.Constructor< ? > cnstrctr, Object... arguments ) {
		Class<?> cls = cnstrctr.getDeclaringClass();
		Class<?> adapterCls = this.mapAnonymousClsToAdapterCls.get( cls );
		if( adapterCls != null ) {
			Context context = new Context() {
				public void invokeEntryPoint( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, final Object... arguments ) {
					VirtualMachine.this.invokeEntryPoint( method, instanceInAlice, arguments );
				}
			};
			Class< ? >[] parameterTypes = { Context.class, edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice.class, Object[].class };
			Object[] args = { context, type, arguments };
			return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( adapterCls, parameterTypes, args );
		} else {
			return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cnstrctr, arguments );
		}
	}
	
	protected Object createInstanceFromAnonymousConstructor( edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor constructor, Object[] arguments ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = constructor.getDeclaringType();
		if( type instanceof edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice anonymousType = (edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice)type;
			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> superType = anonymousType.getSuperType();
			if( superType instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava ) {
				Class< ? > anonymousCls = ((edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava)superType).getClassReflectionProxy().getReification();
				Class< ? > adapterCls = this.mapAnonymousClsToAdapterCls.get( anonymousCls );
				if( adapterCls != null ) {
					final Object instance = this.getThis();
					Context context = new Context() {
						public void invokeEntryPoint( final edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, final Object... arguments ) {
//							new Thread() {
//								@Override
//								public void run() {
									VirtualMachine.this.invokeEntryPoint( method, instance, arguments );
//								}
//							}.start();
						}
					};
					Class< ? >[] parameterTypes = { Context.class, edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice.class, Object[].class };
					Object[] args = { context, anonymousType, arguments };
					return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( adapterCls, parameterTypes, args );
				} else {
					throw new RuntimeException();
				}
			} else {
				throw new RuntimeException();
			}
		} else {
			throw new RuntimeException();
		}
	}
	protected Object createInstance( edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor, Object... arguments ) {
		assert constructor != null;
		if( constructor instanceof edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice ) {
			return this.createInstanceFromConstructorDeclaredInAlice( (edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice)constructor, arguments );
		} else if( constructor instanceof edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava ) {
			return this.createInstanceFromConstructorDeclaredInJava( (edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava)constructor, arguments );
		} else if( constructor instanceof edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor ) {
			return this.createInstanceFromAnonymousConstructor( (edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor)constructor, arguments );
		} else {
			throw new RuntimeException();
		}
	}

	private Object createArrayInstanceFromTypeDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.ArrayTypeDeclaredInAlice type, int[] lengths, Object[] values ) {
		return new ArrayInstanceInAlice( type, lengths, values );
	}
	private Object createArrayInstanceFromTypeDeclaredInJava( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava type, int[] lengths, Object[] values ) {
		Class<?> cls = type.getClassReflectionProxy().getReification();
		assert cls != null;
		Class<?> componentCls = cls.getComponentType();
		assert componentCls != null;
		Object rv = java.lang.reflect.Array.newInstance( componentCls, lengths );
		for( int i=0; i<values.length; i++ ) {
			if( values[ i ] instanceof InstanceInAlice ) {
				InstanceInAlice valueInAlice = (InstanceInAlice)values[ i ];
				values[ i ] = valueInAlice.getInstanceInJava();
			}
			java.lang.reflect.Array.set( rv, i, values[ i ] );
		}
		return rv;
	}
	protected Object createArrayInstance( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type, int[] lengths, Object... values ) {
		assert type != null;
		if( type instanceof edu.cmu.cs.dennisc.alice.ast.ArrayTypeDeclaredInAlice ) {
			return this.createArrayInstanceFromTypeDeclaredInAlice( (edu.cmu.cs.dennisc.alice.ast.ArrayTypeDeclaredInAlice)type, lengths, values );
		} else if( type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava ) {
			return this.createArrayInstanceFromTypeDeclaredInJava( (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava)type, lengths, values );
		} else {
			throw new RuntimeException();
		}
	}
	
	private Object evaluate( edu.cmu.cs.dennisc.alice.ast.Argument argument ) {
		assert argument != null;
		edu.cmu.cs.dennisc.alice.ast.Expression expression = argument.expression.getValue();
		assert expression != null;
		return this.evaluate( expression );
	}
	protected Object[] evaluateArguments( java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters, edu.cmu.cs.dennisc.alice.ast.NodeListProperty< edu.cmu.cs.dennisc.alice.ast.Argument > arguments ) {
		final int N = parameters.size();
		final int M = arguments.size();
		assert N == M;
		Object[] rv = new Object[ N ];
		if( N>0 ) {
			for( int i=0; i<N-1; i++ ) {
				rv[ i ] = this.evaluate( arguments.get( i ) );
			}
			
			edu.cmu.cs.dennisc.alice.ast.AbstractParameter paramLast = parameters.get( N-1 );
			if( paramLast.isVariableLength() ) {
				Class<?> arrayCls =  paramLast.getValueType().getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getReification();
				assert arrayCls != null;
				Class<?> componentCls = arrayCls.getComponentType();
				assert componentCls != null;
				rv[ N-1 ] = java.lang.reflect.Array.newInstance( componentCls, M );
				for( int j=0; j<( M - (N-1) ); j++ ) {
					edu.cmu.cs.dennisc.alice.ast.Argument argumentJ = arguments.get( (N-1) + j );
					assert argumentJ != null;
					Object valueJ = this.evaluate( argumentJ );
					assert valueJ != null;
					java.lang.reflect.Array.set( rv[ N-1 ], j, valueJ );
				}
			} else {
				rv[ N-1 ] = this.evaluate( arguments.get( N-1 ) );
			}
		}
		return rv;
	}
	
	protected Integer getArrayLength( Object array ) {
		if( array != null ) {
			if (array instanceof ArrayInstanceInAlice) {
				ArrayInstanceInAlice arrayInstanceInAlice = (ArrayInstanceInAlice) array;
				return arrayInstanceInAlice.getLength();
			} else {
				return java.lang.reflect.Array.getLength( array );
			}
		} else {
			throw new NullPointerException();
		}
	}
	protected Object getFieldDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance ) {
		assert instance != null;
		assert instance instanceof InstanceInAlice;
		InstanceInAlice instanceInAlice = (InstanceInAlice)instance;
		return instanceInAlice.getFieldValue( field );
	}
	protected void setFieldDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance, Object value ) {
		assert instance instanceof InstanceInAlice;
		InstanceInAlice instanceInAlice = (InstanceInAlice)instance;
		instanceInAlice.setFieldValue( field, value );
	}
	protected Object getFieldDeclaredInJavaWithField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField field, Object instance ) {
		instance = InstanceInAlice.getInstanceInJavaIfNecessary( instance );
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( field.getFieldReflectionProxy().getReification(), instance );
	}
	protected void setFieldDeclaredInJavaWithField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField field, Object instance, Object value ) {
		instance = InstanceInAlice.getInstanceInJavaIfNecessary( instance );
		edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.set( field.getFieldReflectionProxy().getReification(), instance, value );
	}
	protected Object getFieldDeclaredInJavaWithGetterAndSetter( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithGetterAndSetter field, Object instance ) {
		instance = InstanceInAlice.getInstanceInJavaIfNecessary( instance );
		if( instance != null ) {
			return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( instance, field.getGetterReflectionProxy().getReification() );
		} else {
			throw new NullPointerException();
		}
	}
	protected void setFieldDeclaredInJavaWithGetterAndSetter( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithGetterAndSetter field, Object instance, Object value ) {
		instance = InstanceInAlice.getInstanceInJavaIfNecessary( instance );
		value = InstanceInAlice.getInstanceInJavaIfNecessary( value );
		if( instance != null ) {
			edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( instance, field.getSetterReflectionProxy().getReification(), value );
		} else {
			throw new NullPointerException();
		}
	}
	
	protected Object get( edu.cmu.cs.dennisc.alice.ast.AbstractField field, Object instance ) {
		assert field != null;
		if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
			return this.getFieldDeclaredInAlice( (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field, instance );
		} else if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField ) {
			return this.getFieldDeclaredInJavaWithField( (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField)field, instance );
		} else if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithGetterAndSetter ) {
			return this.getFieldDeclaredInJavaWithGetterAndSetter( (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithGetterAndSetter)field, instance );
		} else {
			throw new RuntimeException();
		}
	}
	protected void set( edu.cmu.cs.dennisc.alice.ast.AbstractField field, Object instance, Object value ) {
		assert field != null;
		if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
			this.setFieldDeclaredInAlice( (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field, instance, value );
		} else if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField ) {
			this.setFieldDeclaredInJavaWithField( (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField)field, instance, value );
		} else if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithGetterAndSetter ) {
			this.setFieldDeclaredInJavaWithGetterAndSetter( (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithGetterAndSetter)field, instance, value );
		} else {
			throw new RuntimeException();
		}
	}

	protected Object getItemAtIndex( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> arrayType, Object array, Integer index ) {
		assert arrayType != null;
		assert arrayType.isArray();
		if( array instanceof ArrayInstanceInAlice ) {
			ArrayInstanceInAlice arrayInstanceInAlice = (ArrayInstanceInAlice)array;
			return arrayInstanceInAlice.get( index );
		} else {
			return java.lang.reflect.Array.get( array, index );
		}
	}
	protected void setItemAtIndex( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> arrayType, Object array, Integer index, Object value ) {
		value = InstanceInAlice.getInstanceInJavaIfNecessary( value );
		assert arrayType != null;
		assert arrayType.isArray();
		if( array instanceof ArrayInstanceInAlice ) {
			ArrayInstanceInAlice arrayInstanceInAlice = (ArrayInstanceInAlice)array;
			arrayInstanceInAlice.set( index, value );
		} else {
			java.lang.reflect.Array.set( array, index, value );
		}
	}
	protected Object invokeMethodDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method, Object instance, Object... arguments ) {
		if( method.isStatic() ) {
			assert instance == null;
		} else {
			assert instance instanceof InstanceInAlice;
		}
		InstanceInAlice instanceInAlice = (InstanceInAlice)instance;
		java.util.Map<edu.cmu.cs.dennisc.alice.ast.AbstractParameter,Object> map = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractParameter, Object >();
		for( int i=0; i<arguments.length; i++ ) {
			map.put( method.parameters.get( i ), arguments[ i ] );
		}
		this.pushMethodFrame( instanceInAlice, map );
		try {
			this.execute( method.body.getValue() );
			if( method.isProcedure() ) {
				return null;
			} else {
				throw new RuntimeException( "no return value: " + method.getName() );
			}
		} catch( ReturnException re ) {
			return re.getValue();
		} finally {
			this.popFrame();
		}
	}
	protected Object invokeMethodDeclaredInJava( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava method, Object instance, Object... arguments ) {
		instance = InstanceInAlice.getInstanceInJavaIfNecessary( instance );
		InstanceInAlice.updateArrayWithInstancesInJavaIfNecessary( arguments );
		java.lang.reflect.Method mthd = method.getMethodReflectionProxy().getReification();
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
	
	protected Object invoke( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, Object instance, Object... arguments ) {
		assert method != null;
		if( method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			return this.invokeMethodDeclaredInAlice( (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)method, instance, arguments );
		} else if( method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava ) {
			return this.invokeMethodDeclaredInJava( (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava)method, instance, arguments );
		} else {
			throw new RuntimeException();
		}
	}
	
	protected Object evaluateAssignmentExpression( edu.cmu.cs.dennisc.alice.ast.AssignmentExpression assignmentExpression ) {
		edu.cmu.cs.dennisc.alice.ast.Expression leftHandExpression = assignmentExpression.leftHandSide.getValue();
		edu.cmu.cs.dennisc.alice.ast.Expression rightHandExpression = assignmentExpression.rightHandSide.getValue();
		Object rightHandValue = this.evaluate( rightHandExpression );
		if( assignmentExpression.operator.getValue() == edu.cmu.cs.dennisc.alice.ast.AssignmentExpression.Operator.ASSIGN ) {
			if( leftHandExpression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
				edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)leftHandExpression;
				this.set( fieldAccess.field.getValue(), this.evaluate( fieldAccess.expression.getValue() ), rightHandValue );
			} else if( leftHandExpression instanceof edu.cmu.cs.dennisc.alice.ast.VariableAccess ){
				edu.cmu.cs.dennisc.alice.ast.VariableAccess variableAccess = (edu.cmu.cs.dennisc.alice.ast.VariableAccess)leftHandExpression;
				this.setLocal( variableAccess.variable.getValue(), rightHandValue );
			} else if( leftHandExpression instanceof edu.cmu.cs.dennisc.alice.ast.ArrayAccess ){
				edu.cmu.cs.dennisc.alice.ast.ArrayAccess arrayAccess = (edu.cmu.cs.dennisc.alice.ast.ArrayAccess)leftHandExpression;
				this.setItemAtIndex( arrayAccess.arrayType.getValue(), this.evaluate( arrayAccess.array.getValue() ), this.evaluate( arrayAccess.index.getValue(), Integer.class ), rightHandValue );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: evaluateActual", assignmentExpression.leftHandSide.getValue(), rightHandValue );
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: evaluateActual", assignmentExpression );
		}
		return null;
	}
	protected Object evaluateBooleanLiteral( edu.cmu.cs.dennisc.alice.ast.BooleanLiteral booleanLiteral ) {
		return booleanLiteral.value.getValue();
	}
	protected Object evaluateInstanceCreation( edu.cmu.cs.dennisc.alice.ast.InstanceCreation classInstanceCreation ) {
//		AbstractType classType =classInstanceCreation.constructor.getValue().getDeclaringType();
		Object[] arguments = this.evaluateArguments( classInstanceCreation.constructor.getValue().getParameters(), classInstanceCreation.arguments );
		return this.createInstance( classInstanceCreation.constructor.getValue(), arguments );
	}
	protected Object evaluateArrayInstanceCreation( edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation arrayInstanceCreation ) {
		Object[] values = new Object[ arrayInstanceCreation.expressions.size() ];
		for( int i=0; i<values.length; i++ ) {
			values[ i ] = this.evaluate( arrayInstanceCreation.expressions.get( i ) );
		}
		int[] lengths = new int[ arrayInstanceCreation.lengths.size() ];
		for( int i=0; i<lengths.length; i++ ) {
			lengths[ i ] = arrayInstanceCreation.lengths.get( i );
		}
		return this.createArrayInstance( arrayInstanceCreation.arrayType.getValue(), lengths, values );
	}
	protected Object evaluateArrayAccess( edu.cmu.cs.dennisc.alice.ast.ArrayAccess arrayAccess ) {
		return this.getItemAtIndex( arrayAccess.arrayType.getValue(), this.evaluate( arrayAccess.array.getValue() ), this.evaluate( arrayAccess.index.getValue(), Integer.class ) );
	}
	protected Integer evaluateArrayLength( edu.cmu.cs.dennisc.alice.ast.ArrayLength arrayLength ) {
		return this.getArrayLength( this.evaluate( arrayLength.array.getValue() ) );
	}
	protected Object evaluateFieldAccess( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
		return this.get( fieldAccess.field.getValue(), this.evaluate( fieldAccess.expression.getValue() ) );
	}
	protected Object evaluateVariableAccess( edu.cmu.cs.dennisc.alice.ast.VariableAccess variableAccess ) {
		return this.getLocal( variableAccess.variable.getValue() );
	}
	protected Object evaluateConstantAccess( edu.cmu.cs.dennisc.alice.ast.ConstantAccess constantAccess ) {
		return this.getLocal( constantAccess.constant.getValue() );
	}
	protected Object evaluateArithmeticInfixExpression( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression arithmeticInfixExpression ) {
		Number leftOperand = (Number)this.evaluate( arithmeticInfixExpression.leftOperand.getValue() );
		Number rightOperand = (Number)this.evaluate( arithmeticInfixExpression.rightOperand.getValue() );
		return arithmeticInfixExpression.operator.getValue().operate( leftOperand, rightOperand );
	}
	protected Object evaluateBitwiseInfixExpression( edu.cmu.cs.dennisc.alice.ast.BitwiseInfixExpression bitwiseInfixExpression ) {
		Object leftOperand = this.evaluate( bitwiseInfixExpression.leftOperand.getValue() );
		Object rightOperand = this.evaluate( bitwiseInfixExpression.rightOperand.getValue() );
		return bitwiseInfixExpression.operator.getValue().operate( leftOperand, rightOperand );
	}
	protected Boolean evaluateConditionalInfixExpression( edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression conditionalInfixExpression ) {
		edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator operator = conditionalInfixExpression.operator.getValue();
		Boolean leftOperand = (Boolean)this.evaluate( conditionalInfixExpression.leftOperand.getValue() );
		if( operator == edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator.AND ) {
			if( leftOperand ) {
				return (Boolean)this.evaluate( conditionalInfixExpression.rightOperand.getValue() );
			} else {
				return false;
			}
		} else if( operator == edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator.OR ) {
			if( leftOperand ) {
				return true;
			} else {
				return (Boolean)this.evaluate( conditionalInfixExpression.rightOperand.getValue() );
			}
		} else {
			
			return false;
		}
	}
	protected Boolean evaluateRelationalInfixExpression( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression relationalInfixExpression ) {
		Object leftOperand = this.evaluate( relationalInfixExpression.leftOperand.getValue() );
		Object rightOperand = this.evaluate( relationalInfixExpression.rightOperand.getValue() );
		return relationalInfixExpression.operator.getValue().operate( leftOperand, rightOperand );
	}
	protected Object evaluateShiftInfixExpression( edu.cmu.cs.dennisc.alice.ast.ShiftInfixExpression shiftInfixExpression ) {
		Object leftOperand = this.evaluate( shiftInfixExpression.leftOperand.getValue() );
		Object rightOperand = this.evaluate( shiftInfixExpression.rightOperand.getValue() );
		return shiftInfixExpression.operator.getValue().operate( leftOperand, rightOperand );
	}
	protected Object evaluateLogicalComplement( edu.cmu.cs.dennisc.alice.ast.LogicalComplement logicalComplement ) {
		Boolean operand = this.evaluate( logicalComplement.operand.getValue(), Boolean.class );
		return !operand;
	}
	protected String evaluateStringConcatenation( edu.cmu.cs.dennisc.alice.ast.StringConcatenation stringConcatenation ) {
		StringBuffer sb = new StringBuffer();
		Object leftOperand = this.evaluate( stringConcatenation.leftOperand.getValue() );
		Object rightOperand = this.evaluate( stringConcatenation.rightOperand.getValue() );
		sb.append( leftOperand );
		sb.append( rightOperand );
		return sb.toString();
	}

	protected Object evaluateMethodInvocation( edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation ) {
		if( methodInvocation.isValid() ) {
			assert methodInvocation.method.getValue().getParameters().size() == methodInvocation.arguments.size() : methodInvocation.method.getValue().getName();
			Object[] arguments = this.evaluateArguments( methodInvocation.method.getValue().getParameters(), methodInvocation.arguments );
			return this.invoke( methodInvocation.method.getValue(), this.evaluate( methodInvocation.expression.getValue() ), arguments );
		} else {
			javax.swing.JOptionPane.showMessageDialog( null, "skipping invalid methodInvocation: " + methodInvocation.method.getValue().getName() );
			return null;
		}
	}
	protected Object evaluateNullLiteral( edu.cmu.cs.dennisc.alice.ast.NullLiteral nullLiteral ) {
		return null;
	}
	protected Object evaluateNumberLiteral( edu.cmu.cs.dennisc.alice.ast.NumberLiteral numberLiteral ) {
		return numberLiteral.value.getValue();
	}
	protected Object evaluateDoubleLiteral( edu.cmu.cs.dennisc.alice.ast.DoubleLiteral doubleLiteral ) {
		return doubleLiteral.value.getValue();
	}
	protected Object evaluateFloatLiteral( edu.cmu.cs.dennisc.alice.ast.FloatLiteral floatLiteral ) {
		return floatLiteral.value.getValue();
	}
	protected Object evaluateIntegerLiteral( edu.cmu.cs.dennisc.alice.ast.IntegerLiteral integerLiteral ) {
		return integerLiteral.value.getValue();
	}
	protected Object evaluateParameterAccess( edu.cmu.cs.dennisc.alice.ast.ParameterAccess parameterAccess ) {
		return this.lookup( parameterAccess.parameter.getValue() );
	}
	protected Object evaluateStringLiteral( edu.cmu.cs.dennisc.alice.ast.StringLiteral stringLiteral ) {
		return stringLiteral.value.getValue();
	}
	protected Object evaluateThisExpression( edu.cmu.cs.dennisc.alice.ast.ThisExpression thisExpression ) {
		return this.getThis();
	}
	protected Object evaluateTypeExpression( edu.cmu.cs.dennisc.alice.ast.TypeExpression typeExpression ) {
		return typeExpression.value.getValue();
	}
	protected Object evaluateTypeLiteral( edu.cmu.cs.dennisc.alice.ast.TypeLiteral typeLiteral ) {
		return typeLiteral.value.getValue();
	}
	protected Object evaluateResourceExpression( edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression ) {
		return resourceExpression.resource.getValue();
	}
	

	protected Object evaluate( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		if( expression != null ) {
			if( expression instanceof edu.cmu.cs.dennisc.alice.ast.AssignmentExpression ) {
				return this.evaluateAssignmentExpression( (edu.cmu.cs.dennisc.alice.ast.AssignmentExpression)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.BooleanLiteral ) {
				return this.evaluateBooleanLiteral( (edu.cmu.cs.dennisc.alice.ast.BooleanLiteral)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.InstanceCreation ) {
				return this.evaluateInstanceCreation( (edu.cmu.cs.dennisc.alice.ast.InstanceCreation)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation ) {
				return this.evaluateArrayInstanceCreation( (edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ArrayLength ) {
				return this.evaluateArrayLength( (edu.cmu.cs.dennisc.alice.ast.ArrayLength)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ArrayAccess ) {
				return this.evaluateArrayAccess( (edu.cmu.cs.dennisc.alice.ast.ArrayAccess)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
				return this.evaluateFieldAccess( (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ConstantAccess ) {
				return this.evaluateConstantAccess( (edu.cmu.cs.dennisc.alice.ast.ConstantAccess)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.VariableAccess ) {
				return this.evaluateVariableAccess( (edu.cmu.cs.dennisc.alice.ast.VariableAccess)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression ) {
				return this.evaluateArithmeticInfixExpression( (edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.BitwiseInfixExpression ) {
				return this.evaluateBitwiseInfixExpression( (edu.cmu.cs.dennisc.alice.ast.BitwiseInfixExpression)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression ) {
				return this.evaluateConditionalInfixExpression( (edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression ) {
				return this.evaluateRelationalInfixExpression( (edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ShiftInfixExpression ) {
				return this.evaluateShiftInfixExpression( (edu.cmu.cs.dennisc.alice.ast.ShiftInfixExpression)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.LogicalComplement ) {
				return this.evaluateLogicalComplement( (edu.cmu.cs.dennisc.alice.ast.LogicalComplement)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation ) {
				return this.evaluateMethodInvocation( (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.NullLiteral ) {
				return this.evaluateNullLiteral( (edu.cmu.cs.dennisc.alice.ast.NullLiteral)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.StringConcatenation ) {
				return this.evaluateStringConcatenation( (edu.cmu.cs.dennisc.alice.ast.StringConcatenation)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.NumberLiteral ) {
				return this.evaluateNumberLiteral( (edu.cmu.cs.dennisc.alice.ast.NumberLiteral)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.DoubleLiteral ) {
				return this.evaluateDoubleLiteral( (edu.cmu.cs.dennisc.alice.ast.DoubleLiteral)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FloatLiteral ) {
				return this.evaluateFloatLiteral( (edu.cmu.cs.dennisc.alice.ast.FloatLiteral)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.IntegerLiteral ) {
				return this.evaluateIntegerLiteral( (edu.cmu.cs.dennisc.alice.ast.IntegerLiteral)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ParameterAccess ) {
				return this.evaluateParameterAccess( (edu.cmu.cs.dennisc.alice.ast.ParameterAccess)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.StringLiteral ) {
				return this.evaluateStringLiteral( (edu.cmu.cs.dennisc.alice.ast.StringLiteral)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ThisExpression ) {
				return this.evaluateThisExpression( (edu.cmu.cs.dennisc.alice.ast.ThisExpression)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
				return this.evaluateTypeExpression( (edu.cmu.cs.dennisc.alice.ast.TypeExpression)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.TypeLiteral ) {
				return this.evaluateTypeLiteral( (edu.cmu.cs.dennisc.alice.ast.TypeLiteral)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ResourceExpression ) {
				return this.evaluateResourceExpression( (edu.cmu.cs.dennisc.alice.ast.ResourceExpression)expression );
			} else {
				throw new RuntimeException( expression.getClass().getName() );
			}
		} else {
			throw new NullPointerException();
		}
	}
	protected final <E extends Object> E evaluate( edu.cmu.cs.dennisc.alice.ast.Expression expression, Class< E > cls ) {
		//in order to support python...
		//if( result instanceof Integer ) {
		//	condition = ((Integer)result) != 0;
		//} else {
		//	condition = (Boolean)result;
		//}
		Object value = this.evaluate( expression );
		if( cls.isArray() ) {
			if( value instanceof ArrayInstanceInAlice ) {
				ArrayInstanceInAlice arrayInstanceInAlice = (ArrayInstanceInAlice)value;
				//todo
				value = arrayInstanceInAlice.getValues();
			}
		}
		return cls.cast( value );
	}


	protected void executeAssertStatement( edu.cmu.cs.dennisc.alice.ast.AssertStatement assertStatement ) {
		assert this.evaluate( assertStatement.expression.getValue(), Boolean.class ) : this.evaluate( assertStatement.message.getValue() );
	}
	protected void executeBlockStatement( edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement ) throws ReturnException {
		//todo?
		edu.cmu.cs.dennisc.alice.ast.Statement[] array = new edu.cmu.cs.dennisc.alice.ast.Statement[ blockStatement.statements.size() ];
		blockStatement.statements.toArray( array );
		for( edu.cmu.cs.dennisc.alice.ast.Statement statement : array ) {
			this.execute( statement );
		}
	}
	protected void executeConditionalStatement( edu.cmu.cs.dennisc.alice.ast.ConditionalStatement conditionalStatement ) throws ReturnException {
		for( edu.cmu.cs.dennisc.alice.ast.BooleanExpressionBodyPair booleanExpressionBodyPair : conditionalStatement.booleanExpressionBodyPairs ) {
			if( evaluate( booleanExpressionBodyPair.expression.getValue(), Boolean.class ) ) {
				this.execute( booleanExpressionBodyPair.body.getValue() );
				return;
			}
		}
		this.execute( conditionalStatement.elseBody.getValue() );
	}
	protected void executeComment( edu.cmu.cs.dennisc.alice.ast.Comment comment ) {
	}
	protected void executeCountLoop( edu.cmu.cs.dennisc.alice.ast.CountLoop countLoop ) throws ReturnException {
		edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = countLoop.variable.getValue();
		edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice constant = countLoop.constant.getValue();
		this.pushLocal( variable, -1 );
		try {
			final int n = this.evaluate( countLoop.count.getValue(), Integer.class );
			this.pushLocal( constant, n );
			try {
				for( int i=0; i<n; i++ ) {
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
	protected void executeDoInOrder( edu.cmu.cs.dennisc.alice.ast.DoInOrder doInOrder ) throws ReturnException {
		executeBlockStatement( doInOrder.body.getValue() );
	}
	private static int threadCount = 0;
	protected void executeDoInThread( final edu.cmu.cs.dennisc.alice.ast.DoInThread doInThread ) throws ReturnException {
		final Frame frame = this.createCopyOfCurrentFrame();
		new edu.cmu.cs.dennisc.java.lang.ThreadWithRevealingToString( org.alice.virtualmachine.ThreadGroupUtilities.getThreadGroup(), "DoInThread-"+(VirtualMachine.threadCount++) ) {
			@Override
			public void run() {
				pushCurrentThread( frame );
				try {
					edu.cmu.cs.dennisc.alice.ProgramClosedException.invokeAndCatchProgramClosedException( new Runnable() {
						public void run() {
							try {
								execute( doInThread.body.getValue() );
							} catch( ReturnException re ) {
								//todo
							}
						}
					} );
				} finally {
					popCurrentThread();
				}
			}
		}.start();
	}
	protected void executeDoTogether( edu.cmu.cs.dennisc.alice.ast.DoTogether doTogether ) throws ReturnException {
		edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement = doTogether.body.getValue();
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
			for( int i=0; i<runnables.length; i++ ) {
				final edu.cmu.cs.dennisc.alice.ast.Statement statementI = blockStatement.statements.get( i );
				runnables[ i ] = new Runnable() {;
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
			org.alice.virtualmachine.DoTogether.invokeAndWait( runnables );
		}
	}
	protected void executeExpressionStatement( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement ) {
		@SuppressWarnings("unused")
		Object unused = this.evaluate( expressionStatement.expression.getValue() );
	}
	private void excecuteForEachLoop( edu.cmu.cs.dennisc.alice.ast.AbstractForEachLoop forEachInLoop, Object[] array ) throws ReturnException {
		edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = forEachInLoop.variable.getValue();
		edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement = forEachInLoop.body.getValue();
		this.pushLocal( variable, -1 );
		try {
			for( Object o : array ) {
				this.setLocal( variable, o );
				this.execute( blockStatement );
			}
		} finally {
			this.popLocal( variable );
		}

	}

	protected void executeForEachInArrayLoop( edu.cmu.cs.dennisc.alice.ast.ForEachInArrayLoop forEachInArrayLoop ) throws ReturnException {
		Object[] array = this.evaluate( forEachInArrayLoop.array.getValue(), Object[].class );
		excecuteForEachLoop( forEachInArrayLoop, array );
	}
	protected void executeForEachInIterableLoop( edu.cmu.cs.dennisc.alice.ast.ForEachInIterableLoop forEachInIterableLoop ) throws ReturnException {
		Iterable<?> iterable = this.evaluate( forEachInIterableLoop.iterable.getValue(), Iterable.class );
		excecuteForEachLoop( forEachInIterableLoop, edu.cmu.cs.dennisc.java.lang.IterableUtilities.toArray( iterable ) );
	}
	
	private void excecuteForEachTogether( edu.cmu.cs.dennisc.alice.ast.AbstractEachInTogether forEachInTogether, final Object[] array ) throws ReturnException {
		final edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = forEachInTogether.variable.getValue();
		final edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement = forEachInTogether.body.getValue();

		switch( array.length ) {
		case 0:
			break;
		case 1:
			VirtualMachine.this.pushLocal( variable, array[ 0 ] );
			try {
				VirtualMachine.this.execute( blockStatement );
			} finally {
				VirtualMachine.this.popLocal(variable);
			}
			break;
		default:
			final Frame owner = this.getFrameForThread( Thread.currentThread() );
			org.alice.virtualmachine.ForEachTogether.invokeAndWait( array, new org.alice.virtualmachine.ForEachRunnable< Object >() {
				public void run( Object value ) {
					pushCurrentThread( owner );
					try {
						VirtualMachine.this.pushLocal( variable, value );
						try {
							VirtualMachine.this.execute( blockStatement );
						} catch( ReturnException re ) {
							//todo
						} finally {
							VirtualMachine.this.popLocal(variable);
						}
					} finally {
						popCurrentThread();
					}
				}
			} );
		}
	}
	protected void executeEachInArrayTogether( edu.cmu.cs.dennisc.alice.ast.EachInArrayTogether eachInArrayTogether ) throws ReturnException {
		Object[] array = this.evaluate( eachInArrayTogether.array.getValue(), Object[].class );
		excecuteForEachTogether( eachInArrayTogether, array );
	}
	protected void executeEachInIterableTogether( edu.cmu.cs.dennisc.alice.ast.EachInIterableTogether eachInIterableTogether ) throws ReturnException {
		Iterable<?> iterable = this.evaluate( eachInIterableTogether.iterable.getValue(), Iterable.class );
		excecuteForEachTogether( eachInIterableTogether, edu.cmu.cs.dennisc.java.lang.IterableUtilities.toArray( iterable ) );
	}
	protected void executeReturnStatement( edu.cmu.cs.dennisc.alice.ast.ReturnStatement returnStatement ) throws ReturnException {
		Object returnValue = this.evaluate( returnStatement.expression.getValue() );
		//setReturnValue( returnValue );
		throw new ReturnException( returnValue );
	}
	protected void executeWhileLoop( edu.cmu.cs.dennisc.alice.ast.WhileLoop whileLoop ) throws ReturnException {
		while( this.evaluate( whileLoop.conditional.getValue(), Boolean.class ) ) {
			this.execute( whileLoop.body.getValue() );
		}
	}

	
	protected void executeVariableDeclarationStatement( edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement variableDeclarationStatement ) {
		this.pushLocal( variableDeclarationStatement.variable.getValue(), this.evaluate( variableDeclarationStatement.initializer.getValue() ) );
		//handle pop on exit of owning block statement
	}
	protected void executeConstantDeclarationStatement( edu.cmu.cs.dennisc.alice.ast.ConstantDeclarationStatement constantDeclarationStatement ) {
		this.pushLocal( constantDeclarationStatement.constant.getValue(), this.evaluate( constantDeclarationStatement.initializer.getValue() ) );
		//handle pop on exit of owning block statement
	}

	protected void execute( edu.cmu.cs.dennisc.alice.ast.Statement statement ) throws ReturnException {
		assert statement != null;
		if( statement.isEnabled.getValue() ) {
			if( statement instanceof edu.cmu.cs.dennisc.alice.ast.AssertStatement ) {
				this.executeAssertStatement( (edu.cmu.cs.dennisc.alice.ast.AssertStatement)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.BlockStatement ) {
				this.executeBlockStatement( (edu.cmu.cs.dennisc.alice.ast.BlockStatement)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ConditionalStatement ) {
				this.executeConditionalStatement( (edu.cmu.cs.dennisc.alice.ast.ConditionalStatement)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.Comment ) {
				this.executeComment( (edu.cmu.cs.dennisc.alice.ast.Comment)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.CountLoop ) {
				this.executeCountLoop( (edu.cmu.cs.dennisc.alice.ast.CountLoop)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.DoTogether ) {
				this.executeDoTogether( (edu.cmu.cs.dennisc.alice.ast.DoTogether)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.DoInOrder ) {
				this.executeDoInOrder( (edu.cmu.cs.dennisc.alice.ast.DoInOrder)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.DoInThread ) {
				this.executeDoInThread( (edu.cmu.cs.dennisc.alice.ast.DoInThread)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionStatement ) {
				this.executeExpressionStatement( (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ForEachInArrayLoop ) {
				this.executeForEachInArrayLoop( (edu.cmu.cs.dennisc.alice.ast.ForEachInArrayLoop)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ForEachInIterableLoop ) {
				this.executeForEachInIterableLoop( (edu.cmu.cs.dennisc.alice.ast.ForEachInIterableLoop)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.EachInArrayTogether ) {
				this.executeEachInArrayTogether( (edu.cmu.cs.dennisc.alice.ast.EachInArrayTogether)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.EachInIterableTogether ) {
				this.executeEachInIterableTogether( (edu.cmu.cs.dennisc.alice.ast.EachInIterableTogether)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ReturnStatement ) {
				this.executeReturnStatement( (edu.cmu.cs.dennisc.alice.ast.ReturnStatement)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.WhileLoop ) {
				this.executeWhileLoop( (edu.cmu.cs.dennisc.alice.ast.WhileLoop)statement );
			} else	if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ConstantDeclarationStatement ) {
				this.executeConstantDeclarationStatement( (edu.cmu.cs.dennisc.alice.ast.ConstantDeclarationStatement)statement );
			} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement ) {
				this.executeVariableDeclarationStatement( (edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement)statement );
			} else {
				throw new RuntimeException();
			}
		}
	}
}
