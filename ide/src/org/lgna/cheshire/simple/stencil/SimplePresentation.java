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
package org.lgna.cheshire.simple.stencil;

/**
 * @author Dennis Cosgrove
 */
public class SimplePresentation extends org.lgna.cheshire.simple.Presentation {

	/* package-private */static java.awt.Color CONTROL_COLOR = new java.awt.Color( 230, 230, 255 );

	private final org.lgna.croquet.Operation prevOperation = new PrevStepOperation( this );

	@Override
	protected org.lgna.cheshire.simple.Chapter createChapter( org.lgna.croquet.history.Transaction transaction ) {
		return new org.lgna.cheshire.simple.TransactionChapter( transaction );
	}

	public class Stencil extends org.lgna.cheshire.simple.SimpleStencil {
		private org.lgna.croquet.components.CardPanel cardPanel = new org.lgna.croquet.components.CardPanel();

		public Stencil( org.lgna.croquet.components.AbstractWindow<?> window, org.lgna.cheshire.simple.ScrollRenderer scrollingRequiredRenderer, LayerId menuPolicy ) {
			super( window );
			org.lgna.croquet.components.BorderPanel controlsPanel = new org.lgna.croquet.components.BorderPanel();
			controlsPanel.setOpaque( false );
			org.lgna.croquet.components.FlowPanel controlPanel = new org.lgna.croquet.components.FlowPanel( org.lgna.croquet.components.FlowPanel.Alignment.CENTER, 2, 0 );
			controlPanel.addComponent( SimplePresentation.this.prevOperation.createButton() );
			controlPanel.addComponent( new BookComboBox( SimplePresentation.this.bookComboBoxModel, menuPolicy.isAboveStencil() ) );
			controlPanel.addComponent( NextStepOperation.getInstance().createButton() );
			controlsPanel.addCenterComponent( controlPanel );
			controlsPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );

			for( java.awt.Component component : edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findAllMatches( controlsPanel.getAwtComponent() ) ) {
				if( component instanceof javax.swing.JPanel ) {
					//pass
				} else {
					component.setCursor( java.awt.Cursor.getDefaultCursor() );
				}
			}

			this.cardPanel.setBackgroundColor( null );
			this.internalAddComponent( controlsPanel, java.awt.BorderLayout.PAGE_START );
			this.internalAddComponent( this.cardPanel, java.awt.BorderLayout.CENTER );
			this.addMouseWheelListener( this.mouseWheelListener );
		}

		@Override
		protected org.lgna.cheshire.simple.Page getCurrentPage() {
			return ChapterPage.getInstance( SimplePresentation.this.getBook().getSelectedChapter() );
		}

