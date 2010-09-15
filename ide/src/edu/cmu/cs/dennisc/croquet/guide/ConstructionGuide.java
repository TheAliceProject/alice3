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
package edu.cmu.cs.dennisc.croquet.guide;

import edu.cmu.cs.dennisc.tutorial.*;

/**
 * @author Dennis Cosgrove
 */
public class ConstructionGuide {
	private static ConstructionGuide instance;
	public static ConstructionGuide getInstance() {
		return instance;
	}
	public static javax.swing.JLayeredPane getLayeredPane() {
		edu.cmu.cs.dennisc.croquet.Application application = edu.cmu.cs.dennisc.croquet.Application.getSingleton();
		javax.swing.JFrame frame = application.getFrame().getAwtComponent();
		javax.swing.JLayeredPane layeredPane = frame.getLayeredPane();
		final int PAD = 4;
		frame.getJMenuBar().setBorder( javax.swing.BorderFactory.createEmptyBorder(PAD+32,PAD,0,PAD));
		((javax.swing.JComponent)frame.getContentPane()).setBorder( javax.swing.BorderFactory.createEmptyBorder(0,PAD,PAD,PAD));
		return layeredPane; 
	}

	private AutomaticTutorialStencil stencil;
	private edu.cmu.cs.dennisc.croquet.RootContext sourceContext;

	/*package-private*/ class AutomaticTutorialStencil extends TutorialStencil {
		public AutomaticTutorialStencil( MenuPolicy menuPolicy, ScrollingRequiredRenderer scrollingRequiredRenderer, javax.swing.JLayeredPane layeredPane, edu.cmu.cs.dennisc.croquet.Group[] groups ) {
			super( menuPolicy, scrollingRequiredRenderer, layeredPane, groups, edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext() );
		}
	}
	
	public ConstructionGuide( MenuPolicy menuPolicy, ScrollingRequiredRenderer scrollingRequiredRenderer, edu.cmu.cs.dennisc.croquet.Group[] groupsTrackedForRandomAccess ) {
		instance = this;
		this.stencil = new AutomaticTutorialStencil( menuPolicy, scrollingRequiredRenderer, getLayeredPane(), groupsTrackedForRandomAccess );
	}
	private edu.cmu.cs.dennisc.croquet.Retargeter retargeter;
	public edu.cmu.cs.dennisc.croquet.Retargeter getRetargeter() {
		return this.retargeter;
	}
	public void setRetargeter( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.retargeter = retargeter;
	}
	
	public void retargetOriginalContext( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.sourceContext.retarget( retargeter );
	}
	
