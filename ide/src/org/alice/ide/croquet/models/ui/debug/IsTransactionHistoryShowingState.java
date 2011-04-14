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
package org.alice.ide.croquet.models.ui.debug;

/**
 * @author Dennis Cosgrove
 */
class TransactionTreeModel extends edu.cmu.cs.dennisc.javax.swing.models.AbstractMutableTreeModel< Object > {
	private edu.cmu.cs.dennisc.croquet.TransactionHistory root;
	public TransactionTreeModel( edu.cmu.cs.dennisc.croquet.TransactionHistory root ) {
		this.root = root;
	}
	public edu.cmu.cs.dennisc.croquet.TransactionHistory getRoot() {
		return this.root;
	}
	public boolean isLeaf( Object node ) {
		return node instanceof edu.cmu.cs.dennisc.croquet.PrepStep< ? >;
	}
	public int getChildCount( Object parent ) {
		if( parent instanceof edu.cmu.cs.dennisc.croquet.TransactionHistory ) {
			edu.cmu.cs.dennisc.croquet.TransactionHistory transactionHistory = (edu.cmu.cs.dennisc.croquet.TransactionHistory)parent;
			return transactionHistory.getTransactionCount();
		} else if( parent instanceof edu.cmu.cs.dennisc.croquet.Transaction ) {
			edu.cmu.cs.dennisc.croquet.Transaction transaction = (edu.cmu.cs.dennisc.croquet.Transaction)parent;
			edu.cmu.cs.dennisc.croquet.CompletionStep< ? > completionStep = transaction.getCompletionStep();
			return transaction.getPrepStepCount()+(completionStep!=null?1:0);
		} else if( parent instanceof edu.cmu.cs.dennisc.croquet.CompletionStep< ? > ) {
			edu.cmu.cs.dennisc.croquet.CompletionStep< ? > completionStep = (edu.cmu.cs.dennisc.croquet.CompletionStep< ? >)parent;
			return completionStep.getTransactionHistory() != null ? 1 : 0;
		} else {
			return 0;
		}
	}
	public Object getChild( Object parent, int index ) {
		if( parent instanceof edu.cmu.cs.dennisc.croquet.TransactionHistory ) {
			edu.cmu.cs.dennisc.croquet.TransactionHistory transactionHistory = (edu.cmu.cs.dennisc.croquet.TransactionHistory)parent;
			return transactionHistory.getTransactionAt( index );
		} else if( parent instanceof edu.cmu.cs.dennisc.croquet.Transaction ) {
			edu.cmu.cs.dennisc.croquet.Transaction transaction = (edu.cmu.cs.dennisc.croquet.Transaction)parent;
			if( index < transaction.getPrepStepCount() ) {
				return transaction.getPrepStepAt( index );
			} else {
				return transaction.getCompletionStep();
			}
		} else if( parent instanceof edu.cmu.cs.dennisc.croquet.CompletionStep< ? > ) {
			assert index == 0;
			edu.cmu.cs.dennisc.croquet.CompletionStep< ? > completionStep = (edu.cmu.cs.dennisc.croquet.CompletionStep< ? >)parent;
			return completionStep.getTransactionHistory();
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	public int getIndexOfChild( Object parent, Object child ) {
		if( parent instanceof edu.cmu.cs.dennisc.croquet.TransactionHistory ) {
			edu.cmu.cs.dennisc.croquet.TransactionHistory transactionHistory = (edu.cmu.cs.dennisc.croquet.TransactionHistory)parent;
			if( child instanceof edu.cmu.cs.dennisc.croquet.Transaction ) {
				return transactionHistory.getIndexOfTransaction( (edu.cmu.cs.dennisc.croquet.Transaction)child );
			} else {
				return -1;
			}
		} else if( parent instanceof edu.cmu.cs.dennisc.croquet.Transaction ) {
			edu.cmu.cs.dennisc.croquet.Transaction transaction = (edu.cmu.cs.dennisc.croquet.Transaction)parent;
			if( child instanceof edu.cmu.cs.dennisc.croquet.PrepStep< ? > ) {
				return transaction.getIndexOfPrepStep( (edu.cmu.cs.dennisc.croquet.PrepStep< ? >)child );
			} else {
				return -1;
			}
		} else if( parent instanceof edu.cmu.cs.dennisc.croquet.CompletionStep< ? > ) {
			edu.cmu.cs.dennisc.croquet.CompletionStep< ? > completionStep = (edu.cmu.cs.dennisc.croquet.CompletionStep< ? >)parent;
			assert child == completionStep.getTransactionHistory();
			return 0;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	
	private java.util.List< Object > updatePath( java.util.List< Object > rv, Object node ) {
		Object parent;
		if( node instanceof edu.cmu.cs.dennisc.croquet.Transaction ) {
			parent = ((edu.cmu.cs.dennisc.croquet.Transaction)node).getParent();
		} else if( node instanceof edu.cmu.cs.dennisc.croquet.Step ) {
			parent = ((edu.cmu.cs.dennisc.croquet.Step)node).getParent();
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

/**
 * @author Dennis Cosgrove
 */
public class IsTransactionHistoryShowingState extends org.alice.ide.croquet.models.IsFrameShowingState {
	private static class SingletonHolder {
		private static IsTransactionHistoryShowingState instance = new IsTransactionHistoryShowingState( edu.cmu.cs.dennisc.croquet.TransactionManager.getRootTransactionHistory() );
	}
	public static IsTransactionHistoryShowingState getInstance() {
		return SingletonHolder.instance;
	}

	public static IsTransactionHistoryShowingState createInstance( edu.cmu.cs.dennisc.croquet.TransactionHistory transactionHistory ) {
		return new IsTransactionHistoryShowingState( transactionHistory );
	}
	
	private final edu.cmu.cs.dennisc.croquet.TransactionHistory transactionHistory;
	private IsTransactionHistoryShowingState( edu.cmu.cs.dennisc.croquet.TransactionHistory transactionHistory ) {
		super( org.alice.ide.ProjectApplication.INFORMATION_GROUP, java.util.UUID.fromString( "a584d3f3-2fbd-4991-bbc6-98fb68c74e6f" ), true );
		this.transactionHistory = transactionHistory;
	}
	@Override
	protected void localize() {
		super.localize();
		this.setTextForBothTrueAndFalse( "Transaction Tree" );
	}
	@Override
	protected javax.swing.JFrame createFrame() {
		javax.swing.JFrame rv = super.createFrame();
		final int SCREEN_INDEX = 1;
		rv.setBounds( edu.cmu.cs.dennisc.java.awt.GraphicsDeviceUtilities.getScreenDeviceDefaultConfigurationBounds( SCREEN_INDEX ) );
		return rv;
	}
	@Override
	protected java.awt.Component createPane() {
		final TransactionTreeModel treeModel = new TransactionTreeModel( this.transactionHistory );
		final javax.swing.JTree tree = new javax.swing.JTree( treeModel );

		for( int i = 0; i < tree.getRowCount(); i++ ) {
			tree.expandRow( i );
		}
		tree.setRootVisible( false );
		final javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( tree );
		scrollPane.getVerticalScrollBar().setUnitIncrement( 12 );
		edu.cmu.cs.dennisc.croquet.TransactionManager.addStepObserver( new edu.cmu.cs.dennisc.croquet.TransactionManager.StepObserver() {
			public void addingStep( edu.cmu.cs.dennisc.croquet.Step< ? > step ) {
			}
			public void addedStep( edu.cmu.cs.dennisc.croquet.Step< ? > step ) {
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						treeModel.reload();
						int childCount = treeModel.getChildCount( treeModel.getRoot() );
						for( int i=0; i<tree.getRowCount(); i++ ) {
							if( i<childCount-1 ) {
								tree.collapseRow( i );
							} else {
								tree.expandRow( i );
							}
						}
						tree.scrollRowToVisible( tree.getRowCount()-1 );
						scrollPane.getHorizontalScrollBar().setValue( 0 );
					}
				} );
			}
		} );
		return scrollPane;
	}
	@Override
	protected void handleChanged( boolean value ) {
		super.handleChanged( value );
		//todo: remove listener
	}
}
