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

/**
 * @author Dennis Cosgrove
 */
public abstract class AdornedDialogCoreComposite<V extends org.lgna.croquet.views.CompositeView<?, ?>, DCC extends org.lgna.croquet.imp.dialog.DialogContentComposite<?>> extends AbstractDialogComposite<V> {
	protected static final org.lgna.croquet.history.Step.Key<Boolean> IS_COMMITED_KEY = org.lgna.croquet.history.Step.Key.createInstance( "DialogCoreComposite.IS_COMMITED_KEY" );

	protected static abstract class InternalDialogOperation extends Operation {
		private final AdornedDialogCoreComposite coreComposite;

		public InternalDialogOperation( java.util.UUID id, AdornedDialogCoreComposite coreComposite ) {
			super( DIALOG_IMPLEMENTATION_GROUP, id );
			this.coreComposite = coreComposite;
		}

		public AdornedDialogCoreComposite getDialogCoreComposite() {
			return this.coreComposite;
		}

		@Override
		protected Class<? extends Element> getClassUsedForLocalization() {
			return this.coreComposite.getClassUsedForLocalization();
		}
	}

	private static abstract class InternalFinishOperation extends InternalDialogOperation {
		private final boolean isCommit;

		public InternalFinishOperation( java.util.UUID id, AdornedDialogCoreComposite coreComposite, boolean isCommit ) {
			super( id, coreComposite );
			this.isCommit = isCommit;
		}

		@Override
		protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			if( ( this.isCommit == false ) || this.getDialogCoreComposite().isClearedForCommit() ) {
				org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
				AdornedDialogCoreComposite coreComposite = this.getDialogCoreComposite();
				assert coreComposite != null : this;
				org.lgna.croquet.history.CompletionStep<?> dialogStep = transaction.getOwner().getOwner();
				assert dialogStep != null : transaction;
				org.lgna.croquet.views.Dialog dialog = dialogStep.getEphemeralDataFor( DIALOG_KEY );
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
		private InternalCommitOperation( AdornedDialogCoreComposite coreComposite ) {
			super( java.util.UUID.fromString( "8618f47b-8a2b-45e1-ad03-0ff76e2b7e35" ), coreComposite, true );
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
		private InternalCancelOperation( AdornedDialogCoreComposite coreComposite ) {
			super( java.util.UUID.fromString( "c467630e-39ee-49c9-ad07-d20c7a29db68" ), coreComposite, false );
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

	public AdornedDialogCoreComposite( java.util.UUID migrationId ) {
		super( migrationId, IsModal.TRUE );
	}

	protected abstract DCC getDialogContentComposite();

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
	protected org.lgna.croquet.views.CompositeView<?, ?> allocateView( org.lgna.croquet.history.CompletionStep<?> step ) {
		//todo
		return this.getDialogContentComposite().getView();
	}

	@Override
	protected void releaseView( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.views.CompositeView<?, ?> view ) {
		//todo
	}

	protected boolean isClearedForCommit() {
		return true;
	}

	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
		this.getDialogContentComposite().handlePreActivation();
		if( this.isDefaultButtonDesired() ) {
			org.lgna.croquet.views.Button commitButton = this.getDialogContentComposite().getView().getCommitButton();
			if( commitButton != null ) {
				org.lgna.croquet.views.Dialog dialog = step.getEphemeralDataFor( DIALOG_KEY );
				dialog.setDefaultButton( commitButton );
			}
		}
	}

	@Override
	protected void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
		this.getDialogContentComposite().handlePostDeactivation();
	}
}
