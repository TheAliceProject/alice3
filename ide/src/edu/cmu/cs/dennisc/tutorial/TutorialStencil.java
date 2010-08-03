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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ class TutorialStencil extends Stencil {
	@Deprecated
	/*package-private*/ static boolean isResultOfNextOperation = false;
	/*package-private*/ static java.awt.Color CONTROL_COLOR = new java.awt.Color(255, 255, 191);
	/*package-private*/ static edu.cmu.cs.dennisc.croquet.Group TUTORIAL_GROUP = new edu.cmu.cs.dennisc.croquet.Group( java.util.UUID.fromString( "7bfa86e3-234e-4bd1-9177-d4acac0b12d9" ), "TUTORIAL_GROUP" );
	private static edu.cmu.cs.dennisc.croquet.Group TUTORIAL_COMPLETION_GROUP = new edu.cmu.cs.dennisc.croquet.Group( java.util.UUID.fromString( "ea5df77d-d74d-4364-9bf5-2df1b2ede0a4" ), "TUTORIAL_COMPLETION_GROUP" );

	private edu.cmu.cs.dennisc.croquet.ModelContext.ChildrenObserver childrenObserver = new edu.cmu.cs.dennisc.croquet.ModelContext.ChildrenObserver() {
		public void addingChild(edu.cmu.cs.dennisc.croquet.HistoryNode child) {
		}
		public void addedChild(edu.cmu.cs.dennisc.croquet.HistoryNode child) {
			Step step = (Step)stepsComboBoxModel.getSelectedItem();
			if (step instanceof WaitingStep<?>) {
				WaitingStep<?> waitingStep = (WaitingStep<?>) step;
				if( waitingStep.isWhatWeveBeenWaitingFor( child ) ) {
					nextStepOperation.setEnabled( true );
					if( step.isAutoAdvanceDesired() ) {
						javax.swing.SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								nextStepOperation.fire();
							}
						} );
					}
				}
			}
		}
	};
	
