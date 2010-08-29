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

/*package-private*/abstract class Event<C extends ModelContext<?>> extends HistoryNode {
	public Event(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		super(binaryDecoder);
	}
	public Event(C parent) {
		super(parent);
	}
}

/*package-private*/abstract class ModelEvent<C extends ModelContext<?>> extends Event<C> {
	public ModelEvent(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		super(binaryDecoder);
	}
	public ModelEvent(C parent) {
		super(parent);
	}
	@Override
	protected void decodeInternal(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
	}
	@Override
	protected void encodeInternal(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
	}
	//	
}

/**
 * @author Dennis Cosgrove
 */
public abstract class ModelContext<M extends Model> extends HistoryNode {
	public interface CommitObserver {
		public void committing(Edit edit);
		public void committed(Edit edit);
	}
	public interface ChildrenObserver {
		public void addingChild(HistoryNode child);
		public void addedChild(HistoryNode child);
	}
	private java.util.List<CommitObserver> commitObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private java.util.List<ChildrenObserver> childrenObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private java.util.List<HistoryNode> children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private M model;
	private java.util.EventObject awtEvent;
	private ViewController<?, ?> viewController;
	/*package-private*/ModelContext(ModelContext<?> parent, M model, java.util.EventObject awtEvent, ViewController<?, ?> viewController) {
		super(parent);
		this.model = model;
		this.awtEvent = awtEvent;
		this.viewController = viewController;
	}
	public M getModel() {
		return this.model;
	}
	public java.util.EventObject getAwtEvent() {
		return this.awtEvent;
	}
	public ViewController<?, ?> getViewController() {
		return this.viewController;
	}
	public void addCommitObserver(CommitObserver commitObserver) {
		this.commitObservers.add(commitObserver);
	}
	public void removeCommitObserver(CommitObserver commitObserver) {
		this.commitObservers.remove(commitObserver);
	}
	public void addChildrenObserver(ChildrenObserver childrenObserver) {
		this.childrenObservers.add(childrenObserver);
	}
	public void removeChildrenObserver(ChildrenObserver childrenObserver) {
		this.childrenObservers.remove(childrenObserver);
	}
	@Override
	public ModelContext<?> findContextFor(Model model) {
		if (this.model == model) {
			return this;
		} else {
			return super.findContextFor(model);
		}
	}

	public boolean isLeaf() {
		return false;
	}
	//public java.util.Iterator<HistoryTreeNode> iterator() {
	public java.util.Iterator iterator() {
		return this.children.iterator();
	}
	public java.util.Enumeration<HistoryNode> children() {
		return java.util.Collections.enumeration(this.children);
	}
	public boolean getAllowsChildren() {
		return true;
	}
	public HistoryNode getChildAt(int childIndex) {
		return this.children.get(childIndex);
	}
	public int getChildCount() {
		return this.children.size();
	}
	public int getIndex(HistoryNode child) {
		return this.children.indexOf(child);
	}
	public int getIndexOfChild( HistoryNode child ) {
		return this.children.indexOf( child );
	}

	protected void fireCommitting(Edit edit) {
		if (this.commitObservers.size() > 0) {
			for (CommitObserver commitObserver : this.commitObservers) {
				commitObserver.committing(edit);
			}
		}
		ModelContext<?> parent = this.getParent();
		if (parent != null) {
			parent.fireCommitting(edit);
		}
	}
	protected void fireCommitted(Edit edit) {
		if (this.commitObservers.size() > 0) {
			for (CommitObserver commitObserver : this.commitObservers) {
				commitObserver.committed(edit);
			}
		}
		ModelContext<?> parent = this.getParent();
		if (parent != null) {
			parent.fireCommitted(edit);
		}
	}

	protected void fireAddingChild(HistoryNode child) {
		if (this.childrenObservers.size() > 0) {
			for (ChildrenObserver childObserver : this.childrenObservers) {
				childObserver.addingChild(child);
			}
		}
		ModelContext<?> parent = this.getParent();
		if (parent != null) {
			parent.fireAddingChild(child);
		}
	}
	protected void fireAddedChild(HistoryNode child) {
		if (this.childrenObservers.size() > 0) {
			for (ChildrenObserver childObserver : this.childrenObservers) {
				childObserver.addedChild(child);
			}
		}
		ModelContext<?> parent = this.getParent();
		if (parent != null) {
			parent.fireAddedChild(child);
		}
	}

