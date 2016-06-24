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
package org.lgna.project.migration.ast;

import java.util.ArrayList;

import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.DoubleLiteral;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaKeyedArgument;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.LambdaExpression;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.UserLambda;
import org.lgna.project.ast.UserParameter;
import org.lgna.story.event.MouseClickOnScreenEvent;

/**
 * @author Matt May
 */
public class EventAstMigration extends MethodInvocationAstMigration {
	private static final JavaMethod[] removeTheseDetails = {
			org.lgna.story.ast.EventListenerMethodUtilities.ADD_SCENE_ACTIVATION_LISTENER_METHOD,
			org.lgna.story.ast.EventListenerMethodUtilities.MOVE_WITH_ARROWS,
			org.lgna.story.ast.EventListenerMethodUtilities.ADD_TRANSFORMATION_LISTENER_METHOD,
			org.lgna.story.ast.EventListenerMethodUtilities.ADD_START_COLLISION_LISTENER_METHOD,
			org.lgna.story.ast.EventListenerMethodUtilities.ADD_END_COLLISION_LISTENER_METHOD,
			org.lgna.story.ast.EventListenerMethodUtilities.ADD_START_OCCLUSION_EVENT_LISTENER_METHOD,
			org.lgna.story.ast.EventListenerMethodUtilities.ADD_END_OCCLUSION_EVENT_LISTENER_METHOD,
			org.lgna.story.ast.EventListenerMethodUtilities.ADD_ENTER_PROXIMITY_LISTENER_METHOD,
			org.lgna.story.ast.EventListenerMethodUtilities.ADD_EXIT_PROXIMITY_LISTENER_METHOD,
			org.lgna.story.ast.EventListenerMethodUtilities.ADD_ENTER_VIEW_EVENT_LISTENER_METHOD,
			org.lgna.story.ast.EventListenerMethodUtilities.ADD_EXIT_VIEW_EVENT_LISTENER_METHOD
	};

	public EventAstMigration( org.lgna.project.Version minimumVersion, org.lgna.project.Version maximumVersion ) {
		super( minimumVersion, maximumVersion );
	}

	@Override
	protected void migrate( MethodInvocation methodInvocation, org.lgna.project.Project projectIfApplicable ) {
		org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();
		if( method instanceof org.lgna.project.ast.JavaMethod ) {
			org.lgna.project.ast.JavaMethod javaMethod = (org.lgna.project.ast.JavaMethod)method;
			JavaMethod replacementMethod = null;
			if( javaMethod.getDeclaringType() == org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SScene.class ) ) {
				String methodName = method.getName();
				if( methodName.equals( "addTimeListener" ) ) {
					handleAddTimeListener( methodInvocation, javaMethod );
				} else if( methodName.equals( "addProximityEnterListener" ) || methodName.equals( "addProximityExitListener" ) ) {
					JavaMethod newMethod = javaMethod.getName().equals( "addProximityEnterListener" ) ? org.lgna.story.ast.EventListenerMethodUtilities.ADD_ENTER_PROXIMITY_LISTENER_METHOD : org.lgna.story.ast.EventListenerMethodUtilities.ADD_EXIT_PROXIMITY_LISTENER_METHOD;
					methodInvocation.method.setValue( newMethod );
				} else if( methodName.equals( "addMouseClickOnScreenListener" ) ) {
					addMouseClickOnScreenEventParameter( methodInvocation );
				} else if( ( replacementMethod = needsKeyedParamsRemoved( methodName ) ) != null ) {
					removeTheseParams( methodInvocation, replacementMethod );
				}
			}
		}
	}

	private void removeTheseParams( MethodInvocation methodInvocation, JavaMethod replacementMethod ) {
		methodInvocation.method.setValue( replacementMethod );
		AstUtilities.fixRequiredArgumentsIfNecessary( methodInvocation );
	}

	private JavaMethod needsKeyedParamsRemoved( String methodName ) {
		for( JavaMethod method : removeTheseDetails ) {
			if( methodName.equals( method.getName() ) ) {
				return method;
			}
		}
		return null;
	}

	private void addMouseClickOnScreenEventParameter( MethodInvocation methodInvocation ) {
		SimpleArgument simpleArgument = methodInvocation.requiredArguments.get( 0 );
		LambdaExpression lambda = (LambdaExpression)simpleArgument.expression.getValue();
		UserLambda value = (UserLambda)lambda.value.getValue();
		value.requiredParameters.add( new UserParameter( "event", MouseClickOnScreenEvent.class ) );
	}

	private void handleAddTimeListener( MethodInvocation methodInvocation, org.lgna.project.ast.JavaMethod javaMethod ) {
		ArrayList<JavaKeyedArgument> keyedParameter = methodInvocation.keyedArguments.getValue();
		Double duration = null;
		JavaKeyedArgument argToRemove = null;
		for( JavaKeyedArgument arg : keyedParameter ) {
			Expression value = ( (MethodInvocation)arg.expression.getValue() ).requiredArguments.get( 0 ).expression.getValue();
			if( value instanceof DoubleLiteral ) {
				duration = ( (DoubleLiteral)value ).value.getValue();
				argToRemove = arg;
				break;
			} else {
				//pass
			}
		}
		if( duration != null ) {
			keyedParameter.remove( argToRemove );
		} else {
			duration = 0.0;
		}
		methodInvocation.requiredArguments.add( new SimpleArgument( javaMethod.getRequiredParameters().get( 0 ), new DoubleLiteral( duration ) ) );
		methodInvocation.method.setValue( org.lgna.story.ast.EventListenerMethodUtilities.ADD_TIMER_EVENT_LISTENER_METHOD );
	}
}