//	private edu.cmu.cs.dennisc.croquet.ListSelectionState<Step> stepSelectionState = new edu.cmu.cs.dennisc.croquet.ListSelectionState<Step>( TUTORIAL_GROUP, java.util.UUID.fromString( "ad05a285-8b5e-416f-98aa-1c4e6910fa5d" ), new edu.cmu.cs.dennisc.croquet.Codec<Step>() {
//		public edu.cmu.cs.dennisc.tutorial.Step decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
//			throw new RuntimeException( "todo" );
//		}
//		public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, edu.cmu.cs.dennisc.tutorial.Step t) {
//			throw new RuntimeException( "todo" );
//		}
//	} );
	
	private static final boolean IS_FORWARD_ENABLED = true;
	private StepsComboBoxModel stepsComboBoxModel = new StepsComboBoxModel( IS_FORWARD_ENABLED );
	
	private PreviousStepOperation previousStepOperation = new PreviousStepOperation( this.stepsComboBoxModel );
	private NextStepOperation nextStepOperation = new NextStepOperation( this.stepsComboBoxModel );
	//private ExitOperation exitOperation = new ExitOperation();

	private edu.cmu.cs.dennisc.croquet.BooleanState isInterceptingEvents = new edu.cmu.cs.dennisc.croquet.BooleanState( TUTORIAL_GROUP, java.util.UUID.fromString( "c3a009d6-976e-439e-8f99-3c8ff8a0324a" ), true, "intercept events" );
	private edu.cmu.cs.dennisc.croquet.BooleanState isPaintingStencil = new edu.cmu.cs.dennisc.croquet.BooleanState( TUTORIAL_GROUP, java.util.UUID.fromString( "b1c1b125-cfe3-485f-9453-1e57e5b02cb1" ), true, "paint stencil" );
	private edu.cmu.cs.dennisc.croquet.BooleanState isPlayingSounds = new edu.cmu.cs.dennisc.croquet.BooleanState( TUTORIAL_GROUP, java.util.UUID.fromString( "4d8ac630-0679-415a-882f-780c7cb014ef" ), true, "play sounds" );

	private class StepsComboBox extends edu.cmu.cs.dennisc.croquet.JComponent<javax.swing.JComboBox> {
		@Override
		protected javax.swing.JComboBox createAwtComponent() {
			javax.swing.JComboBox rv = new javax.swing.JComboBox(stepsComboBoxModel);
//			{
//				@Override
//				public java.awt.Dimension getPreferredSize() {
//					return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMaximumWidth( super.getPreferredSize(), 32 );
//				}
//			};
			
			//todo: find a better way
			//warning monumentally brittle code below
			rv.addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
				public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
					javax.swing.JComboBox box = (javax.swing.JComboBox)e.getSource();
					javax.accessibility.Accessible accessible = box.getUI().getAccessibleChild( box, 0 );
					if( accessible instanceof javax.swing.JPopupMenu ) {
						javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)accessible;
						java.awt.Component component = jPopupMenu.getComponent( 0 );
						if( component instanceof javax.swing.JScrollPane ) {
							javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane)component;
							java.awt.Dimension size = scrollPane.getPreferredSize();

							javax.swing.JViewport viewport = scrollPane.getViewport();
							java.awt.Component view = viewport.getView();
							java.awt.Dimension viewportSize = view.getPreferredSize();
							size.width = viewportSize.width;
							scrollPane.setPreferredSize( size );
							scrollPane.setMaximumSize( size );
						}
					}
				}
				public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
				}
				public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
				}
			} );
			
			StepCellRenderer stepCellRenderer = new StepCellRenderer( TutorialStencil.this.stepsComboBoxModel, CONTROL_COLOR );
			rv.setRenderer(stepCellRenderer);
			rv.setBackground(CONTROL_COLOR);
			return rv;
		}
	};
	
	private edu.cmu.cs.dennisc.history.HistoryManager[] historyManagers;

	private edu.cmu.cs.dennisc.croquet.BorderPanel controlsPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
	private edu.cmu.cs.dennisc.croquet.CardPanel cardPanel = new edu.cmu.cs.dennisc.croquet.CardPanel();
	private StepsComboBoxModel.SelectionObserver selectionObserver = new StepsComboBoxModel.SelectionObserver() {
		public void selectionChanging( StepsComboBoxModel source, int fromIndex, int toIndex ) {
		}
		public void selectionChanged( StepsComboBoxModel source, int fromIndex, int toIndex ) {
			TutorialStencil.this.handleStepChanged( source.getElementAt( toIndex ) );
		}
	};
	
	public static TutorialStencil createInstance( edu.cmu.cs.dennisc.croquet.Group[] groups ) {
		edu.cmu.cs.dennisc.croquet.Application application = edu.cmu.cs.dennisc.croquet.Application.getSingleton();
		javax.swing.JFrame frame = application.getFrame().getAwtComponent();
		javax.swing.JLayeredPane layeredPane = frame.getLayeredPane();
		final int PAD = 4;
		frame.getJMenuBar().setBorder( javax.swing.BorderFactory.createEmptyBorder(PAD+32,PAD,0,PAD));
		((javax.swing.JComponent)frame.getContentPane()).setBorder( javax.swing.BorderFactory.createEmptyBorder(0,PAD,PAD,PAD));
		return new TutorialStencil( layeredPane, groups, application.getRootContext() ); 
	}
	private TutorialStencil( javax.swing.JLayeredPane layeredPane, edu.cmu.cs.dennisc.croquet.Group[] groups, edu.cmu.cs.dennisc.croquet.ModelContext< ? > rootContext ) {
		super( layeredPane );
		
		rootContext.addChildrenObserver( this.childrenObserver );

		final int N = groups.length;
		this.historyManagers = new edu.cmu.cs.dennisc.history.HistoryManager[ N+1 ];
		for( int i=0; i<N; i++ ) {
			this.historyManagers[ i ] = edu.cmu.cs.dennisc.history.HistoryManager.getInstance( groups[ i ] );
		}
		this.historyManagers[ N ] = edu.cmu.cs.dennisc.history.HistoryManager.getInstance( TUTORIAL_COMPLETION_GROUP );

		this.isInterceptingEvents.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
			public void changing( boolean nextValue ) {
			}
			public void changed( boolean nextValue ) {
				TutorialStencil.this.setEventInterceptEnabled( nextValue );
			}
		} );
		
		this.isPaintingStencil.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
			public void changing( boolean nextValue ) {
			}
			public void changed( boolean nextValue ) {
				TutorialStencil.this.revalidateAndRepaint();
				isInterceptingEvents.setValue( nextValue );
				isInterceptingEvents.setEnabled( nextValue );
			}
		} );

		this.isPlayingSounds.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
			public void changing( boolean nextValue ) {
			}
			public void changed( boolean nextValue ) {
				SoundCache.setEnabled( nextValue );
			}
		} );

		edu.cmu.cs.dennisc.croquet.FlowPanel controlPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel(edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.CENTER, 2, 0);
		controlPanel.addComponent(this.previousStepOperation.createButton());
		controlPanel.addComponent(new StepsComboBox());
		controlPanel.addComponent(this.nextStepOperation.createButton());

		this.controlsPanel.addComponent(controlPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER);
		
		this.isPaintingStencil.setTextForTrueAndTextForFalse( "", "WARNING: stencil is disabled.  Click here to turn re-enable." );
		
		edu.cmu.cs.dennisc.croquet.CheckBox isPlayingSoundsCheckBox = this.isPlayingSounds.createCheckBox();
		isPlayingSoundsCheckBox.getAwtComponent().setOpaque( false );
		edu.cmu.cs.dennisc.croquet.CheckBox isInterceptingEventsCheckBox = this.isInterceptingEvents.createCheckBox();
		isInterceptingEventsCheckBox.getAwtComponent().setOpaque( false );
		edu.cmu.cs.dennisc.croquet.CheckBox isPaintingStencilCheckBox = this.isPaintingStencil.createCheckBox();
		isPaintingStencilCheckBox.getAwtComponent().setOpaque( false );
				
		edu.cmu.cs.dennisc.croquet.FlowPanel westPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel(edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.TRAILING, 2, 0);
		westPanel.addComponent( isPlayingSoundsCheckBox );
		this.controlsPanel.addComponent(westPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_START);

		edu.cmu.cs.dennisc.croquet.FlowPanel eastPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel(edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.TRAILING, 2, 0);
		//eastPanel.addComponent( isInterceptingEventsCheckBox );
		eastPanel.addComponent( isPaintingStencilCheckBox );
		this.controlsPanel.addComponent(eastPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_END);
		this.controlsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 0, 4));

		this.internalAddComponent(this.controlsPanel, java.awt.BorderLayout.NORTH);
		this.internalAddComponent(this.cardPanel, java.awt.BorderLayout.CENTER);
	}

	@Override
	protected boolean isPaintingStencilEnabled() {
		return this.isPaintingStencil.getValue();
	}
	private Step getStep( int index ) {
		return (Step)this.stepsComboBoxModel.getElementAt( index );
	}
	private void preserveHistoryIndices( int stepIndex ) {
		Step step = this.getStep( stepIndex );
		final int N = historyManagers.length;
		int[] indices = new int[ N ];
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "preserveHistoryIndices", stepIndex );
		for( int i=0; i<N; i++ ) {
			indices[ i ] = historyManagers[ i ].getInsertionIndex();
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( historyManagers[ i ], indices[ i ] );
		}
		step.setHistoryIndices( indices );
	}
	private void restoreHistoryIndices( int stepIndex ) {
		Step step = this.getStep( stepIndex );
		final int N = historyManagers.length;
		int[] indices = step.getHistoryIndices();
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "restoreHistoryIndices", stepIndex );
		for( int i=0; i<N; i++ ) {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( historyManagers[ i ], indices[ i ] );
			historyManagers[ i ].setInsertionIndex( indices[ i ] );
		}
	}

	/*package-private*/ static void complete( final edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		if( edit != null ) {
			 edu.cmu.cs.dennisc.croquet.ActionOperation bogusCompletionOperation = new edu.cmu.cs.dennisc.croquet.ActionOperation( TUTORIAL_COMPLETION_GROUP, java.util.UUID.fromString( "d4b1cb3b-f642-4c90-be92-e27d616f6922" ) ) {
				@Override
				protected void perform( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: context edit type" );
					context.commitAndInvokeDo( edit );
				}
			};
			bogusCompletionOperation.fire();
//			edu.cmu.cs.dennisc.croquet.ModelContext< ? > parentContext = edu.cmu.cs.dennisc.croquet.Application.getSingleton().getCurrentContext();
//			edu.cmu.cs.dennisc.croquet.ActionOperationContext rv = new edu.cmu.cs.dennisc.croquet.ActionOperationContext( parentContext, bogusCompletionOperation, null, null );
//			rv.commitAndInvokeDo( edit );
		}
	}

	private int prevSelectedIndex = -1;
	private void completeOrUndoIfNecessary() {
		SoundCache.pushIgnoreStartRequests();
		try {
			int nextSelectedIndex = this.stepsComboBoxModel.getSelectedIndex();
			int undoIndex = Math.max( nextSelectedIndex, 0 );
			if( undoIndex < this.prevSelectedIndex ) {
				this.restoreHistoryIndices( undoIndex );
			} else {
				int index0 = Math.max( this.prevSelectedIndex, 0 );
				int i=index0;
				while( i<=nextSelectedIndex ) {
					if( i != this.prevSelectedIndex ) {
						preserveHistoryIndices( i );
					}
					if( i == nextSelectedIndex ) {
						//pass
					} else {
						if( i==this.prevSelectedIndex && isResultOfNextOperation ) {
							//pass
						} else {
							Step iStep = this.getStep( i );
							iStep.complete();
						}
					}
					i++;
				}
			}
			this.prevSelectedIndex = nextSelectedIndex;
		} finally {
			SoundCache.popIgnoreStartRequests();
		}
	}
	private void handleStepChanged(Step step) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleStepChanged" );
		this.completeOrUndoIfNecessary();
		if( step != null ) {
			step.reset();
			java.util.UUID stepId = step.getId();
			edu.cmu.cs.dennisc.croquet.CardPanel.Key key = this.cardPanel.getKey(stepId);
			if (key != null) {
				// pass
			} else {
				key = this.cardPanel.createKey(step.getCard(), stepId);
				this.cardPanel.addComponent(key);
			}
			this.cardPanel.show(key);
			this.revalidateAndRepaint();

			int selectedIndex = stepsComboBoxModel.getSelectedIndex();

			boolean isWaiting;
			if (step instanceof WaitingStep<?>) {
				WaitingStep<?> waitingStep = (WaitingStep<?>) step;
				isWaiting = waitingStep.isAlreadyInTheDesiredState() == false;
			} else {
				isWaiting = false;
			}
			
			this.nextStepOperation.setEnabled(0 <= selectedIndex && selectedIndex < stepsComboBoxModel.getSize() - 1 && isWaiting==false );
			this.previousStepOperation.setEnabled(1 <= selectedIndex);
//			javax.swing.SwingUtilities.invokeLater( new Runnable() {
//				public void run() {
//					TutorialStencil.this.controlsPanel.repaint();
//				}
//			} );
			this.requestFocus();
			this.revalidateAndRepaint();
		}
	}
	private java.awt.event.KeyListener keyListener = new java.awt.event.KeyListener() {
		public void keyPressed(java.awt.event.KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch( keyCode ) {
			case java.awt.event.KeyEvent.VK_SPACE:
			case java.awt.event.KeyEvent.VK_RIGHT:
			case java.awt.event.KeyEvent.VK_DOWN:
				nextStepOperation.fire( e );
				break;
			case java.awt.event.KeyEvent.VK_BACK_SPACE:
			case java.awt.event.KeyEvent.VK_LEFT:
			case java.awt.event.KeyEvent.VK_UP:
				previousStepOperation.fire( e );
				break;
			}
		}
		public void keyReleased(java.awt.event.KeyEvent e) {
		}
		public void keyTyped(java.awt.event.KeyEvent e) {
		}
	};
	
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo(parent);
		this.stepsComboBoxModel.addSelectionObserver(this.selectionObserver);
		this.handleStepChanged((Step) stepsComboBoxModel.getSelectedItem());
		this.addKeyListener( this.keyListener );
	}

	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		this.removeKeyListener( this.keyListener );
		this.stepsComboBoxModel.removeSelectionObserver(this.selectionObserver);
		super.handleRemovedFrom(parent);
	}
	
	@Override
	protected Step getCurrentStep() {
		return (Step)stepsComboBoxModel.getSelectedItem();
	}
	/*package-private*/ void addStep( Step step ) {
		this.stepsComboBoxModel.addStep( step );
		step.setTutorialStencil( this );
	}
	/*package-private*/ void setSelectedIndex( int index ) {
		this.stepsComboBoxModel.setSelectedIndex( index );
	}
		
	/*package-private*/ edu.cmu.cs.dennisc.croquet.ActionOperation getNextOperation() {
		return this.nextStepOperation;
	}
}
