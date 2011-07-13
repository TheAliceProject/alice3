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
package org.alice.ide.declarationpanes;


/**
 * @author Dennis Cosgrove
 */
public class CreateMethodParameterPane extends CreateParameterPane {
	private java.util.List< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > methodInvocations;
	public CreateMethodParameterPane( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method, java.util.List< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > methodInvocations ) {
		super( method );
		this.methodInvocations = methodInvocations;
	}
	@Override
	protected org.lgna.croquet.components.Component< ? >[] createWarningRow() {
		final int N = this.methodInvocations.size();
		if( N > 0 ) {

			String codeText;
//			if( code instanceof edu.cmu.cs.dennisc.alice.ast.AbstractMethod ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)this.getCode();
				if( method.isProcedure() ) {
					codeText = "procedure";
				} else {
					codeText = "function";
				}
//			} else {
//				codeText = "constructor";
//			}
			
			String text = "I understand that I need to update the invocations to this " + codeText + ".";
			org.lgna.croquet.BooleanState isUnderstandingConfirmed = new org.lgna.croquet.BooleanState( org.alice.ide.ProjectApplication.UI_STATE_GROUP, java.util.UUID.fromString( "21efac8d-c2dd-451f-8065-d2e284a3e244" ), false ) {};
			isUnderstandingConfirmed.setTextForBothTrueAndFalse( text );
			org.lgna.croquet.components.CheckBox checkBox = isUnderstandingConfirmed.createCheckBox();
			checkBox.setBackgroundColor( null );
			
			StringBuffer sb = new StringBuffer();
			sb.append( "<html><body>There " );
			if( N == 1 ) {
				sb.append( "is 1 invocation" );
			} else {
				sb.append( "are " );
				sb.append( N );
				sb.append( " invocations" );
			}
			sb.append( " to this " );
			sb.append( codeText );
			sb.append( " in your program.<br>You will need to fill in " );
			if( N == 1 ) {
				sb.append( "a value" );
			} else {
				sb.append( "values" );
			}
			sb.append( " for the new " );
			if( N == 1 ) {
				sb.append( "argument at the" );
			} else {
				sb.append( "arguments at each" );
			}
			sb.append( " invocation.</body></html>" );

			org.lgna.croquet.components.PageAxisPanel pane = new org.lgna.croquet.components.PageAxisPanel();
			pane.addComponent( new org.lgna.croquet.components.Label( sb.toString() ) );
			pane.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 8 ) );
			pane.addComponent( new org.lgna.croquet.components.LineAxisPanel( new org.lgna.croquet.components.Label( "Tip: look for " ), org.alice.ide.IDE.getActiveInstance().getPreviewFactory().createExpressionPane( new edu.cmu.cs.dennisc.alice.ast.NullLiteral() ) ) );
			pane.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 8 ) );
			pane.addComponent( checkBox );
			return new org.lgna.croquet.components.Component< ? >[] { new org.lgna.croquet.components.Label( "WARNING:" ), pane };
		} else {
			return null;
		}
	}
}
