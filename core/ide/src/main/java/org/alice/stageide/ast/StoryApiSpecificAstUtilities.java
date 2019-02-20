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
package org.alice.stageide.ast;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import org.alice.ide.ProjectDocument;
import org.alice.stageide.StageIDE;
import org.lgna.croquet.Application;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.JavaCodeGenerator;
import org.lgna.project.ast.Lambda;
import org.lgna.project.ast.LambdaExpression;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserLambda;
import org.lgna.project.ast.UserMethod;
import org.lgna.story.SScene;
import org.lgna.story.ast.EventListenerMethodUtilities;
import org.lgna.story.ast.JavaCodeUtilities;

import java.util.Collections;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class StoryApiSpecificAstUtilities {
	public static UserField getSceneFieldFromProgramType( NamedUserType programType ) {
		if( programType != null ) {
			if( programType.fields.size() > 0 ) {
				return programType.fields.get( 0 );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static NamedUserType getSceneTypeFromProgramType( NamedUserType programType ) {
		UserField sceneField = getSceneFieldFromProgramType( programType );
		if( sceneField != null ) {
			return (NamedUserType)sceneField.getValueType();
		} else {
			return null;
		}
	}

	public static NamedUserType getSceneTypeFromProject( Project project ) {
		if( project != null ) {
			return getSceneTypeFromProgramType( project.getProgramType() );
		} else {
			return null;
		}
	}

	public static NamedUserType getSceneTypeFromDocument( ProjectDocument document ) {
		if( document != null ) {
			return getSceneTypeFromProject( document.getProject() );
		} else {
			return null;
		}
	}

	public static List<UserMethod> getUserMethodsInvokedSceneActivationListeners( NamedUserType sceneType ) {
		if( sceneType != null ) {
			List<UserMethod> methods = Lists.newLinkedList();
			UserMethod initializeEventListenersMethod = sceneType.getDeclaredMethod( StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME );
			if( initializeEventListenersMethod != null ) {
				for( Statement statement : initializeEventListenersMethod.body.getValue().statements ) {
					if( statement instanceof ExpressionStatement ) {
						ExpressionStatement expressionStatement = (ExpressionStatement)statement;
						Expression expression = expressionStatement.expression.getValue();
						if( expression instanceof MethodInvocation ) {
							MethodInvocation methodInvocation = (MethodInvocation)expression;
							if( methodInvocation.method.getValue() == EventListenerMethodUtilities.ADD_SCENE_ACTIVATION_LISTENER_METHOD ) {
								SimpleArgument arg0 = methodInvocation.requiredArguments.get( 0 );
								Expression arg0Expression = arg0.expression.getValue();
								if( arg0Expression instanceof LambdaExpression ) {
									LambdaExpression lambdaExpression = (LambdaExpression)arg0Expression;
									Lambda lambda = lambdaExpression.value.getValue();
									if( lambda instanceof UserLambda ) {
										UserLambda userLambda = (UserLambda)lambda;
										IsInstanceCrawler<MethodInvocation> crawler = new IsInstanceCrawler<MethodInvocation>( MethodInvocation.class ) {
											@Override
											protected boolean isAcceptable( MethodInvocation methodInvocation ) {
												return methodInvocation.method.getValue().isUserAuthored();
											}
										};
										userLambda.crawl( crawler, CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY );
										for( MethodInvocation mi : crawler.getList() ) {
											AbstractMethod m = mi.method.getValue();
											if( m instanceof UserMethod ) {
												UserMethod um = (UserMethod)m;
												if( methods.contains( um ) ) {
													//pass
												} else {
													methods.add( um );
												}
											}
										}
									}
								}
							}
						}

					}
				}
			}
			return methods;
		} else {
			return Collections.emptyList();
		}
	}

	public static UserMethod getPerformEditorGeneratedSetUpMethod( NamedUserType sceneType ) {
		if( sceneType != null ) {
			return sceneType.getDeclaredMethod( StageIDE.PERFORM_GENERATED_SET_UP_METHOD_NAME );
		} else {
			return null;
		}
	}

	public static UserMethod getInitializeEventListenersMethod( NamedUserType sceneType ) {
		if( sceneType != null ) {
			return sceneType.getDeclaredMethod( StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME );
		} else {
			return null;
		}
	}

	public static boolean isSceneType( AbstractType<?, ?, ?> type ) {
		if( type != null ) {
			return type.isAssignableTo( SScene.class );
		} else {
			return false;
		}
	}

	private static JavaCodeGenerator s_javaCodeGenerator;

	public static String getInnerCommentForMethodName( AbstractType<?, ?, ?> type, String methodName ) {
		if( s_javaCodeGenerator == null ) {
			s_javaCodeGenerator = JavaCodeUtilities.createJavaCodeGenerator();
		}
		return s_javaCodeGenerator.getLocalizedComment( type, methodName + ".inner", Application.getLocale() );
	}

}
