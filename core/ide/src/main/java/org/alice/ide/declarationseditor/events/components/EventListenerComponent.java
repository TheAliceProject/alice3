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
package org.alice.ide.declarationseditor.events.components;

import javax.swing.BorderFactory;

import org.alice.ide.codeeditor.ArgumentListPropertyPane;
import org.alice.ide.x.components.StatementListPropertyView;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.LambdaExpression;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.SimpleArgumentListProperty;
import org.lgna.project.ast.UserLambda;

/**
 * @author Matt May
 */
public class EventListenerComponent extends BorderPanel {

	public EventListenerComponent( MethodInvocation methodInvocation ) {
		int bottom = 0;
		this.addPageStartComponent( createHeader( methodInvocation ) );
		if( methodInvocation.requiredArguments.size() > 0 ) {
			SimpleArgument argument0 = methodInvocation.requiredArguments.get( 0 );
			AbstractMethod singleAbstractMethod = argument0.parameter.getValue().getValueType().getDeclaredMethods().get( 0 );
			if( argument0.expression.getValue() instanceof LambdaExpression ) {
				LambdaExpression lambdaExpression = (LambdaExpression)argument0.expression.getValue();
				if( lambdaExpression.value.getValue() instanceof UserLambda ) {
					UserLambda lambda = (UserLambda)lambdaExpression.value.getValue();
					//ParametersPane parametersPane = new ParametersPane( org.alice.ide.x.ProjectEditorAstI18nFactory.getInstance(), lambda );

					LineAxisPanel singleAbstractMethodHeader = new LineAxisPanel(
							new Label( "declare procedure", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE ),
							org.lgna.croquet.views.BoxUtilities.createHorizontalSliver( 4 ),
							new Label( singleAbstractMethod.getName(), 1.5f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD ),
							org.lgna.croquet.views.BoxUtilities.createHorizontalSliver( 8 ),
							new org.alice.ide.eventseditor.components.EventAccessorMethodsPanel( lambda )
							);

					StatementListPropertyView putCodeHere = new StatementListPropertyView( org.alice.ide.x.ProjectEditorAstI18nFactory.getInstance(), lambda.body.getValue().statements );
					org.alice.ide.common.BodyPane bodyPane = new org.alice.ide.common.BodyPane( putCodeHere );

					BorderPanel codeContainer = new BorderPanel.Builder()
							.pageStart( singleAbstractMethodHeader )
							.center( bodyPane )
							.build();
					codeContainer.setBackgroundColor( org.alice.ide.ThemeUtilities.getActiveTheme().getEventBodyColor() );
					codeContainer.setBorder( BorderFactory.createEmptyBorder( 8, 8, 4, 4 ) );
					this.addCenterComponent( codeContainer );
					bottom = 8;
				}
			}
		}
		this.setBorder( BorderFactory.createEmptyBorder( 4, 4, bottom, 4 ) );
	}

	private SwingComponentView<?> createHeader( MethodInvocation methodInvocation ) {
		AbstractMethod method = methodInvocation.method.getValue();
		LineAxisPanel rv = new LineAxisPanel();
		rv.setBorder( BorderFactory.createEmptyBorder( 0, 0, 4, 0 ) );
		rv.addComponent( new org.alice.ide.common.ThisPane() );
		rv.addComponent( org.lgna.croquet.views.BoxUtilities.createHorizontalSliver( 4 ) );
		Label label = new Label( methodInvocation.method.getValue().getName(), edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
		rv.addComponent( label );
		if( method.getRequiredParameters() != null ) {
			SimpleArgumentListProperty requiredArgumentsProperty = methodInvocation.getRequiredArgumentsProperty();
			ArgumentListPropertyPane requiredParametersListView = new ArgumentListPropertyPane( org.alice.ide.x.ProjectEditorAstI18nFactory.getInstance(), requiredArgumentsProperty ) {
				@Override
				protected AwtComponentView<?> createComponent( SimpleArgument argument ) {
					if( argument.expression.getValue() instanceof LambdaExpression ) {
						return null;
					} else {
						return super.createComponent( argument );
					}
				}
			};
			//			if(requiredParametersListView.getComposite() != null){
			//			requiredParametersListView.
			rv.addComponent( requiredParametersListView );
			//			}
			//			System.out.println(requiredParametersListView);
		}
		if( method.getKeyedParameter() != null ) {
			SwingComponentView<?> keyedArgumentListView = new org.alice.ide.x.components.KeyedArgumentListPropertyView( org.alice.ide.x.ProjectEditorAstI18nFactory.getInstance(), methodInvocation.getKeyedArgumentsProperty() );
			rv.addComponent( keyedArgumentListView );
		}
		return rv;
	}

}
