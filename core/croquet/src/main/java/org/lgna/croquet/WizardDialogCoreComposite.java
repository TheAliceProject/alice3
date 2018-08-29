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

import edu.cmu.cs.dennisc.java.awt.font.FontUtilities;
import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.models.AbstractListModel;
import edu.cmu.cs.dennisc.javax.swing.models.ListModel;
import edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.dialog.WizardDialogContentComposite;
import org.lgna.croquet.triggers.Trigger;
import org.lgna.croquet.views.*;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class WizardDialogCoreComposite extends GatedCommitDialogCoreComposite<Panel, WizardDialogContentComposite> {
	private static abstract class InternalWizardDialogOperation extends InternalDialogOperation {
		public InternalWizardDialogOperation( UUID id, WizardDialogCoreComposite composite ) {
			super( id, composite );
		}

		@Override
		public WizardDialogCoreComposite getDialogCoreComposite() {
			return (WizardDialogCoreComposite)super.getDialogCoreComposite();
		}
	}

	private static class PreviousOperation extends InternalWizardDialogOperation {
		public PreviousOperation( WizardDialogCoreComposite composite ) {
			super( UUID.fromString( "2b1ff0fd-8d8a-4d23-9d95-6203e9abff9c" ), composite );
		}

		@Override
		protected String getSubKeyForLocalization() {
			return "previous";
		}

		@Override
		protected final void perform( UserActivity userActivity, Trigger trigger ) {
			userActivity.setCompletionModel( this, trigger );
			WizardDialogCoreComposite mainComposite = this.getDialogCoreComposite();
			if( mainComposite.isPrevPageAvailable() ) {
				mainComposite.prev();
			}
			userActivity.finish();
		}
	}

	private static class NextOperation extends InternalWizardDialogOperation {
		public NextOperation( WizardDialogCoreComposite composite ) {
			super( UUID.fromString( "e1239539-1eb0-411d-b808-947d0b1c1e94" ), composite );
		}

		@Override
		protected String getSubKeyForLocalization() {
			return "next";
		}

		@Override
		protected final void perform( UserActivity userActivity, Trigger trigger ) {
			userActivity.setCompletionModel( this, trigger );
			WizardDialogCoreComposite mainComposite = this.getDialogCoreComposite();
			if( mainComposite.isNextPageAvailable() ) {
				mainComposite.next( false );
			}
			userActivity.finish();
		}
	}

	private final NextOperation nextOperation = new NextOperation( this );
	private final PreviousOperation prevOperation = new PreviousOperation( this );

	private static class WizardCardOwnerComposite extends CardOwnerComposite {
		public WizardCardOwnerComposite( WizardPageComposite<?, ?>[] wizardPages ) {
			super( UUID.fromString( "d660e0ed-900a-4f98-ac23-bec8804dba22" ), wizardPages );
		}

		@Override
		public boolean isCardAccountedForInPreferredSizeCalculation( Composite<?> card ) {
			if( card instanceof WizardPageComposite ) {
				WizardPageComposite page = (WizardPageComposite)card;
				return page.isAccountedForInPreferredSizeCalculation();
			} else {
				return super.isCardAccountedForInPreferredSizeCalculation( card );
			}
		}
	}

	private final PlainStringValue stepsLabel = this.createStringValue( "stepsLabel" );
	private int index = 0;

	private int getIndex() {
		return this.index;
	}

	private void setIndex( int index, boolean isInTheMidstOfPreActivation ) {
		this.index = index;
		Composite<?> card = this.index != -1 ? this.cardComposite.getCards().get( index ) : null;
		if( isInTheMidstOfPreActivation ) {
			this.cardComposite.showCardRefrainingFromActivation( card );
		} else {
			this.cardComposite.showCard( card );
		}
		if( this.index != -1 ) {
			this.listSelectionModel.setSelectionInterval( this.index, this.index );
		} else {
			this.listSelectionModel.clearSelection();
		}
		String text;
		if( card instanceof WizardPageComposite ) {
			WizardPageComposite wizardPageComposite = (WizardPageComposite)card;
			text = wizardPageComposite.getName();
		} else {
			text = null;
		}
		this.stepLabel.setText( text );
		this.refreshStatus();
		this.updateEnabled();
	}

	private boolean isPrevPageAvailable() {
		return this.index > 0;
	}

	private boolean isNextPageAvailable() {
		return this.index < ( this.cardComposite.getCards().size() - 1 );
	}

	private void prev() {
		int prevIndex = this.getIndex() - 1;
		this.setIndex( prevIndex, false );
	}

	private void next( boolean isInTheMidstOfPreActivation ) {
		int nextIndex = this.getIndex();
		List<Composite<?>> cards = this.cardComposite.getCards();
		while( nextIndex < ( cards.size() - 1 ) ) {
			nextIndex++;
			WizardPageComposite wizardPageComposite = (WizardPageComposite)cards.get( nextIndex );
			if ( !wizardPageComposite.isAutoAdvanceDesired() ) {
				break;
			}
		}
		this.setIndex( nextIndex, isInTheMidstOfPreActivation );
		this.refreshStatus();
	}

	private final ListModel<WizardPageComposite<?, ?>> listModel = new AbstractListModel<WizardPageComposite<?, ?>>() {
		@Override
		public Object getElementAt( int index ) {
			return cardComposite.getCards().get( index );
		}

		@Override
		public int getSize() {
			return cardComposite.getCards().size();
		}
	};
	private final javax.swing.ListCellRenderer listCellRenderer = new ListCellRenderer<WizardPageComposite<?, ?>>() {
		@Override
		protected JLabel getListCellRendererComponent( JLabel rv, JList list, WizardPageComposite<?, ?> value, int index, boolean isSelected, boolean cellHasFocus ) {
			StringBuilder sb = new StringBuilder();
			sb.append( index + 1 );
			sb.append( ".    " );
			sb.append( value.getName() );
			if( isSelected ) {
				//pass
			} else {
				//todo:
				final String PADDING_TO_ACCOUNT_FOR_SELECTED_TEXT_WEIGHT = "       ";
				sb.append( PADDING_TO_ACCOUNT_FOR_SELECTED_TEXT_WEIGHT );
			}
			rv.setText( sb.toString() );

			TextWeight textWeight;
			if( isSelected ) {
				textWeight = TextWeight.ULTRABOLD;
			} else {
				textWeight = TextWeight.REGULAR;
			}
			FontUtilities.setFontToDerivedFont( rv, textWeight );
			rv.setOpaque( false );
			rv.setForeground( Color.BLACK );
			rv.setBorder( BorderFactory.createEmptyBorder() );
			return rv;
		}
	};
	private final DefaultListSelectionModel listSelectionModel = new DefaultListSelectionModel();
	private final Label stepLabel = new Label( "todo" );

	private final WizardDialogContentComposite contentComposite = new WizardDialogContentComposite( this );
	private final WizardCardOwnerComposite cardComposite;

	public WizardDialogCoreComposite( UUID migrationId, WizardPageComposite<?, ?>[] wizardPages ) {
		super( migrationId );
		this.cardComposite = new WizardCardOwnerComposite( wizardPages );
	}

	@Override
	protected WizardDialogContentComposite getDialogContentComposite() {
		return this.contentComposite;
	}

	public Operation getPrevOperation() {
		return this.prevOperation;
	}

	public Operation getNextOperation() {
		return this.nextOperation;
	}

	public void addPage( WizardPageComposite<?, ?> page ) {
		this.cardComposite.addCard( page );
	}

	public void removePage( WizardPageComposite<?, ?> page ) {
		this.cardComposite.removeCard( page );
	}

	public Iterator<WizardPageComposite<?, ?>> getWizardPageIterator() {
		return (Iterator)this.cardComposite.getCards().iterator();
	}

	private MigPanel createAdornmentPanel( SwingComponentView<?> header ) {
		MigPanel rv = new MigPanel( null, "fill, inset 16", "", "[grow 0, shrink 0][grow 0, shrink 0][grow, shrink]" );
		header.changeFont( TextWeight.ULTRABOLD );
		rv.addComponent( header, "wrap" );
		rv.addComponent( Separator.createInstanceSeparatingTopFromBottom(), "growx, wrap" );
		return rv;
	}

	@Override
	protected Panel createView() {
		CardPanel cardPanel = this.cardComposite.getView();
		Panel rv;
		if( this.isAdornmentDesired() ) {
			AbstractLabel stepsLabel = this.stepsLabel.createLabel();
			JList list = new JList( this.listModel ) {
				@Override
				public boolean contains( int x, int y ) {
					return false;
				}
			};
			list.setSelectionModel( this.listSelectionModel );
			list.setAlignmentX( 0.0f );
			//list.setEnabled( false );
			list.setCellRenderer( this.listCellRenderer );

			MigPanel stepsView = this.createAdornmentPanel( stepsLabel );
			stepsView.setBackgroundColor( Color.WHITE );
			stepsView.getAwtComponent().add( list, "aligny top" );

			MigPanel mainView = this.createAdornmentPanel( this.stepLabel );
			mainView.addComponent( cardPanel, "aligny top" );

			rv = new BorderPanel.Builder()
					.lineStart( stepsView )
					.center( mainView )
					.build();
			rv.setBackgroundColor( cardPanel.getBackgroundColor() );
		} else {
			rv = cardPanel; //note: the composite is not correct.  worth addressing?
		}
		return rv;
	}

	@Override
	protected final Status getStatusPreRejectorCheck() {
		Composite<?> page = this.cardComposite.getShowingCard();
		if( page instanceof WizardPageComposite ) {
			return ( (WizardPageComposite)page ).getPageStatus();
		} else {
			Logger.todo( this, page );
			//todo
			return IS_GOOD_TO_GO_STATUS;
		}
	}

	@Override
	protected String getCommitUiKey() {
		return null;
	}

	@Override
	protected String getDefaultCommitText() {
		return this.findLocalizedText( "finish" );
	}

	@Override
	protected void localize() {
		super.localize();
		this.nextOperation.setName( this.findLocalizedText( "next" ) );
		this.prevOperation.setName( this.findLocalizedText( "previous" ) );
	}

	protected boolean isAdornmentDesired() {
		return true;
	}

	private void updateEnabled() {
		this.nextOperation.setEnabled( this.isNextPageAvailable() );
		this.prevOperation.setEnabled( this.isPrevPageAvailable() );
	}

	@Override
	public void updateIsGoodToGo( boolean isGoodToGo ) {
		this.nextOperation.setEnabled( this.isNextPageAvailable() && isGoodToGo );
		boolean isCommitEnabled = isGoodToGo;
		if( isGoodToGo ) {
			List<Composite<?>> cards = this.cardComposite.getCards();
			for( int i = this.index + 1; i < cards.size(); i++ ) {
				WizardPageComposite page = (WizardPageComposite)cards.get( i );
				if( page.isClearToCommit() ) {
					//pass
				} else {
					isCommitEnabled = false;
					break;
				}
			}
		}
		this.getCommitOperation().setEnabled( isCommitEnabled );
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.index = -1;
		this.next( true );
		this.cardComposite.handlePreActivation();
		Iterator<WizardPageComposite<?, ?>> itr = getWizardPageIterator();
		while( itr.hasNext() ) {
			WizardPageComposite<?, ?> next = itr.next();
			next.resetData();
		}
	}

	@Override
	public void handlePostDeactivation() {
		this.cardComposite.handlePostDeactivation();
		super.handlePostDeactivation();
	}

	@Override
	protected void handlePreShowDialog( Dialog dialog ) {
		for( Composite<?> subComposite : this.cardComposite.getCards() ) {
			if( subComposite instanceof WizardPageComposite<?, ?> ) {
				WizardPageComposite<?, ?> wizardPage = (WizardPageComposite<?, ?>)subComposite;
				wizardPage.handlePreShowDialog();
			}
		}
		super.handlePreShowDialog( dialog );
	}

	@Override
	protected void handlePostHideDialog() {
		super.handlePostHideDialog();
		for( Composite<?> subComposite : this.cardComposite.getCards() ) {
			if( subComposite instanceof WizardPageComposite<?, ?> ) {
				WizardPageComposite<?, ?> wizardPage = (WizardPageComposite<?, ?>)subComposite;
				wizardPage.handlePostHideDialog();
			}
		}
	}
}
