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

/**
 * @author Dennis Cosgrove
 */
public abstract class MenuModel extends PrepModel {
	private static final Group MENU_GROUP = Group.getInstance( java.util.UUID.fromString( "4ed42b1f-b4ea-4f70-99d1-5bb2c3f11081" ), "MENU_GROUP" );
	public static final Model SEPARATOR = null;
	private Class<?> clsForI18N;
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
		}
	};
	public MenuModel( java.util.UUID individualId, Class<?> clsForI18N ) {
		super( MENU_GROUP, individualId );
		this.clsForI18N = clsForI18N;
	}
	public MenuModel( java.util.UUID individualId ) {
		this( individualId, null );
	}
	@Override
	public Iterable< ? extends Model > getChildren() {
		return null;
	}
	
	@Override
	protected void localize() {
		if( clsForI18N != null ) {
			//pass
		} else {
			clsForI18N = this.getClass();
		}
		this.setName( getDefaultLocalizedText( clsForI18N ) );
		this.action.putValue( javax.swing.Action.MNEMONIC_KEY, getLocalizedMnemonicKey( clsForI18N ) );
		this.action.putValue( javax.swing.Action.ACCELERATOR_KEY, getLocalizedAcceleratorKeyStroke( clsForI18N ) );
	}
	@Override
	public String getTutorialNoteText( ModelContext< ? > modelContext, Edit< ? > edit, UserInformation userInformation ) {
		return this.getName();
	}
	private String getName() {
		return (String)this.action.getValue( javax.swing.Action.NAME );
	}
	public void setName( String name ) {
		this.action.putValue( javax.swing.Action.NAME, name );
	}
	private javax.swing.Icon getSmallIcon() {
		return (javax.swing.Icon)this.action.getValue( javax.swing.Action.SMALL_ICON );
	}
	public void setSmallIcon( javax.swing.Icon icon ) {
		this.action.putValue( javax.swing.Action.SMALL_ICON, icon );
	}
	
	private StandardPopupOperation popupMenuOperation;
	public synchronized StandardPopupOperation getPopupMenuOperation() {
		if( this.popupMenuOperation != null ) {
			//pass
		} else {
			this.popupMenuOperation = new StandardPopupOperation( this );
		}
		return this.popupMenuOperation;
	}
	
	
	protected void handlePopupMenuPrologue( PopupMenu popupMenu, StandardPopupOperationContext context ) {
	}
	protected void handlePopupMenuEpilogue( PopupMenu popupMenu, StandardPopupOperationContext context ) {
	}
	
//	private javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
//		public void stateChanged( javax.swing.event.ChangeEvent e ) {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "stateChanged", javax.swing.MenuSelectionManager.defaultManager().getSelectedPath() );
//		}
//	};
	protected void handleShowing( MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
////		//menuItemContainer.addChangeListener( this.changeListener );
//		Container< ? > parent = menuItemContainer.getParent();
////		ModelContext< ? > parentContext;
////		if( parent instanceof MenuBar ) {
////			MenuBar menuBar = (MenuBar)parent;
////			parentContext = menuBar.createMenuBarContext();
////		} else {
////			parentContext = RootContext.getInstance().getCurrentContext();
////		}
//		MenuModelContext context = RootContext.createAndPushMenuModelContext( MenuModel.this, menuItemContainer );
//		context.handleMenuSelected( e );
////		
////		javax.swing.MenuSelectionManager menuSelectionManager = javax.swing.MenuSelectionManager.defaultManager();
////		menuSelectionManager.addChangeListener( this.changeListener );
	}
	protected void handleHiding( MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
//		MenuModelContext context = (MenuModelContext)RootContext.getCurrentContext();
//		context.handleMenuDeselected( e );
//		ModelContext< ? > modelContext = RootContext.popContext();
//		assert modelContext instanceof MenuModelContext;
//		javax.swing.MenuSelectionManager menuSelectionManager = javax.swing.MenuSelectionManager.defaultManager();
//		menuSelectionManager.removeChangeListener( this.changeListener );
	}
	protected void handleCanceled( MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
////		MenuModelContext context = (MenuModelContext)RootContext.getCurrentContext();
////		context.handleMenuCanceled( e );
//		System.err.println( "todo: cancel" + " " + e );
	}
	
	@Override
	public boolean isAlreadyInState( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		return false;
	}
	
	private class PopupMenuListener implements javax.swing.event.PopupMenuListener {
		private MenuItemContainer menuItemContainer;
		public PopupMenuListener( MenuItemContainer menuItemContainer ) {
			this.menuItemContainer = menuItemContainer;
		}
		public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
			MenuModel.this.handleShowing( this.menuItemContainer, e );
		}
		public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
			MenuModel.this.handleHiding( this.menuItemContainer, e );
		}
		public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
			MenuModel.this.handleCanceled( this.menuItemContainer, e );
		}
	}

	private PopupMenuListener popupMenuListener;
	/*package-private*/ final void addPopupMenuListener( MenuItemContainer menuItemContainer ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "addPopupMenuListener", menuItemContainer );
		assert this.popupMenuListener == null;
		this.popupMenuListener = new PopupMenuListener( menuItemContainer );
		menuItemContainer.addPopupMenuListener( this.popupMenuListener );
	}
	/*package-private*/ final void removePopupMenuListener( MenuItemContainer menuItemContainer ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "removePopupMenuListener", menuItemContainer );
		assert this.popupMenuListener != null;
		menuItemContainer.removePopupMenuListener( this.popupMenuListener );
		this.popupMenuListener = null;
	}
	
	
	/*package-private*/ Menu createMenu() {
		Menu rv = new Menu( this ) {
			@Override
			protected void handleDisplayable() {
				super.handleDisplayable();
				MenuModel.this.addPopupMenuListener( this );
				MenuModel.this.addComponent( this );
			}

			@Override
			protected void handleUndisplayable() {
				MenuModel.this.removeComponent( this );
				MenuModel.this.removePopupMenuListener( this );
				super.handleUndisplayable();
			}
			
			@Override
			protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
				this.getAwtComponent().setAction( MenuModel.this.action );
				super.handleAddedTo( parent );
			}
			@Override
			protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
				super.handleRemovedFrom( parent );
				this.getAwtComponent().setAction( null );
			}
		};
		return rv;
	};
}
