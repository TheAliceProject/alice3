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
//		CANCELLED,
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
//	public final boolean isCancelled() {
//		return this.getState() == State.CANCELLED;
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
////		this.state = State.CANCELLED;
////		//this.parent.cancelled();
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

/*package-private*/ abstract class Node implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public enum State {
		COMMITTED, FINISHED, SKIPPED, CANCELLED, PENDING
	}

	private Context parent;
	private java.util.UUID id;

	public Node( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.decode( binaryDecoder );
	}
	public Node( Context parent ) {
		this.parent = parent;
		this.id = java.util.UUID.randomUUID();
	}
	public Context getParent() {
		return this.parent;
	}
	public java.util.UUID getId() {
		return this.id;
	}

	public abstract State getState();
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
}

/*package-private*/ abstract class Event extends Node {
	public Event( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public Event( Context parent ) {
		super( parent );
	}
}

/*package-private*/ class ActionEvent extends Event {
	public ActionEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public ActionEvent( Context parent, AbstractActionOperation actionOperation, java.awt.event.ActionEvent e, KAbstractButton< ? > button ) {
		super( parent );
	}
	@Override
	protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
	}
	@Override
	protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
	}
	@Override
	public State getState() {
		return null;
	}
}

/*package-private*/ class BooleanStateEvent extends Event {
	public BooleanStateEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public BooleanStateEvent( Context parent, BooleanStateOperation booleanStateOperation, java.awt.event.ItemEvent e ) {
		super( parent );
	}
	@Override
	protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
	}
	@Override
	protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
	}
	@Override
	public State getState() {
		return null;
	}
}

/*package-private*/ class ItemSelectionEvent< T > extends Event {
	public ItemSelectionEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public ItemSelectionEvent( Context parent, ItemSelectionOperation< T > itemSelectionOperation, java.awt.event.ItemEvent e ) {
		super( parent );
	}
	@Override
	protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
	}
	@Override
	protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
	}
	@Override
	public State getState() {
		return null;
	}
}

/*package-private*/ class CommitEvent extends Event {
	private Edit edit;
	public CommitEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public CommitEvent( Context parent, Edit edit ) {
		super( parent );
		this.edit = edit;
	}
	@Override
	protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.edit = binaryDecoder.decodeBinaryEncodableAndDecodable( Edit.class );
	}
	@Override
	protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.edit );
	}
	@Override
	public State getState() {
		return State.COMMITTED;
	}
}
/*package-private*/ class FinishEvent extends Event {
	public FinishEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public FinishEvent( Context parent ) {
		super( parent );
	}
	@Override
	protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
	}
	@Override
	protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
	}
	@Override
	public State getState() {
		return State.FINISHED;
	}
}
/*package-private*/ class CancelEvent extends Event {
	public CancelEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public CancelEvent( Context parent ) {
		super( parent );
	}
	@Override
	protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
	}
	@Override
	protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
	}
	@Override
	public State getState() {
		return State.CANCELLED;
	}
}



/**
 * @author Dennis Cosgrove
 */
public class Context extends Node {
	public interface CommitObserver {
		public void committing( Edit edit );
		public void committed( Edit edit );
	}
	private java.util.List< CommitObserver > commitObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private java.util.List< Node > children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	//todo: reduce visibility?
	public Context( Context parent ) {
		super( parent );
	}
	
	public void addCommitObserver( CommitObserver commitObserver ) {
		this.commitObservers.add( commitObserver );
	}
	public void removeCommitObserver( CommitObserver commitObserver ) {
		this.commitObservers.remove( commitObserver );
	}
	
	protected void fireComitting( Edit edit ) {
		for( CommitObserver commitObserver : this.commitObservers ) {
			commitObserver.committing( edit );
		}
		Context parent = this.getParent();
		if( parent != null ) {
			parent.fireComitting( edit );
		}
	}
	protected void fireComitted( Edit edit ) {
		for( CommitObserver commitObserver : this.commitObservers ) {
			commitObserver.committed( edit );
		}
		Context parent = this.getParent();
		if( parent != null ) {
			parent.fireComitted( edit );
		}
	}
	
	@Override
	public State getState() {
		final int N = this.children.size();
		if( N > 0 ) {
			return this.children.get( N - 1 ).getState();
		} else {
			return null;
		}
	}
	public final boolean isCommitted() {
		return this.getState() == State.COMMITTED;
	}
	public final boolean isCancelled() {
		return this.getState() == State.CANCELLED;
	}

	private void addChild( Node child ) {
		synchronized( this.children ) {
			this.children.add( child );
		}
	}
	
	/*package-private*/Context open() {
		Context rv = new Context( this );
		this.addChild( rv );
		return rv;
	}
	/*package-private*/void closeIfNotPending() {
	}

	public void finish() {
		this.addChild( new FinishEvent( this ) );
	}
	public void commitAndInvokeDo( Edit edit ) {
		assert edit != null;
		this.fireComitting( edit );
		edit.doOrRedo( true );
		this.fireComitted( edit );
		this.addChild( new CommitEvent( this, edit ) );
	}
	public void cancel() {
		this.addChild( new CancelEvent( this ) );
	}

