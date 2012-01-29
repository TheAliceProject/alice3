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
public abstract class Operation< S extends org.lgna.croquet.history.OperationStep<? extends Operation<?>>> extends CompletionModel {
	public class SwingModel {
		private javax.swing.Action action = new javax.swing.AbstractAction() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				Operation.this.fire( new org.lgna.croquet.triggers.ActionEventTrigger( e ) );
			}
		};
		public javax.swing.Action getAction() {
			return this.action;
		}
	}
	
	private final SwingModel swingModel = new SwingModel();

	public Operation( Group group, java.util.UUID id ) {
		super( group, id );
	}
	public SwingModel getSwingModel() {
		this.initializeIfNecessary();
		return this.swingModel;
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
	public abstract S createAndPushStep( org.lgna.croquet.triggers.Trigger trigger );

//	public String getTutorialStartNoteText( S step, UserInformation userInformation ) {
//		return "Press " + this.getTutorialNoteText( step, userInformation );
//	}
//
	@Override
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		rv.append( " <strong>" );
		rv.append( this.getName() );
		rv.append( "</strong>" );
		return rv;
	}
	
	protected org.lgna.croquet.edits.Edit< ? > createTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.edits.Edit< ? > originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		return null;
	}
	@Override
	public org.lgna.croquet.edits.Edit< ? > commitTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.edits.Edit< ? > originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		org.lgna.croquet.edits.Edit< ? > replacementEdit = this.createTutorialCompletionEdit( step, originalEdit, retargeter );
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
	public boolean isEnabled() {
		return this.swingModel.action.isEnabled();
	}
	@Override
	public void setEnabled( boolean isEnabled ) {
		this.swingModel.action.setEnabled( isEnabled );
	}

	@Override
	public final S fire( org.lgna.croquet.triggers.Trigger trigger ) {
		if( this.isEnabled() ) {
			return this.handleFire( trigger );
		} else {
			return null;
		}
	}
	public final S fire( java.awt.event.ActionEvent e, org.lgna.croquet.components.ViewController< ?, ? > viewController ) {
		return this.fire( new org.lgna.croquet.triggers.ActionEventTrigger( viewController, e ) );
	}
	public final S fire( java.awt.event.MouseEvent e, org.lgna.croquet.components.ViewController< ?, ? > viewController ) {
		return this.fire( new org.lgna.croquet.triggers.MouseEventTrigger( viewController, e ) );
	}
	@Deprecated
	public final S fire( java.awt.event.MouseEvent e ) {
		return fire( e, null );
	}
	@Deprecated
	public final S fire( java.awt.event.ActionEvent e ) {
		return fire( e, null );
	}
	@Deprecated
	public final S fire() {
		return fire( new org.lgna.croquet.triggers.SimulatedTrigger() );
	}
	
	/*package-private*/ final S handleFire( org.lgna.croquet.triggers.Trigger trigger ) {
		//todo: move up to Model
		this.initializeIfNecessary();
		final S step = this.createAndPushStep( trigger );
		this.perform( step );
		return step;
	}
	protected abstract void perform( S step );

	public String getName() {
		return String.class.cast( this.swingModel.action.getValue( javax.swing.Action.NAME ) );
	}
	public void setName( String name ) {
		this.swingModel.action.putValue( javax.swing.Action.NAME, name );
	}
//	public String getShortDescription() {
//		return String.class.cast( this.swingModel.action.getValue( javax.swing.Action.SHORT_DESCRIPTION ) );
//	}
	public void setShortDescription( String shortDescription ) {
		this.swingModel.action.putValue( javax.swing.Action.SHORT_DESCRIPTION, shortDescription );
	}
//	public String getLongDescription() {
//		return String.class.cast( this.swingModel.action.getValue( javax.swing.Action.LONG_DESCRIPTION ) );
//	}
	public void setLongDescription( String longDescription ) {
		this.swingModel.action.putValue( javax.swing.Action.LONG_DESCRIPTION, longDescription );
	}
	public javax.swing.Icon getSmallIcon() {
		return javax.swing.Icon.class.cast( this.swingModel.action.getValue( javax.swing.Action.SMALL_ICON ) );
	}
	public void setSmallIcon( javax.swing.Icon icon ) {
		this.swingModel.action.putValue( javax.swing.Action.SMALL_ICON, icon );
	}
