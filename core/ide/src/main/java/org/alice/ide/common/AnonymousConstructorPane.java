/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.common;

class MethodPane extends org.lgna.croquet.views.BorderPanel {
	public MethodPane( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.UserMethod method ) {
		org.alice.ide.codeeditor.ParametersPane parametersPane = new org.alice.ide.codeeditor.ParametersPane( factory, method );
		this.addPageStartComponent( new org.alice.ide.codeeditor.MethodHeaderPane( factory, method, false ) );
		this.addLineStartComponent( org.lgna.croquet.views.BoxUtilities.createHorizontalSliver( 12 ) );
		this.addCenterComponent( new BodyPane( factory.createComponent( method.body.getValue() ) ) );
		this.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackgroundColor( org.alice.ide.ThemeUtilities.getActiveTheme().getProcedureColor() );
	}

	@Override
	protected javax.swing.JPanel createJPanel() {
		class MouseEventEnabledPanel extends DefaultJPanel {
			public MouseEventEnabledPanel() {
				this.enableEvents( java.awt.AWTEvent.MOUSE_EVENT_MASK | java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK );
			}
		}
		MouseEventEnabledPanel rv = new MouseEventEnabledPanel();
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class AnonymousConstructorPane extends ExpressionLikeSubstance {
	private org.lgna.project.ast.AnonymousUserConstructor anonymousConstructor;

	public AnonymousConstructorPane( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.AnonymousUserConstructor anonymousConstructor ) {
		super( null );
		this.anonymousConstructor = anonymousConstructor;
		boolean isJava = org.alice.ide.croquet.models.ui.formatter.FormatterState.isJava();
		if( isJava ) {
			org.lgna.croquet.views.LineAxisPanel header = new org.lgna.croquet.views.LineAxisPanel(
					new org.lgna.croquet.views.Label( "new " ),
					TypeComponent.createInstance( anonymousConstructor.getDeclaringType().getSuperType() ),
					new org.lgna.croquet.views.Label( "() {" )
					);
			header.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
			this.addComponent( header );
		}

		org.lgna.project.ast.AnonymousUserType type = this.anonymousConstructor.getDeclaringType();
		for( org.lgna.project.ast.UserMethod method : type.getDeclaredMethods() ) {
			org.lgna.croquet.views.GridPanel pane = org.lgna.croquet.views.GridPanel.createGridPane( 1, 1 );
			int inset = 4;
			int left = 4;
			if( isJava ) {
				left += 12;
			}
			pane.setBorder( javax.swing.BorderFactory.createEmptyBorder( inset, left, inset, inset ) );
			pane.addComponent( new MethodPane( factory, method ) );
			this.addComponent( pane );
		}
		if( isJava ) {
			this.addComponent( new org.lgna.croquet.views.Label( "}" ) );
		}
		this.setBackgroundColor( org.alice.ide.ThemeUtilities.getActiveTheme().getColorFor( org.lgna.project.ast.InstanceCreation.class ) );
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jComponent ) {
		return new javax.swing.BoxLayout( jComponent, javax.swing.BoxLayout.PAGE_AXIS );
	}

	@Override
	public org.lgna.project.ast.AbstractType<?, ?, ?> getExpressionType() {
		return this.anonymousConstructor.getDeclaringType();
	}

	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		return true;
	}
}
