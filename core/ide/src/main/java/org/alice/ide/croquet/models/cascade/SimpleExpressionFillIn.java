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
@Deprecated
public class SimpleExpressionFillIn<E extends org.lgna.project.ast.Expression> extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithoutBlanks<E> {
	private final E transientValue;
	private final boolean isLeadingIconDesired;

	public SimpleExpressionFillIn( E value, boolean isLeadingIconDesired ) {
		super( java.util.UUID.fromString( "7479f074-b5f1-4c72-96da-5ebc3c547db5" ) );
		this.transientValue = value;
		this.isLeadingIconDesired = true;
	}

	@Override
	public E createValue( org.lgna.croquet.imp.cascade.ItemNode<? super E, Void> node, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		return this.transientValue;
	}

	@Override
	public E getTransientValue( org.lgna.croquet.imp.cascade.ItemNode<? super E, Void> node ) {
		return this.transientValue;
	}

	@Override
	protected javax.swing.Icon getLeadingIcon( org.lgna.croquet.imp.cascade.ItemNode<? super E, Void> step ) {
		if( this.isLeadingIconDesired ) {
			if( this.transientValue instanceof org.lgna.project.ast.FieldAccess ) {
				org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)this.transientValue;
				org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
				if( field instanceof org.lgna.project.ast.UserField ) {
					org.lgna.project.ast.UserField userField = (org.lgna.project.ast.UserField)field;
					org.lgna.project.ast.AbstractType<?, ?, ?> type = userField.getValueType();
					if( type != null ) {
						if( type.isAssignableTo( org.lgna.story.SThing.class ) ) {
							java.awt.Dimension size = new java.awt.Dimension( 24, 18 );
							org.lgna.croquet.icon.IconFactory iconFactory = org.alice.stageide.icons.IconFactoryManager.getIconFactoryForField( userField );
							if( iconFactory != null ) {
								return iconFactory.getIcon( size );
							} else {
								return org.lgna.croquet.icon.EmptyIconFactory.getInstance().getIcon( size );
							}
						}
					}
				}
			}
		}
		return super.getLeadingIcon( step );
	}

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( this.transientValue );
	}
}
