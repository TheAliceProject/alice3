/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
public class UserSetter extends AbstractMethod {
	private final UserField field;
	private final java.util.List<UserSetterParameter> requiredParameters = java.util.Collections.unmodifiableList( edu.cmu.cs.dennisc.java.util.Collections.newArrayList( new UserSetterParameter( this ) ) );

	/* package-private */UserSetter( UserField field ) {
		this.field = field;
	}

	public UserField getField() {
		return this.field;
	}

	public AbstractType<?, ?, ?> getReturnType() {
		return this.field.getValueType();
	}

	public java.util.List<? extends AbstractParameter> getRequiredParameters() {
		return this.requiredParameters;
	}

	public AbstractParameter getVariableLengthParameter() {
		return null;
	}

	public AbstractParameter getKeyedParameter() {
		return null;
	}

	@Override
	public org.lgna.project.annotations.Visibility getVisibility() {
		return org.lgna.project.annotations.Visibility.PRIME_TIME;
	}

	@Override
	public AccessLevel getAccessLevel() {
		return AccessLevel.PUBLIC;
	}

	@Override
	public String getName() {
		return "TODO_set_" + this.field.getName();
	}

	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return null;
	}

	@Override
	public boolean isSignatureLocked() {
		return true;
	}

	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public boolean isAbstract() {
		return false;
	}

	@Override
	public boolean isFinal() {
		return false;
	}

	@Override
	public boolean isNative() {
		return false;
	}

	@Override
	public boolean isSynchronized() {
		return false;
	}

	@Override
	public boolean isStrictFloatingPoint() {
		return false;
	}

	@Override
	public AbstractCode getNextLongerInChain() {
		return null;
	}

	@Override
	public AbstractCode getNextShorterInChain() {
		return null;
	}
}
