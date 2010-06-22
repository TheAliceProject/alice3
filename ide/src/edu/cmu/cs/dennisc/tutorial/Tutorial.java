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
	private Step addStep( Step rv ) {
		this.stencil.addStep( rv );
		return rv;
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
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: rename to RootComponentResolver" );
		}
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> getResolved(edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, E item) {
			return itemSelectionState.getRootComponentFor( item );
		}
	}

	public Step addMessageStep( String title, String text ) {
		Step step = new MessageStep( title, text );
		return this.addStep( step );
	}
	public Step addSpotlightStep( String title, String text, Resolver< ? extends edu.cmu.cs.dennisc.croquet.TrackableShape > trackableShapeResolver ) {
		Step step = new SpotlightStep( title, text, trackableShapeResolver, Feature.ConnectionPreference.EAST_WEST );
		return this.addStep( step );
	}
	public Step addActionStep( String title, String text, Resolver< ? extends edu.cmu.cs.dennisc.croquet.Operation< ?,? > > operationResolver, CompletorValidator completorValidator ) {
		Step step = new OperationStep( title, text, operationResolver, completorValidator, completorValidator );
		return this.addStep( step );
	}
	public Step addBooleanStateStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.BooleanState> booleanStateResolver, boolean desiredValue ) {
		Step step = new BooleanStateStep( title, text, booleanStateResolver, desiredValue );
		return this.addStep( step );
	}

	public Step addDialogOpenAndCloseStep( String title, String openText, String closeText, Resolver<edu.cmu.cs.dennisc.croquet.DialogOperation> dialogOperationResolver ) {
		Step step = new DialogOpenAndCloseStep( title, openText, closeText, dialogOperationResolver );
		return this.addStep( step );
	}
	public Step addInputDialogOpenAndCommitStep( String title, String openText, String commitText, Resolver<edu.cmu.cs.dennisc.croquet.InputDialogOperation> inputDialogOperationResolver, CompletorValidator completorValidator ) {
		Step step = new InputDialogOpenAndCommitStep( title, openText, commitText, inputDialogOperationResolver, completorValidator, completorValidator );
		return this.addStep( step );
	}
	
	public <E> Step addListSelectionStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		Step step = new ListSelectionStateStep<E>( title, text, itemSelectionStateResolver, itemResolver, Feature.ConnectionPreference.EAST_WEST );
		return this.addStep( step );
	}
	public <E> Step addSelectTabStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		Step step = new ListSelectionStateStep<E>( title, text, itemSelectionStateResolver, itemResolver, Feature.ConnectionPreference.NORTH_SOUTH );
		return this.addStep( step );
	}
	public <E> Step addSpotlightTabTitleStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		Step step = new SpotlightStep( title, text, new ItemSelectionStateItemResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.NORTH_SOUTH );
		return this.addStep( step );
	}
	public <E> Step addSpotlightTabMainComponentStep( String title, String text, edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		Step step = new SpotlightStep( title, text, new MainComponentResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.EAST_WEST );
		return this.addStep( step );
	}
	public <E> Step addSpotlightTabScrollPaneStep( String title, String text, edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		Step step = new SpotlightStep( title, text, new ScrollPaneResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.EAST_WEST );
		return this.addStep( step );
	}

	private Step addDragAndDropStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String popupMenuText, String inputDialogText, CompletorValidator completorValidator ) {
		Step step = new DragAndDropStep( title, text, dragResolver, dropText, dropShapeResolver, popupMenuText, inputDialogText, completorValidator, completorValidator );
		return this.addStep( step );
	}
	public Step addDragAndDropStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, CompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, dropShapeResolver, null, null, completorValidator );
	}
	public Step addDragAndDropToPopupMenuStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String popupMenuText, CompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, dropShapeResolver, popupMenuText, null, completorValidator );
	}
	public Step addDragAndDropToInputDialogStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String inputDialogText, CompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, dropShapeResolver, null, inputDialogText, completorValidator );
	}
	public Step addDragAndDropToPopupMenuToInputDialogStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String popupMenuText, String inputDialogText, CompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, dropShapeResolver, popupMenuText, inputDialogText, completorValidator );
	}

	
	

	@Deprecated
	public Step EPIC_HACK_addDeclareProcedureDialogOpenAndCommitStep( String title, String openText, String commitText, CompletorValidator completorValidator, final org.alice.ide.operations.ast.DeclareProcedureOperation.EPIC_HACK_Validator validator ) {
		Step step = new InputDialogOpenAndCommitStep( title, openText, commitText, ((org.alice.ide.tutorial.IdeTutorial)this).createDeclareProcedureOperationResolver(), completorValidator, completorValidator ) {
			@Override
			protected void setActiveNote(int activeIndex) {
				super.setActiveNote(activeIndex);
				if( activeIndex == 1 ) {
					org.alice.ide.operations.ast.DeclareProcedureOperation declareProcedureOperation = (org.alice.ide.operations.ast.DeclareProcedureOperation)this.getModel();
					declareProcedureOperation.setValidator( validator );
				}
			}
		};
		return this.addStep( step );
	}
	@Deprecated
	public Step EPIC_HACK_addDeclareMethodParameterDialogOpenAndCommitStep( String title, String openText, String commitText, CompletorValidator completorValidator, final org.alice.ide.operations.ast.DeclareMethodParameterOperation.EPIC_HACK_Validator validator ) {
		Step step = new InputDialogOpenAndCommitStep( title, openText, commitText, ((org.alice.ide.tutorial.IdeTutorial)this).createDeclareMethodParameterOperationResolver(), completorValidator, completorValidator ) {
			@Override
			protected void setActiveNote(int activeIndex) {
				super.setActiveNote(activeIndex);
				if( activeIndex == 1 ) {
					org.alice.ide.operations.ast.DeclareMethodParameterOperation declareParameterOperation = (org.alice.ide.operations.ast.DeclareMethodParameterOperation)this.getModel();
					declareParameterOperation.setValidator( validator );
				}
			}
		};
		return this.addStep( step );
	}

	
	@Deprecated
	public Step EPIC_HACK_addForEachInArrayLoopDragAndDropToPopupMenuToInputDialogStep( String title, String text, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String popupMenuText, String inputDialogText, CompletorValidator completorValidator, final org.alice.ide.cascade.customfillin.CustomInputDialogOperation.EPIC_HACK_Validator validator ) {
		Step step = new DragAndDropStep( title, text, ((org.alice.ide.tutorial.IdeTutorial)this).createForEachInArrayLoopTemplateResolver(), dropText, dropShapeResolver, popupMenuText, inputDialogText, completorValidator, completorValidator ) {
			@Override
			public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryTreeNode<?> child ) {
				boolean rv = super.isWhatWeveBeenWaitingFor( child );
				if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext ) {
					edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext context = (edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext)child;
					edu.cmu.cs.dennisc.croquet.Model<?> model = context.getModel();
					if (model instanceof org.alice.ide.cascade.customfillin.CustomInputDialogOperation ) {
						org.alice.ide.cascade.customfillin.CustomInputDialogOperation customInputDialogOperation = (org.alice.ide.cascade.customfillin.CustomInputDialogOperation) model;
						customInputDialogOperation.setValidator( validator );
					}
				}
				return rv;
			}
		};
		return this.addStep( step );
	}


	public void setVisible( boolean isVisible ) {
		if( isVisible ) {
			this.stencil.addToLayeredPane();
		} else {
			this.stencil.removeFromLayeredPane();
		}
	}
}
