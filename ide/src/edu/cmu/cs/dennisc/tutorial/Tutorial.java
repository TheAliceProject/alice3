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
public class Tutorial {
	private TutorialStencil stencil;
	public Tutorial( edu.cmu.cs.dennisc.croquet.Group[] groups ) {
		this.stencil = TutorialStencil.createInstance( groups );
	}
	public void setSelectedIndex( int index ) {
		this.stencil.setSelectedIndex( index );
	}
	private void addStep( Step step ) {
		this.stencil.addStep( step );
	}
	public void addMessageStep( String title, String text ) {
		Step step = new MessageStep( title, text );
		this.addStep( step );
	}
	public void addSpotlightStep( String title, String text, Resolver< ? extends edu.cmu.cs.dennisc.croquet.TrackableShape > trackableShapeResolver ) {
		Step step = new SpotlightStep( title, text, trackableShapeResolver, Feature.ConnectionPreference.EAST_WEST );
		this.addStep( step );
	}

	/*package-private*/ static class ItemSelectionStateItemResolver<E> implements Resolver< edu.cmu.cs.dennisc.croquet.TrackableShape > {
		private Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver;
		private Resolver< E > itemResolver;
		public ItemSelectionStateItemResolver( Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver, Resolver< E > itemResolver ) {
			this.itemSelectionStateResolver = itemSelectionStateResolver;
			this.itemResolver = itemResolver;
		}
		public edu.cmu.cs.dennisc.croquet.TrackableShape getResolved() {
			edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState = this.itemSelectionStateResolver.getResolved();
			if( itemSelectionState != null ) {
				E item = itemResolver.getResolved();
				return itemSelectionState.getTrackableShapeFor( item );
			} else {
				return null;
			}
		}
	}

	private static abstract class ItemSelectionStateTrackableShapeResolver<E,T> implements Resolver< T > {
		private Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver;
		private Resolver< E > itemResolver;
		public ItemSelectionStateTrackableShapeResolver(Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver, Resolver< E > itemResolver ) {
			this.itemSelectionStateResolver = itemSelectionStateResolver;
			this.itemResolver = itemResolver;
		}
		protected abstract T getResolved( edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, E item );
		public final T getResolved() {
			edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState = itemSelectionStateResolver.getResolved();
			if( itemSelectionState != null ) {
				E item = this.itemResolver.getResolved();
				return getResolved( itemSelectionState, item );
			} else {
				return null;
			}
		}
	}
	
