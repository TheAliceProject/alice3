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

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class MenuItemContainerUtilities {
	private MenuItemContainerUtilities() {
		throw new AssertionError();
	}

	//	public static java.util.List< Model > getMenuPath( MenuItem menuItem ) {
	//		java.util.List< Model > rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
	//		rv.add( menuItem.getModel() );
	//
	//		Container< ? > parent = menuItem.getParent();
	//		while( parent != null ) {
	//			if( parent instanceof MenuItemContainer ) {
	//				MenuItemContainer menuItemContainer = (MenuItemContainer)parent;
	//				rv.add( menuItemContainer.getViewController().getModel() );
	//			} else {
	//				break;
	//			}
	//			parent = parent.getParent();
	//		}
	//		return rv;
	//	}

	public static interface MenuElementObserver {
		public void update( MenuItemContainer menuItemContainer, org.lgna.croquet.StandardMenuItemPrepModel model, ViewController<?, ?> menuElement );
	}

	private static MenuItemContainer addMenuElement( MenuItemContainer menuItemContainer, org.lgna.croquet.StandardMenuItemPrepModel model, MenuElementObserver observer ) {
		ViewController<?, ?> menuElement;
		if( model != null ) {
			menuElement = model.createMenuItemAndAddTo( menuItemContainer );
		} else {
			menuItemContainer.addSeparator();
			menuElement = null;
		}

		if( observer != null ) {
			observer.update( menuItemContainer, model, menuElement );
		}
		return menuItemContainer;
	}

	public static void setMenuElements( MenuItemContainer menuItemContainer, java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> models, MenuElementObserver observer ) {
		final boolean IS_REUSE_READY_FOR_PRIME_TIME = false;
		if( IS_REUSE_READY_FOR_PRIME_TIME ) {
			java.util.Set<AwtComponentView<?>> set = edu.cmu.cs.dennisc.java.util.Sets.newHashSet( menuItemContainer.getMenuComponents() );
			for( AwtComponentView<?> component : set ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( component );
			}
		} else {
			menuItemContainer.forgetAndRemoveAllMenuItems();
			for( org.lgna.croquet.StandardMenuItemPrepModel model : models ) {
				addMenuElement( menuItemContainer, model, observer );
			}
		}
	}

	public static void setMenuElements( MenuItemContainer menuItemContainer, org.lgna.croquet.StandardMenuItemPrepModel[] models, MenuElementObserver observer ) {
		setMenuElements( menuItemContainer, java.util.Arrays.asList( models ), observer );
	}

	public static void setMenuElements( MenuItemContainer menuItemContainer, java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> models ) {
		setMenuElements( menuItemContainer, models, null );
	}

	public static void setMenuElements( MenuItemContainer menuItemContainer, org.lgna.croquet.StandardMenuItemPrepModel[] models ) {
		setMenuElements( menuItemContainer, models, null );
	}
}
