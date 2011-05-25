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

package org.lgna.croquet.steps;

import org.lgna.croquet.components.CascadeMenuItem;
import org.lgna.croquet.components.Menu;
import org.lgna.croquet.components.MenuItemContainer;
import org.lgna.croquet.components.PopupMenu;
import org.lgna.croquet.components.ViewController;

import edu.cmu.cs.dennisc.croquet.*;

/**
 * @author Dennis Cosgrove
 */
abstract class RtModel<M extends Model, C extends org.lgna.croquet.steps.Step< M >> {
	private M model;
	private C step;

	public RtModel( M model, C step ) {
		assert model != null;
		assert step != null : model;
		this.model = model;
		this.step = step;
	}
	public M getModel() {
		return this.model;
	}
	public C getStep() {
		return this.step;
	}
}

abstract class RtNode<M extends Model, C extends org.lgna.croquet.steps.Step< M >> extends RtModel< M, C > {
	private RtNode< ?, ? > parent;
	private RtNode< ?, ? > nextSibling;

	public RtNode( M model, C step ) {
		super( model, step );
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

	protected void updateParentsAndNextSiblings( RtNode< ?, ? >[] rtNodes ) {
		for( RtNode< ?, ? > rtNode : rtNodes ) {
			rtNode.setParent( this );
		}
		if( rtNodes.length > 0 ) {
			RtNode< ?, ? > rtNodeA = rtNodes[ 0 ];
			for( int i = 1; i < rtNodes.length; i++ ) {
				RtNode< ?, ? > rtNodeB = rtNodes[ i ];
				rtNodeA.setNextSibling( rtNodeB );
				rtNodeA = rtNodeB;
			}
			rtNodeA.setNextSibling( null );
		}
	}

	public RtRoot< ? > getRtRoot() {
		return this.parent.getRtRoot();
	}

	protected abstract RtNode[] getChildren();
	protected abstract RtNode< ? extends Model, ? extends org.lgna.croquet.steps.Step< ? > > getNextNode();
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
	protected void addNextNodeMenuItems( MenuItemContainer parent ) {
		for( RtNode child : this.getNextNode().getChildren() ) {
			assert child instanceof RtBlank == false;
			RtItem< ?, ?, ?, ? > rtFillIn = (RtItem< ?, ?, ?, ? >)child;
			ViewController<?,?> menuItem = rtFillIn.getMenuItem();
			if( menuItem != null ) {
				if( menuItem instanceof Menu ) {
					parent.addMenu( (Menu)menuItem );
				} else if( menuItem instanceof CascadeMenuItem ){
					parent.addCascadeMenuItem( (CascadeMenuItem)menuItem );
				} else {
					assert false : menuItem;
				}
			} else {
				parent.addSeparator();
			}
		}
	}
	protected void removeAll( MenuItemContainer parent ) {
		parent.removeAllMenuItems();
	}
}

class RtBlank<B> extends RtNode< CascadeBlank< B >, org.lgna.croquet.steps.CascadeBlankStep< B > > {
	private static <F, B, M extends CascadeItem< F, C >, C extends org.lgna.croquet.steps.CascadeItemStep< F, M, C > > boolean isEmptySeparator( RtItem< F, B, M, C > rtItem ) {
		return rtItem instanceof RtSeparator && ((RtSeparator)rtItem).getMenuItem() == null;
	}
	private static <F, B, M extends CascadeItem< F, C >, C extends org.lgna.croquet.steps.CascadeItemStep< F, M, C >> void cleanUpSeparators( java.util.List< RtItem< F, B, M, C >> rtItems ) {
		java.util.ListIterator< RtItem< F, B, M, C > > listIterator = rtItems.listIterator();
		boolean isLineSeparatorAcceptable = false;
		while( listIterator.hasNext() ) {
			RtItem< F, B, M, C > rtItem = listIterator.next();
			if( isEmptySeparator( rtItem ) ) {
				if( isLineSeparatorAcceptable ) {
					//pass 
				} else {
					listIterator.remove();
				}
				isLineSeparatorAcceptable = false;
			} else {
				isLineSeparatorAcceptable = true;
			}
		}

		//remove separators at the end
		//there should be a maximum of only 1 but we loop anyway 
		final int N = rtItems.size();
		for( int i = 0; i < N; i++ ) {
			int index = N - i - 1;
			if( isEmptySeparator( rtItems.get( index ) ) ) {
				rtItems.remove( index );
			} else {
				break;
			}
		}
	}
	private static boolean isDevoidOfNonSeparators( java.util.List< RtItem> rtItems ) {
		for( RtItem rtItem : rtItems ) {
			if( rtItem instanceof RtSeparator ) {
				//pass
			} else {
				return false;
			}
		}
		return true;
	}

