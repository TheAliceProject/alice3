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
public class ConstructorDeclaredInJava extends AbstractConstructor {
	private static java.util.Map< ConstructorReflectionProxy, ConstructorDeclaredInJava > s_map = new java.util.HashMap< ConstructorReflectionProxy, ConstructorDeclaredInJava >();

	private ConstructorReflectionProxy constructorReflectionProxy;
	private java.util.ArrayList< ParameterDeclaredInJavaConstructor > parameters;

	public static ConstructorDeclaredInJava get( ConstructorReflectionProxy constructorReflectionProxy ) {
		if( constructorReflectionProxy != null ) {
			ConstructorDeclaredInJava rv = s_map.get( constructorReflectionProxy );
			if( rv != null ) {
				//pass
			} else {
				rv = new ConstructorDeclaredInJava( constructorReflectionProxy );
				s_map.put( constructorReflectionProxy, rv );
			}
			return rv;
		} else {
			return null;
		}
	}
	public static ConstructorDeclaredInJava get( java.lang.reflect.Constructor< ? > cnstrctr ) {
		return get( new ConstructorReflectionProxy( cnstrctr ) );
	}
	public static ConstructorDeclaredInJava get( Class<?> declaringCls, Class<?>... parameterClses ) {
		return get( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( declaringCls, parameterClses ) );
	}
	
	private ConstructorDeclaredInJava( ConstructorReflectionProxy constructorReflectionProxy ) {
		this.constructorReflectionProxy = constructorReflectionProxy;
		ClassReflectionProxy[] classReflectionProxies = this.constructorReflectionProxy.getParameterClassReflectionProxies();
		this.parameters = new java.util.ArrayList< ParameterDeclaredInJavaConstructor >();
		this.parameters.ensureCapacity( classReflectionProxies.length );
		java.lang.annotation.Annotation[][] parameterAnnotations = this.constructorReflectionProxy.getParameterAnnotations();
		for( int i = 0; i < classReflectionProxies.length; i++ ) {
			this.parameters.add( new ParameterDeclaredInJavaConstructor( this, i, parameterAnnotations[ i ] ) );
		}
	}
	
	public ConstructorReflectionProxy getConstructorReflectionProxy() {
		return this.constructorReflectionProxy;
	}
	
	@Override
	public TypeDeclaredInJava getDeclaringType() {
		return TypeDeclaredInJava.get( this.constructorReflectionProxy.getDeclaringClassReflectionProxy() );
	}
	@Override
	public java.util.ArrayList< ? extends AbstractParameter > getParameters() {
		return this.parameters;
	}
	@Override
	public org.lgna.project.annotations.Visibility getVisibility() {
		java.lang.reflect.Constructor< ? > cnstrctr = this.constructorReflectionProxy.getReification();
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

	private ConstructorDeclaredInJava nextLongerInChain = null;

	public boolean isParameterInShortestChainedConstructor( ParameterDeclaredInJavaConstructor parameterDeclaredInJavaConstructor ) {
		int index = parameterDeclaredInJavaConstructor.getIndex();
		ConstructorDeclaredInJava constructorDeclaredInJava = (ConstructorDeclaredInJava)getShortestInChain();
		return index < constructorDeclaredInJava.getParameters().size();
	}
	
	@Override
	public AbstractMember getNextLongerInChain() {
		return this.nextLongerInChain;
	}
	public void setNextLongerInChain( ConstructorDeclaredInJava nextLongerInChain ) {
		this.nextLongerInChain = nextLongerInChain;
	}

	private ConstructorDeclaredInJava nextShorterInChain = null;

	@Override
	public AbstractMember getNextShorterInChain() {
		return this.nextShorterInChain;
	}
	public void setNextShorterInChain( ConstructorDeclaredInJava nextShorterInChain ) {
		this.nextShorterInChain = nextShorterInChain;
	}

	@Override
	public Access getAccess() {
		java.lang.reflect.Constructor< ? > cnstrctr = this.constructorReflectionProxy.getReification();
		if( cnstrctr != null ) {
			return Access.get( cnstrctr.getModifiers() );
		} else {
			return null;
		}
	}

	@Override
	public boolean isEquivalentTo( Object o ) {
		ConstructorDeclaredInJava other = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( o, ConstructorDeclaredInJava.class );
		if( other != null ) {
			return this.constructorReflectionProxy.equals( other.constructorReflectionProxy );
		} else {
			return false;
		}
	}
}