//	public int getMnemonicKey() {
//		return Integer.class.cast( this.swingModel.action.getValue( javax.swing.Action.MNEMONIC_KEY ) );
//	}
	private void setMnemonicKey( int mnemonicKey ) {
		this.swingModel.action.putValue( javax.swing.Action.MNEMONIC_KEY, mnemonicKey );
	}
//	public javax.swing.KeyStroke getAcceleratorKey() {
//		return javax.swing.KeyStroke.class.cast( this.swingModel.action.getValue( javax.swing.Action.ACCELERATOR_KEY ) );
//	}
	private void setAcceleratorKey( javax.swing.KeyStroke acceleratorKey ) {
		this.swingModel.action.putValue( javax.swing.Action.ACCELERATOR_KEY, acceleratorKey );
	}

	@Override
	public boolean isAlreadyInState( org.lgna.croquet.edits.Edit< ? > edit ) {
		return false;
	}

	public static class InternalMenuPrepModelResolver extends IndirectResolver< InternalMenuItemPrepModel, Operation< ? > > {
		private InternalMenuPrepModelResolver( Operation< ? > indirect ) {
			super( indirect );
		}
		public InternalMenuPrepModelResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalMenuItemPrepModel getDirect( Operation< ? > indirect ) {
			return indirect.getMenuItemPrepModel();
		}
	}
	private final static class InternalMenuItemPrepModel extends StandardMenuItemPrepModel {
		private final Operation<?> operation;
		private InternalMenuItemPrepModel( Operation<?> operation ) {
			super( java.util.UUID.fromString( "652a76ce-4c05-4c31-901c-ff14548e50aa" ) );
			assert operation != null;
			this.operation = operation;
		}
		@Override
		public Iterable< ? extends Model > getChildren() {
			return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( this.operation );
		}
		@Override
		protected void localize() {
		}
		public Operation<?> getOperation() {
			return this.operation;
		}
		@Override
		public boolean isEnabled() {
			return this.operation.isEnabled();
		}
		@Override
		public void setEnabled( boolean isEnabled ) {
			this.operation.setEnabled( isEnabled );
		}
		@Override
		protected InternalMenuPrepModelResolver createResolver() {
			return new InternalMenuPrepModelResolver( this.operation );
		}
		@Override
		public org.lgna.croquet.components.JComponent< ? > getFirstComponent() {
			return this.operation.getFirstComponent();
		}
		@Override
		public org.lgna.croquet.components.MenuItemContainer createMenuItemAndAddTo( org.lgna.croquet.components.MenuItemContainer rv ) {
			rv.addMenuItem( new org.lgna.croquet.components.MenuItem( this.getOperation() ) );
			return rv;
		}
		@Override
		protected StringBuilder appendRepr( StringBuilder rv ) {
			super.appendRepr( rv );
			rv.append( "[" );
			rv.append( this.getOperation() );
			rv.append( "]" );
			return rv;
		}
		@Override
		protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
			return this.operation.updateTutorialStepText( rv, step, edit, userInformation );
		}
	}

	private InternalMenuItemPrepModel menuPrepModel;
	public synchronized InternalMenuItemPrepModel getMenuItemPrepModel() {
		if( this.menuPrepModel != null ) {
			//pass
		} else {
			this.menuPrepModel = new InternalMenuItemPrepModel( this );
		}
		return this.menuPrepModel;
	}
	
	public org.lgna.croquet.components.Button createButton() {
		return new org.lgna.croquet.components.Button( this );
	}
	public org.lgna.croquet.components.Hyperlink createHyperlink() {
		return new org.lgna.croquet.components.Hyperlink( this );
	}

	public org.lgna.croquet.components.ButtonWithRightClickCascade createButtonWithRightClickCascade( Cascade< ? > cascade ) {
		return new org.lgna.croquet.components.ButtonWithRightClickCascade( this, cascade );
	}
}
