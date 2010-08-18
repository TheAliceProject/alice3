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
	private static class SelfResolver<T> implements Resolver<T> {
		private T t;
		public SelfResolver( T t ) {
			this.t = t;
		}
		public T getResolved() {
			return this.t;
		}
	}
	
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
	public Step addMessageStep( String title, String text ) {
		return this.addStep( new MessageStep( title, text ) );
	}
	public Step addSpotlightStep( String title, String text, Resolver< ? extends edu.cmu.cs.dennisc.croquet.TrackableShape > trackableShapeResolver ) {
		return this.addStep( new SpotlightStep( title, text, trackableShapeResolver, Feature.ConnectionPreference.EAST_WEST ) );
	}
	public Step addSpotlightStepForModel( String title, String text, Resolver< ? extends edu.cmu.cs.dennisc.croquet.Model > modelResolver ) {
		return this.addSpotlightStep( title, text, new FirstComponentResolver(modelResolver) );
	}
	public Step addSpotlightStepForModel( String title, String text, edu.cmu.cs.dennisc.croquet.Model model ) {
		return this.addSpotlightStep( title, text, new FirstComponentResolver( new SelfResolver<edu.cmu.cs.dennisc.croquet.Model>(model)) );
	}
	public Step addActionStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.ActionOperation > actionOperationResolver, ActionOperationCompletorValidator completorValidator ) {
		return this.addStep( new ActionOperationStep( title, text, actionOperationResolver, completorValidator, completorValidator ) );
	}
	public Step addActionStep( String title, String text, edu.cmu.cs.dennisc.croquet.ActionOperation actionOperation, ActionOperationCompletorValidator completorValidator ) {
		return this.addStep( new ActionOperationStep( title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.ActionOperation>(actionOperation), completorValidator, completorValidator ) );
	}
	public Step addBooleanStateStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.BooleanState> booleanStateResolver, boolean desiredValue ) {
		return this.addStep( new BooleanStateStep( title, text, booleanStateResolver, desiredValue ) );
	}
	public Step addBooleanStateStep( String title, String text, edu.cmu.cs.dennisc.croquet.BooleanState booleanState, boolean desiredValue ) {
		return this.addStep( new BooleanStateStep( title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.BooleanState>( booleanState ), desiredValue ) );
	}

	public Step addDialogOpenAndCloseStep( String title, String openText, String closeText, Resolver<edu.cmu.cs.dennisc.croquet.DialogOperation> dialogOperationResolver ) {
		return this.addStep( new DialogOpenAndCloseStep( title, openText, closeText, dialogOperationResolver ) );
	}
	public Step addDialogOpenAndCloseStep( String title, String openText, String closeText, edu.cmu.cs.dennisc.croquet.DialogOperation dialogOperation ) {
		return this.addStep( new DialogOpenAndCloseStep( title, openText, closeText, new SelfResolver<edu.cmu.cs.dennisc.croquet.DialogOperation>( dialogOperation ) ) );
	}

	public Step addInputDialogOpenAndCommitStep( String title, String openText, String commitText, Resolver<edu.cmu.cs.dennisc.croquet.InputDialogOperation<?>> inputDialogOperationResolver, InputDialogOperationCompletorValidatorOkButtonDisabler completorValidatorOkButtonDisabler ) {
		return this.addStep( new InputDialogOpenAndCommitStep( title, openText, commitText, inputDialogOperationResolver, completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler ) );
	}
	public Step addInputDialogOpenAndCommitStep( String title, String openText, String commitText, edu.cmu.cs.dennisc.croquet.InputDialogOperation<?> inputDialogOperation, InputDialogOperationCompletorValidatorOkButtonDisabler completorValidatorOkButtonDisabler ) {
		return this.addStep( new InputDialogOpenAndCommitStep( title, openText, commitText, new SelfResolver<edu.cmu.cs.dennisc.croquet.InputDialogOperation<?>>( inputDialogOperation ), completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler ) );
	}
	
	public Step addPopupMenuStep( String title, String popupText, Resolver<edu.cmu.cs.dennisc.croquet.PopupMenuOperation> popupMenuResolver, String commitText, PopupMenuOperationCompletorValidator completorValidator ) {
		return this.addStep( new PopupMenuStep( title, popupText, popupMenuResolver, commitText, completorValidator, completorValidator ) );
	}
	public Step addPopupMenuStep( String title, String popupText, edu.cmu.cs.dennisc.croquet.PopupMenuOperation popupMenu, String commitText, PopupMenuOperationCompletorValidator completorValidator ) {
		return this.addStep( new PopupMenuStep( title, popupText, new SelfResolver<edu.cmu.cs.dennisc.croquet.PopupMenuOperation>( popupMenu ), commitText, completorValidator, completorValidator ) );
	}
	
	public <E> Step addListSelectionStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		return this.addStep( new ListSelectionStateStep<E>( title, text, itemSelectionStateResolver, itemResolver, Feature.ConnectionPreference.EAST_WEST ) );
	}
	public <E> Step addListSelectionStep( String title, String text, edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, Resolver<? extends E> itemResolver ) {
		return this.addStep( new ListSelectionStateStep<E>( title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>>( itemSelectionState ), itemResolver, Feature.ConnectionPreference.EAST_WEST ) );
	}
	
	public <E> Step addSelectTabStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		return this.addStep( new ListSelectionStateStep<E>( title, text, itemSelectionStateResolver, itemResolver, Feature.ConnectionPreference.NORTH_SOUTH ) );
	}
	public <E> Step addSelectTabStep( String title, String text, edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, Resolver<? extends E> itemResolver ) {
		return this.addStep( new ListSelectionStateStep<E>( title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>>( itemSelectionState ), itemResolver, Feature.ConnectionPreference.NORTH_SOUTH ) );
	}
	
	public <E> Step addSpotlightTabTitleStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		return this.addStep( new SpotlightStep( title, text, new ItemSelectionStateItemResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.NORTH_SOUTH ) );
	}
	public <E> Step addSpotlightTabTitleStep( String title, String text, edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, Resolver<? extends E> itemResolver ) {
		return this.addStep( new SpotlightStep( title, text, new ItemSelectionStateItemResolver( new SelfResolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>>( itemSelectionState ), itemResolver ), Feature.ConnectionPreference.NORTH_SOUTH ) );
	}

	public <E> Step addSpotlightTabMainComponentStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		return this.addStep( new SpotlightStep( title, text, new MainComponentResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.EAST_WEST ) );
	}
	public <E> Step addSpotlightTabMainComponentStep( String title, String text, edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, Resolver<? extends E> itemResolver ) {
		return this.addStep( new SpotlightStep( title, text, new MainComponentResolver( new SelfResolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>>( itemSelectionState ), itemResolver ), Feature.ConnectionPreference.EAST_WEST ) );
	}

	public <E> Step addSpotlightTabScrollPaneStep( String title, String text, Resolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>> itemSelectionStateResolver, Resolver<? extends E> itemResolver ) {
		return this.addStep( new SpotlightStep( title, text, new ScrollPaneResolver( itemSelectionStateResolver, itemResolver ), Feature.ConnectionPreference.EAST_WEST ) );
	}
	public <E> Step addSpotlightTabScrollPaneStep( String title, String text, edu.cmu.cs.dennisc.croquet.ListSelectionState<E> itemSelectionState, Resolver<? extends E> itemResolver ) {
		return this.addStep( new SpotlightStep( title, text, new ScrollPaneResolver( new SelfResolver<edu.cmu.cs.dennisc.croquet.ListSelectionState<E>>( itemSelectionState ), itemResolver ), Feature.ConnectionPreference.EAST_WEST ) );
	}

	
	
	private Step addDragAndDropStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String popupMenuText, String inputDialogText, DragAndDropOperationCompletorValidator completorValidator, edu.cmu.cs.dennisc.croquet.InputDialogOperation.ExternalOkButtonDisabler externalOkButtonDisabler ) {
		return this.addStep( new DragAndDropStep( title, text, dragResolver, dropText, dropShapeResolver, popupMenuText, inputDialogText, completorValidator, completorValidator, externalOkButtonDisabler ) );
	}

	public Step addDragAndDropStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, DragAndDropOperationCompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, dropShapeResolver, null, null, completorValidator, null );
	}
	public Step addDragAndDropToPopupMenuStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String popupMenuText, DragAndDropOperationCompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, dropShapeResolver, popupMenuText, null, completorValidator, null );
	}
	public Step addDragAndDropToInputDialogStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String inputDialogText, DragAndDropOperationCompletorValidatorOkButtonDisabler completorValidatorOkButtonDisabler ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, dropShapeResolver, null, inputDialogText, completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler );
	}
	public Step addDragAndDropToPopupMenuToInputDialogStep( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String popupMenuText, String inputDialogText, DragAndDropOperationCompletorValidatorOkButtonDisabler completorValidatorOkButtonDisabler ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, dropShapeResolver, popupMenuText, inputDialogText, completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler );
	}

	public Step addDragAndDropStep( String title, String text, edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragOperation, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, DragAndDropOperationCompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.DragAndDropOperation>( dragOperation ), dropText, dropShapeResolver, null, null, completorValidator, null );
	}
	public Step addDragAndDropToPopupMenuStep( String title, String text, edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragOperation, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String popupMenuText, DragAndDropOperationCompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.DragAndDropOperation>( dragOperation ), dropText, dropShapeResolver, popupMenuText, null, completorValidator, null );
	}
	public Step addDragAndDropToInputDialogStep( String title, String text, edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragOperation, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String inputDialogText, DragAndDropOperationCompletorValidatorOkButtonDisabler completorValidatorOkButtonDisabler ) {
		return this.addDragAndDropStep(title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.DragAndDropOperation>( dragOperation ), dropText, dropShapeResolver, null, inputDialogText, completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler );
	}
	public Step addDragAndDropToPopupMenuToInputDialogStep( String title, String text, edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragOperation, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String popupMenuText, String inputDialogText, DragAndDropOperationCompletorValidatorOkButtonDisabler completorValidatorOkButtonDisabler ) {
		return this.addDragAndDropStep(title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.DragAndDropOperation>( dragOperation ), dropText, dropShapeResolver, popupMenuText, inputDialogText, completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler );
	}

	public Step addDragAndDropStepForModel( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.Model> dropModelResolver, DragAndDropOperationCompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, new FirstComponentResolver(dropModelResolver), null, null, completorValidator, null );
	}
	public Step addDragAndDropToPopupMenuStepForModel( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.Model> dropModelResolver, String popupMenuText, DragAndDropOperationCompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, new FirstComponentResolver(dropModelResolver), popupMenuText, null, completorValidator, null );
	}
	public Step addDragAndDropToInputDialogStepForModel( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.Model> dropModelResolver, String inputDialogText, DragAndDropOperationCompletorValidatorOkButtonDisabler completorValidatorOkButtonDisabler ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, new FirstComponentResolver(dropModelResolver), null, inputDialogText, completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler );
	}
	public Step addDragAndDropToPopupMenuToInputDialogStepForModel( String title, String text, Resolver< edu.cmu.cs.dennisc.croquet.DragAndDropOperation > dragResolver, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.Model> dropModelResolver, String popupMenuText, String inputDialogText, DragAndDropOperationCompletorValidatorOkButtonDisabler completorValidatorOkButtonDisabler ) {
		return this.addDragAndDropStep(title, text, dragResolver, dropText, new FirstComponentResolver(dropModelResolver), popupMenuText, inputDialogText, completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler );
	}

	public Step addDragAndDropStepForModel( String title, String text, edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragOperation, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.Model> dropModelResolver, DragAndDropOperationCompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.DragAndDropOperation>( dragOperation ), dropText, new FirstComponentResolver(dropModelResolver), null, null, completorValidator, null );
	}
	public Step addDragAndDropToPopupMenuStepForModel( String title, String text, edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragOperation, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.Model> dropModelResolver, String popupMenuText, DragAndDropOperationCompletorValidator completorValidator ) {
		return this.addDragAndDropStep(title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.DragAndDropOperation>( dragOperation ), dropText, new FirstComponentResolver(dropModelResolver), popupMenuText, null, completorValidator, null );
	}
	public Step addDragAndDropToInputDialogStepForModel( String title, String text, edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragOperation, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.Model> dropModelResolver, String inputDialogText, DragAndDropOperationCompletorValidatorOkButtonDisabler completorValidatorOkButtonDisabler ) {
		return this.addDragAndDropStep(title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.DragAndDropOperation>( dragOperation ), dropText, new FirstComponentResolver(dropModelResolver), null, inputDialogText, completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler );
	}
	public Step addDragAndDropToPopupMenuToInputDialogStepForModel( String title, String text, edu.cmu.cs.dennisc.croquet.DragAndDropOperation dragOperation, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.Model> dropModelResolver, String popupMenuText, String inputDialogText, DragAndDropOperationCompletorValidatorOkButtonDisabler completorValidatorOkButtonDisabler ) {
		return this.addDragAndDropStep(title, text, new SelfResolver<edu.cmu.cs.dennisc.croquet.DragAndDropOperation>( dragOperation ), dropText, new FirstComponentResolver(dropModelResolver), popupMenuText, inputDialogText, completorValidatorOkButtonDisabler, completorValidatorOkButtonDisabler );
	}

	
