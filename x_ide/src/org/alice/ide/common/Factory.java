/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.common;


/**
 * @author Dennis Cosgrove
 */
public abstract class Factory {
	//todo: remove
//	static java.util.Map< Object, String > operatorMap = new java.util.HashMap< Object, String >();
//	static {
//		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.PLUS, "+" );
//		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.MINUS, "-" );
//		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.TIMES, "\u00D7" );
//		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.REAL_DIVIDE, "\u00F7" );
//		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.LESS, "<" );
//		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.LESS_EQUALS, "\u2264" );
//		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.GREATER, ">" );
//		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.GREATER_EQUALS, "\u2265" );
//	}

	
	protected java.awt.Component createGetsComponent( boolean isTowardLeading ) { 
		return new org.alice.ide.common.GetsPane( isTowardLeading );
	}
	protected java.awt.Component createTextComponent( String text ) { 
		return edu.cmu.cs.dennisc.zoot.ZLabel.acquire( text );
	}
	public abstract java.awt.Component createExpressionPropertyPane( edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, java.awt.Component prefixPane, edu.cmu.cs.dennisc.alice.ast.AbstractType desiredValueType );
	public java.awt.Component createExpressionPropertyPane( edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, java.awt.Component prefixPane ) {
		return createExpressionPropertyPane( expressionProperty, prefixPane, null );
	}
	protected abstract java.awt.Component createArgumentListPropertyPane( edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty argumentListProperty );
	
