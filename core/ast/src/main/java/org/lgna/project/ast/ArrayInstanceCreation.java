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
public final class ArrayInstanceCreation extends Expression {
	public ArrayInstanceCreation() {
	}

	public ArrayInstanceCreation( AbstractType<?, ?, ?> arrayType, Integer[] lengths, Expression... expressions ) {
		this.arrayType.setValue( arrayType );
		this.lengths.add( lengths );
		this.expressions.add( expressions );
	}

	public ArrayInstanceCreation( Class<?> arrayCls, Integer[] lengths, Expression... expressions ) {
		this( JavaType.getInstance( arrayCls ), lengths, expressions );
	}

	@Override
	public AbstractType<?, ?, ?> getType() {
		return this.arrayType.getValue();
	}

	@Override
	public boolean isValid() {
		AbstractType<?, ?, ?> type = this.getType();
		if( type != null ) {
			if( type.isArray() ) {
				//todo: check lengths
				for( Expression expression : this.expressions ) {
					if( expression != null ) {
						if( type.getComponentType().isAssignableFrom( expression.getType() ) ) {
							if( expression.isValid() ) {
								//pass
							} else {
								return false;
							}
						} else {
							return false;
						}
					} else {
						//todo?
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

	@Override
	public boolean contentEquals( Node o, ContentEqualsStrictness strictness, edu.cmu.cs.dennisc.property.PropertyFilter filter ) {
		if( super.contentEquals( o, strictness, filter ) ) {
			ArrayInstanceCreation other = (ArrayInstanceCreation)o;
			if( this.arrayType.valueContentEquals( other.arrayType, strictness, filter ) ) {
				if( this.lengths.valueEquals( other.lengths, filter ) ) {
					return this.expressions.valueContentEquals( other.expressions, strictness, filter );
				}
			}
		}
		return false;
	}

	@Override
	public void appendJava( JavaCodeGenerator generator ) {
		generator.appendString( "new " );
		generator.appendTypeName( this.arrayType.getValue().getComponentType() );

		//todo: lengths
		generator.appendChar( '[' );
		generator.appendChar( ']' );

		generator.appendChar( '{' );
		String prefix = "";
		for( Expression expression : this.expressions ) {
			generator.appendString( prefix );
			generator.appendExpression( expression );
			prefix = ",";
		}
		generator.appendChar( '}' );
	}

	public final DeclarationProperty<AbstractType<?, ?, ?>> arrayType = DeclarationProperty.createReferenceInstance( this );;
	public final edu.cmu.cs.dennisc.property.ListProperty<Integer> lengths = new edu.cmu.cs.dennisc.property.ListProperty<Integer>( this );
	public final ExpressionListProperty expressions = new ExpressionListProperty( this );
}
