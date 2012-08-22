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
	public MethodHeaderPane( org.lgna.project.ast.UserMethod userMethod, ParametersPane parametersPane, boolean isPreview, org.lgna.project.ast.UserType<?> declaringType ) {
		super( userMethod, parametersPane, isPreview );
		//		edu.cmu.cs.dennisc.croquet.Application application = edu.cmu.cs.dennisc.croquet.Application.getSingleton();
		if( org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState.isJava() ) {
			this.addComponent( org.alice.ide.common.TypeComponent.createInstance( userMethod.getReturnType() ) );
			//			this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 8 ) );
		} else {
			this.addComponent( new org.lgna.croquet.components.Label( "declare ", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE ) );
			StringBuffer sb = new StringBuffer();
			if( userMethod.isProcedure() ) {
				sb.append( "procedure " );
			} else {
				this.addComponent( org.alice.ide.common.TypeComponent.createInstance( userMethod.getReturnType() ) );
				sb.append( " function " );
			}
			this.addComponent( new org.lgna.croquet.components.Label( sb.toString(), edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE ) );
		}

		//		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 8 ) );
		org.alice.ide.ast.components.DeclarationNameLabel nameLabel = new org.alice.ide.ast.components.DeclarationNameLabel( userMethod );
		nameLabel.scaleFont( NAME_SCALE );
		nameLabel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 4, 0, 4 ) );

		if( userMethod.isSignatureLocked.getValue() ) {
			this.addComponent( nameLabel );
		} else {
			class PopupPanel extends org.lgna.croquet.components.ViewController<javax.swing.JPanel, org.lgna.croquet.Model> {
				private org.lgna.croquet.components.Component<?> centerComponent;

				public PopupPanel( org.lgna.croquet.components.Component<?> centerComponent, org.lgna.croquet.MenuModel.InternalPopupPrepModel popupMenuOperation ) {
					super( null );
					this.centerComponent = centerComponent;
					this.setPopupPrepModel( popupMenuOperation );
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
							org.alice.ide.croquet.models.ast.MethodHeaderMenuModel.getInstance( userMethod ).getPopupPrepModel()
					)
					);
		}
		this.addParametersPaneAndInstanceLineIfDesired();
		if( declaringType != null ) {
			//pass
		} else {
			declaringType = userMethod.getDeclaringType();
		}
		if( declaringType != null ) {
			if( declaringType instanceof org.lgna.project.ast.AnonymousUserType ) {
				//pass
			} else {
				this.addComponent( new org.lgna.croquet.components.Label( " on class ", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE ) );
				this.addComponent( org.alice.ide.common.TypeComponent.createInstance( declaringType ) );
			}
		}
	}

	public MethodHeaderPane( org.lgna.project.ast.UserMethod userMethod, ParametersPane parametersPane, boolean isPreview ) {
		this( userMethod, parametersPane, isPreview, null );
	}
}
