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
	public Event( Context parent ) {
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
}

abstract class OperationEvent< O extends Operation, E extends java.util.EventObject, C extends Component< ? > > extends Event {
	private O operation;
	private E event;
	private C component;
	public OperationEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public OperationEvent( Context parent, O operation, E event, C component ) {
		super( parent );
		this.operation = operation;
		this.event = event;
		this.component = component;
	}
	public O getOperation() {
		return this.operation;
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
		rv.append( " operation:" );
		rv.append( this.operation );
		return rv;
	}
}
/*package-private*/ class ActionEvent extends OperationEvent<AbstractActionOperation,java.util.EventObject,Component< ? >> {
	public ActionEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public ActionEvent( Context parent, AbstractActionOperation actionOperation, java.util.EventObject e, Component< ? > component ) {
		super( parent, actionOperation, e, component );
	}
}

/*package-private*/ class BoundedRangeIntegerStateEvent extends OperationEvent {
	public BoundedRangeIntegerStateEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public BoundedRangeIntegerStateEvent( Context parent, BoundedRangeIntegerStateOperation boundedRangeIntegerStateOperation, javax.swing.event.ChangeEvent e ) {
		super( parent, boundedRangeIntegerStateOperation, e, null );
	}
}

/*package-private*/ class BooleanStateEvent extends OperationEvent {
	public BooleanStateEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public BooleanStateEvent( Context parent, BooleanStateOperation booleanStateOperation, java.awt.event.ItemEvent e ) {
		super( parent, booleanStateOperation, e, null );
	}
}

/*package-private*/ class StringStateEvent extends OperationEvent {
	public StringStateEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public StringStateEvent( Context parent, StringStateOperation stringStateOperation, javax.swing.event.DocumentEvent e ) {
		super( parent, stringStateOperation, null, null );
		//note: document event not java.util.EventObject 
	}
}

/*package-private*/ class ItemSelectionEvent< T > extends OperationEvent {
	private int prevIndex;
	private T prevItem;
	private int nextIndex;
	private T nextItem;
	public ItemSelectionEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public ItemSelectionEvent( Context parent, ItemSelectionOperation< T > itemSelectionOperation, java.util.EventObject e, Component< ? > component, int prevIndex, T prevItem, int nextIndex, T nextItem ) {
		super( parent, itemSelectionOperation, e, component );
		this.prevIndex = prevIndex;
		this.prevItem = prevItem;
		this.nextIndex = nextIndex;
		this.nextItem = nextItem;
	}
	@Override
	protected StringBuilder appendRepr( StringBuilder rv ) {
		rv = super.appendRepr( rv );
		rv.append( " " );
		rv.append( this.nextItem );
		rv.append( " " );
		rv.append( this.getComponent() );
		return rv;
	}
}

///*package-private*/ class MenuBarStateChangeEvent extends OperationEvent {
//	public MenuBarStateChangeEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//		super( binaryDecoder );
//	}
//	public MenuBarStateChangeEvent( Context parent, MenuBarOperation menuBarOperation, javax.swing.event.ChangeEvent e, KMenuBar menuBar, javax.swing.SingleSelectionModel singleSelectionModel, int index, MenuOperation menuOperationAtIndex ) {
//		super( parent, menuBarOperation, e, menuBar );
//	}
//}
/*package-private*/ abstract class MenuEvent extends OperationEvent {
	public MenuEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public MenuEvent( Context parent, MenuOperation menuOperation, javax.swing.event.MenuEvent e, Menu menu ) {
		super( parent, menuOperation, e, menu );
	}
}

