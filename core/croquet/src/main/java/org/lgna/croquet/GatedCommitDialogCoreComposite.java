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

/*package-private*/abstract class GatedCommitDialogContentPanel<CC extends GatedCommitDialogContentComposite> extends DialogContentPanel<CC> {
	private final org.lgna.croquet.views.StatusLabel statusLabel = new org.lgna.croquet.views.StatusLabel();
	private final org.lgna.croquet.views.LineAxisPanel controlLine = new org.lgna.croquet.views.LineAxisPanel();

	public GatedCommitDialogContentPanel( CC composite ) {
		super( composite );
		controlLine.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );

		org.lgna.croquet.views.GridBagPanel pageEndPanel = new org.lgna.croquet.views.GridBagPanel();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.anchor = java.awt.GridBagConstraints.NORTH;
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		GatedCommitDialogCoreComposite coreComposite = (GatedCommitDialogCoreComposite)composite.getCoreComposite();
		if( coreComposite.isStatusLineDesired() ) {
			pageEndPanel.addComponent( this.getStatusLabel(), gbc );
		}
		pageEndPanel.addComponent( new org.lgna.croquet.views.HorizontalSeparator(), gbc );
		pageEndPanel.addComponent( controlLine, gbc );
		controlLine.setBackgroundColor( null );

		this.addPageEndComponent( pageEndPanel );

		this.statusLabel.setForegroundColor( java.awt.Color.RED.darker().darker() );
	}

	protected org.lgna.croquet.views.LineAxisPanel getControlLine() {
		return this.controlLine;
	}

	public org.lgna.croquet.views.StatusLabel getStatusLabel() {
		return this.statusLabel;
	}
}

/* package-private */abstract class GatedCommitDialogContentComposite<V extends GatedCommitDialogContentPanel> extends DialogContentComposite<V> {
	public GatedCommitDialogContentComposite( java.util.UUID migrationId, GatedCommitDialogCoreComposite coreComposite ) {
		super( migrationId, coreComposite );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class GatedCommitDialogCoreComposite<V extends org.lgna.croquet.views.CompositeView<?, ?>, CC extends GatedCommitDialogContentComposite<? extends GatedCommitDialogContentPanel<?>>> extends DialogCoreComposite<V, CC> {
	private final java.util.List<CommitRejector> commitRejectors = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	public GatedCommitDialogCoreComposite( java.util.UUID migrationId ) {
		super( migrationId );
	}

	public void addCommitRejector( CommitRejector commitRejector ) {
		this.commitRejectors.add( commitRejector );
	}

	public void removeCommitRejector( CommitRejector commitRejector ) {
		this.commitRejectors.remove( commitRejector );
	}

	public void clearCommitRejectors() {
		this.commitRejectors.clear();
	}

	protected abstract Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step );

	public final Status getStatus( org.lgna.croquet.history.CompletionStep<?> step ) {
		Status status = this.getStatusPreRejectorCheck( step );
		if( status == IS_GOOD_TO_GO_STATUS ) {
			for( CommitRejector rejector : this.commitRejectors ) {
				status = rejector.getRejectionStatus( step );
				if( status == IS_GOOD_TO_GO_STATUS ) {
					//pass
				} else {
					return status;
				}
			}
		}
		return status;
	}

	private final org.lgna.croquet.history.event.Listener listener = new org.lgna.croquet.history.event.Listener() {
		public void changing( org.lgna.croquet.history.event.Event<?> e ) {
		}

		public void changed( org.lgna.croquet.history.event.Event<?> e ) {
			GatedCommitDialogCoreComposite.this.handleFiredEvent( e );
		}
	};

	public boolean isStatusLineDesired() {
		return true;
	}

	protected abstract void updateIsGoodToGo( boolean isGoodToGo );

	private void updateStatus( org.lgna.croquet.history.CompletionStep<?> step ) {
		boolean isGoodToGo;
		AbstractSeverityStatusComposite.Status status = this.getStatus( step );
		if( status != null ) {
			isGoodToGo = status.isGoodToGo();
		} else {
			isGoodToGo = true;
		}
		this.getDialogContentComposite().getView().getStatusLabel().setStatus( status );
		this.updateIsGoodToGo( isGoodToGo );
	}

	protected void refreshStatus() {
		//todo
		org.lgna.croquet.history.CompletionStep<?> step = null;
		this.updateStatus( step );
	}

	protected void handleFiredEvent( org.lgna.croquet.history.event.Event<?> event ) {
		org.lgna.croquet.history.CompletionStep<?> s = null;
		if( event != null ) {
			org.lgna.croquet.history.TransactionNode<?> node = event.getNode();
			if( node != null ) {
				//todo:
				s = node.getFirstStepOfModelAssignableTo( OwnedByCompositeOperation.class, org.lgna.croquet.history.CompletionStep.class );
			}
		}
		this.updateStatus( s );
	}

	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		completionStep.addListener( this.listener );
		this.updateStatus( completionStep );
		super.handlePreShowDialog( completionStep );
	}

	@Override
	protected void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		completionStep.removeListener( this.listener );
		super.handlePostHideDialog( completionStep );
	}

	protected void cancel( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		completionStep.cancel();
	}

	public void addGeneratedSubTransactions( org.lgna.croquet.history.TransactionHistory subTransactionHistory, org.lgna.croquet.edits.Edit<?> ownerEdit ) throws UnsupportedGenerationException {
		org.lgna.croquet.edits.Edit<?> commitEdit = null;
		this.getCommitOperation().addGeneratedTransaction( subTransactionHistory, org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance(), commitEdit, null );
	}

	public void addGeneratedPostTransactions( org.lgna.croquet.history.TransactionHistory ownerTransactionHistory, org.lgna.croquet.edits.Edit<?> edit ) throws UnsupportedGenerationException {
	}
}
