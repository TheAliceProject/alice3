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

/*package-private*/abstract class DialogContentPanel<CC extends DialogContentComposite> extends org.lgna.croquet.components.BorderPanel {
	private final org.lgna.croquet.components.Button commitButton;
	private final org.lgna.croquet.components.Button cancelButton;

	public DialogContentPanel( CC composite ) {
		super( composite );
		DialogCoreComposite coreComposite = composite.getCoreComposite();
		this.commitButton = coreComposite.getCommitOperation().createButton();
		this.cancelButton = coreComposite.getCancelOperation().createButton();
		org.lgna.croquet.components.View<?,?> coreView = coreComposite.getView();
		this.setBackgroundColor( coreView.getBackgroundColor() );
		this.addCenterComponent( coreView );
	}
	public org.lgna.croquet.components.Button getCommitButton() {
		return this.commitButton;
	}
	public org.lgna.croquet.components.Button getCancelButton() {
		return this.cancelButton;
	}
	
	protected org.lgna.croquet.components.Button getLeadingCommitCancelButton() {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
			return this.commitButton;
		} else {
			return this.cancelButton;
		}
	}
	protected org.lgna.croquet.components.Button getTrailingCommitCancelButton() {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
			return this.cancelButton;
		} else {
			return this.commitButton;
		}
	}	
}

