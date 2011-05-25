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

package org.lgna.croquet.components;

import edu.cmu.cs.dennisc.croquet.MenuItemPrepModel;

/**
 * @author Dennis Cosgrove
 */
public class MenuItemContainerUtilities {
	private MenuItemContainerUtilities() {
		throw new AssertionError();
	}
	
//	public static java.util.List< Model > getMenuPath( MenuItem menuItem ) {
//		java.util.List< Model > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
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
	
	public static MenuItemContainer addMenuElement( MenuItemContainer rv, MenuItemPrepModel model ) {
		if( model != null ) {
			model.createMenuItemAndAddTo( rv );
//			if( model instanceof MenuModel ) {
//				MenuModel menuOperation = (MenuModel)model;
//				rv.addMenu( menuOperation.createMenu() );
//			} else if( model instanceof ListSelectionState< ? > ) {
//				ListSelectionState< ? > itemSelectionOperation = (ListSelectionState< ? >)model;
//				rv.addMenu( itemSelectionOperation.getMenuModel().createMenu() );
//			} else if( model instanceof MenuSeparatorModel ) {
//				MenuSeparatorModel menuSeparatorModel = (MenuSeparatorModel)model;
//				rv.addSeparator( menuSeparatorModel.createMenuTextSeparator() );
//			} else if( model instanceof Operation<?> ) {
//				Operation<?> operation = (Operation<?>)model;
//				rv.addMenuItem( operation.createMenuItem() );
//			} else if( model instanceof BooleanState ) {
//				BooleanState booleanState = (BooleanState)model;
//				rv.addCheckBoxMenuItem( booleanState.createCheckBoxMenuItem() );
//			} else {
//				throw new RuntimeException();
//			}
		} else {
			rv.addSeparator();
		}
		return rv;
	}
	public static MenuItemContainer addMenuElements( MenuItemContainer rv, java.util.List<MenuItemPrepModel> models ) {
		for( MenuItemPrepModel model : models ) {
			addMenuElement( rv, model );
		}
		return rv;
	}
	public static MenuItemContainer addMenuElements( MenuItemContainer rv, MenuItemPrepModel[] models ) {
		for( MenuItemPrepModel model : models ) {
			addMenuElement( rv, model );
		}
		return rv;
	}
}
