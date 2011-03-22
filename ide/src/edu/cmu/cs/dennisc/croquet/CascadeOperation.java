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

abstract class RtModel< M extends Model, C extends ModelContext< M > > {
	private M model;
	private C context;
	public RtModel( M model, C context ) {
		assert model != null;
		assert context != null;
		this.model = model;
		this.context = context;
	}
	public M getModel() {
		return this.model;
	}
	public C getContext() {
		return this.context;
	}
}

abstract class RtNode< M extends Model, C extends ModelContext< M > > extends RtModel< M,C > {
	private RtNode<?,?> parent;
	private RtNode<?,?> nextSibling;
	
	public RtNode( M model, C context ) {
		super( model, context );
	}
	protected RtNode< ?, ? > getParent() {
		return this.parent;
	}
	protected RtNode< ?, ? > getNextSibling() {
		return this.nextSibling;
	}
	public void setParent( RtNode< ?, ? > parent ) {
		this.parent = parent;
	}
	public void setNextSibling( RtNode< ?, ? > nextSibling ) {
		this.nextSibling = nextSibling;
	}
	
	protected void updateParentsAndNextSiblings( RtNode< ?,? >[] rtNodes ) {
		for( RtNode< ?,? > rtNode : rtNodes ) {
			rtNode.setParent( this );
		}
		if( rtNodes.length > 0 ) {
			RtNode< ?,? > rtNodeA = rtNodes[ 0 ];
			for( int i=1; i<rtNodes.length; i++ ) {
				RtNode< ?,? > rtNodeB = rtNodes[ i ];
				rtNodeA.setNextSibling( rtNodeB );
				rtNodeA = rtNodeB;
			}
			rtNodeA.setNextSibling( null );
		}
	}
	
	public RtRootFillIn< ? > getRtRootFillIn() {
		return this.parent.getRtRootFillIn();
	}
	
	protected abstract RtNode[] getChildren();
	protected abstract RtNode< ?,? > getNextNode();
	public abstract RtBlank< ? > getNearestBlank();
	public RtBlank< ? > getNextBlank() {
		RtBlank< ? > blank = this.getNearestBlank();
		if( blank != null && blank.getNextSibling() != null ) {
			return (RtBlank< ? >)blank.getNextSibling();
		} else {
			if( this.parent != null ) {
				return this.parent.getNextBlank();
			} else {
				return null;
			}
		}
	}
	protected void addNextNodeMenuItems( javax.swing.JComponent parent ) {
		for( RtNode child : this.getNextNode().getChildren() ) {
			if( child instanceof RtBlank ) {
				child = child.getChildren()[ 0 ];
			}
			RtAbstractFillIn< ?,?,?,? > rtFillIn = (RtAbstractFillIn< ?,?,?,? >)child;
			javax.swing.JComponent menuItem = rtFillIn.getMenuItem();
			if( menuItem != null ) {
				parent.add( menuItem );
			} else {
				if( parent instanceof javax.swing.JMenu ) {
					javax.swing.JMenu menu = (javax.swing.JMenu)parent;
					menu.addSeparator();
				} else if( parent instanceof javax.swing.JPopupMenu ) {
					javax.swing.JPopupMenu popupMenu = (javax.swing.JPopupMenu)parent;
					popupMenu.addSeparator();
				}
			}
		}
	}
}

