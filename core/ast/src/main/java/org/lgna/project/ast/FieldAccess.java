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

/**
 * @author Dennis Cosgrove
 */
public final class FieldAccess extends Expression {
	public FieldAccess() {
	}

	public FieldAccess( Expression expression, AbstractField field ) {
		this.expression.setValue( expression );
		this.field.setValue( field );
	}

	@Override
	public AbstractType<?, ?, ?> getType() {
		AbstractField fieldValue = this.field.getValue();
		return fieldValue != null ? fieldValue.getValueType() : null;
	}

	@Override
	public boolean isValid() {
		boolean rv;
		Expression e = expression.getValue();
		AbstractField f = field.getValue();
		if( ( e != null ) && ( f != null ) ) {
			if( f.isValid() ) {
				if( f.isStatic() ) {
					//todo
					rv = true;
				} else {
					AbstractType<?, ?, ?> declaringType = f.getDeclaringType();
					AbstractType<?, ?, ?> expressionType = e.getType();
					if( expressionType instanceof AnonymousUserType ) {
						//todo
						rv = true;
					} else {
						if( ( declaringType != null ) && ( expressionType != null ) ) {
							rv = declaringType.isAssignableFrom( expressionType );
						} else {
							rv = false;
						}
					}
				}
			} else {
				rv = false;
			}
		} else {
			rv = false;
		}
		return rv;
	}

	@Override
	public boolean contentEquals( Node o, ContentEqualsStrictness strictness, edu.cmu.cs.dennisc.property.PropertyFilter filter ) {
		if( super.contentEquals( o, strictness, filter ) ) {
			FieldAccess other = (FieldAccess)o;
			if( this.expression.valueContentEquals( other.expression, strictness, filter ) ) {
				return this.field.valueContentEquals( other.field, strictness, filter );
			}
		}
		return false;
	}

	@Override
	protected void appendRepr( AstLocalizer localizer ) {
		safeAppendRepr( localizer, this.expression.getValue() );
		localizer.appendDot();
		safeAppendRepr( localizer, this.field.getValue() );
	}

	@Override
	public void appendJava( JavaCodeGenerator generator ) {
		generator.appendExpression( this.expression.getValue() );
		generator.appendChar( '.' );
		generator.appendString( this.field.getValue().getName() );
	}

	public final ExpressionProperty expression = new ExpressionProperty( this ) {
		@Override
		public AbstractType<?, ?, ?> getExpressionType() {
			AbstractField f = field.getValue();
			if( f != null ) {
				return f.getDeclaringType();
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "field.getValue() == null", field );
				return JavaType.OBJECT_TYPE;
			}
		}
	};
	public final DeclarationProperty<AbstractField> field = new DeclarationProperty<AbstractField>( this ) {
		@Override
		public boolean isReference() {
			return true;
		}
	};
}
