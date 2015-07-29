/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.ast;

/**
 * @author Dennis Cosgrove
 */
public class StoryApiSpecificAstUtilities {
	public static org.lgna.project.ast.UserField getSceneFieldFromProgramType( org.lgna.project.ast.NamedUserType programType ) {
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

	public static org.lgna.project.ast.NamedUserType getSceneTypeFromProgramType( org.lgna.project.ast.NamedUserType programType ) {
		org.lgna.project.ast.UserField sceneField = getSceneFieldFromProgramType( programType );
		if( sceneField != null ) {
			return (org.lgna.project.ast.NamedUserType)sceneField.getValueType();
		} else {
			return null;
		}
	}

	public static org.lgna.project.ast.NamedUserType getSceneTypeFromProject( org.lgna.project.Project project ) {
		if( project != null ) {
			return getSceneTypeFromProgramType( project.getProgramType() );
		} else {
			return null;
		}
	}

	public static org.lgna.project.ast.NamedUserType getSceneTypeFromDocument( org.alice.ide.ProjectDocument document ) {
		if( document != null ) {
			return getSceneTypeFromProject( document.getProject() );
		} else {
			return null;
		}
	}

	public static java.util.List<org.lgna.project.ast.UserMethod> getUserMethodsInvokedSceneActivationListeners( org.lgna.project.ast.NamedUserType sceneType ) {
		if( sceneType != null ) {
			java.util.List<org.lgna.project.ast.UserMethod> methods = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			org.lgna.project.ast.UserMethod initializeEventListenersMethod = sceneType.getDeclaredMethod( org.alice.stageide.StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME );
			if( initializeEventListenersMethod != null ) {
				for( org.lgna.project.ast.Statement statement : initializeEventListenersMethod.body.getValue().statements ) {
					if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
						org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)statement;
						org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
						if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
							org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)expression;
							if( methodInvocation.method.getValue() == org.lgna.story.ast.EventListenerMethodUtilities.ADD_SCENE_ACTIVATION_LISTENER_METHOD ) {
								org.lgna.project.ast.SimpleArgument arg0 = methodInvocation.requiredArguments.get( 0 );
								org.lgna.project.ast.Expression arg0Expression = arg0.expression.getValue();
								if( arg0Expression instanceof org.lgna.project.ast.LambdaExpression ) {
									org.lgna.project.ast.LambdaExpression lambdaExpression = (org.lgna.project.ast.LambdaExpression)arg0Expression;
									org.lgna.project.ast.Lambda lambda = lambdaExpression.value.getValue();
									if( lambda instanceof org.lgna.project.ast.UserLambda ) {
										org.lgna.project.ast.UserLambda userLambda = (org.lgna.project.ast.UserLambda)lambda;
										edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.MethodInvocation> crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.MethodInvocation>( org.lgna.project.ast.MethodInvocation.class ) {
											@Override
											protected boolean isAcceptable( org.lgna.project.ast.MethodInvocation methodInvocation ) {
												return methodInvocation.method.getValue().isUserAuthored();
											}
										};
										userLambda.crawl( crawler, org.lgna.project.ast.CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY );
										for( org.lgna.project.ast.MethodInvocation mi : crawler.getList() ) {
											org.lgna.project.ast.AbstractMethod m = mi.method.getValue();
											if( m instanceof org.lgna.project.ast.UserMethod ) {
												org.lgna.project.ast.UserMethod um = (org.lgna.project.ast.UserMethod)m;
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
			return java.util.Collections.emptyList();
		}
	}

	public static org.lgna.project.ast.UserMethod getPerformEditorGeneratedSetUpMethod( org.lgna.project.ast.NamedUserType sceneType ) {
		if( sceneType != null ) {
			return sceneType.getDeclaredMethod( org.alice.stageide.StageIDE.PERFORM_GENERATED_SET_UP_METHOD_NAME );
		} else {
			return null;
		}
	}

	public static org.lgna.project.ast.UserMethod getInitializeEventListenersMethod( org.lgna.project.ast.NamedUserType sceneType ) {
		if( sceneType != null ) {
			return sceneType.getDeclaredMethod( org.alice.stageide.StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME );
		} else {
			return null;
		}
	}

	public static boolean isSceneType( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		if( type != null ) {
			return type.isAssignableTo( org.lgna.story.SScene.class );
		} else {
			return false;
		}
	}

	private static org.lgna.project.ast.JavaCodeGenerator s_javaCodeGenerator;

	public static String getInnerCommentForMethodName( org.lgna.project.ast.AbstractType<?, ?, ?> type, String methodName ) {
		if( s_javaCodeGenerator == null ) {
			s_javaCodeGenerator = org.lgna.story.ast.JavaCodeUtilities.createJavaCodeGenerator();
		}
		return s_javaCodeGenerator.getLocalizedComment( type, methodName + ".inner", org.lgna.croquet.Application.getLocale() );
	}

}