class RtBlank< B > extends RtNode< CascadeBlank<B>, CascadeBlankContext<B> > {
	private static <F, B, M extends AbstractCascadeFillIn< F,B,M,C >, C extends AbstractCascadeFillInContext< F,B,M,C > > boolean isEmptySeparator( RtAbstractFillIn<F,B,M,C> rtOwnee ) {
		 if( rtOwnee.getModel() instanceof CascadeSeparator ) {
			 CascadeSeparator separatorFillIn = (CascadeSeparator)rtOwnee.getModel();
			 return separatorFillIn.isEmpty();
		 } else {
			 return false;
		 }
	}
	private static <F, B, M extends AbstractCascadeFillIn< F,B,M,C >, C extends AbstractCascadeFillInContext< F,B,M,C > > void cleanUpSeparators( java.util.List<RtAbstractFillIn<F,B,M,C>> rtOwnees ) {
		 java.util.ListIterator< RtAbstractFillIn<F,B,M,C> > listIterator = rtOwnees.listIterator();
		 boolean isSeparatorAcceptable = false;
		 while( listIterator.hasNext() ) {
			 RtAbstractFillIn<F,B,M,C> rtOwnee = listIterator.next();
			 if( isEmptySeparator( rtOwnee ) ) {
				 if( isSeparatorAcceptable ) {
					//pass 
				 } else {
					 listIterator.remove();
				 }
				 isSeparatorAcceptable = false;
			 } else {
				 isSeparatorAcceptable = true;
			 }
		 }
		 
		 //remove separators at the end
		 //there should be a maximum of only 1 but we loop anyway 
		 final int N = rtOwnees.size();
		 for( int i=0; i<N; i++ ) {
			 int index = N-i-1;
			 if( isEmptySeparator( rtOwnees.get( index ) ) ) {
				 rtOwnees.remove( index );
			 } else {
				 break;
			 }
		 }
	}

	private RtAbstractFillIn[] rtFillIns;
	private RtAbstractFillIn< B,?,?,? > rtSelectedFillIn;
	public RtBlank( CascadeBlank<B> model ) {
		super( model, ContextManager.createCascadeBlankContext( model ) );
		this.getContext().setRtBlank( this );
	}

	public AbstractCascadeFillInContext getSelectedFillInContext() {
		if( this.rtSelectedFillIn != null ) {
			return this.rtSelectedFillIn.getContext();
		} else {
			return null;
		}
	}
	
	@Override
	protected RtAbstractFillIn[] getChildren() {
		if( this.rtFillIns != null ) {
			//pass
		} else {
			java.util.List< RtAbstractFillIn > baseRtFillIns = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( AbstractCascadeFillIn ownee : this.getModel().getOwnees() ) {
				RtAbstractFillIn rtOwnee;
				if( ownee instanceof CascadeMenu ) {
					CascadeMenu menu = (CascadeMenu)ownee;
					rtOwnee = new RtMenu< B >( menu );
				} else if( ownee instanceof CascadeFillIn ) {
					CascadeFillIn fillIn = (CascadeFillIn)ownee;
					rtOwnee = new RtFillIn( fillIn );
				} else if( ownee instanceof CascadeSeparator ) {
					CascadeSeparator separator = (CascadeSeparator)ownee;
					rtOwnee = new RtSeparator( separator );
				} else {
					rtOwnee = null;
				}
				baseRtFillIns.add( rtOwnee );
			}
			
			java.util.ListIterator< RtAbstractFillIn > listIterator = baseRtFillIns.listIterator();
			while( listIterator.hasNext() ) {
				RtAbstractFillIn rtOwnee = listIterator.next();
				if( rtOwnee.isInclusionDesired() ) {
					//pass
				} else {
					listIterator.remove();
				}
			}
			
			//todo
			cleanUpSeparators( (java.util.List)baseRtFillIns );
		
			this.rtFillIns = edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( (java.util.List)baseRtFillIns, RtAbstractFillIn.class );
			this.updateParentsAndNextSiblings( this.rtFillIns );
		}
		return this.rtFillIns;
	}
	@Override
	protected RtNode< ?, ? > getNextNode() {
		return this;
	}
	@Override
	public RtBlank< ? > getNearestBlank() {
		return this;
	}
	
	public void setSelectedFillIn( RtAbstractFillIn< B,?,?,? > fillIn ) {
		this.rtSelectedFillIn = fillIn;
		RtNode parent = this.getParent();
		if( parent instanceof RtFillIn<?,?> ) {
			RtFillIn<?,?> parentFillIn = (RtFillIn<?,?>)parent;
			for( RtBlank blank : parentFillIn.getChildren() ) {
				if( blank.rtSelectedFillIn != null ) {
					//pass
				} else {
					return;
				}
			}
			parentFillIn.select();
		}
	}