	private static class MainComponentResolver<E> extends ItemSelectionStateTrackableShapeResolver< E, edu.cmu.cs.dennisc.croquet.JComponent< ? > > {
		public MainComponentResolver(Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver, Resolver< E > itemResolver ) {
			super( itemSelectionStateResolver, itemResolver );
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> getResolved(edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, E item) {
			return itemSelectionState.getMainComponentFor( item );
		}
	}
	private static class ScrollPaneResolver<E> extends ItemSelectionStateTrackableShapeResolver< E, edu.cmu.cs.dennisc.croquet.JComponent< ? > > {
		public ScrollPaneResolver(Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver, Resolver< E > itemResolver ) {
			super( itemSelectionStateResolver, itemResolver );
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> getResolved(edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, E item) {
			return itemSelectionState.getScrollPaneFor( item );
		}
	}
	public void addActionStep( String title, String text, Resolver< ? extends edu.cmu.cs.dennisc.croquet.Operation< ?,? > > operationResolver, CompletorValidator completorValidator ) {
		Step step = new OperationStep( title, text, operationResolver, completorValidator, completorValidator );
		this.addStep( step );
	}
	public void addBooleanStateStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.BooleanState> booleanStateResolver, boolean desiredValue ) {
		Step step = new BooleanStateStep( title, text, booleanStateResolver, desiredValue );
		this.addStep( step );
	}
	public void addDialogOpenStep( String title, String text, Resolver<? extends edu.cmu.cs.dennisc.croquet.AbstractDialogOperation> dialogOperationResolver ) {
		Step step = new DialogOpenStep( title, text, dialogOperationResolver );
		this.addStep( step );
	}
	public void addDialogCloseStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.DialogOperation> dialogOperationResolver ) {
		Step step = new DialogCloseStep( title, text, dialogOperationResolver );
		this.addStep( step );
	}
	public void addInputDialogCommitStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.InputDialogOperation> inputDialogOperationResolver, CompletorValidator completorValidator ) {
		Step step = new InputDialogCommitStep( title, text, inputDialogOperationResolver, completorValidator );
		this.addStep( step );
	}
	public <E> void addItemSelectionStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		Step step = new ItemSelectionStateStep<E>( title, text, itemSelectionStateResolver, itemResolver, Feature.ConnectionPreference.EAST_WEST );
		this.addStep( step );
	}
	public <E> void addSelectTabStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		Step step = new ItemSelectionStateStep<E>( title, text, itemSelectionStateResolver, itemResolver, Feature.ConnectionPreference.NORTH_SOUTH );
		this.addStep( step );
	}
	public <E> void addSpotlightTabTitleStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		Step step = new SpotlightStep( title, text, new ItemSelectionStateItemResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.NORTH_SOUTH );
		this.addStep( step );
	}
	public <E> void addSpotlightTabMainComponentStep( String title, String text, edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		Step step = new SpotlightStep( title, text, new MainComponentResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.EAST_WEST );
		this.addStep( step );
	}
	public <E> void addSpotlightTabScrollPaneStep( String title, String text, edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		Step step = new SpotlightStep( title, text, new ScrollPaneResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.EAST_WEST );
		this.addStep( step );
	}

	public void addDragAndDropStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String cascadeText, CompletorValidator completorValidator ) {
		Step step = new DragAndDropStep( title, text, dragResolver, dropText, dropShapeResolver, cascadeText, completorValidator, completorValidator );
		this.addStep( step );
	}
	public void addDragAndDropStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, CompletorValidator completorValidator ) {
		this.addDragAndDropStep(title, text, dragResolver, dropText, dropShapeResolver, null, completorValidator );
	}

	

	@Deprecated
	public void EPIC_HACK_addDeclareProcedureOperationStep( String title, String text, final org.alice.ide.operations.ast.DeclareProcedureOperation.EPIC_HACK_Validator validator ) {
		Step step = new DialogOpenStep<edu.cmu.cs.dennisc.croquet.InputDialogOperation>( title, text, ((org.alice.ide.tutorial.IdeTutorial)this).createDeclareProcedureOperationResolver() ) {
			@Override
			public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryTreeNode<?> child ) {
				if( super.isWhatWeveBeenWaitingFor( child ) ) {
					org.alice.ide.operations.ast.DeclareProcedureOperation declareProcedureOperation = (org.alice.ide.operations.ast.DeclareProcedureOperation)this.getModel();
					declareProcedureOperation.setValidator( validator );
					return true;
				} else {
					return false;
				}
			}
		};
		this.addStep( step );
	}

	public void setVisible( boolean isVisible ) {
		if( isVisible ) {
			this.stencil.addToLayeredPane();
		} else {
			this.stencil.removeFromLayeredPane();
		}
	}
}

