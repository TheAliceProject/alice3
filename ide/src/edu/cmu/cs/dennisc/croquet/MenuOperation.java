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
public class MenuOperation extends Operation {
	public static final Operation SEPARATOR = null;
	private class MenuListener implements javax.swing.event.MenuListener {
		private Menu menu;
		public MenuListener( Menu menu ) {
			this.menu = menu;
		}
		public void menuSelected( javax.swing.event.MenuEvent e ) {
			Application application = Application.getSingleton();
			Context context;
			Component< ? > parent = this.menu.getParent();
			if( parent instanceof MenuBar ) {
				Context rootContext = application.getRootContext();
				context = rootContext.createChildContext();
			} else {
				context = application.getCurrentContext();
			}
			context.addChild( new MenuSelectedEvent( context, MenuOperation.this, e, this.menu ) );
		}
		public void menuDeselected( javax.swing.event.MenuEvent e ) {
			Application application = Application.getSingleton();
			Context context = application.getCurrentContext();
			context.addChild( new MenuDeselectedEvent( context, MenuOperation.this, e, this.menu ) );
		}
		public void menuCanceled( javax.swing.event.MenuEvent e ) {
			Application application = Application.getSingleton();
			Context context = application.getCurrentContext();
			context.addChild( new MenuCanceledEvent( context, MenuOperation.this, e, this.menu ) );
		}
	};
	private java.util.Map< Menu, MenuListener > mapMenuToListener = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private String text;
	private int mnemonic;
	private Operation[] operations;
	public MenuOperation( java.util.UUID groupUUID, java.util.UUID individualId, String text, int mnemonic, Operation... operations ) {
		super( groupUUID, individualId );
		this.text = text;
		this.mnemonic = mnemonic;
		this.operations = operations;
	}
	public MenuOperation( java.util.UUID groupUUID, java.util.UUID individualId, String text, int mnemonic, java.util.List< Operation > operations ) {
		this( groupUUID, individualId, text, mnemonic, edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray(operations, Operation.class) );
	}

	public Operation[] getOperations() {
		return this.operations;
	}
	private void addMenu( Menu menu ) {
		menu.setText( this.text );
		menu.setMnemonic( this.mnemonic );
		assert mapMenuToListener.containsKey( menu ) == false;
		MenuListener menuListener = new MenuListener( menu );
		this.mapMenuToListener.put( menu, menuListener );
		menu.getJComponent().addMenuListener( menuListener );
		menu.getJComponent().addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "actionPerformed", e );
			}
		} );
		this.addComponent( menu );
	}
	private void removeMenu( Menu menu ) {
		this.removeComponent( menu );
		MenuListener menuListener = this.mapMenuToListener.get( menu );
		assert menuListener != null;
		menu.getJComponent().removeMenuListener( menuListener );
		this.mapMenuToListener.remove( menu );
		menu.setMnemonic( 0 );
		menu.setText( null );
	}
	
	public Menu createMenu() {
		Application.getSingleton().register( this );
		Menu rv = new Menu() {
			@Override
			protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleAddedTo( parent );
				MenuOperation.this.addMenu(this);
			}

			@Override
			protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				MenuOperation.this.removeMenu(this);
				super.handleRemovedFrom( parent );
			}
		};
		Application.addMenuElements( rv, this.getOperations() );
		return rv;
	}
	
}