	public boolean isFillInAlreadyDetermined() {
		return this.rtSelectedFillIn != null;
	}

	public B createValue() {
		if( this.rtSelectedFillIn != null ) {
			return this.rtSelectedFillIn.createValue();
		} else {
			throw new RuntimeException();
		}
	}
}

abstract class RtAbstractFillIn<F,B, M extends AbstractCascadeFillIn<F,B,M,C>, C extends AbstractCascadeFillInContext<F,B,M,C> > extends RtNode<M, C> {
	private final RtBlank< B >[] rtBlanks;
	private javax.swing.JMenuItem menuItem = null;
	private boolean wasLast = false; 
	public RtAbstractFillIn( M model, C context ) {
		super( model, context );
		this.getContext().setRtFillIn( this );
		CascadeBlank<B>[] blanks = model.getBlanks();
		final int N;
		if( blanks != null ) {
			N = blanks.length;
		} else {
			N = 0;
		}
		this.rtBlanks = new RtBlank[ N ];
		for( int i=0; i<this.rtBlanks.length; i++ ) {
			this.rtBlanks[ i ] = new RtBlank< B >( blanks[ i ] );
		}
		this.updateParentsAndNextSiblings( this.rtBlanks );
	}

	public CascadeBlankContext< B > getBlankContextAt( int i ) {	
		return this.rtBlanks[ i ].getContext();
	}
	@Override
	public RtBlank< ? > getNearestBlank() {
		RtNode<?,?> parent = this.getParent();
		return parent.getNearestBlank();
	}
	@Override
	protected RtBlank< B >[] getChildren() {
		return this.rtBlanks;
	}
	protected boolean isGoodToGo() {
		if( this.rtBlanks.length > 0 ) {
			for( RtBlank< B > rtBlank : this.rtBlanks ) {
				if( rtBlank.isFillInAlreadyDetermined() ) {
					//pass
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isInclusionDesired() {
		return this.getModel().isInclusionDesired( this.getContext() );
	}
	@Override
	protected RtNode< ?, ? > getNextNode() {
		if( this.rtBlanks.length > 0 ) {
			return this.rtBlanks[ 0 ];
		} else {
			return this.getNextBlank();
		}
	}
	public F createValue() {
		return this.getModel().createValue( this.getContext() );
	}
	protected boolean isLast() {
		return this.getNextNode() == null;
	}
	public void select() {
		//todo
		RtBlank nearestBlank = this.getNearestBlank();
		assert nearestBlank != null : this;
		nearestBlank.setSelectedFillIn( (RtAbstractFillIn)this );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "select:", this );
	}
	public void deselect() {
		//todo?
		//getNearestBlank().setSelectedFillIn( null );
	}

	private javax.swing.JMenu getMenu() {
		return (javax.swing.JMenu)this.getMenuItem();
	}

	private java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			RtAbstractFillIn.this.select();
			RtAbstractFillIn.this.getRtRootFillIn().handleActionPerformed( e );
		}
	};
	private javax.swing.event.MenuListener menuListener = new javax.swing.event.MenuListener() {
		public void menuSelected( javax.swing.event.MenuEvent e ) {
			RtAbstractFillIn.this.select();
			RtAbstractFillIn.this.addNextNodeMenuItems( RtAbstractFillIn.this.menuItem );
		}
		public void menuDeselected( javax.swing.event.MenuEvent e ) {
			RtAbstractFillIn.this.deselect();
			RtAbstractFillIn.this.getMenu().removeAll();
		}
		public void menuCanceled( javax.swing.event.MenuEvent e ) {
		}
	};
	
	public javax.swing.JComponent getMenuItem() {
		AbstractCascadeFillIn< F,B,M,C > fillIn = this.getModel();
		boolean isLast = this.isLast();
		if( this.menuItem != null ) {
			if( this.wasLast == isLast ) {
				//pass
			} else {
				if( this.menuItem instanceof javax.swing.JMenu ) {
					javax.swing.JMenu menu = (javax.swing.JMenu)this.menuItem;
					menu.removeMenuListener( this.menuListener );
				} else {
					this.menuItem.removeActionListener( this.actionListener );
				}
				this.menuItem = null;
			}
		} else {
			//pass
		}
		if( this.menuItem != null ) {
			//pass
		} else {
			if( isLast ) {
				this.menuItem = new javax.swing.JMenuItem();
				this.menuItem.addActionListener( this.actionListener );
			} else {
				javax.swing.JMenu menu = new javax.swing.JMenu();
				menu.addMenuListener( this.menuListener );
				this.menuItem = menu;
			}
			this.menuItem.setText( fillIn.getMenuItemText( this.getContext() ) );
			this.menuItem.setIcon( fillIn.getMenuItemIcon( this.getContext() ) );
		}
		return this.menuItem;
	}
}

class RtFillIn< F,B > extends RtAbstractFillIn< F,B, CascadeFillIn< F,B >, CascadeFillInContext< F,B > > {
	public RtFillIn( CascadeFillIn< F,B > model ) {
		super( model, ContextManager.createCascadeFillInContext( model ) );
	}
}

class RtMenu< F > extends RtAbstractFillIn<F,F,CascadeMenu< F >,CascadeMenuContext< F >> {
	public RtMenu( CascadeMenu< F > model ) {
		super( model, ContextManager.createCascadeMenuContext( model ) );
	}
}

class RtSeparator extends RtAbstractFillIn {
	public RtSeparator( CascadeSeparator model ) {
		super( model, ContextManager.createCascadeSeparatorContext( model ) );
	}
	@Override
	public javax.swing.JComponent getMenuItem() {
		return null;
	}
	@Override
	public RtBlank getNearestBlank() {
		return null;
	}
	@Override
	protected RtNode getNextNode() {
		return null;
	}
}

class RtRootFillIn< T > extends RtFillIn< T[], T > { 
	private final RtOperation<T> rtOperation;
	public RtRootFillIn( RootFillIn< T > model, RtOperation<T> rtOperation ) {
		super( model );
		this.rtOperation = rtOperation;
	}
	@Override
	public RtRootFillIn< ? > getRtRootFillIn() {
		return this;
	}
	@Override
	public RtBlank< ? > getNearestBlank() {
		return null;
	}
	@Override
	public void select() {
	}
	protected void handleActionPerformed( java.awt.event.ActionEvent e ) {
		this.rtOperation.handleActionPerformed( e );
	}
}

class RtOperation<T> extends RtModel< CascadeOperation< T >, CascadeOperationContext< T > > {
	private Operation.PerformObserver performObserver;
	private RtRootFillIn< T > rtRootFillIn;
	public RtOperation( CascadeOperation< T > model, CascadeOperationContext<T> context, Operation.PerformObserver performObserver ) {
		super( model, context );
		this.performObserver = performObserver;
		this.rtRootFillIn = new RtRootFillIn< T >( model.getFillIn(), this );
	}
	protected T[] createValues( Class<T> componentType ) {
		RtBlank< T >[] rtBlanks = this.rtRootFillIn.getChildren();
		T[] rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newTypedArrayInstance( componentType, rtBlanks.length );
		for( int i=0; i<rtBlanks.length; i++ ) {
			rv[ i ] = rtBlanks[ i ].createValue();
		}
		return rv;
	}
	public void perform() {
		if( this.rtRootFillIn.isGoodToGo() ) {
			T[] values = this.createValues( this.getModel().getComponentType() );
			this.getModel().handleCompletion( this.getContext(), this.performObserver, values );
		} else {
			final javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
			//popupMenu.setLightWeightPopupEnabled( false );
			popupMenu.addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
				public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
					RtOperation.this.rtRootFillIn.addNextNodeMenuItems( popupMenu );
				}
				public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
					popupMenu.removeAll();
				}
				public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
					RtOperation.this.handleCancel( e );
				}
			} );
			java.util.EventObject e = this.getContext().getAwtEvent();
			java.awt.Component invoker = (java.awt.Component)e.getSource();
			int x;
			int y;
			if( e instanceof java.awt.event.MouseEvent ) {
				java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)e;
				x = me.getX();
				y = me.getY();
			} else {
				x = 0;
				y = invoker.getHeight();
			}
			edu.cmu.cs.dennisc.javax.swing.PopupMenuUtilities.showModal( popupMenu, invoker, x, y );
		}
	}
	protected void handleActionPerformed( java.awt.event.ActionEvent e ) {
		CascadeOperation< T > model = this.getModel();
		RtBlank< T >[] rtBlanks = this.rtRootFillIn.getChildren();
		T[] values = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newTypedArrayInstance( model.getComponentType(), rtBlanks.length );
		for( int i=0; i<rtBlanks.length; i++ ) {
			values[ i ] = rtBlanks[ i ].createValue();
		}
		model.handleCompletion( this.getContext(), this.performObserver, values );
//		try {
//			Object value = this.getSelectedFillIn().getValue();
//			if( this.taskObserver != null ) {
//				this.taskObserver.handleCompletion( value );
//			} else {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleCompleted (no taskObserver):", this.getSelectedFillIn().getValue() );
//			}
//		} catch( CancelException ce ) {
//			if( this.taskObserver != null ) {
//				this.taskObserver.handleCancelation();
//			} else {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleCancelation (no taskObserver)" );
//			}
//		}
	}
	private void handleCancel( javax.swing.event.PopupMenuEvent e ) {
	}
}

