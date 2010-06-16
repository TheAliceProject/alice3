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
