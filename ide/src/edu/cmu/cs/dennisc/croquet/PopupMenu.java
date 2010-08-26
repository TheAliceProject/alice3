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
public class PopupMenu extends ViewController< javax.swing.JPopupMenu, PopupMenuOperation > implements MenuItemContainer {
	/*package-private*/ PopupMenu( PopupMenuOperation model ) {
		super( model );
	}
	public edu.cmu.cs.dennisc.croquet.ViewController< ?, ? > getViewController() {
		return this;
	}
	
//	public void addChangeListener( javax.swing.event.ChangeListener changeListener ) {
//		this.getAwtComponent().getSelectionModel().addChangeListener( changeListener );
//	}
//	public void removeChangeListener( javax.swing.event.ChangeListener changeListener ) {
//		this.getAwtComponent().getSelectionModel().removeChangeListener( changeListener );
//	}
	
	public void addPopupMenuListener(javax.swing.event.PopupMenuListener listener) {
		this.getAwtComponent().addPopupMenuListener( listener );
	}
	public void removePopupMenuListener(javax.swing.event.PopupMenuListener listener) {
		this.getAwtComponent().removePopupMenuListener( listener );
	}
	
	@Override
	protected javax.swing.JPopupMenu createAwtComponent() {
		javax.swing.JPopupMenu rv = new javax.swing.JPopupMenu();
		return rv;
	}

	public void addMenu( Menu menu ) {
		this.getAwtComponent().add( menu.getAwtComponent() );
	}
	public void addMenuItem( MenuItem menuItem ) {
		this.getAwtComponent().add( menuItem.getAwtComponent() );
	}
	public void addCheckBoxMenuItem( CheckBoxMenuItem checkBoxMenuItem ) {
		this.getAwtComponent().add( checkBoxMenuItem.getAwtComponent() );
	}
	public void addSeparator() {
		this.addSeparator( null );
	}
	public void addSeparator( MenuTextSeparator menuTextSeparator ) {
		if( menuTextSeparator != null ) {
			this.getAwtComponent().add( menuTextSeparator.getAwtComponent() );
		} else {
			this.getAwtComponent().addSeparator();
		}
	}
	
	@Override
	protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
		super.handleAddedTo( parent );
		this.getAwtComponent().getSelectionModel().addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				System.err.println( e );
			}
		} );
	}
	
	public void removeAllMenuItems() {
		//this.internalRemoveAllComponents();
		this.getAwtComponent().removeAll();
	}
	public void forgetAndRemoveAllMenuItems() {
		//this.internalForgetAndRemoveAllComponents();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "forgetAndRemoveAllMenuItems" );
		this.getAwtComponent().removeAll();
	}
	
	public void showAtLocation( Component< ? > invoker, int x, int y ) {
		java.awt.Component awtInvoker;
		if( invoker != null ) {
			awtInvoker = invoker.getAwtComponent();
		} else {
			awtInvoker = null;
		}
		this.getAwtComponent().show( awtInvoker, x, y );
	}
	public void showBelow( Component< ? > invoker ) {
		int x;
		int y;
		if( invoker != null ) {
			x = 0;
			y = invoker.getHeight();
		} else {
			x = 0;
			y = 0;
		}
		this.showAtLocation( invoker, x, y );
	}
}
