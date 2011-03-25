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
public abstract class Operation< C extends OperationContext<? extends Operation<?>>> extends EditSource {
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
	
	public abstract C createAndPushContext( java.util.EventObject e, ViewController< ?, ? > viewController );

	public String getTutorialStartNoteText( OperationContext< ? > operationContext, UserInformation userInformation ) {
		return "Press " + this.getTutorialNoteText( operationContext, userInformation );
	}

	@Override
	public String getTutorialNoteText( AbstractModelContext< ? > modelContext, UserInformation userInformation ) {
		return "<strong>" + this.getName() + "</strong>";
	}
	
	protected Edit< ? > createTutorialCompletionEdit( Edit< ? > originalEdit, edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		return null;
	}
	@Override
	public Edit< ? > commitTutorialCompletionEdit( Edit< ? > originalEdit, edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		Edit< ? > replacementEdit = this.createTutorialCompletionEdit( originalEdit, retargeter );
		if( replacementEdit != null ) {
			final C childContext = this.createAndPushContext( null, null );
			try {
				childContext.commitAndInvokeDo( replacementEdit );
			} finally {
				AbstractModelContext< ? > popContext = ContextManager.popContext();
				assert popContext == childContext : popContext.getClass() + " " + childContext.getClass();
			}
		} else {
			System.err.println( "createTutorialCompletionEdit returned null" );
		}
		return replacementEdit;
	}

	public C fire( java.util.EventObject e, ViewController< ?, ? > viewController ) {
		if( this.isEnabled() ) {
			return this.handleFire(e, viewController);
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
	public Operation( Group group, java.util.UUID id ) {
		super( group, id );
	}
	
	@Override
	protected void localize() {
		String name = this.getDefaultLocalizedText();
		if( name != null ) {
			this.setName( name );
			this.setMnemonicKey( this.getLocalizedMnemonicKey() );
			this.setAcceleratorKey( this.getLocalizedAcceleratorKeyStroke() );
		}
	}
	
	protected static interface PerformObserver { 
		public void handleFinally(); 
	}
	/*package-private*/ final C handleFire( java.util.EventObject e, ViewController< ?, ? > viewController ) {
		final C childContext = this.createAndPushContext( e, viewController );
		this.perform( childContext, new PerformObserver() {
			public void handleFinally() {
				AbstractModelContext< ? > popContext = ContextManager.popContext();
				if( popContext != null ) {
					//assert popContext == childContext : "actual: " + popContext.getClass() + " expected: " + childContext.getClass();
					if( popContext == childContext ) {
						//pass
					} else {
						System.err.println( "actual: " + popContext.getClass() + " expected: " + childContext.getClass() );
					}
				} else {
					System.err.println( "handleFinally popContext==null" );
				}
			}
		} );
		return childContext;
	}
	protected abstract void perform( C context, PerformObserver performObserver );


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

	@Override
	public boolean isAlreadyInState( Edit< ? > edit ) {
		return false;
	}

	/*package-private*/ void addButton(OperationButton<?,?> button) {
		this.addComponent(button);
		button.setAction( Operation.this.action );
//			rv.setModel( this.buttonModel );
		assert Operation.this.mapButtonToListener.containsKey( button ) == false : this;
		ButtonActionListener buttonActionListener = new ButtonActionListener( button );
		Operation.this.mapButtonToListener.put( button, buttonActionListener );
		button.getAwtComponent().addActionListener( buttonActionListener );
	}
	/*package-private*/ void removeButton(OperationButton<?,?> button) {
		ButtonActionListener buttonActionListener = Operation.this.mapButtonToListener.get( button );
		assert buttonActionListener != null;
		button.getAwtComponent().removeActionListener( buttonActionListener );
		mapButtonToListener.remove( button );
//		rv.setModel( null );
		button.setAction( null );
		this.removeComponent(button);
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
