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

/**
 * @author Dennis Cosgrove
 */
public class ParametersPane extends org.alice.ide.croquet.components.AbstractListPropertyPane<org.lgna.project.ast.NodeListProperty<org.lgna.project.ast.UserParameter>, org.lgna.project.ast.UserParameter> {
	public ParametersPane( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.UserCode code ) {
		super( factory, code.getRequiredParamtersProperty(), javax.swing.BoxLayout.LINE_AXIS );
	}

	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getActiveInstance();
	}

	private org.lgna.project.ast.UserCode getCode() {
		return (org.lgna.project.ast.UserCode)getProperty().getOwner();
	}

	@Override
	protected org.lgna.croquet.components.Component<?> createComponent( org.lgna.project.ast.UserParameter parameter ) {
		return new TypedParameterPane( getProperty(), parameter );
	}

	@Override
	protected void addPrefixComponents() {
		//super.addPrefixComponents();
		if( org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.isJava() ) {
			this.addComponent( new org.lgna.croquet.components.Label( "( " ) );
		} else {
			int n = this.getProperty().size();
			String text;
			switch( n ) {
			case 0:
				text = null;
				break;
			case 1:
				text = " with parameter: ";
				break;
			default:
				text = " with parameters: ";
			}
			if( text != null ) {
				this.addComponent( new org.lgna.croquet.components.Label( text, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT ) );
			}
		}
	}

	@Override
	protected org.lgna.croquet.components.Component<?> createInterstitial( int i, int N ) {
		if( i < ( N - 1 ) ) {
			return new org.lgna.croquet.components.Label( ", " );
		} else {
			return org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 4 );
		}
	}

	@Override
	protected void addPostfixComponents() {
		super.addPostfixComponents();
		org.lgna.project.ast.UserCode code = getCode();

		if( code instanceof org.lgna.project.ast.UserMethod ) {
			org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)code;
			if( org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().isSignatureLocked( method ) ) {
				//pass
			} else {
				this.addComponent( org.alice.ide.ast.declaration.AddParameterComposite.getInstance( method ).getOperation().createButton() );
			}
		}
		if( org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.isJava() ) {
			this.addComponent( new org.lgna.croquet.components.Label( " )" ) );
		}
		//this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 16 ) );
	}
}