	protected java.awt.Component createVariableDeclarationPane( edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variableDeclaredInAlice ) {
		return new VariableDeclarationPane( variableDeclaredInAlice );
	}
	protected java.awt.Component createConstantDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice constantDeclaredInAlice ) {
		return new ConstantDeclarationPane( constantDeclaredInAlice );
	}
	
	protected java.awt.Component createPropertyComponent( edu.cmu.cs.dennisc.property.InstanceProperty< ? > property, int underscoreCount ) {
		//todo:
		String propertyName = property.getName();
		//
		
		java.awt.Component rv;
		if( underscoreCount == 2 ) {
			if( "variable".equals( propertyName ) ) {
				rv = this.createVariableDeclarationPane( (edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice)property.getValue() );
			} else if( "constant".equals( propertyName ) ) {
				rv = this.createConstantDeclaredInAlice( (edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice)property.getValue() );
			} else {
				rv = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "TODO: handle underscore count 2: " + propertyName );
			}
		} else if( underscoreCount == 1 ) {
			if( "variable".equals( propertyName ) ) {
				rv = new VariablePane( (edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice)property.getValue() );
			} else if( "constant".equals( propertyName ) ) {
				rv = new ConstantPane( (edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice)property.getValue() );
			} else {
				rv = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "TODO: handle underscore count 1: " + propertyName );
			}
		} else {
			rv = null;
			//todo: remove
//			if( "operator".equals( propertyName ) ) {
//				String value = Factory.operatorMap.get( property.getValue() );
//				if( value != null ) {
//					zoot.ZLabel label = zoot.ZLabel.acquire( value, zoot.font.ZTextWeight.BOLD );
//					label.setFontToScaledFont( 1.5f );
//					rv = label;
//				}
//			}
			if( rv != null ) {
				//pass
			} else {
				if( property instanceof edu.cmu.cs.dennisc.alice.ast.NodeProperty< ? > ) {
					if( property instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionProperty ) {
						rv = this.createExpressionPropertyPane( (edu.cmu.cs.dennisc.alice.ast.ExpressionProperty)property, null );
					} else {
						rv = new NodePropertyPane( this, (edu.cmu.cs.dennisc.alice.ast.NodeProperty< ? >)property );
					}
				} else if( property instanceof edu.cmu.cs.dennisc.alice.ast.ResourceProperty ) {
					rv = new ResourcePropertyPane( this, (edu.cmu.cs.dennisc.alice.ast.ResourceProperty)property );
				} else if( property instanceof edu.cmu.cs.dennisc.property.ListProperty< ? > ) {
					if( property instanceof edu.cmu.cs.dennisc.alice.ast.NodeListProperty< ? > ) {
						if( property instanceof edu.cmu.cs.dennisc.alice.ast.StatementListProperty ) {
							rv = new StatementListPropertyPane( this, (edu.cmu.cs.dennisc.alice.ast.StatementListProperty)property );
						} else if( property instanceof edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty ) {
							rv = this.createArgumentListPropertyPane( (edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty)property );
						} else if( property instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty ) {
							rv = new ExpressionListPropertyPane( this, (edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty)property );
						} else {
							rv = new DefaultNodeListPropertyPane( this, (edu.cmu.cs.dennisc.alice.ast.NodeListProperty< ? >)property );
						}
					} else {
						rv = new DefaultListPropertyPane( this, (edu.cmu.cs.dennisc.property.ListProperty< ? >)property );
					}
				} else {
					rv = new InstancePropertyPane( this, property );
				}
				assert rv != null;
			}
		}
		return rv;
	}
	
	protected java.awt.Component createComponent( org.alice.ide.i18n.GetsChunk getsChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		return this.createGetsComponent( getsChunk.isTowardLeading() );
	}
	protected java.awt.Component createComponent( org.alice.ide.i18n.TextChunk textChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		edu.cmu.cs.dennisc.zoot.ZLabel rv = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( textChunk.getText() );
		return rv;
	}	
	protected java.awt.Component createComponent( org.alice.ide.i18n.PropertyChunk propertyChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		int underscoreCount = propertyChunk.getUnderscoreCount();
		String propertyName = propertyChunk.getPropertyName();
		edu.cmu.cs.dennisc.property.InstanceProperty< ? > property = owner.getInstancePropertyNamed( propertyName );
		return createPropertyComponent( property, underscoreCount );
	}
	protected java.awt.Component createComponent( org.alice.ide.i18n.MethodInvocationChunk methodInvocationChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		String methodName = methodInvocationChunk.getMethodName();
		java.awt.Component rv;
		if( owner instanceof edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration && methodName.equals( "getName" ) ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration declaration = (edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration)owner;
			org.alice.ide.common.DeclarationNameLabel label = new org.alice.ide.common.DeclarationNameLabel( declaration );
//			if( node instanceof edu.cmu.cs.dennisc.alice.ast.AbstractMethod ) {
//				label.setFontToScaledFont( 1.2f );
//			}
			rv = label;
		} else if( owner instanceof edu.cmu.cs.dennisc.alice.ast.Argument && methodName.equals( "getParameterNameText" ) ) {
			edu.cmu.cs.dennisc.alice.ast.Argument argument = (edu.cmu.cs.dennisc.alice.ast.Argument)owner;
			rv = new org.alice.ide.common.DeclarationNameLabel( argument.parameter.getValue() );
		} else if( owner instanceof edu.cmu.cs.dennisc.alice.ast.AbstractConstructor && methodName.equals( "getDeclaringType" ) ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor = (edu.cmu.cs.dennisc.alice.ast.AbstractConstructor)owner;
			rv = new org.alice.ide.common.TypeComponent( constructor.getDeclaringType() );
//		} else if( owner instanceof edu.cmu.cs.dennisc.alice.ast.ResourceExpression && methodName.equals( "getResourceName" ) ) {
//			edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = (edu.cmu.cs.dennisc.alice.ast.ResourceExpression)owner;
//			org.alice.virtualmachine.Resource resource = resourceExpression.resource.getValue();
//			rv = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "resource " + resource.getName() );
		} else {
			java.lang.reflect.Method mthd = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getMethod( owner.getClass(), methodName );
			Object o = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.invoke( owner, mthd );
			String s;
			if( o != null ) {
				if( o instanceof edu.cmu.cs.dennisc.alice.ast.AbstractType ) {
					s = ((edu.cmu.cs.dennisc.alice.ast.AbstractType)o).getName();
				} else {
					s = o.toString();
				}
			} else {
				s = null;
			}
			//s = "<html><h1>" + s + "</h1></html>";
			rv = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( s );
		}
		return rv;
	}

	protected java.awt.Component createComponent( org.alice.ide.i18n.Chunk chunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		if( chunk instanceof org.alice.ide.i18n.TextChunk ) {
			return createComponent( (org.alice.ide.i18n.TextChunk)chunk, owner );
		} else if( chunk instanceof org.alice.ide.i18n.PropertyChunk ) {
			return createComponent( (org.alice.ide.i18n.PropertyChunk)chunk, owner );
		} else if( chunk instanceof org.alice.ide.i18n.MethodInvocationChunk ) {
			return createComponent( (org.alice.ide.i18n.MethodInvocationChunk)chunk, owner );
		} else if( chunk instanceof org.alice.ide.i18n.GetsChunk ) {
			return createComponent( (org.alice.ide.i18n.GetsChunk)chunk, owner );
		} else {
			return edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "unhandled: " + chunk.toString() );
		}
	}
	protected int getPixelsPerIndent() {
		return 10;
	}
	protected java.awt.Component createComponent( org.alice.ide.i18n.Line line, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		int indentCount = line.getIndentCount();
		org.alice.ide.i18n.Chunk[] chunks = line.getChunks();
		assert chunks.length > 0;
		if( indentCount > 0 || chunks.length > 1 ) {
			edu.cmu.cs.dennisc.croquet.swing.LineAxisPane rv = new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane();
			if( indentCount > 0 ) {
				rv.add( javax.swing.Box.createHorizontalStrut( indentCount * this.getPixelsPerIndent() ) );
			}
			for( org.alice.ide.i18n.Chunk chunk : chunks ) {
				java.awt.Component component = createComponent( chunk, owner );
				assert component != null : chunk.toString();
//				rv.setAlignmentY( 0.5f );
				rv.add( component );
			}
			return rv;
		} else {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "skipping line" );
			java.awt.Component rv = createComponent( chunks[ 0 ], owner );
			assert rv != null : chunks[ 0 ].toString();
			return rv;
		}
	}
	protected java.awt.Component createComponent( org.alice.ide.i18n.Page page, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		org.alice.ide.i18n.Line[] lines = page.getLines();
		final int N = lines.length;
		assert N > 0;
		if( N > 1 ) {
			final boolean isLoop = lines[ N-1 ].isLoop();
			edu.cmu.cs.dennisc.croquet.swing.PageAxisPane pagePane = new edu.cmu.cs.dennisc.croquet.swing.PageAxisPane() {
				@Override
				public void paintComponent( java.awt.Graphics g ) {
					super.paintComponent( g );
					java.awt.Color prev = g.getColor();
					if( isLoop ) {
						int n = this.getComponentCount();
						java.awt.Component cFirst = this.getComponent( 0 );
						java.awt.Component cLast = this.getComponent( n-1 );
						g.setColor( edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 160 ) );
						int xB = Factory.this.getPixelsPerIndent();
						int xA = xB/2;
						int yTop = cFirst.getY() + cFirst.getHeight();
						int yBottom = cLast.getY() + cLast.getHeight()/2;
						g.drawLine( xA, yTop, xA, yBottom );
						g.drawLine( xA, yBottom, xB, yBottom );

						int xC = cLast.getX() + cLast.getWidth();
						int xD = xC + Factory.this.getPixelsPerIndent();;
						g.drawLine( xC, yBottom, xD, yBottom );
						g.drawLine( xD, yBottom, xD, cLast.getY() );
						
						final int HALF_TRIANGLE_WIDTH = 3;
						edu.cmu.cs.dennisc.awt.GraphicsUtilties.fillTriangle( g, edu.cmu.cs.dennisc.awt.GraphicsUtilties.Heading.NORTH, xA-HALF_TRIANGLE_WIDTH, yTop, HALF_TRIANGLE_WIDTH+1+HALF_TRIANGLE_WIDTH, 10 );
					}
					g.setColor( prev );
				}
			};
			for( org.alice.ide.i18n.Line line : lines ) {
				pagePane.add( createComponent( line, owner ) );
			}
			pagePane.revalidate();
			pagePane.repaint();
			return pagePane;
		} else {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "skipping page" );
			return createComponent( lines[ 0 ], owner );
		}
	}
	public java.awt.Component createComponent( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		java.awt.Component rv;
		if( owner != null ) {
//			if( owner instanceof org.alice.ide.ast.EmptyExpression ) {
//				rv = new EmptyExpressionPane( (org.alice.ide.ast.EmptyExpression)owner );
//			} else if( owner instanceof org.alice.ide.ast.SelectedFieldExpression ) {
//				rv = new SelectedFieldExpressionPane( (org.alice.ide.ast.SelectedFieldExpression)owner );
//			} else if( owner instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
//				rv = new FieldAccessPane( this, (edu.cmu.cs.dennisc.alice.ast.FieldAccess)owner );
//			} else if( owner instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
//				rv = new TypeComponent( ((edu.cmu.cs.dennisc.alice.ast.TypeExpression)owner).value.getValue() );
//			} else {
				Class< ? > cls = owner.getClass();
				String value = edu.cmu.cs.dennisc.util.ResourceBundleUtilities.getStringFromSimpleNames( cls, "edu.cmu.cs.dennisc.alice.ast.Templates" );
				org.alice.ide.i18n.Page page = new org.alice.ide.i18n.Page( value );
				rv = createComponent( page, owner );
//			}
		} else {
			//rv = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "todo: handle null" );
			rv = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( org.alice.ide.IDE.getSingleton().getTextForNull() );
		}
		return rv;
	}
	

	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.Statement, AbstractStatementPane > map = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.Statement, AbstractStatementPane >();
	public java.util.Map< edu.cmu.cs.dennisc.alice.ast.Statement, AbstractStatementPane > getStatementMap() {
		return this.map;
	}
	public AbstractStatementPane lookup( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		return this.map.get( statement );
	}
	public org.alice.ide.common.AbstractStatementPane createStatementPane( edu.cmu.cs.dennisc.alice.ast.Statement statement, edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty ) {
		org.alice.ide.common.AbstractStatementPane rv;
		if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionStatement ) {
			rv = new org.alice.ide.common.ExpressionStatementPane( this, (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)statement, statementListProperty );
		} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.Comment ) {
			rv = new org.alice.ide.codeeditor.CommentPane( this, (edu.cmu.cs.dennisc.alice.ast.Comment)statement, statementListProperty );
		} else {
			rv = new org.alice.ide.common.DefaultStatementPane( this, statement, statementListProperty );
		}
		return rv;
	}
	public final org.alice.ide.common.AbstractStatementPane createStatementPane( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		return this.createStatementPane( statement, null );
	}
