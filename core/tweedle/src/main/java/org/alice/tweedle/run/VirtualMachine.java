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
package org.alice.tweedle.run;

import org.alice.tweedle.ast.TweedleExpression;
import org.alice.tweedle.TweedleField;
import org.alice.tweedle.TweedleStatement;
import org.alice.tweedle.TweedleClass;
import org.alice.tweedle.TweedleMethod;
import org.alice.tweedle.TweedleValue;

public abstract class VirtualMachine {
/*	public abstract LgnaStackTraceElement[] getStackTrace( Thread thread );

	protected abstract TweedleObject getThis();

	protected abstract void pushBogusFrame( TweedleObject instance );

	protected abstract void pushConstructorFrame( TweedleClass type, java.util.Map<AbstractParameter, Object> map );

	protected abstract void setConstructorFrame( TweedleObject instance );

	protected abstract void pushMethodFrame( TweedleObject instance, UserMethod method, java.util.Map<AbstractParameter, Object> map );

	protected abstract void pushLambdaFrame(  XX instance XX, UserLambda lambda, Invokable singleInvokable, java.util.Map<AbstractParameter, Object> map );

	protected abstract void popFrame();

	protected abstract Object lookup( UserParameter parameter );

	protected abstract void pushLocal( UserLocal local, Object value );

	protected abstract Object getLocal( UserLocal local );

	protected abstract void setLocal( UserLocal local, Object value );

	protected abstract void popLocal( UserLocal local );

	protected abstract void pushCurrentThread( Frame frame );

	protected abstract void popCurrentThread();*/

	public Object[] ENTRY_POINT_evaluate( TweedleObject instance, TweedleExpression[] expressions ) {
		Frame frame = new Frame(instance); // was bogus frame - set this to be instance
		Object[] values = new Object[ expressions.length ];
		for( int i = 0; i < expressions.length; i++ ) {
			values[ i ] = evaluate( frame, expressions[ i ] );
		}
		return values;
	}

	public void ENTRY_POINT_invoke( TweedleObject instance, TweedleMethod method, TweedleValue... arguments ) {
		Frame frame = new Frame(instance);
		invoke(frame, instance, method, arguments );
	}

	public TweedleObject ENTRY_POINT_createInstance( TweedleClass entryPointType, TweedleValue... arguments ) {
		Frame frame = new Frame(null);
		return entryPointType.instantiate(frame, arguments);
	}

	public TweedleValue createAndSetFieldInstance(Frame frame, TweedleObject instance, TweedleField field ) {
		return instance.initializeField(frame, field);
	}

	public Object ACCEPTABLE_HACK_FOR_SCENE_EDITOR_initializeField( TweedleObject instance, TweedleField field ) {
		Frame frame = new Frame(instance);
		return createAndSetFieldInstance(frame, instance, field );
	}

	public void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_executeStatement( TweedleObject instance, TweedleStatement statement ) {
		Frame frame = new Frame(instance);
		execute(frame, statement);
	}

/*	private <T extends TweedleValue> TweedleArray createArray( T[] values ) {
		return new TweedleArray( values );
	}*/

	/*private Object evaluateArgument( AbstractArgument argument ) {
		assert argument != null;
		Expression expression = argument.expression.getPrimitiveValue();
		assert expression != null;
		if( expression instanceof LambdaExpression ) {
			return this.EPIC_HACK_evaluateLambdaExpression( (LambdaExpression)expression, argument );
		} else {
			return this.evaluate( frame, expression );
		}
	}

	public Object[] evaluateArguments( AbstractCode code, NodeListProperty<SimpleArgument> arguments,
					NodeListProperty<SimpleArgument> variableArguments, NodeListProperty<JavaKeyedArgument> keyedArguments ) {
		//todo: when variable length and keyed parameters are offered in the IDE (User) this code will need to be updated
		java.util.List<? extends AbstractParameter> requiredParameters = code.getRequiredParameters();
		AbstractParameter variableParameter = code.getVariableLengthParameter();
		AbstractParameter keyedParameter = code.getKeyedParameter();

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
			JavaType variableArrayType = variableParameter.getValueType().getFirstEncounteredJavaType();
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
			JavaType keyedArrayType = keyedParameter.getValueType().getFirstEncounteredJavaType();
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
			//			AbstractParameter paramLast = requiredParameters.get( N-1 );
			//			if( paramLast.isVariableLength() ) {
			//				Class<?> arrayCls =  paramLast.getValueType().getFirstTypeEncounteredDeclaredInJava().getClassReflectionProxy().getReification();
			//				assert arrayCls != null;
			//				Class<?> componentCls = arrayCls.getComponentType();
			//				assert componentCls != null;
			//				rv[ N-1 ] = java.lang.reflect.Array.newInstance( componentCls, M );
			//				for( int j=0; j<( M - (N-1) ); j++ ) {
			//					AbstractArgument argumentJ = arguments.get( (N-1) + j );
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
	}*/

