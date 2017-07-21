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

import org.lgna.project.ast.localizer.AstLocalizer;

import edu.cmu.cs.dennisc.property.PropertyFilter;

/**
 * @author Dennis Cosgrove
 */
public final class TypeLiteral extends AbstractValueLiteral<AbstractType<?, ?, ?>> {
	public TypeLiteral() {
	}

	public TypeLiteral( AbstractType<?, ?, ?> value ) {
		this.value.setValue( value );
	}

	public TypeLiteral( Class<?> cls ) {
		this( JavaType.getInstance( cls ) );
	}

	@Override
	public AbstractType<?, ?, ?> getType() {
		//todo
		return JavaType.getInstance( value.getValue().getClass() );
	}

	@Override
	public edu.cmu.cs.dennisc.property.InstanceProperty<AbstractType<?, ?, ?>> getValueProperty() {
		return this.value;
	}

	@Override
	protected boolean valuePropertyContentEquals( AbstractValueLiteral<AbstractType<?, ?, ?>> other, ContentEqualsStrictness strictness, PropertyFilter filter ) {
		TypeLiteral otherTypeLiteral = (TypeLiteral)other;
		return this.value.valueContentEquals( otherTypeLiteral.value, strictness, filter );
	}

	@Override
	protected void appendRepr( AstLocalizer localizer ) {
		safeAppendRepr( localizer, this.value.getValue() );
		localizer.appendText( ".class" );
	}

	@Override
	public void appendJava( JavaCodeGenerator generator ) {
		generator.appendTypeName( this.value.getValue() );
		generator.appendString( ".class" );
	}

	public final DeclarationProperty<AbstractType<?, ?, ?>> value = DeclarationProperty.createReferenceInstance( this );
}
