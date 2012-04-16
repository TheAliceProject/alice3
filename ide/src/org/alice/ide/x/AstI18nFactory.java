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

package org.alice.ide.x;

/**
 * @author Dennis Cosgrove
 */
public abstract class AstI18nFactory extends I18nFactory {
	protected abstract org.lgna.project.ast.AbstractType< ?,?,? > getFallBackTypeForThisExpression();
	protected org.lgna.croquet.components.JComponent< ? > EPIC_HACK_createWrapperIfNecessaryForExpressionPanelessComponent( org.lgna.croquet.components.JComponent< ? > component ) {
		return component;
	}
	
	public java.awt.Paint getInvalidExpressionPaint( java.awt.Paint paint, int x, int y, int width, int height ) {
		return paint;
	}
	public org.lgna.croquet.components.JComponent< ? > createArgumentPane( org.lgna.project.ast.AbstractArgument argument, org.lgna.croquet.components.Component< ? > prefixPane ) {
		if( argument instanceof org.lgna.project.ast.SimpleArgument ) {
			org.lgna.project.ast.SimpleArgument simpleArgument = (org.lgna.project.ast.SimpleArgument)argument;
			org.lgna.project.ast.ExpressionProperty expressionProperty = simpleArgument.expression;
			org.lgna.project.ast.Expression expression = expressionProperty.getValue();
			org.lgna.croquet.components.JComponent< ? > rv = new org.alice.ide.x.components.ExpressionPropertyView( this, expressionProperty );
			if( org.alice.ide.IDE.getActiveInstance().isDropDownDesiredFor( expression ) ) {
				org.alice.ide.croquet.models.ast.cascade.ArgumentCascade model = org.alice.ide.croquet.models.ast.cascade.ArgumentCascade.getInstance( simpleArgument );
				org.alice.ide.codeeditor.ExpressionPropertyDropDownPane expressionPropertyDropDownPane = new org.alice.ide.codeeditor.ExpressionPropertyDropDownPane( model.getRoot().getPopupPrepModel(), prefixPane, rv, expressionProperty );
				rv = expressionPropertyDropDownPane;
			}
			return rv;
		} else if( argument instanceof org.lgna.project.ast.JavaKeyedArgument ) {
			org.lgna.project.ast.JavaKeyedArgument keyedArgument = (org.lgna.project.ast.JavaKeyedArgument)argument;
			return new org.alice.ide.x.components.KeyedArgumentView( this, keyedArgument );
		} else {
			throw new RuntimeException( "todo: " + argument );
		}
	}
	
	protected org.lgna.croquet.components.JComponent< ? > createInstanceCreationPane( org.lgna.project.ast.InstanceCreation instanceCreation ) {
		org.lgna.project.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
		if( constructor instanceof org.lgna.project.ast.AnonymousUserConstructor ) {
			return new org.alice.ide.common.AnonymousConstructorPane( this, (org.lgna.project.ast.AnonymousUserConstructor)constructor );
		} else {
			return new org.alice.ide.x.components.InstanceCreationView( this, instanceCreation );
		}
	}
	protected org.lgna.croquet.components.JComponent< ? > createFieldAccessPane( org.lgna.project.ast.FieldAccess fieldAccess ) {
		return new org.alice.ide.x.components.FieldAccessView( this, fieldAccess );
	}
	
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createGetsComponent( boolean isTowardLeadingEdge ) {
		return new org.alice.ide.common.GetsPane( isTowardLeadingEdge );
	}
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createTypeComponent( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		return org.alice.ide.common.TypeComponent.createInstance( type );
	}

