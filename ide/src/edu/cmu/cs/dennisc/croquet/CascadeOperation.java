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

abstract class RtNode< M extends Model, C extends ModelContext< M > > {
	private M model;
	private C context;
	private RtNode<?,?> parent;
	private RtNode<?,?> nextSibling;
	
	public RtNode( M model, C context ) {
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
	
	protected void updateParentsAndNextSiblings( RtNode< ?,? >[] rtNodes ) {
		for( RtNode< ?,? > rtNode : rtNodes ) {
			rtNode.parent = this;
		}
		if( rtNodes.length > 0 ) {
			RtNode< ?,? > rtNodeA = rtNodes[ 0 ];
			for( int i=1; i<rtNodes.length; i++ ) {
				RtNode< ?,? > rtNodeB = rtNodes[ i ];
				rtNodeA.nextSibling = rtNodeB;
				rtNodeA = rtNodeB;
			}
			rtNodeA.nextSibling = null;
		}
	}
	
	protected RtOperation< ? > getRtOperation() {
		return this.parent.getRtOperation();
	}
	protected RtNode< ?, ? > getParent() {
		return this.parent;
	}
	protected RtNode< ?, ? > getNextSibling() {
		return this.nextSibling;
	}
	
	protected abstract RtNode< ?,? >[] getChildren();
	protected abstract RtNode< ?,? > getNextNode();
	protected abstract RtBlank< ? > getNearestBlank();
	protected RtBlank< ? > getNextBlank() {
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
			RtFillIn< ?,? > rtFillIn = (RtFillIn< ?,? >)child;
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
	private static <F,B> boolean isEmptySeparator( RtFillIn< F,B > rtFillIn ) {
		 if( rtFillIn.getModel() instanceof SeparatorFillIn ) {
			 SeparatorFillIn separatorFillIn = (SeparatorFillIn)rtFillIn.getModel();
			 return separatorFillIn.isEmpty();
		 } else {
			 return false;
		 }
	}
	private static <F,B> void cleanUpSeparators( java.util.List< RtFillIn< F,B > > rtFillIns ) {
		 java.util.ListIterator< RtFillIn< F,B > > listIterator = rtFillIns.listIterator();
		 boolean isSeparatorAcceptable = false;
		 while( listIterator.hasNext() ) {
			 RtFillIn< F,B > rtFillIn = listIterator.next();
			 if( isEmptySeparator( rtFillIn ) ) {
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
		 final int N = rtFillIns.size();
		 for( int i=0; i<N; i++ ) {
			 int index = N-i-1;
			 if( isEmptySeparator( rtFillIns.get( index ) ) ) {
				 rtFillIns.remove( index );
			 } else {
				 break;
			 }
		 }
	}

	private RtFillIn< B,? >[] rtFillIns;
	private RtFillIn< B,? > rtSelectedFillIn;
	public RtBlank( CascadeBlank<B> model ) {
		super( model, ContextManager.createCascadeBlankContext( model ) );
	}
	
	@Override
	protected RtFillIn< B,? >[] getChildren() {
		if( this.rtFillIns != null ) {
			//pass
		} else {
			java.util.List< RtFillIn< B,? > > baseRtFillIns = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( CascadeFillIn< B,? > fillIn : this.getModel().getFillIns() ) {
				assert fillIn != null : this.getModel();
				RtFillIn<B,?> rtFillIn;
				if( fillIn instanceof CascadeMenu ) {
					CascadeMenu menu = (CascadeMenu)fillIn;
					rtFillIn = new RtMenu< B >( menu );
				} else {
					rtFillIn = new RtFillIn( fillIn );
				}
				baseRtFillIns.add( rtFillIn );
			}
			
			java.util.ListIterator< RtFillIn< B,? > > listIterator = baseRtFillIns.listIterator();
			while( listIterator.hasNext() ) {
				RtFillIn< B,? > rtFillIn = listIterator.next();
				if( rtFillIn.isInclusionDesired() ) {
					//pass
				} else {
					listIterator.remove();
				}
			}
			
			//todo
			cleanUpSeparators( (java.util.List)baseRtFillIns );
		
			this.rtFillIns = (RtFillIn< B,? >[])edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( (java.util.List)baseRtFillIns, RtFillIn.class );
			this.updateParentsAndNextSiblings( this.rtFillIns );
		}
		return this.rtFillIns;
	}
	@Override
	protected RtNode< ?, ? > getNextNode() {
		return this;
	}
	@Override
	protected RtBlank< ? > getNearestBlank() {
		return this;
	}
	
	public void setSelectedFillIn( RtFillIn< B,? > fillIn ) {
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

abstract class RtBlankOwner<T, M extends CascadeBlankOwner, C extends CascadeBlankOwnerContext< M > > extends RtNode<M, C> {
	private RtBlank< T >[] rtBlanks;
	public RtBlankOwner( M model, C context ) {
		super( model, context );
		CascadeBlank[] blanks = model.getBlanks();
		this.rtBlanks = new RtBlank[ blanks.length ];
		for( int i=0; i<this.rtBlanks.length; i++ ) {
			this.rtBlanks[ i ] = new RtBlank< T >( blanks[ i ] );
		}
		this.updateParentsAndNextSiblings( this.rtBlanks );
	}
	@Override
	protected RtBlank< T >[] getChildren() {
		return this.rtBlanks;
	}
	protected boolean isGoodToGo() {
		if( this.rtBlanks.length > 0 ) {
			for( RtBlank< T > rtBlank : this.rtBlanks ) {
				if( rtBlank.isFillInAlreadyDetermined() ) {
					//pass
				} else {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected RtNode< ?, ? > getNextNode() {
		if( this.rtBlanks.length > 0 ) {
			return this.rtBlanks[ 0 ];
		} else {
			return this.getNextBlank();
		}
	}
}

class RtFillIn< F,B > extends RtBlankOwner< F, CascadeFillIn< F,B >, CascadeFillInContext< F,B > > {
	private javax.swing.JMenuItem menuItem = null;
	private boolean wasLast = false; 
	public RtFillIn( CascadeFillIn< F,B > model ) {
		super( model, ContextManager.createCascadeFillInContext( model ) );
	}
	public boolean isInclusionDesired() {
		return this.getModel().isInclusionDesired( this.getContext() );
	}
	public F createValue() {
		return this.getModel().createValue( this.getContext() );
	}
	protected boolean isLast() {
		return this.getNextNode() == null;
	}

	@Override
	protected RtBlank< ? > getNearestBlank() {
		RtNode<?,?> parent = this.getParent();
		return parent.getNearestBlank();
	}

	public void select() {
		//todo
		getNearestBlank().setSelectedFillIn( (RtFillIn)this );
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
			RtFillIn.this.select();
			RtFillIn.this.getRtOperation().handleActionPerformed( e );
		}
	};
	private javax.swing.event.MenuListener menuListener = new javax.swing.event.MenuListener() {
		public void menuSelected( javax.swing.event.MenuEvent e ) {
			RtFillIn.this.select();
			RtFillIn.this.addNextNodeMenuItems( RtFillIn.this.menuItem );
		}
		public void menuDeselected( javax.swing.event.MenuEvent e ) {
			RtFillIn.this.deselect();
			RtFillIn.this.getMenu().removeAll();
		}
		public void menuCanceled( javax.swing.event.MenuEvent e ) {
		}
	};
	
	protected javax.swing.JComponent getMenuItem() {
		CascadeFillIn< F,B > fillIn = this.getModel();
		if( fillIn instanceof SeparatorFillIn ) {
			this.menuItem = null;
		} else {
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
		}
		return this.menuItem;
	}
}

class RtMenu< FB > extends RtFillIn<FB,FB> {
	public RtMenu( CascadeMenu< FB > model ) {
		super( model );
	}
	@Override
	public FB createValue() {
		RtBlank< FB > child0 = this.getChildren()[ 0 ];
		return child0.createValue();
	}
}

class RtOperation< T > extends RtBlankOwner< T, CascadeOperation< T >, CascadeOperationContext< T > > {
	private final Operation.PerformObserver performObserver;
	public RtOperation( CascadeOperation< T > model, CascadeOperationContext<T> context, Operation.PerformObserver performObserver ) {
		super( model, context );
		this.performObserver = performObserver;
	}

	@Override
	protected RtOperation< ? > getRtOperation() {
		return this;
	}
	
	@Override
	protected RtBlank< ? > getNearestBlank() {
		return null;
	}
	
	protected T[] createValues( Class<T> componentType ) {
		RtBlank< T >[] rtBlanks = this.getChildren();
		T[] rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newTypedArrayInstance( componentType, rtBlanks.length );
		for( int i=0; i<rtBlanks.length; i++ ) {
			rv[ i ] = rtBlanks[ i ].createValue();
		}
		return rv;
	}

	public void perform() {
		if( this.isGoodToGo() ) {
			T[] values = this.createValues( this.getModel().getComponentType() );
			this.getModel().handleCompletion( this.getContext(), performObserver, values );
		} else {
			final javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
			//popupMenu.setLightWeightPopupEnabled( false );
			popupMenu.addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
				public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
					RtOperation.this.addNextNodeMenuItems( popupMenu );
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
		RtBlank< T >[] rtBlanks = this.getChildren();
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


/**
 * @author Dennis Cosgrove
 */
public abstract class CascadeOperation< B > extends Operation< CascadeOperationContext< B > > implements CascadeBlankOwner< B > {
	private final Class<B> componentType;
	public CascadeOperation( Group group, java.util.UUID id, Class<B> componentType ) {
		super( group, id );
		this.componentType = componentType;
	}
	@Override
	public CascadeOperationContext< B > createAndPushContext( java.util.EventObject e, ViewController< ?, ? > viewController ) {
		return ContextManager.createAndPushCascadeOperationContext( this, e, viewController );
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
	
	public abstract CascadeBlank<B>[] getBlanks();
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