	/*package-private*/void handleItemStateChanged( BooleanStateOperation booleanStateOperation, java.awt.event.ItemEvent e ) {
		booleanStateOperation.perform( this, e );
		this.addChild( new BooleanStateEvent( this, booleanStateOperation, e ) );
	}
	/*package-private*/<T> void handleItemStateChanged( ItemSelectionOperation< T > itemSelectionOperation, java.awt.event.ItemEvent e ) {
		itemSelectionOperation.perform( this, e );
		this.addChild( new ItemSelectionEvent< T >( this, itemSelectionOperation, e ) );
	}

	/*package-private*/void handleActionPerformed( AbstractActionOperation actionOperation, java.awt.event.ActionEvent e, KAbstractButton< ? > button ) {
		actionOperation.perform( this, e, button );
		this.addChild( new ActionEvent( this, actionOperation, e, button ) );
	}

	@Override
	protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		Node[] array = binaryDecoder.decodeBinaryEncodableAndDecodableArray( Node.class );
		edu.cmu.cs.dennisc.java.util.CollectionUtilities.set( this.children, array );
	}
	@Override
	protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		Node[] array = new Node[ this.children.size() ];
		this.children.toArray( array );
		binaryEncoder.encode( array );
	}

	//	private java.util.List< Node > children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	//	public CompositeContext( CompositeContext parent, CompositeOperation operation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness ) {
	//		super( parent, operation, e, cancelEffectiveness );
	//	}
	//	@Override
	//	public State getState() {
	//		final int N = this.children.size();
	//		if( N > 0 ) {
	//			return this.children.get( N-1 ).getState();
	//		} else {
	//			return null;
	//		}
	//	}
	//	public CompositeContext getCurrentCompositeActionContext() {
	//		if( this.children.size() > 0 ) {
	//			Node<?> lastChildContext = this.children.get( this.children.size()-1 );
	//			if( lastChildContext instanceof CompositeContext ) {
	//				CompositeContext lastChildCompositeContext = (CompositeContext)lastChildContext;
	//				State state = lastChildContext.getState();
	//				if( state != null && state != State.PENDING ) {
	//					return this;
	//				} else {
	//					return lastChildCompositeContext.getCurrentCompositeActionContext();
	//				}
	//			} else {
	//				return this;
	//			}
	//		} else {
	//			return this;
	//		}
	//	}
	//	
	//	
	//	public <C extends Node> void performAsChild( Operation<C> operation, C context ) {
	//		this.children.add( context );
	//		operation.perform( context );
	//	}
	//	
	////	private boolean isGoodToGo() {
	////		if( this.children.size() > 0 ) {
	////			Context<?> lastChildContext = this.children.get( this.children.size()-1 );
	////			State state = lastChildContext.getState();
	////			return state != null && state != State.PENDING;
	////		} else {
	////			return true;
	////		}
	////	}
	////
	////	private static boolean isGoodToReturn( Context<?> context ) {
	////		State state = context.getState();
	////		if( state != null ) {
	////			if( state == State.PENDING ) {
	////				//todo? handle pend
	////			}
	////			return true;
	////		} else {
	////			return false;
	////		}
	////	}
	////	public CompositeContext performInChildContext( CompositeOperation compositeOperation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness ) {
	////		assert compositeOperation != null;
	////		assert this.isGoodToGo();
	////		CompositeContext rv = new CompositeContext( this, compositeOperation, e, cancelEffectiveness);
	////		this.children.add( rv );
	////		compositeOperation.perform(rv);
	////		assert isGoodToReturn( rv );
	////		return rv;
	////	}
	////	public ActionContext performInChildContext( ActionOperation actionOperation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness ) {
	////		assert actionOperation != null;
	////		assert this.isGoodToGo();
	////		ActionContext rv = new ActionContext( this, actionOperation, e, cancelEffectiveness);
	////		this.children.add( rv );
	////		actionOperation.perform(rv);
	////		assert isGoodToReturn( rv );
	////		return rv;
	////	}
	////
	////	public BooleanStateContext performInChildContext(BooleanStateOperation stateOperation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness, Boolean previousValue, Boolean nextValue) {
	////		assert stateOperation != null;
	////		assert this.isGoodToGo();
	////		BooleanStateContext rv = new BooleanStateContext(this, stateOperation, e, cancelEffectiveness, previousValue, nextValue);
	////		this.children.add( rv );
	////		stateOperation.performStateChange(rv);
	////		assert isGoodToReturn( rv );
	////		return rv;
	////	}
	////	
	////	public <E> ItemSelectionContext<E> performInChildContext(ItemSelectionOperation<E> itemSelectionOperation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness, E previousSelection, E nextSelection) {
	////		assert itemSelectionOperation != null;
	////		assert this.isGoodToGo();
	////		ItemSelectionContext<E> rv = new ItemSelectionContext<E>(this, itemSelectionOperation, e, cancelEffectiveness, previousSelection, nextSelection);
	////		this.children.add( rv );
	////		itemSelectionOperation.performSelectionChange(rv);
	////		assert isGoodToReturn( rv );
	////		return rv;
	////	}
	////	
	////	public BoundedRangeContext performInChildContext(BoundedRangeOperation boundedRangeOperation, java.util.EventObject e, CancelEffectiveness cancelEffectiveness) {
	////		assert boundedRangeOperation != null;
	////		assert this.isGoodToGo();
	////		BoundedRangeContext rv = new BoundedRangeContext(this, boundedRangeOperation, e, cancelEffectiveness);
	////		this.children.add( rv );
	////		boundedRangeOperation.perform(rv);
	////		assert isGoodToReturn( rv );
	////		return rv;
	////	}
}
