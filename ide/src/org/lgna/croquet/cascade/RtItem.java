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

package org.lgna.croquet.cascade;

import org.lgna.croquet.*;
/**
 * @author Dennis Cosgrove
 */
abstract class RtItem<F, B, M extends CascadeItem< F, B >, C extends org.lgna.croquet.cascade.AbstractItemNode< F, B, M >> extends RtNode< M, C > {
	private final RtBlank< B >[] rtBlanks;
	private final CascadeBlankChild< F > owner;
	private final int index;
	private org.lgna.croquet.components.ViewController< ?, ? > menuItem = null;
	private boolean wasLast = false;

	public RtItem( M element, C node, CascadeBlankChild< F > owner, int index ) {
		super( element, node );
		this.owner = owner;
		this.index = index;
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

	public CascadeBlankChild< F > getOwner() {
		return this.owner;
	}
	public int getIndex() {
		return this.index;
	}
	protected abstract CascadeBlank< B >[] getModelBlanks();

	public int getBlankStepCount() {
		return this.rtBlanks.length;
	}
	public org.lgna.croquet.cascade.BlankNode< B > getBlankStepAt( int i ) {
		return this.rtBlanks[ i ].getNode();
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
	public boolean isGoodToGo() {
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
	@Override
	protected RtNode< ? extends Element, ? extends org.lgna.croquet.cascade.CascadeNode< ?, ? > > getNextNode() {
		if( this.rtBlanks.length > 0 ) {
			return this.rtBlanks[ 0 ];
		} else {
			return this.getNextBlank();
		}
	}
	public F createValue() {
		return this.getElement().createValue( this.getNode() );
	}
	protected boolean isLast() {
		return this.getNextNode() == null;
	}
	public void select() {
		//todo
		RtBlank< ? > nearestBlank = this.getNearestBlank();
		assert nearestBlank != null : this;
		nearestBlank.setSelectedFillIn( (RtItem)this );
		//		ContextManager.pushContext( this.getContext() );
	}
	public void deselect() {
		//		AbstractModelContext< ? > stepFillIn = ContextManager.popContext();
		//		assert stepFillIn == this.getContext() : stepFillIn;
	}

	protected void addNextNodeMenuItems( org.lgna.croquet.components.MenuItemContainer parent ) {
		RtNode nextNode = this.getNextNode();
		RtNode[] children = nextNode.getChildren();
		for( RtNode child : children ) {
			assert child instanceof RtBlank == false;
			RtItem< ?, ?, ?, ? > rtItem = (RtItem< ?, ?, ?, ? >)child;
			org.lgna.croquet.components.ViewController< ?, ? > menuItem = rtItem.getMenuItem();
			if( menuItem != null ) {
				if( menuItem instanceof org.lgna.croquet.components.CascadeMenu ) {
					parent.addCascadeMenu( (org.lgna.croquet.components.CascadeMenu)menuItem );
				} else if( menuItem instanceof org.lgna.croquet.components.CascadeMenuItem ) {
					parent.addCascadeMenuItem( (org.lgna.croquet.components.CascadeMenuItem)menuItem );
				} else {
					assert false : menuItem;
				}
			} else {
				parent.addSeparator();
			}
		}

		final boolean IS_SPRING_LAYOUT_ATTEMPT_DESIRED = true;
		if( IS_SPRING_LAYOUT_ATTEMPT_DESIRED ) {
			javax.swing.SpringLayout springLayout = null;
			for( RtNode child : children ) {
				RtItem< ?, ?, ?, ? > rtItem = (RtItem< ?, ?, ?, ? >)child;
				CascadeBlankChild owner = rtItem.getOwner();
				int itemCount = owner != null ? owner.getItemCount() : 0;
				if( itemCount > 1 ) {
					springLayout = new javax.swing.SpringLayout();
					break;
				}
			}
			javax.swing.JComponent jParent = parent.getViewController().getAwtComponent();
			if( jParent instanceof javax.swing.JMenu ) {
				javax.swing.JMenu jMenu = (javax.swing.JMenu)jParent;
				jParent = jMenu.getPopupMenu();
			}
			assert jParent instanceof javax.swing.JPopupMenu : jParent;
			if( springLayout != null ) {
				jParent.setLayout( springLayout );

				CascadeBlank blank = (CascadeBlank)nextNode.getElement();
				CascadeBlankChild[] blankChildren = blank.getFilteredChildren( (BlankNode)nextNode.getNode() );

				javax.swing.Spring widthA = javax.swing.Spring.constant( 0 );
				javax.swing.Spring widthB = javax.swing.Spring.constant( 0 );
				int index;

				index = 0;
				for( CascadeBlankChild child : blankChildren ) {
					java.awt.Component componentA = jParent.getComponent( index + 0 );
					javax.swing.SpringLayout.Constraints contraintsA = springLayout.getConstraints( componentA );
					widthA = javax.swing.Spring.max( widthA, contraintsA.getWidth() );
					int itemCount = child.getItemCount();
					if( itemCount == 2 ) {
						java.awt.Component componentB = jParent.getComponent( index + 1 );
						javax.swing.SpringLayout.Constraints contraintsB = springLayout.getConstraints( componentB );

						widthB = javax.swing.Spring.max( widthB, contraintsB.getWidth() );
						//todo: try to find a better way to account for the wasted icon/text space
						if( componentB instanceof javax.swing.JMenu ) {
							javax.swing.JMenu jMenu = (javax.swing.JMenu)componentB;
							if( jMenu.getText() != null || jMenu.getIcon() != null ) {
								//pass
							} else {
								//widthB = Spring.constant( 24 );
								widthB = contraintsA.getHeight();
							}
						}
					}
					index += itemCount;
				}
				javax.swing.Spring xA = javax.swing.Spring.constant( 0 );
				javax.swing.Spring xB = javax.swing.Spring.sum( xA, widthA );
				javax.swing.Spring widthAB = javax.swing.Spring.sum( xB, widthB );

				javax.swing.Spring y = javax.swing.Spring.constant( 0 );
				index = 0;
				for( CascadeBlankChild child : blankChildren ) {

					java.awt.Component componentA = jParent.getComponent( index + 0 );
					javax.swing.SpringLayout.Constraints contraintsA = springLayout.getConstraints( componentA );

					contraintsA.setX( xA );
					contraintsA.setY( y );

					javax.swing.Spring height;
					int itemCount = child.getItemCount();
					if( itemCount == 1 ) {
						contraintsA.setWidth( widthAB );
						height = contraintsA.getHeight();
					} else {
						assert itemCount == 2;
						contraintsA.setWidth( widthA );
						java.awt.Component componentB = jParent.getComponent( index + 1 );
						javax.swing.SpringLayout.Constraints contraintsB = springLayout.getConstraints( componentB );
						contraintsB.setX( xB );
						contraintsB.setY( y );
						contraintsB.setWidth( widthB );
						height = javax.swing.Spring.max( contraintsA.getHeight(), contraintsB.getHeight() );
						contraintsA.setHeight( height );
						contraintsB.setHeight( height );
					}
					y = javax.swing.Spring.sum( y, height );
					index += itemCount;
				}
				javax.swing.SpringLayout.Constraints pCons = springLayout.getConstraints( jParent );
				pCons.setConstraint( javax.swing.SpringLayout.SOUTH, y );
				pCons.setConstraint( javax.swing.SpringLayout.EAST, widthAB );
			}
		}
	}
	protected void removeAll( org.lgna.croquet.components.MenuItemContainer parent ) {
		parent.removeAllMenuItems();
	}
	
	private java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			RtItem.this.select();
			RtItem.this.getRtRoot().handleActionPerformed( e );
		}
	};
	private javax.swing.event.MenuListener menuListener = new javax.swing.event.MenuListener() {
		public void menuSelected( javax.swing.event.MenuEvent e ) {
			RtItem.this.addNextNodeMenuItems( (org.lgna.croquet.components.CascadeMenu)RtItem.this.getMenuItem() );
			RtItem.this.select();
		}
		public void menuDeselected( javax.swing.event.MenuEvent e ) {
			RtItem.this.deselect();
			RtItem.this.removeAll( (org.lgna.croquet.components.CascadeMenu)RtItem.this.getMenuItem() );
		}
		public void menuCanceled( javax.swing.event.MenuEvent e ) {
		}
	};

	protected org.lgna.croquet.components.ViewController< ?, ? > createMenuItem( CascadeItem< F, B > item, boolean isLast ) {
		org.lgna.croquet.components.ViewController< ?, ? > rv;
		javax.swing.JMenuItem jMenuItem;
		if( isLast ) {
			org.lgna.croquet.components.CascadeMenuItem menuItem = new org.lgna.croquet.components.CascadeMenuItem( item );
			menuItem.getAwtComponent().addActionListener( this.actionListener );
			jMenuItem = menuItem.getAwtComponent();
			rv = menuItem;
		} else {
			org.lgna.croquet.components.CascadeMenu menu = new org.lgna.croquet.components.CascadeMenu( item );
			menu.getAwtComponent().addMenuListener( this.menuListener );
			jMenuItem = menu.getAwtComponent();
			rv = menu;
		}
		jMenuItem.setText( item.getMenuItemText( this.getNode() ) );
		jMenuItem.setIcon( item.getMenuItemIcon( this.getNode() ) );
		return rv;
	}
	public org.lgna.croquet.components.ViewController< ?, ? > getMenuItem() {
		CascadeItem< F, B > item = this.getElement();
		boolean isLast = this.isLast();
		if( this.menuItem != null ) {
			if( this.wasLast == isLast ) {
				//pass
			} else {
				if( this.menuItem instanceof org.lgna.croquet.components.CascadeMenu ) {
					((org.lgna.croquet.components.CascadeMenu)this.menuItem).getAwtComponent().removeMenuListener( this.menuListener );
				} else if( this.menuItem instanceof org.lgna.croquet.components.CascadeMenuItem ) {
					((org.lgna.croquet.components.CascadeMenuItem)this.menuItem).getAwtComponent().removeActionListener( this.actionListener );
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

abstract class RtBlankOwner<F, B, M extends CascadeBlankOwner< F, B >, C extends org.lgna.croquet.cascade.BlankOwnerNode< F, B, M >> extends RtItem< F, B, M, C > {
	public RtBlankOwner( M element, C step, CascadeBlankChild< F > owner, int index ) {
		super( element, step, owner, index );
		this.getNode().setRtBlankOwner( this );
	}
	@Override
	protected final CascadeBlank< B >[] getModelBlanks() {
		return this.getElement().getBlanks();
	}
}

class RtFillIn<F, B> extends RtBlankOwner< F, B, CascadeFillIn< F, B >, org.lgna.croquet.cascade.FillInNode< F, B > > {
	public RtFillIn( CascadeFillIn< F, B > element, CascadeBlankChild< F > owner, int index ) {
		super( element, FillInNode.createInstance( element ), owner, index );
	}
	public boolean isAutomaticallySelectedWhenSoleOption() {
		return this.getElement().isAutomaticallySelectedWhenSoleOption();
	}
}

class RtMenu<FB> extends RtBlankOwner< FB, FB, CascadeMenuModel< FB >, org.lgna.croquet.cascade.MenuNode< FB >> {
	public RtMenu( CascadeMenuModel< FB > element, CascadeBlankChild< FB > owner, int index ) {
		super( element, MenuNode.createInstance( element ), owner, index );
	}
}

class RtSeparator extends RtItem< Void, Void, CascadeSeparator, org.lgna.croquet.cascade.SeparatorNode > {
	public RtSeparator( CascadeSeparator element, CascadeBlankChild< Void > owner, int index ) {
		super( element, SeparatorNode.createInstance( element ), owner, index );
	}
	@Override
	protected CascadeBlank< Void >[] getModelBlanks() {
		return new CascadeBlank[] {};
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
	protected org.lgna.croquet.components.ViewController< ?, ? > createMenuItem( CascadeItem< Void, Void > item, boolean isLast ) {
		//todo
		if( item.getMenuItemText( null ) != null || item.getMenuItemIcon( null ) != null ) {
			org.lgna.croquet.components.ViewController< ?, ? > rv = super.createMenuItem( item, isLast );
			rv.getAwtComponent().setEnabled( false );
			return rv;
		} else {
			return null;
		}
	}
}

class RtCancel<F> extends RtItem< F, Void, CascadeCancel< F >, org.lgna.croquet.cascade.CancelNode< F > > {
	public RtCancel( CascadeCancel< F > element, CascadeBlankChild< F > owner, int index ) {
		super( element, CancelNode.createInstance( element ), owner, index );
	}
	@Override
	protected CascadeBlank< Void >[] getModelBlanks() {
		return new CascadeBlank[] {};
	}
}
