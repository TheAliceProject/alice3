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

package org.lgna.cheshire.docwizardsesque;

import org.lgna.cheshire.stencil.StencilsPresentation;

/**
 * @author Dennis Cosgrove
 */
public class DocWizardsesquePresentation extends org.lgna.cheshire.Presentation {
	/*package-private*/static org.lgna.croquet.Group IMPLEMENTATION_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "90e2f36f-512c-41e4-8e7b-61e10c95cee6" ), "IMPLEMENTATION_GROUP" );
	/*package-private*/static java.awt.Color CONTROL_COLOR = new java.awt.Color( 230, 230, 255 );

	@Override
	protected org.lgna.cheshire.Chapter createChapter(org.lgna.croquet.history.Transaction transaction) {
		return new org.lgna.cheshire.TransactionChapter( transaction );
	}
	private final org.lgna.croquet.components.Frame frame = new org.lgna.croquet.components.Frame();
	private final PreviewComponent previewComponent = new PreviewComponent();
	private final OffTrackPanel offTrackPanel = new OffTrackPanel();
	private final org.lgna.croquet.components.CardPanel cardPanel = new org.lgna.croquet.components.CardPanel();
	private final org.lgna.croquet.components.CardPanel.Key previewKey;
	private final org.lgna.croquet.components.CardPanel.Key offTrackKey;
	private final javax.swing.JTree jTree;
	private final BookTreeModel bookTreeModel;
	private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		public void mouseMoved( java.awt.event.MouseEvent e ) {
			javax.swing.tree.TreePath treePath = jTree.getPathForLocation( e.getX(), e.getY() );
			if( treePath != null ) {
				Object[] path = treePath.getPath();
				if( path.length > 1 ) {
					if( path[ 1 ] instanceof org.lgna.cheshire.TransactionChapter  ) {
						org.lgna.cheshire.TransactionChapter transactionChapter = (org.lgna.cheshire.TransactionChapter)path[ 1 ];
						org.lgna.croquet.edits.ReplacementAcceptability replacementAcceptability = transactionChapter.getReplacementAcceptability();
						if( replacementAcceptability != null ) {
							if( replacementAcceptability.isAcceptable() ) {
								if( replacementAcceptability.isDeviation() ) {
									StringBuilder sbToolTip = new StringBuilder();
									sbToolTip.append( replacementAcceptability.getDeviationSeverity().getRepr( StencilsPresentation.getInstance().getUserInformation() ) );
									sbToolTip.append( ": " );
									sbToolTip.append( replacementAcceptability.getDeviationDescription() );
									jTree.setToolTipText( sbToolTip.toString() );
								}
							}
						}
					}
				}
			}
		}
		public void mouseDragged( java.awt.event.MouseEvent e ) {
		}
	};
	public DocWizardsesquePresentation( 
			org.lgna.croquet.UserInformation userInformation, 
			org.lgna.croquet.history.TransactionHistory originalTransactionHistory,
			org.lgna.croquet.migration.MigrationManager migrationManager,
			org.lgna.cheshire.Filterer filterer,
			org.lgna.cheshire.Recoverer recoverer,
			org.lgna.croquet.Group[] groupsTrackedForRandomAccess
	) {
		super( userInformation, org.lgna.cheshire.ChapterAccessPolicy.ALLOW_ACCESS_TO_ALL_CHAPTERS, originalTransactionHistory, migrationManager, filterer, recoverer, groupsTrackedForRandomAccess );
		
		this.frame.setTitle( "DocWizardsesque" );
		this.frame.setLocation( 0, 0 );
		this.frame.setSize( 280, 720 );
		
		this.frame.addWindowListener( new java.awt.event.WindowListener() {

			public void windowOpened( java.awt.event.WindowEvent e ) {
				DocWizardsesquePresentation.this.startListening();
			}

			public void windowClosing( java.awt.event.WindowEvent e ) {
			}

			public void windowClosed( java.awt.event.WindowEvent e ) {
				DocWizardsesquePresentation.this.stopListening();
			}

			public void windowIconified( java.awt.event.WindowEvent e ) {
			}

			public void windowDeiconified( java.awt.event.WindowEvent e ) {
			}

			public void windowActivated( java.awt.event.WindowEvent e ) {
			}

			public void windowDeactivated( java.awt.event.WindowEvent e ) {
			}
			
		} );
		
		org.lgna.croquet.components.BorderPanel contentPanel = this.frame.getContentPanel();
		
		org.lgna.croquet.components.FlowPanel flowPanel = new org.lgna.croquet.components.FlowPanel();
		flowPanel.addComponent( DoSingleStepOperation.getInstance().createButton() );
		flowPanel.addComponent( DoAllStepsOperation.getInstance().createButton() );

		org.lgna.croquet.components.Label previewLabel = new org.lgna.croquet.components.Label( "start with:", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
		org.lgna.croquet.components.PageAxisPanel previewPanel = new org.lgna.croquet.components.PageAxisPanel( previewLabel, this.previewComponent );
		
		this.previewKey = cardPanel.createKey( previewPanel, java.util.UUID.randomUUID() ); 
		this.offTrackKey = cardPanel.createKey( this.offTrackPanel, java.util.UUID.randomUUID() );
		cardPanel.addComponent( this.previewKey );
		cardPanel.addComponent( this.offTrackKey );
		cardPanel.setMinimumPreferredHeight( 50 );
		cardPanel.show( this.previewKey );
		
		org.lgna.croquet.components.VerticalSplitPane splitPane = new org.lgna.croquet.components.VerticalSplitPane(flowPanel, this.cardPanel );
		
		this.bookTreeModel = new BookTreeModel( this.getBook() );
		this.jTree = new javax.swing.JTree( this.bookTreeModel );
		for( int i = 0; i < jTree.getRowCount(); i++ ) {
			this.jTree.expandRow( i );
		}
		this.jTree.setShowsRootHandles( false );
		this.jTree.setSelectionRow( 0 );
		this.jTree.setCellRenderer( new BookTreeCellRenderer() );
		this.jTree.setRootVisible( false );
		this.jTree.addMouseMotionListener( this.mouseMotionListener );
		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( new org.lgna.croquet.components.SwingAdapter( jTree ) );
		contentPanel.addComponent( splitPane, org.lgna.croquet.components.BorderPanel.Constraint.PAGE_START );
		contentPanel.addComponent( scrollPane, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
	}
	@Override
	protected void handleTransactionCanceled( org.lgna.croquet.history.Transaction transaction ) {
		this.restoreHistoryIndicesDueToCancel();
	}
	private boolean isIgnoringEvents;
	
	private final void handlePotentialReplacementEdit( org.lgna.cheshire.TransactionChapter transactionChapter, org.lgna.croquet.edits.Edit< ? > replacementCandidateEdit ) {
		org.lgna.croquet.edits.Edit< ? > originalEdit = transactionChapter.getTransaction().getEdit();
		org.lgna.croquet.Group group = replacementCandidateEdit.getGroup();
		if( group == DocWizardsesquePresentation.IMPLEMENTATION_GROUP ) {
			//pass
		} else {
			org.lgna.croquet.edits.ReplacementAcceptability replacementAcceptability = originalEdit.getReplacementAcceptability( replacementCandidateEdit, this.getUserInformation() );
			if( replacementAcceptability.isAcceptable() ) {
				transactionChapter.setReplacementAcceptability( replacementAcceptability );
				this.incrementSelectedIndex();
			} else {
				this.cardPanel.show( this.offTrackKey );
			}
		}
	}
	@Override
	protected void handleEvent( org.lgna.croquet.history.event.Event event ) {
		this.jTree.repaint();
		this.previewComponent.repaint();
		if( isIgnoringEvents ) {
			//pass
		} else {
			org.lgna.cheshire.Book book = getBook();
			org.lgna.cheshire.Chapter chapter = book.getSelectedChapter();
			if( chapter instanceof org.lgna.cheshire.TransactionChapter ) {
				org.lgna.cheshire.TransactionChapter transactionChapter = (org.lgna.cheshire.TransactionChapter)chapter;
				if( event instanceof org.lgna.croquet.history.event.EditCommittedEvent ) {
					org.lgna.croquet.history.event.EditCommittedEvent editCommittedEvent = (org.lgna.croquet.history.event.EditCommittedEvent)event;
					org.lgna.croquet.edits.Edit< ? > replacementCandidateEdit = editCommittedEvent.getEdit();
					org.lgna.croquet.history.CompletionStep< ? > completionStep = replacementCandidateEdit.getCompletionStep();
					org.lgna.croquet.history.Transaction transaction = completionStep.getParent();
					org.lgna.croquet.history.TransactionHistory transactionHistory = transaction.getParent();
					if( transactionHistory.getParent() != null ) {
						//pass
					} else {
						try { 
							isIgnoringEvents = true;
							this.handlePotentialReplacementEdit( transactionChapter, replacementCandidateEdit );
						} finally {
							isIgnoringEvents = false;
						}
					}
				} else if( event instanceof org.lgna.croquet.history.event.FinishedEvent ) {
					org.lgna.croquet.history.event.FinishedEvent finishEvent = (org.lgna.croquet.history.event.FinishedEvent)event;
					org.lgna.croquet.history.Transaction transaction = finishEvent.getTransaction();
					org.lgna.croquet.history.TransactionHistory transactionHistory = transaction.getParent();
					if( transactionHistory.getParent() != null ) {
						//pass
					} else {
						if( transaction.getCompletionStep().getModel() == org.alice.stageide.croquet.models.run.RunOperation.getInstance() ) {
							transactionChapter.setReplacementAcceptability( org.lgna.croquet.edits.ReplacementAcceptability.PERFECT_MATCH );
							try { 
								isIgnoringEvents = true;
								this.incrementSelectedIndex();
							} finally {
								isIgnoringEvents = false;
							}
						}
					}
				}
			}
		}
	}
	@Override
	protected void handleChapterChanged( org.lgna.cheshire.Chapter chapter ) {
		super.handleChapterChanged( chapter );
		javax.swing.tree.TreePath treePath = new javax.swing.tree.TreePath( new Object[] { this.getBook(), chapter } );
		this.jTree.setSelectionPath( treePath );
		this.jTree.scrollPathToVisible( treePath );
		this.previewComponent.repaint();
	}
	
	/*package-private*/ void getBackOnTrack() {
		this.restoreHistoryIndicesDueToCancel();
		this.cardPanel.show( this.previewKey );
	}
	@Override
	public void setVisible( boolean isVisible ) {
		this.frame.setVisible( isVisible );
		
	}
}
