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
package org.lgna.cheshire.stencil;

/**
 * @author Dennis Cosgrove
 */
public class StencilsPresentation extends org.lgna.cheshire.Presentation {
	/*package-private*/static org.lgna.croquet.Group IMPLEMENTATION_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "e582737d-b56b-4105-93d2-581853e193e2" ), "IMPLEMENTATION_GROUP" );
	/*package-private*/static java.awt.Color CONTROL_COLOR = new java.awt.Color( 230, 230, 255 );

//	private static Presentation instance;
//	public static Presentation getInstance() {
//		return instance;
//	}
	
	public static javax.swing.JLayeredPane getLayeredPane( org.lgna.croquet.components.AbstractWindow< ? > window ) {
		org.lgna.croquet.Application application = org.lgna.croquet.Application.getActiveInstance();
		javax.swing.JFrame frame = application.getFrame().getAwtComponent();
		javax.swing.JLayeredPane layeredPane = frame.getLayeredPane();
		final int PAD = 4;
		javax.swing.JMenuBar jMenuBar = frame.getJMenuBar();
		int y;
		if( jMenuBar != null ) {
			jMenuBar.setBorder( javax.swing.BorderFactory.createEmptyBorder( PAD + 32, PAD, 0, PAD ) );
			y = 0;
		} else {
			y = PAD + 32;
		}
		((javax.swing.JComponent)frame.getContentPane()).setBorder( javax.swing.BorderFactory.createEmptyBorder( y, PAD, PAD, PAD ) );
		return layeredPane;
	}

	private final org.lgna.croquet.Operation prevOperation = new PrevStepOperation( this );
	private final org.lgna.croquet.BooleanState isInterceptingEvents = new PresentationBooleanState( java.util.UUID.fromString( "c3a009d6-976e-439e-8f99-3c8ff8a0324a" ), true, "intercept events" );
	private final org.lgna.croquet.BooleanState isPaintingStencil = new PresentationBooleanState( java.util.UUID.fromString( "b1c1b125-cfe3-485f-9453-1e57e5b02cb1" ), true, "paint stencil" );
	private final org.lgna.croquet.BooleanState isPlayingSounds = new PresentationBooleanState( java.util.UUID.fromString( "4d8ac630-0679-415a-882f-780c7cb014ef" ), true, "play sounds" );

	@Override
	protected org.lgna.cheshire.Chapter createChapter(org.lgna.croquet.history.Transaction transaction) {
		return new org.lgna.cheshire.TransactionChapter( transaction );
	}
	
	//	public edu.cmu.cs.dennisc.croquet.Operation< ? > getNextOperation() {
	//		return this.nextOperation;
	//	}
	//	public edu.cmu.cs.dennisc.croquet.Operation< ? > getPrevOperation() {
	//		return this.prevOperation;
	//	}

	class Stencil extends org.lgna.stencil.Stencil {
		private org.lgna.croquet.components.CardPanel cardPanel = new org.lgna.croquet.components.CardPanel();
		public Stencil( org.lgna.croquet.components.AbstractWindow< ? > window, org.lgna.stencil.ScrollingRequiredRenderer scrollingRequiredRenderer, org.lgna.stencil.MenuPolicy menuPolicy ) {
			super( getLayeredPane( window ), menuPolicy, scrollingRequiredRenderer );
			org.lgna.croquet.components.BorderPanel controlsPanel = new org.lgna.croquet.components.BorderPanel();
			org.lgna.croquet.components.FlowPanel controlPanel = new org.lgna.croquet.components.FlowPanel( org.lgna.croquet.components.FlowPanel.Alignment.CENTER, 2, 0 );
			controlPanel.addComponent( StencilsPresentation.this.prevOperation.createButton() );
			controlPanel.addComponent( new BookComboBox( StencilsPresentation.this.bookComboBoxModel, menuPolicy.isAboveStencil() ) );
			controlPanel.addComponent( NextStepOperation.getInstance().createButton() );
			controlsPanel.addComponent( controlPanel, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
			StencilsPresentation.this.isPaintingStencil.setTextForTrueAndTextForFalse( "", "WARNING: stencil is disabled.  Click here to turn re-enable." );

			org.lgna.croquet.components.CheckBox isPlayingSoundsCheckBox = StencilsPresentation.this.isPlayingSounds.createCheckBox();
			isPlayingSoundsCheckBox.getAwtComponent().setOpaque( false );
			org.lgna.croquet.components.CheckBox isInterceptingEventsCheckBox = StencilsPresentation.this.isInterceptingEvents.createCheckBox();
			isInterceptingEventsCheckBox.getAwtComponent().setOpaque( false );
			org.lgna.croquet.components.CheckBox isPaintingStencilCheckBox = StencilsPresentation.this.isPaintingStencil.createCheckBox();
			isPaintingStencilCheckBox.getAwtComponent().setOpaque( false );

			org.lgna.croquet.components.FlowPanel westPanel = new org.lgna.croquet.components.FlowPanel( org.lgna.croquet.components.FlowPanel.Alignment.TRAILING, 2, 0 );
			westPanel.addComponent( isPlayingSoundsCheckBox );
			//controlsPanel.addComponent(westPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_START);

			org.lgna.croquet.components.FlowPanel eastPanel = new org.lgna.croquet.components.FlowPanel( org.lgna.croquet.components.FlowPanel.Alignment.TRAILING, 2, 0 );
			//eastPanel.addComponent( isInterceptingEventsCheckBox );
			eastPanel.addComponent( isPaintingStencilCheckBox );
			//controlsPanel.addComponent(eastPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_END);
			controlsPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 0, 4 ) );

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
		}

		@Override
		protected org.lgna.stencil.Page getCurrentPage() {
			return ChapterPage.getInstance( StencilsPresentation.this.getBook().getSelectedChapter() );
		}
		@Override
		protected boolean isPaintingStencilEnabled() {
			return StencilsPresentation.this.isPaintingStencil.getValue();
		}

		private java.awt.event.KeyListener keyListener = new java.awt.event.KeyListener() {
			public void keyPressed( java.awt.event.KeyEvent e ) {
				int keyCode = e.getKeyCode();
				switch( keyCode ) {
				case java.awt.event.KeyEvent.VK_SPACE:
				case java.awt.event.KeyEvent.VK_RIGHT:
				case java.awt.event.KeyEvent.VK_DOWN:
					NextStepOperation.getInstance().fire( new org.lgna.croquet.triggers.SimulatedTrigger() );
					break;
				case java.awt.event.KeyEvent.VK_BACK_SPACE:
				case java.awt.event.KeyEvent.VK_LEFT:
				case java.awt.event.KeyEvent.VK_UP:
					prevOperation.fire( new org.lgna.croquet.triggers.SimulatedTrigger() );
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

		@Override
		protected void handleDisplayable() {
			super.handleDisplayable();
			StencilsPresentation.this.startListening();
			StencilsPresentation.this.handleChapterChanged( StencilsPresentation.this.getBook().getSelectedChapter() );
			this.addKeyListener( this.keyListener );
			this.addMouseListener( this.mouseListener );
		}

		@Override
		protected void handleUndisplayable() {
			this.removeMouseListener( this.mouseListener );
			this.removeKeyListener( this.keyListener );
			StencilsPresentation.this.stopListening();
			super.handleUndisplayable();
		}
		
		@Override
		public org.lgna.croquet.Operation getNextOperation() {
			return NextStepOperation.getInstance();
		}
	}

	private final BookComboBoxModel bookComboBoxModel;
	private final Stencil stencil;

	private boolean isIgnoringEvents = false;
	public StencilsPresentation( 
			org.lgna.croquet.UserInformation userInformation, 
			org.lgna.cheshire.ChapterAccessPolicy transactionAccessPolicy, 
			org.lgna.croquet.history.TransactionHistory originalTransactionHistory,
			org.lgna.croquet.migration.MigrationManager migrationManager,
			org.lgna.cheshire.Filterer filterer,
			org.lgna.cheshire.Recoverer recoverer,
			org.lgna.croquet.Group[] groupsTrackedForRandomAccess, 
			org.lgna.stencil.ScrollingRequiredRenderer scrollingRequiredRenderer, 
			org.lgna.stencil.MenuPolicy menuPolicy
	) {
		super( userInformation, transactionAccessPolicy, originalTransactionHistory, migrationManager, filterer, recoverer, groupsTrackedForRandomAccess );
		this.bookComboBoxModel = new BookComboBoxModel( this.getBook() );
		
		org.lgna.croquet.Application application = org.lgna.croquet.Application.getActiveInstance();
		this.stencil = new Stencil( application.getFrame(), scrollingRequiredRenderer, menuPolicy );
		this.isInterceptingEvents.addAndInvokeValueListener( new org.lgna.croquet.State.ValueListener< Boolean >() {
			public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				StencilsPresentation.this.stencil.setEventInterceptEnabled( nextValue );
			}
		} );

		this.isPaintingStencil.addAndInvokeValueListener( new org.lgna.croquet.State.ValueListener< Boolean >() {
			public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				StencilsPresentation.this.stencil.revalidateAndRepaint();
				StencilsPresentation.this.isInterceptingEvents.setValueTransactionlessly( nextValue );
				StencilsPresentation.this.isInterceptingEvents.setEnabled( nextValue );
			}
		} );

		this.isPlayingSounds.addAndInvokeValueListener( new org.lgna.croquet.State.ValueListener< Boolean >() {
			public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				org.lgna.cheshire.SoundCache.setEnabled( nextValue );
			}
		} );
	}
	@Override
	protected void handleTransactionCanceled( org.lgna.croquet.history.Transaction transaction ) {
		this.restoreHistoryIndicesDueToCancel();
		org.lgna.cheshire.Chapter chapter = this.getBook().getSelectedChapter();
		if( chapter != null ) {
			ChapterPage chapterPage = ChapterPage.getInstance( chapter );
			chapterPage.reset();
		}
	}
	@Override
	protected void handleEvent( org.lgna.croquet.history.event.Event<?> event ) {
		if( this.isIgnoringEvents ) {
			//pass
		} else {
			org.lgna.cheshire.Book book = getBook();
			org.lgna.cheshire.Chapter chapter = book.getSelectedChapter();
			ChapterPage chapterPage = ChapterPage.getInstance( chapter );
			chapterPage.adjustIfNecessary( event );
			if( chapterPage.isWhatWeveBeenWaitingFor( event ) ) {
				NextStepOperation.getInstance().setEnabled( true );
				if( chapterPage.isAutoAdvanceDesired() ) {
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							StencilsPresentation.this.isIgnoringEvents = true;
							try {
								NextStepOperation.getInstance().fire();
							} finally {
								StencilsPresentation.this.isIgnoringEvents = false;
							}
						}
					} );
				}
			}
		}
	}
	@Override
	protected void handleChapterChanged( org.lgna.cheshire.Chapter chapter ) {
		super.handleChapterChanged( chapter );
		if( chapter != null ) {
			ChapterPage chapterPage = ChapterPage.getInstance( chapter );
			chapterPage.refreshNotes();
			Iterable< org.lgna.croquet.Context > contexts = chapter.getAllContexts();
			for( org.lgna.croquet.Context context : contexts ) {
				if( context.isGoodToGo() ) {
					//pass
				} else {
					org.lgna.croquet.history.Transaction recoveryTransaction = context.createRecoveryTransaction();
					this.insertRecoveryTransactionChapter( recoveryTransaction );
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

				NextStepOperation.getInstance().setEnabled( 0 <= selectedIndex && selectedIndex < this.getBook().getChapterCount() - 1 && isWaiting == false );
				
				this.prevOperation.setEnabled( 1 <= selectedIndex );

				this.stencil.requestFocus();
				this.stencil.revalidateAndRepaint();
				this.stencil.setCursor( cursor );
			} else {
				org.lgna.croquet.history.Transaction transaction = ((org.lgna.cheshire.TransactionChapter)chapter).getTransaction();
				
				org.lgna.croquet.history.CompletionStep< ? > completionStep = transaction.getCompletionStep();
				org.lgna.croquet.CompletionModel completionModel = completionStep.getModel();
				
				Iterable< ? extends org.lgna.croquet.PrepModel > prepModels = completionModel.getPotentialRootPrepModels();
				for( org.lgna.croquet.PrepModel prepModel : prepModels ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( prepModel );
				}
				
				edu.cmu.cs.dennisc.java.util.logging.Logger.todo();
				if( false ) {
					org.lgna.croquet.history.PrepStep< ? >[] prepSteps = transaction.getPrepStepsAsArray();
					transaction.removeAllPrepSteps();
					chapterPage.refreshNotes();
					if( chapterPage.isGoodToGo() ) {
						this.handleChapterChanged( chapter );
					} else {
						java.util.List< org.lgna.croquet.MenuItemPrepModel > menuItemPrepModels = this.huntForInMenus( transaction.getCompletionStep().getModel() );
						if( menuItemPrepModels != null ) {
							org.lgna.croquet.history.TransactionManager.simulatedMenuTransaction( transaction, menuItemPrepModels );
							chapterPage.refreshNotes();
							if( chapterPage.isGoodToGo() ) {
								this.handleChapterChanged( chapter );
							}
						} else {
							transaction.setPrepSteps( prepSteps );
							chapterPage.refreshNotes();
							org.lgna.croquet.history.Transaction tabSelectionRecoveryTransaction = this.createTabSelectionRecoveryTransactionIfAppropriate( transaction );
							if( tabSelectionRecoveryTransaction != null ) {
								this.insertRecoveryTransactionChapter( tabSelectionRecoveryTransaction );
							} else {
								org.lgna.croquet.history.Transaction applicationRecoveryTransaction = this.getRecoverer().createTransactionToGetCloserToTheRightStateWhenNoViewControllerCanBeFound( transaction );
								if( applicationRecoveryTransaction != null ) {
									this.insertRecoveryTransactionChapter( applicationRecoveryTransaction );
								} else {
									org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "unable to recover" );
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void insertRecoveryTransactionChapter( org.lgna.croquet.history.Transaction recoveryTransaction ) {
		org.lgna.cheshire.Chapter recoveryChapter = new org.lgna.cheshire.TransactionChapter( recoveryTransaction );
		this.getBook().addChapter( this.getBook().getSelectedIndex(), recoveryChapter );
		this.handleChapterChanged( recoveryChapter );
		this.stencil.revalidateAndRepaint();
	}
	
//	@Override
//	protected edu.cmu.cs.dennisc.croquet.Step< ? > getCurrentStep() {
//		return this.transactionsModel.getSelectedStep();
//	}
//	public void addStep( edu.cmu.cs.dennisc.croquet.Step< ? > transaction ) {
//		this.transactionsModel.addStep( transaction );
//		transaction.setTutorialStencil( this );
//	}
//	public void setSelectedIndex( int index ) {
//		if( index < 0 ) {
//			index += this.transactionsModel.getTransactionCount();
//		}
//		this.transactionsModel.setSelectedIndex( index );
//	}
	@Override
	public void setVisible( boolean isVisible ) {
		if( isVisible ) {
			this.stencil.addToLayeredPane();
		} else {
			this.stencil.removeFromLayeredPane();
		}
	}
}
