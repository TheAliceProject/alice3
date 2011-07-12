/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.common;


/**
 * @author Dennis Cosgrove
 */
public class AssignmentExpressionPane extends org.lgna.croquet.components.LineAxisPanel {
	private edu.cmu.cs.dennisc.alice.ast.AssignmentExpression assignmentExpression;
	public AssignmentExpressionPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.AssignmentExpression assignmentExpression ) {
		this.assignmentExpression = assignmentExpression;
		edu.cmu.cs.dennisc.alice.ast.Expression left = this.assignmentExpression.leftHandSide.getValue();
		
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> desiredValueType;
		edu.cmu.cs.dennisc.alice.ast.Expression expression;
		org.lgna.croquet.components.AxisPanel parent;
		if( left instanceof edu.cmu.cs.dennisc.alice.ast.ArrayAccess ) {
			edu.cmu.cs.dennisc.alice.ast.ArrayAccess arrayAccess = (edu.cmu.cs.dennisc.alice.ast.ArrayAccess)left;
			parent = new org.lgna.croquet.components.LineAxisPanel();
			this.addComponent( parent );
			expression = arrayAccess.array.getValue();
		} else {
			parent = this;
			expression = left;
		}

		boolean isSetter = false;
		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
			edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression;
			org.alice.ide.common.DeclarationNameLabel nameLabel = new org.alice.ide.common.DeclarationNameLabel( fieldAccess.field.getValue() );
//			nameLabel.setFontToScaledFont( 1.5f );
			edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
			org.alice.ide.IDE.AccessorAndMutatorDisplayStyle accessorAndMutatorDisplayStyle = org.alice.ide.IDE.getActiveInstance().getAccessorAndMutatorDisplayStyle( field );
			isSetter = accessorAndMutatorDisplayStyle == org.alice.ide.IDE.AccessorAndMutatorDisplayStyle.GETTER_AND_SETTER;
			desiredValueType = field.getDesiredValueType();
			parent.addComponent( factory.createExpressionPropertyPane( fieldAccess.expression, null, field.getDeclaringType() ) );
			if( org.alice.ide.IDE.getActiveInstance().isJava() ) {
				parent.addComponent( new org.lgna.croquet.components.Label( " . " ) );
			} else {
				parent.addComponent( new org.lgna.croquet.components.Label( " " ) );
			}
			if( isSetter ) {
				parent.addComponent( new org.lgna.croquet.components.Label( "set" ) );
			}
			parent.addComponent( nameLabel );
			if( isSetter ) {
				if( org.alice.ide.IDE.getActiveInstance().isJava() ) {
					parent.addComponent( new org.lgna.croquet.components.Label( "( " ) );
				}
			}
		} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.VariableAccess ) {
			edu.cmu.cs.dennisc.alice.ast.VariableAccess variableAccess = (edu.cmu.cs.dennisc.alice.ast.VariableAccess)expression;
			edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = variableAccess.variable.getValue();
			//todo?
			//desiredValueType = variable.getDesiredValueType();
			desiredValueType = null;
			parent.addComponent( new VariablePane( variable ) );
		} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.ParameterAccess ) {
			edu.cmu.cs.dennisc.alice.ast.ParameterAccess parameterAccess = (edu.cmu.cs.dennisc.alice.ast.ParameterAccess)expression;
			edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter = parameterAccess.parameter.getValue();
			desiredValueType = parameter.getDesiredValueType();
			parent.addComponent( new ParameterPane( null, (edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice)parameter ) );
		} else {
			desiredValueType = null;
			parent.addComponent( new org.lgna.croquet.components.Label( "TODO" ) );
		}
		if( left instanceof edu.cmu.cs.dennisc.alice.ast.ArrayAccess ) {
			edu.cmu.cs.dennisc.alice.ast.ArrayAccess arrayAccess = (edu.cmu.cs.dennisc.alice.ast.ArrayAccess)left;
			parent.addComponent( new org.lgna.croquet.components.Label( "[ " ) );
			parent.addComponent( factory.createExpressionPropertyPane( arrayAccess.index, null ) );
			parent.addComponent( new org.lgna.croquet.components.Label( " ]" ) );
		}
		
		if( isSetter ) {
			//pass
		} else {
			if( org.alice.ide.IDE.getActiveInstance().isJava() ) {
				parent.addComponent( new org.lgna.croquet.components.Label( " = " ) );
			} else {
				parent.addComponent( new org.alice.ide.common.GetsPane( true ) );
			}
		}
		parent.addComponent( factory.createExpressionPropertyPane( this.assignmentExpression.rightHandSide, null, desiredValueType ) );
		if( isSetter ) {
			if( org.alice.ide.IDE.getActiveInstance().isJava() ) {
				parent.addComponent( new org.lgna.croquet.components.Label( " )" ) );
			}
		}
	}
}