		private java.awt.event.KeyListener keyListener = new java.awt.event.KeyListener() {
			public void keyPressed( java.awt.event.KeyEvent e ) {
				int keyCode = e.getKeyCode();
				switch( keyCode ) {
				case java.awt.event.KeyEvent.VK_SPACE:
				case java.awt.event.KeyEvent.VK_RIGHT:
				case java.awt.event.KeyEvent.VK_DOWN:
					NextStepOperation.getInstance().fire();
					break;
				case java.awt.event.KeyEvent.VK_BACK_SPACE:
				case java.awt.event.KeyEvent.VK_LEFT:
				case java.awt.event.KeyEvent.VK_UP:
					prevOperation.fire();
					break;
				}
			}

			public void keyReleased( java.awt.event.KeyEvent e ) {
			}

			public void keyTyped( java.awt.event.KeyEvent e ) {
			}
		};
		private java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
			private java.awt.Cursor prevCursor = null;

			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}

			public void mouseExited( java.awt.event.MouseEvent e ) {
			}

			public void mousePressed( java.awt.event.MouseEvent e ) {
				java.awt.Component component = e.getComponent();
				this.prevCursor = component.getCursor();
				component.setCursor( java.awt.dnd.DragSource.DefaultMoveNoDrop );
			}

			public void mouseReleased( java.awt.event.MouseEvent e ) {
				java.awt.Component component = e.getComponent();
				component.setCursor( this.prevCursor );
				this.prevCursor = null;
			}

			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}
		};

		// todo: I think this is a really bad way to allow scrolling in the stencil... do this better.
		private final java.awt.event.MouseWheelListener mouseWheelListener = new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved( java.awt.event.MouseWheelEvent e ) {
				java.awt.Point p = e.getPoint();
				java.awt.Component component = javax.swing.SwingUtilities.getDeepestComponentAt( org.lgna.croquet.Application.getActiveInstance().getFrame().getAwtComponent().getContentPane(), p.x, p.y );
				if( component != null ) {
					java.awt.Point pComponent = javax.swing.SwingUtilities.convertPoint( e.getComponent(), p, component );
					component.dispatchEvent( new java.awt.event.MouseWheelEvent( component, e.getID(), e.getWhen(), e.getModifiers() + e.getModifiersEx(), pComponent.x, pComponent.y, e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e
							.getScrollAmount(), e.getWheelRotation() ) );
				}
			}
		};

		@Override
		protected void handleDisplayable() {
			super.handleDisplayable();
			SimplePresentation.this.startListening();
			this.addKeyListener( this.keyListener );
			this.addMouseListener( this.mouseListener );
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					SimplePresentation.this.handleChapterChanged( SimplePresentation.this.getBook().getSelectedChapter() );
				}
			} );
		}

		@Override
		protected void handleUndisplayable() {
			this.removeMouseListener( this.mouseListener );
			this.removeKeyListener( this.keyListener );
			SimplePresentation.this.stopListening();
			super.handleUndisplayable();
		}

		@Override
		protected LayerId getStencilsLayer() {
			return LayerId.ABOVE_POPUP_LAYER;
		}

		@Override
		protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
			return new java.awt.BorderLayout();
		}
	}

	private final org.lgna.croquet.history.event.Listener transactionListener = new org.lgna.croquet.history.event.Listener() {
		public void changing( org.lgna.croquet.history.event.Event<?> e ) {
		}

		public void changed( org.lgna.croquet.history.event.Event<?> e ) {
			if( e instanceof org.lgna.croquet.history.event.AddTransactionEvent ) {
				SimplePresentation.this.insertRecoveryChapter( ( (org.lgna.croquet.history.event.AddTransactionEvent)e ).getTransaction(), ( (org.lgna.croquet.history.event.AddTransactionEvent)e ).getIndex() );
			}
		}
	};

	private BookComboBoxModel bookComboBoxModel;
	private Stencil stencil;
	final private org.lgna.croquet.Application application;

	private boolean isIgnoringEvents = false;

	public SimplePresentation( org.lgna.croquet.Application application ) {
		super();
		this.application = application;
	}

	@Override
	public void initializePresentation( org.lgna.cheshire.simple.ChapterAccessPolicy transactionAccessPolicy, org.lgna.croquet.history.TransactionHistory originalTransactionHistory, org.lgna.croquet.migration.MigrationManager migrationManager,
			org.lgna.cheshire.Filterer filterer, org.lgna.cheshire.Recoverer recoverer, org.lgna.croquet.Group[] groupsTrackedForRandomAccess ) {
		super.initializePresentation( transactionAccessPolicy, originalTransactionHistory, migrationManager, filterer, recoverer, groupsTrackedForRandomAccess );
		this.originalTransactionHistory.addListener( this.transactionListener );
		this.setRetargeter( new org.lgna.cheshire.simple.AstLiveRetargeter() );
		this.bookComboBoxModel = new BookComboBoxModel( this.getBook() );
		this.stencil = new Stencil( this.application.getFrame(), new org.lgna.cheshire.simple.SimpleScrollRenderer(), org.lgna.croquet.components.Stencil.LayerId.ABOVE_POPUP_LAYER );
	}

	protected void handleTransactionCanceled() {
		this.restoreHistoryIndicesDueToCancel();
		org.lgna.cheshire.simple.Chapter chapter = this.getBook().getSelectedChapter();
		if( chapter != null ) {
			ChapterPage chapterPage = ChapterPage.getInstance( chapter );
			chapterPage.reset();
		}
	}

	@Override
	protected void handleEvent( org.lgna.croquet.history.event.Event<?> event ) {
		if( this.isIgnoringEvents ) {
			//pass
		} else if( event instanceof org.lgna.croquet.history.event.CancelEvent ) {
			org.lgna.croquet.history.event.CancelEvent cancelEvent = (org.lgna.croquet.history.event.CancelEvent)event;
			try {
				org.lgna.croquet.history.TransactionHistory history = cancelEvent.getNode().getOwnerTransaction().getOwnerTransactionHistory();
				if( history.getOwner() != null ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "ignoring", cancelEvent );
				} else {
					this.handleTransactionCanceled();
				}
			} catch( Throwable t ) {
				t.printStackTrace();
			}
		} else {
			org.lgna.cheshire.simple.Book book = getBook();
			org.lgna.cheshire.simple.Chapter chapter = book.getSelectedChapter();

			if( event instanceof org.lgna.croquet.history.event.EditCommittedEvent ) {
				org.lgna.croquet.history.event.EditCommittedEvent editCommittedEvent = (org.lgna.croquet.history.event.EditCommittedEvent)event;
				org.lgna.croquet.edits.Edit<?> replacementCandidate = editCommittedEvent.getEdit();
				book.handleEditCommitted( replacementCandidate );
			}

			ChapterPage chapterPage = ChapterPage.getInstance( chapter );
			chapterPage.adjustIfNecessary( event );
			if( chapterPage.isWhatWeveBeenWaitingFor( event ) ) {
				NextStepOperation.getInstance().setEnabled( true );
				if( chapterPage.isAutoAdvanceDesired() ) {
					javax.swing.SwingUtilities.invokeLater( new Runnable() {
						public void run() {
							SimplePresentation.this.isIgnoringEvents = true;
							try {
								NextStepOperation.getInstance().fire();
							} finally {
								SimplePresentation.this.isIgnoringEvents = false;
							}
						}
					} );
				}
			}
		}
	}

	@Override
	protected void handleChapterChanged( org.lgna.cheshire.simple.Chapter chapter ) {
		super.handleChapterChanged( chapter );
		if( chapter != null ) {
			ChapterPage chapterPage = ChapterPage.getInstance( chapter );
			chapterPage.refreshNotes();
			Iterable<org.lgna.croquet.Context> contexts = chapter.getAllContexts();
			for( org.lgna.croquet.Context context : contexts ) {
				if( context.isGoodToGo() ) {
					//pass
				} else {
					org.lgna.croquet.history.Transaction[] recoveryTransactions = context.createRecoveryTransactions();
					for( org.lgna.croquet.history.Transaction recoveryTransaction : recoveryTransactions ) {
						this.insertRecoveryTransaction( recoveryTransaction );
					}
					return;
				}
			}
			if( chapterPage.isGoodToGo() ) {
				java.awt.Cursor cursor = java.awt.Cursor.getDefaultCursor();
				if( chapterPage.isStencilRenderingDesired() ) {
					cursor = java.awt.dnd.DragSource.DefaultMoveNoDrop;
				}
				chapterPage.reset();
				java.util.UUID transactionId = chapter.getId();
				org.lgna.croquet.components.CardPanel.Key key = this.stencil.cardPanel.getKey( transactionId );
				if( key != null ) {
					// pass
				} else {
					key = this.stencil.cardPanel.createKey( chapterPage.getCard(), transactionId );
					this.stencil.cardPanel.addComponent( key );
				}
				this.stencil.cardPanel.showKey( key );
				this.stencil.revalidateAndRepaint();

				int selectedIndex = this.getBook().getSelectedIndex();

				//				boolean isAutoAdvanceDesired = false;
				boolean isWaiting = chapterPage.getChapter().isAlreadyInTheDesiredState() == false;

				NextStepOperation.getInstance().setEnabled( ( 0 <= selectedIndex ) && ( selectedIndex < ( this.getBook().getChapterCount() - 1 ) ) && ( isWaiting == false ) );

				this.prevOperation.setEnabled( 1 <= selectedIndex );

				this.stencil.requestFocus();
				this.stencil.revalidateAndRepaint();
				this.stencil.setCursor( cursor );
			} else {
				org.lgna.croquet.history.Transaction transaction = ( (org.lgna.cheshire.simple.TransactionChapter)chapter ).getTransaction();

				org.lgna.croquet.history.CompletionStep<?> completionStep = transaction.getCompletionStep();
				org.lgna.croquet.CompletionModel completionModel = completionStep.getModel();

				// TODO: This should probably not be done here... this should probably be part of croquet contexts or something...
				org.lgna.croquet.history.PrepStep<?>[] prepSteps = transaction.getPrepStepsAsArray();
				transaction.removeAllPrepSteps();
				chapterPage.refreshNotes();
				if( chapterPage.isGoodToGo() ) {
					this.handleChapterChanged( chapter );
				} else {
					java.util.List<org.lgna.croquet.MenuItemPrepModel> menuItemPrepModels = this.huntForInMenus( transaction.getCompletionStep().getModel() );
					if( menuItemPrepModels != null ) {
						chapterPage.refreshNotes();
						if( chapterPage.isGoodToGo() ) {
							this.handleChapterChanged( chapter );
						}
					} else {
						boolean isPrepModelSearchSuccessful = false;
						Iterable<? extends org.lgna.croquet.PrepModel> prepModels = completionModel.getPotentialRootPrepModels();
						assert prepModels != null : completionModel;
						for( org.lgna.croquet.PrepModel prepModel : prepModels ) {
							//todo
							if( prepModel instanceof org.lgna.croquet.PopupPrepModel ) {
								org.lgna.croquet.PopupPrepModel popupPrepModel = (org.lgna.croquet.PopupPrepModel)prepModel;
								org.lgna.croquet.history.PopupPrepStep.createAndAddToTransaction( transaction, popupPrepModel, org.lgna.croquet.triggers.MouseEventTrigger.createRecoveryInstance() );
								chapterPage.refreshNotes();
								if( chapterPage.isGoodToGo() ) {
									org.lgna.croquet.edits.Edit edit = completionStep.getEdit();
									if( edit instanceof org.lgna.croquet.edits.StateEdit ) {
										org.lgna.croquet.edits.StateEdit stateEdit = (org.lgna.croquet.edits.StateEdit)edit;
										if( completionModel instanceof org.alice.ide.declarationseditor.TypeState ) {
											org.lgna.project.ast.AbstractType<?, ?, ?> type = (org.lgna.project.ast.AbstractType<?, ?, ?>)stateEdit.getNextValue();
											org.lgna.croquet.MenuItemPrepModel[] stateValueMenuItemPrepModels = { org.alice.ide.croquet.models.ast.declaration.TypeFillIn.getInstance( type ) };
											org.lgna.croquet.history.MenuItemSelectStep.createAndAddToTransaction( transaction, null, stateValueMenuItemPrepModels, org.lgna.croquet.triggers.ChangeEventTrigger.createRecoveryInstance() );
										} else if( completionModel instanceof org.alice.ide.instancefactory.croquet.InstanceFactoryState ) {
											org.alice.ide.instancefactory.InstanceFactory instanceFactory = (org.alice.ide.instancefactory.InstanceFactory)stateEdit.getNextValue();
											org.lgna.croquet.MenuItemPrepModel[] stateValueMenuItemPrepModels = { org.alice.ide.instancefactory.croquet.InstanceFactoryFillIn.getInstance( instanceFactory ) };
											org.lgna.croquet.history.MenuItemSelectStep.createAndAddToTransaction( transaction, null, stateValueMenuItemPrepModels, org.lgna.croquet.triggers.ChangeEventTrigger.createRecoveryInstance() );
										}
									}
									this.handleChapterChanged( chapter );
									isPrepModelSearchSuccessful = true;
								}
							}
							if( isPrepModelSearchSuccessful ) {
								break;
							} else {
								transaction.removeAllPrepSteps();
							}
						}

						if( isPrepModelSearchSuccessful ) {
							//pass
						} else {
							transaction.setPrepSteps( prepSteps );
							chapterPage.refreshNotes();
							org.lgna.croquet.history.Transaction tabSelectionRecoveryTransaction = this.createTabSelectionRecoveryTransactionIfAppropriate( transaction );
							if( tabSelectionRecoveryTransaction != null ) {
								this.insertRecoveryTransaction( tabSelectionRecoveryTransaction );
							} else {
								org.lgna.croquet.history.Transaction applicationRecoveryTransaction = this.getRecoverer().createTransactionToGetCloserToTheRightStateWhenNoViewControllerCanBeFound( transaction );
								if( applicationRecoveryTransaction != null ) {
									this.insertRecoveryTransaction( applicationRecoveryTransaction );
								} else {

									final org.lgna.cheshire.simple.Chapter accessibleChapter = chapter;
									edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "detailed report:\n\n\n" + chapterPage.getDetailedReport() + "\n\n\n" );
									javax.swing.SwingUtilities.invokeLater( new Runnable() {
										public void run() {
											javax.swing.Icon icon = null;
											String tryAgain = "try again";
											String giveUp = "give up";
											Object o = org.lgna.croquet.Application.getActiveInstance().showOptionDialog( "unable to recover", "Unable to Recover", org.lgna.croquet.MessageType.ERROR, icon, tryAgain, giveUp, 0 );
											if( o == tryAgain ) {
												handleChapterChanged( accessibleChapter );
											}
										}
									} );
									//edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "unable to recover", transaction );
								}
							}
						}
					}
				}
			}
		}
	}

	private void insertRecoveryTransaction( org.lgna.croquet.history.Transaction recoveryTransaction ) {
		assert recoveryTransaction.getCompletionStep().getTrigger().getOrigin() == org.lgna.croquet.triggers.Trigger.Origin.RECOVERY : recoveryTransaction + " must have trigger origin of RECOVERY";
		org.lgna.cheshire.simple.TransactionChapter chapter = (org.lgna.cheshire.simple.TransactionChapter)this.getBook().getSelectedChapter();
		org.lgna.croquet.history.Transaction currentTransaction = chapter.getTransaction();
		boolean insertedTransaction = false;
		for( int i = 0; i < this.originalTransactionHistory.getTransactionCount(); i++ ) {
			if( currentTransaction == this.originalTransactionHistory.getTransactionAt( i ) ) {
				originalTransactionHistory.addTransaction( i, recoveryTransaction );
				insertedTransaction = true;
				break;
			}
		}
		assert insertedTransaction;
	}

	private void insertRecoveryChapter( org.lgna.croquet.history.Transaction recoveryTransaction, int recoveryIndex ) {
		org.lgna.cheshire.simple.Chapter recoveryChapter = new org.lgna.cheshire.simple.TransactionChapter( recoveryTransaction );
		this.getBook().addChapter( recoveryIndex, recoveryChapter );
		this.handleChapterChanged( recoveryChapter );
		this.stencil.revalidateAndRepaint();
	}

	@Override
	protected void handleStateChange( boolean isVisible ) {
		assert this.stencil != null;

		// Adjust spacing for tutorial controls
		javax.swing.JFrame frame = this.application.getFrame().getAwtComponent();
		javax.swing.JMenuBar menu = frame.getJMenuBar();
		int menuSpacer = 0;
		int frameSpacer = 0;
		if( isVisible ) {
			menuSpacer = 48;
			if( menu != null ) {
				frameSpacer = 0;
			} else {
				frameSpacer = menuSpacer;
			}
		}
		if( menu != null ) {
			menu.setBorder( javax.swing.BorderFactory.createEmptyBorder( menuSpacer, 0, 0, 0 ) );
		}
		( (javax.swing.JComponent)frame.getContentPane() ).setBorder( javax.swing.BorderFactory.createEmptyBorder( frameSpacer, 0, 0, 0 ) );

		this.stencil.setStencilShowing( isVisible );
	}
}
