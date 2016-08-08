/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.lgna.croquet.imp.cascade;

import org.lgna.croquet.AbstractCascadeMenuModel;
import org.lgna.croquet.CascadeBlank;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeBlankOwner;
import org.lgna.croquet.CascadeCancel;
import org.lgna.croquet.CascadeFillIn;
import org.lgna.croquet.CascadeItem;
import org.lgna.croquet.CascadeSeparator;

/**
 * @author Dennis Cosgrove
 */
abstract class RtItem<F, B, M extends CascadeItem<F, B>, C extends org.lgna.croquet.imp.cascade.AbstractItemNode<F, B, M>> extends RtNode<M, C> {
	private final RtBlank<B>[] rtBlanks;
	private final CascadeBlankChild<F> owner;
	private final int index;
	private org.lgna.croquet.views.ViewController<?, ?> menuItem = null;
	private boolean wasLast = false;

	public RtItem( M element, C node, CascadeBlankChild<F> owner, int index ) {
		super( element, node );
		this.owner = owner;
		this.index = index;
		java.util.List<? extends CascadeBlank<B>> blanks = this.getModelBlanks();
		final int N;
		if( blanks != null ) {
			N = blanks.size();
		} else {
			N = 0;
		}
		this.rtBlanks = new RtBlank[ N ];
		for( int i = 0; i < N; i++ ) {
			CascadeBlank<B> blankI = blanks.get( i );
			assert blankI != null : this;
			this.rtBlanks[ i ] = new RtBlank<B>( blankI );
		}
		this.updateParentsAndNextSiblings( this.rtBlanks );
	}

	public CascadeBlankChild<F> getOwner() {
		return this.owner;
	}

	public int getIndex() {
		return this.index;
	}

	protected abstract java.util.List<? extends CascadeBlank<B>> getModelBlanks();

	public int getBlankStepCount() {
		return this.rtBlanks.length;
	}

	public org.lgna.croquet.imp.cascade.BlankNode<B> getBlankStepAt( int i ) {
		return this.rtBlanks[ i ].getNode();
	}

	@Override
	public RtBlank<?> getNearestBlank() {
		RtNode<?, ?> parent = this.getParent();
		return parent.getNearestBlank();
	}

	protected RtBlank<B>[] getBlankChildren() {
		return this.rtBlanks;
	}

	public boolean isAutomaticallyDetermined() {
		if( this.rtBlanks.length > 0 ) {
			for( RtBlank<B> rtBlank : this.rtBlanks ) {
				if( rtBlank.isAutomaticallyDetermined() ) {
					//pass
				} else {
					return false;
				}
			}
		}
		return true;
	}

	//todo: rename
	private RtBlank<?> getNextNode() {
		RtBlank<?> rv;
		if( this.rtBlanks.length > 0 ) {
			rv = this.rtBlanks[ 0 ];
		} else {
			rv = this.getNextBlank();
		}
		if( rv != null ) {
			if( rv.isAutomaticallyDetermined() ) {
				return rv.getNextBlank();
			}
		}
		return rv;
	}

