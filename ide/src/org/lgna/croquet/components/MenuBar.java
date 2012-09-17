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

/**
 * @author Dennis Cosgrove
 */
public class MenuBar extends View<javax.swing.JMenuBar, org.lgna.croquet.MenuBarComposite> implements MenuItemContainer {
	public MenuBar( org.lgna.croquet.MenuBarComposite composite ) {
		super( composite );
		for( org.lgna.croquet.StandardMenuItemPrepModel item : composite.getChildren() ) {
			item.createMenuItemAndAddTo( this );
		}
	}

	@Override
	protected javax.swing.JMenuBar createAwtComponent() {
		return new javax.swing.JMenuBar();
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		//		assert mapMenuBarToListener.containsKey( menuBar ) == false;
		//		MenuBarChangeListener listener = new MenuBarChangeListener( menuBar );
		//		this.mapMenuBarToListener.put( menuBar, listener );
		//		menuBar.getJComponent().getSelectionModel().addChangeListener( listener );
	}

	@Override
	protected void handleUndisplayable() {
		//		MenuBarChangeListener listener = this.mapMenuBarToListener.get( menuBar );
		//		assert listener != null;
		//		menuBar.getJComponent().getSelectionModel().removeChangeListener( listener );
		//		this.mapMenuBarToListener.remove( menuBar );
		super.handleUndisplayable();
	}

	public ViewController<?, ?> getViewController() {
		return null;
	}

	public void addPopupMenuListener( javax.swing.event.PopupMenuListener listener ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( listener );
	}

	public void removePopupMenuListener( javax.swing.event.PopupMenuListener listener ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( listener );
	}

	public void addMenu( Menu menu ) {
		this.getAwtComponent().add( menu.getAwtComponent() );
	}

	public void addMenuItem( MenuItem menuItem ) {
		//		edu.cmu.cs.dennisc.java.util.logging.Logger.testing( this.getAwtComponent().getLayout() );
		//		menuItem.setMaximumSizeClampedToPreferredSize( true );
		//		menuItem.setHorizontalAlignment( HorizontalAlignment.TRAILING );
		//		menuItem.setAlignmentX( 1.0f );
		this.getAwtComponent().add( menuItem.getAwtComponent() );
	}

	public void addCascadeMenu( CascadeMenu cascadeMenu ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( cascadeMenu );
	}

	public void addCascadeMenuItem( CascadeMenuItem cascadeMenuItem ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( cascadeMenuItem );
	}

	public void addCheckBoxMenuItem( CheckBoxMenuItem checkBoxMenuItem ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( checkBoxMenuItem );
	}

	public void addSeparator() {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo();
	}

	public void addSeparator( MenuTextSeparator menuTextSeparator ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( menuTextSeparator );
	}

	public void forgetAndRemoveAllMenuItems() {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo();
	}

	public void removeAllMenuItems() {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo();
	}

}
