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
public class MethodHeaderPane extends AbstractCodeHeaderPane {
	public MethodHeaderPane( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice, ParametersPane parametersPane, boolean isPreview ) {
		super( methodDeclaredInAlice, parametersPane, isPreview );
//		edu.cmu.cs.dennisc.croquet.Application application = edu.cmu.cs.dennisc.croquet.Application.getSingleton();
		if( org.alice.ide.IDE.getSingleton().isJava() ) {
			this.addComponent( org.alice.ide.common.TypeComponent.createInstance( methodDeclaredInAlice.getReturnType() ) );
//			this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 8 ) );
		} else {
			this.addComponent( new edu.cmu.cs.dennisc.croquet.Label( "declare ", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE ) );
			StringBuffer sb = new StringBuffer();
			if( methodDeclaredInAlice.isProcedure() ) {
				sb.append( "procedure " );
			} else {
				this.addComponent( org.alice.ide.common.TypeComponent.createInstance( methodDeclaredInAlice.getReturnType() ) );
				sb.append( " function " );
			}
			this.addComponent( new edu.cmu.cs.dennisc.croquet.Label( sb.toString(), edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE ) );
		}
		
		
//		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 8 ) );
		org.alice.ide.common.DeclarationNameLabel nameLabel = new org.alice.ide.common.DeclarationNameLabel( methodDeclaredInAlice );
		nameLabel.scaleFont( NAME_SCALE );
		nameLabel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0,4,0,4 ) );

		if( methodDeclaredInAlice.isSignatureLocked.getValue() ) {
			this.addComponent( nameLabel );
		} else {
			class PopupPanel extends edu.cmu.cs.dennisc.croquet.ViewController<javax.swing.JPanel, edu.cmu.cs.dennisc.croquet.Model> {
				private edu.cmu.cs.dennisc.croquet.Component<?> centerComponent;
				public PopupPanel( edu.cmu.cs.dennisc.croquet.Component<?> centerComponent, edu.cmu.cs.dennisc.croquet.AbstractPopupMenuOperation popupMenuOperation ) {
					super( null );
					this.centerComponent = centerComponent;
					this.setPopupMenuOperation( popupMenuOperation );
				}
				@Override
				protected javax.swing.JPanel createAwtComponent() {
					javax.swing.JPanel rv = new javax.swing.JPanel() {
						@Override
						public java.awt.Dimension getMaximumSize() {
							return this.getPreferredSize();
						}
					};
					rv.setBackground( null );
					rv.setOpaque( false );
					rv.setLayout( new java.awt.BorderLayout() );
					rv.add( centerComponent.getAwtComponent(), java.awt.BorderLayout.CENTER );
					return rv;
				}
			}
			this.addComponent( 
					new PopupPanel( 
							nameLabel, 
							new edu.cmu.cs.dennisc.croquet.PopupMenuOperation(
									java.util.UUID.fromString( "e5c3fed5-6498-421e-9208-0484725adcef" ),
									new org.alice.ide.operations.ast.RenameMethodOperation( methodDeclaredInAlice ) 
			
							) 
					) 
			);
		}
		this.addParametersPaneAndInstanceLineIfDesired();
	}
}