	/*protected Integer getArrayLength( Object array ) {
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
	}*/

	protected TweedleValue get( TweedleField field, TweedleObject instance ) {
		return instance.get(field);
	}

	protected void set( TweedleField field, TweedleObject instance, TweedleValue value ) {
		instance.set(field, value);
	}


	protected Object invoke( Frame frame, TweedleObject target, TweedleMethod method, TweedleValue... arguments ) {
		return method.invoke(frame, target, arguments);
		/*pushMethodFrame( target, method, arguments );
		java.util.Map<AbstractParameter, Object> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		for( int i = 0; i < arguments.length; i++ ) {
			map.put( method.requiredParameters.get( i ), arguments[ i ] );
		}
		try {
			//method specific
		} catch( ReturnException re ) {
			return re.getPrimitiveValue();
		} finally {
			popFrame();
		}*/
	}

	/* These evaluations will be pushed on the Expression classes themselves as evaluate(Frame)


	protected Object evaluateAssignmentExpression( AssignmentExpression assignmentExpression ) {
		Expression leftHandExpression = assignmentExpression.leftHandSide.getPrimitiveValue();
		Expression rightHandExpression = assignmentExpression.rightHandSide.getPrimitiveValue();
		Object rightHandValue = this.evaluate( frame, rightHandExpression );
		if( assignmentExpression.operator.getPrimitiveValue() == AssignmentExpression.Operator.ASSIGN ) {
			if( leftHandExpression instanceof FieldAccess ) {
				FieldAccess fieldAccess = (FieldAccess)leftHandExpression;
				this.set( fieldAccess.field.getPrimitiveValue(), this.evaluate( frame, fieldAccess.expression.getPrimitiveValue() ), rightHandValue );
			} else if( leftHandExpression instanceof LocalAccess ) {
				LocalAccess localAccess = (LocalAccess)leftHandExpression;
				this.setLocal( localAccess.local.getPrimitiveValue(), rightHandValue );
			} else if( leftHandExpression instanceof ArrayAccess ) {
				ArrayAccess arrayAccess = (ArrayAccess)leftHandExpression;
				this.setItemAtIndex( arrayAccess.arrayType.getPrimitiveValue(), this.evaluate( frame, arrayAccess.array.getPrimitiveValue() ), this.evaluateInt( arrayAccess.index.getPrimitiveValue(), "array index is null" ), rightHandValue );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: evaluateActual", assignmentExpression.leftHandSide.getPrimitiveValue(), rightHandValue );
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: evaluateActual", assignmentExpression );
		}
		return null;
	}

	protected Object evaluateBooleanLiteral( BooleanLiteral booleanLiteral ) {
		return booleanLiteral.value.getPrimitiveValue();
	}

	protected Object evaluateArrayInstanceCreation( ArrayInstanceCreation arrayInstanceCreation ) {
		Object[] values = new Object[ arrayInstanceCreation.expressions.size() ];
		for( int i = 0; i < values.length; i++ ) {
			values[ i ] = this.evaluate( frame, arrayInstanceCreation.expressions.get( i ) );
		}
		int[] lengths = new int[ arrayInstanceCreation.lengths.size() ];
		for( int i = 0; i < lengths.length; i++ ) {
			lengths[ i ] = arrayInstanceCreation.lengths.get( i );
		}
		return this.createArrayInstance( arrayInstanceCreation.arrayType.getPrimitiveValue(), lengths, values );
	}

	private void checkIndex( int index, int length ) {
		if( ( 0 <= index ) && ( index < length ) ) {
			//pass
		} else {
			throw new LgnaVmArrayIndexOutOfBoundsException( this, index, length );
		}
	}

	protected Object getItemAtIndex( AbstractType<?, ?, ?> arrayType, Object array, Integer index ) {
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

	protected void setItemAtIndex( AbstractType<?, ?, ?> arrayType, Object array, Integer index, Object value ) {
		value = TweedleObject.getJavaInstanceIfNecessary( value );
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

	protected Object evaluateArrayAccess( ArrayAccess arrayAccess ) {
		return this.getItemAtIndex( arrayAccess.arrayType.getPrimitiveValue(), this.evaluate( frame, arrayAccess.array.getPrimitiveValue() ), this.evaluateInt( arrayAccess.index.getPrimitiveValue(), "array index is null" ) );
	}

	protected Integer evaluateArrayLength( ArrayLength arrayLength ) {
		return this.getArrayLength( this.evaluate( frame, arrayLength.array.getPrimitiveValue() ) );
	}

	protected Object evaluateFieldAccess( FieldAccess fieldAccess ) {
		Object o = fieldAccess.field.getPrimitiveValue();
		if( o instanceof Field ) {
			Field field = fieldAccess.field.getPrimitiveValue();
			Expression expression = fieldAccess.expression.getPrimitiveValue();
			Object value = this.evaluate( frame, expression );
			return this.get( field, value );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "field access field is not a field", o );
			Node node = fieldAccess;
			while( node != null ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "   ", node );
				node = node.getParent();
			}
			return null;
		}
	}

	protected Object evaluateLocalAccess( LocalAccess localAccess ) {
		return this.getLocal( localAccess.local.getPrimitiveValue() );
	}

	protected Object evaluateArithmeticInfixExpression( ArithmeticInfixExpression arithmeticInfixExpression ) {
		Number leftOperand = (Number)this.evaluate( frame, arithmeticInfixExpression.leftOperand.getPrimitiveValue() );
		Number rightOperand = (Number)this.evaluate( frame, arithmeticInfixExpression.rightOperand.getPrimitiveValue() );
		return arithmeticInfixExpression.operator.getPrimitiveValue().operate( leftOperand, rightOperand );
	}

	protected Object evaluateBitwiseInfixExpression( BitwiseInfixExpression bitwiseInfixExpression ) {
		Object leftOperand = this.evaluate( frame, bitwiseInfixExpression.leftOperand.getPrimitiveValue() );
		Object rightOperand = this.evaluate( frame, bitwiseInfixExpression.rightOperand.getPrimitiveValue() );
		return bitwiseInfixExpression.operator.getPrimitiveValue().operate( leftOperand, rightOperand );
	}

	protected Boolean evaluateConditionalInfixExpression( ConditionalInfixExpression conditionalInfixExpression ) {
		ConditionalInfixExpression.Operator operator = conditionalInfixExpression.operator.getPrimitiveValue();
		Boolean leftOperand = (Boolean)this.evaluate( frame, conditionalInfixExpression.leftOperand.getPrimitiveValue() );
		if( operator == ConditionalInfixExpression.Operator.AND ) {
			if( leftOperand ) {
				return (Boolean)this.evaluate( frame, conditionalInfixExpression.rightOperand.getPrimitiveValue() );
			} else {
				return false;
			}
		} else if( operator == ConditionalInfixExpression.Operator.OR ) {
			if( leftOperand ) {
				return true;
			} else {
				return (Boolean)this.evaluate( frame, conditionalInfixExpression.rightOperand.getPrimitiveValue() );
			}
		} else {

			return false;
		}
	}

	protected Boolean evaluateRelationalInfixExpression( RelationalInfixExpression relationalInfixExpression ) {
		Object leftOperand = TweedleObject.getJavaInstanceIfNecessary( this.evaluate( frame, relationalInfixExpression.leftOperand.getPrimitiveValue() ) );
		Object rightOperand = TweedleObject.getJavaInstanceIfNecessary( this.evaluate( frame, relationalInfixExpression.rightOperand.getPrimitiveValue() ) );
		if( leftOperand != null ) {
			if( rightOperand != null ) {
				return relationalInfixExpression.operator.getPrimitiveValue().operate( leftOperand, rightOperand );
			} else {
				throw new LgnaVmNullPointerException( "right operand is null.", this );
			}
		} else {
			if( rightOperand != null ) {
				throw new LgnaVmNullPointerException( "left operand is null.", this );
			} else {
				throw new LgnaVmNullPointerException( "left and right operands are both null.", this );
			}
		}
	}

	protected Object evaluateShiftInfixExpression( ShiftInfixExpression shiftInfixExpression ) {
		Object leftOperand = this.evaluate( frame, shiftInfixExpression.leftOperand.getPrimitiveValue() );
		Object rightOperand = this.evaluate( frame, shiftInfixExpression.rightOperand.getPrimitiveValue() );
		return shiftInfixExpression.operator.getPrimitiveValue().operate( leftOperand, rightOperand );
	}

	protected Object evaluateLogicalComplement( LogicalComplement logicalComplement ) {
		Boolean operand = this.evaluateBoolean( logicalComplement.operand.getPrimitiveValue(), "logical complement expression is null" );
		return !operand;
	}

	protected String evaluateStringConcatenation( StringConcatenation stringConcatenation ) {
		StringBuffer sb = new StringBuffer();
		Object leftOperand = this.evaluate( frame, stringConcatenation.leftOperand.getPrimitiveValue() );
		Object rightOperand = this.evaluate( frame, stringConcatenation.rightOperand.getPrimitiveValue() );
		sb.append( leftOperand );
		sb.append( rightOperand );
		return sb.toString();
	}

	protected Object evaluateMethodInvocation( MethodInvocation methodInvocation ) {
		if( methodInvocation.isValid() ) {
			Object[] allArguments = this.evaluateArguments( methodInvocation.method.getPrimitiveValue(), methodInvocation.requiredArguments, methodInvocation.variableArguments, methodInvocation.keyedArguments );
			int parameterCount = methodInvocation.method.getPrimitiveValue().getRequiredParameters().size();
			if( methodInvocation.method.getPrimitiveValue().getVariableLengthParameter() != null ) {
				parameterCount += 1;
			}
			if( methodInvocation.method.getPrimitiveValue().getKeyedParameter() != null ) {
				parameterCount += 1;
			}
			assert parameterCount == allArguments.length : methodInvocation.method.getPrimitiveValue().getName();
			Expression callerExpression = methodInvocation.expression.getPrimitiveValue();
			Object caller = this.evaluate( frame, callerExpression );
			Invokable method = methodInvocation.method.getPrimitiveValue();
			if( method.isStatic() ) {
				//pass
			} else {
				this.checkNotNull( caller, "caller is null" );
			}
			return this.invoke( caller, method, allArguments );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( methodInvocation.method.getPrimitiveValue() );
			return null;
		}
	}

	protected Object evaluateNullLiteral( NullLiteral nullLiteral ) {
		return null;
	}

	protected Object evaluateNumberLiteral( NumberLiteral numberLiteral ) {
		return numberLiteral.value.getPrimitiveValue();
	}

	protected Object evaluateDoubleLiteral( DoubleLiteral doubleLiteral ) {
		return doubleLiteral.value.getPrimitiveValue();
	}

	protected Object evaluateFloatLiteral( FloatLiteral floatLiteral ) {
		return floatLiteral.value.getPrimitiveValue();
	}

	protected Object evaluateIntegerLiteral( IntegerLiteral integerLiteral ) {
		return integerLiteral.value.getPrimitiveValue();
	}

	protected Object evaluateParameterAccess( ParameterAccess parameterAccess ) {
		return this.lookup( parameterAccess.parameter.getPrimitiveValue() );
	}

	protected Object evaluateStringLiteral( StringLiteral stringLiteral ) {
		return stringLiteral.value.getPrimitiveValue();
	}

	protected Object evaluateThisExpression( ThisExpression thisExpression ) {
		Object rv = this.getThis();
		assert rv != null;
		return rv;
	}

	protected Object evaluateTypeExpression( TypeExpression typeExpression ) {
		return typeExpression.value.getPrimitiveValue();
	}

	protected Object evaluateTypeLiteral( TypeLiteral typeLiteral ) {
		return typeLiteral.value.getPrimitiveValue();
	}

	protected Object evaluateResourceExpression( ResourceExpression resourceExpression ) {
		return resourceExpression.resource.getPrimitiveValue();
	}*/

