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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class Context< O extends Operation > {
	private CompositeContext parent;
	private O operation;
	private java.util.EventObject event;
	private CancelEffectiveness cancelEffectiveness;

	public enum State {
		COMMITTED,
		SKIPPED,
		CANCELLED,
		PENDING
	}
	private State state = null;
	
//	public static class Key<V> {
//		private String name;
//		private Key( String name ) {
//			this.name = name;
//		}
//		@Override
//		public String toString() {
//			return this.name;
//		}
//	};
//	public static <V> Key<V> createKey( String name ) {
//		return new Key( name );
//	}
//	private java.util.Map< Key, Object > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
//	public <V> V get( Key<V> key ) {
//		return (V)this.map.get( key );
//	}
//	public <V> void put( Key<V> key, V value ) {
//		this.map.put( key, value );
//	}

	public Context( CompositeContext parent, O operation, java.util.EventObject event, CancelEffectiveness cancelEffectiveness ) {
		this.parent = parent;
		this.operation = operation;
		this.event = event;
		this.cancelEffectiveness = cancelEffectiveness;
	}
	public Context<?> getParent() {
		return this.parent;
	}
	public O getOperation() {
		return this.operation;
	}
	public java.util.EventObject getEvent() {
		return event;
	}
	public boolean isCancelWorthwhile() {
		return this.cancelEffectiveness == CancelEffectiveness.WORTHWHILE;
	}

	public State getState() {
		return this.state;
	}
	
	public boolean isCommitted() {
		return this.state == State.COMMITTED;
	}
	public boolean isCancelled() {
		return this.state == State.CANCELLED;
	}
	public void commitAndInvokeDo( Edit<O> edit ) {
		assert this.state == null;
		//edu.cmu.cs.dennisc.croquet.event.CommitEvent e = new edu.cmu.cs.dennisc.croquet.event.CommitEvent( this.operation, this, edit );
		//this.parent.committing();
		if( edit != null ) {
			edit.doOrRedo( true );
		}
		this.state = State.COMMITTED;
		//this.parent.committed();
	}
	public void commit() {
		this.commitAndInvokeDo( null );
	}
	public void skip() {
		//edu.cmu.cs.dennisc.croquet.event.SkipEvent e = new edu.cmu.cs.dennisc.croquet.event.SkipEvent( this.operation, this );
		//this.parent.skipping();
		this.state = State.SKIPPED;
		//this.parent.skipped();
	}
	public void cancel() {
		assert this.state == null;
		edu.cmu.cs.dennisc.croquet.event.CancelEvent e = new edu.cmu.cs.dennisc.croquet.event.CancelEvent( this.operation, this );
		//this.parent.canceling();
		this.state = State.CANCELLED;
		//this.parent.cancelled();
	}
	private class PendTaskObserver< E extends Edit,F > implements edu.cmu.cs.dennisc.task.TaskObserver< F > {
		private Resolver<E,F> resolver;
		private E edit;
		public PendTaskObserver( Resolver<E,F> resolver ) {
			this.resolver = resolver;
			this.edit = this.resolver.createEdit();
			this.edit = this.resolver.initialize( this.edit, Context.this, this );
		}
		public void handleCompletion(F f) {
			this.edit = this.resolver.handleCompletion( this.edit, f);
			Context.this.commitAndInvokeDo( this.edit );
		}
		public void handleCancelation() {
			this.resolver.handleCancelation();
			Context.this.cancel();
		}
	}
	public void pend( Resolver<? extends Edit, ?> resolver ) {
		new PendTaskObserver(resolver);
		this.state = State.PENDING;
	}
	
//	public void execute( org.jdesktop.swingworker.SwingWorker< ?, ? > worker ) {
//		worker.execute();
//	}
	
}
