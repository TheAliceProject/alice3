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
public abstract class InvocableReflectionProxy<E> extends MemberReflectionProxy<E> {
	protected ClassReflectionProxy[] parameterClassReflectionProxies;

	public InvocableReflectionProxy( ClassReflectionProxy declaringClassReflectionProxy, ClassReflectionProxy[] parameterClassReflectionProxies ) {
		super( declaringClassReflectionProxy );
		this.parameterClassReflectionProxies = parameterClassReflectionProxies;
	}

	public InvocableReflectionProxy( E e, Class<?> declaringCls, Class<?>[] parameterClses ) {
		super( e, declaringCls );
		this.parameterClassReflectionProxies = ClassReflectionProxy.create( parameterClses );
	}

	@Override
	protected int hashCodeNonReifiable() {
		int rv = super.hashCodeNonReifiable();
		for( ClassReflectionProxy parameterClassReflectionProxy : parameterClassReflectionProxies ) {
			rv = ( 37 * rv ) + parameterClassReflectionProxy.hashCode();
		}
		return rv;
	}

	@Override
	protected boolean equalsInstanceOfSameClassButNonReifiable( org.lgna.project.ast.ReflectionProxy<?> o ) {
		if( super.equalsInstanceOfSameClassButNonReifiable( o ) ) {
			InvocableReflectionProxy<E> other = (InvocableReflectionProxy<E>)o;
			if( this.parameterClassReflectionProxies.length == other.parameterClassReflectionProxies.length ) {
				for( int i = 0; i < this.parameterClassReflectionProxies.length; i++ ) {
					if( edu.cmu.cs.dennisc.java.util.Objects.equals( this.parameterClassReflectionProxies[ i ], other.parameterClassReflectionProxies[ i ] ) ) {
						//pass
					} else {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public ClassReflectionProxy[] getParameterClassReflectionProxies() {
		return this.parameterClassReflectionProxies;
	}

	protected abstract java.lang.annotation.Annotation[][] getReifiedParameterAnnotations();

	public final java.lang.annotation.Annotation[][] getParameterAnnotations() {
		java.lang.annotation.Annotation[][] rv = this.getReifiedParameterAnnotations();
		if( rv != null ) {
			//pass
		} else {
			rv = new java.lang.annotation.Annotation[ this.parameterClassReflectionProxies.length ][];
		}
		return rv;
	}
}
