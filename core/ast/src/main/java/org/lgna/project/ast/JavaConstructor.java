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

/**
 * @author Dennis Cosgrove
 */
public class JavaConstructor extends AbstractConstructor {
	private static final edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<ConstructorReflectionProxy, JavaConstructor> mapReflectionProxyToInstance = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();

	public static JavaConstructor getInstance( ConstructorReflectionProxy constructorReflectionProxy ) {
		if( constructorReflectionProxy != null ) {
			return mapReflectionProxyToInstance.getInitializingIfAbsent( constructorReflectionProxy, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<ConstructorReflectionProxy, JavaConstructor>() {
				@Override
				public org.lgna.project.ast.JavaConstructor initialize( org.lgna.project.ast.ConstructorReflectionProxy key ) {
					return new JavaConstructor( key );
				}
			} );
		} else {
			return null;
		}
	}

	public static JavaConstructor getInstance( java.lang.reflect.Constructor<?> cnstrctr ) {
		return getInstance( new ConstructorReflectionProxy( cnstrctr ) );
	}

	public static JavaConstructor getInstance( Class<?> declaringCls, Class<?>... parameterClses ) {
		return getInstance( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( declaringCls, parameterClses ) );
	}

	private JavaConstructor( ConstructorReflectionProxy constructorReflectionProxy ) {
		this.constructorReflectionProxy = constructorReflectionProxy;
		ClassReflectionProxy[] parameterTypeReflectionProxies = this.constructorReflectionProxy.getParameterClassReflectionProxies();
		final int N;
		if( this.constructorReflectionProxy.isVarArgs() ) {
			N = parameterTypeReflectionProxies.length - 1;
		} else {
			N = parameterTypeReflectionProxies.length;
		}
		java.util.ArrayList<JavaConstructorParameter> list = edu.cmu.cs.dennisc.java.util.Lists.newArrayListWithInitialCapacity( N );
		java.lang.annotation.Annotation[][] parameterAnnotations = this.constructorReflectionProxy.getParameterAnnotations();
		for( int i = 0; i < N; i++ ) {
			list.add( new JavaConstructorParameter( this, i, parameterAnnotations[ i ] ) );
		}
		this.requiredParameters = java.util.Collections.unmodifiableList( list );
		if( this.constructorReflectionProxy.isVarArgs() ) {
			this.variableOrKeyedParameter = new JavaConstructorParameter( this, N, parameterAnnotations[ N ] );
		} else {
			this.variableOrKeyedParameter = null;
		}
	}

	public ConstructorReflectionProxy getConstructorReflectionProxy() {
		return this.constructorReflectionProxy;
	}

	@Override
	public JavaType getDeclaringType() {
		return JavaType.getInstance( this.constructorReflectionProxy.getDeclaringClassReflectionProxy() );
	}

	@Override
	public JavaConstructorParameter getKeyedParameter() {
		if( this.variableOrKeyedParameter != null ) {
			if( variableOrKeyedParameter.getValueType().getComponentType().getKeywordFactoryType() != null ) {
				return this.variableOrKeyedParameter;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public JavaConstructorParameter getVariableLengthParameter() {
		if( this.variableOrKeyedParameter != null ) {
			if( variableOrKeyedParameter.getValueType().getComponentType().getKeywordFactoryType() != null ) {
				return null;
			} else {
				return this.variableOrKeyedParameter;
			}
		} else {
			return null;
		}
	}

	@Override
	public java.util.List<JavaConstructorParameter> getRequiredParameters() {
		return this.requiredParameters;
	}

	@Override
	public org.lgna.project.annotations.Visibility getVisibility() {
		java.lang.reflect.Constructor<?> cnstrctr = this.constructorReflectionProxy.getReification();
		if( cnstrctr != null ) {
			if( cnstrctr.isAnnotationPresent( org.lgna.project.annotations.ConstructorTemplate.class ) ) {
				//todo: investigate cast requirement
				org.lgna.project.annotations.ConstructorTemplate cnstrctrTemplate = cnstrctr.getAnnotation( org.lgna.project.annotations.ConstructorTemplate.class );
				return cnstrctrTemplate.visibility();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private JavaConstructor nextLongerInChain = null;

	public boolean isParameterInShortestChainedConstructor( JavaConstructorParameter javaConstructorParameter ) {
		int index = javaConstructorParameter.getIndex();
		JavaConstructor javaConstructor = (JavaConstructor)getShortestInChain();
		return index < javaConstructor.getRequiredParameters().size();
	}

	@Override
	public JavaConstructor getNextLongerInChain() {
		return this.nextLongerInChain;
	}

	public void setNextLongerInChain( JavaConstructor nextLongerInChain ) {
		this.nextLongerInChain = nextLongerInChain;
	}

	private JavaConstructor nextShorterInChain = null;

	@Override
	public JavaConstructor getNextShorterInChain() {
		return this.nextShorterInChain;
	}

	public void setNextShorterInChain( JavaConstructor nextShorterInChain ) {
		this.nextShorterInChain = nextShorterInChain;
	}

	@Override
	public boolean isSignatureLocked() {
		return true;
	}

	@Override
	public AccessLevel getAccessLevel() {
		java.lang.reflect.Constructor<?> cnstrctr = this.constructorReflectionProxy.getReification();
		if( cnstrctr != null ) {
			return AccessLevel.getValueFromModifiers( cnstrctr.getModifiers() );
		} else {
			return null;
		}
	}

	@Override
	public boolean isEquivalentTo( Object o ) {
		JavaConstructor other = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( o, JavaConstructor.class );
		if( other != null ) {
			return this.constructorReflectionProxy.equals( other.constructorReflectionProxy );
		} else {
			return false;
		}
	}

	@Override
	public boolean isUserAuthored() {
		return false;
	}

	private final ConstructorReflectionProxy constructorReflectionProxy;
	private final java.util.List<JavaConstructorParameter> requiredParameters;
	private final JavaConstructorParameter variableOrKeyedParameter;
}
