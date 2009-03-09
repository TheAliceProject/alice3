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
package alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class PropertyChunk extends Chunk {
	private String propertyName;
	private boolean isBonusSpecified;
	public PropertyChunk( String propertyName ) {
		this.isBonusSpecified = propertyName.startsWith( "_" ) && propertyName.endsWith( "_" );
		if( this.isBonusSpecified ) {
			this.propertyName = propertyName.substring( 1, propertyName.length()-1 );
		} else {
			this.propertyName = propertyName;
		}
	}
//	if( owner instanceof edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice ) {
//		rv = new VariablePane( (edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice)owner );
//	} else if( owner instanceof edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice ) {
//		rv = new ConstantPane( (edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice)owner );
//	} else {
//		rv = new edu.cmu.cs.dennisc.moot.ZLabel( "should never happen" );
//	}
	static java.util.Map< Object, String > operatorMap = new java.util.HashMap< Object, String >();
	static {
		PropertyChunk.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.PLUS, "+" );
		PropertyChunk.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.MINUS, "-" );
		PropertyChunk.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.TIMES, "*" );
		PropertyChunk.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.REAL_DIVIDE, "/" );
		//PropertyChunk.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.REMAINDER, "% a.k.a. remainder" );
		PropertyChunk.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.LESS, "<" );
		PropertyChunk.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.LESS_EQUALS, "<=" );
		PropertyChunk.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.GREATER, ">" );
		PropertyChunk.operatorMap.put( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.GREATER_EQUALS, ">=" );
	}
	@Override
	public javax.swing.JComponent createComponent( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner ) {
		javax.swing.JComponent rv;
		if( this.isBonusSpecified ) {
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
			
			if( this.propertyName.equals( "variable" ) ) {
				edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variableDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice)owner.getInstancePropertyNamed( this.propertyName ).getValue();
				rv = new LocalTypedDeclarationPane( variableDeclaredInAlice );
				rv.add( new alice.ide.ast.TypePane( variableDeclaredInAlice.valueType.getValue() ) );
				rv.add( new VariablePane( variableDeclaredInAlice ) );
			} else if( this.propertyName.equals( "constant" ) ) {
				edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice constantDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice)owner.getInstancePropertyNamed( this.propertyName ).getValue();
				rv = new LocalTypedDeclarationPane( constantDeclaredInAlice );
				rv.add( new alice.ide.ast.TypePane( constantDeclaredInAlice.valueType.getValue() ) );
				rv.add( new ConstantPane( constantDeclaredInAlice ) );
			} else {
				rv = new edu.cmu.cs.dennisc.moot.ZLabel( "TODO: " + this.propertyName );
			}
		} else {
			edu.cmu.cs.dennisc.property.InstanceProperty< ? > property = owner.getInstancePropertyNamed( this.propertyName );
			rv = null;
			if( "operator".equals( this.propertyName ) ) {
				String value = PropertyChunk.operatorMap.get( property.getValue() );
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
					rv = new ExpressionPropertyPane( (edu.cmu.cs.dennisc.alice.ast.ExpressionProperty)property, true );
				} else if( property instanceof edu.cmu.cs.dennisc.alice.ast.NodeProperty< ? > ) {
					rv = new NodePropertyPane( (edu.cmu.cs.dennisc.alice.ast.NodeProperty< ? >)property );
				} else if( property instanceof edu.cmu.cs.dennisc.property.ListProperty< ? > ) {
					if( property instanceof edu.cmu.cs.dennisc.alice.ast.NodeListProperty< ? > ) {
						if( property instanceof edu.cmu.cs.dennisc.alice.ast.StatementListProperty ) {
							rv = new StatementListPropertyPane( (edu.cmu.cs.dennisc.alice.ast.StatementListProperty)property );
						} else if( property instanceof edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty ) {
							rv = new ArgumentListPropertyPane( (edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty)property );
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
	@Override
	protected java.lang.StringBuffer updateRepr( java.lang.StringBuffer rv ) {
		rv.append( "propertyName=" );
		rv.append( this.propertyName );
		return rv;
	}
}