//	@Deprecated
//	public Step EPIC_HACK_addDeclareProcedureDialogOpenAndCommitStep( String title, String openText, String commitText, InputDialogOperationCompletorValidator completorValidator, final org.alice.ide.operations.ast.DeclareProcedureOperation.EPIC_HACK_Validator validator ) {
//		Step step = new InputDialogOpenAndCommitStep( title, openText, commitText, ((org.alice.ide.tutorial.IdeTutorial)this).createDeclareProcedureOperationResolver(), completorValidator, completorValidator ) {
//			@Override
//			protected void setActiveNote(int activeIndex) {
//				super.setActiveNote(activeIndex);
//				if( activeIndex == 1 ) {
//					org.alice.ide.operations.ast.DeclareProcedureOperation declareProcedureOperation = (org.alice.ide.operations.ast.DeclareProcedureOperation)this.getModel();
//					declareProcedureOperation.setValidator( validator );
//				}
//			}
//		};
//		return this.addStep( step );
//	}
//	@Deprecated
//	public Step EPIC_HACK_addDeclareMethodParameterDialogOpenAndCommitStep( String title, String openText, String commitText, InputDialogOperationCompletorValidator completorValidator, final org.alice.ide.operations.ast.DeclareMethodParameterOperation.EPIC_HACK_Validator validator ) {
//		Step step = new InputDialogOpenAndCommitStep( title, openText, commitText, ((org.alice.ide.tutorial.IdeTutorial)this).createDeclareMethodParameterOperationResolver(), completorValidator, completorValidator ) {
//			@Override
//			protected void setActiveNote(int activeIndex) {
//				super.setActiveNote(activeIndex);
//				if( activeIndex == 1 ) {
//					org.alice.ide.operations.ast.DeclareMethodParameterOperation declareParameterOperation = (org.alice.ide.operations.ast.DeclareMethodParameterOperation)this.getModel();
//					declareParameterOperation.setValidator( validator );
//				}
//			}
//		};
//		return this.addStep( step );
//	}
//
//	
//	@Deprecated
//	public Step EPIC_HACK_addForEachInArrayLoopDragAndDropToPopupMenuToInputDialogStep( String title, String text, String dropText, Resolver<? extends edu.cmu.cs.dennisc.croquet.TrackableShape> dropShapeResolver, String popupMenuText, String inputDialogText, DragAndDropOperationCompletorValidator completorValidator, final org.alice.ide.cascade.customfillin.CustomInputDialogOperation.EPIC_HACK_Validator validator ) {
//		Step step = new DragAndDropStep( title, text, ((org.alice.ide.tutorial.IdeTutorial)this).createForEachInArrayLoopTemplateResolver(), dropText, dropShapeResolver, popupMenuText, inputDialogText, completorValidator, completorValidator ) {
//			@Override
//			public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
//				boolean rv = super.isWhatWeveBeenWaitingFor( child );
//				if( child instanceof edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext<?> ) {
//					edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext<?> context = (edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext<?>)child;
//					edu.cmu.cs.dennisc.croquet.Model model = context.getModel();
//					if (model instanceof org.alice.ide.cascade.customfillin.CustomInputDialogOperation ) {
//						org.alice.ide.cascade.customfillin.CustomInputDialogOperation customInputDialogOperation = (org.alice.ide.cascade.customfillin.CustomInputDialogOperation) model;
//						customInputDialogOperation.setValidator( validator );
//					}
//				}
//				return rv;
//			}
//		};
//		return this.addStep( step );
//	}


	public void setVisible( boolean isVisible ) {
		if( isVisible ) {
			this.stencil.addToLayeredPane();
		} else {
			this.stencil.removeFromLayeredPane();
		}
	}
}
