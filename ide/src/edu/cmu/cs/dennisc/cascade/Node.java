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
package edu.cmu.cs.dennisc.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class Node {
	public Node parent = null;
	private Node nextSibling = null;
	private java.util.List<Node> children = null;
	private edu.cmu.cs.dennisc.croquet.Model model = null;
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

	private class MenuModel extends edu.cmu.cs.dennisc.croquet.MenuModel {
		public MenuModel() {
			super( java.util.UUID.fromString( "6f3f3350-dc15-4dca-a650-f88ea484da14" ) );
		}
		@Override
		protected void handleMenuSelected( javax.swing.event.MenuEvent e, edu.cmu.cs.dennisc.croquet.Menu< edu.cmu.cs.dennisc.croquet.MenuModel > menu ) {
			super.handleMenuSelected( e, menu );
			Node.this.handleMenuSelected( e, menu );
		}
		@Override
		protected void handleMenuDeselected( javax.swing.event.MenuEvent e, edu.cmu.cs.dennisc.croquet.Menu< edu.cmu.cs.dennisc.croquet.MenuModel > menu ) {
			Node.this.handleMenuDeselected( e, menu );
			super.handleMenuDeselected( e, menu );
		}
//		@Override
//		protected void handleMenuCanceled( javax.swing.event.MenuEvent e, edu.cmu.cs.dennisc.croquet.Menu< edu.cmu.cs.dennisc.croquet.MenuModel > menu ) {
//			super.handleMenuCanceled( e, menu );
//		}
	}
	
	private class ActionOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
		public ActionOperation() {
			super( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "71f47373-d2b4-474d-b131-307c53443c9d" ) );
		}
		@Override
		protected void perform( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
			Node.this.handleActionOperationPerformed();
			context.finish();
		}
	}
	
//	private ActionOperation actionOperation = new ActionOperation();
//	private MenuModel menuModel = new MenuModel();
//	protected edu.cmu.cs.dennisc.croquet.Model createModel( boolean isLast ) {
//		if( isLast ) {
//			this.menuItem = new javax.swing.JMenuItem();
//			this.menuItem.addActionListener( this.actionListener );
//		} else {
//			javax.swing.JMenu menu = new javax.swing.JMenu();
//			menu.addMenuListener( this  );
//			this.menuItem = menu;
//		}
//	}

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
	
	private static void setText( edu.cmu.cs.dennisc.croquet.Model model, String text ) {
		if( model instanceof edu.cmu.cs.dennisc.croquet.MenuModel ) {
			edu.cmu.cs.dennisc.croquet.MenuModel menuModel = (edu.cmu.cs.dennisc.croquet.MenuModel)model;
			menuModel.setName( text );
		} else if( model instanceof edu.cmu.cs.dennisc.croquet.Operation< ? > ) {
			edu.cmu.cs.dennisc.croquet.Operation< ? > operation = (edu.cmu.cs.dennisc.croquet.Operation< ? >)model;
			operation.setName( text );
		}
	}
	private static void setIcon( edu.cmu.cs.dennisc.croquet.Model model, javax.swing.Icon icon ) {
		if( model instanceof edu.cmu.cs.dennisc.croquet.MenuModel ) {
			edu.cmu.cs.dennisc.croquet.MenuModel menuModel = (edu.cmu.cs.dennisc.croquet.MenuModel)model;
			menuModel.setSmallIcon( icon );
		} else if( model instanceof edu.cmu.cs.dennisc.croquet.Operation< ? > ) {
			edu.cmu.cs.dennisc.croquet.Operation< ? > operation = (edu.cmu.cs.dennisc.croquet.Operation< ? >)model;
			operation.setSmallIcon( icon );
		}
	}
	private edu.cmu.cs.dennisc.croquet.Model createCroquetModel( boolean isLast ) {
		if( isLast ) {
			return new ActionOperation();
		} else {
			return new MenuModel();
		}
	}
	public edu.cmu.cs.dennisc.croquet.Model getCroquetModel() {
		boolean isLast = this.isLast();
		if( this.model != null ) {
			if( this.wasLast == isLast ) {
				//pass
			} else {
//				if( this.menuItem instanceof MenuModel ) {
//					MenuModel menuModel = (MenuModel)this.menuItem;
//					menuModel.removeMenuListener( this );
//				} else {
//					this.menuItem.removeActionListener( this.actionListener );
//				}
				this.model = null;
			}
		} else {
			//pass
		}
		if( this.model != null ) {
			//pass
		} else {
			this.model = this.createCroquetModel( isLast );
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
					edu.cmu.cs.dennisc.javax.swing.SwingUtilities.invalidateTree( component );
					edu.cmu.cs.dennisc.javax.swing.SwingUtilities.doLayoutTree( component );
					java.awt.Dimension size = component.getPreferredSize();
					if( size.width > 0 && size.height > 0 ) {
						javax.swing.Icon icon = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.createIcon( component );
						setIcon( this.model, icon );
						setText( this.model, null );
					} else {
						setIcon( this.model, null );
						setText( this.model, "unknown" );
					}
//				}
			} else {
				setIcon( this.model, null );
				setText( this.model, "unknown" );
			}
			//component.doLayout();
			setMenuItemIconUpToDate( true );
		}
		return this.model;
	}
	private boolean isMenuItemIconUpToDate = false;
	protected boolean isMenuItemIconUpToDate() {
		return this.isMenuItemIconUpToDate;
	}
	protected void setMenuItemIconUpToDate( boolean isMenuItemIconUpToDate ) {
		this.isMenuItemIconUpToDate = isMenuItemIconUpToDate;
	}
