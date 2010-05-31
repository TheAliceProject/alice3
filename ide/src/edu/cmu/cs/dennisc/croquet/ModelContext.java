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

/*package-private*/ abstract class Event extends HistoryTreeNode {
	public Event( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public Event( ModelContext parent ) {
		super( parent );
	}
	public boolean isLeaf() {
		return true;
	}
	public java.util.Enumeration<HistoryTreeNode> children() {
		return null;
	}
	public boolean getAllowsChildren() {
		return false;
	}
	public javax.swing.tree.TreeNode getChildAt( int childIndex ) {
		return null;
	}
	public int getChildCount() {
		return 0;
	}
	public int getIndex( javax.swing.tree.TreeNode node ) {
		return -1;
	}
	
	@Deprecated
	public Model getModel() {
		ModelContext modelContext = this.getParent();
		ModelEvent modelEvent = (ModelEvent)modelContext.getChildAt( 0 );
		return modelEvent.getModel();
	}
}

/*package-private*/ abstract class ModelEvent< M extends Model, E extends java.util.EventObject, C extends Component< ? > > extends Event {
	private M model;
	private E event;
	private C component;
	public ModelEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public ModelEvent( ModelContext parent, M model, E event, C component ) {
		super( parent );
		this.model = model;
		this.event = event;
		this.component = component;
	}
	@Override
	public M getModel() {
		return this.model;
	}
	public E getEvent() {
		return this.event;
	}
	public C getComponent() {
		return this.component;
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
	
	@Override
	protected StringBuilder appendRepr( StringBuilder rv ) {
		super.appendRepr( rv );
		rv.append( " model:" );
		rv.append( this.model );
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ModelContext extends HistoryTreeNode {
	public interface CommitObserver {
		public void committing( Edit edit );
		public void committed( Edit edit );
	}
	public interface ChildrenObserver {
		public void addingChild( HistoryTreeNode child );
		public void addedChild( HistoryTreeNode child );
	}
	private java.util.List< CommitObserver > commitObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private java.util.List< ChildrenObserver > childrenObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private java.util.List< HistoryTreeNode > children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	/*package-private*/ ModelContext( ModelContext parent ) {
		super( parent );
	}
	public void addCommitObserver( CommitObserver commitObserver ) {
		this.commitObservers.add( commitObserver );
	}
	public void removeCommitObserver( CommitObserver commitObserver ) {
		this.commitObservers.remove( commitObserver );
	}
	public void addChildrenObserver( ChildrenObserver childrenObserver ) {
		this.childrenObservers.add( childrenObserver );
	}
	public void removeChildrenObserver( ChildrenObserver childrenObserver ) {
		this.childrenObservers.remove( childrenObserver );
	}

	public boolean isLeaf() {
		return false;
	}
	public java.util.Enumeration<HistoryTreeNode> children() {
		return java.util.Collections.enumeration( this.children );
	}
	public boolean getAllowsChildren() {
		return true;
	}
	public javax.swing.tree.TreeNode getChildAt( int childIndex ) {
		return this.children.get( childIndex );
	}
	public int getChildCount() {
		return this.children.size();
	}
	public int getIndex( javax.swing.tree.TreeNode node ) {
		return this.children.indexOf( node );
	}

	protected void fireCommitting( Edit edit ) {
		if( this.commitObservers.size() > 0 ) {
			for( CommitObserver commitObserver : this.commitObservers ) {
				commitObserver.committing( edit );
			}
		}
		ModelContext parent = this.getParent();
		if( parent != null ) {
			parent.fireCommitting( edit );
		}
	}
	protected void fireCommitted( Edit edit ) {
		if( this.commitObservers.size() > 0 ) {
			for( CommitObserver commitObserver : this.commitObservers ) {
				commitObserver.committed( edit );
			}
		}
		ModelContext parent = this.getParent();
		if( parent != null ) {
			parent.fireCommitted( edit );
		}
	}
	
	protected void fireAddingChild( HistoryTreeNode child ) {
		if( this.commitObservers.size() > 0 ) {
			for( ChildrenObserver childObserver : this.childrenObservers ) {
				childObserver.addingChild( child );
			}
		}
		ModelContext parent = this.getParent();
		if( parent != null ) {
			parent.fireAddingChild( child );
		}
	}
	protected void fireAddedChild( HistoryTreeNode child ) {
		if( this.commitObservers.size() > 0 ) {
			for( ChildrenObserver childObserver : this.childrenObservers ) {
				childObserver.addedChild( child );
			}
		}
		ModelContext parent = this.getParent();
		if( parent != null ) {
			parent.fireAddedChild( child );
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
	//todo
	public ModelContext getCurrentContext() {
		final int N = this.children.size();
		if( N > 0 ) {
			HistoryTreeNode lastChild = this.children.get( N - 1 );
			if( lastChild instanceof ModelContext ) {
				ModelContext lastContext = (ModelContext)lastChild;
				if( lastContext.getState() != null ) {
					return this;
				} else {
					return lastContext.getCurrentContext();
				}
			} else {
				return this;
			}
			
		} else {
			return this;
		}
	}
	
	
	public final boolean isCommitted() {
		return this.getState() == State.COMMITTED;
	}
	public final boolean isCanceled() {
		return this.getState() == State.CANCELED;
	}

	/*package-private*/ void addChild( HistoryTreeNode child ) {
		this.fireAddingChild( child );
		synchronized( this.children ) {
			this.children.add( child );
		}
		this.fireAddedChild( child );
	}
	
	/*package-private*/ModelContext createChildContext() {
		ModelContext rv = new ModelContext( this );
		this.addChild( rv );
		return rv;
	}
	public void finish() {
		this.addChild( new FinishEvent( this ) );
	}
	public void commitAndInvokeDo( Edit edit ) {
		assert edit != null;
		this.fireCommitting( edit );
		edit.doOrRedo( true );
		this.fireCommitted( edit );
		this.addChild( new CommitEvent( this, edit ) );
	}
	public void cancel() {
		this.addChild( new CancelEvent( this ) );
	}
	
	@Deprecated
	public void pend( Resolver< ?, ? > resolver ) {
		class PendTaskObserver< E extends Edit,F > implements edu.cmu.cs.dennisc.task.TaskObserver< F > {
			private ModelContext context;
			private Resolver<E,F> resolver;
			private E edit;
			public PendTaskObserver( ModelContext context, Resolver<E,F> resolver ) {
				this.context = context;
				this.resolver = resolver;
				this.edit = this.resolver.createEdit();
				java.util.UUID id = null;
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: pend id" );
				this.edit = this.resolver.initialize( this.edit, this.context, id, this );
			}
			public void handleCompletion(F f) {
				this.edit = this.resolver.handleCompletion( this.edit, f);
				this.context.commitAndInvokeDo( this.edit );
			}
			public void handleCancelation() {
				this.resolver.handleCancelation();
				this.context.cancel();
			}
		}
		new PendTaskObserver(this, resolver);
	}
	@Deprecated
	public void todo() {
		throw new RuntimeException( "todo" );
	}

	@Override
	protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		HistoryTreeNode[] array = binaryDecoder.decodeBinaryEncodableAndDecodableArray( HistoryTreeNode.class );
		edu.cmu.cs.dennisc.java.util.CollectionUtilities.set( this.children, array );
	}
	@Override
	protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		HistoryTreeNode[] array = new HistoryTreeNode[ this.children.size() ];
		this.children.toArray( array );
		binaryEncoder.encode( array );
	}
}
