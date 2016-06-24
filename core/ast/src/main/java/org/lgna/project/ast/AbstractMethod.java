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
public abstract class AbstractMethod extends AbstractCode implements Method {
	public abstract boolean isStatic();

	public abstract boolean isAbstract();

	public abstract boolean isFinal();

	public abstract boolean isNative();

	public abstract boolean isSynchronized();

	public abstract boolean isStrictFloatingPoint();

	@Override
	public void addModifiers( java.util.Collection<javax.lang.model.element.Modifier> modifiers ) {
		super.addModifiers( modifiers );
		if( this.isFinal() ) {
			modifiers.add( javax.lang.model.element.Modifier.FINAL );
		} else if( this.isAbstract() ) {
			modifiers.add( javax.lang.model.element.Modifier.ABSTRACT );
		}
		if( this.isStatic() ) {
			modifiers.add( javax.lang.model.element.Modifier.STATIC );
		}
		if( this.isNative() ) {
			modifiers.add( javax.lang.model.element.Modifier.NATIVE );
		}
		if( this.isSynchronized() ) {
			modifiers.add( javax.lang.model.element.Modifier.SYNCHRONIZED );
		}
		if( this.isStrictFloatingPoint() ) {
			modifiers.add( javax.lang.model.element.Modifier.STRICTFP );
		}
	}

	public boolean isOverride() {
		java.util.List<? extends AbstractParameter> parameters = this.getRequiredParameters();
		final int N = parameters.size();
		AbstractType<?, ?, ?>[] parameterTypes = new AbstractType[ N ];
		for( int i = 0; i < N; i++ ) {
			parameterTypes[ i ] = parameters.get( i ).getValueType();
		}
		return this.getDeclaringType().getSuperType().findMethod( this.getName(), parameterTypes ) != null;
	}

	public boolean isFunction() {
		return getReturnType() != JavaType.VOID_TYPE;
	}

	public boolean isProcedure() {
		return isFunction() == false;
	}
}