//	protected edu.cmu.cs.dennisc.croquet.Menu< ? > getMenu() {
//		return (edu.cmu.cs.dennisc.croquet.Menu< ? >)getMenuItem();
//	}

	protected void handleMenuSelected( javax.swing.event.MenuEvent e, edu.cmu.cs.dennisc.croquet.Menu< edu.cmu.cs.dennisc.croquet.MenuModel > menu ) {
		edu.cmu.cs.dennisc.croquet.Application.addMenuElements( menu, Node.this.getModels() );
//		Node.this.addNextNodeMenuItems( Node.this.model, menu );
////		javax.swing.SwingUtilities.invokeLater( new Runnable() {
////			public void run() {
////				Node.this.menuItem.revalidate();
////				Node.this.menuItem.repaint();
////			}
////		} );
	}
	protected void handleMenuDeselected( javax.swing.event.MenuEvent e, edu.cmu.cs.dennisc.croquet.Menu< edu.cmu.cs.dennisc.croquet.MenuModel > menu ) {
		menu.getAwtComponent().removeAll();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo handleMenuDeselected" );
	}

	protected java.util.List< edu.cmu.cs.dennisc.croquet.Model > getModels() {
		java.util.List< edu.cmu.cs.dennisc.croquet.Model > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		for( Node child : this.getNextNode().getChildren() ) {
			if( child instanceof Blank ) {
				child = child.getChildren().get( 0 );
			}
			rv.add( child.getCroquetModel() );
		}	
		return rv;
	}
//	protected void addNextNodeMenuItems( javax.swing.JComponent parent ) {
//		for( Node child : this.getNextNode().getChildren() ) {
//			if( child instanceof Blank ) {
//				child = child.getChildren().get( 0 );
//			}
//			javax.swing.JComponent menuItem = child.getMenuItem();
//			if( menuItem != null ) {
//				parent.add( menuItem );
//			} else {
//				if( parent instanceof javax.swing.JMenu ) {
//					javax.swing.JMenu menu = (javax.swing.JMenu)parent;
//					menu.addSeparator();
//				} else if( parent instanceof javax.swing.JPopupMenu ) {
//					javax.swing.JPopupMenu popupMenu = (javax.swing.JPopupMenu)parent;
//					popupMenu.addSeparator();
//				}
//			}
//		}
//	}
	
	
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
