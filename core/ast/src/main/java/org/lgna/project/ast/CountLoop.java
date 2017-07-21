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
public class CountLoop extends AbstractLoop {
	public CountLoop() {
	}

	public CountLoop( UserLocal variable, UserLocal constant, Expression count, BlockStatement body ) {
		super( body );
		this.variable.setValue( variable );
		this.constant.setValue( constant );
		this.count.setValue( count );
	}

	@Override
	public boolean contentEquals( Node o, ContentEqualsStrictness strictness, edu.cmu.cs.dennisc.property.PropertyFilter filter ) {
		if( super.contentEquals( o, strictness, filter ) ) {
			CountLoop other = (CountLoop)o;
			if( this.variable.valueContentEquals( other.variable, strictness, filter ) ) {
				if( this.constant.valueContentEquals( other.constant, strictness, filter ) ) {
					return this.count.valueContentEquals( other.count, strictness, filter );
				}
			}
		}
		return false;
	}

	@Override
	protected void appendRepr( AstLocalizer localizer ) {
		localizer.appendLocalizedText( CountLoop.class, "count" );
		localizer.appendSpace();
		safeAppendRepr( localizer, this.count.getValue() );
		super.appendRepr( localizer );
	}

	@Override
	protected void appendJavaLoopPrefix( JavaCodeGenerator generator ) {
		Expression countValue = this.count.getValue();
		String[] a = generator.getVariableAndConstantNameForCountLoop( this );
		String variableName = a[ 0 ];
		String constantName = a[ 1 ];
		boolean isInline;
		if( countValue instanceof IntegerLiteral ) {
			isInline = true;
		} else if( countValue instanceof ParameterAccess ) {
			isInline = true;
		} else if( countValue instanceof LocalAccess ) {
			LocalAccess access = (LocalAccess)countValue;
			isInline = access.local.getValue().isFinal.getValue();
		} else if( countValue instanceof FieldAccess ) {
			FieldAccess access = (FieldAccess)countValue;
			isInline = access.field.getValue().isFinal();
		} else if( countValue instanceof ArrayLength ) {
			//todo?
			isInline = true;
		} else {
			isInline = false;
		}
		if( isInline ) {
			//pass
		} else {
			generator.appendString( "final int " );
			generator.appendString( constantName );
			generator.appendString( "=" );
			generator.appendExpression( countValue );
			generator.appendSemicolon();
		}
		generator.appendString( "for(Integer " );
		generator.appendString( variableName );
		generator.appendString( "=0;" );
		generator.appendString( variableName );
		generator.appendString( "<" );
		if( isInline ) {
			generator.appendExpression( countValue );
		} else {
			generator.appendString( constantName );
		}
		generator.appendString( ";" );
		generator.appendString( variableName );
		generator.appendString( "++)" );
	}

	public final DeclarationProperty<UserLocal> variable = new DeclarationProperty<UserLocal>( this ) {
		@Override
		public boolean isReference() {
			return false;
		}
	};
	public final DeclarationProperty<UserLocal> constant = new DeclarationProperty<UserLocal>( this ) {
		@Override
		public boolean isReference() {
			return false;
		}
	};
	public final ExpressionProperty count = new ExpressionProperty( this ) {
		@Override
		public AbstractType<?, ?, ?> getExpressionType() {
			return JavaType.getInstance( Integer.class );
		}
	};
}
