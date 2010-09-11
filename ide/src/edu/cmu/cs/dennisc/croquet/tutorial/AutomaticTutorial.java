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
package edu.cmu.cs.dennisc.croquet.tutorial;

import edu.cmu.cs.dennisc.tutorial.*;

class CancelException extends Exception {
	public CancelException( String message ) {
		super( message );
	}
}

interface ParentContextCriterion {
	boolean isAcceptableParentContext( edu.cmu.cs.dennisc.croquet.ModelContext< ? > parentContext );
}

enum IsRootContextCriterion implements ParentContextCriterion {
	SINGLETON;
	public boolean isAcceptableParentContext( edu.cmu.cs.dennisc.croquet.ModelContext< ? > parentContext ) {
		return edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext() == parentContext;
	}
}

enum IsAnyMenuBarModelContextCriterion implements ParentContextCriterion {
	SINGLETON;
	public boolean isAcceptableParentContext( edu.cmu.cs.dennisc.croquet.ModelContext< ? > parentContext ) {
		return parentContext instanceof edu.cmu.cs.dennisc.croquet.MenuBarModelContext;
	}
}

interface Requirement< N extends edu.cmu.cs.dennisc.croquet.HistoryNode > {
	//void bindParentContext();
	//void unbindParentContext();
	boolean isWhatWereLookingFor( edu.cmu.cs.dennisc.croquet.HistoryNode historyNode ) throws CancelException;
}

