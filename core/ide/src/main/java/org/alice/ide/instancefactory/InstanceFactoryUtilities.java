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

package org.alice.ide.instancefactory;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactoryUtilities {
	private InstanceFactoryUtilities() {
		throw new AssertionError();
	}

	public static InstanceFactory getInstanceFactoryForExpression( org.lgna.project.ast.Expression instanceExpression ) {
		InstanceFactory rv;
		if( instanceExpression instanceof org.lgna.project.ast.ThisExpression ) {
			rv = org.alice.ide.instancefactory.ThisInstanceFactory.getInstance();
		} else if( instanceExpression instanceof org.lgna.project.ast.FieldAccess ) {
			org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)instanceExpression;
			org.lgna.project.ast.Expression fieldAccessExpression = fieldAccess.expression.getValue();
			org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
			if( fieldAccessExpression instanceof org.lgna.project.ast.ThisExpression ) {
				if( field instanceof org.lgna.project.ast.UserField ) {
					org.lgna.project.ast.UserField userField = (org.lgna.project.ast.UserField)field;
					rv = org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( userField );
				} else {
					rv = null;
				}
			} else {
				rv = null;
			}
		} else if( instanceExpression instanceof org.lgna.project.ast.MethodInvocation ) {
			org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)instanceExpression;
			org.lgna.project.ast.Expression methodInvocationInstanceExpression = methodInvocation.expression.getValue();
			org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();
			if( methodInvocationInstanceExpression instanceof org.lgna.project.ast.ThisExpression ) {
				rv = org.alice.ide.instancefactory.ThisMethodInvocationFactory.getInstance( method );
			} else if( methodInvocationInstanceExpression instanceof org.lgna.project.ast.FieldAccess ) {
				org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)methodInvocationInstanceExpression;
				org.lgna.project.ast.Expression fieldAccessExpression = fieldAccess.expression.getValue();
				org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
				if( fieldAccessExpression instanceof org.lgna.project.ast.ThisExpression ) {
					if( field instanceof org.lgna.project.ast.UserField ) {
						org.lgna.project.ast.UserField userField = (org.lgna.project.ast.UserField)field;
						rv = org.alice.ide.instancefactory.ThisFieldAccessMethodInvocationFactory.getInstance( userField, method );
					} else {
						rv = null;
					}
				} else {
					rv = null;
				}
			} else if( methodInvocationInstanceExpression instanceof org.lgna.project.ast.LocalAccess ) {
				org.lgna.project.ast.LocalAccess localAccess = (org.lgna.project.ast.LocalAccess)methodInvocationInstanceExpression;
				rv = LocalAccessMethodInvocationFactory.getInstance( localAccess.local.getValue(), method );
			} else if( methodInvocationInstanceExpression instanceof org.lgna.project.ast.ParameterAccess ) {
				org.lgna.project.ast.ParameterAccess parameterAccess = (org.lgna.project.ast.ParameterAccess)methodInvocationInstanceExpression;
				org.lgna.project.ast.AbstractParameter parameter = parameterAccess.parameter.getValue();
				if( parameter instanceof org.lgna.project.ast.UserParameter ) {
					org.lgna.project.ast.UserParameter userParameter = (org.lgna.project.ast.UserParameter)parameter;
					rv = ParameterAccessMethodInvocationFactory.getInstance( userParameter, method );
				} else {
					rv = null;
				}
			} else {
				rv = null;
			}
		} else if( instanceExpression instanceof org.lgna.project.ast.LocalAccess ) {
			org.lgna.project.ast.LocalAccess localAccess = (org.lgna.project.ast.LocalAccess)instanceExpression;
			rv = LocalAccessFactory.getInstance( localAccess.local.getValue() );
		} else if( instanceExpression instanceof org.lgna.project.ast.ParameterAccess ) {
			org.lgna.project.ast.ParameterAccess parameterAccess = (org.lgna.project.ast.ParameterAccess)instanceExpression;
			org.lgna.project.ast.AbstractParameter parameter = parameterAccess.parameter.getValue();
			if( parameter instanceof org.lgna.project.ast.UserParameter ) {
				org.lgna.project.ast.UserParameter userParameter = (org.lgna.project.ast.UserParameter)parameter;
				rv = ParameterAccessFactory.getInstance( userParameter );
			} else {
				rv = null;
			}
		} else {
			rv = null;
		}
		return rv;
	}
}
