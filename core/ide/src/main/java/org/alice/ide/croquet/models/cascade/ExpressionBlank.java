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

package org.alice.ide.croquet.models.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionBlank extends org.lgna.croquet.CascadeBlank<org.lgna.project.ast.Expression> {
	public static <T> ExpressionBlank getBlankForType( org.lgna.project.ast.AbstractType<?, ?, ?> type, org.lgna.project.annotations.ValueDetails<T> details ) {
		ExpressionBlank rv;
		if( type != null ) {
			rv = new TypedExpressionBlank( type, details );
		} else {
			rv = org.alice.ide.croquet.models.cascade.blanks.TypeUnsetBlank.getInstance();
		}
		return rv;
	}

	public static <T> ExpressionBlank getBlankForType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return getBlankForType( type, null );
	}

	public static <T> ExpressionBlank getBlankForType( Class<T> cls, org.lgna.project.annotations.ValueDetails<T> details ) {
		return getBlankForType( org.lgna.project.ast.JavaType.getInstance( cls ), details );
	}

	public static <T> ExpressionBlank getBlankForType( Class<T> cls ) {
		return getBlankForType( cls, null );
	}

	public static ExpressionBlank[] createBlanks( org.lgna.project.ast.AbstractType<?, ?, ?>... types ) {
		ExpressionBlank[] rv = new ExpressionBlank[ types.length ];
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = getBlankForType( types[ i ] );
		}
		return rv;
	}

	public static org.alice.ide.croquet.models.cascade.ExpressionBlank[] createBlanks( Class<?>... clses ) {
		return createBlanks( org.lgna.project.ast.JavaType.getInstances( clses ) );
	}

	private final org.lgna.project.ast.AbstractType<?, ?, ?> valueType;
	private final org.lgna.project.annotations.ValueDetails<?> details;

	public ExpressionBlank( java.util.UUID id, org.lgna.project.ast.AbstractType<?, ?, ?> valueType, org.lgna.project.annotations.ValueDetails<?> details ) {
		super( id );
		this.valueType = valueType;
		this.details = details;
	}

	public <T> ExpressionBlank( java.util.UUID id, Class<T> cls, org.lgna.project.annotations.ValueDetails<T> details ) {
		this( id, org.lgna.project.ast.JavaType.getInstance( cls ), details );
	}

	public <T> ExpressionBlank( java.util.UUID id, Class<T> cls ) {
		this( id, cls, null );
	}

	public org.lgna.project.ast.AbstractType<?, ?, ?> getValueType() {
		return this.valueType;
	}

	@Override
	protected void updateChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> children, org.lgna.croquet.imp.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( ide != null ) {
			ide.getExpressionCascadeManager().appendItems( children, blankNode, this.valueType, this.details );
		}
	}
}
