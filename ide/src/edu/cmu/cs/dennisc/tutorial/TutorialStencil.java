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

import edu.cmu.cs.dennisc.croquet.Resolver;

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
		public void addingChild(edu.cmu.cs.dennisc.croquet.HistoryTreeNode child) {
		}
		public void addedChild(edu.cmu.cs.dennisc.croquet.HistoryTreeNode child) {
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
	
	private StepsComboBoxModel stepsComboBoxModel = new StepsComboBoxModel();
	private PreviousStepOperation previousStepOperation = new PreviousStepOperation( this.stepsComboBoxModel );
	private NextStepOperation nextStepOperation = new NextStepOperation( this.stepsComboBoxModel );
	private ExitOperation exitOperation = new ExitOperation();

	private static boolean isEventInterceptEnabledByDefault = edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyFalse( "edu.cmu.cs.dennisc.tutorial.Stencil.isEventInterceptEnabled" ) == false;
	private edu.cmu.cs.dennisc.croquet.BooleanState isInterceptingEvents = new edu.cmu.cs.dennisc.croquet.BooleanState( TUTORIAL_GROUP, java.util.UUID.fromString( "c3a009d6-976e-439e-8f99-3c8ff8a0324a" ), isEventInterceptEnabledByDefault, "intercept events" );


	private class StepsComboBox extends edu.cmu.cs.dennisc.croquet.JComponent<javax.swing.JComboBox> {
		@Override
		protected javax.swing.JComboBox createAwtComponent() {
			javax.swing.JComboBox rv = new javax.swing.JComboBox(stepsComboBoxModel);
			StepCellRenderer stepCellRenderer = new StepCellRenderer( TutorialStencil.this.stepsComboBoxModel, CONTROL_COLOR );
			rv.setRenderer(stepCellRenderer);
			rv.setBackground(CONTROL_COLOR);
			return rv;
		}
	};
	
	private edu.cmu.cs.dennisc.history.HistoryManager[] historyManagers;

	private edu.cmu.cs.dennisc.croquet.BorderPanel controlsPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
	private edu.cmu.cs.dennisc.croquet.CardPanel cardPanel = new edu.cmu.cs.dennisc.croquet.CardPanel();
	private StepsComboBox comboBox = new StepsComboBox();
	private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
				TutorialStencil.this.handleStepChanged((Step) e.getItem());
			} else {
				// pass
			}
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
		this.historyManagers[ N ] = edu.cmu.cs.dennisc.history.HistoryManager.getInstance( TUTORIAL_GROUP );

		isInterceptingEvents.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
			public void changing( boolean nextValue ) {
			}
			public void changed( boolean nextValue ) {
				TutorialStencil.this.setEventInterceptEnabled( nextValue );
			}
		} );
		
		edu.cmu.cs.dennisc.croquet.FlowPanel controlPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel(edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.CENTER, 2, 0);
		controlPanel.addComponent(this.previousStepOperation.createButton());
		controlPanel.addComponent(comboBox);
		controlPanel.addComponent(this.nextStepOperation.createButton());

		this.controlsPanel.addComponent(controlPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER);
		
		edu.cmu.cs.dennisc.croquet.FlowPanel eastPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel(edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.TRAILING, 2, 0);
		eastPanel.addComponent( this.isInterceptingEvents.createCheckBox() );
		eastPanel.addComponent( this.exitOperation.createButton() );
		this.controlsPanel.addComponent(eastPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST);
		this.controlsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 0, 4));

		this.internalAddComponent(this.controlsPanel, java.awt.BorderLayout.NORTH);
		this.internalAddComponent(this.cardPanel, java.awt.BorderLayout.CENTER);
	}

	private Step getStep( int index ) {
		return (Step)this.stepsComboBoxModel.getElementAt( index );
	}
	private void preserveHistoryIndices( Step step ) {
		final int N = historyManagers.length;
		int[] indices = new int[ N ];
		for( int i=0; i<N; i++ ) {
			indices[ i ] = historyManagers[ i ].getInsertionIndex();
		}
		step.setHistoryIndices( indices );
	}
	private void restoreHistoryIndices( Step step ) {
		final int N = historyManagers.length;
		int[] indices = step.getHistoryIndices();
		for( int i=0; i<N; i++ ) {
			historyManagers[ i ].setInsertionIndex( indices[ i ] );
		}
	}
	private int prevSelectedIndex = -1;
	private void completeOrUndoIfNecessary() {
		int nextSelectedIndex = this.comboBox.getAwtComponent().getSelectedIndex();
		int undoIndex = Math.max( nextSelectedIndex, 0 );
		if( undoIndex < this.prevSelectedIndex ) {
			Step step = this.getStep( undoIndex );
			this.restoreHistoryIndices( step );
		} else {
			edu.cmu.cs.dennisc.croquet.ModelContext< ? > context = edu.cmu.cs.dennisc.croquet.Application.getSingleton().getCurrentContext();
			int index0 = Math.max( this.prevSelectedIndex, 0 );
			int i=index0;
			while( i<=nextSelectedIndex ) {
				Step iStep = this.getStep( i );
				preserveHistoryIndices( iStep );
				if( i == nextSelectedIndex ) {
					//pass
				} else {
					if( i==this.prevSelectedIndex && isResultOfNextOperation ) {
						//pass
					} else {
						iStep.complete( context );
					}
				}
				i++;
			}
		}
		this.prevSelectedIndex = nextSelectedIndex;
	}
	private void handleStepChanged(Step step) {
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
		this.comboBox.getAwtComponent().addItemListener(this.itemListener);
		this.handleStepChanged((Step) stepsComboBoxModel.getSelectedItem());
		this.addKeyListener( this.keyListener );
	}

	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		this.removeKeyListener( this.keyListener );
		this.comboBox.getAwtComponent().addItemListener(this.itemListener);
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
//	public void setVisible( boolean isVisible ) {
//		if( isVisible ) {
//			stencil.addToLayeredPane();
//			
//			//stencil.getAwtComponent().repaint();
//		}
//	}
}
