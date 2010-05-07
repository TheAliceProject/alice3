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
package org.alice.ide.common;

class MethodPane extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	public MethodPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
//		javax.swing.Box header = javax.swing.Box.createHorizontalBox();
//		header.add( zoot.ZLabel.acquire( "declare procedure " ) );
//		header.add( zoot.ZLabel.acquire( method.getName() ) );
//		header.setAlignmentX( 0.0f );
		edu.cmu.cs.dennisc.croquet.Application application = edu.cmu.cs.dennisc.croquet.Application.getSingleton();
		org.alice.ide.codeeditor.ParametersPane parametersPane = new org.alice.ide.codeeditor.ParametersPane( factory, method );
		this.addComponent( new org.alice.ide.codeeditor.MethodHeaderPane( method, parametersPane ), edu.cmu.cs.dennisc.croquet.BorderPanel.CardinalDirection.NORTH );
		this.addComponent( application.createHorizontalStrut( 12 ), edu.cmu.cs.dennisc.croquet.BorderPanel.CardinalDirection.WEST );
		this.addComponent( new BodyPane( factory.createComponent( method.body.getValue() ) ), edu.cmu.cs.dennisc.croquet.BorderPanel.CardinalDirection.CENTER );
		this.setAlignmentX( 0.0f );
		this.setOpaque( true );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getProcedureColor() );
		
		
		throw new RuntimeException( "todo: enableEvents" );
		//this.enableEvents( java.awt.AWTEvent.MOUSE_EVENT_MASK | java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class AnonymousConstructorPane extends ExpressionLikeSubstance {
	private edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor anonymousConstructor;
	public AnonymousConstructorPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor anonymousConstructor ) {
		this.anonymousConstructor = anonymousConstructor;
		if( getIDE().isJava() ) {
			edu.cmu.cs.dennisc.croquet.LineAxisPanel header = new edu.cmu.cs.dennisc.croquet.LineAxisPanel( 
					new edu.cmu.cs.dennisc.croquet.Label( "new " ),
					new TypeComponent( anonymousConstructor.getDeclaringType().getSuperType() ),
					new edu.cmu.cs.dennisc.croquet.Label( "() {" ) 
			);
			header.setAlignmentX( 0.0f );
			this.addComponent( header );
		}
		
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.anonymousConstructor.getDeclaringType();
		for( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method : type.getDeclaredMethods() ) {
			edu.cmu.cs.dennisc.croquet.GridPanel pane = edu.cmu.cs.dennisc.croquet.GridPanel.createGridPane( 1, 1 );
			int inset = 4;
			int left = 4;
			if( getIDE().isJava() ) {
				left += 12;
			}
			pane.setBorder( javax.swing.BorderFactory.createEmptyBorder( inset, left, inset, inset ) );
			pane.addComponent( new MethodPane( factory, (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)method ) );
			this.addComponent( pane );
		}
		if( getIDE().isJava() ) {
			this.addComponent( new edu.cmu.cs.dennisc.croquet.Label( "}" ) );
		}
		this.setBackgroundColor( org.alice.ide.IDE.getSingleton().getColorFor( edu.cmu.cs.dennisc.alice.ast.InstanceCreation.class ) );
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new javax.swing.BoxLayout( jPanel, javax.swing.BoxLayout.PAGE_AXIS );
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
