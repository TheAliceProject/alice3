package edu.cmu.cs.dennisc.history;

public class HistoryManager {
	private static edu.cmu.cs.dennisc.zoot.event.ManagerListener managerListener = new edu.cmu.cs.dennisc.zoot.event.ManagerListener() {
		public void operationCancelling( edu.cmu.cs.dennisc.zoot.event.CancelEvent e ) {
		}
		public void operationCancelled( edu.cmu.cs.dennisc.zoot.event.CancelEvent e ) {
		}
		public void operationCommitting( edu.cmu.cs.dennisc.zoot.event.CommitEvent e ) {
		}
		public void operationCommitted( edu.cmu.cs.dennisc.zoot.event.CommitEvent e ) {
			HistoryManager.handleOperationPerformed( e );
		}
	};
	static {
		edu.cmu.cs.dennisc.zoot.ZManager.addManagerListener( HistoryManager.managerListener );
	}

	private static java.util.Map< java.util.UUID, HistoryManager > map = new java.util.HashMap< java.util.UUID, HistoryManager >();

	public static HistoryManager getInstance( java.util.UUID uuid ) {
		HistoryManager rv = HistoryManager.map.get( uuid );
		if( rv != null ) {
			//pass
		} else {
			rv = new HistoryManager( uuid );
			HistoryManager.map.put( uuid, rv );
		}
		return rv;
	}
	private static void handleOperationPerformed( edu.cmu.cs.dennisc.zoot.event.CommitEvent commitEvent ) {
		edu.cmu.cs.dennisc.zoot.Edit edit = commitEvent.getEdit();
		if( edit != null ) {
			edu.cmu.cs.dennisc.zoot.Operation operation = commitEvent.getTypedSource();
			HistoryManager historyManager = HistoryManager.getInstance( operation.getGroupUUID() );
			historyManager.push( commitEvent );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "no edit to undo for", commitEvent );
		}
	}

	private java.util.Stack< edu.cmu.cs.dennisc.zoot.event.CommitEvent > stack = new java.util.Stack< edu.cmu.cs.dennisc.zoot.event.CommitEvent >();
	private int insertionIndex = 0;
	private java.util.UUID uuid;

	private HistoryManager( java.util.UUID uuid ) {
		this.uuid = uuid;
	}
	public java.util.UUID getUuid() {
		return this.uuid;
	}
	public java.util.Stack< edu.cmu.cs.dennisc.zoot.event.CommitEvent > getStack() {
		return this.stack;
	}
	private void push( edu.cmu.cs.dennisc.zoot.event.CommitEvent commitEvent ) {
		edu.cmu.cs.dennisc.zoot.Operation operation = commitEvent.getTypedSource();
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( operation.getGroupUUID(), this.uuid ) ) {
			edu.cmu.cs.dennisc.history.event.HistoryPushEvent historyPushEvent = new edu.cmu.cs.dennisc.history.event.HistoryPushEvent( this, commitEvent.getEdit() );
			this.fireOperationPushing( historyPushEvent );
			this.stack.setSize( this.insertionIndex );
			this.stack.push( commitEvent );
			this.setInsertionIndex( this.stack.size(), false );
			this.fireOperationPushed( historyPushEvent );
		}
	}

	private static void beep() {
		java.awt.Toolkit.getDefaultToolkit().beep();
	}
	private void undo() {
		if( this.insertionIndex > 0 ) {
			edu.cmu.cs.dennisc.zoot.event.CommitEvent commitEvent = this.stack.get( this.insertionIndex - 1 );
			if( commitEvent != null ) {
				edu.cmu.cs.dennisc.zoot.Edit edit = commitEvent.getEdit();
				if( edit.canRedo() ) {
					edit.undo();
					this.insertionIndex--;
				} else {
					beep();
				}
			} else {
				beep();
			}
		} else {
			beep();
		}
	}
	private void redo() {
		if( this.insertionIndex < this.stack.size() ) {
			edu.cmu.cs.dennisc.zoot.event.CommitEvent commitEvent = this.stack.get( this.insertionIndex );
			if( commitEvent != null ) {
				edu.cmu.cs.dennisc.zoot.Edit edit = commitEvent.getEdit();
				if( edit != null ) {
					if( edit.canRedo() ) {
						edit.doOrRedo( false );
						this.insertionIndex++;
					} else {
						beep();
					}
				} else {
					beep();
				}
			} else {
				beep();
			}
		} else {
			beep();
		}
	}

	public void performUndo() {
		this.setInsertionIndex( this.insertionIndex - 1 );
	}
	public void performRedo() {
		this.setInsertionIndex( this.insertionIndex + 1 );
	}
	public int getInsertionIndex() {
		return this.insertionIndex;
	}
	private void setInsertionIndex( int nextInsertionIndex, boolean isActionDesired ) {
		if( nextInsertionIndex >= 0 && nextInsertionIndex <= this.stack.size() ) {
			if( this.insertionIndex != nextInsertionIndex ) {
				edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent e = new edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent( this, this.insertionIndex, nextInsertionIndex );
				this.fireInsertionIndexChanging( e );

				final int N = Math.abs( nextInsertionIndex - this.insertionIndex );
				if( nextInsertionIndex < this.insertionIndex ) {
					for( int i = 0; i < N; i++ ) {
						if( isActionDesired ) {
							this.undo();
						} else {
							this.insertionIndex--;
						}
					}
				} else {
					for( int i = 0; i < N; i++ ) {
						if( isActionDesired ) {
							this.redo();
						} else {
							this.insertionIndex++;
						}
					}
				}
				assert this.insertionIndex == nextInsertionIndex;
				this.fireInsertionIndexChanged( e );
			}
		}
	}
	public void setInsertionIndex( int nextInsertionIndex ) {
		this.setInsertionIndex( nextInsertionIndex, true );
	}

	private java.util.List< edu.cmu.cs.dennisc.history.event.HistoryListener > historyListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.history.event.HistoryListener >();

	public void addHistoryListener( edu.cmu.cs.dennisc.history.event.HistoryListener l ) {
		synchronized( this.historyListeners ) {
			this.historyListeners.add( l );
		}
	}
	public void removeHistoryListener( edu.cmu.cs.dennisc.history.event.HistoryListener l ) {
		synchronized( this.historyListeners ) {
			this.historyListeners.remove( l );
		}
	}

	private void fireOperationPushing( edu.cmu.cs.dennisc.history.event.HistoryPushEvent e ) {
		synchronized( this.historyListeners ) {
			for( edu.cmu.cs.dennisc.history.event.HistoryListener l : this.historyListeners ) {
				l.operationPushing( e );
			}
		}
	}
	private void fireOperationPushed( edu.cmu.cs.dennisc.history.event.HistoryPushEvent e ) {
		synchronized( this.historyListeners ) {
			for( edu.cmu.cs.dennisc.history.event.HistoryListener l : this.historyListeners ) {
				l.operationPushed( e );
			}
		}
	}
	private void fireInsertionIndexChanging( edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent e ) {
		synchronized( this.historyListeners ) {
			for( edu.cmu.cs.dennisc.history.event.HistoryListener l : this.historyListeners ) {
				l.insertionIndexChanging( e );
			}
		}
	}
	private void fireInsertionIndexChanged( edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent e ) {
		synchronized( this.historyListeners ) {
			for( edu.cmu.cs.dennisc.history.event.HistoryListener l : this.historyListeners ) {
				l.insertionIndexChanged( e );
			}
		}
	}
}
