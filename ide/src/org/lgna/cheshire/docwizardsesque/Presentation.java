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

/**
 * @author Dennis Cosgrove
 */
public class Presentation extends org.lgna.cheshire.Presentation {
	/*package-private*/static edu.cmu.cs.dennisc.croquet.Group IMPLEMENTATION_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "90e2f36f-512c-41e4-8e7b-61e10c95cee6" ), "IMPLEMENTATION_GROUP" );
	/*package-private*/static java.awt.Color CONTROL_COLOR = new java.awt.Color( 230, 230, 255 );

	@Override
	protected org.lgna.cheshire.Chapter createChapter(org.lgna.croquet.steps.Transaction transaction) {
		return new org.lgna.cheshire.TransactionChapter( transaction );
	}
	private final edu.cmu.cs.dennisc.croquet.Frame frame = new edu.cmu.cs.dennisc.croquet.Frame();
	private final javax.swing.JTree jTree;
	private final BookTreeModel bookTreeModel;
	public Presentation( 
			edu.cmu.cs.dennisc.croquet.UserInformation userInformation, 
			org.lgna.croquet.steps.TransactionHistory originalTransactionHistory,
			org.lgna.cheshire.Filterer filterer,
			org.lgna.cheshire.Recoverer recoverer,
			edu.cmu.cs.dennisc.croquet.Group[] groupsTrackedForRandomAccess
	) {
		super( userInformation, org.lgna.cheshire.ChapterAccessPolicy.ALLOW_ACCESS_TO_ALL_CHAPTERS, originalTransactionHistory, filterer, recoverer, groupsTrackedForRandomAccess );
		
		this.frame.setTitle( "DocWizardsesque" );
		this.frame.setLocation( 1400, 0 );
		this.frame.setSize( 240, 800 );
		
		this.frame.addWindowListener( new java.awt.event.WindowListener() {

			public void windowOpened( java.awt.event.WindowEvent e ) {
				Presentation.this.startListening();
			}

			public void windowClosing( java.awt.event.WindowEvent e ) {
			}

			public void windowClosed( java.awt.event.WindowEvent e ) {
				Presentation.this.stopListening();
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
		
		edu.cmu.cs.dennisc.croquet.BorderPanel contentPanel = this.frame.getContentPanel();
		
		edu.cmu.cs.dennisc.croquet.FlowPanel flowPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel();
		flowPanel.addComponent( DoSingleStepOperation.getInstance().createButton() );
		flowPanel.addComponent( DoAllStepsOperation.getInstance().createButton() );

		contentPanel.addComponent( flowPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.PAGE_START );

		this.bookTreeModel = new BookTreeModel( this.getBook() );
		this.jTree = new javax.swing.JTree( this.bookTreeModel );
		for( int i = 0; i < jTree.getRowCount(); i++ ) {
			this.jTree.expandRow( i );
		}
		this.jTree.setSelectionRow( 0 );
		this.jTree.setCellRenderer( new BookTreeCellRenderer() );
		this.jTree.setRootVisible( false );
		edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( new edu.cmu.cs.dennisc.croquet.SwingAdapter( jTree ) );
		contentPanel.addComponent( scrollPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
	}
	@Override
	protected void handleTransactionCanceled( org.lgna.croquet.steps.Transaction transaction ) {
		this.restoreHistoryIndicesDueToCancel();
	}
	private boolean isIgnoringEvents;
	
	private final void handlePotentialReplacementEdit( org.lgna.cheshire.TransactionChapter transactionChapter, edu.cmu.cs.dennisc.croquet.Edit< ? > replacementCandidateEdit ) {
		edu.cmu.cs.dennisc.croquet.Edit< ? > originalEdit = transactionChapter.getTransaction().getEdit();
		edu.cmu.cs.dennisc.croquet.Group group = replacementCandidateEdit.getGroup();
		if( group == Presentation.IMPLEMENTATION_GROUP || group == org.lgna.cheshire.Presentation.COMPLETION_GROUP ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.croquet.ReplacementAcceptability replacementAcceptability = originalEdit.getReplacementAcceptability( replacementCandidateEdit, this.getUserInformation() );
			if( replacementAcceptability.isAcceptable() ) {
				transactionChapter.setReplacementAcceptability( replacementAcceptability );
				this.incrementSelectedIndex();
			} else {
				edu.cmu.cs.dennisc.croquet.Application.getSingleton().showMessageDialog( "you have strayed off course" );
			}
		}
	}
	@Override
	protected void handleEvent( org.lgna.cheshire.events.Event event ) {
		if( isIgnoringEvents ) {
			//pass
		} else {
			org.lgna.cheshire.Book book = getBook();
			org.lgna.cheshire.Chapter chapter = book.getSelectedChapter();
			if( chapter instanceof org.lgna.cheshire.TransactionChapter ) {
				org.lgna.cheshire.TransactionChapter transactionChapter = (org.lgna.cheshire.TransactionChapter)chapter;
				if( event instanceof org.lgna.cheshire.events.EditCommittedEvent ) {
					org.lgna.cheshire.events.EditCommittedEvent editCommittedEvent = (org.lgna.cheshire.events.EditCommittedEvent)event;
					edu.cmu.cs.dennisc.croquet.Edit< ? > replacementCandidateEdit = editCommittedEvent.getEdit();
					org.lgna.croquet.steps.CompletionStep< ? > completionStep = replacementCandidateEdit.getCompletionStep();
					org.lgna.croquet.steps.Transaction transaction = completionStep.getParent();
					org.lgna.croquet.steps.TransactionHistory transactionHistory = transaction.getParent();
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
				}
			}
		}
	}
	@Override
	protected void handleChapterChanged( org.lgna.cheshire.Chapter chapter ) {
		super.handleChapterChanged( chapter );
		javax.swing.tree.TreePath treePath = new javax.swing.tree.TreePath( new Object[] { this.getBook(), chapter } );
		this.jTree.setSelectionPath( treePath );
	}
	
	public void setVisible( boolean isVisible ) {
		this.frame.setVisible( isVisible );
	}
}