//public class Tutorial {
//	@Deprecated
//	/*package-private*/ static boolean isResultOfNextOperation = false;
//
//	/*package-private*/ static java.awt.Color CONTROL_COLOR = new java.awt.Color(255, 255, 191);
//	/*package-private*/ static edu.cmu.cs.dennisc.croquet.Group TUTORIAL_GROUP = new edu.cmu.cs.dennisc.croquet.Group( java.util.UUID.fromString( "7bfa86e3-234e-4bd1-9177-d4acac0b12d9" ), "TUTORIAL_GROUP" );
//	private static edu.cmu.cs.dennisc.croquet.Group TUTORIAL_COMPLETION_GROUP = new edu.cmu.cs.dennisc.croquet.Group( java.util.UUID.fromString( "ea5df77d-d74d-4364-9bf5-2df1b2ede0a4" ), "TUTORIAL_COMPLETION_GROUP" );
//	private edu.cmu.cs.dennisc.croquet.ModelContext.ChildrenObserver childrenObserver = new edu.cmu.cs.dennisc.croquet.ModelContext.ChildrenObserver() {
//		public void addingChild(edu.cmu.cs.dennisc.croquet.HistoryTreeNode child) {
//		}
//		public void addedChild(edu.cmu.cs.dennisc.croquet.HistoryTreeNode child) {
//			Step step = (Step)stepsComboBoxModel.getSelectedItem();
//			if (step instanceof WaitingStep<?>) {
//				WaitingStep<?> waitingStep = (WaitingStep<?>) step;
//				if( waitingStep.isWhatWeveBeenWaitingFor( child ) ) {
//					nextStepOperation.setEnabled( true );
//					if( step.isAutoAdvanceDesired() ) {
//						javax.swing.SwingUtilities.invokeLater(new Runnable() {
//							public void run() {
//								nextStepOperation.fire();
//							}
//						} );
//					}
//				}
//			}
//		}
//	};
//	
//	private StepsComboBoxModel stepsComboBoxModel = new StepsComboBoxModel();
//	private PreviousStepOperation previousStepOperation = new PreviousStepOperation( this.stepsComboBoxModel );
//	private NextStepOperation nextStepOperation = new NextStepOperation( this.stepsComboBoxModel );
//	private ExitOperation exitOperation = new ExitOperation();
//
//	private static boolean isEventInterceptEnabledByDefault = edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyFalse( "edu.cmu.cs.dennisc.tutorial.Stencil.isEventInterceptEnabled" ) == false;
//	private edu.cmu.cs.dennisc.croquet.BooleanState isInterceptingEvents = new edu.cmu.cs.dennisc.croquet.BooleanState( Tutorial.TUTORIAL_GROUP, java.util.UUID.fromString( "c3a009d6-976e-439e-8f99-3c8ff8a0324a" ), isEventInterceptEnabledByDefault, "intercept events" );
//
//
//	private class StepsComboBox extends edu.cmu.cs.dennisc.croquet.JComponent<javax.swing.JComboBox> {
//		@Override
//		protected javax.swing.JComboBox createAwtComponent() {
//			javax.swing.JComboBox rv = new javax.swing.JComboBox(stepsComboBoxModel);
//			StepCellRenderer stepCellRenderer = new StepCellRenderer( Tutorial.this.stepsComboBoxModel, CONTROL_COLOR );
//			rv.setRenderer(stepCellRenderer);
//			rv.setBackground(CONTROL_COLOR);
//			return rv;
//		}
//	};
//
//	private class TutorialStencil extends Stencil {
//		private edu.cmu.cs.dennisc.croquet.BorderPanel controlsPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
//		private edu.cmu.cs.dennisc.croquet.CardPanel cardPanel = new edu.cmu.cs.dennisc.croquet.CardPanel();
//		private StepsComboBox comboBox = new StepsComboBox();
//		private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
//			public void itemStateChanged(java.awt.event.ItemEvent e) {
//				if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
//					TutorialStencil.this.handleStepChanged((Step) e.getItem());
//				} else {
//					// pass
//				}
//			}
//		};
//		public TutorialStencil( javax.swing.JLayeredPane layeredPane ) {
//			super( layeredPane );
//			isInterceptingEvents.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
//				public void changing( boolean nextValue ) {
//				}
//				public void changed( boolean nextValue ) {
//					TutorialStencil.this.setEventInterceptEnabled( nextValue );
//				}
//			} );
//			
//			edu.cmu.cs.dennisc.croquet.FlowPanel controlPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel(edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.CENTER, 2, 0);
//			controlPanel.addComponent(Tutorial.this.previousStepOperation.createButton());
//			controlPanel.addComponent(comboBox);
//			controlPanel.addComponent(Tutorial.this.nextStepOperation.createButton());
//
//			this.controlsPanel.addComponent(controlPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER);
//			
//			edu.cmu.cs.dennisc.croquet.FlowPanel eastPanel = new edu.cmu.cs.dennisc.croquet.FlowPanel(edu.cmu.cs.dennisc.croquet.FlowPanel.Alignment.TRAILING, 2, 0);
//			eastPanel.addComponent( Tutorial.this.isInterceptingEvents.createCheckBox() );
//			eastPanel.addComponent( Tutorial.this.exitOperation.createButton() );
//			this.controlsPanel.addComponent(eastPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST);
//			this.controlsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 0, 4));
//
//			this.internalAddComponent(this.controlsPanel, java.awt.BorderLayout.NORTH);
//			this.internalAddComponent(this.cardPanel, java.awt.BorderLayout.CENTER);
//		}
//		
//		private Step getStep( int index ) {
//			return (Step)Tutorial.this.stepsComboBoxModel.getElementAt( index );
//		}
//		private void preserveHistoryIndices( Step step ) {
//			final int N = historyManagers.length;
//			int[] indices = new int[ N ];
//			for( int i=0; i<N; i++ ) {
//				indices[ i ] = historyManagers[ i ].getInsertionIndex();
//			}
//			step.setHistoryIndices( indices );
//		}
//		private void restoreHistoryIndices( Step step ) {
//			final int N = historyManagers.length;
//			int[] indices = step.getHistoryIndices();
//			for( int i=0; i<N; i++ ) {
//				historyManagers[ i ].setInsertionIndex( indices[ i ] );
//			}
//		}
//		private int prevSelectedIndex = -1;
//		private void completeOrUndoIfNecessary() {
//			int nextSelectedIndex = this.comboBox.getAwtComponent().getSelectedIndex();
//			int undoIndex = Math.max( nextSelectedIndex, 0 );
//			if( undoIndex < this.prevSelectedIndex ) {
//				Step step = this.getStep( undoIndex );
//				this.restoreHistoryIndices( step );
//			} else {
//				edu.cmu.cs.dennisc.croquet.ModelContext< ? > context = edu.cmu.cs.dennisc.croquet.Application.getSingleton().getCurrentContext();
//				int index0 = Math.max( this.prevSelectedIndex, 0 );
//				int i=index0;
//				while( i<=nextSelectedIndex ) {
//					Step iStep = this.getStep( i );
//					preserveHistoryIndices( iStep );
//					if( i == nextSelectedIndex ) {
//						//pass
//					} else {
//						if( i==this.prevSelectedIndex && isResultOfNextOperation ) {
//							//pass
//						} else {
//							iStep.complete( context );
//						}
//					}
//					i++;
//				}
//			}
//			this.prevSelectedIndex = nextSelectedIndex;
//		}
//		private void handleStepChanged(Step step) {
//			this.completeOrUndoIfNecessary();
//			if( step != null ) {
//				step.reset();
//				java.util.UUID stepId = step.getId();
//				edu.cmu.cs.dennisc.croquet.CardPanel.Key key = this.cardPanel.getKey(stepId);
//				if (key != null) {
//					// pass
//				} else {
//					key = this.cardPanel.createKey(step.getCard(), stepId);
//					this.cardPanel.addComponent(key);
//				}
//				this.cardPanel.show(key);
//				this.revalidateAndRepaint();
//
//				int selectedIndex = stepsComboBoxModel.getSelectedIndex();
//
//				boolean isWaiting;
//				if (step instanceof WaitingStep<?>) {
//					WaitingStep<?> waitingStep = (WaitingStep<?>) step;
//					isWaiting = waitingStep.isAlreadyInTheDesiredState() == false;
//				} else {
//					isWaiting = false;
//				}
//				
//				Tutorial.this.nextStepOperation.setEnabled(0 <= selectedIndex && selectedIndex < stepsComboBoxModel.getSize() - 1 && isWaiting==false );
//				Tutorial.this.previousStepOperation.setEnabled(1 <= selectedIndex);
////				javax.swing.SwingUtilities.invokeLater( new Runnable() {
////					public void run() {
////						TutorialStencil.this.controlsPanel.repaint();
////					}
////				} );
//				this.requestFocus();
//				this.revalidateAndRepaint();
//			}
//		}
//		private java.awt.event.KeyListener keyListener = new java.awt.event.KeyListener() {
//			public void keyPressed(java.awt.event.KeyEvent e) {
//				int keyCode = e.getKeyCode();
//				switch( keyCode ) {
//				case java.awt.event.KeyEvent.VK_SPACE:
//				case java.awt.event.KeyEvent.VK_RIGHT:
//				case java.awt.event.KeyEvent.VK_DOWN:
//					nextStepOperation.fire( e );
//					break;
//				case java.awt.event.KeyEvent.VK_BACK_SPACE:
//				case java.awt.event.KeyEvent.VK_LEFT:
//				case java.awt.event.KeyEvent.VK_UP:
//					previousStepOperation.fire( e );
//					break;
//				}
//			}
//			public void keyReleased(java.awt.event.KeyEvent e) {
//			}
//			public void keyTyped(java.awt.event.KeyEvent e) {
//			}
//		};
//		
//		@Override
//		protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
//			super.handleAddedTo(parent);
//			this.comboBox.getAwtComponent().addItemListener(this.itemListener);
//			this.handleStepChanged((Step) stepsComboBoxModel.getSelectedItem());
//			this.addKeyListener( this.keyListener );
//		}
//
//		@Override
//		protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
//			this.removeKeyListener( this.keyListener );
//			this.comboBox.getAwtComponent().addItemListener(this.itemListener);
//			super.handleRemovedFrom(parent);
//		}
//		
//		@Override
//		protected Step getCurrentStep() {
//			return (Step)stepsComboBoxModel.getSelectedItem();
//		}
//	}
//	
//	private edu.cmu.cs.dennisc.history.HistoryManager[] historyManagers;
//	public Tutorial( edu.cmu.cs.dennisc.croquet.Group[] groups ) {
//		final int N = groups.length;
//		this.historyManagers = new edu.cmu.cs.dennisc.history.HistoryManager[ N+1 ];
//		for( int i=0; i<N; i++ ) {
//			this.historyManagers[ i ] = edu.cmu.cs.dennisc.history.HistoryManager.getInstance( groups[ i ] );
//		}
//		this.historyManagers[ N ] = edu.cmu.cs.dennisc.history.HistoryManager.getInstance( TUTORIAL_GROUP );
//	}
//	private void addStep( Step step ) {
//		this.stepsComboBoxModel.addStep( step );
//		step.setTutorial( this );
//	}
//	public void setSelectedIndex( int index ) {
//		this.stepsComboBoxModel.setSelectedIndex( index );
//	}
//	public void addMessageStep( String title, String text ) {
//		Step step = new MessageStep( title, text );
//		this.addStep( step );
//	}
//	public void addSpotlightStep( String title, String text, Resolver< ? extends edu.cmu.cs.dennisc.croquet.TrackableShape > trackableShapeResolver ) {
//		Step step = new SpotlightStep( title, text, trackableShapeResolver, Feature.ConnectionPreference.EAST_WEST );
//		this.addStep( step );
//	}
//
//	/*package-private*/ static class ItemSelectionStateItemResolver<E> implements Resolver< edu.cmu.cs.dennisc.croquet.TrackableShape > {
//		private Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver;
//		private Resolver< E > itemResolver;
//		public ItemSelectionStateItemResolver( Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver, Resolver< E > itemResolver ) {
//			this.itemSelectionStateResolver = itemSelectionStateResolver;
//			this.itemResolver = itemResolver;
//		}
//		public edu.cmu.cs.dennisc.croquet.TrackableShape getResolved() {
//			edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState = this.itemSelectionStateResolver.getResolved();
//			if( itemSelectionState != null ) {
//				E item = itemResolver.getResolved();
//				return itemSelectionState.getTrackableShapeFor( item );
//			} else {
//				return null;
//			}
//		}
//	}
//
//	private static abstract class ItemSelectionStateTrackableShapeResolver<E,T> implements Resolver< T > {
//		private Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver;
//		private Resolver< E > itemResolver;
//		public ItemSelectionStateTrackableShapeResolver(Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver, Resolver< E > itemResolver ) {
//			this.itemSelectionStateResolver = itemSelectionStateResolver;
//			this.itemResolver = itemResolver;
//		}
//		protected abstract T getResolved( edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, E item );
//		public final T getResolved() {
//			edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState = itemSelectionStateResolver.getResolved();
//			if( itemSelectionState != null ) {
//				E item = this.itemResolver.getResolved();
//				return getResolved( itemSelectionState, item );
//			} else {
//				return null;
//			}
//		}
//	}
//	
//	private static class MainComponentResolver<E> extends ItemSelectionStateTrackableShapeResolver< E, edu.cmu.cs.dennisc.croquet.JComponent< ? > > {
//		public MainComponentResolver(Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver, Resolver< E > itemResolver ) {
//			super( itemSelectionStateResolver, itemResolver );
//		}
//		@Override
//		protected edu.cmu.cs.dennisc.croquet.JComponent<?> getResolved(edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, E item) {
//			return itemSelectionState.getMainComponentFor( item );
//		}
//	}
//	private static class ScrollPaneResolver<E> extends ItemSelectionStateTrackableShapeResolver< E, edu.cmu.cs.dennisc.croquet.JComponent< ? > > {
//		public ScrollPaneResolver(Resolver< edu.cmu.cs.dennisc.croquet.ListSelectionState<E> > itemSelectionStateResolver, Resolver< E > itemResolver ) {
//			super( itemSelectionStateResolver, itemResolver );
//		}
//		@Override
//		protected edu.cmu.cs.dennisc.croquet.JComponent<?> getResolved(edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, E item) {
//			return itemSelectionState.getScrollPaneFor( item );
//		}
//	}
//	public void addActionStep( String title, String text, Resolver< ? extends edu.cmu.cs.dennisc.croquet.Operation< ?,? > > operationResolver, CompletorValidator completorValidator ) {
//		Step step = new OperationStep( title, text, operationResolver, completorValidator, completorValidator );
//		this.addStep( step );
//	}
//	public void addBooleanStateStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.BooleanState> booleanStateResolver, boolean desiredValue ) {
//		Step step = new BooleanStateStep( title, text, booleanStateResolver, desiredValue );
//		this.addStep( step );
//	}
//	public void addDialogOpenStep( String title, String text, Resolver<? extends edu.cmu.cs.dennisc.croquet.AbstractDialogOperation> dialogOperationResolver ) {
//		Step step = new DialogOpenStep( title, text, dialogOperationResolver );
//		this.addStep( step );
//	}
//	public void addDialogCloseStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.DialogOperation> dialogOperationResolver ) {
//		Step step = new DialogCloseStep( title, text, dialogOperationResolver );
//		this.addStep( step );
//	}
//	public void addInputDialogCommitStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.InputDialogOperation> inputDialogOperationResolver, CompletorValidator completorValidator ) {
//		Step step = new InputDialogCommitStep( title, text, inputDialogOperationResolver, completorValidator );
//		this.addStep( step );
//	}
//	public <E> void addItemSelectionStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
//		Step step = new ItemSelectionStateStep<E>( title, text, itemSelectionStateResolver, itemResolver, Feature.ConnectionPreference.EAST_WEST );
//		this.addStep( step );
//	}
//	public <E> void addSelectTabStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
//		Step step = new ItemSelectionStateStep<E>( title, text, itemSelectionStateResolver, itemResolver, Feature.ConnectionPreference.NORTH_SOUTH );
//		this.addStep( step );
//	}
//	public <E> void addSpotlightTabTitleStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
//		Step step = new SpotlightStep( title, text, new ItemSelectionStateItemResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.NORTH_SOUTH );
//		this.addStep( step );
//	}
//	public <E> void addSpotlightTabMainComponentStep( String title, String text, edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
//		Step step = new SpotlightStep( title, text, new MainComponentResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.EAST_WEST );
//		this.addStep( step );
//	}
//	public <E> void addSpotlightTabScrollPaneStep( String title, String text, edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
//		Step step = new SpotlightStep( title, text, new ScrollPaneResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.EAST_WEST );
//		this.addStep( step );
//	}
//
//	public void addDragAndDropStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String cascadeText, CompletorValidator completorValidator ) {
//		Step step = new DragAndDropStep( title, text, dragResolver, dropText, dropShapeResolver, cascadeText, completorValidator, completorValidator );
//		this.addStep( step );
//	}
//	public void addDragAndDropStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, CompletorValidator completorValidator ) {
//		this.addDragAndDropStep(title, text, dragResolver, dropText, dropShapeResolver, null, completorValidator );
//	}
//		
//	/*package-private*/ edu.cmu.cs.dennisc.croquet.ActionOperation getNextOperation() {
//		return this.nextStepOperation;
//	}
//	public void setVisible( boolean isVisible ) {
//		if( isVisible ) {
//			edu.cmu.cs.dennisc.croquet.Application application = edu.cmu.cs.dennisc.croquet.Application.getSingleton();
//			javax.swing.JFrame frame = application.getFrame().getAwtWindow();
//			javax.swing.JLayeredPane layeredPane = frame.getLayeredPane();
//			TutorialStencil stencil = new TutorialStencil( layeredPane );
//			stencil.addToLayeredPane();
//			application.getRootContext().addChildrenObserver( this.childrenObserver );
//
//			final int PAD = 4;
//			frame.getJMenuBar().setBorder( javax.swing.BorderFactory.createEmptyBorder(PAD+32,PAD,0,PAD));
//			((javax.swing.JComponent)frame.getContentPane()).setBorder( javax.swing.BorderFactory.createEmptyBorder(0,PAD,PAD,PAD));
//			
//			//stencil.getAwtComponent().repaint();
//		}
//	}
//}