//	public final java.awt.Component createExpressionPane( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
//		return new ExpressionPane( this, expression );
//	}
	
	
	protected java.awt.Component createFieldAccessPane( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
		java.awt.Component rv = new FieldAccessPane( this, fieldAccess );
		java.awt.Component prefixPane = org.alice.ide.IDE.getSingleton().getPrefixPaneForFieldAccessIfAppropriate( fieldAccess );
		if( prefixPane != null ) {
			rv = new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane( prefixPane, rv );
		}
		return rv;
	}
	protected java.awt.Component createInstanceCreationPane( edu.cmu.cs.dennisc.alice.ast.InstanceCreation instanceCreation ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
		if( constructor instanceof edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor ) {
			return new AnonymousConstructorPane( this, (edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor)constructor );
		} else {
			return new ExpressionPane( instanceCreation, this.createComponent( instanceCreation ) );
		}
	}

	public java.awt.Component createExpressionPane( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
//		java.awt.Component rv;
//		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
//			rv = new TypeComponent( ((edu.cmu.cs.dennisc.alice.ast.TypeExpression)expression).value.getValue() );
////		} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
////			rv = new FieldAccessPane( this, (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression );
//		} else {
//			rv = new ExpressionPane( this, expression );
//		}
		java.awt.Component rv = org.alice.ide.IDE.getSingleton().getOverrideComponent( this, expression );
		if( rv != null ) {
			//pass
		} else {
			if( expression instanceof edu.cmu.cs.dennisc.alice.ast.InfixExpression ) {
				edu.cmu.cs.dennisc.alice.ast.InfixExpression infixExpression = (edu.cmu.cs.dennisc.alice.ast.InfixExpression)expression;
				String clsName = infixExpression.getClass().getName();
				java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( clsName, javax.swing.JComponent.getDefaultLocale() );
				
				
				
				//todo: investigate the need for this cast
				Enum e = (Enum)infixExpression.operator.getValue();
				
				
				
				String value = resourceBundle.getString( e.name() );
				org.alice.ide.i18n.Page page = new org.alice.ide.i18n.Page( value );
				java.awt.Component component = this.createComponent( page, infixExpression );
				if( component instanceof java.awt.Container ) {
					java.awt.Container container = (java.awt.Container)component;
					for( java.awt.Component child : container.getComponents() ) {
						if( child instanceof edu.cmu.cs.dennisc.zoot.ZLabel ) {
							edu.cmu.cs.dennisc.zoot.ZLabel label = (edu.cmu.cs.dennisc.zoot.ZLabel)child;
							String text = label.getText();
							if( text.length() == 3 ) {
								char c0 = text.charAt( 0 );
								char c1 = text.charAt( 1 );
								char c2 = text.charAt( 2 );
								if( c0==' ' && c2 == ' ' ) {
									if( Character.isLetterOrDigit( c1 ) ) {
										//pass
									} else {
										label.setFontToScaledFont( 1.75f );
									}
								}
							}
							label.setFontToDerivedFont( edu.cmu.cs.dennisc.zoot.font.ZTextWeight.BOLD );
							//label.setVerticalAlignment( javax.swing.SwingConstants.CENTER );
						}
					}
				}
				rv = new ExpressionPane( infixExpression, component );
				
			} else if( expression instanceof org.alice.ide.ast.EmptyExpression ) {
				rv = new EmptyExpressionPane( (org.alice.ide.ast.EmptyExpression)expression );
			} else if( expression instanceof org.alice.ide.ast.SelectedFieldExpression ) {
				rv = new SelectedFieldExpressionPane( (org.alice.ide.ast.SelectedFieldExpression)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.AssignmentExpression ) {
				rv = new AssignmentExpressionPane( this, (edu.cmu.cs.dennisc.alice.ast.AssignmentExpression)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
				rv = this.createFieldAccessPane( (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
				rv = new TypeComponent( ((edu.cmu.cs.dennisc.alice.ast.TypeExpression)expression).value.getValue() );
			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.InstanceCreation ) {
				rv = this.createInstanceCreationPane( (edu.cmu.cs.dennisc.alice.ast.InstanceCreation)expression );
//			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.AbstractLiteral ) {
//				rv = this.createComponent( expression );
			} else {
				java.awt.Component component = this.createComponent( expression );
				if( org.alice.ide.IDE.getSingleton().isExpressionTypeFeedbackDesired() ) {
					rv = new ExpressionPane( expression, component );
				} else {
					rv = component;
				}
			}
		}
		return rv;
	}
}