	public F createValue( org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		return this.getElement().createValue( this.getNode(), transactionHistory );
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

	protected void addNextNodeMenuItems( org.lgna.croquet.views.MenuItemContainer parent ) {
		RtBlank<?> nextNode = this.getNextNode();
		if( nextNode.isAutomaticallyDetermined() ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( nextNode );
		}
		RtBlank.ItemChildrenAndComboOffsetsPair itemChildrenAndComboOffsetsPair = nextNode.getItemChildrenAndComboOffsets();
		RtItem[] children = itemChildrenAndComboOffsetsPair.getItemChildren();
		final int N = children.length;
		int i = 0;
		while( i < N ) {
			RtItem<?, ?, ?, ?> rtItem = children[ i ];
			org.lgna.croquet.views.ViewController<?, ?> menuItem = rtItem.getMenuItem();
			if( menuItem != null ) {
				if( menuItem instanceof org.lgna.croquet.views.CascadeMenu ) {
					parent.addCascadeMenu( (org.lgna.croquet.views.CascadeMenu)menuItem );
				} else if( menuItem instanceof org.lgna.croquet.views.CascadeMenuItem ) {
					org.lgna.croquet.views.CascadeMenuItem cascadeMenuItem = (org.lgna.croquet.views.CascadeMenuItem)menuItem;
					if( itemChildrenAndComboOffsetsPair.isComboOffset( i ) ) {
						i++;
						RtItem<?, ?, ?, ?> rtItem2 = children[ i ];
						org.lgna.croquet.views.ViewController<?, ?> menuItem2 = rtItem2.getMenuItem();
						if( menuItem2 != null ) {
							if( menuItem2 instanceof org.lgna.croquet.views.CascadeMenu ) {
								org.lgna.croquet.views.CascadeMenu cascadeMenu = (org.lgna.croquet.views.CascadeMenu)menuItem2;
								parent.addCascadeCombo( cascadeMenuItem, cascadeMenu );
							} else {
								assert false : menuItem2;
							}
						} else {
							edu.cmu.cs.dennisc.java.util.logging.Logger.severe( rtItem2 );
							assert false : menuItem2;
						}
					} else {
						parent.addCascadeMenuItem( cascadeMenuItem );
					}
				} else {
					assert false : menuItem;
				}
			} else {
				parent.addSeparator();
			}
			i++;
		}
	}

	protected void removeAll( org.lgna.croquet.views.MenuItemContainer parent ) {
		parent.removeAllMenuItems();
	}

	private java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			RtItem.this.select();
			RtItem.this.getRtRoot().handleActionPerformed( e );
		}
	};
	private javax.swing.event.MenuListener menuListener = new javax.swing.event.MenuListener() {
		@Override
		public void menuSelected( javax.swing.event.MenuEvent e ) {
			RtItem.this.addNextNodeMenuItems( (org.lgna.croquet.views.CascadeMenu)RtItem.this.getMenuItem() );
			RtItem.this.select();
		}

		@Override
		public void menuDeselected( javax.swing.event.MenuEvent e ) {
			RtItem.this.deselect();
			RtItem.this.removeAll( (org.lgna.croquet.views.CascadeMenu)RtItem.this.getMenuItem() );
		}

		@Override
		public void menuCanceled( javax.swing.event.MenuEvent e ) {
		}
	};

	protected org.lgna.croquet.views.ViewController<?, ?> createMenuItem( CascadeItem<F, B> item, boolean isLast ) {
		org.lgna.croquet.views.ViewController<?, ?> rv;
		javax.swing.JMenuItem jMenuItem;
		if( isLast ) {
			org.lgna.croquet.views.CascadeMenuItem menuItem = new org.lgna.croquet.views.CascadeMenuItem( item, this.getRtRoot() );
			menuItem.getAwtComponent().addActionListener( this.actionListener );
			jMenuItem = menuItem.getAwtComponent();
			rv = menuItem;
		} else {
			org.lgna.croquet.views.CascadeMenu menu = new org.lgna.croquet.views.CascadeMenu( item );
			menu.getAwtComponent().addMenuListener( this.menuListener );
			jMenuItem = menu.getAwtComponent();
			rv = menu;
		}
		jMenuItem.setText( item.getMenuItemText( this.getNode() ) );
		jMenuItem.setIcon( item.getMenuItemIcon( this.getNode() ) );
		return rv;
	}

	public org.lgna.croquet.views.ViewController<?, ?> getMenuItem() {
		CascadeItem<F, B> item = this.getElement();
		boolean isLast = this.isLast();
		if( this.menuItem != null ) {
			if( this.wasLast == isLast ) {
				//pass
			} else {
				if( this.menuItem instanceof org.lgna.croquet.views.CascadeMenu ) {
					( (org.lgna.croquet.views.CascadeMenu)this.menuItem ).getAwtComponent().removeMenuListener( this.menuListener );
				} else if( this.menuItem instanceof org.lgna.croquet.views.CascadeMenuItem ) {
					( (org.lgna.croquet.views.CascadeMenuItem)this.menuItem ).getAwtComponent().removeActionListener( this.actionListener );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.menuItem );
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

abstract class RtBlankOwner<F, B, M extends CascadeBlankOwner<F, B>, C extends org.lgna.croquet.imp.cascade.BlankOwnerNode<F, B, M>> extends RtItem<F, B, M, C> {
	public RtBlankOwner( M element, C step, CascadeBlankChild<F> owner, int index ) {
		super( element, step, owner, index );
		this.getNode().setRtBlankOwner( this );
	}

	@Override
	protected final java.util.List<? extends CascadeBlank<B>> getModelBlanks() {
		return this.getElement().getBlanks();
	}
}

class RtFillIn<F, B> extends RtBlankOwner<F, B, CascadeFillIn<F, B>, org.lgna.croquet.imp.cascade.FillInNode<F, B>> {
	public RtFillIn( CascadeFillIn<F, B> element, CascadeBlankChild<F> owner, int index ) {
		super( element, FillInNode.createInstance( element ), owner, index );
	}

	public boolean isAutomaticallySelectedWhenSoleOption() {
		return this.getElement().isAutomaticallySelectedWhenSoleOption();
	}
}

class RtMenu<F, B> extends RtBlankOwner<F, B, AbstractCascadeMenuModel<F, B>, org.lgna.croquet.imp.cascade.MenuNode<F, B>> {
	public RtMenu( AbstractCascadeMenuModel<F, B> element, CascadeBlankChild<F> owner, int index ) {
		super( element, MenuNode.createInstance( element ), owner, index );
	}
}

class RtSeparator extends RtItem<Void, Void, CascadeSeparator, org.lgna.croquet.imp.cascade.SeparatorNode> {
	public RtSeparator( CascadeSeparator element, CascadeBlankChild<Void> owner, int index ) {
		super( element, SeparatorNode.createInstance( element ), owner, index );
	}

	@Override
	protected boolean isLast() {
		return true;
	}

	@Override
	protected java.util.List<CascadeBlank<Void>> getModelBlanks() {
		return java.util.Collections.emptyList();
	}

	@Override
	protected org.lgna.croquet.views.ViewController<?, ?> createMenuItem( CascadeItem<Void, Void> item, boolean isLast ) {
		//todo
		if( ( item.getMenuItemText( null ) != null ) || ( item.getMenuItemIcon( null ) != null ) ) {
			org.lgna.croquet.views.ViewController<?, ?> rv = super.createMenuItem( item, isLast );
			rv.getAwtComponent().setEnabled( false );
			return rv;
		} else {
			return null;
		}
	}
}

class RtCancel<F> extends RtItem<F, Void, CascadeCancel<F>, org.lgna.croquet.imp.cascade.CancelNode<F>> {
	public RtCancel( CascadeCancel<F> element, CascadeBlankChild<F> owner, int index ) {
		super( element, CancelNode.createInstance( element ), owner, index );
	}

	@Override
	protected java.util.List<CascadeBlank<Void>> getModelBlanks() {
		return java.util.Collections.emptyList();
	}
}