	/*protected Object EPIC_HACK_evaluateLambdaExpression( LambdaExpression lambdaExpression, AbstractArgument argument ) {
		Lambda lambda = lambdaExpression.value.getPrimitiveValue();

		AbstractType<?, ?, ?> type = argument.parameter.getPrimitiveValue().getValueType();
		if( type instanceof JavaType ) {

			TweedleObject thisInstance = this.getThis();
			assert thisInstance != null;

			JavaType javaType = (JavaType)type;
			Class<?> interfaceCls = javaType.getClassReflectionProxy().getReification();
			Class<?> adapterCls = this.mapAbstractClsToAdapterCls.get( interfaceCls );
			assert adapterCls != null : interfaceCls;
			Class<?>[] parameterTypes = { org.lgna.project.virtualmachine.LambdaContext.class, Lambda.class, org.lgna.project.virtualmachine.TweedleValue.class };
			Object[] arguments = {
							new LambdaContext() {
								@Override
								public void invokeEntryPoint( Lambda lambda, Invokable singleInvokable, org.lgna.project.virtualmachine.TweedleValue thisInstance, Object... arguments ) {
									assert thisInstance != null;
									if( lambda instanceof UserLambda ) {
										UserLambda userLambda = (UserLambda)lambda;
										java.util.Map<AbstractParameter, Object> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
										for( int i = 0; i < arguments.length; i++ ) {
											map.put( userLambda.requiredParameters.get( i ), arguments[ i ] );
										}
										pushLambdaFrame( thisInstance, userLambda, singleInvokable, map );
										try {
											execute( userLambda.body.getPrimitiveValue() );
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
	}*/

