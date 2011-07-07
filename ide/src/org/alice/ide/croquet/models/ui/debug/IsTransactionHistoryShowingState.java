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
	private org.lgna.croquet.history.TransactionHistory root;
	public TransactionTreeModel( org.lgna.croquet.history.TransactionHistory root ) {
		this.root = root;
	}
	public org.lgna.croquet.history.TransactionHistory getRoot() {
		return this.root;
	}
	public boolean isLeaf( Object node ) {
		return node instanceof org.lgna.croquet.history.PrepStep< ? >;
	}
	public int getChildCount( Object parent ) {
		if( parent instanceof org.lgna.croquet.history.TransactionHistory ) {
			org.lgna.croquet.history.TransactionHistory transactionHistory = (org.lgna.croquet.history.TransactionHistory)parent;
			return transactionHistory.getTransactionCount();
		} else if( parent instanceof org.lgna.croquet.history.Transaction ) {
			org.lgna.croquet.history.Transaction transaction = (org.lgna.croquet.history.Transaction)parent;
			return transaction.getChildStepCount();
		} else if( parent instanceof org.lgna.croquet.history.CompletionStep< ? > ) {
			org.lgna.croquet.history.CompletionStep< ? > completionStep = (org.lgna.croquet.history.CompletionStep< ? >)parent;
			//return completionStep.getTransactionHistory() != null ? 1 : 0;
			org.lgna.croquet.history.TransactionHistory transactionHistory = completionStep.getTransactionHistory();
			if( transactionHistory != null ) {
				return transactionHistory.getTransactionCount();
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
	public Object getChild( Object parent, int index ) {
		if( parent instanceof org.lgna.croquet.history.TransactionHistory ) {
			org.lgna.croquet.history.TransactionHistory transactionHistory = (org.lgna.croquet.history.TransactionHistory)parent;
			return transactionHistory.getTransactionAt( index );
		} else if( parent instanceof org.lgna.croquet.history.Transaction ) {
			org.lgna.croquet.history.Transaction transaction = (org.lgna.croquet.history.Transaction)parent;
			return transaction.getChildStepAt( index );
		} else if( parent instanceof org.lgna.croquet.history.CompletionStep< ? > ) {
			org.lgna.croquet.history.CompletionStep< ? > completionStep = (org.lgna.croquet.history.CompletionStep< ? >)parent;
			org.lgna.croquet.history.TransactionHistory transactionHistory = completionStep.getTransactionHistory();
			if( transactionHistory != null ) {
				return transactionHistory.getTransactionAt( index );
			} else {
				return null;
			}
//			assert index == 0;
//			return completionStep.getTransactionHistory();
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	public int getIndexOfChild( Object parent, Object child ) {
		if( parent instanceof org.lgna.croquet.history.TransactionHistory ) {
			org.lgna.croquet.history.TransactionHistory transactionHistory = (org.lgna.croquet.history.TransactionHistory)parent;
			if( child instanceof org.lgna.croquet.history.Transaction ) {
				return transactionHistory.getIndexOfTransaction( (org.lgna.croquet.history.Transaction)child );
			} else {
				return -1;
			}
		} else if( parent instanceof org.lgna.croquet.history.Transaction ) {
			org.lgna.croquet.history.Transaction transaction = (org.lgna.croquet.history.Transaction)parent;
			if( child instanceof org.lgna.croquet.history.PrepStep< ? > ) {
				return transaction.getIndexOfPrepStep( (org.lgna.croquet.history.PrepStep< ? >)child );
			} else {
				return -1;
			}
		} else if( parent instanceof org.lgna.croquet.history.CompletionStep< ? > ) {
			org.lgna.croquet.history.CompletionStep< ? > completionStep = (org.lgna.croquet.history.CompletionStep< ? >)parent;
			org.lgna.croquet.history.TransactionHistory transactionHistory = completionStep.getTransactionHistory();
			return transactionHistory.getIndexOfTransaction( (org.lgna.croquet.history.Transaction)child );
//			assert child == completionStep.getTransactionHistory();
//			return 0;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	
	private java.util.List< Object > updatePath( java.util.List< Object > rv, Object node ) {
		Object parent;
		if( node instanceof org.lgna.croquet.history.Transaction ) {
			parent = ((org.lgna.croquet.history.Transaction)node).getParent();
			if( parent instanceof org.lgna.croquet.history.TransactionHistory ) {
				org.lgna.croquet.history.TransactionHistory transactionHistory = (org.lgna.croquet.history.TransactionHistory)parent;
				if( transactionHistory.getParent() != null ) {
					parent = transactionHistory; 
				}
			}
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

class TransactionHistoryCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.TreeCellRenderer< Object > {
	@Override
	protected javax.swing.JLabel updateListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
		if( value instanceof org.lgna.croquet.history.Transaction ) {
			org.lgna.croquet.history.Transaction transaction = (org.lgna.croquet.history.Transaction)value;
			int i = transaction.getParent().getIndexOfTransaction( transaction );
			StringBuilder sb = new StringBuilder();
			sb.append( "<html>" );
			sb.append( "transaction[" );
			sb.append( i );
			sb.append( "] " );
			String title = transaction.getTitle( org.lgna.croquet.DefaultUserInformation.SINGLETON );
			if( title != null ) {
				sb.append( "<strong>" );
				sb.append( title );
				sb.append( "</strong>" );
			}
			sb.append( "</html>" );
			rv.setText( sb.toString() );
			rv.setIcon( null );
		} else if( value instanceof org.lgna.croquet.history.CompletionStep< ? > ) {
			org.lgna.croquet.history.CompletionStep< ? > completionStep = (org.lgna.croquet.history.CompletionStep< ? >)value;
			String name;
			if( completionStep.isPending() ) {
				name = "pending";
			} else {
				if( completionStep.isSuccessfullyCompleted() ) {
					name = "completed";
				} else {
					name = "canceled";
				}
			}
			rv.setIcon( new javax.swing.ImageIcon( TransactionHistoryCellRenderer.class.getResource( "images/" + name + ".png" ) ) );
		}
		return rv;
	}	
}

/**
 * @author Dennis Cosgrove
 */
public class IsTransactionHistoryShowingState extends org.alice.ide.croquet.models.IsFrameShowingState {
	public static boolean IS_SIDE_DOCKING_DESIRED = false;
	private static class SingletonHolder {
		private static IsTransactionHistoryShowingState instance = new IsTransactionHistoryShowingState( org.lgna.croquet.history.TransactionManager.getRootTransactionHistory() ) {
			@Override
			protected javax.swing.JFrame createFrame() {
				javax.swing.JFrame rv = super.createFrame();
				if( IS_SIDE_DOCKING_DESIRED ) {
					rv.setLocation( 1280, 0 );
					rv.setSize( 280, 720 );
				}
				return rv;
			}
		};
	}
	public static IsTransactionHistoryShowingState getInstance() {
		return SingletonHolder.instance;
	}

	public static IsTransactionHistoryShowingState createInstance( org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		return new IsTransactionHistoryShowingState( transactionHistory );
	}
	
	private final org.lgna.croquet.history.TransactionHistory transactionHistory;
	private IsTransactionHistoryShowingState( org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		super( org.alice.ide.ProjectApplication.INFORMATION_GROUP, java.util.UUID.fromString( "a584d3f3-2fbd-4991-bbc6-98fb68c74e6f" ), true );
		this.transactionHistory = transactionHistory;
	}
	@Override
	protected void localize() {
		super.localize();
		this.setTextForBothTrueAndFalse( "Transaction History" );
	}
	@Override
	protected javax.swing.JFrame createFrame() {
		javax.swing.JFrame rv = super.createFrame();
		final int SCREEN_INDEX = 1;
		java.awt.Rectangle bounds = edu.cmu.cs.dennisc.java.awt.GraphicsDeviceUtilities.getScreenDeviceDefaultConfigurationBounds( SCREEN_INDEX );
		if( bounds != null ) {
			rv.setBounds( bounds );
		} else {
			org.lgna.croquet.Application application = org.lgna.croquet.Application.getSingleton();
			if( application != null ) {
				org.lgna.croquet.components.Frame frame = application.getFrame();
				if( frame != null ) {
					java.awt.Rectangle bounds2 = frame.getBounds();
					bounds2.x += bounds2.width;
					bounds2.width = 200;
					rv.setBounds( bounds2 );
				}
			}
		}
		return rv;
	}
	@Override
	protected java.awt.Component createPane() {
		final TransactionTreeModel treeModel = new TransactionTreeModel( this.transactionHistory );
		final javax.swing.JTree tree = new javax.swing.JTree( treeModel );

		tree.setCellRenderer( new TransactionHistoryCellRenderer() );
		for( int i = 0; i < tree.getRowCount(); i++ ) {
			tree.expandRow( i );
		}
		tree.setRootVisible( false );
		final javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( tree );
		scrollPane.getVerticalScrollBar().setUnitIncrement( 12 );
		org.lgna.croquet.history.TransactionManager.getRootTransactionHistory().addListener( new org.lgna.croquet.history.event.Listener() {
			private void reload() {
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						treeModel.reload();
						int childCount = treeModel.getChildCount( treeModel.getRoot() );
						for( int i=0; i<tree.getRowCount(); i++ ) {
							if( IS_SIDE_DOCKING_DESIRED==false && i<childCount-1 ) {
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
			public void changing(org.lgna.croquet.history.event.Event e) {
				
			}
			public void changed(org.lgna.croquet.history.event.Event e) {
				if( e instanceof org.lgna.croquet.history.event.AddStepEvent ) {
					this.reload();
				} else if( e instanceof org.lgna.croquet.history.event.FinishedEvent || e instanceof org.lgna.croquet.history.event.EditCommittedEvent ) {
					tree.repaint();
				}
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
