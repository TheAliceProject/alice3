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
package org.lgna.cheshire.simple;

import org.lgna.cheshire.Filterer;
import org.lgna.cheshire.Recoverer;

/**
 * @author Dennis Cosgrove
 */
public abstract class Presentation extends org.lgna.croquet.BooleanState {
	public static org.lgna.croquet.Group COMPLETION_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "d2f09b36-fb08-425d-825c-0075284e095b" ), "COMPLETION_GROUP" );
	public static org.lgna.croquet.Group PRESENTATION_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "e582737d-b56b-4105-93d2-581853e193e2" ), "PRESENTATION_GROUP" );

	private Recoverer recoverer;
	private Book book;
	private boolean isResultOfNextOperation = false;
	private org.lgna.croquet.undo.UndoHistory[] historyManagers;
	private org.lgna.croquet.Retargeter retargeter;
	protected org.lgna.croquet.history.TransactionHistory originalTransactionHistory;

	private final org.lgna.croquet.history.event.Listener listener = new org.lgna.croquet.history.event.Listener() {
		public void changing( org.lgna.croquet.history.event.Event<?> e ) {
		}
		public void changed( org.lgna.croquet.history.event.Event<?> e ) {
			Presentation.this.handleEvent( e );
		}
	};

	private org.lgna.cheshire.simple.Book.SelectionObserver selectionObserver = new org.lgna.cheshire.simple.Book.SelectionObserver() {
		public void selectionChanging( Book source, int fromIndex, int toIndex ) {
		}
		public void selectionChanged( Book source, int fromIndex, int toIndex ) {
			Chapter chapter = source.getChapterAt( toIndex );
			Presentation.this.handleChapterChanged( chapter );
		}
	};

	public Presentation( ) {
		super( org.lgna.cheshire.simple.Presentation.PRESENTATION_GROUP, java.util.UUID.fromString( "1303fdcf-6ba4-4933-9754-5b7933f8c01f" ), false );
		this.addValueListener( new org.lgna.croquet.State.ValueListener<Boolean>() {
			public void changing(org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting) {
			}
			public void changed(org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting) {
				Presentation.this.handleStateChange( state.getValue() );
			}
		} );
	}

	public void initializePresentation( ChapterAccessPolicy accessPolicy, org.lgna.croquet.history.TransactionHistory originalTransactionHistory, org.lgna.croquet.migration.MigrationManager migrationManager, Filterer filterer, Recoverer recoverer, org.lgna.croquet.Group[] groupsTrackedForRandomAccess ) {
		this.validateTransactionHistory( originalTransactionHistory );
		this.originalTransactionHistory = originalTransactionHistory;

		this.recoverer = recoverer;
		this.book = this.generateDraft( accessPolicy, originalTransactionHistory );
		if ( filterer != null ) {
			filterer.filter( this.book.listIterator() );
		}
		this.book.setSelectedIndex(0);

		final int N = groupsTrackedForRandomAccess.length;
		this.historyManagers = new org.lgna.croquet.undo.UndoHistory[ N+1 ];
		for( int i=0; i<N; i++ ) {
			this.historyManagers[ i ] = org.alice.ide.IDE.getActiveInstance().getProjectHistory( groupsTrackedForRandomAccess[ i ] );
		}
		this.historyManagers[ N ] = org.alice.ide.IDE.getActiveInstance().getProjectHistory( COMPLETION_GROUP );

		org.alice.ide.IDE.getActiveInstance().getProjectTransactionHistory().addListener( this.listener );
	}

	protected abstract void handleTransactionCanceled( org.lgna.croquet.history.Transaction transaction );
	protected abstract void handleEvent( org.lgna.croquet.history.event.Event<?> event );

	protected void startListening() {
		this.getBook().addSelectionObserver( this.selectionObserver );
	}
	protected void stopListening() {
		this.getBook().removeSelectionObserver( this.selectionObserver );
	}

	public Recoverer getRecoverer() {
		return this.recoverer;
	}

	protected abstract Chapter createChapter( org.lgna.croquet.history.Transaction transaction );

	private Book generateDraft( ChapterAccessPolicy accessPolicy, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		Book rv = new Book();
		rv.setAccessPolicy( accessPolicy );
		for( org.lgna.croquet.history.Transaction transaction : transactionHistory ) {
			rv.addChapter( this.createChapter( transaction ) );
		}
		return rv;
	}

	private void preserveHistoryIndices( int transactionIndex ) {
		Chapter chapter = this.book.getChapterAt( transactionIndex );
		final int N = historyManagers.length;
		int[] indices = new int[ N ];
		for( int i=0; i<N; i++ ) {
			indices[ i ] = historyManagers[ i ].getInsertionIndex();
		}
		chapter.setHistoryIndices( indices );
	}
	private void restoreHistoryIndices( int transactionIndex ) {
		Chapter chapter = this.book.getChapterAt( transactionIndex );
		final int N = historyManagers.length;
		int[] indices = chapter.getHistoryIndices();
		for( int i=0; i<N; i++ ) {
			historyManagers[ i ].setInsertionIndex( indices[ i ] );
		}
	}

	public void restoreHistoryIndicesDueToCancel() {
		this.restoreHistoryIndices( this.book.getSelectedIndex() );
	}

	private int prevSelectedIndex = -1;
	private void completeOrUndoIfNecessary() {
		int nextSelectedIndex = this.book.getSelectedIndex();
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
						Chapter iChapter = this.book.getChapterAt( i );
						iChapter.complete( COMPLETION_GROUP );
					}
				}
				i++;
			}
		}
		this.prevSelectedIndex = nextSelectedIndex;
	}

	protected void handleChapterChanged(Chapter chapter) {
		this.completeOrUndoIfNecessary();
	}

	public Book getBook() {
		return this.book;
	}

	protected void validateTransactionHistory( org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		java.util.ListIterator< org.lgna.croquet.history.Transaction > transactionListIterator = transactionHistory.listIterator();
		while( transactionListIterator.hasNext() ) {
			org.lgna.croquet.history.Transaction transaction = transactionListIterator.next();
			if( transaction.isValid() ) {
				//pass
			} else {

			}
		}
	}

	private org.lgna.croquet.CompletionModel huntForInMenus( java.util.List< org.lgna.croquet.MenuItemPrepModel > list, org.lgna.croquet.MenuItemPrepModel menuModel, org.lgna.croquet.CompletionModel model ) {
		if( menuModel instanceof org.lgna.croquet.PredeterminedMenuModel ) {
			org.lgna.croquet.PredeterminedMenuModel defaultMenuModel = (org.lgna.croquet.PredeterminedMenuModel)menuModel;
			for( org.lgna.croquet.Model child : defaultMenuModel.getModels() ) {
				if( child instanceof org.lgna.croquet.MenuModel ) {
					org.lgna.croquet.MenuModel childMenuModel = (org.lgna.croquet.MenuModel)child;
					org.lgna.croquet.CompletionModel rv = this.huntForInMenus( list, childMenuModel, model );
					if( rv != null ) {
						list.add( 0, childMenuModel );
					}
					return rv;
				}
				if( child == model ) {
					//list.add( 0, model );
					return model;
				}
			}
		}
		return null;
	}
	protected java.util.List< org.lgna.croquet.MenuItemPrepModel > huntForInMenus( org.lgna.croquet.CompletionModel model ) {
		org.lgna.croquet.MenuBarComposite menuBarComposite = org.lgna.croquet.Application.getActiveInstance().getFrame().getMenuBarComposite();
		if( menuBarComposite != null ) {
			java.util.List< org.lgna.croquet.MenuItemPrepModel > rv = edu.cmu.cs.dennisc.java.util.Collections.newStack();
			for( org.lgna.croquet.MenuItemPrepModel child : menuBarComposite.getChildren() ) {
				org.lgna.croquet.Model found = this.huntForInMenus( rv, child, model );
				if( found != null ) {
					rv.add( 0, child );
					//rv.add( 0, menuBarModel );
					return rv;
				}
			}
		}
		return null;
	}

	protected org.lgna.croquet.history.Transaction createTabSelectionRecoveryTransactionIfAppropriate( org.lgna.croquet.history.Transaction transaction ) {
		org.lgna.croquet.CompletionModel model = transaction.getCompletionStep().getModel();
		for( org.lgna.croquet.TabSelectionState< org.lgna.croquet.TabComposite > tabSelectionState : org.lgna.croquet.Manager.getRegisteredModels( org.lgna.croquet.TabSelectionState.class ) ) {
			for( org.lgna.croquet.TabComposite item : tabSelectionState ) {
				if( tabSelectionState.getValue() == item ) {
					//pass
				} else {
					if( item.contains( model ) ) {
						return this.createRecoveryTransaction( transaction.getOwner(), tabSelectionState, tabSelectionState.getValue(), item );
					}
				}
			}
		}
		return null;
	}

	private <E> org.lgna.croquet.history.Transaction createRecoveryTransaction( org.lgna.croquet.history.TransactionHistory transactionHistory, org.lgna.croquet.State< E > state, E prevValue, E nextValue ) {
		org.lgna.croquet.history.Transaction rv = new org.lgna.croquet.history.Transaction( transactionHistory );
		org.lgna.croquet.history.StateChangeStep< E > completionStep = org.lgna.croquet.history.StateChangeStep.createAndAddToTransaction( rv, state, org.lgna.croquet.triggers.ChangeEventTrigger.createRecoveryInstance() );
		org.lgna.croquet.edits.StateEdit< E > edit = new org.lgna.croquet.edits.StateEdit<E>( completionStep, prevValue, nextValue );
		completionStep.setEdit( edit );
		return rv;
	}

	public org.lgna.croquet.Retargeter getRetargeter() {
		assert this.retargeter != null;
		return this.retargeter;
	}
	public void setRetargeter( org.lgna.croquet.Retargeter retargeter ) {
		this.retargeter = retargeter;
	}

	public void retargetAll( org.lgna.croquet.Retargeter retargeter ) {
		this.book.retargetAll( retargeter );
	}
	public void retargetForward() {
		if( this.book != null ) {
			this.book.retargetForward( this.retargeter );
		}
	}

	public void setSelectedIndex( int index ) {
		this.book.setSelectedIndex( index );
	}

	public void incrementSelectedIndex() {
		if( this.book.getSelectedIndex() < this.book.getChapterCount() - 1 ) {
			try {
				this.isResultOfNextOperation = true;
				this.book.incrementSelectedIndex();
			} finally {
				this.isResultOfNextOperation = false;
			}
		} else {
			org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "end of tutorial" );
		}
	}
	public void decrementSelectedIndex() {
		this.book.decrementSelectedIndex();
	}

	public void showStencilsPresentation() {
		this.setValue(true);
	}

	public void hideStencilsPresentation() {
		this.setValue(false);
	}

	protected abstract void handleStateChange( boolean isVisible );
}