	protected org.lgna.croquet.components.JComponent< ? > createLocalDeclarationPane( org.lgna.project.ast.UserLocal userLocal ) {
		return new org.alice.ide.common.LocalDeclarationPane( userLocal, this.createLocalPane( userLocal ) );
	}
	protected org.lgna.croquet.components.JComponent< ? > createLocalPane( org.lgna.project.ast.UserLocal userLocal ) {
		return new org.alice.ide.common.LocalPane( userLocal );
	}
	protected org.lgna.croquet.components.JComponent< ? > createGenericNodePropertyPane( org.lgna.project.ast.NodeProperty< ? > nodeProperty ) {
		return new org.alice.ide.x.components.NodePropertyView( this, nodeProperty );
	}
	public abstract org.lgna.croquet.components.JComponent< ? > createExpressionPropertyPane( org.lgna.project.ast.ExpressionProperty expressionProperty, org.lgna.project.ast.AbstractType< ?,?,? > type );
	public final org.lgna.croquet.components.JComponent< ? > createExpressionPropertyPane( org.lgna.project.ast.ExpressionProperty expressionProperty ) {
		return this.createExpressionPropertyPane( expressionProperty, null );
	}
	protected org.lgna.croquet.components.JComponent< ? > createResourcePropertyPane( org.lgna.project.ast.ResourceProperty resourceProperty ) {
		return new org.alice.ide.x.components.ResourcePropertyView( this, resourceProperty );
	}
	protected org.lgna.croquet.components.JComponent< ? > createStatementListPropertyPane( org.lgna.project.ast.StatementListProperty statementListProperty ) {
		return new org.alice.ide.x.components.StatementListPropertyView( this, statementListProperty );
	}
	protected abstract org.lgna.croquet.components.JComponent< ? > createSimpleArgumentListPropertyPane( org.lgna.project.ast.SimpleArgumentListProperty argumentListProperty );
	protected abstract org.lgna.croquet.components.JComponent< ? > createKeyedArgumentListPropertyPane( org.lgna.project.ast.KeyedArgumentListProperty argumentListProperty );
	protected org.lgna.croquet.components.JComponent< ? > createExpressionListPropertyPane( org.lgna.project.ast.ExpressionListProperty expressionListProperty ) {
		return new org.alice.ide.x.components.ExpressionListPropertyPane( this, expressionListProperty );
	}
	protected org.lgna.croquet.components.JComponent< ? > createGenericNodeListPropertyPane( org.lgna.project.ast.NodeListProperty< org.lgna.project.ast.AbstractNode > nodeListProperty ) {
		return new org.alice.ide.common.DefaultNodeListPropertyPane( this, nodeListProperty );
	}
	protected org.lgna.croquet.components.JComponent< ? > createGenericListPropertyPane( edu.cmu.cs.dennisc.property.ListProperty< Object > listProperty ) {
		return new org.alice.ide.x.components.ListPropertyLabelsView( this, listProperty );
	}
	protected org.lgna.croquet.components.JComponent< ? > createGenericInstancePropertyPane( edu.cmu.cs.dennisc.property.InstanceProperty property ) {
		return new org.alice.ide.x.components.InstancePropertyLabelView( this, property );
	}
	