	private RtItem[] rtFillIns;
	private RtItem< B, ?, ?, ? > rtSelectedFillIn;

	public RtBlank( CascadeBlank< B > model ) {
		super( model, CascadeBlankStep.createInstance( model ) );
		this.getStep().setRtBlank( this );
	}

	public org.lgna.croquet.steps.CascadeItemStep getSelectedFillInContext() {
		if( this.rtSelectedFillIn != null ) {
			return this.rtSelectedFillIn.getStep();
		} else {
			return null;
		}
	}

	@Override
	protected RtItem[] getChildren() {
		if( this.rtFillIns != null ) {
			//pass
		} else {
			java.util.List< RtItem > baseRtFillIns = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			for( CascadeItem item : this.getModel().getChildren( this.getStep() ) ) {
				RtItem rtItem;
				if( item instanceof CascadeMenu ) {
					CascadeMenu menu = (CascadeMenu)item;
					rtItem = new RtMenu< B >( menu );
				} else if( item instanceof CascadeFillIn ) {
					CascadeFillIn fillIn = (CascadeFillIn)item;
					rtItem = new RtFillIn( fillIn );
//				} else if( item instanceof CascadeRoot ) {
//					CascadeRoot root = (CascadeRoot)item;
//					rtItem = new RtRoot( root );
				} else if( item instanceof CascadeSeparator ) {
					CascadeSeparator separator = (CascadeSeparator)item;
					rtItem = new RtSeparator( separator );
				} else if( item instanceof CascadeCancel ) {
					CascadeCancel cancel = (CascadeCancel)item;
					rtItem = new RtCancel( cancel );
				} else {
					rtItem = null;
				}
				baseRtFillIns.add( rtItem );
			}

			java.util.ListIterator< RtItem > listIterator = baseRtFillIns.listIterator();
			while( listIterator.hasNext() ) {
				RtItem rtItem = listIterator.next();
				if( rtItem.isInclusionDesired() ) {
					//pass
				} else {
					listIterator.remove();
				}
			}

			//todo
			cleanUpSeparators( (java.util.List)baseRtFillIns );

			if( isDevoidOfNonSeparators( baseRtFillIns ) ) {
				baseRtFillIns.add( new RtCancel( CascadeUnfilledInCancel.getInstance() ) );
			}

			this.rtFillIns = edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( (java.util.List)baseRtFillIns, RtItem.class );
			this.updateParentsAndNextSiblings( this.rtFillIns );
		}
		return this.rtFillIns;
	}
	@Override
	protected RtNode< ? extends Model, ? extends org.lgna.croquet.steps.Step< ? > > getNextNode() {
		return this;
	}
	@Override
	public RtBlank< ? > getNearestBlank() {
		return this;
	}

