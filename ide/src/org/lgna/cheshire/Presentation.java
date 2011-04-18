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
package org.lgna.cheshire;

/**
 * @author Dennis Cosgrove
 */
public abstract class Presentation {
	private static edu.cmu.cs.dennisc.croquet.Group COMPLETION_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "d2f09b36-fb08-425d-825c-0075284e095b" ), "COMPLETION_GROUP" );

	private final edu.cmu.cs.dennisc.croquet.UserInformation userInformation;
	private final Book book;
	private boolean isResultOfNextOperation = false;

	private final org.lgna.croquet.steps.TransactionManager.Observer observer = new org.lgna.croquet.steps.TransactionManager.Observer() {
		public void addingStep( org.lgna.croquet.steps.Step< ? > step ) {
		}
		public void addedStep( org.lgna.croquet.steps.Step< ? > step ) {
			Presentation.this.handleEvent( new org.lgna.cheshire.events.StepAddedEvent( step ) );
		}
		public void pendingDrop( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
		}
		public void pendedDrop( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
			Presentation.this.handleEvent( new org.lgna.cheshire.events.DropPendedEvent( completionModel, dropReceptor, dropSite ) );
		}
		public void selectedMenuItems( java.util.List< edu.cmu.cs.dennisc.croquet.Model > models ) {
			Presentation.this.handleEvent( new org.lgna.cheshire.events.MenuSelectionChangedEvent( models ) );
		}
	};

	protected abstract void handleEvent( org.lgna.cheshire.events.Event event );
	private org.lgna.cheshire.Book.SelectionObserver selectionObserver = new org.lgna.cheshire.Book.SelectionObserver() {
		public void selectionChanging( Book source, int fromIndex, int toIndex ) {
		}
		public void selectionChanged( Book source, int fromIndex, int toIndex ) {
			Chapter chapter = source.getChapterAt( toIndex );
			Presentation.this.handleChapterChanged( chapter );
		}
	};

	private final edu.cmu.cs.dennisc.history.HistoryManager[] historyManagers;

	public Presentation( edu.cmu.cs.dennisc.croquet.UserInformation userInformation, ChapterAccessPolicy accessPolicy, org.lgna.croquet.steps.TransactionHistory originalTransactionHistory, Filterer filterer, edu.cmu.cs.dennisc.croquet.Group[] groupsTrackedForRandomAccess ) {
		this.userInformation = userInformation;
		this.book = this.generateDraft( accessPolicy, originalTransactionHistory );
		filterer.filter( this.book.listIterator(), userInformation );
		final int N = groupsTrackedForRandomAccess.length;
		this.historyManagers = new edu.cmu.cs.dennisc.history.HistoryManager[ N+1 ];
		for( int i=0; i<N; i++ ) {
			this.historyManagers[ i ] = edu.cmu.cs.dennisc.history.HistoryManager.getInstance( groupsTrackedForRandomAccess[ i ] );
		}
		this.historyManagers[ N ] = edu.cmu.cs.dennisc.history.HistoryManager.getInstance( COMPLETION_GROUP );
		org.lgna.croquet.steps.TransactionManager.addObserver( this.observer );
	}
	
	protected void startListening() {
		this.getBook().addSelectionObserver( this.selectionObserver );
	}
	protected void stopListening() {
		this.getBook().removeSelectionObserver( this.selectionObserver );
	}

	protected abstract Chapter createChapter( org.lgna.croquet.steps.Transaction transaction );
	private Book generateDraft( ChapterAccessPolicy accessPolicy, org.lgna.croquet.steps.TransactionHistory transactionHistory ) {
		Book rv = new Book();
		rv.setAccessPolicy( accessPolicy );
		for( org.lgna.croquet.steps.Transaction transaction : transactionHistory ) {
			rv.addChapter( this.createChapter( transaction ) );
		}
		return rv;
	}
	
	private void preserveHistoryIndices( int transactionIndex ) {
		Chapter chapter = this.book.getChapterAt( transactionIndex );
		final int N = historyManagers.length;
		int[] indices = new int[ N ];
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "preserveHistoryIndices", transactionIndex );
		for( int i=0; i<N; i++ ) {
			indices[ i ] = historyManagers[ i ].getInsertionIndex();
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( historyManagers[ i ], indices[ i ] );
		}
		chapter.setHistoryIndices( indices );
	}
	private void restoreHistoryIndices( int transactionIndex ) {
		Chapter chapter = this.book.getChapterAt( transactionIndex );
		final int N = historyManagers.length;
		int[] indices = chapter.getHistoryIndices();
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "restoreHistoryIndices", transactionIndex );
		for( int i=0; i<N; i++ ) {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( historyManagers[ i ], indices[ i ] );
			historyManagers[ i ].setInsertionIndex( indices[ i ] );
		}
	}

	/*package-private*/ static void complete( final edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		if( edit != null ) {
			 edu.cmu.cs.dennisc.croquet.ActionOperation bogusCompletionOperation = new edu.cmu.cs.dennisc.croquet.ActionOperation( COMPLETION_GROUP, java.util.UUID.fromString( "d4b1cb3b-f642-4c90-be92-e27d616f6922" ) ) {
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

	
	public void restoreHistoryIndicesDueToCancel() {
		this.restoreHistoryIndices( this.book.getSelectedIndex() );
	}
	
	private int prevSelectedIndex = -1;
	private void completeOrUndoIfNecessary() {
		SoundCache.pushIgnoreStartRequests();
		try {
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
							iChapter.complete();
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
	
	protected void handleChapterChanged(Chapter chapter) {
		this.completeOrUndoIfNecessary();
	}
	
	public edu.cmu.cs.dennisc.croquet.UserInformation getUserInformation() {
		return this.userInformation;
	}
	public Book getBook() {
		return this.book;
	}
	
	private edu.cmu.cs.dennisc.croquet.Retargeter retargeter;
	public edu.cmu.cs.dennisc.croquet.Retargeter getRetargeter() {
		return this.retargeter;
	}
	public void setRetargeter( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.retargeter = retargeter;
	}
	
	public void retargetAll( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.book.retargetAll( retargeter );
	}
	public void retargetForward() {
		this.book.retargetForward( this.retargeter );
	}
	
	protected abstract java.util.List< org.lgna.croquet.steps.Transaction > addTransactionsToGetIntoTheRightStateWhenNoViewControllerCanBeFound( java.util.List< org.lgna.croquet.steps.Transaction > rv );
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
			edu.cmu.cs.dennisc.croquet.Application.getSingleton().showMessageDialog( "end of tutorial" );
		}
	}
	public void decrementSelectedIndex() {
		this.book.decrementSelectedIndex();
	}
}