	private void addMessageStep( String title, String text ) {
		this.stencil.addStep( new MessageStep( title, text ) );
	}
	private static RequirementNote createBonusOperationNote( ParentContextCriterion parentContextCriterion, edu.cmu.cs.dennisc.croquet.OperationContext< ? > operationContext ) {
		if( operationContext instanceof edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext ){
			if( operationContext instanceof edu.cmu.cs.dennisc.croquet.DialogOperationContext ) {
				edu.cmu.cs.dennisc.croquet.DialogOperationContext dialogOperationContext = (edu.cmu.cs.dennisc.croquet.DialogOperationContext)operationContext;
				edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = operationContext.getSuccessfulCompletionEvent();
				if( successfulCompletionEvent != null ) {
					return DialogCloseNote.createInstance( dialogOperationContext, parentContextCriterion, successfulCompletionEvent );
				}
			} else if( operationContext instanceof edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > ) {
				edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<?> inputDialogOperationContext = (edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<?>)operationContext;
				edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = inputDialogOperationContext.getSuccessfulCompletionEvent();
				if( successfulCompletionEvent != null ) {
					//rv.add( InputDialogAcceptableEditNote.createInstance( inputDialogOperationContext, parentContextCriterion ) );
					return InputDialogOperationFinishNote.createInstance( inputDialogOperationContext, parentContextCriterion, successfulCompletionEvent );
				}
			}
		}
		return null;
	}
	private static java.util.List< RetargetableNote > appendNotes( java.util.List< RetargetableNote > rv, ParentContextCriterion parentContextCriterion, edu.cmu.cs.dennisc.croquet.HistoryNode node ) {
		if( node instanceof edu.cmu.cs.dennisc.croquet.MenuBarModelContext ) {
			edu.cmu.cs.dennisc.croquet.MenuBarModelContext menuBarModelContext = (edu.cmu.cs.dennisc.croquet.MenuBarModelContext)node;
			if( menuBarModelContext.getState() != null ) {
				edu.cmu.cs.dennisc.croquet.HistoryNode lastChild = menuBarModelContext.getLastChild();
				if( lastChild instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext ) {
					edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext popupMenuOperationContext = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext)lastChild;
					appendNotes( rv, parentContextCriterion/*IsAnyMenuBarModelContextCriterion.SINGLETON*/, popupMenuOperationContext );
				}
			}
		} else if( node instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext ) {
			edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext = (edu.cmu.cs.dennisc.croquet.DragAndDropContext)node;
			int DND_CONTEXT_CHILD_COUNT = dragAndDropContext.getChildCount();
			if( DND_CONTEXT_CHILD_COUNT > 1 ) {
				edu.cmu.cs.dennisc.croquet.HistoryNode lastChild = dragAndDropContext.getChildAt( DND_CONTEXT_CHILD_COUNT-1 );
				if( lastChild instanceof edu.cmu.cs.dennisc.croquet.ModelContext< ? > ) {
					edu.cmu.cs.dennisc.croquet.ModelContext< ? > childModelContext = (edu.cmu.cs.dennisc.croquet.ModelContext< ? >)lastChild;
					edu.cmu.cs.dennisc.croquet.HistoryNode lastGrandchild = childModelContext.getLastChild();
					DragNote dragNote = DragNote.createInstance( dragAndDropContext, parentContextCriterion );
					DropNote dropNote;
					boolean isCommit = lastGrandchild instanceof edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent;
					if( isCommit ) {
						edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = (edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent)lastGrandchild;
						dropNote = DropNote.createCommitInstance( dragNote.getLastAcceptedContext(), dragAndDropContext, childModelContext, successfulCompletionEvent );
					} else {
						dropNote = DropNote.createPendingInstance( dragNote.getLastAcceptedContext(), dragAndDropContext, childModelContext );
					}
					rv.add( dragNote );
					rv.add( dropNote );
					if( isCommit ) {
						//pass
					} else {
						appendNotes( rv, dropNote.getLastAcceptedContext(), childModelContext );
					}
				}
			}
		} else if( node instanceof edu.cmu.cs.dennisc.croquet.OperationContext ) {
			edu.cmu.cs.dennisc.croquet.OperationContext<?> operationContext = (edu.cmu.cs.dennisc.croquet.OperationContext<?>)node;
			if( node instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext ) {
				edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext popupMenuOperationContext = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext)node;
				int POPUP_CONTEXT_CHILD_COUNT = popupMenuOperationContext.getChildCount();
				if( POPUP_CONTEXT_CHILD_COUNT > 1 ) {
					edu.cmu.cs.dennisc.croquet.HistoryNode<?> firstChild = popupMenuOperationContext.getChildAt( 0 );
					edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent retargetableMenuModelInitializationEvent;
					if( firstChild instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent ) {
						retargetableMenuModelInitializationEvent = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent)firstChild;
					} else {
						retargetableMenuModelInitializationEvent = null;
					}
					edu.cmu.cs.dennisc.croquet.HistoryNode<?> lastChild = popupMenuOperationContext.getChildAt( POPUP_CONTEXT_CHILD_COUNT-1 );
					if( lastChild instanceof edu.cmu.cs.dennisc.croquet.ModelContext< ? > ) {
						ParentContextCriterion menuSelectionParentContextCriterion = parentContextCriterion;
						ParentContextCriterion modelContextParentContextCriterion = null;
						edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext = (edu.cmu.cs.dennisc.croquet.ModelContext< ? >)lastChild;
						edu.cmu.cs.dennisc.croquet.HistoryNode<?> secondToLastChild = popupMenuOperationContext.getChildAt( POPUP_CONTEXT_CHILD_COUNT-2 );
						if( secondToLastChild instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent ) {
							edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent)secondToLastChild;
							final int N = menuSelectionEvent.getModelCount();
							if( N > 0 ) {
								int index0;
								if( menuSelectionEvent.getModelAt( 0 ) instanceof edu.cmu.cs.dennisc.croquet.MenuBarModel ) {
									index0 = 1;
								} else {
									if( node.getParent() instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext ) {
										//pass
									} else {
										PopupMenuOperationStartNote popupMenuOperationStartNote = PopupMenuOperationStartNote.createInstance( popupMenuOperationContext, parentContextCriterion );
										rv.add( popupMenuOperationStartNote );
										menuSelectionParentContextCriterion = popupMenuOperationStartNote.getLastAcceptedContext();
									}
									index0 = 0;
								}
								for( int i=index0; i<N; i++ ) {
									edu.cmu.cs.dennisc.croquet.ModelContext< ? > childContext;
									if( i == N-1 ) {
										childContext = modelContext;
									} else {
										childContext = null;
									}
									MenuSelectionNote note = MenuSelectionNote.createInstance( menuSelectionParentContextCriterion, retargetableMenuModelInitializationEvent, menuSelectionEvent, i, childContext, index0 );
									rv.add( note );
									if( i==index0 ) {
										menuSelectionParentContextCriterion = note.getAcceptedContextAt( index0 );
										retargetableMenuModelInitializationEvent = null;
									}
									if( i == N-1 ) {
										modelContextParentContextCriterion = note.getLastAcceptedContext();
									}
								}
							}
						}
						if( modelContext instanceof edu.cmu.cs.dennisc.croquet.OperationContext< ? > ) {
							edu.cmu.cs.dennisc.croquet.OperationContext< ? > childOperationContext = (edu.cmu.cs.dennisc.croquet.OperationContext< ? >)modelContext;
							edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = childOperationContext.getSuccessfulCompletionEvent();
							if( successfulCompletionEvent != null ) {
								if( childOperationContext instanceof edu.cmu.cs.dennisc.croquet.ActionOperationContext ) {
									edu.cmu.cs.dennisc.croquet.ActionOperationContext actionOperationContext = (edu.cmu.cs.dennisc.croquet.ActionOperationContext)childOperationContext;
									if( actionOperationContext.getChildCount() > 1 ) {
										edu.cmu.cs.dennisc.croquet.HistoryNode<?> possibleInputDialogOperationContext = actionOperationContext.getChildAt( 0 );
										if( possibleInputDialogOperationContext instanceof edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > ) {
											edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? > inputDialogOperationContext = (edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< ? >)possibleInputDialogOperationContext;
											edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent inputDialogCompletionEvent = inputDialogOperationContext.getSuccessfulCompletionEvent();
											if( inputDialogCompletionEvent != null ) {
												rv.add( ForwardingFromActionOperationToInputDialogOperationNote.createInstance( modelContextParentContextCriterion, actionOperationContext, successfulCompletionEvent, inputDialogOperationContext, inputDialogCompletionEvent ) );
											}
										}
									}
								} else {
									RetargetableNote bonusNote = createBonusOperationNote( modelContextParentContextCriterion, childOperationContext );
									if( bonusNote != null ) {
										rv.add( bonusNote );
									}
								}
								RetargetableNote lastNote = rv.get( rv.size()-1 );
								if( lastNote instanceof RequirementNote ) {
									RequirementNote requirementNote = (RequirementNote)lastNote;
									requirementNote.addRequirement( new IsAcceptableSuccessfulCompletionOf( requirementNote.getAcceptedContextAt( -2 ), successfulCompletionEvent ) );
								}
							}
						}
					}
				}
			} else {
				edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = operationContext.getSuccessfulCompletionEvent();
				if( successfulCompletionEvent != null ) {
					if( operationContext instanceof edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext ){
						OperationStartNote startNote = OperationStartNote.createInstance( operationContext, parentContextCriterion );
						RequirementNote bonusNote = createBonusOperationNote( startNote.getLastAcceptedContext(), operationContext );
						rv.add( startNote );
						rv.add( bonusNote );
					} else {
						rv.add( OperationNote.createInstance( operationContext, parentContextCriterion, successfulCompletionEvent ) );
					}
				}
			}
		} else if( node instanceof edu.cmu.cs.dennisc.croquet.BooleanStateContext ) {
			edu.cmu.cs.dennisc.croquet.BooleanStateContext booleanStateContext = (edu.cmu.cs.dennisc.croquet.BooleanStateContext)node;
			edu.cmu.cs.dennisc.croquet.HistoryNode lastChild = booleanStateContext.getLastChild();
			if( lastChild instanceof edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent ) {
				edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = (edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent)lastChild;
				rv.add( BooleanStateNote.createInstance( booleanStateContext, parentContextCriterion, successfulCompletionEvent ) );
			}
		} else if( node instanceof edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< ? > ) {
			edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< ? > listSelectionStateContext = (edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< ? >)node;
			edu.cmu.cs.dennisc.croquet.HistoryNode lastChild = listSelectionStateContext.getLastChild();
			if( lastChild instanceof edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent ) {
				edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = (edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent)lastChild;
				ModelFromContextResolver modelResolver = new ModelFromContextResolver( listSelectionStateContext );
				FirstComponentResolver firstComponentResolver = new FirstComponentResolver( modelResolver );
				edu.cmu.cs.dennisc.croquet.Component< ? > component = firstComponentResolver.getResolved();
				if( component instanceof edu.cmu.cs.dennisc.croquet.ItemSelectable< ?,? > ) {
					edu.cmu.cs.dennisc.croquet.ItemSelectable< ?,? > itemSelectable = (edu.cmu.cs.dennisc.croquet.ItemSelectable< ?,? >)component;
					if( itemSelectable.isSingleStageSelectable() ) {
						rv.add( ListSelectionStateSimpleNote.createInstance( listSelectionStateContext, parentContextCriterion, successfulCompletionEvent ) );
					} else {
						ListSelectionStateStartNote startNote = ListSelectionStateStartNote.createInstance( listSelectionStateContext, parentContextCriterion, successfulCompletionEvent ); 
						rv.add( startNote );
						rv.add( ListSelectionStateFinishNote.createInstance( listSelectionStateContext, startNote.getLastAcceptedContext(), successfulCompletionEvent ) );
					}
				}
			}
		} else {
			System.err.println( "WARNING: " + node + " not handled." );
		}
		return rv;
	}

	/*package-private*/ class ContextStep extends Step implements WaitingStep {
		private edu.cmu.cs.dennisc.croquet.ModelContext< ? > context;
		public ContextStep( edu.cmu.cs.dennisc.croquet.ModelContext< ? > context ) {
			this.context = context;
		}
		@Override
		protected String getTitle() {
			edu.cmu.cs.dennisc.croquet.Model model = context.getModel();
			return model.getClass().getSimpleName();
		}
		@Override
		public void reset() {
			super.reset();
			this.setActiveNote( 0 );
		}
		
		@Override
		protected java.awt.Point calculateLocationOfFirstNote( edu.cmu.cs.dennisc.croquet.Container< ? > container ) {
			java.awt.Point rv = super.calculateLocationOfFirstNote( container );
			rv.x += 200;
			rv.y += 100;
			return rv;
		}
		
		
		private edu.cmu.cs.dennisc.croquet.Model huntFor( java.util.List< edu.cmu.cs.dennisc.croquet.Model > list, edu.cmu.cs.dennisc.croquet.MenuModel menuModel, edu.cmu.cs.dennisc.croquet.Model model ) {
			if( menuModel instanceof edu.cmu.cs.dennisc.croquet.DefaultMenuModel ) {
				edu.cmu.cs.dennisc.croquet.DefaultMenuModel defaultMenuModel = (edu.cmu.cs.dennisc.croquet.DefaultMenuModel)menuModel;
				for( edu.cmu.cs.dennisc.croquet.Model child : defaultMenuModel.getModels() ) {
					if( child instanceof edu.cmu.cs.dennisc.croquet.MenuModel ) {
						edu.cmu.cs.dennisc.croquet.MenuModel childMenuModel = (edu.cmu.cs.dennisc.croquet.MenuModel)child;
						edu.cmu.cs.dennisc.croquet.Model rv = this.huntFor( list, childMenuModel, model );
						if( rv != null ) {
							list.add( 0, childMenuModel );
						}
						return rv;
					}
					if( child == model ) {
						list.add( 0, model );
						return model;
					}
				}
			}
			return null;
		}
		private java.util.List< edu.cmu.cs.dennisc.croquet.Model > huntFor( edu.cmu.cs.dennisc.croquet.Model model ) {
			edu.cmu.cs.dennisc.croquet.MenuBarModel menuBarModel = edu.cmu.cs.dennisc.croquet.Application.getSingleton().getFrame().getMenuBarModel();
			java.util.List< edu.cmu.cs.dennisc.croquet.Model > rv = edu.cmu.cs.dennisc.java.util.Collections.newStack();
			for( edu.cmu.cs.dennisc.croquet.MenuModel menuModel : menuBarModel.getMenuModels() ) {
				edu.cmu.cs.dennisc.croquet.Model found = huntFor( rv, menuModel, model );
				if( found != null ) {
					rv.add( 0, menuModel );
					rv.add( 0, menuBarModel );
					return rv;
				}
			}
			return null;
		}

		private java.util.List< RetargetableNote > notes;
		private java.util.List< RetargetableNote > createNotes() {
			java.util.List< RetargetableNote > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			
			edu.cmu.cs.dennisc.croquet.Model model = this.context.getModel();
			edu.cmu.cs.dennisc.croquet.Component< ? > component = model.getFirstComponent();
			if( component instanceof edu.cmu.cs.dennisc.croquet.MenuItem ) {
				edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = this.context.getSuccessfulCompletionEvent();
				edu.cmu.cs.dennisc.croquet.Model descendantModel = successfulCompletionEvent.getParent().getModel();

				java.util.List< edu.cmu.cs.dennisc.croquet.Model > list = huntFor( descendantModel );
				if( list != null ) {
					edu.cmu.cs.dennisc.croquet.MenuBarModelContext menuBarModelContext = edu.cmu.cs.dennisc.croquet.ContextManager.createContextFor( list, this.context );
					appendNotes( rv, IsRootContextCriterion.IS_PARENT_ROOT_CONTEXT, menuBarModelContext );
				} else {
					//todo?
				}
			} else {
				if( component != null && component.isInView() ) {
					appendNotes( rv, IsRootContextCriterion.IS_PARENT_ROOT_CONTEXT, this.context );
				} else {
				}
			}
			return rv;
		}
		@Override
		public java.util.List< ? extends Note > getNotes() {
			if( this.notes != null ) {
				//pass
			} else {
				this.notes = this.createNotes();
				int i = 1;
				for( Note note : this.notes ) {
					if( this.notes.size() > 1 ) {
						note.setLabel( Integer.toString( i ) );
					}
					note.setTutorialStencil( this.getTutorialStencil() );
					i++;
				}
			}
			return this.notes;
		}
		@Override
		public void setTutorialStencil( edu.cmu.cs.dennisc.tutorial.TutorialStencil tutorialStencil ) {
			super.setTutorialStencil( tutorialStencil );
			if( this.notes != null ) {
				for( Note note : this.notes ) {
					note.setTutorialStencil( this.getTutorialStencil() );
				}
			}
		}
		public boolean isAlreadyInTheDesiredState() {
			return false;
		}
		@Override
		public boolean isEventInterceptable( java.awt.event.MouseEvent e ) {
			int activeNoteIndex = this.getIndexOfFirstActiveNote();
			if( activeNoteIndex > 0 ) {
				return this.notes.get( activeNoteIndex ).isEventInterceptable( e );
			} else {
				return super.isEventInterceptable( e );
			}
		}
		public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
			final int NOTE_COUNT = this.getNoteCount();
			int activeNoteIndex = this.getIndexOfFirstActiveNote();
			activeNoteIndex = Math.max( activeNoteIndex, 0 );
			if( activeNoteIndex < NOTE_COUNT ) {
				RetargetableNote activeNote = this.notes.get( activeNoteIndex );
				if( activeNote.isWhatWeveBeenWaitingFor( child ) ) {
					activeNoteIndex ++;
					if( activeNoteIndex == NOTE_COUNT ) {
						return true;
					} else {
						this.setActiveNote( activeNoteIndex );
					}
				}
			}
			return false;
		}
		@Override
		protected void complete() {
			edu.cmu.cs.dennisc.croquet.SuccessfulCompletionEvent successfulCompletionEvent = this.context.getSuccessfulCompletionEvent();
			edu.cmu.cs.dennisc.croquet.Edit originalEdit = successfulCompletionEvent.getEdit();
			if( originalEdit != null ) {
				edu.cmu.cs.dennisc.croquet.Edit< ? > replacementEdit = originalEdit.getModel().commitTutorialCompletionEdit( originalEdit, retargeter );
				if( replacementEdit != null ) {
					//replacementEdit.addKeyValuePairs( retargeter, originalEdit );
					retargetOriginalContext( retargeter );
				}
//				edu.cmu.cs.dennisc.croquet.Edit replacementEdit = edit.getAcceptableReplacement( retargeter );
//				replacementEdit.setReplacementModel( edit.getModel() );
//				TutorialStencil.complete( replacementEdit );
				//successfulCompletionEvent.getParent().commitAndInvokeDo( replacementEdit );
			}
		}
	}

	/*package-private*/ AutomaticTutorialStencil getStencil() {
		return this.stencil;
	}
	public void addSteps( edu.cmu.cs.dennisc.croquet.RootContext sourceContext ) {
		//this.addMessageStep( "start", "start of tutorial" );
		this.sourceContext = sourceContext;
		final int N = sourceContext.getChildCount();
		for( int i=0; i<N; i++ ) {
			edu.cmu.cs.dennisc.croquet.HistoryNode node = sourceContext.getChildAt( i );
			if( node instanceof edu.cmu.cs.dennisc.croquet.ModelContext< ? > ) {
				edu.cmu.cs.dennisc.croquet.ModelContext< ? > context = (edu.cmu.cs.dennisc.croquet.ModelContext< ? >)node;
				if( context.isSuccessfullyCompleted() ) {
					this.stencil.addStep( new ContextStep( context ) );
				}
			}
		}
		this.addMessageStep( "Finished", "<strong>Congratulations.</strong><br>You have completed the guided interaction." );
	}
	public void setSelectedIndex( int index ) {
		this.stencil.setSelectedIndex( index );
	}
	public void setVisible( boolean isVisible ) {
		if( isVisible ) {
			this.stencil.addToLayeredPane();
		} else {
			this.stencil.removeFromLayeredPane();
		}
	}
}
