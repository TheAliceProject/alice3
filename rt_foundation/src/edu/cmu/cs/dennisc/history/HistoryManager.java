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
	public static HistoryManager get( java.util.UUID uuid ) {
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
		edu.cmu.cs.dennisc.zoot.Operation operation = commitEvent.getTypedSource();
		HistoryManager historyManager = HistoryManager.get( operation.getGroupUUID() );
		historyManager.push( commitEvent );
	}
	private java.util.Stack< edu.cmu.cs.dennisc.zoot.event.CommitEvent > stack = new java.util.Stack< edu.cmu.cs.dennisc.zoot.event.CommitEvent >();
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
			edu.cmu.cs.dennisc.history.event.HistoryEvent historyEvent = new edu.cmu.cs.dennisc.history.event.HistoryEvent( this, commitEvent.getEdit() );
			this.fireOperationPushing( historyEvent );
			this.stack.push( commitEvent );
			this.fireOperationPushed( historyEvent );
		}
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

	private void fireOperationPushing( edu.cmu.cs.dennisc.history.event.HistoryEvent e ) {
		synchronized( this.historyListeners ) {
			for( edu.cmu.cs.dennisc.history.event.HistoryListener l : this.historyListeners ) {
				l.operationPushing( e );
			}
		}
	}
	private void fireOperationPushed( edu.cmu.cs.dennisc.history.event.HistoryEvent e ) {
		synchronized( this.historyListeners ) {
			for( edu.cmu.cs.dennisc.history.event.HistoryListener l : this.historyListeners ) {
				l.operationPushed( e );
			}
		}
	}
}
