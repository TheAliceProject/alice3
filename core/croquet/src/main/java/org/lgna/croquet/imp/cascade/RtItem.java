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

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.AbstractCascadeMenuModel;
import org.lgna.croquet.CascadeBlank;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeBlankOwner;
import org.lgna.croquet.CascadeCancel;
import org.lgna.croquet.CascadeFillIn;
import org.lgna.croquet.CascadeItem;
import org.lgna.croquet.CascadeSeparator;
import org.lgna.croquet.history.TransactionHistory;
import org.lgna.croquet.views.CascadeMenu;
import org.lgna.croquet.views.CascadeMenuItem;
import org.lgna.croquet.views.MenuItemContainer;
import org.lgna.croquet.views.ViewController;

import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
abstract class RtItem<F, B, M extends CascadeItem<F, B>, C extends AbstractItemNode<F, B, M>> extends RtNode<M, C> {
	private final RtBlank<B>[] rtBlanks;
	private final CascadeBlankChild<F> owner;
	private final int index;
	private ViewController<?, ?> menuItem = null;
	private boolean wasLast = false;

	public RtItem( M element, C node, CascadeBlankChild<F> owner, int index ) {
		super( element, node );
		this.owner = owner;
		this.index = index;
		List<? extends CascadeBlank<B>> blanks = this.getModelBlanks();
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

	protected abstract List<? extends CascadeBlank<B>> getModelBlanks();

	public int getBlankStepCount() {
		return this.rtBlanks.length;
	}

	public BlankNode<B> getBlankStepAt( int i ) {
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

	public F createValue( TransactionHistory transactionHistory ) {
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

	protected void addNextNodeMenuItems( MenuItemContainer parent ) {
		RtBlank<?> nextNode = this.getNextNode();
		if( nextNode.isAutomaticallyDetermined() ) {
			Logger.severe( nextNode );
		}
		RtBlank.ItemChildrenAndComboOffsetsPair itemChildrenAndComboOffsetsPair = nextNode.getItemChildrenAndComboOffsets();
		RtItem[] children = itemChildrenAndComboOffsetsPair.getItemChildren();
		final int N = children.length;
		int i = 0;
		while( i < N ) {
			RtItem<?, ?, ?, ?> rtItem = children[ i ];
			ViewController<?, ?> menuItem = rtItem.getMenuItem();
			if( menuItem != null ) {
				if( menuItem instanceof CascadeMenu ) {
					parent.addCascadeMenu( (CascadeMenu)menuItem );
				} else if( menuItem instanceof CascadeMenuItem ) {
					CascadeMenuItem cascadeMenuItem = (CascadeMenuItem)menuItem;
					if( itemChildrenAndComboOffsetsPair.isComboOffset( i ) ) {
						i++;
						RtItem<?, ?, ?, ?> rtItem2 = children[ i ];
						ViewController<?, ?> menuItem2 = rtItem2.getMenuItem();
						if( menuItem2 != null ) {
							if( menuItem2 instanceof CascadeMenu ) {
								CascadeMenu cascadeMenu = (CascadeMenu)menuItem2;
								parent.addCascadeCombo( cascadeMenuItem, cascadeMenu );
							} else {
								assert false : menuItem2;
							}
						} else {
							Logger.severe( rtItem2 );
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

	protected void removeAll( MenuItemContainer parent ) {
		parent.removeAllMenuItems();
	}

	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed( ActionEvent e ) {
			RtItem.this.select();
			RtItem.this.getRtRoot().handleActionPerformed( e );
		}
	};
	private MenuListener menuListener = new MenuListener() {
		@Override
		public void menuSelected( MenuEvent e ) {
			RtItem.this.addNextNodeMenuItems( (CascadeMenu)RtItem.this.getMenuItem() );
			RtItem.this.select();
		}

		@Override
		public void menuDeselected( MenuEvent e ) {
			RtItem.this.deselect();
			RtItem.this.removeAll( (CascadeMenu)RtItem.this.getMenuItem() );
		}

		@Override
		public void menuCanceled( MenuEvent e ) {
		}
	};

	protected ViewController<?, ?> createMenuItem( CascadeItem<F, B> item, boolean isLast ) {
		ViewController<?, ?> rv;
		JMenuItem jMenuItem;
		if( isLast ) {
			CascadeMenuItem menuItem = new CascadeMenuItem( item, this.getRtRoot() );
			menuItem.getAwtComponent().addActionListener( this.actionListener );
			jMenuItem = menuItem.getAwtComponent();
			rv = menuItem;
		} else {
			CascadeMenu menu = new CascadeMenu( item );
			menu.getAwtComponent().addMenuListener( this.menuListener );
			jMenuItem = menu.getAwtComponent();
			rv = menu;
		}
		jMenuItem.setText( item.getMenuItemText( this.getNode() ) );
		jMenuItem.setIcon( item.getMenuItemIcon( this.getNode() ) );
		return rv;
	}

	public ViewController<?, ?> getMenuItem() {
		CascadeItem<F, B> item = this.getElement();
		boolean isLast = this.isLast();
		if( this.menuItem != null ) {
			if( this.wasLast == isLast ) {
				//pass
			} else {
				if( this.menuItem instanceof CascadeMenu ) {
					( (CascadeMenu)this.menuItem ).getAwtComponent().removeMenuListener( this.menuListener );
				} else if( this.menuItem instanceof CascadeMenuItem ) {
					( (CascadeMenuItem)this.menuItem ).getAwtComponent().removeActionListener( this.actionListener );
				} else {
					Logger.severe( this.menuItem );
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

abstract class RtBlankOwner<F, B, M extends CascadeBlankOwner<F, B>, C extends BlankOwnerNode<F, B, M>> extends RtItem<F, B, M, C> {
	public RtBlankOwner( M element, C step, CascadeBlankChild<F> owner, int index ) {
		super( element, step, owner, index );
		this.getNode().setRtBlankOwner( this );
	}

	@Override
	protected final List<? extends CascadeBlank<B>> getModelBlanks() {
		return this.getElement().getBlanks();
	}
}

class RtFillIn<F, B> extends RtBlankOwner<F, B, CascadeFillIn<F, B>, FillInNode<F, B>> {
	public RtFillIn( CascadeFillIn<F, B> element, CascadeBlankChild<F> owner, int index ) {
		super( element, FillInNode.createInstance( element ), owner, index );
	}

	public boolean isAutomaticallySelectedWhenSoleOption() {
		return this.getElement().isAutomaticallySelectedWhenSoleOption();
	}
}

class RtMenu<F, B> extends RtBlankOwner<F, B, AbstractCascadeMenuModel<F, B>, MenuNode<F, B>> {
	public RtMenu( AbstractCascadeMenuModel<F, B> element, CascadeBlankChild<F> owner, int index ) {
		super( element, MenuNode.createInstance( element ), owner, index );
	}
}

class RtSeparator extends RtItem<Void, Void, CascadeSeparator, SeparatorNode> {
	public RtSeparator( CascadeSeparator element, CascadeBlankChild<Void> owner, int index ) {
		super( element, SeparatorNode.createInstance( element ), owner, index );
	}

	@Override
	protected boolean isLast() {
		return true;
	}

	@Override
	protected List<CascadeBlank<Void>> getModelBlanks() {
		return Collections.emptyList();
	}

	@Override
	protected ViewController<?, ?> createMenuItem( CascadeItem<Void, Void> item, boolean isLast ) {
		//todo
		if( ( item.getMenuItemText( null ) != null ) || ( item.getMenuItemIcon( null ) != null ) ) {
			ViewController<?, ?> rv = super.createMenuItem( item, isLast );
			rv.getAwtComponent().setEnabled( false );
			return rv;
		} else {
			return null;
		}
	}
}

class RtCancel<F> extends RtItem<F, Void, CascadeCancel<F>, CancelNode<F>> {
	public RtCancel( CascadeCancel<F> element, CascadeBlankChild<F> owner, int index ) {
		super( element, CancelNode.createInstance( element ), owner, index );
	}

	@Override
	protected List<CascadeBlank<Void>> getModelBlanks() {
		return Collections.emptyList();
	}
}
