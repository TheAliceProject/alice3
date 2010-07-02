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

//import edu.cmu.cs.dennisc.croquet.Node.State;
//
//public abstract class Node implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
//	private Node parent;
//	private java.util.UUID id;
//	public enum State {
//		COMMITTED,
//		SKIPPED,
//		CANCELED,
//		PENDING
//	}
//	public Node( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//		this.decode( binaryDecoder );
//	}
//	public Node( CompositeContext parent ) {
//		this.parent = parent;
//		this.id = java.util.UUID.randomUUID();
//	}
//	public Node getParent() {
//		return this.parent;
//	}
//	public java.util.UUID getId() {
//		return this.id;
//	}
//	public abstract State getState();
//	public final boolean isCommitted() {
//		return this.getState() == State.COMMITTED;
//	}
//	public final boolean isCanceled() {
//		return this.getState() == CANCELED;
//	}
//
//	protected abstract void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder );
//	protected abstract void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder );
//	public final void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//		this.id = edu.cmu.cs.dennisc.java.util.UuidUtilities.decodeUuid( binaryDecoder );
//		this.decodeInternal(binaryDecoder);
//	}
//	public final void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
//		edu.cmu.cs.dennisc.java.util.UuidUtilities.encodeUuid( binaryEncoder, this.id );
//		this.encodeInternal(binaryEncoder);
//	}
//}

//public abstract class ComponentContext< O extends ComponentOperation > extends Node {
////	private State state;
//	private O operation;
//	private java.util.EventObject event;
//	private CancelEffectiveness cancelEffectiveness;
//	public ComponentContext( CompositeContext parent, O operation, java.util.EventObject event, CancelEffectiveness cancelEffectiveness ) {
//		super( parent );
//		this.operation = operation;
//		this.event = event;
//		this.cancelEffectiveness = cancelEffectiveness;
//	}
////	@Override
////	public State getState() {
////		return this.state;
////	}
//	public O getOperation() {
//		return this.operation;
//	}
//	public java.util.EventObject getEvent() {
//		return event;
//	}
//	public CancelEffectiveness getCancelEffectiveness() {
//		return this.cancelEffectiveness;
//	}
//	public boolean isCancelWorthwhile() {
//		return this.cancelEffectiveness == CancelEffectiveness.WORTHWHILE;
//	}
//	
////	private void commitAndInvokeDo( Edit<O> edit ) {
////		assert this.state == null;
////		//edu.cmu.cs.dennisc.croquet.event.CommitEvent e = new edu.cmu.cs.dennisc.croquet.event.CommitEvent( this.getOperation(), this, edit );
////		//this.parent.committing();
////		if( edit != null ) {
////			edit.doOrRedo( true );
////		}
////		this.state = State.COMMITTED;
////		//this.parent.committed();
////	}
////	public void commit() {
////		this.commitAndInvokeDo( null );
////	}
////	public void skip() {
////		//edu.cmu.cs.dennisc.croquet.event.SkipEvent e = new edu.cmu.cs.dennisc.croquet.event.SkipEvent( this.operation, this );
////		//this.parent.skipping();
////		this.state = State.SKIPPED;
////		//this.parent.skipped();
////	}
////	public void cancel() {
////		assert this.state == null;
////		//edu.cmu.cs.dennisc.croquet.event.CancelEvent e = new edu.cmu.cs.dennisc.croquet.event.CancelEvent( this.getOperation(), this );
////		//this.parent.canceling();
////		this.state = State.CANCELED;
////		//this.parent.canceled();
////	}
////	private class PendTaskObserver< E extends Edit,F > implements edu.cmu.cs.dennisc.task.TaskObserver< F > {
////		private Resolver<E,F> resolver;
////		private E edit;
////		public PendTaskObserver( Resolver<E,F> resolver ) {
////			this.resolver = resolver;
////			this.edit = this.resolver.createEdit();
////			this.edit = this.resolver.initialize( this.edit, ComponentContext.this, this );
////		}
////		public void handleCompletion(F f) {
////			this.edit = this.resolver.handleCompletion( this.edit, f);
////			ComponentContext.this.commitAndInvokeDo( this.edit );
////		}
////		public void handleCancelation() {
////			this.resolver.handleCancelation();
////			ComponentContext.this.cancel();
////		}
////	}
////	public void pend( Resolver<? extends Edit, ?> resolver ) {
////		new PendTaskObserver(resolver);
////		this.state = State.PENDING;
////	}
////	
//////	public void execute( org.jdesktop.swingworker.SwingWorker< ?, ? > worker ) {
//////		worker.execute();
//////	}
//	
//}

public abstract class HistoryTreeNode< C extends ModelContext<?> > implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable, javax.swing.tree.TreeNode {
	private C parent;
	private java.util.UUID id;

	public HistoryTreeNode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.decode( binaryDecoder );
	}
	public HistoryTreeNode( C parent ) {
		this.parent = parent;
		this.id = java.util.UUID.randomUUID();
	}
	public C getParent() {
		return this.parent;
	}
	public java.util.UUID getId() {
		return this.id;
	}

	public enum State {
		COMMITTED, 
		FINISHED, 
		//SKIPPED, 
		CANCELED, 
		PENDING
	}
	public abstract State getState();
	
	public ModelContext<?> findContextFor( Model model ) {
		if( this.parent != null ) {
			return this.parent.findContextFor( model );
		} else {
			return null;
		}
	}
	protected abstract void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder );
	protected abstract void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder );
	public final void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.id = edu.cmu.cs.dennisc.java.util.UuidUtilities.decodeUuid( binaryDecoder );
		this.decodeInternal( binaryDecoder );
	}
	public final void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		edu.cmu.cs.dennisc.java.util.UuidUtilities.encodeUuid( binaryEncoder, this.id );
		this.encodeInternal( binaryEncoder );
	}
	
	protected StringBuilder appendRepr( StringBuilder rv ) {
		rv.append( this.getClass().getSimpleName() );
		return rv;
	}
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		this.appendRepr( sb );
		return sb.toString();
	}
}
