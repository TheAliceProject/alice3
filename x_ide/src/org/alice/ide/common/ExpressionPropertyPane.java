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
public class ExpressionPropertyPane extends AbstractPropertyPane< edu.cmu.cs.dennisc.alice.ast.ExpressionProperty > {
//	private javax.swing.JComponent prefixPane;

	//	public ExpressionPropertyPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty property, boolean isDropDownPotentiallyDesired, javax.swing.JComponent prefixPane ) {
	//		super( factory, javax.swing.BoxLayout.LINE_AXIS, property );
	//		this.prefixPane = prefixPane;
	//		this.refresh();
	//	}
	public ExpressionPropertyPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty property ) {
		super( factory, javax.swing.BoxLayout.LINE_AXIS, property );
		this.refresh();
	}

	@Override
	protected void refresh() {
		//		if( this.isDropDownPotentiallyDesired != null ) {
		this.removeAll();
//		javax.swing.JComponent component;
//		edu.cmu.cs.dennisc.alice.ast.Expression expression = getProperty().getValue();
//		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
//			component = new TypeComponent( ((edu.cmu.cs.dennisc.alice.ast.TypeExpression)expression).value.getValue() );
//		} else {
//			if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
//				component = new FieldAccessPane( (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression );
//			} else if( expression instanceof org.alice.ide.ast.EmptyExpression ) {
//				component = new EmptyExpressionPane( (org.alice.ide.ast.EmptyExpression)expression );
//			} else if( expression instanceof org.alice.ide.ast.SelectedFieldExpression ) {
//				component = new SelectedFieldExpressionPane( (org.alice.ide.ast.SelectedFieldExpression)expression );
//			} else if( expression != null ) {
//				component = new ExpressionPane( this.getFactory(), expression );
//			} else {
//				component = new zoot.ZLabel( "todo: handle null" );
//			}
//			//				if( this.isDropDownPotentiallyDesired ) {
//			//					edu.cmu.cs.dennisc.alice.ast.ExpressionProperty property = this.getProperty();
//			//					edu.cmu.cs.dennisc.property.PropertyOwner owner = property.getOwner();	
//			//					if( owner instanceof edu.cmu.cs.dennisc.alice.ast.Argument ) {
//			//						edu.cmu.cs.dennisc.alice.ast.Argument argument = (edu.cmu.cs.dennisc.alice.ast.Argument)owner;
//			//						component = new ArgumentExpressionPropertyDropDownPane( component, this.getProperty(), this.prefixPane, argument );
//			//					} else {
//			//						component = new ExpressionPropertyDropDownPane( component, this.getProperty(), this.prefixPane );
//			//					}
//			//				}
//		}
//		this.add( component );
////				}
		this.add( this.getFactory().createExpressionPane( getProperty().getValue() ) );
		this.revalidate();
		this.repaint();
	}
}
