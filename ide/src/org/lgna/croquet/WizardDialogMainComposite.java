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
		public WizardCardComposite( WizardPageComposite<?>[] wizardPages ) {
			super( java.util.UUID.fromString( "d660e0ed-900a-4f98-ac23-bec8804dba22" ), wizardPages );
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
			protected WizardDialogMainComposite getMainComposite() {
				return (WizardDialogMainComposite)this.getControlsComposite().getGatedCommitDialogComposite().getMainComposite();
			}
		}
		private static class PreviousOperation extends InternalWizardDialogOperation {
			public PreviousOperation( WizardDialogControlsComposite composite ) {
				super( java.util.UUID.fromString( "2b1ff0fd-8d8a-4d23-9d95-6203e9abff9c" ), composite );
			}
			@Override
			protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
				org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
				WizardDialogMainComposite mainComposite = this.getMainComposite();
				if( mainComposite.isPrevPageAvailable() ) {
					mainComposite.prev();
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
				WizardDialogMainComposite mainComposite = this.getMainComposite();
				if( mainComposite.isNextPageAvailable() ) {
					mainComposite.next();
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
			WizardDialogMainComposite mainComposite = this.getMainComposite();
			this.getControlsComposite().nextOperation.setEnabled( mainComposite.isNextPageAvailable() );
			this.getControlsComposite().prevOperation.setEnabled( mainComposite.isPrevPageAvailable() );
		}
		@Override
		public void handleFiredEvent( org.lgna.croquet.history.event.Event< ? > event ) {
			super.handleFiredEvent( event );
			this.updateEnabled();
		}
		@Override
		protected void handlePreShowDialog( org.lgna.croquet.history.TransactionNode<?> node ) {
			this.getMainComposite().setIndex( 0 );
			this.updateEnabled();
			super.handlePreShowDialog( node );
		}
	}

	private final StringValue stepsLabel = this.createStringValue( this.createKey( "stepsLabel" ) );
	private int index = 0;
	private int getIndex() {
		return this.index;
	}
	private void setIndex( int index ) {
		this.index = index;
		this.cardComposite.showCard( this.cardComposite.getCards().get( index ) );
		this.listSelectionModel.setSelectionInterval( this.index, this.index );
		String text;
		if( this.index != -1 ) {
			text = this.cardComposite.getCards().get( this.index ).getDefaultLocalizedText();
		} else {
			text = null;
		}
		this.stepLabel.setText( text );
	}
	private boolean isPrevPageAvailable() {
		return this.index > 0; 
	}
	private boolean isNextPageAvailable() {
		return this.index < this.cardComposite.getCards().size()-1; 
	}
	private void prev() {
		this.setIndex( this.getIndex()-1 );
	}
	private void next() {
		this.setIndex( this.getIndex()+1 );
	}

	private final edu.cmu.cs.dennisc.javax.swing.models.ListModel< WizardPageComposite<?> > listModel = new edu.cmu.cs.dennisc.javax.swing.models.AbstractListModel< WizardPageComposite<?> >() {
		public Object getElementAt( int index ) {
			return cardComposite.getCards().get( index );
		}
		public int getSize() {
			return cardComposite.getCards().size();
		}
	};
	private final javax.swing.ListCellRenderer listCellRenderer = new edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< WizardPageComposite<?> >() {
		@Override
		protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, org.lgna.croquet.WizardPageComposite<?> value, int index, boolean isSelected, boolean cellHasFocus ) {
			StringBuilder sb = new StringBuilder();
			sb.append( index+1 );
			sb.append( ".    " );
			sb.append( value.getDefaultLocalizedText() );
			if( isSelected ) {
				//pass
			} else {
				//todo:
				final String PADDING_TO_ACCOUNT_FOR_SELECTED_TEXT_WEIGHT = "       "; 
				sb.append( PADDING_TO_ACCOUNT_FOR_SELECTED_TEXT_WEIGHT );
			}
			rv.setText( sb.toString() );

			edu.cmu.cs.dennisc.java.awt.font.TextWeight textWeight;
			if( isSelected ) {
				textWeight = edu.cmu.cs.dennisc.java.awt.font.TextWeight.ULTRABOLD;
			} else {
				textWeight = edu.cmu.cs.dennisc.java.awt.font.TextWeight.REGULAR;
			}
			edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( rv, textWeight );
			rv.setOpaque( false );
			rv.setForeground( java.awt.Color.BLACK );
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
			return rv;
		}
	};
	private final javax.swing.DefaultListSelectionModel listSelectionModel = new javax.swing.DefaultListSelectionModel();
	private final org.lgna.croquet.components.Label stepLabel = new org.lgna.croquet.components.Label( "todo" );
	
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
	
	private org.lgna.croquet.components.PageAxisPanel createPageAxisPanel( org.lgna.croquet.components.JComponent<?> header ) {
		final int PAD = 16;
		org.lgna.croquet.components.PageAxisPanel rv = new org.lgna.croquet.components.PageAxisPanel();
		header.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextWeight.ULTRABOLD );
		rv.addComponent( header );
		rv.addComponent( new org.lgna.croquet.components.HorizontalSeparator() );
		rv.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( PAD/2 ) );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( PAD, PAD, PAD, PAD ) );
		return rv;
	}
	@Override
	protected org.lgna.croquet.components.BorderPanel createView() {
		org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel();

		
		org.lgna.croquet.components.ImmutableTextField stepsTextField = this.stepsLabel.createImmutableTextField(); 
		javax.swing.JList list = new javax.swing.JList( this.listModel ) {
			@Override
			public boolean contains( int x, int y ) {
				return false;
			}
		};
		list.setSelectionModel( this.listSelectionModel );
		list.setAlignmentX( 0.0f );
		//list.setEnabled( false );
		list.setCellRenderer( this.listCellRenderer );

		org.lgna.croquet.components.PageAxisPanel stepsView = this.createPageAxisPanel( stepsTextField );
		stepsView.setBackgroundColor( java.awt.Color.WHITE );
		stepsView.getAwtComponent().add( list );

		org.lgna.croquet.components.PageAxisPanel mainView = this.createPageAxisPanel( this.stepLabel );
		mainView.addComponent( this.cardComposite.getView() );

		rv.addComponent( stepsView, org.lgna.croquet.components.BorderPanel.Constraint.LINE_START );
		rv.addComponent( mainView, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
		
		//todo: remove
		this.cardComposite.getView().setBackgroundColor( rv.getBackgroundColor() );
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
