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
package org.alice.ide.recentprojects;

import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.AwtContainerView;
import org.lgna.croquet.views.CascadeMenu;
import org.lgna.croquet.views.CascadeMenuItem;
import org.lgna.croquet.views.CheckBoxMenuItem;
import org.lgna.croquet.views.Menu;
import org.lgna.croquet.views.MenuItem;
import org.lgna.croquet.views.MenuTextSeparator;
import org.lgna.croquet.views.ViewController;

/**
 * @author Dennis Cosgrove
 */
public class RecentProjectsMenuModel extends org.lgna.croquet.MenuModel {
	private static class SingletonHolder {
		private static RecentProjectsMenuModel instance = new RecentProjectsMenuModel();
	}

	public static RecentProjectsMenuModel getInstance() {
		return SingletonHolder.instance;
	}

	private RecentProjectsMenuModel() {
		super( java.util.UUID.fromString( "0a39a07c-d23f-4cf8-a195-5d114b903505" ) );
	}

	private void setChildren( org.lgna.croquet.views.MenuItemContainer menuItemContainer ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		java.net.URI currentUri = ide.getUri();
		java.net.URI[] uris = RecentProjectsListData.getInstance().toArray();
		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> models = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( java.net.URI uri : uris ) {
			if( edu.cmu.cs.dennisc.java.util.Objects.equals( uri, currentUri ) ) {
				//pass
			} else {
				models.add( org.alice.ide.croquet.models.projecturi.OpenRecentProjectOperation.getInstance( uri ).getMenuItemPrepModel() );
			}
		}
		if( models.size() == 0 ) {
			models.add( NoRecentUrisSeparatorModel.getInstance() );
		}
		org.lgna.croquet.views.MenuItemContainerUtilities.setMenuElements( menuItemContainer, models );
	}

	//	@Override
	//	public org.lgna.croquet.components.Menu createMenu() {
	//		org.lgna.croquet.components.Menu rv = super.createMenu();
	//		this.addChildren( rv );
	//		edu.cmu.cs.dennisc.java.util.logging.Logger.testing( rv );
	//		return rv;
	//	}
	//	@Override
	//	public void handlePopupMenuPrologue( org.lgna.croquet.components.PopupMenu popupMenu, org.lgna.croquet.history.StandardPopupPrepStep step ) {
	//		super.handlePopupMenuPrologue( popupMenu, step );
	//		this.addChildren( popupMenu );
	//		edu.cmu.cs.dennisc.java.util.logging.Logger.testing( popupMenu );
	//	}
	@Override
	protected void handleShowing( org.lgna.croquet.views.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
		Object src = e.getSource();
		if( src instanceof javax.swing.JPopupMenu ) {
			final javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)e.getSource();
			this.setChildren( new org.lgna.croquet.views.MenuItemContainer() {
				@Override
				public ViewController<?, ?> getViewController() {
					return null;
				}

				@Override
				public void addPopupMenuListener( javax.swing.event.PopupMenuListener listener ) {
				}

				@Override
				public void removePopupMenuListener( javax.swing.event.PopupMenuListener listener ) {
				}

				@Override
				public AwtComponentView<?> getMenuComponent( int i ) {
					javax.swing.MenuElement menuElement = jPopupMenu.getSubElements()[ i ];
					if( menuElement instanceof java.awt.Component ) {
						java.awt.Component awtComponent = (java.awt.Component)menuElement;
						return AwtComponentView.lookup( awtComponent );
					} else {
						return null;
					}
				}

				@Override
				public int getMenuComponentCount() {
					return jPopupMenu.getSubElements().length;
				}

				@Override
				public synchronized AwtComponentView<?>[] getMenuComponents() {
					final int N = this.getMenuComponentCount();
					AwtComponentView<?>[] rv = new AwtComponentView<?>[ N ];
					for( int i = 0; i < N; i++ ) {
						rv[ i ] = this.getMenuComponent( i );
					}
					return rv;
				}

				@Override
				public AwtContainerView<?> getParent() {
					return null;
				}

				@Override
				public void addMenu( Menu menu ) {
					jPopupMenu.add( menu.getAwtComponent() );
				}

				@Override
				public void addMenuItem( MenuItem menuItem ) {
					jPopupMenu.add( menuItem.getAwtComponent() );
				}

				@Override
				public void addCascadeMenu( CascadeMenu cascadeMenu ) {
				}

				@Override
				public void addCascadeMenuItem( CascadeMenuItem cascadeMenuItem ) {
				}

				@Override
				public void addCascadeCombo( org.lgna.croquet.views.CascadeMenuItem cascadeMenuItem, org.lgna.croquet.views.CascadeMenu cascadeMenu ) {
					this.addCascadeMenuItem( cascadeMenuItem );
					this.addCascadeMenu( cascadeMenu );
				}

				@Override
				public void addCheckBoxMenuItem( CheckBoxMenuItem checkBoxMenuItem ) {
				}

				@Override
				public void addSeparator() {
					jPopupMenu.addSeparator();
				}

				@Override
				public void addSeparator( MenuTextSeparator menuTextSeparator ) {
					jPopupMenu.add( menuTextSeparator.getAwtComponent() );
				}

				@Override
				public void forgetAndRemoveAllMenuItems() {
					edu.cmu.cs.dennisc.java.util.logging.Logger.todo();
					this.removeAllMenuItems();
				}

				@Override
				public void removeAllMenuItems() {
					jPopupMenu.removeAll();
				}

			} );
		}
		super.handleShowing( menuItemContainer, e );
	}
}
