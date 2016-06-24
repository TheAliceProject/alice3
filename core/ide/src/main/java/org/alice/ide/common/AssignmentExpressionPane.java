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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class AssignmentExpressionPane extends org.lgna.croquet.views.LineAxisPanel {
	private org.lgna.project.ast.AssignmentExpression assignmentExpression;

	public AssignmentExpressionPane( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.AssignmentExpression assignmentExpression ) {
		this.assignmentExpression = assignmentExpression;
		org.lgna.project.ast.Expression left = this.assignmentExpression.leftHandSide.getValue();

		org.lgna.project.ast.Expression expression;
		org.lgna.croquet.views.AxisPanel parent;
		if( left instanceof org.lgna.project.ast.ArrayAccess ) {
			org.lgna.project.ast.ArrayAccess arrayAccess = (org.lgna.project.ast.ArrayAccess)left;
			parent = new org.lgna.croquet.views.LineAxisPanel();
			expression = arrayAccess.array.getValue();
			this.addComponent( parent );
		} else {
			parent = this;
			expression = left;
		}

		boolean isSetter = false;
		if( expression != null ) {
			if( expression instanceof org.lgna.project.ast.FieldAccess ) {
				org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)expression;
				org.alice.ide.ast.components.DeclarationNameLabel nameLabel = new org.alice.ide.ast.components.DeclarationNameLabel( fieldAccess.field.getValue() );
				//			nameLabel.setFontToScaledFont( 1.5f );
				org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
				org.alice.ide.IDE.AccessorAndMutatorDisplayStyle accessorAndMutatorDisplayStyle = org.alice.ide.IDE.getActiveInstance().getAccessorAndMutatorDisplayStyle( field );
				isSetter = accessorAndMutatorDisplayStyle == org.alice.ide.IDE.AccessorAndMutatorDisplayStyle.GETTER_AND_SETTER;
				parent.addComponent( factory.createExpressionPropertyPane( fieldAccess.expression, field.getDeclaringType() ) );
				if( org.alice.ide.croquet.models.ui.formatter.FormatterState.isJava() ) {
					parent.addComponent( new org.lgna.croquet.views.Label( " . " ) );
				} else {
					parent.addComponent( new org.lgna.croquet.views.Label( " " ) );
				}
				if( isSetter ) {
					parent.addComponent( new org.lgna.croquet.views.Label( "set" ) );
				}
				parent.addComponent( nameLabel );
				if( isSetter ) {
					if( org.alice.ide.croquet.models.ui.formatter.FormatterState.isJava() ) {
						parent.addComponent( new org.lgna.croquet.views.Label( "( " ) );
					}
				}
			} else if( expression instanceof org.lgna.project.ast.LocalAccess ) {
				org.lgna.project.ast.LocalAccess localAccess = (org.lgna.project.ast.LocalAccess)expression;
				org.lgna.project.ast.UserLocal local = localAccess.local.getValue();
				parent.addComponent( new LocalPane( local, factory.isLocalDraggableAndMutable( local ) ) );
			} else if( expression instanceof org.lgna.project.ast.ParameterAccess ) {
				org.lgna.project.ast.ParameterAccess parameterAccess = (org.lgna.project.ast.ParameterAccess)expression;
				org.lgna.project.ast.AbstractParameter parameter = parameterAccess.parameter.getValue();
				parent.addComponent( new ParameterPane( null, (org.lgna.project.ast.UserParameter)parameter ) );
			} else {
				parent.addComponent( new org.lgna.croquet.views.Label( "TODO" ) );
			}
		} else {
			parent.addComponent( new org.lgna.croquet.views.Label( "???" ) );
		}

		if( left instanceof org.lgna.project.ast.ArrayAccess ) {
			org.lgna.project.ast.ArrayAccess arrayAccess = (org.lgna.project.ast.ArrayAccess)left;
			parent.addComponent( new org.lgna.croquet.views.Label( "[ " ) );
			parent.addComponent( factory.createExpressionPropertyPane( arrayAccess.index ) );
			parent.addComponent( new org.lgna.croquet.views.Label( " ]" ) );
		}

		if( isSetter ) {
			//pass
		} else {
			if( org.alice.ide.croquet.models.ui.formatter.FormatterState.isJava() ) {
				parent.addComponent( new org.lgna.croquet.views.Label( " = " ) );
			} else {
				parent.addComponent( new org.alice.ide.common.GetsPane( true ) );
			}
		}
		org.lgna.project.ast.AbstractType<?, ?, ?> rightHandValueType = this.assignmentExpression.expressionType.getValue();
		parent.addComponent( factory.createExpressionPropertyPane( this.assignmentExpression.rightHandSide, rightHandValueType ) );
		if( isSetter ) {
			if( org.alice.ide.croquet.models.ui.formatter.FormatterState.isJava() ) {
				parent.addComponent( new org.lgna.croquet.views.Label( " )" ) );
			}
		}
	}
}
