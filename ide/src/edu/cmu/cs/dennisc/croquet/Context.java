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
	private O operation;
	private java.util.Map< Object, Object > map = new java.util.HashMap< Object, Object >();
	private boolean isCommitted = false;
	private boolean isCancelled = false;
	private java.util.EventObject e;
	private boolean isCancelWorthwhile;

	public Context( O operation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		this.operation = operation;
		this.e = e;
		this.isCancelWorthwhile = isCancelWorthwhile;
	}
	public O getOperation() {
		return this.operation;
	}
	public boolean isCommitted() {
		return this.isCommitted;
	}
	public boolean isCancelled() {
		return this.isCancelled;
	}
	public boolean isPending() {
		return (this.isCommitted() || this.isCancelled()) == false;
	}
	public void commitAndInvokeDo( Edit edit ) {
		assert this.isPending();
		edu.cmu.cs.dennisc.croquet.event.CommitEvent e = new edu.cmu.cs.dennisc.croquet.event.CommitEvent( this.operation, this, edit );
		Application.fireOperationCommitting( e );
		if( edit != null ) {
			edit.doOrRedo( true );
		}
		this.isCommitted = true;
		Application.fireOperationCommitted( e );
	}
	public void commit() {
		this.commitAndInvokeDo( null );
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
	public void pend(Resolver<? extends Edit, ?> resolver) {
		new PendTaskObserver(resolver);
	}
	public void cancel() {
		assert this.isPending();
		edu.cmu.cs.dennisc.croquet.event.CancelEvent e = new edu.cmu.cs.dennisc.croquet.event.CancelEvent( this.operation, this );
		Application.fireOperationCancelling( e );
		this.isCancelled = true;
		Application.fireOperationCancelled( e );
	}
	public boolean isCancelWorthwhile() {
		return isCancelWorthwhile;
	}
	public <E extends Object> E get( Object key, Class< E > cls ) {
		Object rv = this.get( key );
		if( rv != null ) {
			assert cls.isAssignableFrom( rv.getClass() );
			return (E)rv;
		} else {
			return null;
		}
	}
	public Object get( Object key ) {
		return this.map.get( key );
	}
	public void put( Object key, Object value ) {
		this.map.put( key, value );
	}
	public java.util.EventObject getEvent() {
		return e;
	}
	public ActionContext perform( ActionOperation operation, java.util.EventObject o, boolean isCancelWorthwhile ) {
		return Application.performIfAppropriate( operation, o, isCancelWorthwhile );
	}
	public ItemSelectionContext perform( ItemSelectionOperation operation, java.util.EventObject o, boolean isCancelWorthwhile, Object prevSelection, Object nextSelection ) {
		return Application.performIfAppropriate( operation, o, isCancelWorthwhile, prevSelection, nextSelection );
	}
	public void execute( org.jdesktop.swingworker.SwingWorker< ?, ? > worker ) {
		worker.execute();
	}
}
