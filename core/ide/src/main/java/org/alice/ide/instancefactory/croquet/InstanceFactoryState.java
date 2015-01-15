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

package org.alice.ide.instancefactory.croquet;

import org.alice.ide.instancefactory.InstanceFactory;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactoryState extends org.lgna.croquet.CustomItemStateWithInternalBlank<InstanceFactory> {
	public InstanceFactoryState( org.alice.ide.ProjectDocumentFrame projectDocumentFrame ) {
		super( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "f4e26c9c-0c3d-4221-95b3-c25df0744a97" ), null, org.alice.ide.instancefactory.croquet.codecs.InstanceFactoryCodec.SINGLETON );
		projectDocumentFrame.getMetaDeclarationFauxState().addValueListener( declarationListener );
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.addProjectChangeOfInterestListener( this.projectChangeOfInterestListener );
	}

	private void fallBackToDefaultFactory() {
		this.setValueTransactionlessly( org.alice.ide.instancefactory.ThisInstanceFactory.getInstance() );
	}

	private void handleDeclarationChanged( org.lgna.project.ast.AbstractDeclaration prevValue, org.lgna.project.ast.AbstractDeclaration nextValue ) {
		if( this.ignoreCount == 0 ) {
			if( nextValue instanceof org.lgna.project.ast.AbstractMethod ) {
				org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)nextValue;
				if( method.isStatic() ) {
					this.setValueTransactionlessly( null );
					return;
				}
			}
			InstanceFactory instanceFactory = this.getValue();
			if( instanceFactory != null ) {
				if( instanceFactory.isValid() ) {
					//pass
				} else {
					this.fallBackToDefaultFactory();
				}
			} else {
				this.fallBackToDefaultFactory();
			}
			//			org.lgna.project.ast.AbstractType< ?,?,? > prevType = getDeclaringType( prevValue );
			//			org.lgna.project.ast.AbstractType< ?,?,? > nextType = getDeclaringType( nextValue );
			//			if( prevType != nextType ) {
			//				InstanceFactory prevValue = this.getValue();
			//				if( prevType != null ) {
			//					if( prevValue != null ) {
			//						map.put( prevType, prevValue );
			//					} else {
			//						map.remove( prevType );
			//					}
			//				}
			//				InstanceFactory nextValue;
			//				if( nextType != null ) {
			//					nextValue = map.get( nextType );
			//					if( nextValue != null ) {
			//						//pass
			//					} else {
			//						nextValue = org.alice.ide.instancefactory.ThisInstanceFactory.getInstance();
			//					}
			//				} else {
			//					nextValue = null;
			//				}
			//				this.setValueTransactionlessly( nextValue );
			//			}
		}
	}

	private void handleAstChangeThatCouldBeOfInterest() {
		InstanceFactory instanceFactory = this.getValue();
		if( instanceFactory != null ) {
			if( instanceFactory.isValid() ) {
				//pass
			} else {
				this.fallBackToDefaultFactory();
			}
		} else {
			this.fallBackToDefaultFactory();
		}
	}

	private static org.lgna.croquet.CascadeBlankChild<InstanceFactory> createFillInMenuComboIfNecessary( org.lgna.croquet.CascadeFillIn<InstanceFactory, Void> item, org.lgna.croquet.CascadeMenuModel<InstanceFactory> subMenu ) {
		if( subMenu != null ) {
			return new org.lgna.croquet.CascadeItemMenuCombo<InstanceFactory>( item, subMenu );
		} else {
			return item;
		}
	}

	/* package-private */static org.lgna.croquet.CascadeBlankChild<InstanceFactory> createFillInMenuComboIfNecessaryForField( org.alice.ide.ApiConfigurationManager apiConfigurationManager, org.lgna.project.ast.UserField field ) {
		return createFillInMenuComboIfNecessary(
				InstanceFactoryFillIn.getInstance( org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( field ) ),
				apiConfigurationManager.getInstanceFactorySubMenuForThisFieldAccess( field ) );
	}

	@Override
	protected void appendPrepModelsToCascadeRootPath( java.util.List<org.lgna.croquet.PrepModel> cascadeRootPath, org.lgna.croquet.edits.Edit edit ) {
		super.appendPrepModelsToCascadeRootPath( cascadeRootPath, edit );
		if( edit instanceof org.lgna.croquet.edits.StateEdit ) {
			org.lgna.croquet.edits.StateEdit<InstanceFactory> stateEdit = (org.lgna.croquet.edits.StateEdit<InstanceFactory>)edit;
			InstanceFactory nextValue = stateEdit.getNextValue();
			if( nextValue instanceof org.alice.ide.instancefactory.ThisFieldAccessMethodInvocationFactory ) {
				org.alice.ide.instancefactory.ThisFieldAccessMethodInvocationFactory thisFieldAccessMethodInvocationFactory = (org.alice.ide.instancefactory.ThisFieldAccessMethodInvocationFactory)nextValue;
				org.lgna.project.ast.UserField field = thisFieldAccessMethodInvocationFactory.getField();
				org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
				org.alice.ide.ApiConfigurationManager apiConfigurationManager = ide.getApiConfigurationManager();
				cascadeRootPath.add( apiConfigurationManager.getInstanceFactorySubMenuForThisFieldAccess( field ) );
			}
		} else {
			throw new RuntimeException( edit != null ? edit.toString() : null );
		}
	}

	//todo
	private final ParametersVariablesAndConstantsSeparator parametersVariablesConstantsSeparator = new ParametersVariablesAndConstantsSeparator();

	@Override
	protected void updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.croquet.imp.cascade.BlankNode<InstanceFactory> blankNode ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.alice.ide.ApiConfigurationManager apiConfigurationManager = ide.getApiConfigurationManager();
		org.lgna.project.ast.AbstractDeclaration declaration = org.alice.ide.meta.DeclarationMeta.getDeclaration();
		boolean isStaticMethod;
		if( declaration instanceof org.lgna.project.ast.AbstractMethod ) {
			org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)declaration;
			isStaticMethod = method.isStatic();
		} else {
			isStaticMethod = false;
		}
		org.lgna.project.ast.AbstractType<?, ?, ?> type = org.alice.ide.meta.DeclarationMeta.getType();

		if( isStaticMethod ) {
			//pass
		} else {
			blankChildren.add(
					createFillInMenuComboIfNecessary(
							InstanceFactoryFillIn.getInstance( org.alice.ide.instancefactory.ThisInstanceFactory.getInstance() ),
							apiConfigurationManager.getInstanceFactorySubMenuForThis( type )
					)
					);
		}
		if( type instanceof org.lgna.project.ast.NamedUserType ) {
			org.lgna.project.ast.NamedUserType namedUserType = (org.lgna.project.ast.NamedUserType)type;
			if( isStaticMethod ) {
				//pass
			} else {
				java.util.List<org.lgna.project.ast.UserField> fields = namedUserType.getDeclaredFields();
				java.util.List<org.lgna.project.ast.UserField> filteredFields = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				for( org.lgna.project.ast.UserField field : fields ) {
					if( apiConfigurationManager.isInstanceFactoryDesiredForType( field.getValueType() ) ) {
						filteredFields.add( field );
					}
				}
				final boolean IS_COLLAPSE_DESIRED = true;
				if( IS_COLLAPSE_DESIRED && ( filteredFields.size() > 16 ) ) {
					org.alice.ide.ast.fieldtree.RootNode root = org.alice.ide.ast.fieldtree.FieldTree.createTreeFor(
							filteredFields,
							org.alice.ide.ast.fieldtree.FieldTree.createFirstClassThreshold( org.lgna.story.SBiped.class ),
							org.alice.ide.ast.fieldtree.FieldTree.createFirstClassThreshold( org.lgna.story.SQuadruped.class ),
							org.alice.ide.ast.fieldtree.FieldTree.createFirstClassThreshold( org.lgna.story.SSwimmer.class ),
							org.alice.ide.ast.fieldtree.FieldTree.createFirstClassThreshold( org.lgna.story.SFlyer.class ),

							org.alice.ide.ast.fieldtree.FieldTree.createSecondClassThreshold( org.lgna.story.SProp.class ),
							org.alice.ide.ast.fieldtree.FieldTree.createSecondClassThreshold( org.lgna.story.SShape.class ),
							org.alice.ide.ast.fieldtree.FieldTree.createSecondClassThreshold( org.lgna.story.SThing.class ),
							org.alice.ide.ast.fieldtree.FieldTree.createSecondClassThreshold( Object.class )
							);
					for( org.alice.ide.ast.fieldtree.FieldNode fieldNode : root.getFieldNodes() ) {
						blankChildren.add( createFillInMenuComboIfNecessaryForField( apiConfigurationManager, fieldNode.getDeclaration() ) );
					}
					for( org.alice.ide.ast.fieldtree.TypeNode typeNode : root.getTypeNodes() ) {
						blankChildren.add( new TypeCascadeMenuModel( typeNode, apiConfigurationManager ) );
					}
				} else {
					for( org.lgna.project.ast.UserField field : filteredFields ) {
						blankChildren.add( createFillInMenuComboIfNecessaryForField( apiConfigurationManager, field ) );
					}
				}
			}

			org.lgna.project.ast.AbstractCode code = ide.getFocusedCode();
			if( code instanceof org.lgna.project.ast.UserCode ) {

				java.util.List<org.lgna.croquet.CascadeBlankChild> parameters = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				java.util.List<org.lgna.croquet.CascadeBlankChild> locals = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				boolean containsVariable = false;
				boolean containsConstant = false;
				org.lgna.project.ast.UserCode userCode = (org.lgna.project.ast.UserCode)code;
				for( org.lgna.project.ast.UserParameter parameter : userCode.getRequiredParamtersProperty() ) {
					if( apiConfigurationManager.isInstanceFactoryDesiredForType( parameter.getValueType() ) ) {
						parameters.add(
								createFillInMenuComboIfNecessary(
										InstanceFactoryFillIn.getInstance( org.alice.ide.instancefactory.ParameterAccessFactory.getInstance( parameter ) ),
										apiConfigurationManager.getInstanceFactorySubMenuForParameterAccess( parameter )
								)
								);
					}
				}

				for( org.lgna.project.ast.UserLocal local : org.lgna.project.ProgramTypeUtilities.getLocals( userCode ) ) {
					if( apiConfigurationManager.isInstanceFactoryDesiredForType( local.getValueType() ) ) {
						if( local.isFinal.getValue() ) {
							containsConstant = true;
						} else {
							containsVariable = true;
						}
						locals.add(
								createFillInMenuComboIfNecessary(
										InstanceFactoryFillIn.getInstance( org.alice.ide.instancefactory.LocalAccessFactory.getInstance( local ) ),
										apiConfigurationManager.getInstanceFactorySubMenuForLocalAccess( local )
								)
								);
					}
				}
				if( ( parameters.size() > 0 ) || ( locals.size() > 0 ) ) {
					blankChildren.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
					blankChildren.add( this.parametersVariablesConstantsSeparator );
					StringBuilder sb = new StringBuilder();
					org.lgna.project.ast.NodeUtilities.safeAppendRepr( sb, code );
					sb.append( " " );
					String prefix = "";
					if( parameters.size() > 0 ) {
						sb.append( "parameters" );
						blankChildren.addAll( parameters );
						prefix = ", ";
					}
					if( locals.size() > 0 ) {
						if( containsVariable ) {
							sb.append( prefix );
							sb.append( "variables" );
							prefix = ", ";
						}
						if( containsConstant ) {
							sb.append( prefix );
							sb.append( "constants" );
							prefix = ", ";
						}
						blankChildren.addAll( locals );
					}
					this.parametersVariablesConstantsSeparator.setMenuItemText( sb.toString() );
				}

				if( userCode instanceof org.lgna.project.ast.UserMethod ) {
					org.lgna.project.ast.UserMethod userMethod = (org.lgna.project.ast.UserMethod)userCode;
					if( org.alice.stageide.StageIDE.INITIALIZE_EVENT_LISTENERS_METHOD_NAME.equals( userMethod.getName() ) ) {
						for( org.lgna.project.ast.Statement statement : userMethod.body.getValue().statements ) {
							if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
								org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)statement;
								org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
								if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
									org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)expression;
									java.util.List<org.lgna.croquet.CascadeBlankChild> methodInvocationBlankChildren = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

									for( org.lgna.project.ast.SimpleArgument argument : methodInvocation.requiredArguments ) {
										org.lgna.project.ast.Expression argumentExpression = argument.expression.getValue();
										if( argumentExpression instanceof org.lgna.project.ast.LambdaExpression ) {
											org.lgna.project.ast.LambdaExpression lambdaExpression = (org.lgna.project.ast.LambdaExpression)argumentExpression;
											org.lgna.project.ast.Lambda lambda = lambdaExpression.value.getValue();
											if( lambda instanceof org.lgna.project.ast.UserLambda ) {
												org.lgna.project.ast.UserLambda userLambda = (org.lgna.project.ast.UserLambda)lambda;
												for( org.lgna.project.ast.UserParameter parameter : userLambda.getRequiredParameters() ) {
													org.lgna.project.ast.AbstractType<?, ?, ?> parameterType = parameter.getValueType();
													for( org.lgna.project.ast.AbstractMethod parameterMethod : org.lgna.project.ast.AstUtilities.getAllMethods( parameterType ) ) {
														org.lgna.project.ast.AbstractType<?, ?, ?> parameterMethodReturnType = parameterMethod.getReturnType();
														if( parameterMethodReturnType.isAssignableTo( org.lgna.story.SThing.class ) ) {
															methodInvocationBlankChildren.add(
																	createFillInMenuComboIfNecessary(
																			InstanceFactoryFillIn.getInstance( org.alice.ide.instancefactory.ParameterAccessMethodInvocationFactory.getInstance( parameter, parameterMethod ) ),
																			apiConfigurationManager.getInstanceFactorySubMenuForParameterAccessMethodInvocation( parameter, parameterMethod )
																	)
																	);
														}
													}
												}
											}
										}
									}

									if( methodInvocationBlankChildren.size() > 0 ) {
										org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();
										blankChildren.add( org.alice.ide.croquet.models.cascade.MethodNameSeparator.getInstance( method ) );
										blankChildren.addAll( methodInvocationBlankChildren );
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected org.alice.ide.instancefactory.InstanceFactory getSwingValue() {
		return this.value;
	}

	@Override
	protected void setSwingValue( org.alice.ide.instancefactory.InstanceFactory value ) {
		this.value = value;
	}

	private int ignoreCount = 0;

	public void pushIgnoreAstChanges() {
		ignoreCount++;
	}

	public void popIgnoreAstChanges() {
		ignoreCount--;
		if( ignoreCount == 0 ) {
			this.handleAstChangeThatCouldBeOfInterest();
		}
	}

	private final org.alice.ide.MetaDeclarationFauxState.ValueListener declarationListener = new org.alice.ide.MetaDeclarationFauxState.ValueListener() {
		@Override
		public void changed( org.lgna.project.ast.AbstractDeclaration prevValue, org.lgna.project.ast.AbstractDeclaration nextValue ) {
			InstanceFactoryState.this.handleDeclarationChanged( prevValue, nextValue );
		}
	};
	//todo: map AbstractCode to Stack< InstanceFactory >
	//private java.util.Map< org.lgna.project.ast.AbstractDeclaration, InstanceFactory > map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private InstanceFactory value;

	private final org.alice.ide.project.events.ProjectChangeOfInterestListener projectChangeOfInterestListener = new org.alice.ide.project.events.ProjectChangeOfInterestListener() {
		@Override
		public void projectChanged() {
			handleAstChangeThatCouldBeOfInterest();
		}
	};
}
