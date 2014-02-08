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
public abstract class AbstractMethodContainedByUserField extends AbstractMethod {
	private final UserField field;

	/* package-private */AbstractMethodContainedByUserField( UserField field ) {
		this.field = field;
	}

	public final UserField getField() {
		return this.field;
	}

	@Override
	public final org.lgna.project.ast.AbstractType<?, ?, ?> getDeclaringType() {
		return this.field.getDeclaringType();
	}

	public final AbstractParameter getVariableLengthParameter() {
		return null;
	}

	public final AbstractParameter getKeyedParameter() {
		return null;
	}

	@Override
	public final org.lgna.project.annotations.Visibility getVisibility() {
		return org.lgna.project.annotations.Visibility.PRIME_TIME;
	}

	@Override
	public final AccessLevel getAccessLevel() {
		return AccessLevel.PUBLIC;
	}

	@Override
	public final edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return null;
	}

	@Override
	public final boolean isSignatureLocked() {
		return true;
	}

	@Override
	public final boolean isStatic() {
		return false;
	}

	@Override
	public final boolean isAbstract() {
		return false;
	}

	@Override
	public final boolean isFinal() {
		return false;
	}

	@Override
	public final boolean isNative() {
		return false;
	}

	@Override
	public final boolean isSynchronized() {
		return false;
	}

	@Override
	public final boolean isStrictFloatingPoint() {
		return false;
	}

	@Override
	public final AbstractCode getNextLongerInChain() {
		return null;
	}

	@Override
	public final AbstractCode getNextShorterInChain() {
		return null;
	}
}
