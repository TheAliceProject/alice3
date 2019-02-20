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

import org.alice.ide.IDE;
import org.alice.ide.ast.components.DeclarationNameLabel;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.x.AstI18nFactory;
import org.lgna.croquet.views.AxisPanel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractParameter;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ArrayAccess;
import org.lgna.project.ast.AssignmentExpression;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.LocalAccess;
import org.lgna.project.ast.ParameterAccess;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserParameter;

/**
 * @author Dennis Cosgrove
 */
public class AssignmentExpressionPane extends LineAxisPanel {
	private AssignmentExpression assignmentExpression;

	public AssignmentExpressionPane( AstI18nFactory factory, AssignmentExpression assignmentExpression ) {
		this.assignmentExpression = assignmentExpression;
		Expression left = this.assignmentExpression.leftHandSide.getValue();

		Expression expression;
		AxisPanel parent;
		if( left instanceof ArrayAccess ) {
			ArrayAccess arrayAccess = (ArrayAccess)left;
			parent = new LineAxisPanel();
			expression = arrayAccess.array.getValue();
			this.addComponent( parent );
		} else {
			parent = this;
			expression = left;
		}

		boolean isSetter = false;
		if( expression != null ) {
			if( expression instanceof FieldAccess ) {
				FieldAccess fieldAccess = (FieldAccess)expression;
				DeclarationNameLabel nameLabel = new DeclarationNameLabel( fieldAccess.field.getValue() );
				//			nameLabel.setFontToScaledFont( 1.5f );
				AbstractField field = fieldAccess.field.getValue();
				IDE.AccessorAndMutatorDisplayStyle accessorAndMutatorDisplayStyle = IDE.getActiveInstance().getAccessorAndMutatorDisplayStyle( field );
				isSetter = accessorAndMutatorDisplayStyle == IDE.AccessorAndMutatorDisplayStyle.GETTER_AND_SETTER;
				parent.addComponent( factory.createExpressionPropertyPane( fieldAccess.expression, field.getDeclaringType() ) );
				if( FormatterState.isJava() ) {
					parent.addComponent( new Label( " . " ) );
				} else {
					parent.addComponent( new Label( " " ) );
				}
				if( isSetter ) {
					parent.addComponent( new Label( "set" ) );
				}
				parent.addComponent( nameLabel );
				if( isSetter ) {
					if( FormatterState.isJava() ) {
						parent.addComponent( new Label( "( " ) );
					}
				}
			} else if( expression instanceof LocalAccess ) {
				LocalAccess localAccess = (LocalAccess)expression;
				UserLocal local = localAccess.local.getValue();
				parent.addComponent( new LocalPane( local, factory.isLocalDraggableAndMutable( local ) ) );
			} else if( expression instanceof ParameterAccess ) {
				ParameterAccess parameterAccess = (ParameterAccess)expression;
				AbstractParameter parameter = parameterAccess.parameter.getValue();
				parent.addComponent( new ParameterPane( null, (UserParameter)parameter ) );
			} else {
				parent.addComponent( new Label( "TODO" ) );
			}
		} else {
			parent.addComponent( new Label( "???" ) );
		}

		if( left instanceof ArrayAccess ) {
			ArrayAccess arrayAccess = (ArrayAccess)left;
			parent.addComponent( new Label( "[ " ) );
			parent.addComponent( factory.createExpressionPropertyPane( arrayAccess.index ) );
			parent.addComponent( new Label( " ]" ) );
		}

		if( isSetter ) {
			//pass
		} else {
			if( FormatterState.isJava() ) {
				parent.addComponent( new Label( " = " ) );
			} else {
				parent.addComponent( new GetsPane( true ) );
			}
		}
		AbstractType<?, ?, ?> rightHandValueType = this.assignmentExpression.expressionType.getValue();
		parent.addComponent( factory.createExpressionPropertyPane( this.assignmentExpression.rightHandSide, rightHandValueType ) );
		if( isSetter ) {
			if( FormatterState.isJava() ) {
				parent.addComponent( new Label( " )" ) );
			}
		}
	}
}
