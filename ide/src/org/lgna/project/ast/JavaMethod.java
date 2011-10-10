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
public class JavaMethod extends AbstractMethod {
	private static java.util.Map< MethodReflectionProxy, JavaMethod > s_mapReflectionProxyToJava = new java.util.HashMap< MethodReflectionProxy, JavaMethod >();

	private MethodReflectionProxy methodReflectionProxy;
	private java.util.ArrayList< JavaMethodParameter > parameters;

	public static JavaMethod getInstance( MethodReflectionProxy methodReflectionProxy ) {
		if( methodReflectionProxy != null ) {
			JavaMethod rv = s_mapReflectionProxyToJava.get( methodReflectionProxy );
			if( rv != null ) {
				//pass
			} else {
				rv = new JavaMethod( methodReflectionProxy );
				s_mapReflectionProxyToJava.put( methodReflectionProxy, rv );
			}
			return rv;
		} else {
			return null;
		}
	}
	public static JavaMethod getInstance( java.lang.reflect.Method mthd ) {
		return getInstance( new MethodReflectionProxy( mthd ) );
	}
	public static JavaMethod getInstance( Class< ? > declaringCls, String name, Class< ? >... parameterClses ) {
		return getInstance( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getDeclaredMethod( declaringCls, name, parameterClses ) );
	}

	private JavaMethod( MethodReflectionProxy methodReflectionProxy ) {
		this.methodReflectionProxy = methodReflectionProxy;
		ClassReflectionProxy[] classReflectionProxies = this.methodReflectionProxy.getParameterClassReflectionProxies();
		this.parameters = new java.util.ArrayList< JavaMethodParameter >();
		this.parameters.ensureCapacity( classReflectionProxies.length );
		java.lang.annotation.Annotation[][] parameterAnnotations = this.methodReflectionProxy.getParameterAnnotations();
		for( int i = 0; i < classReflectionProxies.length; i++ ) {
			this.parameters.add( new JavaMethodParameter( this, i, parameterAnnotations[ i ] ) );
		}
	}

	public MethodReflectionProxy getMethodReflectionProxy() {
		return this.methodReflectionProxy;
	}

	@Override
	public boolean isValid() {
		return this.methodReflectionProxy.getReification() != null;
	}
	@Override
	public String getName() {
		return this.methodReflectionProxy.getName();
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return null;
	}

	@Override
	public JavaType getReturnType() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getReification();
		if( mthd != null ) {
			return JavaType.getInstance( mthd.getReturnType() );
		} else {
			return null;
		}
	}
	@Override
	public java.util.ArrayList< ? extends AbstractParameter > getParameters() {
		return this.parameters;
	}

	@Override
	public JavaType getDeclaringType() {
		return JavaType.getInstance( this.methodReflectionProxy.getDeclaringClassReflectionProxy() );
	}
	@Override
	public org.lgna.project.annotations.Visibility getVisibility() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getReification();
		if( mthd != null ) {
			if( mthd.isAnnotationPresent( org.lgna.project.annotations.MethodTemplate.class ) ) {
				org.lgna.project.annotations.MethodTemplate mthdTemplate = mthd.getAnnotation( org.lgna.project.annotations.MethodTemplate.class );
				return mthdTemplate.visibility();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public boolean isParameterInShortestChainedMethod( JavaMethodParameter parameterDeclaredInJavaMethod ) {
		int index = parameterDeclaredInJavaMethod.getIndex();
		JavaMethod methodDeclaredInJava = (JavaMethod)getShortestInChain();
		return index < methodDeclaredInJava.getParameters().size();
	}

	private JavaMethod nextLongerInChain = null;
	@Override
	public JavaMethod getNextLongerInChain() {
		return this.nextLongerInChain;
	}
	/*package-private*/ void setNextLongerInChain( JavaMethod nextLongerInChain ) {
		this.nextLongerInChain = nextLongerInChain;
	}

	private JavaMethod nextShorterInChain = null;
	@Override
	public JavaMethod getNextShorterInChain() {
		return this.nextShorterInChain;
	}
	/*package-private*/ void setNextShorterInChain( JavaMethod nextShorterInChain ) {
		this.nextShorterInChain = nextShorterInChain;
	}

	@Override
	public Access getAccess() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getReification();
		assert mthd != null;
		return Access.get( mthd.getModifiers() );
	}
	@Override
	public boolean isStatic() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getReification();
		assert mthd != null;
		return java.lang.reflect.Modifier.isStatic( mthd.getModifiers() );
	}
	@Override
	public boolean isAbstract() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getReification();
		assert mthd != null;
		return java.lang.reflect.Modifier.isAbstract( mthd.getModifiers() );
	}
	@Override
	public boolean isFinal() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getReification();
		assert mthd != null;
		return java.lang.reflect.Modifier.isFinal( mthd.getModifiers() );
	}
	@Override
	public boolean isNative() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getReification();
		assert mthd != null;
		return java.lang.reflect.Modifier.isNative( mthd.getModifiers() );
	}
	@Override
	public boolean isSynchronized() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getReification();
		assert mthd != null;
		return java.lang.reflect.Modifier.isSynchronized( mthd.getModifiers() );
	}
	@Override
	public boolean isStrictFloatingPoint() {
		java.lang.reflect.Method mthd = this.methodReflectionProxy.getReification();
		assert mthd != null;
		return java.lang.reflect.Modifier.isStrict( mthd.getModifiers() );
	}

	@Override
	public boolean isEquivalentTo( Object o ) {
		JavaMethod other = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( o, JavaMethod.class );
		if( other != null ) {
			return this.methodReflectionProxy.equals( other.methodReflectionProxy );
		} else {
			return false;
		}
	}
}