/*package-private*/ class MenuSelectedEvent extends MenuEvent {
	public MenuSelectedEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public MenuSelectedEvent( Context parent, MenuOperation menuOperation, javax.swing.event.MenuEvent e, Menu menu ) {
		super( parent, menuOperation, e, menu );
	}
}
/*package-private*/ class MenuDeselectedEvent extends MenuEvent {
	public MenuDeselectedEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public MenuDeselectedEvent( Context parent, MenuOperation menuOperation, javax.swing.event.MenuEvent e, Menu menu ) {
		super( parent, menuOperation, e, menu );
	}
}
/*package-private*/ class MenuCanceledEvent extends MenuEvent {
	public MenuCanceledEvent( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public MenuCanceledEvent( Context parent, MenuOperation menuOperation, javax.swing.event.MenuEvent e, Menu menu ) {
		super( parent, menuOperation, e, menu );
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
		return State.CANCELED;
	}
}



/**
 * @author Dennis Cosgrove
 */
public class Context extends HistoryTreeNode {
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
		Context parent = this.getParent();
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
		Context parent = this.getParent();
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
		Context parent = this.getParent();
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
		Context parent = this.getParent();
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
	public Context getCurrentContext() {
		final int N = this.children.size();
		if( N > 0 ) {
			HistoryTreeNode lastChild = this.children.get( N - 1 );
			if( lastChild instanceof Context ) {
				Context lastContext = (Context)lastChild;
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
	
	/*package-private*/Context createChildContext() {
		Context rv = new Context( this );
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
			private Context context;
			private Resolver<E,F> resolver;
			private E edit;
			public PendTaskObserver( Context context, Resolver<E,F> resolver ) {
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
//	/*package-private*/void handleItemStateChanged( BooleanStateOperation booleanStateOperation, java.awt.event.ItemEvent e ) {
//		this.addChild( new BooleanStateEvent( this, booleanStateOperation, e ) );
//		booleanStateOperation.perform( this, e );
//	}
//	/*package-private*/<T> void handleItemStateChanged( ItemSelectionOperation< T > itemSelectionOperation, java.awt.event.ItemEvent e ) {
//		this.addChild( new ItemSelectionEvent< T >( this, itemSelectionOperation, e ) );
//		itemSelectionOperation.perform( this, e );
//	}
//
//	/*package-private*/void handleActionPerformed( AbstractActionOperation actionOperation, java.awt.event.ActionEvent e, KAbstractButton< ? > button ) {
//		this.addChild( new ActionEvent( this, actionOperation, e, button ) );
//		actionOperation.perform( this, e, button );
//	}
//
//	/*package-private*/ void handleStateChanged( MenuBarOperation menuBarOperation, javax.swing.event.ChangeEvent e, KMenuBar menuBar, javax.swing.SingleSelectionModel singleSelectionModel, int index, MenuOperation menuOperationAtIndex ) {
//	}

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

//	class DefaultDragAndDropContext extends AbstractContext< DragAndDropOperation > implements DragAndDropContext {
//		private DropReceptorInfo[] potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
//		private DropReceptor currentDropReceptor;
//		private java.awt.event.MouseEvent latestMouseEvent;
//
//		public DefaultDragAndDropContext( DragAndDropOperation operation, java.awt.event.MouseEvent originalMouseEvent, java.awt.event.MouseEvent latestMouseEvent, java.util.List< ? extends DropReceptor > potentialDropReceptors ) {
//			super( operation, originalMouseEvent, ZManager.CANCEL_IS_WORTHWHILE );
//			this.setLatestMouseEvent( latestMouseEvent );
//			this.potentialDropReceptorInfos = new DropReceptorInfo[ potentialDropReceptors.size() ];
//			int i = 0;
//			for( DropReceptor dropReceptor : potentialDropReceptors ) {
//				KComponent< ? > dropComponent = dropReceptor.getComponent();
//				java.awt.Rectangle bounds = dropComponent.getBounds();
//				bounds = dropComponent.getParent().convertRectangle( bounds, this.getDragSource() );
//				this.potentialDropReceptorInfos[ i ] = new DropReceptorInfo( dropReceptor, bounds );
//				i++;
//			}
//			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
//				//todo: pass original mouse pressed event?
//				dropReceptorInfo.getDropReceptor().dragStarted( this );
//			}
//		}
//
//		public java.awt.event.MouseEvent getOriginalMouseEvent() {
//			return (java.awt.event.MouseEvent)this.getEvent();
//		}
//		public java.awt.event.MouseEvent getLatestMouseEvent() {
//			return this.latestMouseEvent;
//		}
//		public void setLatestMouseEvent( java.awt.event.MouseEvent latestMouseEvent ) {
//			this.latestMouseEvent = latestMouseEvent;
//		}
//
//		public KDragControl getDragSource() {
//			java.util.EventObject e = getEvent();
//			if( e != null ) {
//				return (KDragControl)e.getSource();
//			} else {
//				return null;
//			}
//		}
//		public DropReceptor getCurrentDropReceptor() {
//			return this.currentDropReceptor;
//		}
//		public void setCurrentDropReceptor( DropReceptor currentDropReceptor ) {
//			this.currentDropReceptor = currentDropReceptor;
//		}
//		private DropReceptor getDropReceptorUnder( int x, int y ) {
//			DropReceptor rv = null;
//			int prevHeight = Integer.MAX_VALUE;
//			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
//				assert dropReceptorInfo != null;
//				if( dropReceptorInfo.contains( x, y ) ) {
//					java.awt.Rectangle bounds = dropReceptorInfo.getBounds();
//					assert bounds != null;
//					int nextHeight = bounds.height;
//					if( nextHeight < prevHeight ) {
//						rv = dropReceptorInfo.getDropReceptor();
//						prevHeight = nextHeight;
//					}
//				}
//			}
//			return rv;
//		}
//		private DropReceptor getDropReceptorUnder( java.awt.Rectangle bounds ) {
//			DropReceptor rv = null;
//			int prevHeight = Integer.MAX_VALUE;
//			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
//				if( dropReceptorInfo.intersects( bounds ) ) {
//					int nextHeight = dropReceptorInfo.getBounds().height;
//					if( nextHeight < prevHeight ) {
//						rv = dropReceptorInfo.getDropReceptor();
//						prevHeight = nextHeight;
//					}
//				}
//			}
//			return rv;
//		}
//		protected DropReceptor getDropReceptorUnder( java.awt.event.MouseEvent e ) {
//			DropReceptor rv = getDropReceptorUnder( e.getX(), e.getY() );
//			if( rv != null ) {
//				//pass
//			} else {
//				if( KDragControl.this.dragProxy != null ) {
//					java.awt.Rectangle dragBounds = KDragControl.this.dragProxy.getBounds();
//					dragBounds = javax.swing.SwingUtilities.convertRectangle( KDragControl.this.dragProxy.getParent(), dragBounds, this.getDragSource().getJComponent() );
//					int x = dragBounds.x;
//					int y = dragBounds.y + dragBounds.height / 2;
//					rv = getDropReceptorUnder( x, y );
//					if( rv != null ) {
//						//pass
//					} else {
//						rv = getDropReceptorUnder( dragBounds );
//					}
//				}
//			}
//			return rv;
//		}
//		public void handleMouseDragged( java.awt.event.MouseEvent e ) {
//			this.setLatestMouseEvent( e );
//			DropReceptor nextDropReceptor = getDropReceptorUnder( e );
//			if( this.currentDropReceptor != nextDropReceptor ) {
//				if( this.currentDropReceptor != null ) {
//					KDragControl.this.dragAndDropOperation.handleDragExitedDropReceptor( this );
//					this.currentDropReceptor.dragExited( this, false );
//				}
//				this.currentDropReceptor = nextDropReceptor;
//				if( this.currentDropReceptor != null ) {
//					this.currentDropReceptor.dragEntered( this );
//					KDragControl.this.dragAndDropOperation.handleDragEnteredDropReceptor( this );
//				}
//			}
//			if( KDragControl.this.dragProxy != null ) {
//				KDragControl.this.dragProxy.setOverDropAcceptor( this.currentDropReceptor != null );
//			}
//			if( this.currentDropReceptor != null ) {
//				this.currentDropReceptor.dragUpdated( this );
//			}
//		}
//		public void handleMouseReleased( java.awt.event.MouseEvent e ) {
//			this.setLatestMouseEvent( e );
//			if( this.currentDropReceptor != null ) {
//				this.currentDropReceptor.dragDropped( this );
//				this.currentDropReceptor.dragExited( this, true );
//			}
//			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
//				dropReceptorInfo.getDropReceptor().dragStopped( this );
//			}
//			KDragControl.this.dragAndDropOperation.handleDragStopped( this );
//			this.potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
//		}
//		public void handleCancel( java.util.EventObject e ) {
//			if( this.currentDropReceptor != null ) {
//				this.currentDropReceptor.dragExited( this, false );
//			}
//			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
//				dropReceptorInfo.getDropReceptor().dragStopped( this );
//			}
//			KDragControl.this.dragAndDropOperation.handleDragStopped( this );
//			this.potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
//			KDragControl.this.hideDropProxyIfNecessary();
//		}
//	}
//	public DragControl getDragSource() {
//		throw new RuntimeException( "todo" );
//	}
//	public DropReceptor getCurrentDropReceptor() {
//		throw new RuntimeException( "todo" );
//	}
//	@Deprecated
//	public java.awt.event.MouseEvent getMouseEvent() {
//		throw new RuntimeException( "todo" );
//	}
}