	public void setSelectedFillIn( RtItem< B, ?, ?, ? > item ) {
		this.rtSelectedFillIn = item;
		RtNode parent = this.getParent();
		if( parent instanceof RtFillIn< ?, ? > ) {
			RtFillIn< ?, ? > parentFillIn = (RtFillIn< ?, ? >)parent;
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

abstract class RtItem<F, B, M extends CascadeItem< F, C >, C extends org.lgna.croquet.steps.CascadeItemStep< F, M, C > > extends RtNode< M, C > {
	private final RtBlank< B >[] rtBlanks;
//	private javax.swing.JMenuItem menuItem = null;
	private ViewController<?,?> menuItem = null;
	private boolean wasLast = false;

	public RtItem( M model, C step ) {
		super( model, step );
		CascadeBlank< B >[] blanks = this.getModelBlanks();
		final int N;
		if( blanks != null ) {
			N = blanks.length;
		} else {
			N = 0;
		}
		this.rtBlanks = new RtBlank[ N ];
		for( int i = 0; i < this.rtBlanks.length; i++ ) {
			assert blanks[ i ] != null : this;
			this.rtBlanks[ i ] = new RtBlank< B >( blanks[ i ] );
		}
		this.updateParentsAndNextSiblings( this.rtBlanks );
	}

	protected abstract CascadeBlank< B >[] getModelBlanks();
	
	public org.lgna.croquet.steps.CascadeBlankStep< B > getBlankStepAt( int i ) {
		return this.rtBlanks[ i ].getStep();
	}
	@Override
	public RtBlank< ? > getNearestBlank() {
		RtNode< ?, ? > parent = this.getParent();
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
		return this.getModel().isInclusionDesired( this.getStep() );
	}
	@Override
	protected RtNode< ? extends Model, ? extends org.lgna.croquet.steps.Step< ? > > getNextNode() {
		if( this.rtBlanks.length > 0 ) {
			return this.rtBlanks[ 0 ];
		} else {
			return this.getNextBlank();
		}
	}
	public F createValue() {
		return this.getModel().createValue( this.getStep() );
	}
	protected boolean isLast() {
		return this.getNextNode() == null;
	}
	public void select() {
		//todo
		RtBlank<?> nearestBlank = this.getNearestBlank();
		assert nearestBlank != null : this;
		nearestBlank.setSelectedFillIn( (RtItem)this );
//		ContextManager.pushContext( this.getContext() );
	}
	public void deselect() {
//		AbstractModelContext< ? > stepFillIn = ContextManager.popContext();
//		assert stepFillIn == this.getContext() : stepFillIn;
	}
	private java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			RtItem.this.select();
			RtItem.this.getRtRoot().handleActionPerformed( e );
		}
	};
	private javax.swing.event.MenuListener menuListener = new javax.swing.event.MenuListener() {
		public void menuSelected( javax.swing.event.MenuEvent e ) {
			RtItem.this.addNextNodeMenuItems( (Menu)RtItem.this.getMenuItem() );
			RtItem.this.select();
		}
		public void menuDeselected( javax.swing.event.MenuEvent e ) {
			RtItem.this.deselect();
			RtItem.this.removeAll( (Menu)RtItem.this.getMenuItem() );
		}
		public void menuCanceled( javax.swing.event.MenuEvent e ) {
		}
	};

	protected ViewController<?,?> createMenuItem( CascadeItem< F, C > item, boolean isLast ) {
		ViewController<?,?> rv;
		javax.swing.JMenuItem jMenuItem;
		if( isLast ) {
			CascadeMenuItem menuItem = new CascadeMenuItem( item );
			menuItem.getAwtComponent().addActionListener( this.actionListener );
			jMenuItem = menuItem.getAwtComponent();
			rv = menuItem;
		} else {
			Menu menu = new Menu( item );
			menu.getAwtComponent().addMenuListener( this.menuListener );
			jMenuItem = menu.getAwtComponent();
			rv = menu;
		}
		jMenuItem.setText( item.getMenuItemText( this.getStep() ) );
		jMenuItem.setIcon( item.getMenuItemIcon( this.getStep() ) );
		return rv;
	}
	public ViewController<?,?> getMenuItem() {
		CascadeItem< F, C > item = this.getModel();
		boolean isLast = this.isLast();
		if( this.menuItem != null ) {
			if( this.wasLast == isLast ) {
				//pass
			} else {
				if( this.menuItem instanceof Menu ) {
					((Menu)this.menuItem).getAwtComponent().removeMenuListener( this.menuListener );
				} else if( this.menuItem instanceof CascadeMenuItem ) {
					((CascadeMenuItem)this.menuItem).getAwtComponent().removeActionListener( this.actionListener );
				}
				this.menuItem = null;
			}
		} else {
			//pass
		}
		if( this.menuItem != null ) {
			//pass
		} else {
			this.menuItem = this.createMenuItem( item, isLast );
		}
		return this.menuItem;
	}
}

abstract class RtBlankOwner<F, B, M extends CascadeBlankOwner< F, B, C >, C extends org.lgna.croquet.steps.CascadeBlankOwnerStep< F, B, M, C > > extends RtItem< F, B, M, C > {
	public RtBlankOwner( M model, C step ) {
		super( model, step );
		this.getStep().setRtBlankOwner( this );
	}
	@Override
	protected final CascadeBlank< B >[] getModelBlanks() {
		return this.getModel().getBlanks();
	}
}
class RtFillIn<F, B> extends RtBlankOwner< F, B, CascadeFillIn< F, B >, org.lgna.croquet.steps.CascadeFillInPrepStep< F, B > > {
	public RtFillIn( CascadeFillIn< F, B > model ) {
		super( model, CascadeFillInPrepStep.createInstance( model ) );
	}
}

class RtMenu<FB> extends RtBlankOwner< FB, FB, CascadeMenu< FB >, org.lgna.croquet.steps.CascadeMenuStep< FB >> {
	public RtMenu( CascadeMenu< FB > model ) {
		super( model, CascadeMenuStep.createInstance( model ) );
	}
}

class RtSeparator extends RtItem< Void, Void, CascadeSeparator, org.lgna.croquet.steps.CascadeSeparatorStep > {
	public RtSeparator( CascadeSeparator model ) {
		super( model, CascadeSeparatorStep.createInstance( model ) );
	}
	@Override
	protected CascadeBlank<Void>[] getModelBlanks() {
		return new CascadeBlank[] {  };
	}
	@Override
	public final RtBlank getNearestBlank() {
		return null;
	}
	@Override
	protected final RtNode getNextNode() {
		return null;
	}
	@Override
	protected ViewController<?,?> createMenuItem( CascadeItem< Void, org.lgna.croquet.steps.CascadeSeparatorStep > item, boolean isLast ) {
		//todo
		if( item.getMenuItemText( null ) != null || item.getMenuItemIcon( null ) != null ) {
			ViewController<?,?> rv = super.createMenuItem( item, isLast );
			rv.getAwtComponent().setEnabled( false );
			return rv;
		} else {
			return null;
		}
	}
}

class RtCancel<F> extends RtItem< F, Void, CascadeCancel< F >, org.lgna.croquet.steps.CascadeCancelStep< F > > {
	public RtCancel( CascadeCancel< F > model ) {
		super( model, CascadeCancelStep.createInstance( model ) );
	}
	@Override
	protected CascadeBlank<Void>[] getModelBlanks() {
		return new CascadeBlank[] {};
	}
}

class RtRoot<T> extends RtBlankOwner< T[], T, CascadeRoot< T >, CascadeRootStep< T > > {
	private final RtCascadePopupOperation< T > rtOperation;
	public RtRoot( CascadeRoot< T > model, RtCascadePopupOperation< T > rtOperation ) {
		super( model, CascadeRootStep.createInstance( model ) );
		this.rtOperation = rtOperation;
	}
	@Override
	public RtRoot< ? > getRtRoot() {
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

public class RtCascadePopupOperation<T> extends RtModel< CascadePopupPrepModel< T >, org.lgna.croquet.steps.CascadePopupOperationStep< T > > {
	private Operation.PerformObserver performObserver;
	private RtRoot< T > rtRoot;

	public RtCascadePopupOperation( CascadePopupPrepModel< T > model, org.lgna.croquet.steps.CascadePopupOperationStep< T > step, Operation.PerformObserver performObserver ) {
		super( model, step );
		this.performObserver = performObserver;
		this.rtRoot = new RtRoot< T >( model.getRoot(), this );
	}
	protected T[] createValues( Class< T > componentType ) {
		RtBlank< T >[] rtBlanks = this.rtRoot.getChildren();
		T[] rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newTypedArrayInstance( componentType, rtBlanks.length );
		for( int i = 0; i < rtBlanks.length; i++ ) {
			rv[ i ] = rtBlanks[ i ].createValue();
		}
		return rv;
	}
	public void perform() {
		if( this.rtRoot.isGoodToGo() ) {
			T[] values = this.createValues( this.getModel().getComponentType() );
			this.getModel().handleCompletion( this.getStep(), this.performObserver, values );
		} else {
			final PopupMenu popupMenu = new PopupMenu( this.getModel() );
			//popupMenu.setLightWeightPopupEnabled( false );
			popupMenu.addPopupMenuListener( new javax.swing.event.PopupMenuListener() {
				public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
					//ContextManager.pushContext( RtOperation.this.rtRoot.getContext() );
					RtCascadePopupOperation.this.rtRoot.addNextNodeMenuItems( popupMenu );
				}
				public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
					RtCascadePopupOperation.this.rtRoot.removeAll( popupMenu );
					//AbstractModelContext< ? > step = ContextManager.popContext();
					//assert RtOperation.this.rtRoot.getContext() == step : step;
				}
				public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
					RtCascadePopupOperation.this.handleCancel( e );
				}
			} );
			popupMenu.addComponentListener( new java.awt.event.ComponentListener() {
				public void componentShown( java.awt.event.ComponentEvent e ) {
				}
				public void componentMoved( java.awt.event.ComponentEvent e ) {
				}
				public void componentResized( java.awt.event.ComponentEvent e ) {
					org.lgna.croquet.steps.TransactionManager.firePopupMenuResized( popupMenu );
				}
				public void componentHidden( java.awt.event.ComponentEvent e ) {
				}
			} );
			
			org.lgna.croquet.Trigger trigger = this.getStep().getTrigger();
			trigger.showPopupMenu( popupMenu );
		}
	}
	protected void handleActionPerformed( java.awt.event.ActionEvent e ) {
		CascadePopupPrepModel< T > model = this.getModel();
		RtBlank< T >[] rtBlanks = this.rtRoot.getChildren();
		T[] values = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newTypedArrayInstance( model.getComponentType(), rtBlanks.length );
		boolean isReadyForCompletion;
		try {
			for( int i = 0; i < rtBlanks.length; i++ ) {
				values[ i ] = rtBlanks[ i ].createValue();
			}
			isReadyForCompletion = true;
		} catch( CancelException ce ) {
			isReadyForCompletion = false;
		}
		if( isReadyForCompletion ) {
			model.handleCompletion( this.getStep(), this.performObserver, values );
		} else {
			this.handleCancel( e );
		}
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
	private void handleCancel( java.util.EventObject e ) {
		this.getModel().handleCancel( this.getStep(), this.performObserver );
	}
}
