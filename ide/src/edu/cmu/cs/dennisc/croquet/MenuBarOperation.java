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
public class MenuBarOperation extends Operation {
//	private class MenuBarChangeListener implements javax.swing.event.ChangeListener {
//		private KMenuBar menuBar;
//		public MenuBarChangeListener( KMenuBar menuBar ) {
//			this.menuBar = menuBar;
//		}
//		public void stateChanged( javax.swing.event.ChangeEvent e ) {
//			Application application = Application.getSingleton();
//			Context parentContext = application.getCurrentContext();
//			Context childContext = parentContext.open();
//			javax.swing.SingleSelectionModel singleSelectionModel = (javax.swing.SingleSelectionModel)e.getSource();
//			int index = singleSelectionModel.getSelectedIndex();
//			MenuOperation menuOperationAtIndex = MenuBarOperation.this.menuOperations.get( index );
//			childContext.addChild( new MenuBarStateChangeEvent( childContext, MenuBarOperation.this, e, this.menuBar, singleSelectionModel, index, menuOperationAtIndex ) );
//		}
//	};
//	private java.util.Map< KMenuBar, MenuBarChangeListener > mapMenuBarToListener = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private java.util.concurrent.CopyOnWriteArrayList< MenuOperation > menuOperations = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public MenuBarOperation( Group group, java.util.UUID individualUUID ) {
		super( group, individualUUID );
	}

	public void addMenuOperation( MenuOperation menuOperation ) {
		this.menuOperations.add( menuOperation );
	}
	public void removeMenuOperation( MenuOperation menuOperation ) {
		this.menuOperations.remove( menuOperation );
	}
	public java.util.concurrent.CopyOnWriteArrayList< MenuOperation > getMenuOperations() {
		return this.menuOperations;
	}
	public MenuBar createMenuBar() {
		Application.getSingleton().register( this );
		MenuBar rv = new MenuBar() {
			@Override
			protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleAddedTo( parent );
//				assert mapMenuBarToListener.containsKey( menuBar ) == false;
//				MenuBarChangeListener listener = new MenuBarChangeListener( menuBar );
//				this.mapMenuBarToListener.put( menuBar, listener );
//				menuBar.getJComponent().getSelectionModel().addChangeListener( listener );
				MenuBarOperation.this.addComponent(this);
			}

			@Override
			protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleRemovedFrom( parent );
				MenuBarOperation.this.removeComponent(this);
//				MenuBarChangeListener listener = this.mapMenuBarToListener.get( menuBar );
//				assert listener != null;
//				menuBar.getJComponent().getSelectionModel().removeChangeListener( listener );
//				this.mapMenuBarToListener.remove( menuBar );
			}
		};
		for( MenuOperation menuOperation : this.getMenuOperations() ) {
			rv.addMenu( menuOperation.createMenu() );
		}
		return rv;
	}
	
}
