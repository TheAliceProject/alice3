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
public abstract class FillIn< E > extends Node {
	private edu.cmu.cs.dennisc.croquet.Model model = null;
	private javax.swing.JComponent menuProxy = null;
	private boolean wasLast = false; 
	
	public FillIn() {
	}
	public FillIn( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	
	@Override
	protected void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		//todo?
	}
	@Override
	protected void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		//todo?
	}

	public StringBuilder appendTutorialNoteText( StringBuilder rv, java.util.Locale locale ) {
		return rv;
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
			Blank rootBlank = this.getRootBlank();
			CascadingRoot cascadingRoot = rootBlank.getCascadingRoot();
			this.model = cascadingRoot.createCroquetModel( this, isLast );
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
	@Override
	protected void addPrefixChildren() {
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

	protected void handleMenuSelected( edu.cmu.cs.dennisc.croquet.MenuItemContainer menuItemContainer, java.util.EventObject e ) {
		this.select();
		Blank blank = this.getNextNode();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo handleMenuSelected" );
		edu.cmu.cs.dennisc.croquet.MenuItemContainerUtilities.addMenuElements( menuItemContainer, blank.getModels() );
//		Node.this.addNextNodeMenuItems( Node.this.model, menu );
////		javax.swing.SwingUtilities.invokeLater( new Runnable() {
////			public void run() {
////				Node.this.menuItem.revalidate();
////				Node.this.menuItem.repaint();
////			}
////		} );
	}
	protected void handleMenuDeselected( edu.cmu.cs.dennisc.croquet.MenuItemContainer menuItemContainer, java.util.EventObject e ) {
		this.deselect();
		menuItemContainer.forgetAndRemoveAllMenuItems();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo handleMenuDeselected" );
	}
	protected void handleActionOperationPerformed( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		this.select();
//		this.getRootBlank().handleActionPerformed( context );
	}
//	@Override
//	protected void handleMenuSelected( javax.swing.event.MenuEvent e, edu.cmu.cs.dennisc.croquet.Menu< edu.cmu.cs.dennisc.croquet.MenuModel > menu ) {
//		this.select();
//		super.handleMenuSelected( e, menu );
//	}
//
//	@Override
//	protected void handleMenuDeselected( javax.swing.event.MenuEvent e, edu.cmu.cs.dennisc.croquet.Menu< edu.cmu.cs.dennisc.croquet.MenuModel > menu ) {
//		this.deselect();
//		super.handleMenuDeselected( e, menu );
//	}
//	
//	@Override
//	protected void handleActionOperationPerformed() {
//		this.select();
//		super.handleActionOperationPerformed();
//	}
	

	@Override
	protected void cleanUp() {
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

	public void addBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		super.addChild( blank );
	}

	@Override
	protected boolean isLast() {
		return this.getNextNode() == null;
	}
	
	public Blank getBlankAt( int index ) {
		return (Blank)getChildren().get( index );
	}
	public Blank getParentBlank() {
		return (Blank)getParent();
	}
	
	@Override
	protected Blank getNextNode() {
		java.util.List< Node > children = this.getChildren();
		if( children.size() > 0 ) {
			return getBlankAt( 0 );
		} else {
			return this.getNextBlank();
		}
	}
	public void select() {
		getNearestBlank().setSelectedFillIn( this );
	}
	public void deselect() {
		//todo?
		//getNearestBlank().setSelectedFillIn( null );
	}
	
	public abstract E getTransientValue();
	public abstract E getValue();
	
	
	
//	@Deprecated
//	public void showPopupMenu( java.awt.Component invoker, int x, int y, edu.cmu.cs.dennisc.task.TaskObserver< ? extends Object > taskObserver ) {
//		class DefaultRootBlank extends Blank {
//			@Override
//			protected void addChildren() {
//				this.addFillIn( FillIn.this );
//			}
//			
//			@Override
//			protected java.util.List< edu.cmu.cs.dennisc.croquet.Model > getModels() {
//				FillIn.this.setParent( this );
//				Blank blank0 = (Blank)FillIn.this.getChildren().get( 0 );
//				return blank0.getModels();
//			}
////			@Override
////			protected void addNextNodeMenuItems( javax.swing.JComponent component ) {
////				FillIn.this.setParent( this );
////				FillIn.this.getChildren().get( 0 ).addNextNodeMenuItems( component );
////			}
//		}
//		if( this.getChildren().size() > 0 ) {
//			new DefaultRootBlank().showPopupMenu( invoker, x, y, taskObserver );
//		} else {
//			throw new RuntimeException();
//		}
//	}
}
