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

package org.alice.stageide.custom;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.IDE;
import org.alice.ide.ast.ExpressionCreator;
import org.alice.ide.custom.CustomExpressionCreatorComposite;
import org.alice.ide.custom.components.CustomExpressionCreatorView;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.history.TransactionManager;
import org.lgna.croquet.triggers.ChangeEventTrigger;
import org.lgna.croquet.views.Dialog;
import org.lgna.croquet.views.SwingAdapter;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.Expression;
import org.lgna.story.Color;
import org.lgna.story.EmployeesOnly;

import javax.swing.JColorChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ColorCustomExpressionCreatorComposite extends CustomExpressionCreatorComposite<CustomExpressionCreatorView> {
	private static class SingletonHolder {
		private static ColorCustomExpressionCreatorComposite instance = new ColorCustomExpressionCreatorComposite();
	}

	public static ColorCustomExpressionCreatorComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final JColorChooser jColorChooser = new JColorChooser();
	private final ChangeListener changeListener = new ChangeListener() {
		@Override
		public void stateChanged( ChangeEvent e ) {
			TransactionManager.TODO_REMOVE_fireEvent( ChangeEventTrigger.createUserInstance( e ) );
		}
	};

	private ColorCustomExpressionCreatorComposite() {
		super( UUID.fromString( "6a187cbd-d41f-4513-aa5e-e8750d1d921f" ) );
	}

	@Override
	protected CustomExpressionCreatorView createView() {
		class ColorCustomExpressionCreatorView extends CustomExpressionCreatorView {
			public ColorCustomExpressionCreatorView( ColorCustomExpressionCreatorComposite composite ) {
				super( composite );
			}

			@Override
			protected SwingComponentView<?> createMainComponent() {
				return new SwingAdapter( jColorChooser );
			}
		}
		return new ColorCustomExpressionCreatorView( this );
	}

	@Override
	protected Expression createValue() {
		java.awt.Color awtColor = this.jColorChooser.getColor();
		IDE ide = IDE.getActiveInstance();
		ExpressionCreator expressionCreator = ide.getApiConfigurationManager().getExpressionCreator();
		Color color = EmployeesOnly.createColor( awtColor );
		try {
			return expressionCreator.createExpression( color );
		} catch( ExpressionCreator.CannotCreateExpressionException ccee ) {
			Logger.throwable( ccee, color );
			return null;
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}

	@Override
	protected void initializeToPreviousExpression( Expression expression ) {
		if( expression != null ) {
			try {
				Color color = IDE.getActiveInstance().getSceneEditor().getInstanceInJavaVMForExpression( expression, Color.class );
				if( color != null ) {
					Color4f color4f = EmployeesOnly.getColor4f( color );
					this.jColorChooser.setColor( ColorUtilities.toAwtColor( color4f ) );
				}
			} catch( Throwable t ) {
				Logger.throwable( t, expression );
			}
		}
	}

	@Override
	protected void handlePreShowDialog( Dialog dialog, CompletionStep<?> step ) {
		super.handlePreShowDialog( dialog, step );
		this.jColorChooser.getSelectionModel().addChangeListener( this.changeListener );
	}

	@Override
	protected void handlePostHideDialog( CompletionStep<?> completionStep ) {
		this.jColorChooser.getSelectionModel().removeChangeListener( this.changeListener );
		super.handlePostHideDialog( completionStep );
	}
}
