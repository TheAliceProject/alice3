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

/*package-private*/ class ModelFromContextResolver< M extends edu.cmu.cs.dennisc.croquet.Model > implements edu.cmu.cs.dennisc.croquet.RuntimeResolver< M > {
	private edu.cmu.cs.dennisc.croquet.ModelContext< M > modelContext;
	public ModelFromContextResolver( edu.cmu.cs.dennisc.croquet.ModelContext< M > modelContext ) {
		assert modelContext != null;
		this.modelContext = modelContext;
	}
	public M getResolved() {
		return this.modelContext.getModel();
	}
}
/*package-private*/ class ModelFromMenuSelectionResolver< M extends edu.cmu.cs.dennisc.croquet.Model > implements edu.cmu.cs.dennisc.croquet.RuntimeResolver< M > {
	private edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent;
	private int index;
	public ModelFromMenuSelectionResolver( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent, int index ) {
		this.menuSelectionEvent = menuSelectionEvent;
		this.index = index;
	}
	public M getResolved() {
		return this.menuSelectionEvent.getModelAt( this.index );
	}
}
/*package-private*/ class DropSiteResolver implements edu.cmu.cs.dennisc.croquet.RuntimeResolver< edu.cmu.cs.dennisc.croquet.TrackableShape > {
	private edu.cmu.cs.dennisc.croquet.DragAndDropContext.EnteredPotentialDropSiteEvent enteredPotentialDropSiteEvent;
	public DropSiteResolver( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext ) {
		this.enteredPotentialDropSiteEvent = dragAndDropContext.getLastChildAssignableTo( edu.cmu.cs.dennisc.croquet.DragAndDropContext.EnteredPotentialDropSiteEvent.class );
	}
	public edu.cmu.cs.dennisc.croquet.TrackableShape getResolved() {
		if( this.enteredPotentialDropSiteEvent != null ) {
			edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor = this.enteredPotentialDropSiteEvent.getDropReceptor();
			if( dropReceptor != null ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( this.enteredPotentialDropSiteEvent.getPotentialDropSite() );
				return dropReceptor.getTrackableShape( this.enteredPotentialDropSiteEvent.getPotentialDropSite() );
			}
		}
		return null;
	}
}
/*package-private*/ class ItemResolver<E> implements edu.cmu.cs.dennisc.croquet.RuntimeResolver< E > {
	private edu.cmu.cs.dennisc.croquet.ListSelectionStateEdit< E > listSelectionStateEdit;
	public ItemResolver( edu.cmu.cs.dennisc.croquet.ListSelectionStateEdit< E > listSelectionStateEdit ) {
		this.listSelectionStateEdit = listSelectionStateEdit;
	}
	public E getResolved() {
		return this.listSelectionStateEdit.getNextValue();
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
	
	private static boolean isMouseEventInterceptedInAllCases( java.awt.event.MouseEvent e ) {
		return e.getID() == java.awt.event.MouseEvent.MOUSE_PRESSED || e.getID() == java.awt.event.MouseEvent.MOUSE_RELEASED || e.getID() == java.awt.event.MouseEvent.MOUSE_CLICKED || e.getID() == java.awt.event.MouseEvent.MOUSE_DRAGGED;
	}

	private void addMessageStep( String title, String text ) {
		this.stencil.addStep( new MessageStep( title, text ) );
	}
	private static abstract class HistoryNote extends Note {
		public HistoryNote( String text ) {
			super( text );
		}
		public boolean isEventInterceptable( java.awt.event.MouseEvent e ) {
			return true;
		}
		public abstract boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child );
	}
	
	private static class DragNote extends HistoryNote {
		public static DragNote createInstance( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext ) {
			return new DragNote( dragAndDropContext );
		}
		private DragNote( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext ) {
			super( dragAndDropContext.getModel().getTutorialDragNoteText() );
			ModelFromContextResolver modelResolver = new ModelFromContextResolver( dragAndDropContext );
			FirstComponentResolver firstComponentResolver = new FirstComponentResolver( modelResolver );
			DropSiteResolver dropSiteResolver = new DropSiteResolver( dragAndDropContext ); 
			this.addFeature( new Hole( firstComponentResolver, Feature.ConnectionPreference.EAST_WEST ) );			
		}
		@Override
		public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
			return child instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext;
		}
	}
	private static class DropNote extends HistoryNote {
		public static DropNote createInstance( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext ) {
			return new DropNote( dragAndDropContext );
		}
		private DropNote( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext ) {
			super( dragAndDropContext.getModel().getTutorialDropNoteText() );
			DropSiteResolver dropSiteResolver = new DropSiteResolver( dragAndDropContext ); 
			this.addFeature( new Hole( dropSiteResolver, Feature.ConnectionPreference.EAST_WEST ) );			
		}
		@Override
		public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
			return child instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext.DroppedEvent;
		}
	}

	private static abstract class WaitingOnCommitHistoryNote extends HistoryNote {
		private edu.cmu.cs.dennisc.croquet.Edit< ? > edit;
		public WaitingOnCommitHistoryNote( String text, edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
			super( text );
			this.edit = edit;
		}
		@Override
		public final boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
			boolean rv = false;
			if( child instanceof edu.cmu.cs.dennisc.croquet.CommitEvent ) {
				edu.cmu.cs.dennisc.croquet.CommitEvent commitEvent = (edu.cmu.cs.dennisc.croquet.CommitEvent)child;
				edu.cmu.cs.dennisc.croquet.Edit< ? > replacementEdit = commitEvent.getEdit();
				if( this.edit.isReplacementAcceptable( replacementEdit ) ) {
					edu.cmu.cs.dennisc.croquet.Retargeter retargeter = AutomaticTutorial.getInstance().getRetargeter();
					this.edit.addKeyValuePairs( retargeter, replacementEdit );
					AutomaticTutorial.getInstance().sourceContext.retarget( retargeter );
					rv = true;
				}
			}
			return rv;
		}
		
	}
	
	private static class OperationNote extends WaitingOnCommitHistoryNote {
		public static OperationNote createInstance( edu.cmu.cs.dennisc.croquet.OperationContext operationContext ) {
			return new OperationNote( operationContext, operationContext.getEdit() );
		}
		private OperationNote( edu.cmu.cs.dennisc.croquet.OperationContext<?> operationContext, edu.cmu.cs.dennisc.croquet.Edit<?> operationEdit ) {
			super( "todo", /*operationContext.getModel().getTutorialNoteText( operationEdit ),*/ operationEdit );
			ModelFromContextResolver modelResolver = new ModelFromContextResolver( operationContext );
			FirstComponentResolver firstComponentResolver = new FirstComponentResolver( modelResolver );
			this.addFeature( new Hole( firstComponentResolver, Feature.ConnectionPreference.EAST_WEST ) );			
		}
	}

	private static class BooleanStateNote extends WaitingOnCommitHistoryNote {
		public static BooleanStateNote createInstance( edu.cmu.cs.dennisc.croquet.BooleanStateContext booleanStateContext ) {
			edu.cmu.cs.dennisc.croquet.BooleanStateEdit booleanStateEdit = (edu.cmu.cs.dennisc.croquet.BooleanStateEdit)booleanStateContext.getEdit();
			return new BooleanStateNote( booleanStateContext, booleanStateEdit );
		}
		private BooleanStateNote( edu.cmu.cs.dennisc.croquet.BooleanStateContext booleanStateContext, edu.cmu.cs.dennisc.croquet.BooleanStateEdit booleanStateEdit ) {
			super( booleanStateContext.getModel().getTutorialNoteText( booleanStateEdit ), booleanStateEdit );
			ModelFromContextResolver modelResolver = new ModelFromContextResolver( booleanStateContext );
			FirstComponentResolver firstComponentResolver = new FirstComponentResolver( modelResolver );
			this.addFeature( new Hole( firstComponentResolver, Feature.ConnectionPreference.EAST_WEST ) );			
		}
	}

	private static class ListSelectionStateSimpleNote<E> extends WaitingOnCommitHistoryNote {
		public static <E> ListSelectionStateSimpleNote<E> createInstance( edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< E > listSelectionStateContext ) {
			edu.cmu.cs.dennisc.croquet.ListSelectionStateEdit<E> listSelectionStateEdit = (edu.cmu.cs.dennisc.croquet.ListSelectionStateEdit< E >)listSelectionStateContext.getEdit();
			return new ListSelectionStateSimpleNote( listSelectionStateContext, listSelectionStateEdit );
		}
		private ListSelectionStateSimpleNote( edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< E > listSelectionStateContext, edu.cmu.cs.dennisc.croquet.ListSelectionStateEdit<E> listSelectionStateEdit ) {
			super( listSelectionStateContext.getModel().getTutorialNoteText( listSelectionStateEdit ), listSelectionStateEdit );
			ModelFromContextResolver modelResolver = new ModelFromContextResolver( listSelectionStateContext );
			ItemSelectionStateItemResolver itemSelectionStateItemResolver = new ItemSelectionStateItemResolver( modelResolver, new ItemResolver( listSelectionStateEdit ) );
			this.addFeature( new Hole( itemSelectionStateItemResolver, Feature.ConnectionPreference.EAST_WEST ) );			
		}
	}

	private static class ListSelectionStateStartNote<E> extends HistoryNote {
		public static <E> ListSelectionStateStartNote<E> createInstance( edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< E > listSelectionStateContext ) {
			edu.cmu.cs.dennisc.croquet.ListSelectionStateEdit<E> listSelectionStateEdit = (edu.cmu.cs.dennisc.croquet.ListSelectionStateEdit< E >)listSelectionStateContext.getEdit();
			return new ListSelectionStateStartNote( listSelectionStateContext, listSelectionStateEdit );
		}
		private ListSelectionStateStartNote( edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< E > listSelectionStateContext, edu.cmu.cs.dennisc.croquet.ListSelectionStateEdit<E> listSelectionStateEdit ) {
			super( listSelectionStateContext.getModel().getTutorialNoteStartText( listSelectionStateEdit ) );
			ModelFromContextResolver modelResolver = new ModelFromContextResolver( listSelectionStateContext );
			FirstComponentResolver firstComponentResolver = new FirstComponentResolver( modelResolver );
			this.addFeature( new Hole( firstComponentResolver, Feature.ConnectionPreference.EAST_WEST ) );			
		}
		@Override
		public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
			return child instanceof edu.cmu.cs.dennisc.croquet.ListSelectionStateContext.PopupMenuWillBecomeVisibleEvent;
		}
	}
	private static class ListSelectionStateFinishNote<E> extends WaitingOnCommitHistoryNote {
		public static <E> ListSelectionStateFinishNote<E> createInstance( edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< E > listSelectionStateContext ) {
			edu.cmu.cs.dennisc.croquet.ListSelectionStateEdit<E> listSelectionStateEdit = (edu.cmu.cs.dennisc.croquet.ListSelectionStateEdit< E >)listSelectionStateContext.getEdit();
			return new ListSelectionStateFinishNote( listSelectionStateContext, listSelectionStateEdit );
		}
		private ListSelectionStateFinishNote( edu.cmu.cs.dennisc.croquet.ListSelectionStateContext< E > listSelectionStateContext, edu.cmu.cs.dennisc.croquet.ListSelectionStateEdit<E> listSelectionStateEdit ) {
			super( listSelectionStateContext.getModel().getTutorialNoteFinishText( listSelectionStateEdit ), listSelectionStateEdit );
			ModelFromContextResolver modelResolver = new ModelFromContextResolver( listSelectionStateContext );
			ItemSelectionStateItemResolver itemSelectionStateItemResolver = new ItemSelectionStateItemResolver( modelResolver, new ItemResolver( listSelectionStateEdit ) );
			//FirstComponentResolver firstComponentResolver = new FirstComponentResolver( modelResolver );
			//this.addFeature( new Hole( firstComponentResolver, Feature.ConnectionPreference.EAST_WEST, false ) );			
			this.addFeature( new Hole( itemSelectionStateItemResolver, Feature.ConnectionPreference.EAST_WEST ) {
				@Override
				protected edu.cmu.cs.dennisc.croquet.TrackableShape getTrackableShape() {
					return this.getTrackableShapeResolver().getResolved();
//					this.unbind();
//					this.bind();
//					return super.getTrackableShape();
////					edu.cmu.cs.dennisc.croquet.TrackableShape rv = super.getTrackableShape();
////					if( rv != null ) {
////						//pass
////					} else {
////						this.unbind();
////						this.bind();
////						rv = super.getTrackableShape();
////					}
////					return rv;
				}
			} );			
		}
		@Override
		public boolean isEventInterceptable( java.awt.event.MouseEvent e ) {
			return isMouseEventInterceptedInAllCases( e );
		}
	}
	private static class MenuSelectionNote extends HistoryNote {
		private static String getText( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent, int i ) {
			edu.cmu.cs.dennisc.croquet.Model modelI = menuSelectionEvent.getModelAt( i );
			StringBuilder sb = new StringBuilder();
			sb.append( "select <strong><em>" );
			if( modelI instanceof edu.cmu.cs.dennisc.croquet.MenuModel ) {
				sb.append( ((edu.cmu.cs.dennisc.croquet.MenuModel)modelI).getTutorialNoteText() );
			} else if( modelI instanceof edu.cmu.cs.dennisc.croquet.Operation< ? > ){
				sb.append( ((edu.cmu.cs.dennisc.croquet.Operation< ? >)modelI).getTutorialNoteText() );
			}
			sb.append( "</em></strong>" );
			return sb.toString();
		}
		public static MenuSelectionNote createInstance( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent retargetableMenuModelInitializationEvent, edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent, int i, edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext, int index0 ) {
			return new MenuSelectionNote( retargetableMenuModelInitializationEvent, menuSelectionEvent, i, modelContext, index0 );
		}
		
		
		edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent originalRetargetableMenuModelInitializationEvent;
		private edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent originalMenuSelectionEvent;
		private int i;
		private edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext;

		private static FirstComponentResolver createComponentResolver( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent, int index ) {
			return new FirstComponentResolver( new ModelFromMenuSelectionResolver< edu.cmu.cs.dennisc.croquet.Model >( menuSelectionEvent, index ) );
		}
		
		private MenuSelectionNote( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent retargetableMenuModelInitializationEvent, edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent, int i, edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext, int index0 ) {
			super( getText( menuSelectionEvent, i ) );
			this.originalRetargetableMenuModelInitializationEvent = retargetableMenuModelInitializationEvent;
			this.originalMenuSelectionEvent = menuSelectionEvent;
			this.modelContext = modelContext;
			this.i = i;

			boolean isBelowStencil = AutomaticTutorial.getInstance().getStencil().getMenuPolicy().isBelowStencil();
			boolean isInMenuBar = index0 == 1;
			if( isBelowStencil ) {
				for( int j=index0; j<=i; j++ ) {
					this.addFeature( new MenuHole( createComponentResolver( menuSelectionEvent, j ), Feature.ConnectionPreference.EAST_WEST, j==i, true, false ) );
				}
			} else {
				boolean isCheckMarkRenderingDesired = AutomaticTutorial.getInstance().getStencil().getMenuPolicy().isFeedbackDesired();
				if( isInMenuBar ) {
					this.addFeature( new MenuHole( createComponentResolver( menuSelectionEvent, index0 ), Feature.ConnectionPreference.EAST_WEST, this.i == index0, true, false ) );
				}
				if( isInMenuBar==false || this.i > index0 ) {
					this.addFeature( new MenuHole( createComponentResolver( menuSelectionEvent, this.i ), Feature.ConnectionPreference.EAST_WEST, true, false, isCheckMarkRenderingDesired ) );
				}
			}
		}
		
		@Override
		public boolean isEventInterceptable( java.awt.event.MouseEvent e ) {
			return isMouseEventInterceptedInAllCases( e );
		}

		public boolean isAtLeastWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent ) {
			final int N = this.i+1;
			if( menuSelectionEvent.getModelCount() >= N ) {
				for( int i=0; i<N; i++ ) {
					edu.cmu.cs.dennisc.croquet.Model orginalModelI = this.originalMenuSelectionEvent.getModelAt( i );
					edu.cmu.cs.dennisc.croquet.Model replacementModelI = menuSelectionEvent.getModelAt( i );
					if( orginalModelI == replacementModelI ) {
						//pass
					} else {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		
		
		@Override
		public boolean isWhatWeveBeenWaitingFor( edu.cmu.cs.dennisc.croquet.HistoryNode child ) {
			if( child instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent ) {
				if( this.originalRetargetableMenuModelInitializationEvent != null ) {
					edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent retargetableMenuModelInitializationEvent = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.RetargetableMenuModelInitializationEvent)child;
					edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext popupMenuOperationContext = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext)retargetableMenuModelInitializationEvent.getParent();
					edu.cmu.cs.dennisc.croquet.MenuModel menuModel = popupMenuOperationContext.getModel().getMenuModel();
					if( menuModel instanceof edu.cmu.cs.dennisc.croquet.RetargetableMenuModel ) {
						edu.cmu.cs.dennisc.croquet.RetargetableMenuModel retargetableMenuModel = (edu.cmu.cs.dennisc.croquet.RetargetableMenuModel)menuModel;
						edu.cmu.cs.dennisc.croquet.RetargetingData originalRetargetingData = this.originalRetargetableMenuModelInitializationEvent.getRetargetingData();
						edu.cmu.cs.dennisc.croquet.Retargeter retargeter = AutomaticTutorial.getInstance().getRetargeter();
						originalRetargetingData.addKeyValuePairs( retargeter, retargetableMenuModel.getRetargetableData() );
						AutomaticTutorial.getInstance().sourceContext.retarget( retargeter );
					}
				}
			} else if ( child instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent ) {
				edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent menuSelectionEvent = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext.MenuSelectionEvent)child;
				final int N = this.i;
				if( menuSelectionEvent.getModelCount() == N ) {
					return this.isAtLeastWhatWeveBeenWaitingFor( menuSelectionEvent );
				}
			} else {
				if( this.originalMenuSelectionEvent.getModelCount() == this.i+1 ) {
					if( child instanceof edu.cmu.cs.dennisc.croquet.CommitEvent ) {
						return true;
					}
				}
			}
			return false;
		}
	}
	
	private static java.util.List< HistoryNote > appendNotes( java.util.List< HistoryNote > rv, edu.cmu.cs.dennisc.croquet.HistoryNode node ) {
		if( node instanceof edu.cmu.cs.dennisc.croquet.MenuBarModelContext ) {
			edu.cmu.cs.dennisc.croquet.MenuBarModelContext menuBarModelContext = (edu.cmu.cs.dennisc.croquet.MenuBarModelContext)node;
			edu.cmu.cs.dennisc.croquet.HistoryNode lastChild = menuBarModelContext.getLastChild();
			if( lastChild instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext ) {
				edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext popupMenuOperationContext = (edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext)lastChild;
				appendNotes( rv, popupMenuOperationContext );
			}
		} else if( node instanceof edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext ) {
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
				}
			}
		} else if( node instanceof edu.cmu.cs.dennisc.croquet.DragAndDropContext ) {
			edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext = (edu.cmu.cs.dennisc.croquet.DragAndDropContext)node;
			int DND_CONTEXT_CHILD_COUNT = dragAndDropContext.getChildCount();
			if( DND_CONTEXT_CHILD_COUNT > 1 ) {
				edu.cmu.cs.dennisc.croquet.HistoryNode lastChild = dragAndDropContext.getChildAt( DND_CONTEXT_CHILD_COUNT-1 );
				if( lastChild instanceof edu.cmu.cs.dennisc.croquet.ModelContext< ? > ) {
					edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext = (edu.cmu.cs.dennisc.croquet.ModelContext< ? >)lastChild;
					rv.add( DragNote.createInstance( dragAndDropContext ) );
					rv.add( DropNote.createInstance( dragAndDropContext ) );
					appendNotes( rv, modelContext );
				}
			}
		} else if( node instanceof edu.cmu.cs.dennisc.croquet.OperationContext ) {
			edu.cmu.cs.dennisc.croquet.OperationContext operationContext = (edu.cmu.cs.dennisc.croquet.OperationContext)node;
			rv.add( OperationNote.createInstance( operationContext ) );
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
			appendNotes( rv, this.context );
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
		void setTutorialStencil( edu.cmu.cs.dennisc.tutorial.TutorialStencil tutorialStencil ) {
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