	/*protected Object evaluateLambdaExpression( LambdaExpression lambdaExpression ) {
		throw new RuntimeException( "todo" );
	}*/

	protected TweedleValue evaluate( Frame frame, TweedleExpression expression ) {
//			if( expression instanceof AssignmentExpression ) {
//				rv = this.evaluateAssignmentExpression( (AssignmentExpression)expression );
//			} else if( expression instanceof BooleanLiteral ) {
//				rv = this.evaluateBooleanLiteral( (BooleanLiteral)expression );
//			} else if( expression instanceof InstanceCreation ) {
//				rv = ( (InstanceCreation) expression ).evaluate(this );
//			} else if( expression instanceof ArrayInstanceCreation ) {
//				rv = this.evaluateArrayInstanceCreation( (ArrayInstanceCreation)expression );
//			} else if( expression instanceof ArrayLength ) {
//				rv = this.evaluateArrayLength( (ArrayLength)expression );
//			} else if( expression instanceof ArrayAccess ) {
//				rv = this.evaluateArrayAccess( (ArrayAccess)expression );
//			} else if( expression instanceof FieldAccess ) {
//				rv = this.evaluateFieldAccess( (FieldAccess)expression );
//			} else if( expression instanceof LocalAccess ) {
//				rv = this.evaluateLocalAccess( (LocalAccess)expression );
//			} else if( expression instanceof ArithmeticInfixExpression ) {
//				rv = this.evaluateArithmeticInfixExpression( (ArithmeticInfixExpression)expression );
//			} else if( expression instanceof BitwiseInfixExpression ) {
//				rv = this.evaluateBitwiseInfixExpression( (BitwiseInfixExpression)expression );
//			} else if( expression instanceof ConditionalInfixExpression ) {
//				rv = this.evaluateConditionalInfixExpression( (ConditionalInfixExpression)expression );
//			} else if( expression instanceof RelationalInfixExpression ) {
//				rv = this.evaluateRelationalInfixExpression( (RelationalInfixExpression)expression );
//			} else if( expression instanceof ShiftInfixExpression ) {
//				rv = this.evaluateShiftInfixExpression( (ShiftInfixExpression)expression );
//			} else if( expression instanceof LogicalComplement ) {
//				rv = this.evaluateLogicalComplement( (LogicalComplement)expression );
//			} else if( expression instanceof MethodInvocation ) {
//				rv = this.evaluateMethodInvocation( (MethodInvocation)expression );
//			} else if( expression instanceof NullLiteral ) {
//				rv = this.evaluateNullLiteral( (NullLiteral)expression );
//			} else if( expression instanceof StringConcatenation ) {
//				rv = this.evaluateStringConcatenation( (StringConcatenation)expression );
//			} else if( expression instanceof NumberLiteral ) {
//				rv = this.evaluateNumberLiteral( (NumberLiteral)expression );
//			} else if( expression instanceof DoubleLiteral ) {
//				rv = this.evaluateDoubleLiteral( (DoubleLiteral)expression );
//			} else if( expression instanceof FloatLiteral ) {
//				rv = this.evaluateFloatLiteral( (FloatLiteral)expression );
//			} else if( expression instanceof IntegerLiteral ) {
//				rv = this.evaluateIntegerLiteral( (IntegerLiteral)expression );
//			} else if( expression instanceof ParameterAccess ) {
//				rv = this.evaluateParameterAccess( (ParameterAccess)expression );
//			} else if( expression instanceof StringLiteral ) {
//				rv = this.evaluateStringLiteral( (StringLiteral)expression );
//			} else if( expression instanceof ThisExpression ) {
//				rv = this.evaluateThisExpression( (ThisExpression)expression );
//			} else if( expression instanceof TypeExpression ) {
//				rv = this.evaluateTypeExpression( (TypeExpression)expression );
//			} else if( expression instanceof TypeLiteral ) {
//				rv = this.evaluateTypeLiteral( (TypeLiteral)expression );
//			} else if( expression instanceof ResourceExpression ) {
//				rv = this.evaluateResourceExpression( (ResourceExpression)expression );
//			} else if( expression instanceof LambdaExpression ) {
//				rv = this.evaluateLambdaExpression( (LambdaExpression)expression );
//			} else {
//				throw new RuntimeException( expression.getClass().getName() );
//			}
		return expression.evaluate( frame );
	}

