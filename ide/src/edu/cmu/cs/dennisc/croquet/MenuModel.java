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
public class MenuModel extends Model {
	public static final Model SEPARATOR = null;
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
			context.addChild( new MenuSelectedEvent( context, MenuModel.this, e, this.menu ) );
		}
		public void menuDeselected( javax.swing.event.MenuEvent e ) {
			Application application = Application.getSingleton();
			Context context = application.getCurrentContext();
			context.addChild( new MenuDeselectedEvent( context, MenuModel.this, e, this.menu ) );
		}
		public void menuCanceled( javax.swing.event.MenuEvent e ) {
			Application application = Application.getSingleton();
			Context context = application.getCurrentContext();
			context.addChild( new MenuCanceledEvent( context, MenuModel.this, e, this.menu ) );
		}
	};
	private java.util.Map< Menu, MenuListener > mapMenuToListener = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private String text;
	private int mnemonic;
	private Model[] models;
	public MenuModel( Group group, java.util.UUID individualId, String text, int mnemonic, Model... models ) {
		super( group, individualId );
		this.text = text;
		this.mnemonic = mnemonic;
		this.models = models;
	}
	public MenuModel( Group group, java.util.UUID individualId, String text, int mnemonic, java.util.List< Model > models ) {
		this( group, individualId, text, mnemonic, edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray(models, Model.class) );
	}

	public Model[] getOperations() {
		return this.models;
	}
	
	public Menu createMenu() {
		Application.getSingleton().register( this );
		Menu rv = new Menu() {
			@Override
			protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleAddedTo( parent );
				this.setText( MenuModel.this.text );
				this.setMnemonic( MenuModel.this.mnemonic );
				assert mapMenuToListener.containsKey( this ) == false;
				MenuListener menuListener = new MenuListener( this );
				MenuModel.this.mapMenuToListener.put( this, menuListener );
				this.getAwtComponent().addMenuListener( menuListener );
				this.getAwtComponent().addActionListener( new java.awt.event.ActionListener() {
					public void actionPerformed( java.awt.event.ActionEvent e ) {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "Menu actionPerformed", e );
					}
				} );
			}

			@Override
			protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				MenuModel.this.removeComponent( this );
				MenuListener menuListener = MenuModel.this.mapMenuToListener.get( this );
				assert menuListener != null;
				this.getAwtComponent().removeMenuListener( menuListener );
				MenuModel.this.mapMenuToListener.remove( this );
				this.setMnemonic( 0 );
				this.setText( null );
				super.handleRemovedFrom( parent );
			}
		};
		Application.addMenuElements( rv, this.getOperations() );
		return rv;
	}
	
}
