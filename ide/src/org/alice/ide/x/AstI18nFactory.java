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
	
	protected org.lgna.croquet.components.JComponent< ? > EPIC_HACK_createWrapperIfNecessaryForExpressionPanelessComponent( org.lgna.croquet.components.JComponent< ? > component ) {
		return component;
	}
	
	public org.lgna.croquet.components.JComponent< ? > createArgumentPane( org.lgna.project.ast.Argument argument, org.lgna.croquet.components.Component< ? > prefixPane ) {
		org.lgna.project.ast.ExpressionProperty expressionProperty = argument.expression;
		org.lgna.project.ast.Expression expression = expressionProperty.getValue();
		org.lgna.croquet.components.JComponent< ? > rv = new org.alice.ide.common.ExpressionPropertyPane( this, expressionProperty );
		if( org.alice.ide.IDE.getActiveInstance().isDropDownDesiredFor( expression ) ) {
			org.alice.ide.croquet.models.ast.cascade.ArgumentCascade model = org.alice.ide.croquet.models.ast.cascade.ArgumentCascade.getInstance( argument );
			org.alice.ide.codeeditor.ExpressionPropertyDropDownPane expressionPropertyDropDownPane = new org.alice.ide.codeeditor.ExpressionPropertyDropDownPane( model.getRoot().getPopupPrepModel(), prefixPane, rv, expressionProperty );
			rv = expressionPropertyDropDownPane;
		}
		return rv;
	}
	
	protected org.lgna.croquet.components.JComponent< ? > createInstanceCreationPane( org.lgna.project.ast.InstanceCreation instanceCreation ) {
		org.lgna.project.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
		if( constructor instanceof org.lgna.project.ast.AnonymousUserConstructor ) {
			return new org.alice.ide.common.AnonymousConstructorPane( this, (org.lgna.project.ast.AnonymousUserConstructor)constructor );
		} else {
			return new org.alice.ide.common.ExpressionPane( instanceCreation, this.createComponent( instanceCreation ) );
		}
	}
	protected org.lgna.croquet.components.JComponent< ? > createFieldAccessPane( org.lgna.project.ast.FieldAccess fieldAccess ) {
		org.lgna.croquet.components.JComponent< ? > rv;
		org.alice.ide.common.FieldAccessPane fieldAccessPane = new org.alice.ide.common.FieldAccessPane( this, fieldAccess );
		org.lgna.croquet.components.Component< ? > prefixPane = org.alice.ide.IDE.getActiveInstance().getPrefixPaneForFieldAccessIfAppropriate( fieldAccess );
		if( prefixPane != null ) {
			rv = new org.lgna.croquet.components.LineAxisPanel( prefixPane, fieldAccessPane );
		} else {
			rv = fieldAccessPane;
		}
		return rv;
	}
	
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createGetsComponent( boolean isTowardLeadingEdge ) {
		return new org.alice.ide.common.GetsPane( isTowardLeadingEdge );
	}
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createTypeComponent( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		return org.alice.ide.common.TypeComponent.createInstance( type );
	}

	protected org.lgna.croquet.components.JComponent< ? > createVariableDeclarationPane( org.lgna.project.ast.UserVariable userVariable ) {
		return new org.alice.ide.common.VariableDeclarationPane( userVariable, this.createVariablePane( userVariable ) );
	}
	protected org.lgna.croquet.components.JComponent< ? > createConstantDeclarationPane( org.lgna.project.ast.UserConstant userConstant ) {
		return new org.alice.ide.common.ConstantDeclarationPane( userConstant, this.createConstantPane( userConstant ) );
	}
	protected org.lgna.croquet.components.JComponent< ? > createVariablePane( org.lgna.project.ast.UserVariable userVariable ) {
		return new org.alice.ide.common.VariablePane( userVariable );
	}
	protected org.lgna.croquet.components.JComponent< ? > createConstantPane( org.lgna.project.ast.UserConstant userConstant ) {
		return new org.alice.ide.common.ConstantPane( userConstant );
	}
	protected org.lgna.croquet.components.JComponent< ? > createGenericNodePropertyPane( org.lgna.project.ast.NodeProperty< ? > nodeProperty ) {
		return new org.alice.ide.common.NodePropertyPane( this, nodeProperty );
	}
	public abstract org.lgna.croquet.components.JComponent< ? > createExpressionPropertyPane( org.lgna.project.ast.ExpressionProperty expressionProperty, org.lgna.project.ast.AbstractType< ?,?,? > type );
	public final org.lgna.croquet.components.JComponent< ? > createExpressionPropertyPane( org.lgna.project.ast.ExpressionProperty expressionProperty ) {
		return this.createExpressionPropertyPane( expressionProperty, null );
	}
	protected org.lgna.croquet.components.JComponent< ? > createResourcePropertyPane( org.lgna.project.ast.ResourceProperty resourceProperty ) {
		return new org.alice.ide.common.ResourcePropertyPane( this, resourceProperty );
	}
	protected org.lgna.croquet.components.JComponent< ? > createStatementListPropertyPane( org.lgna.project.ast.StatementListProperty statementListProperty ) {
		return new org.alice.ide.common.StatementListPropertyPane( this, statementListProperty );
	}
	protected abstract org.lgna.croquet.components.JComponent< ? > createArgumentListPropertyPane( org.lgna.project.ast.ArgumentListProperty argumentListProperty );
	protected org.lgna.croquet.components.JComponent< ? > createExpressionListPropertyPane( org.lgna.project.ast.ExpressionListProperty expressionListProperty ) {
		return new org.alice.ide.common.ExpressionListPropertyPane( this, expressionListProperty );
	}
	protected org.lgna.croquet.components.JComponent< ? > createGenericNodeListPropertyPane( org.lgna.project.ast.NodeListProperty< ? > nodeListProperty ) {
		return new org.alice.ide.common.DefaultNodeListPropertyPane( this, nodeListProperty );
	}
	protected org.lgna.croquet.components.JComponent< ? > createGenericListPropertyPane( edu.cmu.cs.dennisc.property.ListProperty< ? > listProperty ) {
		return new org.alice.ide.common.DefaultListPropertyPane( this, listProperty );
	}
	protected org.lgna.croquet.components.JComponent< ? > createGenericInstancePropertyPane( edu.cmu.cs.dennisc.property.InstanceProperty< ? > property ) {
		return new org.alice.ide.common.InstancePropertyPane( this, property );
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
	public org.lgna.croquet.components.JComponent< ? > createExpressionPane( org.lgna.project.ast.Expression expression ) {
//		java.awt.Component rv;
//		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
//			rv = new TypeComponent( ((edu.cmu.cs.dennisc.alice.ast.TypeExpression)expression).value.getValue() );
////		} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
////			rv = new FieldAccessPane( this, (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression );
//		} else {
//			rv = new ExpressionPane( this, expression );
//		}
		org.lgna.croquet.components.JComponent< ? > rv = org.alice.ide.IDE.getActiveInstance().getOverrideComponent( this, expression );
		if( rv != null ) {
			//pass
		} else {
			if( expression instanceof org.lgna.project.ast.InfixExpression ) {
				org.lgna.project.ast.InfixExpression infixExpression = (org.lgna.project.ast.InfixExpression)expression;
				String clsName = infixExpression.getClass().getName();
				java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( clsName, javax.swing.JComponent.getDefaultLocale() );
				
				
				
				//todo: investigate the need for this cast
				Enum e = (Enum)infixExpression.operator.getValue();
				
				
				
				String value = resourceBundle.getString( e.name() );
				org.alice.ide.i18n.Page page = new org.alice.ide.i18n.Page( value );
				org.lgna.croquet.components.JComponent< ? > component = this.createComponent( page, infixExpression );
				for( java.awt.Component child : component.getAwtComponent().getComponents() ) {
					if( child instanceof javax.swing.JLabel ) {
						javax.swing.JLabel label = (javax.swing.JLabel)child;
						String text = label.getText();
						//todo: remove this terrible hack
						boolean isScaleDesired = false;
						if( text.length() == 3 ) {
							char c0 = text.charAt( 0 );
							char c1 = text.charAt( 1 );
							char c2 = text.charAt( 2 );
							if( c0==' ' && c2 == ' ' ) {
								if( Character.isLetterOrDigit( c1 ) ) {
									//pass
								} else {
									isScaleDesired = true;
								}
							}
						} else if( text.length() == 4 ) {
							isScaleDesired = " >= ".equals( text ) || " <= ".equals( text ) || " == ".equals( text );
						}
						edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( label, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
						if( isScaleDesired ) {
							edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToScaledFont( label, 1.5f );
						}
						//label.setVerticalAlignment( javax.swing.SwingConstants.CENTER );
					}
				}
				rv = new org.alice.ide.common.ExpressionPane( infixExpression, component );
				
			} else if( expression instanceof org.alice.ide.ast.EmptyExpression ) {
				rv = new org.alice.ide.common.EmptyExpressionPane( (org.alice.ide.ast.EmptyExpression)expression );
			} else if( expression instanceof org.alice.ide.ast.PreviousValueExpression ) {
				rv = new org.alice.ide.common.PreviousValueExpressionPane( this, (org.alice.ide.ast.PreviousValueExpression)expression );
			} else if( expression instanceof org.alice.ide.ast.CurrentThisExpression ) {
				//todo
				rv = new org.alice.ide.common.ExpressionPane( expression, new org.lgna.croquet.components.Label( org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().getTextForThis() ) );
				rv.setBackgroundColor( org.alice.ide.IDE.getActiveInstance().getTheme().getColorFor( org.lgna.project.ast.ThisExpression.class ) );
			} else if( expression instanceof org.alice.ide.ast.SelectedInstanceFactoryExpression ) {
				//rv = new org.alice.ide.common.SelectedFieldExpressionPane( (org.alice.ide.ast.SelectedInstanceFactoryExpression)expression );
				rv = new org.alice.ide.common.SelectedInstanceFactoryExpressionPanel( this );
			} else if( expression instanceof org.lgna.project.ast.AssignmentExpression ) {
				rv = new org.alice.ide.common.AssignmentExpressionPane( this, (org.lgna.project.ast.AssignmentExpression)expression );
			} else if( expression instanceof org.lgna.project.ast.FieldAccess ) {
				rv = this.createFieldAccessPane( (org.lgna.project.ast.FieldAccess)expression );
			} else if( expression instanceof org.lgna.project.ast.TypeExpression ) {
				if( org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.getInstance().getSelectedItem().isTypeExpressionDesired() ) {
					
					rv = new org.lgna.croquet.components.LineAxisPanel(
							new org.alice.ide.common.DeclarationNameLabel( ((org.lgna.project.ast.TypeExpression)expression).value.getValue() ),
							new org.lgna.croquet.components.Label( "." )
					);
					//rv = TypeComponent.createInstance( ((edu.cmu.cs.dennisc.alice.ast.TypeExpression)expression).value.getValue() );
				} else {
					rv = new org.lgna.croquet.components.Label();
				}
			} else if( expression instanceof org.lgna.project.ast.InstanceCreation ) {
				rv = this.createInstanceCreationPane( (org.lgna.project.ast.InstanceCreation)expression );
//			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.AbstractLiteral ) {
//				rv = this.createComponent( expression );
			} else {
				org.lgna.croquet.components.JComponent< ? > component = this.createComponent( expression );
				if( org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState.getInstance().getValue() ) {
					rv = new org.alice.ide.common.ExpressionPane( expression, component );
				} else {
					rv = this.EPIC_HACK_createWrapperIfNecessaryForExpressionPanelessComponent( component );
				}
			}
		}
		return rv;
	}
	
	

	@Override
	protected org.lgna.croquet.components.JComponent< ? > createPropertyComponent( edu.cmu.cs.dennisc.property.InstanceProperty< ? > property, int underscoreCount ) {
		//todo:
		String propertyName = property.getName();
		//
		
		org.lgna.croquet.components.JComponent< ? > rv;
		if( underscoreCount == 2 ) {
			if( "variable".equals( propertyName ) ) {
				rv = this.createVariableDeclarationPane( (org.lgna.project.ast.UserVariable)property.getValue() );
			} else if( "constant".equals( propertyName ) ) {
				rv = this.createConstantDeclarationPane( (org.lgna.project.ast.UserConstant)property.getValue() );
			} else {
				rv = new org.lgna.croquet.components.Label( "TODO: handle underscore count 2: " + propertyName );
			}
		} else if( underscoreCount == 1 ) {
			if( "variable".equals( propertyName ) ) {
				rv = this.createVariablePane( (org.lgna.project.ast.UserVariable)property.getValue() );
			} else if( "constant".equals( propertyName ) ) {
				rv = this.createConstantPane( (org.lgna.project.ast.UserConstant)property.getValue() );
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
						} else if( property instanceof org.lgna.project.ast.ArgumentListProperty ) {
							rv = this.createArgumentListPropertyPane( (org.lgna.project.ast.ArgumentListProperty)property );
						} else if( property instanceof org.lgna.project.ast.ExpressionListProperty ) {
							rv = this.createExpressionListPropertyPane( (org.lgna.project.ast.ExpressionListProperty)property );
						} else {
							rv = this.createGenericNodeListPropertyPane( (org.lgna.project.ast.NodeListProperty< ? >)property );
						}
					} else {
						rv = this.createGenericListPropertyPane( (edu.cmu.cs.dennisc.property.ListProperty< ? >)property );
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