/*package-private*/abstract class DialogContentComposite<V extends GatedCommitDialogContentPanel> extends SimpleComposite<V> {
	private final DialogCoreComposite coreComposite;
	public DialogContentComposite( java.util.UUID migrationId, DialogCoreComposite coreComposite ) {
		super( migrationId );
		this.coreComposite = coreComposite;
	}
	public DialogCoreComposite getCoreComposite() {
		return this.coreComposite;
	}
	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.coreComposite.handlePreActivation();
	}
	@Override
	public void handlePostDeactivation() {
		this.coreComposite.handlePostDeactivation();
		super.handlePostDeactivation();
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class DialogCoreComposite<V extends org.lgna.croquet.components.View<?,?>,CC extends DialogContentComposite<? extends DialogContentPanel<?>>> extends AbstractDialogComposite<V> {
	protected static final Group DIALOG_IMPLEMENTATION_GROUP = Group.getInstance( java.util.UUID.fromString( "4e436a8e-cfbc-447c-8c80-bc488d318f5b" ), "DIALOG_IMPLEMENTATION_GROUP" );
	protected static final org.lgna.croquet.history.Step.Key< Boolean > IS_COMMITED_KEY = org.lgna.croquet.history.Step.Key.createInstance( "DialogCoreComposite.IS_COMMITED_KEY" );
	public static final class InternalCommitOperationResolver extends IndirectResolver< InternalCommitOperation, DialogCoreComposite > {
		private InternalCommitOperationResolver( DialogCoreComposite indirect ) {
			super( indirect );
		}
		public InternalCommitOperationResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalCommitOperation getDirect( DialogCoreComposite indirect ) {
			return indirect.commitOperation;
		}
	}
	public static final class InternalCancelOperationResolver extends IndirectResolver< InternalCancelOperation, DialogCoreComposite > {
		private InternalCancelOperationResolver( DialogCoreComposite indirect ) {
			super( indirect );
		}
		public InternalCancelOperationResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalCancelOperation getDirect( DialogCoreComposite indirect ) {
			return indirect.cancelOperation;
		}
	}
	protected static abstract class InternalDialogOperation extends ActionOperation {
		private final DialogCoreComposite coreComposite;
		public InternalDialogOperation( java.util.UUID id, DialogCoreComposite coreComposite ) {
			super( DIALOG_IMPLEMENTATION_GROUP, id );
			this.coreComposite = coreComposite;
		}
		public DialogCoreComposite getDialogCoreComposite() {
			return this.coreComposite;
		}
		@Override
		protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return this.coreComposite.getClassUsedForLocalization();
		}
	}
	
	private static abstract class InternalFinishOperation extends InternalDialogOperation {
		private final boolean isCommit;
		public InternalFinishOperation( java.util.UUID id, DialogCoreComposite coreComposite, boolean isCommit ) {
			super( id, coreComposite );
			this.isCommit = isCommit;
		}
		@Override
		protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			if( this.isCommit == false || this.getDialogCoreComposite().isClearedForCommit() ) {
				org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
				DialogCoreComposite coreComposite = this.getDialogCoreComposite();
				assert coreComposite != null : this;
				org.lgna.croquet.history.CompletionStep<?> dialogStep = transaction.getOwner().getOwner();
				assert dialogStep != null : transaction;
				org.lgna.croquet.components.Dialog dialog = dialogStep.getEphemeralDataFor( org.lgna.croquet.dialog.DialogUtilities.DIALOG_KEY );
				assert dialog != null : dialogStep;
				dialogStep.putEphemeralDataFor( IS_COMMITED_KEY, this.isCommit );
				dialog.setVisible( false );
				step.finish();
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( this );
			}
		}
		
	}
	
	private static final class InternalCommitOperation extends InternalFinishOperation {
		private InternalCommitOperation( DialogCoreComposite coreComposite ) {
			super( java.util.UUID.fromString( "8618f47b-8a2b-45e1-ad03-0ff76e2b7e35" ), coreComposite, true );
		}
		@Override
		protected InternalCommitOperationResolver createResolver() {
			return new InternalCommitOperationResolver( this.getDialogCoreComposite() );
		}
		@Override
		protected String getSubKeyForLocalization() {
			return "commit";
		}
		@Override
		protected String findDefaultLocalizedText() {
			String rv = super.findDefaultLocalizedText();
			if( rv != null ) {
				//pass
			} else {
				java.util.Locale locale = javax.swing.JComboBox.getDefaultLocale();
				String commitUiKey = this.getDialogCoreComposite().getCommitUiKey();
				if( commitUiKey != null ) {
					rv = javax.swing.UIManager.getString( commitUiKey, locale );
				}
				if( rv != null ) {
					//pass
				} else {
					rv = this.getDialogCoreComposite().getDefaultCommitText();
				}
			}
			return rv;
		}
	}
	private static final class InternalCancelOperation extends InternalFinishOperation {
		private InternalCancelOperation( DialogCoreComposite coreComposite ) {
			super( java.util.UUID.fromString( "c467630e-39ee-49c9-ad07-d20c7a29db68" ), coreComposite, false );
		}
		@Override
		protected InternalCancelOperationResolver createResolver() {
			return new InternalCancelOperationResolver( this.getDialogCoreComposite() );
		}
		@Override
		protected String getSubKeyForLocalization() {
			return "cancel";
		}
		@Override
		protected String findDefaultLocalizedText() {
			String rv = super.findDefaultLocalizedText();
			if( rv != null ) {
				//pass
			} else {
				java.util.Locale locale = javax.swing.JComboBox.getDefaultLocale();
				rv = javax.swing.UIManager.getString( "OptionPane.cancelButtonText", locale );
				if( rv != null ) {
					//pass
				} else {
					rv = "Cancel";
				}
			}
			return rv;
		}
	}
	private final InternalCommitOperation commitOperation = new InternalCommitOperation( this );
	private final InternalCancelOperation cancelOperation = new InternalCancelOperation( this );

	public DialogCoreComposite( java.util.UUID migrationId ) {
		super( migrationId );
	}
	protected abstract CC getDialogContentComposite();
	public final Operation getCommitOperation() {
		return this.commitOperation;
	}
	public final Operation getCancelOperation() {
		return this.cancelOperation;
	}
	protected abstract String getDefaultCommitText();
	protected abstract String getCommitUiKey();
	protected String getCancelUiKey() {
		return "OptionPane.cancelButtonText";
	}
	@Override
	protected void localize() {
		super.localize();
		String commitText = this.findLocalizedText( "commit" );
		if( commitText != null ) {
			//pass
		} else {
			java.util.Locale locale = javax.swing.JComboBox.getDefaultLocale();
			String commitUiKey = this.getCommitUiKey();
			if( commitUiKey != null ) {
				commitText = javax.swing.UIManager.getString( commitUiKey, locale );
			}
			if( commitText != null ) {
				//pass
			} else {
				commitText = this.getDefaultCommitText();
			}
		}
		this.getCommitOperation().setName( commitText );
		String cancelText = this.findLocalizedText( "cancel" );
		if( cancelText != null ) {
			//pass
		} else {
			java.util.Locale locale = javax.swing.JComboBox.getDefaultLocale();
			cancelText = javax.swing.UIManager.getString( "OptionPane.cancelButtonText", locale );
			if( cancelText != null ) {
				//pass
			} else {
				cancelText = "Cancel";
			}
		}
		this.getCancelOperation().setName( cancelText );
	}
	

	@Override
	protected org.lgna.croquet.components.View<?,?> allocateView( org.lgna.croquet.history.CompletionStep<?> step ) {
		//todo
		return this.getDialogContentComposite().getView();
	}
	@Override
	protected void releaseView( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.View<?,?> view ) {
		//todo
	}

	protected boolean isClearedForCommit() {
		return true;
	}
	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
		this.getDialogContentComposite().handlePreActivation();
		if( this.isDefaultButtonDesired() ) {
			org.lgna.croquet.components.Button commitButton = this.getDialogContentComposite().getView().getCommitButton();
			if( commitButton != null ) {
				org.lgna.croquet.components.Dialog dialog = step.getEphemeralDataFor( org.lgna.croquet.dialog.DialogUtilities.DIALOG_KEY );
				dialog.setDefaultButton( commitButton );
			}
		}
	}
	@Override
	protected void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
		this.getDialogContentComposite().handlePostDeactivation();
	}
}
