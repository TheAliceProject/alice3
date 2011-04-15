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
public abstract class ModelContext<M extends Model> extends HistoryNode< ModelContext > {
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
	private java.util.List<HistoryNode<?>> children;
	private M model;
	private CodableResolver< M > modelResolver;
	private java.util.EventObject awtEvent;
	private ViewController<?, ?> viewController;
	/*package-private*/ ModelContext(M model, java.util.EventObject awtEvent, ViewController<?, ?> viewController) {
		this.children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.model = model;
		if( this.model != null ) {
			this.modelResolver = this.model.getCodableResolver();
		} else {
			this.modelResolver = null;
		}
		this.awtEvent = awtEvent;
		this.viewController = viewController;
	}
	/*package-private*/ ModelContext( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	@Deprecated
	public void EPIC_HACK_clear() {
		this.children.clear();
	}
	
	@Override
	protected final void decodeInternal(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		this.modelResolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
		HistoryNode[] array = binaryDecoder.decodeBinaryEncodableAndDecodableArray(HistoryNode.class);
		this.children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		edu.cmu.cs.dennisc.java.util.CollectionUtilities.set(this.children, array);
		for( HistoryNode node : this.children ) {
			node.setParent( this );
		}
	}
	@Override
	protected final void encodeInternal(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
//		M model = this.getModel();
//		CodableResolver< M > modelResolver;
//		if( model != null ) {
//			modelResolver = model.getCodableResolver();
//		} else {
//			modelResolver = null;
//		}
		binaryEncoder.encode( modelResolver );
		HistoryNode[] array = new HistoryNode[this.children.size()];
		this.children.toArray(array);
		binaryEncoder.encode(array);
	}

	@Override
	public void retarget( Retargeter retargeter ) {
		if( this.modelResolver != null ) {
			//pass
		} else {
			System.err.println( "MODEL RESOVER NULL: " + this );
//			Thread.dumpStack();
		}
		if( this.modelResolver instanceof RetargetableResolver< ? > ) {
			RetargetableResolver< ? > retargetableResolver = (RetargetableResolver< ? >)this.modelResolver;
			retargetableResolver.retarget( retargeter );
		}
		for( HistoryNode child : this.children ) {
			child.retarget( retargeter );
		}
	}

	public M getModel() {
		M rv;
		if( this.model != null ) {
			rv = this.model;
		} else {
			if( this.modelResolver != null ) {
				rv = this.modelResolver.getResolved(); 
				if( this.modelResolver instanceof RetargetableResolver< ? > ) {
					//pass
				} else {
					this.model = rv;
				}
			} else {
				rv = null;
			}
		}
		return rv;
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

	public java.util.ListIterator<HistoryNode<?>> getChildListIterator() {
		return this.children.listIterator();
	}
	public Iterable<HistoryNode<?>> getChildren() {
		return this.children;
	}
	public int getChildCount() {
		return this.children.size();
	}
	public HistoryNode<?> getChildAt(int childIndex) {
		return this.children.get(childIndex);
	}
	public int getIndexOfChild( HistoryNode<?> child ) {
		return this.children.indexOf( child );
	}
	public HistoryNode<?> getLastChild() {
		final int N = this.getChildCount();
		if( N > 0 ) {
			return this.getChildAt( N-1 );
		} else {
			return null;
		}
	}
	public <N extends HistoryNode> N getLastChildAssignableTo( Class<N> cls ) {
		final int N = this.getChildCount();
		int i=N-1;
		while( i >= 0 ) {
			HistoryNode node = this.getChildAt( i );
			if( cls.isAssignableFrom( node.getClass() ) ) {
				return cls.cast( node );
			}
			i--;
		}
		return null;
	}
	

	/*package-private*/ void popping() {
	}
	/*package-private*/ void popped() {
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
//				if( lastNode instanceof MenuModelContext.MenuDeselectedEvent ) { 
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
	//	public final boolean isCommitted() {
	//		return this.getState() == State.COMMITTED;
	//	}
	public final boolean isCanceled() {
		//todo
		for (HistoryNode node : this.children) {
//			if( node instanceof CancelEvent ) {
//				return true;
//			}
			if (node.getState() == State.CANCELED) {
				return true;
			}
		}
		return false;
	}
	
	public final boolean isSuccessfullyCompleted() {
		State state = this.getState();
		return state == State.COMMITTED || state == State.FINISHED;
	}
	
	public final SuccessfulCompletionEvent getSuccessfulCompletionEvent() {
		HistoryNode< ? > lastChild = this.getLastChild();
		if( lastChild instanceof SuccessfulCompletionEvent ) {
			return (SuccessfulCompletionEvent)lastChild;
		} else if( lastChild instanceof ModelContext< ? > ) {
			return ((ModelContext< ? >)lastChild).getSuccessfulCompletionEvent();
		} else {
			return null;
		}
//		final int N = this.children.size();
//		int i = N-1;
//		while( i >= 0 ) {
//			HistoryNode node = this.children.get( i );
//			if( node instanceof SuccessfulCompletionEvent ) {
//				return (SuccessfulCompletionEvent)node;
//			}
//			i --;
//		}
//		return null;
	}

	public void addChild(HistoryNode child) {
		this.addChild( -1, child );
	}
	public void addChild(int index, HistoryNode child) {
		this.fireAddingChild(child);
		synchronized (this.children) {
			if( index == -1 ) {
				this.children.add( child );
			} else {
				this.children.add( index, child );
			}
		}
		child.setParent( this );
		this.fireAddedChild(child);
	}
	
	public void finish() {
		this.addChild(new FinishEvent());
	}
	public void commitAndInvokeDo(Edit<M> edit) {
		assert edit != null;
		edit.setContext(this);
		this.fireCommitting(edit);
		edit.doOrRedo(true);
		this.fireCommitted(edit);
		this.addChild(new CommitEvent(edit));
	}
	public void cancel() {
		this.addChild(new CancelEvent());
	}
	
	public Edit< ? > getEdit() {
		for( HistoryNode historyNode : this.getChildren() ) {
			if( historyNode instanceof CommitEvent ) {
				CommitEvent commitEvent = (CommitEvent)historyNode;
				return commitEvent.getEdit();
			}
		}
		return null;
	}

	@Override
	public String getTutorialStepTitle( UserInformation userInformation ) {
		Model model = this.getModel();
		return model.getTutorialStepTitle( this, userInformation );
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
