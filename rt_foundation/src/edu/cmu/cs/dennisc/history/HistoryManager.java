package edu.cmu.cs.dennisc.history;

public class HistoryManager {
	private static edu.cmu.cs.dennisc.zoot.event.ManagerListener managerListener = new edu.cmu.cs.dennisc.zoot.event.ManagerListener() {
		public void operationPerforming( edu.cmu.cs.dennisc.zoot.event.ManagerEvent managerEvent ) {
		}
		public void operationPerformed( edu.cmu.cs.dennisc.zoot.event.ManagerEvent managerEvent ) {
		}
	};

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
	private static void handleOperationPerformed( edu.cmu.cs.dennisc.zoot.event.ManagerEvent managerEvent ) {
		edu.cmu.cs.dennisc.zoot.Operation operation = managerEvent.getTypedSource();
		HistoryManager historyManager = HistoryManager.get( operation.getGroupUUID() );
		historyManager.push( managerEvent );
	}
	private java.util.Stack< edu.cmu.cs.dennisc.zoot.event.ManagerEvent > stack = new java.util.Stack< edu.cmu.cs.dennisc.zoot.event.ManagerEvent >();
	private java.util.UUID uuid;
	private HistoryManager( java.util.UUID uuid ) {
		this.uuid = uuid;
		edu.cmu.cs.dennisc.zoot.ZManager.addManagerListener( this.managerListener );
	}
	public java.util.UUID getUuid() {
		return this.uuid;
	}
	public java.util.Stack< edu.cmu.cs.dennisc.zoot.event.ManagerEvent > getStack() {
		return this.stack;
	}
	private void push( edu.cmu.cs.dennisc.zoot.event.ManagerEvent managerEvent ) {
		edu.cmu.cs.dennisc.history.event.HistoryEvent e = new edu.cmu.cs.dennisc.history.event.HistoryEvent( this, managerEvent );
		this.fireOperationPushing( e );
		this.stack.push( managerEvent );
		this.fireOperationPushed( e );
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
