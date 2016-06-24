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
public final class ArrayLength extends Expression {
	public ArrayLength() {
	}

	public ArrayLength( Expression array ) {
		this.array.setValue( array );
	}

	@Override
	public AbstractType<?, ?, ?> getType() {
		//todo:
		//return TypeDeclaredInJava.INTEGER_PRIMITIVE_TYPE;
		return JavaType.INTEGER_OBJECT_TYPE;
	}

	@Override
	public boolean isValid() {
		Expression arrayExpression = this.array.getValue();
		if( arrayExpression != null ) {
			AbstractType<?, ?, ?> type = arrayExpression.getType();
			if( type != null ) {
				return type.isArray();
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean contentEquals( Node o, ContentEqualsStrictness strictness, edu.cmu.cs.dennisc.property.PropertyFilter filter ) {
		if( super.contentEquals( o, strictness, filter ) ) {
			ArrayLength other = (ArrayLength)o;
			return this.array.valueContentEquals( other.array, strictness, filter );
		}
		return false;
	}

	@Override
	public void appendJava( JavaCodeGenerator generator ) {
		generator.appendExpression( this.array.getValue() );
		generator.appendString( ".length" );
	}

	public final ExpressionProperty array = new ExpressionProperty( this ) {
		@Override
		public AbstractType<?, ?, ?> getExpressionType() {
			return JavaType.OBJECT_TYPE.getArrayType();
		}
	};
}
