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

package org.lgna.cheshire.docwizardsesque;

/**
 * @author Dennis Cosgrove
 */
public class BookTreeModel extends edu.cmu.cs.dennisc.javax.swing.models.AbstractMutableTreeModel< Object > {
	private final org.lgna.cheshire.Book book;
	public BookTreeModel( org.lgna.cheshire.Book book ) {
		this.book = book;
	}
	public Object getRoot() {
		return this.book;
	}
	public boolean isLeaf( Object node ) {
		return node instanceof org.lgna.croquet.history.Step< ? >;
	}
	public int getIndexOfChild( Object parent, Object child ) {
		if( parent instanceof org.lgna.cheshire.Book ) {
			org.lgna.cheshire.Book book = (org.lgna.cheshire.Book)parent;
			return book.getChapterCount();
		} else if( parent instanceof org.lgna.cheshire.Chapter ) {
			org.lgna.cheshire.Chapter chapter = (org.lgna.cheshire.Chapter)parent;
			if( chapter instanceof org.lgna.cheshire.TransactionChapter ) {
				org.lgna.cheshire.TransactionChapter transactionChapter = (org.lgna.cheshire.TransactionChapter)chapter;
				return transactionChapter.getTransaction().getIndexOfChildStep( (org.lgna.croquet.history.Step< ? >)child );
			}
		}
		return -1;
	}
	public Object getChild( Object parent, int index ) {
		if( parent instanceof org.lgna.cheshire.Book ) {
			org.lgna.cheshire.Book book = (org.lgna.cheshire.Book)parent;
			return book.getChapterAt( index );
		} else if( parent instanceof org.lgna.cheshire.Chapter ) {
			org.lgna.cheshire.Chapter chapter = (org.lgna.cheshire.Chapter)parent;
			if( chapter instanceof org.lgna.cheshire.TransactionChapter ) {
				org.lgna.cheshire.TransactionChapter transactionChapter = (org.lgna.cheshire.TransactionChapter)chapter;
				return transactionChapter.getTransaction().getChildStepAt( index );
			}
		}
		return null;
	}
	public int getChildCount( Object parent ) {
		if( parent instanceof org.lgna.cheshire.Book ) {
			org.lgna.cheshire.Book book = (org.lgna.cheshire.Book)parent;
			return book.getChapterCount();
		} else if( parent instanceof org.lgna.cheshire.Chapter ) {
			org.lgna.cheshire.Chapter chapter = (org.lgna.cheshire.Chapter)parent;
			if( chapter instanceof org.lgna.cheshire.TransactionChapter ) {
				org.lgna.cheshire.TransactionChapter transactionChapter = (org.lgna.cheshire.TransactionChapter)chapter;
				return transactionChapter.getTransaction().getChildStepCount();
			}
		}
		return 0;
	}
	private java.util.List< Object > updatePath( java.util.List< Object > rv, Object node ) {
		Object parent;
		if( node instanceof org.lgna.croquet.history.Transaction ) {
//			parent = ((org.lgna.croquet.steps.Transaction)node).getParent();
//			if( parent instanceof org.lgna.croquet.steps.TransactionHistory ) {
//				org.lgna.croquet.steps.TransactionHistory transactionHistory = (org.lgna.croquet.steps.TransactionHistory)parent;
//				if( transactionHistory.getParent() != null ) {
//					parent = transactionHistory; 
//				}
//			}
			parent = this.book;
		} else if( node instanceof org.lgna.croquet.history.Step ) {
			parent = ((org.lgna.croquet.history.Step)node).getParent();
		} else {
			parent = null;
		}
		if( parent != null ) {
			updatePath( rv, parent );
		}
		rv.add( node );
		return rv;
	}
	public javax.swing.tree.TreePath getTreePath( Object node ) {
		java.util.List< Object > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		updatePath( list, node );
		return new javax.swing.tree.TreePath( list.toArray() );
	}
}