class IsChildOfAndInstanceOf<N extends edu.cmu.cs.dennisc.croquet.HistoryNode> implements Requirement<N> {
	private ParentContextCriterion parentContextCriterion;
	private Class<N> cls;
	public IsChildOfAndInstanceOf( ParentContextCriterion parentContextCriterion, Class<N> cls ) {
		this.parentContextCriterion = parentContextCriterion;
		this.cls = cls;
	}
	protected boolean isSpecificallyWhatWereLookingFor(N historyNode) throws CancelException {
		return true;
	}
	public final boolean isWhatWereLookingFor( edu.cmu.cs.dennisc.croquet.HistoryNode historyNode ) throws CancelException {
		if( this.parentContextCriterion.isAcceptableParentContext( historyNode.getParent() ) ) {
			if( this.cls.isAssignableFrom( historyNode.getClass() ) ) {
				return this.isSpecificallyWhatWereLookingFor( this.cls.cast( historyNode ) );
			} else {
				if( historyNode instanceof edu.cmu.cs.dennisc.croquet.CancelEvent ) {
					throw new CancelException( "cancel" );
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
}

class IsAcceptableCommitOf extends IsChildOfAndInstanceOf< edu.cmu.cs.dennisc.croquet.CommitEvent > {
	private edu.cmu.cs.dennisc.croquet.CommitEvent originalCommitEvent;
	public IsAcceptableCommitOf( ParentContextCriterion parentContextCriterion, edu.cmu.cs.dennisc.croquet.CommitEvent originalCommitEvent ) {
		super( parentContextCriterion, edu.cmu.cs.dennisc.croquet.CommitEvent.class );
		this.originalCommitEvent = originalCommitEvent;
	}
	@Override
	protected boolean isSpecificallyWhatWereLookingFor( edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent ) throws CancelException {
		boolean rv = super.isSpecificallyWhatWereLookingFor( commitEvent );
		if( rv ) {
			edu.cmu.cs.dennisc.croquet.Edit< ? > potentialReplacementEdit = commitEvent.getEdit();
			if( this.originalCommitEvent != null ) {
				edu.cmu.cs.dennisc.croquet.Edit< ? > originalEdit = this.originalCommitEvent.getEdit();
				if( originalEdit.isReplacementAcceptable( potentialReplacementEdit ) ) {
					edu.cmu.cs.dennisc.croquet.Retargeter retargeter = AutomaticTutorial.getInstance().getRetargeter();
					originalEdit.addKeyValuePairs( retargeter, potentialReplacementEdit );
					AutomaticTutorial.getInstance().retargetOriginalContext( retargeter );
				} else {
					throw new CancelException( "unacceptable: replacement edit does not pass muster." );
				}
			} else {
				if( potentialReplacementEdit != null ) {
					throw new CancelException( "unacceptable: replacement edit is null." );
				} else {
					//pass
				}
			}
		}
		return rv;
	}
}

class IsMenuSelectionEventOf extends IsChildOfAndInstanceOf< edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent > {
	private edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent originalMenuSelectionEvent;
	private int requiredIndex;
	public IsMenuSelectionEventOf( ParentContextCriterion parentContextCriterion, edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent originalMenuSelectionEvent, int requiredIndex ) {
		super( parentContextCriterion, edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent.class );
		this.originalMenuSelectionEvent = originalMenuSelectionEvent;
		this.requiredIndex = requiredIndex;
	}
	@Override
	protected boolean isSpecificallyWhatWereLookingFor( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent ) throws CancelException {
		boolean rv = super.isSpecificallyWhatWereLookingFor( menuSelectionEvent );
		if( rv ) {
			
		}
		return rv;
	}
}

class RequirementNote extends HistoryNote implements ParentContextCriterion {
	private java.util.List< Requirement<?> > requirements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private int fulfilledRequirementIndex;
	
	private java.util.List< edu.cmu.cs.dennisc.croquet.HistoryNode > nodes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private int checkIndex = -1;
	
	public RequirementNote( String text, Requirement<?>... requirements ) {
		super( text );
		edu.cmu.cs.dennisc.java.util.Collections.addAll( this.requirements, requirements );
		for( int i=0; i<this.requirements.size(); i++ ) {
			this.nodes.add( null );
		}
	}
	public void setCheckIndex( int checkIndex ) {
		this.checkIndex = checkIndex;
	}
	public void addRequirement( Requirement<?> requirement ) {
		this.requirements.add( requirement );
		this.nodes.add( null );
	}
	@Override
	public void reset() {
		super.reset();
		this.fulfilledRequirementIndex = 0;
		for( int i=0; i<this.nodes.size(); i++ ) {
			this.nodes.set( i, null );
		}
	}
	@Override
	public final boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
		try {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "isWhatWeveBeenWaitingFor", child );
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "isWhatWeveBeenWaitingFor", this.index );
			final int N = this.requirements.size();
			while( this.fulfilledRequirementIndex<N ) {
				if( this.requirements.get( this.fulfilledRequirementIndex ).isWhatWereLookingFor( child ) ) {
					this.nodes.set( this.fulfilledRequirementIndex, child );
					this.fulfilledRequirementIndex += 1;
				} else {
					break;
				}
			}
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "isWhatWeveBeenWaitingFor", this.index );
			return this.fulfilledRequirementIndex == N;
		} catch( CancelException ce ) {
			throw new RuntimeException( "todo", ce );
		}
	}
	public boolean isAcceptableParentContext( edu.cmu.cs.dennisc.croquet.ModelContext< ? > parentContext ) {
		boolean rv;
		final int N = this.requirements.size();
		int actualIndex = this.checkIndex;
		if( actualIndex < 0 ) {
			actualIndex += N;
		}
		edu.cmu.cs.dennisc.croquet.HistoryNode checkNode = this.nodes.get( actualIndex );
		if( checkNode != null ) {
			if( checkNode instanceof edu.cmu.cs.dennisc.croquet.ModelContext ) {
				rv = checkNode == parentContext;
			} else {
				rv = checkNode.getParent() == parentContext;
			}
		} else {
			throw new NullPointerException();
		}
		
//		if( rv ) {
//			//pass
//		} else {
//			for( int i=0; i<N; i++ ) {
//				edu.cmu.cs.dennisc.croquet.HistoryNode node = this.nodes.get( i );
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "isAcceptableParentContext:", i, N, node != null ? node.hashCode() : 0, node );
//			}
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "isAcceptableParentContext: parentCx", parentContext.hashCode(), parentContext );
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "isAcceptableParentContext: value", checkNode == parentContext );
//		}
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class AutomaticTutorial {
	private static AutomaticTutorial instance;
	public static AutomaticTutorial getInstance() {
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
	private java.util.List< edu.cmu.cs.dennisc.croquet.Group > groupsForWhichStepsAreGenerated; 
	private edu.cmu.cs.dennisc.croquet.RootContext sourceContext;

	/*package-private*/ class AutomaticTutorialStencil extends TutorialStencil {
		public AutomaticTutorialStencil( MenuPolicy menuPolicy, javax.swing.JLayeredPane layeredPane, edu.cmu.cs.dennisc.croquet.Group[] groups ) {
			super( menuPolicy, layeredPane, groups, edu.cmu.cs.dennisc.croquet.ContextManager.getRootContext() );
		}
	}
	
	public AutomaticTutorial( MenuPolicy menuPolicy, edu.cmu.cs.dennisc.croquet.Group[] groupsTrackedForRandomAccess, edu.cmu.cs.dennisc.croquet.Group[] bonusGroupsForWhichStepsAreGenerated ) {
		instance = this;
		this.stencil = new AutomaticTutorialStencil( menuPolicy, getLayeredPane(), groupsTrackedForRandomAccess );
		this.groupsForWhichStepsAreGenerated = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		edu.cmu.cs.dennisc.java.util.Collections.addAll( this.groupsForWhichStepsAreGenerated, groupsTrackedForRandomAccess );
		edu.cmu.cs.dennisc.java.util.Collections.addAll( this.groupsForWhichStepsAreGenerated, bonusGroupsForWhichStepsAreGenerated );
		
		
		//todo: remove
		this.groupsForWhichStepsAreGenerated.add( edu.cmu.cs.dennisc.croquet.DragAndDropModel.DRAG_GROUP );
		this.groupsForWhichStepsAreGenerated.add( edu.cmu.cs.dennisc.croquet.MenuBarModel.MENU_BAR_MODEL_GROUP );
		//
		
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
	private static java.util.List< HistoryNote > appendBonusOperationNotes( java.util.List< HistoryNote > rv, edu.cmu.cs.dennisc.croquet.OperationContext< ? > operationContext ) {
		if( operationContext instanceof edu.cmu.cs.dennisc.croquet.DialogOperationContext ) {
			edu.cmu.cs.dennisc.croquet.DialogOperationContext dialogOperationContext = (edu.cmu.cs.dennisc.croquet.DialogOperationContext)operationContext;
			rv.add( new DialogCloseNote( dialogOperationContext ) );
		}
		return rv;
	}
	private static java.util.List< HistoryNote > appendNotes( java.util.List< HistoryNote > rv, ParentContextCriterion parentContextCriterion, edu.cmu.cs.dennisc.croquet.HistoryNode node ) {
		if( node instanceof edu.cmu.cs.dennisc.croquet.MenuBarModelContext ) {
			edu.cmu.cs.dennisc.croquet.MenuBarModelContext menuBarModelContext = (edu.cmu.cs.dennisc.croquet.MenuBarModelContext)node;
			if( menuBarModelContext.getState() != null ) {
				edu.cmu.cs.dennisc.croquet.HistoryNode lastChild = menuBarModelContext.getLastChild();
				if( lastChild instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext ) {
					edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext popupMenuOperationContext = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext)lastChild;
					appendNotes( rv, IsAnyMenuBarModelContextCriterion.SINGLETON, popupMenuOperationContext );
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
					boolean isCommit = lastGrandchild instanceof edu.cmu.cs.dennisc.croquet.CommitEvent;
					if( isCommit ) {
						edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent = (edu.cmu.cs.dennisc.croquet.CommitEvent)lastGrandchild;
						dropNote = DropNote.createCommitInstance( dragNote, dragAndDropContext, childModelContext, commitEvent );
					} else {
						dropNote = DropNote.createPendingInstance( dragNote, dragAndDropContext, childModelContext );
					}
					rv.add( dragNote );
					rv.add( dropNote );
					if( isCommit ) {
						//pass
					} else {
						appendNotes( rv, dropNote, childModelContext );
					}
				}
			}
		} else if( node instanceof edu.cmu.cs.dennisc.croquet.OperationContext ) {
			edu.cmu.cs.dennisc.croquet.OperationContext<?> operationContext = (edu.cmu.cs.dennisc.croquet.OperationContext<?>)node;
			if( node instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext ) {
				edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext popupMenuOperationContext = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext)node;
				int POPUP_CONTEXT_CHILD_COUNT = popupMenuOperationContext.getChildCount();
				if( POPUP_CONTEXT_CHILD_COUNT > 1 ) {
					edu.cmu.cs.dennisc.croquet.HistoryNode firstChild = popupMenuOperationContext.getChildAt( 0 );
					edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent retargetableMenuModelInitializationEvent;
					if( firstChild instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent ) {
						retargetableMenuModelInitializationEvent = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent)firstChild;
					} else {
						retargetableMenuModelInitializationEvent = null;
					}
					edu.cmu.cs.dennisc.croquet.HistoryNode lastChild = popupMenuOperationContext.getChildAt( POPUP_CONTEXT_CHILD_COUNT-1 );
					if( lastChild instanceof edu.cmu.cs.dennisc.croquet.ModelContext< ? > ) {
						edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext = (edu.cmu.cs.dennisc.croquet.ModelContext< ? >)lastChild;
						edu.cmu.cs.dennisc.croquet.HistoryNode secondToLastChild = popupMenuOperationContext.getChildAt( POPUP_CONTEXT_CHILD_COUNT-2 );
						if( secondToLastChild instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent ) {
							edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent)secondToLastChild;
							final int N = menuSelectionEvent.getModelCount();
							if( N > 0 ) {
								int index0;
								if( menuSelectionEvent.getModelAt( 0 ) instanceof edu.cmu.cs.dennisc.croquet.MenuBarModel ) {
									index0 = 1;
								} else {
									index0 = 0;
								}
								for( int i=index0; i<N; i++ ) {
									rv.add( MenuSelectionNote.createInstance( retargetableMenuModelInitializationEvent, menuSelectionEvent, i, modelContext, index0 ) );
									retargetableMenuModelInitializationEvent = null;
								}
							}
						}
						if( modelContext instanceof edu.cmu.cs.dennisc.croquet.OperationContext< ? > ) {
							edu.cmu.cs.dennisc.croquet.OperationContext< ? > childOperationContext = (edu.cmu.cs.dennisc.croquet.OperationContext< ? >)modelContext;
							appendBonusOperationNotes( rv, childOperationContext );
						}
					}
				}
			} else if( node instanceof edu.cmu.cs.dennisc.croquet.AbstractDialogOperationContext ){
				edu.cmu.cs.dennisc.croquet.HistoryNode lastChild = operationContext.getLastChild();
				if( lastChild instanceof edu.cmu.cs.dennisc.croquet.CommitEvent ) {
					rv.add( OperationNote.createInstance( operationContext ) );
					appendBonusOperationNotes( rv, operationContext );
				} else {
					appendNotes( rv, parentContextCriterion, lastChild );
				}
			}
		} else if( node instanceof edu.cmu.cs.dennisc.croquet.BooleanStateContext ) {
			edu.cmu.cs.dennisc.croquet.BooleanStateContext booleanStateContext = (edu.cmu.cs.dennisc.croquet.BooleanStateContext)node;
			rv.add( BooleanStateNote.createInstance( booleanStateContext ) );
		} else if( node instanceof edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< ? > ) {
			edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< ? > listSelectionStateContext = (edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< ? >)node;
			ModelFromContextResolver modelResolver = new ModelFromContextResolver( listSelectionStateContext );
			FirstComponentResolver firstComponentResolver = new FirstComponentResolver( modelResolver );
			edu.cmu.cs.dennisc.croquet.Component< ? > component = firstComponentResolver.getResolved();
			if( component instanceof edu.cmu.cs.dennisc.croquet.ItemSelectable< ?,? > ) {
				edu.cmu.cs.dennisc.croquet.ItemSelectable< ?,? > itemSelectable = (edu.cmu.cs.dennisc.croquet.ItemSelectable< ?,? >)component;
				System.err.println( "itemSelectable: " + itemSelectable );
				if( itemSelectable.isSingleStageSelectable() ) {
					rv.add( ListSelectionStateSimpleNote.createInstance( listSelectionStateContext ) );
				} else {
					rv.add( ListSelectionStateStartNote.createInstance( listSelectionStateContext ) );
					rv.add( ListSelectionStateFinishNote.createInstance( listSelectionStateContext ) );
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
		
		private java.util.List< HistoryNote > notes;
		private java.util.List< HistoryNote > createNotes() {
			java.util.List< HistoryNote > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			appendNotes( rv, IsRootContextCriterion.SINGLETON, this.context );
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
			if( child instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent ) {
				edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent)child;
				for( int i=0; i<NOTE_COUNT; i++ ) {
					HistoryNote historyNote = this.notes.get( i );
					if( historyNote instanceof MenuSelectionNote ) {
						MenuSelectionNote menuSelectionNote = (MenuSelectionNote)historyNote;
						if( menuSelectionNote.isAtLeastWhatWeveBeenWaitingFor( menuSelectionEvent ) ) {
							//pass
						} else {
							this.setActiveNote( i );
							break;
						}
					}
				}
			} else {
				int activeNoteIndex = this.getIndexOfFirstActiveNote();
				activeNoteIndex = Math.max( activeNoteIndex, 0 );
				if( activeNoteIndex < NOTE_COUNT ) {
					HistoryNote activeNote = this.notes.get( activeNoteIndex );
					if( activeNote.isWhatWeveBeenWaitingFor( child ) ) {
						activeNoteIndex ++;
						if( activeNoteIndex == NOTE_COUNT ) {
							return true;
						} else {
							this.setActiveNote( activeNoteIndex );
						}
					}
				}
			}
			return false;
		}
		@Override
		protected void complete() {
		}
	}

	/*package-private*/ AutomaticTutorialStencil getStencil() {
		return this.stencil;
	}
	public void addSteps( edu.cmu.cs.dennisc.croquet.RootContext sourceContext ) {
		this.addMessageStep( "start", "start of tutorial" );
		this.sourceContext = sourceContext;
		final int N = sourceContext.getChildCount();
		for( int i=0; i<N; i++ ) {
			edu.cmu.cs.dennisc.croquet.HistoryNode node = sourceContext.getChildAt( i );
			if( node instanceof edu.cmu.cs.dennisc.croquet.ModelContext< ? > ) {
				edu.cmu.cs.dennisc.croquet.ModelContext< ? > context = (edu.cmu.cs.dennisc.croquet.ModelContext< ? >)node;
				edu.cmu.cs.dennisc.croquet.HistoryNode.State state = context.getState();
				edu.cmu.cs.dennisc.croquet.Group group = context.getModel().getGroup();
				if( state == edu.cmu.cs.dennisc.croquet.HistoryNode.State.CANCELED ) {
					//pass
				} else {
					if( this.groupsForWhichStepsAreGenerated.contains( group ) ) {
						this.stencil.addStep( new ContextStep( context ) );
					}
				}
			}
		}
		this.addMessageStep( "end", "end of tutorial" );
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
