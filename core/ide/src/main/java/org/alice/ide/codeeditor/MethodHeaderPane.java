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
package org.alice.ide.codeeditor;

import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import org.alice.ide.common.TypeComponent;
import org.alice.ide.croquet.models.ast.MethodHeaderMenuModel;
import org.lgna.croquet.MenuModel;
import org.lgna.croquet.Model;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.ViewController;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dennis Cosgrove
 */
public class MethodHeaderPane extends AbstractCodeHeaderPane {
	private final org.alice.ide.x.AstI18nFactory factory;
	private final org.lgna.project.ast.UserMethod userMethod;

	public MethodHeaderPane( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.UserMethod userMethod, boolean isPreview ) {
		super( isPreview );
		this.factory = factory;
		this.userMethod = userMethod;
	}

	protected org.lgna.croquet.views.SwingComponentView<?> createNameLabel() {
		return factory.createNameView( userMethod );
	}

	@Override
	protected void internalRefresh() {
		super.internalRefresh();
		forgetAndRemoveAllComponents();

		if( org.alice.ide.croquet.models.ui.formatter.FormatterState.isJava() ) {
			addComponent( TypeComponent.createInstance( userMethod.getReturnType() ) );
		} else {
			addComponent( getDeclareLabel() );
			StringBuilder sb = new StringBuilder();
			if( userMethod.isStatic() ) {
				sb.append( "*" );
				sb.append( localize("static" ) );
				sb.append( "* " );
			}
			if( userMethod.isProcedure() ) {
				sb.append( localize("procedure" ) );
				sb.append( " " );
			} else {
				addComponent( TypeComponent.createInstance( userMethod.getReturnType() ) );
				sb.append( " " );
				sb.append( localize("function" ) );
				sb.append( " " );
			}
			addComponent( new Label( sb.toString(), TextPosture.OBLIQUE ) );
		}

		SwingComponentView<?> nameLabel = createNameLabel();
		nameLabel.scaleFont( NAME_SCALE );

		if( userMethod.isSignatureLocked.getValue() ) {
			addComponent( nameLabel );
		} else {
			addComponent( new PopupPanel( nameLabel, MethodHeaderMenuModel.getInstance( userMethod ).getPopupPrepModel() ) );
		}

		if( !isPreview() || ( userMethod.requiredParameters.size() > 0 ) ) {
			addComponent( new ParametersPane( factory, userMethod ) );
		}
	}

	class PopupPanel extends ViewController<JPanel, Model> {
		private AwtComponentView<?> centerComponent;

		PopupPanel( AwtComponentView<?> component, MenuModel.InternalPopupPrepModel popupMenuOperation ) {
			super( null );
			centerComponent = component;
			setPopupPrepModel( popupMenuOperation );
		}

		@Override
		protected JPanel createAwtComponent() {
			JPanel rv = new JPanel() {
				@Override
				public Dimension getMaximumSize() {
					return getPreferredSize();
				}
			};
			rv.setBackground( null );
			rv.setOpaque( false );
			rv.setLayout( new BorderLayout() );
			rv.add( centerComponent.getAwtComponent(), BorderLayout.CENTER );
			return rv;
		}
	}
}