	/* TODO push each to the statement being executed

	protected final <E> E evaluate( Expression expression, Class<E> cls ) {
		//in order to support python...
		//if( result instanceof Integer ) {
		//	condition = ((Integer)result) != 0;
		//} else {
		//	condition = (Boolean)result;
		//}
		Object value = this.evaluate( frame, expression );
		if( cls.isArray() ) {
			if( value instanceof UserArrayInstance ) {
				UserArrayInstance userArrayInstance = (UserArrayInstance)value;
				//todo
				value = userArrayInstance.getValues();
			}
		}
		return cls.cast( value );
	}

	private boolean evaluateBoolean( Expression expression, String nullExceptionMessage ) {
		Object value = this.evaluate( frame, expression );
		this.checkNotNull( value, nullExceptionMessage );
		if( value instanceof Boolean ) {
			return (Boolean)value;
		} else {
			throw new LgnaVmClassCastException( this, Boolean.class, value.getClass() );
		}
	}

	private int evaluateInt( Expression expression, String nullExceptionMessage ) {
		Object value = this.evaluate( frame, expression );
		this.checkNotNull( value, nullExceptionMessage );
		if( value instanceof Integer ) {
			return (Integer)value;
		} else {
			throw new LgnaVmClassCastException( this, Integer.class, value.getClass() );
		}
	}

	protected void executeAssertStatement( AssertStatement assertStatement, VirtualMachineListener[] listeners ) {
		assert this.evaluateBoolean( assertStatement.expression.getPrimitiveValue(), "assert condition is null" ) : this.evaluate(
						frame, assertStatement.message.getPrimitiveValue() );
	}

	protected void executeBlockStatement( BlockStatement blockStatement, VirtualMachineListener[] listeners ) throws ReturnException {
		//todo?
		Statement[] array = new Statement[ blockStatement.statements.size() ];
		blockStatement.statements.toArray( array );
		for( Statement statement : array ) {
			this.execute( statement );
		}
	}

	protected void executeConditionalStatement( ConditionalStatement conditionalStatement, VirtualMachineListener[] listeners ) throws ReturnException {
		for( BooleanExpressionBodyPair booleanExpressionBodyPair : conditionalStatement.booleanExpressionBodyPairs ) {
			if( this.evaluateBoolean( booleanExpressionBodyPair.expression.getPrimitiveValue(), "if condition is null" ) ) {
				this.execute( booleanExpressionBodyPair.body.getPrimitiveValue() );
				return;
			}
		}
		this.execute( conditionalStatement.elseBody.getPrimitiveValue() );
	}

	protected void executeComment( Comment comment, VirtualMachineListener[] listeners ) {
	}

	protected void executeCountLoop( CountLoop countLoop, VirtualMachineListener[] listeners ) throws ReturnException {
		UserLocal variable = countLoop.variable.getPrimitiveValue();
		UserLocal constant = countLoop.constant.getPrimitiveValue();
		this.pushLocal( variable, -1 );
		try {
			final int n = this.evaluateInt( countLoop.count.getPrimitiveValue(), "count expression is null" );
			this.pushLocal( constant, n );
			try {
				for( int i = 0; i < n; i++ ) {
					CountLoopIterationEvent countLoopIterationEvent;
					if( listeners != null ) {
						countLoopIterationEvent = new CountLoopIterationEvent( this, countLoop, i, n );
						for( VirtualMachineListener virtualMachineListener : listeners ) {
							virtualMachineListener.countLoopIterating( countLoopIterationEvent );
						}
					} else {
						countLoopIterationEvent = null;
					}
					this.setLocal( variable, i );
					this.execute( countLoop.body.getPrimitiveValue() );
					if( listeners != null ) {
						for( VirtualMachineListener virtualMachineListener : listeners ) {
							virtualMachineListener.countLoopIterated( countLoopIterationEvent );
						}
					}
				}
			} finally {
				this.popLocal( constant );
			}
		} finally {
			this.popLocal( variable );
		}
	}

	protected void executeDoInOrder( DoInOrder doInOrder, VirtualMachineListener[] listeners ) throws ReturnException {
		execute( doInOrder.body.getPrimitiveValue() );
	}

	protected void executeDoTogether( DoTogether doTogether, VirtualMachineListener[] listeners ) throws ReturnException {
		BlockStatement blockStatement = doTogether.body.getPrimitiveValue();
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
				final Statement statementI = blockStatement.statements.get( i );
				runnables[ i ] = new Runnable() {
					;
					@Override
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

	protected void executeExpressionStatement( ExpressionStatement expressionStatement, VirtualMachineListener[] listeners ) {
		@SuppressWarnings( "unused" ) Object unused = this.evaluate( frame, expressionStatement.expression.getPrimitiveValue() );
	}

	protected void excecuteForEachLoop( AbstractForEachLoop forEachInLoop, Object[] array, VirtualMachineListener[] listeners ) throws ReturnException {
		UserLocal item = forEachInLoop.item.getPrimitiveValue();
		BlockStatement blockStatement = forEachInLoop.body.getPrimitiveValue();
		this.pushLocal( item, -1 );
		try {
			int index = 0;
			for( Object o : array ) {
				ForEachLoopIterationEvent forEachLoopIterationEvent;
				if( listeners != null ) {
					forEachLoopIterationEvent = new ForEachLoopIterationEvent( this, forEachInLoop, o, array, index );
					for( VirtualMachineListener virtualMachineListener : listeners ) {
						virtualMachineListener.forEachLoopIterating( forEachLoopIterationEvent );
					}
				} else {
					forEachLoopIterationEvent = null;
				}

				this.setLocal( item, o );
				this.execute( blockStatement );
				if( listeners != null ) {
					for( VirtualMachineListener virtualMachineListener : listeners ) {
						virtualMachineListener.forEachLoopIterated( forEachLoopIterationEvent );
					}
				}
				index++;
			}
		} finally {
			this.popLocal( item );
		}
	}

	protected final void executeForEachInArrayLoop( ForEachInArrayLoop forEachInArrayLoop, VirtualMachineListener[] listeners ) throws ReturnException {
		Object[] array = this.evaluate( forEachInArrayLoop.array.getPrimitiveValue(), Object[].class );
		this.checkNotNull( array, "for each array is null" );
		excecuteForEachLoop( forEachInArrayLoop, array, listeners );
	}

	protected final void executeForEachInIterableLoop( ForEachInIterableLoop forEachInIterableLoop, VirtualMachineListener[] listeners ) throws ReturnException {
		Iterable<?> iterable = this.evaluate( forEachInIterableLoop.iterable.getPrimitiveValue(), Iterable.class );
		this.checkNotNull( iterable, "for each iterable is null" );
		excecuteForEachLoop( forEachInIterableLoop, edu.cmu.cs.dennisc.java.lang.IterableUtilities.toArray( iterable ), listeners );
	}

	protected void excecuteEachInTogether( final AbstractEachInTogether eachInTogether, final Object[] array, final VirtualMachineListener[] listeners ) throws ReturnException {
		final UserLocal item = eachInTogether.item.getPrimitiveValue();
		final BlockStatement blockStatement = eachInTogether.body.getPrimitiveValue();

		switch( array.length ) {
		case 0:
			break;
		case 1:
			Object value = array[ 0 ];
			VirtualMachine.this.pushLocal( item, value );
			try {
				EachInTogetherItemEvent eachInTogetherEvent;
				if( listeners != null ) {
					eachInTogetherEvent = new EachInTogetherItemEvent( this, eachInTogether, value, array );
					for( VirtualMachineListener virtualMachineListener : listeners ) {
						virtualMachineListener.eachInTogetherItemExecuting( eachInTogetherEvent );
					}
				} else {
					eachInTogetherEvent = null;
				}
				VirtualMachine.this.execute( blockStatement );
				if( listeners != null ) {
					for( VirtualMachineListener virtualMachineListener : listeners ) {
						virtualMachineListener.eachInTogetherItemExecuted( eachInTogetherEvent );
					}
				}
			} finally {
				VirtualMachine.this.popLocal( item );
			}
			break;
		default:
			final Frame owner = this.getFrameForThread( Thread.currentThread() );
			org.lgna.common.ThreadUtilities.eachInTogether( new org.lgna.common.EachInTogetherRunnable<Object>() {
				@Override
				public void run( Object value ) {
					pushCurrentThread( owner );
					try {
						VirtualMachine.this.pushLocal( item, value );
						try {
							EachInTogetherItemEvent eachInTogetherEvent;
							if( listeners != null ) {
								eachInTogetherEvent = new EachInTogetherItemEvent( VirtualMachine.this, eachInTogether, value, array );
								for( VirtualMachineListener virtualMachineListener : listeners ) {
									virtualMachineListener.eachInTogetherItemExecuting( eachInTogetherEvent );
								}
							} else {
								eachInTogetherEvent = null;
							}
							VirtualMachine.this.execute( blockStatement );
							if( listeners != null ) {
								for( VirtualMachineListener virtualMachineListener : listeners ) {
									virtualMachineListener.eachInTogetherItemExecuted( eachInTogetherEvent );
								}
							}
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

	protected final void executeEachInArrayTogether( EachInArrayTogether eachInArrayTogether, VirtualMachineListener[] listeners ) throws ReturnException {
		Object[] array = this.evaluate( eachInArrayTogether.array.getPrimitiveValue(), Object[].class );
		this.checkNotNull( array, "each in together array is null" );
		excecuteEachInTogether( eachInArrayTogether, array, listeners );
	}

	protected final void executeEachInIterableTogether( EachInIterableTogether eachInIterableTogether, VirtualMachineListener[] listeners ) throws ReturnException {
		Iterable<?> iterable = this.evaluate( eachInIterableTogether.iterable.getPrimitiveValue(), Iterable.class );
		this.checkNotNull( iterable, "each in together iterable is null" );
		excecuteEachInTogether( eachInIterableTogether, edu.cmu.cs.dennisc.java.lang.IterableUtilities.toArray( iterable ), listeners );
	}

	protected void executeReturnStatement( ReturnStatement returnStatement, VirtualMachineListener[] listeners ) throws ReturnException {
		Object returnValue = this.evaluate( frame, returnStatement.expression.getPrimitiveValue() );
		//setReturnValue( returnValue );
		throw new ReturnException( returnValue );
	}

	protected void executeWhileLoop( WhileLoop whileLoop, VirtualMachineListener[] listeners ) throws ReturnException {
		int i = 0;
		while( this.evaluateBoolean( whileLoop.conditional.getPrimitiveValue(), "while condition is null" ) ) {
			WhileLoopIterationEvent whileLoopIterationEvent;
			if( listeners != null ) {
				whileLoopIterationEvent = new WhileLoopIterationEvent( this, whileLoop, i );
				for( VirtualMachineListener virtualMachineListener : listeners ) {
					virtualMachineListener.whileLoopIterating( whileLoopIterationEvent );
				}
			} else {
				whileLoopIterationEvent = null;
			}
			this.execute( whileLoop.body.getPrimitiveValue() );
			if( listeners != null ) {
				for( VirtualMachineListener virtualMachineListener : listeners ) {
					virtualMachineListener.whileLoopIterated( whileLoopIterationEvent );
				}
			}
			i++;
		}
	}

	protected void executeLocalDeclarationStatement( LocalDeclarationStatement localDeclarationStatement, VirtualMachineListener[] listeners ) {
		this.pushLocal( localDeclarationStatement.local.getPrimitiveValue(), this.evaluate( frame, localDeclarationStatement.initializer.getPrimitiveValue() ) );
		//handle pop on exit of owning block statement
	}*/