	public org.alice.ide.common.AbstractStatementPane createStatementPane( org.lgna.croquet.DragModel dragModel, org.lgna.project.ast.Statement statement, org.lgna.project.ast.StatementListProperty statementListProperty ) {
		org.alice.ide.common.AbstractStatementPane rv;
		if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
			rv = new org.alice.ide.common.ExpressionStatementPane( dragModel, this, (org.lgna.project.ast.ExpressionStatement)statement, statementListProperty );
		} else if( statement instanceof org.lgna.project.ast.Comment ) {
			rv = new org.alice.ide.codeeditor.CommentPane( dragModel, this, (org.lgna.project.ast.Comment)statement, statementListProperty );
		} else {
			rv = new org.alice.ide.common.DefaultStatementPane( dragModel, this, statement, statementListProperty );
		}
		return rv;
	}
	public org.alice.ide.common.AbstractStatementPane createStatementPane( org.lgna.project.ast.Statement statement ) {
		return this.createStatementPane( org.alice.ide.ast.draganddrop.statement.StatementDragModel.getInstance( statement ), statement, null );
	}
	
	protected abstract org.lgna.croquet.components.JComponent< ? > createIdeExpressionPane( org.alice.ide.ast.IdeExpression ideExpression );
	
	public org.lgna.croquet.components.JComponent< ? > createExpressionPane( org.lgna.project.ast.Expression expression ) {
		if( expression instanceof org.alice.ide.ast.IdeExpression ) {
			org.alice.ide.ast.IdeExpression ideExpression = (org.alice.ide.ast.IdeExpression)expression;
			return this.createIdeExpressionPane( ideExpression );
		} else {
			org.lgna.croquet.components.JComponent< ? > rv = null;
			if( expression instanceof org.lgna.project.ast.InfixExpression ) {
				rv = new org.alice.ide.x.components.InfixExpressionView( this, (org.lgna.project.ast.InfixExpression< ? extends Enum< ? > >)expression );
			} else if( expression instanceof org.lgna.project.ast.AssignmentExpression ) {
				rv = new org.alice.ide.common.AssignmentExpressionPane( this, (org.lgna.project.ast.AssignmentExpression)expression );
			} else if( expression instanceof org.lgna.project.ast.FieldAccess ) {
				rv = this.createFieldAccessPane( (org.lgna.project.ast.FieldAccess)expression );
			} else if( expression instanceof org.lgna.project.ast.TypeExpression ) {
				if( org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().isTypeExpressionDesired() ) {
					org.lgna.project.ast.TypeExpression typeExpression = (org.lgna.project.ast.TypeExpression)expression;
					org.lgna.project.ast.Node parent = typeExpression.getParent();
					if( parent instanceof org.lgna.project.ast.MethodInvocation ) {
						org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)parent;
						org.lgna.project.ast.Node grandparent = methodInvocation.getParent();
						if( grandparent instanceof org.lgna.project.ast.JavaKeyedArgument ) {
							org.lgna.project.ast.JavaKeyedArgument javaKeyedArgument = (org.lgna.project.ast.JavaKeyedArgument)grandparent;
							org.lgna.project.ast.AbstractType< ?,?,? > type = org.lgna.project.ast.AstUtilities.getKeywordFactoryType( javaKeyedArgument );
							if( type != null ) {
								rv = new org.lgna.croquet.components.Label( type.getName() + "." );
								//rv.makeStandOut();
							}
						}
					}
					if( rv != null ) {
						//pass
					} else {
						org.lgna.project.ast.AbstractType< ?,?,? > type = typeExpression.value.getValue();
						rv = new org.lgna.croquet.components.LineAxisPanel(
								new org.alice.ide.ast.components.DeclarationNameLabel( type ),
								new org.lgna.croquet.components.Label( "." )
						);
					}
				} else {
					rv = new org.lgna.croquet.components.Label();
				}
			} else if( expression instanceof org.lgna.project.ast.InstanceCreation ) {
				rv = this.createInstanceCreationPane( (org.lgna.project.ast.InstanceCreation)expression );
//				} else if( expression instanceof org.lgna.project.ast.AbstractLiteral ) {
//					rv = this.createComponent( expression );
			} else {
				org.lgna.croquet.components.JComponent< ? > component = this.createComponent( expression );
				if( org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState.getInstance().getValue() ) {
					rv = new org.alice.ide.x.components.ExpressionView( this, expression );
				} else {
					rv = this.EPIC_HACK_createWrapperIfNecessaryForExpressionPanelessComponent( component );
				}
			}
			return rv;
		}
	}
	
	

	private static final java.util.Set< String > LOCAL_PROPERTY_NAMES = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( "local", "item", "variable", "constant" );
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createPropertyComponent( edu.cmu.cs.dennisc.property.InstanceProperty< ? > property, int underscoreCount ) {
		//todo:
		String propertyName = property.getName();
		//
		
		org.lgna.croquet.components.JComponent< ? > rv;
		if( underscoreCount == 2 ) {
			if( LOCAL_PROPERTY_NAMES.contains( propertyName ) ) {
				rv = this.createLocalDeclarationPane( (org.lgna.project.ast.UserLocal)property.getValue() );
			} else {
				rv = new org.lgna.croquet.components.Label( "TODO: handle underscore count 2: " + propertyName );
			}
		} else if( underscoreCount == 1 ) {
			if( LOCAL_PROPERTY_NAMES.contains( propertyName ) ) {
				rv = this.createLocalPane( (org.lgna.project.ast.UserLocal)property.getValue() );
			} else {
				rv = new org.lgna.croquet.components.Label( "TODO: handle underscore count 1: " + propertyName );
			}
		} else {
			rv = null;
			if( rv != null ) {
				//pass
			} else {
				if( property instanceof org.lgna.project.ast.NodeProperty< ? > ) {
					if( property instanceof org.lgna.project.ast.ExpressionProperty ) {
						rv = this.createExpressionPropertyPane( (org.lgna.project.ast.ExpressionProperty)property );
					} else {
						rv = this.createGenericNodePropertyPane( (org.lgna.project.ast.NodeProperty< ? >)property );
					}
				} else if( property instanceof org.lgna.project.ast.ResourceProperty ) {
					rv = this.createResourcePropertyPane( (org.lgna.project.ast.ResourceProperty)property );
				} else if( property instanceof edu.cmu.cs.dennisc.property.ListProperty< ? > ) {
					if( property instanceof org.lgna.project.ast.NodeListProperty< ? > ) {
						if( property instanceof org.lgna.project.ast.StatementListProperty ) {
							rv = this.createStatementListPropertyPane( (org.lgna.project.ast.StatementListProperty)property );
						} else if( property instanceof org.lgna.project.ast.SimpleArgumentListProperty ) {
							rv = this.createSimpleArgumentListPropertyPane( (org.lgna.project.ast.SimpleArgumentListProperty)property );
						} else if( property instanceof org.lgna.project.ast.KeyedArgumentListProperty ) {
							rv = this.createKeyedArgumentListPropertyPane( (org.lgna.project.ast.KeyedArgumentListProperty)property );
						} else if( property instanceof org.lgna.project.ast.ExpressionListProperty ) {
							rv = this.createExpressionListPropertyPane( (org.lgna.project.ast.ExpressionListProperty)property );
						} else {
							rv = this.createGenericNodeListPropertyPane( (org.lgna.project.ast.NodeListProperty< org.lgna.project.ast.AbstractNode >)property );
						}
					} else {
						rv = this.createGenericListPropertyPane( (edu.cmu.cs.dennisc.property.ListProperty< Object >)property );
					}
				} else {
					rv = this.createGenericInstancePropertyPane( property );
				}
			}
		}
		assert rv != null : property;
		return rv;
	}
}
