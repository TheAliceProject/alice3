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

package org.lgna.croquet;

import org.lgna.croquet.triggers.ActionEventTrigger;
import org.lgna.croquet.triggers.EventObjectTrigger;
import org.lgna.croquet.triggers.Trigger;
import org.lgna.croquet.views.PopupButton;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonModel;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class PopupPrepModel extends AbstractPrepModel {
	public class SwingModel {
		private final Action action = new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent e ) {
				PopupPrepModel.this.fire( ActionEventTrigger.createUserInstance( e ) );
			}
		};

		public Action getAction() {
			return this.action;
		}
	}

	private final SwingModel swingModel = new SwingModel();

	public PopupPrepModel( UUID id ) {
		super( id );
	}

	@Override
	protected final void localize() {
		String name = this.findDefaultLocalizedText();
		if( name != null ) {
			this.setName( name );
			//			this.setMnemonicKey( this.getLocalizedMnemonicKey() );
			//			this.setAcceleratorKey( this.getLocalizedAcceleratorKeyStroke() );
		}
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}

	public String getName() {
		return String.class.cast( this.swingModel.action.getValue( Action.NAME ) );
	}

	public void setName( String name ) {
		this.swingModel.action.putValue( Action.NAME, name );
	}

	//	public void setShortDescription( String shortDescription ) {
	//		this.action.putValue( javax.swing.Action.SHORT_DESCRIPTION, shortDescription );
	//	}
	//	public void setLongDescription( String longDescription ) {
	//		this.action.putValue( javax.swing.Action.LONG_DESCRIPTION, longDescription );
	//	}
	//	public void setSmallIcon( javax.swing.Icon icon ) {
	//		this.action.putValue( javax.swing.Action.SMALL_ICON, icon );
	//	}
	//	public void setMnemonicKey( int mnemonicKey ) {
	//		this.action.putValue( javax.swing.Action.MNEMONIC_KEY, mnemonicKey );
	//	}
	//	public void setAcceleratorKey( javax.swing.KeyStroke acceleratorKey ) {
	//		this.action.putValue( javax.swing.Action.ACCELERATOR_KEY, acceleratorKey );
	//	}

	@Override
	public boolean isEnabled() {
		return this.swingModel.action.isEnabled();
	}

	@Override
	public void setEnabled( boolean isEnabled ) {
		this.swingModel.action.setEnabled( isEnabled );
	}

	public PopupButton createPopupButton() {
		return new PopupButton( this );
	}

	private ButtonModel prevButtonModel;

	protected void prologue( Trigger trigger ) {
		this.prevButtonModel = null;
		if( trigger instanceof EventObjectTrigger ) {
			EventObjectTrigger<?> eventTrigger = (EventObjectTrigger<?>)trigger;
			EventObject e = eventTrigger.getEvent();
			Object source = e.getSource();
			if( source instanceof AbstractButton ) {
				AbstractButton button = (AbstractButton)source;
				this.prevButtonModel = button.getModel();
			}
		}
		if( this.prevButtonModel != null ) {
			this.prevButtonModel.setPressed( true );
		}
	}

	protected void epilogue() {
		if( this.prevButtonModel != null ) {
			this.prevButtonModel.setSelected( false );
			this.prevButtonModel.setPressed( false );
		}
	}

	protected abstract void perform( Trigger trigger );

	@Override
	public void fire( Trigger trigger ) {
		perform( trigger );
	}
}
