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
public class Book {
	public static interface SelectionObserver {
		public void selectionChanging( Book source, int fromIndex, int toIndex );
		public void selectionChanged( Book source, int fromIndex, int toIndex );
	}

	private final java.util.List< Chapter > chapters = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private int selectedIndex = -1;
	private int furthestCompletedIndex = -1;
	private ChapterAccessPolicy accessPolicy = ChapterAccessPolicy.ALLOW_ACCESS_UP_TO_AND_INCLUDING_FURTHEST_COMPLETED_CHAPTER;
	private java.util.List< SelectionObserver > selectionObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	public Book() {
	}
	public ChapterAccessPolicy getAccessPolicy() {
		return this.accessPolicy;
	}
	public void setAccessPolicy( ChapterAccessPolicy transactionAccessPolicy ) {
		this.accessPolicy = transactionAccessPolicy;
	}

	public void addSelectionObserver( SelectionObserver selectionObserver ) {
		this.selectionObservers.add( selectionObserver );
	}
	public void removeSelectionObserver( SelectionObserver selectionObserver ) {
		this.selectionObservers.add( selectionObserver );
	}
	public boolean isIndexAccessible( int nextIndex ) {
		return this.accessPolicy.isIndexAccessible( nextIndex, this.furthestCompletedIndex );
	}
	
	public java.util.ListIterator< Chapter > listIterator() {
		return this.chapters.listIterator();
	}
	public Chapter getChapterAt( int index ) {
		return this.chapters.get( index );
	}
	public int getChapterCount() {
		return this.chapters.size();
	}

	public int getSelectedIndex() {
		return this.selectedIndex;
	}
	public void setSelectedIndex( int nextSelectedIndex ) {
		int prevSelectedIndex = this.selectedIndex;
		if( this.selectedIndex != nextSelectedIndex ) {
			for( SelectionObserver selectionObserver : this.selectionObservers ) {
				selectionObserver.selectionChanging( this, prevSelectedIndex, nextSelectedIndex );
			}
			this.selectedIndex = nextSelectedIndex;
			for( SelectionObserver selectionObserver : this.selectionObservers ) {
				selectionObserver.selectionChanged( this, prevSelectedIndex, nextSelectedIndex );
			}
		}
		this.furthestCompletedIndex = Math.max( this.furthestCompletedIndex, nextSelectedIndex );
	}

	/*package-private*/void decrementSelectedIndex() {
		this.setSelectedIndex( this.selectedIndex - 1 );
	}

	/*package-private*/void incrementSelectedIndex() {
		this.setSelectedIndex( this.selectedIndex + 1 );
	}

	public Chapter getSelectedChapter() {
		if( this.selectedIndex >= 0 ) {
			return this.getChapterAt( this.selectedIndex );
		} else {
			return null;
		}
	}
	public void setSelectedChapter( Chapter item ) {
		int prevSelectedIndex = this.selectedIndex;
		int nextSelectedIndex = -1;
		final int N = this.getChapterCount();
		for( int i = 0; i < N; i++ ) {
			if( this.getChapterAt( i ) == item ) {
				nextSelectedIndex = i;
				break;
			}
		}

		if( this.accessPolicy.isIndexAccessible( nextSelectedIndex, this.furthestCompletedIndex ) || nextSelectedIndex < prevSelectedIndex ) {
			this.setSelectedIndex( nextSelectedIndex );
		}
	}

	/*package-private*/ void handleEditCommitted( edu.cmu.cs.dennisc.croquet.Edit< ? > replacementCandidate, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		Chapter chapter = this.getSelectedChapter();
		if( chapter instanceof TransactionChapter ) {
			TransactionChapter transactionChapter = (TransactionChapter)chapter;
			org.lgna.croquet.steps.Transaction transaction = transactionChapter.getTransaction();
			edu.cmu.cs.dennisc.croquet.Edit< ? > originalEdit = transaction.getEdit();
			if( originalEdit != null ) {
				edu.cmu.cs.dennisc.croquet.ReplacementAcceptability replacementAcceptability = originalEdit.getReplacementAcceptability( replacementCandidate, userInformation );
				if( replacementAcceptability.isAcceptable() ) {
					transactionChapter.setReplacementAcceptability( replacementAcceptability );
					edu.cmu.cs.dennisc.croquet.Retargeter retargeter = org.lgna.cheshire.stencil.Presentation.getInstance().getRetargeter();
					originalEdit.addKeyValuePairs( retargeter, replacementCandidate );
					this.retargetForward( retargeter );
				}
			} else {
				System.err.println( "originalEdit is null.  original canceled?" );
			}
		} else if ( chapter instanceof MessageChapter ) {
			assert replacementCandidate == null : replacementCandidate;
		} else {
			assert chapter == null : chapter;
		}
	}

	private void retarget( int fromIndex, edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		final int N = this.getChapterCount();
		for( int i = this.selectedIndex + 1; i < N; i++ ) {
			this.getChapterAt( i ).retarget( retargeter );
		}
	}
	/*package-private*/void retargetAll( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.retarget( 0, retargeter );
	}
	/*package-private*/void retargetForward( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.retarget( this.selectedIndex + 1, retargeter );
	}

	/*package-private*/void addChapter( Chapter item ) {
		this.chapters.add( item );
	}
	/*package-private*/void addChapterAtSelectedIndex( Chapter item ) {
		this.chapters.add( this.selectedIndex, item );
	}
}
