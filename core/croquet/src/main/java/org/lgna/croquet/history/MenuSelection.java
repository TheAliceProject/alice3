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

package org.lgna.croquet.history;

import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.MenuBarComposite;
import org.lgna.croquet.MenuItemPrepModel;
import org.lgna.croquet.Model;
import org.lgna.croquet.Operation;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.Menu;
import org.lgna.croquet.views.MenuBar;
import org.lgna.croquet.views.MenuItem;
import org.lgna.croquet.views.MenuTextSeparator;
import org.lgna.croquet.views.PopupMenu;
import org.lgna.croquet.views.ViewController;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class MenuSelection {
	private static boolean isCroquetMenuSelection( MenuElement[] menuElements ) {
		for( MenuElement menuElement : menuElements ) {
			AwtComponentView<?> component = AwtComponentView.lookup( menuElement.getComponent() );
			if( ( component instanceof MenuBar ) || ( component instanceof MenuItem ) || ( component instanceof Menu ) || ( component instanceof PopupMenu ) || ( component instanceof MenuTextSeparator ) ) {
				return true;
			}
		}
		return menuElements.length == 0;
	}

	private static JMenuBar getJMenuBarOrigin( MenuElement[] menuElements ) {
		if( menuElements.length > 0 ) {
			MenuElement menuElement0 = menuElements[ 0 ];
			if( menuElement0 instanceof JMenuBar ) {
				return (JMenuBar)menuElement0;
			}
		}
		return null;
	}

	private static MenuBar getMenuBarOrigin( MenuElement[] menuElements ) {
		JMenuBar jMenuBar = getJMenuBarOrigin( menuElements );
		if( jMenuBar != null ) {
			return (MenuBar)AwtComponentView.lookup( jMenuBar );
		} else {
			return null;
		}
	}

	private static MenuBarComposite getMenuBarComposite( MenuElement[] menuElements ) {
		MenuBar menuBar = getMenuBarOrigin( menuElements );
		if( menuBar != null ) {
			return menuBar.getComposite();
		} else {
			return null;
		}
	}

	private final MenuBarComposite menuBarComposite;
	private final MenuItemPrepModel[] menuItemPrepModels;

	public MenuSelection() {
		MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
		if( isCroquetMenuSelection( selectedPath ) ) {
			menuBarComposite = getMenuBarComposite( selectedPath );
			List<MenuItemPrepModel> list = Lists.newLinkedList();
			int i0;
			if( menuBarComposite != null ) {
				i0 = 1;
			} else {
				i0 = 0;
			}
			final int N = selectedPath.length;
			for( int i = i0; i < N; i++ ) {
				MenuElement menuElementI = selectedPath[ i ];
				if ( menuElementI instanceof JMenuItem ) {
					JMenuItem jMenuItem = (JMenuItem) menuElementI;
					AwtComponentView<?> component = AwtComponentView.lookup( jMenuItem );
					if ( component instanceof ViewController<?, ?> ) {
						ViewController<?, ?> viewController = (ViewController<?, ?>) component;
						Model model = viewController.getModel();
						if ( model != null ) {
							MenuItemPrepModel menuItemPrepModel;
							if ( model instanceof MenuItemPrepModel ) {
								menuItemPrepModel = (MenuItemPrepModel) model;
							} else if ( model instanceof Operation ) {
								menuItemPrepModel = ( (Operation) model ).getMenuItemPrepModel();
							} else if ( model instanceof BooleanState ) {
								menuItemPrepModel = ( (BooleanState) model ).getMenuItemPrepModel();
							} else {
								throw new RuntimeException( model.toString() );
							}
							list.add( menuItemPrepModel );
						} else {
							throw new NullPointerException();
						}
					}
				}
			}
			menuItemPrepModels = ArrayUtilities.createArray( list, MenuItemPrepModel.class );
		} else {
			menuBarComposite = null;
			menuItemPrepModels = new MenuItemPrepModel[ 0 ];
		}
	}

	public boolean isValid() {
		return ( this.menuBarComposite != null ) || ( this.menuItemPrepModels.length > 0 );
	}

	MenuItemPrepModel getLastMenuItemPrepModel() {
		if( this.menuItemPrepModels.length > 0 ) {
			return this.menuItemPrepModels[ this.menuItemPrepModels.length - 1 ];
		} else {
			return null;
		}
	}

	boolean isPrevious( MenuSelection selection ) {
		if( this.menuBarComposite == selection.menuBarComposite ) {
			if( this.menuItemPrepModels.length == ( selection.menuItemPrepModels.length + 1 ) ) {
				final int N = selection.menuItemPrepModels.length;
				for( int i = 0; i < N; i++ ) {
					if ( this.menuItemPrepModels[i] != selection.menuItemPrepModels[i] ) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
}
