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
public abstract class WizardDialogMainComposite extends GatedCommitMainComposite<org.lgna.croquet.components.BorderPanel> {
	private static class WizardCardComposite extends CardComposite {
		private int index = 0;
		public WizardCardComposite( WizardPageComposite<?>[] wizardPages ) {
			super( java.util.UUID.fromString( "d660e0ed-900a-4f98-ac23-bec8804dba22" ), wizardPages );
		}
		public int getIndex() {
			return this.index;
		}
		public void setIndex( int index ) {
			this.index = index;
			this.showCard( this.getCards().get( index ) );
		}
		public boolean isPrevPageAvailable() {
			return this.index > 0; 
		}
		public boolean isNextPageAvailable() {
			return this.index < this.getCards().size()-1; 
		}
		public void prev() {
			this.setIndex( this.getIndex()-1 );
		}
		public void next() {
			this.setIndex( this.getIndex()+1 );
		}
	}
	private static class WizardDialogControlsComposite extends GatedCommitDialogComposite.ControlsComposite {
		private static abstract class InternalWizardDialogOperation extends InternalDialogOperation {
			public InternalWizardDialogOperation( java.util.UUID id, WizardDialogControlsComposite composite ) {
				super( id, composite );
			}
			@Override
			public WizardDialogControlsComposite getControlsComposite() {
				return (WizardDialogControlsComposite)super.getControlsComposite();
			}
			protected WizardCardComposite getWizardCardComposite() {
				return ((WizardDialogMainComposite)this.getControlsComposite().getGatedCommitDialogComposite().getMainComposite()).getCardComposite();
			}
		}
		private static class PreviousOperation extends InternalWizardDialogOperation {
			public PreviousOperation( WizardDialogControlsComposite composite ) {
				super( java.util.UUID.fromString( "2b1ff0fd-8d8a-4d23-9d95-6203e9abff9c" ), composite );
			}
			@Override
			protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
				org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
				WizardCardComposite wizardCardComposite = this.getWizardCardComposite();
				if( wizardCardComposite.isPrevPageAvailable() ) {
					wizardCardComposite.prev();
				}
				step.finish();
			}
		}
		private static class NextOperation extends InternalWizardDialogOperation {
			public NextOperation( WizardDialogControlsComposite composite ) {
				super( java.util.UUID.fromString( "e1239539-1eb0-411d-b808-947d0b1c1e94" ), composite );
			}
			@Override
			protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
				org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
				WizardCardComposite wizardCardComposite = this.getWizardCardComposite();
				if( wizardCardComposite.isNextPageAvailable() ) {
					wizardCardComposite.next();
				}
				step.finish();
			}
		}
		
		private final NextOperation nextOperation = new NextOperation( this );
		private final PreviousOperation prevOperation = new PreviousOperation( this );

		public WizardDialogControlsComposite( WizardDialogComposite composite ) {
			super( java.util.UUID.fromString( "56e28f65-6da2-4f25-a86b-16b7e3c4940c" ), composite );
		}
		@Override
		protected String getCommitUiKey() {
			return null;
		}
		@Override
		protected String getDefaultCommitText() {
			return "Finish";
		}
		@Override
		protected void localize() {
			super.localize();
			this.nextOperation.setName( this.findLocalizedText( "next", WizardDialogMainComposite.class ) );
			this.prevOperation.setName( this.findLocalizedText( "previous", WizardDialogMainComposite.class ) );
		}
		@Override
		protected void addComponentsToControlLine( org.lgna.croquet.components.LineAxisPanel controlLine, org.lgna.croquet.components.Button leadingOkCancelButton, org.lgna.croquet.components.Button trailingOkCancelButton ) {
			controlLine.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalGlue() );
			controlLine.addComponent( this.prevOperation.createButton() );
			controlLine.addComponent( this.nextOperation.createButton() );
			controlLine.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 8 ) );
			controlLine.addComponent( leadingOkCancelButton );
			controlLine.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 8 ) );
			controlLine.addComponent( trailingOkCancelButton );
		}
	}
	private static class WizardDialogComposite extends GatedCommitDialogComposite<WizardDialogMainComposite,WizardDialogControlsComposite> {
		private final WizardDialogControlsComposite controlsComposite = new WizardDialogControlsComposite( this );
		public WizardDialogComposite( Group operationGroup, WizardDialogMainComposite mainComposite ) {
			super( java.util.UUID.fromString( "fbff50c0-b0d3-48d1-bb10-348790b825b0" ), operationGroup, mainComposite );
		}
		@Override
		protected WizardDialogControlsComposite getControlsComposite() {
			assert this.controlsComposite != null : this;
			return this.controlsComposite;
		}
		private void updateEnabled() {
			WizardCardComposite wizardCardComposite = this.getMainComposite().getCardComposite();
			this.getControlsComposite().nextOperation.setEnabled( wizardCardComposite.isNextPageAvailable() );
			this.getControlsComposite().prevOperation.setEnabled( wizardCardComposite.isPrevPageAvailable() );
		}
		@Override
		public void handleFiredEvent( org.lgna.croquet.history.event.Event< ? > event ) {
			super.handleFiredEvent( event );
			this.updateEnabled();
		}
		@Override
		protected void handlePreShowDialog( org.lgna.croquet.history.Node<?> node ) {
			WizardCardComposite wizardCardComposite = this.getMainComposite().getCardComposite();
			wizardCardComposite.setIndex( 0 );
			this.updateEnabled();
			super.handlePreShowDialog( node );
		}
	}

	private final WizardDialogComposite gatedCommitDialogComposite;
	private final WizardCardComposite cardComposite;
	public WizardDialogMainComposite( java.util.UUID migrationId, Group operationGroup, WizardPageComposite<?>... wizardPages ) {
		super( migrationId );
		this.gatedCommitDialogComposite = new WizardDialogComposite( operationGroup, this );
		this.cardComposite = new WizardCardComposite( wizardPages );
	}
	public void addPage( WizardPageComposite<?> page ) {
		this.cardComposite.addCard( page );
	}
	public void removePage( WizardPageComposite<?> page ) {
		this.cardComposite.removeCard( page );
	}
	private WizardCardComposite getCardComposite() {
		return this.cardComposite;
	}
	@Override
	protected org.lgna.croquet.components.BorderPanel createView() {
		org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel();
		rv.addComponent( this.cardComposite.getView(), org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
		return rv;
	}
	@Override
	protected org.lgna.croquet.StringValue getExplanation( org.lgna.croquet.history.CompletionStep<?> step ) {
		//todo
		return null;
	}
	@Override
	public GatedCommitDialogComposite getGatedCommitDialogComposite() {
		return this.gatedCommitDialogComposite;
	}
	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.cardComposite.handlePreActivation();
	}
	@Override
	public void handlePostDeactivation() {
		this.cardComposite.handlePostDeactivation();
		super.handlePostDeactivation();
	}
}