	@Override
	public State getState() {
		final int N = this.children.size();
		if (N > 0) {
			State state = this.children.get(N - 1).getState();
//			//todo
//			if( state == State.DESELECTED ) {
//				HistoryNode lastNode = this.children.get(N - 1);
//				if( lastNode instanceof edu.cmu.cs.dennisc.croquet.MenuModelContext.MenuDeselectedEvent ) { 
//					//pass
//				} else {
//					return null;
//				}
//			}
			return state;
		} else {
			return null;
		}
	}
//	//todo
//	public ModelContext<?> getCurrentContext() {
//		final int N = this.children.size();
//		if (N > 0) {
//			HistoryNode lastChild = this.children.get(N - 1);
//			if (lastChild instanceof ModelContext<?>) {
//				ModelContext<?> lastContext = (ModelContext<?>) lastChild;
//				State state = lastContext.getState();
//				if( state != null ) {
//					return this;
//				} else {
//					return lastContext.getCurrentContext();
//				}
//			} else {
//				return this;
//			}
//
//		} else {
//			return this;
//		}
//	}

	//	public final boolean isCommitted() {
	//		return this.getState() == State.COMMITTED;
	//	}
	public final boolean isCanceled() {
		//todo
		for (HistoryNode node : this.children) {
			if (node.getState() == State.CANCELED) {
				return true;
			}
		}
		return false;
	}

	protected void addChild(HistoryNode child) {
		this.fireAddingChild(child);
		synchronized (this.children) {
			this.children.add(child);
		}
		this.fireAddedChild(child);
	}

	public void finish() {
		this.addChild(new FinishEvent(this));
	}
	public void commitAndInvokeDo(Edit edit) {
		assert edit != null;
		edit.setContext(this);
		this.fireCommitting(edit);
		edit.doOrRedo(true);
		this.fireCommitted(edit);
		this.addChild(new CommitEvent(this, edit));
	}
	public void cancel() {
		this.addChild(new CancelEvent(this));
	}

//	@Deprecated
//	public void pend(PendResolver<?, ?> resolver) {
//		class PendTaskObserver<E extends Edit, F> implements edu.cmu.cs.dennisc.task.TaskObserver<F> {
//			private ModelContext context;
//			private PendResolver<E, F> resolver;
//			private E edit;
//			public PendTaskObserver(ModelContext context, PendResolver<E, F> resolver) {
//				this.context = context;
//				this.resolver = resolver;
//				this.edit = this.resolver.createEdit();
//				java.util.UUID id = null;
//				edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: pend id");
//				this.edit = this.resolver.initialize(this.edit, this.context, id, this);
//			}
//			public void handleCompletion(F f) {
//				this.edit = this.resolver.handleCompletion(this.edit, f);
//				this.context.commitAndInvokeDo(this.edit);
//			}
//			public void handleCancelation() {
//				this.resolver.handleCancelation();
//				this.context.cancel();
//			}
//		}
//		new PendTaskObserver(this, resolver);
//	}
//	@Deprecated
//	public void todo() {
//		throw new RuntimeException("todo");
//	}

	@Override
	protected void decodeInternal(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		HistoryNode[] array = binaryDecoder.decodeBinaryEncodableAndDecodableArray(HistoryNode.class);
		edu.cmu.cs.dennisc.java.util.CollectionUtilities.set(this.children, array);
	}
	@Override
	protected void encodeInternal(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		HistoryNode[] array = new HistoryNode[this.children.size()];
		this.children.toArray(array);
		binaryEncoder.encode(array);
	}
	@Override
	protected StringBuilder appendRepr( StringBuilder rv ) {
		super.appendRepr( rv );
		State state = this.getState();
		rv.append( "[" );
		rv.append( state );
		rv.append( "]" );
		
//		if( state != null ) {
//			//pass
//		} else {
			rv.append( " " );
			rv.append( this.getModel() );
//		}
		return rv;
	}
}
