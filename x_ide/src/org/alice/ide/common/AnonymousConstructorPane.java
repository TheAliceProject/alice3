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

class MethodPane extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
	public MethodPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
//		javax.swing.Box header = javax.swing.Box.createHorizontalBox();
//		header.add( zoot.ZLabel.acquire( "declare procedure " ) );
//		header.add( zoot.ZLabel.acquire( method.getName() ) );
//		header.setAlignmentX( 0.0f );
		org.alice.ide.codeeditor.ParametersPane parametersPane = new org.alice.ide.codeeditor.ParametersPane( factory, method );
		this.add( new org.alice.ide.codeeditor.MethodHeaderPane( method, parametersPane ), java.awt.BorderLayout.NORTH );
		this.add( javax.swing.Box.createHorizontalStrut( 12 ), java.awt.BorderLayout.WEST );
		this.add( new BodyPane( factory.createComponent( method.body.getValue() ) ), java.awt.BorderLayout.CENTER );
		this.setAlignmentX( 0.0f );
		this.setOpaque( true );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackground( org.alice.ide.IDE.getSingleton().getProcedureColor() );
		this.enableEvents( java.awt.AWTEvent.MOUSE_EVENT_MASK | java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class AnonymousConstructorPane extends ExpressionLikeSubstance {
	private edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor anonymousConstructor;
	public AnonymousConstructorPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor anonymousConstructor ) {
		this.anonymousConstructor = anonymousConstructor;
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.PAGE_AXIS ) );
		
		if( getIDE().isJava() ) {
			javax.swing.Box header = javax.swing.Box.createHorizontalBox();
			header.add( edu.cmu.cs.dennisc.croquet.CroquetUtilities.createLabel( "new " ) );
			header.add( new TypeComponent( anonymousConstructor.getDeclaringType().getSuperType() ) );
			header.add( edu.cmu.cs.dennisc.croquet.CroquetUtilities.createLabel( "() {" ) );
			header.setAlignmentX( 0.0f );
			this.add( header );
		}
		
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.anonymousConstructor.getDeclaringType();
		for( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method : type.getDeclaredMethods() ) {
			edu.cmu.cs.dennisc.croquet.swing.Pane pane = new edu.cmu.cs.dennisc.croquet.swing.Pane();
			pane.setLayout( new java.awt.GridLayout( 1, 1 ) );
			int inset = 4;
			int left = 4;
			if( getIDE().isJava() ) {
				left += 12;
			}
			pane.setBorder( javax.swing.BorderFactory.createEmptyBorder( inset, left, inset, inset ) );
			pane.add( new MethodPane( factory, (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)method ) );
			this.add( pane );
		}
		if( getIDE().isJava() ) {
			this.add( edu.cmu.cs.dennisc.croquet.CroquetUtilities.createLabel( "}" ) );
		}
		this.setBackground( org.alice.ide.IDE.getSingleton().getColorFor( edu.cmu.cs.dennisc.alice.ast.InstanceCreation.class ) );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		return this.anonymousConstructor.getDeclaringType();
	}
	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		return true;
	}
}
