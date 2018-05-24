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
package org.lgna.croquet.color;

import org.lgna.croquet.AbstractSeverityStatusComposite;
import org.lgna.croquet.Application;
import org.lgna.croquet.Element;
import org.lgna.croquet.SimpleOperationInputDialogCoreComposite;
import org.lgna.croquet.color.views.ColorChooserDialogCoreView;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.views.Dialog;

import javax.swing.JColorChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class ColorChooserDialogCoreComposite extends SimpleOperationInputDialogCoreComposite<ColorChooserDialogCoreView> {
	private final ColorState colorState;

	/* package-private */ColorChooserDialogCoreComposite( ColorState colorState ) {
		super( UUID.fromString( "0a29c940-b819-41a2-8ed9-683f80b0ba69" ), Application.INHERIT_GROUP );
		this.colorState = colorState;
	}

	@Override
	protected Class<? extends Element> getClassUsedForLocalization() {
		return this.colorState.getClass();
	}

	@Override
	protected String getSubKeyForLocalization() {
		return "chooserDialogCoreComposite";
	}

	@Override
	protected ColorChooserDialogCoreView createView() {
		return new ColorChooserDialogCoreView( this );
	}

	@Override
	protected AbstractSeverityStatusComposite.Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}

	private Color preColor;

	private final ChangeListener changeListener = new ChangeListener() {
		@Override
		public void stateChanged( ChangeEvent e ) {
			handleStateChanged();
		}
	};

	private void handleStateChanged() {
		JColorChooser jColorChooser = this.getView().getAwtComponent();
		Color awtColor = jColorChooser.getSelectionModel().getSelectedColor();
		//todo
		this.colorState.setValueTransactionlessly( awtColor );
	}

	@Override
	protected void handlePreShowDialog( CompletionStep<?> completionStep ) {
		this.preColor = this.colorState.getValue();
		this.getView().setSelectedColor( preColor );

		JColorChooser jColorChooser = this.getView().getAwtComponent();
		jColorChooser.getSelectionModel().addChangeListener( this.changeListener );
		super.handlePreShowDialog( completionStep );
	}

	@Override
	protected void handleFinally( CompletionStep<?> step, Dialog dialog ) {
		JColorChooser jColorChooser = this.getView().getAwtComponent();
		jColorChooser.getSelectionModel().removeChangeListener( this.changeListener );
		if( step.isCanceled() ) {
			this.colorState.setValueTransactionlessly( this.preColor );
		}
		super.handleFinally( step, dialog );
	}

	@Override
	protected Edit createEdit( CompletionStep<?> completionStep ) {
		Color color = this.getView().getSelectedColor();
		if( color != null ) {
			this.colorState.setValueTransactionlessly( color );
		}
		return null;
	}

	public void addSubComposite( ColorChooserTabComposite<?> composite ) {
		composite.initializeIfNecessary();
		this.getView().addColorChooserTabView( composite.getView() );
	}

	public void removeSubComposite( ColorChooserTabComposite<?> composite ) {
		this.getView().removeColorChooserTabView( composite.getView() );
	}
}
