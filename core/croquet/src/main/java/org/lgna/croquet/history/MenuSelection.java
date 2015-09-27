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

/**
 * @author Dennis Cosgrove
 */
public class MenuSelection {
	private static boolean isCroquetMenuSelection( javax.swing.MenuElement[] menuElements ) {
		for( javax.swing.MenuElement menuElement : menuElements ) {
			org.lgna.croquet.views.AwtComponentView<?> component = org.lgna.croquet.views.AwtComponentView.lookup( menuElement.getComponent() );
			if( ( component instanceof org.lgna.croquet.views.MenuBar ) || ( component instanceof org.lgna.croquet.views.MenuItem ) || ( component instanceof org.lgna.croquet.views.Menu ) || ( component instanceof org.lgna.croquet.views.PopupMenu ) || ( component instanceof org.lgna.croquet.views.MenuTextSeparator ) ) {
				return true;
			}
		}
		return menuElements.length == 0;
	}

	private static javax.swing.JMenuBar getJMenuBarOrigin( javax.swing.MenuElement[] menuElements ) {
		if( menuElements.length > 0 ) {
			javax.swing.MenuElement menuElement0 = menuElements[ 0 ];
			if( menuElement0 instanceof javax.swing.JMenuBar ) {
				return (javax.swing.JMenuBar)menuElement0;
			}
		}
		return null;
	}

	private static org.lgna.croquet.views.MenuBar getMenuBarOrigin( javax.swing.MenuElement[] menuElements ) {
		javax.swing.JMenuBar jMenuBar = getJMenuBarOrigin( menuElements );
		if( jMenuBar != null ) {
			return (org.lgna.croquet.views.MenuBar)org.lgna.croquet.views.AwtComponentView.lookup( jMenuBar );
		} else {
			return null;
		}
	}

	private static org.lgna.croquet.MenuBarComposite getMenuBarComposite( javax.swing.MenuElement[] menuElements ) {
		org.lgna.croquet.views.MenuBar menuBar = getMenuBarOrigin( menuElements );
		if( menuBar != null ) {
			return menuBar.getComposite();
		} else {
			return null;
		}
	}

	private final org.lgna.croquet.triggers.ChangeEventTrigger trigger;
	private final org.lgna.croquet.MenuBarComposite menuBarComposite;
	private final org.lgna.croquet.MenuItemPrepModel[] menuItemPrepModels;

	public MenuSelection( org.lgna.croquet.triggers.ChangeEventTrigger trigger ) {
		this.trigger = trigger;
		javax.swing.MenuElement[] selectedPath = javax.swing.MenuSelectionManager.defaultManager().getSelectedPath();
		if( isCroquetMenuSelection( selectedPath ) ) {
			menuBarComposite = getMenuBarComposite( selectedPath );
			java.util.List<org.lgna.croquet.MenuItemPrepModel> list = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			int i0;
			if( menuBarComposite != null ) {
				i0 = 1;
			} else {
				i0 = 0;
			}
			final int N = selectedPath.length;
			for( int i = i0; i < N; i++ ) {
				javax.swing.MenuElement menuElementI = selectedPath[ i ];
				if( menuElementI instanceof javax.swing.JPopupMenu ) {
					javax.swing.JPopupMenu jPopupMenu = (javax.swing.JPopupMenu)menuElementI;
					//pass
				} else if( menuElementI instanceof javax.swing.JMenuItem ) {
					javax.swing.JMenuItem jMenuItem = (javax.swing.JMenuItem)menuElementI;
					org.lgna.croquet.views.AwtComponentView<?> component = org.lgna.croquet.views.AwtComponentView.lookup( jMenuItem );
					if( component instanceof org.lgna.croquet.views.ViewController<?, ?> ) {
						org.lgna.croquet.views.ViewController<?, ?> viewController = (org.lgna.croquet.views.ViewController<?, ?>)component;
						org.lgna.croquet.Model model = viewController.getModel();
						if( model != null ) {
							org.lgna.croquet.MenuItemPrepModel menuItemPrepModel;
							if( model instanceof org.lgna.croquet.MenuItemPrepModel ) {
								menuItemPrepModel = (org.lgna.croquet.MenuItemPrepModel)model;
							} else if( model instanceof org.lgna.croquet.Operation ) {
								menuItemPrepModel = ( (org.lgna.croquet.Operation)model ).getMenuItemPrepModel();
							} else if( model instanceof org.lgna.croquet.BooleanState ) {
								menuItemPrepModel = ( (org.lgna.croquet.BooleanState)model ).getMenuItemPrepModel();
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
			menuItemPrepModels = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( list, org.lgna.croquet.MenuItemPrepModel.class );
		} else {
			menuBarComposite = null;
			menuItemPrepModels = new org.lgna.croquet.MenuItemPrepModel[ 0 ];
		}
	}

	public org.lgna.croquet.triggers.ChangeEventTrigger getTrigger() {
		return this.trigger;
	}

	public org.lgna.croquet.MenuBarComposite getMenuBarComposite() {
		return this.menuBarComposite;
	}

	public org.lgna.croquet.MenuItemPrepModel[] getMenuItemPrepModels() {
		return this.menuItemPrepModels;
	}

	public boolean isValid() {
		return ( this.menuBarComposite != null ) || ( this.menuItemPrepModels.length > 0 );
	}

	public org.lgna.croquet.MenuItemPrepModel getLastMenuItemPrepModel() {
		if( this.menuItemPrepModels.length > 0 ) {
			return this.menuItemPrepModels[ this.menuItemPrepModels.length - 1 ];
		} else {
			return null;
		}
	}

	public boolean isPrevious( org.lgna.croquet.MenuBarComposite otherMenuBarComposite, org.lgna.croquet.MenuItemPrepModel[] otherMenuItemPrepModels ) {
		if( this.menuBarComposite == otherMenuBarComposite ) {
			if( this.menuItemPrepModels.length == ( otherMenuItemPrepModels.length + 1 ) ) {
				final int N = otherMenuItemPrepModels.length;
				for( int i = 0; i < N; i++ ) {
					if( this.menuItemPrepModels[ i ] == otherMenuItemPrepModels[ i ] ) {
						//pass
					} else {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
}