class RootFillIn<B> extends CascadeFillIn< B[],B > {
	private final CascadeOperation<B> operation;
	private final CascadeBlank<? extends B>[] blanks;
	public RootFillIn( CascadeOperation<B> operation, CascadeBlank<? extends B>[] blanks ) {
		super( java.util.UUID.fromString( "146f0cc3-bc25-4f28-9060-b2d15d7f2f65" ) );
		this.operation = operation;
		this.blanks = blanks;
	}
	public CascadeOperation< B > getOperation() {
		return this.operation;
	}
	@Override
	public CascadeBlank[] getBlanks() {
		return this.blanks;
	}
	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( CascadeFillInContext< B[], B > context ) {
		return null;
	}
	@Override
	public String getMenuItemText( CascadeFillInContext< B[], B > context ) {
		return null;
	}
	@Override
	public javax.swing.Icon getMenuItemIcon( CascadeFillInContext< B[], B > context ) {
		return null;
	}
	@Override
	public B[] createValue( CascadeFillInContext< B[], B > context ) {
		return null;
	}
	@Override
	public B[] getTransientValue( CascadeFillInContext< B[], B > context ) {
		return null;
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class CascadeOperation< B > extends Operation< CascadeOperationContext< B > > {
	private final Class<B> componentType;

	private final RootFillIn< B > fillIn;
	public CascadeOperation( Group group, java.util.UUID id, Class<B> componentType, CascadeBlank<? extends B>... blanks ) {
		super( group, id );
		this.componentType = componentType;
		this.fillIn = new RootFillIn< B >( this, blanks );
	}
	@Override
	public CascadeOperationContext< B > createAndPushContext( java.util.EventObject e, ViewController< ?, ? > viewController ) {
		return ContextManager.createAndPushCascadeOperationContext( this, e, viewController );
	}
	
	/*package-private*/ RootFillIn< B > getFillIn() {
		return this.fillIn;
	}
		
	public Class< B > getComponentType() {
		return this.componentType;
	}
	@Override
	protected void localize() {
	}
	@Override
	public boolean isAlreadyInState( Edit< ? > edit ) {
		//todo?
		return false;
	}
	
	protected abstract Edit< CascadeOperation< B > > createEdit( B[] values );
	
	/*package-private*/ void handleCompletion( CascadeOperationContext<B> context, PerformObserver performObserver, B[] values ) {
		try {
			Edit< CascadeOperation< B > > edit = this.createEdit( values );
			context.commitAndInvokeDo( edit );
		} finally {
			performObserver.handleFinally();
		}
	}

	@Override
	protected void perform( CascadeOperationContext<B> context, PerformObserver performObserver ) {
		RtOperation< B > rt = new RtOperation< B >( this, context, performObserver );
		rt.perform();
	}
}
