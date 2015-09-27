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
public abstract class AbstractMenu<M extends org.lgna.croquet.PrepModel> extends ViewController<javax.swing.JMenu, M> implements MenuItemContainer {
	private boolean isIconSet;
	private javax.swing.Icon setIcon;

	public AbstractMenu( M model ) {
		super( model );
	}

	protected javax.swing.Icon getSetIcon() {
		return this.setIcon;
	}

	public boolean isIconSet() {
		return this.isIconSet;
	}

	public void setIconSet( boolean isIconSet ) {
		this.isIconSet = isIconSet;
	}

	public javax.swing.Icon getIcon() {
		return this.getAwtComponent().getIcon();
	}

	public void setIcon( javax.swing.Icon icon ) {
		this.setIconSet( true );
		this.setIcon = icon;
	}

	protected abstract boolean areIconsDisplayedInMenus();

	@Override
	protected javax.swing.JMenu createAwtComponent() {
		javax.swing.JMenu rv = new javax.swing.JMenu() {
			@Override
			public javax.swing.Icon getIcon() {
				if( AbstractMenu.this.areIconsDisplayedInMenus() ) {
					if( AbstractMenu.this.isIconSet() ) {
						return AbstractMenu.this.getSetIcon();
					} else {
						return super.getIcon();
					}
				} else {
					return null;
				}
			}
		};
		org.lgna.croquet.views.imp.ScrollingPopupMenuUtilities.initializeScrollingCapability( rv.getPopupMenu() );
		return rv;
	}

	@Override
	public AwtComponentView<?> getMenuComponent( int i ) {
		return AwtComponentView.lookup( this.getAwtComponent().getMenuComponent( i ) );
	}

	@Override
	public int getMenuComponentCount() {
		return this.getAwtComponent().getMenuComponentCount();
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
	public final org.lgna.croquet.views.ViewController<?, ?> getViewController() {
		return this;
	}

	@Override
	public void addPopupMenuListener( javax.swing.event.PopupMenuListener listener ) {
		this.getAwtComponent().getPopupMenu().addPopupMenuListener( listener );
	}

	@Override
	public void removePopupMenuListener( javax.swing.event.PopupMenuListener listener ) {
		this.getAwtComponent().getPopupMenu().removePopupMenuListener( listener );
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
	public void addCascadeCombo( org.lgna.croquet.views.CascadeMenuItem cascadeMenuItem, org.lgna.croquet.views.CascadeMenu cascadeMenu ) {
		this.checkEventDispatchThread();
		this.addCascadeMenuItem( cascadeMenuItem );
		org.lgna.croquet.views.imp.ScrollingPopupMenuUtilities.addSideMenu( this.getAwtComponent().getPopupMenu(), cascadeMenu.getAwtComponent() );
	}

	@Override
	public void addCascadeMenuItem( CascadeMenuItem cascadeMenuItem ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().add( cascadeMenuItem.getAwtComponent() );
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
		org.lgna.croquet.views.imp.ScrollingPopupMenuUtilities.removeAllNonScrollComponents( this.getAwtComponent().getPopupMenu() );
	}

	@Override
	public void forgetAndRemoveAllMenuItems() {
		this.checkEventDispatchThread();
		//this.internalForgetAndRemoveAllComponents();
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "forget" );
		org.lgna.croquet.views.imp.ScrollingPopupMenuUtilities.removeAllNonScrollComponents( this.getAwtComponent().getPopupMenu() );
	}
}