	protected void execute( Frame frame, TweedleStatement statement ) {
		if( statement.isEnabled() ) {
			statement.execute(frame);

/*			try {
				if( statement instanceof AssertStatement ) {
					this.executeAssertStatement( (AssertStatement)statement, listeners );
				} else if( statement instanceof BlockStatement) {
					this.executeBlockStatement( (BlockStatement)statement, listeners );
				} else if( statement instanceof ConditionalStatement ) {
					this.executeConditionalStatement( (ConditionalStatement)statement, listeners );
				} else if( statement instanceof Comment ) {
					this.executeComment( (Comment)statement, listeners );
				} else if( statement instanceof CountLoop ) {
					this.executeCountLoop( (CountLoop)statement, listeners );
				} else if( statement instanceof DoTogether ) {
					this.executeDoTogether( (DoTogether)statement, listeners );
				} else if( statement instanceof DoInOrder ) {
					this.executeDoInOrder( (DoInOrder)statement, listeners );
				} else if( statement instanceof ExpressionStatement ) {
					this.executeExpressionStatement( (ExpressionStatement)statement, listeners );
				} else if( statement instanceof ForEachInArrayLoop ) {
					this.executeForEachInArrayLoop( (ForEachInArrayLoop)statement, listeners );
				} else if( statement instanceof ForEachInIterableLoop ) {
					this.executeForEachInIterableLoop( (ForEachInIterableLoop)statement, listeners );
				} else if( statement instanceof EachInArrayTogether ) {
					this.executeEachInArrayTogether( (EachInArrayTogether)statement, listeners );
				} else if( statement instanceof EachInIterableTogether ) {
					this.executeEachInIterableTogether( (EachInIterableTogether)statement, listeners );
				} else if( statement instanceof WhileLoop ) {
					this.executeWhileLoop( (WhileLoop)statement, listeners );
				} else if( statement instanceof LocalDeclarationStatement ) {
					this.executeLocalDeclarationStatement( (LocalDeclarationStatement)statement, listeners );
				} else if( statement instanceof ReturnStatement ) {
					this.executeReturnStatement( (ReturnStatement)statement, listeners );
					// note: does not return.  throws ReturnException.
				} else {
					throw new RuntimeException();
				}
			} finally {*/
//			}
		}
	}
}
