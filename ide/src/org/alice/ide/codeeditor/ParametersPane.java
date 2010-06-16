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
package org.alice.ide.codeeditor;

import edu.cmu.cs.dennisc.alice.ast.NodeListProperty;
import edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice;

/**
 * @author Dennis Cosgrove
 */
public class ParametersPane extends org.alice.ide.common.AbstractListPropertyPane< NodeListProperty< ParameterDeclaredInAlice >> {
	public ParametersPane( org.alice.ide.common.Factory factory, edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code ) {
		super( factory, javax.swing.BoxLayout.LINE_AXIS, code.getParamtersProperty() );
	}
	
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}

	private edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice getCode() {
		return (edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice)getProperty().getOwner();
	}

	@Override
	protected edu.cmu.cs.dennisc.croquet.Component< ? > createComponent( Object parameter ) {
		return new TypedParameterPane( getProperty(), (edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice)parameter );
	}
	@Override
	protected void addPrefixComponents() {
		//super.addPrefixComponents();
		if( getIDE().isJava() ) {
			this.addComponent( new edu.cmu.cs.dennisc.croquet.Label( "( " ) );
		} else {
			int n = this.getProperty().size();
			String text;
			switch( n ) {
			case 0:
				text = " ";
				break;
			case 1:
				text = " with parameter: ";
				break;
			default:
				text = " with parameters: ";
			}
			this.addComponent( new edu.cmu.cs.dennisc.croquet.Label( text, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE ) );
			this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 8 ) );
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component< ? > createInterstitial( int i, int N ) {
		if( i<N-1 ) {
			return new edu.cmu.cs.dennisc.croquet.Label( ", " );
		} else {
			return edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 8 );
		}
	}
	@Override
	protected void addPostfixComponents() {
		super.addPostfixComponents();
		edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice code = getCode();

		if( code instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)code;
			if( method.isSignatureLocked.getValue() ) {
				//pass
			} else {
				this.addComponent( org.alice.ide.operations.ast.DeclareMethodParameterOperation.getInstance( method ).createButton() );
			}
		}
		if( getIDE().isJava() ) {
			this.addComponent( new edu.cmu.cs.dennisc.croquet.Label( " )" ) );
		}
	}
	
	@Override
	protected void refresh() {
		super.refresh();
		org.alice.ide.IDE.getSingleton().refreshAccessibles();
	}
}
