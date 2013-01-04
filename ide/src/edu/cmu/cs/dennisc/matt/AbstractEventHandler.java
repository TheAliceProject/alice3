package edu.cmu.cs.dennisc.matt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lgna.common.ComponentThread;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Visual;
import org.lgna.story.event.AbstractEvent;

import edu.cmu.cs.dennisc.java.util.concurrent.Collections;

public abstract class AbstractEventHandler<L, E extends AbstractEvent> {

	protected boolean shouldFire = true;
	protected Integer count = 0;
	protected Map<Object, MultipleEventPolicy> policyMap = Collections.newConcurrentHashMap();
	protected Map<Object, Map<Object, Boolean>> isFiringMap = Collections.newConcurrentHashMap();
	protected EventRecorder recorder = EventRecorder.getSingleton();
	private CopyOnWriteArrayList<E> queue = new CopyOnWriteArrayList<E>();
	private Object NULL_OBJECT = new Object();

	protected void fireEvent( final L listener, final E event, final Object object ) {
		final Object o = object == null ? NULL_OBJECT : object;
		if( isFiringMap.get( listener ) == null ) {
			isFiringMap.put( listener, new ConcurrentHashMap<Object, Boolean>() );
		}
		if( isFiringMap.get( listener ).get( o ) == null ) {
			isFiringMap.get( listener ).put( o, false );
		}
		if( shouldFire ) {
			ComponentThread thread = new org.lgna.common.ComponentThread( new Runnable() {
				public void run() {
					fire( listener, event );
					if( policyMap.get( listener ).equals( MultipleEventPolicy.ENQUEUE ) ) {
						fireDequeue( listener );
					}
					isFiringMap.get( listener ).put( o, false );
				}
			}, "eventThread" );
			if( isFiringMap.get( listener ).get( o ).equals( false ) ) {
				isFiringMap.get( listener ).put( o, true );
				thread.start();
				return;
			} else if( policyMap.get( listener ).equals( MultipleEventPolicy.COMBINE ) ) {
				thread.start();
			} else if( policyMap.get( listener ).equals( MultipleEventPolicy.ENQUEUE ) ) {
				enqueue( event );
			}
		}
	}

	protected void enqueue( E event ) {
		queue.add( event );
	}

	protected void fireDequeue( L listener ) {
		CopyOnWriteArrayList<E> internalQueue;
		if( queue.size() == 0 ) {
			return;
		}
		internalQueue = new CopyOnWriteArrayList<E>( queue );
		queue.clear();
		while( internalQueue.size() > 0 ) {
			fire( listener, internalQueue.remove( 0 ) );
		}
		fireDequeue( listener );
	}

	private void fire( L listener, E event ) {
		recorder.recordEvent( event );
		nameOfFireCall( listener, event );
	}

	protected abstract void nameOfFireCall( L listener, E event );

	public final void silenceListeners() {
		shouldFire = false;
	}

	public final void restoreListeners() {
		shouldFire = true;
	}

	protected void registerIsFiringMap( Object eventListener ) {
		isFiringMap.put( eventListener, new ConcurrentHashMap<Object, Boolean>() );
		isFiringMap.get( eventListener ).put( eventListener, false );
	}

	protected void registerIsFiringMap( Object eventListener, Visual[] targets ) {
		isFiringMap.put( eventListener, new ConcurrentHashMap<Object, Boolean>() );
		if( ( targets != null ) && ( targets.length > 0 ) ) {
			for( Visual target : targets ) {
				isFiringMap.get( eventListener ).put( target, false );
			}
		}
	}

	protected void registerPolicyMap( L listener, MultipleEventPolicy policy ) {
		policyMap.put( listener, policy );
	}

	protected void fireEvent( L listener, E event ) {
		fireEvent( listener, event, listener ); //used if policy is not constrained by anything else, such as selected model for mouse click events
	}

}
