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
package org.alice.ide.x.components;

/**
 * @author Dennis Cosgrove
 */
public class ArgumentListPropertyPane extends org.alice.ide.common.AbstractArgumentListPropertyPane {
	public ArgumentListPropertyPane( org.alice.ide.x.ImmutableAstI18nFactory factory, org.lgna.project.ast.SimpleArgumentListProperty property ) {
		super( factory, property );
	}

	@Override
	protected boolean isComponentDesiredFor( org.lgna.project.ast.SimpleArgument argument, int i, int N ) {
		if( i == 0 ) {
			if( argument != null ) {
				org.lgna.project.ast.AbstractParameter parameter = argument.parameter.getValue();
				if( parameter != null ) {
					org.lgna.project.ast.Code code = parameter.getCode();
					if( code instanceof org.lgna.project.ast.JavaMethod ) {
						org.lgna.project.ast.JavaMethod javaMethod = (org.lgna.project.ast.JavaMethod)code;
						if( javaMethod.isAnnotationPresent( org.lgna.project.annotations.AddEventListenerTemplate.class ) ) {
							org.lgna.project.ast.AbstractType<?, ?, ?> parameterType = parameter.getValueType();
							if( parameterType != null ) {
								if( parameterType.isInterface() ) {
									//assume it is going to be a lambda
									return parameterType.getDeclaredMethods().size() != 1;
								}
							}
						}
					}
				}
			}
		}
		return super.isComponentDesiredFor( argument, i, N );
	}

	@Override
	protected org.lgna.croquet.components.Component<?> createComponent( org.lgna.project.ast.SimpleArgument argument ) {
		org.lgna.croquet.components.Component<?> expressionComponent = this.getFactory().createExpressionPane( argument.expression.getValue() );
		org.lgna.project.ast.AbstractParameter parameter = argument.parameter.getValue();
		final boolean IS_PARAMETER_NAME_DESIRED = parameter.getParent() instanceof org.lgna.project.ast.AbstractMethod;
		if( IS_PARAMETER_NAME_DESIRED ) {
			String parameterName = org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().getNameForDeclaration( parameter );
			if( ( parameterName != null ) && ( parameterName.length() > 0 ) ) {
				org.lgna.croquet.components.LineAxisPanel rv = new org.lgna.croquet.components.LineAxisPanel();
				rv.addComponent( new org.lgna.croquet.components.Label( parameterName + ": " ) );
				rv.addComponent( expressionComponent );
				return rv;
			} else {
				return expressionComponent;
			}
		} else {
			return expressionComponent;
		}
	}
}
