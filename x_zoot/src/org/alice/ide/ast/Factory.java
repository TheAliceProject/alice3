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
package org.alice.ide.ast;


/**
 * @author Dennis Cosgrove
 */
public abstract class Factory {
	protected java.awt.Component createComponent( org.alice.ide.i18n.GetsChunk getsChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		return new org.alice.ide.ast.GetsPane( getsChunk.isTowardLeading() );
	}
	protected java.awt.Component createComponent( org.alice.ide.i18n.TextChunk textChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		return new zoot.ZLabel( textChunk.getText() );
	}
	static java.util.Map< Object, String > operatorMap = new java.util.HashMap< Object, String >();
	static {
		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.PLUS, "+" );
		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.MINUS, "-" );
		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.TIMES, "*" );
		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.REAL_DIVIDE, "/" );
		//Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.REMAINDER, "% a.k.a. remainder" );
		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.LESS, "<" );
		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.LESS_EQUALS, "<=" );
		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.GREATER, ">" );
		Factory.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.GREATER_EQUALS, ">=" );
	}
	
	protected abstract javax.swing.JComponent createExpressionPropertyPane( edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty );
	protected abstract javax.swing.JComponent createArgumentListPropertyPane( edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty argumentListProperty );
	
	protected java.awt.Component createComponent( org.alice.ide.i18n.PropertyChunk propertyChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		boolean isBonusSpecified = propertyChunk.isBonusSpecified();
		String propertyName = propertyChunk.getPropertyName();
		javax.swing.JComponent rv;
		if( isBonusSpecified ) {
			class LocalTypedDeclarationPane extends TypedDeclarationPane {
				private edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice localDeclaredInAlice;
				public LocalTypedDeclarationPane( edu.cmu.cs.dennisc.alice.ast.LocalDeclaredInAlice localDeclaredInAlice ) {
					this.localDeclaredInAlice = localDeclaredInAlice;
				}
				@Override
				protected void handleAltTriggered( java.awt.event.MouseEvent e ) {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: popup menu" );
					//javax.swing.JPopupMenu popupMenu = getIDE().createJPopupMenu( new alice.ide.operations.ast.RenameLocalDeclarationOperation( this.localDeclaredInAlice ) );
					//popupMenu.show( e.getComponent(), e.getX(), e.getY() );
				}
			}
			
			if( propertyName.equals( "variable" ) ) {
				edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variableDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice)owner.getInstancePropertyNamed( propertyName ).getValue();
				rv = new LocalTypedDeclarationPane( variableDeclaredInAlice );
				rv.add( new org.alice.ide.ast.TypePane( variableDeclaredInAlice.valueType.getValue() ) );
				rv.add( new VariablePane( variableDeclaredInAlice ) );
			} else if( propertyName.equals( "constant" ) ) {
				edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice constantDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice)owner.getInstancePropertyNamed( propertyName ).getValue();
				rv = new LocalTypedDeclarationPane( constantDeclaredInAlice );
				rv.add( new org.alice.ide.ast.TypePane( constantDeclaredInAlice.valueType.getValue() ) );
				rv.add( new ConstantPane( constantDeclaredInAlice ) );
			} else {
				rv = new edu.cmu.cs.dennisc.moot.ZLabel( "TODO: " + propertyName );
			}
		} else {
			edu.cmu.cs.dennisc.property.InstanceProperty< ? > property = owner.getInstancePropertyNamed( propertyName );
			rv = null;
			if( "operator".equals( propertyName ) ) {
				String value = Factory.operatorMap.get( property.getValue() );
				if( value != null ) {
					edu.cmu.cs.dennisc.moot.ZLabel label = new edu.cmu.cs.dennisc.moot.ZLabel( value );
					label.setFontToScaledFont( 2.0f );
					rv = label;
				}
			}
			if( rv != null ) {
				//pass
			} else {
				if( property instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionProperty ) {
					rv = this.createExpressionPropertyPane( (edu.cmu.cs.dennisc.alice.ast.ExpressionProperty)property );
				} else if( property instanceof edu.cmu.cs.dennisc.alice.ast.NodeProperty< ? > ) {
					rv = new NodePropertyPane( (edu.cmu.cs.dennisc.alice.ast.NodeProperty< ? >)property );
				} else if( property instanceof edu.cmu.cs.dennisc.property.ListProperty< ? > ) {
					if( property instanceof edu.cmu.cs.dennisc.alice.ast.NodeListProperty< ? > ) {
						if( property instanceof edu.cmu.cs.dennisc.alice.ast.StatementListProperty ) {
							rv = new StatementListPropertyPane( (edu.cmu.cs.dennisc.alice.ast.StatementListProperty)property );
						} else if( property instanceof edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty ) {
							rv = this.createArgumentListPropertyPane( (edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty)property );
						} else if( property instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty ) {
							rv = new ExpressionListPropertyPane( (edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty)property );
						} else {
							rv = new DefaultNodeListPropertyPane( (edu.cmu.cs.dennisc.alice.ast.NodeListProperty< ? >)property );
						}
					} else {
						rv = new DefaultListPropertyPane( (edu.cmu.cs.dennisc.property.ListProperty< ? >)property );
					}
				} else {
					rv = new InstancePropertyPane( property );
				}
			}
		}
		return rv;
	}
	protected java.awt.Component createComponent( org.alice.ide.i18n.MethodInvocationChunk methodInvocationChunk, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		String methodName = methodInvocationChunk.getMethodName();
		javax.swing.JComponent rv;
		if( owner instanceof edu.cmu.cs.dennisc.alice.ast.Node && methodName.equals( "getName" ) ) {
			edu.cmu.cs.dennisc.alice.ast.Node node = (edu.cmu.cs.dennisc.alice.ast.Node)owner;
			org.alice.ide.ast.NodeNameLabel label = new org.alice.ide.ast.NodeNameLabel( node );
			if( node instanceof edu.cmu.cs.dennisc.alice.ast.AbstractMethod ) {
				label.setFontToScaledFont( 1.5f );
			}
			rv = label;
		} else if( owner instanceof edu.cmu.cs.dennisc.alice.ast.Argument && methodName.equals( "getParameterNameText" ) ) {
			edu.cmu.cs.dennisc.alice.ast.Argument argument = (edu.cmu.cs.dennisc.alice.ast.Argument)owner;
			rv = new org.alice.ide.ast.NodeNameLabel( argument.parameter.getValue() );
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
			rv = new zoot.ZLabel( s );
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
			return new zoot.ZLabel( "unhandled: " + chunk.toString() );
		}
	}
	protected int getPixelsPerIndent() {
		return 16;
	}
	protected java.awt.Component createComponent( org.alice.ide.i18n.Line line, edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		int indentCount = line.getIndentCount();
		org.alice.ide.i18n.Chunk[] chunks = line.getChunks();
		assert chunks.length > 0;
		if( indentCount > 0 || chunks.length > 1 ) {
			swing.LineAxisPane rv = new swing.LineAxisPane();
			if( indentCount > 0 ) {
				rv.add( javax.swing.Box.createHorizontalStrut( indentCount * this.getPixelsPerIndent() ) );
			}
			for( org.alice.ide.i18n.Chunk chunk : chunks ) {
				java.awt.Component component = createComponent( chunk, owner );
				assert component != null : chunk.toString();
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
		assert lines.length > 0;
		if( lines.length > 1 ) {
			swing.PageAxisPane pagePane = new swing.PageAxisPane();
			for( org.alice.ide.i18n.Line line : lines ) {
				pagePane.add( createComponent( line, owner ) );
			}
			return pagePane;
		} else {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "skipping page" );
			return createComponent( lines[ 0 ], owner );
		}
	}
	public java.awt.Component createComponent( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		Class< ? > cls = owner.getClass();
		String value = edu.cmu.cs.dennisc.util.ResourceBundleUtilities.getStringFromSimpleNames( cls, "edu.cmu.cs.dennisc.alice.ast.Templates" );
		org.alice.ide.i18n.Page page = new org.alice.ide.i18n.Page( value );
		return createComponent( page, owner );
	}
}
