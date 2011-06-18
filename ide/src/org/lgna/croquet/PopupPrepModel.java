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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class PopupPrepModel extends PrepModel {
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			PopupPrepModel.this.fire( new org.lgna.croquet.triggers.ActionEventTrigger( e ) );
		}
	};
	public PopupPrepModel( java.util.UUID id ) {
		super( id );
	}
	
	@Override
	protected final void localize() {
		String name = this.getDefaultLocalizedText();
		if( name != null ) {
			this.setName( name );
//			this.setMnemonicKey( this.getLocalizedMnemonicKey() );
//			this.setAcceleratorKey( this.getLocalizedAcceleratorKeyStroke() );
		}
	}
	
	public javax.swing.Action getAction() {
		return this.action;
	}
	private String getName() {
		return String.class.cast( this.action.getValue( javax.swing.Action.NAME ) );
	}
	private void setName( String name ) {
		this.action.putValue( javax.swing.Action.NAME, name );
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
	
	public org.lgna.croquet.components.PopupButton createPopupButton() {
		return new org.lgna.croquet.components.PopupButton( this );
	}

	public static interface PerformObserver { 
		public void handleFinally(); 
	}
	protected abstract org.lgna.croquet.history.Step<?> perform( org.lgna.croquet.triggers.Trigger trigger, PerformObserver performObserver );
	
	@Override
	public org.lgna.croquet.history.Step<?> fire( org.lgna.croquet.triggers.Trigger trigger ) {
		if( this.isEnabled() ) {
			return this.perform( trigger, new PerformObserver() {
				public void handleFinally() {
					//todo?
				}
			} );
		} else {
			return null;
		}
	}
}
