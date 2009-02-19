/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.alice.virtualmachine;

/**
 * @author Dennis Cosgrove
 */
public abstract class VirtualMachine {
	@Deprecated
	public Object getAccessForSceneEditor( edu.cmu.cs.dennisc.alice.ast.AbstractField field, Object instance ) {
		return get( field, instance );
	}
	@Deprecated
	public Object createInstanceForSceneEditor( edu.cmu.cs.dennisc.alice.ast.AbstractType entryPointType ) {
		pushCurrentThread( null );
		try {
			return this.createInstance( this.entryPointType.getDeclaredConstructor() );
		} finally {
			popCurrentThread();
		}
	}

	
	protected abstract Object getThis();

	protected abstract void pushFrame( InstanceInAlice instance, java.util.Map<edu.cmu.cs.dennisc.alice.ast.AbstractParameter,Object> map );
	protected abstract void popFrame();

	protected abstract Object lookup( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter );

	protected abstract void pushLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local, Object value );
	protected abstract Object getLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local );
	protected abstract void setLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local, Object value );
	protected abstract void popLocal( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice local );

	protected abstract void pushCurrentThread( Thread parentThread );
	protected abstract void popCurrentThread();
	
//	public Object createInstanceEntryPoint( edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor, Object... arguments ) {
//		pushCurrentThread( null );
//		try {
//			return this.createInstance( constructor, arguments );
//		} finally {
//			popCurrentThread();
//		}
//	}

	
	public void invokeEntryPoint( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method, java.lang.Object instance, java.lang.Object... arguments ) {
		pushCurrentThread( null );
		try {
			invoke( method, instance, arguments );
		} finally {
			popCurrentThread();
		}
	}

	private edu.cmu.cs.dennisc.alice.ast.AbstractType entryPointType;
	public Object createInstanceEntryPoint( edu.cmu.cs.dennisc.alice.ast.AbstractType entryPointType ) {
		this.setEntryPointType( entryPointType );
		pushCurrentThread( null );
		try {
			return this.createInstance( this.entryPointType.getDeclaredConstructor() );
		} finally {
			popCurrentThread();
		}
	}
	
	public void setEntryPointType( edu.cmu.cs.dennisc.alice.ast.AbstractType entryPointType ) {
		this.entryPointType = entryPointType;
	}
		
	// special functionality added for the scene editor
	private boolean isConstructorBodyExecutionDesired = true;
	public boolean isConstructorBodyExecutionDesired() {
		return this.isConstructorBodyExecutionDesired;
	}
	public void setConstructorBodyExecutionDesired(boolean isConstructorBodyExecutionDesired) {
		this.isConstructorBodyExecutionDesired = isConstructorBodyExecutionDesired;
	}
	
	protected Object createInstanceFromConstructorDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice constructor, Object[] arguments ) {
		InstanceInAlice instanceInAlice = new InstanceInAlice( this, constructor, arguments );
		if( this.isConstructorBodyExecutionDesired ) {
			java.util.Map<edu.cmu.cs.dennisc.alice.ast.AbstractParameter,Object> map = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractParameter, Object >();
			for( int i=0; i<arguments.length; i++ ) {
				map.put( constructor.parameters.get( i ), arguments[ i ] );
			}
			this.pushFrame( instanceInAlice, map );
			try {
				this.executeBlockStatement( constructor.body.getValue() );
			} catch( ReturnException re ) {
				throw new RuntimeException( re );
			} finally {
				this.popFrame();
			}
		}
		return instanceInAlice;
	}
	protected Object createInstanceFromConstructorDeclaredInJava( edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor, Object[] arguments ) {
		//todo
		for( int i=0; i<arguments.length; i++ ) {
			Object value = arguments[ i ];
			if( value instanceof InstanceInAlice ) {
				InstanceInAlice valueInAlice = (InstanceInAlice)value;
				arguments[ i ] = valueInAlice.getInstanceInJava();
			}
		}
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( constructor.getCnstrctr(), arguments );
	}
	protected Object createInstance( edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor, Object... arguments ) {
		assert constructor != null;
		if( constructor instanceof edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice ) {
			return this.createInstanceFromConstructorDeclaredInAlice( (edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice)constructor, arguments );
		} else if( constructor instanceof edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava ) {
			return this.createInstanceFromConstructorDeclaredInJava( (edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava)constructor, arguments );
		} else {
			throw new RuntimeException();
		}
//		java.lang.reflect.Method mthd = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getDeclaredMethod( VirtualMachine.class, "createInstanceActual", constructor.getClass(), Object[].class );
//		return this.invokeMthd( mthd, constructor, arguments );
	}

	protected Object createArrayInstanceFromTypeDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.ArrayTypeDeclaredInAlice type, int[] lengths, Object[] arguments ) {
		//todo
		//Object rv = java.lang.reflect.Array.newInstance( type.getLeafType().getFirstClassEncounteredDeclaredInJava(), lengths );
		Object rv = java.lang.reflect.Array.newInstance( Object.class, lengths );
		for( int i=0; i<arguments.length; i++ ) {
			java.lang.reflect.Array.set( rv, i, arguments[ i ] );
		}
		return rv;
	}
	protected Object createArrayInstanceFromTypeDeclaredInJava( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava type, int[] lengths, Object[] arguments ) {
		Class<?> cls = type.getCls();
		assert cls != null;
		Class<?> componentCls = cls.getComponentType();
		assert componentCls != null;
		Object rv = java.lang.reflect.Array.newInstance( componentCls, lengths );
		for( int i=0; i<arguments.length; i++ ) {
			if( arguments[ i ] instanceof InstanceInAlice ) {
				InstanceInAlice valueInAlice = (InstanceInAlice)arguments[ i ];
				arguments[ i ] = valueInAlice.getInstanceInJava();
			}
			java.lang.reflect.Array.set( rv, i, arguments[ i ] );
		}
		return rv;
	}
	protected Object createArrayInstance( edu.cmu.cs.dennisc.alice.ast.AbstractType type, int[] lengths, Object... arguments ) {
		assert type != null;
		if( type instanceof edu.cmu.cs.dennisc.alice.ast.ArrayTypeDeclaredInAlice ) {
			return this.createArrayInstanceFromTypeDeclaredInAlice( (edu.cmu.cs.dennisc.alice.ast.ArrayTypeDeclaredInAlice)type, lengths, arguments );
		} else if( type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava ) {
			return this.createArrayInstanceFromTypeDeclaredInJava( (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava)type, lengths, arguments );
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
		Object[] rv = new Object[ N ];
		if( N>0 ) {
			for( int i=0; i<N-1; i++ ) {
				rv[ i ] = this.evaluate( arguments.get( i ) );
			}
			
			edu.cmu.cs.dennisc.alice.ast.AbstractParameter paramLast = parameters.get( N-1 );
			if( paramLast.isVariableLength() ) {
				Class<?> arrayCls =  paramLast.getValueType().getFirstClassEncounteredDeclaredInJava();
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
			return java.lang.reflect.Array.getLength( array );
		} else {
			throw new NullPointerException();
		}
	}
	protected Object getFieldDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance ) {
		assert instance instanceof InstanceInAlice;
		InstanceInAlice instanceInAlice = (InstanceInAlice)instance;
		return instanceInAlice.get( field );
	}
	protected void setFieldDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance, Object value ) {
		assert instance instanceof InstanceInAlice;
		InstanceInAlice instanceInAlice = (InstanceInAlice)instance;
		instanceInAlice.set( field, value );
	}
	protected Object getFieldDeclaredInJavaWithField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField field, Object instance ) {
		if( instance instanceof InstanceInAlice ) {
			InstanceInAlice instanceInAlice = (InstanceInAlice)instance;
			instance = instanceInAlice.getInstanceInJava();
		}
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.get( field.getFld(), instance );
	}
	protected void setFieldDeclaredInJavaWithField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField field, Object instance, Object value ) {
		//todo
		if( value instanceof InstanceInAlice ) {
			InstanceInAlice valueInAlice = (InstanceInAlice)value;
			value = valueInAlice.getInstanceInJava();
		}
		edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.set( field.getFld(), instance, value );
	}
	protected Object getFieldDeclaredInJavaWithGetterAndSetter( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithGetterAndSetter field, Object instance ) {
		if( instance instanceof InstanceInAlice ) {
			InstanceInAlice instanceInAlice = (InstanceInAlice)instance;
			instance = instanceInAlice.getInstanceInJava();
		}
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.invoke( instance, field.getGttr() );
	}
	protected void setFieldDeclaredInJavaWithGetterAndSetter( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithGetterAndSetter field, Object instance, Object value ) {
		//todo

		if( instance instanceof InstanceInAlice ) {
			InstanceInAlice instanceInAlice = (InstanceInAlice)instance;
			instance = instanceInAlice.getInstanceInJava();
		}

		if( value instanceof InstanceInAlice ) {
			InstanceInAlice valueInAlice = (InstanceInAlice)value;
			value = valueInAlice.getInstanceInJava();
		}
		edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.invoke( instance, field.getSttr(), value );
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

	protected Object getItemAtIndex( edu.cmu.cs.dennisc.alice.ast.AbstractType arrayType, Object array, Integer index ) {
		assert arrayType != null;
		assert arrayType.isArray();
		return java.lang.reflect.Array.get( array, index );
	}
	protected void setItemAtIndex( edu.cmu.cs.dennisc.alice.ast.AbstractType arrayType, Object array, Integer index, Object value ) {
		assert arrayType != null;
		assert arrayType.isArray();
		java.lang.reflect.Array.set( array, index, value );
	}
	protected Object invokeMethodDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method, Object instance, Object... arguments ) {
		assert instance instanceof InstanceInAlice;
		InstanceInAlice instanceInAlice = (InstanceInAlice)instance;
		java.util.Map<edu.cmu.cs.dennisc.alice.ast.AbstractParameter,Object> map = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractParameter, Object >();
		for( int i=0; i<arguments.length; i++ ) {
			map.put( method.parameters.get( i ), arguments[ i ] );
		}
		this.pushFrame( instanceInAlice, map );
		try {
			this.execute( method.body.getValue() );
			if( method.isProcedure() ) {
				return null;
			} else {
				throw new RuntimeException( "no return value" );
			}
		} catch( ReturnException re ) {
			return re.getValue();
		} finally {
			this.popFrame();
		}
	}
	protected Object invokeMethodDeclaredInJava( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava method, Object instance, Object... arguments ) {
		//todo
		if( instance instanceof InstanceInAlice ) {
			InstanceInAlice instanceInAlice = (InstanceInAlice)instance;
			instance = instanceInAlice.getInstanceInJava();
		}

		for( int i = 0; i < arguments.length; i++ ) {
			if( arguments[ i ] instanceof InstanceInAlice ) {
				InstanceInAlice valueInAlice = (InstanceInAlice)arguments[ i ];
				arguments[ i ] = valueInAlice.getInstanceInJava();
			}
		}
//		try {
			return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.invoke( instance, method.getMthd(), arguments );
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
		return (Number)arithmeticInfixExpression.operator.getValue().operate( leftOperand, rightOperand );
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
		return (Boolean)relationalInfixExpression.operator.getValue().operate( leftOperand, rightOperand );
	}
	protected Object evaluateShiftInfixExpression( edu.cmu.cs.dennisc.alice.ast.ShiftInfixExpression shiftInfixExpression ) {
		Object leftOperand = this.evaluate( shiftInfixExpression.leftOperand.getValue() );
		Object rightOperand = this.evaluate( shiftInfixExpression.rightOperand.getValue() );
		return shiftInfixExpression.operator.getValue().operate( leftOperand, rightOperand );
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
		return this.invoke( methodInvocation.method.getValue(), this.evaluate( methodInvocation.expression.getValue() ), this.evaluateArguments( methodInvocation.method.getValue().getParameters(), methodInvocation.arguments ) );
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
	protected Object evaluateEntryPointTypeExpression( edu.cmu.cs.dennisc.alice.ast.EntryPointTypeExpression entryPointTypeExpression ) {
		return this.entryPointType;
	}
	

	protected Object evaluate( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
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
		} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.EntryPointTypeExpression ) {
			return this.evaluateEntryPointTypeExpression( (edu.cmu.cs.dennisc.alice.ast.EntryPointTypeExpression)expression );
		} else {
			throw new RuntimeException();
		}
	}
	protected final <E extends Object> E evaluate( edu.cmu.cs.dennisc.alice.ast.Expression expression, Class< E > cls ) {
		//todo: for python
//		Object result = this.evaluate( booleanExpressionBodyPair.expression.getValue() );
//		Boolean condition;
//		if( result instanceof Integer ) {
//			condition = ((Integer)result) != 0;
//		} else {
//			condition = (Boolean)result;
//		}
//		if( condition ) {
		return (E)this.evaluate( expression );
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
			Runnable[] runnables = new Runnable[ blockStatement.statements.size() ];
			final Thread parentThread = Thread.currentThread();
			for( int i=0; i<runnables.length; i++ ) {
				final edu.cmu.cs.dennisc.alice.ast.Statement statementI = blockStatement.statements.get( i );
				runnables[ i ] = new Runnable() {;
					public void run() {
						//edu.cmu.cs.dennisc.print.PrintUtilities.println( statementI );
						pushCurrentThread( parentThread );
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
		excecuteForEachLoop( forEachInIterableLoop, edu.cmu.cs.dennisc.lang.IterableUtilities.toArray( iterable ) );
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
			final Thread parentThread = Thread.currentThread();
			org.alice.virtualmachine.ForEachTogether.invokeAndWait( array, new org.alice.virtualmachine.ForEachRunnable< Object >() {
				public void run( Object value ) {
					pushCurrentThread( parentThread );
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
		Iterable iterable = this.evaluate( eachInIterableTogether.iterable.getValue(), Iterable.class );
		excecuteForEachTogether( eachInIterableTogether, edu.cmu.cs.dennisc.lang.IterableUtilities.toArray( iterable ) );
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
