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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class Operation< C extends OperationContext<?>> extends Model {
	private class ButtonActionListener implements java.awt.event.ActionListener {
		private AbstractButton< ?,? > button;
		public ButtonActionListener( AbstractButton< ?,? > button ) {
			this.button = button;
		}
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			Operation.this.fire( e, this.button );
		}
	}
	private java.util.Map< AbstractButton< ?,? >, ButtonActionListener > mapButtonToListener = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
	protected abstract C createContext( ModelContext<?> parent, java.util.EventObject e, ViewController< ?, ? > viewController );

	public C fire( java.util.EventObject e, ViewController< ?, ? > viewController ) {
		if( this.isEnabled() ) {
			Application application = Application.getSingleton();
			ModelContext<?> parentContext = application.getCurrentContext();
			return this.handleFire(parentContext, e, viewController);
		} else {
			return null;
		}
	}
	@Deprecated
	public C fire( java.util.EventObject e ) {
		return fire( e, null );
	}
	@Deprecated
	public C fire() {
		return fire( null );
	}
	
	
	
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
		}
	};
	public Operation( Group group, java.util.UUID individualUUID ) {
		super( group, individualUUID );
		this.localize();
	}
	
	@Override
	/*package-private*/ void localize() {
		this.setName( this.getDefaultLocalizedText() );
		this.setMnemonicKey( this.getLocalizedMnemonicKey() );
		this.setAcceleratorKey( this.getLocalizedAcceleratorKeyStroke() );
	}
	
	/*package-private*/ final C handleFire( ModelContext<?> parentContext, java.util.EventObject e, ViewController< ?, ? > viewController ) {
		C childContext = this.createContext( parentContext, e, viewController );
		this.perform( childContext );
		return childContext;
	}
	protected abstract void perform( C context );
	//protected abstract void perform( ModelContext context, java.util.EventObject e, Component<?> component );

	public String getName() {
		return String.class.cast( this.action.getValue( javax.swing.Action.NAME ) );
	}
	public void setName( String name ) {
		this.action.putValue( javax.swing.Action.NAME, name );
	}
//	public String getShortDescription() {
//		return String.class.cast( this.action.getValue( javax.swing.Action.SHORT_DESCRIPTION ) );
//	}
	public void setShortDescription( String shortDescription ) {
		this.action.putValue( javax.swing.Action.SHORT_DESCRIPTION, shortDescription );
	}
//	public String getLongDescription() {
//		return String.class.cast( this.action.getValue( javax.swing.Action.LONG_DESCRIPTION ) );
//	}
	public void setLongDescription( String longDescription ) {
		this.action.putValue( javax.swing.Action.LONG_DESCRIPTION, longDescription );
	}
	public javax.swing.Icon getSmallIcon() {
		return javax.swing.Icon.class.cast( this.action.getValue( javax.swing.Action.SMALL_ICON ) );
	}
	public void setSmallIcon( javax.swing.Icon icon ) {
		this.action.putValue( javax.swing.Action.SMALL_ICON, icon );
	}
//	public int getMnemonicKey() {
//		return Integer.class.cast( this.action.getValue( javax.swing.Action.MNEMONIC_KEY ) );
//	}
	private void setMnemonicKey( int mnemonicKey ) {
		this.action.putValue( javax.swing.Action.MNEMONIC_KEY, mnemonicKey );
	}
//	public javax.swing.KeyStroke getAcceleratorKey() {
//		return javax.swing.KeyStroke.class.cast( this.action.getValue( javax.swing.Action.ACCELERATOR_KEY ) );
//	}
	private void setAcceleratorKey( javax.swing.KeyStroke acceleratorKey ) {
		this.action.putValue( javax.swing.Action.ACCELERATOR_KEY, acceleratorKey );
	}


	/*package-private*/ void addButton(OperationButton<?,?> button) {
		button.setAction( Operation.this.action );
//			rv.setModel( this.buttonModel );
		assert Operation.this.mapButtonToListener.containsKey( button ) == false;
		ButtonActionListener buttonActionListener = new ButtonActionListener( button );
		Operation.this.mapButtonToListener.put( button, buttonActionListener );
		button.getAwtComponent().addActionListener( buttonActionListener );
		this.addComponent(button);
	}
	/*package-private*/ void removeButton(OperationButton<?,?> button) {
		this.removeComponent(button);
		ButtonActionListener buttonActionListener = Operation.this.mapButtonToListener.get( button );
		assert buttonActionListener != null;
		button.getAwtComponent().removeActionListener( buttonActionListener );
		mapButtonToListener.remove( button );
//		rv.setModel( null );
		button.setAction( null );
	}
	
	public Button createButton() {
		return new Button( this );
	}
	public Hyperlink createHyperlink() {
		return new Hyperlink( this );
	}
	public MenuItem createMenuItem() {
		return new MenuItem( this );
	}
}
