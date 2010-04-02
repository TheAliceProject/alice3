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

package edu.cmu.cs.dennisc.alice.ast;

//todo: add generic capability?
/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractType extends AbstractAccessibleDeclaration {
	private static java.util.Map< Class<?>, Class<?>> s_mapPrimitiveToWrapper;
	static {
		s_mapPrimitiveToWrapper = new java.util.HashMap< Class<?>, Class<?>>();
		s_mapPrimitiveToWrapper.put( java.lang.Void.TYPE, java.lang.Void.class );
		s_mapPrimitiveToWrapper.put( java.lang.Boolean.TYPE, java.lang.Boolean.class );
		s_mapPrimitiveToWrapper.put( java.lang.Byte.TYPE, java.lang.Byte.class );
		s_mapPrimitiveToWrapper.put( java.lang.Character.TYPE, java.lang.Character.class );
		s_mapPrimitiveToWrapper.put( java.lang.Short.TYPE, java.lang.Short.class );
		s_mapPrimitiveToWrapper.put( java.lang.Integer.TYPE, java.lang.Integer.class );
		s_mapPrimitiveToWrapper.put( java.lang.Long.TYPE, java.lang.Long.class );
		s_mapPrimitiveToWrapper.put( java.lang.Float.TYPE, java.lang.Float.class );
		s_mapPrimitiveToWrapper.put( java.lang.Double.TYPE, java.lang.Double.class );
	}
	private static Class<?> getClsWrapperIfNecessary( TypeDeclaredInJava typeDeclaredInJava ) { 
		Class<?> rv = typeDeclaredInJava.getClassReflectionProxy().getReification();
		assert rv != null : typeDeclaredInJava;
		if( rv.isPrimitive() ) {
			rv = s_mapPrimitiveToWrapper.get( rv );
			assert rv != null;
		}
		return rv;
	}
	
	public TypeDeclaredInJava getFirstTypeEncounteredDeclaredInJava() {
		AbstractType type = this; 
		while( type instanceof TypeDeclaredInJava == false ) {
			type = type.getSuperType();
		}
		return (TypeDeclaredInJava)type;
	}
//	public Class<?> getFirstClassEncounteredDeclaredInJava() {
//		return getFirstTypeEncounteredDeclaredInJava().getCls();
//	}
	public boolean isAssignableFrom( AbstractType other ) {
		if( other != null ) {
			TypeDeclaredInJava thisTypeDeclaredInJava = this.getFirstTypeEncounteredDeclaredInJava();
			TypeDeclaredInJava otherTypeDeclaredInJava = other.getFirstTypeEncounteredDeclaredInJava();
			return getClsWrapperIfNecessary( thisTypeDeclaredInJava ).isAssignableFrom( getClsWrapperIfNecessary( otherTypeDeclaredInJava ) );
		} else {
			//todo?
			return false;
		}
	}
	public boolean isAssignableFrom( Class<?> other ) {
		return isAssignableFrom( TypeDeclaredInJava.get( other ) );
	}
	public boolean isAssignableTo( AbstractType other ) {
		return other.isAssignableFrom( this );
	}
	public boolean isAssignableTo( Class<?> other ) {
		return isAssignableTo( TypeDeclaredInJava.get( other ) );
	}

	public abstract boolean isFollowToSuperClassDesired();
	public abstract boolean isConsumptionBySubClassDesired();
	public abstract AbstractPackage getPackage();
	public abstract AbstractType getSuperType();
	public abstract java.util.ArrayList< ? extends AbstractConstructor > getDeclaredConstructors();
	public abstract java.util.ArrayList< ? extends AbstractMethod > getDeclaredMethods();
	public abstract java.util.ArrayList< ? extends AbstractField > getDeclaredFields();

	public abstract boolean isInterface();
	public abstract boolean isStatic();
	//cannot be final and abstract
	public abstract boolean isAbstract();
	public abstract boolean isFinal();
	public abstract boolean isStrictFloatingPoint();
	
	public abstract boolean isArray();
	public abstract AbstractType getComponentType();
	
	public AbstractConstructor getDeclaredConstructor() {
		return getDeclaredConstructor( new AbstractType[] {} );
	}
	public AbstractConstructor getDeclaredConstructor( AbstractType... parameterTypes ) {
		AbstractConstructor rv = null;
		for( AbstractConstructor constructor : getDeclaredConstructors() ) {
			rv = constructor;
			java.util.ArrayList< ? extends AbstractParameter > parameters = constructor.getParameters();
			for( int i=0; i<parameterTypes.length; i++ ) {
				AbstractType parameterType = parameterTypes[ i ];
				if( parameterType.equals( parameters.get( i ).getValueType() ) ) {
					//pass
				} else {
					rv = null;
					break;
				}
			}
			if( rv != null ) {
				break;
			}
		}
		return rv;
	}
	public AbstractConstructor getDeclaredConstructor( Class<?>... parameterClses ) {
		return getDeclaredConstructor( TypeDeclaredInJava.get( parameterClses ) );
	}
	public AbstractMethod getDeclaredMethod( String name ) {
		return getDeclaredMethod( name, new AbstractType[] {} );
	}
	public AbstractMethod getDeclaredMethod( String name, AbstractType... parameterTypes ) {
		AbstractMethod rv = null;
		for( AbstractMethod method : getDeclaredMethods() ) {
			if( method.getName().equals( name ) ) {
				java.util.ArrayList< ? extends AbstractParameter > parameters = method.getParameters();
				if( parameters.size() == parameterTypes.length ) {
					rv = method;
					for( int i=0; i<parameterTypes.length; i++ ) {
						AbstractType parameterType = parameterTypes[ i ];
						if( parameterType.equals( parameters.get( i ).getValueType() ) ) {
							//pass
						} else {
							rv = null;
							break;
						}
					}
				}
				if( rv != null ) {
					break;
				}
			} else {
				rv = null;
			}
		}
		return rv;
	}
	public AbstractMethod getDeclaredMethod( String name, Class<?>... parameterClses ) {
		return getDeclaredMethod( name, TypeDeclaredInJava.get( parameterClses ) );
	}

	public AbstractField getDeclaredField( AbstractType valueType, String name ) {
		AbstractField rv = null;
		for( AbstractField field : getDeclaredFields() ) {
			if( field.getName().equals( name ) && field.getValueType().equals( valueType ) ) {
				rv = field;
				break;
			} else {
				rv = null;
			}
		}
		return rv;
	}
	public AbstractField getDeclaredField( Class<?> valueCls, String name ) {
		return getDeclaredField( TypeDeclaredInJava.get( valueCls ), name );
	}
	
	public abstract AbstractType getArrayType();
	
	public AbstractMethod findMethod( String name, AbstractType... parameterTypes ) {
		AbstractMethod rv = null;
		AbstractType type = this;
		while( type != null ) {
			AbstractMethod method = type.getDeclaredMethod( name, parameterTypes );
			if( method != null ) {
				rv = method;
				break;
			}
			type = type.getSuperType();
		}
		return rv;
//		//todo: this will need to be udpated when you can inherit from other TypesDeclaredInAlice
//		TypeDeclaredInJava typeDeclaredInJava = this.getDeclaringType().getFirstTypeEncounteredDeclaredInJava();
//		Class<?> clsDeclaredInJava = typeDeclaredInJava.getCls();
//		Class<?>[] parameterClses = new Class< ? >[ this.parameters.size() ];
//		int i = 0;
//		for( AbstractParameter parameter : this.parameters ) {
//			if( parameter instanceof ParameterDeclaredInJava ) {
//				ParameterDeclaredInJava parameterDeclaredInJava = (ParameterDeclaredInJava)parameter;
//				parameterClses[ i ] = parameterDeclaredInJava.getValueTypeDeclaredInJava().getCls();
//			} else {
//				return false;
//			}
//			i++;
//		}
//		java.lang.reflect.Method mthd = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getMethod( clsDeclaredInJava, this.getName(), parameterClses );
//		return mthd != null;
	}
}
