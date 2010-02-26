/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class Node implements javax.swing.event.MenuListener/*, java.awt.event.ActionListener*/ {
	public Node parent = null;
	private Node nextSibling = null;
	private java.util.List<Node> children = null;
	private javax.swing.JMenuItem menuItem = null;
	private javax.swing.JComponent menuProxy = null;
	private boolean wasLast = false; 
	
	protected void addChild( Node node ) {
		if( this.children.size() > 0 ) {
			Node prevLast = this.children.get( this.children.size()-1 );
			prevLast.nextSibling = node;
		}
		node.parent = this;
		node.nextSibling = null;
		this.children.add( node );
	}

	private void cleanUpUndesiredSeparators() {
		 java.util.ListIterator< Node > listIterator = this.children.listIterator();
		 boolean isSeparatorAcceptable = false;
		 while( listIterator.hasNext() ) {
			 Node node = listIterator.next();
			 if( node instanceof SeparatorFillIn ) {
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
		 
		 //remove separators at the end (should only be a maximum of 1)
		 final int N = this.children.size();
		 for( int i=0; i<N; i++ ) {
			 int index = N-i-1;
			 if( this.children.get( index ) instanceof SeparatorFillIn ) {
				 this.children.remove( index );
			 } else {
				 break;
			 }
		 }
	}
	protected abstract void addChildren();
	public java.util.List<Node> getChildren() {
		 if( this.children != null ) {
			 //pass
		 } else {
			 this.children = new java.util.LinkedList< Node >();
			 this.addChildren();
			 this.cleanUpUndesiredSeparators();
		 }
		 return this.children;
	}
	
//	protected javax.swing.Icon getMenuItemIcon() {
//		return null;
//	}
//	protected String getMenuItemText() {
//		return this.toString();
//	}
	
	protected Node getNextSibling() {
		return this.nextSibling;
	}
	protected Node getParent() {
		return this.parent;
	}
	protected void setParent( Node parent ) {
		this.parent = parent;
	}
	protected boolean isLast() {
		return false;
	}
	protected Blank getRootBlank() {
		if( this.parent != null ) {
			return this.parent.getRootBlank();
		} else {
			return (Blank)this;
		}
	}
	protected javax.swing.JComponent createMenuProxy() {
		return new javax.swing.JLabel( "todo: override getMenuProxy" );
	}
	protected javax.swing.JComponent getMenuProxy() {
		//todo
//		if( this.menuProxy != null ) {
//			//pass
//		} else {
			this.menuProxy = this.createMenuProxy();
//		}
		return this.menuProxy;
		
	}
	
	private java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			Node.this.handleActionOperationPerformed();
		}
	};
	protected javax.swing.JComponent getMenuItem() {
		boolean isLast = this.isLast();
		if( this.menuItem != null ) {
			if( this.wasLast == isLast ) {
				//pass
			} else {
				if( this.menuItem instanceof javax.swing.JMenu ) {
					javax.swing.JMenu menu = (javax.swing.JMenu)this.menuItem;
					menu.removeMenuListener( this );
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
				menu.addMenuListener( this  );
				this.menuItem = menu;
			}
			//this.menuItem.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
			this.isMenuItemIconUpToDate = false;
		}
		
		if( this.isMenuItemIconUpToDate() ) {
			//pass
		} else {
			javax.swing.JComponent component = this.getMenuProxy();
			if( component != null ) {
//				if( component instanceof javax.swing.JLabel ) {
//					javax.swing.JLabel label = (javax.swing.JLabel)component;
//					this.menuItem.setText( label.getText() );
//					this.menuItem.setIcon( label.getIcon() );
//				} else {
					edu.cmu.cs.dennisc.swing.SwingUtilities.invalidateTree( component );
					edu.cmu.cs.dennisc.swing.SwingUtilities.doLayoutTree( component );
					java.awt.Dimension size = component.getPreferredSize();
					if( size.width > 0 && size.height > 0 ) {
						javax.swing.Icon icon = edu.cmu.cs.dennisc.swing.SwingUtilities.createIcon( component );
						this.menuItem.setIcon( icon );
						this.menuItem.setText( null );
					} else {
						this.menuItem.setIcon( null );
						this.menuItem.setText( "unknown" );
					}
//				}
			} else {
				this.menuItem.setIcon( null );
				this.menuItem.setText( "unknown" );
			}
			//component.doLayout();
			setMenuItemIconUpToDate( true );
		}
		return this.menuItem;
	}
	private boolean isMenuItemIconUpToDate = false;
	protected boolean isMenuItemIconUpToDate() {
		return this.isMenuItemIconUpToDate;
	}
	protected void setMenuItemIconUpToDate( boolean isMenuItemIconUpToDate ) {
		this.isMenuItemIconUpToDate = isMenuItemIconUpToDate;
	}
	protected javax.swing.JMenu getMenu() {
		return (javax.swing.JMenu)getMenuItem();
	}

	protected void addNextNodeMenuItems( javax.swing.JComponent parent ) {
		for( Node child : this.getNextNode().getChildren() ) {
			if( child instanceof Blank ) {
				child = child.getChildren().get( 0 );
			}
			javax.swing.JComponent menuItem = child.getMenuItem();
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
	public void menuSelected( javax.swing.event.MenuEvent e ) {
		this.addNextNodeMenuItems( this.menuItem );
//		javax.swing.SwingUtilities.invokeLater( new Runnable() {
//			public void run() {
//				Node.this.menuItem.revalidate();
//				Node.this.menuItem.repaint();
//			}
//		} );
	}
	public void menuDeselected( javax.swing.event.MenuEvent e ) {
		this.getMenu().removeAll();
	}
	public void menuCanceled( javax.swing.event.MenuEvent e ) {
	}

	
	protected void handleActionOperationPerformed() {
		this.getRootBlank().handleActionPerformed();
	}
	
	protected Blank getNearestBlank() {
		return this.parent.getNearestBlank();
	}
	
	protected Blank getNextBlank() {
		Blank blank = this.getNearestBlank();
		if( blank.getNextSibling() != null ) {
			return (Blank)blank.getNextSibling();
		} else {
			if( this.parent != null ) {
				return this.parent.getNextBlank();
			} else {
				return null;
			}
		}
	}
	protected abstract Node getNextNode();
}
