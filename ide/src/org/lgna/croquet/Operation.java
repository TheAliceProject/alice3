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

import org.lgna.croquet.edits.Edit;

/**
 * @author Dennis Cosgrove
 */
public abstract class Operation< S extends org.lgna.croquet.steps.OperationStep<? extends Operation<?>>> extends CompletionModel {
//	private class ButtonActionListener implements java.awt.event.ActionListener {
//		private AbstractButton< ?,? > button;
//		public ButtonActionListener( AbstractButton< ?,? > button ) {
//			this.button = button;
//		}
//		public void actionPerformed( java.awt.event.ActionEvent e ) {
//			Operation.this.fire( e, this.button );
//		}
//	}
//	private java.util.Map< AbstractButton< ?,? >, ButtonActionListener > mapButtonToListener = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	
//	private final java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
//		public void actionPerformed( java.awt.event.ActionEvent e ) {
//			Operation.this.fire( e );
//		}
//	};
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			Operation.this.fire( e );
		}
	};
	public Operation( Group group, java.util.UUID id ) {
		super( group, id );
	}
	
	public javax.swing.Action getAction() {
		return this.action;
	}
//	public java.awt.event.ActionListener getActionListener() {
//		return this.actionListener;
//	}
	
	@Override
	protected void localize() {
		String name = this.getDefaultLocalizedText();
		if( name != null ) {
			this.setName( name );
			this.setMnemonicKey( this.getLocalizedMnemonicKey() );
			this.setAcceleratorKey( this.getLocalizedAcceleratorKeyStroke() );
		}
	}
	public abstract S createAndPushStep( org.lgna.croquet.Trigger trigger );

//	public String getTutorialStartNoteText( S step, UserInformation userInformation ) {
//		return "Press " + this.getTutorialNoteText( step, userInformation );
//	}
//
//	protected String getTutorialNoteName() {
//		return this.getName();
//	}
//	@Override
//	protected StringBuilder updateTutorialStepText( StringBuilder rv, ModelContext< ? > modelContext, Edit< ? > edit, UserInformation userInformation ) {
//		rv.append( "Click <strong>" );
//		rv.append( this.getTutorialNoteName() );
//		rv.append( "</strong>" );
//		return rv;
//	}
	
	protected Edit< ? > createTutorialCompletionEdit( org.lgna.croquet.steps.CompletionStep<?> step, Edit< ? > originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		return null;
	}
	@Override
	public Edit< ? > commitTutorialCompletionEdit( org.lgna.croquet.steps.CompletionStep<?> step, Edit< ? > originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		Edit< ? > replacementEdit = this.createTutorialCompletionEdit( step, originalEdit, retargeter );
//		if( replacementEdit != null ) {
//			final S step = this.createAndPushStep( null, null );
//			try {
//				step.commitAndInvokeDo( replacementEdit );
//			} finally {
//				ModelContext< ? > popContext = ContextManager.popContext();
//				assert popContext == step : popContext.getClass() + " " + step.getClass();
//			}
//		} else {
//			System.err.println( "createTutorialCompletionEdit returned null" );
//		}
		return replacementEdit;
	}

	@Override
	public S fire( org.lgna.croquet.Trigger trigger ) {
		if( this.isEnabled() ) {
			return this.handleFire( trigger );
		} else {
			return null;
		}
	}
	public S fire( java.awt.event.ActionEvent e, org.lgna.croquet.components.ViewController< ?, ? > viewController ) {
		if( this.isEnabled() ) {
			return this.handleFire( new org.lgna.croquet.triggers.ActionEventTrigger( viewController, e ) );
		} else {
			return null;
		}
	}
	public S fire( java.awt.event.MouseEvent e, org.lgna.croquet.components.ViewController< ?, ? > viewController ) {
		if( this.isEnabled() ) {
			return this.handleFire( new org.lgna.croquet.triggers.MouseEventTrigger( viewController, e ) );
		} else {
			return null;
		}
	}
	@Deprecated
	public S fire( java.awt.event.MouseEvent e ) {
		return fire( e, null );
	}
	@Deprecated
	public S fire( java.awt.event.ActionEvent e ) {
		return fire( e, null );
	}
	@Deprecated
	public S fire() {
		return fire( org.lgna.croquet.triggers.SimulatedTrigger.SINGLETON );
	}
	
	public static interface PerformObserver { 
		public void handleFinally(); 
	}
	/*package-private*/ final S handleFire( org.lgna.croquet.Trigger trigger ) {
		final S step = this.createAndPushStep( trigger );
		this.perform( step, new PerformObserver() {
			public void handleFinally() {
//				ModelContext< ? > popContext = ContextManager.popContext();
//				if( popContext != null ) {
//					//assert popContext == step : "actual: " + popContext.getClass() + " expected: " + step.getClass();
//					if( popContext == step ) {
//						//pass
//					} else {
//						System.err.println( "actual: " + popContext.getClass() + " expected: " + step.getClass() );
//					}
//				} else {
//					System.err.println( "handleFinally popContext==null" );
//				}
			}
		} );
		return step;
	}
	protected abstract void perform( S step, PerformObserver performObserver );

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

	private OperationMenuItemPrepModel menuPrepModel;
	public synchronized OperationMenuItemPrepModel getMenuItemPrepModel() {
		if( this.menuPrepModel != null ) {
			//pass
		} else {
			this.menuPrepModel = new OperationMenuItemPrepModel( this );
		}
		return this.menuPrepModel;
	}
	
	public org.lgna.croquet.components.Button createButton() {
		return new org.lgna.croquet.components.Button( this );
	}
	public org.lgna.croquet.components.Hyperlink createHyperlink() {
		return new org.lgna.croquet.components.Hyperlink( this );
	}
}
