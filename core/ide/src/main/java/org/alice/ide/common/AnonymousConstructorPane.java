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

import org.alice.ide.ThemeUtilities;
import org.alice.ide.codeeditor.MethodHeaderPane;
import org.alice.ide.codeeditor.ParametersPane;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.x.AstI18nFactory;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.GridPanel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AnonymousUserConstructor;
import org.lgna.project.ast.AnonymousUserType;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.UserMethod;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.LayoutManager;

class MethodPane extends BorderPanel {
	public MethodPane( AstI18nFactory factory, UserMethod method ) {
		ParametersPane parametersPane = new ParametersPane( factory, method );
		this.addPageStartComponent( new MethodHeaderPane( factory, method, false ) );
		this.addLineStartComponent( BoxUtilities.createHorizontalSliver( 12 ) );
		this.addCenterComponent( new BodyPane( factory.createComponent( method.body.getValue() ) ) );
		this.setAlignmentX( Component.LEFT_ALIGNMENT );
		this.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackgroundColor( ThemeUtilities.getActiveTheme().getProcedureColor() );
	}

	@Override
	protected JPanel createJPanel() {
		class MouseEventEnabledPanel extends DefaultJPanel {
			public MouseEventEnabledPanel() {
				this.enableEvents( AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK );
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
	private AnonymousUserConstructor anonymousConstructor;

	public AnonymousConstructorPane( AstI18nFactory factory, AnonymousUserConstructor anonymousConstructor ) {
		super( null );
		this.anonymousConstructor = anonymousConstructor;
		boolean isJava = FormatterState.isJava();
		if( isJava ) {
			LineAxisPanel header = new LineAxisPanel(
					new Label( "new " ),
					TypeComponent.createInstance( anonymousConstructor.getDeclaringType().getSuperType() ),
					new Label( "() {" )
					);
			header.setAlignmentX( Component.LEFT_ALIGNMENT );
			this.addComponent( header );
		}

		AnonymousUserType type = this.anonymousConstructor.getDeclaringType();
		for( UserMethod method : type.getDeclaredMethods() ) {
			GridPanel pane = GridPanel.createGridPane( 1, 1 );
			int inset = 4;
			int left = 4;
			if( isJava ) {
				left += 12;
			}
			pane.setBorder( BorderFactory.createEmptyBorder( inset, left, inset, inset ) );
			pane.addComponent( new MethodPane( factory, method ) );
			this.addComponent( pane );
		}
		if( isJava ) {
			this.addComponent( new Label( "}" ) );
		}
		this.setBackgroundColor( ThemeUtilities.getActiveTheme().getColorFor( InstanceCreation.class ) );
	}

	@Override
	protected LayoutManager createLayoutManager( JPanel jComponent ) {
		return new BoxLayout( jComponent, BoxLayout.PAGE_AXIS );
	}

	@Override
	public AbstractType<?, ?, ?> getExpressionType() {
		return this.anonymousConstructor.getDeclaringType();
	}

	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		return true;
	}
}
