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

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.PopupPrepModel;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.views.imp.ScrollingPopupMenuUtilities;

import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.event.PopupMenuListener;
import java.awt.Component;

/**
 * @author Dennis Cosgrove
 */
public class PopupMenu extends ViewController<JPopupMenu, PopupPrepModel> implements MenuItemContainer {
	private final UserActivity userActivity;

	public PopupMenu( PopupPrepModel model, UserActivity userActivity ) {
		super( model );
		this.userActivity = userActivity;
	}

	@Override
	public ViewController<?, ?> getViewController() {
		return this;
	}

	//	public void addChangeListener( javax.swing.event.ChangeListener changeListener ) {
	//		this.getAwtComponent().getSelectionModel().addChangeListener( changeListener );
	//	}
	//	public void removeChangeListener( javax.swing.event.ChangeListener changeListener ) {
	//		this.getAwtComponent().getSelectionModel().removeChangeListener( changeListener );
	//	}

	@Override
	public void addPopupMenuListener( PopupMenuListener listener ) {
		this.getAwtComponent().addPopupMenuListener( listener );
	}

	@Override
	public void removePopupMenuListener( PopupMenuListener listener ) {
		this.getAwtComponent().removePopupMenuListener( listener );
	}

	@Override
	public UserActivity getActivity() {
		return userActivity;
	}

	@Override
	protected JPopupMenu createAwtComponent() {
		JPopupMenu rv = new JPopupMenu();
		ScrollingPopupMenuUtilities.initializeScrollingCapability( rv );
		return rv;
	}

	@Override
	public AwtComponentView<?> getMenuComponent( int i ) {
		MenuElement menuElement = this.getAwtComponent().getSubElements()[ i ];
		if( menuElement instanceof Component ) {
			Component awtComponent = (Component)menuElement;
			return AwtComponentView.lookup( awtComponent );
		} else {
			return null;
		}
	}

	@Override
	public int getMenuComponentCount() {
		return this.getAwtComponent().getSubElements().length;
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
	public void addMenu( Menu menu ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().add( menu.getAwtComponent() );
	}

	@Override
	public void addMenuItem( MenuItem menuItem ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().add( menuItem.getAwtComponent() );
	}

	@Override
	public void addCascadeMenu( CascadeMenu cascadeMenu ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().add( cascadeMenu.getAwtComponent() );
	}

	@Override
	public void addCascadeMenuItem( CascadeMenuItem cascadeMenuItem ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().add( cascadeMenuItem.getAwtComponent() );
	}

	@Override
	public void addCascadeCombo( CascadeMenuItem cascadeMenuItem, CascadeMenu cascadeMenu ) {
		this.checkEventDispatchThread();
		this.addCascadeMenuItem( cascadeMenuItem );
		ScrollingPopupMenuUtilities.addSideMenu( this.getAwtComponent(), cascadeMenu.getAwtComponent() );
	}

	@Override
	public void addCheckBoxMenuItem( CheckBoxMenuItem checkBoxMenuItem ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().add( checkBoxMenuItem.getAwtComponent() );
	}

	@Override
	public void addSeparator( MenuTextSeparator menuTextSeparator ) {
		this.checkEventDispatchThread();
		if( menuTextSeparator != null ) {
			this.getAwtComponent().add( menuTextSeparator.getAwtComponent() );
		} else {
			this.getAwtComponent().addSeparator();
		}
	}

	@Override
	public final void addSeparator() {
		this.addSeparator( null );
	}

	@Override
	public void removeAllMenuItems() {
		this.checkEventDispatchThread();
		//this.internalRemoveAllComponents();
		ScrollingPopupMenuUtilities.removeAllNonScrollComponents( this.getAwtComponent() );
	}

	@Override
	public void forgetAndRemoveAllMenuItems() {
		this.checkEventDispatchThread();
		//this.internalForgetAndRemoveAllComponents();
		Logger.todo( "forget" );
		ScrollingPopupMenuUtilities.removeAllNonScrollComponents( this.getAwtComponent() );
	}

	//	@Override
	//	protected void handleDisplayable() {
	//		super.handleDisplayable();
	//		this.getAwtComponent().getSelectionModel().addChangeListener( new javax.swing.event.ChangeListener() {
	//			public void stateChanged( javax.swing.event.ChangeEvent e ) {
	//				System.err.println( "stateChanged: " + e );
	//			}
	//		} );
	//	}

	public void showAtLocation( AwtComponentView<?> invoker, int x, int y ) {
		this.checkEventDispatchThread();
		Component awtInvoker;
		if( invoker != null ) {
			awtInvoker = invoker.getAwtComponent();
		} else {
			awtInvoker = null;
		}
		this.getAwtComponent().show( awtInvoker, x, y );
	}

	public final void showBelow( AwtComponentView<?> invoker ) {
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
