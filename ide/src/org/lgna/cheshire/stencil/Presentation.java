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
public abstract class Presentation extends org.lgna.cheshire.Presentation {
	/*package-private*/static edu.cmu.cs.dennisc.croquet.Group IMPLEMENTATION_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "e582737d-b56b-4105-93d2-581853e193e2" ), "IMPLEMENTATION_GROUP" );
	/*package-private*/static java.awt.Color CONTROL_COLOR = new java.awt.Color( 230, 230, 255 );

	private static Presentation instance;
	public static Presentation getInstance() {
		return instance;
	}
	
	public static javax.swing.JLayeredPane getLayeredPane( boolean isOptimizedForBugRepro ) {
		edu.cmu.cs.dennisc.croquet.Application application = edu.cmu.cs.dennisc.croquet.Application.getSingleton();
		javax.swing.JFrame frame = application.getFrame().getAwtComponent();
		javax.swing.JLayeredPane layeredPane = frame.getLayeredPane();
		if( isOptimizedForBugRepro ) {
			frame.getJMenuBar().setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 300, 0, 0 ) );
			((javax.swing.JComponent)frame.getContentPane()).setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 300, 0, 0 ) );
		} else {
			final int PAD = 4;
			frame.getJMenuBar().setBorder( javax.swing.BorderFactory.createEmptyBorder( PAD + 32, PAD, 0, PAD ) );
			((javax.swing.JComponent)frame.getContentPane()).setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, PAD, PAD, PAD ) );
		}
		return layeredPane;
	}

	private final edu.cmu.cs.dennisc.croquet.Operation< ? > nextOperation = new NextStepOperation( this );
	private final edu.cmu.cs.dennisc.croquet.Operation< ? > prevOperation = new PrevStepOperation( this );
	private final edu.cmu.cs.dennisc.croquet.BooleanState isInterceptingEvents = new PresentationBooleanState( java.util.UUID.fromString( "c3a009d6-976e-439e-8f99-3c8ff8a0324a" ), true, "intercept events" );
	private final edu.cmu.cs.dennisc.croquet.BooleanState isPaintingStencil = new PresentationBooleanState( java.util.UUID.fromString( "b1c1b125-cfe3-485f-9453-1e57e5b02cb1" ), true, "paint stencil" );
	private final edu.cmu.cs.dennisc.croquet.BooleanState isPlayingSounds = new PresentationBooleanState( java.util.UUID.fromString( "4d8ac630-0679-415a-882f-780c7cb014ef" ), true, "play sounds" );

	@Override
	protected org.lgna.cheshire.Chapter createChapter(org.lgna.croquet.steps.Transaction transaction) {
		return new org.lgna.cheshire.TransactionChapter( transaction );
	}
	
	//	public edu.cmu.cs.dennisc.croquet.Operation< ? > getNextOperation() {
	//		return this.nextOperation;
	//	}
	//	public edu.cmu.cs.dennisc.croquet.Operation< ? > getPrevOperation() {
	//		return this.prevOperation;
	//	}

	class Stencil extends org.lgna.stencil.Stencil {
		private boolean isOptimizedForBugRepro;
		private edu.cmu.cs.dennisc.croquet.CardPanel cardPanel = new edu.cmu.cs.dennisc.croquet.CardPanel();

		//		public static TutorialStencil createInstance( edu.cmu.cs.dennisc.croquet.Group[] groups ) {
		//			return new TutorialStencil( edu.cmu.cs.dennisc.cheshire.MenuPolicy.ABOVE_STENCIL_WITHOUT_FEEDBACK, TransactionAccessPolicy.ALLOW_ACCESS_UP_TO_AND_INCLUDING_FURTHEST_COMPLETED_TRANSACTION, DefaultScrollingRequiredRenderer.INSTANCE, false, groups, edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext() ); 
		//		}

		public Stencil( org.lgna.stencil.MenuPolicy menuPolicy, org.lgna.stencil.ScrollingRequiredRenderer scrollingRequiredRenderer, boolean isOptimizedForBugRepro ) {
			super( menuPolicy, scrollingRequiredRenderer, getLayeredPane( isOptimizedForBugRepro ) );
			this.isOptimizedForBugRepro = isOptimizedForBugRepro;
			if( this.isOptimizedForBugRepro ) {
				edu.cmu.cs.dennisc.croquet.LineAxisPanel prevNextPanel = new edu.cmu.cs.dennisc.croquet.LineAxisPanel();
				prevNextPanel.addComponent( Presentation.this.prevOperation.createButton() );
				prevNextPanel.addComponent( Presentation.this.nextOperation.createButton() );

				edu.cmu.cs.dennisc.croquet.BorderPanel controlPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
				controlPanel.addComponent( prevNextPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.PAGE_START );
				controlPanel.addComponent( new BookList( bookComboBoxModel ), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );

				this.internalAddComponent( controlPanel, java.awt.BorderLayout.LINE_START );

			} else {
				edu.cmu.cs.dennisc.croquet.BorderPanel controlsPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
				edu.cmu.cs.dennisc.croquet.FlowPanel controlPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel( edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.CENTER, 2, 0 );
				controlPanel.addComponent( Presentation.this.prevOperation.createButton() );
				controlPanel.addComponent( new BookComboBox( Presentation.this.bookComboBoxModel, this.isAbovePopupMenus() == false ) );
				controlPanel.addComponent( Presentation.this.nextOperation.createButton() );
				controlsPanel.addComponent( controlPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
				Presentation.this.isPaintingStencil.setTextForTrueAndTextForFalse( "", "WARNING: stencil is disabled.  Click here to turn re-enable." );

				edu.cmu.cs.dennisc.croquet.CheckBox isPlayingSoundsCheckBox = Presentation.this.isPlayingSounds.createCheckBox();
				isPlayingSoundsCheckBox.getAwtComponent().setOpaque( false );
				edu.cmu.cs.dennisc.croquet.CheckBox isInterceptingEventsCheckBox = Presentation.this.isInterceptingEvents.createCheckBox();
				isInterceptingEventsCheckBox.getAwtComponent().setOpaque( false );
				edu.cmu.cs.dennisc.croquet.CheckBox isPaintingStencilCheckBox = Presentation.this.isPaintingStencil.createCheckBox();
				isPaintingStencilCheckBox.getAwtComponent().setOpaque( false );

				edu.cmu.cs.dennisc.croquet.FlowPanel westPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel( edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.TRAILING, 2, 0 );
				westPanel.addComponent( isPlayingSoundsCheckBox );
				//controlsPanel.addComponent(westPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_START);

				edu.cmu.cs.dennisc.croquet.FlowPanel eastPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel( edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.TRAILING, 2, 0 );
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

				this.internalAddComponent( controlsPanel, java.awt.BorderLayout.PAGE_START );
			}
			this.internalAddComponent( this.cardPanel, java.awt.BorderLayout.CENTER );
		}

		@Override
		protected org.lgna.stencil.Page getCurrentPage() {
			return ChapterPage.getInstance( Presentation.this.getBook().getSelectedChapter() );
		}
		@Override
		protected boolean isPaintingStencilEnabled() {
			return Presentation.this.isPaintingStencil.getValue();
		}

		private java.awt.event.KeyListener keyListener = new java.awt.event.KeyListener() {
			public void keyPressed( java.awt.event.KeyEvent e ) {
				int keyCode = e.getKeyCode();
				switch( keyCode ) {
				case java.awt.event.KeyEvent.VK_SPACE:
				case java.awt.event.KeyEvent.VK_RIGHT:
				case java.awt.event.KeyEvent.VK_DOWN:
					nextOperation.fire( e );
					break;
				case java.awt.event.KeyEvent.VK_BACK_SPACE:
				case java.awt.event.KeyEvent.VK_LEFT:
				case java.awt.event.KeyEvent.VK_UP:
					prevOperation.fire( e );
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
			Presentation.this.startListening();
			Presentation.this.handleChapterChanged( Presentation.this.getBook().getSelectedChapter() );
			this.addKeyListener( this.keyListener );
			this.addMouseListener( this.mouseListener );
		}

		@Override
		protected void handleUndisplayable() {
			this.removeMouseListener( this.mouseListener );
			this.removeKeyListener( this.keyListener );
			Presentation.this.stopListening();
			super.handleUndisplayable();
		}
		
		@Override
		public edu.cmu.cs.dennisc.croquet.Operation< ? > getNextOperation() {
			return Presentation.this.nextOperation;
		}
	}

	private final BookComboBoxModel bookComboBoxModel;
	private final Stencil stencil;

	private boolean isIgnoring = false;
	public Presentation( 
			edu.cmu.cs.dennisc.croquet.UserInformation userInformation, 
			org.lgna.cheshire.ChapterAccessPolicy transactionAccessPolicy, 
			org.lgna.croquet.steps.TransactionHistory originalTransactionHistory,
			org.lgna.cheshire.Filterer filterer,
			edu.cmu.cs.dennisc.croquet.Group[] groupsTrackedForRandomAccess, 
			org.lgna.stencil.MenuPolicy menuPolicy, 
			org.lgna.stencil.ScrollingRequiredRenderer scrollingRequiredRenderer, 
			boolean isOptimizedForBugRepro 
	) {
		super( userInformation, transactionAccessPolicy, originalTransactionHistory, filterer, groupsTrackedForRandomAccess );
		assert instance == null;
		instance = this;
		this.bookComboBoxModel = new BookComboBoxModel( this.getBook() );
		this.stencil = new Stencil( menuPolicy, scrollingRequiredRenderer, isOptimizedForBugRepro );
		this.isInterceptingEvents.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
			public void changing( boolean nextValue ) {
			}
			public void changed( boolean nextValue ) {
				Presentation.this.stencil.setEventInterceptEnabled( nextValue );
			}
		} );

		this.isPaintingStencil.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
			public void changing( boolean nextValue ) {
			}
			public void changed( boolean nextValue ) {
				Presentation.this.stencil.revalidateAndRepaint();
				Presentation.this.isInterceptingEvents.setValue( nextValue );
				Presentation.this.isInterceptingEvents.setEnabled( nextValue );
			}
		} );

		this.isPlayingSounds.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
			public void changing( boolean nextValue ) {
			}
			public void changed( boolean nextValue ) {
				org.lgna.cheshire.SoundCache.setEnabled( nextValue );
			}
		} );
	}
	/*package-private*/ edu.cmu.cs.dennisc.croquet.Operation< ? > getNextOperation() {
		return this.nextOperation;
	}
	@Override
	protected void handleTransactionCanceled( org.lgna.croquet.steps.Transaction transaction ) {
		Presentation.getInstance().restoreHistoryIndicesDueToCancel();
		org.lgna.cheshire.Chapter chapter = this.getBook().getSelectedChapter();
		if( chapter != null ) {
			ChapterPage chapterPage = ChapterPage.getInstance( chapter );
			chapterPage.reset();
		}
	}
	@Override
	protected void handleEvent( org.lgna.cheshire.events.Event event ) {
		if( isIgnoring ) {
			//pass
		} else {
			org.lgna.cheshire.Book book = getBook();
			org.lgna.cheshire.Chapter chapter = book.getSelectedChapter();
			ChapterPage chapterPage = ChapterPage.getInstance( chapter );
			chapterPage.adjustIfNecessary( event );
			if( chapterPage.isWhatWeveBeenWaitingFor( event ) ) {
				nextOperation.setEnabled( true );
				if( chapterPage.isAutoAdvanceDesired() ) {
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							isIgnoring = true;
							try {
								nextOperation.fire();
							} finally {
								isIgnoring = false;
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
		java.awt.Cursor cursor = java.awt.Cursor.getDefaultCursor();
		if( chapter != null ) {
			ChapterPage chapterPage = ChapterPage.getInstance( chapter );
			if( chapterPage.isStencilRenderingDesired() ) {
				cursor = java.awt.dnd.DragSource.DefaultMoveNoDrop;
			}
			chapterPage.reset();
			java.util.UUID transactionId = chapter.getId();
			edu.cmu.cs.dennisc.croquet.CardPanel.Key key = this.stencil.cardPanel.getKey( transactionId );
			if( key != null ) {
				// pass
			} else {
				key = this.stencil.cardPanel.createKey( chapterPage.getCard(), transactionId );
				this.stencil.cardPanel.addComponent( key );
			}
			this.stencil.cardPanel.show( key );
			this.stencil.revalidateAndRepaint();

			int selectedIndex = this.getBook().getSelectedIndex();

			//				boolean isAutoAdvanceDesired = false;
			boolean isWaiting = chapterPage.getChapter().isAlreadyInTheDesiredState() == false;
			if( this.stencil.isOptimizedForBugRepro ) {
				//pass
			} else {
				this.nextOperation.setEnabled( 0 <= selectedIndex && selectedIndex < this.getBook().getChapterCount() - 1 && isWaiting == false );
			}
			this.prevOperation.setEnabled( 1 <= selectedIndex );

			this.stencil.requestFocus();
			this.stencil.revalidateAndRepaint();
		}
		this.stencil.setCursor( cursor );
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
	public void setVisible( boolean isVisible ) {
		if( isVisible ) {
			this.stencil.addToLayeredPane();
		} else {
			this.stencil.removeFromLayeredPane();
		}
	}
}
